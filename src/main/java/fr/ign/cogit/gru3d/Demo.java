package fr.ign.cogit.gru3d;

import fr.ign.cogit.gru3d.regleUrba.Executor;

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
