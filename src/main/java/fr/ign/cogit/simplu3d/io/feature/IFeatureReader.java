package fr.ign.cogit.simplu3d.io.feature;

import fr.ign.cogit.geoxygene.api.feature.IFeature;

/**
 * 
 * Convert features to domain object
 * 
 * TODO check consistency for id management (forward feature.getId() to item.id)
 * TODO clarify ability to manage relationships (or not)
 * 
 * @author MBorne
 *
 * @param <T>
 */
public interface IFeatureReader<T> {
	
	/**
	 * Converts a feature to a model
	 * @param feature
	 * @return
	 */
	public T read(IFeature feature);

}
