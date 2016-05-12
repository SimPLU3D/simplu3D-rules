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

import java.util.Date;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.model.citygml.landuse.CG_LandUse;

/**
 * 
 * Une zone d'urbanisme servant de support à la définition de règles.
 * 
 * @see ZONE_URBA http://cnig.gouv.fr/wp-content/uploads/2014/10/
 *      141002_Standard_CNIG_PLU_diffusion.pdf
 * 
 * @author Brasebin Mickaël
 *
 */
public class UrbaZone extends CG_LandUse {

	public final String CLASSE = "Zone";

	/**
	 * Les sous parcelles correspondant à la zone d'urbanisme (les parcelles
	 * peuvent être à cheval sur plusieurs zone d'urbanisme)
	 */
	private IFeatureCollection<SubParcel> subParcels = new FT_FeatureCollection<SubParcel>();

	private String libelle = "";
	private String libelong = "";
	private String typeZone = "";
	private String destdomi = "";
	private String nomFic = "";
	private String urlFic = "";
	private String insee = "";
	private Date dateDeb = null;
	private Date dateFin = null;
	private String text = "";
	private String idPLU;

	// Pour la zone urba
	public UrbaZone(IGeometry geom) {
		super();
		this.setGeom(geom);
	}

	// Pour les sous-parcelles
	public void setSubParcels(IFeatureCollection<SubParcel> subParcels) {
		this.subParcels = subParcels;
	}

	public IFeatureCollection<SubParcel> getSubParcels() {
		return subParcels;
	}

	// Pour le libelle de la zone urba
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getLibelle() {
		return libelle;
	}

	// Pour le libelle long de la zone urba
	public void setLibelong(String libelong) {
		this.libelong = libelong;
	}

	public String getLibelong() {
		return libelong;
	}

	// Pour le type de la zone urba
	public void setTypeZone(String typeZone) {
		this.typeZone = typeZone;
	}

	public String getTypeZone() {
		return typeZone;
	}

	// Pour la vocation de la zone urba
	public void setDestdomi(String destdomi) {
		this.destdomi = destdomi;
	}

	public String getDestdomi() {
		return destdomi;
	}

	// Pour le nom du fichier joint à la zone urba
	public void setNomFic(String nomFic) {
		this.nomFic = nomFic;
	}

	public String getNomFic() {
		return nomFic;
	}

	// Pour l'URL du fichier joint à la zone urba
	public void setUrlFic(String urlFic) {
		this.urlFic = urlFic;
	}

	public String getUrlFic() {
		return urlFic;
	}

	// Pour le code INSEE de la commune de la zone urba
	public void setInsee(String insee) {
		this.insee = insee;
	}

	public String getInsee() {
		return insee;
	}

	// Pour la date d'approbation de la zone urba
	public void setDateDeb(Date date) {
		this.dateDeb = date;
	}

	public Date getDateDeb() {
		return dateDeb;
	}

	// Pour la date de validité de la zone urba
	public void setDateFin(Date date) {
		this.dateFin = date;
	}

	public Date getDateFin() {
		return dateFin;
	}

	// Pour les commentaires sur la zone urba
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	// ID du PLU
	public String getIdPLU() {
		return idPLU;
	}

	public void setIdPLU(String idPLU) {
		this.idPLU = idPLU;
	}

}
