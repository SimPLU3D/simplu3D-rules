package fr.ign.cogit.simplu3d.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import fr.ign.cogit.geoxygene.util.conversion.ParseException;
import fr.ign.cogit.geoxygene.util.conversion.WktGeOxygene;
import fr.ign.cogit.simplu3d.model.UrbaZone;
import junit.framework.TestCase;

public class UrbaZoneJpaTest extends TestCase {

	private EntityManagerFactory emf;
	
	@Override
	protected void setUp() throws Exception {
		this.emf = Persistence.createEntityManagerFactory("simplu3d");
	}
	
	@Override
	protected void tearDown() throws Exception {
		this.emf.close();
	}
	
	public void testFindAll(){
		EntityManager entityManager = emf.createEntityManager();
		List<UrbaZone> items = entityManager.createQuery("SELECT u FROM UrbaZone u", UrbaZone.class).getResultList();
		assertEquals(0,items.size());
		entityManager.close();
	}
	
	public void testCreate() throws ParseException{
		EntityManager entityManager = emf.createEntityManager();
		{
			entityManager.getTransaction().begin();
			UrbaZone item = new UrbaZone();
			item.setInsee("25349");
			item.setGeom(WktGeOxygene.makeGeOxygene("POINT(3.0 4.0)").buffer(2.0));
			entityManager.persist(item);
			entityManager.getTransaction().commit();
		}
		{
			List<UrbaZone> items = entityManager.createQuery("SELECT u FROM UrbaZone u", UrbaZone.class).getResultList();
			assertEquals(1,items.size());
			UrbaZone item = items.get(0);
			assertEquals("25349",item.getInsee());
			assertEquals(
				"POLYGON ((5.0 4.0 0.0, 4.961571 3.609819 0.0, 4.847759 3.234633 0.0, 4.662939 2.88886 0.0, 4.414214 2.585786 0.0, 4.11114 2.337061 0.0, 3.765367 2.152241 0.0, 3.390181 2.038429 0.0, 3.0 2.0 0.0, 2.609819 2.038429 0.0, 2.234633 2.152241 0.0, 1.88886 2.337061 0.0, 1.585786 2.585786 0.0, 1.337061 2.88886 0.0, 1.152241 3.234633 0.0, 1.038429 3.609819 0.0, 1.0 4.0 0.0, 1.038429 4.390181 0.0, 1.152241 4.765367 0.0, 1.337061 5.11114 0.0, 1.585786 5.414214 0.0, 1.88886 5.662939 0.0, 2.234633 5.847759 0.0, 2.609819 5.961571 0.0, 3.0 6.0 0.0, 3.390181 5.961571 0.0, 3.765367 5.847759 0.0, 4.11114 5.662939 0.0, 4.414214 5.414214 0.0, 4.662939 5.11114 0.0, 4.847759 4.765367 0.0, 4.961571 4.390181 0.0, 5.0 4.0 0.0))",
				item.getGeom().toString()
			);
		}
		entityManager.close();
	}

}
