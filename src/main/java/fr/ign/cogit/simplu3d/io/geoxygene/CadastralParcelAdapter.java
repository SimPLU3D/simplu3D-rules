package fr.ign.cogit.simplu3d.io.geoxygene;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.simplu3d.model.CadastralParcel;

/**
 * 
 * CadastralParcel reader
 * 
 * TODO cleanup "code" management. Currently either "CODE" or CODE_DEP+CODE_COM+COM_ABS+SECTION+NUMERO
 * 
 * @author MBorne
 *
 */
public class CadastralParcelAdapter extends AbstractFeatureAdapter<CadastralParcel>{
	public static final String ATT_CODE_PARC = "CODE";
	
	public static final String ATT_BDP_CODE_DEP = "CODE_DEP";
	public static final String ATT_BDP_CODE_COM = "CODE_COM";
	public static final String ATT_BDP_COM_ABS = "COM_ABS";	
	public static final String ATT_BDP_SECTION = "SECTION";	
	public static final String ATT_BDP_NUMERO = "NUMERO";	

	public static final String ATT_HAS_TO_BE_SIMULATED = "SIMUL";
	
	@Override
	public CadastralParcel read(IFeature feature) {
		CadastralParcel cadastralParcel = new CadastralParcel();
		cadastralParcel.setId(feature.getId());

		/*
		 * read code attribute
		 */
		String code = readStringAttribute(feature, ATT_CODE_PARC);
		if ( code == null ){
			String codeDep = readStringAttribute(feature, ATT_BDP_CODE_DEP);
			String codeCom = readStringAttribute(feature, ATT_BDP_CODE_COM);
			String comAbs  = readStringAttribute(feature, ATT_BDP_COM_ABS);
			String section = readStringAttribute(feature, ATT_BDP_SECTION);
			String numero = readStringAttribute(feature, ATT_BDP_NUMERO);
			// complete BDP
			if ( codeDep != null && codeCom != null && comAbs != null && section != null && numero != null ){
				code = codeDep+codeCom+comAbs+section+numero;
			}
		}
		cadastralParcel.setCode(code);
		
		/*
		 * read simulation attribute
		 */
		Object o = findAttribute(feature, ATT_HAS_TO_BE_SIMULATED);
		if (o != null) {
			cadastralParcel.setHasToBeSimulated(1 == Integer.parseInt(o.toString()));
		}

		cadastralParcel.setGeom(feature.getGeom());
		return cadastralParcel;
	}
	

}
