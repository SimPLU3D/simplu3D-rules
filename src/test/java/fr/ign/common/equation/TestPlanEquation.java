package fr.ign.common.equation;

import fr.ign.common.tools.Utils;
import fr.ign.common.topology.Triangle;
import fr.ign.common.topology.Vertex;
import fr.ign.common.util.Vecteur;
import junit.framework.TestCase;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TestPlanEquation extends TestCase {
    // ---------------------------------- ATTRIBUTES ----------------------------------
    private static final double epsilon = Math.pow(10,-3);
    private static final Logger log = Logger.getLogger(TestPlanEquation.class.getName());
    // ---------------------------------- PREPROCESS ----------------------------------
    // ------------------------------------ TESTS -------------------------------------
    // --------------------------------------------------------------------------------
    // Test for method to compute plane equation from 3 points : Oxy plane
    // --------------------------------------------------------------------------------
    public void testPlanEquation1() {
        log.info("Test for method to compute plane equation from 3 points : Oxy plane");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(1,0,0);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1, p2, p3);
        // Recovering parameteres
        double a = eq.getCoeffa();
        double b = eq.getCoeffb();
        double c = eq.getCoeffc();
        double d = eq.getCoeffd();
        // Expected result
        double aExp = 0;
        double bExp = 0;
        double cExp = 1;
        double dExp = 0;
        // Normalizations
        double somme = aExp+bExp+cExp+dExp;
        aExp = aExp/somme;
        bExp = bExp/somme;
        cExp = cExp/somme;
        dExp = dExp/somme;
        // Comparsion
        assertEquals("a coefficient in plane equation is incorrect", aExp, a, epsilon);
        assertEquals("b coefficient in plane equation is incorrect", bExp, b, epsilon);
        assertEquals("c coefficient in plane equation is incorrect", cExp, c, epsilon);
        assertEquals("d coefficient in plane equation is incorrect", dExp, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute plane equation from 3 points : Oxz plane
    // --------------------------------------------------------------------------------
    public void testPlanEquation2() {
        log.info("Test for method to compute plane equation from 3 points : Oxz plane");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(1,0,0);
        Coordinate p3 = new Coordinate(0,0,1);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1, p2, p3);
        // Recovering parameteres
        double a = eq.getCoeffa();
        double b = eq.getCoeffb();
        double c = eq.getCoeffc();
        double d = eq.getCoeffd();
        // Expected result
        double aExp = 0;
        double bExp = 1;
        double cExp = 0;
        double dExp = 0;
        // Normalizations
        double somme = a+b+c+d;
        a = a/somme;
        b = b/somme;
        c = c/somme;
        d = d/somme;
        // Comparison
        assertEquals("a coefficient in plane equation is incorrect", aExp, a, epsilon);
        assertEquals("b coefficient in plane equation is incorrect", bExp, b, epsilon);
        assertEquals("c coefficient in plane equation is incorrect", cExp, c, epsilon);
        assertEquals("d coefficient in plane equation is incorrect", dExp, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute plane equation from 3 points : Oyz plane
    // --------------------------------------------------------------------------------
    public void testPlanEquation3() {
        log.info("Test for method to compute plane equation from 3 points : Oyz plane");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(0,1,0);
        Coordinate p3 = new Coordinate(0,0,1);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1, p2, p3);
        // Recovering parameteres
        double a = eq.getCoeffa();
        double b = eq.getCoeffb();
        double c = eq.getCoeffc();
        double d = eq.getCoeffd();
        // Expected result
        double aExp = 1;
        double bExp = 0;
        double cExp = 0;
        double dExp = 0;
        // Normalizations
        double somme = a+b+c+d;
        a = a/somme;
        b = b/somme;
        c = c/somme;
        d = d/somme;
        // Comparison
        assertEquals("a coefficient in plane equation is incorrect", aExp, a, epsilon);
        assertEquals("b coefficient in plane equation is incorrect", bExp, b, epsilon);
        assertEquals("c coefficient in plane equation is incorrect", cExp, c, epsilon);
        assertEquals("d coefficient in plane equation is incorrect", dExp, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute plane equation from 3 points : random plane
    // --------------------------------------------------------------------------------
    public void testPlanEquation4() {
        log.info("Test for method to compute plane equation from 3 points : random plane");
        // Creating random points
        Coordinate p1 = new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100);
        Coordinate p2 = new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100);
        Coordinate p3 = new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // Recovering parameteres
        double a = eq.getCoeffa();
        double b = eq.getCoeffb();
        double c = eq.getCoeffc();
        double d = eq.getCoeffd();
        // Substitution
        double verification1 = a*p1.getX()+b*p1.getY()+c*p1.getZ()+d;
        double verification2 = a*p2.getX()+b*p2.getY()+c*p2.getZ()+d;
        double verification3 = a*p3.getX()+b*p3.getY()+c*p3.getZ()+d;
        // Comparison
        assertEquals("Randomly created plane equation is incorrect", 0, verification1, epsilon);
        assertEquals("Randomly created plane equation is incorrect", 0, verification2, epsilon);
        assertEquals("Randomly created plane equation is incorrect", 0, verification3, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute plane equation from 3 points : colinear case
    // --------------------------------------------------------------------------------
    public void testPlanEquation5() {
        log.info("Test for method to compute plane equation from 3 points : colinear case");
        // Creating points
        Coordinate p1 = new Coordinate(0, 0, 0);
        Coordinate p2 = new Coordinate(1, 1, 1);
        Coordinate p3 = new Coordinate(2, 2, 2);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1, p2, p3);
        // Recovering parameteres
        double a = eq.getCoeffa();
        double b = eq.getCoeffb();
        double c = eq.getCoeffc();
        double d = eq.getCoeffd();
        // Comparison
        assertEquals("a coefficient in plane equation is incorrect", 0, a, epsilon);
        assertEquals("b coefficient in plane equation is incorrect", 0, b, epsilon);
        assertEquals("c coefficient in plane equation is incorrect", 0, c, epsilon);
        assertEquals("d coefficient in plane equation is incorrect", 0, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute plane equation from 1 point and 1 normal vector
    // --------------------------------------------------------------------------------
    public void testPlanEquation6() {
        log.info("Test for method to compute plane equation from 1 point and 1 normal vector");
        // Creating random point
        Coordinate p = new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100);
        // Creating random normal vector
        Vecteur n = new Vecteur(new Coordinate(Math.random()*100, Math.random()*100, Math.random()*100));
        n.normalise();
        // Computing equation
        PlanEquation eq = new PlanEquation(n, p);
        // Recovering parameters
        double a = eq.getCoeffa();
        double b = eq.getCoeffb();
        double c = eq.getCoeffc();
        double d = eq.getCoeffd();
        // Expected parameters
        double aExp = n.getX();
        double bExp = n.getY();
        double cExp = n.getZ();
        double dExp = -(aExp*p.getX()+bExp*p.getY()+cExp*p.getZ());
        // Normalizations
        double somme1 = a+b+c+d;
        a = a/(somme1);
        b = b/(somme1);
        c = c/(somme1);
        d = d/(somme1);
        double somme2 = aExp+bExp+cExp+dExp;
        aExp = aExp/(somme2);
        bExp = bExp/(somme2);
        cExp = cExp/(somme2);
        dExp = dExp/(somme2);
        // Comparisons
        assertEquals("a coefficient in plane equation is incorrect", aExp, a, epsilon);
        assertEquals("b coefficient in plane equation is incorrect", bExp, b, epsilon);
        assertEquals("c coefficient in plane equation is incorrect", cExp, c, epsilon);
        assertEquals("d coefficient in plane equation is incorrect", dExp, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute plane equation from 1 point and 1 null vector
    // --------------------------------------------------------------------------------
    public void testPlanEquation7() {
        log.info("Test for method to compute plane equation from 1 point and 1 null vector");
        // Creating random point
        Coordinate p = new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100);
        // Creating random normal vector
        Vecteur n = new Vecteur(0,0,0);
        // Computing equation
        PlanEquation eq = new PlanEquation(n, p);
        // Recovering parameters
        double a = eq.getCoeffa();
        double b = eq.getCoeffb();
        double c = eq.getCoeffc();
        double d = eq.getCoeffd();
        // Comparison
        assertEquals("a coefficient in plane equation is incorrect", Double.NaN, a, epsilon);
        assertEquals("b coefficient in plane equation is incorrect", Double.NaN, b, epsilon);
        assertEquals("c coefficient in plane equation is incorrect", Double.NaN, c, epsilon);
        assertEquals("d coefficient in plane equation is incorrect", Double.NaN, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute plane equation from surface
    // --------------------------------------------------------------------------------
    public void testPlanEquation8() {
        log.info("Test for method to compute plane equation from surface");
        // Creating surface
        Polygon surface = Utils.createSquarePolygon(2, 0, 0);
        // Computing equation
        PlanEquation eq = new PlanEquation(surface);
        // recovering parameters
        double a = eq.getCoeffa();
        double b = eq.getCoeffb();
        double c = eq.getCoeffc();
        double d = eq.getCoeffd();
        // Normalizations
        double somme = a+b+c+d;
        a = a/somme;
        b = b/somme;
        c = c/somme;
        d = d/somme;
        // Comparison
        assertEquals("a coefficient in plane equation is incorrect", 0, a, epsilon);
        assertEquals("b coefficient in plane equation is incorrect", 0, b, epsilon);
        assertEquals("c coefficient in plane equation is incorrect", 1, c, epsilon);
        assertEquals("d coefficient in plane equation is incorrect", 0, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute plane equation from list of points
    // --------------------------------------------------------------------------------
    public void testPlanEquation9() {
        log.info("Test for method to compute plane equation from list of points");
        // Creating list of points
        List<Coordinate> dpl = new ArrayList<>();
        // Filling list with random coordinates
        for (int i=0; i<10; i++){
            dpl.add(new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100));
        }
        // Computing equation
        PlanEquation eq = new PlanEquation(dpl.toArray(new Coordinate[0]));
        // recovering parameters
        double a = eq.getCoeffa();
        double b = eq.getCoeffb();
        double c = eq.getCoeffc();
        double d = eq.getCoeffd();
        // Expected results
        PlanEquation eq2 = new PlanEquation(dpl.get(0), dpl.get(1), dpl.get(2));
        double aExp = eq2.getCoeffa();
        double bExp = eq2.getCoeffb();
        double cExp = eq2.getCoeffc();
        double dExp = eq2.getCoeffd();
        // Normalizations
        double somme1 = a+b+c+d;
        a = a/somme1;
        b = b/somme1;
        c = c/somme1;
        d = d/somme1;
        double somme2 = aExp+bExp+cExp+dExp;
        aExp = aExp/somme2;
        bExp = bExp/somme2;
        cExp = cExp/somme2;
        dExp = dExp/somme2;
        // Comparison
        assertEquals("a coefficient in plane equation is incorrect", aExp, a, epsilon);
        assertEquals("b coefficient in plane equation is incorrect", bExp, b, epsilon);
        assertEquals("c coefficient in plane equation is incorrect", cExp, c, epsilon);
        assertEquals("d coefficient in plane equation is incorrect", dExp, d, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute equation from a plane
    // --------------------------------------------------------------------------------
    public void testPlanEquation10() {
        log.info("Test for method to compute equation from a plane");
        // Creating random points
        Coordinate p1 = new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100);
        Coordinate p2 = new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100);
        Coordinate p3 = new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1, p2, p3);
        // Substitution
        double verification1 = eq.equationValue(p1);
        double verification2 = eq.equationValue(p2);
        double verification3 = eq.equationValue(p3);
        // Comparison
        assertEquals("Randomly created plane equation is incorrect", 0, verification1, epsilon);
        assertEquals("Randomly created plane equation is incorrect", 0, verification2, epsilon);
        assertEquals("Randomly created plane equation is incorrect", 0, verification3, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute sign from a plane
    // --------------------------------------------------------------------------------
    public void testPlanEquation11() {
        log.info("Test for method to compute sign from a plane");
        // Creating random points
        Coordinate p1 = new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100);
        Coordinate p2 = new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100);
        Coordinate p3 = new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1, p2, p3);
        // Recovering parameteres
        double a = eq.getCoeffa();
        double b = eq.getCoeffb();
        double c = eq.getCoeffc();
        double d = eq.getCoeffd();
        // New random point
        Coordinate p = new Coordinate(Math.random()*100,Math.random()*100,Math.random()*100);
        // Recovering result
        double s = eq.equationSignum(p);
        // Expected result
        double variable = a*p.getX()+b*p.getY()+c*p.getZ()+d;
        double sExp = (variable != 0) ? (variable)/Math.abs(variable) : 0;
        // Comparison
        assertEquals("Randomly created plane equation is incorrect", sExp, s, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute intersection on a plane (Oxy plane)
    // --------------------------------------------------------------------------------
    public void testPlanEquation12() {
        log.info("Test for method to compute intersection on a plane (Oxy plane)");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(1,0,0);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1, p2, p3);
        // Creating line
        Coordinate p4 = new Coordinate(0,2,2);
        Coordinate p5 = new Coordinate(0,-2,-2);
        // Processing intersection
        Coordinate p = eq.intersectionLinePlan(p4, p5);
        // Comparison
        assertEquals("Intersection coordinate X between line and plane is incorrect", 0, p.getX(), epsilon);
        assertEquals("Intersection coordinate Y between line and plane is incorrect", 0, p.getY(), epsilon);
        assertEquals("Intersection coordinate Z between line and plane is incorrect", 0, p.getZ(), epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute intersection on a plane : case "parallel"
    // --------------------------------------------------------------------------------
    public void testPlanEquation13() {
        log.info("Test for method to compute intersection on a plane : case 'parallel'");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(1,0,0);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1, p2, p3);
        // Creating line
        Coordinate p4 = new Coordinate(0,2,2);
        Coordinate p5 = new Coordinate(0,-2,2);
        // Processing intersection
        Coordinate p = eq.intersectionLinePlan(p4, p5);
        // Comparison
        assertNull("Intersections should be null", p);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute intersection between plane and half line
    // --------------------------------------------------------------------------------
    public void testPlanEquation14() {
        log.info("Test for method to compute intersection between plane and half line");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(1,0,0);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // Creating line
        Coordinate p4 = new Coordinate(0,0,4);
        Coordinate p5 = new Coordinate(0,0,2);
        // Processing intersections
        boolean bool1 =  eq.intersecteHalfLinePlan(p4, p5, 4.1);
        boolean bool2 =  eq.intersecteHalfLinePlan(p4, p5, 3.9);
        // Comparison
        assertTrue("Half line and plane should intersect", bool1);
        assertFalse("Half line and plane should not intersect", bool2);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute intersection between plane and half line
    // --------------------------------------------------------------------------------
    public void testPlanEquation15() {
        log.info("Test for method to compute intersection between plane and half line");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(1,0,0);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // Line origin
        Coordinate p4 = new Coordinate(0,0,4);
        // Line direction
        Vecteur direction = new Vecteur(0,0,-1);
        // Processing intersections
        boolean bool1 =  eq.intersectionLinePlan(p4, direction, 4.1);
        boolean bool2 =  eq.intersectionLinePlan(p4, direction, 3.9);
        // Comparison
        assertTrue("Half line and plane should intersect", bool1);
        assertFalse("Half line and plane should not intersect", bool2);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute intersection between plane and half line (parallel)
    // --------------------------------------------------------------------------------
    public void testPlanEquation16() {
        log.info("Test for method to compute intersection between plane and half line (parallel)");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(1,0,0);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // Creating lines
        Coordinate p4 = new Coordinate(0,2,2);
        Coordinate p5 = new Coordinate(0,4,2);
        Coordinate p6 = new Coordinate(0,2,0);
        Coordinate p7 = new Coordinate(0,4,0);
        // Processing intersections
        boolean bool1 =  eq.intersecteHalfLinePlan(p6, p7, 100);
        boolean bool2 =  eq.intersecteHalfLinePlan(p4, p5, 100);
        // Comparison
        assertTrue("Half line and plane should intersect", bool1);
        assertFalse("Half line and plane should not intersect", bool2);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute intersection point between plane and half line
    // --------------------------------------------------------------------------------
    public void testPlanEquation17() {
        log.info("Test for method to compute intersection point between plane and half line");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,2);
        Coordinate p2 = new Coordinate(1,0,2);
        Coordinate p3 = new Coordinate(0,1,2);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // Line origin
        Coordinate p4 = new Coordinate(3,3,4);
        // Line direction
        Vecteur direction = new Vecteur(0,0,-1);
        // Processing intersections
        Coordinate pi =  (Coordinate) eq.intersectionHalfLinePlan(p4, direction);
        // Computing expected result
        Coordinate piExp = new Coordinate(3,3,2);
        // Comparison
        assertEquals("Intersection point coordinates are incorrect", pi, piExp);
    }

    // --------------------------------------------------------------------------------
    // Test for method to compute intersection point between plane and line
    // --------------------------------------------------------------------------------
    public void testPlanEquation18() {
        log.info("Test for method to compute intersection point between plane and line");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,2);
        Coordinate p2 = new Coordinate(1,0,2);
        Coordinate p3 = new Coordinate(0,1,2);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // Line origin
        Coordinate p4 = new Coordinate(3,3,4);
        // Line direction
        Vecteur direction = new Vecteur(0,1,0);
        // Processing intersections
        Coordinate pi =  (Coordinate) eq.intersectionHalfLinePlan(p4, direction);
        // Comparison
        assertNull("There should be no intesection", pi);
    }

    // --------------------------------------------------------------------------------
    // Test for method to project a 2D point on a plane
    // --------------------------------------------------------------------------------
    public void testPlanEquation19() {
        log.info("Test for method to project a 2D point on a plane");
        // Creating points
        Coordinate p1 = new Coordinate(0,3,2);
        Coordinate p2 = new Coordinate(1,0,2);
        Coordinate p3 = new Coordinate(12,1,2);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // New point
        Coordinate p0 = new Coordinate(3,3);
        // Projection
        double alti = eq.getZ(p0);
        // Projected point
        Coordinate pp = new Coordinate(p0.getX(), p0.getY(), alti);
        // Test
        double valTest = eq.equationValue(pp);
        // Comparsion
        assertEquals("Projected point should be located on plane", 0, valTest, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process intersection between plane and triangle
    // --------------------------------------------------------------------------------
    public void testPlanEquation20() {
        log.info("Test for method to process intersection between plane and triangle");
        //Coordinate.PRECISION = 15;
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(1,0,0);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // Creating triangle
        Coordinate pt1 = new Coordinate(0,0,10);
        Coordinate pt2 = new Coordinate(-5,-5,-10);
        Coordinate pt3 = new Coordinate(5,5,-10);
        Triangle triangle = new Triangle(new Vertex(pt1), new Vertex(pt2), new Vertex(pt3));
        // Processing intersection
        LineString line = (LineString) eq.triangleIntersection(triangle);
        // Expected result
        Coordinate dpIni = new Coordinate(-2.5,-2.5,0);
        Coordinate dpEnd = new Coordinate(2.5,2.5,0);
        // Comparison
        assertTrue("Triangle intersection with plane is incorrect", line.getCoordinateN(0).distance(dpIni) < epsilon);
        assertTrue("Triangle intersection with plane is incorrect", line.getCoordinateN(1).distance(dpEnd) < epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process intersection between plane and triangle
    // --------------------------------------------------------------------------------
    public void testPlanEquation21() {
        log.info("Test for method to process intersection between plane and triangle");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(1,0,0);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // Creating triangle
        Coordinate pt1 = new Coordinate(0,0,15);
        Coordinate pt2 = new Coordinate(-5,-5,10);
        Coordinate pt3 = new Coordinate(5,5,10);
        Triangle triangle = new Triangle(new Vertex(pt1), new Vertex(pt2), new Vertex(pt3));
        // Processing intersection
        LineString line = (LineString) eq.triangleIntersection(triangle);
        // Comparison
        assertNull("There should be no intersection", line);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process intersection between plane and triangle
    // --------------------------------------------------------------------------------
    public void testPlanEquation22() {
        log.info("Test for method to process intersection between plane and triangle");
        // NEEDS FIXING METHOD triangleIntersection
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(1,0,0);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // Creating triangle vertices
        Coordinate pt1 = new Coordinate(0,0,0);
        Coordinate pt2 = new Coordinate(1,1,0);
        Coordinate pt3 = new Coordinate(0,1,0);
        // Creating trinagle
        Triangle triangle = new Triangle(new Vertex(pt1), new Vertex(pt2), new Vertex(pt3));
        @SuppressWarnings("unused")
        LineString line = (LineString) eq.triangleIntersection(triangle);
    }

    // --------------------------------------------------------------------------------
    // Test for method to process equation value : 
    // BE CAREFUL, REDUDANCY with equation Value Method !!!
    // --------------------------------------------------------------------------------
    public void testPlanEquation23() {
        log.info("Test for method to process equation value : ");
        log.warning("BE CAREFUL, REDUDANCY with equation Value Method !!!");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(0,0,1);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation (x = 0)
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // Creating random point
        double X = Math.random()*100;
        double Y = Math.random()*100;
        double Z = Math.random()*100;
        Coordinate p = new Coordinate(X,Y,Z);
        // Checking if point is satisfying equation
        double val = eq.positionPointInPlan(p);
        // Processing expected value
        double valExp = eq.getCoeffa()*X+eq.getCoeffb()*Y+eq.getCoeffc()*Z+eq.getCoeffd();
        // Comparison
        assertEquals("Equation value on point is incorrect", valExp, val, epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to return plane normal vector
    // --------------------------------------------------------------------------------
    public void testPlanEquation24() {
        log.info("Test for method to return plane normal vector");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(0,0,1);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation (x = 0)
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // Recovering normal vector
        Vecteur n = eq.getNormale();
        // Expected normal
        Vecteur nExp = new Vecteur(1,0,0);
        // Comparison
        assertEquals("Normal vector X coordinate is incorrect", Math.abs(n.getX()), Math.abs(nExp.getX()), epsilon);
        assertEquals("Normal vector Y coordinate is incorrect", Math.abs(n.getY()), Math.abs(nExp.getY()), epsilon);
        assertEquals("Normal vector Z coordinate is incorrect", Math.abs(n.getZ()), Math.abs(nExp.getZ()), epsilon);
    }

    // --------------------------------------------------------------------------------
    // Test for method to return orthogonal projection
    // --------------------------------------------------------------------------------
    public void testPlanEquation25() {
        log.info("Test for method to return orthogonal projection");
        // Creating points
        Coordinate p1 = new Coordinate(0,0,0);
        Coordinate p2 = new Coordinate(0,0,1);
        Coordinate p3 = new Coordinate(0,1,0);
        // Computing equation (x = 0)
        PlanEquation eq = new PlanEquation(p1,p2,p3);
        // Creating point
        Coordinate p = new Coordinate(10,10,10);
        // Processing projection
        Coordinate pp = eq.normalCasting(p);
        // Expected result
        Coordinate ppExp = new Coordinate(0,10,10);
        // Comparison
        assertEquals("Orthogonal projection coordinates are incorrect", pp, ppExp);
    }
}
