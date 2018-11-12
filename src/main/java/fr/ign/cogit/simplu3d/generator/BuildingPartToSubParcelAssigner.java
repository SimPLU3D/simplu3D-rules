package fr.ign.cogit.simplu3d.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.calculation.Cut3DGeomFrom2D;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.geoxygene.sig3d.equation.PlanEquation;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.BuildingPart;
import fr.ign.cogit.simplu3d.model.SubParcel;

/**
 * 
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class BuildingPartToSubParcelAssigner {

	private boolean cutBuildingOnSubParcels = false;

	public boolean isCutBuildingOnSubParcels() {
		return cutBuildingOnSubParcels;
	}

	public void setCutBuildingOnSubParcels(boolean cutBuildingOnSubParcels) {
		this.cutBuildingOnSubParcels = cutBuildingOnSubParcels;
	}

	/**
	 * Create a link between buildings and the different subparcels. Building part
	 * may be created according to the method for buildings that lay one more than
	 * one subparcel
	 * 
	 * @param building a building to assign to sub parcels
	 */
	public void assignBuildingToSubParcels(Building building) {
		if (cutBuildingOnSubParcels) {
			Collection<SubParcel> subParcels = building.getbPU().getSubParcels();
			assert !subParcels.isEmpty();
			if (subParcels.size() == 1) {
				return;
			}
			assignCompleteMethod(building, subParcels);
		} else {
			BuildingPart buildingPart = building.getBuildingParts().get(0);
			// TODO find best SubParcel
			if (building.getbPU() == null) {
				return;
			}

			SubParcel subParcel = building.getbPU().getSubParcels().get(0);
			buildingPart.setSubParcel(subParcel);
			subParcel.getBuildingsParts().add(buildingPart);
		}
	}

	private void assignCompleteMethod(Building building, Collection<SubParcel> subParcels) {
		List<BuildingPart> buildingParts = new ArrayList<>();

		// On récupère l'emprise du bâtiment
		IPolygon polyBat = (IPolygon) building.getFootprint();

		// On peut avoir plusieurs objets qui intersectent la géométrie du
		// bâtiment
		for (SubParcel subParcel : subParcels) {
			IPolygon polySP = (IPolygon) subParcel.getGeom();

			if (!polySP.intersects(polyBat)) {
				continue;
			}

			BuildingPart bP = null;

			// 2 cas : le bâtiment est triangulé, si c'est le cas on appelle une
			// première méthode sinon
			// on en appelle une autre moins fiable
			if (FromPolygonToTriangle.isConvertible(FromGeomToSurface.convertGeom(building.getGeom()))) {
				List<IOrientableSurface> listCut = Cut3DGeomFrom2D.cutFeatureFromPolygon(building,
						(IPolygon) polySP.clone());
				bP = new BuildingPart(new GM_MultiSurface<>(listCut));
			} else {
				bP = cutNonTriangulatedFeature(building, (IPolygon) polySP.clone());
			}

			if (bP != null) {
				subParcel.getBuildingsParts().add(bP);
				bP.setSubParcel(subParcel);
				buildingParts.add(bP);
			}

		}

		// replace existing building parts
		building.setBuildingParts(buildingParts);
	}

	/**
	 * Découpe un multisurface 3D non triangulé par rapport à un polygone 2D
	 * 
	 * @param feat
	 * @param polyCut
	 * @return
	 */
	private static BuildingPart cutNonTriangulatedFeature(IFeature feat, IPolygon polyCut) {
		// On récupère la liste des faces de la multisurface
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

				lOS.addAll(FromGeomToSurface.convertGeom(inter));

			}

		}

		return new BuildingPart(new GM_MultiSurface<IOrientableSurface>(lOS));
	}

}
