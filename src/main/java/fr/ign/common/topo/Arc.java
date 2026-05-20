package fr.ign.common.topo;

import fr.ign.cogit.simplu3d.model.UrbanFeature;
import org.locationtech.jts.geom.LineString;

import java.util.ArrayList;
import java.util.Collection;

public class Arc {
    LineString geometry;
    Noeud source;
    Noeud target;
    int type;
    int side;
    Face left;
    Face right;
    Collection<UrbanFeature> features = new ArrayList<>();
    public Arc() {}
    public Arc(Noeud source, Noeud target, LineString geometry, Face left, Face right) {
        this.source = source;
        this.target = target;
        this.geometry = geometry;
        this.left = left;
        this.right = right;
    }
    public Arc(LineString geometry, Face left, Face right, int type, int side) {
        this.geometry = geometry;
        this.left = left;
        this.right = right;
        this.type = type;
        this.side = side;
    }
    public void setGeometrie(LineString ls) {
        this.geometry = ls;
    }
    public LineString getGeometrie() {
        return this.geometry;
    }
    public void setTYpe(int valueType) {
        this.type = valueType;
    }
    public int getType() {
        return this.type;
    }
    public int getSide() {
        return this.side;
    }
    public void setSide(int valueType) {
        this.side = valueType;
    }

    public Noeud getNoeudIni() {
        return this.getSource();
    }

    private Noeud getSource() {
        return this.source;
    }

    public Noeud getNoeudFin() {
        return this.getTarget();
    }

    private Noeud getTarget() {
        return this.target;
    }

    public void addCorrespondant(UrbanFeature feature) {
        this.features.add(feature);
    }

    public Face getFaceGauche() {
        return this.left;
    }
    public Face getFaceDroite() {
        return this.right;
    }

    public double longueur() {
        return this.geometry.getLength();
    }
}
