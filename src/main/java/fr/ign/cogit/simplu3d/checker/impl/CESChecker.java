package fr.ign.cogit.simplu3d.checker.impl;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.model.AbstractRuleChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.Rules;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.Regulation;

public class CESChecker extends AbstractRuleChecker {

	public final static String CODE_CES = "CES_MAX";

	public CESChecker(Rules r) {
		super(r);
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		if (this.getRules().getArt_9() == 99 ||  this.getRules().getArt_9() == 88) {
			return lUNR;
		}

		double totalArea = 0;
		for (Building b : bPU.getBuildings()) {
			totalArea = totalArea + b.getFootprint().area();

		}

		double aireParcelle = bPU.getGeom().area();

		double calculateCES = totalArea / aireParcelle;

		if (calculateCES > this.getRules().getArt_9()) {
			lUNR.add(new UnrespectedRule("Le CES n'est pas respecté - CES mesuré : " + calculateCES + "  CES attendu "
					+ this.getRules().getArt_9(), bPU.getGeom(), CODE_CES));

		}

		return lUNR;
	}

	@Override
	public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
		List<GeometricConstraints> geomConstraints = new ArrayList<>();

		Regulation r1 = this.getRules();

		if (r1 != null && r1.getArt_12() != 99) {

			GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(bPU, r1);
			if (gc != null) {
				geomConstraints.add(gc);

			}

		}

		return geomConstraints;

	}

	private GeometricConstraints generateGEometricConstraintsForOneRegulation(BasicPropertyUnit bPU, Regulation r1) {

		return new GeometricConstraints(
				"Part minimale d'espaces libres de toute la surface de la parcelle" + r1.getArt_12(), bPU.getGeom(),
				CODE_CES);
	}

}
