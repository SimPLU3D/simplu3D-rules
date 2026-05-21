package fr.ign.common.io;

import fr.ign.cogit.simplu3d.dao.Repository;
import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import org.geotools.api.data.*;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import java.io.File;
import java.util.*;

public class ShapefileWriter {
    public static void write(Repository<? extends UrbanFeature> features, String fileName) {
        File file = new File(fileName);
        try {
            ShapefileDataStore store = new ShapefileDataStore(file.toURI().toURL());
            FeatureType featureType = features.getFeatureType();
            SimpleFeatureType simpleFeatureType = featureType.toSimpleFeatureType();
            store.createSchema(simpleFeatureType);
            SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource(store.getSchema().getName());
            Transaction t = new DefaultTransaction();
            featureStore.setTransaction(t);
            DefaultFeatureCollection collection = new DefaultFeatureCollection(null, featureStore.getSchema());
            SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureStore.getSchema());
            for (UrbanFeature f : features.findAll()) {
                List<Object> list = new ArrayList<>();
                list.add(f.getGeometry());
                for (Map.Entry<String, String> entry : featureType.getAttributes().entrySet()) {
                    list.add(f.getAttribute(entry.getKey()));
                }
                SimpleFeature simpleFeature = builder.buildFeature(f.getId(), list.toArray());
                collection.add(simpleFeature);
            }
            featureStore.addFeatures(collection);
            if (features.getCoordinateReferenceSystem() != null)
                store.forceSchemaCRS(features.getCoordinateReferenceSystem());
            t.commit();
            t.close();
            store.dispose();
            //FIXME make this work better (attributes)
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
