package fr.ign.cogit.simplu3d.io.load.instruction;

import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.simplu3d.importer.applicationClasses.AssignBuildingPartToSubParcel;
import fr.ign.cogit.simplu3d.importer.applicationClasses.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.importer.applicationClasses.RoadImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.ZonesImporter;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;

public class LoadExpRennes {

  /**
   * Permet le chargement des données de RM dans la base de données
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    PostgisManager.SRID = "2154";

    String host = "localhost";
    String user = "postgres";
    String pw = "postgres";
    String database = "simplu3d-rennes";
    String folder = "/home/mickael/data/mbrasebin/donnees/dataRennes/";
    String port = "5432";

    // Rerouting towards the new files
    LoaderSHP.NOM_FICHIER_PLU = "DOC_URBA.shp";
    LoaderSHP.NOM_FICHIER_ZONAGE = "zones_UB2.shp";
    LoaderSHP.NOM_FICHIER_PARCELLE = "parcelles_UB2.shp";
    LoaderSHP.NOM_FICHIER_TERRAIN = "MNT_UB2_L93.asc";
    LoaderSHP.NOM_FICHIER_VOIRIE = "Voirie_UB2.shp";
    // LoaderSHP.NOM_FICHIER_BATIMENTS = "Bati_UB2_3D.shp";
    LoaderSHP.NOM_FICHIER_BATIMENTS = "out/Bati_UB2_3D_V2.shp";

    LoaderSHP.NOM_FICHIER_PRESC_LINEAIRE = "[Insert File Name].shp";

    // Corrections on attributes
    RoadImporter.ATT_LARGEUR = "LARGEUR";
    RoadImporter.ATT_NOM_RUE = "NOM_VOIE_G";
    RoadImporter.ATT_TYPE = "NATURE";

    CadastralParcelLoader.ATT_ID_PARC = "NUMERO";
    CadastralParcelLoader.TYPE_ANNOTATION = 1;

    ZonesImporter.NOM_ATT_TYPE_ZONE = "TYPE";

    AssignBuildingPartToSubParcel.RATIO_MIN = 0.8;
    AssignBuildingPartToSubParcel.ASSIGN_METHOD = 0;

    Load.loadAll(host, port, user, pw, database, folder);

  }

}
