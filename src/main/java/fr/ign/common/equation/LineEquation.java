package fr.ign.common.equation;

import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.Coordinate;

public class LineEquation {

    /**
     * Equation de la forme : x = a1 * t + a0 y = b1 * t + b0 z = c1 * t + c0
     */
    private final double a0;
    private final double a1;
    private final double b0;
    private final double b1;
    private final double c0;
    private final double c1;
    public static double EPSILON = 0.1;
    public LineEquation(double a0, double a1, double b0, double b1, double c0, double c1) {
        super();
        this.a0 = a0;
        this.a1 = a1;
        this.b0 = b0;
        this.b1 = b1;
        if (Double.isNaN(c0)) {
            // 2D vector
            this.c0 = 0;
            this.c1 = 0;
        } else {
            this.c0 = c0;
            this.c1 = c1;
        }
    }
    public LineEquation(Coordinate dp, Vecteur v) {
        this(dp.getX(), v.getX(), dp.getY(), v.getY(), dp.getZ(), v.getZ());
    }
    public LineEquation(Coordinate dp1, Coordinate dp2) {
        this(dp1, new Vecteur(dp1, dp2));
    }
    public Coordinate valueAt(double t) {
        return new Coordinate(this.a1 * t + this.a0, this.b1 * t + this.b0, this.c1 * t + this.c0);
    }

    public Vecteur getVecteur() {
        return new Vecteur(a1, b1, c1);
    }

    public double distance(Coordinate dp) {
        Coordinate dpLine = valueAt(0);
        Vecteur v = new Vecteur(dp, dpLine);
        return v.prodVectoriel(this.getVecteur()).norme() / this.getVecteur().norme();
    }

    public boolean isPointOnLine(Coordinate dp) {
        Vecteur v1 = new Vecteur(dp, new Coordinate(this.a0, this.b0, this.c0));
        Vecteur v2 = new Vecteur(this.a1, this.b1, this.c1);
        return (v1.prodVectoriel(v2).norme() < LineEquation.EPSILON);
    }

    public Coordinate intersectionLinePlan(PlanEquation eq) {
        Coordinate dp = new Coordinate(this.a0, this.b0, this.c0);
        Coordinate dp2 = this.valueAt(10);
        // Normale au plan orthogonale au plan (pas d'intersection ou tout est
        // intersection)
        if (Math.abs(eq.getNormale().prodScalaire(new Vecteur(dp, dp2))) < LineEquation.EPSILON) {
            return null;
        }
        Coordinate p = eq.intersectionLinePlan(dp, dp2);
        // Modification
        // -----------------------------------------------------------------------------------
        if (p != null){return p;}
        return eq.intersectionLinePlan(dp, this.valueAt(-10));
    }
}
