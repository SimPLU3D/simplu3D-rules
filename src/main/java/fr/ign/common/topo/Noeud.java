package fr.ign.common.topo;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

public class Noeud {
    Coordinate coordinate;
    public Noeud(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
    public Geometry getGeom() {
        return new GeometryFactory().createPoint(coordinate);
    }
    public Coordinate getCoord() {
        return coordinate;
    }
}
