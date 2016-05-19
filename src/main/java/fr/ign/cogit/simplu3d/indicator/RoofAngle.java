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

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.sig3d.calculation.AngleFromSurface;
import fr.ign.cogit.simplu3d.model.RoofSurface;

/**
 * 
 * @author MBrasebin
 *
 */
public class RoofAngle {

  public static double angleMin(RoofSurface t) {

    double angleMin = Double.POSITIVE_INFINITY;
    for (IOrientableSurface o : t.getLod2MultiSurface()) {

      angleMin = Math.min(angleMin, AngleFromSurface.calculate(o));
    }

    return angleMin;

  }

  public static double angleMax(RoofSurface t) {

    double angleMax = Double.NEGATIVE_INFINITY;
    
    for (IOrientableSurface o : t.getLod2MultiSurface()) {

      angleMax = Math.max(angleMax, AngleFromSurface.calculate(o));
    }

    return angleMax;

  }

}
