package fr.ign.cogit.simplu3d.io.feature;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;

/**
 * 
 * Partial implementation of IFeatureReader and helpers
 * 
 * @author MBorne
 *
 * @param <T>
 */
public abstract class AbstractFeatureReader<T> implements IFeatureReader<T>{

	@Override
	public Collection<T> readAll(IFeatureCollection<IFeature> features) {
		List<T> result = new ArrayList<>(features.size());
		for (IFeature feature : features) {
			result.add(read(feature));
		}
		return result;
	}

	/**
	 * Read a string 
	 * @param feature
	 * @param attributeName
	 * @return a string or null
	 */
	protected String readStringAttribute(IFeature feature, String attributeName) {
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
	protected Integer readIntegerAttribute(IFeature feature, String attributeName) {
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
	protected Double readDoubleAttribute(IFeature feature, String attributeName) {
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
	protected Date readDateAttribute(IFeature feature, String attributeName, DateFormat dateFormat) {
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
	protected Object findAttribute(IFeature feature, String attributeName){
		Object result = feature.getAttribute(attributeName);
		if ( result != null ){
			return result;
		}
		result = feature.getAttribute(attributeName.toLowerCase());
		return result;
	}
}
