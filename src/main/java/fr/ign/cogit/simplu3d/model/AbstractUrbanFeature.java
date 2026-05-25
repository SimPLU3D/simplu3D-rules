package fr.ign.cogit.simplu3d.model;

import org.locationtech.jts.geom.Geometry;

import java.util.HashMap;
import java.util.Map;

public class AbstractUrbanFeature implements UrbanFeature {
    protected Geometry geometry = null;
    public Geometry getGeometry() {
        return this.geometry;
    }
    public void setGeometry(Geometry geom) {
        this.geometry = geom;
    }
    private String id;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    private final Map<String, Object> attributes = new HashMap<>();
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }
    public String getStringAttribute(String key) {
        Object value = this.attributes.get(key);
        return (value != null) ? value.toString() : null;
    }
    public boolean getBooleanAttribute(String key) {
        Object value = this.attributes.get(key);
        if (value instanceof Boolean b) {
            return b;
        }
        return Boolean.parseBoolean(value.toString());
    }
    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }
}
