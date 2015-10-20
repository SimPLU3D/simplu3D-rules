package fr.ign.cogit.simplu3d.io.load.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTMArea;
import fr.ign.cogit.geoxygene.sig3d.util.ColorShade;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.model.application.Environnement;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class LoaderSHP {

  /*
   * Nom des fichiers en entrée
   */
  public static String NOM_FICHIER_ZONAGE = "ZONE_URBA.shp";
  public static String NOM_FICHIER_PARCELLE = "parcelle.shp";
  public static String NOM_FICHIER_VOIRIE = "route.shp";
  public static String NOM_FICHIER_BATIMENTS = "bati.shp";
  public static String NOM_FICHIER_TERRAIN = "MNT_BD3D.asc";
  public static String NOM_FICHIER_PRESC_LINEAIRE = "PRESCRIPTION_LIN.shp";
  public static String NOM_FICHIER_PLU = "DOC_URBA.shp";

  public Environnement getEnvironnement(String folder) throws Exception {
    return LoaderSHP.load(folder);
  }

  public static Environnement load(String folder) throws Exception {
    return load(folder, new FileInputStream(folder + NOM_FICHIER_TERRAIN));
  }

  public static Environnement loadNoDTM(File folder) throws Exception {
    Environnement env = Environnement.getInstance();
    env.folder = folder.getAbsolutePath();
    // Chargement des fichiers
    IFeatureCollection<IFeature> pluColl = ShapefileReader.read(folder + File.separator + NOM_FICHIER_PLU);
    IFeature featPLU = null;
    if (!pluColl.isEmpty()) {
      featPLU = pluColl.get(0);
    }
    IFeatureCollection<IFeature> zoneColl = ShapefileReader.read(folder + File.separator + NOM_FICHIER_ZONAGE);
    IFeatureCollection<IFeature> parcelleColl = ShapefileReader.read(folder + File.separator + NOM_FICHIER_PARCELLE);
    IFeatureCollection<IFeature> voirieColl = ShapefileReader.read(folder + File.separator + NOM_FICHIER_VOIRIE);
    IFeatureCollection<IFeature> batiColl = ShapefileReader.read(folder + File.separator + NOM_FICHIER_BATIMENTS);
    IFeatureCollection<IFeature> prescriptions = ShapefileReader.read(folder + File.separator + NOM_FICHIER_PRESC_LINEAIRE);
    return LoadFromCollection.load(featPLU, zoneColl, parcelleColl, voirieColl, batiColl, prescriptions, folder.getAbsolutePath(), null);
  }

  public static Environnement load(String folder, InputStream dtmStream) throws Exception {
    Environnement env = Environnement.getInstance();
    env.folder = folder;
    // Chargement des fichiers
    IFeatureCollection<IFeature> pluColl = ShapefileReader.read(folder + NOM_FICHIER_PLU);
    IFeature featPLU = null;
    if (!pluColl.isEmpty()) {
      featPLU = pluColl.get(0);
    }
    IFeatureCollection<IFeature> zoneColl = ShapefileReader.read(folder + NOM_FICHIER_ZONAGE);
    IFeatureCollection<IFeature> parcelleColl = ShapefileReader.read(folder + NOM_FICHIER_PARCELLE);
    IFeatureCollection<IFeature> voirieColl = ShapefileReader.read(folder + NOM_FICHIER_VOIRIE);
    IFeatureCollection<IFeature> batiColl = ShapefileReader.read(folder + NOM_FICHIER_BATIMENTS);
    IFeatureCollection<IFeature> prescriptions = ShapefileReader.read(folder + NOM_FICHIER_PRESC_LINEAIRE);
    // sous-parcelles route sans z, zonage, les bordures etc...
    DTMArea dtm = new DTMArea(dtmStream, "Terrain", true, 1, ColorShade.BLUE_CYAN_GREEN_YELLOW_WHITE);
    return LoadFromCollection.load(featPLU, zoneColl, parcelleColl, voirieColl, batiColl, prescriptions, folder, dtm);
  }
}
