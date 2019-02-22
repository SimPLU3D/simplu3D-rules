package fr.ign.cogit.simplu3d.analysis;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.analysis.FindObjectInDirection;
import fr.ign.cogit.geoxygene.util.conversion.WktGeOxygene;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.LoadFromCollection;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import junit.framework.TestCase;

public class OppositeBoundaryFinderTest extends TestCase {
	
	public void test1() {
		OppositeBoundaryFinderTest.main(null);
	}


	public static void main(String[] args) {

		String polWKT = "POLYGON ((926132.266 6688555.264 0.0, 926132.26 6688555.27 0.0, 926132.27 6688555.28 0.0, 926134.07 6688557.65 0.0, 926134.08 6688557.65 0.0, 926134.09 6688557.66 0.0, 926134.1 6688557.65 0.0, 926134.11 6688557.65 0.0, 926134.16 6688557.61 0.0, 926134.18 6688557.6 0.0, 926161.83 6688535.07 0.0, 926161.91 6688535.01 0.0, 926162.0 6688534.97 0.0, 926162.1 6688534.96 0.0, 926162.2 6688534.96 0.0, 926162.3 6688534.98 0.0, 926162.39 6688535.02 0.0, 926162.47 6688535.07 0.0, 926162.54 6688535.14 0.0, 926202.64 6688585.96 0.0, 926202.66 6688585.97 0.0, 926202.68 6688585.96 0.0, 926212.03 6688578.5 0.0, 926230.4 6688564.17 0.0, 926230.42 6688564.16 0.0, 926213.98 6688541.91 0.0, 926196.959 6688518.829 0.0, 926189.46 6688508.67 0.0, 926189.45 6688508.68 0.0, 926174.611 6688520.768 0.0, 926160.411 6688532.339 0.0, 926160.39 6688532.36 0.0, 926160.378 6688532.366 0.0, 926132.27 6688555.27 0.0, 926132.266 6688555.264 0.0))";

		CadastralParcelLoader.REAFFECT_GEOM_TOPOLOGICAL_MAP = true;
		
		double maximumDistance = 50;

		try {

			IPolygon polygon = (IPolygon) WktGeOxygene.makeGeOxygene(polWKT);
			IFeature parcelle = new DefaultFeature(polygon);
			IFeatureCollection<IFeature> parcelleCollection = new FT_FeatureCollection<>();
			parcelleCollection.add(parcelle);

			Environnement env = LoadFromCollection.load(null, new FT_FeatureCollection<IFeature>(), parcelleCollection,
					new FT_FeatureCollection<IFeature>(), new FT_FeatureCollection<IFeature>(),
					new FT_FeatureCollection<IFeature>(), null);

			for (CadastralParcel cP : env.getCadastralParcels()) {
				
				System.out.println("cP  : " + cP.getGeom());

				for (ParcelBoundary pB : cP.getBoundaries()) {

					FindObjectInDirection.find(pB, cP, env.getCadastralParcels(), maximumDistance);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
