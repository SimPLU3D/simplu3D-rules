/**
 * 
 * This software is released under the licence CeCILL
 * <p></p>
 * see LICENSE.TXT
 * <p></p>
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

import fr.ign.cogit.simplu3d.generator.RoofSurfaceGenerator;
import fr.ign.common.analysis.building.HauteurCalculation;
import fr.ign.common.analysis.building.StoreyCalculation;
import fr.ign.common.analysis.roof.RoofDetection;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.util.Box3D;
import fr.ign.common.util.Util;
import org.locationtech.jts.geom.*;

/**
 *
 * Un bâtiment abstrait (AbstractBuilding) qui est soit un Building soit un
 * BuildingPart et qui est composé de BuildingParts.
 * <p></p>
 * see CityGML
 * <a href="http://www.citygml.org/fileadmin/citygml/docs/CityGML_2_0_0_UML_diagrams.pdf">...</a>
 *
 * @author Brasebin Mickaël
 *
 */
public abstract class AbstractBuilding extends AbstractUrbanFeature {

	@Override
	public void setGeometry(Geometry geom) {
		this.geometry = geom;
		createElements();
	}
	private void createElements() {
		// Etape 1 : détection du toit et des façades
		List<Polygon> lOS = FromGeomToSurface.convertGeom(this.geometry);
		MultiPolygon surfaceRoof = RoofDetection.detectRoof(this, 0.2);
		// Util.detectRoof(lOS, 0.2);
		MultiPolygon surfaceWall = Util.detectVertical(lOS, 0.2);
		// Création facade
		WallSurface f = new WallSurface();
		f.setGeometry(surfaceWall);
		List<WallSurface> lF = new ArrayList<>();
		lF.add(f);
		this.setWallSurfaces(lF);
		// Etape 2 : on créé l'emprise du bâtiment
		// FIXME ???
		//footprint = FootprintGenerator.convert(surfaceRoof);
		Geometry union = (surfaceRoof != null) ? surfaceRoof.union() : geometry.union();
		if (union instanceof MultiPolygon) {
			union = union.buffer(1).union().buffer(-1);
		}
		if (union instanceof Polygon) {
			footprint = (Polygon) union;
		} else {
			footprint = null;
		}
		if (footprint != null) {
			// Création toit
			RoofSurface t = RoofSurfaceGenerator.create(surfaceRoof, (Polygon) footprint.copy());
			// Affectation
			this.setRoofSurface(t);
		}
	}
	private List<BuildingPart> buildingParts = new ArrayList<>();
	private RoofSurface roofSurface = null;

	private List<WallSurface> wallSurfaces;

	private String destination;
	private Polygon footprint;

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
	private List<SubParcel> subParcels = new ArrayList<>();

	private int storeysAboveGround = -1;

	// TODO check default value for CityGML (was not restored for storeysAboveGround)
	private double storeyHeightsAboveGround;

	protected AbstractBuilding() {
		super();
	}

/*
	@Deprecated
	public AbstractBuilding(Geometry geom) {
        this.setGeometry(geom);
		// Etape 1 : détection du toit et des façades
		List<Polygon> lOS = FromGeomToSurface.convertGeom(geom);
		MultiPolygon surfaceRoof = RoofDetection.detectRoof(this, 0.2);
		// Util.detectRoof(lOS, 0.2);
		MultiPolygon surfaceWall = Util.detectVertical(lOS, 0.2);
		// Création facade
		WallSurface f = new WallSurface();
		f.setGeometry(surfaceWall);
		List<WallSurface> lF = new ArrayList<>();
		lF.add(f);
		this.setWallSurfaces(lF);
		// Etape 2 : on créé l'emprise du bâtiment
		// FIXME ???
		//footprint = FootprintGenerator.convert(surfaceRoof);
		Geometry union = (surfaceRoof != null) ? surfaceRoof.union() : geom.union();
		if (union instanceof MultiPolygon) {
			union = union.buffer(1).union().buffer(-1);
		}
		if (union instanceof Polygon) {
			footprint = (Polygon) union;
		} else {
			footprint = null;
		}
		if (footprint != null) {
			// Création toit
			RoofSurface t = RoofSurfaceGenerator.create(surfaceRoof, (Polygon) footprint.copy());
			// Affectation
			this.setRoofSurface(t);
		}
	}
*/

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

	public MultiPolygon getLod2MultiSurface() {
		return (MultiPolygon) getGeometry();
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

	public void setFootprint(Polygon footprint) {
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
		this.wallSurfaces = new ArrayList<>();
		this.wallSurfaces.addAll(facades);
	}


	public Polygon getFootprint() {
		return footprint;
	}

	//// Geometric operators @TODO : Should we move this ?

	public abstract AbstractBuilding clone();

	public double height(int pB, int pH) {
		double zHaut = Double.NaN;
        zHaut = switch (pH) {
            case 0 -> HauteurCalculation.calculateZHautPPE(this);
            case 1 -> HauteurCalculation.calculateZhautMinRoof(this);
            case 2 -> HauteurCalculation.calculateZHautPHF(this);
            default -> zHaut;
        };
		double zBas = Double.NaN;
		switch (pB) {
			case 0:
				List<Geometry> geom = new ArrayList<>();
				List<SubParcel> lSubParcels = this.getSubParcels();
				if (lSubParcels.isEmpty()) {
					zBas = HauteurCalculation.calculateZBasPBB(this);
					break;
				}
				for (ParcelBoundary bP : lSubParcels.get(0).getBoundaries()) {
					geom.add(bP.getGeometry());
				}
				zBas = HauteurCalculation.calculateZBasEP(this, geom);
				break;
			case 1:
				zBas = HauteurCalculation.calculateZBasPBB(this);
				break;
			case 2:
				List<Geometry> geometriesParcel = new ArrayList<>();
				geometriesParcel.add(this.getSubParcels().get(0).getCadastralParcel().getGeometry());
				zBas = HauteurCalculation.calculateZBasPBT(geometriesParcel);
				break;
			case 3:
				List<Geometry> geometriesParcel2 = new ArrayList<>();
				geometriesParcel2.add(this.getSubParcels().get(0).getCadastralParcel().getGeometry());
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
	public boolean prospect(Geometry geom, double slope, double hIni) {
		double shift = 0;
		Box3D box = new Box3D(this.getGeometry());
		Coordinate[] dpl = this.getRoof().getGeometry().getCoordinates();
		double zMin = box.getLLDP().getZ();
		for (Coordinate dp : dpl) {
			if (geom.distance(new GeometryFactory().createPoint(dp)) * slope + hIni < shift + dp.getZ() - zMin) {
				return false;
			}
		}
		return true;
	}
}
