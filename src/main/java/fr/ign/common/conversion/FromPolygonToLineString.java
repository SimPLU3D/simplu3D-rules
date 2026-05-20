package fr.ign.common.conversion;

import fr.ign.common.util.GeomUtils;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.List;

public class FromPolygonToLineString {
    public static List<LineString> convertMultiPolygonToLineStrings(MultiPolygon multiPolygon) {
        return convertPolygonToLineStrings(GeomUtils.getPolygons(multiPolygon));
    }
    public static List<LineString> convertPolygonToLineStrings(List<Polygon> polygons) {
        List<LineString> lineStrings = new ArrayList<>();
        for (Polygon polygon : polygons) {
            lineStrings.addAll(convertPolygonToLineStrings(polygon));
        }
        return lineStrings;
    }
    public static List<LineString> convertPolygonToLineStrings(Polygon polygon) {
        List<LinearRing> rings = new ArrayList<>();
        rings.add(polygon.getExteriorRing());
        for (int i = 0; i<polygon.getNumInteriorRing(); i++) {
            rings.add(polygon.getInteriorRingN(i));
        }
        List<LineString> lineStrings = new ArrayList<>();
        for (LinearRing ring : rings) {
            for (int i = 0; i < ring.getCoordinates().length - 1; i++) {
                lineStrings.add(polygon.getFactory().createLineString(new Coordinate[]{ring.getCoordinates()[i], ring.getCoordinates()[i + 1]}));
            }
        }
        return lineStrings;
    }
}
