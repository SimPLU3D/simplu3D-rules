package fr.ign.cogit.simplu3d.checker;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToLineString;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.WallSurface;

/**
 * Le coefficient d'emprise au sol peut atteindre getEmpriseSol de la superficie
 * du terrain pour les terrains cumulant les caractéristiques suivantes :
 * superficie supérieure ou égale à getEmpriseSurface(), largeur de façade
 * supérieure ou égale à getEmpLargeurMin mètres, - Pour les terrains dont l'une
 * des caractéristiques en matière de superficie et de largeur de façade ne
 * répond pas aux critères fixés ci-dessus, le coefficient d'emprise au sol, y
 * compris les constructions annexes, est limité à getEmpriseSolAlt de la
 * superficie du terrain.
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class CheckerCES implements IRuleChecker {
	
	public final static String CODE_CES = "CES";
	
	private Rules rules ;

	public CheckerCES(Rules rules) {
		this.rules = rules;
	}
	

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		// Cette information permet de savoir si on doit avoir un CES de 0.5 ou
		// de 0.4
		boolean conditionchecked = true;

		// Est-ce que la superficie du terrain est inférieure ou égale à
		// getEmpriseSurface()
		double area = bPU.getGeom().area();
		if (area < rules.getEmpriseSurface()) {
			conditionchecked = false;
		}

		// Est-ce que la largeur des façades est supérieure à EmpLargeurMin
		boolean hasBeenCheckded = false;
		// On récupère les parties de bâtiments
		bouclecp: for (CadastralParcel cP : bPU.getCadastralParcels()) {

			for (SubParcel sP : cP.getSubParcels()) {

				for (AbstractBuilding bP : sP.getBuildingsParts()) {

					List<WallSurface> lSWS = bP.getWallSurfaces();

					if (lSWS != null && !lSWS.isEmpty()) {
						for (WallSurface sWS : lSWS) {

							if (!sWS.getGeom().isEmpty()) {

								IEnvelope enve = sWS.getGeom().getEnvelope();

								if (enve.length() > rules.getEmpLargeurMin()) {
									hasBeenCheckded = true;
									break bouclecp;
								}

							}

						}
						break bouclecp;
					}

					// Si on n'a pas de wallsurface, on se sert de l'emprise
					IMultiSurface<IOrientableSurface> multiS = FromGeomToSurface.convertMSGeom(bP.getFootprint());

					List<IOrientableCurve> ios = new ArrayList<>();
					for (IOrientableSurface os : multiS) {
						ios.addAll(FromGeomToLineString.convert(os));
					}

					for (IOrientableCurve os : ios) {
						IEnvelope enve = os.getEnvelope();

						if (enve.length() > rules.getEmpLargeurMin()) {
							hasBeenCheckded = true;
							break bouclecp;
						}

					}
				}

			}

		}

		// Si les 2 conditions sont vérifiées c'est
		double ces = rules.getEmpriseSol();
		// Si une des deux n'est pas vérifiée, c'est l'autre
		if (!conditionchecked || !hasBeenCheckded) {
			ces = rules.getEmpriseSolAlt();
		}

		// On calcule l'aire totale bâtie
		double aireBati = 0;
		for (CadastralParcel cP : bPU.getCadastralParcels()) {

			for (SubParcel sP : cP.getSubParcels()) {

				for (AbstractBuilding bP : sP.getBuildingsParts()) {

					aireBati = aireBati + bP.getFootprint().area();

				}

			}

		}

		// On regarde si le CES est respectée
		if (bPU.getGeom().area() * ces < aireBati) {
			// LE CES n'est pas respectée on ajoute une incohérence

			double cesCalculated = (aireBati / bPU.getGeom().area());
			cesCalculated = (Math.round(100 * cesCalculated)) / 100.0;

			lUNR.add(new UnrespectedRule(
					"Le CES n'est pas respecté - CES mesuré : " + cesCalculated + "  CES attendu " + ces, bPU.getGeom(),
					CODE_CES));

		}

		return lUNR;
	}

}
