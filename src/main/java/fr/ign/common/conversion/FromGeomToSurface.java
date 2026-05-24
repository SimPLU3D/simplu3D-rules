package fr.ign.common.conversion;

import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.List;

public class FromGeomToSurface {
    public static List<Polygon> convertGeom(Geometry geometry) {
        List<Polygon> polygons = new ArrayList<>();
        if (geometry instanceof Polygon) {
            polygons.add((Polygon) geometry);
        } else if (geometry instanceof MultiPolygon) {
            for (int index = 0; index < geometry.getNumGeometries(); index++) {
                polygons.add((Polygon) geometry.getGeometryN(index));
            }
        } else if (geometry instanceof GeometryCollection) {
            for (int index = 0; index < geometry.getNumGeometries(); index++) {
                polygons.addAll(convertGeom(geometry.getGeometryN(index)));
            }
        }
        return polygons;
    }

    public static MultiPolygon convertMSGeom(Geometry geometry) {
        return geometry.getFactory().createMultiPolygon(convertGeom(geometry).toArray(new Polygon[0]));
    }
}
