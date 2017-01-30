package fr.ign.cogit.simplu3d.dao.geoxygene;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.simplu3d.dao.UrbaZoneRepository;
import fr.ign.cogit.simplu3d.io.feature.UrbaZoneReader;
import fr.ign.cogit.simplu3d.model.UrbaZone;

public class UrbaZoneRepositoryGeoxygene extends AbstractRepositoryGeoxygene<UrbaZone> implements UrbaZoneRepository {

	public UrbaZoneRepositoryGeoxygene(IFeatureCollection<IFeature> features) {
		super(features, new UrbaZoneReader());
	}


}
