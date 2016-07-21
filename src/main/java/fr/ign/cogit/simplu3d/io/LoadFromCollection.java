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
package fr.ign.cogit.simplu3d.io;

import java.util.Collection;
import java.util.logging.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.semantic.AbstractDTM;
import fr.ign.cogit.simplu3d.analysis.AssignRoadToParcelBoundary;
import fr.ign.cogit.simplu3d.dao.BuildingRepository;
import fr.ign.cogit.simplu3d.dao.PrescriptionRepository;
import fr.ign.cogit.simplu3d.dao.RoadRepository;
import fr.ign.cogit.simplu3d.dao.UrbaZoneRepository;
import fr.ign.cogit.simplu3d.dao.geoxygene.BuildingRepositoryGeoxygene;
import fr.ign.cogit.simplu3d.dao.geoxygene.PrescriptionRepositoryGeoxygene;
import fr.ign.cogit.simplu3d.dao.geoxygene.RoadRepositoryGeoxygene;
import fr.ign.cogit.simplu3d.dao.geoxygene.UrbaZoneRepositoryGeoxygene;
import fr.ign.cogit.simplu3d.generator.BasicPropertyUnitGenerator;
import fr.ign.cogit.simplu3d.generator.SubParcelGenerator;
import fr.ign.cogit.simplu3d.importer.AssignBuildingPartToSubParcel;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.feature.UrbaDocumentReader;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.Prescription;
import fr.ign.cogit.simplu3d.model.Road;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.UrbaDocument;
import fr.ign.cogit.simplu3d.model.UrbaZone;
import fr.ign.cogit.simplu3d.util.AssignZ;

/**
 * 
 * TODO EnvironnementBuilder to ease/clarify option management
 *   setZoneUrbas(..)
 *   set...
 *   setGenerateSubParcel(true)
 *   setTranslateToZero(origin)
 *   //...
 *   build() : Environnement
 *   
 * @author MBrasebin
 *
 */
public class LoadFromCollection {

  public final static boolean SURSAMPLED = true;

  private final static Logger logger = Logger.getLogger(LoadFromCollection.class.getCanonicalName());

  public static Environnement load(IFeature featPLU,
      IFeatureCollection<IFeature> zoneColl,
      IFeatureCollection<IFeature> parcelleColl,
      IFeatureCollection<IFeature> voirieColl,
      IFeatureCollection<IFeature> batiColl,
      IFeatureCollection<IFeature> linearPrescriptions, 
      String ruleFolder,
      AbstractDTM dtm
    ) throws Exception {
	  Environnement env = Environnement.createEnvironnement();

    return LoadFromCollection.load(featPLU, zoneColl, parcelleColl, voirieColl,
        batiColl, linearPrescriptions, ruleFolder, dtm, env);
  }

  public static Environnement load(IFeature featPLU,
      IFeatureCollection<IFeature> zoneColl,
      IFeatureCollection<IFeature> parcelleColl,
      IFeatureCollection<IFeature> voirieColl,
      IFeatureCollection<IFeature> batiColl,
      IFeatureCollection<IFeature> linearPrescriptions, String ruleFolder,
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
      for (IFeature feat : linearPrescriptions) {
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
    	UrbaDocumentReader urbaDocumentAdapter = new UrbaDocumentReader();
		plu = urbaDocumentAdapter.read(featPLU);
    }

    env.setUrbaDocument(plu);

    logger.info("PLU creation");

    // Etape 2 : création des zones et assignation des règles aux zones
    UrbaZoneRepository urbaZoneRepository = new UrbaZoneRepositoryGeoxygene(zoneColl);
    IFeatureCollection<UrbaZone> zones = new FT_FeatureCollection<>();
    zones.addAll(urbaZoneRepository.findAll());

    logger.info("Zones loaded");

    // Etape 3 : assignement des zonages au PLU
    env.setUrbaZones(zones);

    logger.info("Zones assigned");

    // Etape 4 : chargement des parcelles et créations des bordures
    IFeatureCollection<CadastralParcel> parcelles = CadastralParcelLoader
        .assignBordureToParcelleWithOrientation(parcelleColl);

    env.setCadastralParcels(parcelles);

    logger.info("Parcel borders created");

    // Etape 5 : import des sous parcelles
    {
    	IFeatureCollection<SubParcel> sousParcelles = new FT_FeatureCollection<>();
    	SubParcelGenerator subParcelGenerator = new SubParcelGenerator(zones);
    	for (CadastralParcel cadastralParcel : parcelles) {
    		sousParcelles.addAll(subParcelGenerator.createSubParcels(cadastralParcel));
		}
    	env.setSubParcels(sousParcelles);
    }

    logger.info("Sub parcels loaded");

    // Etape 6 : création des unités foncirèes
    BasicPropertyUnitGenerator bpuBuilder = new BasicPropertyUnitGenerator(parcelles);
    IFeatureCollection<BasicPropertyUnit> collBPU = bpuBuilder.createPropertyUnits();
    env.setBpU(collBPU);

    logger.info("Basic property units created");

    // Etape 7 : import des bâtiments
    BuildingRepository buildingRepository = new BuildingRepositoryGeoxygene(batiColl);
    Collection<Building> buildings = buildingRepository.findAll();
    env.getBuildings().addAll(buildings);
    
    logger.info("Buildings imported");

    // Etape 7.1 : assignation des batiments aux BpU
    
    AssignBuildingPartToSubParcel.assign(buildings, collBPU);
    
    
    // Etape 8 : chargement des voiries
    RoadRepository roadRepository = new RoadRepositoryGeoxygene(voirieColl);
    IFeatureCollection<Road> roads = new FT_FeatureCollection<>();
    roads.addAll(roadRepository.findAll());
    env.setRoads(roads);

    logger.info("Roads loaded");

    // Etape 9 : on affecte les liens entres une bordure et ses objets
    // adjacents (bordure sur route => route + relation entre les limites de parcelles)
    AssignRoadToParcelBoundary.process(parcelles, roads);

    logger.info("Links with roads created");

    // Etape 10 : on importe les alignements
    {
        PrescriptionRepository prescriptionRepository = new PrescriptionRepositoryGeoxygene(linearPrescriptions);
        Collection<Prescription> prescriptions = prescriptionRepository.findAll();
        env.getPrescriptions().addAll(prescriptions);
    }

    logger.info("Alignment loaded");

    // Etape 11 : on affecte des z à tout ce bon monde // - parcelles,
    // sous-parcelles route sans z, zonage, les bordures etc...
    env.setTerrain(dtm);
    try {
      AssignZ.toParcelle(env.getCadastralParcels(), dtm, SURSAMPLED);
      AssignZ.toSousParcelle(env.getSubParcels(), dtm, SURSAMPLED);
      AssignZ.toVoirie(env.getRoads(), dtm, SURSAMPLED);
      AssignZ.toPrescriptions(env.getPrescriptions(), dtm, SURSAMPLED);
      AssignZ.toZone(env.getUrbaZones(), dtm, false);
    } catch (Exception e) {
      e.printStackTrace();
    }

    logger.info("3D created");

    return env;
  }

}
