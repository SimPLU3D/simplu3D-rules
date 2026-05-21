package fr.ign.cogit.simplu3d.io.feature;

import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FeatureType {
    private final String featureTypeName;
    public String getFeatureTypeName() {
        return featureTypeName;
    }
    private String geometryType;
    public String getGeometryType() {
        return geometryType;
    }
    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }
    private final Map<String, String> attributes = new HashMap<>();
    public Map<String, String> getAttributes() {
        return attributes;
    }
    public FeatureType(String featureTypeName) {
        this.featureTypeName = featureTypeName;
    }
    private static String makeType(String attribute, String type) {
        return attribute + ":" + type;
    }
    public SimpleFeatureType toSimpleFeatureType() throws SchemaException {
        String spec = attributes.entrySet().stream().map(entry -> makeType(entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(","));
        return DataUtilities.createType(featureTypeName,
                makeType("geometry", geometryType) + "," + spec);
    }
}
