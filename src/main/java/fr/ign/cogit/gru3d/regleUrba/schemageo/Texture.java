package fr.ign.cogit.gru3d.regleUrba.schemageo;

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
 * Classe de texture règles urbanisme
 * 
 * 
 */
public class Texture {

  private String nom = null;

  private String uri = null;

  public Texture(String nom, String uri) {
    this.nom = nom;
    this.uri = uri;
  }

  public String getNom() {
    return this.nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getUri() {
    return this.uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

}
