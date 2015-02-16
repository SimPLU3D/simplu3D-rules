package fr.ign.cogit.simplu3d.util;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
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
public class PointInPolygon {

  public static IDirectPosition get(IPolygon poly) {

    IEnvelope env = poly.getEnvelope();

    while (true) {

      double x = Math.random()
          * (env.getUpperCorner().getX() - env.getLowerCorner().getX())
          + env.getLowerCorner().getX();
      double y = Math.random()
          * (env.getUpperCorner().getY() - env.getLowerCorner().getY())
          + env.getLowerCorner().getY();

      IDirectPosition dp = new DirectPosition(x, y);

      if (poly.contains((new GM_Point(dp)).buffer(0.05))) {
        return dp;
      }

    }

  }
}
