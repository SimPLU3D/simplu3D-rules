package fr.ign.cogit.simplu3d.checker.impl;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.simplu3d.checker.model.AbstractRuleChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.util.JTS;

public class BuildingInParcelChecker extends AbstractRuleChecker {

	public static final String CODE = "BUILDING_IN_PARCEL";

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		List<UnrespectedRule> unrespectedRules = new ArrayList<>();

		// TODO see why bPU.getBuildings() is empty (loader issue?)

		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			for (SubParcel sP : cP.getSubParcels()) {
				Geometry parcelGeometry = JTS.toJTS(sP.getGeom());
				for (AbstractBuilding bP : sP.getBuildingsParts()) {
					Geometry footprint = JTS.toJTS(bP.getFootprint());
					if (footprint == null) {
						throw new RuntimeException("No footprint!");
					}
					if (parcelGeometry.contains(footprint)) {
						continue;
					}
					Geometry difference = footprint.difference(parcelGeometry);
					double area = difference.getArea();
					if (area < 1.0) {
						continue;
					}
					unrespectedRules.add(new UnrespectedRule("Le bâtiment est en dehors de la parcelle",
							JTS.fromJTS(difference), CODE));
				}
			}
		}

		return unrespectedRules;
	}

	@Override
	public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
		List<GeometricConstraints> lGC = new ArrayList<>();
		lGC.add(new GeometricConstraints("Le bâtiment doit être à l'intérieur de la parcelle",
				bPU.getGeom().buffer(50).difference(bPU.getGeom()), CODE));
		return lGC;
	}

}
