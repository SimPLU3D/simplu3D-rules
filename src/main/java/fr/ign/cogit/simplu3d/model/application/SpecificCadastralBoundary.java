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

import org.citygml4j.model.citygml.core.CityObject;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.model.citygml.core.CG_CityObject;

/**
 * 
 * Frontière de parcelle
 * 
 * @author Brasebin Mickaël
 *
 */
public class SpecificCadastralBoundary extends CG_CityObject {

	public enum SpecificCadastralBoundaryType{
		BOT(0),
		LAT(1),
		UNKNOWN(99),
		INTRA(3),
		ROAD(4),
		LATERAL_TEMP(98),
		PUB(5);
		
		private int value;
		
		private SpecificCadastralBoundaryType(int type){
			value = type;
		}
		public int getValueType(){
			return value;
		}
		
		public static SpecificCadastralBoundaryType getTypeFromInt(int type){
			SpecificCadastralBoundaryType[] val = SpecificCadastralBoundaryType.values();
			for(int i=0; i <val.length; i++){
				if(val[i].getValueType() == type)
				{
					return val[i];
				}
			}
			
			return null;
		}
		
	}
	
	public enum SpecificCadastralBoundarySide{
		LEFT(0),
		RIGHT(1),
		UNKNOWN(99);
		
		private int value;
		
		private SpecificCadastralBoundarySide(int type){
			value = type;
		}
		public int getValueType(){
			return value;
		}
		
		public static SpecificCadastralBoundarySide getTypeFromInt(int type){
			SpecificCadastralBoundarySide[] val = SpecificCadastralBoundarySide.values();
			for(int i=0; i <val.length; i++){
				if(val[i].getValueType() == type)
				{
					return val[i];
				}
			}
			
			return null;
		}
	}
	
	private SpecificCadastralBoundaryType type;
	private SpecificCadastralBoundarySide side;
	


	public Alignement alignement = null;
	public Recoil recoil = null;
	public int id_subpar;
	public int id_adj;
	public String tabRef = "";

	// Il s'agit de l'objet qui ne référence pas cette bordure et qui est
	// adjacent
	// à la bordure
	private IFeature featAdj = null;

	public SpecificCadastralBoundary(IGeometry geom) {
		this.setGeom(geom);
	}
	
	public IFeature getFeatAdj() {
		return featAdj;
	}

	public void setFeatAdj(IFeature featAdj) {
		this.featAdj = featAdj;
	}




	@Override
	public CityObject export() {

		return null;
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

	public int getIdSubPar() {
		return id_subpar;
	}

	public void setIdSubPar(int id_subpar) {
		this.id_subpar = id_subpar;
	}

	public int getIdAdj() {
		return id_adj;
	}

	public void setIdAdj(int id_adj) {
		this.id_adj = id_adj;
	}

	public String getTableRef() {
		return tabRef;
	}

	public void setTableRef(String tabRef) {
		this.tabRef = tabRef;
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
