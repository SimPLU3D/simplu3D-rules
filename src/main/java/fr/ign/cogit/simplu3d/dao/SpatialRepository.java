package fr.ign.cogit.simplu3d.dao;

import java.util.Collection;

import com.vividsolutions.jts.geom.Envelope;

public interface SpatialRepository<T> extends Repository<T> {

	/**
	 * Read items intersecting a bbox 
	 * @param bbox
	 * @return
	 */
	public Collection<T> findByEnvelope(Envelope bbox);
	
}
