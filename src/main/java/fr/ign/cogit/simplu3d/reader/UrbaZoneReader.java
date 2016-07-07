package fr.ign.cogit.simplu3d.reader;

import java.text.SimpleDateFormat;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.io.structDatabase.postgis.ParametersInstructionPG;
import fr.ign.cogit.simplu3d.model.UrbaZone;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class UrbaZoneReader extends AbstractReader<UrbaZone>{

	public static String ATT_LIBELLE = "LIBELLE";
	public static String ATT_LIBELONG = "LIBELONG";
	public static String ATT_TYPE_ZONE = "TYPEZONE";
	public static String ATT_DESTDOMI = "DESTDOMI";
	public static String ATT_NOMFIC = "NOMFIC";
	public static String ATT_URLFIC = "URLFIC";
	public static String ATT_INSEE = "INSEE";
	public static String VALIDITY_DATE_APPRO = "DATAPPRO";
	public static String VALIDITY_DATE_VALID = "DATVALID";
	public static String ATT_TEXT = "TEXT";


	@Override
	public UrbaZone read(IFeature feature) {
		UrbaZone z = new UrbaZone(FromGeomToSurface.convertMSGeom(feature.getGeom()));

		// Pour le Libelle de la zone urba
		z.setLibelle(readStringAttribute(feature,ATT_LIBELLE));

		// Pour le Libelong de la zone urba
		z.setLibelong(readStringAttribute(feature,ATT_LIBELONG));

		// Pour le type de la zone urba
		z.setTypeZone(readStringAttribute(feature,ATT_TYPE_ZONE));

		// Pour le Destdomi de la zone urba
		z.setDestdomi(readStringAttribute(feature,ATT_DESTDOMI));

		// Pour le nom du fichier associé
		z.setNomFic(readStringAttribute(feature,ATT_NOMFIC));

		// Pour l'url du fichier associé
		z.setUrlFic(readStringAttribute(feature,ATT_URLFIC));

		// Pour le code insee de la commune de la zone urba
		z.setInsee(readStringAttribute(feature,ATT_INSEE));

		// Pour la date d'approbation de la zone urba (date de début)
		SimpleDateFormat sdfdeb = new SimpleDateFormat(ParametersInstructionPG.DATE_FORMAT_DU1);
		z.setDateDeb(readDateAttribute(feature, VALIDITY_DATE_APPRO, sdfdeb));

		// Pour la date de validité de la zone urba (date de fin)
		z.setDateFin(readDateAttribute(feature, VALIDITY_DATE_VALID, sdfdeb));

		// Pour les commentaires éventuels sur la zone urba
		z.setText(readStringAttribute(feature,ATT_TEXT));

		return z;
	}

}
