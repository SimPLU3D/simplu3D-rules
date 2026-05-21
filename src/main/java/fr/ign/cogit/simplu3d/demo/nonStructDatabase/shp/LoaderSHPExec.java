package fr.ign.cogit.simplu3d.demo.nonStructDatabase.shp;

import fr.ign.cogit.simplu3d.io.export.ExportInstance;

import java.net.URL;

/**
 *
 * This software is released under the licence CeCILL
 * <p>
 * see LICENSE.TXT
 * <p>
 * see <a href="http://www.cecill.info/">...</a>
 * <p>
 *
 *
 * copyright IGN
 *
 * @author Brasebin Mickaël
 *
 * @version 1.0
 **/
public class LoaderSHPExec {
	public static void main(String[] args) throws Exception {
		URL folder = LoaderSHPExec.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/");
		String folderOut = "/tmp/simplu3d-rules/";
		ExportInstance.export(folder, folderOut);
	}
}
