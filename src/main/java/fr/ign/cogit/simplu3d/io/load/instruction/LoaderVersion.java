package fr.ign.cogit.simplu3d.io.load.instruction;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.WktGeOxygene;
import fr.ign.cogit.simplu3d.importer.applicationClasses.AssignBuildingPartToSubParcel;
import fr.ign.cogit.simplu3d.importer.applicationClasses.BuildingImporter;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.util.ExtractIdMaxPG;

public class LoaderVersion {

	// Parameters of connection to the database
	public static String host = "localhost";
	public static String user = "postgres";
	public static String pw = "postgres";
	public static String database = "simplu3d-rennes";
	public static String port = "5432";

	// Parameters for the recovery of the data of versionning
	public static String folder = "/home/mickael/data/mbrasebin/donnees/dataRennes/";
	public static String NOM_FICHIER_BATI = "out/Bati_UB2_3D_V3.shp";
	public static String NOM_ATT_VERSION = "version_id";
	public static String NOM_FICHIER_POINT = "BATI_UB2_V3_POINT.shp";
	public static String ATT_ID_VERSION = "version_id";
	public static String NOM_FICHIER_MNT = "MNT_UB2_L93.asc";

  // Parameters for the processings of SQL requests
  public static String OP_GEOM_FROM_TEXT = "ST_GeomFromText";
  public static String SRID = "2154";

  public static void main(String[] args) throws Exception {

    // Correction of the name of certain variables (by precaution)
    Load.database = database;
    LoaderPostGISTest.database = database;
    AssignBuildingPartToSubParcel.ASSIGN_METHOD = 0; // simple method

    ParametersInstructionPG.ID_VERSION = 40;
    int idVersion = ParametersInstructionPG.ID_VERSION;
    int idUtilisateur = 2;

    // Somes test
    String gh = createWhereClauseVersion(host, port, database, user, pw,
        idVersion);
    retrieveListIdVersionWithoutTableVersion(host, port, database, user, pw);
    retrieveListIdVersionWithTableVersion(host, port, database, user, pw,
        idUtilisateur);

    // We indicate to BuildingImporter that it is about an update
    BuildingImporter.versionning = true;

    // Load of the environment from the database
    Environnement env = LoaderPostGISTest.load(folder, idVersion);

    // We get back the present maximal ID in the table Building of the database
    int idIniBuilding = ExtractIdMaxPG.idMaxTable(host, port, database, user,
        pw, ParametersInstructionPG.TABLE_BUILDING,
        ParametersInstructionPG.ATT_BUILDING_ID);

    // Correction of the collection of BPU contained in the database for our
    // needs
    IFeatureCollection<BasicPropertyUnit> collBPU = updateCollBPUEnvironnement(env);

    // Load of the buildings shapefile added during the versionning
    IFeatureCollection<IFeature> batiColl = ShapefileReader.read(folder
        + NOM_FICHIER_BATI);

    // Processing of buildings imported with the BuildingImporter
    IFeatureCollection<Building> buildColl = BuildingImporter.importBuilding(
        batiColl, collBPU);

    // Update of BuildingPart Table
    searchForNewBuildingPart(host, port, user, pw, database, idIniBuilding,
        collBPU, buildColl);

    // Load of the buildings shapefile deleted during the versionning
    IFeatureCollection<IFeature> pointColl = ShapefileReader.read(folder
        + NOM_FICHIER_POINT);

    // Update of Version Table
    searchForBuildingPartsToBeDeleted(pointColl, env, host, port, database,
        user, pw);

  }

  /**
   * Permet de mettre à jour les tables en relations avec les Building Parts en
   * y ajoutant de nouveaux objets
   * @param host
   * @param port
   * @param user
   * @param pw
   * @param database
   * @param idIniBuilding l'id max de la table Building Part
   * @param collBPU une IFC contenant les BPU
   * @param buildColl une IFC contenant les nouveaux batiments ajoutés par la
   *          version
   * @return
   * @throws Exception
   */
  public static boolean searchForNewBuildingPart(String host, String port,
      String user, String pw, String database, Integer idIniBuilding,
      IFeatureCollection<BasicPropertyUnit> collBPU,
      IFeatureCollection<Building> buildColl) throws Exception {

    for (BasicPropertyUnit currentBPU : collBPU) {

      List<CadastralParcel> listCP = currentBPU.getCadastralParcel();

      for (CadastralParcel currentCP : listCP) {

        IFeatureCollection<SubParcel> featCollSp = currentCP.getSubParcel();

        for (SubParcel currentSP : featCollSp) {

          IFeatureCollection<AbstractBuilding> featCollBP = currentSP
              .getBuildingsParts();

          int idSP = currentSP.getId();

          for (AbstractBuilding currentBP : featCollBP) {

            int idBuildCurrentBP = currentBP.getIdBuilding();

            if (idBuildCurrentBP > idIniBuilding) {

              for (Building currentBuilding : buildColl) {

                int idCurrentBuilding = currentBuilding.getIdBuilding();
                int idVersion = currentBuilding.getIdVersion();

                if (idCurrentBuilding == idBuildCurrentBP) {

                  currentBP.setIdVersion(idVersion);

                  updateTableBuildingPart(host, port, database, user, pw,
                      currentBP, idSP);

                  List<SpecificWallSurface> listWall = currentBP.getFacade();

                  for (SpecificWallSurface sws : listWall) {
                    updateTableWall(host, port, database, user, pw, currentBP,
                        sws);
                  }

                  RoofSurface currentToit = currentBP.getRoof();
                  updateTableRoof(host, port, database, user, pw, currentBP,
                      currentToit);

                  if (currentToit.getRoofing().isEmpty()) {
                    System.out.println(" -- Roofing's empty -- ");
                  } else {
                    updateTableRoofing(host, port, database, user, pw,
                        currentToit);
                  }

                  if (currentToit.getGutter().isEmpty()) {
                    System.out.println(" -- Gutter's empty -- ");
                  } else {
                    updateTableGutter(host, port, database, user, pw,
                        currentToit);
                  }

                  if (currentToit.getGable().isEmpty()) {
                    System.out.println(" -- Gable's empty -- ");
                  } else {
                    updateTableGable(host, port, database, user, pw,
                        currentToit);
                  }

                }

              }

            }

          }

        }

      }

    }

    return true;
  }

  /**
   * Permet de mettre à jour la table version en y ajoutant les id des batiments
   * supprimés dans une version (calcul par intersection entre des points en
   * entrée correspondants aux batiments détruits et les polygones des batiments
   * contenus en base)
   * 
   * @param pointColl les points correspondants aux batiments détruits
   * @param env l'environnement contenu en base
   * @param host
   * @param port
   * @param database
   * @param user
   * @param pw
   * @return
   * @throws Exception
   */
  public static boolean searchForBuildingPartsToBeDeleted(
      IFeatureCollection<IFeature> pointColl, Environnement env, String host,
      String port, String database, String user, String pw) throws Exception {

    for (IFeature currentPoint : pointColl) {

      // We get back the version number of the point
      Object attPoint = currentPoint.getAttribute(ATT_ID_VERSION);
      int idVersion;

      if (attPoint != null) {

        String attPointIdVersion = attPoint.toString();
        idVersion = Integer.parseInt(attPointIdVersion);

      } else {

        System.out.println("L'attribut ID Version n'a pas été trouvé");
        return false;

      }

      IGeometry geomPoint = currentPoint.getGeom();

      // We look for the building which intersects with the geometry of
      // the
      // point
      for (AbstractBuilding currentBuilding : env.getBuildings()) {

        IGeometry geomBuild = currentBuilding.getGeom();

        if (geomPoint.intersects(geomBuild)) {

          int idBuilding = currentBuilding.getId();

          // We launch the update of the table Version
          updateTableVersion(idVersion, idBuilding, host, port, database, user,
              pw);

        }

      }

    }

    return true;

  }

  /**
   * Construit de nouvelles relations entre les BPU, les parcelles et les
   * sous-parcelles
   * @param env
   * @return
   * @throws Exception
   */
  public static IFeatureCollection<BasicPropertyUnit> updateCollBPUEnvironnement(
      Environnement env) throws Exception {

    IFeatureCollection<BasicPropertyUnit> featBPU = env.getBpU();
    IFeatureCollection<CadastralParcel> featCadPar = env.getCadastralParcels();
    IFeatureCollection<SubParcel> featSubPar = env.getSubParcels();

    // On initialise une IFC<BPU> vide
    IFeatureCollection<BasicPropertyUnit> collBPU = new FT_FeatureCollection<BasicPropertyUnit>();

    // On reconstruit les liaisons dans l'ordre adapté à nos besoins
    for (BasicPropertyUnit currentBPU : featBPU) {

      // On récupère l'ID de la BPU actuelle
      int idBPU = currentBPU.getId();

      // on initialise une liste de parcelles
      List<CadastralParcel> listParcel = new ArrayList<CadastralParcel>();

      for (CadastralParcel currentCadPar : featCadPar) {

        // On récupère l'ID de la parcelle et l'ID de la BPU à laquelle
        // elle
        // appartient
        int idCadPar = currentCadPar.getId();
        int idBPUCadPar = currentCadPar.getIdBPU();

        // On initialise une IFC<SP>
        IFeatureCollection<SubParcel> collSP = new FT_FeatureCollection<SubParcel>();

        // On compare nos deux ID BPU et si égalité on passe aux SP
        if (idBPU == idBPUCadPar) {

          for (SubParcel currentSubPar : featSubPar) {

            // On récupère l'ID de la parcelle à laquelle la
            // sous-parcelle
            // actuelle appartient
            int idCadParSubPar = currentSubPar.getIdCadPar();

            // On compare nos deux ID CP et si égalité
            if (idCadPar == idCadParSubPar) {

              // On ajoute la Sp actuelle à la IFC<SP>
              collSP.add(currentSubPar);

            }

          }

          // On ajoute la IFC<SP> à la parcelle cadastrale actuekke
          currentCadPar.setSubParcel(collSP);

          // On ajoute la parcelle cadastrale actuelle à la liste des
          // parcelles
          // traitées
          listParcel.add(currentCadPar);

        }

      }

      // On ajoute la liste des parcelles traitées à la BPU actuelle
      currentBPU.setCadastralParcel(listParcel);

      // On ajoute la BPU actuelle à la IFC<BPU> des BPU traitées
      collBPU.add(currentBPU);

    }

    // on retourne la IFC<BPU> complétée avec les liaisons qui nous
    // conviennent
    return collBPU;

  }

  /**
   * Exécute une requête SQL pour la table Version
   * @param idVersion
   * @param idBuild
   * @param host
   * @param port
   * @param database
   * @param user
   * @param pw
   * @return
   * @throws Exception
   */
  public static boolean updateTableVersion(Integer idVersion, Integer idBuild,
      String host, String port, String database, String user, String pw)
      throws Exception {

    // On cherche l'ID max de la table Version
    int idBIni = (ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_VERSION,
        ParametersInstructionPG.ATT_VERSION_ID));

    // On produit l'ID qui servira pour l'objet actuel
    int idCurrent = idBIni + 1;

    // On prépare la requête pour mettre à jour la table version
    String sql_insert = "INSERT INTO " + ParametersInstructionPG.TABLE_VERSION
        + " (" + ParametersInstructionPG.ATT_VERSION_ID + ", "
        + ParametersInstructionPG.ATT_VERSION_ID_BUILD_DEL + ", "
        + ParametersInstructionPG.ATT_VERSION_ID_VERSION_BUILD + ") VALUES ("
        + idCurrent + ", " + idBuild + ", " + idVersion + ")";

    // On execute la requête dans PostGis à l'aide du PostGisManager
    PostgisManager.executeSimpleRequestInsert(host, port, database, user, pw,
        sql_insert);

    return true;
  }

  /**
   * Exécute une requête SQL pour la table Building
   * @param host
   * @param port
   * @param user
   * @param pw
   * @param database
   * @param building
   * @return
   * @throws Exception
   */
  public static int updateTableBuilding(String host, String port, String user,
      String pw, String database, Building building) throws Exception {

    // On cherche l'ID max de la table Building
    int idBIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_BUILDING,
        ParametersInstructionPG.ATT_BUILDING_ID);

    // On produit l'ID qui servira pour l'objet actuel
    int idCurrent = idBIni + 1;

    // on attribut l'ID trouvé au building
    building.setIdBuilding(idCurrent);

    // On prépare la requête pour mettre à jour la table version
    String sql_insert = "INSERT INTO " + ParametersInstructionPG.TABLE_BUILDING
        + " (" + ParametersInstructionPG.ATT_BUILDING_ID + ") VALUES ("
        + idCurrent + ")";

    // On execute la requête dans PostGis à l'aide du PostGisManager
    PostgisManager.executeSimpleRequestInsert(host, port, database, user, pw,
        sql_insert);

    return idBIni;
  }

  /**
   * Exécute une requête SQL pour la table Building Part
   * @param host
   * @param port
   * @param database
   * @param user
   * @param pw
   * @param currentBP
   * @param idSP
   * @return
   * @throws Exception
   */
  public static boolean updateTableBuildingPart(String host, String port,
      String database, String user, String pw, AbstractBuilding currentBP,
      Integer idSP) throws Exception {

    // On cherche l'ID max de la table Building Part
    int idBIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_BUILDING_PART,
        ParametersInstructionPG.ATT_BUILDING_PART_ID);

    // On produit l'ID qui servira pour l'objet actuel
    int idCurrent = idBIni + 1;

    // On en profite pour set l'id de la partie de batiment
    currentBP.setId(idCurrent);

    // On récupère l'ID du batiment
    int ibBuilding = currentBP.getIdBuilding();

    // On récupère l'Id de la version
    int idversion = currentBP.getIdVersion();

    // On récupère la géométrie et on la stocke au sein d'un string au
    // format
    // WKT
    String geom = WktGeOxygene.makeWkt(currentBP.getFootprint());

    // On prépare la requête pour mettre à jour la table
    String sql_insert = "INSERT INTO "
        + ParametersInstructionPG.TABLE_BUILDING_PART + " ("
        + ParametersInstructionPG.ATT_BUILDING_PART_ID + ", "
        + ParametersInstructionPG.ATT_BUILDING_PART_ID_BUILD + ", "
        + ParametersInstructionPG.ATT_BUILDING_PART_ID_SUBPAR + ", "
        + ParametersInstructionPG.ATT_BUILDING_PART_ID_VERSION + ", "
        + ParametersInstructionPG.ATT_BUILDING_PART_GEOM + ") VALUES ("
        + idCurrent + ", " + ibBuilding + ", " + idSP + ", " + idversion + ", "
        + OP_GEOM_FROM_TEXT + "('" + geom + "', " + SRID + " )" + ")";

    // On execute la requête dans PostGis à l'aide du PostGisManager
    PostgisManager.executeSimpleRequestInsert(host, port, database, user, pw,
        sql_insert);

    return true;
  }

  /**
   * Exécute une requête SQL pour la table Wall
   * @param host
   * @param port
   * @param database
   * @param user
   * @param pw
   * @param currentBP
   * @param sws
   * @return
   * @throws Exception
   */
  public static boolean updateTableWall(String host, String port,
      String database, String user, String pw, AbstractBuilding currentBP,
      SpecificWallSurface sws) throws Exception {

    // On cherche l'ID max de la table Wall
    int idBIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_WALL_SURFACE,
        ParametersInstructionPG.ATT_WALL_SURFACE_ID);

    // On produit l'ID qui servira pour l'objet actuel
    int idCurrent = idBIni + 1;

    // On récupère l'Id de la BP
    int idBP = currentBP.getId();

    // On en profite pour set l'Id du mur
    sws.setId(idCurrent);

    // On récupère la géométrie et on la stocke au sein d'un string au
    // format
    // WKT
    String geom = WktGeOxygene.makeWkt(sws.getGeom());

    // On prépare la requête pour mettre à jour la table
    String sql_insert = "INSERT INTO "
        + ParametersInstructionPG.TABLE_WALL_SURFACE + " ("
        + ParametersInstructionPG.ATT_WALL_SURFACE_ID + ", "
        + ParametersInstructionPG.ATT_WALL_SURFACE_ID_BUILDP + ", "
        + ParametersInstructionPG.ATT_WALL_SURFACE_GEOM + ") VALUES ("
        + idCurrent + ", " + idBP + ", " + OP_GEOM_FROM_TEXT + "('" + geom
        + "', " + SRID + " )" + ")";

    // On execute la requête dans PostGis à l'aide du PostGisManager
    PostgisManager.executeSimpleRequestInsert(host, port, database, user, pw,
        sql_insert);

    return true;
  }

  /**
   * Exécute une requête SQL pour la table Roof
   * @param host
   * @param port
   * @param database
   * @param user
   * @param pw
   * @param currentBP
   * @param currentToit
   * @return
   * @throws Exception
   */
  public static boolean updateTableRoof(String host, String port,
      String database, String user, String pw, AbstractBuilding currentBP,
      RoofSurface currentToit) throws Exception {

    // On cherche l'ID max de la table Roof
    int idBIni = ExtractIdMaxPG
        .idMaxTable(host, port, database, user, pw,
            ParametersInstructionPG.TABLE_ROOF,
            ParametersInstructionPG.ATT_ROOF_ID);

    // On produit l'ID qui servira pour l'objet actuel
    int idCurrent = idBIni + 1;

    // On en profite pour set l'Id du mur
    currentToit.setId(idCurrent);

    // On récupère l'Id de la BP
    int idBP = currentBP.getId();

    // On récupère les angles
    double angleMin = currentToit.getAngleMin();
    double angleMax = currentToit.getAngleMax();

    // On récupère la géométrie et on la stocke au sein d'un string au
    // format
    // WKT
    String geom = WktGeOxygene.makeWkt(currentToit.getGeom());

    // On prépare la requête pour mettre à jour la table
    String sql_insert = "INSERT INTO " + ParametersInstructionPG.TABLE_ROOF
        + " (" + ParametersInstructionPG.ATT_ROOF_ID + ", "
        + ParametersInstructionPG.ATT_ROOF_ID_BUILDPART + ", "
        + ParametersInstructionPG.ATT_ROOF_ANGLE_MAX + ", "
        + ParametersInstructionPG.ATT_ROOF_ANGLE_MIN + ", "
        + ParametersInstructionPG.ATT_ROOF_GEOM + ") VALUES (" + idCurrent
        + ", " + idBP + ", " + angleMax + ", " + angleMin + ", "
        + OP_GEOM_FROM_TEXT + "('" + geom + "', " + SRID + " )" + ")";

    // On execute la requête dans PostGis à l'aide du PostGisManager
    PostgisManager.executeSimpleRequestInsert(host, port, database, user, pw,
        sql_insert);

    return true;
  }

  /**
   * Exécute une requête SQL pour la table Roofing
   * @param host
   * @param port
   * @param database
   * @param user
   * @param pw
   * @param currentToit
   * @return
   * @throws Exception
   */
  public static boolean updateTableRoofing(String host, String port,
      String database, String user, String pw, RoofSurface currentToit)
      throws Exception {

    // On cherche l'ID max de la table Roofing
    int idBIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_ROOFING,
        ParametersInstructionPG.ATT_ROOFING_ID);

    // On produit l'ID qui servira pour l'objet actuel
    int idCurrent = idBIni + 1;

    // On récupère l'Id du toit
    int idToit = currentToit.getId();

    // On récupère la géométrie et on la stocke au sein d'un string au
    // format
    // WKT
    String geom = WktGeOxygene.makeWkt(currentToit.getRoofing());

    // On prépare la requête pour mettre à jour la table
    String sql_insert = "INSERT INTO " + ParametersInstructionPG.TABLE_ROOFING
        + " (" + ParametersInstructionPG.ATT_ROOFING_ID + ", "
        + ParametersInstructionPG.ATT_ROOFING_ID_ROOF + ", "
        + ParametersInstructionPG.ATT_ROOFING_GEOM + ") VALUES (" + idCurrent
        + ", " + idToit + ", " + OP_GEOM_FROM_TEXT + "('" + geom + "', " + SRID
        + " )" + ")";

    // On execute la requête dans PostGis à l'aide du PostGisManager
    PostgisManager.executeSimpleRequestInsert(host, port, database, user, pw,
        sql_insert);

    return true;
  }

  /**
   * Exécute une requête SQL pour la table Gutter
   * @param host
   * @param port
   * @param database
   * @param user
   * @param pw
   * @param currentToit
   * @return
   * @throws Exception
   */
  public static boolean updateTableGutter(String host, String port,
      String database, String user, String pw, RoofSurface currentToit)
      throws Exception {

    // On cherche l'ID max de la table Gutter
    int idBIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_GUTTER,
        ParametersInstructionPG.ATT_GUTTER_ID);

    // On produit l'ID qui servira pour l'objet actuel
    int idCurrent = idBIni + 1;

    // On récupère l'Id du toit
    int idToit = currentToit.getId();

    // On récupère la géométrie et on la stocke au sein d'un string au
    // format
    // WKT
    String geom = WktGeOxygene.makeWkt(currentToit.getGutter());

    // On prépare la requête pour mettre à jour la table
    String sql_insert = "INSERT INTO " + ParametersInstructionPG.TABLE_GUTTER
        + " (" + ParametersInstructionPG.ATT_GUTTER_ID + ", "
        + ParametersInstructionPG.ATT_GUTTER_ID_ROOF + ", "
        + ParametersInstructionPG.ATT_GUTTER_GEOM + ") VALUES (" + idCurrent
        + ", " + idToit + ", " + OP_GEOM_FROM_TEXT + "('" + geom + "', " + SRID
        + " )" + ")";

    // On execute la requête dans PostGis à l'aide du PostGisManager
    PostgisManager.executeSimpleRequestInsert(host, port, database, user, pw,
        sql_insert);

    return true;
  }

  /**
   * Exécute une requête SQL pour la table Gable
   * @param host
   * @param port
   * @param database
   * @param user
   * @param pw
   * @param currentToit
   * @return
   * @throws Exception
   */
  public static boolean updateTableGable(String host, String port,
      String database, String user, String pw, RoofSurface currentToit)
      throws Exception {

    // On cherche l'ID max de la table Gable
    int idBIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_GABLE,
        ParametersInstructionPG.ATT_GABLE_ID);

    // On produit l'ID qui servira pour l'objet actuel
    int idCurrent = idBIni + 1;

    // On récupère l'Id du toit
    int idToit = currentToit.getId();

    // On récupère la géométrie et on la stocke au sein d'un string au
    // format
    // WKT
    String geom = WktGeOxygene.makeWkt(currentToit.getGable());

    // On prépare la requête pour mettre à jour la table
    String sql_insert = "INSERT INTO " + ParametersInstructionPG.TABLE_GABLE
        + " (" + ParametersInstructionPG.ATT_GABLE_ID + ", "
        + ParametersInstructionPG.ATT_GABLE_ID_ROOF + ", "
        + ParametersInstructionPG.ATT_GABLE_GEOM + ") VALUES (" + idCurrent
        + ", " + idToit + ", " + OP_GEOM_FROM_TEXT + "('" + geom + "', " + SRID
        + " )" + ")";

    // On execute la requête dans PostGis à l'aide du PostGisManager
    PostgisManager.executeSimpleRequestInsert(host, port, database, user, pw,
        sql_insert);

    return true;
  }

  /**
   * Permet de construire une clause SQL pour charger les Building Parts de
   * l'environnement par défaut ou pour une version particulière
   * @param host
   * @param port
   * @param database
   * @param user
   * @param pw
   * @param idVersion
   * @return
   * @throws Exception
   */
  public static String createWhereClauseVersion(String host, String port,
      String database, String user, String pw, Integer idVersion)
      throws Exception {

    // On récupère la liste des id à supprimer
    List<Integer> listIdDeleted = searchForIdToDeleteInVersion(host, port,
        database, user, pw, idVersion);

    // On récupère les noms des différents attributs
    String AttIdVersion = ParametersInstructionPG.ATT_BUILDING_PART_ID_VERSION;
    String AttIdBP = ParametersInstructionPG.ATT_BUILDING_PART_ID;

    // On prépare la clause pour la requête
    String whereClause = "";

    // Cas 1 : on charge l'env par défaut
    if (idVersion == -1) {

      whereClause = AttIdVersion + " IS NULL ";

      // Cas 2 : on récupère les données pour la version donnée
    } else {

      whereClause = AttIdVersion + " = " + Integer.toString(idVersion)
          + " OR ( " + AttIdVersion + " IS NULL";

      for (Integer valId : listIdDeleted) {

        whereClause = whereClause + " AND " + AttIdBP + " != "
            + Integer.toString(valId);

      }
      whereClause = whereClause + " )";
    }

    System.out.println("Requête SQL : SELECT * FROM building_part WHERE "
        + whereClause);

    // on renvoi la clause
    return whereClause;
  }

  /**
   * Permet d'obtenir les identifiants des BP supprimées dans une version
   * @param host
   * @param port
   * @param database
   * @param user
   * @param pw
   * @param idVersion
   * @return
   * @throws Exception
   */
  public static List<Integer> searchForIdToDeleteInVersion(String host,
      String port, String database, String user, String pw, Integer idVersion)
      throws Exception {

    // On prépare la liste qui va recevoir les id
    List<Integer> listIdDeleted = new ArrayList<Integer>();

    // On récupère les noms des éléments de la base de données
    String table = ParametersInstructionPG.TABLE_VERSION;
    String AttVersionBuild = ParametersInstructionPG.ATT_VERSION_ID_VERSION_BUILD;
    String AttIdBuildDel = ParametersInstructionPG.ATT_VERSION_ID_BUILD_DEL;

    // On prépare la clause pour la requête SQL
    String whereClause = AttVersionBuild + " = " + Integer.toString(idVersion);

    // On récupère le contenu de la table version
    IFeatureCollection<IFeature> featColl = PostgisManager
        .loadNonGeometricTableWhereClause(host, port, database, user, pw,
            table, whereClause);

    // On isole les id des build à supprimer et on renseigne la liste
    for (IFeature feat : featColl) {

      Object att = feat.getAttribute(AttIdBuildDel);

      if (att != null) {
        String attStr = att.toString();
        int attint = Integer.parseInt(attStr);
        listIdDeleted.add(attint);
      }

    }

    // On renvoi la liste
    return listIdDeleted;
  }

  /**
   * Permet de retrouver la liste des versions contenues en base de données
   * @param host
   * @param port
   * @param database
   * @param user
   * @param pw
   * @return
   * @throws Exception
   */
  public static List<Integer> retrieveListIdVersionWithoutTableVersion(
      String host, String port, String database, String user, String pw)
      throws Exception {

    // On prépare la liste qui contiendra les id ainsi que deux listes
    // temporaires
    List<Integer> listIdVersion = new ArrayList<Integer>();
    List<Integer> listTempV = new ArrayList<Integer>();
    List<Integer> listTempBP = new ArrayList<Integer>();

    // On récupère les noms des éléments de la base de données
    String tableVersion = ParametersInstructionPG.TABLE_VERSION;
    String tableBuildingPart = ParametersInstructionPG.TABLE_BUILDING_PART;
    String attidVersionBP = ParametersInstructionPG.ATT_BUILDING_PART_ID_VERSION;
    String attidVersionVers = ParametersInstructionPG.ATT_VERSION_ID_VERSION_BUILD;

    // On prépare la clause
    String whereClause1 = " 1 = 1 ORDER BY " + attidVersionVers;

    // On récupère le contenu de la table version
    IFeatureCollection<IFeature> featCollVersion = PostgisManager
        .loadNonGeometricTableWhereClause(host, port, database, user, pw,
            tableVersion, whereClause1);

    // On récupère l'id version et on le stocke dans une liste temporaire
    for (IFeature feat : featCollVersion) {

      Object att = feat.getAttribute(attidVersionVers);

      if (att != null) {
        String attStr = att.toString();
        int attint = Integer.parseInt(attStr);
        if (!listTempV.contains(attint)) {
          listTempV.add(attint);
        }
      }
    }

    // On prépare la clause
    String whereClause2 = " 1 = 1 ORDER BY " + attidVersionBP;

    // On récupère le contenu de la table BP
    IFeatureCollection<IFeature> featCollBP = PostgisManager
        .loadGeometricTableWhereClause(host, port, database, user, pw,
            tableBuildingPart, whereClause2);

    // On récupère l'id version et on le stocke dans une liste temporaire
    for (IFeature feat : featCollBP) {

      Object att = feat.getAttribute(attidVersionBP);

      if (att != null) {
        String attStr = att.toString();
        int attint = Integer.parseInt(attStr);
        if (!listTempBP.contains(attint)) {
          listTempBP.add(attint);
        }
      }
    }

    // on compare le contenu des deux listes et on complète la liste finale
    if (listTempBP.containsAll(listTempV)
        && (listTempBP.size() == listTempV.size())) {
      listIdVersion.addAll(listTempV);
      System.out.println("ID version : " + listIdVersion);
    } else {
      System.out.println("Problème dans les numéros de version");
    }

    // on retourne la liste d'id
    return listIdVersion;

  }

  /**
   * Permet de retrouver la liste des versions contenues en base de données
   * @param host
   * @param port
   * @param database
   * @param user
   * @param pw
   * @param idUtilisateur
   * @return
   * @throws Exception
   */
  public static List<Integer> retrieveListIdVersionWithTableVersion(
      String host, String port, String database, String user, String pw,
      Integer idUtilisateur) throws Exception {

    // On prépare la liste pour les id
    List<Integer> listIdVersion = new ArrayList<Integer>();

    // On récupère les noms des éléments de la base de données
    String tableUserVersion = ParametersInstructionPG.TABLE_USER_VERSION;
    String attidUser = ParametersInstructionPG.ATT_USER_VERS_ID_USER;
    String attidVersion = ParametersInstructionPG.ATT_USER_VERS_ID_VERSION;

    // On prépare la clause de la requête SQL
    String whereClause = "";

    // On complète la clause en fonction de l'id de l'utilisateur
    if (idUtilisateur == -1) {
      whereClause = "1 = 1" + " ORDER BY " + attidVersion;
    } else {
      whereClause = attidUser + " = " + Integer.toString(idUtilisateur)
          + " ORDER BY " + attidVersion;
    }

    // On charge le contenu de la table user version
    IFeatureCollection<IFeature> featColl = PostgisManager
        .loadNonGeometricTableWhereClause(host, port, database, user, pw,
            tableUserVersion, whereClause);

    // On isole les id version et on complete la liste
    for (IFeature feat : featColl) {

      Object att = feat.getAttribute(attidVersion);

      if (att != null) {
        String attStr = att.toString();
        int attint = Integer.parseInt(attStr);
        if (!listIdVersion.contains(attint)) {
          listIdVersion.add(attint);
        }
      }
    }

    // On retourne la liste
    return listIdVersion;

  }

  /**
   * Permet de supprimer la valeur Z de parcelles cadastrales
   * @param collBPU
   * @return
   */
  public static IFeatureCollection<BasicPropertyUnit> deleteZCadastralParcel(
      IFeatureCollection<BasicPropertyUnit> collBPU) {

    for (BasicPropertyUnit featBPU : collBPU) {

      for (CadastralParcel featCp : featBPU.getCadastralParcel()) {

        List<IOrientableSurface> lIOS = FromGeomToSurface.convertGeom(featCp
            .getGeom());

        IMultiSurface<IOrientableSurface> polHorz = Util.detectNonVertical(
            lIOS, 0.2);

        for (IOrientableSurface os : polHorz) {

          IGeometry batiGeom = os;

          for (IDirectPosition dp : batiGeom.coord()) {

            double x = dp.getX();
            double y = dp.getY();

            dp.setCoordinate(x, y);

          }

        }

        featCp.setGeom(polHorz);

      }

    }

    return collBPU;

  }

  /**
   * Permet de suppimer la valeur Z de Batiments
   * @param batiColl
   * @return
   */
  public static IFeatureCollection<IFeature> deleteZBuilding(
      IFeatureCollection<IFeature> batiColl) {

    for (IFeature featBatid : batiColl) {

      List<IOrientableSurface> lIOS = FromGeomToSurface.convertGeom(featBatid
          .getGeom());

      IMultiSurface<IOrientableSurface> polHorz = Util.detectNonVertical(lIOS,
          0.2);

      for (IOrientableSurface os : polHorz) {

        IGeometry batiGeom = os;

        for (IDirectPosition dp : batiGeom.coord()) {

          double x = dp.getX();
          double y = dp.getY();

          dp.setCoordinate(x, y);

        }

      }

      featBatid.setGeom(polHorz);

    }

    return batiColl;

  }

}
