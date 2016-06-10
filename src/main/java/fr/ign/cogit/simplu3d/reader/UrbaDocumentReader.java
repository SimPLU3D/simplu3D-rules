package fr.ign.cogit.simplu3d.reader;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.io.structDatabase.postgis.ParametersInstructionPG;
import fr.ign.cogit.simplu3d.model.UrbaDocument;

/**
 * 
 * Read features as UrbaDocument
 * 
 * @author MBorne
 *
 */
public class UrbaDocumentReader {

	public static String ATT_ID_URBA = "IDURBA";
	public static String ATT_TYPE_DOC = "TYPEDOC";
	public static String ATT_DATE_APPRO = "DATAPPRO";
	public static String ATT_DATE_FIN = "DATEFIN";
	public static String ATT_INTER_CO = "INTERCO";
	public static String ATT_SIREN = "SIREN";
	public static String ATT_ETAT = "ETAT";
	public static String ATT_NOM_REG = "NOMREG";
	public static String ATT_URL_REG = "URLREG";
	public static String ATT_NOM_PLAN = "NOMPLAN";
	public static String ATT_URL_PLAN = "URLPLAN";
	public static String ATT_SITE_WEB = "SITEWEB";
	public static String ATT_TYPE_REF = "TYPEREF";
	public static String ATT_DATE_REF = "DATEREF";

	/**
	 * Read shapefile
	 * @param path
	 * @return
	 */
	public List<UrbaDocument> readShapefile(File path){
		IFeatureCollection<IFeature> features = ShapefileReader.read(path.toString());
		return read(features);
	}
	
	/**
	 * Read feature collection
	 * @param features
	 * @return
	 */
	public List<UrbaDocument> read(IFeatureCollection<IFeature> features) {
		List<UrbaDocument> result = new ArrayList<>();
		for (IFeature feature : features) {
			result.add(read(feature));
		}
		return result;
	}

	/**
	 * Read a single feature
	 * @param feature
	 * @return
	 */
	public UrbaDocument read(IFeature feature) {
		UrbaDocument result = new UrbaDocument();

		// Id Document Urbanisme
		result.setIdUrba(readStringAttribute(feature,ATT_ID_URBA));
		
		// Type Document Urbanisme
		result.setTypeDoc(readStringAttribute(feature,ATT_TYPE_DOC));

		// Date Approbation Document Urbanisme
		SimpleDateFormat sdfdeb = new SimpleDateFormat(ParametersInstructionPG.DATE_FORMAT_DU1);
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
		SimpleDateFormat sdfref = new SimpleDateFormat(ParametersInstructionPG.DATE_FORMAT_DU2);
		result.setDateRef(readDateAttribute(feature, ATT_DATE_REF, sdfref));

		return result;
	}
	
	/**
	 * Read a string 
	 * @param feature
	 * @param attributeName
	 * @return a string or null
	 */
	private String readStringAttribute(IFeature feature, String attributeName){
		Object object = feature.getAttribute(attributeName);
		if ( object == null ){
			return null;
		}else{
			return object.toString();
		}
	}

	/**
	 * Read a date with a given format
	 * @param feature
	 * @param attributeName
	 * @param dateFormat
	 * @return
	 */
	private Date readDateAttribute(IFeature feature, String attributeName, DateFormat dateFormat){
		Object object = feature.getAttribute(attributeName);
		if ( object == null ){
			return null;
		}
		try {
			return dateFormat.parse(object.toString());
		} catch (ParseException e) {
			//TODO log
			return null;
		}
	}
	
}
