package fr.ign.cogit.simplu3d.util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.sig3d.Messages;

public class ExtractIdMaxPG {

  private final static Logger logger = Logger.getLogger(ExtractIdMaxPG.class
      .getName());

  /**
   * Cette fonction permet de récupérer la valeur du plus grand identifiant
   * d'une table
   * 
   * @param host hote (localhost accepté)
   * @param port port d'écoute
   * @param database nom de la base de données
   * @param user utilisateur
   * @param pw mot de passe
   * @param nameTable nom de la table
   * @param nameColId nom de la colonne contenant les id
   * @return un int de la valeur du plus grand id (exemple : 6) ou 0 si la table
   *         est vide
   */
  public static int idMaxTable(String host, String port, String database,
      String user, String pw, String nameTable, String nameColId)
      throws Exception {

    java.sql.Connection conn;

    // Objet pour contenir l'ID Max
    int idMax = 0;
    try {

      // Création de l'URL de chargement
      String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
      ExtractIdMaxPG.logger.info(Messages.getString("PostGIS.Try") + url);

      // Connexion
      conn = DriverManager.getConnection(url, user, pw);

      // Requete pour extraire la valeur maximale de la colonne ID
      Statement s = conn.createStatement();
      ResultSet r = s.executeQuery("SELECT MAX(" + nameColId + ") FROM "
          + nameTable);

      // Ajout de la valeur extraite à idMax
      while (r.next()) {
        idMax = r.getInt(1);
      }

      int x = idMax + 1;
      System.out.println("\n----- Table '" + nameTable
          + "' found. The present maximal identifier in the column ID ("
          + nameColId + ") is : " + idMax + ". -----");
      System.out
          .println("----- The first integrated identifier will have for value : "
              + x + ". -----\n");

      // Fermeture de la connection
      s.close();
      conn.close();
      ExtractIdMaxPG.logger.info(Messages.getString("PostGIS.End"));

    } catch (Exception e) {
      throw e;
    }

    return idMax;

  }

}
