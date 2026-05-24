package fr.ign.common.calculation;

import fr.ign.common.util.Util;
import junit.framework.TestCase;
import org.junit.Assert;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TestUtil extends TestCase {
    private final double epsilon = Math.pow(10, -4);    // Scale error
    private static final Logger log = Logger.getLogger(TestUtil.class.getName());
    private static final GeometryFactory geometryFactory = new GeometryFactory();
    // --------------------------------------------------------------------------------
    // Test for method to detect vertical faces
    // --------------------------------------------------------------------------------
    public void testCenterOf6() {
        log.info("Test for method to detect vertical faces");
        // Creating list of surfaces
        ArrayList<Polygon> AS = new ArrayList<>();

        // Surface 1
        List<Coordinate> dpl1 = new ArrayList<>();
        dpl1.add(new Coordinate(0,0,0));
        dpl1.add(new Coordinate(0,1,0));
        dpl1.add(new Coordinate(1,1,0));
        dpl1.add(new Coordinate(1,0,0));
        dpl1.add(new Coordinate(0,0,0));
        Polygon surface1 = geometryFactory.createPolygon(dpl1.toArray(new Coordinate[0]));
        // Surface 2
        List<Coordinate> dpl2 = new ArrayList<>();
        dpl2.add(new Coordinate(0,0,0));
        dpl2.add(new Coordinate(0,0,1));
        dpl2.add(new Coordinate(1,0,1));
        dpl2.add(new Coordinate(1,0,0));
        dpl2.add(new Coordinate(0,0,0));
        Polygon surface2 = geometryFactory.createPolygon(dpl2.toArray(new Coordinate[0]));
        // Filling surface list
        AS.add(surface1);
        AS.add(surface2);
        // Detecting vertical surface
        MultiPolygon MS = Util.detectVertical(AS, epsilon);
        // Recovering vertical surface
        Polygon vertical = (Polygon) MS.getGeometryN(0);
        // recovering number of detected surfaces
        int nDetect = MS.getNumGeometries();
        // Comparison
        assertEquals("Vertical surface has not been detected", vertical.toString(), surface2.toString());
        assertEquals("Wrong number of vertical surface has been detected", 1, nDetect);
    }

    // --------------------------------------------------------------------------------
    // Test for method to detect vertical faces with empty list
    // --------------------------------------------------------------------------------
    public void testCenterOf7() {
        log.info("Test for method to detect vertical faces with empty list");
        // Creating list of surfaces
        ArrayList<Polygon> AS = new ArrayList<>();
        // Detecting vertical surface
        MultiPolygon MS = Util.detectVertical(AS, epsilon);
        // recovering number of detected surfaces
        int nDetect = MS.getNumGeometries();
        // Comparison
        assertEquals("Wrong number of vertical surface has been detected", 0, nDetect);
    }

    // --------------------------------------------------------------------------------
    // Test for method to detect non-vertical faces
    // --------------------------------------------------------------------------------
    public void testCenterOf8() {
        log.info("Test for method to detect non-vertical faces");
        // Creating list of surfaces
        ArrayList<Polygon> AS = new ArrayList<>();
        // Surface 1
        List<Coordinate> dpl1 = new ArrayList<>();
        dpl1.add(new Coordinate(0,0,0));
        dpl1.add(new Coordinate(0,2,0));
        dpl1.add(new Coordinate(2,2,0));
        dpl1.add(new Coordinate(2,0,0));
        dpl1.add(new Coordinate(0,0,0));
        Polygon surface1 = geometryFactory.createPolygon(dpl1.toArray(new Coordinate[0]));
        // Surface 2
        List<Coordinate> dpl2 = new ArrayList<>();
        dpl2.add(new Coordinate(0,0,0));
        dpl2.add(new Coordinate(0,0,1));
        dpl2.add(new Coordinate(1,0,1));
        dpl2.add(new Coordinate(1,0,0));
        dpl2.add(new Coordinate(0,0,0));
        Polygon surface2 = geometryFactory.createPolygon(dpl2.toArray(new Coordinate[0]));
        // Filling surface list
        AS.add(surface1);
        AS.add(surface2);
        // Detecting vertical surface
        MultiPolygon MS = Util.detectNonVertical(AS, epsilon);
        // Recovering vertical surface
        Polygon noVertical = (Polygon) MS.getGeometryN(0);
        // recovering number of detected surfaces
        int nDetect = MS.getNumGeometries();
        // Comparison
        assertEquals("Non-vertical surface has not been detected", 8, noVertical.getLength(), epsilon);
        assertEquals("Wrong number of non-vertical surface has been detected", 1, nDetect);
    }

    // --------------------------------------------------------------------------------
    // Test for method to detect non-vertical faces with empty list
    // --------------------------------------------------------------------------------
    public void testCenterOf9() {
        log.info("Test for method to detect non-vertical faces with empty list");
        // Creating list of surfaces
        ArrayList<Polygon> AS = new ArrayList<>();
        // Detecting vertical surface
        MultiPolygon MS = Util.detectNonVertical(AS, epsilon);
        // recovering number of detected surfaces
        int nDetect = MS.getNumGeometries();
        // Comparison
        assertEquals("Wrong number of non-vertical surface has been detected", 0, nDetect);
    }

    // --------------------------------------------------------------------------------
    // Test for method to detect roof faces
    // --------------------------------------------------------------------------------
    public void testCenterOf10() {
        log.info("Test for method to detect roof faces");
        // Creating list of surfaces
        ArrayList<Polygon> AS = new ArrayList<>();
        // Surface 1
        List<Coordinate> dpl1 = new ArrayList<>();
        dpl1.add(new Coordinate(0,0,0));
        dpl1.add(new Coordinate(0,2,0));
        dpl1.add(new Coordinate(2,2,0));
        dpl1.add(new Coordinate(2,0,0));
        dpl1.add(new Coordinate(0,0,0));
        Polygon surface1 = geometryFactory.createPolygon(dpl1.toArray(new Coordinate[0]));
        // Surface 2
        List<Coordinate> dpl2 = new ArrayList<>();
        dpl2.add(new Coordinate(0,0,0));
        dpl2.add(new Coordinate(0,0,1));
        dpl2.add(new Coordinate(1,0,1));
        dpl2.add(new Coordinate(1,0,0));
        dpl2.add(new Coordinate(0,0,0));
        Polygon surface2 = geometryFactory.createPolygon(dpl2.toArray(new Coordinate[0]));
        // Filling surface list
        AS.add(surface1);
        AS.add(surface2);
        // Detecting vertical surface
        MultiPolygon MS = Util.detectRoof(AS, epsilon);
        // Recovering vertical surface
        Assert.assertNotNull(MS);
        Polygon noVertical = (Polygon) MS.getGeometryN(0);
        // recovering number of detected surfaces
        int nDetect = MS.getNumGeometries();
        // Comparison
        assertEquals("Roof has not been detected", 8, noVertical.getLength(), epsilon);
        assertEquals("Wrong number of roof surfaces has been detected", 1, nDetect);
    }

    // --------------------------------------------------------------------------------
    // Test for method to detect roof with empty list
    // --------------------------------------------------------------------------------
    public void testCenterOf11() {
        log.info("Test for method to detect roof with empty list");
        // Creating list of surfaces
        ArrayList<Polygon> AS = new ArrayList<>();
        // Detecting vertical surface
        MultiPolygon MS = Util.detectRoof(AS, epsilon);
        // Comparison
        assertNull("Wrong number of roof surfaces has been detected", MS);
    }

    // --------------------------------------------------------------------------------
    // Test for method to detect roof in list of triangles
    // --------------------------------------------------------------------------------
    public void testCenterOf12() {
        log.info("Test for method to detect roof in list of triangles");
        // Creating list of surfaces
        ArrayList<Polygon> AT = new ArrayList<>();
        // Surface 1
        List<Coordinate> dpl1 = new ArrayList<>();
        dpl1.add(new Coordinate(0,0,0));
        dpl1.add(new Coordinate(0,2,0));
        dpl1.add(new Coordinate(2,2,0));
        dpl1.add(new Coordinate(0,0,0));
        Polygon surface1 = geometryFactory.createPolygon(dpl1.toArray(new Coordinate[0]));
        // Surface 2
        List<Coordinate> dpl2 = new ArrayList<>();
        dpl2.add(new Coordinate(0,0,0));
        dpl2.add(new Coordinate(0,0,1));
        dpl2.add(new Coordinate(1,0,1));
        dpl2.add(new Coordinate(0,0,0));
        Polygon surface2 = geometryFactory.createPolygon(dpl2.toArray(new Coordinate[0]));
        // Filling surface list
        AT.add(surface1);
        AT.add(surface2);
        // Detecting vertical surface
        MultiPolygon MS = Util.detectRoofTriangles(AT, epsilon);
        // recovering number of detected surfaces
        Assert.assertNotNull(MS);
        int nDetect = MS.getNumGeometries();
        // Comparison
        assertEquals("Wrong number of roof surfaces has been detected", 1, nDetect);
    }

    // --------------------------------------------------------------------------------
    // Test for method to detect roof in list of triangles
    // --------------------------------------------------------------------------------
    public void testCenterOf13() {
        log.info("Test for method to detect roof in list of triangles");
        // Creating list of surfaces
        ArrayList<Polygon> AT = new ArrayList<>();
        List<Coordinate> dpl2 = new ArrayList<>();
        dpl2.add(new Coordinate(0,0,0));
        dpl2.add(new Coordinate(0,0,1));
        dpl2.add(new Coordinate(1,0,1));
        dpl2.add(new Coordinate(0,0,0));
        Polygon surface2 = geometryFactory.createPolygon(dpl2.toArray(new Coordinate[0]));
        // Filling surface list
        AT.add(surface2);
        // Detecting vertical surface
        MultiPolygon MS = Util.detectRoofTriangles(AT, epsilon);
        // Comparison
        assertNull("Wrong number of roof surfaces has been detected", MS);
    }

    // --------------------------------------------------------------------------------
    // Test for method to detect roof in list of triangles
    // --------------------------------------------------------------------------------
    public void testCenterOf14() {
        log.info("Test for method to detect roof in list of triangles");
        // Creating list of surfaces
        ArrayList<Polygon> AT = new ArrayList<>();
        // Surface 1
        List<Coordinate> dpl1 = new ArrayList<>();
        dpl1.add(new Coordinate(0,0,0));
        dpl1.add(new Coordinate(0,2,0));
        dpl1.add(new Coordinate(2,2,0));
        dpl1.add(new Coordinate(0,0,0));
        Polygon surface1 = geometryFactory.createPolygon(dpl1.toArray(new Coordinate[0]));
        // Surface 2
        List<Coordinate> dpl2 = new ArrayList<>();
        dpl2.add(new Coordinate(0,0,0));
        dpl2.add(new Coordinate(0,0,1));
        dpl2.add(new Coordinate(1,0,1));
        dpl2.add(new Coordinate(0,0,0));
        Polygon surface2 = geometryFactory.createPolygon(dpl2.toArray(new Coordinate[0]));
        // Filling surface list
        AT.add(surface1);
        AT.add(surface2);
        // Detecting vertical surface
        MultiPolygon MS = Util.detectRoofTriangles(AT, epsilon);
        // recovering number of detected surfaces
        Assert.assertNotNull(MS);
        int nDetect = MS.getNumGeometries();
        // Comparison
        assertEquals("Wrong number of floor surfaces has been detected", 1, nDetect);
    }

    // --------------------------------------------------------------------------------
    // Test for method to detect floor in list of triangles
    // --------------------------------------------------------------------------------
    public void testCenterOf15() {
        log.info("Test for method to detect floor in list of triangles");
        // Creating list of surfaces
        ArrayList<Polygon> AT = new ArrayList<>();
        List<Coordinate> dpl2 = new ArrayList<>();
        dpl2.add(new Coordinate(0,0,0));
        dpl2.add(new Coordinate(0,0,1));
        dpl2.add(new Coordinate(1,0,1));
        dpl2.add(new Coordinate(0,0,0));
        Polygon surface2 = geometryFactory.createPolygon(dpl2.toArray(new Coordinate[0]));
        // Filling surface list
        AT.add(surface2);
        // Detecting vertical surface
        MultiPolygon MS = Util.detectFloorTriangles(AT, epsilon);
        // Comparison
        assertNull("Wrong number of floor surfaces has been detected", MS);
    }
}
