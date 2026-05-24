package fr.ign.common.equation;

import fr.ign.common.tools.Utils;
import junit.framework.TestCase;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.logging.Logger;

public class TestApproximatedPlanEquation extends TestCase {
    // ---------------------------------- ATTRIBUTES ----------------------------------
    private static final double epsilon = Math.pow(10,-1);
    private static final Logger log = Logger.getLogger(TestApproximatedPlanEquation.class.getName());
    // ---------------------------------- PREPROCESS ----------------------------------
    // ------------------------------------ TESTS -------------------------------------
    // --------------------------------------------------------------------------------
    // Test for method to build approximated plan equation : 3 points
    // --------------------------------------------------------------------------------
    public void testApproximatedPlanEquation1() {
        log.info("Test for method to build approximated plan equation : 3 points");
        // Creating list of points
        ArrayList<Coordinate> dpl = new ArrayList<>();
        dpl.add(new Coordinate(0,0,0));
        dpl.add(new Coordinate(1,1,0));
        dpl.add(new Coordinate(1,2,0));
        // Creating plane
        ApproximatedPlanEquation apex = new ApproximatedPlanEquation(dpl.toArray(new Coordinate[0]));
        // Verifications
        double val1 = apex.equationValue(dpl.get(0));
        double val2 = apex.equationValue(dpl.get(1));
        double val3 = apex.equationValue(dpl.get(2));
        // Comparison
        assertEquals("Approximated plane equation does not fit definition points", 0, val1, epsilon);
        assertEquals("Approximated plane equation does not fit definition points", 0, val2, epsilon);
        assertEquals("Approximated plane equation does not fit definition points", 0, val3, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to build approximated plan equation : 1 million coplanar points
    // --------------------------------------------------------------------------------
    public void testApproximatedPlanEquation4() {
        log.info("Test for method to build approximated plan equation : 1 million coplanar points");
        // Number of points
        int n = 1000000;
        // Creating list of points
        ArrayList<Coordinate> dpl = new ArrayList<>();
        for (int i=0; i<n; i++){
            dpl.add(new Coordinate(Math.random()*100,Math.random()*100,10));
        }
        // Creating plane
        ApproximatedPlanEquation apex = new ApproximatedPlanEquation(dpl.toArray(new Coordinate[0]));
        // Comparison
        double val;
        for (int i=0; i<n; i++){
            val = apex.equationValue(dpl.get(0));
            assertEquals("Approximated plane equation does not fit definition points", 0, val, epsilon);
        }
    }

    // --------------------------------------------------------------------------------
    // Test for method to build approximated plan equation : surface
    // --------------------------------------------------------------------------------
    public void testApproximatedPlanEquation6() {
        log.info("Test for method to build approximated plan equation : surface");
        // Creating surface
        Polygon surface = Utils.createSquarePolygon(2, 0, 0);
        // Computing plane equation
        ApproximatedPlanEquation apex = new ApproximatedPlanEquation(surface.getCoordinates());
        // Comparison
        assertEquals("Approximated plane equation is incorrect", 0, apex.getCoeffa(), epsilon);
        assertEquals("Approximated plane equation is incorrect", 0, apex.getCoeffb(), epsilon);
        assertEquals("Approximated plane equation is incorrect", 1, Math.abs(apex.getCoeffc()), epsilon);
        assertEquals("Approximated plane equation is incorrect", 0, apex.getCoeffd(), epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to build approximated plan equation : polygon
    // --------------------------------------------------------------------------------
    public void testApproximatedPlanEquation7() {
        log.info("Test for method to build approximated plan equation : polygon");
        // Creating surface
        Polygon polygon = Utils.createSquarePolygon(2, 0, 0);
        // Computing plane equation
        ApproximatedPlanEquation apex = new ApproximatedPlanEquation(polygon.getCoordinates());
        // Comparison
        assertEquals("Approximated plane equation is incorrect", 0, apex.getCoeffa(), epsilon);
        assertEquals("Approximated plane equation is incorrect", 0, apex.getCoeffb(), epsilon);
        assertEquals("Approximated plane equation is incorrect", 1, Math.abs(apex.getCoeffc()), epsilon);
        assertEquals("Approximated plane equation is incorrect", 0, apex.getCoeffd(), epsilon);
    }
}
