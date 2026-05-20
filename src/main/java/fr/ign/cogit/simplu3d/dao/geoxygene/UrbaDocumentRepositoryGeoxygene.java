package fr.ign.cogit.simplu3d.dao.geoxygene;

import fr.ign.cogit.simplu3d.dao.UrbaDocumentRepository;
import fr.ign.cogit.simplu3d.io.feature.UrbaDocumentReader;
import fr.ign.cogit.simplu3d.model.UrbaDocument;
import org.geotools.feature.DefaultFeatureCollection;

public class UrbaDocumentRepositoryGeoxygene extends AbstractRepositoryGeoxygene<UrbaDocument> implements UrbaDocumentRepository {

	public UrbaDocumentRepositoryGeoxygene(DefaultFeatureCollection features) {
		super(features, new UrbaDocumentReader());
	}
}
