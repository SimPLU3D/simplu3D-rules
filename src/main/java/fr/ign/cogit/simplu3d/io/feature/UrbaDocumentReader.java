package fr.ign.cogit.simplu3d.io.feature;

import java.text.SimpleDateFormat;

import fr.ign.cogit.simplu3d.dao.AbstractRepository;
import fr.ign.cogit.simplu3d.model.UrbaDocument;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.FeatureCollection;

public class UrbaDocumentReader extends AbstractFeatureReader<UrbaDocument> {

  public static final String ATT_ID_URBA;// = "IDURBA";
  public static final String ATT_TYPE_DOC;// = "TYPEDOC";
  public static final String ATT_DATE_APPRO;// = "DATAPPRO";
  public static final String ATT_DATE_FIN;// = "DATEFIN";
  public static final String ATT_INTER_CO;// = "INTERCO";
  public static final String ATT_SIREN;// = "SIREN";
  public static final String ATT_ETAT;// = "ETAT";
  public static final String ATT_NOM_REG;// = "NOMREG";
  public static final String ATT_URL_REG;// = "URLREG";
  public static final String ATT_NOM_PLAN;// = "NOMPLAN";
  public static final String ATT_URL_PLAN;// = "URLPLAN";
  public static final String ATT_SITE_WEB;// = "SITEWEB";
  public static final String ATT_TYPE_REF;// = "TYPEREF";
  public static final String ATT_DATE_REF;// = "DATEREF";

  public final static String DATE_FORMAT_DU1;// = "yyyyMMdd";
  public final static String DATE_FORMAT_DU2;// = "yyyy";

  static {
    ATT_ID_URBA = AttribNames.getATT_ID_URBA();
    ATT_TYPE_DOC = AttribNames.getATT_TYPE_DOC();
    ATT_DATE_APPRO = AttribNames.getATT_DATE_APPRO();
    ATT_DATE_FIN = AttribNames.getATT_DATE_FIN();
    ATT_INTER_CO = AttribNames.getATT_INTER_CO();
    ATT_SIREN = AttribNames.getATT_SIREN();
    ATT_ETAT = AttribNames.getATT_ETAT();
    ATT_NOM_REG = AttribNames.getATT_NOM_REG();
    ATT_URL_REG = AttribNames.getATT_URL_REG();
    ATT_NOM_PLAN = AttribNames.getATT_NOM_PLAN();
    ATT_URL_PLAN = AttribNames.getATT_URL_PLAN();
    ATT_SITE_WEB = AttribNames.getATT_SITE_WEB();
    ATT_TYPE_REF = AttribNames.getATT_TYPE_REF();
    ATT_DATE_REF = AttribNames.getATT_DATE_REF();
    DATE_FORMAT_DU1 = AttribNames.getDATE_FORMAT_DU1();
    DATE_FORMAT_DU2 = AttribNames.getDATE_FORMAT_DU2();
  }
  public UrbaDocumentReader() {
    super(new FeatureType("UrbaDocument"));
    this.featureType.setGeometryType("Polygon");
    this.featureType.getAttributes().put(ATT_ID_URBA, "String");
    this.featureType.getAttributes().put(ATT_TYPE_DOC, "String");
    this.featureType.getAttributes().put(ATT_DATE_APPRO, "String");
    this.featureType.getAttributes().put(ATT_DATE_FIN, "String");
    this.featureType.getAttributes().put(ATT_INTER_CO, "String");
    this.featureType.getAttributes().put(ATT_SIREN, "String");
    this.featureType.getAttributes().put(ATT_ETAT, "String");
    this.featureType.getAttributes().put(ATT_NOM_REG, "String");
    this.featureType.getAttributes().put(ATT_URL_REG, "String");
    this.featureType.getAttributes().put(ATT_NOM_PLAN, "String");
    this.featureType.getAttributes().put(ATT_URL_PLAN, "String");
    this.featureType.getAttributes().put(ATT_SITE_WEB, "String");
    this.featureType.getAttributes().put(ATT_TYPE_REF, "String");
    this.featureType.getAttributes().put(ATT_DATE_REF, "String");
    this.featureType.getAttributes().put(DATE_FORMAT_DU1, "String");
    this.featureType.getAttributes().put(DATE_FORMAT_DU2, "String");
  }

  @Override
  public UrbaDocument read(SimpleFeature feature) {
    UrbaDocument result = new UrbaDocument();

    // Id Document Urbanisme
    result.setIdUrba(readStringAttribute(feature, ATT_ID_URBA));

    // Type Document Urbanisme
    result.setTypeDoc(readStringAttribute(feature, ATT_TYPE_DOC));

    // Date Approbation Document Urbanisme
    SimpleDateFormat sdfdeb = new SimpleDateFormat(DATE_FORMAT_DU1);
    result.setDateAppro(readDateAttribute(feature, ATT_DATE_APPRO, sdfdeb));

    // Date Fin Document Urbanisme
    result.setDateFin(readDateAttribute(feature, ATT_DATE_FIN, sdfdeb));

    // Inter-Co
    result.setInterCo(readStringAttribute(feature, ATT_INTER_CO));

    // Siren
    result.setSiren(readStringAttribute(feature, ATT_SIREN));

    // Etat
    result.setEtat(readStringAttribute(feature, ATT_ETAT));

    // Nom Région
    result.setNomReg(readStringAttribute(feature, ATT_NOM_REG));

    // URL région
    result.setUrlReg(readStringAttribute(feature, ATT_URL_REG));

    // Nom Plan
    result.setNomPlan(readStringAttribute(feature, ATT_NOM_PLAN));

    // URL Plan
    result.setUrlPlan(readStringAttribute(feature, ATT_URL_PLAN));

    // Site Web
    result.setSiteWeb(readStringAttribute(feature, ATT_SITE_WEB));

    // Type Ref
    result.setTypeRef(readStringAttribute(feature, ATT_TYPE_REF));

    // Date Référence
    SimpleDateFormat sdfref = new SimpleDateFormat(DATE_FORMAT_DU2);
    result.setDateRef(readDateAttribute(feature, ATT_DATE_REF, sdfref));

    return result;
  }
  @Override
  public AbstractRepository<UrbaDocument> create(FeatureCollection<SimpleFeatureType, SimpleFeature> features) {
    return new AbstractRepository<>(readAll(features), this.featureType, (features.getSchema() != null) ? features.getSchema().getCoordinateReferenceSystem() : null);
  }
}
