package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
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
public class Building extends AbstractBuilding {

  public Building(){
    super();
  }
  public Building(IGeometry geom) {
    super(geom);
  }

  @Override
  public AbstractBuilding clone() {
    Building b = new Building((IGeometry) this.getGeom().clone());
    b.isNew = this.isNew;
    return b;
  }
}
