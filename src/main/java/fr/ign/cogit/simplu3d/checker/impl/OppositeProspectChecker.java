package fr.ign.cogit.simplu3d.checker.impl;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.model.AbstractRuleChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;
import fr.ign.common.conversion.FromGeomToLineString;
import fr.ign.common.equation.ProspectCalculation;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

//FIXME ADD TEST
public class OppositeProspectChecker extends AbstractRuleChecker {

	public final static String CODE_PROSPECT_LIMITE = "OPPOSITE_PROSPECT";

	private MultiLineString ims;

	public OppositeProspectChecker(ZoneRegulation rules) {
		super(rules);
	}

	private void prepare(BasicPropertyUnit bPU) {
		List<ParcelBoundary> lPB = bPU.getCadastralParcels().get(0).getBoundariesByType(ParcelBoundaryType.ROAD);
		List<LineString> lineStrings = new ArrayList<>();
		for (ParcelBoundary pB : lPB) {
			ParcelBoundary oppositeBoundary = pB.getOppositeBoundary();
			if (oppositeBoundary == null)
				continue;
			lineStrings.addAll(FromGeomToLineString.convert(oppositeBoundary.getGeometry()));
		}
		ims = bPU.getGeometry().getFactory().createMultiLineString(lineStrings.toArray(new LineString[0]));
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		this.prepare(bPU);
        List<AbstractBuilding> lBuildings = new ArrayList<>(bPU.getBuildings());
		return checkOppositeProspect(bPU, lBuildings, context);
	}

	public List<UnrespectedRule> checkOppositeProspect(BasicPropertyUnit bPU, List<AbstractBuilding> lBuildings,
			RuleContext context) {
		this.prepare(bPU);
		List<UnrespectedRule> lUNR = new ArrayList<>();
		if (lBuildings.isEmpty()) {
			return lUNR;
		}
		if (ims.isEmpty()) {
			return lUNR;
		}
		for (AbstractBuilding b : lBuildings) {
			boolean bool;
			bool = b.prospect(ims, this.getRules().getSlopeOppositeProspect(),
					this.getRules().gethIniOppositeProspect());
			if (!bool && context.isStopOnFailure()) {
				lUNR.add(null);
				return lUNR;
			}
			if (!bool) {
				lUNR.add(new UnrespectedRule("Prospect non respecté", b.getGeometry(), "Prospect"));
			}
		}
		return lUNR;
	}

	@Override
	public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
		List<GeometricConstraints> lGeom = new ArrayList<>();
		this.prepare(bPU);
		if (ims.isEmpty()) {
			return lGeom;
		}
		double coeff = this.getRules().getSlopeOppositeProspect();
		double hIni = this.getRules().gethIniOppositeProspect();
		if (this.getRules().getGeomBande().isEmpty() || coeff == 0) {
			return lGeom;
		}
		for (int index = 0; index < ims.getNumGeometries(); index++) {
			LineString curveTemp = (LineString) ims.getGeometryN(index);
			if (!curveTemp.buffer(0.5).intersects(this.getRules().getGeomBande())) {
				continue;
			}
			Geometry geom = ProspectCalculation.calculate(this.getRules().getGeomBande(), curveTemp, coeff, hIni);
			if (geom != null) {
				lGeom.add(new GeometricConstraints(
						"Distance minimale des constructions par rapport aux limites séparatives relative à a hauteur du bâtiment. Retrait  1/"
								+ coeff,
						geom, CODE_PROSPECT_LIMITE));
			}
		}
		return lGeom;
	}
}
