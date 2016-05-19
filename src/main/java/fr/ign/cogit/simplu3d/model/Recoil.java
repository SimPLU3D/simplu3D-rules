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

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.ICurve;

/**
 * 
 * Recoil constraints object to be built at a distance (distance) of a reference object (objRef)
 * 
 * @author Brasebin Mickaël
 *
 */
public class Recoil extends Alignement {

	public Recoil(int type, ICurve geom) {
		super(type, geom);
	}

	public double distance;

	public IFeature objRef;

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public IFeature getObjRef() {
		return objRef;
	}

	public void setObjRef(IFeature objRef) {
		this.objRef = objRef;
	}

}
