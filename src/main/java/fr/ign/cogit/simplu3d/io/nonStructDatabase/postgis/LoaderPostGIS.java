package fr.ign.cogit.simplu3d.io.nonStructDatabase.postgis;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.simplu3d.io.LoadFromCollection;
import fr.ign.cogit.simplu3d.model.Environnement;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class LoaderPostGIS {

	// Default table as input of the integration process
	public final static String NOM_TABLE_PLU = "plu";
	public final static String NOM_TABLE_ZONAGE = "zonage";
	public final static String NOM_TABLE_PARCELLE = "parcelle";
	public final static String NOM_TABLE_VOIRIE = "route";
	public final static String NOM_TABLE_BATIMENTS = "batiment";
	public final static String NOM_TABLE_PRESC_LINEAIRE = "prescriptionlin";
	public final static String NOM_TABLE_PRESC_SURF = "prescriptionsurf";
	public final static String NOM_TABLE_PRESC_PCT = "prescriptionpct";

	public final static String NOM_TABLE_TERRAIN = "mnt";

	String host = "";
	String port = "";
	String database = "";
	String user = "";
	String pw = "";
	String schema = "";

	public LoaderPostGIS(String host, String port, String database, String user, String pw) {
		this(host, port, database, "public", user, pw);
	}

	public LoaderPostGIS(String host, String port, String database, String schema, String user, String pw) {
		super();
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.pw = pw;
		this.schema = schema;
	}

	public Environnement load() throws Exception {
	
		IFeatureCollection<IFeature> pluColl = loadGeometricTableOrEmptyCollection(NOM_TABLE_PLU);
		IFeature featPLU = null;
		if (!pluColl.isEmpty()) {
			featPLU = pluColl.get(0);
		}

		IFeatureCollection<IFeature> zoneColl = loadGeometricTableOrEmptyCollection(NOM_TABLE_ZONAGE);
		IFeatureCollection<IFeature> parcelleColl = loadGeometricTableOrEmptyCollection(NOM_TABLE_PARCELLE);
		IFeatureCollection<IFeature> voirieColl = loadGeometricTableOrEmptyCollection(NOM_TABLE_VOIRIE);
		IFeatureCollection<IFeature> batiColl =loadGeometricTableOrEmptyCollection(	NOM_TABLE_BATIMENTS);
		IFeatureCollection<IFeature> prescriptions = loadGeometricTableOrEmptyCollection(NOM_TABLE_PRESC_LINEAIRE);
		prescriptions.addAll(loadGeometricTableOrEmptyCollection(NOM_TABLE_PRESC_PCT));
		prescriptions.addAll(loadGeometricTableOrEmptyCollection(NOM_TABLE_PRESC_SURF));
		
		DTMPostGISNoJava3D dtm = new DTMPostGISNoJava3D(host, port, database, schema, NOM_TABLE_TERRAIN, user, pw);
		
		
		//If the DTM is not well loaded, we set it to null for the next step and do not consider the DTP
		if(! dtm.isWellLoaded()) {
			dtm = null;
		}

		return LoadFromCollection.load(featPLU, zoneColl, parcelleColl, voirieColl, batiColl, prescriptions, 
				dtm);

	}

	private IFeatureCollection<IFeature> loadGeometricTableOrEmptyCollection(String tableName) throws Exception {
		IFeatureCollection<IFeature> featColl = PostgisManager.loadGeometricTable(host, port, database, schema,
				tableName, user, pw);
		if (featColl == null) {
			return new FT_FeatureCollection<>();
		}
		return featColl;
	}

}
