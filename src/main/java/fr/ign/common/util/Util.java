package fr.ign.common.util;

import fr.ign.common.equation.ApproximatedPlanEquation;
import fr.ign.common.equation.PlanEquation;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Util {
    /**
     * Détecte une liste de faces verticales à partir d'une liste de faces
     *
     * @param lSurf     liste des surfaces que l'on traite
     * @param tolerance tolérence en radian pour considérer si une face est
     *                  verticale ou non
     * @return une liste de faces verticales
     */
    public static MultiPolygon detectVertical(List<Polygon> lSurf, double tolerance) {
        List<Polygon> result = new ArrayList<>();
        for (Polygon surfTemp : lSurf) {
            PlanEquation eqP = new PlanEquation(surfTemp);
            double scalarProduct = eqP.getNormale().prodScalaire(MathConstant.vectZ);
            if (Math.abs(scalarProduct) < Math.sin(tolerance) * eqP.getNormale().norme()) {
                result.add(surfTemp);
            }
        }
        return new GeometryFactory().createMultiPolygon(result.toArray(new Polygon[0]));
    }

    /**
     * Détecte une liste de faces non verticales à partir d'une liste de faces
     *
     * @param lSurf     liste des surfaces que l'on traite
     * @param tolerance tolérence en radian pour considérer si une face est
     *                  verticale ou non
     * @return une liste de faces non verticales
     */
    public static MultiPolygon detectNonVertical(List<Polygon> lSurf, double tolerance) {
        List<Polygon> result = new ArrayList<>();
        for (Polygon surfTemp : lSurf) {
            PlanEquation eqP = new PlanEquation(surfTemp);
            double scalarProduct = eqP.getNormale().prodScalaire(MathConstant.vectZ);
            if (!(Math.abs(scalarProduct) < Math.sin(tolerance) * eqP.getNormale().norme())) {
                result.add(surfTemp);
            }
        }
        return new GeometryFactory().createMultiPolygon(result.toArray(new Polygon[0]));
    }

    public static MultiPolygon detectRoof(ArrayList<Polygon> lSurf, double tolerance) {
        List<Polygon> lFacesNVerticales = new ArrayList<>();
        for (Polygon surfTemp : lSurf) {
            Coordinate[] dplTemp = surfTemp.getCoordinates();
            ApproximatedPlanEquation eqP = new ApproximatedPlanEquation(dplTemp);
            Vecteur v = eqP.getNormale();
            v.normalise();
            if (Math.abs(v.getZ()) > tolerance) {
                lFacesNVerticales.add(surfTemp);
            }
        }
        int nFacesNVerticales = lFacesNVerticales.size();
        if (nFacesNVerticales == 0) {
            return null;
        }
        return new GeometryFactory().createMultiPolygon(lFacesNVerticales.toArray(new Polygon[0]));
    }

    public static MultiPolygon detectRoofTriangles(ArrayList<Polygon> lSurf, double tolerance) {
        List<Polygon> lFacesNVerticales = new ArrayList<>();
        for (Polygon surfTemp : lSurf) {
            Coordinate[] dplTemp = surfTemp.getCoordinates();
            ApproximatedPlanEquation eqP = new ApproximatedPlanEquation(dplTemp);
            Vecteur v = eqP.getNormale();
            v.normalise();
            if (Math.abs(v.getZ()) > tolerance) {
                lFacesNVerticales.add(surfTemp.getFactory().createPolygon(surfTemp.getExteriorRing()));
            }
        }
        int nFacesNVerticales = lFacesNVerticales.size();
        if (nFacesNVerticales == 0) {
            return null;
        }
        return new GeometryFactory().createMultiPolygon(lFacesNVerticales.toArray(new Polygon[0]));
    }

    public static MultiPolygon detectFloorTriangles(ArrayList<Polygon> lSurf, double tolerance) {
        List<Polygon> lFacesNVerticales = new ArrayList<>();
        for (Polygon surfTemp : lSurf) {
            Coordinate[] dplTemp = surfTemp.getCoordinates();
            ApproximatedPlanEquation eqP = new ApproximatedPlanEquation(dplTemp);
            Vecteur v = eqP.getNormale();
            v.normalise();
            if (v.getZ() < -tolerance) {
                lFacesNVerticales.add(surfTemp.getFactory().createPolygon(surfTemp.getExteriorRing()));
            }
        }
        int nFacesNVerticales = lFacesNVerticales.size();
        if (nFacesNVerticales == 0) {
            return null;
        }
        return new GeometryFactory().createMultiPolygon(lFacesNVerticales.toArray(new Polygon[0]));
    }
}
