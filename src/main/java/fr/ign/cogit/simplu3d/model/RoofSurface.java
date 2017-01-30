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

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.sig3d.indicator.RoofAngle;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;

/**
 * 
 * Une surface de toit
 * 
 * @author Brasebin Mickaël
 *
 */
public class RoofSurface extends DefaultFeature {

	private IMultiCurve<IOrientableCurve> roofing;
	private IMultiCurve<IOrientableCurve> gable;
	private IMultiCurve<IOrientableCurve> gutter;
	private IMultiCurve<IOrientableCurve> interiorEdge;

	private Materiau material;

	/**
	 * cached RoofAngle.angleMax
	 */
	private double angleMax = Double.NaN;
	/**
	 * cached RoofAngle.angleMin
	 */
	private double angleMin = Double.NaN;


	public IMultiCurve<IOrientableCurve> getGable() {
		return gable;
	}

	public void setGable(IMultiCurve<? extends IOrientableCurve> pignons) {
		this.gable = new GM_MultiCurve<IOrientableCurve>();
		this.gable.addAll(pignons);
	}

	public IMultiCurve<IOrientableCurve> getGutter() {
		return gutter;
	}

	public void setGutter(IMultiCurve<? extends IOrientableCurve> ligneGoutierre) {
		this.gutter = new GM_MultiCurve<IOrientableCurve>();
		this.gutter.addAll(ligneGoutierre);
	}

	public IMultiCurve<IOrientableCurve> getRoofing() {
		return roofing;
	}

	public void setRoofing(IMultiCurve<? extends IOrientableCurve> faitage) {
		this.roofing = new GM_MultiCurve<IOrientableCurve>();
		this.roofing.addAll(faitage);
	}

	public IMultiCurve<IOrientableCurve> getInteriorEdge() {
		return interiorEdge;
	}

	public void setInteriorEdge(IMultiCurve<IOrientableCurve> interiorEdge) {
		this.interiorEdge = interiorEdge;
	}

	public Materiau getMaterial() {
		return material;
	}

	public void setMaterial(Materiau mat) {
		this.material = mat;
	}

	public double getAngleMax() {
		if (angleMax == Double.NaN) {
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

	@SuppressWarnings("unchecked")
	public IMultiSurface<IOrientableSurface> getLod2MultiSurface() {
		return (IMultiSurface<IOrientableSurface>) this.getGeom();
	}

	@SuppressWarnings("unchecked")
	public Object clone() {
		RoofSurface tCopy = new RoofSurface();

		tCopy.setGeom((IGeometry) this.getGeom().clone());
		tCopy.setGutter((IMultiCurve<IOrientableCurve>) this.getGutter().clone());
		tCopy.setRoofing((IMultiCurve<IOrientableCurve>) this.getRoofing().clone());

		return tCopy;
	}
}
