package fr.ign.cogit.simplu3d.checker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.operation.union.CascadedPolygonUnion;

import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;

public class CheckCES implements IRuleChecker {

  public final static String CODE_CES = "CES";

  private double cesMax;
  private BasicPropertyUnit bPU;

  public CheckCES(double cesMax, BasicPropertyUnit bPU) {
    super();
    this.cesMax = cesMax;
    this.bPU = bPU;

  }

  @Override
  public List<UnrespectedRule> check(BasicPropertyUnit bPU,
      RuleContext checker) {

    List<AbstractBuilding> lBuildings = new ArrayList<>();

    lBuildings.addAll(bPU.getBuildings());

    double airePAr = this.bPU.getGeom().area();

    try {
      return this.evaluateCES(airePAr, lBuildings, checker);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;
  }

  public List<UnrespectedRule> evaluateCES(double parcelleArea, List<AbstractBuilding> lBuildings, RuleContext context) throws Exception {

		List<UnrespectedRule> lUNR = new ArrayList<UnrespectedRule>();

		if (lBuildings.isEmpty()) {
			return lUNR;
		}

		int nbElem = lBuildings.size();
		
		
		Collection<Geometry> collGeom = new ArrayList<>();


		


		Geometry geomTemp;	
				

		for (int i = 0; i < nbElem; i++) {
			
		
				geomTemp = JtsGeOxygene.makeJtsGeom(lBuildings.get(i).getGeom());
		
			
			collGeom.add(geomTemp);

		}

		Geometry union = CascadedPolygonUnion.union(collGeom);
		
		double ces = (union.getArea() / parcelleArea);

		if (ces > cesMax) {
			
	

				lUNR.add(new UnrespectedRule("CES dépassé : " + ces + "> " + cesMax, JtsGeOxygene.makeGeOxygeneGeom(union), CODE_CES));
	
			
			
		}

		return lUNR;
	}

  @Override
  public List<GeometricConstraints> generate(BasicPropertyUnit bPU) {
    // TODO Auto-generated method stub
    return null;
  }

}
