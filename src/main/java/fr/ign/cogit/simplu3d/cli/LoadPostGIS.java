package fr.ign.cogit.simplu3d.cli;

import java.io.File;

import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.io.structDatabase.postgis.storer.BasicStorer;
import fr.ign.cogit.simplu3d.model.Environnement;

public class LoadPostGIS {
	
	public static void main(String[] args){
		if ( args.length < 1 ){
			System.err.println("loadpostgis <folder>");
			System.exit(1);
		}
		File folder = new File( args[0] ) ;
		if ( ! folder.exists() ){
			System.err.println("folder doesn't exists : "+folder);
			System.exit(1);
		}
		
		String host = "localhost";
		String port = "5432";
		String user = "postgres";
		String pw = "postgres";
		String database = "simplu";
		
		try {
			Environnement env = LoaderSHP.loadNoDTM(folder);
			BasicStorer.loadPlu(host, port, user, pw, database, env);
		} catch (Exception e) {
			e.printStackTrace();	
		}		
	}

}
