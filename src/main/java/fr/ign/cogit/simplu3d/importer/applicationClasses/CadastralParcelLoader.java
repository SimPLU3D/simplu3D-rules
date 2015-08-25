package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
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
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToLineString;
import fr.ign.cogit.geoxygene.sig3d.equation.LineEquation;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.algo.SmallestSurroundingRectangleComputation;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.util.PointInPolygon;

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
public class CadastralParcelLoader {

	public static final int UNKNOWN = 99;
	public static final int LATERAL_TEMP = 98;
	
	public static int TYPE_ANNOTATION = 1;
	
	
	public static int WIDTH_DEP = 3;

	public static String ATT_ID_PARC = "ID_Parcell";

	private static Logger logger = Logger
			.getLogger(CadastralParcelLoader.class);

	public static IFeatureCollection<CadastralParcel> assignBordureToParcelleWithOrientation(
			IFeatureCollection<IFeature> parcelCollection) {

		System.out.println("NB Parcelles : " + parcelCollection.size());

		IFeatureCollection<CadastralParcel> cadastralParcels = new FT_FeatureCollection<>();

		// On créer une carte topo avec les parcelles
		CarteTopo cT = newCarteTopo("Parcelles", parcelCollection, 0.2);

		System.out.println("Je passe là !!!!");

		for (Face f : cT.getPopFaces()) {
			IFeatureCollection<CadastralParcel> cadastralParcelTemp;
			
			if(TYPE_ANNOTATION == 1){
				cadastralParcelTemp= analyseFace(f, WIDTH_DEP);
			}else{

				 cadastralParcelTemp = analyseFace2(
						f, WIDTH_DEP);
				
			}
			
		
				
			

			if (cadastralParcelTemp != null) {
				cadastralParcels.addAll(cadastralParcelTemp);
			}

		}

		int nbElem = cT.getPopFaces().size();

		parcelCollection.initSpatialIndex(Tiling.class, false);

		for (int i = 0; i < nbElem; i++) {

			Face f = cT.getPopFaces().get(i);
			CadastralParcel parc = cadastralParcels.get(i);

			Collection<IFeature> coll = parcelCollection.select(
					PointInPolygon.get(f.getGeometrie()), 0);

			if (coll.isEmpty() || coll.size() > 1) {
				logger.error("Several parcels for a single face of CarteTopo");
				System.exit(0);
			}

			Iterator<IFeature> it = coll.iterator();
			IFeature feat = it.next();

			int idParc = Integer.parseInt(feat.getAttribute(ATT_ID_PARC)
					.toString());
		//	System.out.println(idParc);
			parc.setId(idParc);
	
			List<Arc> lArc = f.arcs();
			int nbArcs = lArc.size();

			for (int j = 0; j < nbArcs; j++) {

				Arc a = lArc.get(j);
				SpecificCadastralBoundary sCB = parc
						.getSpecificCadastralBoundary().get(j);

				if (sCB.getType() == SpecificCadastralBoundary.ROAD) {
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

	private static IFeatureCollection<CadastralParcel> analyseFace2(Face f,
			double thresholdIni) {

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
				a.setOrientation(SpecificCadastralBoundary.ROAD);

			} else {
				a.setOrientation(UNKNOWN);

			}

		}

		// //////////////////////////////////////
		// / Détermination des arcs latéraux donnant sur la voirie
		// //////////////////////////////////////

		List<Arc> lArcLateral = new ArrayList<Arc>();

		// Type latéral, les noeuds débouchent sur une arete sans parcelle
		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == SpecificCadastralBoundary.ROAD) {

				List<Arc> lA = new ArrayList<Arc>();

				lA.addAll(a.getNoeudIni().getSortants());
				lA.addAll(a.getNoeudFin().getSortants());
				lA.addAll(a.getNoeudIni().getEntrants());
				lA.addAll(a.getNoeudFin().getEntrants());

				for (Arc aTemp : lA) {

					if (aTemp.getOrientation() == SpecificCadastralBoundary.ROAD) {
						continue;
					}

					aTemp.setOrientation(SpecificCadastralBoundary.LAT);

					// On détermine le côté de la parcelle
					determineSide(aTemp, a, f);

					lArcLateral.add(aTemp);
				}

			}

		}
		
		//On determine l'arrête la plus loin de la voirie, arrête de type fond
		// Type latéral, les noeuds débouchent sur une arete sans parcelle
		IMultiCurve<IOrientableCurve> iMS = new GM_MultiCurve<>();
		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == SpecificCadastralBoundary.ROAD) {
				
				iMS.add(a.getGeometrie());
				
			}
			
		}		
		
		double distanceMax = - 1;
		
		
		Arc bestCandidate = null;
		
		for (Arc a : arcsParcelles) {
			
			
			if(a.getOrientation() != UNKNOWN){
				continue;
			}
			
			double distanceTemp = a.getGeom().distance(iMS);
			
			if(distanceTemp > distanceMax){
				distanceMax = distanceTemp;
				bestCandidate = a;
			}
			
			
			
		}
		
		
		//On affecte Bot à ceux qui sont proches de bot
		if(bestCandidate != null){
			bestCandidate.setOrientation(SpecificCadastralBoundary.BOT);
			
			
			annoteBotCandidate(bestCandidate, f);
			
			
		}
		


		// On instancie l'objet parcelle à partir de la carte totpo
		IMultiSurface<IOrientableSurface> ms = FromGeomToSurface
				.convertMSGeom(f.getGeom());

		CadastralParcel p = new CadastralParcel(ms);
		parcelles.add(p);

		List<Arc> listArcLat = new ArrayList<Arc>();

		List<Arc> listArcTemp = new ArrayList<>();

		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == SpecificCadastralBoundary.LAT) {
				listArcTemp.add(a);

			}

		}

		// On classe les arcs
		for (Arc a : listArcTemp) {

			System.out.println("Classement des arcs");

			// On ne garde que les arcs latéraux

			double currentSide = a.getPoids();

			// On a un arc latéral
			listArcLat.add(a);

			// On détermine le sommet initial : celui qui donne sur la voirie
			IDirectPosition somInitial = null;
			IDirectPosition somFinal = null;

			for (Arc aTemp : a.getNoeudIni().arcs()) {

				if (aTemp.getOrientation() == SpecificCadastralBoundary.ROAD) {
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
					if (aTemp.getOrientation() != UNKNOWN) {
						arcsATraites.remove(i);
						i--;
						continue;
					}

					// Pas un arc de la face en cours, on ne le traite pas
					boolean isArc = f.getArcsDirects().contains(aTemp)
							|| f.getArcsIndirects().contains(aTemp);

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

				if (aCandidat.getNoeudIni().getCoord().distance(somInitial) > aCandidat
						.getNoeudFin().getCoord().distance(somInitial)) {

					somFinal = aCandidat.getNoeudIni().getCoord();

				} else {
					somFinal = aCandidat.getNoeudFin().getCoord();
				}

				LineEquation lE = new LineEquation(somInitial, somFinal);

				/* double dist1 = */lE.distance(aCandidat.getNoeudIni()
						.getCoord());

				/* double dist2 = */lE.distance(aCandidat.getNoeudFin()
						.getCoord());

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
			
			}// Fin while

		}// Fin de boucle sur les arcs

		// Tous les arcs ont été mis dans une liste sauf les fonds de parcelle
		for (Arc a : arcsParcelles) {
			SpecificCadastralBoundary cB = new SpecificCadastralBoundary(
					a.getGeom());
			p.getSpecificCadastralBoundary().add(cB);

			if (a.getOrientation() == SpecificCadastralBoundary.ROAD) {
				cB.setType(SpecificCadastralBoundary.ROAD);
				continue;
			}

			if (listArcLat.contains(a)) {
				cB.setType(SpecificCadastralBoundary.LAT);
				cB.setSide((int) a.getPoids());

			} else {

				cB.setType(SpecificCadastralBoundary.BOT);

			}

		}

		return parcelles;
	}
	private static void annoteBotCandidate(Arc bestCandidate, Face f) {
		

		List<Arc> arcsATraites = new ArrayList<>();
		arcsATraites.add(bestCandidate);
		
		
		while(! arcsATraites.isEmpty()){
			
			Arc a = arcsATraites.remove(0);

			a.setOrientation(SpecificCadastralBoundary.BOT);
			
			List<Arc> laTemp = new ArrayList<>();
			laTemp.addAll(a.getNoeudFin().arcs());
			laTemp.addAll(a.getNoeudIni().arcs());
			
			
			Vecteur v = new Vecteur(a.getNoeudIni().getCoord(), a.getNoeudFin().getCoord()); 
			v.normalise();
			for(Arc aTemp : laTemp){
			
		
				if(aTemp.getOrientation() != UNKNOWN){
					continue;
				}
				
				if(aTemp.getFaceDroite() != f && aTemp.getFaceGauche() != f){
					continue;
				}
				Vecteur v2 = new Vecteur(aTemp.getNoeudIni().getCoord(), aTemp.getNoeudFin().getCoord()); 
				
				v2.normalise();
				
				if(Math.abs(v.prodScalaire(v2)) > 0.9){
					arcsATraites.add(aTemp);
				}
				
				
				
			}
			
			
			
		}
		
			
	}

	private static IFeatureCollection<CadastralParcel> analyseFace(Face f,
			double thresholdIni) {

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
				a.setOrientation(SpecificCadastralBoundary.ROAD);

			} else {
				a.setOrientation(UNKNOWN);

			}

		}

		// //////////////////////////////////////
		// / Détermination des arcs latéraux donnant sur la voirie
		// //////////////////////////////////////

		List<Arc> lArcLateral = new ArrayList<Arc>();

		// Type latéral, les noeuds débouchent sur une arete sans parcelle
		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == SpecificCadastralBoundary.ROAD) {

				List<Arc> lA = new ArrayList<Arc>();

				lA.addAll(a.getNoeudIni().getSortants());
				lA.addAll(a.getNoeudFin().getSortants());
				lA.addAll(a.getNoeudIni().getEntrants());
				lA.addAll(a.getNoeudFin().getEntrants());

				for (Arc aTemp : lA) {

					if (aTemp.getOrientation() == SpecificCadastralBoundary.ROAD) {
						continue;
					}

					aTemp.setOrientation(SpecificCadastralBoundary.LAT);

					// On détermine le côté de la parcelle
					determineSide(aTemp, a, f);

					lArcLateral.add(aTemp);
				}

			}

		}

		// On détermine le seuil de dépassement de la face en fonction de sa
		// taille
		double threshold = determineThreshol(f, thresholdIni);

		// On instancie l'objet parcelle à partir de la carte totpo
		IMultiSurface<IOrientableSurface> ms = FromGeomToSurface
				.convertMSGeom(f.getGeom());

		CadastralParcel p = new CadastralParcel(ms);
		parcelles.add(p);

		List<Arc> listArcLat = new ArrayList<Arc>();

		List<Arc> listArcTemp = new ArrayList<>();

		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == SpecificCadastralBoundary.LAT) {
				listArcTemp.add(a);

			}

		}

		// On classe les arcs
		for (Arc a : listArcTemp) {

			System.out.println("Classement des arcs");

			// On ne garde que les arcs latéraux

			double currentSide = a.getPoids();

			// On a un arc latéral
			listArcLat.add(a);

			// On détermine le sommet initial : celui qui donne sur la voirie
			IDirectPosition somInitial = null;
			IDirectPosition somFinal = null;

			for (Arc aTemp : a.getNoeudIni().arcs()) {

				if (aTemp.getOrientation() == SpecificCadastralBoundary.ROAD) {
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
					if (aTemp.getOrientation() != UNKNOWN) {
						arcsATraites.remove(i);
						i--;
						continue;
					}

					// Pas un arc de la face en cours, on ne le traite pas
					boolean isArc = f.getArcsDirects().contains(aTemp)
							|| f.getArcsIndirects().contains(aTemp);

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

				if (aCandidat.getNoeudIni().getCoord().distance(somInitial) > aCandidat
						.getNoeudFin().getCoord().distance(somInitial)) {

					somFinal = aCandidat.getNoeudIni().getCoord();

				} else {
					somFinal = aCandidat.getNoeudFin().getCoord();
				}

				LineEquation lE = new LineEquation(somInitial, somFinal);

				/* double dist1 = */lE.distance(aCandidat.getNoeudIni()
						.getCoord());

				/* double dist2 = */lE.distance(aCandidat.getNoeudFin()
						.getCoord());

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

			}// Fin while

		}// Fin de boucle sur les arcs

		// Tous les arcs ont été mis dans une liste sauf les fonds de parcelle
		for (Arc a : arcsParcelles) {
			SpecificCadastralBoundary cB = new SpecificCadastralBoundary(
					a.getGeom());
			p.getSpecificCadastralBoundary().add(cB);

			if (a.getOrientation() == SpecificCadastralBoundary.ROAD) {
				cB.setType(SpecificCadastralBoundary.ROAD);
				continue;
			}

			if (listArcLat.contains(a)) {
				cB.setType(SpecificCadastralBoundary.LAT);
				cB.setSide((int) a.getPoids());

			} else {

				cB.setType(SpecificCadastralBoundary.BOT);

			}

		}

		return parcelles;
	}
	
	

	private static void determineSide(Arc aTemp, Arc a, Face f) {
		
		
		
		//C'est un arc direct
		if(f.getArcsDirects().contains(aTemp)){
			
			
			if(aTemp.getNoeudFin().equals(a.getNoeudIni()) || aTemp.getNoeudFin().equals(a.getNoeudFin())){
				
				aTemp.setPoids(SpecificCadastralBoundary.LEFT_SIDE);
				
			}else{
				
				aTemp.setPoids(SpecificCadastralBoundary.RIGHT_SIDE);
			}
		
			
			
			return;
		}
		
		
		if(f.getArcsIndirects().contains(aTemp)){

			
			
			if(aTemp.getNoeudFin().equals(a.getNoeudIni()) || aTemp.getNoeudFin().equals(a.getNoeudFin())){
				
				aTemp.setPoids(SpecificCadastralBoundary.RIGHT_SIDE);
				
			}else{
				
				aTemp.setPoids(SpecificCadastralBoundary.LEFT_SIDE);
			}
		
			
			
		}
		
		
		


		
		/*
		
		if (aTemp.getNoeudFin().equals(a.getNoeudFin())
				&& aTemp.getFaceDroite() == f) {


			return;
		}

		if (aTemp.getNoeudFin().equals(a.getNoeudFin())
				&&aTemp.getFaceGauche() == f) {

			aTemp.setPoids(SpecificCadastralBoundary.LEFT_SIDE);
			return;

		}

		if (aTemp.getNoeudFin().equals(a.getNoeudIni())
				&&aTemp.getFaceGauche() == f) {
			aTemp.setPoids(SpecificCadastralBoundary.RIGHT_SIDE);
			return;
		}

		if (aTemp.getNoeudFin().equals(a.getNoeudIni())
				&& aTemp.getFaceDroite() == f) {

			aTemp.setPoids(SpecificCadastralBoundary.LEFT_SIDE);
			return;
		}

		if (aTemp.getNoeudIni().equals(a.getNoeudFin())
				&& aTemp.getFaceDroite() == f) {

			aTemp.setPoids(SpecificCadastralBoundary.LEFT_SIDE);

			return;
		}

		if (aTemp.getNoeudIni().equals(a.getNoeudFin())
				&&aTemp.getFaceGauche() ==f) {
			aTemp.setPoids(SpecificCadastralBoundary.RIGHT_SIDE);
			return;

		}

		if (aTemp.getNoeudIni().equals(a.getNoeudIni())
				&&aTemp.getFaceGauche() ==f) {

			aTemp.setPoids(SpecificCadastralBoundary.LEFT_SIDE);
			return;
		}

		if (aTemp.getNoeudIni().equals(a.getNoeudIni())
				&& aTemp.getFaceDroite() == f) {

			aTemp.setPoids(SpecificCadastralBoundary.RIGHT_SIDE);
			return;
		}*/


	}

	private static double determineThreshol(Face f, double thresholdIni) {
		IPolygon poly = SmallestSurroundingRectangleComputation.getSSR(f
				.getGeometrie());
		double l1 = poly.coord().get(0).distance2D(poly.coord().get(1));
		double l2 = poly.coord().get(1).distance2D(poly.coord().get(2));

		double largeur = Math.min(l1, l2);

		System.out.println(largeur);

		if (largeur / 2.5 < thresholdIni) {

			// System.out.println("Modification de la largeur de dépassementj'y passe");

			return largeur / 2.5;
		}

		return thresholdIni;
	}

	@Deprecated
	public static IFeatureCollection<CadastralParcel> assignBordureToParcelle(
			IFeatureCollection<IFeature> parcelCollection) {

		System.out.println("NB Parcelles : " + parcelCollection.size());

		// On créer une carte topo avec les parcelles
		CarteTopo cT = newCarteTopo("Parcelles", parcelCollection, 0.1);

		System.out.println("NB faces : " + cT.getPopFaces().size());

		// On parcourt les arcs (futures bordures)
		IPopulation<Arc> arcsParcelles = cT.getPopArcs();

		// Type voirie : elles n'ont pas de voisins
		for (Arc a : arcsParcelles) {

			if (a.getFaceDroite() == null || a.getFaceGauche() == null) {
				a.setOrientation(SpecificCadastralBoundary.ROAD);

			} else {
				a.setOrientation(UNKNOWN);
			}

		}

		// Type latéral, les noeuds débouchent sur une arrete sans parcelle
		for (Arc a : arcsParcelles) {

			if (a.getOrientation() == SpecificCadastralBoundary.ROAD) {

				List<Arc> lA = new ArrayList<Arc>();

				lA.addAll(a.getNoeudIni().getSortants());
				lA.addAll(a.getNoeudFin().getSortants());
				lA.addAll(a.getNoeudIni().getEntrants());
				lA.addAll(a.getNoeudFin().getEntrants());

				for (Arc aTemp : lA) {

					if (aTemp.getOrientation() == SpecificCadastralBoundary.ROAD) {
						continue;
					}

					aTemp.setOrientation(SpecificCadastralBoundary.LAT);

				}

			}

		}

		// On affecte les types voiries et fond
		IPopulation<Face> facesParcelles = cT.getPopFaces();

		boucleFace: for (Face f : facesParcelles) {
			List<Arc> lA = new ArrayList<Arc>();

			lA.addAll(f.getArcsDirects());
			lA.addAll(f.getArcsIndirects());

			for (Arc a : lA) {

				if (a.getOrientation() == SpecificCadastralBoundary.ROAD) {
					continue boucleFace;
				}

			}

			bouclarc: for (Arc a : lA) {

				List<Arc> lATemp = new ArrayList<Arc>();

				lATemp.addAll(a.getNoeudIni().getSortants());
				lATemp.addAll(a.getNoeudFin().getSortants());
				lATemp.addAll(a.getNoeudIni().getEntrants());
				lATemp.addAll(a.getNoeudFin().getEntrants());

				for (Arc aTemp : lATemp) {

					if (aTemp.getOrientation() == SpecificCadastralBoundary.ROAD) {
						continue bouclarc;
					}

				}

				a.setOrientation(SpecificCadastralBoundary.BOT);

			}

		}

		// IFeatureCollection<Bordure> bordures = new
		// FT_FeatureCollection<Bordure>();
		// List<Arc> arcsTreated = new ArrayList<Arc>();
		IFeatureCollection<CadastralParcel> parcelles = new FT_FeatureCollection<CadastralParcel>();

		// Toutes les arretes sont supposées être affectées à un type
		for (Face f : facesParcelles) {

			IMultiSurface<IOrientableSurface> ms = FromGeomToSurface
					.convertMSGeom(f.getGeom());

			// On a la parcelle
			CadastralParcel p = new CadastralParcel(ms);
			parcelles.add(p);

			List<Arc> lArcs = new ArrayList<Arc>();
			lArcs.addAll(f.getArcsDirects());
			lArcs.addAll(f.getArcsIndirects());

			for (Arc a : lArcs) {

				SpecificCadastralBoundary b = new SpecificCadastralBoundary(
						a.getGeom());
				b.setType(a.getOrientation());

				p.getSpecificCadastralBoundary().add(b);

			}

		}

		return parcelles;

	}

	public static CarteTopo newCarteTopo(String name,
			IFeatureCollection<? extends IFeature> collection, double threshold) {

		try {
			// Initialisation d'une nouvelle CarteTopo
			CarteTopo carteTopo = new CarteTopo(name);
			carteTopo.setBuildInfiniteFace(false);
			// Récupération des arcs de la carteTopo
			IPopulation<Arc> arcs = carteTopo.getPopArcs();
			// Import des arcs de la collection dans la carteTopo
			for (IFeature feature : collection) {

				List<ILineString> lLLS = FromPolygonToLineString
						.convertPolToLineStrings((IPolygon) FromGeomToSurface
								.convertGeom(feature.getGeom()).get(0));

				for (ILineString ls : lLLS) {

					if (ls.length() == 0) {
						System.out.println("PROOOOOOOOOOOO");
					}

					// création d'un nouvel élément
					Arc arc = arcs.nouvelElement();
					// affectation de la géométrie de l'objet issu de la
					// collection
					// à l'arc de la carteTopo
					arc.setGeometrie(ls);
					// instanciation de la relation entre l'arc créé et l'objet
					// issu de la collection
					arc.addCorrespondant(feature);

				}

			}

			if (!test(carteTopo)) {
				System.out.println("Error 1");
			}

			carteTopo.creeNoeudsManquants(-1);

			if (!test(carteTopo)) {
				System.out.println("Error 2");
			}

			carteTopo.fusionNoeuds(threshold);

			carteTopo.filtreArcsDoublons();

			// Création de la topologie Arcs Noeuds

			carteTopo.creeTopologieArcsNoeuds(threshold);
			// La carteTopo est rendue planaire

			/*
			 * if (!test(carteTopo)) { System.out.println("Error 3"); }
			 */

			carteTopo.rendPlanaire(threshold);

			/*
			 * if (!test(carteTopo)) { System.out.println("Error 4"); }
			 * carteTopo.filtreArcsDoublons(); if (!test(carteTopo)) {
			 * System.out.println("Error 5"); }
			 */

			// DEBUG2.addAll(carteTopo.getListeArcs());

			carteTopo.creeTopologieArcsNoeuds(threshold);

			/*
			 * if (!test(carteTopo)) { System.out.println("Error 6"); }
			 */

			carteTopo.creeTopologieFaces();

			// carteTopo.filtreNoeudsSimples();

			// Création des faces de la carteTopo
			// carteTopo.creeTopologieFaces();

			/*
			 * if (!test(carteTopo)) { System.out.println("Error 7"); }
			 */

			return carteTopo;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean test(CarteTopo ct) {

		for (Arc a : ct.getPopArcs()) {
			if (a.getGeometrie().length() == 0) {
				return false;
			}

		}
		return true;
	}

}
