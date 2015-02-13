package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.ICurve;
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
public class Recoil extends Alignement {
  
  
  public Recoil(int type, ICurve geom) {
    super(type, geom);
    // TODO Auto-generated constructor stub
  }

  public double distance;
  
  public IFeature objRef;

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  public IFeature getObjRef() {
    return objRef;
  }

  public void setObjRef(IFeature objRef) {
    this.objRef = objRef;
  }
  
  

}
