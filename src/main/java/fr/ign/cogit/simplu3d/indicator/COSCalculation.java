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

import java.util.List;

import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.SubParcel;

/**
 * 
 * Calcul du COS
 * 
 * @author MBrasebin
 *
 */
public class COSCalculation {

  public static enum METHOD {
    SIMPLE, FLOOR_CUT
  }

  public static double assess(SubParcel p, METHOD m) {
    double area = p.getLod2MultiSurface().area();
    return assess(p.getBuildingsParts().getElements(), m, area);
  }

  public static double assess(BasicPropertyUnit bPu, METHOD m) {

    double area = bPu.getpol2D().area();

    return assess(bPu.getBuildings(), m, area);
  }

  public static double assess(List<? extends AbstractBuilding> lBuildings,
      METHOD m, double area) {

    double aireBatie = 0;

    switch (m) {

      case SIMPLE:
        aireBatie = SHONCalculation.assessSimpleAireBati(lBuildings);
        break;
      case FLOOR_CUT:
        aireBatie = SHONCalculation.assessAireBatieFromCut(lBuildings);
        break;
    }

    return aireBatie / area;
  }

}
