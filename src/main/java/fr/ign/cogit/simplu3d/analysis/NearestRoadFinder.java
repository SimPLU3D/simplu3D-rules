package fr.ign.cogit.simplu3d.analysis;

import java.util.Collection;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.model.Road;

/**
 * 
 * Find nearest road from a given geometry
 * 
 * TODO externalize and test matching distance? Find an existing matching
 * algorithm in geoxygene?
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class NearestRoadFinder {

	private IFeatureCollection<Road> roads;

	private double maximumDistance = 10.0;

	public NearestRoadFinder(IFeatureCollection<Road> roads) {
		this.roads = roads;

		if (!this.roads.hasSpatialIndex()) {
			this.roads.initSpatialIndex(Tiling.class, false);
		}
	}

	public double getMaximumDistance() {
		return maximumDistance;
	}

	public void setMaximumDistance(double maximumDistance) {
		this.maximumDistance = maximumDistance;
	}

	/**
	 * Find the best road for
	 * 
	 * @param geometry
	 * @return the best Road, null if no Road found
	 */
	public Road findNearestRoad(IGeometry geometry) {

		return (Road) findNearest(roads, geometry, getMaximumDistance());
	}

	public static IFeature findNearest(IFeatureCollection<? extends IFeature> featColl, IGeometry geometry,
			double distMax) {

		IGeometry buffer = geometry.buffer(distMax);

		if (!featColl.hasSpatialIndex()) {
			featColl.initSpatialIndex(Tiling.class, true);
		}

		Collection<? extends IFeature> candidates = featColl.select(buffer);


		if (candidates.isEmpty()) {
			return null;
		}

		if (candidates.size() == 1) {
			return candidates.iterator().next();
		}

		// Find best orientation
		IDirectPositionList dpl = geometry.coord();
		Vecteur v = new Vecteur(dpl.get(0), dpl.get(dpl.size() - 1));
		v.normalise();
		IFeature bestCandidate = null;
		double bestScore = Double.NEGATIVE_INFINITY;
		for (IFeature candidate : candidates) {
			IDirectPositionList dpl2 = geometry.coord();
			Vecteur v2 = new Vecteur(dpl2.get(0), dpl2.get(dpl2.size() - 1));
			v2.normalise();
			double cos = Math.abs(v.prodScalaire(v2));
			if (cos > Math.cos(Math.PI / 5)) {
				// à voir .....
				if (cos > bestScore) {
					bestCandidate = candidate;
					bestScore = cos;
				}
			}
		}

		return bestCandidate;
	}

}
