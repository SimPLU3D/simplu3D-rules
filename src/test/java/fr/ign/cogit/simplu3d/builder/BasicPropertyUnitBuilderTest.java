package fr.ign.cogit.simplu3d.builder;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.reader.CadastralParcelReader;
import junit.framework.TestCase;

/**
 * 
 * @warning imcomplete test
 * 
 * TODO Create a specific dataset for this test with an owner
 * 
 * @author MBorne
 *
 */
public class BasicPropertyUnitBuilderTest extends TestCase {

	private Collection<CadastralParcel> cadastralParcels ;
	
	@Override
	protected void setUp() throws Exception {
		File path = new File(
			getClass().getClassLoader().getResource("demo-01/PARCELLE.shp").getPath()
		);

		CadastralParcelReader reader = new CadastralParcelReader();
		cadastralParcels = reader.readShapefile(path);
	}
	
	@Test
	public void testAggregation(){
		BasicPropertyUnitBuilder builder = new BasicPropertyUnitBuilder(cadastralParcels);
		IFeatureCollection<BasicPropertyUnit> propertyUnits = builder.buildPropertyUnits();
		assertEquals(84, propertyUnits.size() );
	}
	
	
}
