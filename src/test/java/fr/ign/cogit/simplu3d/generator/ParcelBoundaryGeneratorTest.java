package fr.ign.cogit.simplu3d.generator;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.generator.boundary.Method1BoundaryAnalyzer;
import fr.ign.cogit.simplu3d.generator.boundary.Method2BoundaryAnalyzer;
import fr.ign.cogit.simplu3d.io.feature.CadastralParcelReader;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.common.io.ShapefileReader;
import junit.framework.TestCase;
import org.geotools.feature.DefaultFeatureCollection;

public class ParcelBoundaryGeneratorTest extends TestCase {
	private static final Logger logger = Logger.getLogger(ParcelBoundaryGeneratorTest.class.getName());

    private SpatialRepository<CadastralParcel> cadastralParcels ;
	
	@Override
	public void setUp() throws Exception {
		URL url = getClass().getClassLoader().getResource("cadastral-parcel-01/PARCELLE.shp");
		logger.fine("READ");
		DefaultFeatureCollection collection = ShapefileReader.read(url);
		logger.fine("FOUND " + collection.size() + " features");
        SpatialRepository<CadastralParcel> repository = new CadastralParcelReader().create(collection);
		logger.fine("REPO with " + repository.count() + " features");
		cadastralParcels = repository;
	}

	public void testParcel0001(){
		logger.fine("testParcel0001");
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		logger.fine("builder");
		CadastralParcel cadastralParcel = cadastralParcels.findAll().toArray(new CadastralParcel[0])[0];
		logger.fine(cadastralParcel + " code = " + cadastralParcel.getCode());
		assertEquals("0001", cadastralParcel.getCode());
		Collection<ParcelBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		logger.fine(cadastralParcel + " boundaries = " + boundaries.size());
		assertEquals(4, boundaries.size());
		assertEquals(sumLength(boundaries), cadastralParcel.getGeometry().getLength(), 0.1);
		// NullFaceAnalyzer => check UNKNOWN
		for (ParcelBoundary specificCadastralBoundary : boundaries) {
			assertEquals(ParcelBoundaryType.UNKNOWN, specificCadastralBoundary.getParcelBoundaryType());
			assertEquals(ParcelBoundarySide.UNKNOWN, specificCadastralBoundary.getSide());
		}
	}
	
	public void testParcel0001WithMethod1(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		builder.setBoundaryAnalyzer(new Method1BoundaryAnalyzer());
		CadastralParcel cadastralParcel = cadastralParcels.findAll().toArray(new CadastralParcel[0])[0];
		assertEquals("0001", cadastralParcel.getCode());
		Collection<ParcelBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertEquals(4, boundaries.size());
		assertEquals(sumLength(boundaries), cadastralParcel.getGeometry().getLength(), 0.1);
		// NullFaceAnalyzer => check UNKNOWN
		for (ParcelBoundary specificCadastralBoundary : boundaries) {
			assertEquals(ParcelBoundaryType.ROAD, specificCadastralBoundary.getParcelBoundaryType());
			//TODO ensure this behavior
			//assertEquals(SpecificCadastralBoundarySide.UNKNOWN, specificCadastralBoundary.getSide());
		}
	}

	public void testParcel0002(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		CadastralParcel cadastralParcel = cadastralParcels.findAll().toArray(new CadastralParcel[0])[1];
		assertEquals("0002", cadastralParcel.getCode());
		Collection<ParcelBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertEquals(sumLength(boundaries), cadastralParcel.getGeometry().getLength(), 0.1);
	}
	
	public void testParcel0002WithMethod1(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		builder.setBoundaryAnalyzer(new Method1BoundaryAnalyzer());
		CadastralParcel cadastralParcel = cadastralParcels.findAll().toArray(new CadastralParcel[0])[1];
		assertEquals("0002", cadastralParcel.getCode());
		Collection<ParcelBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertEquals(sumLength(boundaries), cadastralParcel.getGeometry().getLength(), 0.1);
		Map<ParcelBoundaryType, Integer> countTypes = new HashMap<>();
		for (ParcelBoundary boundary : boundaries) {
			if ( countTypes.containsKey(boundary.getParcelBoundaryType()) ){
				countTypes.put( boundary.getParcelBoundaryType(), countTypes.get(boundary.getParcelBoundaryType()) + 1 );
			}else{
				countTypes.put( boundary.getParcelBoundaryType(), 1 );
			}
		}
		assertTrue(countTypes.containsKey(ParcelBoundaryType.ROAD));
		assertTrue(countTypes.containsKey(ParcelBoundaryType.LAT));
		assertEquals(2,countTypes.size());
        assertEquals(4, (int) countTypes.get(ParcelBoundaryType.ROAD));
        assertEquals(3, (int) countTypes.get(ParcelBoundaryType.LAT));
	}
	
	public void testParcel0002WithMethod2(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		builder.setBoundaryAnalyzer(new Method2BoundaryAnalyzer());
		CadastralParcel cadastralParcel = cadastralParcels.findAll().toArray(new CadastralParcel[0])[1];
		assertEquals("0002", cadastralParcel.getCode());
		Collection<ParcelBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertEquals(sumLength(boundaries), cadastralParcel.getGeometry().getLength(), 0.1);
		Map<ParcelBoundaryType, Integer> countTypes = new HashMap<>();
		for (ParcelBoundary boundary : boundaries) {
			if (countTypes.containsKey(boundary.getParcelBoundaryType())) {
				countTypes.put(boundary.getParcelBoundaryType(), countTypes.get(boundary.getParcelBoundaryType()) + 1);
			} else {
				countTypes.put(boundary.getParcelBoundaryType(), 1);
			}
		}
		assertTrue(countTypes.containsKey(ParcelBoundaryType.ROAD));
		assertTrue(countTypes.containsKey(ParcelBoundaryType.LAT));
		assertEquals(2,countTypes.size());
        assertEquals(4, (int) countTypes.get(ParcelBoundaryType.ROAD));
        assertEquals(3, (int) countTypes.get(ParcelBoundaryType.LAT));
	}

	public void testParcel0003(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		CadastralParcel cadastralParcel = cadastralParcels.findAll().toArray(new CadastralParcel[0])[2];
		assertEquals("0003", cadastralParcel.getCode());
		Collection<ParcelBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertEquals(sumLength(boundaries), cadastralParcel.getGeometry().getLength(), 0.1);
	}
	
	public void testParcel0004(){
		CadastralBoundaryGenerator builder = new CadastralBoundaryGenerator(cadastralParcels);
		CadastralParcel cadastralParcel = cadastralParcels.findAll().toArray(new CadastralParcel[0])[3];
		assertEquals("0004", cadastralParcel.getCode());
		Collection<ParcelBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertEquals(sumLength(boundaries), cadastralParcel.getGeometry().getLength(), 0.1);
	}

	private double sumLength(Collection<ParcelBoundary> boundaries){
		double perimeter = 0.0;
		for (ParcelBoundary specificCadastralBoundary : boundaries) {
			perimeter += specificCadastralBoundary.getGeometry().getLength();
		}
		return perimeter;
	}
}
