package fr.ign.cogit.simplu3d.generator;

import java.net.URL;
import java.util.Collection;

import fr.ign.cogit.simplu3d.dao.CadastralParcelRepository;
import fr.ign.cogit.simplu3d.dao.UrbaZoneRepository;
import fr.ign.cogit.simplu3d.dao.geoxygene.CadastralParcelRepositoryGeoxygene;
import fr.ign.cogit.simplu3d.dao.geoxygene.UrbaZoneRepositoryGeoxygene;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.UrbaZone;
import fr.ign.common.io.ShapefileReader;
import junit.framework.TestCase;

public class SubParcelGeneratorTest extends TestCase {
	
	private Collection<UrbaZone> urbaZones ;
	
	private Collection<CadastralParcel> cadastralParcels ;
	
	@Override
	protected void setUp() throws Exception {
		// load urbaZones
		{
			URL url = getClass().getClassLoader().getResource("demo-02/ZONE_URBA.shp");
			UrbaZoneRepository repository = new UrbaZoneRepositoryGeoxygene(ShapefileReader.read(url));
			urbaZones = repository.findAll();
		}
		// load cadastralParcels
		{
			URL url = getClass().getClassLoader().getResource("demo-02/PARCELLE.SHP");
			CadastralParcelRepository repository = new CadastralParcelRepositoryGeoxygene(ShapefileReader.read(url));
			cadastralParcels = repository.findAll();
		}
	}
	
	/**
	 * check parcel out of UrbaZone
	 */
	public void testParcel35238000BW0655(){
		SubParcelGenerator generator = new SubParcelGenerator(urbaZones);
		CadastralParcel cadastralParcel = findParcelByCode("35238000BW0655");
		assertNotNull(cadastralParcel);
		Collection<SubParcel> subParcels = generator.createSubParcels(cadastralParcel);
		assertEquals(1, subParcels.size());
		SubParcel subParcel = subParcels.iterator().next();
		// check UrbaZone
		assertNull(subParcel.getUrbaZone());
		// check relation SubParcel/CadastralParcel
		assertSame(cadastralParcel, subParcel.getCadastralParcel());
		
		assertTrue(subParcel.getGeometry().equals(cadastralParcel.getGeometry())); // same geometry
		assertNotSame(subParcel.getGeometry(), cadastralParcel.getGeometry()); // geom is cloned
	}
	
	
	
	public void testGlobal(){
		SubParcelGenerator generator = new SubParcelGenerator(urbaZones);
		for (CadastralParcel cadastralParcel : cadastralParcels) {
			Collection<SubParcel> subParcels = generator.createSubParcels(cadastralParcel);
            assertFalse(subParcels.isEmpty()); // at least one SubParcel
		}
	}
	
	private CadastralParcel findParcelByCode(String code){
		for (CadastralParcel cadastralParcel : cadastralParcels) {
			if ( cadastralParcel.getCode().equals(code) ){
				return cadastralParcel;
			}
		}
		return null;
	}
}
