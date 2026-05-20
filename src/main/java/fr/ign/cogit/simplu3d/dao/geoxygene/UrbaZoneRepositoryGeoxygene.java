package fr.ign.cogit.simplu3d.dao.geoxygene;

import fr.ign.cogit.simplu3d.dao.UrbaZoneRepository;
import fr.ign.cogit.simplu3d.io.feature.UrbaZoneReader;
import fr.ign.cogit.simplu3d.model.UrbaZone;
import org.geotools.feature.DefaultFeatureCollection;

public class UrbaZoneRepositoryGeoxygene extends AbstractRepositoryGeoxygene<UrbaZone> implements UrbaZoneRepository {
	public UrbaZoneRepositoryGeoxygene(DefaultFeatureCollection features) {
		super(features, new UrbaZoneReader());
	}
}
