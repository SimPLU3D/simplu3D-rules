package fr.ign.cogit.simplu3d.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.UrbaZone;
import fr.ign.common.conversion.FromGeomToSurface;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

/**
 * 
 * Generate SubParcels as an intersection of CadastralParcel and UrbaZone
 * 
 * @author MBorne
 *
 */
public class SubParcelGenerator {
	/**
	 * 
	 */
	private final Collection<UrbaZone> urbaZones;
	// removed : not the place to fix topology (snap geometries to each other)
	// private double minArea;
	/**
	 * 
	 * @param urbaZones the regulation urban zone used to split parcels
	 */
	public SubParcelGenerator(Collection<UrbaZone> urbaZones) {
		this.urbaZones = urbaZones;
	}

	/**
	 * Create SubParcels for an UrbaZone
	 * 
	 * @param cadastralParcel the cadastral parcels used to create sub parcels
	 * @return a collection of SubParcel
	 */
	public Collection<SubParcel> createSubParcels(CadastralParcel cadastralParcel) {

		Collection<SubParcel> subParcels = new ArrayList<>();

		/*
		 * create SubParcel as the intersection with UrbaZone
		 */
		for (UrbaZone urbaZone : urbaZones) {
			Geometry intersection = urbaZone.getGeometry().intersection(cadastralParcel.getGeometry());
			if (intersection == null || intersection.isEmpty()) {
				continue;
			}
			SubParcel subParcel = createSubParcel(intersection);
			if (subParcel == null) {
				continue;
			}
			// CadastralParcel / SubParcel
			subParcel.setCadastralParcel(cadastralParcel);
			cadastralParcel.getSubParcels().add(subParcel);
			// UrbaZone / SubParcel
			subParcel.setZoneUrba(urbaZone);
			urbaZone.getSubParcels().add(subParcel);
			subParcels.add(subParcel);
		}
		/*
		 * Compute SubParcel's union to create a SubParcel with a null UrbaZone to
		 * ensure that the union of SubParcels is equal to CadastralParcel
		 */
		Geometry union = unionAll(subParcels);
		if (union == null || union.isEmpty()) {
			Geometry nonSharedGeometry = cadastralParcel.getGeometry().copy();
			SubParcel subParcelWithoutZone = createSubParcel(nonSharedGeometry);
			if (subParcelWithoutZone != null) {
				// CadastralParcel / SubParcel
				subParcelWithoutZone.setCadastralParcel(cadastralParcel);
				cadastralParcel.getSubParcels().add(subParcelWithoutZone);
				subParcels.add(subParcelWithoutZone);
			}

		} else if (!(Math.abs(union.getArea() - cadastralParcel.getGeometry().getArea()) < 0.001
				* cadastralParcel.getGeometry().getArea())) {
			// part(s) of CadastralParcel belong to UrbaZone
			Geometry nonSharedGeometry = cadastralParcel.getGeometry().difference(union);
			SubParcel subParcelWithoutZone = createSubParcel(nonSharedGeometry);
			if (subParcelWithoutZone != null) {
				// CadastralParcel / SubParcel
				subParcelWithoutZone.setCadastralParcel(cadastralParcel);
				cadastralParcel.getSubParcels().add(subParcelWithoutZone);
				subParcels.add(subParcelWithoutZone);
			}
		}
		return subParcels;
	}

	private Geometry unionAll(Collection<SubParcel> subParcels) {
		Geometry result = null;
		for (SubParcel subParcel : subParcels) {
			if (result == null) {
				result = subParcel.getGeometry();
			} else {
				result = result.union(subParcel.getGeometry());
			}
		}
		return result;
	}

	/**
	 * Create SubParcel from intersection's geometry
	 *
	 */
	private SubParcel createSubParcel(Geometry intersection) {
		SubParcel subParcel = new SubParcel();
		MultiPolygon iMS = FromGeomToSurface.convertMSGeom(intersection);
		// TODO check this code: we now create a copy
		List<Polygon> polygons = new ArrayList<>();
		for (int i = 0; i < iMS.getNumGeometries(); i++) {
			Polygon os = (Polygon) iMS.getGeometryN(i);
			if (os != null && !os.isEmpty()) {
				polygons.add(os);
			}
		}
		MultiPolygon newGeometry = iMS.getFactory().createMultiPolygon(polygons.toArray(new Polygon[0]));
		if (newGeometry == null || iMS.isEmpty()) {
			return null;
		}
		subParcel.setGeometry(newGeometry);
		return subParcel;
	}
}
