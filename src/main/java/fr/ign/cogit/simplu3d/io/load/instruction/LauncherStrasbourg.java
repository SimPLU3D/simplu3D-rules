package fr.ign.cogit.simplu3d.io.load.instruction;

import java.awt.Color;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object1d;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object2d;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;
import fr.ign.cogit.geoxygene.sig3d.semantic.Map3D;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.sig3d.util.ColorLocalRandom;
import fr.ign.cogit.geoxygene.sig3d.util.ColorRandom;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;

public class LauncherStrasbourg {

  public static String folder = "D:/0_Masson/1_CDD_SIMPLU/2_Travail/0_Workspace/"
      + "simplu3d/simplu3D-rules/src/main/resources/fr/ign/cogit/simplu3d/data/";

  public static void main(String[] args) throws Exception {

    // On corrige le nom de la base de données
    Load.database = "strasbourg_simplu";

    // On charge l'environnement depuis la base de données
    Environnement env = LoaderPostGISTest.load(folder);

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
      roofColl.add(currentBP.getToit());
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

    MainWindow fenPrincipale = new MainWindow();
    Map3D carte = fenPrincipale.getInterfaceMap3D().getCurrent3DMap();

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

}
