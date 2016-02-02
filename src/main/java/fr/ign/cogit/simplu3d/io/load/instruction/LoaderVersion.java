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
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.util.ExtractIdMaxPG;

public class LoaderVersion {

  // Paramètres de connection à la base de données
  public static String host = "localhost";
  public static String user = "postgres";
  public static String pw = "postgres";
  public static String database = "test_simplu3d";
  public static String port = "5432";

  // Paramètres pour la récupération des données de versionning
  public static String folder = "D:/0_Masson/1_CDD_SIMPLU/2_Travail/0_Workspace/"
      + "simplu3d/simplu3D-rules/src/main/resources/fr/ign/cogit/simplu3d/data/dataRennes/";
  public static String NOM_FICHIER_BATI = "out/Bati_UB2_3D_V3.shp";
  public static String NOM_ATT_VERSION = "version_id";
  public static String NOM_FICHIER_POINT = "BATI_UB2_V3_POINT.shp";
  public static String ATT_ID_VERSION = "version_id";
  public static String NOM_FICHIER_MNT = "MNT_UB2_L93.asc";

  // Paramètres pour les traitements des requêtes SQL
  public static String OP_GEOM_FROM_TEXT = "ST_GeomFromText";
  public static String SRID = "2154";

  public static void main(String[] args) throws Exception {

    // Correction du nom de certaines variables (par précaution)
    LoaderPostGISTest.NOM_MNT = NOM_FICHIER_MNT;
    Load.database = database;
    LoaderPostGISTest.database = database;
    AssignBuildingPartToSubParcel.ASSIGN_METHOD = 0; // méthode "simple"

    // Chargement de l'environnement depuis la base de données
    Environnement env = LoaderPostGISTest.load(folder);

    // Correction de la collection de BPU contenue das la BD pour nos besoins
    IFeatureCollection<BasicPropertyUnit> collBPU = updateCollBPUEnvironnement(env);

    // Chargement du shapefile des batiments ajoutées lors du versionning
    IFeatureCollection<IFeature> batiColl = ShapefileReader.read(folder
        + NOM_FICHIER_BATI);

    // Traitement des batiments importés à l'aide du BuildingImporter
    IFeatureCollection<Building> buildColl = BuildingImporter.importBuilding(
        batiColl, collBPU);

    // TODO : reprendre cette partie du code
    // Update de la table building
    for (Building buil : buildColl) {

      // Partie très sensible à la casse
      if (buil.getIdBuilding() == 0) {
        System.out.println("New Building, update table building");

        int idBuilding = updateTableBuilding(host, port, user, pw, database,
            buil);
        IGeometry geomBuilding = buil.getGeom();

        searchBuildingPart(host, port, user, pw, database, idBuilding,
            geomBuilding, collBPU);

      }
    }

    // Chargement du shapefile des batiments supprimés lors du versionning
    IFeatureCollection<IFeature> pointColl = ShapefileReader.read(folder
        + NOM_FICHIER_POINT);

    // Update de la table Version après recherche des batiments supprimés
    // searchForBuildingPartsToBeDeleted(pointColl, env, host, port, database,
    // user, pw);

  }

  public static boolean searchForBuildingPartsToBeDeleted(
      IFeatureCollection<IFeature> pointColl, Environnement env, String host,
      String port, String database, String user, String pw) throws Exception {

    for (IFeature currentPoint : pointColl) {

      // On récupère le numéro de version du point
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

      // On cherche le batiment qui intersecte avec la géométrie du point
      for (AbstractBuilding currentBuilding : env.getBuildings()) {

        IGeometry geomBuild = currentBuilding.getGeom();

        if (geomPoint.intersects(geomBuild)) {

          int idBuilding = currentBuilding.getId();

          // On lance la mise à jour de la table version
          updateTableVersion(idVersion, idBuilding, host, port, database, user,
              pw);

        }

      }

    }

    return true;

  }

  public static boolean searchBuildingPart(String host, String port,
      String user, String pw, String database, Integer idBuilding,
      IGeometry geomBuilding, IFeatureCollection<BasicPropertyUnit> collBPU)
      throws Exception {

    // TODO : reprendre entièrement le code de cette fonction...

    for (BasicPropertyUnit currentBPU : collBPU) {

      List<CadastralParcel> listCP = currentBPU.getCadastralParcel();

      for (CadastralParcel currentCP : listCP) {

        IFeatureCollection<SubParcel> featCollSp = currentCP.getSubParcel();

        for (SubParcel currentSP : featCollSp) {

          IFeatureCollection<AbstractBuilding> featCollBP = currentSP
              .getBuildingsParts();

          int idSP = currentSP.getId();

          for (AbstractBuilding currentBP : featCollBP) {

            IGeometry geo = currentBP.getFootprint();

            // Partie très sensible à la casse
            if (currentBP.getIdBuilding() == 0) {
              System.out.println("on passe ici");

              if (geo.intersects(geomBuilding.buffer(0.01))) {
                System.out
                    .println("INTERSECTION -----------------------------------------------------------");

                updateTableBuildingPart(host, port, database, user, pw,
                    currentBP, idBuilding, idSP);

              } else {

                System.out.println("pas d'intersection");
              }

            }

          }

        }

      }

    }

    return true;
  }

  public static IFeatureCollection<BasicPropertyUnit> updateCollBPUEnvironnement(
      Environnement env) throws Exception {

    // On cherche les BPU, les parcelles et les Sous-parcelles contenues dans
    // l'environnement
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

        // On récupère l'ID de la parcelle et l'ID de la BPU à laquelle elle
        // appartient
        int idCadPar = currentCadPar.getId();
        int idBPUCadPar = currentCadPar.getIdBPU();

        // On initialise une IFC<SP>
        IFeatureCollection<SubParcel> collSP = new FT_FeatureCollection<SubParcel>();

        // On compare nos deux ID BPU et si égalité on passe aux SP
        if (idBPU == idBPUCadPar) {

          for (SubParcel currentSubPar : featSubPar) {

            // On récupère l'ID de la parcelle à laquelle la sous-parcelle
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

          // On ajoute la parcelle cadastrale actuelle à la liste des parcelles
          // traitées
          listParcel.add(currentCadPar);

        }

      }

      // On ajoute la liste des parcelles traitées à la BPU actuelle
      currentBPU.setCadastralParcel(listParcel);

      // On ajoute la BPU actuelle à la IFC<BPU> des BPU traitées
      collBPU.add(currentBPU);

    }

    // on retourne la IFC<BPU> complétée avec les liaisons qui nous conviennent
    return collBPU;

  }

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
        + idBIni + ")";

    // On execute la requête dans PostGis à l'aide du PostGisManager
    PostgisManager.executeSimpleRequestInsert(host, port, database, user, pw,
        sql_insert);

    return idBIni;
  }

  public static boolean updateTableBuildingPart(String host, String port,
      String database, String user, String pw, AbstractBuilding currentBP,
      Integer idBuilding, Integer idSP) throws Exception {

    // On récupère la valeur de l'attribut ID Version
    Object attBuild = currentBP.getAttribute(NOM_ATT_VERSION);
    int objInt;

    // On vérifie que l'objet n'est pas vide sinon on renvoi false
    if (attBuild != null) {
      String objStr = attBuild.toString();
      objInt = Integer.parseInt(objStr);
    } else {
      System.out.println("L'attribut ID Version n'a pas été trouvé");
      return false;
    }

    // On cherche l'ID max de la table Building Part
    int idBIni = ExtractIdMaxPG.idMaxTable(host, port, database, user, pw,
        ParametersInstructionPG.TABLE_BUILDING_PART,
        ParametersInstructionPG.ATT_BUILDING_PART_ID);

    // On produit l'ID qui servira pour l'objet actuel
    int idCurrent = idBIni + 1;

    // On récupère la géométri et on la stocke au sein d'un string au format WKT
    String geom = WktGeOxygene.makeWkt(currentBP.getFootprint());

    // On prépare la requête pour mettre à jour la table
    String sql_insert = "INSERT INTO "
        + ParametersInstructionPG.TABLE_BUILDING_PART + " ("
        + ParametersInstructionPG.ATT_BUILDING_PART_ID + ", "
        + ParametersInstructionPG.ATT_BUILDING_PART_ID_BUILD + ", "
        + ParametersInstructionPG.ATT_BUILDING_PART_ID_SUBPAR + ", "
        + ParametersInstructionPG.ATT_BUILDING_PART_ID_VERSION + ", "
        + ParametersInstructionPG.ATT_BUILDING_PART_GEOM + ") VALUES ("
        + idCurrent + ", " + idBuilding + ", " + idSP + ", " + objInt + ", "
        + OP_GEOM_FROM_TEXT + "('" + geom + "', " + SRID + " )" + ")";

    // On execute la requête dans PostGis à l'aide du PostGisManager
    PostgisManager.executeSimpleRequestInsert(host, port, database, user, pw,
        sql_insert);

    return true;
  }

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

  public static IFeatureCollection<Building> importVersionBuilding(
      IFeatureCollection<IFeature> featBuilding) {

    IFeatureCollection<Building> featBuildingOut = new FT_FeatureCollection<>();

    for (IFeature feat : featBuilding) {

      Building build = new Building();

      Object attBuild = feat
          .getAttribute(ParametersInstructionPG.ATT_BUILDING_PART_ID_VERSION);

      if (attBuild != null) {

        String objStr = attBuild.toString();

        int objInt = Integer.parseInt(objStr);

        build.setIdVersion(objInt);

      }

      featBuildingOut.add(build);

    }

    return featBuildingOut;

  }

}
