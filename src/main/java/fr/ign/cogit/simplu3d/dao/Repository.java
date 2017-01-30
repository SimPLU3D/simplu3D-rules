package fr.ign.cogit.simplu3d.dao;

import java.util.Collection;

public interface Repository<T> {

	/**
	 * Count items
	 * @return
	 */
	public int count();
	
	/**
	 * Read all items from repository
	 * @return
	 */
	public Collection<T> findAll();

}
