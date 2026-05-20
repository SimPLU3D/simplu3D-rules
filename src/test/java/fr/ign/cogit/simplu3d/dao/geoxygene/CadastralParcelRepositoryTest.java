package fr.ign.cogit.simplu3d.dao.geoxygene;

import java.net.URL;
import java.util.Collection;

import fr.ign.common.io.ShapefileReader;
import org.geotools.feature.DefaultFeatureCollection;

import fr.ign.cogit.simplu3d.dao.CadastralParcelRepository;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import junit.framework.TestCase;

public class CadastralParcelRepositoryTest extends TestCase {
	
	CadastralParcelRepository repository ;
	
	@Override
	public void setUp() throws Exception {
		URL url = getClass().getClassLoader().getResource("demo-01/PARCELLE.shp");
		DefaultFeatureCollection features = ShapefileReader.read(url);
		this.repository = new CadastralParcelRepositoryGeoxygene(features);
	}
	
	public void testDemo01(){
		Collection<CadastralParcel> cadastralParcels = repository.findAll();
		assertEquals(84, cadastralParcels.size());
		CadastralParcel first = cadastralParcels.iterator().next();
		assertEquals( "35238000BW0952", first.getCode() );
		assertNotNull( first.getGeometry() );
		assertTrue( Math.abs( first.getGeometry().getArea() - 306.51 ) < 0.1 );
	}

}
