/**
 * 
 *        This software is released under the licence CeCILL
 * <p></p>
 *        see LICENSE.TXT
 * <p></p>
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
package fr.ign.cogit.simplu3d.generator;

import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.conversion.FromPolygonToLineString;
import fr.ign.common.topo.Arc;
import fr.ign.common.topo.CarteTopo;
import fr.ign.common.topo.Groupe;
import fr.ign.common.util.Operateurs;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * 
 * Create footprint for building
 * 
 * @author MBrasebin
 *
 */
public class FootprintGenerator {
	private static final Logger logger = Logger.getLogger(FootprintGenerator.class.getName());
	/**
	 * Create footprint from MultiSurface
	 * @param geomIni an intial geometry
	 * @return the footprint as a Polygon
	 */
	public static Polygon convert(Geometry geomIni) {
		List<Polygon> lPol = new ArrayList<>();
		CarteTopo cT = newCarteTopo(
			FromPolygonToLineString.convertPolygonToLineStrings(FromGeomToSurface.convertGeom(geomIni)),
			0.1
		);
		// on récupère les différents groupes
        assert cT != null;
        Collection<Groupe> lG = cT.decomposeConnexes();
		for (Groupe g : lG) {
			Collection<Arc> lArcs = g.getListeArcs();
			List<LineString> bordBatiment = new ArrayList<>();
			for (Arc a : lArcs) {
				if (a.getFaceDroite() == null || a.getFaceGauche() == null) {
					bordBatiment.add(a.getGeometrie());
				}
			}
			logger.warning("FootprintGenerator");
			LineString lS = Operateurs.union(bordBatiment);
			if (lS == null) {
				logger.severe("L'union des lignes extérieures est nulle");
				return null;
			}
			Coordinate[] dplTemp = lS.getCoordinates();
			if (dplTemp.length <= 3) {
				logger.severe("L'emprise du bâtiment a moins de 3 sommets");
				return null;
			}
			if (!dplTemp[0].equals2D(dplTemp[dplTemp.length - 1], 0.1)) {
				logger.severe("Problem");
				return null;
			}
			LineString lSClone = (LineString) lS.copy();
			for (Coordinate dp : lSClone.getCoordinates()) {
				dp.setZ(Double.NaN);
			}
			lPol.add(lS.getFactory().createPolygon(lSClone.getCoordinates()));
		}
		if (lPol.isEmpty()) {
			return null;
		}
		if (lPol.size() == 1) {
			return lPol.get(0);
		}
		Geometry geom = lPol.get(0).buffer(1);
		int nbPol = lPol.size();
		for (int i = 1; i < nbPol; i++) {
			geom = lPol.get(i).buffer(1).union(geom);
		}
		geom = geom.buffer(-1);
		return convert(geom);
	}

	public static CarteTopo newCarteTopo(List<LineString> lLLS, double threshold) {
		try {
			// Initialisation d'une nouvelle CarteTopo
			CarteTopo carteTopo = new CarteTopo();
			carteTopo.addGeometries(lLLS);
			/*
			carteTopo.setBuildInfiniteFace(false);
			// Récupération des arcs de la carteTopo
			Collection<Arc> arcs = carteTopo.getPopArcs();
			// Import des arcs de la collection dans la carteTopo
			for (LineString ls : lLLS) {
				// création d'un nouvel élément
				Arc arc = new Arc();
				arcs.add(arc);
				//arcs.nouvelElement();
				// affectation de la géométrie de l'objet issu de la collection
				// à l'arc de la carteTopo
				arc.setGeometrie(ls);
				// instanciation de la relation entre l'arc créé et l'objet
				// issu de la collection
				// arc.addCorrespondant(feature);
			}
			carteTopo.creeNoeudsManquants(0.1);
			// carteTopo.fusionNoeuds(threshold);
			// carteTopo.filtreArcsDoublons();
			// Création de la topologie Arcs Noeuds
			carteTopo.creeTopologieArcsNoeuds(threshold);
			// La carteTopo est rendue planaire
			carteTopo.rendPlanaire(threshold);
			carteTopo.filtreArcsDoublons();
			// DEBUG2.addAll(carteTopo.getListeArcs());
			// carteTopo.creeTopologieFaces();
			carteTopo.creeTopologieArcsNoeuds(threshold);
			// Création des faces de la carteTopo
			carteTopo.creeTopologieFaces();*/
			return carteTopo;
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return null;
	}
}
