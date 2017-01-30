package fr.ign.cogit.simplu3d.generator.boundary;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToLineString;

/**
 * Create a topology ("CarteTopo") from a set of CadastralParcels
 * 
 * TODO add(CadastralParcel) / build() : CarteTopo (keep newCarteTopo as a facade)
 * 
 * @author MBrasebin
 *
 */
public class CarteTopoParcelBoundaryBuilder {
	
	private static Logger logger = Logger.getLogger(CarteTopoParcelBoundaryBuilder.class);
	
	
	public static CarteTopo newCarteTopo(
		String name, 
		Collection<? extends IFeature> features,
		double threshold
	) {
		logger.info("Create a CarteTopo for "+features.size()+" feature(s)");
		try {
			// Initialisation d'une nouvelle CarteTopo
			CarteTopo carteTopo = new CarteTopo(name);
			carteTopo.setBuildInfiniteFace(false);
			// Récupération des arcs de la carteTopo
			IPopulation<Arc> arcs = carteTopo.getPopArcs();
			// Import des arcs de la collection dans la carteTopo
			for (IFeature feature : features) {

				List<ILineString> lLLS = FromPolygonToLineString.convertPolToLineStrings((IPolygon) feature.getGeom());

				for (ILineString ls : lLLS) {
					//TODO use threshold?
					if (ls.length() == 0) {
						logger.warn("Ignore null length edge");
					}

					// création d'un nouvel élément
					Arc arc = arcs.nouvelElement();
					// affectation de la géométrie de l'objet issu de la
					// collection
					// à l'arc de la carteTopo
					arc.setGeometrie(ls);
					// instanciation de la relation entre l'arc créé et l'objet
					// issu de la collection
					arc.addCorrespondant(feature);

				}

			}

			if ( ! hasNullLengthEdge(carteTopo) ) {
				logger.warn("null length edges found");
			}

			logger.info("Create missing nodes at the end of edges...");
			carteTopo.creeNoeudsManquants(-1);

			if ( ! hasNullLengthEdge(carteTopo) ) {
				logger.warn("null length edges found");
			}

			logger.info("Merge nodes with a threshold ("+threshold+")...");
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
			
			if ( carteTopo.getPopFaces().isEmpty() ){
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
			if (a.getGeometrie().length() == 0) {
				return false;
			}

		}
		return true;
	}
}
