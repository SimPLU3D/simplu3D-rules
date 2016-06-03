package fr.ign.cogit.simplu3d.checker;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;

/**
 * 
 * A composite checker 
 * 
 * @author MBorne
 *
 */
public class CompositeChecker implements IRuleChecker {

	private List<IRuleChecker> children = new ArrayList<>();
	
	/**
	 * Indicates if all child rules should be checked or
	 *  if process should stop when the first error is met
	 */
	private boolean stopOnFailure = false ;
	
	public List<IRuleChecker> getChildren(){
		return children;
	}
	
	public void addChild(IRuleChecker child){
		this.children.add(child);
	}

	public boolean isStopOnFailure() {
		return stopOnFailure;
	}

	public void setStopOnFailure(boolean stopOnFailure) {
		this.stopOnFailure = stopOnFailure;
	}
	
	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU) {
		List<UnrespectedRule> unrespectedRules = new ArrayList<>();
		for (IRuleChecker child : children) {
			unrespectedRules.addAll(child.check(bPU));
			// optional processing stop on failure
			if ( stopOnFailure && ! unrespectedRules.isEmpty() ){
				return unrespectedRules;
			}
		}
		return unrespectedRules;
	}
	
}
