package fr.ign.cogit.simplu3d.io.load.instruction;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.BuildingPart;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaDocument;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;

public class AutomaticAssignment {

	/**
	 * Permet de créer les variables de l'environnement à partir de
	 * IFeatureCollection contenant les données extraites d'une base de données
	 * 
	 * @param env
	 *            L'environnement dans lequel on charge les informations
	 * @param dtm
	 *            Le MNT de la zone
	 * @param pluImport
	 *            La IFeatureCollection des documents d'urbanisme importées
	 *            depuis la base de données
	 * @param zuImport
	 *            La IFeatureCollection des zones urbaines importées depuis la
	 *            base de données
	 * @param subParImport
	 *            La IFeatureCollection des sous-parcelles importées depuis la
	 *            base de données
	 * @param scbImport
	 *            La IFeatureCollection des Specific Cadastral Boundary
	 *            importées depuis la base de données
	 * @param roadImport
	 *            La IFeatureCollection des routes importées depuis la base de
	 *            données
	 * @param axisImport
	 *            La IFeatureCollection des axes des routes importés depuis la
	 *            base de données
	 * @param cadParImport
	 *            La IFeatureCollection des parcelles cadastrales importées
	 *            depuis la base de données
	 * @param bpuImport
	 *            La IFeatureCollection des Basic Property Unit importées depuis
	 *            la base de données
	 * @param bpImport
	 *            La IFeatureCollection des parties de batiments importées
	 *            depuis la base de données
	 * @param buildImport
	 *            La IFeatureCollection des batiments importés depuis la base de
	 *            données
	 * @param wallImport
	 *            La IFeatureCollection des murs importés depuis la base de
	 *            données
	 * @param roofImport
	 *            La IFeatureCollection des toits importés depuis la base de
	 *            données
	 * @param roofingImport
	 *            La IFeatureCollection des pignons importés depuis la base de
	 *            données
	 * @param gutterImport
	 *            La IFeatureCollection des gouttières importées depuis la base
	 *            de données
	 * @param gableImport
	 *            La IFeatureCollection des faitages importés depuis la base de
	 *            données
	 * @return
	 */

	public static Environnement assignment(Environnement env, UrbaDocument pluImport,
			IFeatureCollection<UrbaZone> zuImport, IFeatureCollection<SubParcel> subParImport,
			IFeatureCollection<SpecificCadastralBoundary> scbImport, IFeatureCollection<Road> roadImport,
			IFeatureCollection<Road> axisImport, IFeatureCollection<CadastralParcel> cadParImport,
			IFeatureCollection<BasicPropertyUnit> bpuImport, IFeatureCollection<BuildingPart> bpImport,
			IFeatureCollection<Building> buildImport, IFeatureCollection<SpecificWallSurface> wallImport,
			IFeatureCollection<RoofSurface> roofImport, IFeatureCollection<RoofSurface> roofingImport,
			IFeatureCollection<RoofSurface> gutterImport, IFeatureCollection<RoofSurface> gableImport) {

		// The name of both necessary tables for the processing of the SCB
		String NOM_TABLE_ROAD = ParametersInstructionPG.TABLE_ROAD;
		String NOM_TABLE_SUB_PARCEL = ParametersInstructionPG.TABLE_SUB_PARCEL;

		// "Simple" variable is assigned to the environment
		env.setPlu(pluImport);
		env.setTerrain(null);

		// The other variables are assigned, but empty
		env.setUrbaZones(new FT_FeatureCollection<UrbaZone>());
		env.setSubParcels(new FT_FeatureCollection<SubParcel>());
		env.setCadastralParcels(new FT_FeatureCollection<CadastralParcel>());
		env.setBpU(new FT_FeatureCollection<BasicPropertyUnit>());
		env.setBuildings(new FT_FeatureCollection<AbstractBuilding>());
		env.setRoads(new FT_FeatureCollection<Road>());

		// Here some collections to store some object
		IFeatureCollection<UrbaZone> urbaZonePLU = new FT_FeatureCollection<UrbaZone>();
		IFeatureCollection<SubParcel> subParcelleZone = new FT_FeatureCollection<SubParcel>();
		IFeatureCollection<CadastralParcel> cadParcelsSubPar = new FT_FeatureCollection<CadastralParcel>();
		IFeatureCollection<BasicPropertyUnit> bPUCadPar = new FT_FeatureCollection<BasicPropertyUnit>();
		IFeatureCollection<AbstractBuilding> buildPartSubPar = new FT_FeatureCollection<AbstractBuilding>();
		IFeatureCollection<Road> roadSCB = new FT_FeatureCollection<Road>();

		// We launch a series of loops to connect between them the various
		// objects
		// according to their attributes
		for (UrbaZone currentZone : zuImport) {

			int idZone = currentZone.getId();

			for (SubParcel currentSubP : subParImport) {

				int idCurrentSP = currentSubP.getId();
				int idCadParInt = currentSubP.getIdCadPar();
				int idZUTraitInt = currentSubP.getIdZoneUrba();

				if (idZone != idZUTraitInt) {

					// System.out.println(" -- ATTENTION : La sous-parcelle "
					// + currentSubP.getId()
					// + " n'appartient pas à la zone urba ayant pour id : "
					// + idZUTraitInt + " --");

				} else {

					System.out.println("\n" + "La zone urba n°" + idZUTraitInt + " contient la sous-parcelle n°"
							+ currentSubP.getId());

					for (CadastralParcel currentCadParcel : cadParImport) {

						int idCadPar = currentCadParcel.getId();
						int idBPUTraitInt = currentCadParcel.getId();

						if (idCadPar != idCadParInt) {

							// System.out.println(" -- ATTENTION : La parcelle
							// cadastrale "
							// + idCadPar
							// + " n'appartient pas à la sous-parcelle ayant
							// pour id : "
							// + currentSubP.getId() + " --");

						} else {

							System.out.println("\t" + "La sous-parcelle n°" + currentSubP.getId()
									+ " appartient à la parcelle n°" + idCadPar);

							currentSubP.setParcelle(currentCadParcel);
							currentCadParcel.getSubParcel().add(currentSubP);
							cadParcelsSubPar.add(currentCadParcel);

							for (BasicPropertyUnit currentBPU : bpuImport) {

								int idBPU = currentBPU.getId();

								if (idBPU != idBPUTraitInt) {

									// System.out
									// .println(" -- ATTENTION : La Basic
									// Property Unit "
									// + currentBPU.getId()
									// +
									// " n'appartient pas à la parcelle
									// cadastrale ayant pour id : "
									// + currentCadParcel.getId() + " --");

								} else {

									System.out.println("\t\t" + "La parcelle cadastrale n°" + currentCadParcel.getId()
											+ " appartient à la BPU n°" + currentBPU.getId());

									currentCadParcel.setbPU(currentBPU);
									currentBPU.getCadastralParcel().add(currentCadParcel);
									bPUCadPar.add(currentBPU);

								}

							}

						}

					}

					IFeatureCollection<SpecificCadastralBoundary> tempSCB = new FT_FeatureCollection<SpecificCadastralBoundary>();

					for (SpecificCadastralBoundary currentSCB : scbImport) {

						int idSCB = currentSCB.getId();
						int idRef = currentSCB.getIdAdj();
						int idParent = currentSCB.getIdSubPar();

						if (idParent != idCurrentSP) {

							// System.out.println("On ne traite pas la SCB ayant
							// pour ID : "
							// + idSCB + " car elle n'appartient pas à la sous
							// parcelle "
							// + idCurrentSP);

						} else {

							tempSCB.add(currentSCB);

							String tabRef = currentSCB.getTableRef();

							System.out.println(
									"\t" + "La Sous-Parcelle n°" + idCurrentSP + " contient à la SCB n°" + idSCB);

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
												
												
												
												
												currentRoad.setAxe(currentAxis.getAxis());
												roadSCB.add(currentRoad);

												System.out.println("\t\t\t" + "La route n°" + idRoad
														+ " correspond à l'axe n°" + idRoadAxis);

											} else {

												// System.out.println("Attention
												// : L'axe ayant pour ID : "
												// + idRoadAxis
												// + " ne correspond pas à la
												// route ayant pour ID : "
												// + idRoad);

											}

										}

									} else {

										// System.out.println("Attention : La
										// route ayant pour ID : "
										// + idRoad
										// + " n'est pas adjacente à la SCB
										// ayant pour ID : "
										// + idSCB);

									}

								}

							} else if (tabRef.equals(NOM_TABLE_SUB_PARCEL)) {

								for (SubParcel currentSubParcel : subParImport) {

									int idSubPar = currentSubParcel.getId();

									if (idRef == idSubPar) {

										currentSCB.setFeatAdj(currentSubParcel);

										System.out.println("\t\t" + "La SCB n°" + idSCB
												+ " fait référence à la sous-parcelle n°" + idSubPar);

									} else {

										// System.out
										// .println("Attention : La
										// sous-parcelle ayant pour ID : "
										// + idSubPar
										// + " n'est pas adjacente à la SCB
										// ayant pour ID : "
										// + idSCB);

									}

								}

							} else {

								System.out.println("Problème avec l'objet adjacent de la SCB ayant pour ID : " + idSCB);

							}

						}

						currentSubP.setSpecificCadBoundary(tempSCB);

					}

					IFeatureCollection<AbstractBuilding> tempAbsBuildParts = new FT_FeatureCollection<AbstractBuilding>();

					for (AbstractBuilding currentBuildingPart : bpImport) {

						int idCurrentBP = currentBuildingPart.getId();
						int idBuilding = currentBuildingPart.getId();
						int idSubParcel = currentBuildingPart.getIdSubPar();

						if (idSubParcel != idCurrentSP) {

							// System.out.println("On ne traite pas la Building
							// Part ayant pour ID : "
							// + idCurrentBP +
							// " car elle n'appartient pas à la sous parcelle "
							// + idCurrentSP);

						} else {

							tempAbsBuildParts.add(currentBuildingPart);

							System.out.println("\t" + "La sous-parcelle n°" + idCurrentSP
									+ " contient la partie de batiment n°" + idCurrentBP);

							for (Building currentBuilding : buildImport) {

								int idCurrentBuilding = currentBuilding.getId();

								if (idBuilding != idCurrentBuilding) {

									// System.out.println("On ne traite pas le
									// Building ayant pour ID : "
									// + idCurrentBuilding +
									// " car il ne contient pas la Building Part
									// ayant pour ID : "
									// + idCurrentBP);

								} else {

									buildPartSubPar.add(currentBuildingPart);

									System.out.println("\t\t" + "La partie de batiment n°" + idCurrentBP
											+ " appartient au batiment n°" + idCurrentBuilding);

								}

							}

							for (SpecificWallSurface currentWall : wallImport) {

								int idCurrentWall = currentWall.getId();
								int idBuilPartWall = currentWall.getIdBuildPart();

								if (idBuilPartWall != idCurrentBP) {

									// System.out.println("On ne traite pas le
									// mur ayant pour ID : "
									// + idCurrentWall +
									// " car il n'appartient pas à la Building
									// Part ayant pour ID : "
									// + idCurrentBP);

								} else {

									currentBuildingPart.setWall(currentWall);
									currentBuildingPart.getFacade().add(currentWall);

									// On set la BPU dans lequel se trouve la BP
									CadastralParcel cadParTemp = currentSubP.getParcel();
									BasicPropertyUnit bputemp = cadParTemp.getbPU();
									currentBuildingPart.setbPU(bputemp);

									System.out.println("\t\t" + "La partie de batiment n°" + idCurrentBP
											+ " est rattachée au mur n°" + idCurrentWall);

								}

							}

							for (RoofSurface currentRoof : roofImport) {

								int idCurrentRoof = currentRoof.getId();
								int idBuilPartRoof = currentRoof.getIdBuildPart();

								if (idBuilPartRoof != idCurrentBP) {

									// System.out.println("On ne traite pas le
									// toit ayant pour ID : "
									// + idCurrentRoof +
									// " car il n'appartient pas à la Building
									// Part ayant pour ID : "
									// + idCurrentBP);

								} else {

									currentBuildingPart.setToit(currentRoof);

									System.out.println("\t\t" + "La partie de batiment n°" + idCurrentBP
											+ " est rattachée au toit n°" + idCurrentRoof);

									for (RoofSurface currentRoofing : roofingImport) {

										int idCurrentRoofing = currentRoofing.getId();
										int idRoofRoofing = currentRoofing.getIdRoof();

										if (idRoofRoofing != idCurrentRoof) {

											// System.out.println("On ne traite
											// pas le roofing ayant pour ID : "
											// + idCurrentRoofing +
											// " car il n'appartient pas au toit
											// ayant pour ID : "
											// + idCurrentRoof);

										} else {

											currentRoof.setRoofing(currentRoofing.getRoofing());

											System.out.println("\t\t\t" + "Le toit n°" + idCurrentRoof
													+ " est rattaché au roofing n°" + idCurrentRoofing);

										}

									}

									for (RoofSurface currentGutter : gutterImport) {

										int idCurrentGutter = currentGutter.getId();
										int idRoofGutter = currentGutter.getIdRoof();

										if (idRoofGutter != idCurrentRoof) {

											// System.out.println("On ne traite
											// pas le gutter ayant pour ID : "
											// + idCurrentGutter +
											// " car il n'appartient pas au toit
											// ayant pour ID : "
											// + idCurrentRoof);

										} else {

											currentRoof.setGutter(currentGutter.getGutter());

											System.out.println("\t\t\t" + "Le toit n°" + idCurrentRoof
													+ " est rattaché au gutter n°" + idCurrentGutter);

										}

									}

									for (RoofSurface currentGable : gableImport) {

										int idCurrentGable = currentGable.getId();
										int idRoofGable = currentGable.getIdRoof();

										if (idRoofGable != idCurrentRoof) {

											// System.out.println("On ne traite
											// pas le gable ayant pour ID : "
											// + idCurrentGable +
											// " car il n'appartient pas au toit
											// ayant pour ID : "
											// + idCurrentRoof);

										} else {

											currentRoof.setGable(currentGable.getGable());

											System.out.println("\t\t\t" + "Le toit n°" + idCurrentRoof
													+ " est rattaché au gable n°" + idCurrentGable);

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

		// We integrate objects, bound between them, into the environment
		env.getUrbaZones().addAll(urbaZonePLU);
		env.getCadastralParcels().addAll(cadParcelsSubPar);
		env.getSubParcels().addAll(subParcelleZone);
		env.getBpU().addAll(bPUCadPar);
		env.getRoads().addAll(roadSCB);
		env.getBuildings().addAll(buildPartSubPar);

		System.out.println("\n" + "----- End of Automatic Assignment -----" + "\n");

		return env;

	}

}
