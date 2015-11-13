package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.io.load.application.ParemetersApplication;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;
/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class ZonesImporter {

  public final static String NOM_ATT_LIBELLE = "LIBELLE";
  public final static String NOM_ATT_LIBELONG = "LIBELONG";
  public final static String NOM_ATT_TYPE_ZONE = "TYPEZONE";
  public final static String NOM_ATT_DESTDOMI = "DESTDOMI";
  public final static String NOM_ATT_NOMFIC = "NOMFIC";
  public final static String NOM_ATT_URLFIC = "URLFIC";
  public final static String NOM_ATT_INSEE = "INSEE";
  public final static String NOM_VALIDITY_DATE_APPRO = "DATAPPRO";
  public final static String NOM_VALIDITY_DATE_VALID = "DATVALID";
  public final static String NOM_ATT_TEXT = "TEXT";

  @SuppressWarnings("deprecation")
  public static IFeatureCollection<UrbaZone> importUrbaZone(
    IFeatureCollection<IFeature> zoneColl) {

    IFeatureCollection<UrbaZone> zones = new FT_FeatureCollection<UrbaZone>();
    for (IFeature feat : zoneColl) {

      UrbaZone z = new UrbaZone(FromGeomToSurface.convertMSGeom(feat.getGeom()));

      System.out.println("ZoneImporter : " + z.getGeom().getClass());
      
      
      // Pour le Libelle de la zone urba
      Object o = feat.getAttribute(NOM_ATT_LIBELLE);
      
      if (o != null) {
        z.setLibelle(o.toString());
      }
      
      
      // Pour le Libelong de la zone urba
      o = feat.getAttribute(NOM_ATT_LIBELONG);
      
      if (o != null) {
        z.setLibelong(o.toString());
      }
      
      
      // Pour le type de la zone urba
      o = feat.getAttribute(NOM_ATT_TYPE_ZONE);

      if (o != null) {
        z.setTypeZone(o.toString());
      }
      
      
      // Pour le Destdomi de la zone urba
      o = feat.getAttribute(NOM_ATT_DESTDOMI);

      if (o != null) {
        z.setDestdomi(o.toString());
      }
      
      
      // Pour le nom du fichier associé
      o = feat.getAttribute(NOM_ATT_NOMFIC);

      if (o != null) {
        z.setNomFic(o.toString());
      }
      
      
      // Pour l'url du fichier associé
      o = feat.getAttribute(NOM_ATT_URLFIC);

      if (o != null) {
        z.setUrlFic(o.toString());
      }
      
      
      // Pour le code insee de la commune de la zone urba
      o = feat.getAttribute(NOM_ATT_INSEE);

      if (o != null) {
        z.setInsee(o.toString());
      }

      
      // Pour la date d'approbation de la zone urba (date de début)
      o = feat.getAttribute(NOM_VALIDITY_DATE_APPRO);

      SimpleDateFormat sdfdeb = new SimpleDateFormat(ParemetersApplication.DATE_FORMAT_ZU);
      if (o != null) {
        try {
			z.setDateDeb(sdfdeb.parse(o.toString()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
 
      
      // Pour la date de validité de la zone urba (date de fin)
      o = feat.getAttribute(NOM_VALIDITY_DATE_VALID);
      if (o != null) {
        try {
            z.setDateFin(sdfdeb.parse(o.toString()));
            
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      }
      
      
      // Pour les commentaires éventuels sur la zone urba
      o = feat.getAttribute(NOM_ATT_TEXT);
      
      if (o != null) { 
        z.setText(o.toString());
      }
      

      zones.add(z);

    }

    return zones;

  }

}
