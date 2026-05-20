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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A SubParcel is a part of CadastralParcel cut according to an UrbaZone
 * 
 * @author Brasebin Mickaël
 *
 */
public class SubParcel implements UrbanFeature {
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
	/**
	 * Parent CadastralParcel
	 */
	private CadastralParcel cadastralParcel;

	/**
	 * UrbaZone corresponding to the SubParcel (nullable)
	 */
	private UrbaZone urbaZone = null;



	public List<ParcelBoundary> getBoundaries() {
		return boundaries;
	}

	/**
	 * average slope
	 */
	private double avgSlope;

	private List<AbstractBuilding> buildings = new ArrayList<>();

	/**
	 * Boundaries TODO check construction / remove if useless?
	 */
	private final List<ParcelBoundary> boundaries = new ArrayList<>();

	/**
	 * cached area
	 */
	private double area = -1;

	public SubParcel() {
	}

	public SubParcel(Geometry iMS) {
		this.setGeometry(iMS);
	}

	public void setCadastralParcel(CadastralParcel cadastralParcel) {
		this.cadastralParcel = cadastralParcel;
	}

	public CadastralParcel getCadastralParcel() {
		return cadastralParcel;
	}

	public UrbaZone getUrbaZone() {
		return urbaZone;
	}

	public void setZoneUrba(UrbaZone urbaZone) {
		this.urbaZone = urbaZone;
	}

	public double getAvgSlope() {
		return avgSlope;
	}

	public void setAvgSlope(Double avgSlope) {
		this.avgSlope = avgSlope;
	}

	
	
	
	public List<AbstractBuilding> getBuildingsParts() {
		return buildings;
	}

	public void setBuildingsParts(List<AbstractBuilding> buildingsParts) {
		this.buildings = buildingsParts;
	}


	public double getArea() {
		if (area == -1) {
			area = this.getGeometry().getArea();
		}
		return area;
	}

	

}
