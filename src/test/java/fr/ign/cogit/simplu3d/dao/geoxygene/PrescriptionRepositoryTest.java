package fr.ign.cogit.simplu3d.dao.geoxygene;

import java.io.File;
import java.util.Collection;

import org.junit.Test;

import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.dao.PrescriptionRepository;
import fr.ign.cogit.simplu3d.model.Prescription;
import junit.framework.TestCase;

public class PrescriptionRepositoryTest extends TestCase {
	
	@Test
	public void testReadLinearPrescription(){
		File path = new File(
			getClass().getClassLoader().getResource("44118_PLU_20150219/PRESCRIPTION_LIN.shp").getPath()
		);
		PrescriptionRepository repository = new PrescriptionRepositoryGeoxygene(
			ShapefileReader.read(path.toString())
		);
		Collection<Prescription> prescription = repository.findAll();
		assertEquals(466, prescription.size());

		Prescription first = prescription.iterator().next();
		assertNotNull(first.getGeom());
		//TODO check type
		assertEquals(
			"Patrimoine protégé au titre de la Loi paysage - Murs à préserver", 
			first.getLabel() 
		);
		//TODO check other attributes
	}
	
	
}
