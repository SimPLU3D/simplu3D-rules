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

import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;

/**
 * 
 * CityGML - Une route avec un axe central
 * 
 * @author Brasebin Mickaël
 *
 */
public class Road extends DefaultFeature {

	private IMultiCurve<ILineString> axis;
	private String name;
	private String type;
	private double width;
	private List<String> usages;

	public Road() {
		super();
	}

	public Road(IMultiSurface<IOrientableSurface> surfVoie) {
		this.setGeom(surfVoie);
	}

	public IMultiCurve<ILineString> getAxis() {
		return axis;
	}

	public void setAxis(IMultiCurve<ILineString> axis) {
		this.axis = axis;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getUsages() {
		return usages;
	}

	public void setUsages(List<String> usages) {
		this.usages = usages;
	}

}
