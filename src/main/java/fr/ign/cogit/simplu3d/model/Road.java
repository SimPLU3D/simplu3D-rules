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

import java.util.List;

import org.locationtech.jts.geom.MultiLineString;

import static fr.ign.cogit.simplu3d.io.feature.RoadReader.*;

/**
 * 
 * CityGML - Une route avec un axe central
 * 
 * @author Brasebin Mickaël
 *
 */
public class Road extends AbstractUrbanFeature {
	private MultiLineString axis;
	//private String name;
	//private String type;
	//private double width;
	private List<String> usages;

	public Road() {
		super();
	}

	public Road(MultiLineString surfVoie) {
		this.setGeometry(surfVoie);
	}

	public MultiLineString getAxis() {
		return axis;
	}

	public void setAxis(MultiLineString axis) {
		this.axis = axis;
	}

	public String getRoadName() {
		return this.getAttribute(ATT_NOM_RUE).toString();
	}

	public void setRoadName(String name) {
		this.setAttribute(ATT_NOM_RUE, name);
		//this.name = name;
	}

	public double getWidth() {
		return Double.parseDouble(this.getAttribute(ATT_LARGEUR).toString());
	}

	public void setWidth(double width) {
		this.setAttribute(ATT_LARGEUR, width);
		//this.width = width;
	}

	public List<String> getUsages() {
		return usages;
	}

	public void setUsages(List<String> usages) {
		this.usages = usages;
	}

}
