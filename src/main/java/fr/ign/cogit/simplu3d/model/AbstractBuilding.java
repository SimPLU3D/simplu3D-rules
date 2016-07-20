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

import org.citygml4j.model.citygml.CityGML;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.sig3d.analysis.roof.RoofDetection;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.simplu3d.generator.FootprintGenerator;
import fr.ign.cogit.simplu3d.generator.RoofSurfaceGenerator;
import fr.ign.cogit.simplu3d.indicator.HauteurCalculation;
import fr.ign.cogit.simplu3d.indicator.StoreyCalculation;

/**
 * 
 * Un bâtiment abstrait (AbstractBuilding) qui est soit un Building soit un
 * BuildingPart et qui est composé de BuildingParts.
 * 
 * @see CityGML
 *      http://www.citygml.org/fileadmin/citygml/docs/CityGML_2_0_0_UML_diagrams
 *      .pdf#page=11&zoom=150,1,706
 * 
 * @author Brasebin Mickaël
 *
 */
public abstract class AbstractBuilding extends DefaultFeature {

	private List<BuildingPart> buildingParts = new ArrayList<BuildingPart>();
	private RoofSurface roofSurface = null;

	private List<SpecificWallSurface> wallSurfaces;

	private String destination;
	private IOrientableSurface footprint;

	/**
	 * TODO compute from BuildingParts?
	 */
	private List<SubParcel> subParcels = new ArrayList<SubParcel>();
	/**
	 * TODO compute from the first subParcel?
	 */
	private BasicPropertyUnit bPU;

	
	private int storeysAboveGround = -1;
	
	//TODO check default value for CityGML (was not restored for storeysAboveGround)
	private double storeyHeightsAboveGround;

	/**
	 * TODO go private
	 */
	public boolean isNew = false;
	
	protected AbstractBuilding() {
		super();
	}

	@Deprecated
	public AbstractBuilding(IGeometry geom) {
		this.setGeom(geom);

		// Etape 1 : détection du toit et des façades
		List<IOrientableSurface> lOS = FromGeomToSurface.convertGeom(geom);
		@SuppressWarnings("unchecked")
		IMultiSurface<IOrientableSurface> surfaceRoof = (IMultiSurface<IOrientableSurface>) RoofDetection
				.detectRoof(this, 0.2, true);

		// Util.detectRoof(lOS,
		// 0.2);
		IMultiSurface<IOrientableSurface> surfaceWall = Util.detectVertical(lOS, 0.2);

		// Création facade
		SpecificWallSurface f = new SpecificWallSurface();
		f.setGeom(surfaceWall);

		List<SpecificWallSurface> lF = new ArrayList<SpecificWallSurface>();
		lF.add(f);
		this.setFacade(lF);

		// Etape 2 : on créé l'emprise du bâtiment
		footprint = FootprintGenerator.convert(surfaceRoof);

		if (footprint != null) {
			// Création toit
			RoofSurface t = RoofSurfaceGenerator.create(surfaceRoof, (IPolygon) footprint.clone());

			// Affectation
			this.setRoofSurface(t);
		}
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	
	public List<SubParcel> getSubParcels() {
		return subParcels;
	}

	public void setSubParcels(List<SubParcel> subParcels) {
		this.subParcels = subParcels;
	}

	public BasicPropertyUnit getbPU() {
		return bPU;
	}

	public void setbPU(BasicPropertyUnit bPU) {
		this.bPU = bPU;
	}

	public RoofSurface getRoof() {
		return roofSurface;
	}

	public void setRoof(RoofSurface roof) {
		this.roofSurface = roof;
	}

	@SuppressWarnings("unchecked")
	public IMultiSurface<IOrientableSurface> getLod2MultiSurface() {
		return (IMultiSurface<IOrientableSurface>)getGeom();
	}

	public void setStoreysAboveGround(int storeysAboveGround) {
		this.storeysAboveGround = storeysAboveGround;
	}
	

	public double getStoreyHeightsAboveGround() {
		return storeyHeightsAboveGround;
	}

	public void setStoreyHeightsAboveGround(double storeyHeightsAboveGround) {
		this.storeyHeightsAboveGround = storeyHeightsAboveGround;
	}

	public List<BuildingPart> getBuildingPart() {
		return buildingParts;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setFootprint(IOrientableSurface footprint) {
		this.footprint = footprint;
	}

	public List<BuildingPart> consistsOfBuildingPart() {
		return buildingParts;
	}

	public void setBuildingPart(List<BuildingPart> buildingPart) {
		this.buildingParts = buildingPart;
	}

	public int getStoreysAboveGround() {
		if (this.storeysAboveGround == -1) {
			this.storeysAboveGround = StoreyCalculation.process(this);
		}

		return this.storeysAboveGround;
	}

	public void setRoofSurface(RoofSurface toit) {
		this.roofSurface = toit;
	}

	public List<SpecificWallSurface> getWallSurfaces() {
		return wallSurfaces;
	}

	public void setFacade(List<? extends SpecificWallSurface> facades) {
		this.wallSurfaces = new ArrayList<SpecificWallSurface>();
		this.wallSurfaces.addAll(facades);
	}


	public boolean isNew() {
		return isNew;
	}

	public IOrientableSurface getFootprint() {
		return footprint;
	}

	
	////Geometric operators @TODO : Should we move this ?
	
	public abstract AbstractBuilding clone();

	public double height(int pB, int pH) {
		double h = HauteurCalculation.calculate(this, pB, pH);
		return h;
	}

	/**
	 *
	 * @param geom
	 * @param slope
	 * @param hIni
	 * @return
	 */
	public boolean prospect(IGeometry geom, double slope, double hIni) {
		double zMin = 0;
		IDirectPositionList dpl = null;
		double shift = 0;
		Box3D box = new Box3D(this.getGeom());
		dpl = this.getRoof().getGeom().coord();
		zMin = box.getLLDP().getZ();
		for (IDirectPosition dp : dpl) {
			if (geom.distance(new GM_Point(dp)) * slope + hIni < shift + dp.getZ() - zMin) {
				return false;
			}
		}
		return true;
	}
}
