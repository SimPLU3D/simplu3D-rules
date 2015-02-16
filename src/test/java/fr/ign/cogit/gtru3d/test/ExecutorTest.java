package fr.ign.cogit.gtru3d.test;

import junit.framework.Assert;

import org.junit.Test;

import fr.ign.cogit.gru3d.regleUrba.Executor;



public class ExecutorTest {

  /**
   * @param args
   */
  public static void main(String[] args) {

    (new ExecutorTest()).testLoader();
  }
  
  
  @Test
  public void testLoader(){
    
    
    fr.ign.cogit.gru3d.regleUrba.Executor.DATA_REPOSITORY = ExecutorTest.class.getClassLoader()
        .getResource("fr/ign/cogit/gtru/data/").getPath();
    
    try {
      Executor.main(null);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Assert.assertTrue(true);
  }

  

}
