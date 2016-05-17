package fr.ign.cogit.simplu3d.io.load.application;

import java.util.logging.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.semantic.AbstractDTM;
import fr.ign.cogit.simplu3d.importer.applicationClasses.AlignementImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.AssignLinkToBordure;
import fr.ign.cogit.simplu3d.importer.applicationClasses.BasicPropertyUnitImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.BuildingImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.importer.applicationClasses.PLUImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.RoadImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.SubParcelImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.ZonesImporter;
import fr.ign.cogit.simplu3d.model.application.Alignement;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaDocument;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;
import fr.ign.cogit.simplu3d.util.AssignZ;

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
public class LoadFromCollection {

  public final static boolean SURSAMPLED = true;

  private final static Logger logger = Logger
      .getLogger(LoadFromCollection.class.getCanonicalName());

  public static Environnement load(IFeature featPLU,
      IFeatureCollection<IFeature> zoneColl,
      IFeatureCollection<IFeature> parcelleColl,
      IFeatureCollection<IFeature> voirieColl,
      IFeatureCollection<IFeature> batiColl,
      IFeatureCollection<IFeature> prescriptions, String ruleFolder,
      AbstractDTM dtm) throws Exception {
    Environnement env = Environnement.getInstance();

    return LoadFromCollection.load(featPLU, zoneColl, parcelleColl, voirieColl,
        batiColl, prescriptions, ruleFolder, dtm, env);
  }

  public static Environnement load(IFeature featPLU,
      IFeatureCollection<IFeature> zoneColl,
      IFeatureCollection<IFeature> parcelleColl,
      IFeatureCollection<IFeature> voirieColl,
      IFeatureCollection<IFeature> batiColl,
      IFeatureCollection<IFeature> prescriptions, String ruleFolder,
      AbstractDTM dtm, Environnement env) throws Exception {

    // Etape 0 : doit on translater tous les objets ?

    if (Environnement.TRANSLATE_TO_ZERO) {
      Environnement.dpTranslate = zoneColl.envelope().center();
      for (IFeature feat : zoneColl) {
        feat.setGeom(feat.getGeom().translate(
            -Environnement.dpTranslate.getX(),
            -Environnement.dpTranslate.getY(), 0));
      }
      for (IFeature feat : parcelleColl) {
        feat.setGeom(feat.getGeom().translate(
            -Environnement.dpTranslate.getX(),
            -Environnement.dpTranslate.getY(), 0));
      }
      for (IFeature feat : voirieColl) {
        feat.setGeom(feat.getGeom().translate(
            -Environnement.dpTranslate.getX(),
            -Environnement.dpTranslate.getY(), 0));
      }
      for (IFeature feat : batiColl) {
        feat.setGeom(feat.getGeom().translate(
            -Environnement.dpTranslate.getX(),
            -Environnement.dpTranslate.getY(), 0));
      }
      for (IFeature feat : prescriptions) {
        feat.setGeom(feat.getGeom().translate(
            -Environnement.dpTranslate.getX(),
            -Environnement.dpTranslate.getY(), 0));
      }
    }

    // Etape 1 : création de l'objet PLU
    UrbaDocument plu;
    if (featPLU == null) {
      plu = new UrbaDocument();
    } else {
      plu = PLUImporter.loadPLU(featPLU);
    }

    env.setPlu(plu);

    logger.info("PLU creation");

    // Etape 2 : création des zones et assignation des règles aux zones
    IFeatureCollection<UrbaZone> zones = ZonesImporter.importUrbaZone(zoneColl);

    logger.info("Zones loaded");

    // Etape 3 : assignement des zonages au PLU
    plu.getlUrbaZone().addAll(zones);
    env.setUrbaZones(zones);

    logger.info("Zones assigned");

    // Etape 4 : chargement des parcelles et créations des bordures
    IFeatureCollection<CadastralParcel> parcelles = CadastralParcelLoader
        .assignBordureToParcelleWithOrientation(parcelleColl);

    if (parcelles == null) {
      throw new Exception("Bad topology in Parcel List");
    }

    env.setCadastralParcels(parcelles);

    logger.info("Parcel borders created");

    // Etape 5 : import des sous parcelles
    IFeatureCollection<SubParcel> sousParcelles = SubParcelImporter.create(
        parcelles, zones);
    env.setSubParcels(sousParcelles);

    logger.info("Sub parcels loaded");

    // Etape 6 : création des unités foncirèes
    IFeatureCollection<BasicPropertyUnit> collBPU = BasicPropertyUnitImporter
        .importBPU(parcelles);
    env.setBpU(collBPU);

    logger.info("Basic property units created");

    // Etape 7 : import des bâtiments
    IFeatureCollection<Building> buildings = BuildingImporter.importBuilding(
        batiColl, collBPU);
    env.getBuildings().addAll(buildings);

    logger.info("Buildings imported");

    // Etape 8 : chargement des voiries

    IFeatureCollection<Road> roads = RoadImporter.importRoad(voirieColl);
    env.setRoads(roads);

    logger.info("Roads loaded");

    // Etape 9 : on affecte les liens entres une bordure et ses objets
    // adjacents
    AssignLinkToBordure.process(parcelles, roads);

    logger.info("Links with roads created");

    // Etape 10 : on importe les alignements
    IFeatureCollection<Alignement> alignementColl = AlignementImporter
        .importRecul(prescriptions, parcelles);
    env.setAlignements(alignementColl);

    logger.info("Alignment loaded");

    // Etape 11 : on affecte des z à tout ce bon monde // - parcelles,
    // sous-parcelles route sans z, zonage, les bordures etc...
    env.setTerrain(dtm);
    try {
      AssignZ.toParcelle(env.getParcelles(), dtm, SURSAMPLED);
      AssignZ.toSousParcelle(env.getSubParcels(), dtm, SURSAMPLED);
      AssignZ.toVoirie(env.getRoads(), dtm, SURSAMPLED);
      AssignZ.toAlignement(alignementColl, dtm, SURSAMPLED);
      AssignZ.toZone(env.getUrbaZones(), dtm, false);
    } catch (Exception e) {
      e.printStackTrace();
    }

    logger.info("3D created");

    return env;
  }

}
