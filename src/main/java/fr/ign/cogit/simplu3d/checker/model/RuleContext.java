package fr.ign.cogit.simplu3d.checker.model;

/**
 * 
 * Execution parameters for RuleChecker
 * 
 * @author MBrasebin
 *
 */
public class RuleContext {

	private boolean stopOnFailure = false;

	public boolean isStopOnFailure() {
		return stopOnFailure;
	}

	public void setStopOnFailure(boolean stopOnFailure) {
		this.stopOnFailure = stopOnFailure;
	}

}
