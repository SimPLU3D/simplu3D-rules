package fr.ign.cogit.simplu3d.checker.impl;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.model.AbstractRuleChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;
import fr.ign.common.analysis.Cut3DGeomFrom2D;
import fr.ign.common.conversion.FromGeomToLineString;
import fr.ign.common.conversion.FromGeomToSurface;
import org.locationtech.jts.geom.*;

public class DistanceToRoadCheck extends AbstractRuleChecker  {

  public final static String CODE_DISTANCE_VOIRIE = "RECUL_VOIRIE";
  


  public DistanceToRoadCheck(ZoneRegulation rules) {
	  super(rules);
  }

  @Override
  public List<UnrespectedRule> check(BasicPropertyUnit bPU,
      RuleContext context) {

    List<UnrespectedRule> lUR = new ArrayList<>();
    MultiLineString iCurve = this.getBotLimit(bPU);
    if (this.getRules() != null && this.getRules().getArt6() != 99) {
      UnrespectedRule gc = generateUnrespectedRulesOneReg(bPU, this.getRules(), iCurve);
      if (gc != null) {
        lUR.add(gc);
      }
    }
    return lUR;
  }

  public UnrespectedRule generateUnrespectedRulesOneReg(BasicPropertyUnit bPU,
      ZoneRegulation r, MultiLineString iCurve) {
    for (Building b : bPU.getBuildings()) {
      if (!b.isNew()) {
        continue;
      }
      GeometricConstraints gc = this
          .generateGEometricConstraintsForOneRegulation(bPU, r, iCurve);
      if(gc == null ||gc.getGeometry() == null ||gc.getGeometry().isEmpty()){
        return null;
      }
      List<Polygon> os = FromGeomToSurface.convertGeom(gc.getGeometry());
      double distance = b.getFootprint().distance(iCurve);
      if (distance > r.getArt6()) {
        continue;
      }
      for (Polygon osTemp : os) {
        List<Polygon> lOS = Cut3DGeomFrom2D
            .cutListSurfaceFromPolygon(
                FromGeomToSurface.convertGeom(b.getGeometry()), osTemp);
        MultiPolygon ims = bPU.getGeometry().getFactory().createMultiPolygon(lOS.toArray(new Polygon[0]));
          return new UnrespectedRule(
              "Recul par rapport à la voirie non respectée " + distance
                  + " m au lieu de " + r.getArt6() + " m",
              ims, CODE_DISTANCE_VOIRIE);
      }
    }
    return null;
  }

  /**
   * TODO describe
   * @param bPU unit
   * @return limit
   */
  private MultiLineString getBotLimit(BasicPropertyUnit bPU) {
    List<LineString> lineStrings = new ArrayList<>();
    for (CadastralParcel cP : bPU.getCadastralParcels()) {
      for (ParcelBoundary sc : cP.getBoundaries()) {
        if (sc.getParcelBoundaryType() == ParcelBoundaryType.ROAD) {
          lineStrings.addAll(FromGeomToLineString.convert(sc.getGeometry()));
        }
      }
    }
    return bPU.getGeometry().getFactory().createMultiLineString(lineStrings.toArray(new LineString[0]));
  }

  @Override
  public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
    List<GeometricConstraints> geomConstraints = new ArrayList<>();
    MultiLineString iCurve = this.getBotLimit(bPU);
    if (this.getRules()!= null && this.getRules().getArt6() != 99) {
      GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(
          bPU, this.getRules(), iCurve);
      if (gc != null) {
        geomConstraints.add(gc);
      }
    }
    return geomConstraints;
  }

  private GeometricConstraints generateGEometricConstraintsForOneRegulation(
      BasicPropertyUnit bPU, ZoneRegulation r,
      MultiLineString iCurve) {
    Geometry geom = bPU.getGeometry().intersection(iCurve.buffer(r.getArt6()));
    MultiPolygon iMS = FromGeomToSurface.convertMSGeom(geom);
    Geometry finalGeom = iMS.intersection(r.getGeomBande());
    MultiPolygon iMSFinale = FromGeomToSurface.convertMSGeom(finalGeom);
    if (iMSFinale != null && !iMSFinale.isEmpty() && iMSFinale.getArea() > 0.5) {
        return new GeometricConstraints(
            "Recul d'une distance de " + r.getArt6()
                + " m par rapport à la voirie",
            iMS, CODE_DISTANCE_VOIRIE);
    }
    return null;
  }
}
