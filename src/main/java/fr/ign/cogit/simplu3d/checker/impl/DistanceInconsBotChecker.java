package fr.ign.cogit.simplu3d.checker.impl;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.model.AbstractRuleChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;
import fr.ign.common.extrusion.Extrusion2DObject;
import fr.ign.common.conversion.FromGeomToLineString;
import fr.ign.common.conversion.FromGeomToSurface;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;

/**
 * Une bande de getBandIncons() m (par rapport au fond de la parcelle) est
 * inconstructible
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class DistanceInconsBotChecker extends AbstractRuleChecker {

	public final static String CODE_BANDE_FOND_PARCELLE = "BANDE_FOND_PARCELLE";

	public DistanceInconsBotChecker(ZoneRegulation rules) {
		super(rules);
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		List<UnrespectedRule> lUNR = new ArrayList<>();
		// On récupère la limite de fond
		MultiLineString ims = getBotLimit(bPU);
		if (ims.isEmpty()) {
			return lUNR;
		}
		// On récupère les parties de bâtiments
		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			for (SubParcel sP : cP.getSubParcels()) {
				for (AbstractBuilding bP : sP.getBuildingsParts()) {
					// On vérifie si la distance est bonne
					if (bP.getFootprint().distance(ims) < this.getRules().getBandIncons()) {
						Geometry buffer = ims.buffer(this.getRules().getBandIncons());
						Geometry geomError = cP.getGeometry().intersection(buffer);
						lUNR.add(new UnrespectedRule("Bande de constructibilité de fond de parcelle non respectée ",
								geomError, CODE_BANDE_FOND_PARCELLE));
					}
				}
			}
		}
		return lUNR;
	}

	/**
	 * TODO describe
	 * 
	 * @param bPU unit
	 * @return limits
	 */
	private MultiLineString getBotLimit(BasicPropertyUnit bPU) {
		List<LineString> lineStrings = new ArrayList<>();
		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			for (ParcelBoundary sc : cP.getBoundaries()) {
				if (sc.getParcelBoundaryType() == ParcelBoundaryType.BOT) {
					lineStrings.addAll(FromGeomToLineString.convert(sc.getGeometry()));
				}
			}
		}
		return bPU.getGeometry().getFactory().createMultiLineString(lineStrings.toArray(new LineString[0]));
	}

	@Override
	public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
		List<GeometricConstraints> geomConstraints = new ArrayList<>();
		MultiLineString iCurve = this.getBotLimit(bPU);
		if (this.getRules() == null && this.getRules().getArt6() != 99) {
			GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(bPU, this.getRules(), iCurve);
			if (gc != null) {
				geomConstraints.add(gc);
			}
		}
		return geomConstraints;
	}

	private GeometricConstraints generateGEometricConstraintsForOneRegulation(BasicPropertyUnit bPU, ZoneRegulation r,
	                                                                          MultiLineString iCurve) {
		Geometry geom = bPU.getGeometry().intersection(iCurve.buffer(r.getArt6()));
		MultiPolygon iMS = FromGeomToSurface.convertMSGeom(geom);

		Geometry finalGeom = iMS.intersection(r.getGeomBande());
		MultiPolygon iMSFinale = FromGeomToSurface.convertMSGeom(finalGeom);

		if (iMSFinale != null && !iMSFinale.isEmpty() && iMSFinale.getArea() > 0.5) {
			Geometry returnedGeom = Extrusion2DObject.convertFromGeometry(iMSFinale, 0, 0);
            return new GeometricConstraints(
                    "Recul d'une distance de " + r.getArt6() + " m par rapport à la voirie", returnedGeom, "Art 6");
		}
		return null;
	}

}
