package fr.ign.common.calculation;

import fr.ign.common.tools.Utils;
import junit.framework.TestCase;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TestProximity extends TestCase {
    // ---------------------------------- ATTRIBUTES ----------------------------------
    private final double epsilon = Math.pow(10, -10);    // Scale error
    private static final Logger log = Logger.getLogger(TestProximity.class.getName());
    // ------------------------------------ TESTS -------------------------------------
    // --------------------------------------------------------------------------------
    // Test for minimal distance between set of points processing
    // --------------------------------------------------------------------------------
    public void testNearest1() {
        log.info("Test for minimal distance between set of points processing");
        Proximity prox = new Proximity();
        // Creating point lists
        List<Coordinate> POINTS1 = Utils.createPointsList(1, 0, 0, 1);
        List<Coordinate> POINTS2 = Utils.createPointsList(1, 0.8, 0.8, 2);
        // Processing shortest distance
        prox.nearestLC(POINTS1, POINTS2);
        // Recovering closest couple of points
        Coordinate p1 = prox.getNearest();
        Coordinate p2 = prox.getNearest2();
        // Recovering shortest distance
        double d = prox.getDistance();
        // Setting expected result points
        Coordinate p1Expected = new Coordinate(1, 1, 1);
        Coordinate p2Expected = new Coordinate(0.8, 0.8, 2);
        // Processing expected distance
        double dExp = Math.sqrt(0.04+0.04+1);
        // Tests
        assertEquals("Closest point in list 1 is incorrect", p1Expected, p1);
        assertEquals("Closest point in list 2 is incorrect", p2Expected, p2);
        assertEquals("Shortest distance between set of points is incorrect", dExp, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for minimal distance between single point and set of points processing
    // --------------------------------------------------------------------------------
    public void testNearest2() {
        log.info("Test for minimal distance between single point and set of points processing");
        Proximity prox = new Proximity();
        // Creating point lists
        List<Coordinate> POINTS = new ArrayList<>();
        // Filling list
        POINTS.add(new Coordinate(153, 127, 124));
        POINTS.add(new Coordinate(375, 871, 212));
        POINTS.add(new Coordinate(112, 155, 135));
        POINTS.add(new Coordinate(159, 792, 336));
        // Creating single points
        Coordinate point1 = new Coordinate(110, 700, 300);
        Coordinate point2 = new Coordinate(153, 127, 124);
        // Processing case 1
        prox.nearest(point1, POINTS.toArray(new Coordinate[0]));
        // Expected results
        Coordinate pExp = new Coordinate(159, 792, 336);
        double dExp = Math.sqrt((159-110)*(159-110)+(792-700)*(792-700)+(336-300)*(336-300));
        // Actual results
        Coordinate p = prox.getNearest();
        double d = prox.getDistance();
        // Tests
        assertEquals("Closest point of set is incorrect", pExp, p);
        assertEquals("Shortest distance between point and set of points is incorrect", dExp, d, epsilon);
        // Processing case 2
        prox.nearest(point2, POINTS.toArray(new Coordinate[0]));
        // Expected results
        pExp = new Coordinate(153, 127, 124);
        dExp = 0;
        // Actual results
        p = prox.getNearest();
        d = prox.getDistance();
        // Tests
        assertEquals("Closest point of set is incorrect", pExp, p);
        assertEquals("Shortest distance between point and set of points is incorrect", dExp, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for minimal distance between single point and set of points processing
    // --------------------------------------------------------------------------------
    public void testNearest3() {
        log.info("Test for minimal distance between single point and set of points processing");
        Proximity prox = new Proximity();
        // Creating point lists
        List<Coordinate> POINTS = new ArrayList<>();
        // Filling list with cube
        POINTS.add(new Coordinate(0, 0, 0));
        POINTS.add(new Coordinate(0, 1, 0));
        POINTS.add(new Coordinate(1, 1, 0));
        POINTS.add(new Coordinate(1, 0, 0));
        POINTS.add(new Coordinate(0, 0, 1));
        POINTS.add(new Coordinate(0, 1, 1));
        POINTS.add(new Coordinate(1, 1, 1));
        POINTS.add(new Coordinate(1, 0, 1));
        // Creating single points in cube centre
        Coordinate point = new Coordinate(0.5,0.5,0.5);
        // Processing
        prox.nearest(point, POINTS.toArray(new Coordinate[0]));
        // Comparison
        double dExp = Math.sqrt(0.5*0.5+0.5*0.5+0.5*0.5);
        double d = prox.getDistance();
        assertEquals("Shortest distance between point and set of points is incorrect", dExp, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for minimal distance between single point and single surface
    // --------------------------------------------------------------------------------
    public void testNearest4() {
        log.info("Test for minimal distance between single point and single surface");
        Proximity prox = new Proximity();
        // Creating surfaces list
        List<Polygon> SURFACES = new ArrayList<>();
        SURFACES.add(Utils.createSquarePolygon(100, 0, 0));
        // Creating single points
        Coordinate point1 = new Coordinate(49,150,50);		// Case 1
        Coordinate point2 = new Coordinate(51,150,50);		// Case 2
        // Processing proximity functionnalities : case 1
        prox.nearest(point1, SURFACES);
        // Processing expected results
        Coordinate p1Expected = new Coordinate(49,150,50);
        Coordinate p2Expected = new Coordinate(0,100,0);
        Polygon surfaceExpected = SURFACES.get(0);
        double dExpected = Math.sqrt(49*49+50*50+50*50);
        // Recovering results
        Coordinate p1 = prox.getNearest();
        Coordinate p2 = prox.getNearest2();
        Polygon surface = prox.getContainingFace();
        double d = prox.getDistance();
        // Comparison
        assertEquals("Closest point is incorrect", p1Expected, p1);
        assertEquals("Closest point of surface is incorrect", p2Expected, p2);
        assertEquals("Closest containing surface is incorrect", surfaceExpected, surface);
        assertEquals("Shortest distance point-surfaces is incorrect", dExpected, d, epsilon);
        // Processing proximity functionnalities : case 2
        prox.nearest(point2, SURFACES);
        // Processing expected results
        p1Expected = new Coordinate(51,150,50);
        p2Expected = new Coordinate(100,100,0);
        // Recovering results
        p1 = prox.getNearest();
        p2 = prox.getNearest2();
        surface = prox.getContainingFace();
        d = prox.getDistance();
        // Comparison
        assertEquals("Closest point is incorrect", p1Expected, p1);
        assertEquals("Closest point of surface is incorrect", p2Expected, p2);
        assertEquals("Closest containing surface is incorrect", surfaceExpected, surface);
        assertEquals("Shortest distance point-surfaces is incorrect", dExpected, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for minimal distance between single point and surfaces list
    // --------------------------------------------------------------------------------
    public void testNearest5() {
        log.info("Test for minimal distance between single point and surfaces list");
        Proximity prox = new Proximity();
        // Creating surfaces
        List<Polygon> SURFACES = new ArrayList<>();
        // Surfaces
        SURFACES.add(Utils.createSquarePolygon(1, 0, 0));
        SURFACES.add(Utils.createSquarePolygon(1, 1, 1));
        // Creating single points
        Coordinate point1 = new Coordinate(1.2, 0.2, 0);		// Case 1
        Coordinate point2 = new Coordinate(1.2, 0.8, 0);		// Case 2
        // Processing : case 1
        prox.nearest(point1, SURFACES);
        // Processing expected results
        Coordinate p1Expected = new Coordinate(1.2, 0.2, 0.0);
        Coordinate p2Expected = new Coordinate(1.0, 0.0, 0.0);
        Polygon surfaceExpected = SURFACES.get(0);
        double dExpected = Math.sqrt(0.2*0.2+0.2*0.2);
        // Recovering results
        Coordinate p1 = prox.getNearest();
        Coordinate p2 = prox.getNearest2();
        Polygon surface = prox.getContainingFace();
        double d = prox.getDistance();
        // Comparison
        assertEquals("Closest point is incorrect", p1Expected, p1);
        assertEquals("Closest point of surface is incorrect", p2Expected, p2);
        assertEquals("Closest containing surface is incorrect", surfaceExpected, surface);
        assertEquals("Shortest distance point-surfaces is incorrect", dExpected, d, epsilon);
        // Processing : case 2
        prox.nearest(point2, SURFACES);
        // Processing expected results
        p1Expected = new Coordinate(1.2, 0.8, 0.0);
        p2Expected = new Coordinate(1.0, 1.0, 0.0);
        // Recovering results
        p1 = prox.getNearest();
        p2 = prox.getNearest2();
        surface = prox.getContainingFace();
        d = prox.getDistance();
        // Special surface condition
        boolean b0 = surface.equals(SURFACES.get(0));
        boolean b1 = surface.equals(SURFACES.get(1));
        // Comparison
        assertEquals("Closest point is incorrect", p1Expected, p1);
        assertEquals("Closest point of surface is incorrect", p2Expected, p2);
        assertTrue("Closest containing surface is incorrect", b0 || b1);
        assertEquals("Shortest distance point-surfaces is incorrect", dExpected, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for minimal distance between list of point and single surface
    // --------------------------------------------------------------------------------
    public void testNearest6() {
        log.info("Test for minimal distance between list of point and single surface");
        Proximity prox = new Proximity();
        // Creating surfaces
        ArrayList<Polygon> SURFACES = new ArrayList<>();
        // Surface
        SURFACES.add(Utils.createSquarePolygon(1, 0, 0));
        // Creating list of points
        List<Coordinate> POINTS = new ArrayList<>();
        POINTS.add(new Coordinate(-10,12,3));
        POINTS.add(new Coordinate(0.1,0.1,10));
        POINTS.add(new Coordinate(1.3,0.4,0));
        // Processing case
        prox.nearestLP(POINTS, SURFACES);
        // Processing expected results
        Coordinate p1Expected = new Coordinate(1.3, 0.4, 0.0);
        Coordinate p2Expected = new Coordinate(1.0, 0.0, 0.0);
        Polygon surfaceExpected = SURFACES.get(0);
        double dExpected = Math.sqrt(0.3*0.3+0.4*0.4);
        // Recovering results
        Coordinate p1 = prox.getNearest();
        Coordinate p2 = prox.getNearest2();
        Polygon surface = prox.getContainingFace();
        double d = prox.getDistance();
        // Comparison
        assertEquals("Closest point is incorrect", p1Expected, p1);
        assertEquals("Closest point of surface is incorrect", p2Expected, p2);
        assertEquals("Closest containing surface is incorrect", surfaceExpected, surface);
        assertEquals("Shortest distance point-surfaces is incorrect", dExpected, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for minimal distance between list of point and list of surfaces
    // --------------------------------------------------------------------------------
    public void testNearest7() {
        log.info("Test for minimal distance between list of point and list of surfaces");
        Proximity prox = new Proximity();
        // Creating surfaces
        ArrayList<Polygon> SURFACES = new ArrayList<>();
        // Surfaces
        SURFACES.add(Utils.createSquarePolygon(1, 0, 0));
        SURFACES.add(Utils.createSquarePolygon(1, 1, 1));
        // Creating list of points
        List<Coordinate> POINTS = new ArrayList<>();
        POINTS.add(new Coordinate(-10,12,3));
        POINTS.add(new Coordinate(0.1,0.1,10));
        POINTS.add(new Coordinate(1.3,0.4,0));
        // Processing case
        prox.nearestLP(POINTS, SURFACES);
        // Processing expected results
        Coordinate p1Expected = new Coordinate(1.3, 0.4, 0.0);
        Coordinate p2Expected = new Coordinate(1.0, 0.0, 0.0);
        Polygon surfaceExpected = SURFACES.get(0);
        double dExpected = Math.sqrt(0.3*0.3+0.4*0.4);
        // Recovering results
        Coordinate p1 = prox.getNearest();
        Coordinate p2 = prox.getNearest2();
        Polygon surface = prox.getContainingFace();
        double d = prox.getDistance();
        // Comparison
        assertEquals("Closest point is incorrect", p1Expected, p1);
        assertEquals("Closest point of surface is incorrect", p2Expected, p2);
        assertEquals("Closest containing surface is incorrect", surfaceExpected, surface);
        assertEquals("Shortest distance point-surfaces is incorrect", dExpected, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test when two points are same point
    // --------------------------------------------------------------------------------
    public void testNearest8() {
        log.info("Test when two points are same point");
        Proximity prox = new Proximity();
        // Creating point lists
        List<Coordinate> POINTS1 = Utils.createPointsList(1, 0, 0, 1);
        List<Coordinate> POINTS2 = Utils.createPointsList(1, 0, 0, 1);
        // Processing shortest distance
        prox.nearestLC(POINTS1, POINTS2);
        // Recovering closest couple of points
        Coordinate p1 = prox.getNearest();
        Coordinate p2 = prox.getNearest2();
        // Recovering shortest distance
        double d = prox.getDistance();
        // Processing expected distance
        double dExp = 0;
        boolean test1 =  false;
        for (Coordinate p : POINTS1){test1 = test1 || p1.equals(p);}
        boolean test2 =  false;
        for (Coordinate p : POINTS2){test2 = test2 || p2.equals(p);}
        // Tests
        assertTrue("Closest point in list 1 is incorrect", test1);
        assertTrue("Closest point in list 2 is incorrect", test2);
        assertEquals("Shortest distance between set of points is incorrect", dExp, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Basic test case
    // --------------------------------------------------------------------------------
    public void testNearest9() {
        log.info("Basic test case");
        Proximity prox = new Proximity();
        // Creating surfaces
        ArrayList<Polygon> SURFACES = new ArrayList<>();
        // Creating list of points
        List<Coordinate> POINTS = new ArrayList<>();
        // Processing case
        prox.nearestLP(POINTS, SURFACES);
        // Recovering results
        Coordinate p1 = prox.getNearest();
        Coordinate p2 = prox.getNearest2();
        Polygon surface = prox.getContainingFace();
        double d = prox.getDistance();
        // Comparison
        assertNull("Point nearest should be null", p1);
        assertNull("Point nearest2 should be null", p2);
        assertNull("Surface containingFace should be null", surface);
        assertTrue("Distance should be null", Double.isNaN(d));
    }

    // --------------------------------------------------------------------------------
    // Basic test case
    // --------------------------------------------------------------------------------
    public void testNearest10() {
        log.info("Basic test case");
        Proximity prox = new Proximity();
        // Creating surfaces
        ArrayList<Polygon> SURFACES = new ArrayList<>();
        // Creating list of points
        Coordinate pt = new Coordinate(10,10,10);
        // Processing case
        prox.nearest(pt, SURFACES);
        // Recovering results
        Coordinate p1 = prox.getNearest();
        Coordinate p2 = prox.getNearest2();
        Polygon surface = prox.getContainingFace();
        double d = prox.getDistance();
        // Comparison
        assertNull("Point nearest should be null", p1);
        assertNull("Point nearest2 should be null", p2);
        assertNull("Surface containingFace should be null", surface);
        assertTrue("Distance should be null", Double.isNaN(d));
    }

    // --------------------------------------------------------------------------------
    // Basic test case
    // --------------------------------------------------------------------------------
    public void testNearest11() {
        log.info("Basic test case");
        Proximity prox = new Proximity();
        // Creating point lists
        List<Coordinate> POINTS1 = new ArrayList<>();
        List<Coordinate> POINTS2 = new ArrayList<>();
        // Processing shortest distance
        prox.nearestLC(POINTS1, POINTS2);
        // Tests
        assertTrue("", true);
    }

    // --------------------------------------------------------------------------------
    // Basic test case
    // --------------------------------------------------------------------------------
    public void testNearest12() {
        log.info("Basic test case");
        Proximity prox = new Proximity();
        // Creating list of points
        Coordinate pt = new Coordinate(10,10,10);
        // Processing case
        prox.nearest(pt, new Coordinate[0]);
        // Recovering results
        Coordinate p1 = prox.getNearest();
        Coordinate p2 = prox.getNearest2();
        Polygon surface = prox.getContainingFace();
        double d = prox.getDistance();
        // Comparison
        assertNull("Point nearest should be null", p1);
        assertNull("Point nearest2 should be null", p2);
        assertNull("Surface containingFace should be null", surface);
        assertTrue("Distance should be null", Double.isNaN(d));
    }
}
