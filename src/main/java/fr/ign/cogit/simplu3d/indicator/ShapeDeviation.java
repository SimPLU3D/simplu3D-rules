package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.geoxygene.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Solid;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;

public class ShapeDeviation {

  private double value;

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
 *
   * Rapport entre le volume d'un objet de le volume de sa boite orientée
   * @param bP
   */
  public ShapeDeviation(AbstractBuilding bP) {

    value = 1;

    OrientedBoundingBox oBB = new OrientedBoundingBox(bP.getGeom());

    if (oBB.getPoly() != null) {

      double zMin = oBB.getzMin();
      double zMax = oBB.getzMax();

      double volArea = oBB.getPoly().area() * (zMax - zMin);

      if (volArea == 0) {
        return;
      }

      
      double vBati = Calculation3D.volume(new GM_Solid(FromPolygonToTriangle.convertAndTriangle(bP.getRoof()
          .getLod2MultiSurface().getList())));

      value = vBati / volArea;

      if (value > 1) {

        System.out.println("Why ?" + value);
      }

    }

  }

  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }

}
