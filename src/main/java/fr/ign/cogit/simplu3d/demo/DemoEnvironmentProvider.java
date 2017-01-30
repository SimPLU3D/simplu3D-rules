/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
package fr.ign.cogit.simplu3d.demo;

import java.io.File;

import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.Environnement;

/**
 * 
 * Helper that provides demo environments
 * 
 * @author MBrasebin
 *
 */
public class DemoEnvironmentProvider {

	private static Environnement defaultEnvironment = null;

	public static Environnement getDefaultEnvironment() {
		if (defaultEnvironment == null) {
			String folder = DemoEnvironmentProvider.class.getClassLoader()
					.getResource("fr/ign/cogit/simplu3d/data/")
					.getPath();

			try {
				defaultEnvironment = LoaderSHP.load(new File(folder), DemoEnvironmentProvider.class
						.getResourceAsStream("/fr/ign/cogit/simplu3d/data/" + LoaderSHP.NOM_FICHIER_TERRAIN));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defaultEnvironment;

	}
}
