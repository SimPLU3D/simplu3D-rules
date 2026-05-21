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

import fr.ign.common.conversion.FromGeomToSurface;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Adjacent CadastralParcels with common owner
 * <p>
 * see <a href="http://inspire.ec.europa.eu/featureconcept/BasicPropertyUnit">...</a>
 *
 * @author Brasebin Mickaël
 *
 */
public class BasicPropertyUnit extends AbstractUrbanFeature {

  /**
	 * Les parcelles cadastrales
	 */
	private List<CadastralParcel> cadastralParcels = new ArrayList<>();
	/**
	 * Les bâtiments présents sur la BasicPropertyUnit
	 */
	private List<Building> buildings = new ArrayList<>();
	
	/**
	 * TODO remove this attribute and define geom when BasicPropertyUnit is created
	 */
	private MultiPolygon geometryFromChildren = null;

	/**
	 * TODO describe, duplicate geom?
	 */
	private Polygon pol2D = null;
	/**
	 * cached area
	 */
	private double area = -1;
	
	public BasicPropertyUnit() {
    }

	public List<CadastralParcel> getCadastralParcels() {
		return cadastralParcels;
	}

	public void setCadastralParcels(List<CadastralParcel> cadastralParcels) {
		this.cadastralParcels = cadastralParcels;
	}

	/**
	 * Helper to get all SubParcels
	 * @return the sub parcels of the parcels that belong to this basic property unit
	 */
	public List<SubParcel> getSubParcels(){
		List<SubParcel> subParcels = new ArrayList<>();
		for (CadastralParcel cadastralParcel : cadastralParcels) {
			subParcels.addAll(cadastralParcel.getSubParcels());
		}
		return subParcels;
	}
	

	public List<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}


	/**
	 * Build geometry as the union of CadastralParcel's geometries
	 * <p>
	 * TODO remove and define geometry in BasicPropertyUnitGenerator
	 * 
	 * @return generate a geometry from the cadastral parcels that belong to the basicproperty unit
	 */
	public MultiPolygon generateGeom() {
		if (geometryFromChildren == null) {
			GeometryFactory factory = new GeometryFactory();
			List<Polygon> polygons =  new ArrayList<>();
 			for (CadastralParcel cP : this.getCadastralParcels()) {
				Geometry geomP = cP.getGeometry();
				if (geomP != null) {
					List<Polygon> lG = FromGeomToSurface.convertGeom(geomP);
                    polygons.addAll(lG);
                }
			}
			geometryFromChildren = factory.createMultiPolygon(polygons.toArray(new Polygon[0]));
		}

		return geometryFromChildren;
	}


	public Polygon getPol2D() {
		return pol2D;
	}

	public void setPol2D(Polygon pol) {
		pol2D = pol;
	}


	public double getArea() {
		if (area == -1) {
			area = this.getPol2D().getArea();
		}
		return area;
	}

	@Override
	public String toString() {
		return "BPU " + getId() + " - " + getGeometry() + " - with " + getCadastralParcels().size() + " parcels";
	}
}
