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

import fr.ign.cogit.geoxygene.feature.FT_Feature;

/**
 * 
 * CNIG PLU - Prescription ponctuelle, linéaire ou surfacique portée par un document d'urbanisme
 * 
 * @author Brasebin Mickaël
 *
 */
public class Prescription extends FT_Feature {
	
	/**
	 * Type of the prescription according to COVADIS standard
	*/
	public int type;
	
	public Prescription(int type) {
		super();
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
