package fr.ign.cogit.simplu3d.io.load.instruction;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.geoxygene.sig3d.semantic.AbstractDTM;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
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

public class LoadFromCollectionPostGIS {

  public static Environnement load(String folder, IFeature featPLU,
      IFeatureCollection<IFeature> zoneColl,
      IFeatureCollection<IFeature> parcelleColl,
      IFeatureCollection<IFeature> voirieColl,
      IFeatureCollection<IFeature> batiColl,
      IFeatureCollection<IFeature> prescriptions, String ruleFolder,
      AbstractDTM dtm) throws Exception {

    Environnement env = Environnement.getInstance();

    return LoadFromCollectionPostGIS
        .load(folder, featPLU, zoneColl, parcelleColl, voirieColl, batiColl,
            prescriptions, ruleFolder, dtm, env);
  }

  public static Environnement load(String folder, IFeature featPLU,
      IFeatureCollection<IFeature> zoneColl,
      IFeatureCollection<IFeature> parcelleColl,
      IFeatureCollection<IFeature> voirieColl,
      IFeatureCollection<IFeature> batiColl,
      IFeatureCollection<IFeature> prescriptions, String ruleFolder,
      AbstractDTM dtm, Environnement env) throws Exception {

    // Parameters of connection to the PostGIS database
    String host = Load.host;
    String user = Load.user;
    String pw = Load.pw;
    String database = Load.database;
    String port = Load.port;

    // Name of tables in PostGIS
    String NOM_TABLE_AXE = ParametersInstructionPG.TABLE_AXE;
    String NOM_TABLE_BASIC_PROPERTY_UNIT = ParametersInstructionPG.TABLE_BASIC_PROPERTY_UNIT;
    String NOM_TABLE_BUILDING = ParametersInstructionPG.TABLE_BUILDING;
    String NOM_TABLE_BUILDING_PART = ParametersInstructionPG.TABLE_BUILDING_PART;
    String NOM_TABLE_CADASTRAL_PARCEL = ParametersInstructionPG.TABLE_CADASTRAL_PARCEL;
    String NOM_TABLE_DOC_URBA = ParametersInstructionPG.TABLE_DOC_URBA;
    String NOM_TABLE_GABLE = ParametersInstructionPG.TABLE_GABLE;
    String NOM_TABLE_GUTTER = ParametersInstructionPG.TABLE_GUTTER;
    String NOM_TABLE_ROAD = ParametersInstructionPG.TABLE_ROAD;
    String NOM_TABLE_ROOF = ParametersInstructionPG.TABLE_ROOF;
    String NOM_TABLE_ROOFING = ParametersInstructionPG.TABLE_ROOFING;
    String NOM_TABLE_SPECIFIC_CBOUNDARY = ParametersInstructionPG.TABLE_SPECIFIC_CBOUNDARY;
    String NOM_TABLE_SUB_PARCEL = ParametersInstructionPG.TABLE_SUB_PARCEL;
    String NOM_TABLE_WALL_SURFACE = ParametersInstructionPG.TABLE_WALL_SURFACE;
    String NOM_TABLE_ZONE_URBA = ParametersInstructionPG.TABLE_ZONE_URBA;

    // Here some loaders
    IFeatureCollection<IFeature> cadParcelsLoad = PostgisManager
        .loadGeometricTable(host, port, database, user, pw,
            NOM_TABLE_CADASTRAL_PARCEL);
    IFeatureCollection<IFeature> subParcelsLoad = PostgisManager
        .loadGeometricTable(host, port, database, user, pw,
            NOM_TABLE_SUB_PARCEL);
    IFeatureCollection<IFeature> bPULoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_BASIC_PROPERTY_UNIT);
    IFeatureCollection<IFeature> buildingPartLoad = PostgisManager
        .loadGeometricTable(host, port, database, user, pw,
            NOM_TABLE_BUILDING_PART);
    IFeatureCollection<IFeature> scbLoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_SPECIFIC_CBOUNDARY);
    IFeatureCollection<IFeature> roadLoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_ROAD);
    IFeatureCollection<IFeature> axisLoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_AXE);
    IFeatureCollection<IFeature> roofLoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_ROOF);
    IFeatureCollection<IFeature> buildingLoad = PostgisManager
        .loadNonGeometricTable(host, port, database, user, pw,
            NOM_TABLE_BUILDING);
    IFeatureCollection<IFeature> wallLoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_WALL_SURFACE);
    IFeatureCollection<IFeature> roofingLoad = PostgisManager
        .loadGeometricTable(host, port, database, user, pw, NOM_TABLE_ROOFING);
    IFeatureCollection<IFeature> gableLoad = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_GABLE);
    IFeatureCollection<IFeature> gutterLoad = PostgisManager
        .loadGeometricTable(host, port, database, user, pw, NOM_TABLE_GUTTER);

    // Here some importers
    IFeatureCollection<SubParcel> subParcelsImport = ImporterPostGIS
        .importSubParcel(subParcelsLoad);
    IFeatureCollection<CadastralParcel> cadParcelsImport = ImporterPostGIS
        .importCadParcel(cadParcelsLoad);
    IFeatureCollection<BasicPropertyUnit> bPUImport = ImporterPostGIS
        .importBasicPropUnit(bPULoad);
    IFeatureCollection<BuildingPart> buildingPartImport = ImporterPostGIS
        .importBuildPart(buildingPartLoad);
    IFeatureCollection<SpecificCadastralBoundary> scbImport = ImporterPostGIS
        .importSpecificCadBound(scbLoad);
    IFeatureCollection<Road> roadImport = ImporterPostGIS.importRoad(roadLoad);
    IFeatureCollection<Road> axisImport = ImporterPostGIS.importAxis(axisLoad);
    IFeatureCollection<RoofSurface> roofImport = ImporterPostGIS
        .importRoof(roofLoad);
    IFeatureCollection<Building> buildingImport = ImporterPostGIS
        .importBuilding(buildingLoad);
    IFeatureCollection<SpecificWallSurface> wallImport = ImporterPostGIS
        .importWall(wallLoad);
    IFeatureCollection<RoofSurface> roofingImport = ImporterPostGIS
        .importRoofing(roofingLoad);
    IFeatureCollection<RoofSurface> gableImport = ImporterPostGIS
        .importGable(gableLoad);
    IFeatureCollection<RoofSurface> gutterImport = ImporterPostGIS
        .importGutter(gutterLoad);

    // Here some collections
    IFeatureCollection<UrbaZone> urbaZonePLU = new FT_FeatureCollection<UrbaZone>();
    IFeatureCollection<SubParcel> subParcelleZone = new FT_FeatureCollection<SubParcel>();
    IFeatureCollection<CadastralParcel> cadParcelsSubPar = new FT_FeatureCollection<CadastralParcel>();
    IFeatureCollection<BasicPropertyUnit> bPUCadPar = new FT_FeatureCollection<BasicPropertyUnit>();
    IFeatureCollection<AbstractBuilding> buildPartSubPar = new FT_FeatureCollection<AbstractBuilding>();
    IFeatureCollection<Road> roadSCB = new FT_FeatureCollection<Road>();

    // Stage 0 : We indicate the folder and we set the DTM
    env.folder = folder;

    // Stage 1 : We integrate the PLU
    IFeatureCollection<IFeature> pluBD = PostgisManager.loadNonGeometricTable(
        host, port, database, user, pw, NOM_TABLE_DOC_URBA);

    PLU docUrba = ImporterPostGIS.importPLU(pluBD);

    // On set le PLU
    env.setPlu(docUrba);

    // Stage 2 : We integrate Urba Zone
    IFeatureCollection<UrbaZone> featZone = ImporterPostGIS
        .importZoneUrba(zoneColl);

    // On set les variables de l'environnement
    env.setUrbaZones(new FT_FeatureCollection<UrbaZone>());
    env.setSubParcels(new FT_FeatureCollection<SubParcel>());
    env.setCadastralParcels(new FT_FeatureCollection<CadastralParcel>());
    env.setBpU(new FT_FeatureCollection<BasicPropertyUnit>());
    env.setBuildings(new FT_FeatureCollection<AbstractBuilding>());
    env.setRoads(new FT_FeatureCollection<Road>());

    // TODO : Ajouter ici condition sur l'ID du PLU pour lier les ZU au PLU

    for (UrbaZone currentZone : featZone) {

      int idZone = currentZone.getId();

      for (SubParcel currentSubP : subParcelsImport) {

        int idCurrentSP = currentSubP.getId();
        int idCadParInt = currentSubP.getIdCadPar();
        int idZUTraitInt = currentSubP.getIdZoneUrba();

        // System.out.println("Comparaison ID ZU : " + idZone + " / "
        // + idZUTraitInt);

        if (idZone != idZUTraitInt) {

          // System.out.println(" -- ATTENTION : La sous-parcelle "
          // + currentSubP.getId()
          // + " n'appartient pas à la zone urba ayant pour id : "
          // + idZUTraitInt + " --");

        } else {

          System.out.println("\n" + "La zone urba n°" + idZUTraitInt
              + " contient la sous-parcelle n°" + currentSubP.getId());

          for (CadastralParcel currentCadParcel : cadParcelsImport) {

            int idCadPar = currentCadParcel.getId();
            int idBPUTraitInt = currentCadParcel.getIdBPU();

            // System.out.println("Comparaison ID Parcelle Cad : " + idCadPar
            // + " / " + idCadParInt);

            if (idCadPar != idCadParInt) {

              // System.out.println(" -- ATTENTION : La parcelle cadastrale "
              // + idCadPar
              // + " n'appartient pas à la sous-parcelle ayant pour id : "
              // + currentSubP.getId() + " --");

            } else {

              System.out.println("\t" + "La sous-parcelle n°"
                  + currentSubP.getId() + " appartient à la parcelle n°"
                  + idCadPar);

              currentSubP.setParcelle(currentCadParcel);
              cadParcelsSubPar.add(currentCadParcel);

              for (BasicPropertyUnit currentBPU : bPUImport) {

                int idBPU = currentBPU.getId();

                // System.out.println("Comparaison ID BPU : " + idBPU + " / "
                // + idBPUTraitInt);

                if (idBPU != idBPUTraitInt) {

                  // System.out
                  // .println(" -- ATTENTION : La Basic Property Unit "
                  // + currentBPU.getId()
                  // +
                  // " n'appartient pas à la parcelle cadastrale ayant pour id : "
                  // + currentCadParcel.getId() + " --");

                } else {

                  System.out.println("\t\t" + "La parcelle cadastrale n°"
                      + currentCadParcel.getId() + " appartient à la BPU n°"
                      + currentBPU.getId());

                  currentCadParcel.setbPU(currentBPU);
                  bPUCadPar.add(currentBPU);

                }

              }

            }

          }

          for (SpecificCadastralBoundary currentSCB : scbImport) {

            int idSCB = currentSCB.getId();
            int idRef = currentSCB.getIdAdj();
            int idParent = currentSCB.getIdSubPar();

            if (idParent != idCurrentSP) {

              // System.out.println("On ne traite pas la SCB ayant pour ID : "
              // + idSCB + " car elle n'appartient pas à la sous parcelle "
              // + idCurrentSP);

            } else {

              String tabRef = currentSCB.getTableRef();

              System.out.println("\t" + "La Sous-Parcelle n°" + idCurrentSP
                  + " contient à la SCB n°" + idSCB);

              if (tabRef.equals(NOM_TABLE_ROAD)) {

                for (Road currentRoad : roadImport) {

                  int idRoad = currentRoad.getId();

                  if (idRef == idRoad) {

                    currentSCB.setFeatAdj(currentRoad);

                    System.out.println("\t\t" + "La SCB n°" + idSCB
                        + " fait référence à la route n°" + idRoad);

                    for (Road currentAxis : axisImport) {

                      int idRoadAxis = currentAxis.getIdRoad();

                      if (idRoadAxis == idRoad) {

                        currentRoad.setAxe(currentAxis);
                        roadSCB.add(currentRoad);

                        System.out.println("\t\t\t" + "La route n°" + idRoad
                            + " correspond à l'axe n°" + idRoadAxis);

                      } else {

                        // System.out.println("Attention : L'axe ayant pour ID : "
                        // + idRoadAxis
                        // + " ne correspond pas à la route ayant pour ID : "
                        // + idRoad);

                      }

                    }

                  } else {

                    // System.out.println("Attention : La route ayant pour ID : "
                    // + idRoad
                    // + " n'est pas adjacente à la SCB ayant pour ID : "
                    // + idSCB);

                  }

                }

              } else if (tabRef.equals(NOM_TABLE_SUB_PARCEL)) {

                for (SubParcel currentSubParcel : subParcelsImport) {

                  int idSubPar = currentSubParcel.getId();

                  if (idRef == idSubPar) {

                    currentSCB.setFeatAdj(currentSubParcel);

                    System.out.println("\t\t" + "La SCB n°" + idSCB
                        + " fait référence à la sous-parcelle n°" + idSubPar);

                  } else {

                    // System.out
                    // .println("Attention : La sous-parcelle ayant pour ID : "
                    // + idSubPar
                    // + " n'est pas adjacente à la SCB ayant pour ID : "
                    // + idSCB);

                  }

                }

              } else {

                System.out
                    .println("Problème avec l'objet adjacent de la SCB ayant pour ID : "
                        + idSCB);

              }

            }

          }

          IFeatureCollection<AbstractBuilding> tempAbsBuildParts = new FT_FeatureCollection<AbstractBuilding>();

          for (AbstractBuilding currentBuildingPart : buildingPartImport) {

            int idCurrentBP = currentBuildingPart.getId();
            int idBuilding = currentBuildingPart.getIdBuilding();
            int idSubParcel = currentBuildingPart.getIdSubPar();

            if (idSubParcel != idCurrentSP) {

              // System.out.println("On ne traite pas la Building Part ayant pour ID : "
              // + idCurrentBP +
              // " car elle n'appartient pas à la sous parcelle "
              // + idCurrentSP);

            } else {

              tempAbsBuildParts.add(currentBuildingPart);

              System.out.println("\t" + "La sous-parcelle n°" + idCurrentSP
                  + " contient la partie de batiment n°" + idCurrentBP);

              for (Building currentBuilding : buildingImport) {

                int idCurrentBuilding = currentBuilding.getId();
                // System.out.println("Comparaison ID : " + idBuilding + " / " +
                // idCurrentBuilding);

                if (idBuilding != idCurrentBuilding) {

                  // System.out.println("On ne traite pas le Building ayant pour ID : "
                  // + idCurrentBuilding +
                  // " car il ne contient pas la Building Part ayant pour ID : "
                  // + idCurrentBP);

                } else {

                  currentBuildingPart.setBuilding(currentBuilding);
                  buildPartSubPar.add(currentBuildingPart);

                  System.out.println("\t\t" + "La partie de batiment n°"
                      + idCurrentBP + " appartient au batiment n°"
                      + idCurrentBuilding);

                }

              }

              for (SpecificWallSurface currentWall : wallImport) {

                int idCurrentWall = currentWall.getId();
                int idBuilPartWall = currentWall.getIdBuildPart();

                if (idBuilPartWall != idCurrentBP) {

                  // System.out.println("On ne traite pas le mur ayant pour ID : "
                  // + idCurrentWall +
                  // " car il n'appartient pas à la Building Part ayant pour ID : "
                  // + idCurrentBP);

                } else {

                  currentBuildingPart.setWall(currentWall);

                  System.out.println("\t\t" + "La partie de batiment n°"
                      + idCurrentBP + " est rattachée au mur n°"
                      + idCurrentWall);

                }

              }

              for (RoofSurface currentRoof : roofImport) {

                int idCurrentRoof = currentRoof.getId();
                int idBuilPartRoof = currentRoof.getIdBuildPart();

                if (idBuilPartRoof != idCurrentBP) {

                  // System.out.println("On ne traite pas le toit ayant pour ID : "
                  // + idCurrentRoof +
                  // " car il n'appartient pas à la Building Part ayant pour ID : "
                  // + idCurrentBP);

                } else {

                  currentBuildingPart.setRoof(currentRoof);

                  System.out.println("\t\t" + "La partie de batiment n°"
                      + idCurrentBP + " est rattachée au toit n°"
                      + idCurrentRoof);

                  for (RoofSurface currentRoofing : roofingImport) {

                    int idCurrentRoofing = currentRoofing.getId();
                    int idRoofRoofing = currentRoofing.getIdRoof();

                    if (idRoofRoofing != idCurrentRoof) {

                      // System.out.println("On ne traite pas le roofing ayant pour ID : "
                      // + idCurrentRoofing +
                      // " car il n'appartient pas au toit ayant pour ID : "
                      // + idCurrentRoof);

                    } else {

                      currentRoof.setRoofing(currentRoofing.getRoofing());

                      System.out.println("\t\t\t" + "Le toit n°"
                          + idCurrentRoof + " est rattaché au roofing n°"
                          + idCurrentRoofing);

                    }

                  }

                  for (RoofSurface currentGutter : gutterImport) {

                    int idCurrentGutter = currentGutter.getId();
                    int idRoofGutter = currentGutter.getIdRoof();

                    if (idRoofGutter != idCurrentRoof) {

                      // System.out.println("On ne traite pas le gutter ayant pour ID : "
                      // + idCurrentGutter +
                      // " car il n'appartient pas au toit ayant pour ID : "
                      // + idCurrentRoof);

                    } else {

                      currentRoof.setGutter(currentGutter.getGutter());

                      System.out.println("\t\t\t" + "Le toit n°"
                          + idCurrentRoof + " est rattaché au gutter n°"
                          + idCurrentGutter);

                    }

                  }

                  for (RoofSurface currentGable : gableImport) {

                    int idCurrentGable = currentGable.getId();
                    int idRoofGable = currentGable.getIdRoof();

                    if (idRoofGable != idCurrentRoof) {

                      // System.out.println("On ne traite pas le gable ayant pour ID : "
                      // + idCurrentGable +
                      // " car il n'appartient pas au toit ayant pour ID : "
                      // + idCurrentRoof);

                    } else {

                      currentRoof.setGable(currentGable.getGable());

                      System.out.println("\t\t\t" + "Le toit n°"
                          + idCurrentRoof + " est rattaché au gable n°"
                          + idCurrentGable);

                    }

                  }

                }

              }

            }

            currentSubP.setBuildingsParts(tempAbsBuildParts);

          }

          subParcelleZone.add(currentSubP);

        }

      }

      currentZone.setSubParcels(subParcelleZone);
      urbaZonePLU.add(currentZone);

    }

    // TODO : set ici les variables de l'environnement
    env.setTerrain(dtm);
    env.getUrbaZones().addAll(urbaZonePLU);
    env.getCadastralParcels().addAll(cadParcelsSubPar);
    env.getSubParcels().addAll(subParcelleZone);
    env.getBpU().addAll(bPUCadPar);
    env.getRoads().addAll(roadSCB);
    env.getBuildings().addAll(buildPartSubPar);

    System.out.println("\n" + "----- Fin LoadFromCollectionPostGIS -----");

    System.out.println("\n" + "ID Cad Par : "
        + env.getCadastralParcels().get(4).getId() + "\n" + "Num Parcelle : "
        + env.getCadastralParcels().get(4).getNum() + "\n" + "Surface : "
        + env.getCadastralParcels().get(4).getArea() + "\n" + "ID BPU : "
        + env.getCadastralParcels().get(4).getIdBPU());

    System.out.println("\n" + "ID SubParcel : "
        + env.getSubParcels().get(0).getId() + "\n" + "ID Zone Urba : "
        + env.getSubParcels().get(0).getIdZoneUrba() + "\n"
        + "ID Cadastral parcelle : " + env.getSubParcels().get(0).getIdCadPar()
        + "\n" + "Avg slope : " + env.getSubParcels().get(0).getAvgSlope()
        + "\n" + "Area : " + env.getSubParcels().get(0).getArea());

    System.out.println("\n" + "ID Building Part : "
        + env.getBuildings().get(0).getId() + "\n" + "ID Building : "
        + env.getBuildings().get(0).getIdBuilding() + "\n" + "ID Sub parcel : "
        + env.getBuildings().get(0).getIdSubPar());

    System.out.println("\n" + "ID Road : " + env.getRoads().get(0).getId()
        + "\n" + "Nom : " + env.getRoads().get(0).getName() + "\n" + "Type : "
        + env.getRoads().get(0).getType() + "\n" + "Largeur : "
        + env.getRoads().get(0).getWidth() + "\n" + "Axe ID : "
        + env.getRoads().get(0).getAxe().getId());

    return env;

  }
}
