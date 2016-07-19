package fr.ign.cogit.simplu3d.generator.building;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.analysis.roof.RoofDetection;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.generator.FootprintGenerator;
import fr.ign.cogit.simplu3d.generator.RoofSurfaceGenerator;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.RoofSurface;
import fr.ign.cogit.simplu3d.model.SpecificWallSurface;

/**
 * 
 * @author MBorne
 *
 */
public class BuildingFactory {
	
	/**
	 * Create a building from a IMultiSurface.
	 * 
	 * @warning wall, roof, etc. are computed
	 * 
	 * @param geom
	 * @return
	 */
	public static Building createBuildingFromMultiSurface(IGeometry geom){
		Building building = new Building();
		building.setGeom(geom);

		// Etape 1 : détection du toit et des façades
		List<IOrientableSurface> lOS = FromGeomToSurface.convertGeom(geom);
		@SuppressWarnings("unchecked")
		IMultiSurface<IOrientableSurface> surfaceRoof = (IMultiSurface<IOrientableSurface>) RoofDetection
				.detectRoof(building, 0.2, true);

		// Util.detectRoof(lOS,
		// 0.2);
		IMultiSurface<IOrientableSurface> surfaceWall = Util.detectVertical(lOS, 0.2);

		// Création facade
		SpecificWallSurface f = new SpecificWallSurface();
		f.setGeom(surfaceWall);
		f.setLod2MultiSurface(surfaceWall);

		List<SpecificWallSurface> lF = new ArrayList<SpecificWallSurface>();
		lF.add(f);
		building.setFacade(lF);

		// Etape 2 : on créé l'emprise du bâtiment
		IOrientableSurface footprint = FootprintGenerator.convert(surfaceRoof);
		building.setFootprint(footprint);
		
		if (footprint != null) {
			// Création toit
			RoofSurface t = RoofSurfaceGenerator.create(surfaceRoof, (IPolygon) footprint.clone());

			// Affectation
			building.setRoofSurface(t);
		}
		return building;
	}

}
