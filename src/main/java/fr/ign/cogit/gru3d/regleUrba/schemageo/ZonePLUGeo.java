package fr.ign.cogit.gru3d.regleUrba.schemageo;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.feature.FT_Feature;

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
 * Classe de l'environnement géographique Décrit une zone du PLU comme une
 * ensemble de parcelles
 */
public class ZonePLUGeo extends FT_Feature {

  String nom = null;

  List<Parcelle> lParcelles = new ArrayList<Parcelle>();

  public String getNom() {
    return this.nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public List<Parcelle> getlParcelles() {
    return this.lParcelles;
  }

  public void setlParcelles(List<Parcelle> lParcelles) {
    this.lParcelles = lParcelles;
  }

}
