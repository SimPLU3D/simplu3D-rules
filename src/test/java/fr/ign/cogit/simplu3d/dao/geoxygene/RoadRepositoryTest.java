package fr.ign.cogit.simplu3d.dao.geoxygene;

import java.io.File;
import java.util.Collection;

import org.junit.Test;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.dao.RoadRepository;
import fr.ign.cogit.simplu3d.model.Road;
import junit.framework.TestCase;

public class RoadRepositoryTest extends TestCase {
	
	RoadRepository repository ;

	@Override
	public void setUp() throws Exception {
		File path = new File(
			getClass().getClassLoader().getResource("demo-01/TRONCON_ROUTE.shp").getPath()
		);
		IFeatureCollection<IFeature> features = ShapefileReader.read(path.toString());
		this.repository = new RoadRepositoryGeoxygene(features);
	}

	
	@Test
	public void testDemo01(){
		Collection<Road> roads = repository.findAll();
		assertEquals(39, roads.size());

		Road first = roads.iterator().next();
		assertEquals(4.0, first.getWidth());
		assertTrue( Math.abs( first.getAxis().length() - 68.8) < 1.0 );
		assertTrue( Math.abs( first.getGeom().area() - 601.0 ) < 1.0 );
		
	}
	
}
