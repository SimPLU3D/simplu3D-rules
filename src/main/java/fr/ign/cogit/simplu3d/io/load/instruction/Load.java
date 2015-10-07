package fr.ign.cogit.simplu3d.io.load.instruction;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;

public class Load {
  
  

  public static void main(String[] args) throws Exception{
    
    
    PostgisManager.SRID = "2154";
    
    
    
    String host = "localhost";
    String user =  "postgres";
    String pw = "postgres";
    String database = "test";
    String folder =  "D:/0_Masson/1_CDD_SIMPLU/2_Travail/0_Workspace/simplu3d/simplu3D-rules/src/main/resources/fr/ign/cogit/simplu3d/data/";
    String port = "5432";
    
    load(host, port, user, pw, database, folder);
    
  }
  
  
  
  public static boolean load(String host, String port, String user, String pw, String database,  String folder) throws Exception{
    
    //Chargement de l'environnement
    Environnement env = LoaderSHP.load(folder);
    
    IFeatureCollection<UrbaZone> featCUrbzone = env.getUrbaZones();
    
    for(UrbaZone u : featCUrbzone){
      
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_NOM, u.getName(), "String");
        

        
        System.out.println(u.getGeom().getClass());
        
        
        System.out.println("Att" + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_NOM));

    }
    
  // for(int i = 0 ; i < featCUrbzone.size();i ++){
    // AttributeManager.addAttribute(featCUrbzone.get(i), "Test", 3283, "Integer");
    // }
    
    
    
    
    
    
 //PostgisManager.saveGeometricTable(host, port, database, user, pw, "testzoneurba", env.getUrbaZones(), true);
    
    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_ZONE_URBA_GEOM;
   PostgisManager.insertInGeometricTable(host, port, database, user, pw, ParametersInstructionPG.TABLE_ZONE_URBA , env.getUrbaZones());
    
    
    return true;
  }
  
  
  
}
