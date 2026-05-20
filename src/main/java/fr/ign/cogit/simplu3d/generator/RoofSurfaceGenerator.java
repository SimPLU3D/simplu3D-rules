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
 **/
package fr.ign.cogit.simplu3d.generator;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.model.RoofSurface;
import fr.ign.common.analysis.roof.ClassifyRoof;
import fr.ign.common.analysis.building.DetectPignon;
import fr.ign.common.conversion.FromPolygonToLineString;
import org.locationtech.jts.geom.*;

/**
 * Create a RoofSurface from MultiPolygon
 *  
 * @author MBrasebin
 *
 */
public class RoofSurfaceGenerator {
	private static final double epsilonAngle = 0.15;
	private static final double epsilonDist = 1;
	/**
	 * 
	 * Create a RoofSurface from polygons corresponding to the Roof.
	 * RoofDetection.detectRoof to extract roof polygons from building geometry
	 * 
	 * @param surfaceRoof a 3D MultiSurface 
	 * @param emprise the 2D footprint of the building
	 * @return a new RoofSurface object
	 */
	public static RoofSurface create(MultiPolygon surfaceRoof, Polygon emprise) {
		RoofSurface t = new RoofSurface();
		t.setGeometry(surfaceRoof);
		t.setGutter(
				affectZEmprise(emprise,
				FromPolygonToLineString.convertMultiPolygonToLineStrings(surfaceRoof), 0.5)
		);
		ClassifyRoof cR = new ClassifyRoof(t, epsilonAngle, epsilonDist);
		LineString[] lineStrings = cR.getInteriorLineStrings().toArray(new LineString[0]);
		// On affecte les curves
		MultiLineString iMS = surfaceRoof.getFactory().createMultiLineString(lineStrings);
		t.setInteriorEdge(iMS);
		LineString[] lineStrings2 = cR.getFaitage(epsilonAngle).toArray(new LineString[0]);
		MultiLineString iMS2 = surfaceRoof.getFactory().createMultiLineString(lineStrings2);
		t.setRoofing(iMS2);
		MultiLineString ext = (MultiLineString) t.getGutter().copy();
		LineString[] lPignons = DetectPignon.detectPignon(ext, t.getRoofing(), 0.2, epsilonAngle).toArray(new LineString[0]);
		MultiLineString pignons = surfaceRoof.getFactory().createMultiLineString(lPignons);
		t.setGable(pignons);
		if (cR.getExteriorLineStrings() != null && !cR.getExteriorLineStrings().isEmpty()) {
			t.setGutter(surfaceRoof.getFactory().createMultiLineString(cR.getExteriorLineStrings().toArray(new LineString[0])));
		}
		return t;
	}

	public static MultiLineString affectZEmprise(Polygon poly, List<LineString> iMC, double threshold) {
		int dpPoly = poly.getCoordinates().length;
		List<LineString> lMC = new ArrayList<>();
		for (int i = 0; i < dpPoly - 1; i++) {
			Coordinate dp1 = poly.getCoordinates()[i];
			Coordinate dp2 = poly.getCoordinates()[i+1];
			for (LineString c : iMC) {
				Coordinate[] dplTemp = c.getCoordinates();
				boolean equals1 = dplTemp[0].equals2D(dp1, threshold) && dplTemp[1].equals2D(dp2, threshold);
				boolean equals2 = dplTemp[0].equals2D(dp2, threshold) && dplTemp[1].equals2D(dp1, threshold);
				if (equals1 || equals2) {
					Coordinate[] dpTempTemp = new Coordinate[]{dplTemp[0], dplTemp[1]};
					lMC.add(poly.getFactory().createLineString(dpTempTemp));
				}
			}
		}
		return poly.getFactory().createMultiLineString(lMC.toArray(new LineString[0]));
	}
}
