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
package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.ICurve;

/**
 * 
 * CNIG PLU - Prescription linéaire
 * 
 * @author Brasebin Mickaël
 *
 */
public class Prescription_LIN extends Prescription {
  



  public Prescription_LIN(int type, ICurve geom) {
    super(type);

    this.geom = geom;
  }
  
  

}
