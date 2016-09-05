package fr.ign.cogit.simplu3d.checker;

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

	
	
}
