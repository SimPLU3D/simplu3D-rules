package fr.ign.cogit.simplu3d.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.dao.CadastralParcelRepository;
import fr.ign.cogit.simplu3d.dao.geoxygene.CadastralParcelRepositoryGeoxygene;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;
import junit.framework.TestCase;

public class ParcelBoundaryBuilderTest extends TestCase {
	
	private CadastralParcelRepository repository ;
	
	private List<CadastralParcel> cadastralParcels ;
	
	@Override
	protected void setUp() throws Exception {
		File path = new File(
			getClass().getClassLoader().getResource("cadastral-parcel-01/PARCELLE.shp").getPath()
		);
		repository = new CadastralParcelRepositoryGeoxygene(ShapefileReader.read(path.toString()));
		cadastralParcels = new ArrayList<>(repository.findAll());
	}

	public void testParcel0001(){
		ParcelBoundaryBuilder builder = new ParcelBoundaryBuilder(repository);
		CadastralParcel cadastralParcel = cadastralParcels.get(0);
		assertEquals("0001", cadastralParcel.getCode());
		Collection<SpecificCadastralBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertEquals(1, boundaries.size());
	}
	
	public void testParcel0002(){
		ParcelBoundaryBuilder builder = new ParcelBoundaryBuilder(repository);
		CadastralParcel cadastralParcel = cadastralParcels.get(1);
		assertEquals("0002", cadastralParcel.getCode());
		
		Collection<SpecificCadastralBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertTrue( Math.abs(sumLength(boundaries) - cadastralParcel.getGeom().length()) < 1.0e-8 );
	}

	public void testParcel0003(){
		ParcelBoundaryBuilder builder = new ParcelBoundaryBuilder(repository);
		CadastralParcel cadastralParcel = cadastralParcels.get(2);
		assertEquals("0003", cadastralParcel.getCode());
		
		Collection<SpecificCadastralBoundary> boundaries = builder.createParcelBoundaries(cadastralParcel);
		assertTrue( Math.abs(sumLength(boundaries) - cadastralParcel.getGeom().length()) < 1.0e-8 );
	}
	
	public void testParcel0004(){
		ParcelBoundaryBuilder builder = new ParcelBoundaryBuilder(repository);
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

