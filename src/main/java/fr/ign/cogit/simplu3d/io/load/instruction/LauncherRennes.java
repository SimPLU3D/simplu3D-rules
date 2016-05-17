package fr.ign.cogit.simplu3d.io.load.instruction;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object1d;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object2d;
import fr.ign.cogit.geoxygene.sig3d.semantic.Map3D;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.sig3d.util.ColorLocalRandom;
import fr.ign.cogit.simplu3d.io.load.instruction.checker.Checker;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;

public class LauncherRennes {



  public static int idUtilisateur = -1;

  public static Map3D carte;

  public static void main(String[] args) throws Exception {

    // On corrige le nom de la base de données et du MNT pour la zone de Rennes
    Load.database = "test_simplu3d";
    Load.host = "172.16.0.87";


    // On construit la fenêtre principale
    MainWindow fenPrincipale = new MainWindow();
    carte = fenPrincipale.getInterfaceMap3D().getCurrent3DMap();

    // On affiche les données par défaut
    afficheMap(-1);

    // On ajoute le menu de sélection des versions
    fenPrincipale.getMainMenuBar().add(
        (new LauncherRennes()).generateCombobox());

    // On ajoute le button de vérification des règles
    fenPrincipale.getMainMenuBar().add((new LauncherRennes()).generateButton());

    // On actualise la fenêtre
    fenPrincipale.setVisible(true);

  }

  /**
   * Affiche au sein du viewer 3D les données en fonction d'un numéro de version
   * 
   * @param idVersion l'identifiant de la version à charger
   * @throws Exception
   */

  public static void afficheMap(int idVersion) throws Exception {

    // On supprime les couches
//  carte.removeLayer("Parcelles");
  carte.removeLayer("Toit");
//    carte.removeLayer("Route");
    carte.removeLayer("Murs");
    carte.removeLayer("Pignon");
    carte.removeLayer("Faitage");
    carte.removeLayer("Gouttière");

    // On charge l'environnement depuis la base de données
    Environnement env = LoaderPostGISTest.load(null, idVersion);

    // On récupère les données qui nous intéressent dans l'environnement
    IFeatureCollection<AbstractBuilding> buildingColl = env.getBuildings();
    IFeatureCollection<Road> roadColl = env.getRoads();
    IFeatureCollection<CadastralParcel> parcelColl = env.getCadastralParcels();

    // On initialise des IFC pour les murs et les toits
    IFeatureCollection<SpecificWallSurface> wallColl = new FT_FeatureCollection<SpecificWallSurface>();
    IFeatureCollection<RoofSurface> roofColl = new FT_FeatureCollection<RoofSurface>();

    // On complète les IFC murs et toits en faisant une boucle sur les BP
    for (AbstractBuilding currentBP : buildingColl) {
      wallColl.add(currentBP.getWall());
      
      RoofSurface featRoof = currentBP.getRoof();
     // System.out.println(currentBP.getIdVersion() );
      if(currentBP.getIdVersion() == -1){
    	  
    	  
    	   featRoof.setRepresentation(new Object2d(featRoof, Color.red));
      }else{
    	  
    	  featRoof.setRepresentation(new Object2d(featRoof, new Color( 	237,145,33)));
      }
      
      roofColl.add(featRoof);
    }

    IFeatureCollection<IFeature> featGutter = new FT_FeatureCollection<IFeature>();
    IFeatureCollection<IFeature> featGable = new FT_FeatureCollection<IFeature>();
    IFeatureCollection<IFeature> featRoofing = new FT_FeatureCollection<IFeature>();

    // On défini les représentations et on récupère les données sur les
    // goutières, les faitages et les pignons
    for (RoofSurface featRoof : roofColl) {
   

      if (featRoof.getGutter() != null && !featRoof.getGutter().isEmpty()) {
        featGutter.add(new DefaultFeature(featRoof.getGutter()));
      }

      if (featRoof.getRoofing() != null && !featRoof.getRoofing().isEmpty()) {
        featRoofing.add(new DefaultFeature(featRoof.getRoofing()));
      }

      if (featRoof.getGable() != null && !featRoof.getGable().isEmpty()) {
        featGable.add(new DefaultFeature(featRoof.getGable()));
      }

    }

    for (IFeature featWall : wallColl) {
      featWall.setRepresentation(new Object2d(featWall, Color.lightGray));
    }

    for (IFeature featParcel : parcelColl) {
      Color c = ColorLocalRandom.getRandomColor(new Color(10, 150, 10), 0, 50,
          0);
      featParcel.setRepresentation(new Object2d(featParcel, c));
    }

    VectorLayer roof = new VectorLayer(roofColl, "Toit");

    VectorLayer road = new VectorLayer(roadColl, "Route", true, Color.gray, 1,
        true);

    VectorLayer wall = new VectorLayer(wallColl, "Murs");

    VectorLayer parcel = new VectorLayer(parcelColl, "Parcelles");

    // On ajoute les couches à la carte
    carte.addLayer(roof);
    carte.addLayer(road);
    carte.addLayer(wall);
    carte.addLayer(parcel);

    Object1d.width = 4;

    if (!featGable.isEmpty()) {
      VectorLayer vectGutter = new VectorLayer(featGable, "Pignon", Color.blue);
      carte.addLayer(vectGutter);
    }

    if (!featRoofing.isEmpty()) {
      VectorLayer vectGutter = new VectorLayer(featRoofing, "Faitage",
          Color.white);
      carte.addLayer(vectGutter);
    }

    if (!featGutter.isEmpty()) {
      VectorLayer vectGutter = new VectorLayer(featGutter, "Gouttière",
          Color.yellow);
      carte.addLayer(vectGutter);
    }

  }

  /**
   * Génère un bouton pour la vérification des règles d'urbanisme sur la BPU
   * sélectionnée
   * 
   * @return
   */
  private JButton generateButton() {

    JButton jB = new JButton(
        "Vérifier les règles pour la/les parcelle(s) sélectionnée(s)");

    jB.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        IFeatureCollection<IFeature> featC = carte.getIMap3D().getSelection();
        IFeatureCollection<BasicPropertyUnit> bpuColl = new FT_FeatureCollection<BasicPropertyUnit>();

        for (IFeature feat : featC) {
          if (feat instanceof CadastralParcel) {
            CadastralParcel cad = (CadastralParcel) feat;
            BasicPropertyUnit bpu = cad.getbPU();
            bpuColl.add(bpu);
          }
        }

        try {
          Checker.checkSelection(bpuColl);
        } catch (Exception e1) {

          e1.printStackTrace();
        }

      }
    });

    return jB;

  }

  /**
   * Génère une liste déroulante permettant de sélectionner la version à charger
   * dans l'interface
   * 
   * @return
   * @throws Exception
   */
  private JComboBox<Version> generateCombobox() throws Exception {

    // On génére les éléments à partir de la requête
    Vector<Version> lVersion = new Vector<>();
    lVersion.add(new Version(-1, "Données par défaut"));

    // On récupère les numéros de version pour l'utilisateur donné
    List<Integer> listIdVersion = LoaderVersion
        .retrieveListIdVersionWithTableVersion(Load.host, Load.port,
            Load.database, Load.user, Load.pw, idUtilisateur);

    // On ajoute les versions trouvées à la liste
    for (int j = 0; j < listIdVersion.size(); j++) {
      int id = listIdVersion.get(j);
      String nom = "Version " + Integer.toString(id);
      lVersion.add(new Version(id, nom));
    }

    // On construit le menu déroulant
    JComboBox<Version> comb = new JComboBox<>(lVersion);

    comb.setName("Menu_Version");
    comb.setSize(50, 20);
    comb.setSelectedIndex(0);
    comb.setVisible(true);
    comb.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        JComboBox<Version> cb = (JComboBox<Version>) e.getSource();
        Version selectedLine = (Version) cb.getSelectedItem();

        System.out.println("Version n° " + selectedLine.getID());
        System.out.println("Version nom " + selectedLine.getNom());

        int index = cb.getSelectedIndex();

        System.out.println("Selected index " + index);

        // On lance le chargement
        try {
          afficheMap(selectedLine.getID());
        } catch (Exception e1) {

          e1.printStackTrace();
        }

      }
    });
    return comb;

  }

  public class Version {

    private int id;
    private String nom;

    public Version(int id, String nom) {
      this.id = id;
      this.nom = nom;
    }

    public int getID() {
      return id;
    }

    public String getNom() {
      return nom;
    }

    public String toString() {
      return this.getNom();
    }

  }

}
