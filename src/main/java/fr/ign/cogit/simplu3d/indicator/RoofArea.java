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

import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;

/**
 * 
 * @author MBrasebin
 *
 */
public class RoofArea {

  private double value = 0;

  public RoofArea(RoofSurface t) {

    value = Calculation3D.area(FromPolygonToTriangle.convertAndTriangle(t
        .getLod2MultiSurface().getList()));

  }

  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }

}
