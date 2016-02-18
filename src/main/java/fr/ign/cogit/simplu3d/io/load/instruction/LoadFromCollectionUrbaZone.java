package fr.ign.cogit.simplu3d.io.load.instruction;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.BuildingPart;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.PLU;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;

public class LoadFromCollectionUrbaZone {

	public static Environnement load(Environnement env, Integer searchIdZU) throws Exception {

		// Parameters PostGIS database
		String host = Load.host;
		String user = Load.user;
		String pw = Load.pw;
		String database = Load.database;
		String port = Load.port;

		// Elements d'info ZU
		String tableZU = ParametersInstructionPG.TABLE_ZONE_URBA;
		String attIdZU = ParametersInstructionPG.ATT_ZONE_URBA_ID;

		String tablePLU = ParametersInstructionPG.TABLE_DOC_URBA;
		String attIdPLU = ParametersInstructionPG.ATT_DOC_URBA_ID_URBA;

		String tableSubPar = ParametersInstructionPG.TABLE_SUB_PARCEL;
		String attIdSubPar = ParametersInstructionPG.ATT_SUB_PARCEL_ID;
		String attIdSubParZU = ParametersInstructionPG.ATT_SUB_PARCEL_ID_ZU;

		String tableCadPar = ParametersInstructionPG.TABLE_CADASTRAL_PARCEL;
		String attIdCadPar = ParametersInstructionPG.ATT_CAD_PARCEL_ID;

		String tableBPU = ParametersInstructionPG.TABLE_BASIC_PROPERTY_UNIT;
		String attIdBPU = ParametersInstructionPG.ATT_BPU_ID;

		String tableSCB = ParametersInstructionPG.TABLE_SPECIFIC_CBOUNDARY;
		String attIdSCBSubPar = ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID_SUB_PAR;

		String tableRoad = ParametersInstructionPG.TABLE_ROAD;
		String attIdRoad = ParametersInstructionPG.ATT_ROAD_ID;

		String tableAxis = ParametersInstructionPG.TABLE_AXE;
		String attIdAxisRoad = ParametersInstructionPG.ATT_AXE_ID_ROAD;

		String tableBP = ParametersInstructionPG.TABLE_BUILDING_PART;
		String attIdBPSubPar = ParametersInstructionPG.ATT_BUILDING_PART_ID_SUBPAR;

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

		// Partie ZU
		String clauseZU = generatedWhereClauseInt(searchIdZU, attIdZU);
		IFeatureCollection<IFeature> zuLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableZU, clauseZU);
		IFeatureCollection<UrbaZone> zuImport = ImporterPostGIS.importZoneUrba(zuLoad);

		if (zuImport.isEmpty()) {
			System.out.println(
					"\n" + "ERREUR : La valeur '" + searchIdZU + "' n'existe pas dans la table " + tableZU + ".");
			return null;
		}

		// Partie PLU
		String idZUPLU = zuImport.get(0).getIdPLU();
		String clausePLU = generatedWhereClauseString(idZUPLU, attIdPLU);
		IFeatureCollection<IFeature> pluLoad = PostgisManager.loadNonGeometricTableWhereClause(host, port, database,
				user, pw, tablePLU, clausePLU);
		PLU pluImport = ImporterPostGIS.importPLU(pluLoad);

		// Partie SP
		String clauseSP = generatedWhereClauseInt(searchIdZU, attIdSubParZU);
		IFeatureCollection<IFeature> spLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableSubPar, clauseSP);
		IFeatureCollection<SubParcel> subParImport = ImporterPostGIS.importSubParcel(spLoad);

		// Partie CP
		List<Integer> idCadParList = new ArrayList<Integer>();

		for (SubParcel currentSP : subParImport) {
			int id = currentSP.getIdCadPar();
			if (!idCadParList.contains(id)) {
				idCadParList.add(id);
			}
		}

		String clauseCP = generatedWhereClauseList(idCadParList, attIdCadPar);
		IFeatureCollection<IFeature> cpLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableCadPar, clauseCP);
		IFeatureCollection<CadastralParcel> cadParImport = ImporterPostGIS.importCadParcel(cpLoad);

		// Partie BPU
		List<Integer> idBPUList = new ArrayList<Integer>();

		for (CadastralParcel currentCP : cadParImport) {
			int id = currentCP.getIdBPU();
			if (!idBPUList.contains(id)) {
				idBPUList.add(id);
			}
		}

		String clauseBPU = generatedWhereClauseList(idBPUList, attIdBPU);
		IFeatureCollection<IFeature> bpuLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableBPU, clauseBPU);
		IFeatureCollection<BasicPropertyUnit> bpuImport = ImporterPostGIS.importBasicPropUnit(bpuLoad);

		// Partie SCB
		List<Integer> idSubParList = new ArrayList<Integer>();

		for (SubParcel currentSP : subParImport) {
			int id = currentSP.getId();
			if (!idSubParList.contains(id)) {
				idSubParList.add(id);
			}
		}

		String clauseSCB = generatedWhereClauseList(idSubParList, attIdSCBSubPar);
		IFeatureCollection<IFeature> scbLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableSCB, clauseSCB);

		IFeatureCollection<SpecificCadastralBoundary> scbImport = ImporterPostGIS.importSpecificCadBound(scbLoad);

		// Partie Objets adjacents
		List<Integer> idSubParAdjList = new ArrayList<Integer>();
		List<Integer> idParCadAdjList = new ArrayList<Integer>();
		List<Integer> idZUAdjList = new ArrayList<Integer>();
		List<Integer> idBPUAdjList = new ArrayList<Integer>();
		List<Integer> idRoadAdjList = new ArrayList<Integer>();

		for (SpecificCadastralBoundary currentSCB : scbImport) {

			String tabRef = currentSCB.getTableRef();
			int id = currentSCB.getIdAdj();

			if (tabRef.equalsIgnoreCase("road")) {
				if (!idRoadAdjList.contains(id)) {
					idRoadAdjList.add(id);
				}
			} else if (tabRef.equalsIgnoreCase("sub_parcel")) {
				if (!idSubParAdjList.contains(id)) {
					if (!idSubParList.contains(id)) {
						idSubParAdjList.add(id);
					}
				}
			} else {
				System.out.println("Erreur, la table reference n'est pas reconnue");
			}

		}

		String clauseRoadAdj = generatedWhereClauseList(idRoadAdjList, attIdRoad);
		IFeatureCollection<IFeature> roadLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableRoad, clauseRoadAdj);
		IFeatureCollection<Road> roadImport = ImporterPostGIS.importRoad(roadLoad);

		// Condition permettant de charger les parcelles non présentes sur la Zu
		// mais adjacentes
		if (!idSubParAdjList.isEmpty()) {

			String clauseSPAdj = generatedWhereClauseList(idSubParAdjList, attIdSubPar);
			IFeatureCollection<IFeature> spAdjLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
					user, pw, tableSubPar, clauseSPAdj);
			IFeatureCollection<SubParcel> spAdjImport = ImporterPostGIS.importSubParcel(spAdjLoad);
			subParImport.addAll(spAdjImport);
			idSubParList.addAll(idSubParAdjList);

			for (SubParcel currentSPAdj : spAdjImport) {
				int id = currentSPAdj.getIdCadPar();
				if (!idParCadAdjList.contains(id)) {
					idParCadAdjList.add(id);
				}
			}

			String clauseCPAdj = generatedWhereClauseList(idParCadAdjList, attIdCadPar);
			IFeatureCollection<IFeature> cadParAdjLoad = PostgisManager.loadGeometricTableWhereClause(host, port,
					database, user, pw, tableCadPar, clauseCPAdj);
			IFeatureCollection<CadastralParcel> cadParAdjImport = ImporterPostGIS.importCadParcel(cadParAdjLoad);
			cadParImport.addAll(cadParAdjImport);

			for (SubParcel currentSPAdj : spAdjImport) {
				int id = currentSPAdj.getIdZoneUrba();
				if (!idZUAdjList.contains(id)) {
					idZUAdjList.add(id);
				}
			}

			String clauseZUAdj = generatedWhereClauseList(idZUAdjList, attIdZU);
			IFeatureCollection<IFeature> zuAdjLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
					user, pw, tableZU, clauseZUAdj);
			IFeatureCollection<UrbaZone> zuAdjImport = ImporterPostGIS.importZoneUrba(zuAdjLoad);
			zuImport.addAll(zuAdjImport);

			for (CadastralParcel currentCPAdj : cadParAdjImport) {
				int id = currentCPAdj.getIdBPU();
				if (!idBPUAdjList.contains(id)) {
					idBPUAdjList.add(id);
				}
			}

			String clauseBPUAdj = generatedWhereClauseList(idBPUAdjList, attIdBPU);
			IFeatureCollection<IFeature> bpuAdjLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
					user, pw, tableBPU, clauseBPUAdj);
			IFeatureCollection<BasicPropertyUnit> bpuAdjImport = ImporterPostGIS.importBasicPropUnit(bpuAdjLoad);
			bpuImport.addAll(bpuAdjImport);

		}

		// Partie sur les axes
		String clauseAxis = generatedWhereClauseList(idRoadAdjList, attIdAxisRoad);
		IFeatureCollection<IFeature> axisLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableAxis, clauseAxis);
		IFeatureCollection<Road> axisImport = ImporterPostGIS.importAxis(axisLoad);

		// Partie sur les building part
		String clauseBP = generatedWhereClauseList(idSubParList, attIdBPSubPar);
		IFeatureCollection<IFeature> bpLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableBP, clauseBP);
		IFeatureCollection<BuildingPart> bpImport = ImporterPostGIS.importBuildPart(bpLoad);

		// Partie sur les building
		List<Integer> idBuildList = new ArrayList<Integer>();

		for (BuildingPart currentBP : bpImport) {
			int id = currentBP.getIdBuilding();
			if (!idBuildList.contains(id)) {
				idBuildList.add(id);
			}
		}

		String clauseBuild = generatedWhereClauseList(idBuildList, attIdBuild);
		IFeatureCollection<IFeature> buildLoad = PostgisManager.loadNonGeometricTableWhereClause(host, port, database,
				user, pw, tableBuild, clauseBuild);
		IFeatureCollection<Building> buildImport = ImporterPostGIS.importBuilding(buildLoad);

		// Partie sur les murs
		List<Integer> idBuildPartList = new ArrayList<Integer>();

		for (BuildingPart currentBP : bpImport) {
			int id = currentBP.getId();
			if (!idBuildPartList.contains(id)) {
				idBuildPartList.add(id);
			}
		}

		String clauseWall = generatedWhereClauseList(idBuildPartList, attIdWallBP);
		IFeatureCollection<IFeature> wallLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableWall, clauseWall);
		IFeatureCollection<SpecificWallSurface> wallImport = ImporterPostGIS.importWall(wallLoad);

		// Partie sur les toits
		String clauseRoof = generatedWhereClauseList(idBuildPartList, attIdRoofBP);
		IFeatureCollection<IFeature> roofLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database, user,
				pw, tableRoof, clauseRoof);
		IFeatureCollection<RoofSurface> roofImport = ImporterPostGIS.importRoof(roofLoad);

		// Partie sur les roofing
		List<Integer> idRoofList = new ArrayList<Integer>();

		for (RoofSurface currentR : roofImport) {
			int id = currentR.getId();
			if (!idRoofList.contains(id)) {
				idRoofList.add(id);
			}
		}

		String clauseRoofing = generatedWhereClauseList(idRoofList, attIdRoofingR);
		IFeatureCollection<IFeature> roofingLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
				user, pw, tableRoofing, clauseRoofing);
		IFeatureCollection<RoofSurface> roofingImport = ImporterPostGIS.importRoofing(roofingLoad);

		// Partie sur les gutter
		String clauseGutter = generatedWhereClauseList(idRoofList, attIdGutterR);
		IFeatureCollection<IFeature> gutterLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
				user, pw, tableGutter, clauseGutter);

		IFeatureCollection<RoofSurface> gutterImport = ImporterPostGIS.importGutter(gutterLoad);

		// Partie sur les gable
		String clauseGable = generatedWhereClauseList(idRoofList, attIdGableR);
		IFeatureCollection<IFeature> gableLoad = PostgisManager.loadGeometricTableWhereClause(host, port, database,
				user, pw, tableGable, clauseGable);
		IFeatureCollection<RoofSurface> gableImport = ImporterPostGIS.importGable(gableLoad);

		System.out.println("\n" + "----- End of Load From Collection Urba Zone -----");

		return AutomaticAssignment.assignment(env, pluImport, zuImport, subParImport, scbImport, roadImport, axisImport,
				cadParImport, bpuImport, bpImport, buildImport, wallImport, roofImport, roofingImport, gutterImport,
				gableImport);

	}

	public static String generatedWhereClauseInt(Integer integ, String nomAtt) {

		String clauseOut = "";
		String idStr = integ.toString();
		clauseOut = clauseOut.concat(nomAtt).concat(" = '").concat(idStr).concat("'");

		return clauseOut;

	}

	public static String generatedWhereClauseString(String str, String nomAtt) {

		String clauseOut = "";
		clauseOut = clauseOut.concat(nomAtt).concat(" = '").concat(str).concat("'");

		return clauseOut;

	}

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

}
