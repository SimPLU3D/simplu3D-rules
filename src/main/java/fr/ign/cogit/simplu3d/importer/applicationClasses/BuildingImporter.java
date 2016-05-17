package fr.ign.cogit.simplu3d.importer.applicationClasses;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.simplu3d.io.load.instruction.LoaderVersion;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;

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

  public static boolean versionning = false;

  /**
   * Codepermettant d'affecter des bâtiments aux bonnes unités foncière (1
   * bâtiment appartient à une seule unité foncière)
   * @param featBati Les bâtiments à prendre en compte en entrée
   * @param collBPU Une collection d'unités foncières
   * @return Liste de bâtiments affectés aux bonnes unités foncières
   */
  public static IFeatureCollection<Building> importBuilding(
      IFeatureCollection<IFeature> featBati,
      IFeatureCollection<BasicPropertyUnit> collBPU) {

    IFeatureCollection<Building> batiments = new FT_FeatureCollection<Building>();

    for (IFeature batiFeat : featBati) {

      // On crée le bâtiment
      Building b = new Building(batiFeat.getGeom());

      // Si il s'agit d'une update, on récupère l'id version
      if (versionning == true) {
        try {
          setIdVersionAndIdBuilding(batiFeat, b);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      batiments.add(b);

    }

    AssignBuildingPartToSubParcel.assign(batiments, collBPU);

    return batiments;

  }

  /**
   * AFfecte l'IDF de la version au bâtiment pour le versionning.
   * 
   * @param batiFeat
   * @param b
   * @throws Exception
   */
  public static void setIdVersionAndIdBuilding(IFeature batiFeat, Building b)
      throws Exception {

    // On récupère l'Id Version dans la table attributaire du shape chargé
    Object attBuild = batiFeat.getAttribute(LoaderVersion.NOM_ATT_VERSION);

    // On set l'Id version s'il n'est pas vide
    if (attBuild != null) {
      String objStr = attBuild.toString();
      int objInt = Integer.parseInt(objStr);
      b.setIdVersion(objInt);
    } else {
      System.out.println("L'ID version est vide");
    }

    // On set ensuite l'IdBuilding en fonction des valeurs trouvées dans la BD
    // et on en profite pour mettre à jour la table Building avec les nouveaux
    // batiments
    LoaderVersion.updateTableBuilding(LoaderVersion.host, LoaderVersion.port,
        LoaderVersion.user, LoaderVersion.pw, LoaderVersion.database, b);

  }

}
