/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;

/**
 * 
 * Calcul de la surface de la facade
 * 
 * @author MBrasebin
 *
 */
public class FacadeArea {

	private double value = 0;

	public FacadeArea(AbstractBuilding b) {
		for (SpecificWallSurface f : b.getFacade()) {
			FacadeArea fA = new FacadeArea(f);
			value = value + fA.getValue();
		}
	}

	public FacadeArea(SpecificWallSurface f) {
		value = Calculation3D.area(FromPolygonToTriangle.convertAndTriangle(f.getLod2MultiSurface().getList()));
	}

	public double getValue() {
		return value;
	}

}
