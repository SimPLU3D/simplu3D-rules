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
package fr.ign.cogit.simplu3d.model;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;

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
public class PublicSpace extends DefaultFeature {

	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IPolygon getGeom() {
		return (IPolygon) geom;
	}

	public void setGeom(IPolygon geom) {
		this.geom = geom;
	}

}
