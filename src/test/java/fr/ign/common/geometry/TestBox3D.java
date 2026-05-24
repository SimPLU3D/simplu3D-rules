package fr.ign.common.geometry;

import fr.ign.common.tools.Utils;
import junit.framework.TestCase;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TestBox3D extends TestCase {
    // ---------------------------------- ATTRIBUTES ----------------------------------
    private static final Logger log = Logger.getLogger(TestBox3D.class.getName());
    // ---------------------------------- PREPROCESS ----------------------------------
    // ------------------------------------ TESTS -------------------------------------
    // --------------------------------------------------------------------------------
    // Test for method to get box center from extremal coordinates
    // --------------------------------------------------------------------------------
    public void testBox3D1() {
        log.info("Test for method to get box center from extremal coordinates");
        // Building box
        Coordinate pmin = new Coordinate(10,10,10);
        Coordinate pmax = new Coordinate(15,20,12);
        Box3D box = new Box3D(pmin, pmax);
        // Getting center
        Coordinate pc = box.getCenter();
        // Expected result
        Coordinate pcExp = new Coordinate(12.5,15,11);
        // Comparison
        assertEquals("Box center coordinates are incorrect", pc, pcExp);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process bounding box from geometry
    // --------------------------------------------------------------------------------
    public void testBox3D2() {
        log.info("Test for method to process bounding box from geometry");
        // Creating cube
        Geometry cube = Utils.createCube(0, 0, 0, 10);
        // Computing bounding box
        Box3D box = new Box3D(cube);
        // Recovering extremal points
        Coordinate pmin = box.getLLDP();
        Coordinate pmax = box.getURDP();
        // Expected result
        Coordinate pminExp = new Coordinate(0,0,0);
        Coordinate pmaxExp = new Coordinate(10,10,10);
        // Comparison
        assertEquals("Bounding box is incorrect", pmin, pminExp);
        assertEquals("Bounding box is incorrect", pmax, pmaxExp);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process bounding box from list of points
    // --------------------------------------------------------------------------------
    public void testBox3D3() {
        log.info("Test for method to process bounding box from list of points");
        // Creating list of random points
        List<Coordinate> dpl = new ArrayList<>();
        for (int i=0; i<100; i++){
            dpl.add(new Coordinate(Math.random()*100, Math.random()*100, Math.random()*100));
        }
        // Creating bounding box
        Box3D box = new Box3D(dpl.toArray(new Coordinate[0]));
        // Recovering parameters
        Coordinate pmin = box.getLLDP();
        Coordinate pmax = box.getURDP();
        // Processing expected parameters
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double zmin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;
        double zmax = Double.MIN_VALUE;
        for (Coordinate coordinate : dpl) {
            xmin = Math.min(xmin, coordinate.getX());
            ymin = Math.min(ymin, coordinate.getY());
            zmin = Math.min(zmin, coordinate.getZ());
            xmax = Math.max(xmax, coordinate.getX());
            ymax = Math.max(ymax, coordinate.getY());
            zmax = Math.max(zmax, coordinate.getZ());
        }
        Coordinate pminExp = new Coordinate(xmin,ymin,zmin);
        Coordinate pmaxExp = new Coordinate(xmax,ymax,zmax);
        // Comparison
        assertEquals("Bounding box is incorrect", pmin, pminExp);
        assertEquals("Bounding box is incorrect", pmax, pmaxExp);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process intersection between boxes
    // --------------------------------------------------------------------------------
    public void testBox3D4() {
        log.info("Test for method to process intersection between boxes");
        // Creating boxes
        Coordinate pmin1 = new Coordinate(0,0,0);
        Coordinate pmax1 = new Coordinate(10,20,30);
        Coordinate pmin2 = new Coordinate(5,5,5);
        Coordinate pmax2 = new Coordinate(100,100,100);
        Coordinate pmin3 = new Coordinate(20,30,40);
        Coordinate pmax3 = new Coordinate(100,100,100);
        Box3D box1 = new Box3D(pmin1, pmax1);
        Box3D box2 = new Box3D(pmin2, pmax2);
        Box3D box3 = new Box3D(pmin3, pmax3);
        // Processing intersection
        boolean b1 = box1.intersect(box2);
        boolean b2 = box1.intersect(box3);
        // Comparison
        assertTrue("Intersection is not empty",b1);
        assertFalse("Intersection is empty",b2);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process intersection between boxes
    // --------------------------------------------------------------------------------
    public void testBox3D5() {
        log.info("Test for method to process intersection between boxes");
        // Creating boxes
        Coordinate pmin1 = new Coordinate(0,0,0);
        Coordinate pmax1 = new Coordinate(10,20,30);
        Coordinate pmin2 = new Coordinate(5,5,5);
        Coordinate pmax2 = new Coordinate(100,100,100);
        Coordinate pmin3 = new Coordinate(20,30,40);
        Coordinate pmax3 = new Coordinate(100,100,100);
        Box3D box1 = new Box3D(pmin1, pmax1);
        Box3D box2 = new Box3D(pmin2, pmax2);
        Box3D box3 = new Box3D(pmin3, pmax3);
        // Processing intersection
        Box3D boxI1 = box1.intersection(box2);
        Box3D boxI2 = box1.intersection(box3);
        // Recovering results
        Coordinate pminI1 = boxI1.getLLDP();
        Coordinate pmaxI1 = boxI1.getURDP();
        // Expected results
        Coordinate pminI1Exp = new Coordinate(5,5,5);
        Coordinate pmaxI1Exp = new Coordinate(10,20,30);
        // Comparison
        assertEquals("Bounding boxes intersection is incorrect", pminI1, pminI1Exp);
        assertEquals("Bounding boxes intersection is incorrect", pmaxI1, pmaxI1Exp);
        assertNull("Bounding boxes intersection is incorrect", boxI2);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process union between two boxes : standard case
    // --------------------------------------------------------------------------------
    public void testBox3D6() {
        log.info("Test for method to process union between two boxes : standard case");
        // Creating boxes
        Coordinate pmin1 = new Coordinate(0,0,0);
        Coordinate pmax1 = new Coordinate(10,20,30);
        Coordinate pmin2 = new Coordinate(5,5,5);
        Coordinate pmax2 = new Coordinate(100,100,100);
        Box3D box1 = new Box3D(pmin1, pmax1);
        Box3D box2 = new Box3D(pmin2, pmax2);
        // Processing union
        Box3D boxI1 = box1.union(box2);
        // Recovering results
        Coordinate pminI1 = boxI1.getLLDP();
        Coordinate pmaxI1 = boxI1.getURDP();
        // Expected results
        Coordinate pminI1Exp = new Coordinate(0,0,0);
        Coordinate pmaxI1Exp = new Coordinate(100,100,100);
        // Comparison
        assertEquals("Bounding boxes union is incorrect", pminI1, pminI1Exp);
        assertEquals("Bounding boxes union is incorrect", pmaxI1, pmaxI1Exp);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process union between two boxes : no intersection case
    // --------------------------------------------------------------------------------
    public void testBox3D7() {
        log.info("Test for method to process union between two boxes : no intersection case");
        // Creating boxes
        Coordinate pmin1 = new Coordinate(0,0,0);
        Coordinate pmax1 = new Coordinate(10,20,30);
        Coordinate pmin2 = new Coordinate(50,50,50);
        Coordinate pmax2 = new Coordinate(100,100,100);
        Box3D box1 = new Box3D(pmin1, pmax1);
        Box3D box2 = new Box3D(pmin2, pmax2);
        // Processing union
        Box3D boxI1 = box1.union(box2);
        // Recovering results
        Coordinate pminI1 = boxI1.getLLDP();
        Coordinate pmaxI1 = boxI1.getURDP();
        // Expected results
        Coordinate pminI1Exp = new Coordinate(0,0,0);
        Coordinate pmaxI1Exp = new Coordinate(100,100,100);
        // Comparison
        assertEquals("Bounding boxes union is incorrect", pminI1, pminI1Exp);
        assertEquals("Bounding boxes union is incorrect", pmaxI1, pmaxI1Exp);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process union between two boxes : same boxes case
    // --------------------------------------------------------------------------------
    public void testBox3D8() {
        log.info("Test for method to process union between two boxes : same boxes case");
        // Creating boxes
        Coordinate pmin1 = new Coordinate(0,0,0);
        Coordinate pmax1 = new Coordinate(10,20,30);
        Box3D box1 = new Box3D(pmin1, pmax1);
        // Processing union
        Box3D boxI1 = box1.union(box1);
        // Recovering results
        Coordinate pminI1 = boxI1.getLLDP();
        Coordinate pmaxI1 = boxI1.getURDP();
        // Expected results
        Coordinate pminI1Exp = new Coordinate(0,0,0);
        Coordinate pmaxI1Exp = new Coordinate(10,20,30);
        // Comparison
        assertEquals("Bounding boxes union is incorrect", pminI1, pminI1Exp);
        assertEquals("Bounding boxes union is incorrect", pmaxI1, pmaxI1Exp);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process polygon footprint
    // --------------------------------------------------------------------------------
    public void testBox3D9() {
        log.info("Test for method to process polygon footprint");
        // Creating boxes
        Coordinate pmin1 = new Coordinate(0,0,0);
        Coordinate pmax1 = new Coordinate(10,20,30);
        Box3D box = new Box3D(pmin1, pmax1);
        // Processing footprint
        Polygon footprint = box.to2D();
        // Expected result
        String s = "POLYGON ((0 0, 10 0, 10 20, 0 20, 0 0))";
        // Comparison
        assertEquals("Bounding box footprint is incorrect", s, footprint.toString());
    }
}
