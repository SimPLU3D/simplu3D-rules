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

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;

/**
 * 
 * @author Brasebin Mickaël
 *
 */
public class WallSurface extends DefaultFeature {

	private WallSurfaceType type;
	private Materiau material;
	private boolean isWindowLess;

	public WallSurface() {
		super();
	}

	public WallSurface(WallSurfaceType type, boolean isWindowLess) {
		super();
		this.type = type;
		this.isWindowLess = isWindowLess;
	}

	public Materiau getMat() {
		return material;
	}

	public void setMat(Materiau mat) {
		this.material = mat;
	}

	public boolean isWindowLess() {
		return isWindowLess;
	}

	public void setWindowLess(boolean isWindowLess) {
		this.isWindowLess = isWindowLess;
	}

	public WallSurfaceType getType() {
		return type;
	}

	public void setType(WallSurfaceType type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public IMultiSurface<IOrientableSurface> getLod2MultiSurface() {
		return (IMultiSurface<IOrientableSurface>) this.getGeom();
	}

}
