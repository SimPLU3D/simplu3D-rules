package fr.ign.cogit.simplu3d.generator;

import java.io.File;
import java.util.Collection;

import org.junit.Test;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.dao.CadastralParcelRepository;
import fr.ign.cogit.simplu3d.dao.geoxygene.CadastralParcelRepositoryGeoxygene;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import junit.framework.TestCase;

/**
 * 
 * warning incomplete test
 * 
 * TODO Create a specific dataset for this test with an owner
 * 
 * @author MBorne
 *
 */
public class BasicPropertyUnitGeneratorTest extends TestCase {

	private Collection<CadastralParcel> cadastralParcels ;
	
	@Override
	protected void setUp() throws Exception {
		File path = new File(
			getClass().getClassLoader().getResource("demo-01/PARCELLE.shp").getPath()
		);
		CadastralParcelRepository repository = new CadastralParcelRepositoryGeoxygene(ShapefileReader.read(path.toString()));
		cadastralParcels = repository.findAll();
	}
	
	@Test
	public void testAggregation(){
		BasicPropertyUnitGenerator builder = new BasicPropertyUnitGenerator(cadastralParcels);
		IFeatureCollection<BasicPropertyUnit> propertyUnits = builder.createPropertyUnits();
		assertEquals(84, propertyUnits.size() );
	}
	
	
}
