package fr.ign.cogit.simplu3d.generator;

import java.util.ArrayList;
import java.util.Collection;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.UrbaZone;

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
	private Collection<UrbaZone> urbaZones;

	// removed : not the place to fix topology (snap geometries to each other)
	// private double minArea;

	/**
	 * 
	 * @param cadastralParcels
	 */
	public SubParcelGenerator(Collection<UrbaZone> urbaZones) {
		this.urbaZones = urbaZones;
	}

	/**
	 * Create SubParcels for an UrbaZone
	 * 
	 * @param urbaZone
	 * @return
	 */
	public Collection<SubParcel> createSubParcels(CadastralParcel cadastralParcel) {

		Collection<SubParcel> subParcels = new ArrayList<>();

		/*
		 * create SubParcel as the intersection with UrbaZone
		 */
		for (UrbaZone urbaZone : urbaZones) {
			IGeometry intersection = urbaZone.getGeom().intersection(cadastralParcel.getGeom());
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
		IGeometry union = unionAll(subParcels);
		if (union == null || union.isEmpty()) {
			IGeometry nonSharedGeometry = (IGeometry) cadastralParcel.getGeom().clone();
			SubParcel subParcelWithoutZone = createSubParcel(nonSharedGeometry);

			if (subParcelWithoutZone != null) {
				// CadastralParcel / SubParcel
				subParcelWithoutZone.setCadastralParcel(cadastralParcel);
				cadastralParcel.getSubParcels().add(subParcelWithoutZone);
				subParcels.add(subParcelWithoutZone);
			}

		} else if (!(Math.abs(union.area() - cadastralParcel.getGeom().area()) < 0.001
				* cadastralParcel.getGeom().area())) {
			// part(s) of CadastralParcel belong to UrbaZone
			IGeometry nonSharedGeometry = cadastralParcel.getGeom().difference(union);
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

	private IGeometry unionAll(Collection<SubParcel> subParcels) {
		IGeometry result = null;
		for (SubParcel subParcel : subParcels) {
			if (result == null) {
				result = subParcel.getGeom();
			} else {
				result = result.union(subParcel.getGeom());
			}
		}
		return result;
	}

	/**
	 * Create SubParcel from intersection's geometry
	 * 
	 * @param intersection
	 * @return
	 */
	private SubParcel createSubParcel(IGeometry intersection) {
		SubParcel subParcel = new SubParcel();

		IMultiSurface<IOrientableSurface> iMS = FromGeomToSurface.convertMSGeom(intersection);

		// TODO check this code (create a copy would be better?)
		int nbContrib = iMS.size();
		for (int i = 0; i < nbContrib; i++) {
			IOrientableSurface os = iMS.get(i);

			if (os == null || os.isEmpty()) {
				iMS.remove(i);
				i--;
				nbContrib--;
			}
		}

		if (iMS == null || iMS.isEmpty()) {
			return null;
		}
		subParcel.setGeom(iMS);

		return subParcel;
	}
}
