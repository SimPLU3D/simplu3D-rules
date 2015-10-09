package fr.ign.cogit.simplu3d.io.load.instruction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.io.load.application.ParemetersApplication;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;

public class Load {
  
  
  public static void main(String[] args) throws Exception{
      
    PostgisManager.SRID = "2154";
    
    String host = "localhost";
    String user =  "postgres";
    String pw = "postgres";
    String database = "test_simplu3d";
    String folder =  "D:/0_Masson/1_CDD_SIMPLU/2_Travail/0_Workspace/simplu3d/simplu3D-rules/src/main/resources/fr/ign/cogit/simplu3d/data/";
    String port = "5432";
    
    loadZoneUrba(host, port, user, pw, database, folder);
    loadRoad(host, port, user, pw, database, folder);
    loadAxis(host, port, user, pw, database, folder);
    //loadParcel(host, port, user, pw, database, folder);
    
  }
  
  // Chargement des informations sur les zones urba dans PostGis
  public static boolean loadZoneUrba(String host, String port, String user, String pw, String database,  String folder) throws Exception{
    
    // Chargement de l'environnement
    Environnement env = LoaderSHP.load(folder);
    
    // Chargement des données de la Zone Urba
    IFeatureCollection<UrbaZone> featCUrbzone = env.getUrbaZones();
    
    // Boucle sur le contenu de la Zone Urba
    for(UrbaZone u : featCUrbzone){
      
        // Ajout du libelle de la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_LIBELLE, u.getLibelle(), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Zone urba, attribut libelle : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_LIBELLE));
        
        // Ajout du libelle long de la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_LIBELONG, u.getLibelong(), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Zone urba, attribut libelong : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_LIBELONG));
        
        // Ajout du type de la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_TYPEZONE, u.getTypeZone(), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Zone urba, attribut type zone : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_TYPEZONE));
        
        // Ajout de la vocation (destdomi) de la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_DESTDOMI, u.getDestdomi(), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Zone urba, attribut vocation (destdomi) : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_DESTDOMI));
        
        // Ajout du nom du fichier relatif à la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_NOMFIC, u.getNomFic(), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Zone urba, attribut nom fichier : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_NOMFIC));
        
        // Ajout de l'url du fichier relatif à la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_URLFIC, u.getUrlFic(), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Zone urba, attribut URL fichier : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_URLFIC));
        
        // Ajout du code INSEE de la commune de la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_INSEE, u.getInsee(), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Zone urba, attribut code INSEE : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_INSEE));
        
        // Pour le format de la date
        SimpleDateFormat sdf = new SimpleDateFormat(ParemetersApplication.DATE_FORMAT);
 
        // Ajout de la date de départ de la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_DATE_APPRO , sdf.format(u.getDateDeb()), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Zone urba, attribut date départ : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_DATE_APPRO));
        
        // Ajout de la date de fin de la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_DATE_VALID ,  sdf.format(u.getDateFin()), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Zone urba, attribut date fin : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_DATE_VALID));
        
        // Ajout du commentaire de la Zone Urba
        AttributeManager.addAttribute(u, ParametersInstructionPG.ATT_ZONE_URBA_COMMENTAIRE , u.getText(), "String");
        System.out.println(u.getGeom().getClass());
        System.out.println("Zone urba, attribut commentaire : " + u.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_COMMENTAIRE));

    }
    
    // for(int i = 0 ; i < featCUrbzone.size();i ++){
    // AttributeManager.addAttribute(featCUrbzone.get(i), "Test", 3283, "Integer");
    // }
    
    //PostgisManager.saveGeometricTable(host, port, database, user, pw, "testzoneurba", env.getUrbaZones(), true);
    
    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_ZONE_URBA_GEOM;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw, ParametersInstructionPG.TABLE_ZONE_URBA , env.getUrbaZones());
    
    
    return true;
  }
  
  
  // Chargement des informations sur les routes dans PostGis
  public static boolean loadRoad(String host, String port, String user, String pw, String database,  String folder) throws Exception{
    
    // Chargement de l'environnement
    Environnement env = LoaderSHP.load(folder);
    
    // Chargement des données des routes
    IFeatureCollection<Road> featCRoad = env.getRoads();
    
    // Création d'une feature pour la géomtérie des axes
    IFeatureCollection<IFeature> featCRoadAxis = new FT_FeatureCollection <IFeature>();
    
    // Boucle sur le contenu des routes
    for(Road r : featCRoad){
      
      // Ajout du nom de la route
      AttributeManager.addAttribute(r, ParametersInstructionPG.ATT_ROAD_NOM, r.getName(), "String");
      System.out.println(r.getGeom().getClass());
      System.out.println("Road, attribut nom : " + r.getAttribute(ParametersInstructionPG.ATT_ROAD_NOM));
      
      // Ajout du type de la route
      AttributeManager.addAttribute(r, ParametersInstructionPG.ATT_ROAD_TYPE, r.getUsage(), "String");
      System.out.println(r.getGeom().getClass());
      System.out.println("Road, attribut usage : " + r.getAttribute(ParametersInstructionPG.ATT_ROAD_TYPE));
      
      // Ajout de la largeur de la route
      AttributeManager.addAttribute(r, ParametersInstructionPG.ATT_ROAD_LARGEUR, r.getWidth(), "Double");
      System.out.println(r.getGeom().getClass());
      System.out.println("Road, attribut largeur : " + r.getAttribute(ParametersInstructionPG.ATT_ROAD_LARGEUR));
      
    }
    
    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_ROAD_GEOM_SURF;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw, ParametersInstructionPG.TABLE_ROAD , env.getRoads());
    
    return true;
  }
  
  
  // Chargement de l'axe des routes
  public static boolean loadAxis(String host, String port, String user, String pw, String database,  String folder) throws Exception{
    
    // Chargement de l'environnement
    Environnement env = LoaderSHP.load(folder);
    
    // Chargement des données des routes
    IFeatureCollection<Road> featCRoad = env.getRoads();
    
    // Création d'une feature pour la géométrie des axes
    IFeatureCollection<IFeature> featCRoadAxis = new FT_FeatureCollection <IFeature>();
    
    // Boucle sur le contenu des routes
    for(Road ra : featCRoad){
    
      // Pour extraire la géométrie de l'axe
      IGeometry geomAxe = ((IGeometry) ra.getAxis().clone());
      IFeature featAxis = new DefaultFeature(geomAxe);
      System.out.println(featAxis.getGeom());
      System.out.println(featCRoadAxis.getFlagGeom());
      
     // AttributeManager.addAttribute(featAxis, "axe_geom", geomAxe ,"Integer");
      featCRoadAxis.add(featAxis.cloneGeom());
      System.out.println(featCRoadAxis.getFlagGeom());
    
    }
    
    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_AXE_GEOM;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw, ParametersInstructionPG.TABLE_AXE , featCRoadAxis);
    
    return true;
  }
  
  
  //Chargement des parcelles cadastrales
  public static boolean loadParcel(String host, String port, String user, String pw, String database,  String folder) throws Exception{
   
    // Chargement de l'environnement
    Environnement env = LoaderSHP.load(folder);
   
    // Chargement des données des parcelles
    IFeatureCollection<CadastralParcel> featCCadPar = env.getCadastralParcels();
   
    // Boucle sur le contenu des parcelles
    for(CadastralParcel cp : featCCadPar){
      
      // Ajout du numéro de parcelle
      AttributeManager.addAttribute(cp, ParametersInstructionPG.ATT_CAD_PARCEL_NUM, cp.getId(), "Integer");
      System.out.println(cp.getGeom().getClass());
      System.out.println("Parcelle, attribut ID : " + cp.getAttribute(ParametersInstructionPG.ATT_CAD_PARCEL_NUM));
      
      // Ajout de la surface de la parcelle
      AttributeManager.addAttribute(cp, ParametersInstructionPG.ATT_CAD_PARCEL_SURF, cp.getArea() , "Double");
      System.out.println(cp.getGeom().getClass());
      System.out.println("Parcelle, attribut surface : " + cp.getAttribute(ParametersInstructionPG.ATT_CAD_PARCEL_SURF));
   
    }
   
    PostgisManager.NAME_COLUMN_GEOM = ParametersInstructionPG.ATT_CAD_PARCEL_GEOM;
    PostgisManager.insertInGeometricTable(host, port, database, user, pw, ParametersInstructionPG.TABLE_CADASTRAL_PARCEL , env.getCadastralParcels());
   
    return true;
  }
  
  
}
