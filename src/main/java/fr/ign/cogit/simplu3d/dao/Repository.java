package fr.ign.cogit.simplu3d.dao;

import java.util.Collection;

public interface Repository<T> {

	/**
	 * Count items
	 * @return the number of item
	 */
	public int count();
	
	/**
	 * Read all items from repository
	 * @return a collection of objects from the class T
	 */
	public Collection<T> findAll();

}
