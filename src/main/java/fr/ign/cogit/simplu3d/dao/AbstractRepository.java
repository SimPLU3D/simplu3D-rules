package fr.ign.cogit.simplu3d.dao;

import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AbstractRepository<T> implements Repository<T> {
    protected final List<T> items;
    public int count(){
        return items.size();
    }
    public List<T> findAll() {
        return items;
    }
    private final FeatureType featureType;
    public FeatureType getFeatureType() {
        return featureType;
    }
    private final CoordinateReferenceSystem crs;
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }
    public AbstractRepository(Collection<T> items, FeatureType featureType, CoordinateReferenceSystem crs) {
        this.items = new ArrayList<>(items);
        this.featureType = featureType;
        this.crs = crs;
    }
}
