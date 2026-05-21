/**
 * 
 * This software is released under the licence CeCILL
 * <p>
 * see LICENSE.TXT
 * <p>
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
package fr.ign.cogit.simplu3d.model;

import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;

/**
 * 
 * @author Brasebin Mickaël
 *
 */
public class Building extends AbstractBuilding {
	public Building() {
		super();
	}

	@Override
	public AbstractBuilding clone() {
		Building b = new Building();
		b.setGeometry(this.getGeometry().copy());
		setNew( this.isNew() );
		return b;
	}

	private BasicPropertyUnit bPU;
	public BasicPropertyUnit getbPU() {
		return bPU;
	}

	public void setbPU(BasicPropertyUnit bPU) {
		this.bPU = bPU;
	}
// FIXME think about that
	final static SimpleFeatureType FEATURE_TYPE;

    static {
        try {
            FEATURE_TYPE = DataUtilities.createType(
                    "Building",
					"the_geom:Polygon,"
							+ "id:Integer,"
							+ "name:String"//bpu
            );
        } catch (SchemaException e) {
            throw new RuntimeException(e);
        }
    }
}
