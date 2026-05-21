package fr.ign.cogit.simplu3d.model;

import org.locationtech.jts.geom.Geometry;

public interface UrbanFeature {
    Geometry getGeometry();
    void setGeometry(Geometry geometry);
    void setId(String id);
    String getId();
    Object getAttribute(String key);
    void setAttribute(String key, Object value);
}
