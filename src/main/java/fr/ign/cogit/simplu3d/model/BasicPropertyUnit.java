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

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;

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
	 * Les bâtiments présents sur la parcelle
	 */
	public List<Building> buildings = new ArrayList<Building>();
	/**
	 * Les parcelles cadastrales
	 */
	public List<CadastralParcel> cadastralParcels = new ArrayList<CadastralParcel>();

	public BasicPropertyUnit() {

	}

	public List<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}

	public List<CadastralParcel> getCadastralParcel() {
		return cadastralParcels;
	}

	public void setCadastralParcel(List<CadastralParcel> cadastralParcel) {
		this.cadastralParcels = cadastralParcel;
	}

	IMultiSurface<IOrientableSurface> geom = null;

	public IMultiSurface<IOrientableSurface> generateGeom() {

		if (geom == null) {
			geom = new GM_MultiSurface<IOrientableSurface>();
			for (CadastralParcel cP : this.getCadastralParcel()) {
				IGeometry geomP = cP.getGeom();

				if (geomP != null) {

					List<IOrientableSurface> lG = FromGeomToSurface.convertGeom(geomP);
					if (lG != null) {
						geom.addAll(lG);
					}
				}

			}
		}

		return geom;
	}

	private IPolygon pol2D = null;

	public IPolygon getpol2D() {

		return pol2D;
	}

	public void setpol2D(IPolygon pol) {
		pol2D = pol;
	}

	Geometry geomjts = null;

	public Geometry getGeomJTS() {

		if (geomjts == null) {
			try {
				geomjts = JtsGeOxygene.makeJtsGeom(geom);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return geomjts;
	}

	public String toString() {

		return "" + id;

	}

	private double area = -1;

	public double getArea() {
		if (area == -1) {
			area = this.getpol2D().area();
		}
		return area;
	}

}
