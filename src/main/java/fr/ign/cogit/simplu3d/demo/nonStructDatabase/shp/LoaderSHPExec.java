package fr.ign.cogit.simplu3d.demo.nonStructDatabase.shp;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.simplu3d.io.export.ExportInstance;

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
public class LoaderSHPExec {
	public static IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

	public static void main(String[] args) throws Exception {

		String folder = "/home/mbrasebin/Documents/Donnees/ArtiScales/ArtiScalesTest/Donnees/";

		String folderOut = "/tmp/tmp/";
		
		ExportInstance.export(folder, folderOut);

		System.out.println("It is finished");

	}

}
