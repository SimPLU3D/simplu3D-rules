package fr.ign.cogit.simplu3d.dao.geoxygene;

import java.net.URL;
import java.util.Collection;

import fr.ign.common.io.ShapefileReader;
import org.geotools.feature.DefaultFeatureCollection;

import fr.ign.cogit.simplu3d.dao.BuildingRepository;
import fr.ign.cogit.simplu3d.model.Building;
import junit.framework.TestCase;
import org.locationtech.jts.geom.MultiPolygon;

public class BuildingRepositoryTest extends TestCase {
	BuildingRepository repository ;
	@Override
	public void setUp() throws Exception {
		URL url = getClass().getClassLoader().getResource("demo-01/BATIMENT.shp");
		DefaultFeatureCollection features = ShapefileReader.read(url);
		this.repository = new BuildingRepositoryGeoxygene(features);
	}

	public void testDemo01(){
		Collection<Building> buildings = repository.findAll();
		assertEquals(117, buildings.size());
		Building first = buildings.iterator().next();
		assertNotNull(first.getGeometry());
		assertTrue(first.getGeometry() instanceof MultiPolygon);
		// TODO change behavior
		assertNotNull(first.getRoof());
	}
}
