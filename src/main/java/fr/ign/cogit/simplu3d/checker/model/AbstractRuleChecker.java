package fr.ign.cogit.simplu3d.checker.model;

import java.util.logging.Logger;

import fr.ign.cogit.simplu3d.model.Rules;

/**
 * 
 * Abstract checker with rules attribute
 * 
 * @author MBrabesin
 *
 */
public abstract class AbstractRuleChecker implements IRuleChecker {

	protected final Logger logger = Logger.getLogger(getClass().getCanonicalName());

	public AbstractRuleChecker() {

	}

	public AbstractRuleChecker(Rules rules) {
		this.setRules(rules);
	}

	private Rules rules;

	public Rules getRules() {
		return rules;
	}

	public void setRules(Rules rules) {
		this.rules = rules;
	}

}
