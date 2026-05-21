package fr.ign.cogit.simplu3d.dao;

import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.index.SpatialIndex;
import org.locationtech.jts.index.strtree.STRtree;

import java.util.Collection;

public class SpatialRepository<T extends UrbanFeature> extends AbstractRepository<T> {
	private SpatialIndex spatialIndex;
	public SpatialRepository(Collection<T> items, FeatureType featureType, CoordinateReferenceSystem crs) {
		super(items, featureType, crs);
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
	public Collection<T> findIntersects(Geometry geometry){
		buildSpatialIndex();
		@SuppressWarnings("unchecked")
		Collection<T> features = spatialIndex.query(geometry.getEnvelopeInternal());
		return features.stream().filter(f->f.getGeometry().intersects(geometry)).toList();
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
			if (item == null) {
				continue;
			}
			Geometry geometry = item.getGeometry();
			spatialIndex.insert(geometry.getEnvelopeInternal(), item);
		}
	}
}
