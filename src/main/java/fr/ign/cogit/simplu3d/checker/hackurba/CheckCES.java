package fr.ign.cogit.simplu3d.checker.hackurba;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Regulation;

public class CheckCES  implements IRuleChecker {

  public final static String CODE_EMPTY_SPACE = "ESPACE_VIDE";

  public CheckCES() {

  }

  @Override
  public List<UnrespectedRule> check(BasicPropertyUnit bPU,
      RuleContext context) {
    List<UnrespectedRule> lUNR = new ArrayList<>();

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
        "Part minimale d'espaces libres de toute la surface de la parcelle"
            + r1.getArt_12(),
        bPU.getGeom(), CODE_EMPTY_SPACE);
  }

}
