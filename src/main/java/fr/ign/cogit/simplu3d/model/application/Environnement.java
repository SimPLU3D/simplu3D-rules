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

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.model.citygml.core.CG_CityModel;
import fr.ign.cogit.geoxygene.sig3d.semantic.AbstractDTM;
import fr.ign.parameters.ParameterComponent;
import fr.ign.parameters.Parameters;

/**
 * 
 * Regroupe les différents éléments nécessaire pour la vérification et la simulation des règles
 * d'urbanisme.
 * 
 * @author Brasebin Mickaël
 *
 */
public class Environnement extends CG_CityModel {
	public String folder;
	public UrbaDocument urbaDoc;

	private static Environnement env = null;

	public IFeatureCollection<CadastralParcel> cadastralParcels = new FT_FeatureCollection<>();
	public IFeatureCollection<SubParcel> subParcels = new FT_FeatureCollection<>();
	public IFeatureCollection<AbstractBuilding> buildings = new FT_FeatureCollection<>();
	public IFeatureCollection<UrbaZone> urbaZones = new FT_FeatureCollection<>();
	public IFeatureCollection<Alignement> alignements = new FT_FeatureCollection<>();
	public IFeatureCollection<BasicPropertyUnit> bpU = new FT_FeatureCollection<>();

	public AbstractDTM terrain;
	public IFeatureCollection<Road> roads = new FT_FeatureCollection<Road>();

	public static IDirectPosition dpTranslate = null;

	public static boolean VERBOSE = false;
	public static boolean TRANSLATE_TO_ZERO = false;

	public IFeatureCollection<CadastralParcel> getParcelles() {
		return cadastralParcels;
	}

	public void setParcelles(IFeatureCollection<CadastralParcel> parcelles) {
		this.cadastralParcels = parcelles;
	}

	protected Environnement() {

	}

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

	public IFeatureCollection<Alignement> getAlignements() {
		return alignements;
	}

	public void setAlignements(IFeatureCollection<Alignement> alignements) {
		this.alignements = alignements;
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

	public static boolean isVERBOSE() {
		return VERBOSE;
	}

	public static void setVERBOSE(boolean vERBOSE) {
		VERBOSE = vERBOSE;
	}

	public static boolean isTRANSLATE_TO_ZERO() {
		return TRANSLATE_TO_ZERO;
	}

	public static void setTRANSLATE_TO_ZERO(boolean tRANSLATE_TO_ZERO) {
		TRANSLATE_TO_ZERO = tRANSLATE_TO_ZERO;
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

	public UrbaDocument getPlu() {
		return urbaDoc;
	}

	public void setPlu(UrbaDocument plu) {
		this.urbaDoc = plu;
	}

	public Parameters loadParameters(String path) {
		try {
			return ParameterComponent.unmarshall(new File(path));
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

}
