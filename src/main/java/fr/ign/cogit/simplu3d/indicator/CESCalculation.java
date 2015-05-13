package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
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
public class CESCalculation {

  public static double assess(SubParcel p) {

    double area = p.getLod2MultiSurface().area();
    double aireBatie = 0;
   
    
    for(AbstractBuilding b:p.getBuildingsParts()){
      aireBatie = aireBatie + b.getFootprint().area();
    }
    
    return aireBatie/area;
    
  }
}