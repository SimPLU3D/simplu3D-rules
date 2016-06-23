package fr.ign.cogit.simplu3d.reader;

import java.io.File;
import java.util.List;

import org.junit.Test;

import fr.ign.cogit.simplu3d.model.CadastralParcel;
import junit.framework.TestCase;

public class CadastralParcelReaderTest extends TestCase {
	
	@Test
	public void testDemo01(){
		File path = new File(
			getClass().getClassLoader().getResource("demo-01/PARCELLE.shp").getPath()
		);

		CadastralParcelReader reader = new CadastralParcelReader();
		List<CadastralParcel> cadastralParcels = reader.readShapefile(path);
		assertEquals(84, cadastralParcels.size());

		CadastralParcel first = cadastralParcels.get(0);
		//TODO "0952" with string identifier
		assertEquals( 952, first.getId() );
		assertNotNull( first.getGeom() );
		assertTrue( Math.abs( first.getGeom().area() - 306.51 ) < 0.1 );
	}

}
