package fr.ign.common.conversion;

import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FromGeomToLineString {
    public static Collection<LineString> convert(Geometry geometry) {
        if (geometry instanceof MultiLineString) {
            List<LineString> lineStrings = new ArrayList<>();
            for (int index = 0; index < geometry.getNumGeometries(); index++) {
                lineStrings.add((LineString) geometry.getGeometryN(index));
            }
            return lineStrings;
        }
        if (geometry instanceof LineString) {
            List<LineString> lineStrings = new ArrayList<>();
            lineStrings.add((LineString) geometry);
            return lineStrings;
        }
        if (geometry instanceof Polygon) {
            return convertSurface((Polygon) geometry);
        }
        if (geometry instanceof MultiPolygon) {
            List<LineString> lineStrings = new ArrayList<>();
            for (int index = 0; index < geometry.getNumGeometries(); index++) {
                lineStrings.addAll(convertSurface((Polygon) geometry.getGeometryN(index)));
            }
            return lineStrings;
        }
        return new ArrayList<>();
    }

    private static Collection<LineString> convertSurface(Polygon geometry) {
        List<LineString> lineStrings = new ArrayList<>();
        lineStrings.add(geometry.getExteriorRing());
        for (int index = 0; index < geometry.getNumInteriorRing(); index++) {
            lineStrings.add(geometry.getInteriorRingN(index));
        }
        return lineStrings;
    }
}
