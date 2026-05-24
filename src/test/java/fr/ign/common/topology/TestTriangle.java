package fr.ign.common.topology;

import fr.ign.common.equation.PlanEquation;
import junit.framework.TestCase;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.logging.Logger;

public class TestTriangle extends TestCase {
    // ---------------------------------- ATTRIBUTES ----------------------------------
    private static final double epsilon = Math.pow(10,-5);
    private static final Logger log = Logger.getLogger(TestTriangle.class.getName());
    // ---------------------------------- PREPROCESS ----------------------------------
    // ------------------------------------ TESTS -------------------------------------
    // --------------------------------------------------------------------------------
    // Test for method to create triangle
    // --------------------------------------------------------------------------------
    public void testTriangle1() {
        log.info("Test for method to create triangle");
        // Creating vertices
        Vertex[] VLIST = new Vertex[3];
        VLIST[0] = new Vertex(0, 0, 0);
        VLIST[1] = new Vertex(10, 0, 0);
        VLIST[2] = new Vertex(10, 10, 0);
        // Creating triangle
        Triangle triangle = new Triangle(VLIST);
        // Tests
        assertEquals("Triangle vertex 1 coordinate X is incorrect", 0.0, triangle.getCoordinate(0).getX());
        assertEquals("Triangle vertex 1 coordinate Y is incorrect", 0.0, triangle.getCoordinate(0).getY());
        assertEquals("Triangle vertex 1 coordinate Z is incorrect", 0.0, triangle.getCoordinate(0).getZ());
        assertEquals("Triangle vertex 2 coordinate X is incorrect", 10.0, triangle.getCoordinate(1).getX());
        assertEquals("Triangle vertex 2 coordinate Y is incorrect", 0.0, triangle.getCoordinate(1).getY());
        assertEquals("Triangle vertex 2 coordinate Z is incorrect", 0.0, triangle.getCoordinate(1).getZ());
        assertEquals("Triangle vertex 3 coordinate X is incorrect", 10.0, triangle.getCoordinate(2).getX());
        assertEquals("Triangle vertex 3 coordinate Y is incorrect", 10.0, triangle.getCoordinate(2).getY());
        assertEquals("Triangle vertex 3 coordinate Z is incorrect", 0.0, triangle.getCoordinate(2).getZ());
    }

    // --------------------------------------------------------------------------------
    // Test for method to process plane equation from triangle
    // --------------------------------------------------------------------------------
    public void testTriangle2() {
        log.info("Test for method to process plane equation from triangle");
        // Creating vertices
        Vertex[] VLIST = new Vertex[3];
        VLIST[0] = new Vertex(0, 0, 0);
        VLIST[1] = new Vertex(10, 0, 0);
        VLIST[2] = new Vertex(10, 10, 0);
        // Creating triangle
        Triangle triangle = new Triangle(VLIST);
        // Processing plane equation
        PlanEquation pEq = triangle.getPlanEquation();
        // Tests
        assertEquals("Vertex 1 should be included in plane", 0.0, pEq.equationValue(VLIST[0].getCoordinate()));
        assertEquals("Vertex 2 should be included in plane", 0.0, pEq.equationValue(VLIST[1].getCoordinate()));
        assertEquals("Vertex 3 should be included in plane", 0.0, pEq.equationValue(VLIST[2].getCoordinate()));
    }

    // --------------------------------------------------------------------------------
    // Test for method to process triangle center
    // --------------------------------------------------------------------------------
    public void testTriangle3() {
        log.info("Test for method to process process triangle center");
        // Creating vertices
        Vertex[] VLIST = new Vertex[3];
        VLIST[0] = new Vertex(0, 0, 0);
        VLIST[1] = new Vertex(10, 0, 0);
        VLIST[2] = new Vertex(5, 10, 0);
        // Creating triangle
        Triangle triangle = new Triangle(VLIST);
        // Processing center
        Coordinate dp = (Coordinate) triangle.getCenter();
        // Tests
        assertEquals("Triangle center X coordinate is incorrect", 5, dp.getX(), epsilon);
        assertEquals("Triangle center Y coordinate is incorrect", 10.0/3.0, dp.getY(), epsilon);
        assertEquals("Triangle center Z coordinate is incorrect", 0, dp.getZ(), epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process orientable surface
    // --------------------------------------------------------------------------------
    public void testTriangle4() {
        log.info("Test for method to process orientable surface");
        // Creating vertices
        Vertex[] VLIST = new Vertex[3];
        VLIST[0] = new Vertex(0, 0, 0);
        VLIST[1] = new Vertex(10, 0, 0);
        VLIST[2] = new Vertex(5, 10, 0);
        // Creating triangle
        Triangle triangle = new Triangle(VLIST);
        // Processing orientable surface
        Polygon surface = triangle.getPolygon();
        // Tests
        assertEquals("Created surface area is incorrect", 50, surface.getArea(), epsilon);
        assertEquals("Created surface centroid is incorrect", surface.getCentroid().getCoordinate().getX(), triangle.getCenter().getX(), epsilon);
        assertEquals("Created surface centroid is incorrect", surface.getCentroid().getCoordinate().getY(), triangle.getCenter().getY(), epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to translate triangle
    // --------------------------------------------------------------------------------
    public void testTriangle5() {
        log.info("Test for method to translate triangle");
        // Creating vertices
        Vertex[] VLIST = new Vertex[3];
        VLIST[0] = new Vertex(0, 0, 0);
        VLIST[1] = new Vertex(10, 0, 0);
        VLIST[2] = new Vertex(5, 10, 0);
        // Creating triangle
        Triangle triangle = new Triangle(VLIST);
        // Translating triangle
        triangle = triangle.translateTriangle(1, 1, 1);
        // Expected result
        Coordinate pt1 = new Coordinate(1, 1, 1);
        Coordinate pt2 = new Coordinate(11, 1, 1);
        Coordinate pt3 = new Coordinate(6, 11, 1);
        // Tests
        assertEquals("Translated triangle vertex 1 is incorrect", triangle.getCoordinate(0), pt1);
        assertEquals("Translated triangle vertex 2 is incorrect", triangle.getCoordinate(1), pt2);
        assertEquals("Translated triangle vertex 3 is incorrect", triangle.getCoordinate(2), pt3);
    }

    // --------------------------------------------------------------------------------
    // Test for method to translate triangle from vertex
    // --------------------------------------------------------------------------------
    public void testTriangle6() {
        log.info("Test for method to translate triangle from vertex");
        // Creating vertices
        Vertex[] VLIST = new Vertex[3];
        VLIST[0] = new Vertex(0, 0, 0);
        VLIST[1] = new Vertex(10, 0, 0);
        VLIST[2] = new Vertex(5, 10, 0);
        // Creating triangle
        Triangle triangle = new Triangle(VLIST);
        // Translating triangle
        triangle = triangle.translateTriangle(new Coordinate(1, 1, 1));
        // Expected result
        Coordinate pt1 = new Coordinate(1, 1, 1);
        Coordinate pt2 = new Coordinate(11, 1, 1);
        Coordinate pt3 = new Coordinate(6, 11, 1);
        // Tests
        assertEquals("Translated triangle vertex 1 is incorrect", triangle.getCoordinate(0), pt1);
        assertEquals("Translated triangle vertex 2 is incorrect", triangle.getCoordinate(1), pt2);
        assertEquals("Translated triangle vertex 3 is incorrect", triangle.getCoordinate(2), pt3);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process triangle edges
    // --------------------------------------------------------------------------------
    public void testTriangle7() {
        log.info("Test for method to process triangle edges");
        // Creating vertices
        Vertex[] VLIST = new Vertex[3];
        VLIST[0] = new Vertex(0, 0, 0);
        VLIST[1] = new Vertex(10, 0, 0);
        VLIST[2] = new Vertex(5, 10, 0);
        // Creating triangle
        Triangle triangle = new Triangle(VLIST);
        // Processing edges
        ArrayList<Edge> EDGES = (ArrayList<Edge>) triangle.calculEdge();
        // Tests
        assertEquals("Wrong number of edges in triangle", 3, EDGES.size());
    }

    // --------------------------------------------------------------------------------
    // Test for method to process triangle barycenter
    // --------------------------------------------------------------------------------
    public void testTriangle8() {
        log.info("Test for method to process process triangle barycenter");
        log.warning("Redundancy with 'center' method");
        // Creating vertices
        Vertex[] VLIST = new Vertex[3];
        VLIST[0] = new Vertex(0, 0, 0);
        VLIST[1] = new Vertex(10, 0, 0);
        VLIST[2] = new Vertex(5, 10, 0);
        // Creating triangle
        Triangle triangle = new Triangle(VLIST);
        // Processing center
        Coordinate dp = triangle.Barycentre();
        // Tests
        assertEquals("Triangle center X coordinate is incorrect", 5, dp.getX(), epsilon);
        assertEquals("Triangle center Y coordinate is incorrect", 10.0/3.0, dp.getY(), epsilon);
        assertEquals("Triangle center Z coordinate is incorrect", 0, dp.getZ(), epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to know if edges are included in triangles
    // --------------------------------------------------------------------------------
    public void testTriangle9() {
        log.info("Test for method to process triangle edges");
        // Creating vertices
        Vertex[] VLIST = new Vertex[3];
        VLIST[0] = new Vertex(0, 0, 0);
        VLIST[1] = new Vertex(10, 0, 0);
        VLIST[2] = new Vertex(5, 10, 0);
        // Creating triangle
        Triangle triangle = new Triangle(VLIST);
        // Processing edges
        ArrayList<Edge> EDGES = (ArrayList<Edge>) triangle.calculEdge();
        // Tests
        assertTrue("Edge 1 should be included in triangle", triangle.containEdge(EDGES.get(0)));
        assertTrue("Edge 2 should be included in triangle", triangle.containEdge(EDGES.get(1)));
        assertTrue("Edge 3 should be included in triangle", triangle.containEdge(EDGES.get(2)));
    }

    // --------------------------------------------------------------------------------
    // Test for method to know if edges are included in triangles
    // --------------------------------------------------------------------------------
    public void testTriangle10() {
        log.info("Test for method to process triangle edges");
        // Creating vertices
        Vertex[] VLIST = new Vertex[3];
        VLIST[0] = new Vertex(0, 0, 0);
        VLIST[1] = new Vertex(10, 0, 0);
        VLIST[2] = new Vertex(5, 10, 0);
        // Creating triangle
        Triangle triangle = new Triangle(VLIST);
        // Processing edges
        ArrayList<Edge> EDGES = (ArrayList<Edge>) triangle.calculEdge();
        // Tests
        assertTrue("Edge 1 should be included in triangle", triangle.contientEdge(EDGES.get(0)));
        assertTrue("Edge 2 should be included in triangle", triangle.contientEdge(EDGES.get(1)));
        assertTrue("Edge 3 should be included in triangle", triangle.contientEdge(EDGES.get(2)));
    }
}
