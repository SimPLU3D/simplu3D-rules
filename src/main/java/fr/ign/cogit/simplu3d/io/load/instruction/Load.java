package fr.ign.cogit.simplu3d.io.load.instruction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.io.load.application.ParemetersApplication;
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
    
    // Chargement de l'environnement
    Environnement env = LoaderSHP.load(folder);
    
    // Chargement des données de la Zone Urba
    IFeatureCollection<UrbaZone> featCUrbzone = env.getUrbaZones();
    
    // Boucle sur le contenu de la Zone Urba
    for(UrbaZone u : featCUrbzone){
      
        // Ajout du nom de la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_NOM, u.getName(), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Attribut nom : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_NOM));
        
        // Ajout du commentaire de la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_COMMENTAIRE , u.getText(), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Attribut commentaire : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_COMMENTAIRE));
        
        SimpleDateFormat sdf = new SimpleDateFormat(ParemetersApplication.DATE_FORMAT);
 
        
        // Ajout de la date de départ de la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_DATE_DEBUT , sdf.format(u.getDateDeb()), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Attribut date départ : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_DATE_DEBUT));
        
        // Ajout de la date de fin de la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_DATE_FIN ,  sdf.format(u.getDateFin()), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Attribut date fin : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_DATE_FIN));

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
