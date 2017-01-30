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

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;

/**
 * 
 * CityGML - Une partie de bâtiment formant un AbstractBuilding
 * 
 * @author Brasebin Mickaël
 *
 */
public class BuildingPart extends AbstractBuilding {

	public SubParcel subParcel;

	public BuildingPart() {
		super();
	}
	
	@Deprecated
	public BuildingPart(IGeometry geom) {
		super(geom);
	}

	public SubParcel getSubParcel() {
		return subParcel;
	}

	public void setSubParcel(SubParcel subParcel) {
		this.subParcel = subParcel;
	}

	@Override
	public AbstractBuilding clone() {
		BuildingPart b = new BuildingPart((IGeometry) this.getGeom().clone());
		b.generated = this.generated;
		return b;
	}

}
