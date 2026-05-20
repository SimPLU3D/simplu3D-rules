package fr.ign.cogit.simplu3d.generator.building;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.generator.FootprintGenerator;
import fr.ign.cogit.simplu3d.generator.RoofSurfaceGenerator;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.RoofSurface;
import fr.ign.cogit.simplu3d.model.WallSurface;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.analysis.roof.RoofDetection;
import fr.ign.common.util.Util;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

/**
 * 
 * @author MBorne
 *
 */
public class BuildingFactory {
	
	/**
	 * Create a building from a IMultiSurface.
	 * <p></p>
	 * warning wall, roof, etc. are computed
	 * 
	 * @param geom a new 3D building
	 * @return a MultiSurface geometry that is used to create a building
	 */
	public static Building createBuildingFromMultiSurface(Geometry geom){
		Building building = new Building();
		building.setGeometry(geom);
		// Etape 1 : détection du toit et des façades
		List<Polygon> lOS = FromGeomToSurface.convertGeom(geom);
		MultiPolygon surfaceRoof = RoofDetection.detectRoof(building, 0.2);
		// Util.detectRoof(lOS, 0.2);
		MultiPolygon surfaceWall = Util.detectVertical(lOS, 0.2);
		// Création facade
		WallSurface f = new WallSurface();
		f.setGeometry(surfaceWall);
		List<WallSurface> lF = new ArrayList<>();
		lF.add(f);
		building.setWallSurfaces(lF);
		// Etape 2 : on créé l'emprise du bâtiment
		Polygon footprint = FootprintGenerator.convert(surfaceRoof);
		building.setFootprint(footprint);
		if (footprint != null) {
			// Création toit
			RoofSurface t = RoofSurfaceGenerator.create(surfaceRoof, (Polygon) footprint.copy());
			// Affectation
			building.setRoofSurface(t);
		}
		return building;
	}
}
