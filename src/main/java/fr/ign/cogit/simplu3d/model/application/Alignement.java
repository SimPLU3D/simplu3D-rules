package fr.ign.cogit.simplu3d.model.application;

import java.util.List;

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
public class Alignement extends Prescription_LIN {


  public Alignement(int type, ICurve geom) {
    super(type, geom);
    // TODO Auto-generated constructor stub
  }

  public boolean isStrict;

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public List<Alignement> opposite() {
    return null;
  }

}
