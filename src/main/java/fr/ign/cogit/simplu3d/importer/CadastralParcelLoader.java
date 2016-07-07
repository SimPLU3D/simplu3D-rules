/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 * 
 *          Assigne les bordures aux parcelles 3 types de bordure (voirie, fond
 *          ou latéral) en fonction du voisinage
 * 
 */
package fr.ign.cogit.simplu3d.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.equation.LineEquation;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.algo.SmallestSurroundingRectangleComputation;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.builder.CarteTopoBuilder;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundarySide;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundaryType;
import fr.ign.cogit.simplu3d.util.PointInPolygon;

/**
 * 
 * Create CadastralParcel and SpecificCadastralBoundaries from a set of Features
 * 
 * TODO split processing (take an input Collection<CadastralParcel> in a CadastralBoundaryBuilder)
 * 
 * @author MBrasebin
 *
 */
public class CadastralParcelLoader {
	// TODO member variable
	public static int TYPE_ANNOTATION = 1;
	public static double MINIMUM_AREA_PARC = 2;
	public static int WIDTH_DEP = 3;

	public static String ATT_ID_PARC = "NUMERO";
	public static String ATT_HAS_TO_BE_SIMULATED = "simul";
	// public static String ATT_ID_PARC = "NUMERO";

	private static Logger logger = Logger.getLogger(CadastralParcelLoader.class);

	public static IFeatureCollection<CadastralParcel> assignBordureToParcelleWithOrientation(
			IFeatureCollection<IFeature> parcelCollection) {

		// System.out.println("NB Parcelles : " + parcelCollection.size());

		IFeatureCollection<CadastralParcel> cadastralParcels = new FT_FeatureCollection<>();

		// On créer une carte topo avec les parcelles
		CarteTopo cT = CarteTopoBuilder.newCarteTopo("Parcelles", parcelCollection, 0.2);

		for (Face f : cT.getPopFaces()) {
			IFeatureCollection<CadastralParcel> cadastralParcelTemp;

			if (TYPE_ANNOTATION == 1) {
				cadastralParcelTemp = analyseFace(f, WIDTH_DEP);
			} else {

				cadastralParcelTemp = analyseFace2(f, WIDTH_DEP);

			}

			if (cadastralParcelTemp != null) {
				cadastralParcels.addAll(cadastralParcelTemp);
			}

		}

		int nbElem = cT.getPopFaces().size();

		parcelCollection.initSpatialIndex(Tiling.class, false);

		/*
		 * Boucle pour le rapport d'attributs et pour indiquer les objets
		 * voisins en fonction des limites séparatives
		 */
		for (int i = 0; i < nbElem; i++) {

			Face f = cT.getPopFaces().get(i);
			CadastralParcel parc = cadastralParcels.get(i);

			Collection<IFeature> coll = parcelCollection.select(PointInPolygon.get(f.getGeometrie()), 0);

			if (coll.isEmpty()) {
				logger.warn("Error in topology some faces were created with no corersponding parcels");

				continue;
			}

			if (coll.size() > 1) {
				logger.error("Error in topology some faces were created with serveral corresponding parcels");

				continue;
			}

			if (parc.getGeom().area() < MINIMUM_AREA_PARC) {

				continue;
			}

			Iterator<IFeature> it = coll.iterator();
			IFeature feat = it.next();

			Object idParc = feat.getAttribute(ATT_ID_PARC);
			if ( idParc == null ){
				throw new RuntimeException("attribute "+ATT_ID_PARC+" not found");
			}
			//int idParc = Integer.parseInt(.toString());

			Object o = feat.getAttribute(ATT_HAS_TO_BE_SIMULATED);

			if (o != null) {
				parc.setHasToBeSimulated(1 == Integer.parseInt(o.toString()));

			}

			// System.out.println(idParc);
			parc.setId(Integer.parseInt(idParc.toString()));

			List<Arc> lArc = f.arcs();
			int nbArcs = lArc.size();

			for (int j = 0; j < nbArcs; j++) {

				Arc a = lArc.get(j);
				SpecificCadastralBoundary sCB = parc.getSpecificCadastralBoundary().get(j);

				if (sCB.getType() == SpecificCadastralBoundaryType.ROAD) {
					continue;

				}

				Face fCand = a.getFaceDroite();

				if (fCand == f) {
					fCand = a.getFaceGauche();
				}

				int indexFCand = cT.getPopFaces().getElements().indexOf(fCand);

				if (indexFCand != -1) {
					sCB.setFeatAdj(cadastralParcels.get(indexFCand));
				}

			}

		}

		return cadastralParcels;

	}

	private static IFeatureCollection<CadastralParcel> analyseFace2(Face f, double thresholdIni) {

		// Les parcelles générés en sortie

		IFeatureCollection<CadastralParcel> parcelles = new FT_FeatureCollection<CadastralParcel>();

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
				a.setOrientation(SpecificCadastralBoundaryType.ROAD.getValueType());

			} else {
				a.setOrientation(SpecificCadastralBoundaryType.UNKNOWN.getValueType());

			}

		}

		// //////////////////////////////////////
		// / Détermination des arcs latéraux donnant sur la voirie
		// //////////////////////////////////////

		List<Arc> lArcLateral = new ArrayList<Arc>();

		// Type latéral, les noeuds débouchent sur une arete sans parcelle
		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == SpecificCadastralBoundaryType.ROAD.getValueType()) {

				List<Arc> lA = new ArrayList<Arc>();

				lA.addAll(a.getNoeudIni().getSortants());
				lA.addAll(a.getNoeudFin().getSortants());
				lA.addAll(a.getNoeudIni().getEntrants());
				lA.addAll(a.getNoeudFin().getEntrants());

				for (Arc aTemp : lA) {

					if (aTemp.getOrientation() == SpecificCadastralBoundaryType.ROAD.getValueType()) {
						continue;
					}

					aTemp.setOrientation(SpecificCadastralBoundaryType.LAT.getValueType());

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

			if (a.getOrientation() == SpecificCadastralBoundaryType.ROAD.getValueType()) {

				iMS.add(a.getGeometrie());

			}

		}

		double distanceMax = -1;

		Arc bestCandidate = null;

		for (Arc a : arcsParcelles) {

			if (a.getOrientation() != SpecificCadastralBoundaryType.UNKNOWN.getValueType()) {
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
			bestCandidate.setOrientation(SpecificCadastralBoundaryType.BOT.getValueType());

			annoteBotCandidate(bestCandidate, f);

		}

		// On instancie l'objet parcelle à partir de la carte totpo
		IMultiSurface<IOrientableSurface> ms = FromGeomToSurface.convertMSGeom(f.getGeom());

		CadastralParcel p = new CadastralParcel(ms);
		parcelles.add(p);

		List<Arc> listArcLat = new ArrayList<Arc>();

		List<Arc> listArcTemp = new ArrayList<>();

		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == SpecificCadastralBoundaryType.LAT.getValueType()) {
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

				if (aTemp.getOrientation() == SpecificCadastralBoundaryType.ROAD.getValueType()) {
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
					if (aTemp.getOrientation() != SpecificCadastralBoundaryType.UNKNOWN.getValueType()) {
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
					System.out.println("> 1, il doit y avoir un bug");
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
			SpecificCadastralBoundary cB = new SpecificCadastralBoundary(a.getGeom());
			p.getSpecificCadastralBoundary().add(cB);

			if (a.getOrientation() == SpecificCadastralBoundaryType.ROAD.getValueType()) {
				cB.setType(SpecificCadastralBoundaryType.ROAD);
				continue;
			}

			if (listArcLat.contains(a)) {
				cB.setType(SpecificCadastralBoundaryType.LAT);
				cB.setSide(SpecificCadastralBoundarySide.getTypeFromInt((int) a.getPoids()));

			} else {

				cB.setType(SpecificCadastralBoundaryType.BOT);

			}

		}

		return parcelles;
	}

	private static void annoteBotCandidate(Arc bestCandidate, Face f) {

		List<Arc> arcsATraites = new ArrayList<>();
		arcsATraites.add(bestCandidate);

		while (!arcsATraites.isEmpty()) {

			Arc a = arcsATraites.remove(0);

			a.setOrientation(SpecificCadastralBoundaryType.BOT.getValueType());

			List<Arc> laTemp = new ArrayList<>();
			laTemp.addAll(a.getNoeudFin().arcs());
			laTemp.addAll(a.getNoeudIni().arcs());

			Vecteur v = new Vecteur(a.getNoeudIni().getCoord(), a.getNoeudFin().getCoord());
			v.normalise();
			for (Arc aTemp : laTemp) {

				if (aTemp.getOrientation() != SpecificCadastralBoundaryType.UNKNOWN.getValueType()) {
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

	private static IFeatureCollection<CadastralParcel> analyseFace(Face f, double thresholdIni) {

		// Les parcelles générés en sortie

		IFeatureCollection<CadastralParcel> parcelles = new FT_FeatureCollection<CadastralParcel>();

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
				a.setOrientation(SpecificCadastralBoundaryType.ROAD.getValueType());

			} else {
				a.setOrientation(SpecificCadastralBoundaryType.UNKNOWN.getValueType());

			}

		}

		// //////////////////////////////////////
		// / Détermination des arcs latéraux donnant sur la voirie
		// //////////////////////////////////////

		List<Arc> lArcLateral = new ArrayList<Arc>();

		// Type latéral, les noeuds débouchent sur une arete sans parcelle
		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == SpecificCadastralBoundaryType.ROAD.getValueType()) {

				List<Arc> lA = new ArrayList<Arc>();

				lA.addAll(a.getNoeudIni().getSortants());
				lA.addAll(a.getNoeudFin().getSortants());
				lA.addAll(a.getNoeudIni().getEntrants());
				lA.addAll(a.getNoeudFin().getEntrants());

				for (Arc aTemp : lA) {

					if (aTemp.getOrientation() == SpecificCadastralBoundaryType.ROAD.getValueType()) {
						continue;
					}

					aTemp.setOrientation(SpecificCadastralBoundaryType.LAT.getValueType());

					// On détermine le côté de la parcelle
					determineSide(aTemp, a, f);

					lArcLateral.add(aTemp);
				}

			}

		}

		// On détermine le seuil de dépassement de la face en fonction de sa
		// taille
		double threshold = determineThreshold(f, thresholdIni);

		// On instancie l'objet parcelle à partir de la carte totpo
		IMultiSurface<IOrientableSurface> ms = FromGeomToSurface.convertMSGeom(f.getGeom());

		CadastralParcel p = new CadastralParcel(ms);
		parcelles.add(p);

		List<Arc> listArcLat = new ArrayList<Arc>();

		List<Arc> listArcTemp = new ArrayList<>();

		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == SpecificCadastralBoundaryType.LAT.getValueType()) {
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

				if (aTemp.getOrientation() == SpecificCadastralBoundaryType.ROAD.getValueType()) {
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
					if (aTemp.getOrientation() != SpecificCadastralBoundaryType.UNKNOWN.getValueType()) {
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
					System.out.println("> 1, il doit y avoir un bug");
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

				if (largeur < threshold) {

					a = aCandidat;
					aCandidat.setPoids(currentSide);

					listArcLat.add(aCandidat);
				} else {

					break;

				}

			} // Fin while

		} // Fin de boucle sur les arcs

		// Tous les arcs ont été mis dans une liste sauf les fonds de parcelle
		for (Arc a : arcsParcelles) {
			SpecificCadastralBoundary cB = new SpecificCadastralBoundary(a.getGeom());
			p.getSpecificCadastralBoundary().add(cB);

			if (a.getOrientation() == SpecificCadastralBoundaryType.ROAD.getValueType()) {
				cB.setType(SpecificCadastralBoundaryType.ROAD);
				continue;
			}

			if (listArcLat.contains(a)) {
				cB.setType(SpecificCadastralBoundaryType.LAT);
				cB.setSide(SpecificCadastralBoundarySide.getTypeFromInt((int) a.getPoids()));

			} else {

				cB.setType(SpecificCadastralBoundaryType.BOT);

			}

		}

		return parcelles;
	}

	private static void determineSide(Arc aTemp, Arc a, Face f) {

		// C'est un arc direct
		if (f.getArcsDirects().contains(aTemp)) {

			if (aTemp.getNoeudFin().equals(a.getNoeudIni()) || aTemp.getNoeudFin().equals(a.getNoeudFin())) {

				aTemp.setPoids(SpecificCadastralBoundarySide.LEFT.getValueType());

			} else {

				aTemp.setPoids(SpecificCadastralBoundarySide.RIGHT.getValueType());
			}

			return;
		}

		if (f.getArcsIndirects().contains(aTemp)) {

			if (aTemp.getNoeudFin().equals(a.getNoeudIni()) || aTemp.getNoeudFin().equals(a.getNoeudFin())) {

				aTemp.setPoids(SpecificCadastralBoundary.SpecificCadastralBoundarySide.RIGHT.getValueType());

			} else {

				aTemp.setPoids(SpecificCadastralBoundary.SpecificCadastralBoundarySide.LEFT.getValueType());
			}

		}

	}

	private static double determineThreshold(Face f, double thresholdIni) {
		IPolygon poly = SmallestSurroundingRectangleComputation.getSSR(f.getGeometrie());
		double l1 = poly.coord().get(0).distance2D(poly.coord().get(1));
		double l2 = poly.coord().get(1).distance2D(poly.coord().get(2));

		double largeur = Math.min(l1, l2);

		// System.out.println(largeur);

		if (largeur / 2.5 < thresholdIni) {

			// System.out.println("Modification de la largeur de dépassementj'y
			// passe");

			return largeur / 2.5;
		}

		return thresholdIni;
	}


}
