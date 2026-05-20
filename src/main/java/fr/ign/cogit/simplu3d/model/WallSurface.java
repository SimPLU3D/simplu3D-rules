/**
 * 
 * This software is released under the licence CeCILL
 * <p>
 * see LICENSE.TXT
 * <p>
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

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;

/**
 * 
 * @author Brasebin Mickaël
 *
 */
public class WallSurface implements UrbanFeature {
	private Geometry geometry = null;
	public Geometry getGeometry() {
		return this.geometry;
	}
	public void setGeometry(Geometry geom) {
		this.geometry = geom;
	}
	private String id;
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}

	private WallSurfaceType type;
	private Materiau material;
	private boolean isWindowLess;

	public WallSurface() {
	}

	public WallSurface(WallSurfaceType type, boolean isWindowLess) {
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

	public MultiPolygon getLod2MultiSurface() {
		return (MultiPolygon) this.getGeometry();
	}
}
