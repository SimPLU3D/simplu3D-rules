package fr.ign.cogit.simplu3d.checker.hackurba;

import java.util.List;

import fr.ign.cogit.simplu3d.checker.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Regulation;
import fr.ign.cogit.simplu3d.util.SimpleBandProduction;

public class CheckerHackUrba {
  
  
  
  private static  CompositeCheckerHackUrba prepareChecker(BasicPropertyUnit bPU,
      Regulation r1, Regulation r2, RuleContext context){
    
    new SimpleBandProduction(bPU, r1,
        r2);

    CompositeCheckerHackUrba compositeChecker = new CompositeCheckerHackUrba();

    CheckDistanceToRoad checkDistance = new CheckDistanceToRoad();
    compositeChecker.addChild(checkDistance);

    CheckDistanceToLatLimit checkDistanceLat = new CheckDistanceToLatLimit();
    compositeChecker.addChild(checkDistanceLat);

    CheckDistanceToBotLimit checkBotLimit = new CheckDistanceToBotLimit();
    compositeChecker.addChild(checkBotLimit);

    CheckProspectToSeparativeBoundaries checkPRospectToExistingBuilding = new CheckProspectToSeparativeBoundaries();
    compositeChecker.addChild(checkPRospectToExistingBuilding);

    CheckDistanceBetweenBuildings checkerBuildingBetweenBuildings = new CheckDistanceBetweenBuildings();
    compositeChecker.addChild(checkerBuildingBetweenBuildings);

    CheckHeightMax checkHeightMax = new CheckHeightMax();
    compositeChecker.addChild(checkHeightMax);

    CheckCES checkCES = new CheckCES();
    compositeChecker.addChild(checkCES);

    CheckParking checkParking = new CheckParking();
    compositeChecker.addChild(checkParking);

    CheckEmptySpace checkEmpty = new CheckEmptySpace();
    compositeChecker.addChild(checkEmpty);

    return compositeChecker;
    
  }

  public static List<UnrespectedRule> check(BasicPropertyUnit bPU,
      Regulation r1, Regulation r2, RuleContext context) {
    
      return prepareChecker(bPU, r1, r2, context).check(bPU, r1, r2, context);

  }
  
  public static List<GeometricConstraints> generateConstraints(BasicPropertyUnit bPU,
      Regulation r1, Regulation r2, RuleContext context) {
    
      return prepareChecker(bPU, r1, r2, context).generateGeometrycConstraints(bPU, r1, r2, context);

  }
}
