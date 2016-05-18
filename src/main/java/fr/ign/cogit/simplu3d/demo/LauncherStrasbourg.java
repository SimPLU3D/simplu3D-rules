package fr.ign.cogit.simplu3d.demo;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

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
import fr.ign.cogit.simplu3d.io.load.instruction.Load;
import fr.ign.cogit.simplu3d.io.load.instruction.LoaderPostGISTest;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;

public class LauncherStrasbourg {

  public static String folder = "D:/0_Masson/1_CDD_SIMPLU/2_Travail/0_Workspace/"
      + "simplu3d/simplu3D-rules/src/main/resources/fr/ign/cogit/simplu3d/data/";

  public static Map3D carte;

  public static void main(String[] args) throws Exception {

    MainWindow fenPrincipale = new MainWindow();
    carte = fenPrincipale.getInterfaceMap3D().getCurrent3DMap();

    afficheMap(-1);

    fenPrincipale.getMainMenuBar().add(
        (new LauncherStrasbourg()).generateCombobox());
    fenPrincipale.setVisible(true);
  }

  public static void afficheMap(int idVersion) throws Exception {

    // On supprime les couches
    carte.removeLayer("Parcelles");

    // On corrige le nom de la base de données
    Load.database = "strasbourg_simplu";

    // On charge l'environnement depuis la base de données

    Environnement env = LoaderPostGISTest.load(folder, idVersion);

    // On récupère les données qui nous intéressent dans l'environnement
    IFeatureCollection<AbstractBuilding> buildingColl = env.getBuildings();
    IFeatureCollection<Road> roadColl = env.getRoads();
    IFeatureCollection<CadastralParcel> parcelColl = env.getCadastralParcels();

    // On initialise des IFC pour les murs et les toits
    IFeatureCollection<SpecificWallSurface> wallColl = new FT_FeatureCollection<SpecificWallSurface>();
    IFeatureCollection<RoofSurface> roofColl = new FT_FeatureCollection<RoofSurface>();

    // On complète les IFC murs et toits en faisant une boucle sur les BP
    for (AbstractBuilding currentBP : buildingColl) {
      wallColl.addAll(currentBP.getWallSurfaces());
      roofColl.add(currentBP.getRoof());
    }

    IFeatureCollection<IFeature> featGutter = new FT_FeatureCollection<IFeature>();
    IFeatureCollection<IFeature> featGable = new FT_FeatureCollection<IFeature>();
    IFeatureCollection<IFeature> featRoofing = new FT_FeatureCollection<IFeature>();
    
    // On défini les représentations et on récupère les données sur les
    // goutières, les faitages et les pignons
    for (RoofSurface featRoof : roofColl) {
      featRoof.setRepresentation(new Object2d(featRoof, Color.red));

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

  private JComboBox<Version> generateCombobox() {

    // Générer les éléments à partir de la requête
    Vector<Version> lVersion = new Vector<>();
    lVersion.add(new Version(-1, "Données par défaut"));
    lVersion.add(new Version(40, "Version 40"));
    lVersion.add(new Version(41, "Version 41"));
    lVersion.add(new Version(42, "Version 42"));

    JComboBox<Version> comb = new JComboBox<>(lVersion);
    comb.setName("test");
    comb.setSize(100, 20);
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

        // J'ai l'ID de la version à sélectionner, maintenant, il faut lancer le
        // chargement
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
