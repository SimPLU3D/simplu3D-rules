package fr.ign.cogit.simplu3d.io.load.instruction;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.geoxygene.sig3d.semantic.AbstractDTM;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.BuildingPart;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.PLU;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;

public class LoadFromCollectionPostGIS {

  public static Environnement load(String folder,
      IFeatureCollection<IFeature> PLUColl,
      IFeatureCollection<IFeature> zoneColl,
      IFeatureCollection<IFeature> parcelleColl,
      IFeatureCollection<IFeature> voirieColl,
      IFeatureCollection<IFeature> batiColl,
      IFeatureCollection<IFeature> prescriptions, AbstractDTM dtm)
      throws Exception {

    Environnement env = Environnement.getInstance();
    env.folder = folder;

    return LoadFromCollectionPostGIS.load(folder, PLUColl, zoneColl,
        parcelleColl, voirieColl, batiColl, prescriptions, dtm, env);
  }

  public static Environnement load(String folder,
      IFeatureCollection<IFeature> PLUColl,
      IFeatureCollection<IFeature> zoneColl,
      IFeatureCollection<IFeature> parcelleColl,
      IFeatureCollection<IFeature> voirieColl,
      IFeatureCollection<IFeature> batiColl,
      IFeatureCollection<IFeature> prescriptions, AbstractDTM dtm,
      Environnement env) throws Exception {

    // Parameters of connection to the PostGIS database
    String host = Load.host;
    String user = Load.user;
    String pw = Load.pw;
    String database = Load.database;
    String port = Load.port;

    // Name of tables in PostGIS
    String NOM_TABLE_AXE = ParametersInstructionPG.TABLE_AXE;
    String NOM_TABLE_BASIC_PROPERTY_UNIT = ParametersInstructionPG.TABLE_BASIC_PROPERTY_UNIT;
    String NOM_TABLE_BUILDING_PART = ParametersInstructionPG.TABLE_BUILDING_PART;
    String NOM_TABLE_GABLE = ParametersInstructionPG.TABLE_GABLE;
    String NOM_TABLE_GUTTER = ParametersInstructionPG.TABLE_GUTTER;
    String NOM_TABLE_ROOF = ParametersInstructionPG.TABLE_ROOF;
    String NOM_TABLE_ROOFING = ParametersInstructionPG.TABLE_ROOFING;
    String NOM_TABLE_SPECIFIC_CBOUNDARY = ParametersInstructionPG.TABLE_SPECIFIC_CBOUNDARY;
    String NOM_TABLE_SUB_PARCEL = ParametersInstructionPG.TABLE_SUB_PARCEL;
    String NOM_TABLE_WALL_SURFACE = ParametersInstructionPG.TABLE_WALL_SURFACE;

    // Here some loaders
    IFeatureCollection<IFeature> subParcelsLoad = PostgisManager
        .loadGeometricTable(host, port, database, user, pw,
            NOM_TABLE_SUB_PARCEL);
    IFeatureCollection<IFeature> bPULoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_BASIC_PROPERTY_UNIT);
    IFeatureCollection<IFeature> buildingPartLoad = PostgisManager
        .loadGeometricTable(host, port, database, user, pw,
            NOM_TABLE_BUILDING_PART);
    IFeatureCollection<IFeature> scbLoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_SPECIFIC_CBOUNDARY);
    IFeatureCollection<IFeature> axisLoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_AXE);
    IFeatureCollection<IFeature> roofLoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_ROOF);
    IFeatureCollection<IFeature> wallLoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_WALL_SURFACE);
    IFeatureCollection<IFeature> roofingLoad = PostgisManager
        .loadGeometricTable(host, port, database, user, pw, NOM_TABLE_ROOFING);
    IFeatureCollection<IFeature> gableLoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_GABLE);
    IFeatureCollection<IFeature> gutterLoad = PostgisManager
        .loadGeometricTable(host, port, database, user, pw, NOM_TABLE_GUTTER);

    // Here some importers
    PLU pluImport = ImporterPostGIS.importPLU(PLUColl);
    IFeatureCollection<UrbaZone> zuImport = ImporterPostGIS
        .importZoneUrba(zoneColl);
    IFeatureCollection<SubParcel> subParImport = ImporterPostGIS
        .importSubParcel(subParcelsLoad);
    IFeatureCollection<CadastralParcel> cadParImport = ImporterPostGIS
        .importCadParcel(parcelleColl);
    IFeatureCollection<BasicPropertyUnit> bpuImport = ImporterPostGIS
        .importBasicPropUnit(bPULoad);
    IFeatureCollection<BuildingPart> bpImport = ImporterPostGIS
        .importBuildPart(buildingPartLoad);
    IFeatureCollection<SpecificCadastralBoundary> scbImport = ImporterPostGIS
        .importSpecificCadBound(scbLoad);
    IFeatureCollection<Road> roadImport = ImporterPostGIS
        .importRoad(voirieColl);
    IFeatureCollection<Road> axisImport = ImporterPostGIS.importAxis(axisLoad);
    IFeatureCollection<RoofSurface> roofImport = ImporterPostGIS
        .importRoof(roofLoad);
    IFeatureCollection<Building> buildImport = ImporterPostGIS
        .importBuilding(batiColl);
    IFeatureCollection<SpecificWallSurface> wallImport = ImporterPostGIS
        .importWall(wallLoad);
    IFeatureCollection<RoofSurface> roofingImport = ImporterPostGIS
        .importRoofing(roofingLoad);
    IFeatureCollection<RoofSurface> gableImport = ImporterPostGIS
        .importGable(gableLoad);
    IFeatureCollection<RoofSurface> gutterImport = ImporterPostGIS
        .importGutter(gutterLoad);

    return AutomaticAssignment.assignment(env, dtm, pluImport, zuImport,
        subParImport, scbImport, roadImport, axisImport, cadParImport,
        bpuImport, bpImport, buildImport, wallImport, roofImport,
        roofingImport, gutterImport, gableImport);

  }
}
