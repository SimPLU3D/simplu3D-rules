package fr.ign.cogit.simplu3d.generator.boundary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.common.topo.Arc;
import fr.ign.common.topo.CarteTopo;
import fr.ign.common.topo.Face;
import fr.ign.common.equation.LineEquation;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

public class Method2BoundaryAnalyzer extends AbstractBoundaryAnalyzer {
	private static final Logger logger = Logger.getLogger(Method2BoundaryAnalyzer.class.getName());

	public Method2BoundaryAnalyzer() {
		super();
	}

	@Override
	public void analyze(CarteTopo carteTopo, Face f) {
		// //////////////////////////////////////
		// / Détermination des arcs donnant sur la voirie
		// //////////////////////////////////////
		// On parcourt les arcs (futures limites de parcelles)
		Collection<Arc> arcsParcelles = carteTopo.getEdges(f);
		// Type voirie : elles n'ont qu'un voisin
		for (Arc a : arcsParcelles) {
			if (a.longueur() == 0) {
				continue;
			}
			if (a.getFaceDroite() == null || a.getFaceGauche() == null) {
				a.setTYpe(ParcelBoundaryType.ROAD.getValueType());
			} else {
				a.setTYpe(ParcelBoundaryType.UNKNOWN.getValueType());
			}
		}
		// //////////////////////////////////////
		// / Détermination des arcs latéraux donnant sur la voirie
		// //////////////////////////////////////
		List<Arc> lArcLateral = new ArrayList<>();
		// Type latéral, les noeuds débouchent sur une arete sans parcelle
		for (Arc a : arcsParcelles) {
			if (a.getType() == ParcelBoundaryType.ROAD.getValueType()) {
				List<Arc> lA = new ArrayList<>();
				lA.addAll(carteTopo.getSortants(a.getNoeudIni()));
				lA.addAll(carteTopo.getSortants(a.getNoeudFin()));
				lA.addAll(carteTopo.getEntrants(a.getNoeudIni()));
				lA.addAll(carteTopo.getEntrants(a.getNoeudFin()));
				for (Arc aTemp : lA) {
					if (aTemp.getType() == ParcelBoundaryType.ROAD.getValueType()) {
						continue;
					}
					aTemp.setTYpe(ParcelBoundaryType.LAT.getValueType());
					// On détermine le côté de la parcelle
					determineSide(aTemp, a, f);
					lArcLateral.add(aTemp);
				}
			}
		}
		// On determine l'arrête la plus loin de la voirie, arrête de type fond
		// Type latéral, les noeuds débouchent sur une arete sans parcelle
		List<LineString> lineStrings = new ArrayList<>();
		for (Arc a : arcsParcelles) {
			if (a.getType() == ParcelBoundaryType.ROAD.getValueType()) {
				lineStrings.add(a.getGeometrie());
			}
		}
		MultiLineString iMS = new GeometryFactory().createMultiLineString(lineStrings.toArray(new LineString[0]));
		double distanceMax = -1;
		Arc bestCandidate = null;
		for (Arc a : arcsParcelles) {
			if (a.getType() != ParcelBoundaryType.UNKNOWN.getValueType()) {
				continue;
			}
			double distanceTemp = a.getGeometrie().distance(iMS);
			if (distanceTemp > distanceMax) {
				distanceMax = distanceTemp;
				bestCandidate = a;
			}
		}
		// On affecte Bot à ceux qui sont proches de bot
		if (bestCandidate != null) {
			bestCandidate.setTYpe(ParcelBoundaryType.BOT.getValueType());
			annoteBotCandidate(carteTopo, bestCandidate, f);
		}
		// On instancie l'objet parcelle à partir de la carte totpo
		List<Arc> listArcLat = new ArrayList<>();
		List<Arc> listArcTemp = new ArrayList<>();
		for (Arc a : arcsParcelles) {
			if (a.getType() == ParcelBoundaryType.LAT.getValueType()) {
				listArcTemp.add(a);
			}
		}
		// On classe les arcs
		for (Arc a : listArcTemp) {
			// System.out.println("Classement des arcs");
			// On ne garde que les arcs latéraux
			int currentSide = a.getSide();
			// On a un arc latéral
			listArcLat.add(a);
			// On détermine le sommet initial : celui qui donne sur la voirie
			Coordinate somInitial = null;
			Coordinate somFinal = null;
			for (Arc aTemp : carteTopo.getEdges(a.getNoeudIni())) {
				if (aTemp.getType() == ParcelBoundaryType.ROAD.getValueType()) {
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
				List<Arc> arcsATraites = new ArrayList<>();
				arcsATraites.addAll(carteTopo.getEdges(a.getNoeudIni()));
				arcsATraites.addAll(carteTopo.getEdges(a.getNoeudFin()));
				List<LineString> lineStringList = new ArrayList<>();
				lineStringList.add(a.getGeometrie());
				// On élimine les arcs que l'on ne traitera pas
				for (int i = 0; i < arcsATraites.size(); i++) {
					Arc aTemp = arcsATraites.get(i);
					// déjà typé, on ne le traite pas
					if (aTemp.getType() != ParcelBoundaryType.UNKNOWN.getValueType()) {
						arcsATraites.remove(i);
						i--;
						continue;
					}
					// Pas un arc de la face en cours, on ne le traite pas
					boolean isArc = (aTemp.getFaceDroite() == f) || (aTemp.getFaceGauche() == f);
					//boolean isArc = f.getArcsDirects().contains(aTemp) || f.getArcsIndirects().contains(aTemp);
					if (!isArc) {
						arcsATraites.remove(i);
						i--;
						continue;
					}
					// Dans une des listes, on ne le traite pas
					if (listArcLat.contains(aTemp)) {
						arcsATraites.remove(i);
						i--;
                    }
				}
				if (arcsATraites.isEmpty()) {
					break;
				}
				if (arcsATraites.size() > 1) {
					logger.severe(Method2BoundaryAnalyzer.class + "   > 1, il doit y avoir un bug");
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
				largeur = Double.NEGATIVE_INFINITY;
				lineStringList.add(aCandidat.getGeometrie());
				MultiLineString iMC = new GeometryFactory().createMultiLineString(lineStrings.toArray(new LineString[0]));
				for (Coordinate dp : iMC.getCoordinates()) {
					largeur = Math.max(largeur, lE.distance(dp));
				}
/*
				IDirectPositionList dpl = new DirectPositionList();
				dpl.add(lE.valueAt(0));
				dpl.add(lE.valueAt(1));
*/
				// }
				a = aCandidat;
				aCandidat.setSide(currentSide);
				listArcLat.add(aCandidat);
			} // Fin while
		} // Fin de boucle sur les arcs
		// Tous les arcs ont été mis dans une liste sauf les fonds de parcelle
		for (Arc a : arcsParcelles) {
			if (a.getType() == ParcelBoundaryType.ROAD.getValueType()) {
				continue;
			}
			if (listArcLat.contains(a)) {
				a.setTYpe(ParcelBoundaryType.LAT.getValueType());
			} else {
				a.setTYpe(ParcelBoundaryType.BOT.getValueType());
			}
		}
	}

	/*
	private Arc cloneArc(Arc a) {
		Arc aclone = new Arc();
		aclone.setGeom(a.getGeom());
		aclone.setGeometrie(a.getGeometrie());
		aclone.setPoids(a.getPoids());
		aclone.setOrientation(a.getOrientation());
		aclone.setFaceDroite(a.getFaceDroite());
		aclone.setFaceGauche(a.getFaceGauche());

		return aclone;
	}*/

	private static void annoteBotCandidate(CarteTopo carteTopo, Arc bestCandidate, Face f) {
		List<Arc> arcsATraites = new ArrayList<>();
		arcsATraites.add(bestCandidate);
		while (!arcsATraites.isEmpty()) {
			Arc a = arcsATraites.remove(0);
			a.setTYpe(ParcelBoundaryType.BOT.getValueType());
			List<Arc> laTemp = new ArrayList<>();
			Vecteur v = null;
			if (a.getNoeudIni() != null && a.getNoeudFin() != null) {
				laTemp.addAll(carteTopo.getEdges(a.getNoeudIni()));
				laTemp.addAll(carteTopo.getEdges(a.getNoeudFin()));
				v = new Vecteur(a.getNoeudIni().getCoord(), a.getNoeudFin().getCoord());
				v.normalise();
			}
			for (Arc aTemp : laTemp) {
				if (aTemp.getType() != ParcelBoundaryType.UNKNOWN.getValueType()) {
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
