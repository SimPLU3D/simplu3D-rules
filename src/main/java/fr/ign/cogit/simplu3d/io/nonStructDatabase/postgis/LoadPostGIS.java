package fr.ign.cogit.simplu3d.io.nonStructDatabase.postgis;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.simplu3d.io.LoadFromCollection;
import fr.ign.cogit.simplu3d.model.Environnement;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see  http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class LoadPostGIS {

	/*
	 * Nom des fichiers en entrée
	 */
	public final static String NOM_TABLE_ZONAGE = "zonage";
	public final static String NOM_TABLE_PARCELLE = "parcelle";
	public final static String NOM_TABLE_VOIRIE = "route";
	public final static String NOM_TABLE_BATIMENTS = "bati";
	public final static String NOM_TABLE_PRESC_LINEAIRE = "prescription_lin";
	public final static String NOM_TABLE_PLU = "PLU";

	public final static String NOM_TABLE_TERRAIN = "mnt";

	String host = "";
	String port = "";
	String database = "";
	String user = "";
	String pw = "";
	String schema = "";

	public LoadPostGIS(String host, String port, String database, String user, String pw) {
		this(host, port, database, "public", user, pw);
	}

	public LoadPostGIS(String host, String port, String database, String schema, String user, String pw) {
		super();
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.pw = pw;
		this.schema = schema;
	}

	public Environnement loadNoOCLRules() throws Exception {
		return load(null);
	}

	public Environnement load(String folder) throws Exception {
		Environnement env = Environnement.createEnvironnement();
	

		IFeatureCollection<IFeature> pluColl = PostgisManager.loadGeometricTable(host, port, database, schema,
				NOM_TABLE_ZONAGE, user, pw);
		IFeature featPLU = null;
		if (!pluColl.isEmpty()) {
			featPLU = pluColl.get(0);
		}

		IFeatureCollection<IFeature> zoneColl = PostgisManager.loadGeometricTable(host, port, database, schema,
				NOM_TABLE_ZONAGE, user, pw);
		IFeatureCollection<IFeature> parcelleColl = PostgisManager.loadGeometricTable(host, port, database, schema,
				NOM_TABLE_PARCELLE, user, pw);
		IFeatureCollection<IFeature> voirieColl = PostgisManager.loadGeometricTable(host, port, database, schema,
				NOM_TABLE_VOIRIE, user, pw);
		IFeatureCollection<IFeature> batiColl = PostgisManager.loadGeometricTable(host, port, database, schema,
				NOM_TABLE_BATIMENTS, user, pw);
		IFeatureCollection<IFeature> prescriptions = PostgisManager.loadGeometricTable(host, port, database, schema,
				NOM_TABLE_PRESC_LINEAIRE, user, pw);

		DTMPostGISNoJava3D dtm = new DTMPostGISNoJava3D(host, port, database, schema, NOM_TABLE_TERRAIN, user, pw);

		return LoadFromCollection.load(featPLU, zoneColl, parcelleColl, voirieColl, batiColl, prescriptions, folder,
				dtm);

	}
}
