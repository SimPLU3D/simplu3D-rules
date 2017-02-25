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

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;

/**
 * 
 * Frontière de parcelle
 * 
 * 
 * Note that SpecificCadastralBoundary forms a topology map
 * 
 * @author Brasebin Mickaël
 *
 */
public class ParcelBoundary extends DefaultFeature {
	/**
	 * CadastralParcel owning the boundary
	 */
	private CadastralParcel cadastralParcel;
	/**
	 * The type of the boundary
	 */
	private ParcelBoundaryType type;
	/**
	 * The side of the boundary (relative to the parcel)
	 */
	private ParcelBoundarySide side;

	/**
	 * Optional Road linked to the boundary
	 */
	private Road road = null ;
	
	
	/**
	 * Optional oppositBoundary
	 */
	private ParcelBoundary oppositeBoundary;
	

	/**
	 * TODO see if a list is required
	 */
	private Prescription prescription = null;


	public ParcelBoundary(){
		
	}
	
	public ParcelBoundary(IGeometry geom) {
		this.setGeom(geom);
	}
	
	
	public CadastralParcel getCadastralParcel() {
		return cadastralParcel;
	}

	public void setCadastralParcel(CadastralParcel cadastralParcel) {
		this.cadastralParcel = cadastralParcel;
	}

	public ParcelBoundaryType getType() {
		return type;
	}

	public void setType(ParcelBoundaryType type) {
		this.type = type;
	}


	public ParcelBoundarySide getSide() {
		return side;
	}

	public void setSide(ParcelBoundarySide side) {
		this.side = side;
	}
	
	
	public Road getRoad() {
		return road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

	public Prescription getPrescription() {
		return prescription;
	}

	public void setAlignement(Prescription prescription) {
		this.prescription = prescription;
	}

	
	public ParcelBoundary getOppositeBoundary() {
		return oppositeBoundary;
	}

	public void setOppositeBoundary(ParcelBoundary oppositeBoundary) {
		this.oppositeBoundary = oppositeBoundary;
	}

	/**
	 * Returns the Road if defined, else the CadastralParcel
	 * @return
	 */
	@Deprecated
	public IFeature getFeatAdj(){
		if ( this.road != null ){
			return this.road ;
		}else{
			return this.cadastralParcel;
		}
	}
	
}