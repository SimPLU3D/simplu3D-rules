package fr.ign.cogit.simplu3d.dao;

import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;

import java.util.Collection;
import java.util.List;

public interface Repository<T> {

    /**
     * Count items
     * @return the number of item
     */
    int count();

    /**
     * Read all items from repository
     * @return a collection of objects from the class T
     */
    List<T> findAll();
    FeatureType getFeatureType();
    CoordinateReferenceSystem getCoordinateReferenceSystem();
}