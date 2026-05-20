package fr.ign.cogit.simplu3d.dao.geoxygene;

import fr.ign.cogit.simplu3d.dao.RoadRepository;
import fr.ign.cogit.simplu3d.io.feature.RoadReader;
import fr.ign.cogit.simplu3d.model.Road;
import org.geotools.feature.DefaultFeatureCollection;

public class RoadRepositoryGeoxygene extends AbstractRepositoryGeoxygene<Road> implements RoadRepository {
	public RoadRepositoryGeoxygene(DefaultFeatureCollection features) {
		super(features, new RoadReader());
	}
}
