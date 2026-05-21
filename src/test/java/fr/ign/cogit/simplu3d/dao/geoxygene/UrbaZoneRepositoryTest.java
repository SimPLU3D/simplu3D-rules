package fr.ign.cogit.simplu3d.dao.geoxygene;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;

import fr.ign.cogit.simplu3d.dao.AbstractRepository;
import fr.ign.common.io.ShapefileReader;

import fr.ign.cogit.simplu3d.io.feature.UrbaZoneReader;
import fr.ign.cogit.simplu3d.model.UrbaZone;
import junit.framework.TestCase;

public class UrbaZoneRepositoryTest extends TestCase {
	
	public void testReadShapefile(){
		URL url = getClass().getClassLoader().getResource("44118_PLU_20150219/ZONE_URBA.shp");
		AbstractRepository<UrbaZone> repository = new UrbaZoneReader().create(ShapefileReader.read(url));
		Collection<UrbaZone> urbaZones = repository.findAll();
		assertEquals(124, urbaZones.size());
		UrbaZone first = urbaZones.iterator().next();
		assertNotNull(first.getGeometry());
		//TODO change behavior (identifier should be assigned by database)?
		//assertEquals(0,first.getId());
		assertEquals("NL",first.getLibelle());		
		assertEquals("",first.getLibelong());		
		assertEquals("N",first.getTypeZone());
		assertEquals("04",first.getDestdomi());
		assertEquals("44118_reglement_20150219.pdf",first.getNomFic());
		assertEquals("",first.getUrlFic());
		assertEquals("44118",first.getInsee());
		SimpleDateFormat dateFormat = new SimpleDateFormat(UrbaZoneReader.DATE_FORMAT_DU1);
		assertEquals("20150219",dateFormat.format(first.getDateApprobation()));
		assertNull(first.getDateValidite());
		assertNull(first.getText());
	}
}
