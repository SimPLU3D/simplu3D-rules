package fr.ign.cogit.simplu3d.io.regulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import fr.ign.cogit.simplu3d.model.ZoneRegulation;

public class RennesRegulationReader {

	public static Map<String, ZoneRegulation> loadRules(String path) throws IOException {

		// On initialise la map
		Map<String, ZoneRegulation> table = new Hashtable<>();

		// On charge le fichier CSV avec modèle-Rennes
		File f = new File(path);
		if (!f.exists()) {
			return table;
		}

		// On lit le fichier
		BufferedReader in = new BufferedReader(new FileReader(f));
		// On saute la première ligne car c'est une en-tête
		String line = in.readLine();
		// On traite chaque ligne
		while ((line = in.readLine()) != null) {

			// On instancier la réglementation
			ZoneRegulation r = regulationFromLine(line);
			// On regarde si le code imu a été rencontré auparavant
			String nom_Zone = r.getNomZone();
			table.put(nom_Zone, r);

		}

		in.close();

		return table;
	}

	public static ZoneRegulation regulationFromLine(String str) {
		return regulationFromTable(str.split(","));
	}

	public static ZoneRegulation regulationFromTable(String[] tabS) {
		return new ZoneRegulation(

				tabS[0], // nomZone
				Double.parseDouble(tabS[1]), Double.parseDouble(tabS[2]), Double.parseDouble(tabS[3]),
				Double.parseDouble(tabS[4]), Double.parseDouble(tabS[5]), Double.parseDouble(tabS[6]),
				Double.parseDouble(tabS[7]), Double.parseDouble(tabS[8]), Double.parseDouble(tabS[9]),
				Double.parseDouble(tabS[10]), Double.parseDouble(tabS[11]), Double.parseDouble(tabS[12]),
				Double.parseDouble(tabS[13]), Double.parseDouble(tabS[14]), Double.parseDouble(tabS[15]),
				Double.parseDouble(tabS[16]), Double.parseDouble(tabS[17]), Double.parseDouble(tabS[18]),
				Double.parseDouble(tabS[19])

		);

	}

}
