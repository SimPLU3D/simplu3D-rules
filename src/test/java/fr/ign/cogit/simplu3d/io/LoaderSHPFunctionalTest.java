package fr.ign.cogit.simplu3d.io;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.UrbaDocument;
import junit.framework.TestCase;
import org.junit.Assert;
import org.locationtech.jts.geom.MultiLineString;

/**
 *
 * Test LoaderSHP with sample environments
 * 
 * @author MBorne
 *
 */
public class LoaderSHPFunctionalTest extends TestCase {

	public void testDefaultEnvironment(){
		Environnement env = DemoEnvironmentProvider.getDefaultEnvironment();
		UrbaDocument plu = env.getUrbaDocument();
		Assert.assertNotNull(plu);
		Assert.assertEquals(1, env.getUrbaZones().size());
		Collection<ParcelBoundary> bordures = new ArrayList<>();
		int count = 0;
		Assert.assertEquals("Toutes les parcelles sont chargées.", 19, env.getCadastralParcels().size());
		Assert.assertNotNull(env.getBpU());
		Assert.assertEquals("Toutes les unités foncières sont chargées.", 19, env.getBpU().size());
		for (BasicPropertyUnit bPU : env.getBpU()) {
			Assert.assertNotNull(bPU.getCadastralParcels());
			Assert.assertFalse(bPU.getCadastralParcels().isEmpty());
			for (CadastralParcel sp : bPU.getCadastralParcels()) {
				count = count + sp.getBoundaries().size();
				Assert.assertNotNull(sp.getBoundaries());
				Assert.assertFalse(sp.getBoundaries().isEmpty());
                bordures.addAll(sp.getBoundaries());
			}
		}
		Assert.assertEquals("Toutes les limites séparatives sont chargées.", 142, count);//FIXME was 140!?
		Assert.assertEquals("Toutes les sous parcelles sont chargées.", 19, env.getSubParcels().size());
		//DefaultFeatureCollection featToits = new DefaultFeatureCollection();
		Assert.assertEquals("Les emprises sont générées.", 40, env.getBuildings().size());
/*
		for (AbstractBuilding b : env.getBuildings()) {
			featToits.add(new DefaultFeature(b.getFootprint()));
		}
*/

		//DefaultFeatureCollection featFaitage = new DefaultFeatureCollection();
        List<MultiLineString> roofing = new ArrayList<>();
		for (AbstractBuilding b : env.getBuildings()) {
			//featFaitage.add(new DefaultFeature(b.getRoof().getRoofing()));
			roofing.add(b.getRoof().getRoofing());
		}
		Assert.assertEquals("Les faîtages sont générés.", 40, roofing.size());
		Assert.assertNotNull(env.getRoads());
		Assert.assertFalse(env.getRoads().isEmpty());
		Assert.assertTrue("Le test est un succès", true);
	}
	
	public void testLoadProjectT1(){
		URL url = getClass().getClassLoader().getResource("ProjectT1");
		try {
            assertNotNull(url);
            Environnement env = LoaderSHP.load(url);
			assertEquals( 5, env.getBpU().size() ) ;			
			assertEquals( 0, env.getBuildings().size() ) ;
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void testLoadProjectT2(){
		URL url = getClass().getClassLoader().getResource("ProjectT2");
		try {
            assertNotNull(url);
            Environnement env = LoaderSHP.load(url);
			assertEquals( 4, env.getBpU().size() ) ;			
			assertEquals( 3, env.getBuildings().size() ) ;
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void testLoadProjectT3(){
		URL url = getClass().getClassLoader().getResource("ProjectT3");
		try {
            assertNotNull(url);
            Environnement env = LoaderSHP.load(url);
			assertEquals( 19, env.getBpU().size() ) ;			
			assertEquals( 0, env.getBuildings().size() ) ;
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void testLoadProjectT4(){
		URL url = getClass().getClassLoader().getResource("ProjectT4");
		try {
            assertNotNull(url);
            Environnement env = LoaderSHP.load(url);
			assertEquals( 1, env.getBpU().size() ) ;			
			assertEquals( 1, env.getBuildings().size() ) ;
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
