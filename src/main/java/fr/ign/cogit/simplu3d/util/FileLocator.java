package fr.ign.cogit.simplu3d.util;

import java.io.File;

/**
 * 
 * Case insensitive file finder
 * 
 * @author MBorne
 *
 */
public class FileLocator {

	public static File findFile(File folder, String filename){
		for ( File file : folder.listFiles() ){
			if ( file.getName().matches("(?i)"+filename)){
				return file;
			}
		}
		return null;
	}
	
}
