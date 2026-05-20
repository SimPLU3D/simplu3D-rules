package fr.ign.common.analysis.roof;

import fr.ign.cogit.simplu3d.model.UrbanFeature;
import fr.ign.common.equation.ApproximatedPlanEquation;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.List;

public class RoofDetection {
    static GeometryFactory fact = new GeometryFactory();
    public static MultiPolygon detectRoof(UrbanFeature building, double threshold) {
        List<Polygon> polygons = FromGeomToSurface.convertGeom(building.getGeometry());
        if (polygons.isEmpty()) {
            return null;
        }
        List<Polygon> lOSOut = detectRoof(polygons, threshold);
        if (lOSOut == null) {
            return fact.createMultiPolygon();
        }
        return fact.createMultiPolygon(lOSOut.toArray(new Polygon[0]));
    }
    /**
     * Permet de détecter le toit, il s'agit des faces dont le produit scalaire
     * par rapport à l'axe z est supérieur à tolérance
     * @param lSurf list of polygons
     * @param tolerence a tolerance
     * @return a list of roof polygons
     */
    public static List<Polygon> detectRoof(List<Polygon> lSurf, double tolerence) {
        List<Polygon> lFacesNVerticales = new ArrayList<>();
        for (Polygon surfTemp : lSurf) {
            Coordinate[] dplTemp = surfTemp.getCoordinates();
            ApproximatedPlanEquation eqP = new ApproximatedPlanEquation(dplTemp);
            Vecteur v = eqP.getNormale();
            v.normalise();
            if (Math.abs(v.getZ()) > tolerence) {
                lFacesNVerticales.add(surfTemp);
            }
        }
        int nFacesNVerticales = lFacesNVerticales.size();
        if (nFacesNVerticales == 0) {
            return null;
        }
        return lFacesNVerticales;
    }
    public static List<Polygon> detectRoof(Geometry geometry, double threshold) {
        List<Polygon> lOS = FromGeomToSurface.convertGeom(geometry);
        if (lOS.isEmpty()) {
            return null;
        }
        return detectRoof(lOS, threshold);
    }
}
