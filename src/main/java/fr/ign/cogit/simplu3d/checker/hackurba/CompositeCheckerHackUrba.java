package fr.ign.cogit.simplu3d.checker.hackurba;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Regulation;

/**
 * 
 * A composite checker 
 * 
 * @author MBorne
 *
 */
public class CompositeCheckerHackUrba  {

	private List<IRuleChecker> children = new ArrayList<>();
	
	
	
	public List<IRuleChecker> getChildren(){
		return children;
	}
	
	public void addChild(IRuleChecker child){
		this.children.add(child);
	}


	public List<UnrespectedRule> check(BasicPropertyUnit bPU, Regulation r1, Regulation r2, RuleContext context) {
	  bPU.setR1(r1);
	  bPU.setR2(r2);
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
	


    public List<GeometricConstraints> generateGeometrycConstraints(
        BasicPropertyUnit bPU, Regulation r1, Regulation r2,
        RuleContext context) {
      
      bPU.setR1(r1);
      bPU.setR2(r2);
      List<GeometricConstraints> geometricConstraints = new ArrayList<>();
      for (IRuleChecker child : children) {
        geometricConstraints.addAll(child.generate(bPU));
        // optional processing stop on failure
        if ( context.isStopOnFailure() && ! geometricConstraints.isEmpty() ){
            return geometricConstraints;
        }
    }
      
      return geometricConstraints;
      
    }
	
}
