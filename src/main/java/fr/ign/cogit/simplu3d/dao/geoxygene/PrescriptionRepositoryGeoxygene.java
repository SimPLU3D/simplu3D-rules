package fr.ign.cogit.simplu3d.dao.geoxygene;

import fr.ign.cogit.simplu3d.dao.PrescriptionRepository;
import fr.ign.cogit.simplu3d.io.feature.PrescriptionReader;
import fr.ign.cogit.simplu3d.model.Prescription;
import org.geotools.feature.DefaultFeatureCollection;

public class PrescriptionRepositoryGeoxygene extends AbstractRepositoryGeoxygene<Prescription> implements PrescriptionRepository {
	public PrescriptionRepositoryGeoxygene(DefaultFeatureCollection features) {
		super(features, new PrescriptionReader());
	}
}
