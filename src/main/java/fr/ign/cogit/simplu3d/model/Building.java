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

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.Geometry;

/**
 * 
 * @author Brasebin Mickaël
 *
 */
public class Building extends AbstractBuilding {
	public Building() {
		super();
	}

/*
	@Deprecated
	public Building(Geometry geom) {
		super(geom);
	}
*/

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

    /**
	 * Converts this domain object to a GeoTools SimpleFeature.
	 */
	public SimpleFeature toFeature() {
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(FEATURE_TYPE);
		builder.set("the_geom", this.getGeometry());
		builder.set("id", this.getId());
		// Set other attributes...
//FIXME
		// Generate a unique feature ID (required?)
		String featureId = "building_" + this.getId();
		return builder.buildFeature(featureId);
	}

	/**
	 * Static factory method to create a Building from a SimpleFeature.
	 */
	public static Building fromFeature(SimpleFeature feature) {
		int id = (int) feature.getAttribute("id");
		Geometry geom = (Geometry) feature.getDefaultGeometry();
		// Extract other attributes...

		Building b = new Building();
		b.setGeometry(geom/* resolve relationships */);
		return b;
	}
}
