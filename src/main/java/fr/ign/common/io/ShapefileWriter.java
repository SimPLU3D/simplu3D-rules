package fr.ign.common.io;

import fr.ign.cogit.simplu3d.dao.Repository;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import org.geotools.api.data.*;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ShapefileWriter {
    public static void write(Repository<? extends UrbanFeature> features, String fileName) {
        FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
        File file = new File(fileName);
        try {
            Map<String, ?> map = Collections.singletonMap("url", file.toURI().toURL());
            DataStore myData = factory.createNewDataStore(map);

            String typeName = "my";
            SimpleFeatureType featureType = DataUtilities.createType(typeName, "geom:Point,name:String,age:Integer,description:String");
            myData.createSchema(featureType);
            SimpleFeatureStore featureSource = (SimpleFeatureStore) myData.getFeatureSource(typeName);
            //featureSource.addFeatures(features);
            //FIXME
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
