package fr.ign.cogit.simplu3d.reader;

import java.io.File;
import java.util.List;

import org.junit.Test;

import fr.ign.cogit.simplu3d.model.Road;
import junit.framework.TestCase;

public class RoadsReaderTest extends TestCase {
	
	@Test
	public void testDemo01(){
		File path = new File(
			getClass().getClassLoader().getResource("demo-01/TRONCON_ROUTE.shp").getPath()
		);
		
		RoadReader.ATT_LARGEUR = "LARGEUR"; //TODO case insensitive
		RoadReader.ATT_NOM_RUE = "NOM_VOIE_G";
		RoadReader.ATT_TYPE    = "NATURE"; //TODO case insensitive

		RoadReader reader = new RoadReader();
		List<Road> roads = reader.readShapefile(path);
		assertEquals(39, roads.size());

		Road first = roads.get(0);
		assertEquals(4.0, first.getWidth());
		assertTrue( Math.abs( first.getAxis().length() - 68.8) < 1.0 );
		assertTrue( Math.abs( first.getGeom().area() - 601.0 ) < 1.0 );
	}
	
}
