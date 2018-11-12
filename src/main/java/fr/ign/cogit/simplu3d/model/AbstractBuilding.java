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

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.sig3d.analysis.roof.RoofDetection;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.sig3d.indicator.HauteurCalculation;
import fr.ign.cogit.geoxygene.sig3d.indicator.StoreyCalculation;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.simplu3d.generator.FootprintGenerator;
import fr.ign.cogit.simplu3d.generator.RoofSurfaceGenerator;

/**
 * 
 * Un bâtiment abstrait (AbstractBuilding) qui est soit un Building soit un
 * BuildingPart et qui est composé de BuildingParts.
 * 
 * see CityGML
 * http://www.citygml.org/fileadmin/citygml/docs/CityGML_2_0_0_UML_diagrams .pdf
 * 
 * @author Brasebin Mickaël
 *
 */
public abstract class AbstractBuilding extends DefaultFeature {

	private List<BuildingPart> buildingParts = new ArrayList<BuildingPart>();
	private RoofSurface roofSurface = null;

	private List<WallSurface> wallSurfaces;

	private String destination;
	private IOrientableSurface footprint;

	private boolean isNew = false;

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * TODO compute from BuildingParts?
	 */
	private List<SubParcel> subParcels = new ArrayList<SubParcel>();

	private int storeysAboveGround = -1;

	// TODO check default value for CityGML (was not restored for
	// storeysAboveGround)
	private double storeyHeightsAboveGround;

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
		WallSurface f = new WallSurface();
		f.setGeom(surfaceWall);

		List<WallSurface> lF = new ArrayList<WallSurface>();
		lF.add(f);
		this.setWallSurfaces(lF);

		// Etape 2 : on créé l'emprise du bâtiment
		footprint = FootprintGenerator.convert(surfaceRoof);

		if (footprint != null) {
			// Création toit
			RoofSurface t = RoofSurfaceGenerator.create(surfaceRoof, (IPolygon) footprint.clone());

			// Affectation
			this.setRoofSurface(t);
		}
	}

	public List<SubParcel> getSubParcels() {
		return subParcels;
	}

	public void setSubParcels(List<SubParcel> subParcels) {
		this.subParcels = subParcels;
	}

	public RoofSurface getRoof() {
		return roofSurface;
	}

	public void setRoof(RoofSurface roof) {
		this.roofSurface = roof;
	}

	@SuppressWarnings("unchecked")
	public IMultiSurface<IOrientableSurface> getLod2MultiSurface() {
		return (IMultiSurface<IOrientableSurface>) getGeom();
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

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setFootprint(IOrientableSurface footprint) {
		this.footprint = footprint;
	}

	public List<BuildingPart> getBuildingParts() {
		return buildingParts;
	}

	public void setBuildingParts(List<BuildingPart> buildingParts) {
		this.buildingParts = buildingParts;
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

	public List<WallSurface> getWallSurfaces() {
		return wallSurfaces;
	}

	public void setWallSurfaces(List<? extends WallSurface> facades) {
		this.wallSurfaces = new ArrayList<WallSurface>();
		this.wallSurfaces.addAll(facades);
	}


	public IOrientableSurface getFootprint() {
		return footprint;
	}

	//// Geometric operators @TODO : Should we move this ?

	public abstract AbstractBuilding clone();

	public double height(int pB, int pH) {

		double zHaut = Double.NaN;

		switch (pH) {
		case 0:
			zHaut = HauteurCalculation.calculateZHautPPE(this);
			break;
		case 1:
			zHaut = HauteurCalculation.calculateZhautMinRoof(this);
			break;
		case 2:
			zHaut = HauteurCalculation.calculateZHautPHF(this);
			break;

		}
		double zBas = Double.NaN;

		switch (pB) {
		case 0:
			List<IGeometry> geom = new ArrayList<>();
			List<SubParcel> lSubParcels = this.getSubParcels();
			if (lSubParcels.isEmpty()) {
				zBas = HauteurCalculation.calculateZBasPBB(this);
				break;
			}
			for (ParcelBoundary bP : lSubParcels.get(0).getBoundaries()) {
				geom.add(bP.getGeom());
			}
			zBas = HauteurCalculation.calculateZBasEP(this, geom);
			break;
		case 1:
			zBas = HauteurCalculation.calculateZBasPBB(this);
			break;
		case 2:
			List<IGeometry> geometriesParcel = new ArrayList<>();
			geometriesParcel.add(this.getSubParcels().get(0).getCadastralParcel().getGeom());
			zBas = HauteurCalculation.calculateZBasPBT(geometriesParcel);
			break;
		case 3:
			List<IGeometry> geometriesParcel2 = new ArrayList<>();
			geometriesParcel2.add(this.getSubParcels().get(0).getCadastralParcel().getGeom());
			zBas = HauteurCalculation.calculateZBasPHT(geometriesParcel2);
			break;

		}

		return zHaut - zBas;
	}

	/**
	 * Calculate a prospect constraints
	 * 
	 * @param geom  the geometry from which the constraint is assessed
	 * @param slope the slope of the constraints
	 * @param hIni  the initial hight
	 * @return true if the constraint is respected if not false
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
