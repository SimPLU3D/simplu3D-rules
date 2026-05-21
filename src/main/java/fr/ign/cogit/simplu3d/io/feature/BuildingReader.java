package fr.ign.cogit.simplu3d.io.feature;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.BuildingPart;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.FeatureCollection;
import org.locationtech.jts.geom.Geometry;

public class BuildingReader extends AbstractFeatureReader<Building> {
	public BuildingReader() {
		super(new FeatureType("Building"));
		this.featureType.setGeometryType("MultiPolygon");
	}
	@Override
	public Building read(SimpleFeature feature) {
		// TODO check construction
		Geometry geometry = (Geometry) feature.getDefaultGeometry();
		Building building = new Building();
		building.setGeometry(geometry);
		BuildingPart buildingParts = new BuildingPart();
		buildingParts.setGeometry(geometry);
		building.getBuildingParts().add(buildingParts);
		return building;
	}
	@Override
	public SpatialRepository<Building> create(FeatureCollection<SimpleFeatureType, SimpleFeature> features) {
		return new SpatialRepository<>(readAll(features), this.featureType, (features.getSchema() != null) ? features.getSchema().getCoordinateReferenceSystem() : null);
	}
}
