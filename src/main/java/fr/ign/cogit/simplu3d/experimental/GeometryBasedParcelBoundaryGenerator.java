package fr.ign.cogit.simplu3d.experimental;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import fr.ign.cogit.simplu3d.dao.CadastralParcelRepository;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;

/**
 * 
 * Create SpecificCadastralBoundary studying geometry sharing between adjacent CadastralParcels
 * 
 * @author MBorne
 *
 */
public class GeometryBasedParcelBoundaryGenerator {

	private CadastralParcelRepository parcelRepository;

	public GeometryBasedParcelBoundaryGenerator(CadastralParcelRepository parcelRepository) {
		this.parcelRepository = parcelRepository;
	}

	/**
	 * 
	 * Create boundaries for a given parcel
	 * 
	 * @param cadastralParcel
	 * @return
	 * @throws IOException
	 */
	public Collection<SpecificCadastralBoundary> createParcelBoundaries(CadastralParcel cadastralParcel) {
		Geometry completeBoundary = cadastralParcel.getGeometry().getBoundary();
		Geometry nonSharedBoundaries = completeBoundary;

		Collection<SpecificCadastralBoundary> cadastralBoundaries = new ArrayList<SpecificCadastralBoundary>();
		for (CadastralParcel neighbor : parcelRepository.findByEnvelope(cadastralParcel.getGeometry().getEnvelopeInternal())) {
			// TODO ensure same instance and compare instances
			if (neighbor.getId() == cadastralParcel.getId()) {
				continue;
			}
			Geometry neighborBoundary = neighbor.getGeometry().getBoundary();
			Geometry commonGeometry = completeBoundary.intersection(neighborBoundary);
			// filter empty geometries and points
			if (!commonGeometry.isEmpty() && (commonGeometry.getDimension() != 0)) {
				nonSharedBoundaries = nonSharedBoundaries.difference(commonGeometry);
				cadastralBoundaries.addAll(createBoundariesFromGeometry(commonGeometry));
			}
		}

		cadastralBoundaries.addAll(createBoundariesFromGeometry(nonSharedBoundaries));
		return cadastralBoundaries;
	}

	/**
	 * Convert a LineString or MultiLineString to a set of boundaries
	 * @param geometry
	 * @return
	 */
	private List<SpecificCadastralBoundary> createBoundariesFromGeometry(Geometry geometry) {
		List<SpecificCadastralBoundary> result = new ArrayList<>();
		if (geometry.isEmpty()) {
			return result;
		}
		if (geometry instanceof LineString) {
			SpecificCadastralBoundary cadastralBoundary = new SpecificCadastralBoundary();
			cadastralBoundary.setGeometry((LineString) geometry);
			result.add(cadastralBoundary);
		} else if (geometry instanceof MultiLineString) {
			MultiLineString multiLineString = (MultiLineString) geometry;
			for (int i = 0; i < multiLineString.getNumGeometries(); i++) {
				SpecificCadastralBoundary cadastralBoundary = new SpecificCadastralBoundary();
				cadastralBoundary.setGeometry((LineString) multiLineString.getGeometryN(i));
				result.add(cadastralBoundary);
			}
		} else {
			throw new RuntimeException("Unexpected geometry type : " + geometry.getGeometryType());
		}
		return result;
	}

}
