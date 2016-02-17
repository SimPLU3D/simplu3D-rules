package fr.ign.cogit.simplu3d.io.load.instruction;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTMArea;
import fr.ign.cogit.geoxygene.sig3d.util.ColorShade;
import fr.ign.cogit.simplu3d.model.application.Environnement;

public class LoaderPostGISTest {

  // Information about the PostGIS database
  public static String host = Load.host;
  public static String user = Load.user;
  public static String pw = Load.pw;
  public static String database = Load.database;
  public static String port = Load.port;

  // List of the names of tables in PostGIS
  public final static String NOM_TABLE_BUILDING = ParametersInstructionPG.TABLE_BUILDING;
  public final static String NOM_TABLE_CADASTRAL_PARCEL = ParametersInstructionPG.TABLE_CADASTRAL_PARCEL;
  public final static String NOM_TABLE_DOC_URBA = ParametersInstructionPG.TABLE_DOC_URBA;
  public final static String NOM_TABLE_ROAD = ParametersInstructionPG.TABLE_ROAD;
  public final static String NOM_TABLE_SPECIFIC_CBOUNDARY = ParametersInstructionPG.TABLE_SPECIFIC_CBOUNDARY;
  public final static String NOM_TABLE_ZONE_URBA = ParametersInstructionPG.TABLE_ZONE_URBA;

  public static String NOM_MNT = "MNT_BD3D.asc";

  public Environnement getEnvironnement(String folder, Integer idVersion) throws Exception {
    return LoaderPostGISTest.load(folder, idVersion);
  }

  public static Environnement load(String folder, Integer idVersion) throws Exception {
    return load(folder, idVersion, new FileInputStream(folder + File.separator + NOM_MNT));
  }

  public static Environnement load(String folder, Integer idVersion, InputStream dtmStream)
      throws Exception {

    Environnement env = Environnement.getInstance();
    env.folder = folder;

    // We get back the information on the PLU
    IFeatureCollection<IFeature> pluColl = PostgisManager
        .loadNonGeometricTable(host, port, database, user, pw,
            NOM_TABLE_DOC_URBA);

    IFeatureCollection<IFeature> zoneColl = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_ZONE_URBA);

    IFeatureCollection<IFeature> parcelleColl = PostgisManager
        .loadGeometricTable(host, port, database, user, pw,
            NOM_TABLE_CADASTRAL_PARCEL);

    IFeatureCollection<IFeature> voirieColl = PostgisManager
        .loadGeometricTable(host, port, database, user, pw, NOM_TABLE_ROAD);

    IFeatureCollection<IFeature> batiColl = PostgisManager
        .loadNonGeometricTable(host, port, database, user, pw,
            NOM_TABLE_BUILDING);

    IFeatureCollection<IFeature> prescriptions = PostgisManager
        .loadGeometricTable(host, port, database, user, pw,
            NOM_TABLE_SPECIFIC_CBOUNDARY);

    // PostGis does not manage correctly the DTM files
    // Thus we use a raster file rather than a DTM contained in PostGIS
    DTMArea dtm = new DTMArea(dtmStream, "Terrain", true, 1,
        ColorShade.BLUE_CYAN_GREEN_YELLOW_WHITE);

    return LoadFromCollectionPostGIS.load(folder, idVersion, pluColl, zoneColl,
        parcelleColl, voirieColl, batiColl, prescriptions, dtm);

  }
}
