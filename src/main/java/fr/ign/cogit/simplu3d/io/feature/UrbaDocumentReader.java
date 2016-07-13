package fr.ign.cogit.simplu3d.io.feature;

import java.text.SimpleDateFormat;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.simplu3d.model.UrbaDocument;

public class UrbaDocumentReader extends AbstractFeatureReader<UrbaDocument> {

	public static final String ATT_ID_URBA = "IDURBA";
	public static final String ATT_TYPE_DOC = "TYPEDOC";
	public static final String ATT_DATE_APPRO = "DATAPPRO";
	public static final String ATT_DATE_FIN = "DATEFIN";
	public static final String ATT_INTER_CO = "INTERCO";
	public static final String ATT_SIREN = "SIREN";
	public static final String ATT_ETAT = "ETAT";
	public static final String ATT_NOM_REG = "NOMREG";
	public static final String ATT_URL_REG = "URLREG";
	public static final String ATT_NOM_PLAN = "NOMPLAN";
	public static final String ATT_URL_PLAN = "URLPLAN";
	public static final String ATT_SITE_WEB = "SITEWEB";
	public static final String ATT_TYPE_REF = "TYPEREF";
	public static final String ATT_DATE_REF = "DATEREF";
	
	public final static String DATE_FORMAT_DU1 = "yyyyMMdd";
	public final static String DATE_FORMAT_DU2 = "yyyy";
	
	@Override
	public UrbaDocument read(IFeature feature) {
		UrbaDocument result = new UrbaDocument();

		// Id Document Urbanisme
		result.setIdUrba(readStringAttribute(feature,ATT_ID_URBA));
		
		// Type Document Urbanisme
		result.setTypeDoc(readStringAttribute(feature,ATT_TYPE_DOC));

		// Date Approbation Document Urbanisme
		SimpleDateFormat sdfdeb = new SimpleDateFormat(DATE_FORMAT_DU1);
		result.setDateAppro(readDateAttribute(feature, ATT_DATE_APPRO, sdfdeb));

		// Date Fin Document Urbanisme
		result.setDateFin(readDateAttribute(feature, ATT_DATE_FIN, sdfdeb));

		// Inter-Co
		result.setInterCo(readStringAttribute(feature,ATT_INTER_CO));

		// Siren
		result.setSiren(readStringAttribute(feature,ATT_SIREN));

		// Etat
		result.setEtat(readStringAttribute(feature,ATT_ETAT));

		// Nom Région
		result.setNomReg(readStringAttribute(feature,ATT_NOM_REG));

		// URL région
		result.setUrlReg(readStringAttribute(feature,ATT_URL_REG));

		// Nom Plan
		result.setNomPlan(readStringAttribute(feature,ATT_NOM_PLAN));

		// URL Plan
		result.setUrlPlan(readStringAttribute(feature,ATT_URL_PLAN));

		// Site Web
		result.setSiteWeb(readStringAttribute(feature,ATT_SITE_WEB));

		// Type Ref
		result.setTypeRef(readStringAttribute(feature,ATT_TYPE_REF));

		// Date Référence
		SimpleDateFormat sdfref = new SimpleDateFormat(DATE_FORMAT_DU2);
		result.setDateRef(readDateAttribute(feature, ATT_DATE_REF, sdfref));

		return result;
	}

}
