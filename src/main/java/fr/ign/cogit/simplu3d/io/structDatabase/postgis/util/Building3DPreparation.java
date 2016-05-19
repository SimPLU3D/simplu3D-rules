package fr.ign.cogit.simplu3d.io.structDatabase.postgis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTMArea;
import fr.ign.cogit.geoxygene.sig3d.util.ColorShade;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;

public class Building3DPreparation {

  public final static boolean sursampled = true;

  public static String folder = "D:/0_Masson/1_CDD_SIMPLU/2_Travail/0_Workspace/simplu3d/"
      + "simplu3D-rules/src/main/resources/fr/ign/cogit/simplu3d/data/dataRennes/";
  public static String folderOut = folder + "out/";

  public static String fichierMNT = "MNT_UB2_L93.asc";
  public static String fichierBati = "Bati_UB2_V3.shp";

  public static String attHauteur = "HAUTEUR";

  public static String nomFichierOut = "Bati_UB2_3D_V3";
  public static String nomShpOut = nomFichierOut + ".shp";
  public static String nomPrjOut = nomFichierOut + ".prj";

  public static void main(String[] args) throws Exception {

    // Etape n°1 : Chargement du MNT
    DTMArea dtm = loadDTM(folder);

    // Etape n°2 : Chargement des batiments
    IFeatureCollection<IFeature> batiColl = ShapefileReader.read(folder
        + fichierBati);

    // Etape n°3 : On boucle sur les batiments pour créer la 3D
    for (IFeature currentBat : batiColl) {

      // On plaque les batiments sur le MNT
      IGeometry geom = dtm.mapGeom(currentBat.getGeom(), 0, true, sursampled);
      // currentBat.setGeom(geom);

      // On récupère le ZMin
      Box3D boxBati = new Box3D(geom);
      Double zMin = boxBati.getLLDP().getZ();
      // System.out.println("id : " + currentBat.getId() + " / zMin : " + zMin);

      // On récupère la hauteur du batiment
      Object attHauteurObj = currentBat.getAttribute(attHauteur);
      String attHauteurStr = attHauteurObj.toString();
      int hauteur = Integer.parseInt(attHauteurStr);

      // On calcul ZMax
      Double zMax = zMin + hauteur;

      // On extrude le batiment
      IFeature featOutExtrud = Extrusion2DObject.convertFromFeature(currentBat,
          zMin, zMax);

      // On réaffecte la géométrie 3D aux batiments de base
      currentBat.setGeom(FromGeomToSurface.convertMSGeom(featOutExtrud
          .getGeom()));

    }

    // Etape n°4 : on exporte au format shp
    ShapefileWriter.write(batiColl, folderOut + nomShpOut);

    // Etape n°5 : (facultative) on crée un fichier PRJ en Lambert93
    lancerCreationSCR();

    System.out.println("End");

  }

  /**
   * Permet le chargement d'un MNT
   * @param folder le chemin d'accès au MNT
   * @return un MNT au format DTMArea
   * @throws Exception
   */
  public static DTMArea loadDTM(String folder) throws Exception {

    return loadDTM(new File(folder), new FileInputStream(folder
        + File.separator + fichierMNT));

  }

  /**
   * Permet le chargement d'un MNT
   * @param folder le chemin d'accès au MNT
   * @param dtmStream
   * @return un MNT au format DTMArea
   * @throws Exception
   */
  public static DTMArea loadDTM(File folder, InputStream dtmStream)
      throws Exception {

    DTMArea dtm = new DTMArea(dtmStream, "Terrain", true, 1,
        ColorShade.BLUE_CYAN_GREEN_YELLOW_WHITE);

    return dtm;

  }

  /**
   * Construit un fichier PRJ
   * @throws FileNotFoundException
   */
  static void lancerCreationSCR() throws FileNotFoundException {

    // Création du fichier PRJ (si un fichier existe déjà, il est supprimé)
    File fichierPRJ = new File(folderOut + nomPrjOut);
    PrintWriter outputPRJ = new PrintWriter(fichierPRJ);

    completPRJ(outputPRJ);

    System.out.println("\n--> Création d'un fichier PRJ : " + nomPrjOut);

    outputPRJ.close();
  }

  /**
   * Permet de compléter le PRj avec les informations du Lambert93
   * @param output
   * @throws FileNotFoundException
   */
  static void completPRJ(PrintWriter output) throws FileNotFoundException {

    // Paramètres du lambert 93
    output.print("PROJCS[\"RGF93_Lambert_93\"," + "GEOGCS[\"GCS_RGF93\","
        + "DATUM[\"D_RGF_1993\","
        + "SPHEROID[\"GRS_1980\",6378137,298.257222101]],"
        + "PRIMEM[\"Greenwich\",0],"
        + "UNIT[\"Degree\",0.017453292519943295]],"
        + "PROJECTION[\"Lambert_Conformal_Conic\"],"
        + "PARAMETER[\"standard_parallel_1\",49],"
        + "PARAMETER[\"standard_parallel_2\",44],"
        + "PARAMETER[\"latitude_of_origin\",46.5],"
        + "PARAMETER[\"central_meridian\",3],"
        + "PARAMETER[\"false_easting\",700000],"
        + "PARAMETER[\"false_northing\",6600000]," + "UNIT[\"Meter\",1]]");

  }

}
