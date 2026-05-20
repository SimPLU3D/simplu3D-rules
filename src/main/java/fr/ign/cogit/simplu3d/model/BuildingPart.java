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

/*
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
*/

import org.locationtech.jts.geom.Geometry;

/**
 * 
 * CityGML - Une partie de bâtiment formant un AbstractBuilding
 * 
 * @author Brasebin Mickaël
 *
 */
public class BuildingPart extends AbstractBuilding {

	private SubParcel subParcel;

	public BuildingPart() {
		super();
	}
	
/*
	@Deprecated
	public BuildingPart(Geometry geom) {
		super(geom);
	}
*/

	public SubParcel getSubParcel() {
		return subParcel;
	}

	public void setSubParcel(SubParcel subParcel) {
		this.subParcel = subParcel;
	}

	@Override
	public AbstractBuilding clone() {
		BuildingPart b = new BuildingPart();
		b.setGeometry(this.getGeometry().copy());
		b.setNew(this.isNew());
		return b;
	}

}
