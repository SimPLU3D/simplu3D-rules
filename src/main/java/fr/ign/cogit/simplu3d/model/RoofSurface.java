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

import fr.ign.common.analysis.roof.RoofAngle;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;

/**
 * 
 * Une surface de toit
 * 
 * @author Brasebin Mickaël
 *
 */
public class RoofSurface extends AbstractUrbanFeature {
	private MultiLineString roofing;
	private MultiLineString gable;
	private MultiLineString gutter;
	private MultiLineString interiorEdge;

	private Materiau material;

	/**
	 * cached RoofAngle.angleMax
	 */
	private double angleMax = Double.NaN;
	/**
	 * cached RoofAngle.angleMin
	 */
	private double angleMin = Double.NaN;

	public RoofSurface() {
	}


	public MultiLineString getGable() {
		return gable;
	}

	public void setGable(MultiLineString pignons) {
		this.gable = (MultiLineString) pignons.copy();
	}

	public MultiLineString getGutter() {
		return gutter;
	}

	public void setGutter(MultiLineString ligneGoutierre) {
		this.gutter = (MultiLineString) ligneGoutierre.copy();
	}

	public MultiLineString getRoofing() {
		return roofing;
	}

	public void setRoofing(MultiLineString faitage) {
		this.roofing = (MultiLineString) faitage.copy();
	}

	public MultiLineString getInteriorEdge() {
		return interiorEdge;
	}

	public void setInteriorEdge(MultiLineString interiorEdge) {
		this.interiorEdge = interiorEdge;
	}

	public Materiau getMaterial() {
		return material;
	}

	public void setMaterial(Materiau mat) {
		this.material = mat;
	}

	public double getAngleMax() {
		if (Double.isNaN(angleMax)) {
			angleMax = RoofAngle.angleMax(this);
		}
		return angleMax;
	}

	public double getAngleMin() {
		if (Double.isNaN(angleMin)) {
			angleMin = RoofAngle.angleMin(this);
		}
		return angleMin;
	}

	public MultiPolygon getLod2MultiSurface() {
		return (MultiPolygon) this.getGeometry();
	}

	public RoofSurface copy() {
		RoofSurface tCopy = new RoofSurface();
		tCopy.setGeometry(this.getGeometry().copy());
		tCopy.setGutter((MultiLineString) this.getGutter().copy());
		tCopy.setRoofing((MultiLineString) this.getRoofing().copy());
		return tCopy;
	}
}
