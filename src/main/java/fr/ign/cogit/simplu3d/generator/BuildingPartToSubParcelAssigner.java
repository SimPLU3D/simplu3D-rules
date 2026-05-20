package fr.ign.cogit.simplu3d.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.BuildingPart;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import fr.ign.common.analysis.Cut3DGeomFrom2D;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.conversion.FromPolygonToTriangle;
import fr.ign.common.equation.PlanEquation;
import fr.ign.common.util.GeomUtils;
import fr.ign.common.util.Util;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

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
		Polygon polyBat = building.getFootprint();

		// On peut avoir plusieurs objets qui intersectent la géométrie du
		// bâtiment
		for (SubParcel subParcel : subParcels) {
			Polygon polySP = (Polygon) subParcel.getGeometry();
			if (!polySP.intersects(polyBat)) {
				continue;
			}
			BuildingPart bP = null;
			// 2 cas : le bâtiment est triangulé, si c'est le cas on appelle une
			// première méthode sinon
			// on en appelle une autre moins fiable
			if (FromPolygonToTriangle.isConvertible(FromGeomToSurface.convertGeom(building.getGeometry()))) {
				List<Polygon> listCut = Cut3DGeomFrom2D.cutFeatureFromPolygon(building, (Polygon) polySP.copy());
				bP = new BuildingPart();
				bP.setGeometry(new GeometryFactory().createMultiPolygon(listCut.toArray(new Polygon[0])));
			} else {
				bP = cutNonTriangulatedFeature(building, (Polygon) polySP.copy());
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
	 * Découpe un multisurface 3D non triangulé par rapport à un polygone 2D.
	 */
	private static BuildingPart cutNonTriangulatedFeature(UrbanFeature feat, Polygon polyCut) {
		// On récupère la liste des faces de la multisurface
		List<Polygon> lIOS = FromGeomToSurface.convertGeom(feat.getGeometry());

		MultiPolygon polVert = Util.detectVertical(lIOS, 0.2);

		MultiPolygon polHorz = Util.detectNonVertical(lIOS, 0.2);

        assert polVert != null;
        List<Polygon> lOS = Cut3DGeomFrom2D.cutListSurfaceFromPolygon(GeomUtils.getPolygons(polVert), polyCut);

        assert polHorz != null;
        for (Polygon os : GeomUtils.getPolygons(polHorz)) {
			PlanEquation pEq = new PlanEquation(os);
			if (pEq.getNormale().getZ() < 0) {
				Geometry inter = os.intersection(polyCut.copy());
				if (inter.getArea() < 3) {
					return null;
				}
				lOS.addAll(FromGeomToSurface.convertGeom(inter));
			}
		}
		BuildingPart bp = new BuildingPart();
		bp.setGeometry(polyCut.getFactory().createMultiPolygon(lOS.toArray(new Polygon[0])));
		return bp;
	}
}
