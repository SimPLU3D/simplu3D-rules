package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.simplu3d.io.load.application.ParemetersApplication;
import fr.ign.cogit.simplu3d.model.application.UrbaDocument;

public class PLUImporter {

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

	public static UrbaDocument loadPLU(IFeature feat) {

		UrbaDocument p = new UrbaDocument();

		// Id Document Urbanisme
		Object oplu = feat.getAttribute(ATT_ID_URBA);
		if (oplu != null) {
			p.setIdUrba(oplu.toString());
		}

		// Type Document Urbanisme
		oplu = feat.getAttribute(ATT_TYPE_DOC);
		if (oplu != null) {
			p.setTypeDoc(oplu.toString());
		}

		// Date Approbation Document Urbanisme
		oplu = feat.getAttribute(ATT_DATE_APPRO);
		SimpleDateFormat sdfdeb = new SimpleDateFormat(ParemetersApplication.DATE_FORMAT_DU1);
		if (oplu != null) {
			try {
				p.setDateAppro(sdfdeb.parse(oplu.toString()));
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}

		// Date Fin Document Urbanisme
		oplu = feat.getAttribute(ATT_DATE_FIN);
		if (oplu != null) {
			try {
				p.setDateFin(sdfdeb.parse(oplu.toString()));
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}

		// Inter-Co
		oplu = feat.getAttribute(ATT_INTER_CO);
		if (oplu != null) {
			p.setInterCo(oplu.toString());
		}

		// Siren
		oplu = feat.getAttribute(ATT_SIREN);
		if (oplu != null) {
			p.setSiren(oplu.toString());
		}

		// Etat
		oplu = feat.getAttribute(ATT_ETAT);
		if (oplu != null) {
			p.setEtat(oplu.toString());
		}

		// Nom Région
		oplu = feat.getAttribute(ATT_NOM_REG);
		if (oplu != null) {
			p.setNomReg(oplu.toString());
		}

		// URL région
		oplu = feat.getAttribute(ATT_URL_REG);
		if (oplu != null) {
			p.setUrlReg(oplu.toString());
		}

		// Nom Plan
		oplu = feat.getAttribute(ATT_NOM_PLAN);
		if (oplu != null) {
			p.setNomPlan(oplu.toString());
		}

		// URL Plan
		oplu = feat.getAttribute(ATT_URL_PLAN);
		if (oplu != null) {
			p.setUrlPlan(oplu.toString());
		}

		// Site Web
		oplu = feat.getAttribute(ATT_SITE_WEB);
		if (oplu != null) {
			p.setSiteWeb(oplu.toString());
		}

		// Type Ref
		oplu = feat.getAttribute(ATT_TYPE_REF);
		if (oplu != null) {
			p.setTypeRef(oplu.toString());
		}

		// Date Référence
		oplu = feat.getAttribute(ATT_DATE_REF);
		SimpleDateFormat sdfref = new SimpleDateFormat(ParemetersApplication.DATE_FORMAT_DU2);
		if (oplu != null) {
			try {
				p.setDateRef(sdfref.parse(oplu.toString()));
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}

		return p;
	}
}
