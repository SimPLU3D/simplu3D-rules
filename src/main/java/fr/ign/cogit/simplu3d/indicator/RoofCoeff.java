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
public class RoofCoeff {

  private double value = 0;

  public RoofCoeff(AbstractBuilding b) {

    double h1 = HauteurCalculation.calculate(b,
       0,
       1);
    double h2 = HauteurCalculation.calculate(b,
        0,
       0);

    value = h1 / h2 - 1;

  }

  public Double getValue() {

    return value;
  }

}
