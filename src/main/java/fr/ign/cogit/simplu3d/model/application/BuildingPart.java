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
public class BuildingPart extends AbstractBuilding {

  public BuildingPart(IGeometry geom) {
    super(geom);
  }

  public BuildingPart() {
    // TODO Auto-generated constructor stub
  }

  public SubParcel sP;

  public SubParcel getsP() {
    return sP;
  }

  public void setsP(SubParcel sP) {
    this.sP = sP;
  }

  @Override
  public AbstractBuilding clone() {

    BuildingPart b = new BuildingPart((IGeometry) this.getGeom().clone());
    b.isNew = this.isNew;

    return b;

  }

}
