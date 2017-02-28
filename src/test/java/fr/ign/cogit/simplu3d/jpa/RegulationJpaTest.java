package fr.ign.cogit.simplu3d.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import fr.ign.cogit.simplu3d.model.Rules;
import junit.framework.TestCase;

public class RegulationJpaTest extends TestCase {

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
		List<Rules> items = entityManager.createQuery("SELECT u FROM Regulation u", Rules.class).getResultList();
		assertEquals(0,items.size());
		entityManager.close();
	}
//	
//	public void testCreate(){
//		EntityManager entityManager = emf.createEntityManager();
//		{
//			entityManager.getTransaction().begin();
//			UrbaDocument item = new UrbaDocument();
//			item.setTypeDoc("PLU");
//			entityManager.persist(item);
//			entityManager.getTransaction().commit();
//		}
//		{
//			List<UrbaDocument> items = entityManager.createQuery("SELECT u FROM UrbaDocument u", UrbaDocument.class).getResultList();
//			assertEquals(1,items.size());
//			UrbaDocument item = items.get(0);
//			assertEquals("PLU",item.getTypeDoc());
//		}
//		entityManager.close();
//	}
	
}
