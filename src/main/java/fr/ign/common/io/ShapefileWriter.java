package fr.ign.common.io;

import fr.ign.cogit.simplu3d.dao.Repository;
import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import org.geotools.api.data.*;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;

import java.io.File;
import java.util.*;

//FIXME ADD TEST
public class ShapefileWriter {
    public static void write(Repository<? extends UrbanFeature> features, String fileName) {
        ShapefileWriter.write(features.findAll(), features.getFeatureType(), features.getCoordinateReferenceSystem(), fileName);
    }
    public static void write(Collection<? extends UrbanFeature> features, FeatureType featureType, CoordinateReferenceSystem crs, String fileName) {
        File file = new File(fileName);
        try {
            ShapefileDataStore store = new ShapefileDataStore(file.toURI().toURL());
            SimpleFeatureType simpleFeatureType = featureType.toSimpleFeatureType();
            store.createSchema(simpleFeatureType);
            SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource(store.getSchema().getName());
            Transaction t = new DefaultTransaction();
            featureStore.setTransaction(t);
            DefaultFeatureCollection collection = new DefaultFeatureCollection(null, featureStore.getSchema());
            SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureStore.getSchema());
            for (UrbanFeature f : features) {
                List<Object> list = new ArrayList<>();
                list.add(f.getGeometry());
                for (Map.Entry<String, String> entry : featureType.getAttributes().entrySet()) {
                    list.add(f.getAttribute(entry.getKey()));
                }
                SimpleFeature simpleFeature = builder.buildFeature(f.getId(), list.toArray());
                collection.add(simpleFeature);
            }
            featureStore.addFeatures(collection);
            if (crs != null)
                store.forceSchemaCRS(crs);
            t.commit();
            t.close();
            store.dispose();
            //FIXME make this work better (attributes)
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
