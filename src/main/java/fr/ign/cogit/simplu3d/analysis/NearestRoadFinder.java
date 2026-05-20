package fr.ign.cogit.simplu3d.analysis;

import java.util.Collection;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.model.Road;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

/**
 * 
 * Find nearest road from a given geometry
 * <p>
 * TODO externalize and test matching distance? Find an existing matching
 * algorithm in geoxygene?
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class NearestRoadFinder {
	private final SpatialRepository<Road> roads;
	private static double maximumDistance = 10.0;

	public NearestRoadFinder(SpatialRepository<Road> roads) {
		this.roads = roads;
	}

	public  static double getMaximumDistance() {
		return maximumDistance;
	}

	public static void setMaximumDistance(double maximumDistance) {
		NearestRoadFinder.maximumDistance = maximumDistance;
	}

	/**
	 * Find the best road for
	 * 
	 * @param geometry the geometry used to find the nearest road
	 * @return the best Road, null if no Road found
	 */
	public Road findNearestRoad(Geometry geometry) {
		return findNearest(roads, geometry, getMaximumDistance());
	}

	public static <U extends UrbanFeature> U findNearest(SpatialRepository<U> featColl, Geometry geometry, double distMax) {
		Geometry buffer = geometry.buffer(distMax);
		Collection<U> candidates = featColl.findIntersects(buffer);
		if (candidates.isEmpty()) {
			return null;
		}
		if (candidates.size() == 1) {
			return candidates.iterator().next();
		}
		// Find best orientation
        return getBestCandidate(geometry, candidates);
	}

	private static <U extends UrbanFeature> U getBestCandidate(Geometry geometry, Collection<U> candidates) {
		Coordinate[] dpl = geometry.getCoordinates();
		Vecteur v = new Vecteur(dpl[0], dpl[dpl.length - 1]);
		v.normalise();
		U bestCandidate = null;
		double bestScore = Double.NEGATIVE_INFINITY;
		for (U candidate : candidates) {
			Coordinate[] dpl2 = geometry.getCoordinates();
			Vecteur v2 = new Vecteur(dpl2[0], dpl2[dpl2.length - 1]);
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
