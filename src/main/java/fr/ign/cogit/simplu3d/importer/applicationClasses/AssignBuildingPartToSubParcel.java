package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.util.Iterator;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.Cut3DGeomFrom2D;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.geoxygene.sig3d.equation.PlanEquation;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.BuildingPart;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;

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
 **/
public class AssignBuildingPartToSubParcel {

	/**
	 * Aire minimale pour considérée un polygone comme attaché à une parcelle
	 */
	public static double RATIO_MIN = 0.8;

	public static int ASSIGN_METHOD = 0;

	public static void assign(IFeatureCollection<Building> buildings, IFeatureCollection<BasicPropertyUnit> collBPU) {

		for (Building b : buildings) {

			// Etape3 : on associe le bâtiment à la sous parcelles
			IFeatureCollection<IFeature> featTemp = new FT_FeatureCollection<IFeature>();

			for (BasicPropertyUnit bPU : collBPU) {

				IMultiSurface<IOrientableSurface> iMSTemp = new GM_MultiSurface<IOrientableSurface>();

				for (CadastralParcel cP : bPU.getCadastralParcel()) {
					iMSTemp.addAll(FromGeomToSurface.convertGeom(cP.getGeom()));

				}
				featTemp.add(new DefaultFeature(iMSTemp));
			}

			if (!featTemp.hasSpatialIndex()) {

				featTemp.initSpatialIndex(Tiling.class, false);

			}

			if (ASSIGN_METHOD == 0) {

				assignSimpleMethod(b, featTemp, collBPU);

			} else if (ASSIGN_METHOD == 1) {

				assignCompleteMethod(b, featTemp, collBPU);

			} else {

				System.out.println("Erreur sur le numéro de méthode");

			}

		}

	}

	private static boolean assignSimpleMethod(Building b, IFeatureCollection<IFeature> featTemp,
			IFeatureCollection<BasicPropertyUnit> collBPU) {

		// On récupère l'emprise du bâtiment
		IPolygon polyBat = (IPolygon) b.getFootprint();

		// On trouve les BPU qui l'intersecte dans la liste temporaire
		Iterator<IFeature> itSP = featTemp.select(polyBat).iterator();

		boolean isAttached = false;

		// On peut avoir plusieurs objets qui intersectent la géométrie du
		// bâtiment
		while (itSP.hasNext()) {
			IFeature sp = itSP.next();

			double aireEmprise = polyBat.area();
			IGeometry spGeom = sp.getGeom();
			IGeometry interSpPolyBat = polyBat.intersection(spGeom.buffer(0.01));

			if (!polyBat.intersects(spGeom)) {
				System.out.println("Pas d'intersection");
			}

			double area = interSpPolyBat.area();

			// double area = (polyBat.intersection(sp.getGeom())).area();

			if (area / aireEmprise > RATIO_MIN) {

				int index = featTemp.getElements().indexOf(sp);

				collBPU.get(index).getBuildings().add(b);
				collBPU.get(index).getCadastralParcel().get(0).getSubParcel().get(0).getBuildingsParts().add(b);

				isAttached = true;

			}

		}

		return isAttached;

	}

	private static boolean assignCompleteMethod(Building b, IFeatureCollection<IFeature> featTemp,
			IFeatureCollection<BasicPropertyUnit> collBPU) {

		// On récupère l'emprise du bâtiment
		IPolygon polyBat = (IPolygon) b.getFootprint();

		// On trouve les BPU qui l'intersecte dans la liste temporaire
		Iterator<IFeature> itSP = featTemp.select(polyBat).iterator();

		boolean isAttached = false;

		// On peut avoir plusieurs objets qui intersectent la géométrie du
		// bâtiment
		while (itSP.hasNext()) {

			// On traite chaque BPU (en gros parcelle) qui intersecte le
			// bâtiment
			IFeature sp = itSP.next();
			IGeometry geomSP = sp.getGeom();
			List<IOrientableSurface> listSP = FromGeomToSurface.convertGeom(geomSP);
			IPolygon polySP = null;

			for (IOrientableSurface iosSP : listSP) {
				polySP = (IPolygon) iosSP;
			}

			int index = featTemp.getElements().indexOf(sp);
			// Assignation bâtiment BDPU
			collBPU.get(index).getBuildings().add(b);

			// Quand on va assigner les sous parties, il va falloir découper le
			// bâtiment
			// System.out.println("\n \t" +
			// "----- On est ici dans AssignBuildingpartToSubParcel -----");
			// System.out.println("b : " + b.getGeom() + "\n" + "poly : " +
			// polySP);

			BuildingPart bP = null;

			if (FromPolygonToTriangle.isConvertible(FromGeomToSurface.convertGeom(b.getGeom()))) {

				List<IOrientableSurface> listCut = Cut3DGeomFrom2D.cutFeatureFromPolygon(b, (IPolygon) polySP.clone());
				bP = new BuildingPart(new GM_MultiSurface<>(listCut));

				// System.out.println(listCut);
			} else {
				bP = cutForBDTopo(b, (IPolygon) polySP.clone());
			}

			if (bP != null) {
				// Puis on assigne
				collBPU.get(index).getCadastralParcel().get(0).getSubParcel().get(0).getBuildingsParts().add(bP);
			}

			// Il faut vérifier et recompter le nombre de partie par bâtiment

			isAttached = true;

		}

		return isAttached;
	}

	private static BuildingPart cutForBDTopo(IFeature feat, IPolygon polyCut) {

		List<IOrientableSurface> lIOS = FromGeomToSurface.convertGeom(feat.getGeom());

		IMultiSurface<IOrientableSurface> polVert = Util.detectVertical(lIOS, 0.2);

		IMultiSurface<IOrientableSurface> polHorz = Util.detectNonVertical(lIOS, 0.2);

		List<IOrientableSurface> lOS = Cut3DGeomFrom2D.cutListSurfaceFromPolygon(polVert.getList(), polyCut);

		for (IOrientableSurface os : polHorz) {
			PlanEquation pEq = new PlanEquation(os);

			if (pEq.getNormale().getZ() < 0) {

				IGeometry inter = os.intersection((IGeometry) polyCut.clone());

				if (inter.area() < 3) {
					return null;
				}

			//	Box3D b3D = new Box3D(os);

				// inter = Extrusion2DObject.convertFromGeometry(inter,
				// b3D.getLLDP().getZ(), b3D.getLLDP().getZ());

				lOS.addAll(FromGeomToSurface.convertGeom(inter));

			}

		}

		return new BuildingPart(new GM_MultiSurface(lOS));
	}

}
