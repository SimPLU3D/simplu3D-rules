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

//FIXME ADD TEST
public class DistanceToLatLimitCheck extends AbstractRuleChecker {

	public final static String CODE_DISTANCE_LAT = "RECUL_LATERAL";

	public DistanceToLatLimitCheck(ZoneRegulation rules) {
		super(rules);
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
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

	public UnrespectedRule generateUnrespectedRulesOneReg(BasicPropertyUnit bPU, ZoneRegulation r,
			MultiLineString iCurve) {
		for (Building b : bPU.getBuildings()) {
			if (!b.isNew()) {
				continue;
			}
			GeometricConstraints gc = this.generateGeometricConstraintsForOneRegulation(r, iCurve);
			if (gc == null || gc.getGeometry() == null || gc.getGeometry().isEmpty()) {
				return null;
			}
			List<Polygon> os = FromGeomToSurface.convertGeom(gc.getGeometry());
			double distance = b.getFootprint().distance(iCurve);
			if (distance > r.getArt72()) {
				continue;
			}
			for (Polygon osTemp : os) {
				List<Polygon> lOS = Cut3DGeomFrom2D
						.cutListSurfaceFromPolygon(FromGeomToSurface.convertGeom(b.getGeometry()), osTemp);
				MultiPolygon ims = bPU.getGeometry().getFactory().createMultiPolygon(lOS.toArray(new Polygon[0]));
                return new UnrespectedRule(
                        "Recul par rapport aux limites séparatives latérales non respectées " + distance
                                + " m au lieu de " + r.getArt72() + " m",
                        ims, CODE_DISTANCE_LAT);
			}
		}
		return null;
	}

	/**
	 * TODO describe
	 * 
	 * @param bPU unit
	 * @return limit
	 */
	private MultiLineString getBotLimit(BasicPropertyUnit bPU) {
		List<LineString> lineStrings = new ArrayList<>();
		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			for (ParcelBoundary sc : cP.getBoundaries()) {
				if (sc.getParcelBoundaryType() == ParcelBoundaryType.LAT) {
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
		if (this.getRules() != null && this.getRules().getArt_71() != 99) {
			GeometricConstraints gc = generateGeometricConstraintsForOneRegulation(this.getRules(), iCurve);
			if (gc != null) {
				geomConstraints.add(gc);
			}
		}
		return geomConstraints;
	}

	private GeometricConstraints generateGeometricConstraintsForOneRegulation(ZoneRegulation r,
	                                                                          MultiLineString iCurve) {
		if (iCurve == null || iCurve.isEmpty()) {
			return null;
		}
		if (r.getArt_71() != 0) {
			return new GeometricConstraints("Alignement contre une des limites latérales", iCurve.buffer(0.5),
					CODE_DISTANCE_LAT);
		}
		Geometry geom = r.getGeomBande().intersection(iCurve.buffer(r.getArt72()));
		MultiPolygon iMS = FromGeomToSurface.convertMSGeom(geom);
		Geometry finalGeom = iMS.intersection(r.getGeomBande());
		MultiPolygon iMSFinale = FromGeomToSurface.convertMSGeom(finalGeom);
		if (iMSFinale != null && !iMSFinale.isEmpty() && iMSFinale.getArea() > 0.5) {
            return new GeometricConstraints(
                    "Recul par rapport aux limites latérales " + r.getArt72() + " m ", iMS, CODE_DISTANCE_LAT);
		}
		return null;
	}
}
