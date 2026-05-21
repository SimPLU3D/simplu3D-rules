package fr.ign.cogit.simplu3d.dao.geoxygene;

import java.net.URL;
import java.util.Collection;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.io.feature.RoadReader;
import fr.ign.common.io.ShapefileReader;
import org.geotools.feature.DefaultFeatureCollection;

import fr.ign.cogit.simplu3d.model.Road;
import junit.framework.TestCase;

public class RoadRepositoryTest extends TestCase {

	SpatialRepository<Road> repository ;

	@Override
	public void setUp() throws Exception {
		URL url = getClass().getClassLoader().getResource("demo-01/TRONCON_ROUTE.shp");
		DefaultFeatureCollection features = ShapefileReader.read(url);
		this.repository = new RoadReader().create(features);
	}

	public void testDemo01(){
		Collection<Road> roads = repository.findAll();
		assertEquals(39, roads.size());
		Road first = roads.iterator().next();
		assertEquals(4.0, first.getWidth());
		assertEquals(68.8, first.getAxis().getLength(), 1.0);
		assertEquals(601.0, first.getGeometry().getArea(), 1.0);
	}
}
