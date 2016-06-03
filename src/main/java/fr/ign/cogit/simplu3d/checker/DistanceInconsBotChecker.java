package fr.ign.cogit.simplu3d.checker;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToLineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundaryType;

/**
 * Une bande de getBandIncons() m (par rapport au fond de la parcelle) est
 * inconstructible
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class DistanceInconsBotChecker implements IRuleChecker {

	public final static String CODE_BANDE_FOND_PARCELLE = "BANDE_FOND_PARCELLE";

	private Rules rules;

	public DistanceInconsBotChecker(Rules rules) {
		this.rules = rules;
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		// On récupère la limite de fond
		IMultiCurve<IOrientableCurve> ims = getBotLimit(bPU);

		if (ims.isEmpty()) {
			return lUNR;
		}

		// On récupère les parties de bâtiments
		for (CadastralParcel cP : bPU.getCadastralParcel()) {

			for (SubParcel sP : cP.getSubParcel()) {

				for (AbstractBuilding bP : sP.getBuildingsParts()) {

					// On vérifie si la distance est bonne
					if (bP.getFootprint().distance(ims) < rules.getBandIncons()) {

						IGeometry buffer = ims.buffer(rules.getBandIncons());

						IGeometry geomError = cP.getGeom().intersection(buffer);

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
	 * @param bPU
	 * @return
	 */
	private IMultiCurve<IOrientableCurve> getBotLimit(BasicPropertyUnit bPU) {
		IMultiCurve<IOrientableCurve> img = new GM_MultiCurve<>();

		for (CadastralParcel cP : bPU.getCadastralParcel()) {
			// ////////////////////////
			// / Cette partie du code contient les vérificateurs de règles
			// /
			// /
			// /////////////////////////
			for (SpecificCadastralBoundary sc : cP.getSpecificCadastralBoundary()) {
				if (sc.getType() == SpecificCadastralBoundaryType.BOT) {
					img.addAll(FromGeomToLineString.convert(sc.getGeom()));
				}
			}
		}

		// System.out.println("nombre de limites de fond : " + img.size());

		return img;
	}

}
