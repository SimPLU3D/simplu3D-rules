/**
 * 
 * This software is released under the licence CeCILL
 * <p>
 * see LICENSE.TXT
 * <p>
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

import java.net.URL;
import java.util.logging.Logger;

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
	private static final Logger logger = Logger.getLogger(DemoEnvironmentProvider.class.getName());

	private static Environnement defaultEnvironment = null;

	public static Environnement getDefaultEnvironment() {
		if (defaultEnvironment == null) {
			URL url = DemoEnvironmentProvider.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/");
			try {
				assert url != null;
				defaultEnvironment = LoaderSHP.load(
						url,
						DemoEnvironmentProvider.class.getResourceAsStream("/fr/ign/cogit/simplu3d/data/" + LoaderSHP.NOM_FICHIER_TERRAIN));
			} catch (Exception e) {
				logger.severe(e.getMessage());
			}
		}
		return defaultEnvironment;
	}
}
