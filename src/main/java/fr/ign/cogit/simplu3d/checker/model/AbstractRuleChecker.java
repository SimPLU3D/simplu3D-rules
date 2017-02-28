package fr.ign.cogit.simplu3d.checker.model;

import java.util.logging.Logger;

import fr.ign.cogit.simplu3d.model.ZoneRegulation;

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

	public AbstractRuleChecker(ZoneRegulation rules) {
		this.setRules(rules);
	}

	private ZoneRegulation rules;

	public ZoneRegulation getRules() {
		return rules;
	}

	public void setRules(ZoneRegulation rules) {
		this.rules = rules;
	}

}
