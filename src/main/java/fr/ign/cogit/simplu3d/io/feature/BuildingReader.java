package fr.ign.cogit.simplu3d.io.feature;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.simplu3d.model.Building;

public class BuildingReader extends AbstractFeatureReader<Building> {

	@Override
	public Building read(IFeature feature) {
		// TODO check construction
		Building building = new Building(feature.getGeom());
		return building;
	}

	
}
