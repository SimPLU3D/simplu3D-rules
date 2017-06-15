package fr.ign.cogit.simplu3d.checker.impl;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.model.AbstractRuleChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;

public class ParkingChecker extends AbstractRuleChecker {

	public final static String CODE_PARKING = "PLACE_PARKINGS";

	public ParkingChecker(ZoneRegulation rules) {
		super(rules);
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		return lUNR;
	}

	@Override
	public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
		List<GeometricConstraints> geomConstraints = new ArrayList<>();

		if (this.getRules() != null && this.getRules().getArt12() != 99) {

			GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(bPU, this.getRules());
			if (gc != null) {
				geomConstraints.add(gc);

			}

		}

		return geomConstraints;

	}

	private GeometricConstraints generateGEometricConstraintsForOneRegulation(BasicPropertyUnit bPU, ZoneRegulation r1) {

		return new GeometricConstraints("Nombre de places de stationnement par logement : " + r1.getArt12(),
				bPU.getGeom(), CODE_PARKING);
	}

}
