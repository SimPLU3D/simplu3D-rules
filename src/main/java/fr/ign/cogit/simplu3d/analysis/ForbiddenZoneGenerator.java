package fr.ign.cogit.simplu3d.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Prescription;
import fr.ign.cogit.simplu3d.model.PrescriptionType;

public class ForbiddenZoneGenerator {

	private double radiusOfInfluence = 20;

	public IGeometry generateUnionGeometry(IFeatureCollection<Prescription> prescriptions, BasicPropertyUnit bPU) {

		if (!prescriptions.hasSpatialIndex()) {
			prescriptions.initSpatialIndex(Tiling.class, false);
		}

		Collection<Prescription> selected = prescriptions.select(bPU.getGeom().buffer(radiusOfInfluence));

		List<IGeometry> geometries = new ArrayList<IGeometry>();

		for (Prescription pres : selected) {
			IGeometry geom = generateWithCheckedGeom(pres, bPU);

			if (geom != null && !geom.isEmpty()) {
				geometries.add(geom);
			}

		}

		if (geometries.isEmpty())
			return null;

		IGeometry geom = bPU.getGeom();

		for (IGeometry geomTemp : geometries) {
			geom = geom.difference(geomTemp);
		}
		return geom;
	}

	public IGeometry generate(Prescription p, BasicPropertyUnit bPU) {
		if (!bPU.getGeom().intersects(p.getGeom()))
			return null;

		return generateWithCheckedGeom(p, bPU);
	}

	/**
	 * @TODO
	 * @param p
	 * @param bPU
	 * @return
	 */
	private IGeometry generateWithCheckedGeom(Prescription p, BasicPropertyUnit bPU) {
		PrescriptionType type = p.getType();

		switch (type) {
		case ELEMENT_PAYSAGE:
			break;
		case EMPLACEMENT_RESERVE:
			break;
		case ESPACE_BOISE:
			break;
		case FACADE_ALIGNMENT:
			break;
		case LIMITATION_BRUIT:
			break;
		case RECOIL:
			break;
		case SECTEUR_MIXITE:
			break;
		case UNKNOWN:
			break;
		default:
			break;

		}
		return null;
	}
}
