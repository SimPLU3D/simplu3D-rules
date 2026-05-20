package fr.ign.common.topo;

import org.locationtech.jts.geom.Polygon;

public class Face {
    Polygon geometry;
    public Polygon getGeometrie() {
        return geometry;
    }
    public Face(Polygon geometry) {
        this.geometry = geometry;
    }
}
