/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
package fr.ign.cogit.simplu3d.reader;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.sig3d.calculation.Proximity;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.equation.ApproximatedPlanEquation;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.model.Road;

/**
 * Reader for ROAD.shp
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class RoadReader extends AbstractReader<Road> {

	public static String ATT_NOM_RUE = "nom_rue_g";
	public static String ATT_LARGEUR = "largeur";
	public static String ATT_TYPE = "nature";


	@Override
	public Road read(IFeature feature) {
		Road result = new Road();
		result.setName(readStringAttribute(feature, ATT_NOM_RUE));
		
		Double width = readDoubleAttribute(feature, ATT_LARGEUR);
		if ( width == null ){
			return null;
		}
		result.setWidth(width);

		List<String> usages = new ArrayList<String>();
		usages.add(readStringAttribute(feature, ATT_TYPE));
		result.setUsage(usages);

		// read axis...
		IGeometry geom = feature.getGeom();

		IMultiCurve<ILineString> axe = null;

		if (geom instanceof ILineString) {
			ILineString c = (ILineString) geom;
			axe = new GM_MultiCurve<ILineString>();
			axe.add(c);
		} else if (geom instanceof IMultiCurve<?>) {
			axe = (IMultiCurve<ILineString>) geom;
		}

		if (axe == null) {
			throw new RuntimeException("Error in Voirie Importer axe is not a ILineString");
		}

		result.setAxe(axe);

		// build surface geometry...

		// TODO epsilon check?
		if (result.getWidth() == 0.0 ) {
			throw new RuntimeException("Unsupported Road "+ATT_LARGEUR+" (0.0) for feature "+feature.getId());
		}

		IGeometry obj = geom.buffer(result.getWidth());
		IDirectPositionList dpl = obj.coord();
		IDirectPositionList dplRoute = feature.getGeom().coord();

		int nbDPL = dpl.size();

		for (int i = 0; i < nbDPL; i++) {
			Proximity c = new Proximity();
			IDirectPosition dp = dpl.get(i);

			c.nearest(dp, dplRoute);
			dp.setZ(c.nearest.getZ());
		}

		ApproximatedPlanEquation eq = new ApproximatedPlanEquation(dpl);

		Vecteur normal = eq.getNormale();

		if (normal.getZ() < 0) {
			dpl.inverseOrdre();
		}

		IMultiSurface<IOrientableSurface> surfVoie = FromGeomToSurface.convertMSGeom(obj);

		result.setLod2MultiSurface(surfVoie);
		result.setGeom(surfVoie);

		return result;
	}

}
