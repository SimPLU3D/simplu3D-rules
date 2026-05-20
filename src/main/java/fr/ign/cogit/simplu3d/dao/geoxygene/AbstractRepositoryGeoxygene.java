package fr.ign.cogit.simplu3d.dao.geoxygene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.simplu3d.io.feature.IFeatureReader;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.feature.DefaultFeatureCollection;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.index.SpatialIndex;
import org.locationtech.jts.index.strtree.STRtree;

/**
 * 
 * Repository implement over a geoxygene feature collection
 * 
 * @author MBorne
 *
 * @param <T> The feature class that will be read
 */
public class AbstractRepositoryGeoxygene<T> {
	/**
	 * helper for IFeature/domain conversion
	 */
	private final IFeatureReader<T> featureAdapter;
	/**
	 * Items read from geoxygene's feature collection
	 */
	private final List<T> items;
	
	/**
	 * Cached spatial index
	 */
	private SpatialIndex spatialIndex;

	public AbstractRepositoryGeoxygene(DefaultFeatureCollection features, IFeatureReader<T> featureAdapter){
		this.featureAdapter = featureAdapter;
		this.items = new ArrayList<>(features.size());
		for (SimpleFeature feature : features) {
			T item = featureAdapter.read(feature) ;
			this.items.add(item);
		}
	}

	protected IFeatureReader<T> getFeatureAdapter() {
		return featureAdapter;
	}

	public int count(){
		return items.size();
	}

	public Collection<T> findAll() {
		return items;
	}
	
	/**
	 * 
	 * @param bbox the bounding box in which features will be selected
	 * @return a collection of entity from class T
	 */
	@SuppressWarnings("unchecked")
	public Collection<T> findByEnvelope(Envelope bbox){
		buildSpatialIndex();
		return spatialIndex.query(bbox);
	}

	/**
	 * Build spatial index for input features
	 */
	private void buildSpatialIndex() {
		if (spatialIndex != null) {
			return;
		}
		spatialIndex = new STRtree();
		for (T item : items) {
			if (!(item instanceof UrbanFeature)) {
				continue;
			}
			Geometry geometry = ((UrbanFeature) item).getGeometry();
			spatialIndex.insert(geometry.getEnvelopeInternal(), item);
		}
	}
}
