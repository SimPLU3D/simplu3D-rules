package fr.ign.cogit.simplu3d.io.feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ign.cogit.simplu3d.model.Regulation;

public class RegulationReader {
  
  
  public RegulationReader(){
    
  }
  
  
  public  List<Regulation> prepareRegulation(String path) throws IOException  {

    File f = new File(path);

    List<Regulation> lRegulation = new ArrayList<>();
    
    
    // On lit le fichier
    BufferedReader in = new BufferedReader(new FileReader(f));
    String line = in.readLine();

    Object[] listItem = line.split(";");

    // On traite chaque ligne
    while ((line = in.readLine()) != null) {

        Object[] listValue = line.split(";");

        Map<String, Object> newmap = new HashMap<>();

        for (int i = 0; i < listValue.length; i++) {
            newmap.put(listItem[i].toString(), listValue[i]);

        }

        System.out.println("*************************************************");
        System.out.println("*******************" + listValue[0] + "******************");

        int code_imu = 0; 
        String libelle_zone = newmap.get("Libelle_Zo").toString(); // LIBELLE_ZONE
        String insee = newmap.get("INSEE").toString();
        int date_approbation = Integer.parseInt(newmap.get("ANNEE").toString());
        String libelle_de_base = newmap.get("LIBELLE_DE").toString(); // LIBELLE_DE_BASE
        String libelle_de_dul = newmap.get("LIBELLE__1").toString(); // LIBELLE_DE_DUL
        int fonctions = Integer.parseInt(newmap.get("B1_FONCT").toString());
        int top_zac = Integer.parseInt(newmap.get("B1_TOP_ZAC").toString());
        int zonage_coherent = Integer.parseInt(newmap.get("B1_ZON_COR").toString());
        int correction_zonage = Integer.parseInt(newmap.get("B1_ZON_COR").toString());
        int typ_bande = Integer.parseInt(newmap.get("B1_T_BANDE").toString());
        int bande = Integer.parseInt(newmap.get("B1_BANDE").toString());
        int art_5 = Integer.parseInt(newmap.get("B1_ART_5").toString());
        double art_6 = Double.parseDouble(newmap.get("B1_ART_6").toString());
        int art_71 = Integer.parseInt(newmap.get("B1_ART_71").toString());
        double art_72 = Double.parseDouble(newmap.get("B1_ART_72").toString());
        double art_73 = Double.parseDouble(newmap.get("B1_ART_73").toString());
        int art_74 = Integer.parseInt(newmap.get("B1_ART_74").toString());
        double art_8 = Double.parseDouble(newmap.get("B1_ART_8").toString());
        double art_9 = Double.parseDouble(newmap.get("B1_ART_9").toString());
        int art_10_top = Integer.parseInt(newmap.get("B1_ART_10T").toString());
        int art_101 = Integer.parseInt(newmap.get("B1_ART_10").toString()); // ATTENTION
                                                                            // A
                                                                            // CHANGER
        int art_102 = Integer.parseInt(newmap.get("B1_HAUT_MT").toString());
        double art_12 = Double.parseDouble(newmap.get("B1_ART_12").toString());
        double art_13 = Double.parseDouble(newmap.get("B1_ART_13").toString());
        double art_14 = Double.parseDouble(newmap.get("B1_ART_14").toString());


        Regulation r = new Regulation(code_imu, libelle_zone, insee, date_approbation, libelle_de_base,
                libelle_de_dul, fonctions, top_zac, zonage_coherent, correction_zonage, typ_bande, bande, art_5,
                art_6, art_71, art_72, art_73, art_74, art_8, art_9, art_10_top, art_101, art_102, art_12, art_13,
                art_14);


        lRegulation.add(r);
        r.setNumBand(1);


        System.out.println(r.toString());

        if (bande != 0) {
            int fonctions_2 = Integer.parseInt(newmap.get("B2_FONCT").toString());
            int art_5_2 = Integer.parseInt(newmap.get("B2_ART_5").toString());
            double art_6_2 = Double.parseDouble(newmap.get("B2_ART_6").toString());
            int art_71_2 = Integer.parseInt(newmap.get("B2_ART_71").toString());
            double art_72_2 = Double.parseDouble(newmap.get("B2_ART_72").toString());
            double art_73_2 = Double.parseDouble(newmap.get("B2_ART_73").toString());
            int art_74_2 = Integer.parseInt(newmap.get("B2_ART_74").toString());
            double art_8_2 = Double.parseDouble(newmap.get("B2_ART_8").toString());
            double art_9_2 = Double.parseDouble(newmap.get("B2_ART_9").toString());
            int art_10_top_2 = Integer.parseInt(newmap.get("B2_ART_10T").toString());
            int art_101_2 = Integer.parseInt(newmap.get("B2_ART_10").toString()); // ATTENTION
                                                                                    // A
                                                                                    // CHANGER
            int art_102_2 = Integer.parseInt(newmap.get("B2_HAUT_M").toString());
            double art_12_2 = Double.parseDouble(newmap.get("B2_ART_12").toString());
            double art_13_2 = Double.parseDouble(newmap.get("B2_ART_13").toString());
            double art_14_2 = Double.parseDouble(newmap.get("B2_ART_14").toString());

            Regulation r2 = new Regulation(code_imu, libelle_zone, insee, date_approbation, libelle_de_base,
                    libelle_de_dul, fonctions_2, top_zac, zonage_coherent, correction_zonage, typ_bande, bande,
                    art_5_2, art_6_2, art_71_2, art_72_2, art_73_2, art_74_2, art_8_2, art_9_2, art_10_top_2,
                    art_101_2, art_102_2, art_12_2, art_13_2, art_14_2);

            System.out.println(r2.toString());

            lRegulation.add(r2);
            r2.setNumBand(2);
        }

        System.out.println("*************************************************");

    }

    in.close();

    return lRegulation;

}

}
