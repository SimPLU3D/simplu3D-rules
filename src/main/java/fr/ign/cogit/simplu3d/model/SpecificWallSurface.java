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

import fr.ign.cogit.geoxygene.sig3d.model.citygml.building.CG_WallSurface;

/**
 * 
 * @author Brasebin Mickaël
 *
 */
public class SpecificWallSurface extends CG_WallSurface {

	public enum SpecificWallSurfaceType {
		BOT(0), LAT(1), UNKNOWN(99), ROAD(2);

		private int value;

		private SpecificWallSurfaceType(int type) {
			value = type;
		}

		public int getValueType() {
			return value;
		}

		public static SpecificWallSurfaceType getTypeFromInt(int type) {
			SpecificWallSurfaceType[] val = SpecificWallSurfaceType.values();
			for (int i = 0; i < val.length; i++) {
				if (val[i].getValueType() == type) {
					return val[i];
				}
			}

			return null;
		}

	}

	public SpecificWallSurfaceType type;
	public int idBuildPart;
	public Materiau material;
	public boolean isWindowLess;
	public double length;

	public SpecificWallSurface() {
		super();
	}

	public SpecificWallSurface(SpecificWallSurfaceType type, boolean isWindowLess) {
		super();
		this.type = type;
		this.isWindowLess = isWindowLess;
	}

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

	public boolean isWindowLess() {
		return isWindowLess;
	}

	public void setWindowLess(boolean isWindowLess) {
		this.isWindowLess = isWindowLess;
	}

	public SpecificWallSurfaceType getType() {
		return type;
	}

	public void setType(SpecificWallSurfaceType type) {
		this.type = type;
	}

}
