package fr.ign.cogit.simplu3d.reader;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;

/**
 * 
 * Converts features to Model
 *  
 * @warning attribute must be searched with their upper case form
 *  
 * @author MBorne
 *
 * @param <Model> target class
 */
public abstract class AbstractReader<Model> {

	/**
	 * Read a feature
	 * @param feature
	 * @return
	 */
	public abstract Model read(IFeature feature);

	/**
	 * Read shapefile
	 * @param path
	 * @return
	 */
	public List<Model> readShapefile(File path) {
		IFeatureCollection<IFeature> features = ShapefileReader.read(path.toString());
		return read(features);
	}

	/**
	 * Read feature collection
	 * @param features
	 * @return
	 */
	public List<Model> read(IFeatureCollection<IFeature> features) {
		List<Model> result = new ArrayList<>(features.size());
		read(features,result);
		return result;
	}
	
	/**
	 * Read features to result
	 * @param features
	 * @param result
	 */
	public void read(IFeatureCollection<IFeature> features, Collection<Model> result){
		for (IFeature feature : features) {
			Model model = read(feature);
			if ( model != null ){
				result.add(model);	
			}
		}
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
