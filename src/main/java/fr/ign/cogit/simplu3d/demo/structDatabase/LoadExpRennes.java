package fr.ign.cogit.simplu3d.demo.structDatabase;

import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.simplu3d.importer.AssignBuildingPartToSubParcel;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.io.structDatabase.postgis.storer.BasicStorer;
import fr.ign.cogit.simplu3d.reader.RoadReader;
import fr.ign.cogit.simplu3d.reader.UrbaZoneReader;

public class LoadExpRennes {

	/**
	 * Permet le chargement des données de RM dans la base de données
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		PostgisManager.SRID = "2154";

		String host = "localhost";
		String user = "postgres";
		String pw = "postgres";
		String database = "simplu3d-rennes";
		String folder = "/home/mickael/data/mbrasebin/donnees/dataRennes/";
		String port = "5432";

		CadastralParcelLoader.TYPE_ANNOTATION = 1;

		// UrbaZoneReader.ATT_TYPE_ZONE = "TYPE";

		AssignBuildingPartToSubParcel.RATIO_MIN = 0.8;
		AssignBuildingPartToSubParcel.ASSIGN_METHOD = 0;

		BasicStorer.loadAll(host, port, user, pw, database, folder);
	}

}
