/**
 * 
 *        This software is released under the licence CeCILL
 * <p></p>
 *        see LICENSE.TXT
 * <p></p>
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.common.conversion.FromGeomToSurface;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

/**
 * 
 * Create BasicPropertyUnit as an aggregation of adjacent CadastralParcel's with
 * the same owner
 *  <p></p>
 * warning aggregation is not yet implemented. A BasicPropertyUnit is
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
	private final SpatialRepository<CadastralParcel> cadastralParcels;
	private final GeometryFactory geometryFactory = new GeometryFactory();
	/**
	 * TODO Constructor with a CadastralParcel iterator?
	 * @param cadastralParcels the cadastral parcels used to generate basicproperty unit
	 */
	public BasicPropertyUnitGenerator(SpatialRepository<CadastralParcel> cadastralParcels){
		this.cadastralParcels = cadastralParcels ;
	}

	/**
	 * TODO implement aggregation based on a "ownerId"
	 * @return a collection of basic property units
	 */
	public SpatialRepository<BasicPropertyUnit> createPropertyUnits() {
		Collection<BasicPropertyUnit> collection = new ArrayList<>();
		for (CadastralParcel cadastralParcel : cadastralParcels.findAll()) {
			BasicPropertyUnit bpU = new BasicPropertyUnit();
			// relation with CadastralParcel
			bpU.getCadastralParcels().add(cadastralParcel);
			cadastralParcel.setbPU(bpU);
			// TODO find a better way to identify BasicPropertyUnits
			bpU.setId(cadastralParcel.getId());
			// TODO check if it's possible to use a single setter
			bpU.setPol2D(FromGeomToSurface.convertGeom(cadastralParcel.getGeometry()).get(0));
            List<Polygon> polys = new ArrayList<>(FromGeomToSurface.convertGeom(cadastralParcel.getGeometry()));
			MultiPolygon geom = geometryFactory.createMultiPolygon(polys.toArray(new Polygon[0]));
			bpU.setGeometry(geom);
			collection.add(bpU);
		}
		return new SpatialRepository<>(collection);
	}
}
