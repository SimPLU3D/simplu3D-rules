package fr.ign.cogit.simplu3d.reader;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.simplu3d.model.CadastralParcel;

/**
 * 
 * CadastralParcel reader
 * 
 * @author MBorne
 *
 */
public class CadastralParcelReader extends AbstractReader<CadastralParcel>{
	/**
	 * TODO change to IDU (more universal than "NUMERO" from IGN's BDParcellaire)
	 */
	private static final String ATT_ID_PARC = "NUMERO";
	
	@Override
	public CadastralParcel read(IFeature feature) {
		CadastralParcel cadastralParcel = new CadastralParcel();
		//TODO allow string identifier
		cadastralParcel.setId( readIntegerAttribute(feature, ATT_ID_PARC) ) ;
		cadastralParcel.setGeom(feature.getGeom());
		return cadastralParcel;
	}


}
