package fr.ign.cogit.simplu3d.checker.hackurba;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.simplu3d.checker.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Regulation;

public class CheckHeightMax  implements IRuleChecker {

  public final static String CODE_HEIGHT_MAX = "HAUTEUR_MAX";

  public CheckHeightMax() {

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

    if (r1 != null && r1.getArt_10_top() != 88 &&  r1.getArt_10_top() != 99) {

      GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(
          bPU, r1);
      if (gc != null) {
        geomConstraints.add(gc);

      }

    }

    Regulation r2 = bPU.getR2();

    if (r2 != null && r2.getArt_10_top() != 88 &&  r2.getArt_10_top() != 99) {

      GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(
          bPU, r2);
      if (gc != null) {
        geomConstraints.add(gc);

      }

    }

    return geomConstraints;
  }

  private GeometricConstraints generateGEometricConstraintsForOneRegulation(
      BasicPropertyUnit bPU, Regulation r1) {
    
    double hauteurMax = 0;
    if(r1.getArt_10_top() == 0){
      hauteurMax = r1.getArt_101() * 3.0;
    }else{
      hauteurMax = r1.getArt_101();
    }

 
    return new GeometricConstraints(
        "Coefficient d’emprise au sol maximum"
            + r1.getArt_9(),
        Extrusion2DObject.convertFromGeometry(r1.getGeomBande(), 0, hauteurMax), CODE_HEIGHT_MAX);
  }
}