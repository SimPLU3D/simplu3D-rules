package fr.ign.cogit.simplu3d.io.feature;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.simplu3d.model.Prescription;
import fr.ign.cogit.simplu3d.model.PrescriptionType;

/**
 * 
 * Read prescription features (PRESCRIPTION_PCT, PRESCRIPTION_LIN, PRESCRIPTION_SURF)
 * 
 * @author MBorne
 *
 */
public class PrescriptionReader extends AbstractFeatureReader<Prescription> {
	public final static String ATT_TYPE = "TYPEPSC";
	public static final String ATT_LABEL = "LIBELLE";
	
	@Override
	public Prescription read(IFeature feature) {
		Prescription prescription = new Prescription();
		prescription.setId(feature.getId());

		prescription.setGeom(feature.getGeom());
		prescription.setType(readPrescriptionType(feature));
		prescription.setLabel( readStringAttribute(feature, ATT_LABEL) ) ;

		return prescription;
	}

	/**
	 * 
	 * TODO improve and rely on TYPEPSC2 in CNIG standard
	 * 
	 * @param feature
	 * @return
	 */
	protected PrescriptionType readPrescriptionType(IFeature feature){
		String type = readStringAttribute(feature, ATT_TYPE);
		if ( type == null || type.isEmpty() ){
			return PrescriptionType.UNKNOWN;
		}
		if ( type.equals("11") ){
			return PrescriptionType.RECOIL;
		}else{
			return PrescriptionType.FACADE_ALIGNMENT;
		}
	}

}
