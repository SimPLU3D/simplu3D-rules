package fr.ign.cogit.simplu3d.checker.hackurba;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.simplu3d.checker.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.Regulation;

public class CheckDistanceBetweenBuildings implements IRuleChecker {

  public final static String CODE_DISTANCE_BULDINGS = "RECUL_BATIMENT";

  public CheckDistanceBetweenBuildings() {

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

    if (r1 != null && r1.getArt_8() != 99) {

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

    IMultiSurface<IOrientableSurface> ims = new GM_MultiSurface<>();

    for (Building b : bPU.getBuildings()) {
      ims.addAll(FromGeomToSurface.convertGeom(b.getFootprint()));
    }
    
    GeometricConstraints gC = new GeometricConstraints("Distance entre bâtiments de " + r1.getArt_8() + " m", ims , CODE_DISTANCE_BULDINGS);
    
    
    return null;
  }

}
