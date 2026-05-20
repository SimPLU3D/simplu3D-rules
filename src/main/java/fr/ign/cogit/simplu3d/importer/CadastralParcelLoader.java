/**
 * 
 * This software is released under the licence CeCILL
 * <p></p>
 * see LICENSE.TXT
 * <p></p>
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 * 
 *          Assigne les bordures aux parcelles 3 types de bordure (voirie, fond
 *          ou latéral) en fonction du voisinage
 * 
 */
package fr.ign.cogit.simplu3d.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import fr.ign.cogit.simplu3d.generator.CadastralBoundaryGenerator;
import fr.ign.cogit.simplu3d.generator.boundary.Method1BoundaryAnalyzer;
import fr.ign.cogit.simplu3d.generator.boundary.Method2BoundaryAnalyzer;
import fr.ign.cogit.simplu3d.io.feature.CadastralParcelReader;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import org.geotools.feature.DefaultFeatureCollection;

public class CadastralParcelLoader {
	private static final Logger logger = Logger.getLogger(CadastralParcelLoader.class.getName());

	public static int TYPE_ANNOTATION = 2;
	
	//We assign the result of the topologic map to the parcel geometry
	public static boolean REAFFECT_GEOM_TOPOLOGICAL_MAP = false;
	
	public static Collection<CadastralParcel> assignBordureToParcelleWithOrientation(
			DefaultFeatureCollection parcelCollection
	) {
		/*
		 * converts feature to CadastralParcel
		 */
        CadastralParcelReader adapter = new CadastralParcelReader();
        Collection<CadastralParcel> cadastralParcels = new ArrayList<>(adapter.readAll(parcelCollection));
		CadastralBoundaryGenerator boundaryGenerator = new CadastralBoundaryGenerator(cadastralParcels);
		logger.fine("CadastralBoundaryGenerator with " + cadastralParcels.size() + " parcels");
		if ( TYPE_ANNOTATION == 1 ){
			boundaryGenerator.setBoundaryAnalyzer(new Method1BoundaryAnalyzer());
		}else if ( TYPE_ANNOTATION == 2 ){
			boundaryGenerator.setBoundaryAnalyzer(new Method2BoundaryAnalyzer());
		}
		boundaryGenerator.buildCarteTopoAndAnalyzeFaces();
		logger.fine("boundaryGenerator with " + boundaryGenerator.getCarteTopo().getPopFaces().size() + " faces");
		for (CadastralParcel cadastralParcel : cadastralParcels) {
			Collection<ParcelBoundary> boundaries = boundaryGenerator.createParcelBoundaries(cadastralParcel);
			cadastralParcel.getBoundaries().addAll(boundaries);
		}
		return cadastralParcels;
	}
}
