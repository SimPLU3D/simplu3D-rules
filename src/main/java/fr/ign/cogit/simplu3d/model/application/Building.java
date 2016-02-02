package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
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

  private int idBuilding = 0;
  private int idVersion = 0;

  public void setIdBuilding(int idBuilding) {
    this.idBuilding = idBuilding;
  }

  public int getIdBuilding() {
    return idBuilding;
  }
  
  public void setIdVersion(int IdVersion) {
    this.idVersion = IdVersion;
  }

  public int getIdVersion() {
    return idVersion;
  }

  public Building() {
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
