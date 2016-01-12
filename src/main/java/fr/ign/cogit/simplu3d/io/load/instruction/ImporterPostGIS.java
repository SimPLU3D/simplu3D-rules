package fr.ign.cogit.simplu3d.io.load.instruction;

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
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.BuildingPart;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.PLU;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;

public class ImporterPostGIS {

  public static PLU importPLU(IFeatureCollection<IFeature> featPLU) {

    PLU pluOut = new PLU();

    // Some date format we use here :
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 1995-12-25
    SimpleDateFormat sdfYYYY = new SimpleDateFormat("yyyy"); // 1995

    for (IFeature feat : featPLU) {

      Object attPLU = feat
          .getAttribute(ParametersInstructionPG.ATT_DOC_URBA_ID_URBA);

      if (attPLU != null) {
        pluOut.setIdUrba(attPLU.toString());
      }

      attPLU = feat.getAttribute(ParametersInstructionPG.ATT_DOC_URBA_TYPE_DOC);

      if (attPLU != null) {
        pluOut.setTypeDoc(attPLU.toString());
      }

      attPLU = feat
          .getAttribute(ParametersInstructionPG.ATT_DOC_URBA_DATE_APPRO);

      if (attPLU != null) {
        String dateAp = attPLU.toString();
        java.util.Date dateAppro = new java.util.Date();

        try {
          // System.out.println("You try...");
          dateAppro = sdf.parse(dateAp);
        } catch (ParseException e) {
          // System.out.println("... And you fail for dateAppro");
          // TODO Auto-generated catch block
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
          // TODO Auto-generated catch block
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
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        // System.out.println("... And it's a succes for dateRef");

        pluOut.setDateRef(dateRef);

      }

    }

    return pluOut;

  }

  public static IFeatureCollection<UrbaZone> importZoneUrba(
      IFeatureCollection<IFeature> featZone) {

    IFeatureCollection<UrbaZone> featZoneOut = new FT_FeatureCollection<>();

    // Some date format we use here :
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 1995-12-25

    for (IFeature feat : featZone) {

      UrbaZone urbaZoneOut = new UrbaZone(feat.getGeom());

      Object attZU = feat
          .getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_ID);

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

      attZU = feat
          .getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_DATE_APPRO);

      if (attZU != null) {
        String dateDeb = attZU.toString();
        java.util.Date dateDebut = new java.util.Date();

        try {
          // System.out.println("You try...");
          dateDebut = sdf.parse(dateDeb);
        } catch (ParseException e) {
          // System.out.println("... And you fail for dateDebut");
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        // System.out.println("... And it's a succes for dateDebut");

        urbaZoneOut.setDateDeb(dateDebut);

      }

      attZU = feat
          .getAttribute(ParametersInstructionPG.ATT_ZONE_URBA_DATE_VALID);

      if (attZU != null) {
        String dateFi = attZU.toString();
        java.util.Date dateFin = new java.util.Date();

        try {
          // System.out.println("You try...");
          dateFin = sdf.parse(dateFi);
        } catch (ParseException e) {
          // System.out.println("... And you fail for dateFin");
          // TODO Auto-generated catch block
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

  public static IFeatureCollection<SubParcel> importSubParcel(
      IFeatureCollection<IFeature> featSubParcel) {

    IFeatureCollection<SubParcel> featSubParcelOut = new FT_FeatureCollection<>();

    for (IFeature feat : featSubParcel) {

      SubParcel sp = new SubParcel(feat.getGeom());

      Object attSP = feat
          .getAttribute(ParametersInstructionPG.ATT_SUB_PARCEL_ID);

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

      attSP = feat
          .getAttribute(ParametersInstructionPG.ATT_SUB_PARCEL_ID_CADPAR);

      objStr = attSP.toString();
      objInt = Integer.parseInt(objStr);

      if (attSP != null) {
        sp.setIdCadPar(objInt);
      }

      attSP = feat
          .getAttribute(ParametersInstructionPG.ATT_SUB_PARCEL_AVG_SLOPE);

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

  public static IFeatureCollection<Road> importRoad(
      IFeatureCollection<IFeature> featRoad) {

    IFeatureCollection<Road> featRoadOut = new FT_FeatureCollection<>();

    for (IFeature feat : featRoad) {

      IMultiSurface<IOrientableSurface> ms = FromGeomToSurface
          .convertMSGeom(feat.getGeom());

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

  public static IFeatureCollection<Road> importAxis(
      IFeatureCollection<IFeature> featAxis) {

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

  public static IFeatureCollection<SpecificCadastralBoundary> importSpecificCadBound(
      IFeatureCollection<IFeature> featSCB) {

    IFeatureCollection<SpecificCadastralBoundary> featSCBOut = new FT_FeatureCollection<>();

    for (IFeature feat : featSCB) {
      SpecificCadastralBoundary scb = new SpecificCadastralBoundary(
          feat.getGeom());

      Object attSCB = feat
          .getAttribute(ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID);

      if (attSCB != null) {
        String objStr = attSCB.toString();
        int objInt = Integer.parseInt(objStr);

        scb.setId(objInt);

      }

      attSCB = feat
          .getAttribute(ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_TYPE);

      if (attSCB != null) {
        String objStr = attSCB.toString();
        int objInt = Integer.parseInt(objStr);

        scb.setType(objInt);

      }

      attSCB = feat
          .getAttribute(ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_SIDE);

      if (attSCB != null) {
        String objStr = attSCB.toString();
        int objInt = Integer.parseInt(objStr);

        scb.setSide(objInt);

      }

      attSCB = feat
          .getAttribute(ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID_SUB_PAR);

      if (attSCB != null) {
        String objStr = attSCB.toString();
        int objInt = Integer.parseInt(objStr);

        scb.setIdSubPar(objInt);

      }

      attSCB = feat
          .getAttribute(ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_ID_ADJ);

      if (attSCB != null) {
        String objStr = attSCB.toString();
        int objInt = Integer.parseInt(objStr);

        scb.setIdAdj(objInt);

      }

      attSCB = feat
          .getAttribute(ParametersInstructionPG.ATT_SPECIFIC_CBOUNDARY_TABLE_REF);

      if (attSCB != null) {
        String objStr = attSCB.toString();

        scb.setTableRef(objStr);

      }

      featSCBOut.add(scb);

    }

    return featSCBOut;

  }

  public static IFeatureCollection<CadastralParcel> importCadParcel(
      IFeatureCollection<IFeature> featParcel) {

    IFeatureCollection<CadastralParcel> featParcelOut = new FT_FeatureCollection<>();

    for (IFeature feat : featParcel) {

      IMultiSurface<IOrientableSurface> ms = FromGeomToSurface
          .convertMSGeom(feat.getGeom());

      CadastralParcel cp = new CadastralParcel(ms);

      Object attCP = feat
          .getAttribute(ParametersInstructionPG.ATT_CAD_PARCEL_ID);

      String objStr = attCP.toString();
      int objInt = Integer.parseInt(objStr);

      if (attCP != null) {
        cp.setId(objInt);
      }

      attCP = feat.getAttribute(ParametersInstructionPG.ATT_CAD_PARCEL_ID_BPU);

      objStr = attCP.toString();
      objInt = Integer.parseInt(objStr);

      if (attCP != null) {
        cp.setIdBPU(objInt);
      }

      attCP = feat.getAttribute(ParametersInstructionPG.ATT_CAD_PARCEL_NUM);

      objStr = attCP.toString();
      objInt = Integer.parseInt(objStr);

      if (attCP != null) {
        cp.setNum(objInt);
      }

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

  public static IFeatureCollection<BasicPropertyUnit> importBasicPropUnit(
      IFeatureCollection<IFeature> featBPU) {

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

  public static IFeatureCollection<BuildingPart> importBuildPart(
      IFeatureCollection<IFeature> featBuildingPart) {

    IFeatureCollection<BuildingPart> featBuildingOut = new FT_FeatureCollection<>();

    for (IFeature feat : featBuildingPart) {
      BuildingPart bp = new BuildingPart(feat.getGeom());

      Object attBuildPart = feat
          .getAttribute(ParametersInstructionPG.ATT_BUILDING_PART_ID);

      if (attBuildPart != null) {
        String objStr = attBuildPart.toString();
        int objInt = Integer.parseInt(objStr);

        bp.setId(objInt);

      }

      attBuildPart = feat
          .getAttribute(ParametersInstructionPG.ATT_BUILDING_PART_ID_BUILD);

      String objStr = attBuildPart.toString();
      int objInt = Integer.parseInt(objStr);

      if (attBuildPart != null) {
        bp.setIdBuilding(objInt);
      }

      attBuildPart = feat
          .getAttribute(ParametersInstructionPG.ATT_BUILDING_PART_ID_SUBPAR);

      objStr = attBuildPart.toString();
      objInt = Integer.parseInt(objStr);

      if (attBuildPart != null) {
        bp.setIdSubPar(objInt);
      }

      featBuildingOut.add(bp);

    }

    return featBuildingOut;

  }

  public static IFeatureCollection<Building> importBuilding(
      IFeatureCollection<IFeature> featBuilding) {

    IFeatureCollection<Building> featBuildingOut = new FT_FeatureCollection<>();

    for (IFeature feat : featBuilding) {

      Building build = new Building();

      Object attBuild = feat
          .getAttribute(ParametersInstructionPG.ATT_BUILDING_ID);

      if (attBuild != null) {

        String objStr = attBuild.toString();

        int objInt = Integer.parseInt(objStr);

        build.setId(objInt);

      }

      featBuildingOut.add(build);

    }

    return featBuildingOut;

  }

  public static IFeatureCollection<SpecificWallSurface> importWall(
      IFeatureCollection<IFeature> featWall) {

    IFeatureCollection<SpecificWallSurface> featWallOut = new FT_FeatureCollection<>();

    for (IFeature feat : featWall) {

      IGeometry geom = feat.getGeom();

      IMultiSurface<IOrientableSurface> lOS = FromGeomToSurface
          .convertMSGeom(geom);

      SpecificWallSurface sW = new SpecificWallSurface();

      IMultiSurface<IOrientableSurface> ims = new GM_MultiSurface<>();
      ims.addAll(lOS);
      sW.setLod2MultiSurface(ims);
      sW.setGeom(ims);

      Object attWall = feat
          .getAttribute(ParametersInstructionPG.ATT_WALL_SURFACE_ID);

      if (attWall != null) {

        String objStr = attWall.toString();

        int objInt = Integer.parseInt(objStr);

        sW.setId(objInt);

      }

      attWall = feat
          .getAttribute(ParametersInstructionPG.ATT_WALL_SURFACE_ID_BUILDP);

      if (attWall != null) {

        String objStr = attWall.toString();

        int objInt = Integer.parseInt(objStr);

        sW.setIdBuildPart(objInt);

      }

      featWallOut.add(sW);

    }

    return featWallOut;

  }

  public static IFeatureCollection<RoofSurface> importRoof(
      IFeatureCollection<IFeature> featRoof) {

    IFeatureCollection<RoofSurface> featRoofOut = new FT_FeatureCollection<>();

    for (IFeature feat : featRoof) {

      IGeometry geom = feat.getGeom();

      IMultiSurface<IOrientableSurface> lOS = FromGeomToSurface
          .convertMSGeom(geom);

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

      attRoof = feat
          .getAttribute(ParametersInstructionPG.ATT_ROOF_ID_BUILDPART);

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

  public static IFeatureCollection<RoofSurface> importRoofing(
      IFeatureCollection<IFeature> featRoofing) {

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

      Object attRoofing = feat
          .getAttribute(ParametersInstructionPG.ATT_ROOFING_ID);

      if (attRoofing != null) {

        String objStr = attRoofing.toString();

        int objInt = Integer.parseInt(objStr);

        roofing.setId(objInt);

      }

      attRoofing = feat
          .getAttribute(ParametersInstructionPG.ATT_ROOFING_ID_ROOF);

      if (attRoofing != null) {

        String objStr = attRoofing.toString();

        int objInt = Integer.parseInt(objStr);

        roofing.setIdRoof(objInt);

      }

      featRoofingOut.add(roofing);

    }

    return featRoofingOut;

  }

  public static IFeatureCollection<RoofSurface> importGable(
      IFeatureCollection<IFeature> featGable) {

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

        gable.setIdRoof(objInt);

      }

      featGableOut.add(gable);

    }

    return featGableOut;

  }

  public static IFeatureCollection<RoofSurface> importGutter(
      IFeatureCollection<IFeature> featGutter) {

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

      Object attGutter = feat
          .getAttribute(ParametersInstructionPG.ATT_GUTTER_ID);

      if (attGutter != null) {

        String objStr = attGutter.toString();

        int objInt = Integer.parseInt(objStr);

        gutter.setId(objInt);

      }

      attGutter = feat.getAttribute(ParametersInstructionPG.ATT_GUTTER_ID_ROOF);

      if (attGutter != null) {

        String objStr = attGutter.toString();

        int objInt = Integer.parseInt(objStr);

        gutter.setIdRoof(objInt);

      }

      featGutterOut.add(gutter);

    }

    return featGutterOut;

  }

}
