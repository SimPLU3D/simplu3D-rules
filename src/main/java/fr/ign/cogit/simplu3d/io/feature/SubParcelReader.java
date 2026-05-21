package fr.ign.cogit.simplu3d.io.feature;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.model.SubParcel;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.FeatureCollection;

import java.util.Collection;

public class SubParcelReader extends AbstractFeatureReader<SubParcel> {
	public SubParcelReader() {
		super(new FeatureType("SubParcel"));
		this.featureType.setGeometryType("Polygon");
	}
	@Override
	public SubParcel read(SimpleFeature feature) {
		return null;
	}
	@Override
	public SpatialRepository<SubParcel> create(FeatureCollection<SimpleFeatureType, SimpleFeature> features) {
		return new SpatialRepository<>(readAll(features), this.featureType, (features.getSchema() != null) ? features.getSchema().getCoordinateReferenceSystem() : null);
	}
	public SpatialRepository<SubParcel> create(Collection<SubParcel> features, CoordinateReferenceSystem crs) {
		return new SpatialRepository<>(features, this.featureType, crs);
	}
}
