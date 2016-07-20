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
package fr.ign.cogit.simplu3d.analysis;

import java.util.Collection;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.Road;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundaryType;

/**
 * 
 * Define Road attribute for parcel boundaries
 * 
 * @author MBrasebin
 *
 */
public class AssignRoadToParcelBoundary {

	public static void process(Collection<CadastralParcel> cadastralParcels, IFeatureCollection<Road> roads) {
		NearestRoadFinder roadFinder = new NearestRoadFinder(roads);
		for (CadastralParcel cadastralParcel : cadastralParcels) {
			for (SpecificCadastralBoundary boundary : cadastralParcel.getBoundariesByType(SpecificCadastralBoundaryType.ROAD)) {
				boundary.setRoad(roadFinder.findNearestRoad(boundary.getGeom()));
			}
		}
	}

}
