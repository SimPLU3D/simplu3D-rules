package fr.ign.common.topology;

import junit.framework.TestCase;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.io.WKTWriter;

import java.util.ArrayList;
import java.util.logging.Logger;

public class TestVertex extends TestCase {
    // ---------------------------------- ATTRIBUTES ----------------------------------
    private static final double epsilon = Math.pow(10,-5);
    private static final Logger log = Logger.getLogger(TestVertex.class.getName());
    // ---------------------------------- PREPROCESS ----------------------------------
    // ------------------------------------ TESTS -------------------------------------
    // --------------------------------------------------------------------------------
    // Test for method to create vertex from coordinates
    // --------------------------------------------------------------------------------
    public void testVertex1() {
        log.info("Test for method to create vertex from coordinates");
        Vertex vertex = new Vertex(10, 10, 10);
        // If no fail
        assertEquals("Vertex X coordinate is incorrect", 10, vertex.getCoordinate().getX(), epsilon);
        assertEquals("Vertex Y coordinate is incorrect", 10, vertex.getCoordinate().getY(), epsilon);
        assertEquals("Vertex Z coordinate is incorrect", 10, vertex.getCoordinate().getZ(), epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to create vertex from direct position
    // --------------------------------------------------------------------------------
    public void testVertex2() {
        log.info("Test for method to create vertex from direct position");
        Vertex vertex = new Vertex(new Coordinate(10, 10, 10));
        // If no fail
        assertEquals("Vertex X coordinate is incorrect", 10, vertex.getCoordinate().getX(), epsilon);
        assertEquals("Vertex Y coordinate is incorrect", 10, vertex.getCoordinate().getY(), epsilon);
        assertEquals("Vertex Z coordinate is incorrect", 10, vertex.getCoordinate().getZ(), epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to determine common vertex in triangle list
    // --------------------------------------------------------------------------------
    public void testVertex3() {
        log.info("Test for method to determine common vertex in triangle list");
        // Creating vertices
        Vertex vertex1 = new Vertex(0, 0, 0);
        Vertex vertex2 = new Vertex(1, 0, 0);
        Vertex vertex3 = new Vertex(1, 1, 0);
        Vertex vertex6 = new Vertex(2, 0, 0);
        Vertex vertex7 = new Vertex(10, 10, 0);
        Vertex vertex8 = new Vertex(20, 10, 0);
        Vertex vertex9 = new Vertex(20, 20, 0);
        // Defining triangles
        vertex1.ajouteTriangle(new Triangle(vertex1, vertex2, vertex3));
        vertex2.ajouteTriangle(new Triangle(vertex1, vertex2, vertex3));
        vertex3.ajouteTriangle(new Triangle(vertex1, vertex2, vertex3));
        vertex2.ajouteTriangle(new Triangle(vertex2, vertex3, vertex6));
        vertex3.ajouteTriangle(new Triangle(vertex2, vertex3, vertex6));
        vertex6.ajouteTriangle(new Triangle(vertex2, vertex3, vertex6));
        vertex7.ajouteTriangle(new Triangle(vertex7, vertex8, vertex9));
        vertex8.ajouteTriangle(new Triangle(vertex7, vertex8, vertex9));
        vertex9.ajouteTriangle(new Triangle(vertex7, vertex8, vertex9));
        // Getting common triangles on vertex 2 and 3
        ArrayList<Triangle> TC = (ArrayList<Triangle>) vertex2.hasTriangleCommun(vertex3);
        // Tests
        WKTWriter writer = new WKTWriter(3);
        assertEquals("Wrong number of triangles detected", 2, TC.size());
        assertEquals("Common triangle is incorrect", "POLYGON Z((0 0 0, 1 0 0, 1 1 0, 0 0 0))", writer.write(TC.get(0).getPolygon()));
        assertEquals("Common triangle is incorrect", "POLYGON Z((1 0 0, 1 1 0, 2 0 0, 1 0 0))", writer.write(TC.get(1).getPolygon()));
    }
}
