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
package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.ICurve;

/**
 * 
 * Un Alignement est un type de prescription linéaire
 * 
 * @author Brasebin Mickaël
 *
 */
public class Alignement extends Prescription_LIN {

	public boolean isStrict;

	public Alignement(int type, ICurve geom) {
		super(type, geom);
	}

}
