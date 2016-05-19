package fr.ign.cogit.simplu3d.io.structDatabase.postgis.loader;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.simplu3d.io.structDatabase.postgis.ParametersInstructionPG;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.BuildingPart;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.Road;
import fr.ign.cogit.simplu3d.model.RoofSurface;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.SpecificWallSurface;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.UrbaDocument;
import fr.ign.cogit.simplu3d.model.UrbaZone;

public class LoaderBPU {

	public final static String NOM_MNT = "MNT_BD3D.asc";

	public Environnement getEnvironnement(Integer IdVersion, Integer searchIdBPU) throws Exception {
		return LoaderBPU.load(IdVersion, searchIdBPU);
	}

	public static Environnement load(Integer IdVersion, Integer searchIdBPU) throws Exception {

		Environnement env = Environnement.getInstance();

		return load(env, IdVersion, searchIdBPU);

	}

	public static Environnement load(Environnement env, Integer IdVersion, Integer searchIdBPU) throws Exception {

		// Parameters PostGIS database
		String host = ParametersInstructionPG.host;
		String user = ParametersInstructionPG.user;
		String pw = ParametersInstructionPG.pw;
		String database = ParametersInstructionPG.database;
		String port = ParametersInstructionPG.port;

		// Parameters tables
		String tableBPU = ParametersInstructionPG.TABLE_BASIC_PROPERTY_UNIT;
		String attIdBPU = ParametersInstructionPG.ATT_BPU_ID;

		String tableCadPar = ParametersInstructionPG.TABLE_CADASTRAL_PARCEL;
		String attIdCadPar = ParametersInstructionPG.ATT_CAD_PARCEL_ID;
		String attIdCadParBPU = ParametersInstructionPG.ATT_CAD_PARCEL_ID_BPU;

		String tableSubPar = ParametersInstructionPG.TABLE_SUB_PARCEL;
		String attIdSubPar = ParametersInstructionPG.ATT_SUB_PARCEL_ID;
		String attIdSubParCadPar = ParametersInstructionPG.ATT_SUB_PARCEL_ID_CADPAR;

		String tableSCB = ParametersInstructionPG.TABLE_SPECIFIC_CBOUNDARY;
		String attIdSCBSubPar = ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID_SUB_PAR;

		String tableRoad = ParametersInstructionPG.TABLE_ROAD;
		String attIdRoad = ParametersInstructionPG.ATT_ROAD_ID;

		String tableAxis = ParametersInstructionPG.TABLE_AXE;
		String attIdAxisRoad = ParametersInstructionPG.ATT_AXE_ID_ROAD;

		String tableZU = ParametersInstructionPG.TABLE_ZONE_URBA;
		String attIdZU = ParametersInstructionPG.ATT_ZONE_URBA_ID;

		String tablePLU = ParametersInstructionPG.TABLE_DOC_URBA;
		String attIdPLU = ParametersInstructionPG.ATT_DOC_URBA_ID_URBA;

		String tableBP = ParametersInstructionPG.TABLE_BUILDING_PART;
		String attIdBP = ParametersInstructionPG.ATT_BUILDING_PART_ID;
		String attIdBPSubPar = ParametersInstructionPG.ATT_BUILDING_PART_ID_SUBPAR;
		String attIdBPVersion = ParametersInstructionPG.ATT_BUILDING_PART_ID_VERSION;

		String tableBuild = ParametersInstructionPG.TABLE_BUILDING;
		String attIdBuild = ParametersInstructionPG.ATT_BUILDING_ID;

		String tableWall = ParametersInstructionPG.TABLE_WALL_SURFACE;
		String attIdWallBP = ParametersInstructionPG.ATT_WALL_SURFACE_ID_BUILDP;

		String tableRoof = ParametersInstructionPG.TABLE_ROOF;
		String attIdRoofBP = ParametersInstructionPG.ATT_ROOF_ID_BUILDPART;

		String tableRoofing = ParametersInstructionPG.TABLE_ROOFING;
		String attIdRoofingR = ParametersInstructionPG.ATT_ROOFING_ID_ROOF;

		String tableGutter = ParametersInstructionPG.TABLE_GUTTER;
		String attIdGutterR = ParametersInstructionPG.ATT_GUTTER_ID_ROOF;

		String tableGable = ParametersInstructionPG.TABLE_GABLE;
		String attIdGableR = ParametersInstructionPG.ATT_GABLE_ID_ROOF;

		// -------------------------------------------------------------------------
		// Partie 1 : Chargement de la Basic Property Unit ciblée et des objets
		// qui
		// lui sont associés (ses parcelles, ses sous-parcelles, ses
		// batiments...)
		// -------------------------------------------------------------------------

		// On construit la clause basée sur l'identifiant fourni en entrée et on
		// lance le chargement et l'import de la BPU dans une IFeatureCollection
		String clauseBPU = generatedWhereClauseInt(searchIdBPU, attIdBPU);
		IFeatureCollection<IFeature> bpuLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableBPU, clauseBPU);
		IFeatureCollection<BasicPropertyUnit> bpuImport = BasicLoader.importBasicPropUnit(bpuLoad);

		// Message d'erreur si la IFC produite et vide (Cas BPU non trouvée dans
		// la
		// base de données) et on arrête le chargement
		if (bpuImport.isEmpty()) {
			System.out.println(
					"\n" + "ERREUR : La valeur '" + searchIdBPU + "' n'existe pas dans la table " + tableBPU + ".");
			return null;
		}

		// On construit la clause pour charger la parcelle à laquelle la BPU
		// appartient et on lance son chargement et son import depuis la base
		String clauseCadPar = generatedWhereClauseInt(searchIdBPU, attIdCadParBPU);
		IFeatureCollection<IFeature> cadParLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
				user, pw, tableCadPar, clauseCadPar);
		IFeatureCollection<CadastralParcel> cadParImport = BasicLoader.importCadParcel(cadParLoad);

		// On récupère les identifiants des parcelles importées au sein d'une
		// liste
		List<Integer> idCadParList = new ArrayList<Integer>();
		for (CadastralParcel currentCP : cadParImport) {
			int id = currentCP.getId();
			if (!idCadParList.contains(id)) {
				idCadParList.add(id);
			}
		}

		// On se sert de cette liste pour produire la clause qui va servir à
		// l'import des sous-parcelles et on lance le chargement et l'import des
		// sous-parcelles depuis la base de données
		String clauseSubPar = generatedWhereClauseList(idCadParList, attIdSubParCadPar);
		IFeatureCollection<IFeature> subParLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
				user, pw, tableSubPar, clauseSubPar);
		IFeatureCollection<SubParcel> subParImport = BasicLoader.importSubParcel(subParLoad);

		// On récupère les id des sous-parcelles importées au sein d'une liste
		List<Integer> idSubParList = new ArrayList<Integer>();
		for (SubParcel currentSP : subParImport) {
			int id = currentSP.getId();
			if (!idSubParList.contains(id)) {
				idSubParList.add(id);
			}
		}

		// On se sert de cette liste pour produire la clause qui va servir à
		// l'import des specific cadastral boundary et on lance le chargement et
		// l'import des SCB depuis la base de données
		String clauseSCB = generatedWhereClauseList(idSubParList, attIdSCBSubPar);
		IFeatureCollection<IFeature> scbLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableSCB, clauseSCB);

		IFeatureCollection<SpecificCadastralBoundary> scbImport = BasicLoader.importSpecificCadBound(scbLoad);

		// -------------------------------------------------------------------------
		// Partie 2 : Chargement des BPU adjacente à la BPu ciblée et des objets
		// qui
		// leurs sont associés (leurs parcelles, leurs sous-parcelles...)
		// -------------------------------------------------------------------------

		List<Integer> idSubParAdjList = new ArrayList<Integer>();
		List<Integer> idParCadAdjList = new ArrayList<Integer>();
		List<Integer> idBPUAdjList = new ArrayList<Integer>();
		List<Integer> idRoadAdjList = new ArrayList<Integer>();

		// A partir des SCB de la BPU cible, on récupère les identifiants des
		// routes
		// et des sous-parcelles adjacentes au sein de différentes liste
		for (SpecificCadastralBoundary currentSCB : scbImport) {
			String tabRef = currentSCB.getTableRef();
			int id = currentSCB.getIdAdj();
			if (tabRef.equalsIgnoreCase("road")) {
				if (!idRoadAdjList.contains(id)) {
					idRoadAdjList.add(id);
				}
			} else if (tabRef.equalsIgnoreCase("sub_parcel")) {
				if (!idSubParAdjList.contains(id)) {
					idSubParAdjList.add(id);
				}
			} else {
				System.out.println("Erreur, la table reference n'est pas reconnue");
			}
		}

		// On se sert de la liste contenant les identifiants des routes
		// adjacentes
		// pour produire la clause qui va servir à l'import de ces routes et on
		// lance le chargement et l'import depuis la base de données
		String clauseRoadAdj = generatedWhereClauseList(idRoadAdjList, attIdRoad);
		IFeatureCollection<IFeature> roadLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableRoad, clauseRoadAdj);
		IFeatureCollection<Road> roadImport = BasicLoader.importRoad(roadLoad);

		// On se sert de la liste contenant les identifiants des sous-parcelles
		// adjacentes pour produire la clause qui va servir à l'import de ces
		// sous-parcelles et on lance le chargement et l'import
		String clauseSPAdj = generatedWhereClauseList(idSubParAdjList, attIdSubPar);
		IFeatureCollection<IFeature> spAdjLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
				user, pw, tableSubPar, clauseSPAdj);
		IFeatureCollection<SubParcel> spAdjImport = BasicLoader.importSubParcel(spAdjLoad);

		// On ajoute les sous-parcelles ainsi chargées à la IFC contenant déjà
		// les
		// sous-parcelles de la BPU cible et on combine les listes d'id de
		// sous-parcelles
		subParImport.addAll(spAdjImport);
		idSubParList.addAll(idSubParAdjList);

		// On se sert de la liste des id des sous-parcelles adjacentes pour
		// produire
		// la clause qui servira à l'import des SCB de ces sous-parcelles
		// adjacentes
		// et on lance le chargement et l'import de ces SCB
		String clauseSCBAdj = generatedWhereClauseList(idSubParAdjList, attIdSCBSubPar);
		IFeatureCollection<IFeature> scbAdjLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
				user, pw, tableSCB, clauseSCBAdj);
		IFeatureCollection<SpecificCadastralBoundary> scbAdjImport = BasicLoader.importSpecificCadBound(scbAdjLoad);

		// On ajoute les nouvelles SCB à notre IFC contenant les SCB de la BPU
		// cible
		scbImport.addAll(scbAdjImport);

		// On récupère depuis les sous-parcelles adjacentes chargées les
		// identifiants des parcelles adjacentes au sein d'une liste
		for (SubParcel currentSPAdj : spAdjImport) {
			int idCP = currentSPAdj.getIdCadPar();
			if (!idParCadAdjList.contains(idCP)) {
				idParCadAdjList.add(idCP);
			}
		}

		// On se sert de cette liste d'identifiant pour produire la clause
		// servant à
		// l'import des parcelles adjacentes et on lance le chargement et
		// l'import
		// des parcelles depuis la base de données
		String clauseCPAdj = generatedWhereClauseList(idParCadAdjList, attIdCadPar);
		IFeatureCollection<IFeature> cadParAdjLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
				user, pw, tableCadPar, clauseCPAdj);
		IFeatureCollection<CadastralParcel> cadParAdjImport = BasicLoader.importCadParcel(cadParAdjLoad);

		// On ajoute les nouvelles parcelles à l'IFC contenant les parcelles
		// déjà
		// chargées et en relation avec la BPU cible
		cadParImport.addAll(cadParAdjImport);

		// On récupère depuis les parcelles adjacentes chargées les identifiants
		// des
		// BPU au sein d'une liste
		for (CadastralParcel currentCPAdj : cadParAdjImport) {
			int id = currentCPAdj.getId();
			if (!idBPUAdjList.contains(id)) {
				idBPUAdjList.add(id);
			}
		}

		// On se sert de cette liste d'identifiant pour produire la clause
		// servant à
		// l'import des BPU adjacentes et on lance le chargement et l'import
		// des BPU depuis la base de données
		String clauseBPUAdj = generatedWhereClauseList(idBPUAdjList, attIdBPU);
		IFeatureCollection<IFeature> bpuAdjLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
				user, pw, tableBPU, clauseBPUAdj);
		IFeatureCollection<BasicPropertyUnit> bpuAdjImport = BasicLoader.importBasicPropUnit(bpuAdjLoad);

		// On ajoute les nouvelles BPU à l'IFC contenant la BPU cible
		bpuImport.addAll(bpuAdjImport);

		// On se sert de la liste des idnetifiants des routes adjacentes pous
		// produire la clause servant à charger les axes des routes adjacentes
		// et on
		// lance le chargement et l'import de ces axes depuis la base de données
		String clauseAxis = generatedWhereClauseList(idRoadAdjList, attIdAxisRoad);
		IFeatureCollection<IFeature> axisLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableAxis, clauseAxis);
		IFeatureCollection<Road> axisImport = BasicLoader.importAxis(axisLoad);

		// On récupère depuis les sous-parcelles chargées les identifiants des
		// zones
		// urbanisées au sein d'une liste
		List<Integer> idZUList = new ArrayList<Integer>();
		for (SubParcel currentSP : subParImport) {
			int id = currentSP.getIdZoneUrba();
			if (!idZUList.contains(id)) {
				idZUList.add(id);
			}
		}

		// On se sert de cette liste pour construire la clause pui va servir à
		// charger les zones urba et on lance le chargement et l'import depuis
		// la
		// base de données
		String clauseZU = generatedWhereClauseList(idZUList, attIdZU);
		IFeatureCollection<IFeature> zuLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableZU, clauseZU);
		IFeatureCollection<UrbaZone> zuImport = BasicLoader.importZoneUrba(zuLoad);

		// On se sert des zones urba chargées pour obtenir les identifiants du
		// PLU
		List<String> idPLUList = new ArrayList<String>();
		for (UrbaZone currentZU : zuImport) {
			String id = currentZU.getIdPLU();
			if (!idPLUList.contains(id)) {
				idPLUList.add(id);
			}
		}

		// On se sert de l'identifiant pour charger et importer le plu depuis la
		// base de données
		String clausePLU = generatedWhereClauseList(idPLUList, attIdPLU);
		IFeatureCollection<IFeature> pluLoad = PostgisManager.loadNonGeometricTableWhereClause(host, port, database,
				user, pw, tablePLU, clausePLU);
		UrbaDocument pluImport = BasicLoader.importPLU(pluLoad);

		// On utilise la liste des identifiants de sous-parcelles pour produire
		// la
		// clause servant à obtenir les parties de batiments et on lance le
		// chargement et l'import des parties de batiments depuis la base de
		// données
		String clauseBP = generatedWhereClauseListBuilding(idSubParList, attIdBPSubPar, IdVersion, attIdBPVersion,
				attIdBP, host, port, database, user, pw);
		IFeatureCollection<IFeature> bpLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableBP, clauseBP);
		IFeatureCollection<BuildingPart> bpImport = BasicLoader.importBuildPart(bpLoad);

		// On récupère depuis les parties de batiments chargées la liste des
		// identifiants de batiments
		List<Integer> idBuildList = new ArrayList<Integer>();
		for (BuildingPart currentBP : bpImport) {
			int id = currentBP.getIdBuilding();
			if (!idBuildList.contains(id)) {
				idBuildList.add(id);
			}
		}

		// On se sert de cette liste pour charger et importer depuis la base de
		// donnée les batiments
		String clauseBuild = generatedWhereClauseList(idBuildList, attIdBuild);
		IFeatureCollection<IFeature> buildLoad = PostgisManager.loadNonGeometricTableWhereClause(host, port, database,
				user, pw, tableBuild, clauseBuild);
		IFeatureCollection<Building> buildImport = BasicLoader.importBuilding(buildLoad);

		// On récupère depuis les parties de batiments chargées la liste des
		// identifiants de parties de batiments
		List<Integer> idBuildPartList = new ArrayList<Integer>();
		for (BuildingPart currentBP : bpImport) {
			int id = currentBP.getId();
			if (!idBuildPartList.contains(id)) {
				idBuildPartList.add(id);
			}
		}

		// On se sert de cette liste pour charger et importer depuis la base de
		// données les murs des batiments
		String clauseWall = generatedWhereClauseList(idBuildPartList, attIdWallBP);
		IFeatureCollection<IFeature> wallLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableWall, clauseWall);
		IFeatureCollection<SpecificWallSurface> wallImport = BasicLoader.importWall(wallLoad);

		// On se sert de la liste des identifiants des parties de batiments pour
		// charger et importer les toits
		String clauseRoof = generatedWhereClauseList(idBuildPartList, attIdRoofBP);
		IFeatureCollection<IFeature> roofLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableRoof, clauseRoof);
		IFeatureCollection<RoofSurface> roofImport = BasicLoader.importRoof(roofLoad);

		// On se sert des toits importés pour obtenir dans une liste les
		// identifiants des toits
		List<Integer> idRoofList = new ArrayList<Integer>();
		for (RoofSurface currentR : roofImport) {
			int id = currentR.getId();
			if (!idRoofList.contains(id)) {
				idRoofList.add(id);
			}
		}

		// On se sert de cette liste d'identifiants de toits pour charger et
		// importer les faitages depuis la base de données
		String clauseRoofing = generatedWhereClauseList(idRoofList, attIdRoofingR);
		IFeatureCollection<IFeature> roofingLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
				user, pw, tableRoofing, clauseRoofing);
		IFeatureCollection<RoofSurface> roofingImport = BasicLoader.importRoofing(roofingLoad);

		// On se sert de cette liste d'identifiants de toits pour charger et
		// importer les gouttières depuis la base de données
		String clauseGutter = generatedWhereClauseList(idRoofList, attIdGutterR);
		IFeatureCollection<IFeature> gutterLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
				user, pw, tableGutter, clauseGutter);
		IFeatureCollection<RoofSurface> gutterImport = BasicLoader.importGutter(gutterLoad);

		// On se sert de cette liste d'identifiants de toits pour charger et
		// importer les pignons depuis la base de données
		String clauseGable = generatedWhereClauseList(idRoofList, attIdGableR);
		IFeatureCollection<IFeature> gableLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
				user, pw, tableGable, clauseGable);
		IFeatureCollection<RoofSurface> gableImport = BasicLoader.importGable(gableLoad);

		System.out.println("\n" + "----- End of Load From Collection BPU -----");

		// On lance la création des relations entres tous les objets chargés et
		// on
		// retourne le résultat
		return AutomaticAssignment.assignment(env, pluImport, zuImport, subParImport, scbImport, roadImport, axisImport,
				cadParImport, bpuImport, bpImport, buildImport, wallImport, roofImport, roofingImport, gutterImport,
				gableImport);

	}

	/**
	 * Génère une clause SQL à partir d'un Integer et du nom d'un attribut
	 * 
	 * @param integ
	 *            la valeur de l'attribut à chercher
	 * @param nomAtt
	 *            la nom de l'attribut à chercher
	 * @return une clause au format String
	 */
	public static String generatedWhereClauseInt(Integer integ, String nomAtt) {

		String clauseOut = "";
		String idStr = integ.toString();
		clauseOut = clauseOut.concat(nomAtt).concat(" = '").concat(idStr).concat("'");

		return clauseOut;

	}

	/**
	 * Génère une clause SQL à partir d'une chaine de caractère et d'un attribut
	 * 
	 * @param str
	 *            la valeur de l'attribut à chercher
	 * @param nomAtt
	 *            la nom de l'attribut à chercher
	 * @return une clause au format String
	 */
	public static String generatedWhereClauseString(String str, String nomAtt) {

		String clauseOut = "";
		clauseOut = clauseOut.concat(nomAtt).concat(" = '").concat(str).concat("'");

		return clauseOut;

	}

	/**
	 * Génère une clause SQL à partir d'une liste et d'un attribut
	 * 
	 * @param liste
	 *            la liste contenant les valeurs à chercher
	 * @param nomAtt
	 *            la nom de l'attribut à chercher
	 * @return une clause au format String
	 */
	public static String generatedWhereClauseList(List<?> liste, String nomAtt) {

		String clauseOut = "";

		for (int i = 0; i < liste.size(); i++) {
			String idStr = liste.get(i).toString();
			if (i == 0) {
				clauseOut = clauseOut.concat(nomAtt).concat(" = '").concat(idStr).concat("'");
			} else {
				clauseOut = clauseOut.concat(" OR ").concat(nomAtt).concat(" = '").concat(idStr).concat("'");
			}
		}

		return clauseOut;

	}

	/**
	 * Génère une clause SQL. Spécialement créée pour extraire des buildings
	 * parts en fonction d'un id de version et d'un id BPU
	 * 
	 * @param liste
	 *            La liste des id de sous-parcelles
	 * @param nomAttSP
	 *            Nom de l'attribut pour les sous-parcelles
	 * @param idVersion
	 *            Valeur de l'id version
	 * @param nomAttVer
	 *            Nom de l'attribut pour les version
	 * @param nomAttId
	 *            Nom de l'attribut pour les id
	 * @param host
	 * @param port
	 * @param database
	 * @param user
	 * @param pw
	 * @return une clause au format String
	 * @throws Exception
	 */
	public static String generatedWhereClauseListBuilding(List<?> liste, String nomAttSP, Integer idVersion,
			String nomAttVer, String nomAttId, String host, String port, String database, String user, String pw)
					throws Exception {

		List<Integer> listIdDeleted = LoaderVersion.searchForIdToDeleteInVersion(host, port, database, user, pw,
				idVersion);

		String clauseOut = "";
		String whereClause = "";

		for (int i = 0; i < liste.size(); i++) {
			String idStr = liste.get(i).toString();
			if (i == 0) {
				if (idVersion == -1) {
					clauseOut = clauseOut.concat(nomAttSP).concat(" = '").concat(idStr).concat("'").concat(" AND ")
							.concat(nomAttVer).concat(" IS NULL ");
				} else {
					whereClause = nomAttVer + " = " + Integer.toString(idVersion) + " OR ( " + nomAttVer + " IS NULL";
					for (Integer valId : listIdDeleted) {
						whereClause = whereClause + " AND " + nomAttId + " != " + Integer.toString(valId);
					}
					whereClause = whereClause + " )";
					clauseOut = clauseOut.concat(nomAttSP).concat(" = '").concat(idStr).concat("'").concat(" AND (")
							.concat(whereClause).concat(")");
				}
			} else {
				if (idVersion == -1) {
					clauseOut = clauseOut.concat(" OR ").concat(nomAttSP).concat(" = '").concat(idStr).concat("'")
							.concat(" AND ").concat(nomAttVer).concat(" IS NULL ");
				} else {
					whereClause = nomAttVer + " = " + Integer.toString(idVersion) + " OR ( " + nomAttVer + " IS NULL";
					for (Integer valId : listIdDeleted) {
						whereClause = whereClause + " AND " + nomAttId + " != " + Integer.toString(valId);
					}
					whereClause = whereClause + " )";
					clauseOut = clauseOut.concat(" OR ").concat(nomAttSP).concat(" = '").concat(idStr).concat("'")
							.concat(" AND (").concat(whereClause).concat(")");
				}
			}
		}

		return clauseOut;

	}

}
