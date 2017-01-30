package fr.ign.cogit.simplu3d.dao.geoxygene;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.simplu3d.dao.PrescriptionRepository;
import fr.ign.cogit.simplu3d.io.feature.PrescriptionReader;
import fr.ign.cogit.simplu3d.model.Prescription;

public class PrescriptionRepositoryGeoxygene extends AbstractRepositoryGeoxygene<Prescription> implements PrescriptionRepository {

	public PrescriptionRepositoryGeoxygene(IFeatureCollection<IFeature> features) {
		super(features, new PrescriptionReader());
	}

}
