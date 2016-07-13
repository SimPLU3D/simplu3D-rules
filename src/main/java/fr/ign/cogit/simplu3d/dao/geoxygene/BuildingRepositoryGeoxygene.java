package fr.ign.cogit.simplu3d.dao.geoxygene;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.simplu3d.dao.BuildingRepository;
import fr.ign.cogit.simplu3d.io.feature.BuildingReader;
import fr.ign.cogit.simplu3d.model.Building;

public class BuildingRepositoryGeoxygene extends AbstractRepositoryGeoxygene<Building> implements BuildingRepository {

	public BuildingRepositoryGeoxygene(IFeatureCollection<IFeature> features) {
		super(features, new BuildingReader());
	}

}
