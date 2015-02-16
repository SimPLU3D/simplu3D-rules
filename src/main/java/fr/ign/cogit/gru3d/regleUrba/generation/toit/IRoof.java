package fr.ign.cogit.gru3d.regleUrba.generation.toit;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
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
public interface IRoof {

  
  public String getType();
  public IMultiSurface<IPolygon> getRoof();
  public IMultiSurface<IPolygon> generateWall(double zMin);
  public IMultiSurface<IPolygon> generateBuilding(double zMin);
}
