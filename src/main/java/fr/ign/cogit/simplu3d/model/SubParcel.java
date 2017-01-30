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

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;

/**
 * 
 * A SubParcel is a part of CadastralParcel cut according to an UrbaZone
 * 
 * @author Brasebin Mickaël
 *
 */
public class SubParcel extends DefaultFeature {

	/**
	 * Parent CadastralParcel
	 */
	private CadastralParcel cadastralParcel;

	/**
	 * UrbaZone corresponding to the SubParcel (nullable)
	 */
	private UrbaZone urbaZone = null;

	public List<AbstractBuilding> getBuildings() {
		return buildings;
	}

	public List<ParcelBoundary> getBoundaries() {
		return boundaries;
	}

	/**
	 * average slope
	 */
	private double avgSlope;

	/**
	 * BuildingParts associated to the SubParcel
	 * 
	 * Note that an original input building can be split to match the SubParcel
	 * 
	 */
	public List<AbstractBuilding> buildings = new ArrayList<AbstractBuilding>();

	/**
	 * Boundaries TODO check construction / remove if useless?
	 */
	public List<ParcelBoundary> boundaries = new ArrayList<ParcelBoundary>();

	/**
	 * cached area
	 */
	private double area = -1;

	public SubParcel() {
		super();
	}

	public SubParcel(IGeometry iMS) {
		super();
		this.setGeom(iMS);
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
			area = this.getGeom().area();
		}
		return area;
	}

	@SuppressWarnings("unchecked")
	public IMultiSurface<IOrientableSurface> getLod2MultiSurface() {
		return (IMultiSurface<IOrientableSurface>) this.getGeom();
	}
	
	

}
