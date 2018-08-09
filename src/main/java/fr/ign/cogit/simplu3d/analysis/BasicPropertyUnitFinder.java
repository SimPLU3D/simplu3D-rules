package fr.ign.cogit.simplu3d.analysis;

import java.util.Collection;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;

public class BasicPropertyUnitFinder {

	private IFeatureCollection<BasicPropertyUnit> basicPropertyUnits;
		
	public BasicPropertyUnitFinder(IFeatureCollection<BasicPropertyUnit> basicPropertyUnits){
		this.basicPropertyUnits = basicPropertyUnits;
		
		if (! this.basicPropertyUnits.hasSpatialIndex()) {
			this.basicPropertyUnits.initSpatialIndex(Tiling.class, false);
		}
	}
	
	
	public BasicPropertyUnit findBestIntersections(IGeometry polyBat){
		
		if(polyBat == null) {
			return null;
		}
		double area = polyBat.area();
		
		if(area == 0) {
			return null;
		}
		
		Collection<BasicPropertyUnit> candidates = basicPropertyUnits.select(polyBat);
		
		BasicPropertyUnit bestCandidate = null;
		double maxRatio = 0.0;
		for (BasicPropertyUnit candidate : candidates) {
			IGeometry intersection = candidate.getGeom().intersection(polyBat);
			if ( intersection == null || intersection.isEmpty() ){
				continue;
			}
			double ratio = intersection.area() / area;
			if ( ratio > maxRatio ){
				bestCandidate = candidate ;
				maxRatio = ratio;
			}
		}
		return bestCandidate ;		
	}
}
