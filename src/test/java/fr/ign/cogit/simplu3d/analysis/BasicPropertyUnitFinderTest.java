package fr.ign.cogit.simplu3d.analysis;

import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import junit.framework.TestCase;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.Collection;

public class BasicPropertyUnitFinderTest extends TestCase {
	
	private Collection<BasicPropertyUnit> basicPropertyUnits = new ArrayList<>();
	
	@Override
	public void setUp() throws Exception {
		basicPropertyUnits = new ArrayList<>();
		basicPropertyUnits.add(createBpU("0",0,0,1.0));
		basicPropertyUnits.add(createBpU("1",2,2,1.0));
	}
	
	private BasicPropertyUnit createBpU(String id,double x, double y, double r){
		BasicPropertyUnit bPU = new BasicPropertyUnit();
		bPU.setId(id);
		bPU.setGeometry(createBuffer(x, y, r));
		return bPU;
	}
	
	private Geometry createBuffer(double x, double y, double r){
		Point point = new GeometryFactory().createPoint(new Coordinate(x,y));
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
		assertEquals("0", intersection.getId());
	}
	
	public void testMultipleIntersections(){
		BasicPropertyUnitFinder finder = new BasicPropertyUnitFinder(basicPropertyUnits);
		BasicPropertyUnit intersection = finder.findBestIntersections(createBuffer(1.2,1.2,1.0));
		assertNotNull(intersection);
		assertEquals("1", intersection.getId());
	}
	
}

