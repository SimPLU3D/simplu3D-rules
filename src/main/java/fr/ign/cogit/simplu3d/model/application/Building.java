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
package fr.ign.cogit.simplu3d.model.application;

import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;

/**
 * 
 * @author Brasebin Mickaël
 *
 */
public class Building extends AbstractBuilding {

	private int idVersion = 0;

	public Building() {
		super();
	}

	public Building(IGeometry geom) {
		super(geom);
	}

	public void setIdBuilding(int idBuilding) {
		this.id = id;
	}

	public int getIdBuilding() {
		return id;
	}

	public void setIdVersion(int IdVersion) {
		this.idVersion = IdVersion;
	}

	public int getIdVersion() {
		return idVersion;
	}


	@Override
	public AbstractBuilding clone() {
		Building b = new Building((IGeometry) this.getGeom().clone());
		b.isNew = this.isNew;
		return b;
	}


}
