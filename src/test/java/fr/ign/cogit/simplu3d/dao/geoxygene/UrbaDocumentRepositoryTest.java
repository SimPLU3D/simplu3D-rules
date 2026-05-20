package fr.ign.cogit.simplu3d.dao.geoxygene;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;

import fr.ign.cogit.simplu3d.dao.AbstractRepository;
import fr.ign.common.io.ShapefileReader;

import fr.ign.cogit.simplu3d.io.feature.UrbaDocumentReader;
import fr.ign.cogit.simplu3d.model.UrbaDocument;
import junit.framework.TestCase;

public class UrbaDocumentRepositoryTest extends TestCase {

	public void testDemo01(){
		URL url = getClass().getClassLoader().getResource("demo-01/DOC_URBA.shp");
		AbstractRepository<UrbaDocument> repository = new AbstractRepository<>(new UrbaDocumentReader().readAll(ShapefileReader.read(url)));
		Collection<UrbaDocument> urbaDocuments = repository.findAll();
		assertEquals(1, urbaDocuments.size());

		UrbaDocument first = urbaDocuments.iterator().next();
		assertEquals("12345_20151017", first.getIdUrba());
		assertEquals("PLU", first.getTypeDoc());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(UrbaDocumentReader.DATE_FORMAT_DU1);
		assertEquals("20151017",dateFormat.format(first.getDateAppro()));
		assertEquals("20181017",dateFormat.format(first.getDateFin()));
		//assertEquals("F",first.getInterCo()); //TODO
		assertEquals("268002541",first.getSiren());		
		assertEquals("03",first.getEtat());
		
		assertEquals("12345_reglement_20151017.pdf",first.getNomReg());
		assertEquals("https://github.com/IGNF/simplu3D-rules",first.getUrlReg());
		assertEquals("TOTO",first.getNomPlan());
		assertEquals("https://github.com/IGNF/simplu3D-rules",first.getUrlPlan());
		assertEquals("https://github.com/IGNF/simplu3D-rules",first.getSiteWeb());
		assertEquals("01",first.getTypeRef());
		assertEquals("20150101",dateFormat.format(first.getDateRef()));		
	}
	
	public void test44118(){
		URL url = getClass().getClassLoader().getResource("44118_PLU_20150219/DOC_URBA.shp");
		AbstractRepository<UrbaDocument> repository = new AbstractRepository<>(new UrbaDocumentReader().readAll(ShapefileReader.read(url)));
		Collection<UrbaDocument> urbaDocuments = repository.findAll();
/*
 * TODO allow EMPTY/NULL geometry in geoxygene https://github.com/IGNF/geoxygene/issues/3
 * 
 * fr.ign.cogit.geoxygene.util.conversion.ShapefileReader read
 * AVERTISSEMENT: géométrie nulle, objet ignoré
 * 
 */
		assertEquals(404, urbaDocuments.size());

		UrbaDocument first = urbaDocuments.iterator().next();
		assertEquals("4400120120223", first.getIdUrba());
		assertEquals("PLU", first.getTypeDoc());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(UrbaDocumentReader.DATE_FORMAT_DU1);
		assertEquals("20120223",dateFormat.format(first.getDateAppro()));
		assertNull(first.getDateFin());
		assertEquals("F",first.getInterCo());
		assertEquals("",first.getSiren());
		assertEquals("03",first.getEtat());
		
		assertEquals("44001_reglement_20120223.pdf",first.getNomReg());
		assertEquals("",first.getUrlReg());
		assertEquals("",first.getNomPlan());
		assertEquals("",first.getUrlPlan());
		assertEquals("",first.getSiteWeb());
		assertEquals("01",first.getTypeRef());
		assertNull(first.getDateRef());
	}
}
