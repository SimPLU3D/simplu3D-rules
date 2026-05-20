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

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

/**
 * 
 * This theme refers to various objects that represent public zones. Some
 * constraints are expressed in relation with public zones according to its type
 * (i.e., public garden, channels, rail track zones). Public roads are a
 * particular type of public space, it is represented by a surface and has a
 * type according to administration typologies (i.e., channel, public garden,
 * public place).
 * 
 * @author Brasebin Mickaël
 *
 */
public class PublicSpace implements UrbanFeature {
	private Geometry geometry = null;
	public Geometry getGeometry() {
		return this.geometry;
	}
	public void setGeometry(Geometry geom) {
		this.geometry = geom;
	}
	private String id;
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}

	private String type;

	public String getPublicSpaceType() {
		return type;
	}

	public void setPublicSpaceType(String type) {
		this.type = type;
	}

	public Polygon getGeom() {
		return (Polygon) this.getGeometry();
	}

	public void setGeom(Polygon geom) {
		this.setGeometry(geom);
	}

}
