package fr.ign.cogit.simplu3d.analysis;

import java.util.List;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.index.SpatialIndex;
import org.locationtech.jts.index.strtree.STRtree;

public class BasicPropertyUnitFinder {
    private final SpatialIndex index;
	public BasicPropertyUnitFinder(SpatialRepository<BasicPropertyUnit> basicPropertyUnits){
        this.index = new STRtree();
		for (BasicPropertyUnit unit : basicPropertyUnits.findAll()){
			index.insert(unit.getGeometry().getEnvelopeInternal(), unit);
		}
	}

	public BasicPropertyUnit findBestIntersections(Geometry polyBat){
		if(polyBat == null) {
			return null;
		}
		double area = polyBat.getArea();
		if(area == 0) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<BasicPropertyUnit> candidates = index.query(polyBat.getEnvelopeInternal());
		BasicPropertyUnit bestCandidate = null;
		double maxRatio = 0.0;
		for (BasicPropertyUnit candidate : candidates) {
			Geometry intersection = candidate.getGeometry().intersection(polyBat);
			if ( intersection == null || intersection.isEmpty() ){
				continue;
			}
			double ratio = intersection.getArea() / area;
			if ( ratio > maxRatio ){
				bestCandidate = candidate ;
				maxRatio = ratio;
			}
		}
		return bestCandidate ;
	}
}
