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

/*
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.semantic.AbstractDTM;
*/

import fr.ign.common.dtm.AbstractDTM;
import org.locationtech.jts.geom.Coordinate;

import java.util.Collection;

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

	private static Environnement env = null;

	private UrbaDocument urbaDoc;

	private Collection<BasicPropertyUnit> bpU = null;//TODO
	private Collection<CadastralParcel> cadastralParcels = null;//TODO
	private Collection<SubParcel> subParcels = null;//TODO

	private Collection<Building> buildings = null;//TODO

	private Collection<UrbaZone> urbaZones = null;//TODO
	private Collection<Prescription> prescriptions = null;//TODO

	private AbstractDTM terrain;
	private Collection<Road> roads = null;//TODO

	protected Environnement() {

	}

	/**
	 * Create a new environment instance
	 * 
	 * @return a new environnement and erase the existing one if necesesary
	 */
	public static Environnement createEnvironnement() {
		env = new Environnement();
		return env;
	}

	/**
	 * TODO remove and replace usage by demo.DefaultEnvironnement.getInstance()
	 * 
	 * @return the current environnement a new one is created if there is no
	 *         existing
	 */
	public static Environnement getInstance() {
		if (env == null) {
			env = new Environnement();
		}
		return env;
	}

	public Collection<CadastralParcel> getCadastralParcels() {
		return cadastralParcels;
	}

	public void setCadastralParcels(Collection<CadastralParcel> cadastralParcels) {
		this.cadastralParcels = cadastralParcels;
	}

	public Collection<SubParcel> getSubParcels() {
		return subParcels;
	}

	public void setSubParcels(Collection<SubParcel> subParcels) {
		this.subParcels = subParcels;
	}

	public Collection<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(Collection<Building> buildings) {
		this.buildings = buildings;
	}

	public Collection<UrbaZone> getUrbaZones() {
		return urbaZones;
	}

	public void setUrbaZones(Collection<UrbaZone> urbaZones) {
		this.urbaZones = urbaZones;
	}

	public Collection<Prescription> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(Collection<Prescription> prescriptions) {
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

	public Collection<Road> getRoads() {
		return roads;
	}

	public void setRoads(Collection<Road> roads) {
		this.roads = roads;
	}

	public Collection<BasicPropertyUnit> getBpU() {
		return bpU;
	}

	public void setBpU(Collection<BasicPropertyUnit> bpU) {
		this.bpU = bpU;
	}

	public UrbaDocument getUrbaDocument() {
		return urbaDoc;
	}

	public void setUrbaDocument(UrbaDocument plu) {
		this.urbaDoc = plu;
	}

}
