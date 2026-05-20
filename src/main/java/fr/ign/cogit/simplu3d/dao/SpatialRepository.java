package fr.ign.cogit.simplu3d.dao;

import org.locationtech.jts.geom.Envelope;

import java.util.Collection;

public interface SpatialRepository<T> extends Repository<T> {
	/**
	 * Read items intersecting a bbox 
	 * @param bbox the bounding box where features are selected
	 * @return a collection of selected features of the class T
	 */
    Collection<T> findByEnvelope(Envelope bbox);
}
