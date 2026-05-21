package fr.ign.cogit.simplu3d.io.feature;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.FeatureCollection;

import java.util.Collection;

public class ParcelBoundaryReader extends AbstractFeatureReader<ParcelBoundary> {
	public ParcelBoundaryReader() {
		super(new FeatureType("ParcelBoundary"));
		this.featureType.setGeometryType("Polygon");
	}
	@Override
	public ParcelBoundary read(SimpleFeature feature) {
		return null;
	}
	@Override
	public SpatialRepository<ParcelBoundary> create(FeatureCollection<SimpleFeatureType, SimpleFeature> features) {
		return new SpatialRepository<>(readAll(features), this.featureType, (features.getSchema() != null) ? features.getSchema().getCoordinateReferenceSystem() : null);
	}
	public SpatialRepository<ParcelBoundary> create(Collection<ParcelBoundary> features, CoordinateReferenceSystem crs) {
		return new SpatialRepository<>(features, this.featureType, crs);
	}
}
