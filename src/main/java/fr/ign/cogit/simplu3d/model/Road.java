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

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.sig3d.model.citygml.transportation.CG_Road;

/**
 * 
 * CityGML - Une route avec un axe central
 * 
 * @author Brasebin Mickaël
 *
 */
public class Road extends CG_Road {

	private IMultiCurve<ILineString> axis;
	public String name;
	public String type;
	public int idRoad;
	private double width;


	public Road() {
		super();
	}

	public Road(org.citygml4j.model.citygml.transportation.Road tO) {
		super(tO);
	}

	public Road(IMultiSurface<IOrientableSurface> surfVoie) {
		this.setLod2MultiSurface(surfVoie);
		this.setGeom(surfVoie);
	}

	public IMultiCurve<ILineString> getAxis() {
		return axis;
	}

	public void setAxe(IMultiCurve<ILineString> axis) {
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

	public int getIdRoad() {
		return idRoad;
	}

	public void setIdRoad(int idRoad) {
		this.idRoad = idRoad;
	}



}
