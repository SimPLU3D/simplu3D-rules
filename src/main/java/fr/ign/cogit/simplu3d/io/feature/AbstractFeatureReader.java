package fr.ign.cogit.simplu3d.io.feature;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 
 * Partial implementation of IFeatureReader and helpers
 * 
 * @author MBorne
 *
 * @param <T> the class of feature to read
 */
public abstract class AbstractFeatureReader<T> implements IFeatureReader<T>{
	protected final FeatureType featureType;
	public FeatureType getFeatureType() {
		return featureType;
	}
    protected AbstractFeatureReader(FeatureType featureType) {
        this.featureType = featureType;
    }
	@Override
	public Collection<T> readAll(FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
		List<T> result = new ArrayList<>(collection.size());
        try (FeatureIterator<SimpleFeature> iterator = collection.features()) {
            while (iterator.hasNext()) {
                T feature = read(iterator.next());
                result.add(feature);
            }
        }
		return result;
	}

	/**
	 * Return the string value from an attribute 
	 * @param feature a feature
	 * @param attributeName an attribute name
	 * @return a string or null
	 */
	protected String readStringAttribute(SimpleFeature feature, String attributeName) {
		Object object = findAttribute(feature,attributeName);
		if ( object == null ){
			return null;
		}else{
			return object.toString();
		}
	}

	/**
	 * Read an integer
	 * @param feature
	 * @param attributeName
	 * @return
	 */
	protected Integer readIntegerAttribute(SimpleFeature feature, String attributeName) {
		Object object = findAttribute(feature,attributeName);
		if ( object == null ){
			return null;
		}else{
			return Integer.parseInt(object.toString());
		}
	}
	
	/**
	 * Read a double 
	 * @param feature
	 * @param attributeName
	 * @return a string or null
	 */
	protected Double readDoubleAttribute(SimpleFeature feature, String attributeName) {
		Object object = findAttribute(feature,attributeName);
		if ( object == null ){
			return null;
		}else{
			return Double.parseDouble(object.toString());
		}
	}

	
	/**
	 * Read a date with a given format
	 * @param feature
	 * @param attributeName
	 * @param dateFormat
	 * @return
	 */
	protected Date readDateAttribute(SimpleFeature feature, String attributeName, DateFormat dateFormat) {
		Object object = findAttribute(feature,attributeName);
		if ( object == null ){
			return null;
		}
		try {
			return dateFormat.parse(object.toString());
		} catch (ParseException e) {
			//TODO log
			return null;
		}
	}
	
	/**
	 * 
	 * Find an attribute by name
	 * 
	 * TODO improve relying on a regexp?
	 * 
	 * @param feature
	 * @param attributeName upper case attribute name
	 * @return
	 */
	protected Object findAttribute(SimpleFeature feature, String attributeName){
		Object result = feature.getAttribute(attributeName);
		if ( result != null ){
			return result;
		}
		result = feature.getAttribute(attributeName.toLowerCase());
		return result;
	}
}
