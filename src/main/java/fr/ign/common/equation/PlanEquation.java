package fr.ign.common.equation;

import fr.ign.common.util.GeomUtils;
import fr.ign.common.util.Vecteur;
import fr.ign.common.topology.Triangle;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

public class PlanEquation {
    /*
     * Les coefficients, l'équation et la normale au plan
     */
    protected double coeffa;
    public double getCoeffa() {
        return coeffa;
    }
    protected double coeffb;
    public double getCoeffb() {
        return coeffb;
    }
    protected double coeffc;
    public double getCoeffc() {
        return coeffc;
    }
    protected double coeffd;
    public double getCoeffd() {
        return this.coeffd;
    }

    protected String equation;
    protected Vecteur normaleToPlane;

    public static double EPSILON = 0.001;
    public PlanEquation() {
    }

    public PlanEquation(Vecteur v1, Coordinate dp) {
        if(v1.norme() == 0){
            this.coeffa = Double.NaN;
            this.coeffb = Double.NaN;
            this.coeffc = Double.NaN;
            this.coeffd = Double.NaN;
        }else{
            v1.normalise();
            double a = v1.getX();
            double b = v1.getY();
            double c = v1.getZ();
            // Calcul de d
            double d = -a * dp.getX() - b * dp.getY() - c * dp.getZ();
            this.coeffa = a;
            this.coeffb = b;
            this.coeffc = c;
            this.coeffd = d;
            this.normaleToPlane = new Vecteur(a, b, c);
            this.normaleToPlane.normalise();
        }
        // Affichage de l'équation implicite de l'équation
        this.equation = coeffa + "x +" + coeffb + "y +" + coeffc + "z +" + coeffd + " = 0";
    }

    public PlanEquation(double a, double b, double c, double d) {
        if(a ==0 && b ==0 && c ==0){
            this.coeffa = Double.NaN;
            this.coeffb = Double.NaN;
            this.coeffc = Double.NaN;
            this.coeffd = Double.NaN;
        }else{
            this.coeffa = a;
            this.coeffb = b;
            this.coeffc = c;
            this.coeffd = d;
            this.normaleToPlane = new Vecteur(a, b, c);
            this.normaleToPlane.normalise();
        }
        // Affichage de l'équation implicite de l'équation
        this.equation = coeffa + "x +" + coeffb + "y +" + coeffc + "z +" + coeffd + " = 0";
    }
    public PlanEquation(Polygon polygon) {
        this(polygon.getCoordinates());
    }

    public PlanEquation(Coordinate dpCenter, Coordinate dp1, Coordinate dp2) {
        // Création des vecteurs plans
        Vecteur v1 = new Vecteur(dpCenter, dp1);
        Vecteur v2 = new Vecteur(dpCenter, dp2);
        v1.normalise();
        v2.normalise();
        // On utilise la forme paramètrique : vect AM = k.vectU + t.vectV (k,t)
        // appart. à R2
        Vecteur normale = v1.prodVectoriel(v2);
        double a = normale.getX();
        double b = normale.getY();
        double c = normale.getZ();
        if (a == 0 && b == 0 && c == 0) {
            return;
            //PlanEquation.logger.warn(Messages.getString("Plans.PointsAlignes"));
        }
        // Calcul de d
        double d = -a * dpCenter.getX() - b * dpCenter.getY() - c * dpCenter.getZ();
        this.coeffa = a;
        this.coeffb = b;
        this.coeffc = c;
        this.coeffd = d;
        this.normaleToPlane = new Vecteur(a, b, c);
        this.normaleToPlane.normalise();
        // Affichage de l'équation implicite de l'équation
        this.equation = a + "x +" + b + "y +" + c + "z +" + d + " = 0";
    }

    public PlanEquation(Coordinate[] coordinates) {
        // - 1 : récupèration de trois points pour définir son équation
        Coordinate pt1 = coordinates[0];
        Coordinate pt2 = coordinates[1];
        Coordinate pt3 = coordinates[2];

        // - 2 : Création de deux vecteurs du plan
        Vecteur v1 = new Vecteur(pt1, pt2);
        Vecteur v2 = new Vecteur(pt1, pt3);
        v1.normalise();
        v2.normalise();

        // - 3 : Vérification qu'ils ne sont pas alignés
        if (v1.prodScalaire(v2) == 0) {
            // Création du vecteur 1
            Coordinate dp1 = new Coordinate();
            boolean bdp1 = true;
            if (pt1.equals(pt2)) {
                for (Coordinate coordinate : coordinates) {
                    dp1 = coordinate;
                    if (bdp1) {
                        if (!dp1.equals(pt1) || !dp1.equals(pt2) || !dp1.equals(pt3)) {
                            v1 = new Vecteur(pt1, dp1);
                            bdp1 = false;
                        }
                    }
                }// boucle i
            }
            // Création du vecteur 2
            if (pt1.equals(pt3)) {
                for (Coordinate dp2 : coordinates) {
                    if (!dp2.equals(pt1) || !dp2.equals(pt2) || !dp2.equals(pt3) || !dp2.equals(dp1)) {
                        v2 = new Vecteur(pt1, dp2);
                    }
                }// boucle i
            }
        }
        // On utilise la forme paramètrique : vect AM = k.vectU + t.vectV (k,t)
        // appart. à R2
        Vecteur normale = v1.prodVectoriel(v2);
        double a = normale.getX();
        double b = normale.getY();
        double c = normale.getZ();
        // Calcul de d
        double d = -a * pt1.getX() - b * pt1.getY() - c * pt1.getZ();
        this.coeffa = a;
        this.coeffb = b;
        this.coeffc = c;
        this.coeffd = d;
        // Affichage de l'équation implicite de l'équation
        this.equation = a + "x +" + b + "y +" + c + "z +" + d + " = 0";
        this.normaleToPlane = new Vecteur(a, b, c);
        this.normaleToPlane.normalise();
    }

    public Vecteur getNormale() {
        return this.normaleToPlane;
    }

    public Coordinate intersectionLinePlan(Coordinate dp1, Coordinate dp2) {
        // - 1 : Création d'un vecteur directeur de la droite
        Vecteur vectDirecteur = new Vecteur(dp1, dp2);
        vectDirecteur = vectDirecteur.vectNorme();
        double coeffa = this.coeffa;
        double coeffb = this.coeffb;
        double coeffc = this.coeffc;
        double coeffd = this.coeffd;
        if (Math.abs(vectDirecteur.getX()) < PlanEquation.EPSILON) {
            vectDirecteur.setX(0.0);
        }
        if (Math.abs(vectDirecteur.getY()) < PlanEquation.EPSILON) {
            vectDirecteur.setY(0.0);
        }
        if (Math.abs(vectDirecteur.getZ()) < PlanEquation.EPSILON) {
            vectDirecteur.setZ(0.0);
        }
        if (Math.abs(coeffa) < PlanEquation.EPSILON) {
            coeffa = 0;
        }
        if (Math.abs(coeffb) < PlanEquation.EPSILON) {
            coeffb = 0;
        }
        if (Math.abs(coeffc) < PlanEquation.EPSILON) {
            coeffc = 0;
        }
        if (Math.abs(coeffd) < PlanEquation.EPSILON) {
            coeffd = 0;
        }
        // - 2 : Calcul du paramètre k : Coefficient de la forme paramètrique de
        // l'équation de la droite
        double k = -(coeffa * dp1.getX() + coeffb * dp1.getY() + coeffc
                * dp1.getZ() + coeffd)
                / (coeffa * vectDirecteur.getX() + coeffb * vectDirecteur.getY() + coeffc
                * vectDirecteur.getZ());
        if (k < 0) {
            return null;
        }
        // - 3 : Calcul du point d'intersection
        Coordinate dpIntersectionDroitePlan = dp1.copy();
        dpIntersectionDroitePlan = GeomUtils.move(dpIntersectionDroitePlan, vectDirecteur.multConstante(k).getCoordinate());
        vectDirecteur.normalise();
        this.getNormale().normalise();
        return dpIntersectionDroitePlan;
    }

    public boolean intersectionLinePlan(Coordinate dp1, Vecteur v1, double dist) {
        Coordinate dp2 = dp1.copy();
        dp2 = GeomUtils.move(dp2, v1.getCoordinate());
        Coordinate inter = this.intersectionLinePlan(dp1, dp2);
        return (!(dp1.distance3D(inter) > dist)) && !Double.isNaN(inter.getX()) && !Double.isInfinite(inter.getX());
    }

    public Coordinate intersectionLinePlan(Coordinate dp1, Vecteur v1) {
        Coordinate dp2 = dp1.copy();
        dp2 = GeomUtils.move(dp2, v1.getCoordinate());
        return this.intersectionLinePlan(dp1, dp2);
    }

    public double equationValue(Coordinate dp) {
        return (this.coeffa * dp.getX() + this.coeffb * dp.getY() + this.coeffc
                * dp.getZ() + this.coeffd);
    }

    public double getZ(Coordinate dp) {
        return -(this.coeffa * dp.getX() + this.coeffb * dp.getY() + this.coeffd) / this.coeffc;
    }

    public Geometry triangleIntersection(Triangle tri) {
        Coordinate dp1 = tri.getCoordinate(0);
        Coordinate dp2 = tri.getCoordinate(1);
        Coordinate dp3 = tri.getCoordinate(2);
        LineEquation l1 = new LineEquation(dp1, dp2);
        LineEquation l2 = new LineEquation(dp2, dp3);
        LineEquation l3 = new LineEquation(dp3, dp1);
        Coordinate dpInter1 = l1.intersectionLinePlan(this);
        Coordinate dpInter2 = l2.intersectionLinePlan(this);
        Coordinate dpInter3 = l3.intersectionLinePlan(this);
        if (dpInter1 != null) {
            Vecteur vect = new Vecteur(dp1, dp2);
            Vecteur vectTemp = new Vecteur(dp1, dpInter1);
            double scal = vect.prodScalaire(vectTemp);
            if (scal < -EPSILON || vect.norme() < vectTemp.norme() + EPSILON) {
                // il n'est pas sur le triangle
                dpInter1 = null;
            }
        }
        if (dpInter2 != null) {
            Vecteur vect = new Vecteur(dp2, dp3);
            Vecteur vectTemp = new Vecteur(dp2, dpInter2);
            double scal = vect.prodScalaire(vectTemp);
            if (scal < -EPSILON || vect.norme() < vectTemp.norme() + EPSILON) {
                // il n'est pas sur le triangle
                dpInter2 = null;
            }
        }
        if (dpInter3 != null) {
            Vecteur vect = new Vecteur(dp3, dp1);
            Vecteur vectTemp = new Vecteur(dp3, dpInter3);
            double scal = vect.prodScalaire(vectTemp);
            if (scal < -EPSILON|| vect.norme() < vectTemp.norme() + EPSILON) {
                // il n'est pas sur le triangle
                dpInter3 = null;
            }
        }
        List<Coordinate> dpl = new ArrayList<>();
        if (dpInter1 != null) {
            dpl.add(dpInter1);
        }
        if (dpInter2 != null) {
            dpl.add(dpInter2);
        }
        if (dpInter3 != null) {
            dpl.add(dpInter3);
        }
        if (dpl.isEmpty()) {
            return null;
        }
        if (dpl.size() == 1) {
            return tri.getPolygon().getFactory().createPoint(dpl.get(0));
        }
        if (dpl.size() == 2) {
            return tri.getPolygon().getFactory().createLineString(dpl.toArray(new Coordinate[0]));
        }
        return tri.getPolygon();
    }

    public double equationSignum(Coordinate dp) {
        return Math.signum(this.coeffa * dp.getX() + this.coeffb * dp.getY() + this.coeffc * dp.getZ() + this.coeffd);
    }

    public boolean intersecteHalfLinePlan(Coordinate dp1, Coordinate dp2, double dist) {
        Coordinate inter = this.intersectionLinePlan(dp1, dp2);
        System.out.println("Intersection point: " + inter);
        Vecteur vectDirecteur = new Vecteur(dp1, dp2);
        // -----------------------------------------------------------------------------
        // Testing parallel case
        Vecteur n = new Vecteur(coeffa,coeffb,coeffc);
        Vecteur v = new Vecteur(dp1,dp2);
        System.out.println("prodScalaire: " + n.prodScalaire(v));
        if (n.prodScalaire(v) == 0){
            // Testing if dp1 belongs to the plan
            return equationValue(dp1) == 0;
        }
        System.out.println("prodScalaire2: " + vectDirecteur.prodScalaire(new Vecteur(dp1, inter)));
        System.out.println("distance: " + dp1.distance3D(inter));
        // -----------------------------------------------------------------------------
        return !(vectDirecteur.prodScalaire(new Vecteur(dp1, inter)) < 0)
                && (!(dp1.distance3D(inter) > dist)) && !Double.isNaN(inter.getX())
                && !Double.isInfinite(inter.getX());
    }

    public Object intersectionHalfLinePlan(Coordinate dp1, Vecteur v1) {
        // -----------------------------------------------------------------------------
        // Testing parallel case
        Vecteur n = new Vecteur(coeffa,coeffb,coeffc);
        if (n.prodScalaire(v1) == 0){
            // Testing if dp1 belongs to the plan
            if (equationValue(dp1) == 0){
                // Number of intersections = infinity
                return dp1;
            }
            else{
                // 0 intersection
                return null;
            }
        }
        // -----------------------------------------------------------------------------
        Coordinate dp2 = dp1.copy();
        dp2 = GeomUtils.move(dp2, v1.getCoordinate());
        Coordinate inter = this.intersectionLinePlan(dp1, dp2);
        Vecteur vectDirecteur = new Vecteur(dp1, dp2);
        if (vectDirecteur.prodScalaire(new Vecteur(dp1, inter)) > 0) {
            return inter;
        }
        return null;
    }

    public double positionPointInPlan(Coordinate dp) {
        return (this.coeffa * dp.getX()) + (this.coeffb * dp.getY()) + (this.coeffc * dp.getZ()) + this.coeffd;
    }

    public Coordinate normalCasting(Coordinate dp) {
        return this.intersectionLinePlan(dp, this.getNormale());
    }
}
