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

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;

/**
 * 
 * CNIG PLU - Prescription surfacique
 * 
 * @author Brasebin Mickaël
 *
 */
public class Prescription_SURF extends Prescription {

	public Prescription_SURF(int type, IOrientableSurface geom) {
		super(type);
		this.geom = geom;
	}

	private IOrientableSurface geom;

	public IOrientableSurface geom() {
		return geom;
	}

}
