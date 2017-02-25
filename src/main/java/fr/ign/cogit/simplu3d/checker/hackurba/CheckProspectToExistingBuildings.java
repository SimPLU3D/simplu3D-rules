package fr.ign.cogit.simplu3d.checker.hackurba;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToLineString;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.analysis.ProspectCalculation;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.checker.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.Regulation;

public class CheckProspectToExistingBuildings implements IRuleChecker {

  public final static String CODE_DISTANCE_PROSPECT_BUILINDG = "PROSPECT_EXISTING";


  public CheckProspectToExistingBuildings() {

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

    if (r1 != null && r1.getArt_74() != 99) {

      List<GeometricConstraints> gc = generateGEometricConstraintsForOneRegulation( bPU, r1);
      if (gc != null) {
        geomConstraints.addAll(gc);

      }

    }

    Regulation r2 = bPU.getR2();

    if (r2 != null && r2.getArt_74() != 99) {

      List<GeometricConstraints> gc =  generateGEometricConstraintsForOneRegulation(bPU, r2);
      if (gc != null) {
        geomConstraints.addAll(gc);

      }


    }

    return geomConstraints;

  }

  private List<GeometricConstraints> generateGEometricConstraintsForOneRegulation(
      BasicPropertyUnit bPU, Regulation r) {
    List<Building> listBuildings = bPU.getBuildings();
    
    List<GeometricConstraints> lGC = new ArrayList<>();
    
    for(Building b:listBuildings){
      int coeff = r.getArt_74();
      IGeometry geom= this.generateProspect(b, coeff, r.getGeomBande());
      
      lGC.add(new GeometricConstraints("Distance minimale des constructions par rapport aux limits séparatives ", geom, 
          CODE_DISTANCE_PROSPECT_BUILINDG));
      
    }
    
    
    
    return lGC;
  }
  
  
  
  private IGeometry generateProspect(Building b, double slope, IGeometry polygon){
    
    
    return ProspectCalculation.calculate(b.getFootprint(), polygon, slope, 0);
    
  }

}
