package fr.ign.cogit.simplu3d.demo.structDatabase;

import fr.ign.cogit.simplu3d.io.structDatabase.postgis.storer.BasicStorer;

public class StorerPostGISDemo {
	

	public static String host = "localhost";
	public static String user = "postgres";
	public static String pw = "postgres";
	// public static String database = "test_simplu3d";
	// public static String database = "strasbourg_simplu";
	public static String database = "test";
	public static String folder = "D:/0_Masson/1_CDD_SIMPLU/2_Travail/0_Workspace/simplu3d/simplu3D-rules/src/main/resources/fr/ign/cogit/simplu3d/data/";
	public static String port = "5432";
	
	
	public static void main(String[] args) throws Exception{
		
		BasicStorer.loadAll( host,  port,  user,  pw,  database,  folder);
		
	}

}
