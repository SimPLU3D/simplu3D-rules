package fr.ign.cogit.simplu3d.io.feature;

/*
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
*/
import fr.ign.cogit.simplu3d.model.Prescription;
import fr.ign.cogit.simplu3d.model.PrescriptionType;
import fr.ign.common.io.AttributeManager;
import org.geotools.api.feature.simple.SimpleFeature;
import org.locationtech.jts.geom.Geometry;

/**
 * 
 * Read prescription features (PRESCRIPTION_PCT, PRESCRIPTION_LIN,
 * PRESCRIPTION_SURF)
 * 
 * @author MBorne
 *
 */
public class PrescriptionReader extends AbstractFeatureReader<Prescription> {
  public final static String ATT_TYPE;// = "TYPEPSC";
  public final static String ATT_LABEL;// = "LIBELLE";
  public final static String ATT_RECOIL;
  
  static {
    ATT_TYPE = AttribNames.getATT_TYPE_PRESCRIPTION();
    ATT_RECOIL = AttribNames.getATT_RECOIL();
    ATT_LABEL = AttribNames.getATT_LABEL();
  }

  @Override
  public Prescription read(SimpleFeature feature) {
    Prescription prescription = new Prescription();
    prescription.setId(feature.getID());

    prescription.setGeometry((Geometry) feature.getDefaultGeometry());
    prescription.setType(readPrescriptionType(feature));
    prescription.setLabel(readStringAttribute(feature, ATT_LABEL));

    Object o = feature.getAttribute(ATT_RECOIL);

    if (o != null) {
      AttributeManager.addAttribute(prescription, ATT_RECOIL, Double.parseDouble(o.toString()), "Double");
    }

    return prescription;
  }

  /**
   * 
   * TODO improve and rely on TYPEPSC2 in CNIG standard
   *
   */
  protected PrescriptionType readPrescriptionType(SimpleFeature feature) {
    String type = readStringAttribute(feature, ATT_TYPE);
    if (type == null || type.isEmpty()) {
      return PrescriptionType.UNKNOWN;
    }

    int typeInt = Integer.parseInt(type);
    return PrescriptionType.getPrescriptionById(typeInt);
  }
}
