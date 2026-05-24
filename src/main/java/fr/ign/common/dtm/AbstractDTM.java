package fr.ign.common.dtm;

import fr.ign.common.extrusion.Extrusion2DObject;
import fr.ign.common.extrusion.Extrusion3DObject;
import fr.ign.common.geometry.Box3D;
import fr.ign.common.util.GeomUtils;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.Polygon;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDTM {
    // Emprise 3D du MNT
    protected Box3D emprise = null;
    // Exageration
    protected int exageration;
    // Chemin du MNT
    protected String path;
    // Chemin de l'image
    protected String imagePath = "";
    // Dégradés
    protected Color[] colorShade = null;
    // Paramètres de représentation
    protected Envelope imageEnvelope;
    // Indique que le MNT est rempli
    protected boolean isFilled;
    private static final GeometryFactory fact = new GeometryFactory();
    protected AbstractDTM() {
        super();
    }
    public AbstractDTM(String path,
                       String imagePath,
                       Envelope imageEnvelope,
                       int exageration) {
        super();
        this.exageration = exageration;
        this.path = path;
        this.imagePath = imagePath;
        this.imageEnvelope = imageEnvelope;
    }
    public abstract MultiPolygon processSurfacicGrid(double xmin, double xmax, double ymin, double ymax);

    public abstract MultiLineString processLinearGrid(double xmin, double ymin, double xmax, double ymax);

    public abstract Coordinate castCoordinate(double x, double y);

    public Coordinate castCoordinate(double x, double y, double offsetting) {
        Coordinate c = castCoordinate(x, y);
        c.setZ(c.z + offsetting);
        return c;
    }

    public abstract Geometry getGeometryAt(double x, double y);

    public Geometry mapGeom(Geometry geom, double altMax, boolean isHeigth, boolean isSurSampled) {
        if (geom instanceof Polygon poly) {
            Geometry geometryJTS = this.mapSurface(poly, altMax, isHeigth, isSurSampled && (isHeigth && altMax == 0));
            return postProcessingSurface(geometryJTS, isSurSampled, isHeigth, altMax);
        }
        // TODO : on passe ici pour les scb
        if (geom instanceof MultiPolygon poly) {
            int nbElem = poly.getNumGeometries();
            List<Polygon> resultMS = new ArrayList<>();
            for (int i = 0; i < nbElem; i++) {
                Polygon polyTemp = (Polygon) poly.getGeometryN(i);
                Geometry geometryJTS = this.mapSurface(polyTemp, altMax, isHeigth, isSurSampled && (isHeigth && altMax == 0));
                Geometry geomTransformed = postProcessingSurface(geometryJTS, isSurSampled, isHeigth, altMax);
                if (geomTransformed == null) {
                    continue;
                } else if (geometryJTS instanceof MultiPolygon mp) {
                    resultMS.addAll(GeomUtils.getPolygons(mp));
                } else if (geometryJTS instanceof Polygon p) {
                    resultMS.add(p);
                }
            }
            if (resultMS.size() == 1) {
                return resultMS.get(0);
            }

            if (resultMS.size() > 1) {
                return fact.createMultiPolygon(resultMS.toArray(new Polygon[0]));
            }
            return null;
        }
        // GM_Curve
        if (geom instanceof LineString curve) {
            return this.mapCurve(curve, altMax, isHeigth, isSurSampled);
        }

        // GM_MultiCurve
        if (geom instanceof MultiLineString multiCurve) {
            int nbElem = multiCurve.getNumGeometries();
            List<LineString> resultMCurve = new ArrayList<>();
            for (int i = 0; i < nbElem; i++) {
                LineString curveTemp = (LineString) multiCurve.getGeometryN(i);
                LineString gls = this.mapCurve(curveTemp, altMax, isHeigth, isSurSampled);
                if (gls == null) {
                    continue;
                }
                resultMCurve.add(gls);
            }
            return fact.createMultiLineString(resultMCurve.toArray(new LineString[0]));
        }
        // GM_Point
        if (geom instanceof Point point) {
            Coordinate coor = this.castCoordinate(point.getX(), point.getY(), altMax);
            if (isHeigth) {
                return Extrusion2DObject.convertFromPoint(fact.createPoint(coor), coor.getZ(), coor.getZ() + altMax);
            } else {
                return Extrusion2DObject.convertFromPoint(fact.createPoint(coor), coor.getZ(), altMax);
            }
        }
        // GM_MultiPoint
        if (geom instanceof MultiPoint multiPoint) {
            int nbElem = multiPoint.getNumGeometries();
            List<LineString> resultMCurve = new ArrayList<>();
            List<Point> resultMPoints = new ArrayList<>();
            for (int i = 0; i < nbElem; i++) {
                Point point = (Point) multiPoint.getGeometryN(i);
                Coordinate coor = this.castCoordinate(point.getX(), point.getY(), altMax);
                Geometry geomResult = null;
                if (isHeigth) {
                    geomResult = Extrusion2DObject.convertFromPoint(fact.createPoint(coor), coor.getZ(), coor.getZ() + altMax);
                } else {
                    geomResult = Extrusion2DObject.convertFromPoint(fact.createPoint(coor), coor.getZ(), altMax);
                }
                if (geomResult instanceof LineString ls) {
                    resultMCurve.add(ls);
                } else {
                    resultMPoints.add((Point) geomResult);
                }
            }
            if (resultMPoints.isEmpty()) {
                return fact.createMultiLineString(resultMCurve.toArray(new LineString[0]));
            } else if (resultMCurve.isEmpty()) {
                return fact.createMultiPoint(resultMPoints.toArray(new Point[0]));
            } else {
                return fact.createGeometryCollection(new Geometry[]{
                        fact.createMultiPoint(resultMPoints.toArray(new Point[0])),
                        fact.createMultiLineString(resultMCurve.toArray(new LineString[0]))
                });
            }
        }
        return null;
    }

    private Geometry postProcessingSurface(Geometry geomTemp, boolean isSurSampled, boolean isHeigth, double altMax) {
        if (!isSurSampled) {
            if (geomTemp == null || geomTemp.isEmpty()) {
                return null;
            }
            Box3D b = new Box3D(geomTemp);
            double zmin = b.getLLDP().getZ();
            if (isHeigth) {
                // On renvoie des solides qui s'extrudent à partir de zmin
                geomTemp = Extrusion2DObject.convertFromGeometry(geomTemp, zmin, zmin + altMax);
            } else {
                // On renvoie des solides qui s'extrudent à partir de zmin
                geomTemp = Extrusion2DObject.convertFromGeometry(geomTemp, zmin, altMax);
            }
        } else {
            if (isHeigth) {
                // On renvoie des solides qui s'extrudent à partir de zmin
                geomTemp = Extrusion3DObject.conversionFromGeom(geomTemp, altMax);
            } else {
                Box3D b = new Box3D(geomTemp);
                double zmin = b.getLLDP().getZ();
                geomTemp = Extrusion3DObject.conversionFromGeom(geomTemp, altMax - zmin);
            }
        }
        return geomTemp;
    }

    private Geometry mapSurface(Polygon poly, double altMax, boolean isHeight, boolean isSursampled) {
        // On surechantillonne ou non
        if (!isSursampled) {
            // On ne sur échantillonne pas, on se contente de plaquer les
            // bordures du polygones
            LineString curve = this.mapCurve(poly.getExteriorRing(), 0, true, false);
            assert curve != null;
            LinearRing gls = fact.createLinearRing(curve.getCoordinates());
            Coordinate[] dpl = gls.getCoordinates();
            double zmin = Double.POSITIVE_INFINITY;
            for (Coordinate coordinate : dpl) {
                double ztemp = coordinate.z;
                if (zmin > ztemp) {
                    zmin = ztemp;
                }
            }
            int nbInterior = poly.getNumInteriorRing();
            LinearRing[] holes = new LinearRing[nbInterior];
            for (int i = 0; i < nbInterior; i++) {
                LineString interiorCurve = this.mapCurve(poly.getInteriorRingN(i), 0, true, false);
                assert interiorCurve != null;
                LinearRing gls2 = fact.createLinearRing(interiorCurve.getCoordinates());
                holes[i] = gls2;
            }
            return fact.createPolygon(gls, holes);
        }
        // Cas sur échantillonné
        // On récupère les géométries sur lesquelles il faudra découper le
        // polygone
        List<Polygon> lPoly = new ArrayList<>();
        MultiPolygon multiP;
        Coordinate[] coordEnv = poly.getEnvelope().getCoordinates();
        // System.out.println("on passe ici");
        //We add 5 m in order to avoid numerical errors
        double xmin = coordEnv[0].x-5;
        double xmax = coordEnv[2].x+5;
        double ymin = coordEnv[0].y-5;
        double ymax = coordEnv[2].y+5;
        multiP = this.processSurfacicGrid(xmin, xmax, ymin, ymax);
        int nbPoly = multiP.getNumGeometries();
        // On crée une géométrie géoxygne pour chacune de ces mailles
        // (2 triangles par maille)
        for (int i = 0; i < nbPoly; i++) {
            Geometry ob = multiP.getGeometryN(i).intersection(poly);
            if (ob == null || ob.getNumPoints() == 0) {
                continue;
            }
            List<Polygon> p1 = this.returnIntersectionFromPolygon(ob);
            for (Polygon polygon : p1) {
                LineString ls = this.mapCurve(polygon.getExteriorRing(), altMax, isHeight, false);
                assert ls != null;
                Polygon pNew = fact.createPolygon(fact.createLinearRing(ls.getCoordinates()), null);
                lPoly.add(pNew);
            }
        }
        if (lPoly.isEmpty()) {
            return null;
        }
        Polygon[] polygons = lPoly.toArray(new Polygon[0]);
        return fact.createMultiPolygon(polygons);
    }

    private List<Polygon> returnIntersectionFromPolygon(Geometry geom) {
        List<Polygon> polys = new ArrayList<>();
        if (geom == null) {
            return null;
        }
        if (geom instanceof Polygon) {
            polys.add((Polygon) geom);
            return polys;
        } else if (geom instanceof MultiPolygon multiP) {
            int nbElem = multiP.getNumGeometries();
            for (int i = 0; i < nbElem; i++) {
                polys.add((Polygon) multiP.getGeometryN(i));
            }
            return polys;
        } else if (geom instanceof GeometryCollection coll) {
            int nbGeom = coll.getNumGeometries();
            for (int i = 0; i < nbGeom; i++) {
                polys.addAll(this.returnIntersectionFromPolygon(coll.getGeometryN(i)));
            }
            return polys;
        }
        return polys;
    }

    private LineString mapCurve(LineString ls, double altMax, boolean isHeigth, boolean isSursampled) {
        Coordinate[] lDPFinal;
        if (isSursampled) {
            // On sur échantillonne donc on découpe les lignes en fonctions des
            // lignes des mailles du MNT
            Geometry env = ls.getEnvelope();
            Coordinate[] coordEnv = env.getCoordinates();
            MultiLineString gml;
            double xmin, xmax, ymin, ymax;
            if (coordEnv.length < 2) {
                return null;
            }else if(coordEnv.length == 2){
                xmin = (coordEnv[0].x) - 5;
                xmax = (coordEnv[1].x) + 5;
                ymin = (coordEnv[0].y) - 5;
                ymax = (coordEnv[1].y) + 5;
            } else {
                //We add 5 m in order to avoid numerical errors
                xmin = (coordEnv[0].x) - 5;
                xmax = (coordEnv[2].x) + 5;
                ymin = (coordEnv[0].y) - 5;
                ymax = (coordEnv[2].y) + 5;
                //gml = this.processLinearGrid(coordEnv[0].x, coordEnv[0].y,
                //coordEnv[2].x, coordEnv[2].y);
            }
            gml = this.processLinearGrid(xmin, ymin, xmax, ymax);
            // On récupère les points d'intersections
            Geometry obj = gml.intersection(ls);
            // On réordonne les points obtenus et ceux de la ligne initiale
            lDPFinal = this.fusion(ls, obj.getCoordinates()).getCoordinates();
        } else {
            // On ne suréchantillonne pas le ligne finale garde les mêmes points que les initiaux
            lDPFinal = ls.getCoordinates();
        }
        int nbPoints = lDPFinal.length;
        Coordinate[] coords = new Coordinate[nbPoints];
        // On extrude ou plaque les différents points de la polyligne finale en
        // fonction des cas
        for (int i = 0; i < nbPoints; i++) {
            if (isHeigth) {
                coords[i] = this.castCoordinate(lDPFinal[i].x, lDPFinal[i].y, altMax);
            } else {
                coords[i] = this.castCoordinate(lDPFinal[i].x, lDPFinal[i].y, altMax - lDPFinal[i].z);
            }
        }
        return fact.createLineString(coords);
    }

    private Geometry fusion(LineString gls, Coordinate[] dpl) {
        // On recupere les points de la ligne initiale
        Coordinate[] dplIni = gls.getCoordinates();
        Coordinate dpPred = dplIni[0];
        int nbelem = dplIni.length;
        // Points servant pour la ligne finale
        List<Coordinate> dplFin = new ArrayList<>(nbelem);
        Coordinate dpSuiv = null;
        for (int i = 1; i < nbelem; i++) {
            // On procede par couples de points dans le GM lineString
            dpSuiv = dplIni[i];
            // On regarde le max des distances, on ne recuperera aucun point
            // intermédiaire plus loin
            double max = dpSuiv.distance(dpPred);
            dplFin.add(dpPred);
            Vecteur vIni = new Vecteur(dpSuiv.x - dpPred.x, dpSuiv.y - dpPred.y, 0);
            List<Coordinate> dplTemp = new ArrayList<>();
            ArrayList<Double> tabDist = new ArrayList<>();
            for (Coordinate coordinate : dpl) {
                Vecteur vTemp = new Vecteur(coordinate.x - dpPred.x, coordinate.y - dpPred.y, 0);
                // Les points sont alignes
                if (vTemp.prodScalaire(vIni) > 0) {
                    if (Math.abs(vTemp.prodVectoriel(vIni).norme()) < 0.00001) {
                        dplTemp.add(coordinate);
                        tabDist.add(vTemp.norme());
                    }
                }
            }
            // On ordonne les points et on ajoute a dplFin
            for (int j = 0; j < tabDist.size(); j++) {
                double d = Double.POSITIVE_INFINITY;
                int ind = 0;
                int n = tabDist.size();
                for (int k = 0; k < n; k++) {
                    if (tabDist.get(k) < d) {
                        ind = k;
                        d = tabDist.get(k);
                    }
                }
                if (d > max) {
                    break;
                }
                dplFin.add(dplTemp.get(ind));
                tabDist.set(ind, Double.POSITIVE_INFINITY);
            }
            dpPred = dpSuiv;
        }
        dplFin.add(dpSuiv);
        int nbP = dplFin.size();
        Coordinate[] coordF = new Coordinate[nbP];
        for (int i = 0; i < nbP; i++) {
            coordF[i] = dplFin.get(i);
        }
        return fact.createLineString(coordF);
    }
}
