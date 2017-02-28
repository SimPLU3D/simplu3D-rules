package fr.ign.cogit.simplu3d.checker.experiments;

import java.util.List;

import fr.ign.cogit.simplu3d.checker.impl.CESChecker;
import fr.ign.cogit.simplu3d.checker.impl.DistanceBetweenBuildingsChecker;
import fr.ign.cogit.simplu3d.checker.impl.DistanceToBotLimitChecker;
import fr.ign.cogit.simplu3d.checker.impl.DistanceToLatLimitCheck;
import fr.ign.cogit.simplu3d.checker.impl.DistanceToRoadCheck;
import fr.ign.cogit.simplu3d.checker.impl.EmptySpaceChecker;
import fr.ign.cogit.simplu3d.checker.impl.HeightMaxChecker;
import fr.ign.cogit.simplu3d.checker.impl.ParkingChecker;
import fr.ign.cogit.simplu3d.checker.impl.ProspectToLatLimitChecker;
import fr.ign.cogit.simplu3d.checker.model.CompositeChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;
import fr.ign.cogit.simplu3d.util.SimpleBandProduction;

public class HackUrbaChecker {

	private static CompositeChecker prepareChecker(BasicPropertyUnit bPU, ZoneRegulation r1, ZoneRegulation r2, RuleContext context) {

		new SimpleBandProduction(bPU, r1, r2);

		CompositeChecker compositeChecker = new CompositeChecker();

		DistanceToRoadCheck checkDistanceBand1 = new DistanceToRoadCheck(r1);
		compositeChecker.addChild(checkDistanceBand1);

		DistanceToRoadCheck checkDistanceBand2 = new DistanceToRoadCheck(r2);
		compositeChecker.addChild(checkDistanceBand2);

		DistanceToBotLimitChecker checkBotLimitB1 = new DistanceToBotLimitChecker(r1);
		compositeChecker.addChild(checkBotLimitB1);

		DistanceToBotLimitChecker checkBotLimitB2 = new DistanceToBotLimitChecker(r2);
		compositeChecker.addChild(checkBotLimitB2);

		CESChecker checkCES = new CESChecker(r1);
		compositeChecker.addChild(checkCES);

		DistanceBetweenBuildingsChecker checkerBuildingBetweenBuildings = new DistanceBetweenBuildingsChecker(r1);
		compositeChecker.addChild(checkerBuildingBetweenBuildings);

		DistanceToLatLimitCheck checkDistanceLatB1 = new DistanceToLatLimitCheck(r1);
		compositeChecker.addChild(checkDistanceLatB1);

		DistanceToLatLimitCheck checkDistanceLatB2 = new DistanceToLatLimitCheck(r2);
		compositeChecker.addChild(checkDistanceLatB2);

		ProspectToLatLimitChecker checkPRospectToExistingBuildingB1 = new ProspectToLatLimitChecker(r1);
		compositeChecker.addChild(checkPRospectToExistingBuildingB1);

		ProspectToLatLimitChecker checkPRospectToExistingBuildingB2 = new ProspectToLatLimitChecker(r2);
		compositeChecker.addChild(checkPRospectToExistingBuildingB2);

		HeightMaxChecker checkHeightMax = new HeightMaxChecker(r1);
		compositeChecker.addChild(checkHeightMax);

		HeightMaxChecker checkHeightMax2 = new HeightMaxChecker(r2);
		compositeChecker.addChild(checkHeightMax2);

		ParkingChecker checkParking = new ParkingChecker(r1);
		compositeChecker.addChild(checkParking);

		EmptySpaceChecker checkEmpty = new EmptySpaceChecker(r2);
		compositeChecker.addChild(checkEmpty);

		return compositeChecker;

	}

	public static List<UnrespectedRule> check(BasicPropertyUnit bPU, ZoneRegulation r1, ZoneRegulation r2, RuleContext context) {

		return prepareChecker(bPU, r1, r2, context).check(bPU, context);

	}

	public static List<GeometricConstraints> generateConstraints(BasicPropertyUnit bPU, ZoneRegulation r1, ZoneRegulation r2,
			RuleContext context) {

		return prepareChecker(bPU, r1, r2, context).generate(bPU, context);

	}
}

