package fr.ign.cogit.simplu3d.generator.boundary;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.equation.LineEquation;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;

public class Method2BoundaryAnalyzer extends AbstractBoundaryAnalyzer {

	public Method2BoundaryAnalyzer() {
		super();
	}

	@Override
	public void analyze(Face f) {
		// //////////////////////////////////////
		// / Détermination des arcs donnant sur la voirie
		// //////////////////////////////////////

		// On parcourt les arcs (futures limites de parcelles)
		List<Arc> arcsParcelles = f.arcs();

		// Type voirie : elles n'ont qu'un voisin
		for (Arc a : arcsParcelles) {

			if (a.longueur() == 0) {
				continue;
			}
			if (a.getFaceDroite() == null || a.getFaceGauche() == null) {
				a.setOrientation(ParcelBoundaryType.ROAD.getValueType());

			} else {
				a.setOrientation(ParcelBoundaryType.UNKNOWN.getValueType());

			}

		}

		// //////////////////////////////////////
		// / Détermination des arcs latéraux donnant sur la voirie
		// //////////////////////////////////////

		List<Arc> lArcLateral = new ArrayList<Arc>();

		// Type latéral, les noeuds débouchent sur une arete sans parcelle
		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == ParcelBoundaryType.ROAD.getValueType()) {

				List<Arc> lA = new ArrayList<Arc>();

				lA.addAll(a.getNoeudIni().getSortants());
				lA.addAll(a.getNoeudFin().getSortants());
				lA.addAll(a.getNoeudIni().getEntrants());
				lA.addAll(a.getNoeudFin().getEntrants());

				for (Arc aTemp : lA) {

					if (aTemp.getOrientation() == ParcelBoundaryType.ROAD.getValueType()) {
						continue;
					}

					aTemp.setOrientation(ParcelBoundaryType.LAT.getValueType());

					// On détermine le côté de la parcelle
					determineSide(aTemp, a, f);

					lArcLateral.add(aTemp);
				}

			}

		}

		// On determine l'arrête la plus loin de la voirie, arrête de type fond
		// Type latéral, les noeuds débouchent sur une arete sans parcelle
		IMultiCurve<IOrientableCurve> iMS = new GM_MultiCurve<>();
		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == ParcelBoundaryType.ROAD.getValueType()) {

				iMS.add(a.getGeometrie());

			}

		}

		double distanceMax = -1;

		Arc bestCandidate = null;

		for (Arc a : arcsParcelles) {

			if (a.getOrientation() != ParcelBoundaryType.UNKNOWN.getValueType()) {
				continue;
			}

			double distanceTemp = a.getGeom().distance(iMS);

			if (distanceTemp > distanceMax) {
				distanceMax = distanceTemp;
				bestCandidate = a;
			}

		}

		// On affecte Bot à ceux qui sont proches de bot
		if (bestCandidate != null) {
			bestCandidate.setOrientation(ParcelBoundaryType.BOT.getValueType());

			annoteBotCandidate(bestCandidate, f);
		}

		// On instancie l'objet parcelle à partir de la carte totpo
		IMultiSurface<IOrientableSurface> ms = FromGeomToSurface.convertMSGeom(f.getGeom());

		List<Arc> listArcLat = new ArrayList<Arc>();

		List<Arc> listArcTemp = new ArrayList<>();

		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == ParcelBoundaryType.LAT.getValueType()) {
				listArcTemp.add(a);

			}

		}

		// On classe les arcs
		for (Arc a : listArcTemp) {

			// System.out.println("Classement des arcs");

			// On ne garde que les arcs latéraux

			double currentSide = a.getPoids();

			// On a un arc latéral
			listArcLat.add(a);

			// On détermine le sommet initial : celui qui donne sur la voirie
			IDirectPosition somInitial = null;
			IDirectPosition somFinal = null;

			for (Arc aTemp : a.getNoeudIni().arcs()) {

				if (aTemp.getOrientation() == ParcelBoundaryType.ROAD.getValueType()) {
					somInitial = a.getNoeudIni().getCoord();
					somFinal = a.getNoeudFin().getCoord();
					break;
				}

			}

			if (somInitial == null) {
				somFinal = a.getNoeudIni().getCoord();
				somInitial = a.getNoeudFin().getCoord();
			}

			while (true) {
				List<Arc> arcsATraites = new ArrayList<Arc>();
				arcsATraites.addAll(a.getNoeudIni().arcs());
				arcsATraites.addAll(a.getNoeudFin().arcs());

				IMultiCurve<IOrientableCurve> iMC = new GM_MultiCurve<IOrientableCurve>();
				iMC.add(a.getGeometrie());

				// On élimine les arcs que l'on ne traitera pas
				for (int i = 0; i < arcsATraites.size(); i++) {

					Arc aTemp = arcsATraites.get(i);

					// déjà typé, on ne le traite pas
					if (aTemp.getOrientation() != ParcelBoundaryType.UNKNOWN.getValueType()) {
						arcsATraites.remove(i);
						i--;
						continue;
					}

					// Pas un arc de la face en cours, on ne le traite pas
					boolean isArc = f.getArcsDirects().contains(aTemp) || f.getArcsIndirects().contains(aTemp);

					if (!isArc) {
						arcsATraites.remove(i);
						i--;
						continue;
					}

					// Dans une des listes, on ne le traite pas
					if (listArcLat.contains(aTemp)) {
						arcsATraites.remove(i);
						i--;
						continue;
					}

				}

				if (arcsATraites.isEmpty()) {
					break;
				}

				if (arcsATraites.size() > 1) {
					System.out.println(CadastralParcelLoader.class.toString() + "   > 1, il doit y avoir un bug");
				}

				// Nous n'avons qu'un candidat ... normalement
				Arc aCandidat = arcsATraites.remove(0);

				double largeur = 999;
				/* double area =iMC.convexHull().area(); */
				// if (area > 0.001) {

				if (aCandidat.getNoeudIni().getCoord().distance(somInitial) > aCandidat.getNoeudFin().getCoord()
						.distance(somInitial)) {

					somFinal = aCandidat.getNoeudIni().getCoord();

				} else {
					somFinal = aCandidat.getNoeudFin().getCoord();
				}

				LineEquation lE = new LineEquation(somInitial, somFinal);

				/* double dist1 = */lE.distance(aCandidat.getNoeudIni().getCoord());

				/* double dist2 = */lE.distance(aCandidat.getNoeudFin().getCoord());

				largeur = Double.NEGATIVE_INFINITY;

				iMC.add(aCandidat.getGeometrie());

				for (IDirectPosition dp : iMC.coord()) {

					largeur = Math.max(largeur, lE.distance(dp));

				}

				IDirectPositionList dpl = new DirectPositionList();
				dpl.add(lE.valueAt(0));
				dpl.add(lE.valueAt(1));

				// }

				a = aCandidat;
				aCandidat.setPoids(currentSide);

				listArcLat.add(aCandidat);

			} // Fin while

		} // Fin de boucle sur les arcs

		// Tous les arcs ont été mis dans une liste sauf les fonds de parcelle
		for (Arc a : arcsParcelles) {
			if (a.getOrientation() == ParcelBoundaryType.ROAD.getValueType()) {
				continue;
			}

			if (listArcLat.contains(a)) {
				a.setOrientation(ParcelBoundaryType.LAT.getValueType());
			} else {
				a.setOrientation(ParcelBoundaryType.BOT.getValueType());
			}
		}

	}

	
	private static void annoteBotCandidate(Arc bestCandidate, Face f) {

		List<Arc> arcsATraites = new ArrayList<>();
		arcsATraites.add(bestCandidate);

		while (!arcsATraites.isEmpty()) {

			Arc a = arcsATraites.remove(0);

			a.setOrientation(ParcelBoundaryType.BOT.getValueType());

			List<Arc> laTemp = new ArrayList<>();
			laTemp.addAll(a.getNoeudFin().arcs());
			laTemp.addAll(a.getNoeudIni().arcs());

			Vecteur v = new Vecteur(a.getNoeudIni().getCoord(), a.getNoeudFin().getCoord());
			v.normalise();
			for (Arc aTemp : laTemp) {

				if (aTemp.getOrientation() != ParcelBoundaryType.UNKNOWN.getValueType()) {
					continue;
				}

				if (aTemp.getFaceDroite() != f && aTemp.getFaceGauche() != f) {
					continue;
				}
				Vecteur v2 = new Vecteur(aTemp.getNoeudIni().getCoord(), aTemp.getNoeudFin().getCoord());

				v2.normalise();

				if (Math.abs(v.prodScalaire(v2)) > 0.9) {
					arcsATraites.add(aTemp);
				}

			}

		}

	}
	
}

