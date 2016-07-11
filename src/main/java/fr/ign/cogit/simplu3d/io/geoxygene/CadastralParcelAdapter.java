package fr.ign.cogit.simplu3d.io.geoxygene;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.simplu3d.model.CadastralParcel;

/**
 * 
 * CadastralParcel reader
 * 
 * @author MBorne
 *
 */
public class CadastralParcelAdapter extends AbstractFeatureAdapter<CadastralParcel>{
	/**
	 * TODO change to IDU (more universal than "NUMERO" from IGN's BDParcellaire)
	 */
	public static final String ATT_ID_PARC = "NUMERO";

	public static final String ATT_HAS_TO_BE_SIMULATED = "simul";
	
	@Override
	public CadastralParcel read(IFeature feature) {
		CadastralParcel cadastralParcel = new CadastralParcel();
		cadastralParcel.setId( readIntegerAttribute(feature, ATT_ID_PARC) ) ;

		Object o = findAttribute(feature, ATT_HAS_TO_BE_SIMULATED);
		if (o != null) {
			cadastralParcel.setHasToBeSimulated(1 == Integer.parseInt(o.toString()));
		}

		cadastralParcel.setGeom(feature.getGeom());
		return cadastralParcel;
	}

}
