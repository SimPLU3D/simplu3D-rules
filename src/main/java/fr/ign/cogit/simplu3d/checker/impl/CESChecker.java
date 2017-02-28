package fr.ign.cogit.simplu3d.checker.impl;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.model.AbstractRuleChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.Rules;

public class CESChecker extends AbstractRuleChecker {

	public final static String CODE_CES = "CES_MAX";

	public CESChecker(Rules r) {
		super(r);
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		if (this.getRules().getArt9() == 99 || this.getRules().getArt9() == 88) {
			return lUNR;
		}

		double totalArea = 0;
		for (Building b : bPU.getBuildings()) {
			totalArea = totalArea + b.getFootprint().area();

		}

		double aireParcelle = bPU.getGeom().area();

		double calculateCES = totalArea / aireParcelle;

		double ces = determineCES(bPU);

		if (calculateCES > ces) {
			lUNR.add(new UnrespectedRule(
					"Le CES n'est pas respecté - CES mesuré : " + calculateCES + "  CES attendu " + ces, bPU.getGeom(),
					CODE_CES));

		}

		return lUNR;
	}

	private double determineCES(BasicPropertyUnit bPU) {

		boolean conditionchecked = true;

		// Est-ce que la superficie du terrain est inférieure ou égale à
		// getEmpriseSurface()
		double area = bPU.getGeom().area();
		if (area < this.getRules().getEmpriseSurface()) {
			conditionchecked = false;
		}

		// Si les 2 conditions sont vérifiées c'est
		double ces = this.getRules().getArt9();
		// Si une des deux n'est pas vérifiée, c'est l'autre
		if (!conditionchecked) {
			ces = this.getRules().getEmpriseSolAlt();
		}

		return ces;

	}

	@Override
	public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
		List<GeometricConstraints> geomConstraints = new ArrayList<>();

		Rules r1 = this.getRules();

		if (r1 != null && r1.getArt12() != 99) {

			GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(bPU, r1);
			if (gc != null) {
				geomConstraints.add(gc);

			}

		}

		return geomConstraints;

	}

	private GeometricConstraints generateGEometricConstraintsForOneRegulation(BasicPropertyUnit bPU, Rules r1) {

		double ces = determineCES(bPU);

		return new GeometricConstraints("Coefficient d'emprise au sol maximum  : " + ces, bPU.getGeom(), CODE_CES);
	}

}
