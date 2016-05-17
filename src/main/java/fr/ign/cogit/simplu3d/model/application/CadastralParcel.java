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
package fr.ign.cogit.simplu3d.model.application;

import org.citygml4j.model.citygml.landuse.LandUse;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToLineString;
import fr.ign.cogit.geoxygene.sig3d.model.citygml.landuse.CG_LandUse;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;

/**
 * 
 * Une parcelle cadastrale (CadastralParcel) décomposée en sous parcelles (SubParcel)
 * 
 * @author Brasebin Mickaël
 *
 */
public class CadastralParcel extends CG_LandUse {

	public final String CLASSE = "Parcelle";
	private int num = 0;

	/**
	 * TODO déplacer au niveau applicatif?
	 */
	private boolean hasToBeSimulated = false;

	public IFeatureCollection<SubParcel> subParcels = new FT_FeatureCollection<SubParcel>();
	public IFeatureCollection<SpecificCadastralBoundary> specificCB = new FT_FeatureCollection<SpecificCadastralBoundary>();

	public BasicPropertyUnit bPU;

	/**
	 * Géométrie contenant la ligne contre laquelle un bâtiment doit être construit
	 */
	private IGeometry consLine = null;

	
	public BasicPropertyUnit getbPU() {
		return bPU;
	}

	public void setbPU(BasicPropertyUnit bPU) {
		this.bPU = bPU;
	}


	public void setNum(int num) {
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public double area = Double.NaN;

	public CadastralParcel() {
		super();
	}

	public CadastralParcel(IMultiSurface<IOrientableSurface> iMS) {
		super();

		this.setLod2MultiSurface(iMS);
		this.setGeom(iMS);

		this.setClazz(CLASSE);

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

	public IFeatureCollection<SpecificCadastralBoundary> getSpecificSideBoundary(int side) {
		FT_FeatureCollection<SpecificCadastralBoundary> featC = new FT_FeatureCollection<>();

		for (SpecificCadastralBoundary sc : specificCB) {

			if (sc.getSide() == side) {
				featC.add(sc);
			}

		}

		return featC;
	}

	public CadastralParcel(LandUse landUse) {
		super(landUse);

		this.setClazz(CLASSE);

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

			IFeatureCollection<SpecificCadastralBoundary> sCP = this.getBorduresFront();

			for (SpecificCadastralBoundary sCB : sCP) {

				iMS.addAll(FromGeomToLineString.convert(sCB.getGeom()));
			}

			consLine = iMS;
		}

		return consLine;

	}

	public IFeatureCollection<SpecificCadastralBoundary> getBorduresFront() {
		IFeatureCollection<SpecificCadastralBoundary> borduresLat = new FT_FeatureCollection<SpecificCadastralBoundary>();
		for (SpecificCadastralBoundary b : this.specificCB) {
			if (b.getType() == SpecificCadastralBoundary.ROAD) {
				borduresLat.add(b);
			}

		}
		return borduresLat;
	}
}
