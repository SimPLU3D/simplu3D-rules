package fr.ign.cogit.simplu3d.importer.applicationClasses;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.BuildingPart;

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
public class BuildingImporter {

    /**
     * @ TODO : buildingparts non gérés
     * 
     * @param featBati
     * @param collBPU
     * @return
     */
    public static IFeatureCollection<Building> importBuilding(
            IFeatureCollection<IFeature> featBati,
            IFeatureCollection<BasicPropertyUnit> collBPU) {

        IFeatureCollection<Building> batiments = new FT_FeatureCollection<Building>();

        for (IFeature batiFeat : featBati) {
            
            // On crée le bâtiment
            Building b = new Building(batiFeat.getGeom());
            batiments.add(b);

        }

        AssignBuildingPartToSubParcel.assign(batiments, collBPU);

        return batiments;

    }

}
