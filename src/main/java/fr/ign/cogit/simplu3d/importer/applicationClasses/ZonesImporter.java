package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

  public final static String NOM_ATT_NAME_ZONE = "TYPEZONE";
  public final static String NOM_ATT_TEXT_ZONE = "TEXT";
  public final static String NOM_VALIDITY_DATE_DEB = "DATE_DEB";
  public final static String NOM_VALIDITY_DATE_FIN = "DATE_FIN";

  @SuppressWarnings("deprecation")
  public static IFeatureCollection<UrbaZone> importUrbaZone(
      IFeatureCollection<IFeature> zoneColl) {

    IFeatureCollection<UrbaZone> zones = new FT_FeatureCollection<UrbaZone>();
    for (IFeature feat : zoneColl) {

      UrbaZone z = new UrbaZone(FromGeomToSurface.convertMSGeom(feat.getGeom()));

      
      System.out.println("ZoneImporter : " + z.getGeom().getClass());
      
      Object o = feat.getAttribute(NOM_ATT_NAME_ZONE);

      if (o != null) {
        z.setName(o.toString());
      }

      o = feat.getAttribute(NOM_ATT_TEXT_ZONE);
                 

      if (o != null) {
    	  
    	  
        z.setText(o.toString());
      }

      o = feat.getAttribute(NOM_VALIDITY_DATE_DEB);

      SimpleDateFormat sdfdeb = new SimpleDateFormat(ParemetersApplication.DATE_FORMAT);
      
      
      if (o != null) {
    	  
        try {
			z.setDateDeb(sdfdeb.parse(o.toString()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
      
      o = feat.getAttribute(NOM_VALIDITY_DATE_FIN);

 
      
      if (o != null) {
          
        try {
            z.setDateFin(sdfdeb.parse(o.toString()));
            
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      }
      

      zones.add(z);

    }

    return zones;

  }

}
