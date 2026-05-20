package fr.ign.cogit.simplu3d.generator;

import java.net.URL;
import java.util.Collection;

import fr.ign.common.io.ShapefileReader;

import fr.ign.cogit.simplu3d.dao.CadastralParcelRepository;
import fr.ign.cogit.simplu3d.dao.geoxygene.CadastralParcelRepositoryGeoxygene;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import junit.framework.TestCase;

/**
 * 
 * warning incomplete test
 * <p>
 * TODO Create a specific dataset for this test with an owner
 * 
 * @author MBorne
 *
 */
public class BasicPropertyUnitGeneratorTest extends TestCase {

	private Collection<CadastralParcel> cadastralParcels ;
	
	@Override
	protected void setUp() throws Exception {
		URL url = getClass().getClassLoader().getResource("demo-01/PARCELLE.shp");
		CadastralParcelRepository repository = new CadastralParcelRepositoryGeoxygene(ShapefileReader.read(url));
		cadastralParcels = repository.findAll();
	}
	
	public void testAggregation(){
		BasicPropertyUnitGenerator builder = new BasicPropertyUnitGenerator(cadastralParcels);
		Collection<BasicPropertyUnit> propertyUnits = builder.createPropertyUnits();
		assertEquals(84, propertyUnits.size() );
	}
}
