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

import java.util.List;

import org.citygml4j.model.citygml.landuse.LandUse;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.model.citygml.landuse.CG_LandUse;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundaryType;

/**
 * 
 * @author Brasebin Mickaël
 *
 */
public class SubParcel extends CG_LandUse {

	public final String CLASSE = "SousParcelle";

	private CadastralParcel parcelle;

	/**
	 * TODO replace by ZoneUrba
	 */
	private int idZU = 0;
	private int idCp = 0;
	private double avgSlope;
	private double area = -1;
	private UrbaZone zU = null;

	/**
	 * BuildingParts associated to the SubParcel
	 * 
	 * Note that an original input building can be split to match the SubParcel
	 * 
	 */
	public IFeatureCollection<AbstractBuilding> buildingsParts = new FT_FeatureCollection<AbstractBuilding>();
	
	/**
	 * Boundaries
	 * TODO check construction / remove if useless?
	 */
	public IFeatureCollection<SpecificCadastralBoundary> sCBoundary = new FT_FeatureCollection<SpecificCadastralBoundary>();

	
	public UrbaZone getUrbaZone() {
		return zU;
	}

	public void setZoneUrba(UrbaZone zU) {
		this.zU = zU;
	}

	public void setIdZoneUrba(int idZU) {
		this.idZU = idZU;
	}

	public int getIdZoneUrba() {
		return idZU;
	}

	public void setIdCadPar(int idCp) {
		this.idCp = idCp;
	}

	public int getIdCadPar() {
		return idCp;
	}

	public double builtRatio() {
		return 0;
	}

	public double FARVolume() {
		return 0;
	}

	public double FARRatio() {
		return 0;
	}

	public SubParcel(IGeometry iMS) {
		super();
		this.setGeom(iMS);
		this.setClazz(CLASSE);
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public double getAvgSlope() {
		return avgSlope;
	}

	public void setAvgSlope(Double avgSlope) {
		this.avgSlope = avgSlope;
	}

	public double getArea() {

		if (area == -1) {
			area = this.getGeom().area();
		}
		return area;
	}

	public void setSpecificCadBoundary(IFeatureCollection<SpecificCadastralBoundary> spCBoundary) {
		this.sCBoundary = spCBoundary;
	}

	public List<SpecificCadastralBoundary> getSpecificCadastralBoundary() {
		return sCBoundary.getElements();
	}

	public IFeatureCollection<SpecificCadastralBoundary> getSpecificCadastralBoundaryColl() {
		return sCBoundary;
	}

	public SubParcel() {
		super();
		this.setClazz(CLASSE);

	}

	public SubParcel(LandUse landUse) {
		super(landUse);

		this.setClazz(CLASSE);

	}

	public IFeatureCollection<SpecificCadastralBoundary> getBorduresFond() {
		IFeatureCollection<SpecificCadastralBoundary> borduresFond = new FT_FeatureCollection<SpecificCadastralBoundary>();
		for (SpecificCadastralBoundary b : this.sCBoundary) {
			if (b.getType() == SpecificCadastralBoundaryType.BOT) {
				borduresFond.add(b);
			}

		}
		return borduresFond;
	}

	public IFeatureCollection<SpecificCadastralBoundary> getBorduresLat() {
		IFeatureCollection<SpecificCadastralBoundary> borduresLat = new FT_FeatureCollection<SpecificCadastralBoundary>();
		for (SpecificCadastralBoundary b : this.sCBoundary) {
			if (b.getType() == SpecificCadastralBoundaryType.LAT) {
				borduresLat.add(b);
			}

		}
		return borduresLat;
	}

	public IFeatureCollection<SpecificCadastralBoundary> getBorduresRoad() {
		IFeatureCollection<SpecificCadastralBoundary> borduresLat = new FT_FeatureCollection<SpecificCadastralBoundary>();
		for (SpecificCadastralBoundary b : this.sCBoundary) {
			if (b.getType() == SpecificCadastralBoundaryType.ROAD) {
				borduresLat.add(b);
			}

		}
		return borduresLat;
	}

	public void setParcelle(CadastralParcel cP) {
		this.parcelle = cP;
	}

	public CadastralParcel getParcel() {
		return parcelle;
	}

	public IFeatureCollection<AbstractBuilding> getBuildingsParts() {
		return buildingsParts;
	}

	public void setBuildingsParts(IFeatureCollection<AbstractBuilding> buildingsParts) {
		this.buildingsParts = buildingsParts;
	}

	/**
	 * Compute CES
	 * TODO externalize
	 * @return
	 */
	public double getces() {

		double area = this.getArea();

		int bP = this.getBuildingsParts().size();

		if (bP == 0) {
			return 0;
		}

		Geometry geom = null;
		try {
			geom = JtsGeOxygene.makeJtsGeom(this.getBuildingsParts().get(0).getFootprint());

			for (int i = 0; i < bP; i++) {
				geom = geom.union(JtsGeOxygene.makeJtsGeom(this.getBuildingsParts().get(i).getFootprint()));

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		if (geom == null) {
			return 0;
		}

		return geom.getArea() / area;

	}

	public IGeometry getConsLine() {
		SubParcel.getLogger().error("Empty method getConsline()");
		return null;
	}

}
