package fr.ign.cogit.simplu3d.checker;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;

public class CheckHeight implements IRuleChecker {

	private double heightMax;

	public CheckHeight(double heightMax) {
		super();
		this.heightMax = heightMax;
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {

		List<AbstractBuilding> lBuildings = new ArrayList<>();




			lBuildings.addAll(bPU.getBuildings());
		
		return checkHeight(bPU, lBuildings, context);

	}

	public List<UnrespectedRule> checkHeight(BasicPropertyUnit bPU, List<AbstractBuilding> lBuildings,
			RuleContext context) {
		List<UnrespectedRule> lUNR = new ArrayList<UnrespectedRule>();

		if (lBuildings.isEmpty()) {
			return lUNR;
		}

		for (AbstractBuilding b : lBuildings) {

			boolean bool = (b.height(0, 1) < heightMax);

			if (!bool & context.isStopOnFailure()) {
				lUNR.add(null);
				return lUNR;

			}

			if (!bool) {
				lUNR.add(new UnrespectedRule("Hauteur non respectée", b.getGeom(), "Hauteur maximale"));
			}

		}

		return lUNR;
	}

  @Override
  public List<GeometricConstraints> generate(BasicPropertyUnit bPU) {
    // TODO Auto-generated method stub
    return null;
  }

}
