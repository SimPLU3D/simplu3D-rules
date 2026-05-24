package fr.ign.common.convert;

import fr.ign.common.extrusion.Extrusion2DObject;
import fr.ign.common.tools.Utils;
import junit.framework.TestCase;
import org.junit.Assert;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTWriter;

import java.util.logging.Logger;

public class TestExtrusion2DObject extends TestCase {
    private static final Logger log = Logger.getLogger(TestExtrusion2DObject.class.getName());
    private static final GeometryFactory factory = new GeometryFactory();
    // ----------------------------------- METHODS ------------------------------------
    // --------------------------------------------------------------------------------
    // Test for method to extrude 2D object
    // --------------------------------------------------------------------------------
    public void testConvertFromGeometry1() {
        log.info("Test for method to extrude 2D object");
        // Creating square
        Polygon square = Utils.createSquarePolygon(1, 0, 0);
        // Operating extrusion
        Geometry cube = Extrusion2DObject.convertFromGeometry(square, 0, 1);
        // Processing expected result
        Geometry cubeExp = Utils.createCube(0,0,0,1);
        // Comparison
        assertTrue("Extruded geometry is incorrect", cube.equalsTopo(cubeExp));
    }

    // --------------------------------------------------------------------------------
    // Test for method to extrude 2D object (null object)
    // --------------------------------------------------------------------------------
    public void testConvertFromGeometry2() {
        log.info("Test for method to extrude 2D object (null object)");
        // Operating extrusion
        Geometry cube = Extrusion2DObject.convertFromGeometry(null, 0, 1);
        // Comparison
        assertNull("Extruded null geometry is incorrect", cube);
    }

    // --------------------------------------------------------------------------------
    // Test for method to extrude 2D polygon
    // --------------------------------------------------------------------------------
    public void testConvertFromGeometry3() {
        log.info("Test for method to extrude 2D polygon ");
        // Creating square
        Polygon square = Utils.createSquarePolygon(1, 0, 0);
        // Operating extrusion
        Geometry cube = Extrusion2DObject.convertFromPolygon(square, 0, 1);
        // Processing expected result
        Geometry cubeExp = Utils.createCube(0,0,0,1);
        // Comparison
        Assert.assertNotNull(cube);
        assertTrue("Extruded geometry is incorrect", cube.equalsTopo(cubeExp));
    }

    // --------------------------------------------------------------------------------
    // Test for method to extrude 2D line
    // --------------------------------------------------------------------------------
    public void testConvertFromGeometry4() {
        log.info("Test for method to extrude 2D line");
        // Creating 1D line string
        LineString line = factory.createLineString(new Coordinate[]{new Coordinate(0, 0), new Coordinate(1, 0)});
        // Operating line extrusion
        Geometry surface = Extrusion2DObject.convertFromLine(line, 0, 1);
        WKTWriter writer = new WKTWriter(3);
        // Processing expected result
        String sExp = "MULTIPOLYGON Z(((1 0 0, 0 0 0, 0 0 1, 1 0 1, 1 0 0)))";
        // Comparison
        assertEquals("Extruded line is incorrect", sExp, writer.write(surface));
    }

    // --------------------------------------------------------------------------------
    // Test for method to extrude 2D point
    // --------------------------------------------------------------------------------
    public void testConvertFromGeometry5() {
        log.info("Test for method to extrude 2D point");
        // Creating point
        Point p = factory.createPoint(new Coordinate(0,0));
        // Operating extrusion
        Geometry line = Extrusion2DObject.convertFromPoint(p, 10, 15);
        // Processing expected result
        LineString lineExpected = factory.createLineString(new Coordinate[]{new Coordinate(0, 0, 10), new Coordinate(0, 0, 15)});
        // Comparison
        assertEquals("Extruded point is incorrect", line.toString(), lineExpected.toString());
    }
}
