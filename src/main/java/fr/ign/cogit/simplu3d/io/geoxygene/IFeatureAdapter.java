package fr.ign.cogit.simplu3d.io.geoxygene;

import fr.ign.cogit.geoxygene.api.feature.IFeature;

/**
 * 
 * Convert features to domain object
 * 
 * TODO check consistency for id management (forward feature.getId() to item.id)
 * 
 * @author MBorne
 *
 * @param <T>
 */
public interface IFeatureAdapter<T> {
	
	public T read(IFeature feature);

	//public IFeature write(T item);
	//public IFeatureType getFeatureType();

}
