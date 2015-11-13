package fr.ign.cogit.simplu3d.io.load.instruction;

import java.io.File;

import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.simplu3d.importer.applicationClasses.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.PLU;

/*
 * Liste de remarques :
 * 
 * Le fichier de zonage fourni par RM n'est pas aux normes du CNIG.
 * 
 * Le fichier de zonage comporte très peu des champs nécessaires pour compléter
 * la table dans PostGis.
 * 
 * Lorsque l'on teste l'export des parcelles vers PostGis, elles sont découpés
 * selon un MNT qui n'est pas présent!?
 * 
 * Le MNT RENNES (récupéré dans les données Open Data de RM) renvoie
 * "OutOfMemoryError". Les cellules du raster de RM font 2m*2m alors que celles
 * du raster de Strasbourg font 10m*10m (mesures effectuées sur QGIS). Le
 * problème vient peut-être de là.
 * 
 */
public class LoadExpRennes {

  public static void main(String[] args) throws Exception {

    PostgisManager.SRID = "2154";
    
    CadastralParcelLoader.TYPE_ANNOTATION = 2;

    String host = "localhost";
    String user = "postgres";
    String pw = "postgres";
    String database = "test_simplu3d";
    String folder = "D:/0_Masson/1_CDD_SIMPLU/2_Travail/0_Workspace/simplu3d/simplu3D-rules/src/main/resources/fr/ign/cogit/simplu3d/data/";
    String port = "5432";

    // Rerouting towards the new files of Rennes

    LoaderSHP.NOM_FICHIER_ZONAGE = "[Insert File Name].shp";
    LoaderSHP.NOM_FICHIER_PARCELLE = "parcelles_UB2_rennes_test.shp";
    LoaderSHP.NOM_FICHIER_VOIRIE = "[Insert File Name].shp";
    LoaderSHP.NOM_FICHIER_BATIMENTS = "[Insert File Name].shp";

    // TODO : Problème sur le MNT de Rennes Métropole : out of memory (trop
    // volumineux???)
    LoaderSHP.NOM_FICHIER_TERRAIN = "[Insert File Name].asc";

    LoaderSHP.NOM_FICHIER_PRESC_LINEAIRE = "[Insert File Name].shp";
    LoaderSHP.NOM_FICHIER_PLU = "DOC_URBA.shp";

    System.out.println("List of the handled files : \n \t"
        + "File zonage : \t \t"
        + LoaderSHP.NOM_FICHIER_ZONAGE
        + "\n \t"
        + "File parcelle : \t"
        + LoaderSHP.NOM_FICHIER_PARCELLE
        + "\n \t"
        + "File voirie : \t \t"
        + LoaderSHP.NOM_FICHIER_VOIRIE
        + "\n \t"
        + "File batiments : \t"
        + LoaderSHP.NOM_FICHIER_BATIMENTS
        + "\n \t"
        + "File terrain : \t \t"
        + LoaderSHP.NOM_FICHIER_TERRAIN
        + "\n \t"
        + "File prescription : \t"
        + LoaderSHP.NOM_FICHIER_PRESC_LINEAIRE
        + "\n \t"
        + "File PLU : \t \t"
        + LoaderSHP.NOM_FICHIER_PLU);

    // Corrections on the names of the attributes
    CadastralParcelLoader.ATT_ID_PARC = "NUMERO";

    // TODO : Problems on the "loadAll" function, see in "Load.java"
    Load.loadAll(host, port, user, pw, database, folder);

  }

}

// Soit modifier les données soit modifier les valeurs des champs

// String str =
// bpu.getAttribute(ParametersInstructionPG.ATT_BPU_ID).toString();
// Double dbl =
// Double.parseDouble(bpu.getAttribute(ParametersInstructionPG.ATT_BPU_ID).toString());
// Integer ngr =
// Integer.parseInt(bpu.getAttribute(ParametersInstructionPG.ATT_BPU_ID).toString());
