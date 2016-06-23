package fr.ign.cogit.simplu3d.reader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import fr.ign.cogit.simplu3d.io.structDatabase.postgis.ParametersInstructionPG;
import fr.ign.cogit.simplu3d.model.UrbaZone;
import junit.framework.TestCase;

public class UrbaZoneReaderTest extends TestCase {
	
	@Test
	public void testReadShapefile(){
		File path = new File(
			getClass().getClassLoader().getResource("44118_PLU_20150219/ZONE_URBA.shp").getPath()
		);
		UrbaZoneReader reader = new UrbaZoneReader();
		List<UrbaZone> urbaZones = reader.readShapefile(path);
		assertEquals(124, urbaZones.size());

		UrbaZone first = urbaZones.get(0);
		
		assertNotNull(first.getGeom());
		
		//TODO change behavior (identifier should be assigned by database)?
		//assertEquals(0,first.getId());

		assertEquals("NL",first.getLibelle());		
		assertEquals("",first.getLibelong());		
		assertEquals("N",first.getTypeZone());
		assertEquals("04",first.getDestdomi());
		assertEquals("44118_reglement_20150219.pdf",first.getNomFic());
		assertEquals("",first.getUrlFic());
		assertEquals("44118",first.getInsee());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(ParametersInstructionPG.DATE_FORMAT_DU1);
		assertEquals("20150219",dateFormat.format(first.getDateDeb()));
		assertNull(first.getDateFin());

		assertNull(first.getText());
		assertNull(first.getIdPLU());
	}
	
	
}
