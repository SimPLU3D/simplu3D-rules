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

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;

/**
 * 
 * Adjacent CadastralParcels with common owner
 * 
 * @see http://inspire.ec.europa.eu/featureconcept/BasicPropertyUnit
 * 
 * @author Brasebin Mickaël
 *
 */
public class BasicPropertyUnit extends DefaultFeature {
  

  /**
	 * Les parcelles cadastrales
	 */
	private List<CadastralParcel> cadastralParcels = new ArrayList<CadastralParcel>();
	/**
	 * Les bâtiments présents sur la BasicPropertyUnit
	 */
	private List<Building> buildings = new ArrayList<Building>();
	
	/**
	 * TODO remove this attribute and define geom when BasicPropertyUnit is created
	 */
	private IMultiSurface<IOrientableSurface> geometryFromChildren = null;

	/**
	 * TODO describe, duplicate geom?
	 */
	private IPolygon pol2D = null;
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
	 * @return
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
	 * 
	 * TODO remove and define geometry in BasicPropertyUnitGenerator
	 * 
	 * @return
	 */
	public IMultiSurface<IOrientableSurface> generateGeom() {
		if (geometryFromChildren == null) {
			geometryFromChildren = new GM_MultiSurface<IOrientableSurface>();
			for (CadastralParcel cP : this.getCadastralParcels()) {
				IGeometry geomP = cP.getGeom();

				if (geomP != null) {

					List<IOrientableSurface> lG = FromGeomToSurface.convertGeom(geomP);
					if (lG != null) {
						geometryFromChildren.addAll(lG);
					}
				}
			}
		}

		return geometryFromChildren;
	}


	public IPolygon getpol2D() {
		return pol2D;
	}

	public void setpol2D(IPolygon pol) {
		pol2D = pol;
	}


	public double getArea() {
		if (area == -1) {
			area = this.getpol2D().area();
		}
		return area;
	}
	

}
