package fr.ign.cogit.simplu3d.checker.hackurba;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.simplu3d.checker.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.Regulation;

public class CheckHeightMax  implements IRuleChecker {

  public final static String CODE_HEIGHT_MAX = "HAUTEUR_MAX";

  public CheckHeightMax() {

  }

  @Override
  public List<UnrespectedRule> check(BasicPropertyUnit bPU,
      RuleContext context) {
    
    List<UnrespectedRule> lUNR = new ArrayList<>();
    
    Regulation r1 = bPU.getR1();
    Regulation r2 = bPU.getR2();
    
    List<Building> lBuilding = new ArrayList<>();
    for(Building b:bPU.getBuildings()){
      if(b.isNew){
        lBuilding.add(b);
      }
    }

    if (lBuilding.isEmpty()) {
        return lUNR;
    }


    for (AbstractBuilding b : lBuilding) {

     double height = b.height(1, 1);
     
     if(r1 != null &&r1.getGeomBande() != null &&r1.getArt_10_top() != 88 &&  r1.getArt_10_top() != 99){
       if(b.getFootprint().intersects(r1.getGeomBande())){
         double hauteurMax = r1.getArt_102();
         
         if(height > hauteurMax){
           
           lUNR.add(new UnrespectedRule("Hauteur non respectée. Hauteur mesurée : " + height + " - hauteur maximale " + hauteurMax, b.getGeom(), CODE_HEIGHT_MAX));
           
         }
         
         
       }
     }

     
     if(r2 != null &&r2.getGeomBande() != null &&r2.getArt_10_top() != 88 &&  r2.getArt_10_top() != 99){
       if(b.getFootprint().intersects(r2.getGeomBande())){
         
         double hauteurMax = r2.getArt_102();

         
         if(height > hauteurMax){
           
           lUNR.add(new UnrespectedRule("Hauteur non respectée. Hauteur mesurée : " + height + " - hauteur maximale " + hauteurMax, b.getGeom(), CODE_HEIGHT_MAX));
           
         }
         
         
       }
     }

          

    }

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
    if(r1.getGeomBande().isEmpty()){
      return null;
    }
    double hauteurMax = r1.getArt_102();

 
    return new GeometricConstraints(
        "La hauteur maximale est : "
            + hauteurMax,
        Extrusion2DObject.convertFromGeometry(r1.getGeomBande(), hauteurMax, hauteurMax), CODE_HEIGHT_MAX);
  }
}