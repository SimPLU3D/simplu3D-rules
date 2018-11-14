/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
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

import java.util.Collection;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.simplu3d.generator.CadastralBoundaryGenerator;
import fr.ign.cogit.simplu3d.generator.boundary.Method1BoundaryAnalyzer;
import fr.ign.cogit.simplu3d.generator.boundary.Method2BoundaryAnalyzer;
import fr.ign.cogit.simplu3d.io.feature.CadastralParcelReader;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;

public class CadastralParcelLoader {

	public static int TYPE_ANNOTATION = 1;
	public static double MINIMUM_AREA_PARC = 2;
	public static int WIDTH_DEP = 3;
	
	public static IFeatureCollection<CadastralParcel> assignBordureToParcelleWithOrientation(
		IFeatureCollection<IFeature> parcelCollection
	) {
		/*
		 * converts feature to CadastralParcel
		 */
		IFeatureCollection<CadastralParcel> cadastralParcels = new FT_FeatureCollection<>();
		CadastralParcelReader adapter = new CadastralParcelReader();
		
		cadastralParcels.addAll(adapter.readAll(parcelCollection));
		
		CadastralBoundaryGenerator boundaryGenerator = new CadastralBoundaryGenerator(cadastralParcels);
		if ( TYPE_ANNOTATION == 1 ){
			boundaryGenerator.setBoundaryAnalyzer(new Method1BoundaryAnalyzer());
		}else if ( TYPE_ANNOTATION == 2 ){
			boundaryGenerator.setBoundaryAnalyzer(new Method2BoundaryAnalyzer());
		}
		for (CadastralParcel cadastralParcel : cadastralParcels) {
			Collection<ParcelBoundary> boundaries = boundaryGenerator.createParcelBoundaries(cadastralParcel);
			cadastralParcel.getBoundaries().addAll(boundaries);
		}
		return cadastralParcels;
	}

}
