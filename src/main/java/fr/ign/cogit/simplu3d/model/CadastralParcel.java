/**
 * 
 * This software is released under the licence CeCILL
 * <p></p>
 * see LICENSE.TXT
 *<p></p>
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

import fr.ign.common.conversion.FromGeomToLineString;
import org.locationtech.jts.geom.*;

/**
 * 
 * Une parcelle cadastrale (CadastralParcel) décomposée en sous parcelles
 * (SubParcel)
 * 
 * @author Brasebin Mickaël
 *
 */
public class CadastralParcel implements UrbanFeature {

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
	 * Parent BasicPropertyUnit
	 */
	private BasicPropertyUnit bPU;
	
	/**
	 * Children SubParcel
	 */
	private List<SubParcel> subParcels = new ArrayList<>();

	/**
	 * Boundaries for the CadastralParcel.
	 * <p></p>
	 * TODO see if it's possible to store boundaries only on subParcels
	 */
	private List<ParcelBoundary> boundaries = new ArrayList<>();

	/**
	 * Logical identifier of the CadastralParcel.
	 * <p></p>
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
	private Geometry consLine = null;

	/**
	 * Cached area
	 */
	private double area = Double.NaN;

	public CadastralParcel() {
	}

	public CadastralParcel(MultiPolygon iMS) {
		this.setGeometry(iMS);
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
		List<ParcelBoundary> result = new ArrayList<>();
		for (ParcelBoundary boundary : getBoundaries()) {
			if ( boundary.getParcelBoundaryType().equals(type) ) {
				result.add(boundary);
			}
		}
		return result;
	}
	
	public List<ParcelBoundary> getBoundariesByTypes(List<ParcelBoundaryType> types) {
		List<ParcelBoundary> result = new ArrayList<>();
		for (ParcelBoundary boundary : getBoundaries()) {
			for(ParcelBoundaryType type: types ){
				if ( boundary.getParcelBoundaryType().equals(type) ) {
					result.add(boundary);
				}	
			}
		}
		return result;
	}

	public List<ParcelBoundary> getBoundariesBySide(ParcelBoundarySide side) {
		List<ParcelBoundary> result = new ArrayList<>();
		for (ParcelBoundary boundary : getBoundaries()) {
			if ( boundary.getSide().equals(side) ) {
				result.add(boundary);
			}
		}
		return result;
	}

	public List<ParcelBoundary> getBoundariesBySides(List<ParcelBoundarySide> sides) {
		List<ParcelBoundary> result = new ArrayList<>();
		for (ParcelBoundary boundary : getBoundaries()) {
			for(ParcelBoundarySide side: sides ){
				if ( boundary.getSide().equals(side) ) {
					result.add(boundary);
				}
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
	
	public Geometry getConsLine() {
		if (consLine == null) {
			List<LineString> lineStrings = new ArrayList<>();
			Collection<ParcelBoundary> sCP = getBoundariesByType(ParcelBoundaryType.ROAD);
			for (ParcelBoundary sCB : sCP) {
				lineStrings.addAll(FromGeomToLineString.convert(sCB.getGeometry()));
			}
			consLine = new GeometryFactory().createMultiLineString(lineStrings.toArray(new LineString[0]));
		}
		return consLine;
	}

	public double getArea() {
		if (Double.isNaN(area)) {
			area = this.getGeometry().getArea();
		}
		return area;
	}
	@Override
	public String toString() {
		return "Parcel " + getId() + " - " + getGeometry();
	}
}
