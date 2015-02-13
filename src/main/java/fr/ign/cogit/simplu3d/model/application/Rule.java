package fr.ign.cogit.simplu3d.model.application;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
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
public class Rule {

  public static int LAST_ID = 0;

  public int integerID = LAST_ID++;

  public Constraint constraint;

  public String text;

  public Rule(Constraint constraint, String text) {
    super();
    this.constraint = constraint;
    this.text = text;
  }

}
