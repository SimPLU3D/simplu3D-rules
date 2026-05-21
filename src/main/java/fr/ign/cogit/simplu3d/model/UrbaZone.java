/**
 * 
 * This software is released under the licence CeCILL
 * <p>
 * see LICENSE.TXT
 * <p>
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static fr.ign.cogit.simplu3d.io.feature.UrbaZoneReader.*;

/**
 *
 * Une zone d'urbanisme servant de support à la définition de règles.
 * <p>
 * see ZONE_URBA <a href="http://cnig.gouv.fr/wp-content/uploads/2014/10/">...</a>
 *      141002_Standard_CNIG_PLU_diffusion.pdf
 *
 * @author Brasebin Mickaël
 *
 */
//@Entity
public class UrbaZone extends AbstractUrbanFeature {
	/**
	 * Les sous parcelles correspondant à la zone d'urbanisme (les parcelles
	 * peuvent être à cheval sur plusieurs zone d'urbanisme)
	 */
	private List<SubParcel> subParcels = new ArrayList<>();

	//private String libelle = "";
	//private String libelong = "";
	//private String typeZone = "";
	//private String destdomi = "";
	//private String nomFic = "";
	//private String urlFic = "";
	//private String insee = "";
	// TODO rename DATAPPRO
	//private Date dateDeb = null;
	// TODO rename DATVALID
	//private Date dateFin = null;
	//private String text = "";
	private IZoneRegulation zoneRegulation = null;

	// Pour la zone urba
	public UrbaZone() {
		super();
	}
/*
	@Override
	@Type(type = "fr.ign.cogit.geoxygene.datatools.hibernate.GeOxygeneGeometryUserType")
*/

//	@Transient
	public List<SubParcel> getSubParcels() {
		return subParcels;
	}

	public void setSubParcels(List<SubParcel> subParcels) {
		this.subParcels = subParcels;
	}

	public void setLibelle(String libelle) {
		this.setAttribute(ATT_LIBELLE, libelle);
		//this.libelle = libelle;
	}

	public String getLibelle() {
		return this.getStringAttribute(ATT_LIBELLE);
	}

	// Pour le libelle long de la zone urba
	public void setLibelong(String libelong) {
		this.setAttribute(ATT_LIBELONG, libelong);
		//this.libelong = libelong;
	}

	public String getLibelong() {
		return this.getStringAttribute(ATT_LIBELONG);
	}

	// Pour le type de la zone urba
	public void setTypeZone(String typeZone) {
		this.setAttribute(ATT_TYPE_ZONE, typeZone);
		//this.typeZone = typeZone;
	}

	public String getTypeZone() {
		return this.getStringAttribute(ATT_TYPE_ZONE);
	}

	// Pour la vocation de la zone urba
	public void setDestdomi(String destdomi) {
		this.setAttribute(ATT_DESTDOMI, destdomi);
		//this.destdomi = destdomi;
	}

	public String getDestdomi() {
		return this.getStringAttribute(ATT_DESTDOMI);
	}

	// Pour le nom du fichier joint à la zone urba
	public void setNomFic(String nomFic) {
		this.setAttribute(ATT_NOMFIC, nomFic);
		//this.nomFic = nomFic;
	}

	public String getNomFic() {
		return this.getStringAttribute(ATT_NOMFIC);
	}

	// Pour l'URL du fichier joint à la zone urba
	public void setUrlFic(String urlFic) {
		this.setAttribute(ATT_URLFIC, urlFic);
		//this.urlFic = urlFic;
	}

	public String getUrlFic() {
		return this.getStringAttribute(ATT_URLFIC);
	}

	// Pour le code INSEE de la commune de la zone urba
	public void setInsee(String insee) {
		this.setAttribute(ATT_INSEE, insee);
		//this.insee = insee;
	}

	public String getInsee() {
		return this.getAttribute(ATT_INSEE).toString();
	}

	// Pour la date d'approbation de la zone urba
	public void setDateApprobation(Date date) {
		this.setAttribute(VALIDITY_DATE_APPRO, date);
		//this.dateDeb = date;
	}

	public Date getDateApprobation() {
		return (Date) this.getAttribute(VALIDITY_DATE_APPRO);
	}

	// Pour la date de validité de la zone urba
	public void setDateValidite(Date date) {
		this.setAttribute(VALIDITY_DATE_VALID, date);
		//this.dateFin = date;
	}

	public Date getDateValidite() {
		return (Date) this.getAttribute(VALIDITY_DATE_VALID);
	}

	// Pour les commentaires sur la zone urba
	public String getText() {
		return this.getStringAttribute(ATT_TEXT);
	}

	public void setText(String text) {
		this.setAttribute(ATT_TEXT, text);
	}

	public IZoneRegulation getZoneRegulation() {
		return zoneRegulation;
	}

	public void setZoneRegulation(IZoneRegulation zoneRegulation) {
		this.zoneRegulation = zoneRegulation;
	}
}
