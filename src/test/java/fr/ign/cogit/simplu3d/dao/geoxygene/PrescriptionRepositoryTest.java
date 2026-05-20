package fr.ign.cogit.simplu3d.dao.geoxygene;

import java.net.URL;
import java.util.Collection;

import fr.ign.common.io.ShapefileReader;

import fr.ign.cogit.simplu3d.dao.PrescriptionRepository;
import fr.ign.cogit.simplu3d.model.Prescription;
import junit.framework.TestCase;

public class PrescriptionRepositoryTest extends TestCase {
	
	public void testReadLinearPrescription(){
		URL url = getClass().getClassLoader().getResource("44118_PLU_20150219/PRESCRIPTION_LIN.shp");
		PrescriptionRepository repository = new PrescriptionRepositoryGeoxygene(ShapefileReader.read(url));
		Collection<Prescription> prescription = repository.findAll();
		assertEquals(466, prescription.size());
		Prescription first = prescription.iterator().next();
		assertNotNull(first.getGeometry());
		//TODO check type
		assertEquals(
			"Patrimoine protégé au titre de la Loi paysage - Murs à préserver", 
			first.getLabel() 
		);
		//TODO check other attributes
	}
}
