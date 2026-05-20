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
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;
import fr.ign.common.conversion.FromGeomToLineString;
import fr.ign.common.equation.ProspectCalculation;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

public class ProspectToLatLimitChecker extends AbstractRuleChecker {

	public final static String CODE_PROSPECT_LIMITE = "PROSPECT_EXISTING";

	public ProspectToLatLimitChecker(ZoneRegulation rules) {
		super(rules);
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		List<UnrespectedRule> lUNR = new ArrayList<>();
		List<MultiLineString> limits = getBotLimit(bPU);
		if (limits.isEmpty()) {
			return lUNR;
		}
		List<Building> buildingsTemp = bPU.getBuildings();
		List<Building> buildings = new ArrayList<>();
		for (Building b : buildingsTemp) {
			if (b.isNew()) {
				buildings.add(b);
			}
		}
		if (buildings.isEmpty()) {
			return lUNR;
		}
		ZoneRegulation r = this.getRules();
		for (MultiLineString iMC : limits) {
			for (Building b : buildings) {
				if (r != null && r.getGeomBande() != null && r.getArt10top() != 88 && r.getArt10top() != 99) {
					int coeff = r.getArt74();
					if (b.getFootprint().intersects(r.getGeomBande()) && coeff != 0) {
						boolean respected = b.prospect(iMC, coeff, 0);
						if (!respected) {
							lUNR.add(new UnrespectedRule("Prospect non respecté par rapport aux limites séparatives",
									bPU.getGeometry(), CODE_PROSPECT_LIMITE));
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
            geomConstraints.addAll(gc);
        }
		return geomConstraints;
	}

	private List<MultiLineString> getBotLimit(BasicPropertyUnit bPU) {
		List<LineString> bot = new ArrayList<>();
		List<LineString> left = new ArrayList<>();
		List<LineString> right = new ArrayList<>();

		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			for (ParcelBoundary sc : cP.getBoundaries()) {
				if (sc.getParcelBoundaryType().equals(ParcelBoundaryType.BOT)) {
					bot.addAll(FromGeomToLineString.convert(sc.getGeometry()));
				}
				if ((sc.getSide().equals(ParcelBoundarySide.LEFT)) && (sc.getParcelBoundaryType().equals(ParcelBoundaryType.LAT))) {
					left.addAll(FromGeomToLineString.convert(sc.getGeometry()));
				}
				if ((sc.getSide().equals(ParcelBoundarySide.RIGHT)) && (sc.getParcelBoundaryType().equals(ParcelBoundaryType.LAT))) {
					right.addAll(FromGeomToLineString.convert(sc.getGeometry()));
				}
			}
		}
		List<MultiLineString> img = new ArrayList<>();
		GeometryFactory factory = bPU.getGeometry().getFactory();
		img.add(factory.createMultiLineString(bot.toArray(new LineString[0])));
		img.add(factory.createMultiLineString(left.toArray(new LineString[0])));
		img.add(factory.createMultiLineString(right.toArray(new LineString[0])));
		return img;
	}

	private List<GeometricConstraints> generateGEometricConstraintsForOneRegulation(BasicPropertyUnit bPU,
			ZoneRegulation r) {
		List<MultiLineString> imc = getBotLimit(bPU);
		int coeff = r.getArt74();
		List<GeometricConstraints> lGeom = new ArrayList<>();
		if (r.getGeomBande().isEmpty() || coeff == 0) {
			return lGeom;
		}
		for (MultiLineString curveTemp : imc) {
			if (!curveTemp.buffer(0.5).intersects(r.getGeomBande())) {
				continue;
			}
			Geometry geom = ProspectCalculation.calculate(r.getGeomBande(), curveTemp, 1.0 / coeff, 0);
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
