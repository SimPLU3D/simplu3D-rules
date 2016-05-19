package fr.ign.cogit.simplu3d.io.structDatabase.postgis.loader;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
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
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundarySide;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundaryType;

public class BasicLoader {

	/**
	 * Permet de charger au sein d'un objet PLU les données contenues dans une
	 * IFeatureCollection provenant de l'import de données depuis une base de
	 * données
	 * 
	 * @param featPLU
	 * @return
	 */
	public static UrbaDocument importPLU(IFeatureCollection<IFeature> featPLU) {

		UrbaDocument pluOut = new UrbaDocument();

		// Some date format we use here :
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 1995-12-25
		SimpleDateFormat sdfYYYY = new SimpleDateFormat("yyyy"); // 1995

		for (IFeature feat : featPLU) {

			Object attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_ID_URBA);

			if (attPLU != null) {
				pluOut.setIdUrba(attPLU.toString());
			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_TYPE_DOC);

			if (attPLU != null) {
				pluOut.setTypeDoc(attPLU.toString());
			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_DATE_APPRO);

			if (attPLU != null) {
				String dateAp = attPLU.toString();
				java.util.Date dateAppro = new java.util.Date();

				try {
					// System.out.println("You try...");
					dateAppro = sdf.parse(dateAp);
				} catch (ParseException e) {
					// System.out.println("... And you fail for dateAppro");

					e.printStackTrace();
				}

				// System.out.println("... And it's a succes for dateAppro");

				pluOut.setDateAppro(dateAppro);

			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_DATE_FIN);

			if (attPLU != null) {
				String dateFi = attPLU.toString();
				java.util.Date dateFin = new java.util.Date();

				try {
					// System.out.println("You try...");
					dateFin = sdf.parse(dateFi);
				} catch (ParseException e) {
					// System.out.println("... And you fail for dateFin");

					e.printStackTrace();
				}

				// System.out.println("... And it's a succes for dateFin");

				pluOut.setDateFin(dateFin);

			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_INTERCO);

			if (attPLU != null) {
				pluOut.setInterCo(attPLU.toString());
			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_SIREN);

			if (attPLU != null) {
				pluOut.setSiren(attPLU.toString());
			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_ETAT);

			if (attPLU != null) {
				pluOut.setEtat(attPLU.toString());
			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_NOM_REG);

			if (attPLU != null) {
				pluOut.setNomReg(attPLU.toString());
			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_URL_REG);

			if (attPLU != null) {
				pluOut.setUrlReg(attPLU.toString());
			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_NOM_PLAN);

			if (attPLU != null) {
				pluOut.setNomPlan(attPLU.toString());
			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_URL_PLAN);

			if (attPLU != null) {
				pluOut.setUrlPlan(attPLU.toString());
			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_SITE);

			if (attPLU != null) {
				pluOut.setSiteWeb(attPLU.toString());
			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_TYPE_REF);

			if (attPLU != null) {
				pluOut.setTypeRef(attPLU.toString());
			}

			attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_DATE_REF);

			if (attPLU != null) {
				String dateRe = attPLU.toString();
				java.util.Date dateRef = new java.util.Date();

				try {
					// System.out.println("You try...");
					dateRef = sdfYYYY.parse(dateRe);
				} catch (ParseException e) {
					// System.out.println("... And you fail for dateRef");

					e.printStackTrace();
				}

				// System.out.println("... And it's a succes for dateRef");

				pluOut.setDateRef(dateRef);

			}

		}

		return pluOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection<UrbaZone> les données
	 * contenues dans une IFeatureCollection provenant de l'import de données
	 * depuis une base de données
	 * 
	 * @param featZone
	 * @return
	 */
	public static IFeatureCollection<UrbaZone> importZoneUrba(IFeatureCollection<IFeature> featZone) {

		IFeatureCollection<UrbaZone> featZoneOut = new FT_FeatureCollection<>();

		// Some date format we use here :
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 1995-12-25

		for (IFeature feat : featZone) {

			UrbaZone urbaZoneOut = new UrbaZone(feat.getGeom());

			Object attZU = feat.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_ID);

			String objStr = attZU.toString();
			int objInt = Integer.parseInt(objStr);

			if (attZU != null) {
				urbaZoneOut.setId(objInt);
			}

			attZU = feat.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_LIBELLE);

			if (attZU != null) {
				urbaZoneOut.setLibelle(attZU.toString());
			}

			attZU = feat.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_LIBELONG);

			if (attZU != null) {
				urbaZoneOut.setLibelong(attZU.toString());
			}

			attZU = feat.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_TYPEZONE);

			if (attZU != null) {
				urbaZoneOut.setTypeZone(attZU.toString());
			}

			attZU = feat.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_DESTDOMI);

			if (attZU != null) {
				urbaZoneOut.setDestdomi(attZU.toString());
			}

			attZU = feat.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_NOMFIC);

			if (attZU != null) {
				urbaZoneOut.setNomFic(attZU.toString());
			}

			attZU = feat.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_URLFIC);

			if (attZU != null) {
				urbaZoneOut.setUrlFic(attZU.toString());
			}

			attZU = feat.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_INSEE);

			if (attZU != null) {
				urbaZoneOut.setInsee(attZU.toString());
			}

			attZU = feat.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_DATE_APPRO);

			if (attZU != null) {
				String dateDeb = attZU.toString();
				java.util.Date dateDebut = new java.util.Date();

				try {
					// System.out.println("You try...");
					dateDebut = sdf.parse(dateDeb);
				} catch (ParseException e) {
					// System.out.println("... And you fail for dateDebut");

					e.printStackTrace();
				}

				// System.out.println("... And it's a succes for dateDebut");

				urbaZoneOut.setDateDeb(dateDebut);

			}

			attZU = feat.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_DATE_VALID);

			if (attZU != null) {
				String dateFi = attZU.toString();
				java.util.Date dateFin = new java.util.Date();

				try {
					// System.out.println("You try...");
					dateFin = sdf.parse(dateFi);
				} catch (ParseException e) {
					// System.out.println("... And you fail for dateFin");

					e.printStackTrace();
				}

				// System.out.println("... And it's a succes for dateFin");

				urbaZoneOut.setDateFin(dateFin);

			}

			attZU = feat.getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_ID_PLU);

			if (attZU != null) {
				urbaZoneOut.setIdPLU(attZU.toString());
			}

			featZoneOut.add(urbaZoneOut);

		}

		return featZoneOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection<SubParcel> les données
	 * contenues dans une IFeatureCollection provenant de l'import de données
	 * depuis une base de données
	 * 
	 * @param featSubParcel
	 * @return
	 */
	public static IFeatureCollection<SubParcel> importSubParcel(IFeatureCollection<IFeature> featSubParcel) {

		IFeatureCollection<SubParcel> featSubParcelOut = new FT_FeatureCollection<>();

		for (IFeature feat : featSubParcel) {

			SubParcel sp = new SubParcel(feat.getGeom());

			Object attSP = feat.getAttribute(ParametersInstructionPG.ATT_SUB_PARCEL_ID);

			String objStr = attSP.toString();
			int objInt = Integer.parseInt(objStr);

			if (attSP != null) {
				sp.setId(objInt);
			}

			attSP = feat.getAttribute(ParametersInstructionPG.ATT_SUB_PARCEL_ID_ZU);

			objStr = attSP.toString();
			objInt = Integer.parseInt(objStr);

			if (attSP != null) {
				sp.setIdZoneUrba(objInt);
			}

			attSP = feat.getAttribute(ParametersInstructionPG.ATT_SUB_PARCEL_ID_CADPAR);

			objStr = attSP.toString();
			objInt = Integer.parseInt(objStr);

			if (attSP != null) {
				sp.setIdCadPar(objInt);
			}

			attSP = feat.getAttribute(ParametersInstructionPG.ATT_SUB_PARCEL_AVG_SLOPE);

			objStr = attSP.toString();
			Double objDbl = Double.parseDouble(objStr);

			if (attSP != null) {
				sp.setAvgSlope(objDbl);
			}

			attSP = feat.getAttribute(ParametersInstructionPG.ATT_SUB_PARCEL_SURF);

			objStr = attSP.toString();
			objDbl = Double.parseDouble(objStr);

			if (attSP != null) {
				sp.setArea(objDbl);
			}

			featSubParcelOut.add(sp);

		}

		return featSubParcelOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection<Road> les données
	 * contenues dans une IFeatureCollection provenant de l'import de données
	 * depuis une base de données
	 * 
	 * @param featRoad
	 * @return
	 */
	public static IFeatureCollection<Road> importRoad(IFeatureCollection<IFeature> featRoad) {

		IFeatureCollection<Road> featRoadOut = new FT_FeatureCollection<>();

		for (IFeature feat : featRoad) {

			IMultiSurface<IOrientableSurface> ms = FromGeomToSurface.convertMSGeom(feat.getGeom());

			Road road = new Road(ms);

			Object attRoad = feat.getAttribute(ParametersInstructionPG.ATT_ROAD_ID);

			if (attRoad != null) {
				String objStr = attRoad.toString();
				int objInt = Integer.parseInt(objStr);

				road.setId(objInt);

			}

			attRoad = feat.getAttribute(ParametersInstructionPG.ATT_ROAD_NOM);

			if (attRoad != null) {

				String objStr = attRoad.toString();

				road.setName(objStr);

			}

			attRoad = feat.getAttribute(ParametersInstructionPG.ATT_ROAD_TYPE);

			if (attRoad != null) {

				String objStr = attRoad.toString();

				road.setType(objStr);

			}

			attRoad = feat.getAttribute(ParametersInstructionPG.ATT_ROAD_LARGEUR);

			if (attRoad != null) {

				String objStr = attRoad.toString();
				Double objDbl = Double.parseDouble(objStr);

				road.setWidth(objDbl);
			}

			featRoadOut.add(road);

		}

		return featRoadOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection<Road> les données
	 * contenues dans une IFeatureCollection provenant de l'import de données
	 * depuis une base de données
	 * 
	 * @param featAxis
	 * @return
	 */
	public static IFeatureCollection<Road> importAxis(IFeatureCollection<IFeature> featAxis) {

		IFeatureCollection<Road> featAxisOut = new FT_FeatureCollection<>();

		for (IFeature feat : featAxis) {

			Road axis = new Road();

			IGeometry geom = feat.getGeom();
			IMultiCurve<ILineString> axe = null;

			if (geom instanceof ILineString) {

				ILineString c = (ILineString) geom;
				axe = new GM_MultiCurve<ILineString>();
				axe.add(c);

			} else if (geom instanceof IMultiCurve<?>) {

				axe = (IMultiCurve<ILineString>) geom;

			}

			axis.setAxe(axe);

			Object attAxis = feat.getAttribute(ParametersInstructionPG.ATT_AXE_ID);

			if (attAxis != null) {
				String objStr = attAxis.toString();
				int objInt = Integer.parseInt(objStr);

				axis.setId(objInt);

			}

			attAxis = feat.getAttribute(ParametersInstructionPG.ATT_AXE_ID_ROAD);

			if (attAxis != null) {
				String objStr = attAxis.toString();
				int objInt = Integer.parseInt(objStr);

				axis.setIdRoad(objInt);

			}

			featAxisOut.add(axis);

		}

		return featAxisOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection
	 * <SpecificCadastralBoundary> les données contenues dans une
	 * IFeatureCollection provenant de l'import de données depuis une base de
	 * données
	 * 
	 * @param featSCB
	 * @return
	 */
	public static IFeatureCollection<SpecificCadastralBoundary> importSpecificCadBound(
			IFeatureCollection<IFeature> featSCB) {

		IFeatureCollection<SpecificCadastralBoundary> featSCBOut = new FT_FeatureCollection<>();

		for (IFeature feat : featSCB) {
			SpecificCadastralBoundary scb = new SpecificCadastralBoundary(feat.getGeom());

			Object attSCB = feat.getAttribute(ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID);

			if (attSCB != null) {
				String objStr = attSCB.toString();
				int objInt = Integer.parseInt(objStr);

				scb.setId(objInt);

			}

			attSCB = feat.getAttribute(ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_TYPE);

			if (attSCB != null) {
				String objStr = attSCB.toString();
				int objInt = Integer.parseInt(objStr);

				scb.setType(SpecificCadastralBoundaryType.getTypeFromInt(objInt));

			}

			attSCB = feat.getAttribute(ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_SIDE);

			if (attSCB != null) {
				String objStr = attSCB.toString();
				int objInt = Integer.parseInt(objStr);

				scb.setSide(SpecificCadastralBoundarySide.getTypeFromInt(objInt));

			}

			attSCB = feat.getAttribute(ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID_SUB_PAR);

			if (attSCB != null) {
				String objStr = attSCB.toString();
				int objInt = Integer.parseInt(objStr);

				scb.setIdSubPar(objInt);

			}

			attSCB = feat.getAttribute(ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID_ADJ);

			if (attSCB != null) {
				String objStr = attSCB.toString();
				int objInt = Integer.parseInt(objStr);

				scb.setIdAdj(objInt);

			}

			attSCB = feat.getAttribute(ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_TABLE_REF);

			if (attSCB != null) {
				String objStr = attSCB.toString();

				scb.setTableRef(objStr);

			}

			featSCBOut.add(scb);

		}

		return featSCBOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection<CadastralParcel> les
	 * données contenues dans une IFeatureCollection provenant de l'import de
	 * données depuis une base de données
	 * 
	 * @param featParcel
	 * @return
	 */
	public static IFeatureCollection<CadastralParcel> importCadParcel(IFeatureCollection<IFeature> featParcel) {

		IFeatureCollection<CadastralParcel> featParcelOut = new FT_FeatureCollection<>();

		for (IFeature feat : featParcel) {

			IMultiSurface<IOrientableSurface> ms = FromGeomToSurface.convertMSGeom(feat.getGeom());

			CadastralParcel cp = new CadastralParcel(ms);

			Object attCP = feat.getAttribute(ParametersInstructionPG.ATT_CAD_PARCEL_ID);

			String objStr = attCP.toString();
			int objInt = Integer.parseInt(objStr);

			if (attCP != null) {
				cp.setId(objInt);
			}

			attCP = feat.getAttribute(ParametersInstructionPG.ATT_CAD_PARCEL_ID_BPU);

			objStr = attCP.toString();
			objInt = Integer.parseInt(objStr);

			if (attCP != null) {
				cp.setId(objInt);
			}

			attCP = feat.getAttribute(ParametersInstructionPG.ATT_CAD_PARCEL_NUM);

			objStr = attCP.toString();
			objInt = Integer.parseInt(objStr);

			attCP = feat.getAttribute(ParametersInstructionPG.ATT_CAD_PARCEL_SURF);

			objStr = attCP.toString();
			Double objDbl = Double.parseDouble(objStr);

			if (attCP != null) {
				cp.setArea(objDbl);
			}

			featParcelOut.add(cp);

		}

		return featParcelOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection<BasicPropertyUnit> les
	 * données contenues dans une IFeatureCollection provenant de l'import de
	 * données depuis une base de données
	 * 
	 * @param featBPU
	 * @return
	 */
	public static IFeatureCollection<BasicPropertyUnit> importBasicPropUnit(IFeatureCollection<IFeature> featBPU) {

		IFeatureCollection<BasicPropertyUnit> featBPUOut = new FT_FeatureCollection<>();

		for (IFeature feat : featBPU) {

			BasicPropertyUnit bpu = new BasicPropertyUnit();
			bpu.setGeom(feat.getGeom());

			Object attBPU = feat.getAttribute(ParametersInstructionPG.ATT_BPU_ID);

			if (attBPU != null) {
				String objStr = attBPU.toString();
				int objInt = Integer.parseInt(objStr);

				bpu.setId(objInt);

			}

			featBPUOut.add(bpu);

		}

		return featBPUOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection<BuildingPart> les
	 * données contenues dans une IFeatureCollection provenant de l'import de
	 * données depuis une base de données
	 * 
	 * @param featBuildingPart
	 * @return
	 */
	public static IFeatureCollection<BuildingPart> importBuildPart(IFeatureCollection<IFeature> featBuildingPart) {

		IFeatureCollection<BuildingPart> featBuildingOut = new FT_FeatureCollection<>();

		for (IFeature feat : featBuildingPart) {
			BuildingPart bp = new BuildingPart(feat.getGeom());

			Object attBuildPart = feat.getAttribute(ParametersInstructionPG.ATT_BUILDING_PART_ID);

			if (attBuildPart != null) {
				String objStr = attBuildPart.toString();
				int objInt = Integer.parseInt(objStr);

				bp.setId(objInt);

			}

			attBuildPart = feat.getAttribute(ParametersInstructionPG.ATT_BUILDING_PART_ID_BUILD);

			if (attBuildPart != null) {
				String objStr = attBuildPart.toString();
				int objInt = Integer.parseInt(objStr);
				bp.setIdBuilding(objInt);
			}

			attBuildPart = feat.getAttribute(ParametersInstructionPG.ATT_BUILDING_PART_ID_SUBPAR);

			if (attBuildPart != null) {
				String objStr = attBuildPart.toString();
				int objInt = Integer.parseInt(objStr);
				bp.setIdSubPar(objInt);
			}

			attBuildPart = feat.getAttribute(ParametersInstructionPG.ATT_BUILDING_PART_ID_VERSION);

			if (attBuildPart != null) {
				String objStr = attBuildPart.toString();
				int objInt = Integer.parseInt(objStr);
				bp.setIdVersion(objInt);
			} else {
				bp.setIdVersion(-1);
			}

			featBuildingOut.add(bp);

		}

		return featBuildingOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection<Building> les données
	 * contenues dans une IFeatureCollection provenant de l'import de données
	 * depuis une base de données
	 * 
	 * @param featBuilding
	 * @return
	 */
	public static IFeatureCollection<Building> importBuilding(IFeatureCollection<IFeature> featBuilding) {

		IFeatureCollection<Building> featBuildingOut = new FT_FeatureCollection<>();

		for (IFeature feat : featBuilding) {

			Building build = new Building();

			Object attBuild = feat.getAttribute(ParametersInstructionPG.ATT_BUILDING_ID);

			if (attBuild != null) {

				String objStr = attBuild.toString();

				int objInt = Integer.parseInt(objStr);

				build.setId(objInt);

			}

			featBuildingOut.add(build);

		}

		return featBuildingOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection
	 * <SpecificWallSurface> les données contenues dans une IFeatureCollection
	 * provenant de l'import de données depuis une base de données
	 * 
	 * @param featWall
	 * @return
	 */
	public static IFeatureCollection<SpecificWallSurface> importWall(IFeatureCollection<IFeature> featWall) {

		IFeatureCollection<SpecificWallSurface> featWallOut = new FT_FeatureCollection<>();

		for (IFeature feat : featWall) {

			IGeometry geom = feat.getGeom();

			IMultiSurface<IOrientableSurface> lOS = FromGeomToSurface.convertMSGeom(geom);

			SpecificWallSurface sW = new SpecificWallSurface();

			IMultiSurface<IOrientableSurface> ims = new GM_MultiSurface<>();
			ims.addAll(lOS);
			sW.setLod2MultiSurface(ims);
			sW.setGeom(ims);

			Object attWall = feat.getAttribute(ParametersInstructionPG.ATT_WALL_SURFACE_ID);

			if (attWall != null) {

				String objStr = attWall.toString();

				int objInt = Integer.parseInt(objStr);

				sW.setId(objInt);

			}

			attWall = feat.getAttribute(ParametersInstructionPG.ATT_WALL_SURFACE_ID_BUILDP);

			if (attWall != null) {

				String objStr = attWall.toString();

				int objInt = Integer.parseInt(objStr);

				sW.setIdBuildPart(objInt);

			}

			featWallOut.add(sW);

		}

		return featWallOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection<RoofSurface> les
	 * données contenues dans une IFeatureCollection provenant de l'import de
	 * données depuis une base de données
	 * 
	 * @param featRoof
	 * @return
	 */
	public static IFeatureCollection<RoofSurface> importRoof(IFeatureCollection<IFeature> featRoof) {

		IFeatureCollection<RoofSurface> featRoofOut = new FT_FeatureCollection<>();

		for (IFeature feat : featRoof) {

			IGeometry geom = feat.getGeom();

			IMultiSurface<IOrientableSurface> lOS = FromGeomToSurface.convertMSGeom(geom);

			RoofSurface rS = new RoofSurface();

			IMultiSurface<IOrientableSurface> ims = new GM_MultiSurface<>();
			ims.addAll(lOS);
			rS.setLod2MultiSurface(ims);
			rS.setGeom(ims);

			Object attRoof = feat.getAttribute(ParametersInstructionPG.ATT_ROOF_ID);

			if (attRoof != null) {

				String objStr = attRoof.toString();

				int objInt = Integer.parseInt(objStr);

				rS.setId(objInt);

			}

			attRoof = feat.getAttribute(ParametersInstructionPG.ATT_ROOF_ID_BUILDPART);

			if (attRoof != null) {

				String objStr = attRoof.toString();

				int objInt = Integer.parseInt(objStr);

				rS.setIdBuildPart(objInt);

			}

			attRoof = feat.getAttribute(ParametersInstructionPG.ATT_ROOF_ANGLE_MIN);

			if (attRoof != null) {

				String objStr = attRoof.toString();

				Double objDb = Double.parseDouble(objStr);

				rS.setAngleMin(objDb);

			}

			attRoof = feat.getAttribute(ParametersInstructionPG.ATT_ROOF_ANGLE_MAX);

			if (attRoof != null) {

				String objStr = attRoof.toString();

				Double objDb = Double.parseDouble(objStr);

				rS.setAngleMax(objDb);

			}

			featRoofOut.add(rS);

		}

		return featRoofOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection<RoofSurface> les
	 * données contenues dans une IFeatureCollection provenant de l'import de
	 * données depuis une base de données
	 * 
	 * @param featRoof
	 * @return
	 */
	public static IFeatureCollection<RoofSurface> importRoofing(IFeatureCollection<IFeature> featRoofing) {

		IFeatureCollection<RoofSurface> featRoofingOut = new FT_FeatureCollection<>();

		for (IFeature feat : featRoofing) {

			RoofSurface roofing = new RoofSurface();
			IGeometry geom = feat.getGeom();
			IMultiCurve<IOrientableCurve> roofi = null;

			if (geom instanceof IOrientableCurve) {

				IOrientableCurve c = (IOrientableCurve) geom;
				roofi = new GM_MultiCurve<IOrientableCurve>();
				roofi.add(c);

			} else if (geom instanceof IMultiCurve<?>) {

				roofi = (IMultiCurve<IOrientableCurve>) geom;

			}

			roofing.setRoofing(roofi);

			Object attRoofing = feat.getAttribute(ParametersInstructionPG.ATT_ROOFING_ID);

			if (attRoofing != null) {

				String objStr = attRoofing.toString();

				int objInt = Integer.parseInt(objStr);

				roofing.setId(objInt);

			}

			attRoofing = feat.getAttribute(ParametersInstructionPG.ATT_ROOFING_ID_ROOF);

			if (attRoofing != null) {

				String objStr = attRoofing.toString();

				int objInt = Integer.parseInt(objStr);

				roofing.setId(objInt);

			}

			featRoofingOut.add(roofing);

		}

		return featRoofingOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection<RoofSurface> les
	 * données contenues dans une IFeatureCollection provenant de l'import de
	 * données depuis une base de données
	 * 
	 * @param featRoof
	 * @return
	 */
	public static IFeatureCollection<RoofSurface> importGable(IFeatureCollection<IFeature> featGable) {

		IFeatureCollection<RoofSurface> featGableOut = new FT_FeatureCollection<>();

		for (IFeature feat : featGable) {

			RoofSurface gable = new RoofSurface();
			IGeometry geom = feat.getGeom();
			IMultiCurve<IOrientableCurve> gab = null;

			if (geom instanceof IOrientableCurve) {

				IOrientableCurve c = (IOrientableCurve) geom;
				gab = new GM_MultiCurve<IOrientableCurve>();
				gab.add(c);

			} else if (geom instanceof IMultiCurve<?>) {

				gab = (IMultiCurve<IOrientableCurve>) geom;

			}

			gable.setGable(gab);

			Object attGable = feat.getAttribute(ParametersInstructionPG.ATT_GABLE_ID);

			if (attGable != null) {

				String objStr = attGable.toString();

				int objInt = Integer.parseInt(objStr);

				gable.setId(objInt);

			}

			attGable = feat.getAttribute(ParametersInstructionPG.ATT_GABLE_ID_ROOF);

			if (attGable != null) {

				String objStr = attGable.toString();

				int objInt = Integer.parseInt(objStr);

				gable.setId(objInt);

			}

			featGableOut.add(gable);

		}

		return featGableOut;

	}

	/**
	 * Permet de charger au sein d'une IFeatureCollection<RoofSurface> les
	 * données contenues dans une IFeatureCollection provenant de l'import de
	 * données depuis une base de données
	 * 
	 * @param featRoof
	 * @return
	 */
	public static IFeatureCollection<RoofSurface> importGutter(IFeatureCollection<IFeature> featGutter) {

		IFeatureCollection<RoofSurface> featGutterOut = new FT_FeatureCollection<>();

		for (IFeature feat : featGutter) {

			RoofSurface gutter = new RoofSurface();
			IGeometry geom = feat.getGeom();
			IMultiCurve<IOrientableCurve> gut = null;

			if (geom instanceof IOrientableCurve) {

				IOrientableCurve c = (IOrientableCurve) geom;
				gut = new GM_MultiCurve<IOrientableCurve>();
				gut.add(c);

			} else if (geom instanceof IMultiCurve<?>) {

				gut = (IMultiCurve<IOrientableCurve>) geom;

			}

			gutter.setGutter(gut);

			Object attGutter = feat.getAttribute(ParametersInstructionPG.ATT_GUTTER_ID);

			if (attGutter != null) {

				String objStr = attGutter.toString();

				int objInt = Integer.parseInt(objStr);

				gutter.setId(objInt);

			}

			attGutter = feat.getAttribute(ParametersInstructionPG.ATT_GUTTER_ID_ROOF);

			if (attGutter != null) {

				String objStr = attGutter.toString();

				int objInt = Integer.parseInt(objStr);

				gutter.setId(objInt);

			}

			featGutterOut.add(gutter);

		}

		return featGutterOut;

	}
	


	public static Environnement load(Integer idVersion, IFeatureCollection<IFeature> PLUColl,
			IFeatureCollection<IFeature> zoneColl, IFeatureCollection<IFeature> parcelleColl,
			IFeatureCollection<IFeature> voirieColl, IFeatureCollection<IFeature> batiColl,
			IFeatureCollection<IFeature> prescriptions) throws Exception {

		Environnement env = Environnement.getInstance();

		return load(idVersion, PLUColl, zoneColl, parcelleColl, voirieColl, batiColl,
				prescriptions, env);
	}

	public static Environnement load(Integer idVersion, IFeatureCollection<IFeature> PLUColl,
			IFeatureCollection<IFeature> zoneColl, IFeatureCollection<IFeature> parcelleColl,
			IFeatureCollection<IFeature> voirieColl, IFeatureCollection<IFeature> batiColl,
			IFeatureCollection<IFeature> prescriptions, Environnement env) throws Exception {

		// Parameters of connection to the PostGIS database
		String host = ParametersInstructionPG.host;
		String user = ParametersInstructionPG.user;
		String pw = ParametersInstructionPG.pw;
		String database = ParametersInstructionPG.database;
		String port = ParametersInstructionPG.port;

		// Name of tables in PostGIS
		String NOM_TABLE_AXE = ParametersInstructionPG.TABLE_AXE;
		String NOM_TABLE_BASIC_PROPERTY_UNIT = ParametersInstructionPG.TABLE_BASIC_PROPERTY_UNIT;
		String NOM_TABLE_BUILDING_PART = ParametersInstructionPG.TABLE_BUILDING_PART;
		String NOM_TABLE_GABLE = ParametersInstructionPG.TABLE_GABLE;
		String NOM_TABLE_GUTTER = ParametersInstructionPG.TABLE_GUTTER;
		String NOM_TABLE_ROOF = ParametersInstructionPG.TABLE_ROOF;
		String NOM_TABLE_ROOFING = ParametersInstructionPG.TABLE_ROOFING;
		String NOM_TABLE_SPECIFIC_CBOUNDARY = ParametersInstructionPG.TABLE_SPECIFIC_CBOUNDARY;
		String NOM_TABLE_SUB_PARCEL = ParametersInstructionPG.TABLE_SUB_PARCEL;
		String NOM_TABLE_WALL_SURFACE = ParametersInstructionPG.TABLE_WALL_SURFACE;

		// Here some loaders
		IFeatureCollection<IFeature> subParcelsLoad = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_SUB_PARCEL);
		IFeatureCollection<IFeature> bPULoad = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_BASIC_PROPERTY_UNIT);

		String whereClause = LoaderVersion.createWhereClauseVersion(host, port, database, user, pw, idVersion);
		IFeatureCollection<IFeature> buildingPartLoad = PostgisManager.loadGeometricTableWhereClause(host, port,
				database, user, pw, NOM_TABLE_BUILDING_PART, whereClause);

		IFeatureCollection<IFeature> scbLoad = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_SPECIFIC_CBOUNDARY);
		IFeatureCollection<IFeature> axisLoad = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_AXE);
		IFeatureCollection<IFeature> roofLoad = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_ROOF);
		IFeatureCollection<IFeature> wallLoad = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_WALL_SURFACE);
		IFeatureCollection<IFeature> roofingLoad = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_ROOFING);
		IFeatureCollection<IFeature> gableLoad = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_GABLE);
		IFeatureCollection<IFeature> gutterLoad = PostgisManager.loadGeometricTable(host, port, database, user, pw,
				NOM_TABLE_GUTTER);

		// Here some importers
		UrbaDocument pluImport = BasicLoader.importPLU(PLUColl);
		IFeatureCollection<UrbaZone> zuImport = BasicLoader.importZoneUrba(zoneColl);
		IFeatureCollection<SubParcel> subParImport = BasicLoader.importSubParcel(subParcelsLoad);
		IFeatureCollection<CadastralParcel> cadParImport = BasicLoader.importCadParcel(parcelleColl);
		IFeatureCollection<BasicPropertyUnit> bpuImport = BasicLoader.importBasicPropUnit(bPULoad);
		IFeatureCollection<BuildingPart> bpImport = BasicLoader.importBuildPart(buildingPartLoad);
		IFeatureCollection<SpecificCadastralBoundary> scbImport = BasicLoader.importSpecificCadBound(scbLoad);
		IFeatureCollection<Road> roadImport = BasicLoader.importRoad(voirieColl);
		IFeatureCollection<Road> axisImport = BasicLoader.importAxis(axisLoad);
		IFeatureCollection<RoofSurface> roofImport = BasicLoader.importRoof(roofLoad);
		IFeatureCollection<Building> buildImport = BasicLoader.importBuilding(batiColl);
		IFeatureCollection<SpecificWallSurface> wallImport = BasicLoader.importWall(wallLoad);
		IFeatureCollection<RoofSurface> roofingImport = BasicLoader.importRoofing(roofingLoad);
		IFeatureCollection<RoofSurface> gableImport = BasicLoader.importGable(gableLoad);
		IFeatureCollection<RoofSurface> gutterImport = BasicLoader.importGutter(gutterLoad);

		return AutomaticAssignment.assignment(env, pluImport, zuImport, subParImport, scbImport, roadImport, axisImport,
				cadParImport, bpuImport, bpImport, buildImport, wallImport, roofImport, roofingImport, gutterImport,
				gableImport);

	}

}
