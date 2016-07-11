package fr.ign.cogit.simplu3d.dao.geoxygene;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.simplu3d.dao.UrbaDocumentRepository;
import fr.ign.cogit.simplu3d.io.geoxygene.UrbaDocumentAdapter;
import fr.ign.cogit.simplu3d.model.UrbaDocument;

public class UrbaDocumentRepositoryGeoxygene extends AbstractRepositoryGeoxygene<UrbaDocument> implements UrbaDocumentRepository {

	public UrbaDocumentRepositoryGeoxygene(IFeatureCollection<IFeature> features) {
		super(features, new UrbaDocumentAdapter());
	}


}
