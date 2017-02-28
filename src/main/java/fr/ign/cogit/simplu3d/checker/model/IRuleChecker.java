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


	public abstract List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context);

	
	public abstract List<GeometricConstraints> generate(BasicPropertyUnit bPU , RuleContext context);

}
