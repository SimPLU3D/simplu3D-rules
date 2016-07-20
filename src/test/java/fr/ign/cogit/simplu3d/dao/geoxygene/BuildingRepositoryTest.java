package fr.ign.cogit.simplu3d.dao.geoxygene;

import java.io.File;
import java.util.Collection;

import org.junit.Test;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.dao.BuildingRepository;
import fr.ign.cogit.simplu3d.model.Building;
import junit.framework.TestCase;

public class BuildingRepositoryTest extends TestCase {
	
	BuildingRepository repository ;

	@Override
	public void setUp() throws Exception {
		File path = new File(
			getClass().getClassLoader().getResource("demo-01/BATIMENT.shp").getPath()
		);
		IFeatureCollection<IFeature> features = ShapefileReader.read(path.toString());
		this.repository = new BuildingRepositoryGeoxygene(features);
	}

	
	@Test
	public void testDemo01(){
		Collection<Building> buildings = repository.findAll();
		assertEquals(117, buildings.size());

		Building first = buildings.iterator().next();
		
		assertNotNull(first.getGeom());
		assertTrue( first.getGeom() instanceof IMultiSurface<?>);

		// TODO change behavior
		assertNotNull(first.getRoof());
	}
	
}
