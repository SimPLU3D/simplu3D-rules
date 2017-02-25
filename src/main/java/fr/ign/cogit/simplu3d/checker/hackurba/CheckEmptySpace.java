package fr.ign.cogit.simplu3d.checker.hackurba;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.Regulation;

public class CheckEmptySpace  implements IRuleChecker {

  public final static String CODE_EMPTY_SPACE = "ESPACE LIBRE";

  public CheckEmptySpace() {

  }

  @Override
  public List<UnrespectedRule> check(BasicPropertyUnit bPU,
      RuleContext context) {
    List<UnrespectedRule> lUNR = new ArrayList<>();
    
    if(bPU.getR1().getArt_12() ==99 ||bPU.getR1().getArt_12() ==88){
      return lUNR;
    }
    
    double totalArea = 0;
    for(Building b:bPU.getBuildings()){
      totalArea = totalArea + b.getFootprint().area();
      
      
    }
    
    double aireParcelle = bPU.getGeom().area();
    
    double calculateEmptySpace = 1 -  totalArea/aireParcelle;
    
    if(calculateEmptySpace < bPU.getR1().getArt_12()){
      lUNR.add(new UnrespectedRule(
          "Le taux d'espace vide n'est pas respecté. Mesurée : " + calculateEmptySpace + "   attendu " + bPU.getR1().getArt_12(), bPU.getGeom(),
          CODE_EMPTY_SPACE));
    }

    return lUNR;
  }

  @Override
  public List<GeometricConstraints> generate(BasicPropertyUnit bPU) {
    List<GeometricConstraints> geomConstraints = new ArrayList<>();

    Regulation r1 = bPU.getR1();

    if (r1 != null && r1.getArt_12() != 99) {

      GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(
          bPU, r1);
      if (gc != null) {
        geomConstraints.add(gc);

      }

    }

    return geomConstraints;

  }

  private GeometricConstraints generateGEometricConstraintsForOneRegulation(
      BasicPropertyUnit bPU, Regulation r1) {
    
    

 
    return new GeometricConstraints(
        "Coefficient d’emprise au sol maximum"
            + r1.getArt_12(),
        bPU.getGeom(), CODE_EMPTY_SPACE);
  }

}
