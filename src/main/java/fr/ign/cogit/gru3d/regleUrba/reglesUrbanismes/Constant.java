package fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes;
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
public abstract class Constant {
  /**
   * Coefficient permettant d'obtenir le COS en éléminant les parties communes,
   * locaux techniques, isolation ext.
   */
  public final static double COEFF_COS = 0.8;

  /**
   * Constante fixant la hauteur d'un étage
   */
  public static double HAUTEUR_ETAGE = 3;
}
