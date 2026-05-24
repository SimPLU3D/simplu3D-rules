package fr.ign.cogit.simplu3d.checker.impl;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.model.AbstractRuleChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;
import fr.ign.common.conversion.FromGeomToSurface;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

//FIXME ADD TEST
public class DistanceBetweenBuildingsChecker extends AbstractRuleChecker {

	public final static String CODE_DISTANCE_BULDINGS = "RECUL_BATIMENT";

	public DistanceBetweenBuildingsChecker(ZoneRegulation rules) {
		super(rules);
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		List<Building> buildingsNew = new ArrayList<>();

		if (this.getRules() != null && this.getRules().getArt8() != 99) {
			return lUNR;
		}

		for (Building b : bPU.getBuildings()) {
			if (b.isNew()) {
				buildingsNew.add(b);
			}
		}

		for (Building b : bPU.getBuildings()) {
			for (Building bNew : buildingsNew) {
				if (b.equals(bNew)) {
					continue;
				}

				double distanceMeasured = b.getFootprint().distance(bNew.getFootprint());
				if (distanceMeasured < this.getRules().getArt8()) {
					continue;
				}

				lUNR.add(new UnrespectedRule("Distance entre bâtiments non respectées", null, CODE_DISTANCE_BULDINGS));

			}
		}

		return lUNR;
	}

	@Override
	public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
		List<GeometricConstraints> geomConstraints = new ArrayList<>();

		ZoneRegulation r1 = this.getRules();

		if (r1 != null && r1.getArt8() != 99) {

			GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(bPU, r1);
			if (gc != null) {
				geomConstraints.add(gc);

			}

		}

		return geomConstraints;

	}

	private GeometricConstraints generateGEometricConstraintsForOneRegulation(BasicPropertyUnit bPU, ZoneRegulation r1) {

		List<Polygon> polygons = new ArrayList<>();
		for (Building b : bPU.getBuildings()) {
			polygons.addAll(FromGeomToSurface.convertGeom(b.getFootprint().buffer(r1.getArt8())));
		}
		if (polygons.isEmpty()) {
			return null;
		}
		MultiPolygon ims = bPU.getGeometry().getFactory().createMultiPolygon(polygons.toArray(new Polygon[0]));
        return new GeometricConstraints("Distance entre bâtiments de " + r1.getArt8() + " m", ims,
                CODE_DISTANCE_BULDINGS);
	}

}
