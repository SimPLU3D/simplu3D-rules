package fr.ign.cogit.simplu3d.io.feature;

public class AttribNames {
  // CadastralParcelReader
  private static String ATT_CODE_PARC = "CODE";
  private static String ATT_BDP_CODE_DEP = "CODE_DEP";
  private static String ATT_BDP_CODE_COM = "CODE_COM";
  private static String ATT_BDP_COM_ABS = "COM_ABS";
  private static String ATT_BDP_SECTION = "SECTION";
  private static String ATT_BDP_NUMERO = "NUMERO";
  private static String ATT_HAS_TO_BE_SIMULATED = "SIMUL";

  // PrescriptionReader
  private static String ATT_TYPE_PRESCRIPTION = "TYPEPSC";
  private static String ATT_LABEL = "LIBELLE";
  /**
   * Valeur de l'attribut de recul s'il existe
   */
  private static String ATT_RECOIL = "RECUL";

  // RoadReader
  private static String ATT_NOM_RUE = "NOM_RUE_G";
  private static String ATT_LARGEUR = "LARGEUR";
  private static String ATT_TYPE_ROAD = "NATURE";

  // UrbaDocumentReader
  private static String ATT_ID_URBA = "IDURBA";
  private static String ATT_TYPE_DOC = "TYPEDOC";
  private static String ATT_DATE_APPRO = "DATAPPRO";
  private static String ATT_DATE_FIN = "DATEFIN";
  private static String ATT_INTER_CO = "INTERCO";
  private static String ATT_SIREN = "SIREN";
  private static String ATT_ETAT = "ETAT";
  private static String ATT_NOM_REG = "NOMREG";
  private static String ATT_URL_REG = "URLREG";
  private static String ATT_NOM_PLAN = "NOMPLAN";
  private static String ATT_URL_PLAN = "URLPLAN";
  private static String ATT_SITE_WEB = "SITEWEB";
  private static String ATT_TYPE_REF = "TYPEREF";
  private static String ATT_DATE_REF = "DATEREF";
  private static String DATE_FORMAT_DU1 = "yyyyMMdd";
  private static String DATE_FORMAT_DU2 = "yyyy";

  // UrbaZoneReader
  private static String ATT_LIBELLE = "LIBELLE";
  private static String ATT_LIBELONG = "LIBELONG";
  private static String ATT_TYPE_ZONE = "TYPEZONE";
  private static String ATT_DESTDOMI = "DESTDOMI";
  private static String ATT_NOMFIC = "NOMFIC";
  private static String ATT_URLFIC = "URLFIC";
  private static String ATT_INSEE = "INSEE";
  private static String VALIDITY_DATE_APPRO = "DATAPPRO";
  private static String VALIDITY_DATE_VALID = "DATVALID";
  private static String ATT_TEXT = "TEXT";
  // public final static String DATE_FORMAT_DU1 = "yyyyMMdd";

  public static String getATT_BDP_CODE_DEP() {
    return ATT_BDP_CODE_DEP;
  }

  public static void setATT_BDP_CODE_DEP(String aTT_BDP_CODE_DEP) {
    ATT_BDP_CODE_DEP = aTT_BDP_CODE_DEP;
  }

  public static String getATT_BDP_CODE_COM() {
    return ATT_BDP_CODE_COM;
  }

  public static void setATT_BDP_CODE_COM(String aTT_BDP_CODE_COM) {
    ATT_BDP_CODE_COM = aTT_BDP_CODE_COM;
  }

  public static String getATT_BDP_COM_ABS() {
    return ATT_BDP_COM_ABS;
  }

  public static void setATT_BDP_COM_ABS(String aTT_BDP_COM_ABS) {
    ATT_BDP_COM_ABS = aTT_BDP_COM_ABS;
  }

  public static String getATT_BDP_SECTION() {
    return ATT_BDP_SECTION;
  }

  public static void setATT_BDP_SECTION(String aTT_BDP_SECTION) {
    ATT_BDP_SECTION = aTT_BDP_SECTION;
  }

  public static String getATT_BDP_NUMERO() {
    return ATT_BDP_NUMERO;
  }

  public static void setATT_BDP_NUMERO(String aTT_BDP_NUMERO) {
    ATT_BDP_NUMERO = aTT_BDP_NUMERO;
  }

  public static String getATT_HAS_TO_BE_SIMULATED() {
    return ATT_HAS_TO_BE_SIMULATED;
  }

  public static void setATT_HAS_TO_BE_SIMULATED(
      String aTT_HAS_TO_BE_SIMULATED) {
    ATT_HAS_TO_BE_SIMULATED = aTT_HAS_TO_BE_SIMULATED;
  }

  public static String getATT_LABEL() {
    return ATT_LABEL;
  }

  public static void setATT_LABEL(String aTT_LABEL) {
    ATT_LABEL = aTT_LABEL;
  }

  public static String getATT_RECOIL() {
    return ATT_RECOIL;
  }

  public static void setATT_RECOIL(String aTT_RECOIL) {
    ATT_RECOIL = aTT_RECOIL;
  }

  public static String getATT_NOM_RUE() {
    return ATT_NOM_RUE;
  }

  public static void setATT_NOM_RUE(String aTT_NOM_RUE) {
    ATT_NOM_RUE = aTT_NOM_RUE;
  }

  public static String getATT_LARGEUR() {
    return ATT_LARGEUR;
  }

  public static void setATT_LARGEUR(String aTT_LARGEUR) {
    ATT_LARGEUR = aTT_LARGEUR;
  }

  public static String getATT_ID_URBA() {
    return ATT_ID_URBA;
  }

  public static void setATT_ID_URBA(String aTT_ID_URBA) {
    ATT_ID_URBA = aTT_ID_URBA;
  }

  public static String getATT_TYPE_DOC() {
    return ATT_TYPE_DOC;
  }

  public static void setATT_TYPE_DOC(String aTT_TYPE_DOC) {
    ATT_TYPE_DOC = aTT_TYPE_DOC;
  }

  public static String getATT_DATE_APPRO() {
    return ATT_DATE_APPRO;
  }

  public static void setATT_DATE_APPRO(String aTT_DATE_APPRO) {
    ATT_DATE_APPRO = aTT_DATE_APPRO;
  }

  public static String getATT_DATE_FIN() {
    return ATT_DATE_FIN;
  }

  public static void setATT_DATE_FIN(String aTT_DATE_FIN) {
    ATT_DATE_FIN = aTT_DATE_FIN;
  }

  public static String getATT_INTER_CO() {
    return ATT_INTER_CO;
  }

  public static void setATT_INTER_CO(String aTT_INTER_CO) {
    ATT_INTER_CO = aTT_INTER_CO;
  }

  public static String getATT_SIREN() {
    return ATT_SIREN;
  }

  public static void setATT_SIREN(String aTT_SIREN) {
    ATT_SIREN = aTT_SIREN;
  }

  public static String getATT_ETAT() {
    return ATT_ETAT;
  }

  public static void setATT_ETAT(String aTT_ETAT) {
    ATT_ETAT = aTT_ETAT;
  }

  public static String getATT_NOM_REG() {
    return ATT_NOM_REG;
  }

  public static void setATT_NOM_REG(String aTT_NOM_REG) {
    ATT_NOM_REG = aTT_NOM_REG;
  }

  public static String getATT_URL_REG() {
    return ATT_URL_REG;
  }

  public static void setATT_URL_REG(String aTT_URL_REG) {
    ATT_URL_REG = aTT_URL_REG;
  }

  public static String getATT_NOM_PLAN() {
    return ATT_NOM_PLAN;
  }

  public static void setATT_NOM_PLAN(String aTT_NOM_PLAN) {
    ATT_NOM_PLAN = aTT_NOM_PLAN;
  }

  public static String getATT_URL_PLAN() {
    return ATT_URL_PLAN;
  }

  public static void setATT_URL_PLAN(String aTT_URL_PLAN) {
    ATT_URL_PLAN = aTT_URL_PLAN;
  }

  public static String getATT_SITE_WEB() {
    return ATT_SITE_WEB;
  }

  public static void setATT_SITE_WEB(String aTT_SITE_WEB) {
    ATT_SITE_WEB = aTT_SITE_WEB;
  }

  public static String getATT_TYPE_REF() {
    return ATT_TYPE_REF;
  }

  public static void setATT_TYPE_REF(String aTT_TYPE_REF) {
    ATT_TYPE_REF = aTT_TYPE_REF;
  }

  public static String getATT_DATE_REF() {
    return ATT_DATE_REF;
  }

  public static void setATT_DATE_REF(String aTT_DATE_REF) {
    ATT_DATE_REF = aTT_DATE_REF;
  }

  public static String getDATE_FORMAT_DU1() {
    return DATE_FORMAT_DU1;
  }

  public static void setDATE_FORMAT_DU1(String dATE_FORMAT_DU1) {
    DATE_FORMAT_DU1 = dATE_FORMAT_DU1;
  }

  public static String getDATE_FORMAT_DU2() {
    return DATE_FORMAT_DU2;
  }

  public static void setDATE_FORMAT_DU2(String dATE_FORMAT_DU2) {
    DATE_FORMAT_DU2 = dATE_FORMAT_DU2;
  }

  public static String getATT_LIBELLE() {
    return ATT_LIBELLE;
  }

  public static void setATT_LIBELLE(String aTT_LIBELLE) {
    ATT_LIBELLE = aTT_LIBELLE;
  }

  public static String getATT_LIBELONG() {
    return ATT_LIBELONG;
  }

  public static void setATT_LIBELONG(String aTT_LIBELONG) {
    ATT_LIBELONG = aTT_LIBELONG;
  }

  public static String getATT_TYPE_ZONE() {
    return ATT_TYPE_ZONE;
  }

  public static void setATT_TYPE_ZONE(String aTT_TYPE_ZONE) {
    ATT_TYPE_ZONE = aTT_TYPE_ZONE;
  }

  public static String getATT_DESTDOMI() {
    return ATT_DESTDOMI;
  }

  public static void setATT_DESTDOMI(String aTT_DESTDOMI) {
    ATT_DESTDOMI = aTT_DESTDOMI;
  }

  public static String getATT_NOMFIC() {
    return ATT_NOMFIC;
  }

  public static void setATT_NOMFIC(String aTT_NOMFIC) {
    ATT_NOMFIC = aTT_NOMFIC;
  }

  public static String getATT_URLFIC() {
    return ATT_URLFIC;
  }

  public static void setATT_URLFIC(String aTT_URLFIC) {
    ATT_URLFIC = aTT_URLFIC;
  }

  public static String getATT_INSEE() {
    return ATT_INSEE;
  }

  public static void setATT_INSEE(String aTT_INSEE) {
    ATT_INSEE = aTT_INSEE;
  }

  public static String getVALIDITY_DATE_APPRO() {
    return VALIDITY_DATE_APPRO;
  }

  public static void setVALIDITY_DATE_APPRO(String vALIDITY_DATE_APPRO) {
    VALIDITY_DATE_APPRO = vALIDITY_DATE_APPRO;
  }

  public static String getVALIDITY_DATE_VALID() {
    return VALIDITY_DATE_VALID;
  }

  public static void setVALIDITY_DATE_VALID(String vALIDITY_DATE_VALID) {
    VALIDITY_DATE_VALID = vALIDITY_DATE_VALID;
  }

  public static String getATT_TEXT() {
    return ATT_TEXT;
  }

  public static void setATT_TEXT(String aTT_TEXT) {
    ATT_TEXT = aTT_TEXT;
  }

  public static String getATT_TYPE_PRESCRIPTION() {
    return ATT_TYPE_PRESCRIPTION;
  }

  public static void setATT_TYPE_PRESCRIPTION(String aTT_TYPE_PRESCRIPTION) {
    ATT_TYPE_PRESCRIPTION = aTT_TYPE_PRESCRIPTION;
  }

  public static String getATT_TYPE_ROAD() {
    return ATT_TYPE_ROAD;
  }

  public static void setATT_TYPE_ROAD(String aTT_TYPE_ROAD) {
    ATT_TYPE_ROAD = aTT_TYPE_ROAD;
  }

  public static String getATT_CODE_PARC() {
    return ATT_CODE_PARC;
  }

  public static void setATT_CODE_PARC(String aTT_CODE_PARC) {
    ATT_CODE_PARC = aTT_CODE_PARC;
  }

}
