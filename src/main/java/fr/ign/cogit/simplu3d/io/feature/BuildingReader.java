package fr.ign.cogit.simplu3d.io.feature;

import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.BuildingPart;
import org.geotools.api.feature.simple.SimpleFeature;
import org.locationtech.jts.geom.Geometry;

public class BuildingReader extends AbstractFeatureReader<Building> {

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
}
