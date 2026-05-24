package fr.ign.common.convert;

import fr.ign.common.conversion.FromPolygonToLineString;
import fr.ign.common.tools.Utils;
import junit.framework.TestCase;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TestFromPolygonToLineString extends TestCase {
    private static final Logger log = Logger.getLogger(TestFromPolygonToLineString.class.getName());
    // ----------------------------------- METHODS ------------------------------------
    // --------------------------------------------------------------------------------
    // Test for method to convert polygon to line strings
    // --------------------------------------------------------------------------------
    public void testConvertListPolToLineStrings1() {
        log.info("Test for method to convert polygon to line strings");
        // Creating square polygon
        Polygon square = Utils.createSquarePolygon(2, 0, 0);
        // Recovering line strings
        List<LineString> LINES = FromPolygonToLineString.convertPolygonToLineStrings(square);
        assertEquals("Wrong number of line string in polygon decomposition", 4, LINES.size());
    }

    // --------------------------------------------------------------------------------
    // Test for method to convert a list of polygons to line strings
    // --------------------------------------------------------------------------------
    public void testConvertListPolToLineStrings2() {
        log.info("Test for method to convert a list of polygons to line strings");
        // Creating square polygon
        Polygon square1 = Utils.createSquarePolygon(2, 0, 0);
        Polygon square2 = Utils.createSquarePolygon(2, 0, 0);
        // Creating list
        ArrayList<Polygon> LIST = new ArrayList<>();
        LIST.add(square1);
        LIST.add(square2);
        // Recovering line strings
        List<LineString> LINES = FromPolygonToLineString.convertPolygonToLineStrings(LIST);
        assertEquals("Wrong number of line string in polygon decomposition", 8, LINES.size());
    }
}
