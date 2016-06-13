package fr.ign.cogit.simplu3d.demo;

import java.io.File;
import java.util.List;


import fr.ign.cogit.simplu3d.checker.Checker;
import fr.ign.cogit.simplu3d.checker.Rules;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.importer.AssignBuildingPartToSubParcel;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.reader.RoadReader;
import fr.ign.cogit.simplu3d.reader.UrbaZoneReader;

public class DemoClassLoaderSimPLU3DApi {

	public static void main(String[] args) throws Exception {
		
		// Rerouting towards the new files
		LoaderSHP.NOM_FICHIER_PLU = "DOC_URBA.shp";
		LoaderSHP.NOM_FICHIER_ZONAGE = "zones_UB2.shp";
		LoaderSHP.NOM_FICHIER_PARCELLE = "parcelles_UB2.shp";
		LoaderSHP.NOM_FICHIER_TERRAIN = "MNT_UB2_L93.asc";
		LoaderSHP.NOM_FICHIER_VOIRIE = "Voirie_UB2.shp";
		// LoaderSHP.NOM_FICHIER_BATIMENTS = "Bati_UB2_3D.shp";
		LoaderSHP.NOM_FICHIER_BATIMENTS = "Bati_UB2_3D_V2.shp";

		LoaderSHP.NOM_FICHIER_PRESC_LINEAIRE = "no_file.shp";

		// Corrections on attributes
		RoadReader.ATT_LARGEUR = "LARGEUR";
		RoadReader.ATT_NOM_RUE = "NOM_VOIE_G";
		RoadReader.ATT_TYPE = "NATURE";

		CadastralParcelLoader.ATT_ID_PARC = "NUMERO";
		CadastralParcelLoader.TYPE_ANNOTATION = 1;

		UrbaZoneReader.ATT_TYPE_ZONE = "TYPE";

		AssignBuildingPartToSubParcel.RATIO_MIN = 0.8;
		AssignBuildingPartToSubParcel.ASSIGN_METHOD = 0;
		
		Environnement env = LoaderSHP.loadNoDTM(new File(
				"/home/mickael/data/mbrasebin/workspace/simPLU3D/simplu3d-api/src/test/resources/data/demo-01"));

		BasicPropertyUnit bPU = env.getBpU().get(20);
		List<UnrespectedRule> lSp = Checker.check(bPU, new Rules("UB2,6,0.5,500,16,0.4,16.5,0,0,4,1,1,11,1,3,6,8.5,1,3.5,7"));
		
		System.out.println(lSp.size());
		
	}

}
