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

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;

/**
 * 
 * Frontière de parcelle
 * 
 * 
 * Note that SpecificCadastralBoundary forms a topology map
 * 
 * @author Brasebin Mickaël
 *
 */
public class SpecificCadastralBoundary extends DefaultFeature {
	/**
	 * The type of the boundary
	 */
	private SpecificCadastralBoundaryType type;
	/**
	 * The side of the boundary (relative to the parcel)
	 */
	private SpecificCadastralBoundarySide side;
	private Alignement alignement = null;
	private Recoil recoil = null;

	/**
	 * Il s'agit de l'objet qui ne référence pas cette bordure et qui est adjacent
	 * à la bordure
	 * TODO split to Road and CadastralParcel
	 */
	private IFeature featAdj = null;

	public SpecificCadastralBoundary(){
		
	}
	
	public SpecificCadastralBoundary(IGeometry geom) {
		this.setGeom(geom);
	}
	
	public IFeature getFeatAdj() {
		return featAdj;
	}

	public void setFeatAdj(IFeature featAdj) {
		this.featAdj = featAdj;
	}

	public Alignement getAlignement() {
		return alignement;
	}

	public void setAlignement(Alignement alignement) {
		this.alignement = alignement;
	}

	public Recoil getRecoil() {
		return recoil;
	}

	public void setRecoil(Recoil recoil) {
		this.recoil = recoil;
	}

	public SpecificCadastralBoundaryType getType() {
		return type;
	}

	public void setType(SpecificCadastralBoundaryType type) {
		this.type = type;
	}

	/**
	 * @return the side
	 */
	public SpecificCadastralBoundarySide getSide() {
		return side;
	}
	/**
	 * @param side
	 *            the side to set
	 */
	public void setSide(SpecificCadastralBoundarySide side) {
		this.side = side;
	}


}
