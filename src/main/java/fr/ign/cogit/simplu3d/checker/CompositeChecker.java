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
	
	
	
	public List<IRuleChecker> getChildren(){
		return children;
	}
	
	public void addChild(IRuleChecker child){
		this.children.add(child);
	}


	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		List<UnrespectedRule> unrespectedRules = new ArrayList<>();
		for (IRuleChecker child : children) {
			unrespectedRules.addAll(child.check(bPU, context));
			// optional processing stop on failure
			if ( context.isStopOnFailure() && ! unrespectedRules.isEmpty() ){
				return unrespectedRules;
			}
		}
		return unrespectedRules;
	}
	

    @Override
    public List<GeometricConstraints> generate(BasicPropertyUnit bPU) {
      // TODO Auto-generated method stub
      return null;
    }
	
}
