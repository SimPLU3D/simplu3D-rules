package fr.ign.cogit.simplu3d.io.load.instruction.checker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToLineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.io.load.instruction.Load;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;

public class Checker {

  // id de la version par défaut
  public static int idVersion = -1;

  // chemin d'accès aux données
  public static String folder = "D:/0_Masson/1_CDD_SIMPLU/2_Travail/0_Workspace/"
      + "simplu3d/simplu3D-rules/src/main/resources/fr/ign/cogit/simplu3d/data/dataRennes/";

  // Chemin d'accès au fichier csv contenant les règles
  public static String file = "D:/0_Masson/1_CDD_SIMPLU/2_Travail/0_Workspace/simplu3d/"
      + "simplu3D-rules/src/main/resources/rules/rennes-ub2/regles_rennes.csv";



  /**
   * Permets de vérifier les règles d'urbanisme sur une ou plusieurs BPU
   * contenues dans une IFeatureCollection
   * @param bpuColl La IFeatureCollection contenant les BPU
   * @throws Exception
   */
  public static List<UnrespectedRule>  checkSelection(
      IFeatureCollection<BasicPropertyUnit> bpuColl) throws Exception {

    // On charge les règles depuis la base de données
    Map<String, Rules> map = Rules.loadRulesFromDataBase(Load.host, Load.port,
        Load.user, Load.pw, Load.database);

    // On vérifie le contenu de l'import des règles
    System.out.println("\n" + "Résultat Import Règles : "
        + map.get("UB2").toString());
    String nomZone = "UB2";
    
    List<UnrespectedRule> lUNRTot = new ArrayList<>();

    // On check les règles pour chaque BPU
    for (BasicPropertyUnit currentBPU : bpuColl) {

      // On lance les vérification :
      List<UnrespectedRule> lUNR = check(currentBPU, map.get(nomZone));

      // On affiche les résultats
      System.out.println("\n" + "Unrespected Rules on BPU "
          + currentBPU.getId() + " : ");
      for (UnrespectedRule un : lUNR) {
        System.out.println("\t" + un);
      }
      
      lUNRTot.addAll(lUNR);

    }

    System.out.println("\n" + " ----- Fin de la vérification des règles ----- "
        + "\n");
    return lUNRTot;
  }

  /**
   * Permet de vérifier les règles sur une BPU à partir d'un ensemble de règles
   * @param bPU Une Basic Property Unit
   * @param rules Les règles d'urbanisme
   * @return une liste de règles non respectées
   */
  public static List<UnrespectedRule> check(BasicPropertyUnit bPU, Rules rules) {

    List<UnrespectedRule> lUNR = new ArrayList<>();

    // Une bande de getBandIncons() m (par rapport au fond de la parcelle)
    // est
    // inconstructible.
    lUNR.addAll(checkDistanceInconsBot(bPU, rules));

    // - Le coefficient d'emprise au sol peut atteindre getEmpriseSol de la
    // superficie du
    // terrain pour les terrains cumulant les caractéristiques suivantes :
    // superficie supérieure ou égale à getEmpriseSurface(), largeur de
    // façade
    // supérieure ou égale à getEmpLargeurMin mètres, - Pour les terrains
    // dont l'une des
    // caractéristiques en matière de superficie et de largeur de façade ne
    // répond pas aux critères fixés ci-dessus, le coefficient d'emprise au
    // sol,
    // y compris les constructions annexes, est limité à getEmpriseSolAlt de
    // la superficie du
    // terrain.
    lUNR.addAll(checkCES(bPU, rules));

    // On récupère les bâtiments par bande :
    Map<Integer, List<AbstractBuilding>> mapB = getBuildingByBand(bPU, rules);

    // On traite la bande 1 : le prospect en fonction de la largeur de la voirie
    lUNR.addAll(checkProspectBand1(mapB.get(1), bPU, rules));

    // On traite la bande 1 : le recul par rapport aux limites latérales et
    // à l'alignement
    lUNR.addAll(checkLateralFront(mapB.get(1), bPU, rules));

    // On traite la bande 2 avec les valeurs ules.getSlopeProspectLat(),
    // rules.gethIniProspectLat()
    lUNR.addAll(checkProspectBand2(mapB.get(2), bPU, rules));

    // On vérifie la hauteur par rapport à getHauteurMax2
    lUNR.addAll(checkHeightDistanceBand2(mapB.get(2), bPU, rules));

    return lUNR;
  }

  // ////////////////////////
  // / Cette partie du code contient les vérificateurs de règles
  // /
  // /
  // /////////////////////////

  /**
   * Une bande de getBandIncons() m (par rapport au fond de la parcelle) est
   * inconstructible.
   * 
   * @param bPU une BPU
   * @param rules des règles
   * @return une liste de règles non respectées
   */
  private static List<UnrespectedRule> checkDistanceInconsBot(
      BasicPropertyUnit bPU, Rules rules) {

    List<UnrespectedRule> lUNR = new ArrayList<>();

    // On récupère la limite de fond
    IMultiCurve<IOrientableCurve> ims = getBotLimit(bPU);
    IFeature feat = new DefaultFeature(ims);

    // On récupère les parties de bâtiments
    for (CadastralParcel cP : bPU.getCadastralParcel()) {

      for (SubParcel sP : cP.getSubParcel()) {

        for (AbstractBuilding bP : sP.getBuildingsParts()) {

          // On vérifie si la distance est bonne
          if (bP.getFootprint().distance(ims) < rules.getBandIncons()) {
            // System.out.println("Distance : " +
            // bP.getFootprint().distance(ims));
            lUNR.add(new UnrespectedRule(
                "Bande de constructibilité de fond de parcelle non respectée ",
                bP, feat));
          }

        }

      }
    }

    return lUNR;
  }

  /**
   * - Le coefficient d'emprise au sol peut atteindre getEmpriseSol de la
   * superficie du terrain pour les terrains cumulant les caractéristiques
   * suivantes : superficie supérieure ou égale à getEmpriseSurface(), largeur
   * de façade supérieure ou égale à getEmpLargeurMin mètres, - Pour les
   * terrains dont l'une des caractéristiques en matière de superficie et de
   * largeur de façade ne répond pas aux critères fixés ci-dessus, le
   * coefficient d'emprise au sol, y compris les constructions annexes, est
   * limité à getEmpriseSolAlt de la superficie du terrain.
   * 
   * @param bPU une BPU
   * @param rules des règles
   * @return une liste de règles non respectées
   */
  private static List<UnrespectedRule> checkCES(BasicPropertyUnit bPU,
      Rules rules) {
    List<UnrespectedRule> lUNR = new ArrayList<>();

    // Cette information permet de savoir si on doit avoir un CES de 0.5 ou
    // de 0.4
    boolean conditionchecked = true;

    // Est-ce que la superficie du terrain est inférieure ou égale à
    // getEmpriseSurface()
    double area = bPU.getGeom().area();
    if (area < rules.getEmpriseSurface()) {
      conditionchecked = false;
    }

    // Est-ce que la largeur des façades est supérieure à EmpLargeurMin
    boolean hasBeenCheckded = false;
    // On récupère les parties de bâtiments
    bouclecp: for (CadastralParcel cP : bPU.getCadastralParcel()) {

      for (SubParcel sP : cP.getSubParcel()) {

        for (AbstractBuilding bP : sP.getBuildingsParts()) {

          List<SpecificWallSurface> lSWS = bP.getFacade();

          // System.out.println("facades : " + lSWS);

          for (SpecificWallSurface sWS : lSWS) {

            if (!sWS.getGeom().isEmpty()) {

              IEnvelope enve = sWS.getGeom().getEnvelope();

              if (enve.length() > rules.getEmpLargeurMin()) {
                hasBeenCheckded = true;
                break bouclecp;
              }

            }

          }

        }

      }

    }

    // Si les 2 conditions sont vérifiées c'est
    double ces = rules.getEmpriseSol();
    // Si une des deux n'est pas vérifiée, c'est l'autre
    if (!conditionchecked || !hasBeenCheckded) {
      ces = rules.getEmpriseSolAlt();
    }

    // On calcule l'aire totale bâtie
    double aireBati = 0;
    for (CadastralParcel cP : bPU.getCadastralParcel()) {

      for (SubParcel sP : cP.getSubParcel()) {

        for (AbstractBuilding bP : sP.getBuildingsParts()) {

          aireBati = aireBati + bP.getFootprint().area();

        }

      }

    }

    // On regarde si le CES est respectée
    if (bPU.getGeom().area() * ces < aireBati) {
      // LE CES n'est pas respectée on ajoute une incohérence

      lUNR.add(new UnrespectedRule("Le CES n'est pas respecté - CES mesuré : "
          + (aireBati / bPU.getGeom().area()) + "  CES attendu " + ces, bPU,
          null));

    }

    return lUNR;
  }

  /**
   * Le prospect est vérifié en fonction de la largeur de la route
   * rules.getLargMaxProspect1() : Si c'est plus on vérifie le prospect par
   * rapport aux limites sur la voirie avec les paramètres : slope :
   * rules.getProspectVoirie2Slope() et hIni : r.getWidth() *
   * rules.getProspectVoirie2Slope() + rules.getProspectVoirie2Hini() sinon
   * slope getProspectVoirie1Slope et hIni : r.getWidth() *
   * rules.getProspectVoirie1Slope() + rules.getProspectVoirie1Hini()
   * @param list une liste de Building Part en AbstractBuilding
   * @param bPU une BPU
   * @param rules des règles
   * @return une liste de règles non respectées
   */
  private static Collection<? extends UnrespectedRule> checkProspectBand1(
      List<AbstractBuilding> list, BasicPropertyUnit bPU, Rules rules) {
    List<UnrespectedRule> lUNR = new ArrayList<>();

    // On récupère les limites de fronts de parcelles (celles qui peuvent
    // nous donner une information sur les routes)
    List<SpecificCadastralBoundary> lFronLimit = getFrontLimit(bPU);
    // System.out.println("Liste SCB road : " + lFronLimit);

    for (SpecificCadastralBoundary sc : lFronLimit) {
      // On récupère la route adjance
      if (sc.getFeatAdj() != null) {

        IFeature routeAdj = sc.getFeatAdj();
        // System.out.println("Road v1 : " + routeAdj);

        // Normalement on peut caster
        Road r = (Road) routeAdj;
        // System.out.println("Road v2 : " + r);
        // System.out.println("Road Width : " + r.getWidth());
        // System.out.println("Rule larg max prospect 1 : "
        // + rules.getLargMaxProspect1());

        for (AbstractBuilding ab : list) {

          // suivant le cas, on a 2 prospect
          if (r.getWidth() > rules.getLargMaxProspect1()) {
            // Est-ce qu'il respecte le prospect ?
            if (!ab.prospect(
                sc.getGeom(),
                rules.getProspectVoirie2Slope(),
                r.getWidth() * rules.getProspectVoirie2Slope()
                    + rules.getProspectVoirie2Hini())) {
              lUNR.add(new UnrespectedRule(
                  "Prospect non respecté (route de plus de "
                      + rules.getLargMaxProspect1() + " m de large", ab, sc));
            }

          } else {
            // Est-ce qu'il respecte l'autre prospect ?
            if (!ab.prospect(
                sc.getGeom(),
                rules.getProspectVoirie1Slope(),
                r.getWidth() * rules.getProspectVoirie1Slope()
                    + rules.getProspectVoirie1Hini())) {
              lUNR.add(new UnrespectedRule(
                  "Prospect non respecté (route de moins de "
                      + rules.getLargMaxProspect1() + " m de large", ab, sc));
            }
          }
        }

      }
    }

    return lUNR;

  }

  /**
   * Ici on vérifie que la distance du bâtiment est supérieur à getAlignement
   * par rapport à la voirie et on vérifie que la distance latérale est comprise
   * entre rules.getReculLatMin() et rules.getReculLatMax()
   * 
   * @param list une liste de Building Part en AbstractBuilding
   * @param bPU une BPU
   * @param rules des règles
   * @return une liste de règles non respectées
   */
  private static Collection<? extends UnrespectedRule> checkLateralFront(
      List<AbstractBuilding> list, BasicPropertyUnit bPU, Rules rules) {
    List<UnrespectedRule> lUNR = new ArrayList<>();

    IMultiCurve<IOrientableCurve> iMSFront = getFrontLimitGeom(bPU);

    for (AbstractBuilding aB : list) {
      if (aB.getFootprint().distance(iMSFront) > rules.getAlignement() + 0.5) {
        lUNR.add(new UnrespectedRule(
            "Non respect de la distance à l'alignement ", aB,
            new DefaultFeature(iMSFront)));
      }
    }

    IMultiCurve<IOrientableCurve> iMSLat = getLatLimit(bPU);

    for (AbstractBuilding aB : list) {
      if (aB.getFootprint().distance(iMSLat) > rules.getReculLatMin()
          && aB.getFootprint().distance(iMSLat) < rules.getReculLatMax()) {
        lUNR.add(new UnrespectedRule(
            "Non respect de la distance aux limites latérales ", aB,
            new DefaultFeature(iMSLat)));
      }
    }

    return lUNR;
  }

  /**
   * On vérifie en bande 2 le prospect latéral avec les valeurs
   * ules.getSlopeProspectLat(), rules.gethIniProspectLat()
   * 
   * @param list une liste de Building Part en AbstractBuilding
   * @param bPU une BPU
   * @param rules des règles
   * @return une liste de règles non respectées
   */
  private static Collection<? extends UnrespectedRule> checkProspectBand2(
      List<AbstractBuilding> list, BasicPropertyUnit bPU, Rules rules) {
    List<UnrespectedRule> lUNR = new ArrayList<>();

    // On récupère les limites de fronts de parcelles (celles qui peuvent
    // nous donner une information sur les routes)
    IMultiCurve<IOrientableCurve> imc = getLatLimit(bPU);

    bouclebuilding: for (AbstractBuilding ab : list) {

      for (IOrientableCurve os : imc) {

        // Est-ce qu'il respecte l'autre prospect ?
        if (!ab.prospect(os, rules.getSlopeProspectLat(),
            rules.gethIniProspectLat())) {
          lUNR.add(new UnrespectedRule(
              "Prospect latéral non respecté en seconde bande ", ab,
              new DefaultFeature(os)));
          continue bouclebuilding;
        }

      }
    }

    return lUNR;

  }

  /**
   * On vérifie en bande 2 la hauteur des bâtiments par rapport à getHauteurMax2
   * 
   * @param list une liste de Building Part en AbstractBuilding
   * @param bPU une BPU
   * @param rules des règles
   * @return une liste de règles non respectées
   */
  private static Collection<? extends UnrespectedRule> checkHeightDistanceBand2(
      List<AbstractBuilding> list, BasicPropertyUnit bPU, Rules rules) {
    List<UnrespectedRule> lUNR = new ArrayList<>();

    // On vérifie la hauteur
    for (AbstractBuilding ab : list) {
      double hauteur = ab.height(0, 1);

      if (hauteur > rules.getHauteurMax2()) {
        lUNR.add(new UnrespectedRule(
            "Non respect de la hauteur maximale en seconde bande ", ab, null));
      }

    }

    return lUNR;
  }

  // //////////////////////
  // // Cette partie concerne les accesseurs
  // //////////////////////

  /**
   * On récupère les bâtiments en 2 bandes (avec priorité pour la première)
   * 
   * - Alignement + r.getBand1 - Alignement + r.getBand1 + r.getBand2 TODO :
   * couper en 2 les bâtiments en fonction des zones dans lesquells ils sont
   * 
   * @param bPU une BPU
   * @param r des règles
   * @return une Map<Integer, List<AbstractBuilding>>
   */
  private static Map<Integer, List<AbstractBuilding>> getBuildingByBand(
      BasicPropertyUnit bPU, Rules r) {

    IGeometry getFrontLimit = getFrontLimitGeom(bPU);

    Map<Integer, List<AbstractBuilding>> mapB = new Hashtable<>();

    mapB.put(1, new ArrayList<AbstractBuilding>());
    mapB.put(2, new ArrayList<AbstractBuilding>());

    for (CadastralParcel cP : bPU.getCadastralParcel()) {

      for (SubParcel sP : cP.getSubParcel()) {

        for (AbstractBuilding bP : sP.getBuildingsParts()) {

          if (bP.getFootprint().distance(getFrontLimit) < r.getAlignement()
              + r.getBand1()) {
            // Bande 1 : on ajoute la bâtiment à la liste 1
            mapB.get(1).add(bP);

          } else if (bP.getFootprint().distance(getFrontLimit) < r
              .getAlignement() + r.getBand1() + r.getBand2()) {

            // Bande 2 : on ajoute la bâtiment à la liste 1
            mapB.get(2).add(bP);
          }

        }
      }
    }
    return mapB;
  }

  private static List<SpecificCadastralBoundary> getFrontLimit(
      BasicPropertyUnit bPU) {
    List<SpecificCadastralBoundary> lSC = new ArrayList<>();

    for (CadastralParcel cP : bPU.getCadastralParcel()) {
      // System.out.println("parcelle : " + cP);

      for (SubParcel sP : cP.getSubParcel()) {
        // System.out.println("sous-parcelle : " + sP);

        for (SpecificCadastralBoundary sc : sP.getSpecificCadastralBoundary()) {
          // System.out.println("SCB : " + sc);
          // System.out.println("SCB type : " + sc.getType());

          if (sc.getType() == SpecificCadastralBoundary.ROAD) {
            lSC.add(sc);
          }

        }

      }

    }

    return lSC;

  }

  private static IMultiCurve<IOrientableCurve> getFrontLimitGeom(
      BasicPropertyUnit bPU) {
    IMultiCurve<IOrientableCurve> img = new GM_MultiCurve<>();

    for (CadastralParcel cP : bPU.getCadastralParcel()) {

      for (SubParcel sP : cP.getSubParcel()) {

        for (SpecificCadastralBoundary sc : sP.getSpecificCadastralBoundary()) {

          if (sc.getType() == SpecificCadastralBoundary.ROAD) {
            img.addAll(FromGeomToLineString.convert(sc.getGeom()));
          }

        }

      }

    }

    return img;

  }

  private static IMultiCurve<IOrientableCurve> getLatLimit(BasicPropertyUnit bPU) {
    IMultiCurve<IOrientableCurve> img = new GM_MultiCurve<>();

    for (CadastralParcel cP : bPU.getCadastralParcel()) {

      for (SubParcel sP : cP.getSubParcel()) {

        for (SpecificCadastralBoundary sc : sP.getSpecificCadastralBoundary()) {

          if (sc.getType() == SpecificCadastralBoundary.LAT) {
            img.addAll(FromGeomToLineString.convert(sc.getGeom()));
          }

        }

      }

    }

    return img;

  }

  private static IMultiCurve<IOrientableCurve> getBotLimit(BasicPropertyUnit bPU) {
    IMultiCurve<IOrientableCurve> img = new GM_MultiCurve<>();

    for (CadastralParcel cP : bPU.getCadastralParcel()) {

      for (SubParcel sP : cP.getSubParcel()) {

        // ////////////////////////
        // / Cette partie du code contient les vérificateurs de règles
        // /
        // /
        // /////////////////////////

        for (SpecificCadastralBoundary sc : sP.getSpecificCadastralBoundary()) {

          if (sc.getType() == SpecificCadastralBoundary.BOT) {
            img.addAll(FromGeomToLineString.convert(sc.getGeom()));
          }

        }

      }

    }

    return img;

  }

}
