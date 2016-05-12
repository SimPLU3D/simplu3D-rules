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
package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;

/**
 * 
 * @author MBrasebin
 *
 */
public class ShapeFactor2  {

  private double value;

  public ShapeFactor2(AbstractBuilding b) {
    
    

    double h1 = HauteurCalculation.calculate(b,
        PointBasType.PLUS_BAS_BATIMENT,
       1);
    


    double area = b.getFootprint().area();
    
    
    
    value = Math.pow(h1, 2) / area;

  }


  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }



}
