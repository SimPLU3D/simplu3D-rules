package fr.ign.cogit.simplu3d.io.geoxygene;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import fr.ign.cogit.geoxygene.api.feature.IFeature;

public abstract class AbstractFeatureAdapter<T> implements IFeatureAdapter<T>{

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
