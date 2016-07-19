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
package fr.ign.cogit.simplu3d.generator;

import java.util.Collection;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;

/**
 * 
 * Create BasicPropertyUnit as an aggregation of adjacent CadastralParcel's with
 * the same owner
 *  
 * @warning aggregation is not yet implemented. A BasicPropertyUnit is
 *          associated to a CadastralParcel
 * 
 * 
 * 
 * @author MBrasebin
 *
 */
public class BasicPropertyUnitGenerator {
	
	/**
	 * 
	 */
	private Collection<CadastralParcel> cadastralParcels;

	/**
	 * TODO Constructor with a CadastralParcel iterator?
	 * @param cadastralParcels
	 */
	public BasicPropertyUnitGenerator(Collection<CadastralParcel> cadastralParcels){
		this.cadastralParcels = cadastralParcels ;
	}

	/**
	 * TODO implement aggregation based on a "owerId"
	 * @param cadastralParcels
	 * @return
	 */
	public IFeatureCollection<BasicPropertyUnit> createPropertyUnits() {

		IFeatureCollection<BasicPropertyUnit> result = new FT_FeatureCollection<BasicPropertyUnit>();

		for (CadastralParcel cadastralParcel : cadastralParcels) {
			BasicPropertyUnit bpU = new BasicPropertyUnit();
			
			// relation with CadastralParcel
			bpU.getCadastralParcels().add(cadastralParcel);
			cadastralParcel.setbPU(bpU);

			// TODO find a better way to identify BasicPropertyUnits
			bpU.setId(cadastralParcel.getId());
			
			// TODO check if it's possible to use a single setter
			bpU.setpol2D((IPolygon) FromGeomToSurface.convertGeom(cadastralParcel.getGeom()).get(0));

			IMultiSurface<IOrientableSurface> geom = new GM_MultiSurface<>();
			geom.addAll(FromGeomToSurface.convertGeom(cadastralParcel.getGeom()));
			bpU.setGeom(geom);
			
			result.add(bpU);
		}

		return result;
	}

}
