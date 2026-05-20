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

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiLineString;

/**
 * 
 * CityGML - Une route avec un axe central
 * 
 * @author Brasebin Mickaël
 *
 */
public class Road implements UrbanFeature {
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
	private MultiLineString axis;
	private String name;
	private String type;
	private double width;
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
		return name;
	}

	public void setRoadName(String name) {
		this.name = name;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public String getRoadType() {
		return type;
	}

	public void setRoadType(String type) {
		this.type = type;
	}

	public List<String> getUsages() {
		return usages;
	}

	public void setUsages(List<String> usages) {
		this.usages = usages;
	}

}
