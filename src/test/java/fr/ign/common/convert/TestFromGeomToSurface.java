package fr.ign.common.convert;

import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.tools.Utils;
import junit.framework.TestCase;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTWriter;

import java.util.List;
import java.util.logging.Logger;

public class TestFromGeomToSurface extends TestCase {
    private static final Logger log = Logger.getLogger(TestFromGeomToSurface.class.getName());
    // ----------------------------------- METHODS ------------------------------------
    // --------------------------------------------------------------------------------
    // Test for method to convert solid to surfaces
    // --------------------------------------------------------------------------------
    public void testConvertGeom1() {
        log.info("Test for method to convert solid to surfaces");
        // Creating solid
        Geometry cube = Utils.createCube(0, 0, 0, 10);
        // Converting into surfaces
        List<Polygon> AS = FromGeomToSurface.convertGeom(cube);
        WKTWriter writer = new WKTWriter(3);
        String s1 = "POLYGON Z((0 0 0, 10 0 0, 10 10 0, 0 10 0, 0 0 0))";
        String s2 = "POLYGON Z((0 0 0, 10 0 0, 10 0 10, 0 0 10, 0 0 0))";
        String s3 = "POLYGON Z((0 0 0, 0 10 0, 0 10 10, 0 0 10, 0 0 0))";
        String s4 = "POLYGON Z((0 0 10, 10 0 10, 10 10 10, 0 10 10, 0 0 10))";
        String s5 = "POLYGON Z((0 10 0, 10 10 0, 10 10 10, 0 10 10, 0 10 0))";
        String s6 = "POLYGON Z((10 0 0, 10 10 0, 10 10 10, 10 0 10, 10 0 0))";
        String ps1 = writer.write(AS.get(0));
        String ps2 = writer.write(AS.get(1));
        String ps3 = writer.write(AS.get(2));
        String ps4 = writer.write(AS.get(3));
        String ps5 = writer.write(AS.get(4));
        String ps6 = writer.write(AS.get(5));
        boolean b1 = ((ps1.equals(s1))||(ps1.equals(s2))||(ps1.equals(s3))||(ps1.equals(s4))||(ps1.equals(s5))||(ps1.equals(s6)));
        boolean b2 = ((ps2.equals(s1))||(ps2.equals(s2))||(ps2.equals(s3))||(ps2.equals(s4))||(ps2.equals(s5))||(ps2.equals(s6)));
        boolean b3 = ((ps3.equals(s1))||(ps3.equals(s2))||(ps3.equals(s3))||(ps3.equals(s4))||(ps3.equals(s5))||(ps3.equals(s6)));
        boolean b4 = ((ps4.equals(s1))||(ps4.equals(s2))||(ps4.equals(s3))||(ps4.equals(s4))||(ps4.equals(s5))||(ps4.equals(s6)));
        boolean b5 = ((ps5.equals(s1))||(ps5.equals(s2))||(ps5.equals(s3))||(ps5.equals(s4))||(ps5.equals(s5))||(ps5.equals(s6)));
        boolean b6 = ((ps6.equals(s1))||(ps6.equals(s2))||(ps6.equals(s3))||(ps6.equals(s4))||(ps6.equals(s5))||(ps6.equals(s6)));
        // Comparison
        assertTrue("Wrong decomposition in surfaces",b1);
        assertTrue("Wrong decomposition in surfaces",b2);
        assertTrue("Wrong decomposition in surfaces",b3);
        assertTrue("Wrong decomposition in surfaces",b4);
        assertTrue("Wrong decomposition in surfaces",b5);
        assertTrue("Wrong decomposition in surfaces",b6);

    }

    // --------------------------------------------------------------------------------
    // Test for method to convert solid into multi-surfaces
    // --------------------------------------------------------------------------------
    public void testConvertGeom2() {
        log.info("Test for method to convert solid into multi-surfaces");
        // Creating solid
        Geometry cube = Utils.createCube(0, 0, 0, 10);
        // Converting into surfaces
        MultiPolygon AS = FromGeomToSurface.convertMSGeom(cube);
        WKTWriter writer = new WKTWriter(3);
        String s1 = "POLYGON Z((0 0 0, 10 0 0, 10 10 0, 0 10 0, 0 0 0))";
        String s2 = "POLYGON Z((0 0 0, 10 0 0, 10 0 10, 0 0 10, 0 0 0))";
        String s3 = "POLYGON Z((0 0 0, 0 10 0, 0 10 10, 0 0 10, 0 0 0))";
        String s4 = "POLYGON Z((0 0 10, 10 0 10, 10 10 10, 0 10 10, 0 0 10))";
        String s5 = "POLYGON Z((0 10 0, 10 10 0, 10 10 10, 0 10 10, 0 10 0))";
        String s6 = "POLYGON Z((10 0 0, 10 10 0, 10 10 10, 10 0 10, 10 0 0))";
        String ps1 = writer.write(AS.getGeometryN(0));
        String ps2 = writer.write(AS.getGeometryN(1));
        String ps3 = writer.write(AS.getGeometryN(2));
        String ps4 = writer.write(AS.getGeometryN(3));
        String ps5 = writer.write(AS.getGeometryN(4));
        String ps6 = writer.write(AS.getGeometryN(5));
        boolean b1 = ((ps1.equals(s1))||(ps1.equals(s2))||(ps1.equals(s3))||(ps1.equals(s4))||(ps1.equals(s5))||(ps1.equals(s6)));
        boolean b2 = ((ps2.equals(s1))||(ps2.equals(s2))||(ps2.equals(s3))||(ps2.equals(s4))||(ps2.equals(s5))||(ps2.equals(s6)));
        boolean b3 = ((ps3.equals(s1))||(ps3.equals(s2))||(ps3.equals(s3))||(ps3.equals(s4))||(ps3.equals(s5))||(ps3.equals(s6)));
        boolean b4 = ((ps4.equals(s1))||(ps4.equals(s2))||(ps4.equals(s3))||(ps4.equals(s4))||(ps4.equals(s5))||(ps4.equals(s6)));
        boolean b5 = ((ps5.equals(s1))||(ps5.equals(s2))||(ps5.equals(s3))||(ps5.equals(s4))||(ps5.equals(s5))||(ps5.equals(s6)));
        boolean b6 = ((ps6.equals(s1))||(ps6.equals(s2))||(ps6.equals(s3))||(ps6.equals(s4))||(ps6.equals(s5))||(ps6.equals(s6)));
        // Comparison
        assertTrue("Wrong decomposition in surfaces",b1);
        assertTrue("Wrong decomposition in surfaces",b2);
        assertTrue("Wrong decomposition in surfaces",b3);
        assertTrue("Wrong decomposition in surfaces",b4);
        assertTrue("Wrong decomposition in surfaces",b5);
        assertTrue("Wrong decomposition in surfaces",b6);
    }
}
