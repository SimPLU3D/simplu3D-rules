package fr.ign.cogit.simplu3d.checker.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToLineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.checker.model.AbstractRuleChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.Road;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;
import fr.ign.cogit.simplu3d.model.SubParcel;

/**
 * 
 * Applique les validations par bande
 * 
 * TODO splitter
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class BandsChecker extends AbstractRuleChecker {

	public final static String CODE_PROSPECT_VOIRIE = "PROSPECT_VOIRIE";
	public final static String CODE_PROSPECT_LAT = "PROSPECT_LAT";
	public final static String CODE_ALIGNEMENT = "ALIGNEMENT";
	public final static String CODE_ALIGNEMENT_LAT = "ALIGNEMENT_LAT";
	public static final String CODE_HMAX_BAND2 = "HMAX_BAND2";


	
	public BandsChecker(){
		super();
	}

	public BandsChecker(ZoneRegulation rules){
		super(rules);
	}
	
	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		// On récupère les bâtiments par bande :
		Map<Integer, List<AbstractBuilding>> mapB = getBuildingByBand(bPU, this.getRules());

		// On traite la bande 1 : le prospect en fonction de la largeur de la
		// voirie
		lUNR.addAll(checkProspectBand1(mapB.get(1), bPU,  this.getRules()));

		// On traite la bande 1 : le recul par rapport aux limites latérales et
		// à l'alignement
		lUNR.addAll(checkLateralFront(mapB.get(1), bPU,  this.getRules()));

		// On traite la bande 2 avec les valeurs ules.getSlopeProspectLat(),
		// rules.gethIniProspectLat()
		lUNR.addAll(checkProspectBand2(mapB.get(2), bPU,  this.getRules()));

		// On vérifie la hauteur par rapport à getHauteurMax2
		lUNR.addAll(checkHeightDistanceBand2(mapB.get(2), bPU,  this.getRules()));

		return lUNR;
	}

	/**
	 * Le prospect est vérifié en fonction de la largeur de la route
	 * rules.getLargMaxProspect1() : Si c'est plus on vérifie le prospect par
	 * rapport aux limites sur la voirie avec les paramètres : slope :
	 * rules.getProspectVoirie2Slope() et hIni : r.getWidth() *
	 * rules.getProspectVoirie2Slope() + rules.getProspectVoirie2Hini() sinon
	 * slope getProspectVoirie1Slope et hIni : r.getWidth() *
	 * rules.getProspectVoirie1Slope() + rules.getProspectVoirie1Hini()
	 * 
	 * @param list
	 *            une liste de Building Part en AbstractBuilding
	 * @param bPU
	 *            une BPU
	 * @param rules
	 *            des règles
	 * @return une liste de règles non respectées
	 */
	private List<UnrespectedRule> checkProspectBand1(List<AbstractBuilding> list, BasicPropertyUnit bPU, ZoneRegulation rules) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		// On récupère les limites de fronts de parcelles (celles qui peuvent
		// nous donner une information sur les routes)
		List<ParcelBoundary> lFronLimit = getFrontLimit(bPU);

		for (ParcelBoundary sc : lFronLimit) {
			Road road = sc.getRoad();

			// On récupère la route adjance
			if (road != null) {

				for (AbstractBuilding ab : list) {

					// suivant le cas, on a 2 prospect
					if (road.getWidth() > rules.getLargMaxProspect1()) {
						// Est-ce qu'il respecte le prospect ?
						if (!ab.prospect(sc.getGeom(), rules.getProspectVoirie2Slope(),
								road.getWidth() * rules.getProspectVoirie2Slope() + rules.getProspectVoirie2Hini())) {
							lUNR.add(new UnrespectedRule("Prospect non respecté (route de plus de "
									+ rules.getLargMaxProspect1() + " m de large", ab.getFootprint(),
									CODE_PROSPECT_VOIRIE));
						}

					} else {
						// Est-ce qu'il respecte l'autre prospect ?
						if (!ab.prospect(sc.getGeom(), rules.getProspectVoirie1Slope(),
								road.getWidth() * rules.getProspectVoirie1Slope() + rules.getProspectVoirie1Hini())) {
							lUNR.add(new UnrespectedRule("Prospect non respecté (route de moins de "
									+ rules.getLargMaxProspect1() + " m de large", ab.getFootprint(),
									CODE_PROSPECT_VOIRIE));
						}
					}
				}

			}
		}

		return lUNR;

	}

	/**
	 * Ici on vérifie que la distance du bâtiment est supérieur à getAlignement
	 * par rapport à la voirie et on vérifie que la distance latérale est
	 * comprise entre rules.getReculLatMin() et rules.getReculLatMax()
	 * 
	 * @param list
	 *            une liste de Building Part en AbstractBuilding
	 * @param bPU
	 *            une BPU
	 * @param rules
	 *            des règles
	 * @return une liste de règles non respectées
	 */
	private List<UnrespectedRule> checkLateralFront(List<AbstractBuilding> list, BasicPropertyUnit bPU, ZoneRegulation rules) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		IMultiCurve<IOrientableCurve> iMSFront = getFrontLimitGeom(bPU);

		for (AbstractBuilding aB : list) {
			if (aB.getFootprint().distance(iMSFront) > rules.getAlignement() + 0.5) {

				IGeometry geomError = bPU.getGeom().intersection(iMSFront.buffer(0.5));

				lUNR.add(new UnrespectedRule("Non respect de la distance à l'alignement ", geomError, CODE_ALIGNEMENT));
			}
		}

		IMultiCurve<IOrientableCurve> iMSLat = getLatLimit(bPU);

		for (AbstractBuilding aB : list) {
			if (aB.getFootprint().distance(iMSLat) < rules.getReculLatMin()
					&& aB.getFootprint().distance(iMSLat) > rules.getReculLatMax()) {

				IGeometry geomError = bPU.getGeom().intersection(iMSLat.buffer(rules.getReculLatMax()));

				geomError = geomError.difference(iMSLat.buffer(rules.getReculLatMin()));

				lUNR.add(new UnrespectedRule("Non respect de la distance aux limites latérales ", geomError,
						CODE_ALIGNEMENT_LAT));
			}
		}

		return lUNR;
	}

	/**
	 * On vérifie en bande 2 le prospect latéral avec les valeurs
	 * ules.getSlopeProspectLat(), rules.gethIniProspectLat()
	 * 
	 * @param list
	 *            une liste de Building Part en AbstractBuilding
	 * @param bPU
	 *            une BPU
	 * @param rules
	 *            des règles
	 * @return une liste de règles non respectées
	 */
	private List<UnrespectedRule> checkProspectBand2(List<AbstractBuilding> list, BasicPropertyUnit bPU, ZoneRegulation rules) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		// On récupère les limites de fronts de parcelles (celles qui peuvent
		// nous donner une information sur les routes)
		IMultiCurve<IOrientableCurve> imc = getLatLimit(bPU);

		bouclebuilding: for (AbstractBuilding ab : list) {

			for (IOrientableCurve os : imc) {

				// Est-ce qu'il respecte l'autre prospect ?
				if (!ab.prospect(os, rules.getSlopeProspectLat(), rules.gethIniProspectLat())) {
					lUNR.add(new UnrespectedRule("Prospect latéral non respecté en seconde bande ", ab.getFootprint(),
							CODE_PROSPECT_LAT));
					continue bouclebuilding;
				}

			}
		}

		return lUNR;

	}

	/**
	 * On vérifie en bande 2 la hauteur des bâtiments par rapport à
	 * getHauteurMax2
	 * 
	 * @param list
	 *            une liste de Building Part en AbstractBuilding
	 * @param bPU
	 *            une BPU
	 * @param rules
	 *            des règles
	 * @return une liste de règles non respectées
	 */
	private static List<UnrespectedRule> checkHeightDistanceBand2(List<AbstractBuilding> list, BasicPropertyUnit bPU,
			ZoneRegulation rules) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		// On vérifie la hauteur
		for (AbstractBuilding ab : list) {
			double hauteur = ab.height(1, 1);

			if (hauteur > rules.getHauteurMax2()) {
				lUNR.add(new UnrespectedRule("Non respect de la hauteur maximale en seconde bande ", ab.getFootprint(),
						CODE_HMAX_BAND2));
			}

		}

		return lUNR;
	}

	/**
	 * On récupère les bâtiments en 2 bandes (avec priorité pour la première)
	 * 
	 * - Alignement + r.getBand1 - Alignement + r.getBand1 + r.getBand2
	 * 
	 * @TODO : couper en 2 les bâtiments en fonction des zones dans lesquells
	 *       ils sont
	 * 
	 * @param bPU
	 *            une BPU
	 * @param r
	 *            des règles
	 * @return une Map<Integer, List<AbstractBuilding>>
	 */
	private Map<Integer, List<AbstractBuilding>> getBuildingByBand(BasicPropertyUnit bPU, ZoneRegulation r) {

		IGeometry getFrontLimit = getFrontLimitGeom(bPU);

		Map<Integer, List<AbstractBuilding>> mapB = new Hashtable<>();

		mapB.put(1, new ArrayList<AbstractBuilding>());
		mapB.put(2, new ArrayList<AbstractBuilding>());

		for (CadastralParcel cP : bPU.getCadastralParcels()) {

			for (SubParcel sP : cP.getSubParcels()) {

				for (AbstractBuilding bP : sP.getBuildingsParts()) {

					if (bP.getFootprint().distance(getFrontLimit) < r.getAlignement() + r.getBand1()) {
						// Bande 1 : on ajoute la bâtiment à la liste 1
						mapB.get(1).add(bP);

						IGeometry geom = getFrontLimit.buffer(r.getAlignement() + r.getBand1());

						IGeometry geom2 = getFrontLimit.buffer(r.getAlignement() + r.getBand1() + r.getBand2());

						if (geom != null && geom2 != null && !geom.isEmpty() && !geom2.isEmpty()) {

							IGeometry geomInter = geom2.intersection(geom);

							if (geomInter != null && !geomInter.isEmpty()) {
								if (bP.getFootprint().intersects(geomInter)) {
									mapB.get(2).add(bP);
								}

							}
						}

					} else if (bP.getFootprint().distance(getFrontLimit) < r.getAlignement() + r.getBand1()
							+ r.getBand2()) {

						// Bande 2 : on ajoute la bâtiment à la liste 1
						mapB.get(2).add(bP);
					}

				}
			}
		}
		return mapB;
	}

	/**
	 * Get front limit for the parcel
	 * 
	 * TODO add to model?
	 * 
	 * @param bPU
	 * @return
	 */
	private List<ParcelBoundary> getFrontLimit(BasicPropertyUnit bPU) {
		List<ParcelBoundary> lSC = new ArrayList<>();

		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			for (ParcelBoundary sc : cP.getBoundaries()) {
				if (sc.getType() == ParcelBoundaryType.ROAD) {
					lSC.add(sc);
				}
			}
		}

		return lSC;

	}

	/**
	 * Get front limit for the given parcel
	 * 
	 * TODO add to model?
	 * 
	 * @param bPU
	 * @return
	 */
	private IMultiCurve<IOrientableCurve> getFrontLimitGeom(BasicPropertyUnit bPU) {
		IMultiCurve<IOrientableCurve> img = new GM_MultiCurve<>();

		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			for (ParcelBoundary sc : cP.getBoundaries()) {
				if (sc.getType() == ParcelBoundaryType.ROAD) {
					img.addAll(FromGeomToLineString.convert(sc.getGeom()));
				}
			}
		}

		return img;
	}

	/**
	 * Get lateral limit for the given parcel
	 * 
	 * TODO add to model?
	 * 
	 * @param bPU
	 * @return
	 */
	private IMultiCurve<IOrientableCurve> getLatLimit(BasicPropertyUnit bPU) {
		IMultiCurve<IOrientableCurve> img = new GM_MultiCurve<>();

		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			for (ParcelBoundary sc : cP.getBoundaries()) {
				if (sc.getType() == ParcelBoundaryType.LAT) {
					img.addAll(FromGeomToLineString.convert(sc.getGeom()));
				}
			}
		}

		return img;
	}

	@Override
	public List<GeometricConstraints> generate(BasicPropertyUnit bPU, RuleContext ruleContext) {
		// TODO Auto-generated method stub
		return null;
	}

}
