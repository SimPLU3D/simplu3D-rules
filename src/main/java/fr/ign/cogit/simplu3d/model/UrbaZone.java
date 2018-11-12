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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;

/**
 * 
 * Une zone d'urbanisme servant de support à la définition de règles.
 * 
 * see ZONE_URBA http://cnig.gouv.fr/wp-content/uploads/2014/10/
 *      141002_Standard_CNIG_PLU_diffusion.pdf
 * 
 * @author Brasebin Mickaël
 *
 */
@Entity
public class UrbaZone extends DefaultFeature {

	/**
	 * Les sous parcelles correspondant à la zone d'urbanisme (les parcelles
	 * peuvent être à cheval sur plusieurs zone d'urbanisme)
	 */
	private List<SubParcel> subParcels = new ArrayList<SubParcel>();

	private String libelle = "";
	private String libelong = "";
	private String typeZone = "";
	private String destdomi = "";
	private String nomFic = "";
	private String urlFic = "";
	private String insee = "";
	// TODO rename DATAPPRO
	private Date dateDeb = null;
	// TODO rename DATVALID
	private Date dateFin = null;
	private String text = "";
	private IZoneRegulation zoneRegulation = null;


	// Pour la zone urba
	public UrbaZone() {
		super();
	}

	@Override
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	
	@Override
	@Type(type = "fr.ign.cogit.geoxygene.datatools.hibernate.GeOxygeneGeometryUserType")
	public IGeometry getGeom() {
		return geom;
	}

	@Transient
	public List<SubParcel> getSubParcels() {
		return subParcels;
	}

	public void setSubParcels(List<SubParcel> subParcels) {
		this.subParcels = subParcels;
	}


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
	

	public IZoneRegulation getZoneRegulation() {
		return zoneRegulation;
	}

	public void setZoneRegulation(IZoneRegulation zoneRegulation) {
		this.zoneRegulation = zoneRegulation;
	}

}
