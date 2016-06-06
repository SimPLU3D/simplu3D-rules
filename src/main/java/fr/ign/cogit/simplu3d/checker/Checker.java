package fr.ign.cogit.simplu3d.checker;

import java.util.List;

import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;

/**
 * 
 * Facade for rules validation
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class Checker {
	
	/**
	 * Permet de vérifier les règles sur une BPU à partir d'un ensemble de règles
	 * 
	 * @param bPU
	 *            Une Basic Property Unit
	 * @param rules
	 *            Les règles d'urbanisme
	 * @return une liste de règles non respectées
	 */
	public static List<UnrespectedRule> check(BasicPropertyUnit bPU, Rules rules){
		boolean stopOnFailure = false;
		return check(bPU, rules, stopOnFailure);
	}
	

	/**
	 * Permet de vérifier les règles sur une BPU à partir d'un ensemble de règles
	 * 
	 * @param bPU
	 *            Une Basic Property Unit
	 * @param rules
	 *            Les règles d'urbanisme
	 * @return une liste de règles non respectées
	 */
	public static List<UnrespectedRule> check(BasicPropertyUnit bPU, Rules rules, boolean stopOnFailure) {
		CompositeChecker checker = new CompositeChecker();
		checker.setStopOnFailure(stopOnFailure);
		checker.addChild(new CheckerCES(rules));
		checker.addChild(new BuildingInParcelChecker());
		checker.addChild(new DistanceInconsBotChecker(rules));
		checker.addChild(new BandsChecker(rules));
		return checker.check(bPU);
	}

}
