package fr.ign.cogit.simplu3d.dao.geoxygene;

import fr.ign.cogit.simplu3d.dao.BuildingRepository;
import fr.ign.cogit.simplu3d.io.feature.BuildingReader;
import fr.ign.cogit.simplu3d.model.Building;
import org.geotools.feature.DefaultFeatureCollection;

public class BuildingRepositoryGeoxygene extends AbstractRepositoryGeoxygene<Building> implements BuildingRepository {
	public BuildingRepositoryGeoxygene(DefaultFeatureCollection features) {
		super(features, new BuildingReader());
	}
}
