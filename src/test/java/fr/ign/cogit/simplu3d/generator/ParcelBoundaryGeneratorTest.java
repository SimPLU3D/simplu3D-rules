package fr.ign.cogit.simplu3d.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.dao.CadastralParcelRepository;
import fr.ign.cogit.simplu3d.dao.geoxygene.CadastralParcelRepositoryGeoxygene;
import fr.ign.cogit.simplu3d.generator.boundary.Method1BoundaryAnalyzer;
import fr.ign.cogit.simplu3d.generator.boundary.Method2BoundaryAnalyzer;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundarySide;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundaryType;
import junit.framework.TestCase;

public class ParcelBoundaryGeneratorTest extends TestCase {
	
	private CadastralParcelRepository repository ;
	
	private List<CadastralParcel> cadastralParcels ;
	
	@Override
	public void setUp() throws Exception {
		File path = new File(
			getClass().getClassLoader().getResource("cadastral-parcel-01/PARCELLE.shp").getPath()
		);
		repository = new CadastralParcelRepositoryGeoxygene(ShapefileReader.read(path.toString()));
		cadastralParcels = new ArrayList<>(repository.findAll());
	}
	
	

	public void testParcel0001(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		CadastralParcel cadastralParcel = cadastralParcels.get(0);
		assertEquals("0001", cadastralParcel.getCode());
		Collection<SpecificCadastralBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertEquals(4, boundaries.size());
		assertTrue( Math.abs(sumLength(boundaries) - cadastralParcel.getGeom().length()) < 1.0e-8 );
		// NullFaceAnalyzer => check UNKNOWN
		for (SpecificCadastralBoundary specificCadastralBoundary : boundaries) {
			assertEquals(SpecificCadastralBoundaryType.UNKNOWN, specificCadastralBoundary.getType());
			assertEquals(SpecificCadastralBoundarySide.UNKNOWN, specificCadastralBoundary.getSide());
		}
	}
	
	public void testParcel0001WithMethod1(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		builder.setBoundaryAnalyzer(new Method1BoundaryAnalyzer());
		CadastralParcel cadastralParcel = cadastralParcels.get(0);
		assertEquals("0001", cadastralParcel.getCode());
		Collection<SpecificCadastralBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertEquals(4, boundaries.size());
		assertTrue( Math.abs(sumLength(boundaries) - cadastralParcel.getGeom().length()) < 1.0e-8 );
		// NullFaceAnalyzer => check UNKNOWN
		for (SpecificCadastralBoundary specificCadastralBoundary : boundaries) {
			assertEquals(SpecificCadastralBoundaryType.ROAD, specificCadastralBoundary.getType());
			//TODO ensure this behavior
			//assertEquals(SpecificCadastralBoundarySide.UNKNOWN, specificCadastralBoundary.getSide());
		}
	}

	
	public void testParcel0002(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		CadastralParcel cadastralParcel = cadastralParcels.get(1);
		assertEquals("0002", cadastralParcel.getCode());
		
		Collection<SpecificCadastralBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertTrue( Math.abs(sumLength(boundaries) - cadastralParcel.getGeom().length()) < 1.0e-8 );
	}
	
	public void testParcel0002WithMethod1(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		builder.setBoundaryAnalyzer(new Method1BoundaryAnalyzer());
		CadastralParcel cadastralParcel = cadastralParcels.get(1);
		assertEquals("0002", cadastralParcel.getCode());

		Collection<SpecificCadastralBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertTrue( Math.abs(sumLength(boundaries) - cadastralParcel.getGeom().length()) < 1.0e-8 );
		
		Map<SpecificCadastralBoundaryType, Integer> countTypes = new HashMap<>();
		
		for (SpecificCadastralBoundary boundary : boundaries) {
			if ( countTypes.containsKey(boundary.getType()) ){
				countTypes.put( boundary.getType(), countTypes.get(boundary.getType()) + 1 );
			}else{
				countTypes.put( boundary.getType(), 1 );
			}
		}
		assertTrue(countTypes.containsKey(SpecificCadastralBoundaryType.ROAD));
		assertTrue(countTypes.containsKey(SpecificCadastralBoundaryType.LAT));
		assertEquals(2,countTypes.size());
		
		assertTrue(4 == countTypes.get(SpecificCadastralBoundaryType.ROAD));
		assertTrue(3 == countTypes.get(SpecificCadastralBoundaryType.LAT));
	}
	
	public void testParcel0002WithMethod2(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		builder.setBoundaryAnalyzer(new Method2BoundaryAnalyzer());
		CadastralParcel cadastralParcel = cadastralParcels.get(1);
		assertEquals("0002", cadastralParcel.getCode());

		Collection<SpecificCadastralBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertTrue( Math.abs(sumLength(boundaries) - cadastralParcel.getGeom().length()) < 1.0e-8 );
		
		Map<SpecificCadastralBoundaryType, Integer> countTypes = new HashMap<>();
		
		for (SpecificCadastralBoundary boundary : boundaries) {
			if ( countTypes.containsKey(boundary.getType()) ){
				countTypes.put( boundary.getType(), countTypes.get(boundary.getType()) + 1 );
			}else{
				countTypes.put( boundary.getType(), 1 );
			}
		}
		assertTrue(countTypes.containsKey(SpecificCadastralBoundaryType.ROAD));
		assertTrue(countTypes.containsKey(SpecificCadastralBoundaryType.LAT));
		assertEquals(2,countTypes.size());
		
		assertTrue(4 == countTypes.get(SpecificCadastralBoundaryType.ROAD));
		assertTrue(3 == countTypes.get(SpecificCadastralBoundaryType.LAT));
	}
	
	

	public void testParcel0003(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		CadastralParcel cadastralParcel = cadastralParcels.get(2);
		assertEquals("0003", cadastralParcel.getCode());
		
		Collection<SpecificCadastralBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertTrue( Math.abs(sumLength(boundaries) - cadastralParcel.getGeom().length()) < 1.0e-8 );
	}
	
	public void testParcel0004(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		CadastralParcel cadastralParcel = cadastralParcels.get(3);
		assertEquals("0004", cadastralParcel.getCode());
		
		Collection<SpecificCadastralBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertTrue( Math.abs(sumLength(boundaries) - cadastralParcel.getGeom().length()) < 1.0e-8 );
	}

	
	private double sumLength(Collection<SpecificCadastralBoundary> boundaries){
		double perimeter = 0.0;
		for (SpecificCadastralBoundary specificCadastralBoundary : boundaries) {
			perimeter += specificCadastralBoundary.getGeom().length();
		}
		return perimeter;
	}
	
	
	
}

