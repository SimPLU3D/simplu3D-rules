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

	/**
	 * Check the given rule on a Parcel
	 * @param parcel
	 * @return
	 */
	public List<UnrespectedRule> check(BasicPropertyUnit bPU);
	
}
