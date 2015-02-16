package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
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
public class PublicSpace extends DefaultFeature {
  
  
  public String type;
  
  public IPolygon geom;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public IPolygon getGeom() {
    return geom;
  }

  public void setGeom(IPolygon geom) {
    this.geom = geom;
  }
  
  
  
  
   

}
