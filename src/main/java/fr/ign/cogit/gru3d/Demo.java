package fr.ign.cogit.gru3d;

import fr.ign.cogit.gru3d.regleUrba.Executor;
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
public class Demo {

  /**
   * @param args
   * @throws Exception 
   */
  public static void main(String[] args) throws Exception {

    Executor.DATA_REPOSITORY = Demo.class.getClassLoader()
        .getResource("fr/ign/cogit/gtru/data/").getPath();
    
    Executor.main(null);
  }
  

}
