package fr.ign.cogit.simplu3d.dao.geoxygene;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.simplu3d.dao.RoadRepository;
import fr.ign.cogit.simplu3d.io.feature.RoadReader;
import fr.ign.cogit.simplu3d.model.Road;

public class RoadRepositoryGeoxygene extends AbstractRepositoryGeoxygene<Road> implements RoadRepository {

	public RoadRepositoryGeoxygene(IFeatureCollection<IFeature> features) {
		super(features, new RoadReader());
	}

}
