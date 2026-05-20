package fr.ign.cogit.simplu3d.dao.geoxygene;

import fr.ign.cogit.simplu3d.dao.CadastralParcelRepository;
import fr.ign.cogit.simplu3d.io.feature.CadastralParcelReader;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import org.geotools.feature.DefaultFeatureCollection;

public class CadastralParcelRepositoryGeoxygene extends AbstractRepositoryGeoxygene<CadastralParcel> implements CadastralParcelRepository {
	public CadastralParcelRepositoryGeoxygene(DefaultFeatureCollection features) {
		super(features, new CadastralParcelReader());
	}
}
