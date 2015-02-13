package fr.ign.cogit.gru3d.io.exports;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.contrib.appariement.Lien;

/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 *
 * Permet d'exporter les liens suite à un appariement
 * 
 * 
 */
public class ExportLinks {

  public static boolean exports(String path,
      IFeatureCollection<IFeature> featCollRef, List<Lien> lLiens,
      IFeatureCollection<IFeature> featCollComp) {

    try {
      PrintWriter out = new PrintWriter(
          new BufferedWriter(new FileWriter(path)));

      int nbLiens = lLiens.size();

      for (int i = 0; i < nbLiens; i++) {

        String s = "";

        Lien l = lLiens.get(i);

        List<IFeature> lObjRef = l.getObjetsRef();
        int nbRef = lObjRef.size();

        for (int j = 0; j < nbRef; j++) {

          if (j != 0) {
            s = s + ";";
          }

          s = s + featCollRef.getElements().indexOf(lObjRef.get(j));

        }

        s = s + "/";

        List<IFeature> lObjComp = l.getObjetsComp();
        int nbComp = lObjComp.size();

        for (int j = 0; j < nbComp; j++) {

          if (j != 0) {
            s = s + ";";
          }

          s = s + featCollComp.getElements().indexOf(lObjComp.get(j));

        }

        s = s + "\n";

        out.append(s);

      }

      out.flush();
      out.close();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return false;
    }

    return true;

  }

}
