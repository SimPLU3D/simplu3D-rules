package fr.ign.common.conversion;

import fr.ign.common.topology.Triangle;
import fr.ign.common.topology.Vertex;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

public class FromPolygonToTriangle {
    public static boolean isConvertible(Polygon polygon) {
        Coordinate[] dpl = polygon.getCoordinates();
        if (dpl.length == 4) {
            return true;
        } else return dpl.length == 5;
    }
    public static boolean isConvertible(List<Polygon> polygons) {
        for(Polygon polygon : polygons){
            if(!isConvertible(polygon)){
                return false;
            }
        }
        return true;
    }

    public static List<Triangle> convertAndTriangle(List<Polygon> polygonList) {
        List<Triangle> lT = new ArrayList<>();
        for (Polygon pol : polygonList) {
            List<Triangle> tri = convertAndTriangle(pol);
            if (tri != null) {
                lT.addAll(tri);
            }
        }
        return lT;
    }
    public static List<Triangle> convertAndTriangle(Polygon polygon) {
        List<Triangle> lTri = new ArrayList<>();
        Coordinate[] dpl = polygon.getCoordinates();
        if (dpl.length == 4) {
            lTri.add(new Triangle(new Vertex(dpl[0]), new Vertex(dpl[1]), new Vertex(dpl[2])));
            return lTri;
        } else if (dpl.length == 5) {
            Vertex v0 = new Vertex(dpl[0]);
            Vertex v1 = new Vertex(dpl[1]);
            Vertex v2 = new Vertex(dpl[2]);
            Vertex v3 = new Vertex(dpl[3]);
            lTri.add(new Triangle(v0, v1, v2));
            lTri.add(new Triangle(v2, v3, v0));
            return lTri;
        }
        return null;
    }
}
