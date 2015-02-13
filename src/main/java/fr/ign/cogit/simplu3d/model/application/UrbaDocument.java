package fr.ign.cogit.simplu3d.model.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class UrbaDocument {
  
  
  public Date approvalDate;
  public Date endDate;
  public String documentType;
  
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
