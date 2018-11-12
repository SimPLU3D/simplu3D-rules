package fr.ign.cogit.simplu3d.io.feature;

import java.util.Collection;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;

/**
 * 
 * Convert features to domain object
 * 
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
	public T read(IFeature feature);
	
	/**
	 * Converts a collection of features to a collection of models
	 * @param features features with undefined class to read
	 * @return a collection of read features
	 */
	public Collection<T> readAll(IFeatureCollection<IFeature> features);

}
