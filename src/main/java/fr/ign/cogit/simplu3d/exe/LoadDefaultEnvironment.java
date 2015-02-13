package fr.ign.cogit.simplu3d.exe;

import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Environnement;
/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class LoadDefaultEnvironment {

  private static Environnement ENV_SINGLETON = null;

  public static Environnement getENVDEF() {
    if (ENV_SINGLETON == null) {
      String folder = LoadDefaultEnvironment.class.getClassLoader()
          .getResource("fr/ign/cogit/simplu3d/data/").getPath();

      try {
        ENV_SINGLETON = LoaderSHP.load(folder);
      } catch (CloneNotSupportedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return ENV_SINGLETON;

  }
}
