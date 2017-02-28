package fr.ign.cogit.simplu3d.checker.impl;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.simplu3d.checker.model.AbstractRuleChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;

public class HeightMaxChecker extends AbstractRuleChecker {

	public final static String CODE_HEIGHT_MAX = "HAUTEUR_MAX";

	public HeightMaxChecker(ZoneRegulation rules) {
		super(rules);
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {

		List<UnrespectedRule> lUNR = new ArrayList<>();

		List<Building> lBuilding = new ArrayList<>();
		for (Building b : bPU.getBuildings()) {
			if (b.isNew) {
				lBuilding.add(b);
			}
		}

		if (lBuilding.isEmpty()) {
			return lUNR;
		}

		for (AbstractBuilding b : lBuilding) {

			double height = b.height(1, 1);

			if (this.getRules() != null && this.getRules().getGeomBande() != null
					&& this.getRules().getArt10top() != 88 && this.getRules().getArt10top() != 99) {
				if (b.getFootprint().intersects(this.getRules().getGeomBande())) {
					double hauteurMax = this.getRules().getArt102();

					if (height > hauteurMax) {

						lUNR.add(new UnrespectedRule("Hauteur non respectée. Hauteur mesurée : " + height
								+ " - hauteur maximale " + hauteurMax, b.getGeom(), CODE_HEIGHT_MAX));

					}

				}
			}

		}

		return lUNR;
	}

	@Override
	public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
		List<GeometricConstraints> geomConstraints = new ArrayList<>();

		if (this.getRules() != null && this.getRules().getArt10top() != 88 && this.getRules().getArt10top() != 99) {

			GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(bPU, this.getRules());
			if (gc != null) {
				geomConstraints.add(gc);

			}

		}

		return geomConstraints;

	}

	private GeometricConstraints generateGEometricConstraintsForOneRegulation(BasicPropertyUnit bPU, ZoneRegulation rules) {
		if (rules.getGeomBande().isEmpty()) {
			return null;
		}
		double hauteurMax = rules.getArt102();

		return new GeometricConstraints("La hauteur maximale est : " + hauteurMax,
				Extrusion2DObject.convertFromGeometry(rules.getGeomBande(), hauteurMax, hauteurMax), CODE_HEIGHT_MAX);
	}
}