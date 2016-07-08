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
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundarySide;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundaryType;

/**
 * 
 * Une parcelle cadastrale (CadastralParcel) décomposée en sous parcelles
 * (SubParcel)
 * 
 * @author Brasebin Mickaël
 *
 */
public class CadastralParcel extends DefaultFeature {

	public final String CLASSE = "Parcelle";

	/**
	 * TODO déplacer au niveau applicatif?
	 */
	private boolean hasToBeSimulated = false;

	public IFeatureCollection<SubParcel> subParcels = new FT_FeatureCollection<SubParcel>();
	public IFeatureCollection<SpecificCadastralBoundary> specificCB = new FT_FeatureCollection<SpecificCadastralBoundary>();

	public BasicPropertyUnit bPU;

	private String parcelle_id;

	public String getParcelle_id() {
		return parcelle_id;
	}

	public void setParcelle_id(String parcelle_id) {
		this.parcelle_id = parcelle_id;
	}

	/**
	 * Géométrie contenant la ligne contre laquelle un bâtiment doit être
	 * construit
	 */
	private IGeometry consLine = null;

	public BasicPropertyUnit getbPU() {
		return bPU;
	}

	public void setbPU(BasicPropertyUnit bPU) {
		this.bPU = bPU;
	}

	public double area = Double.NaN;

	public CadastralParcel() {
		super();
	}

	public CadastralParcel(IMultiSurface<IOrientableSurface> iMS) {
		super();

		this.setGeom(iMS);

	}

	public boolean hasToBeSimulated() {
		return hasToBeSimulated;
	}

	public void setHasToBeSimulated(boolean bool) {
		hasToBeSimulated = bool;
	}

	public IFeatureCollection<SpecificCadastralBoundary> getSpecificCadastralBoundary() {
		return specificCB;
	}

	public void setSpecificCadastralBoundary(IFeatureCollection<SpecificCadastralBoundary> bordures) {
		this.specificCB = bordures;
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

	public IFeatureCollection<SubParcel> getSubParcel() {
		return subParcels;
	}

	public void setSubParcel(IFeatureCollection<SubParcel> sousParcelles) {
		this.subParcels = sousParcelles;
	}

	public double getArea() {

		if (Double.isNaN(area)) {
			area = this.getGeom().area();
		}

		return area;
	}

	public void setArea(double area) {
		this.area = area;
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

}
