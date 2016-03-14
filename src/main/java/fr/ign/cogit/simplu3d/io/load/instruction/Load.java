package fr.ign.cogit.simplu3d.io.load.instruction;

import java.io.File;
import java.text.SimpleDateFormat;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.io.load.application.ParemetersApplication;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.PLU;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;
import fr.ign.cogit.simplu3d.util.ExtractIdMaxPG;

public class Load {

  public static String host = "localhost";
  public static String user = "postgres";
  public static String pw = "postgres";
  // public static String database = "test_simplu3d";
  // public static String database = "strasbourg_simplu";
  public static String database = "test";
  public static String folder = "D:/0_Masson/1_CDD_SIMPLU/2_Travail/0_Workspace/simplu3d/simplu3D-rules/src/main/resources/fr/ign/cogit/simplu3d/data/";
  public static String port = "5432";

  public static void main(String[] args) throws Exception {

    PostgisManager.SRID = "2154";
    int searchIdBPU = 8;
    int searchIdZU = 1;

    /*
     * At present, the program is parameterized to work with a named database
     * "test_simplu3d". Furthermore, the SQl script to build tables in pgAdmin
     * (Creation_BDD.sql) is available at the address :
     * "/simplu3d-rules/src/main/resources/fr/ign/cogit/simplu3d/sql"
     */

    // Environnement env = LoaderSHP.load(new File(folder));
    // Environnement env = LoaderPostGISTest.load(folder);
    // Environnement env = LoaderBPU.load(folder, searchIdBPU);
    Environnement env = LoaderUrbaZone.load(folder, searchIdZU);

    // PLU featCPlu = loadPlu(host, port, user, pw, database, env);

    // loadBasicPropertyUnit(host, port, user, pw, database, env);
    // loadZoneUrba(host, port, user, pw, database, env, featCPlu);
    // loadParcel(host, port, user, pw, database, env);
    // loadSubParcel(host, port, user, pw, database, env);
    // loadRoad(host, port, user, pw, database, env);
    // loadAxis(host, port, user, pw, database, env);
    // loadBuilding(host, port, user, pw, database, env);
    // loadBuildingsParts(host, port, user, pw, database, env);
    // loadSpecificCBoundary(host, port, user, pw, database, env);
    // loadRoof(host, port, user, pw, database, env);
    // loadWall(host, port, user, pw, database, env);
    // loadRoofing(host, port, user, pw, database, env);
    // loadGutter(host, port, user, pw, database, env);
    // loadGable(host, port, user, pw, database, env);

    System.out.println("\n----- Loading completed (hopefully) -----");

  }

  public static boolean loadAll(String host, String port, String user,
      String pw, String database, String folder) throws Exception {

    Environnement env = LoaderSHP.load(new File(folder));

    PLU featCPlu = loadPlu(host, port, user, pw, database, env);

    loadBasicPropertyUnit(host, port, user, pw, database, env);
    loadZoneUrba(host, port, user, pw, database, env, featCPlu);
    loadParcel(host, port, user, pw, database, env);
    loadSubParcel(host, port, user, pw, database, env);
    loadRoad(host, port, user, pw, database, env);
    loadAxis(host, port, user, pw, database, env);
    loadBuilding(host, port, user, pw, database, env);
    loadBuildingsParts(host, port, user, pw, database, env);
    loadRoof(host, port, user, pw, database, env);
    loadWall(host, port, user, pw, database, env);
    loadRoofing(host, port, user, pw, database, env);
    loadGutter(host, port, user, pw, database, env);
    loadGable(host, port, user, pw, database, env);
    loadSpecificCBoundary(host, port, user, pw, database, env);

    System.out.println("\n----- Loading completed (hopefully) -----");

    return true;

  }

  // Chargement des PLU
  public static PLU loadPlu(String host, String port, String user, String pw,
      String database, Environnement env) throws Exception {

    // IFeature featCDoc = env.getPlu();

    PLU featCPlu = env.getPlu();

    IFeature featTemp = new DefaultFeature();
    IFeatureCollection<IFeature> featCDocUrba = new FT_FeatureCollection<IFeature>();

    int idPLUIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_DOC_URBA,
        ParametersInstructionPG.ATT_DOC_URBA_ID);

    SimpleDateFormat sdfdu1 = new SimpleDateFormat(
        ParemetersApplication.DATE_FORMAT_DU1);
    SimpleDateFormat sdfdu2 = new SimpleDateFormat(
        ParemetersApplication.DATE_FORMAT_DU2);

    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_ID, (++idPLUIni), "Integer");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_ID_URBA, featCPlu.getIdUrba(),
        "String");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_TYPE_DOC, featCPlu.getTypeDoc(),
        "String");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_DATE_APPRO,
        sdfdu1.format(featCPlu.getDateAppro()), "String");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_DATE_FIN,
        sdfdu1.format(featCPlu.getDateFin()), "String");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_INTERCO, featCPlu.getInterCo(),
        "String");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_SIREN, featCPlu.getSiren(),
        "String");
    AttributeManager
        .addAttribute(featTemp, ParametersInstructionPG.ATT_DOC_URBA_ETAT,
            featCPlu.getEtat(), "String");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_NOM_REG, featCPlu.getNomReg(),
        "String");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_URL_REG, featCPlu.getUrlReg(),
        "String");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_NOM_PLAN, featCPlu.getNomPlan(),
        "String");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_URL_PLAN, featCPlu.getUrlPlan(),
        "String");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_SITE, featCPlu.getSiteWeb(),
        "String");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_TYPE_REF, featCPlu.getTypeRef(),
        "String");
    AttributeManager.addAttribute(featTemp,
        ParametersInstructionPG.ATT_DOC_URBA_DATE_REF,
        sdfdu2.format(featCPlu.getDateRef()), "String");

    featCDocUrba.add(featTemp);

    PostgisManager.insertInNonGeometricTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_DOC_URBA, featCDocUrba);

    return featCPlu;
  }

  // Chargement des Basic Property Unit
  public static boolean loadBasicPropertyUnit(String host, String port,
      String user, String pw, String database, Environnement env)
      throws Exception {

    IFeatureCollection<BasicPropertyUnit> featCBPU = env.getBpU();

    int idBPUIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_BASIC_PROPERTY_UNIT,
        ParametersInstructionPG.ATT_BPU_ID);

    for (BasicPropertyUnit bpu : featCBPU) {

      AttributeManager.addAttribute(bpu, ParametersInstructionPG.ATT_BPU_ID,
          (++idBPUIni), "Integer");

    }

    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_BPU_GEOM;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_BASIC_PROPERTY_UNIT, env.getBpU());

    return true;
  }

  // Chargement des informations sur les zones urba dans PostGis
  public static boolean loadZoneUrba(String host, String port, String user,
      String pw, String database, Environnement env, PLU featCPlu)
      throws Exception {

    IFeatureCollection<UrbaZone> featCUrbzone = env.getUrbaZones();

    int idZUIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_ZONE_URBA,
        ParametersInstructionPG.ATT_ZONE_URBA_ID);

    for (UrbaZone u : featCUrbzone) {

      // SimpleDateFormat sdf = new SimpleDateFormat(
      // ParemetersApplication.DATE_FORMAT_ZU);

      // AttributeManager.addAttribute(u,
      // ParametersInstructionPG.ATT_ZONE_URBA_ID_PLU, featCPlu.getIdUrba(),
      // "String");
      AttributeManager.addAttribute(u,
          ParametersInstructionPG.ATT_ZONE_URBA_ID, (++idZUIni), "Integer");
      // AttributeManager.addAttribute(u,
      // ParametersInstructionPG.ATT_ZONE_URBA_LIBELLE, u.getLibelle(),
      // "String");
      // AttributeManager.addAttribute(u,
      // ParametersInstructionPG.ATT_ZONE_URBA_LIBELONG, u.getLibelong(),
      // "String");
      // AttributeManager.addAttribute(u,
      // ParametersInstructionPG.ATT_ZONE_URBA_TYPEZONE, u.getTypeZone(),
      // "String");
      // AttributeManager.addAttribute(u,
      // ParametersInstructionPG.ATT_ZONE_URBA_DESTDOMI, u.getDestdomi(),
      // "String");
      // AttributeManager
      // .addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_NOMFIC,
      // u.getNomFic(), "String");
      // AttributeManager
      // .addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_URLFIC,
      // u.getUrlFic(), "String");
      // AttributeManager.addAttribute(u,
      // ParametersInstructionPG.ATT_ZONE_URBA_INSEE, u.getInsee(), "String");
      // AttributeManager.addAttribute(u,
      // ParametersInstructionPG.ATT_ZONE_URBA_DATE_APPRO,
      // sdf.format(u.getDateDeb()), "String");
      // AttributeManager.addAttribute(u,
      // ParametersInstructionPG.ATT_ZONE_URBA_DATE_VALID,
      // sdf.format(u.getDateFin()), "String");
      // AttributeManager.addAttribute(u,
      // ParametersInstructionPG.ATT_ZONE_URBA_COMMENTAIRE, u.getText(),
      // "String");

    }

    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_ZONE_URBA_GEOM;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_ZONE_URBA, env.getUrbaZones());

    return true;
  }

  // Chargement des parcelles cadastrales
  public static boolean loadParcel(String host, String port, String user,
      String pw, String database, Environnement env) throws Exception {

    IFeatureCollection<CadastralParcel> featCCadPar = env.getCadastralParcels();

    int idPCIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_CADASTRAL_PARCEL,
        ParametersInstructionPG.ATT_CAD_PARCEL_ID);

    for (CadastralParcel cp : featCCadPar) {

      AttributeManager.addAttribute(cp,
          ParametersInstructionPG.ATT_CAD_PARCEL_ID, (++idPCIni), "Integer");

      for (BasicPropertyUnit bpu : env.getBpU()) {

        if (bpu.getCadastralParcel().contains(cp)) {

          AttributeManager.addAttribute(cp,
              ParametersInstructionPG.ATT_CAD_PARCEL_ID_BPU,
              bpu.getAttribute(ParametersInstructionPG.ATT_BPU_ID), "Integer");
          break;
        }

      }

      AttributeManager.addAttribute(cp,
          ParametersInstructionPG.ATT_CAD_PARCEL_NUM, cp.getId(), "Integer");
      AttributeManager.addAttribute(cp,
          ParametersInstructionPG.ATT_CAD_PARCEL_SURF, cp.getArea(), "Double");

    }

    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_CAD_PARCEL_GEOM;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_CADASTRAL_PARCEL,
        env.getCadastralParcels());

    return true;
  }

  // Chargement des sous-parcelles cadastrales
  public static boolean loadSubParcel(String host, String port, String user,
      String pw, String database, Environnement env) throws Exception {

    IFeatureCollection<SubParcel> featCSubParcel = env.getSubParcels();

    int idSPCIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_SUB_PARCEL,
        ParametersInstructionPG.ATT_SUB_PARCEL_ID);

    for (SubParcel sp : featCSubParcel) {

      AttributeManager.addAttribute(sp,
          ParametersInstructionPG.ATT_SUB_PARCEL_ID, (++idSPCIni), "Integer");

      for (UrbaZone ub : env.getUrbaZones()) {

        if (ub.getSubParcels().contains(sp)) {

          AttributeManager.addAttribute(sp,
              ParametersInstructionPG.ATT_SUB_PARCEL_ID_ZU,
              ub.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_ID),
              "Integer");

        }

      }

      for (CadastralParcel cp : env.getCadastralParcels()) {

        if (cp.getSubParcel().contains(sp)) {

          AttributeManager.addAttribute(sp,
              ParametersInstructionPG.ATT_SUB_PARCEL_ID_CADPAR,
              cp.getAttribute(ParametersInstructionPG.ATT_CAD_PARCEL_ID),
              "Integer");

        }

      }

      AttributeManager.addAttribute(sp,
          ParametersInstructionPG.ATT_SUB_PARCEL_AVG_SLOPE, sp.getAvgSlope(),
          "Double");
      AttributeManager.addAttribute(sp,
          ParametersInstructionPG.ATT_SUB_PARCEL_SURF, sp.getArea(), "Double");

    }

    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_SUB_PARCEL_GEOM;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_SUB_PARCEL, env.getSubParcels());

    return true;
  }

  // Chargement des informations sur les routes dans PostGis
  public static boolean loadRoad(String host, String port, String user,
      String pw, String database, Environnement env) throws Exception {

    IFeatureCollection<Road> featCRoad = env.getRoads();

    int idRoIni = ExtractIdMaxPG
        .idMaxTable(host, port, database, user, pw,
            ParametersInstructionPG.TABLE_ROAD,
            ParametersInstructionPG.ATT_ROAD_ID);

    for (Road r : featCRoad) {

      AttributeManager.addAttribute(r, ParametersInstructionPG.ATT_ROAD_ID,
          (++idRoIni), "Integer");
      AttributeManager.addAttribute(r, ParametersInstructionPG.ATT_ROAD_NOM,
          r.getName(), "String");
      AttributeManager.addAttribute(r, ParametersInstructionPG.ATT_ROAD_TYPE,
          r.getUsage(), "String");
      AttributeManager.addAttribute(r,
          ParametersInstructionPG.ATT_ROAD_LARGEUR, r.getWidth(), "Double");

    }

    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_ROAD_GEOM_SURF;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_ROAD, env.getRoads());

    return true;
  }

  // Chargement de l'axe des routes
  public static boolean loadAxis(String host, String port, String user,
      String pw, String database, Environnement env) throws Exception {

    IFeatureCollection<Road> featCRoad = env.getRoads();
    IFeatureCollection<IFeature> featCRoadAxis = new FT_FeatureCollection<IFeature>();

    int idRoAIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_AXE, ParametersInstructionPG.ATT_AXE_ID);

    for (Road ra : featCRoad) {

      IGeometry geomAxe = ((IGeometry) ra.getAxis().clone());
      IFeature featAxis = new DefaultFeature(geomAxe);

      AttributeManager.addAttribute(featAxis,
          ParametersInstructionPG.ATT_AXE_ID, (++idRoAIni), "Integer");

      AttributeManager.addAttribute(featAxis,
          ParametersInstructionPG.ATT_AXE_ID_ROAD,
          ra.getAttribute(ParametersInstructionPG.ATT_ROAD_ID), "Integer");

      featCRoadAxis.add(featAxis.cloneGeom());

    }

    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_AXE_GEOM;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_AXE, featCRoadAxis);

    return true;
  }

  // Chargement des Specific Cadastral Boundary
  public static boolean loadSpecificCBoundary(String host, String port,
      String user, String pw, String database, Environnement env)
      throws Exception {

    IFeatureCollection<SubParcel> featCSubParcel = env.getSubParcels();
    IFeatureCollection<IFeature> featCSpecificCBoundary = new FT_FeatureCollection<>();

    int idSCBIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_SPECIFIC_CBOUNDARY,
        ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID);

    for (SubParcel sb : featCSubParcel) {

      for (SpecificCadastralBoundary spc : sb.getParcel()
          .getSpecificCadastralBoundary()) {

        featCSpecificCBoundary.add(spc);

        IFeature featAdj = spc.getFeatAdj();

        if (featAdj instanceof Road) {

          Road roadAdj = (Road) featAdj;

          AttributeManager.addAttribute(spc,
              ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID_ADJ,
              roadAdj.getAttribute(ParametersInstructionPG.ATT_ROAD_ID),
              "Integer");
          AttributeManager.addAttribute(spc,
              ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_TABLE_REF,
              ParametersInstructionPG.TABLE_ROAD, "String");

        } else if (featAdj instanceof CadastralParcel) {

          CadastralParcel parceC = (CadastralParcel) featAdj;

          AttributeManager.addAttribute(
              spc,
              ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID_ADJ,
              parceC.getSubParcel().get(0)
                  .getAttribute(ParametersInstructionPG.ATT_SUB_PARCEL_ID),
              "Integer");
          AttributeManager.addAttribute(spc,
              ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_TABLE_REF,
              ParametersInstructionPG.TABLE_SUB_PARCEL, "String");

        } else {

          if (featAdj != null) {

            System.out.println("Not managed class : " + featAdj.getClass());

          }

        }

        AttributeManager.addAttribute(spc,
            ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID, (++idSCBIni),
            "Integer");
        AttributeManager.addAttribute(spc,
            ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_TYPE, spc.getType(),
            "String");
        AttributeManager.addAttribute(spc,
            ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_SIDE, spc.getSide(),
            "String");
        AttributeManager.addAttribute(spc,
            ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID_SUB_PAR,
            sb.getAttribute(ParametersInstructionPG.ATT_SUB_PARCEL_ID),
            "Integer");

      }

    }

    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_GEOM;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_SPECIFIC_CBOUNDARY,
        featCSpecificCBoundary, true);

    return true;
  }

  // Chargement des batiments
  public static boolean loadBuilding(String host, String port, String user,
      String pw, String database, Environnement env) throws Exception {

    IFeatureCollection<AbstractBuilding> featCAbstractBuilding = env
        .getBuildings();

    int idBIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_BUILDING,
        ParametersInstructionPG.ATT_BUILDING_ID);

    for (AbstractBuilding ab : featCAbstractBuilding) {

      AttributeManager.addAttribute(ab,
          ParametersInstructionPG.ATT_BUILDING_ID, (++idBIni), "Integer");

    }

    PostgisManager.insertInNonGeometricTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_BUILDING, env.getBuildings());

    return true;
  }

  public static boolean loadBuildingsParts(String host, String port,
      String user, String pw, String database, Environnement env)
      throws Exception {

    IFeatureCollection<AbstractBuilding> featCAbstractBuilding = env
        .getBuildings();
    IFeatureCollection<SubParcel> featCSubParcel = env.getSubParcels();
    IFeatureCollection<IFeature> featCBuildingPart = new FT_FeatureCollection<>();

    int idBPIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_BUILDING_PART,
        ParametersInstructionPG.ATT_BUILDING_PART_ID);

    for (AbstractBuilding abp : featCAbstractBuilding) {

      // TODO : boucle sur BP pour affecter geom BP
      IFeature featTemp = new DefaultFeature(abp.getFootprint());

      AttributeManager.addAttribute(featTemp,
          ParametersInstructionPG.ATT_BUILDING_PART_ID, (++idBPIni), "Integer");
      AttributeManager.addAttribute(featTemp,
          ParametersInstructionPG.ATT_BUILDING_PART_ID_BUILD,
          abp.getAttribute(ParametersInstructionPG.ATT_BUILDING_ID), "Integer");

      AttributeManager.addAttribute(abp,
          ParametersInstructionPG.ATT_BUILDING_PART_ID,
          featTemp.getAttribute(ParametersInstructionPG.ATT_BUILDING_PART_ID),
          "Integer");

      for (SubParcel sp : featCSubParcel) {

        if (sp.getBuildingsParts().contains(abp)) {

          AttributeManager.addAttribute(featTemp,
              ParametersInstructionPG.ATT_BUILDING_PART_ID_SUBPAR,
              sp.getAttribute(ParametersInstructionPG.ATT_SUB_PARCEL_ID),
              "Integer");

        }

      }

      featCBuildingPart.add(featTemp);

    }

    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_BUILDING_PART_GEOM;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_BUILDING_PART, featCBuildingPart);

    return true;
  }

  // Chargement des toits
  public static boolean loadRoof(String host, String port, String user,
      String pw, String database, Environnement env) throws Exception {

    IFeatureCollection<AbstractBuilding> featCAbstractBuilding = env
        .getBuildings();
    IFeatureCollection<IFeature> featCRoof = new FT_FeatureCollection<>();

    int idRoIni = ExtractIdMaxPG
        .idMaxTable(host, port, database, user, pw,
            ParametersInstructionPG.TABLE_ROOF,
            ParametersInstructionPG.ATT_ROOF_ID);

    for (AbstractBuilding abr : featCAbstractBuilding) {

      AttributeManager.addAttribute(abr.getRoof(),
          ParametersInstructionPG.ATT_ROOF_ID, (++idRoIni), "Integer");
      AttributeManager.addAttribute(abr.getRoof(),
          ParametersInstructionPG.ATT_ROOF_ANGLE_MAX, abr.getRoof()
              .getAngleMax(), "Double");
      AttributeManager.addAttribute(abr.getRoof(),
          ParametersInstructionPG.ATT_ROOF_ANGLE_MIN, abr.getRoof()
              .getAngleMin(), "Double");

      if (abr.getRoof() instanceof RoofSurface) {

        AttributeManager.addAttribute(abr.getRoof(),
            ParametersInstructionPG.ATT_ROOF_ID_BUILDPART,
            abr.getAttribute(ParametersInstructionPG.ATT_BUILDING_PART_ID),
            "Integer");

      }

      featCRoof.add(abr.getRoof());

    }

    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_ROOF_GEOM;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_ROOF, featCRoof);

    return true;
  }

  // Chargement de l'arrête haute du toit
  public static boolean loadRoofing(String host, String port, String user,
      String pw, String database, Environnement env) throws Exception {

    IFeatureCollection<AbstractBuilding> featCAbstractBuilding = env
        .getBuildings();
    IFeatureCollection<IFeature> featCRoofing = new FT_FeatureCollection<>();

    int idRoofingIni = ExtractIdMaxPG.idMaxTable(host, port, database, user,
        pw, ParametersInstructionPG.TABLE_ROOFING,
        ParametersInstructionPG.ATT_ROOFING_ID);

    for (AbstractBuilding abrf : featCAbstractBuilding) {

      IFeature featTempRoofing = new DefaultFeature(abrf.getRoof().getRoofing());

      if (abrf.getRoof().getRoofing().coord().isEmpty()) {

        System.out
            .println("- Watch out, the Roofing attribute is empty in the case currently being processed -");

      } else {

        AttributeManager
            .addAttribute(featTempRoofing,
                ParametersInstructionPG.ATT_ROOFING_ID, (++idRoofingIni),
                "Integer");

        AttributeManager.addAttribute(featTempRoofing,
            ParametersInstructionPG.ATT_ROOFING_ID_ROOF, abrf.getRoof()
                .getAttribute(ParametersInstructionPG.ATT_ROOF_ID), "Integer");

        featCRoofing.add(featTempRoofing);

      }

    }

    if (!featCRoofing.isEmpty()) {

      PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_ROOFING_GEOM;
      PostgisManager.insertInGeometricTable(host, port, database, user, pw,
          ParametersInstructionPG.TABLE_ROOFING, featCRoofing);

      return true;

    } else {

      return true;

    }
  }

  // Chargement des gouttières
  public static boolean loadGutter(String host, String port, String user,
      String pw, String database, Environnement env) throws Exception {

    IFeatureCollection<AbstractBuilding> featCAbstractBuilding = env
        .getBuildings();
    IFeatureCollection<IFeature> featCGutter = new FT_FeatureCollection<>();

    int idGutterIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_GUTTER,
        ParametersInstructionPG.ATT_GUTTER_ID);

    for (AbstractBuilding abg : featCAbstractBuilding) {

      IFeature featTempGutter = new DefaultFeature(abg.getRoof().getGutter());

      if (abg.getRoof().getGutter().coord().isEmpty()) {

        System.out
            .println("- Watch out, the Gutter attribute is empty in the case currently being processed -");

      } else {

        AttributeManager.addAttribute(featTempGutter,
            ParametersInstructionPG.ATT_GUTTER_ID, (++idGutterIni), "Integer");

        AttributeManager.addAttribute(featTempGutter,
            ParametersInstructionPG.ATT_GUTTER_ID_ROOF, abg.getRoof()
                .getAttribute(ParametersInstructionPG.ATT_ROOF_ID), "Integer");

        featCGutter.add(featTempGutter);

      }

    }

    if (!featCGutter.isEmpty()) {

      PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_GUTTER_GEOM;
      PostgisManager.insertInGeometricTable(host, port, database, user, pw,
          ParametersInstructionPG.TABLE_GUTTER, featCGutter);

      return true;

    } else {

      return true;

    }
  }

  // Chargement des pignons
  public static boolean loadGable(String host, String port, String user,
      String pw, String database, Environnement env) throws Exception {

    IFeatureCollection<AbstractBuilding> featCAbstractBuilding = env
        .getBuildings();
    IFeatureCollection<IFeature> featCGable = new FT_FeatureCollection<>();

    int idGableIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_GABLE,
        ParametersInstructionPG.ATT_GABLE_ID);

    for (AbstractBuilding abga : featCAbstractBuilding) {

      IFeature featTempGable = new DefaultFeature(abga.getRoof().getGable());

      if (abga.getRoof().getGable().coord().isEmpty()) {

        System.out
            .println("- Watch out, the Gable attribute is empty in the case currently being processed -");

      } else {

        AttributeManager.addAttribute(featTempGable,
            ParametersInstructionPG.ATT_GABLE_ID, (++idGableIni), "Integer");

        AttributeManager.addAttribute(featTempGable,
            ParametersInstructionPG.ATT_GABLE_ID_ROOF, abga.getRoof()
                .getAttribute(ParametersInstructionPG.ATT_ROOF_ID), "Integer");

        featCGable.add(featTempGable);

      }

    }

    if (!featCGable.isEmpty()) {

      PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_GABLE_GEOM;
      PostgisManager.insertInGeometricTable(host, port, database, user, pw,
          ParametersInstructionPG.TABLE_GABLE, featCGable);

      return true;

    } else {

      return true;

    }
  }

  // Chargement des murs
  public static boolean loadWall(String host, String port, String user,
      String pw, String database, Environnement env) throws Exception {

    IFeatureCollection<AbstractBuilding> featCAbstractBuilding = env
        .getBuildings();
    IFeatureCollection<IFeature> featCWall = new FT_FeatureCollection<>();

    int idWIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_WALL_SURFACE,
        ParametersInstructionPG.ATT_WALL_SURFACE_ID);

    for (AbstractBuilding ab : featCAbstractBuilding) {

      for (SpecificWallSurface sws : ab.getFacade()) {

        featCWall.add(sws);

        AttributeManager.addAttribute(sws,
            ParametersInstructionPG.ATT_WALL_SURFACE_ID, (++idWIni), "Integer");

        if (ab.getFacade().contains(sws)) {

          AttributeManager.addAttribute(sws,
              ParametersInstructionPG.ATT_WALL_SURFACE_ID_BUILDP,
              ab.getAttribute(ParametersInstructionPG.ATT_BUILDING_PART_ID),
              "Integer");

        }

      }

    }

    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_WALL_SURFACE_GEOM;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_WALL_SURFACE, featCWall);

    return true;

  }

}
