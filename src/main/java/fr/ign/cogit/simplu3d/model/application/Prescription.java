package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.feature.FT_Feature;
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
public class Prescription extends FT_Feature {
  
  
  
  public Prescription(int type) {
    super();
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int type;

}
