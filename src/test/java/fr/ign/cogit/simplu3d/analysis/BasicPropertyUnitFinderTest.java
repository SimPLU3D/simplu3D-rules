package fr.ign.cogit.simplu3d.analysis;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import junit.framework.TestCase;

public class BasicPropertyUnitFinderTest extends TestCase {
	
	private IFeatureCollection<BasicPropertyUnit> basicPropertyUnits = new FT_FeatureCollection<>();
	
	@Override
	public void setUp() throws Exception {
		basicPropertyUnits = new FT_FeatureCollection<>();
		basicPropertyUnits.add(createBpU(0,0,0,1.0));
		basicPropertyUnits.add(createBpU(1,2,2,1.0));
	}
	
	private BasicPropertyUnit createBpU(int id,double x, double y, double r){
		BasicPropertyUnit bPU = new BasicPropertyUnit();
		bPU.setId(id);
		bPU.setGeom(createBuffer(x, y, r));
		return bPU;
	}
	
	private IGeometry createBuffer(double x, double y, double r){
		IPoint point = new GM_Point(new DirectPosition(x,y));
		return point.buffer(r);
	}
	
	public void testNoIntersection(){
		BasicPropertyUnitFinder finder = new BasicPropertyUnitFinder(basicPropertyUnits);
		BasicPropertyUnit intersection = finder.findBestIntersections(createBuffer(10.0, 10.0, 2.0));
		assertNull(intersection);
	}
	
	public void testSingleIntersection(){
		BasicPropertyUnitFinder finder = new BasicPropertyUnitFinder(basicPropertyUnits);
		BasicPropertyUnit intersection = finder.findBestIntersections(createBuffer(0,0,0.5));
		assertNotNull(intersection);
		assertEquals(0, intersection.getId());
	}
	
	public void testMultipleIntersections(){
		BasicPropertyUnitFinder finder = new BasicPropertyUnitFinder(basicPropertyUnits);
		BasicPropertyUnit intersection = finder.findBestIntersections(createBuffer(1.2,1.2,1.0));
		assertNotNull(intersection);
		assertEquals(1, intersection.getId());
	}
	
}

