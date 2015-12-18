package fr.ign.cogit.simplu3d.io.load.instruction;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTMArea;
import fr.ign.cogit.geoxygene.sig3d.util.ColorShade;
import fr.ign.cogit.simplu3d.importer.applicationClasses.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.model.application.Environnement;

// The objective here is to create collection from data contained in a PostGIS
// database to be able to feed the environment

public class LoaderPostGISTest {

  // Information about the PostGIS database
  public static String host = Load.host;
  public static String user = Load.user;
  public static String pw = Load.pw;
  public static String database = Load.database;
  public static String port = Load.port;

  // List of the names of tables in PostGIS
  public final static String NOM_TABLE_AXE = ParametersInstructionPG.TABLE_AXE;
  public final static String NOM_TABLE_BASIC_PROPERTY_UNIT = ParametersInstructionPG.TABLE_BASIC_PROPERTY_UNIT;
  public final static String NOM_TABLE_BUILDING = ParametersInstructionPG.TABLE_BUILDING;
  public final static String NOM_TABLE_BUILDING_PART = ParametersInstructionPG.TABLE_BUILDING_PART;
  public final static String NOM_TABLE_CADASTRAL_PARCEL = ParametersInstructionPG.TABLE_CADASTRAL_PARCEL;
  public final static String NOM_TABLE_DOC_URBA = ParametersInstructionPG.TABLE_DOC_URBA;
  public final static String NOM_TABLE_GABLE = ParametersInstructionPG.TABLE_GABLE;
  public final static String NOM_TABLE_GUTTER = ParametersInstructionPG.TABLE_GUTTER;
  public final static String NOM_TABLE_PUBLIC_SPACE = ParametersInstructionPG.TABLE_PUBLIC_SPACE;
  public final static String NOM_TABLE_ROAD = ParametersInstructionPG.TABLE_ROAD;
  public final static String NOM_TABLE_ROOF = ParametersInstructionPG.TABLE_ROOF;
  public final static String NOM_TABLE_ROOFING = ParametersInstructionPG.TABLE_ROOFING;
  public final static String NOM_TABLE_SPECIFIC_CBOUNDARY = ParametersInstructionPG.TABLE_SPECIFIC_CBOUNDARY;
  public final static String NOM_TABLE_SUB_PARCEL = ParametersInstructionPG.TABLE_SUB_PARCEL;
  public final static String NOM_TABLE_WALL_SURFACE = ParametersInstructionPG.TABLE_WALL_SURFACE;
  public final static String NOM_TABLE_ZONE_URBA = ParametersInstructionPG.TABLE_ZONE_URBA;

  public final static String NOM_MNT = "MNT_BD3D.asc";

  public Environnement getEnvironnement(String folder) throws Exception {
    return LoaderPostGISTest.load(folder);
  }

  public static Environnement load(String folder) throws Exception {
    return load(folder, new FileInputStream(folder + File.separator + NOM_MNT));
  }

  public static Environnement load(String folder, InputStream dtmStream)
      throws Exception {

    // Corrections on the names of the attributes
    CadastralParcelLoader.ATT_ID_PARC = ParametersInstructionPG.ATT_CAD_PARCEL_NUM;

    Environnement env = Environnement.getInstance();
    env.folder = folder;

    // We get back the information on the PLU
    IFeatureCollection<IFeature> pluColl = PostgisManager
        .loadNonGeometricTable(host, port, database, user, pw,
            NOM_TABLE_DOC_URBA);
    IFeature featPLU = null;

    if (!pluColl.isEmpty()) {
      featPLU = pluColl.get(0);
    }

    // Then, we get back the information contained in the other layers necessary
    // for the creation of the environment : Zonage, Parcelles, Voirie...
    IFeatureCollection<IFeature> zoneColl = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_ZONE_URBA);

    IFeatureCollection<IFeature> parcelleColl = PostgisManager
        .loadGeometricTable(host, port, database, user, pw,
            NOM_TABLE_CADASTRAL_PARCEL);

    IFeatureCollection<IFeature> voirieColl = PostgisManager
        .loadGeometricTable(host, port, database, user, pw, NOM_TABLE_ROAD);

    // TODO : The table containing buildings has no geometry...
    IFeatureCollection<IFeature> batiColl = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_BUILDING);

    IFeatureCollection<IFeature> prescriptions = PostgisManager
        .loadGeometricTable(host, port, database, user, pw,
            NOM_TABLE_SPECIFIC_CBOUNDARY);

    // PostGis does not manage correctly the DTM files
    // Thus we use a raster file rather than a DTM contained in PostGIS
    DTMArea dtm = new DTMArea(dtmStream, "Terrain", true, 1,
        ColorShade.BLUE_CYAN_GREEN_YELLOW_WHITE);

    return LoadFromCollectionPostGIS.load(folder, featPLU, zoneColl,
        parcelleColl, voirieColl, batiColl, prescriptions, folder, dtm);

  }
}
