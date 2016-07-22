package fr.ign.cogit.simplu3d.importer;

import java.util.Collection;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.simplu3d.analysis.BasicPropertyUnitFinder;
import fr.ign.cogit.simplu3d.generator.BuildingPartToSubParcelAssigner;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;

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
public class AssignBuildingPartToSubParcel {

	/**
	 * Aire minimale pour considérée un polygone comme attaché à une parcelle
	 */
	public static double RATIO_MIN = 0.8;

	public static int ASSIGN_METHOD = 0;

	public static void assign(
		Collection<Building> buildings,
		IFeatureCollection<BasicPropertyUnit> basicPropertyUnits
	) {
		/*
		 * assignation des buildings aux BpU
		 */
		{
			BasicPropertyUnitFinder finder = new BasicPropertyUnitFinder(basicPropertyUnits);
			for (Building building : buildings) {
				BasicPropertyUnit bPU = finder.findBestIntersections(building.getFootprint());
				if (bPU != null) {
					bPU.getBuildings().add(building);
					building.setbPU(bPU);
				}
			}
		}

		/*
		 * Si METHODE 2, on découpe les bâtiments, sinon, on assigne à la Sub
		 */
		BuildingPartToSubParcelAssigner assigner = new BuildingPartToSubParcelAssigner();
		assigner.setCutBuildingOnSubParcels(ASSIGN_METHOD == 2);
		for (Building building : buildings) {
			assigner.assignBuildingToSubParcels(building);
		}
	}

}
