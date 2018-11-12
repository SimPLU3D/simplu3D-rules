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
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.semantic.AbstractDTM;

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
	public static IDirectPosition dpTranslate = null;

	private static Environnement env = null;

	private UrbaDocument urbaDoc;

	private IFeatureCollection<BasicPropertyUnit> bpU = new FT_FeatureCollection<>();
	private IFeatureCollection<CadastralParcel> cadastralParcels = new FT_FeatureCollection<>();
	private IFeatureCollection<SubParcel> subParcels = new FT_FeatureCollection<>();

	private IFeatureCollection<AbstractBuilding> buildings = new FT_FeatureCollection<>();

	private IFeatureCollection<UrbaZone> urbaZones = new FT_FeatureCollection<>();
	private IFeatureCollection<Prescription> prescriptions = new FT_FeatureCollection<>();

	private AbstractDTM terrain;
	private IFeatureCollection<Road> roads = new FT_FeatureCollection<Road>();

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

	public IFeatureCollection<CadastralParcel> getCadastralParcels() {
		return cadastralParcels;
	}

	public void setCadastralParcels(IFeatureCollection<CadastralParcel> cadastralParcels) {
		this.cadastralParcels = cadastralParcels;
	}

	public IFeatureCollection<SubParcel> getSubParcels() {
		return subParcels;
	}

	public void setSubParcels(IFeatureCollection<SubParcel> subParcels) {
		this.subParcels = subParcels;
	}

	public IFeatureCollection<AbstractBuilding> getBuildings() {
		return buildings;
	}

	public void setBuildings(IFeatureCollection<AbstractBuilding> buildings) {
		this.buildings = buildings;
	}

	public IFeatureCollection<UrbaZone> getUrbaZones() {
		return urbaZones;
	}

	public void setUrbaZones(IFeatureCollection<UrbaZone> urbaZones) {
		this.urbaZones = urbaZones;
	}

	public IFeatureCollection<Prescription> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(IFeatureCollection<Prescription> prescriptions) {
		this.prescriptions = prescriptions;
	}

	public AbstractDTM getTerrain() {
		return terrain;
	}

	public void setTerrain(AbstractDTM terrain) {
		this.terrain = terrain;
	}

	public static IDirectPosition getDpTranslate() {
		return dpTranslate;
	}

	public static void setDpTranslate(IDirectPosition dpTranslate) {
		Environnement.dpTranslate = dpTranslate;
	}

	public IFeatureCollection<Road> getRoads() {
		return roads;
	}

	public void setRoads(IFeatureCollection<Road> roads) {
		this.roads = roads;
	}

	public IFeatureCollection<BasicPropertyUnit> getBpU() {
		return bpU;
	}

	public void setBpU(IFeatureCollection<BasicPropertyUnit> bpU) {
		this.bpU = bpU;
	}

	public UrbaDocument getUrbaDocument() {
		return urbaDoc;
	}

	public void setUrbaDocument(UrbaDocument plu) {
		this.urbaDoc = plu;
	}

}
