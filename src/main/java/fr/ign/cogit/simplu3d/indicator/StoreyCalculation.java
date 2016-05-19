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

import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;

/**
 * 
 * @author MBrasebin
 *
 */
public class StoreyCalculation {

	public static double HAUTEUR_ETAGE = 3;

	public static boolean CONSIDER_ROOF = true;

	public static boolean USE_STOREYS_HEIGH_ATT = false;

	public static int process(AbstractBuilding batiment) {
		Box3D b = new Box3D(batiment.getGeom());
		double zMin = b.getLLDP().getZ();
		double zMax = 0;
		if (CONSIDER_ROOF) {
			zMax = b.getURDP().getZ();
		} else {
			Box3D b2 = new Box3D(batiment.getRoof().getGeom());
			zMax = b2.getLLDP().getZ();
		}

		double hauteurEtage = 0;
		if (USE_STOREYS_HEIGH_ATT) {
			hauteurEtage = batiment.getStoreyHeightsAboveGround();
		}

		if (hauteurEtage < 0.1) {
			hauteurEtage = HAUTEUR_ETAGE;
		}

		int nbEtage = (int) ((zMax - zMin) / hauteurEtage);
		return Math.max(nbEtage, 1);
	}
}
