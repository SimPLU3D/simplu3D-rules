/**
 * 
 * This software is released under the licence CeCILL
 * <p>
 * see LICENSE.TXT
 * <p>
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

import fr.ign.cogit.simplu3d.dao.*;
import fr.ign.common.dtm.AbstractDTM;
import org.locationtech.jts.geom.Coordinate;

/**
 * 
 * Regroupe les différents éléments nécessaire pour la vérification et la
 * simulation des règles d'urbanisme.
 * 
 * @author Brasebin Mickaël
 *
 */
public class Environnement {

	/**
	 * Indicate if the global environnement is translated (may be done to avoid
	 * problem due to big coordinates effect)
	 */
	public static boolean TRANSLATE_TO_ZERO = false;
	/**
	 * Indicate if activated the translation vector
	 */
	public static Coordinate dpTranslate = null;

    private UrbaDocument urbaDoc;

	private SpatialRepository<BasicPropertyUnit> bpU = null;//TODO
	private SpatialRepository<CadastralParcel> cadastralParcels = null;//TODO
	private SpatialRepository<SubParcel> subParcels = null;//TODO

	private SpatialRepository<Building> buildings = null;//TODO

	private AbstractRepository<UrbaZone> urbaZones = null;//TODO
	private SpatialRepository<Prescription> prescriptions = null;//TODO

	private AbstractDTM terrain;
	private SpatialRepository<Road> roads = null;//TODO

	protected Environnement() {

	}

	/**
	 * Create a new environment instance
	 * 
	 * @return a new environnement and erase the existing one if necesesary
	 */
	public static Environnement createEnvironnement() {
        return new Environnement();
	}

	public SpatialRepository<CadastralParcel> getCadastralParcels() {
		return cadastralParcels;
	}

	public void setCadastralParcels(SpatialRepository<CadastralParcel> cadastralParcels) {
		this.cadastralParcels = cadastralParcels;
	}

	public SpatialRepository<SubParcel> getSubParcels() {
		return subParcels;
	}

	public void setSubParcels(SpatialRepository<SubParcel> subParcels) {
		this.subParcels = subParcels;
	}

	public SpatialRepository<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(SpatialRepository<Building> buildings) {
		this.buildings = buildings;
	}

	public AbstractRepository<UrbaZone> getUrbaZones() {
		return urbaZones;
	}

	public void setUrbaZones(AbstractRepository<UrbaZone> urbaZones) {
		this.urbaZones = urbaZones;
	}

	public SpatialRepository<Prescription> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(SpatialRepository<Prescription> prescriptions) {
		this.prescriptions = prescriptions;
	}

	public AbstractDTM getTerrain() {
		return terrain;
	}

	public void setTerrain(AbstractDTM terrain) {
		this.terrain = terrain;
	}

	public static Coordinate getDpTranslate() {
		return dpTranslate;
	}

	public static void setDpTranslate(Coordinate dpTranslate) {
		Environnement.dpTranslate = dpTranslate;
	}

	public SpatialRepository<Road> getRoads() {
		return roads;
	}

	public void setRoads(SpatialRepository<Road> roads) {
		this.roads = roads;
	}

	public SpatialRepository<BasicPropertyUnit> getBpU() {
		return bpU;
	}

	public void setBpU(SpatialRepository<BasicPropertyUnit> bpU) {
		this.bpU = bpU;
	}

	public UrbaDocument getUrbaDocument() {
		return urbaDoc;
	}

	public void setUrbaDocument(UrbaDocument plu) {
		this.urbaDoc = plu;
	}
}
