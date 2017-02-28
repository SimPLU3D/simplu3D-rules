package fr.ign.cogit.simplu3d.checker.experiments;

import java.util.List;

import fr.ign.cogit.simplu3d.checker.impl.BandsChecker;
import fr.ign.cogit.simplu3d.checker.impl.BuildingInParcelChecker;
import fr.ign.cogit.simplu3d.checker.impl.CESChecker;
import fr.ign.cogit.simplu3d.checker.impl.DistanceInconsBotChecker;
import fr.ign.cogit.simplu3d.checker.model.CompositeChecker;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.Rules;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;

/**
 * 
 * Facade for rules validation
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class RennesChecker {

	/**
	 * Permet de vérifier les règles sur une BPU à partir d'un ensemble de
	 * règles
	 * 
	 * @param bPU
	 *            Une Basic Property Unit
	 * @param rules
	 *            Les règles d'urbanisme
	 * @return une liste de règles non respectées
	 */
	public static List<UnrespectedRule> check(BasicPropertyUnit bPU, Rules rules, RuleContext context) {
		CompositeChecker checker = new CompositeChecker();
		checker.addChild(new CESChecker(rules));
		checker.addChild(new BuildingInParcelChecker());
		checker.addChild(new DistanceInconsBotChecker(rules));
		checker.addChild(new BandsChecker(rules));
		return checker.check(bPU, context);
	}

}
