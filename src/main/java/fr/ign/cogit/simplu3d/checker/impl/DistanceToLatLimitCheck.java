package fr.ign.cogit.simplu3d.checker.impl;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToLineString;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.calculation.Cut3DGeomFrom2D;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.simplu3d.checker.model.AbstractRuleChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.Rules;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.Regulation;

public class DistanceToLatLimitCheck extends AbstractRuleChecker {

	public final static String CODE_DISTANCE_LAT = "RECUL_LATERAL";

	public DistanceToLatLimitCheck() {

	}

	public DistanceToLatLimitCheck(Rules rules) {
		super(rules);
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {

		List<UnrespectedRule> lUR = new ArrayList<>();

		IMultiCurve<IOrientableCurve> iCurve = this.getBotLimit(bPU);

		if (this.getRules() != null && this.getRules().getArt_6() != 99) {

			UnrespectedRule gc = generateUnrespectedRulesOneReg(bPU, this.getRules(), iCurve);
			if (gc != null) {
				lUR.add(gc);

			}

		}

		return lUR;
	}

	public UnrespectedRule generateUnrespectedRulesOneReg(BasicPropertyUnit bPU, Regulation r,
			IMultiCurve<IOrientableCurve> iCurve) {

		for (Building b : bPU.getBuildings()) {
			if (!b.isNew) {
				continue;
			}

			GeometricConstraints gc = this.generateGEometricConstraintsForOneRegulation(bPU, r, iCurve);

			if (gc == null || gc.getGeometry() == null || gc.getGeometry().isEmpty()) {
				return null;
			}

			List<IOrientableSurface> os = FromGeomToSurface.convertGeom(gc.getGeometry());

			double distance = b.getFootprint().distance(iCurve);
			if (distance > r.getArt_72()) {

				continue;

			}

			for (IOrientableSurface osTemp : os) {

				List<IOrientableSurface> lOS = Cut3DGeomFrom2D
						.cutListSurfaceFromPolygon(FromGeomToSurface.convertGeom(b.getGeom()), (IPolygon) osTemp);

				IMultiSurface<IOrientableSurface> ims = new GM_MultiSurface<>(lOS);

				UnrespectedRule uRout = new UnrespectedRule(
						"Recul par rapport aux limites séparatives latérales non respectées " + distance
								+ " m au lieu de " + r.getArt_72() + " m",
						ims, CODE_DISTANCE_LAT);

				return uRout;

			}

		}

		return null;

	}

	/**
	 * TODO describe
	 * 
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
	public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
		List<GeometricConstraints> geomConstraints = new ArrayList<>();

		IMultiCurve<IOrientableCurve> iCurve = this.getBotLimit(bPU);

		if (this.getRules() != null && this.getRules().getArt_71() != 99) {

			GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(bPU, this.getRules(), iCurve);
			if (gc != null) {
				geomConstraints.add(gc);

			}

		}

		return geomConstraints;

	}

	private GeometricConstraints generateGEometricConstraintsForOneRegulation(BasicPropertyUnit bPU, Regulation r,
			IMultiCurve<IOrientableCurve> iCurve) {

		if (iCurve == null || iCurve.isEmpty()) {
			return null;
		}

		if (r.getArt_71() != 0) {
			return new GeometricConstraints("Alignement contre une des limites latérales", iCurve.buffer(0.5),
					CODE_DISTANCE_LAT);

		}
		IGeometry geom = r.getGeomBande().intersection(iCurve.buffer(r.getArt_72()));
		IMultiSurface<IOrientableSurface> iMS = FromGeomToSurface.convertMSGeom(geom);

		IGeometry finalGeom = iMS.intersection(r.getGeomBande());
		IMultiSurface<IOrientableSurface> iMSFinale = FromGeomToSurface.convertMSGeom(finalGeom);

		if (iMSFinale != null && !iMSFinale.isEmpty() && iMSFinale.area() > 0.5) {
			GeometricConstraints gC = new GeometricConstraints(
					"Recul par rapport aux limites latérales " + r.getArt_72() + " m ", iMS, CODE_DISTANCE_LAT);
			return gC;
		}
		return null;
	}

}
