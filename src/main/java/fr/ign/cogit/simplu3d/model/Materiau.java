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
package fr.ign.cogit.simplu3d.model;

/**
 * 
 * Classe décrivant la métériau qui s'applique sur les surfaces des bâtiments
 * 
 * @author Brasebin Mickaël
 *
 */
public class Materiau {

	public String nomMateriau;
	public String textRep = null;
	public double textH = Double.NaN, textL = Double.NaN;

	public Materiau(String nomMateriau, String textRep, double textH, double textL) {
		this.nomMateriau = nomMateriau;
		this.textRep = textRep;
		this.textH = textH;
		this.textL = textL;
	}

	public String getNomMateriau() {
		return nomMateriau;
	}

	public void setNomMateriau(String nomMateriau) {
		this.nomMateriau = nomMateriau;
	}

	public String getTextRep() {
		return textRep;
	}

	public void setTextRep(String textRep) {
		this.textRep = textRep;
	}

	public double getTextH() {
		return textH;
	}

	public void setTextH(double textH) {
		this.textH = textH;
	}

	public double getTextL() {
		return textL;
	}

	public void setTextL(double textL) {
		this.textL = textL;
	}

}
