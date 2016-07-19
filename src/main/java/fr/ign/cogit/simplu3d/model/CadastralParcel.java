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

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
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
	 * Logical identifier of the CadastralParcel
	 * 
	 * For BDParcellaire (france) : INSEE_COMMUNE + COMM_ABS + SECTION + NUMERO (ex : 15012011AD250)
	 */
	private String code;

	public List<SubParcel> subParcels = new ArrayList<SubParcel>();
	public List<SpecificCadastralBoundary> specificCB = new ArrayList<SpecificCadastralBoundary>();

	public BasicPropertyUnit bPU;

	/**
	 * (cache) Géométrie contenant la ligne contre laquelle un bâtiment doit être construit
	 */
	private IGeometry consLine = null;

	/**
	 * TODO déplacer au niveau applicatif?
	 */
	private boolean hasToBeSimulated = false;

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
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public BasicPropertyUnit getbPU() {
		return bPU;
	}



	public IGeometry getConsLine() {

		if (consLine == null) {

			IMultiCurve<IOrientableCurve> iMS = new GM_MultiCurve<>();

			IFeatureCollection<SpecificCadastralBoundary> sCP = this
					.getSpecificCadastralBoundaryByType(SpecificCadastralBoundaryType.ROAD);

			for (SpecificCadastralBoundary sCB : sCP) {

				iMS.addAll(FromGeomToLineString.convert(sCB.getGeom()));
			}

			consLine = iMS;
		}

		return consLine;
	}

	public List<SpecificCadastralBoundary> getSpecificCadastralBoundary() {
		return specificCB;
	}

	public IFeatureCollection<SpecificCadastralBoundary> getSpecificCadastralBoundaryByType(
			SpecificCadastralBoundaryType type) {
		IFeatureCollection<SpecificCadastralBoundary> borduresLat = new FT_FeatureCollection<SpecificCadastralBoundary>();
		for (SpecificCadastralBoundary b : this.specificCB) {
			if (b.getType() == type) {
				borduresLat.add(b);
			}

		}
		return borduresLat;
	}

	public IFeatureCollection<SpecificCadastralBoundary> getSpecificSideBoundary(
			SpecificCadastralBoundarySide scbSide) {
		FT_FeatureCollection<SpecificCadastralBoundary> featC = new FT_FeatureCollection<>();

		for (SpecificCadastralBoundary sc : specificCB) {

			if (sc.getSide() == scbSide) {
				featC.add(sc);
			}

		}

		return featC;
	}

	public List<SubParcel> getSubParcel() {
		return subParcels;
	}

	public boolean hasToBeSimulated() {
		return hasToBeSimulated;
	}

	public void setbPU(BasicPropertyUnit bPU) {
		this.bPU = bPU;
	}


	public void setHasToBeSimulated(boolean bool) {
		hasToBeSimulated = bool;
	}

	public void setSpecificCadastralBoundary(List<SpecificCadastralBoundary> bordures) {
		this.specificCB = bordures;
	}
	
	public void setSubParcel(List<SubParcel> sousParcelles) {
		this.subParcels = sousParcelles;
	}
	
	public double getArea() {
		if (Double.isNaN(area)) {
			area = this.getGeom().area();
		}
		return area;
	}
}
