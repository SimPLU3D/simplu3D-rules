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
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * Un document d'urbanisme porteur d'une liste de zone d'urbanisme.
 * 
 * @see DOC_URBA http://cnig.gouv.fr/wp-content/uploads/2014/10/
 *      141002_Standard_CNIG_PLU_diffusion.pdf
 * 
 * @author Brasebin Mickaël
 *
 */
@Entity
public class UrbaDocument {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id ;
	
	private String idUrba = "";
	private String typeDoc = "";
	private Date dateAppro = null;
	private Date dateFin = null;
	private String interCo = "";
	private String siren = "";
	private String etat = "";
	private String nomReg = "";
	private String urlReg = "";
	private String nomPlan = "";
	private String urlPlan = "";
	private String siteWeb = "";
	private String typeRef = "";
	private Date dateRef = null;
	private List<UrbaZone> zones = new ArrayList<>();


	/**
	 * Date d'approbation du document (DOC_URBA.DATAPPRO)
	 */
	private Date approvalDate;
	/**
	 * Date de fin de validité (DOC_URBA.DATEFIN)
	 */
	private Date endDate;
	/**
	 * Type de document d'urbanisme
	 */
	private UrbaDocumentType documentType;

	public UrbaDocument() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public UrbaDocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(UrbaDocumentType documentType) {
		this.documentType = documentType;
	}

	public void setIdUrba(String idUrba) {
		this.idUrba = idUrba;
	}

	public String getIdUrba() {
		return idUrba;
	}

	public void setTypeDoc(String typeDoc) {
		this.typeDoc = typeDoc;
	}

	public String getTypeDoc() {
		return typeDoc;
	}

	public void setDateAppro(Date date) {
		this.dateAppro = date;
	}

	public Date getDateAppro() {
		return dateAppro;
	}

	public void setDateFin(Date date) {
		this.dateFin = date;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setInterCo(String interCo) {
		this.interCo = interCo;
	}

	public String getInterCo() {
		return interCo;
	}

	public void setSiren(String siren) {
		this.siren = siren;
	}

	public String getSiren() {
		return siren;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getEtat() {
		return etat;
	}

	public void setNomReg(String nomReg) {
		this.nomReg = nomReg;
	}

	public String getNomReg() {
		return nomReg;
	}

	public void setUrlReg(String urlreg) {
		this.urlReg = urlreg;
	}

	public String getUrlReg() {
		return urlReg;
	}

	public void setNomPlan(String nomPlan) {
		this.nomPlan = nomPlan;
	}

	public String getNomPlan() {
		return nomPlan;
	}

	public void setUrlPlan(String urlPlan) {
		this.urlPlan = urlPlan;
	}

	public String getUrlPlan() {
		return urlPlan;
	}

	public void setSiteWeb(String siteWeb) {
		this.siteWeb = siteWeb;
	}

	public String getSiteWeb() {
		return siteWeb;
	}

	public void setTypeRef(String typeRef) {
		this.typeRef = typeRef;
	}

	public String getTypeRef() {
		return typeRef;
	}

	public void setDateRef(Date dateRef) {
		this.dateRef = dateRef;
	}

	public Date getDateRef() {
		return dateRef;
	}
	
	
	public List<UrbaZone> getZones() {
		return zones;
	}

	public void setZones(List<UrbaZone> zones) {
		this.zones = zones;
	}


}
