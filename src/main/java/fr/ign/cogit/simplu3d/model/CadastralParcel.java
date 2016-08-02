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
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToLineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;

/**
 * 
 * Une parcelle cadastrale (CadastralParcel) décomposée en sous parcelles
 * (SubParcel)
 * 
 * @author Brasebin Mickaël
 *
 */
public class CadastralParcel extends DefaultFeature {

	/**
	 * Parent BasicPropertyUnit
	 */
	private BasicPropertyUnit bPU;
	
	/**
	 * Children SubParcel
	 */
	private List<SubParcel> subParcels = new ArrayList<SubParcel>();

	/**
	 * Boundaries for the CadastralParcel
	 * 
	 * TODO see if it's possible to store boundaries only on subParcels
	 */
	private List<ParcelBoundary> boundaries = new ArrayList<ParcelBoundary>();

	/**
	 * Logical identifier of the CadastralParcel
	 * 
	 * For BDParcellaire (france) : INSEE_COMMUNE + COMM_ABS + SECTION + NUMERO (ex : 15012011AD250)
	 */
	private String code;
	
	/**
	 * TODO déplacer au niveau applicatif?
	 */
	private boolean hasToBeSimulated = false;

	/**
	 * (cached) Géométrie contenant la ligne contre laquelle un bâtiment doit être construit
	 */
	private IGeometry consLine = null;

	/**
	 * Cached area
	 */
	public double area = Double.NaN;

	public CadastralParcel() {
		super();
	}

	public CadastralParcel(IMultiSurface<IOrientableSurface> iMS) {
		super();

		this.setGeom(iMS);
	}

	public BasicPropertyUnit getbPU() {
		return bPU;
	}
	
	public void setbPU(BasicPropertyUnit bPU) {
		this.bPU = bPU;
	}


	public List<SubParcel> getSubParcels() {
		return subParcels;
	}
	
	public void setSubParcels(List<SubParcel> subParcels) {
		this.subParcels = subParcels;
	}

	
	public List<ParcelBoundary> getBoundaries() {
		return boundaries;
	}

	public List<ParcelBoundary> getBoundariesByType(ParcelBoundaryType type) {
		List<ParcelBoundary> result = new ArrayList<ParcelBoundary>();
		for (ParcelBoundary boundary : getBoundaries()) {
			if ( boundary.getType().equals(type) ) {
				result.add(boundary);
			}
		}
		return result;
	}

	public List<ParcelBoundary> getBoundariesBySide(ParcelBoundarySide side) {
		List<ParcelBoundary> result = new ArrayList<ParcelBoundary>();
		for (ParcelBoundary boundary : getBoundaries()) {
			if ( boundary.getSide().equals(side) ) {
				result.add(boundary);
			}
		}
		return result;
	}

	public void setBoundaries(List<ParcelBoundary> bordures) {
		this.boundaries = bordures;
	}

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public boolean hasToBeSimulated() {
		return hasToBeSimulated;
	}

	public void setHasToBeSimulated(boolean hasToBeSimulated) {
		this.hasToBeSimulated = hasToBeSimulated;
	}

	
	public IGeometry getConsLine() {
		if (consLine == null) {
			IMultiCurve<IOrientableCurve> iMS = new GM_MultiCurve<>();
			Collection<ParcelBoundary> sCP = getBoundariesByType(ParcelBoundaryType.ROAD);
			for (ParcelBoundary sCB : sCP) {
				iMS.addAll(FromGeomToLineString.convert(sCB.getGeom()));
			}
			consLine = iMS;
		}
		return consLine;
	}
	
	
	public double getArea() {
		if (Double.isNaN(area)) {
			area = this.getGeom().area();
		}
		return area;
	}
}
