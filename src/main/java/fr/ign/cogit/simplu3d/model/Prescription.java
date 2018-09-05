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

import fr.ign.cogit.geoxygene.feature.DefaultFeature;

/**
 * 
 * CNIG PLU - Prescription ponctuelle, linéaire ou surfacique portée par un
 * document d'urbanisme
 * 
 * TODO ajouter une zone d'influence pour éviter de gérer 45 types de relation
 * avec les objets du modèle et s'appuyer sur une relation spatiale?
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class Prescription extends DefaultFeature {

	/**
	 * Type of the prescription according to CNIG standard (TYPEPSC2)
	 */
	public PrescriptionType type;
	/**
	 * The label of the prescription (CNIG : LIBELLE)
	 */
	private String label;

	public Prescription() {
		this.type = PrescriptionType.UNKNOWN;
	}

	public PrescriptionType getType() {
		return type;
	}

	public void setType(PrescriptionType type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	double value;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Prescription [type=" + type + ", label=" + label + ", value=" + value + ", geom" + this.getGeom()+  "]";
	}
	
	

}
