package fr.ign.common.equation;

import fr.ign.common.util.Vecteur;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.locationtech.jts.geom.Coordinate;

import java.util.logging.Logger;

public class TestLineEquation extends TestCase {
    // ---------------------------------- ATTRIBUTES ----------------------------------
    private static final double epsilon = Math.pow(10,-1);
    private static final Logger log = Logger.getLogger(TestLineEquation.class.getName());
    // ---------------------------------- PREPROCESS ----------------------------------
    // ------------------------------------ TESTS -------------------------------------
    // --------------------------------------------------------------------------------
    // Test for method to build line equation from point and vector
    // --------------------------------------------------------------------------------
    public void testLineEquation1() {
        log.info("Test for method to build line equation from point and vector");
        // Creating point
        Coordinate p = new Coordinate(10,10,10);
        // Creating vector
        Vecteur v = (new Vecteur(1,1,2)).getNormalised();
        // Creating line equation
        LineEquation eq = new LineEquation(p, v);
        // Comparisons
        assertEquals("Line equation a0 parameter is incorrect", p.getX(), eq.getA0(), epsilon);
        assertEquals("Line equation b0 parameter is incorrect", p.getY(), eq.getB0(), epsilon);
        assertEquals("Line equation c0 parameter is incorrect", p.getZ(), eq.getC0(), epsilon);
        assertEquals("Line equation a1 parameter is incorrect", v.getX(), eq.getA1(), epsilon);
        assertEquals("Line equation a1 parameter is incorrect", v.getY(), eq.getB1(), epsilon);
        assertEquals("Line equation a1 parameter is incorrect", v.getZ(), eq.getC1(), epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to build line equation from two points
    // --------------------------------------------------------------------------------
    public void testLineEquation2() {
        log.info("Test for method to build line equation from two points");
        // Creating points
        Coordinate p1 = new Coordinate(10,10,10);
        Coordinate p2 = new Coordinate(11,11,12);
        // Creating line equation
        LineEquation eq = new LineEquation(p1, p2);
        // Comparisons
        assertEquals("Line equation a0 parameter is incorrect", p1.getX(), eq.getA0(), epsilon);
        assertEquals("Line equation b0 parameter is incorrect", p1.getY(), eq.getB0(), epsilon);
        assertEquals("Line equation c0 parameter is incorrect", p1.getZ(), eq.getC0(), epsilon);
        assertEquals("Line equation a1 parameter is incorrect", p2.getX()-p1.getX(), eq.getA1(), epsilon);
        assertEquals("Line equation a1 parameter is incorrect", p2.getY()-p1.getY(), eq.getB1(), epsilon);
        assertEquals("Line equation a1 parameter is incorrect", p2.getZ()-p1.getZ(), eq.getC1(), epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process value at t
    // --------------------------------------------------------------------------------
    public void testLineEquation3() {
        log.info("Test for method to process value at t");
        // Creating point
        Coordinate p = new Coordinate(10,10,10);
        // Creating vector
        Vecteur v = (new Vecteur(1,1,2)).getNormalised();
        // Creating line equation
        LineEquation eq = new LineEquation(p, v);
        // Computing value at 0 and 2
        Coordinate p0 = eq.valueAt(0);
        Coordinate p2 = eq.valueAt(2);
        // Processing expected result
        double x = p.getX()+2*v.getX();
        double y = p.getY()+2*v.getY();
        double z = p.getZ()+2*v.getZ();
        Coordinate p2Exp = new Coordinate(x,y,z);
        // Comparisons
        assertEquals("Line equation value at 0 is incorrect", p0, p);
        assertEquals("Line equation value at 2 is incorrect", p2, p2Exp);
    }

    // --------------------------------------------------------------------------------
    // Test for method to know if a point belongs to a line
    // --------------------------------------------------------------------------------
    public void testLineEquation4() {
        log.info("Test for method to know if a point belongs to a line");
        // Creating point
        Coordinate p = new Coordinate(10,10,10);
        // Creating vector
        Vecteur v = (new Vecteur(1,1,2)).getNormalised();
        // Creating line equation
        LineEquation eq = new LineEquation(p, v);
        // Creating points
        Coordinate p1 = new Coordinate(9,9,8);
        Coordinate p2 = new Coordinate(9,9,7);
        // Processing method
        boolean b1 = eq.isPointOnLine(p1);
        boolean b2 = eq.isPointOnLine(p2);
        // Comparisons
        assertTrue("Point should satisfy line equation", b1);
        assertFalse("Point should not satisfy line equation", b2);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process intersection with a plane
    // --------------------------------------------------------------------------------
    public void testLineEquation5() {
        log.info("Test for method to process intersection with a plane");
        // Creating point
        Coordinate p = new Coordinate(10,10,10);
        // Creating vector
        Vecteur v = (new Vecteur(1,0,1)).getNormalised();
        // Creating line equation
        LineEquation eqL = new LineEquation(p, v);
        // Creating plan
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(1,0,0);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation
        PlanEquation eqP = new PlanEquation(new Coordinate[]{p1, p2, p3});
        // Processing intersection
        Coordinate pi = eqL.intersectionLinePlan(eqP);
        // Expected intersection
        Coordinate piExp = new Coordinate(0,10,0);
        // Comparison
        assertEquals("Intersection between line and point is incorrect", pi, piExp);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process intersection with a plane (parallel case)
    // --------------------------------------------------------------------------------
    public void testLineEquation6() {
        log.info("Test for method to process intersection with a plane (parallel case)");
        // Creating point
        Coordinate p = new Coordinate(10,10,10);
        // Creating vector
        Vecteur v = (new Vecteur(1,1,0)).getNormalised();
        // Creating line equation
        LineEquation eqL = new LineEquation(p, v);
        // Creating plan
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(1,0,0);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation
        PlanEquation eqP = new PlanEquation(new Coordinate[]{p1, p2, p3});
        // Processing intersection
        Coordinate pi = eqL.intersectionLinePlan(eqP);
        // Comparison
        assertNull("There should be no intersection in parallel case", pi);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process distance between a line and a point
    // --------------------------------------------------------------------------------
    public void testLineEquation7() {
        log.info("Test for method to process distance between a line and a point");
        // Creating point
        Coordinate p = new Coordinate(10,10,10);
        // Creating vector
        Vecteur v = (new Vecteur(1,1,0)).getNormalised();
        // Creating line equation
        LineEquation eq = new LineEquation(p, v);
        // Creating points
        Coordinate p1 = new Coordinate(11,11,10);
        Coordinate p2 = new Coordinate(12,11,10);
        // Processing distances
        double d1 = eq.distance(p1);
        double d2 = eq.distance(p2);
        // Expected result
        double d2Exp = Math.sqrt(2)/2;
        // Comparison
        assertEquals("Distance between line and point is incorrect", 0, d1, epsilon);
        assertEquals("Distance between line and point is incorrect", d2Exp, d2, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process intersection between two lines
    // --------------------------------------------------------------------------------
    public void testLineEquation8() {
        log.info("Test for method to process intersection between two lines");
        // Creating random point
        double x = Math.random()*100;
        double y = Math.random()*100;
        double z = Math.random()*100;
        Coordinate p = new Coordinate(x,y,z);
        // Creating random vectors
        double x1 = Math.random()*100;
        double y1 = Math.random()*100;
        double z1 = Math.random()*100;
        double x2 = Math.random()*100;
        double y2 = Math.random()*100;
        double z2 = Math.random()*100;
        Vecteur v1 = (new Vecteur(x1,y1,z1)).getNormalised();
        Vecteur v2 = (new Vecteur(x2,y2,z2)).getNormalised();
        // Creating line equations
        LineEquation eq1 = new LineEquation(p, v1);
        LineEquation eq2 = new LineEquation(p, v2);
        // Processing intersection
        Coordinate pi = (Coordinate) eq1.intersectionLineLine(eq2);
        // Comparison
        assertEquals("Intersection between lines is incorrect", pi, p);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process intersection between two lines (parallel case)
    // --------------------------------------------------------------------------------
    public void testLineEquation9() {
        log.info("Test for method to process intersection between two lines (parallel case)");
        // Creating random points
        double x = Math.random()*100;
        double y = Math.random()*100;
        double z = Math.random()*100;
        Coordinate p1 = new Coordinate(x,y,z);
        Coordinate p2 = new Coordinate(x+1,y+1,z+1);
        // Creating random vector
        double x1 = Math.random()*100;
        double y1 = Math.random()*100;
        double z1 = Math.random()*100;
        Vecteur v = (new Vecteur(x1,y1,z1)).getNormalised();
        // Creating line equations
        LineEquation eq1 = new LineEquation(p1, v);
        LineEquation eq2 = new LineEquation(p2, v);
        // Processing intersection
        Coordinate pi = eq1.intersectionLineLine(eq2);
        // Comparison
        assertNull("Intersection between lines is incorrect", pi);
    }
}
