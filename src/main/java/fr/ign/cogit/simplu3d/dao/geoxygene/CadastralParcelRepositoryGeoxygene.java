package fr.ign.cogit.simplu3d.dao.geoxygene;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.simplu3d.dao.CadastralParcelRepository;
import fr.ign.cogit.simplu3d.io.geoxygene.CadastralParcelAdapter;
import fr.ign.cogit.simplu3d.model.CadastralParcel;

public class CadastralParcelRepositoryGeoxygene extends AbstractRepositoryGeoxygene<CadastralParcel> implements CadastralParcelRepository {

	public CadastralParcelRepositoryGeoxygene(IFeatureCollection<IFeature> features) {
		super(features, new CadastralParcelAdapter());
	}


}
