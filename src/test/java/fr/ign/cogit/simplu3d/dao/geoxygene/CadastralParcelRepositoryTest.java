package fr.ign.cogit.simplu3d.dao.geoxygene;

import java.io.File;
import java.util.Collection;

import org.junit.Test;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.dao.CadastralParcelRepository;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import junit.framework.TestCase;

public class CadastralParcelRepositoryTest extends TestCase {
	
	CadastralParcelRepository repository ;
	
	@Override
	public void setUp() throws Exception {
		File path = new File(
			getClass().getClassLoader().getResource("demo-01/PARCELLE.shp").getPath()
		);
		IFeatureCollection<IFeature> features = ShapefileReader.read(path.toString());
		this.repository = new CadastralParcelRepositoryGeoxygene(features);
	}

	
	@Test
	public void testDemo01(){
		Collection<CadastralParcel> cadastralParcels = repository.findAll();
		assertEquals(84, cadastralParcels.size());

		CadastralParcel first = cadastralParcels.iterator().next();
		//TODO "0952" with string identifier
		assertEquals( 952, first.getId() );
		assertNotNull( first.getGeom() );
		assertTrue( Math.abs( first.getGeom().area() - 306.51 ) < 0.1 );
	}

}
