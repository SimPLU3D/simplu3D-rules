package fr.ign.cogit.simplu3d.io.feature;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.FeatureCollection;

import java.util.Collection;

public class BasicPropertyUnitReader extends AbstractFeatureReader<BasicPropertyUnit> {
	public BasicPropertyUnitReader() {
		super(new FeatureType("BasicPropertyUnit"));
		this.featureType.setGeometryType("Polygon");
	}
	@Override
	public BasicPropertyUnit read(SimpleFeature feature) {
		return null;
	}
	@Override
	public SpatialRepository<BasicPropertyUnit> create(FeatureCollection<SimpleFeatureType, SimpleFeature> features) {
		return new SpatialRepository<>(readAll(features), this.featureType, (features.getSchema() != null) ? features.getSchema().getCoordinateReferenceSystem() : null);
	}
	public SpatialRepository<BasicPropertyUnit> create(Collection<BasicPropertyUnit> features, CoordinateReferenceSystem crs) {
		return new SpatialRepository<>(features, this.featureType, crs);
	}
}
