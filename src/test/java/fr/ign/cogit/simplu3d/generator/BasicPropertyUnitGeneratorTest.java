package fr.ign.cogit.simplu3d.generator;

import java.net.URL;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.io.feature.CadastralParcelReader;
import fr.ign.common.io.ShapefileReader;

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

	private SpatialRepository<CadastralParcel> cadastralParcels ;
	
	@Override
	protected void setUp() throws Exception {
		URL url = getClass().getClassLoader().getResource("demo-01/PARCELLE.shp");
        cadastralParcels = new SpatialRepository<>(new CadastralParcelReader().readAll(ShapefileReader.read(url)));
	}
	
	public void testAggregation(){
		BasicPropertyUnitGenerator builder = new BasicPropertyUnitGenerator(cadastralParcels);
		SpatialRepository<BasicPropertyUnit> propertyUnits = builder.createPropertyUnits();
		assertEquals(84, propertyUnits.count() );
	}
}
