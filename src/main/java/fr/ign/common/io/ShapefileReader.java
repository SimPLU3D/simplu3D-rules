package fr.ign.common.io;

import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.referencing.FactoryException;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.referencing.CRS;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ShapefileReader {
    public static DefaultFeatureCollection read(URL url) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put( "url", url );
            DataStore dataStore = DataStoreFinder.getDataStore( map );
            SimpleFeatureSource featureSource = dataStore.getFeatureSource( dataStore.getTypeNames()[0] );
            DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(featureSource.getFeatures());
            dataStore.dispose();
            return featureCollection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
