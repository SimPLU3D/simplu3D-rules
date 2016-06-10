package fr.ign.cogit.simplu3d.reader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import fr.ign.cogit.simplu3d.io.structDatabase.postgis.ParametersInstructionPG;
import fr.ign.cogit.simplu3d.model.UrbaDocument;
import junit.framework.TestCase;

public class UrbaDocumentReaderTest extends TestCase {
	
	@Test
	public void testDemo01(){
		File path = new File(
			getClass().getClassLoader().getResource("demo-01/DOC_URBA.shp").getPath()
		);
		AbstractReader<UrbaDocument> reader = new UrbaDocumentReader();
		List<UrbaDocument> urbaDocuments = reader.readShapefile(path);
		assertEquals(1, urbaDocuments.size());

		UrbaDocument first = urbaDocuments.get(0);
		assertEquals("12345_20151017", first.getIdUrba());
		assertEquals("PLU", first.getTypeDoc());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(ParametersInstructionPG.DATE_FORMAT_DU1);
		assertEquals("20151017",dateFormat.format(first.getDateAppro()));
		assertEquals("20181017",dateFormat.format(first.getDateFin()));
		assertEquals("T",first.getInterCo());
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
	
	@Test
	public void test44118(){
		File path = new File(
			getClass().getClassLoader().getResource("44118_PLU_20150219/DOC_URBA.shp").getPath()
		);
		AbstractReader<UrbaDocument> reader = new UrbaDocumentReader();
		List<UrbaDocument> urbaDocuments = reader.readShapefile(path);
/*
 * TODO fix geoxygene
 * 
 * fr.ign.cogit.geoxygene.util.conversion.ShapefileReader read
 * AVERTISSEMENT: géométrie nulle, objet ignoré
 * 
 */
		assertEquals(404, urbaDocuments.size());

		UrbaDocument first = urbaDocuments.get(0);
		assertEquals("12345_20151017", first.getIdUrba());
		assertEquals("PLU", first.getTypeDoc());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(ParametersInstructionPG.DATE_FORMAT_DU1);
		assertEquals("20151017",dateFormat.format(first.getDateAppro()));
		assertEquals("20181017",dateFormat.format(first.getDateFin()));
		assertEquals("T",first.getInterCo());
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
}
