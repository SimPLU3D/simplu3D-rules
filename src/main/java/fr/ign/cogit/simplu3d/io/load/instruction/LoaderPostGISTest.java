package fr.ign.cogit.simplu3d.io.load.instruction;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
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

	public Environnement getEnvironnement(String folder, Integer idVersion) throws Exception {
		return LoaderPostGISTest.load(folder, idVersion);
	}

	public static Environnement load(String folder, Integer idVersion) throws Exception {
		return load(idVersion);
	}

	public static Environnement load(Integer idVersion) throws Exception {

		// We get back the information on the PLU
		IFeatureCollection<IFeature> pluColl = PostgisManager.loadNonGeometricTable(host, port, database, user, pw,
				NOM_TABLE_DOC_URBA);

		IFeatureCollection<IFeature> zoneColl = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_ZONE_URBA);

		IFeatureCollection<IFeature> parcelleColl = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_CADASTRAL_PARCEL);

		IFeatureCollection<IFeature> voirieColl = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_ROAD);

		IFeatureCollection<IFeature> batiColl = PostgisManager.loadNonGeometricTable(host, port, database, user, pw,
				NOM_TABLE_BUILDING);

		IFeatureCollection<IFeature> prescriptions = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_SPECIFIC_CBOUNDARY);

		return LoadFromCollectionPostGIS.load(idVersion, pluColl, zoneColl, parcelleColl, voirieColl, batiColl,
				prescriptions);

	}
}
