package fr.ign.common.topo;

import org.jgrapht.graph.DefaultEdge;
import org.locationtech.jts.geom.LineString;

public class HalfEdge extends DefaultEdge {
    LineString geometry;
    public void setGeometrie(LineString ls) {
        this.geometry = ls;
    }
    public LineString getGeometrie() {
        return this.geometry;
    }
    HalfEdge twin = null;
    public void setTwin(HalfEdge twin) {
        this.twin = twin;
    }
    public HalfEdge getTwin() {
        return twin;
    }
    Face face = null;
    public void setFace(Face face) {
        this.face = face;
    }
    public Face getFace() {
        return face;
    }
    Arc edge = null;
    public void setEdge(Arc edge) {
        this.edge = edge;
    }
    public Arc getEdge() {
        return this.edge;
    }
}
