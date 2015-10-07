package fr.ign.cogit.simplu3d.model.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.model.citygml.landuse.CG_LandUse;

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
public class UrbaZone extends CG_LandUse {

  public final String CLASSE = "Zone";

  private IFeatureCollection<SubParcel> subParcels = new FT_FeatureCollection<SubParcel>();
  private String name = "";
  private List<Rule> rules = new ArrayList<Rule>();
  private String text = "";
  private Date dateDeb = null;
  
  
  
 

  public UrbaZone(IGeometry geom) {
    super();
    this.setGeom(geom);
  }
  
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public List<Rule> getRules() {
    return rules;
  }

  public void setRules(List<Rule> rules) {
    this.rules = rules;
  }

  public void setName(String name) {
    this.name = name;
  }
  public Date getDateDeb() {
    return dateDeb;
  }

  public void setDateDeb(Date date) {
    this.dateDeb = date;
  }


  public IFeatureCollection<SubParcel> getSubParcels() {
    return subParcels;
  }

  public void setSubParcels(IFeatureCollection<SubParcel> subParcels) {
    this.subParcels = subParcels;
  }

  public String getName() {
    return name;
  }



}
