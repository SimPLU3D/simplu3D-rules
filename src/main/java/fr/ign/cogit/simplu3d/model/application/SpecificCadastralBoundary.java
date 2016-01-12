package fr.ign.cogit.simplu3d.model.application;

import org.citygml4j.model.citygml.core.CityObject;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.model.citygml.core.CG_CityObject;

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
public class SpecificCadastralBoundary extends CG_CityObject {

  public final static int BOT = 0;
  public final static int LAT = 1;
  public final static int UNKNOWN = 2;
  public final static int INTRA = 3;
  public final static int ROAD = 4;
  public final static int PUB = 5;

  public final static int RIGHT_SIDE = 0;
  public final static int LEFT_SIDE = 1;
  public final static int UNKOWN_SIDE = 99;

  public Alignement alignement = null;
  public Recoil recoil = null;
  public int type;
  public int side = UNKOWN_SIDE;
  public int id_subpar;
  public int id_adj;
  public String tabRef = "";

  // Il s'agit de l'objet qui ne référence pas cette bordure et qui est adjacent
  // à la bordure
  private IFeature featAdj = null;

  public IFeature getFeatAdj() {
    return featAdj;
  }

  public CadastralParcel getCadastralCadastralParcel() {
    return null;
  }

  public SubParcel getSubParcel() {
    return null;
  }

  public void setFeatAdj(IFeature featAdj) {
    this.featAdj = featAdj;
  }

  public SpecificCadastralBoundary(IGeometry geom) {
    this.setGeom(geom);
  }

  @Override
  public CityObject export() {
    // TODO Auto-generated method stub
    return null;
  }

  public Alignement getAlignement() {
    return alignement;
  }

  public void setAlignement(Alignement alignement) {
    this.alignement = alignement;
  }

  public Recoil getRecoil() {
    return recoil;
  }

  public void setRecoil(Recoil recoil) {
    this.recoil = recoil;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getIdSubPar() {
    return id_subpar;
  }

  public void setIdSubPar(int id_subpar) {
    this.id_subpar = id_subpar;
  }

  public int getIdAdj() {
    return id_adj;
  }

  public void setIdAdj(int id_adj) {
    this.id_adj = id_adj;
  }

  public String getTableRef() {
    return tabRef;
  }

  public void setTableRef(String tabRef) {
    this.tabRef = tabRef;
  }

  /**
   * @return the side
   */
  public int getSide() {
    return side;
  }

  /**
   * @param side the side to set
   */
  public void setSide(int side) {
    this.side = side;
  }

}
