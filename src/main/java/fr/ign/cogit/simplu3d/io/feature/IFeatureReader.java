package fr.ign.cogit.simplu3d.io.feature;

import fr.ign.cogit.simplu3d.dao.Repository;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.FeatureCollection;

import java.util.Collection;

/**
 * 
 * Convert features to domain object
 * <p>
 * TODO check consistency for id management (forward feature.getId() to item.id)
 * TODO clarify ability to manage relationships (or not)
 * 
 * @author MBorne
 *
 * @param <T> the class of read features
 */
public interface IFeatureReader<T> {
	
	/**
	 * Converts a feature to a model
	 * @param feature feature with undefined class to read
	 * @return a feature of the given class
	 */
    T read(SimpleFeature feature);
	
	/**
	 * Converts a collection of features to a collection of models
	 * @param features features with undefined class to read
	 * @return a collection of read features
	 */
    Collection<T> readAll(FeatureCollection<SimpleFeatureType, SimpleFeature> features);
	Repository<T> create(FeatureCollection<SimpleFeatureType, SimpleFeature> features);
}
