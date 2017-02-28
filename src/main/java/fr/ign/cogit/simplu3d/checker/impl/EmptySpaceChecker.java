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

public class EmptySpaceChecker extends AbstractRuleChecker {

	public final static String CODE_EMPTY_SPACE = "ESPACE LIBRE";

	public  EmptySpaceChecker(Rules rules) {
		super(rules);
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		if (this.getRules().getArt_12() == 99 || this.getRules().getArt_12() == 88) {
			return lUNR;
		}

		double totalArea = 0;
		for (Building b : bPU.getBuildings()) {
			totalArea = totalArea + b.getFootprint().area();

		}

		double aireParcelle = bPU.getGeom().area();

		double calculateEmptySpace = 1 - totalArea / aireParcelle;

		if (calculateEmptySpace < this.getRules().getArt_12()) {
			lUNR.add(new UnrespectedRule("Le taux d'espace vide n'est pas respecté. Mesurée : " + calculateEmptySpace
					+ "   attendu " + this.getRules().getArt_12(), bPU.getGeom(), CODE_EMPTY_SPACE));
		}

		return lUNR;
	}

	@Override
	public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
		List<GeometricConstraints> geomConstraints = new ArrayList<>();

		if (this.getRules() != null && this.getRules().getArt_12() != 99) {

			GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(bPU, this.getRules());
			if (gc != null) {
				geomConstraints.add(gc);

			}

		}

		return geomConstraints;

	}

	private GeometricConstraints generateGEometricConstraintsForOneRegulation(BasicPropertyUnit bPU, Regulation r1) {

		return new GeometricConstraints("Coefficient d’emprise au sol maximum" + r1.getArt_12(), bPU.getGeom(),
				CODE_EMPTY_SPACE);
	}

}
