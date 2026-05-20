package fr.ign.common.util;

import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.List;

public class GeomUtils {
    public static Geometry translate(Geometry g, double x, double y, double z) {
        CoordinateSequenceFilter filter = new CoordinateSequenceFilter() {
            @Override
            public void filter(CoordinateSequence seq, int i) {
                seq.setOrdinate(i, 0, seq.getOrdinate(i, 0) + x);
                seq.setOrdinate(i, 1, seq.getOrdinate(i, 1) + y);
                seq.setOrdinate(i, 2, seq.getOrdinate(i, 2) + z);
            }
            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public boolean isGeometryChanged() {
                return true;
            }
        };
        Geometry transformed = g.copy();
        transformed.apply(filter);
        return transformed;
    }
    public static List<Polygon> getPolygons(MultiPolygon multiPolygon) {
        List<Polygon> polys = new ArrayList<>();
        for (int index = 0; index < multiPolygon.getNumGeometries(); index++) {
            polys.add((Polygon) multiPolygon.getGeometryN(index));
        }
        return polys;
    }
    public static List<LineString> getLineStrings(MultiLineString multiLineString) {
        List<LineString> lineStrings = new ArrayList<>();
        for (int index = 0; index < multiLineString.getNumGeometries(); index++) {
            lineStrings.add((LineString) multiLineString.getGeometryN(index));
        }
        return lineStrings;
    }

    public static List<LineString> getLineStrings(Polygon polygon) {
        List<LineString> lineStrings = new ArrayList<>();
        lineStrings.add(polygon.getFactory().createLineString(polygon.getExteriorRing().getCoordinates()));
        for (int index = 0; index < polygon.getNumInteriorRing(); index++) {
            lineStrings.add(polygon.getFactory().createLineString(polygon.getInteriorRingN(index).getCoordinates()));
        }
        return lineStrings;
    }

    public static boolean equals(Coordinate p1, Coordinate p2, double tolerance) {
        double x1, x2;
        for (int i = 0; i <= 1; i++) {
            x1 = p1.getOrdinate(i);
            x2 = p2.getOrdinate(i);
            if ((x2 > x1 + tolerance) || (x2 < x1 - tolerance)) {
                return false;
            }
        }
        if (!Double.isNaN(p1.getZ())) {
            if (!Double.isNaN(p2.getZ())) {
                x1 = p1.getOrdinate(2);
                x2 = p2.getOrdinate(2);
                return (!(x2 > x1 + tolerance)) && (!(x2 < x1 - tolerance));
            }
        }
        return true;
    }

    public static Coordinate move(Coordinate p, Coordinate offsetPoint) {
        Coordinate result = new Coordinate();
        result.setX(p.getX() + offsetPoint.getX());
        result.setY(p.getY() + offsetPoint.getY());
        result.setZ(p.getZ() + offsetPoint.getZ());
        return result;
    }

    public static Polygon rotation(Polygon geom, Coordinate c, double angle) {
        double cos = Math.cos(angle), sin = Math.sin(angle);
        GeometryFactory gf = new GeometryFactory();
        // rotation de l'enveloppe
        Coordinate[] coord = geom.getExteriorRing().getCoordinates();
        Coordinate[] coord_ = new Coordinate[coord.length];
        for (int i = 0; i < coord.length; i++) {
            double x = coord[i].x, y = coord[i].y;
            coord_[i] = new Coordinate(
                    c.x + cos * (x - c.x) - sin * (y - c.y),
                    c.y + sin * (x - c.x) + cos * (y - c.y));
        }
        LinearRing lr = gf.createLinearRing(coord_);
        // rotation des trous
        LinearRing[] trous = new LinearRing[geom.getNumInteriorRing()];
        for (int j = 0; j < geom.getNumInteriorRing(); j++) {
            Coordinate[] coord2 = geom.getInteriorRingN(j).getCoordinates();
            Coordinate[] coord2_ = new Coordinate[coord2.length];
            for (int i = 0; i < coord2.length; i++) {
                double x = coord2[i].x, y = coord2[i].y;
                coord2_[i] = new Coordinate(c.x + cos * (x - c.x) - sin * (y - c.y),
                        c.y + sin * (x - c.x) + cos * (y - c.y));
            }
            trous[j] = gf.createLinearRing(coord2_);
        }
        return gf.createPolygon(lr, trous);
    }
}
