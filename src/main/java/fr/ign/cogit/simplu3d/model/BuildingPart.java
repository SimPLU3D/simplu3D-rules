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

	private int idBuildind = 0;
	private int idSubPar = 0;


	public SubParcel subParcel;
	
	public BuildingPart(IGeometry geom) {
		super(geom);
	}

	public BuildingPart() {
		super();
	}

	public SubParcel getsP() {
		return subParcel;
	}

	public void setsP(SubParcel sP) {
		this.subParcel = sP;
	}

	public void setIdBuilding(int idBuildind) {
		this.idBuildind = idBuildind;
	}

	public int getIdBuilding() {
		return idBuildind;
	}

	public void setIdSubPar(int idSubPar) {
		this.idSubPar = idSubPar;
	}

	public int getIdSubPar() {
		return idSubPar;
	}

	@Override
	public AbstractBuilding clone() {

		BuildingPart b = new BuildingPart((IGeometry) this.getGeom().clone());
		b.isNew = this.isNew;

		return b;

	}

}
