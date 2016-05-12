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

import fr.ign.cogit.geoxygene.sig3d.model.citygml.building.CG_WallSurface;

/**
 * 
 * @author Brasebin Mickaël
 *
 */
public class SpecificWallSurface extends CG_WallSurface {

	public final static String FOND = "FOND";
	public final static String LATERAL = "LAT";
	public final static String VOIE = "VOIE";

	//TODO enum
	public String type;
	public int idBuildPart;
	public Materiau material;
	public boolean isWindowLess;
	public double length;

	public Materiau getMat() {
		return material;
	}

	public void setMat(Materiau mat) {
		this.material = mat;
	}

	public int getIdBuildPart() {
		return idBuildPart;
	}

	public void setIdBuildPart(int idBuildPart) {
		this.idBuildPart = idBuildPart;
	}

	public SpecificWallSurface() {
		super();
	}

	public SpecificWallSurface(String type, boolean isWindowLess) {
		super();
		this.type = type;
		this.isWindowLess = isWindowLess;
	}

	public boolean isWindowLess() {
		return isWindowLess;
	}

	public void setAveugle(boolean isWindowLess) {
		this.isWindowLess = isWindowLess;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
