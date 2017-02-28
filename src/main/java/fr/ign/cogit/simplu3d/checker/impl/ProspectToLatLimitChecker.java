package fr.ign.cogit.simplu3d.checker.impl;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToLineString;
import fr.ign.cogit.geoxygene.sig3d.analysis.ProspectCalculation;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.checker.model.AbstractRuleChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;

public class ProspectToLatLimitChecker extends AbstractRuleChecker {

	public final static String CODE_PROSPECT_LIMITE = "PROSPECT_EXISTING";

	public ProspectToLatLimitChecker(ZoneRegulation rules) {
		super(rules);
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {

		List<UnrespectedRule> lUNR = new ArrayList<UnrespectedRule>();

		List<IMultiCurve<IOrientableCurve>> limits = getBotLimit(bPU);

		if (limits.isEmpty()) {
			return lUNR;
		}

		List<Building> buildingsTemp = bPU.getBuildings();
		List<Building> buildings = new ArrayList<>();

		for (Building b : buildingsTemp) {
			if (b.isNew) {
				buildings.add(b);
			}
		}

		if (buildings.isEmpty()) {
			return lUNR;
		}

		ZoneRegulation r = this.getRules();
		
		for (IMultiCurve<IOrientableCurve> iMC : limits) {
			for (Building b : buildings) {
				if (r != null && r.getGeomBande() != null && r.getArt10top() != 88 && r.getArt10top() != 99) {
					int coeff = r.getArt74();

					if (b.getFootprint().intersects(r.getGeomBande()) && coeff != 0) {

						boolean respected = b.prospect(iMC, coeff, 0);

						if (!respected) {
							lUNR.add(new UnrespectedRule("Prospect non respecté par rapport aux limites séparatives",
									bPU.getGeom(), CODE_PROSPECT_LIMITE));
						}
					}
				}
			}
		}

		return lUNR;
	}

	@Override
	public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
		List<GeometricConstraints> geomConstraints = new ArrayList<>();
		
		ZoneRegulation r = this.getRules();
		

		if (r != null && r.getArt74() != 99 && r.getArt74() != 0) {

			List<GeometricConstraints> gc = generateGEometricConstraintsForOneRegulation(bPU, r);
			if (gc != null) {
				geomConstraints.addAll(gc);

			}

		}

		return geomConstraints;

	}

	private List<IMultiCurve<IOrientableCurve>> getBotLimit(BasicPropertyUnit bPU) {
		List<IMultiCurve<IOrientableCurve>> img = new ArrayList<>();
		img.add(new GM_MultiCurve<>());
		img.add(new GM_MultiCurve<>());
		img.add(new GM_MultiCurve<>());

		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			for (ParcelBoundary sc : cP.getBoundaries()) {
				if (sc.getType().equals(ParcelBoundaryType.BOT)) {

					img.get(0).addAll(FromGeomToLineString.convert(sc.getGeom()));
				}

				if ((sc.getSide().equals(ParcelBoundarySide.LEFT)) && (sc.getType().equals(ParcelBoundaryType.LAT))) {

					img.get(1).addAll(FromGeomToLineString.convert(sc.getGeom()));
				}

				if ((sc.getSide().equals(ParcelBoundarySide.RIGHT)) && (sc.getType().equals(ParcelBoundaryType.LAT))) {

					img.get(2).addAll(FromGeomToLineString.convert(sc.getGeom()));
				}
			}
		}

		return img;
	}

	private List<GeometricConstraints> generateGEometricConstraintsForOneRegulation(BasicPropertyUnit bPU,
			ZoneRegulation r) {

		List<IMultiCurve<IOrientableCurve>> imc = getBotLimit(bPU);

		int coeff = r.getArt74();

		List<GeometricConstraints> lGeom = new ArrayList<>();

		if (r.getGeomBande().isEmpty() || coeff == 0) {
			return lGeom;
		}

		for (IMultiCurve<IOrientableCurve> curveTemp : imc) {

			if (!curveTemp.buffer(0.5).intersects(r.getGeomBande())) {
				continue;
			}
			IGeometry geom = ProspectCalculation.calculate(r.getGeomBande(), curveTemp, 1.0 / coeff, 0);

			if (geom != null) {
				lGeom.add(new GeometricConstraints(
						"Distance minimale des constructions par rapport aux limites séparatives relative à a hauteur du bâtiment. Retrait  1/"
								+ coeff,
						geom, CODE_PROSPECT_LIMITE));

			}

		}

		return lGeom;
	}

}
