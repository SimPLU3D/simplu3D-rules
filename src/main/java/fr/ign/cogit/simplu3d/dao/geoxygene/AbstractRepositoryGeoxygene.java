package fr.ign.cogit.simplu3d.dao.geoxygene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.io.feature.IFeatureReader;

/**
 * 
 * Repository implement over a geoxygene feature collection
 * 
 * @author MBorne
 *
 * @param <T>
 */
public class AbstractRepositoryGeoxygene<T> {
	/**
	 * helper for IFeature/domain conversion
	 */
	private IFeatureReader<T> featureAdapter ;
	/**
	 * Items read from geoxygene's feature collection
	 */
	private List<T> items ;
	
	/**
	 * Cached spatial index
	 */
	private SpatialIndex spatialIndex;

	
	public AbstractRepositoryGeoxygene(IFeatureCollection<IFeature> features, IFeatureReader<T> featureAdapter){
		this.featureAdapter = featureAdapter;
		this.items = new ArrayList<>(features.size());
		for (IFeature feature : features) {
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
	 * @param bbox
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<T> findByEnvelope(Envelope bbox){
		buildSpatialIndex();
		
		return spatialIndex.query(bbox);
	}

	/**
	 * Build spatial index for input features
	 */
	private void buildSpatialIndex(){
		if ( spatialIndex != null ){
			return;
		}
		spatialIndex = new STRtree();
		for (T item : items) {
			if ( ! (item instanceof IFeature) ){
				continue;
			}
			IGeometry geometry = ((IFeature) item).getGeom();
			IEnvelope envelope = geometry.getEnvelope();
			if ( envelope.isEmpty() ){
				continue;
			}
			spatialIndex.insert( 
				new Envelope(
					envelope.getLowerCorner().getX(), envelope.getUpperCorner().getX(),
					envelope.getLowerCorner().getY(), envelope.getUpperCorner().getY()
				), 
				item
			);
		}
	}

}
