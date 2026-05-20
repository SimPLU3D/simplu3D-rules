package fr.ign.cogit.simplu3d.checker.model;

import java.util.List;

import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;

/**
 * 
 * Interface for RuleChecker
 * 
 * @author MBorne
 *
 */
public interface IRuleChecker {
	List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context);
	List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext context);
}
