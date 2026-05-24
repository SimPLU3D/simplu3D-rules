package fr.ign.common.tools;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static GeometryFactory factory = new GeometryFactory();
    // --------------------------------------------------------------------------------
    // Method to create a square polygon, size s, shift dx, dy
    // --------------------------------------------------------------------------------
    public static Polygon createSquarePolygon(double s, double dx, double dy) {
        List<Coordinate> pointsList = new ArrayList<>();
        pointsList.add(new Coordinate(dx+0, dy+0,  0));
        pointsList.add(new Coordinate(dx+s, dy+0,  0));
        pointsList.add(new Coordinate(dx+s, dy+s,  0));
        pointsList.add(new Coordinate(dx+0, dy+s,  0));
        pointsList.add(new Coordinate(dx+0, dy+0,  0));
        return factory.createPolygon(pointsList.toArray(new Coordinate[0]));
    }

    public static Geometry createCube(double x, double y, double z, double c) {
        // Creating boundaries
        ArrayList<Polygon> BOUNDARY = new ArrayList<>();
        // Filling boundary
        // FACE 1
        List<Coordinate> dpl1 = new ArrayList<>();
        dpl1.add(new Coordinate(x,   y,   z));
        dpl1.add(new Coordinate(x+c, y,   z));
        dpl1.add(new Coordinate(x+c, y+c, z));
        dpl1.add(new Coordinate(x,   y+c, z));
        dpl1.add(new Coordinate(x,   y,   z));
        Polygon polygon1 = factory.createPolygon(dpl1.toArray(new Coordinate[0]));
        BOUNDARY.add(polygon1);
        // FACE 2
        List<Coordinate> dpl2 = new ArrayList<>();
        dpl2.add(new Coordinate(x,   y, z));
        dpl2.add(new Coordinate(x+c, y, z));
        dpl2.add(new Coordinate(x+c, y, z+c));
        dpl2.add(new Coordinate(x,   y, z+c));
        dpl2.add(new Coordinate(x,   y, z));
        Polygon polygon2 = factory.createPolygon(dpl2.toArray(new Coordinate[0]));
        BOUNDARY.add(polygon2);
        // FACE 3
        List<Coordinate> dpl3 = new ArrayList<>();
        dpl3.add(new Coordinate(x, y,   z));
        dpl3.add(new Coordinate(x, y+c, z));
        dpl3.add(new Coordinate(x, y+c, z+c));
        dpl3.add(new Coordinate(x, y,   z+c));
        dpl3.add(new Coordinate(x, y,   z));
        Polygon polygon3 = factory.createPolygon(dpl3.toArray(new Coordinate[0]));
        BOUNDARY.add(polygon3);
        // FACE 4
        List<Coordinate> dpl4 = new ArrayList<>();
        dpl4.add(new Coordinate(x,   y,   z+c));
        dpl4.add(new Coordinate(x+c, y,   z+c));
        dpl4.add(new Coordinate(x+c, y+c, z+c));
        dpl4.add(new Coordinate(x,   y+c, z+c));
        dpl4.add(new Coordinate(x,   y,   z+c));
        Polygon polygon4 = factory.createPolygon(dpl4.toArray(new Coordinate[0]));
        BOUNDARY.add(polygon4);
        // FACE 5
        List<Coordinate> dpl5 = new ArrayList<>();
        dpl5.add(new Coordinate(x,   y+c, z));
        dpl5.add(new Coordinate(x+c, y+c, z));
        dpl5.add(new Coordinate(x+c, y+c, z+c));
        dpl5.add(new Coordinate(x,   y+c, z+c));
        dpl5.add(new Coordinate(x,   y+c, z));
        Polygon polygon5 = factory.createPolygon(dpl5.toArray(new Coordinate[0]));
        BOUNDARY.add(polygon5);
        // FACE 6
        List<Coordinate> dpl6 = new ArrayList<>();
        dpl6.add(new Coordinate(x+c, y,   z));
        dpl6.add(new Coordinate(x+c, y+c, z));
        dpl6.add(new Coordinate(x+c, y+c, z+c));
        dpl6.add(new Coordinate(x+c, y,   z+c));
        dpl6.add(new Coordinate(x+c, y,   z));
        Polygon polygon6 = factory.createPolygon(dpl6.toArray(new Coordinate[0]));
        BOUNDARY.add(polygon6);
        // Creating solid
        return factory.createGeometryCollection(BOUNDARY.toArray(new Geometry[0]));
    }

    public static List<Coordinate> createPointsList(double s, double dx, double dy, double dz) {
        List<Coordinate> POINTS = new ArrayList<>();
        // Shifting second square
        POINTS.add(new Coordinate(0+dx, 0+dy, dz));
        POINTS.add(new Coordinate(s+dx, 0+dy, dz));
        POINTS.add(new Coordinate(s+dx, s+dy, dz));
        POINTS.add(new Coordinate(0+dx, s+dy, dz));
        return POINTS;
    }
}
