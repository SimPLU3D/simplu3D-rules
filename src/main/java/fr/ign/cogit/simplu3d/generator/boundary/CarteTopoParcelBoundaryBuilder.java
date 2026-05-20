package fr.ign.cogit.simplu3d.generator.boundary;

import fr.ign.cogit.simplu3d.model.UrbanFeature;
import fr.ign.common.topo.Arc;
import fr.ign.common.topo.CarteTopo;

import java.util.Collection;
import java.util.logging.Logger;

/**
 * Create a topology ("CarteTopo") from a set of CadastralParcels
 * <p>
 * TODO add(CadastralParcel) / build() : CarteTopo (keep newCarteTopo as a facade)
 * 
 * @author MBrasebin
 *
 */
public class CarteTopoParcelBoundaryBuilder {

	private static final Logger logger = Logger.getLogger(CarteTopoParcelBoundaryBuilder.class.getName());

	public static CarteTopo newCarteTopo(
			Collection<? extends UrbanFeature> features,
			double threshold
	) {
		logger.fine("Create a CarteTopo for " + features.size() + " feature(s)");
		try {
			// Initialisation d'une nouvelle CarteTopo
			CarteTopo carteTopo = new CarteTopo();
			carteTopo.addFeatures(features);
			/*
			carteTopo.setBuildInfiniteFace(false);
			// Récupération des arcs de la carteTopo
			Collection<Arc> arcs = new ArrayList<>();
			//carteTopo.getPopArcs();
			// Import des arcs de la collection dans la carteTopo
			for (UrbanFeature feature : features) {
				List<LineString> lLLS = FromPolygonToLineString.convertPolToLineStrings((Polygon) feature.getGeometry());
				for (LineString ls : lLLS) {
					//TODO use threshold?
					if (ls.getLength() == 0) {
						logger.warning("Ignore null length edge");
					}
					// création d'un nouvel élément
					Arc arc = new Arc();//arcs.nouvelElement();
					// affectation de la géométrie de l'objet issu de la
					// collection
					// à l'arc de la carteTopo
					arc.setGeometrie(ls);
					// instanciation de la relation entre l'arc créé et l'objet
					// issu de la collection
					arc.addCorrespondant(feature);
					arcs.add(arc);
				}
			}
			if (!hasNullLengthEdge(carteTopo)) {
				logger.warning("null length edges found");
			}
			logger.info("Create missing nodes at the end of edges...");
			carteTopo.creeNoeudsManquants(-1);

			if (!hasNullLengthEdge(carteTopo)) {
				logger.warning("null length edges found");
			}

			logger.info("Merge nodes with a threshold (" + threshold + ")...");
			carteTopo.fusionNoeuds(threshold);

			logger.info("Remove duplicate edges...");
			carteTopo.filtreArcsDoublons();

			// Création de la topologie Arcs Noeuds
			logger.info("Build node/edge topology...");
			carteTopo.creeTopologieArcsNoeuds(threshold);
			// La carteTopo est rendue planaire

			logger.info("Make planar graph...");
			carteTopo.rendPlanaire(threshold);

			logger.info("(Re-)build node/edge topology...");
			carteTopo.creeTopologieArcsNoeuds(threshold);
			logger.info("Build faces...");
			carteTopo.creeTopologieFaces();
*/
			if (carteTopo.getPopFaces().isEmpty()) {
				throw new RuntimeException("no faces created!");
			}

			// carteTopo.filtreNoeudsSimples();
			return carteTopo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static boolean hasNullLengthEdge(CarteTopo ct) {
		for (Arc a : ct.getPopArcs()) {
			if (a.getGeometrie().getLength() == 0) {
				return false;
			}
		}
		return true;
	}
}
