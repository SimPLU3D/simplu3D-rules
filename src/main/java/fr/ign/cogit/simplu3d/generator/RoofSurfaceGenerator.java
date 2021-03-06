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
 **/
package fr.ign.cogit.simplu3d.generator;

import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.sig3d.analysis.roof.ClassifyRoof;
import fr.ign.cogit.geoxygene.sig3d.analysis.roof.DetectPignon;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToLineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.model.RoofSurface;

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
	 * Create a RoofSurface from polygons corresponding to the Roof
	 * 
	 * RoofDetection.detectRoof to extract roof polygons from building geometry
	 * 
	 * @param surfaceRoof a 3D MultiSurface 
	 * @param emprise the 2D footprint of the building
	 * @return a new RoofSurface object
	 */
	@SuppressWarnings("unchecked")
	public static RoofSurface create(IMultiSurface<IOrientableSurface> surfaceRoof, IPolygon emprise) {

		RoofSurface t = new RoofSurface();
		t.setGeom(surfaceRoof);

		t.setGutter(affectZEmprise(emprise,
				FromPolygonToLineString.convertListPolToLineStrings((IMultiSurface<IOrientableSurface>) surfaceRoof.clone()),
				0.5));

		//
		ClassifyRoof cR = new ClassifyRoof(t, epsilonAngle, epsilonDist);

		List<ILineString> lLS = cR.getInteriorLineStrings();
		// On affecte les curves
		IMultiCurve<IOrientableCurve> iMS = new GM_MultiCurve<IOrientableCurve>();
		iMS.addAll(lLS);
		t.setInteriorEdge(iMS);

		IMultiCurve<IOrientableCurve> iMS2 = new GM_MultiCurve<IOrientableCurve>();
		iMS2.addAll(cR.getFaitage(epsilonAngle));

		t.setRoofing(iMS2);

		IMultiCurve<IOrientableCurve> ext = new GM_MultiCurve<IOrientableCurve>();
		ext.addAll(t.getGutter());

		// ILineString lS = Operateurs.union(lLSExt);

		// IPolygon poly = new GM_Polygon(
		// Filtering.DouglasPeuckerLineString(lS,
		// 0.2));

		// List<ILineString> lPignons = DetectPignon.detectPignon( ext,
		// epsilonAngle);
		List<ILineString> lPignons = DetectPignon.detectPignon(ext, t.getRoofing(), 0.2, epsilonAngle);

		IMultiCurve<IOrientableCurve> pignons = new GM_MultiCurve<IOrientableCurve>();
		pignons.addAll(lPignons);
		t.setGable(pignons);
		if (cR.getExteriorLineStrings() != null && !cR.getExteriorLineStrings().isEmpty()) {
			t.setGutter(new GM_MultiCurve<>(cR.getExteriorLineStrings()));
		}

		// IFeatureCollection<IFeature> fC = DetectPignon.detectPignon(poly
		// /*lLSExt*/, lLS , epsilonDist,epsilonAngle)

		return t;

	}

	public static IMultiCurve<IOrientableCurve> affectZEmprise(IPolygon poly, List<ILineString> iMC, double threshold) {

		int dpPoly = poly.coord().size();
		IMultiCurve<IOrientableCurve> mC = new GM_MultiCurve<IOrientableCurve>();

		bouclea: for (int i = 0; i < dpPoly - 1; i++) {

			IDirectPosition dp1 = poly.coord().get(i);
			IDirectPosition dp2 = poly.coord().get(i + 1);

			for (IOrientableCurve c : iMC) {

				IDirectPositionList dplTemp = c.coord();

				if (dplTemp.get(0).equals2D(dp1, threshold) && dplTemp.get(1).equals2D(dp2, threshold)) {

					IDirectPositionList dpTempTemp = new DirectPositionList();
					dpTempTemp.add((IDirectPosition) dplTemp.get(0).clone());
					dpTempTemp.add((IDirectPosition) dplTemp.get(1).clone());
					mC.add(new GM_LineString(dpTempTemp));

					continue bouclea;
				}

				if (dplTemp.get(0).equals2D(dp2, threshold) && dplTemp.get(1).equals2D(dp1, threshold)) {

					IDirectPositionList dpTempTemp = new DirectPositionList();
					dpTempTemp.add((IDirectPosition) dplTemp.get(0).clone());
					dpTempTemp.add((IDirectPosition) dplTemp.get(1).clone());
					mC.add(new GM_LineString(dpTempTemp));
					continue bouclea;
				}

			}

		}
		/*
		 * 
		 * IDirectPositionList dpl = iMC.coord(); IPolygon polyOut = (IPolygon)
		 * poly.clone();
		 * 
		 * 
		 * IDirectPositionList dpl2 = polyOut.coord();
		 * 
		 * for(IDirectPosition dp:dpl2){
		 * 
		 * Proximity p = new Proximity(); IDirectPosition dpT= p.nearest(dp,
		 * dpl);
		 * 
		 * 
		 * dp.setZ(dpT.getZ());
		 * 
		 * 
		 * 
		 * }
		 */

		return mC;

	}

}
