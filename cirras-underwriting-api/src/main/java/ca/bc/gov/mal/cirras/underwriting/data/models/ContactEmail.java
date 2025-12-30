package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.Date;

public interface ContactEmail extends Serializable {

 	public Integer getContactEmailId();
	public void setContactEmailId(Integer contactEmailId);
 
 	public Integer getContactId();
	public void setContactId(Integer contactId);
 
 	public String getEmailAddress();
	public void setEmailAddress(String emailAddress);
 
 	public Boolean getIsPrimaryEmailInd();
	public void setIsPrimaryEmailInd(Boolean isPrimaryEmailInd);
 
 	public Date getEffectiveDate();
	public void setEffectiveDate(Date effectiveDate);
 
 	public Date getExpiryDate();
	public void setExpiryDate(Date expiryDate);

	public Boolean getIsActive();
	public void setIsActive(Boolean isActive);
	
	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);

	public String getTransactionType();
	public void setTransactionType(String transactionType);
}
