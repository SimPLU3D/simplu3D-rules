package fr.ign.common.extrusion;

import fr.ign.common.equation.ApproximatedPlanEquation;
import fr.ign.common.util.GeomUtils;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Extrusion3DObject {
    private final static Logger logger = Logger.getLogger(Extrusion3DObject.class.getName());
    public static Geometry conversionFromGeom(Geometry geom, double heigth) {
        if (heigth == 0) {
            return geom;
        }
        if (geom == null) {
            Extrusion3DObject.logger.warning("3DGIS.GeomEmpty");
            return null;
        }
        Geometry geomFinale;
        // On essaie de retrouver la classe de la geometrie
        // Ordre de test simple puis complexe de la dim la plus elevee a la
        // plus faible
        /*
         * Extrusion3DObject.logger.debug(Messages
         * .getString("FenetreShapeFile3D.FeatureConversion") + " : " +
         * geom.toString());
         */
        if (geom instanceof Polygon p) {
            geomFinale = Extrusion3DObject.convertitFromPolygon(p, heigth);
        } else if (geom instanceof MultiPolygon mp) {
            if (mp.getNumGeometries() == 1) {
                geomFinale = Extrusion3DObject.convertitFromPolygon((Polygon) mp.getGeometryN(0), heigth);
            } else {
                geomFinale = Extrusion3DObject.convertitFromMultiPolygon(mp, heigth);
            }
        } else if (geom instanceof LineString l) {
            geomFinale = Extrusion3DObject.convertitFromLine(l, heigth);
        } else if (geom instanceof MultiLineString multiLineString) {
            geomFinale = Extrusion3DObject.convertitFromMultiLineString(multiLineString, heigth);
        } else if (geom instanceof Point p) {
            geomFinale = Extrusion3DObject.convertitFromPoint(p, heigth);
        } else if (geom instanceof MultiPoint mp) {
            geomFinale = Extrusion3DObject.convertitFromMultiPoint(mp, heigth);
        } else if (geom instanceof GeometryCollection gc) {
            List<Geometry> geometries = new ArrayList<>();
            for (int index = 0; index < gc.getNumGeometries(); index++) {
                geometries.add(conversionFromGeom(gc.getGeometryN(index), heigth));
            }
            return geom.getFactory().createGeometryCollection(geometries.toArray(new Geometry[0]));
        } else {
            return null;
        }
        return geomFinale;
    }

    private static Geometry convertitFromMultiPoint(MultiPoint multiP, double heigth) {
        int nbPoints = multiP.getNumGeometries();
        if (nbPoints == 0) {
            return null;
        }
        if (heigth == 0 || Double.isNaN(heigth)) {
            return multiP;
        } else {
            List<LineString> multiC = new ArrayList<>();
            for (int i = 0; i < nbPoints; i++) {
                multiC.add((LineString) Extrusion3DObject.convertitFromPoint((Point) multiP.getGeometryN(i), heigth));
            }
            return multiP.getFactory().createMultiLineString(multiC.toArray(new LineString[0]));
        }
    }

    private static Geometry convertitFromPoint(Point point, double heigth) {
        if (heigth == 0 || Double.isNaN(heigth)) {
            return point;
        }
        Coordinate pIni = point.getCoordinate();
        double z = pIni.getZ() + heigth;
        Coordinate pFin = new Coordinate(pIni.getX(), pIni.getY(), z);
        return point.getFactory().createLineString(new Coordinate[]{pIni, pFin});
    }

    private static Geometry convertitFromMultiLineString(MultiLineString multiLS, double heigth) {
        int nbLine = multiLS.getNumGeometries();
        if (nbLine == 0) {
            Extrusion3DObject.logger.fine("3DGIS.GeomEmpty");
            return null;
        }
        if (heigth == 0 || Double.isNaN(heigth)) {
            return multiLS;
        } else {
            List<Polygon> mSurface = new ArrayList<>();
            // On decompose le multiPolygon en liste de polygones
            for (int indpoly = 0; indpoly < nbLine; indpoly++) {
                // On applique la transformation a chaque polygone
                LineString p = (LineString) multiLS.getGeometryN(indpoly);
                mSurface.addAll(GeomUtils.getPolygons((MultiPolygon) convertitFromLine(p, heigth)));
            }
            return multiLS.getFactory().createMultiPolygon(mSurface.toArray(new Polygon[0]));
        }
    }

    private static Geometry convertitFromLine(LineString ls, double height) {
        if (height == 0 || Double.isNaN(height)) {
            return ls;
        }
        List<Polygon> lOS = new ArrayList<>();
        List<Coordinate> lPoints = Arrays.asList(ls.getCoordinates());
        int nbPoints = lPoints.size();
        Coordinate pIni = lPoints.get(nbPoints - 1);
        for (Coordinate pSuiv : lPoints) {
            List<Coordinate> fTemp = new ArrayList<>();
            Coordinate pSuivZmin = new Coordinate(pSuiv.getX(), pSuiv.getY(), pSuiv.getZ() + height);
            Coordinate pIniZmin = new Coordinate(pIni.getX(), pIni.getY(), pIni.getZ() + height);
            fTemp.add(pSuiv);
            fTemp.add(pSuivZmin);
            fTemp.add(pIniZmin);
            fTemp.add(pIni);
            pIni = pSuiv;
            Polygon oS = ls.getFactory().createPolygon(fTemp.toArray(new Coordinate[0]));
            lOS.add(oS);
        }
        return ls.getFactory().createMultiPolygon(lOS.toArray(new Polygon[0]));
    }

    private static Geometry convertitFromMultiPolygon(MultiPolygon mp, double height) {
        int nbPolygon = mp.getNumGeometries();
        if (nbPolygon == 0) {
            return null;
        }
        if (height == 0 || Double.isNaN(height)) {
            return mp;
        } else {
            List<Geometry> mSolid = new ArrayList<>();
            // On decompose le multiPolygon en liste de polygones
            for (int indpoly = 0; indpoly < nbPolygon; indpoly++) {
                // On applique la transformation a chaque polygone
                Polygon p = (Polygon) mp.getGeometryN(indpoly);
                mSolid.add(Extrusion3DObject.convertitFromPolygon(p, height));
            }
            // Solide
            return mp.getFactory().createGeometryCollection(mSolid.toArray(new Geometry[0]));
        }
    }

    private static Geometry convertitFromPolygon(Polygon polyIni, double height) {
        ApproximatedPlanEquation eq = new ApproximatedPlanEquation(polyIni.getCoordinates());
        if (eq.getNormale().getZ() < 0) {
            polyIni = polyIni.reverse();
        }
        if (height == 0 || Double.isNaN(height)) {
            return polyIni;
        }
        if (height < 0) {
            Extrusion3DObject.logger.finer("FenetreShapeFile3D.NegativeExtrusion");

        } else if (height > 0) {
            Extrusion3DObject.logger.finer("FenetreShapeFile3D.PositiveExtrusion");
        }
        List<Polygon> lOS = new ArrayList<>();
        int nbContrib = 1 + polyIni.getNumInteriorRing();
        LinearRing exterior = null;
        List<LinearRing> interior = new ArrayList<>();
        for (int idContrib = 0; idContrib < nbContrib; idContrib++) {
            List<Coordinate> lPExt = new ArrayList<>();
            List<Coordinate> lPoints;
            if (idContrib == 0) {
                lPoints = new ArrayList<>(Arrays.stream(polyIni.getExteriorRing().getCoordinates()).toList());
            } else {
                lPoints = new ArrayList<>(Arrays.stream(polyIni.getInteriorRingN(idContrib - 1).getCoordinates()).toList());
            }
            int nbPoints = lPoints.size();
            if (!lPoints.get(0).equals(lPoints.get(nbPoints - 1))) {
                lPoints.add(lPoints.get(0));
            }
            Coordinate pIni = lPoints.get(0);
            for (int j = 1; j < nbPoints; j++) {
                List<Coordinate> fTemp = new ArrayList<>();
                Coordinate pSuiv = lPoints.get(j);
                Coordinate pSuivZmin = new Coordinate(pSuiv.getX(), pSuiv.getY(), pSuiv.getZ() + height);
                Coordinate pIniZmin = new Coordinate(pIni.getX(), pIni.getY(), pIni.getZ() + height);
                lPExt.add(pIniZmin);
                fTemp.add(pIni);
                fTemp.add(pSuiv);
                fTemp.add(pSuivZmin);
                fTemp.add(pIniZmin);
                fTemp.add(pIni);
                pIni = pSuiv;
                Polygon oS = polyIni.getFactory().createPolygon(fTemp.toArray(new Coordinate[0]));
                lOS.add(oS);
                if (j == (nbPoints - 1)) {
                    lPExt.add(pSuivZmin);
                }
            }
            if (idContrib == 0) {
                exterior = polyIni.getFactory().createLinearRing(lPExt.toArray(new Coordinate[0]));
            } else {
                interior.add(polyIni.getFactory().createLinearRing(lPExt.toArray(lPExt.toArray(new Coordinate[0]))));
            }
        }
        Polygon surFHaut = (Polygon) polyIni.copy();
        lOS.add(surFHaut);
        lOS.add(polyIni.getFactory().createPolygon(exterior, interior.toArray(new LinearRing[0])));
        return polyIni.getFactory().createGeometryCollection(lOS.toArray(new Geometry[0]));
    }
}
