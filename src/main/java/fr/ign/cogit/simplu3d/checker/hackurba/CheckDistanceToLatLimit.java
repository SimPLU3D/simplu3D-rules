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
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.checker.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.Regulation;

public class CheckDistanceToLatLimit implements IRuleChecker {

  public final static String CODE_DISTANCE_LAT = "RECUL_LATERAL";


  public CheckDistanceToLatLimit() {

  }

  @Override
  public List<UnrespectedRule> check(BasicPropertyUnit bPU,
      RuleContext context) {
    List<UnrespectedRule> lUNR = new ArrayList<>();

    return lUNR;
  }

  /**
   * TODO describe
   * @param bPU
   * @return
   */
  private IMultiCurve<IOrientableCurve> getBotLimit(BasicPropertyUnit bPU) {
    IMultiCurve<IOrientableCurve> img = new GM_MultiCurve<>();

    for (CadastralParcel cP : bPU.getCadastralParcels()) {
      for (ParcelBoundary sc : cP.getBoundaries()) {
        if (sc.getType() == ParcelBoundaryType.LAT) {
          img.addAll(FromGeomToLineString.convert(sc.getGeom()));
        }
      }
    }

    return img;
  }

  @Override
  public List<GeometricConstraints> generate(BasicPropertyUnit bPU) {
    List<GeometricConstraints> geomConstraints = new ArrayList<>();

    IMultiCurve<IOrientableCurve> iCurve = this.getBotLimit(bPU);

    Regulation r1 = bPU.getR1();

    if (r1 != null && r1.getArt_71() != 99) {

      GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(
          bPU, r1, iCurve);
      if (gc != null) {
        geomConstraints.add(gc);

      }

    }

    Regulation r2 = bPU.getR2();

    if (r2 != null && r2.getArt_71() != 99) {

      GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(
          bPU, r2, iCurve);
      if (gc != null) {
        geomConstraints.add(gc);

      }

    }

    return geomConstraints;

  }

  private GeometricConstraints generateGEometricConstraintsForOneRegulation(
      BasicPropertyUnit bPU, Regulation r,
      IMultiCurve<IOrientableCurve> iCurve) {
    
    
    if(iCurve == null ||  iCurve.isEmpty()){
      return null;
    }
    
    if(r.getArt_71() != 0){
      return new GeometricConstraints("Alignement contre une des limites latérales",          
          iCurve.buffer(0.5), CODE_DISTANCE_LAT);
      
          
    }
    IGeometry geom = r.getGeomBande().intersection(iCurve.buffer(r.getArt_72()));
    IMultiSurface<IOrientableSurface> iMS = FromGeomToSurface
        .convertMSGeom(geom);

    IGeometry finalGeom = iMS.intersection(r.getGeomBande());
    IMultiSurface<IOrientableSurface> iMSFinale = FromGeomToSurface
        .convertMSGeom(finalGeom);

    if (iMSFinale != null && !iMSFinale.isEmpty() && iMSFinale.area() > 0.5) {
      GeometricConstraints gC = new GeometricConstraints(
          "Recul par rapport aux limites latérales " + r.getArt_72()
              + " m ",
          iMS, CODE_DISTANCE_LAT);
      return gC;
    }
    return null;
  }

}
