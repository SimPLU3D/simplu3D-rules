package fr.ign.common.analysis;

import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.conversion.FromPolygonToTriangle;
import fr.ign.common.util.OrientedBoundingBox;
import fr.ign.common.util.Vecteur;
import fr.ign.common.analysis.building.CutBuilding;
import fr.ign.common.equation.PlanEquation;
import fr.ign.common.topology.Triangle;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Cut3DGeomFrom2D {
    private static final Logger logger = Logger.getLogger(Cut3DGeomFrom2D.class.getName());

    public static List<Polygon> cutListSurfaceFromPolygon(List<Polygon> polygons, Polygon osTemp) {
        List<Polygon> lOS = new ArrayList<>();
        for (Polygon p : polygons) {
            lOS.addAll(cutSurfaceFromPolygon(p, osTemp));
        }
        return lOS;
    }

    private static List<Polygon> cutSurfaceFromPolygon(Polygon p, Polygon polyCut) {
        List<Triangle> lT = FromPolygonToTriangle.convertAndTriangle(p);
        assert lT != null;
        if (lT.get(0).isEmpty()){
            logger.severe("lT est vide");
        }
        return cutTriangleFromPolygon(lT, polyCut);
    }

    private static List<Polygon> cutTriangleFromPolygon(List<Triangle> lT, Polygon polyCut) {
        List<Polygon> lGeom = new ArrayList<>();
        List<Triangle> verticalT = new ArrayList<>();
        List<Triangle> horizontalT = new ArrayList<>();
        for (Triangle t : lT) {
            if (isVertical(t)) {
                verticalT.add(t);
            } else {
                horizontalT.add(t);
            }
        }
        for (Triangle t : horizontalT) {
            Polygon geomTemp = handleHorizontal(t, polyCut);
            if (geomTemp != null) {
                lGeom.add(geomTemp);
            }
        }
        List<LineString> lSExtCut = new ArrayList<>();
        int nbSom = polyCut.getExteriorRing().getNumPoints();
        for (int i = 0; i < nbSom - 1; i++) {
            lSExtCut.add(polyCut.getFactory().createLineString(new Coordinate[]{
                    polyCut.getExteriorRing().getCoordinateN(i),
                    polyCut.getExteriorRing().getCoordinateN(i + 1)
            }));
        }
        for (Triangle t : verticalT) {
            double distance1 = t.getCoordinate(0).distance(t.getCoordinate(1));
            double distance2 = t.getCoordinate(2).distance(t.getCoordinate(1));
            double distance3 = t.getCoordinate(2).distance(t.getCoordinate(0));
            LineString ls = null;
            if (distance1 > distance2 && distance1 > distance3) {
                ls = polyCut.getFactory().createLineString(new Coordinate[]{
                        t.getCoordinate(0),
                        t.getCoordinate(1)
                });
            }
            if (distance2 >= distance1 && distance2 > distance3) {
                ls = polyCut.getFactory().createLineString(new Coordinate[]{
                        t.getCoordinate(2),
                        t.getCoordinate(1)
                });
            }
            if (distance3 >= distance1 && distance3 >= distance2) {
                ls = polyCut.getFactory().createLineString(new Coordinate[]{
                        t.getCoordinate(2),
                        t.getCoordinate(0)
                });
            }
            if (ls == null) {
                logger.severe("ls null");
            }
            if (polyCut.contains(ls)) {
                lGeom.add(t.getPolygon());
                continue;
            }
            // on a un segment 2D représentatif
            for (LineString lsTemp : lSExtCut) {
                if (!lsTemp.intersects(ls)) {
                    continue;
                }
                Coordinate dp1 = lsTemp.getCoordinateN(0);
                dp1.setZ(0);
                Coordinate dp2 = lsTemp.getCoordinateN(1);
                dp2.setZ(0);
                Coordinate dp3 = dp1.copy();
                dp3.setZ(1);
                PlanEquation ep = new PlanEquation(new Coordinate[]{dp1, dp2, dp3});
                List<Geometry> lOS = CutBuilding.cut(t, ep);
                for (Geometry os : lOS) {
                    if (os != null && !os.isEmpty()) {
                        List<Coordinate> dpl = new ArrayList<>(List.of(os.getCoordinates()));
                        dpl.add(t.getCoordinate(0));
                        dpl.add(t.getCoordinate(1));
                        dpl.add(t.getCoordinate(2));
                        int nbPos = dpl.size();
                        for (int i = 0; i < nbPos; i++) {
                            Point p = polyCut.getFactory().createPoint(dpl.get(i));
                            if (!polyCut.buffer(0.2).contains(p)) {
                                dpl.remove(i);
                                i--;
                                nbPos--;
                            }
                        }
                        nbPos = dpl.size();
                        if (nbPos < 2 || nbPos > 4) {
                            logger.severe("What");
                            continue;
                        }
                        if (nbPos == 3) {
                            lGeom.add(polyCut.getFactory().createPolygon(new Coordinate[]{dpl.get(0), dpl.get(1), dpl.get(2)}));
                            continue;
                        }
                        if (nbPos == 4) {
                            Vecteur v1 = new Vecteur(dpl.get(0), dpl.get(1));
                            v1.normalise();
                            Vecteur v2 = new Vecteur(dpl.get(1), dpl.get(2));
                            v2.normalise();
                            // if(v1.prodVectoriel(v2).norme() < 0.1){
                            lGeom.add(polyCut.getFactory().createPolygon(new Coordinate[]{dpl.get(3), dpl.get(1), dpl.get(2)}));
                            lGeom.add(polyCut.getFactory().createPolygon(new Coordinate[]{dpl.get(3), dpl.get(0), dpl.get(2)}));
                            // }else{
                            lGeom.add(polyCut.getFactory().createPolygon(new Coordinate[]{dpl.get(0), dpl.get(1), dpl.get(2)}));
                            lGeom.add(polyCut.getFactory().createPolygon(new Coordinate[]{dpl.get(3), dpl.get(2), dpl.get(0)}));
                            // }
                        }
                        // lGeom.add(os);
                    }
                }
            }
        }
        return lGeom;
    }

    private static Polygon handleHorizontal(Triangle t, Polygon poly) {
        Geometry geom = t.getPolygon().intersection(poly);
        if (geom.isEmpty() || geom.getArea() == 0) {
            return null;
        }
        PlanEquation eQ = new PlanEquation(t.getPolygon());
        for (Coordinate dp : geom.getCoordinates()) {
            dp.setZ(eQ.getZ(dp));
        }
        return FromGeomToSurface.convertGeom(geom).get(0);
    }

    private static boolean isVertical(Triangle t) {
        PlanEquation pE = new PlanEquation(t.getPolygon());
        Vecteur v = pE.getNormale().getNormalised();
        return Math.abs(v.getZ()) < 0.1;
    }

    public static List<Polygon> cutFeatureFromPolygon(Building building, Polygon polyCut) {
        List<Polygon> lS = new ArrayList<>();
        OrientedBoundingBox oBB = new OrientedBoundingBox(building.getGeometry());
        if (!oBB.getPoly().intersects(building.getGeometry())) {
            return lS;
        }
        return cutListSurfaceFromPolygon(FromGeomToSurface.convertGeom(building.getGeometry()), polyCut);
    }
}
