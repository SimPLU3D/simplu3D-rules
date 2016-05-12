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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * Un document d'urbanisme porteur d'une liste de zone d'urbanisme.
 * 
 * @see DOC_URBA http://cnig.gouv.fr/wp-content/uploads/2014/10/141002_Standard_CNIG_PLU_diffusion.pdf
 * 
 * @author Brasebin Mickaël
 *
 */
public class UrbaDocument {
	/**
	 * Date d'approbation du document (DOC_URBA.DATAPPRO)
	 */
	public Date approvalDate;
	/**
	 * Date de fin de validité (DOC_URBA.DATEFIN)
	 */
	public Date endDate;
	/**
	 * Type de document d'urbanisme (DOC_URBA.TYPEDOC)
	 * TODO ENUM PLU, POS, PSMV
	 */
	public String documentType;
	/**
	 * Liste des zones d'urbanismes
	 * TODO renommer en urbaZones
	 */
	public List<UrbaZone> lUrbaZone = new ArrayList<UrbaZone>();

	public UrbaDocument() {
		super();
	}

	public UrbaDocument(Date approvalDate, Date endDate, String documentType) {
		super();
		this.approvalDate = approvalDate;
		this.endDate = endDate;
		this.documentType = documentType;
	}

	public List<UrbaZone> getlUrbaZone() {
		return lUrbaZone;
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

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

}
