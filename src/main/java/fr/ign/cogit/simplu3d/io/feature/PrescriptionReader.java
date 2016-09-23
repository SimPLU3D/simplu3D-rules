package fr.ign.cogit.simplu3d.io.feature;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.simplu3d.model.Prescription;
import fr.ign.cogit.simplu3d.model.PrescriptionType;

/**
 * 
 * Read prescription features (PRESCRIPTION_PCT, PRESCRIPTION_LIN,
 * PRESCRIPTION_SURF)
 * 
 * @author MBorne
 *
 */
public class PrescriptionReader extends AbstractFeatureReader<Prescription> {
	public final static String ATT_TYPE = "TYPEPSC";
	public static final String ATT_LABEL = "LIBELLE";

	/**
	 * Valeur de l'attribut de recul s'il existe
	 */
	public final static String ATT_RECOIL = "RECUL";

	@Override
	public Prescription read(IFeature feature) {
		Prescription prescription = new Prescription();
		prescription.setId(feature.getId());

		prescription.setGeom(feature.getGeom());
		prescription.setType(readPrescriptionType(feature));
		prescription.setLabel(readStringAttribute(feature, ATT_LABEL));

		Object o = prescription.getAttribute(ATT_RECOIL);

		if (o != null) {
			AttributeManager.addAttribute(feature, ATT_RECOIL, Double.parseDouble(o.toString()), "Double");
		}

		return prescription;
	}

	/**
	 * 
	 * TODO improve and rely on TYPEPSC2 in CNIG standard
	 * 
	 * @param feature
	 * @return
	 */
	protected PrescriptionType readPrescriptionType(IFeature feature) {
		String type = readStringAttribute(feature, ATT_TYPE);
		if (type == null || type.isEmpty()) {
			return PrescriptionType.UNKNOWN;
		}

		int typeInt = Integer.parseInt(type);
		return PrescriptionType.getPrescriptionById(typeInt);
	}

}
