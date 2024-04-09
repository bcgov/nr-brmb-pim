package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;

public interface ContactPhone extends Serializable {

 	public Integer getContactPhoneId();
	public void setContactPhoneId(Integer contactPhoneId);
 
 	public Integer getContactId();
	public void setContactId(Integer contactId);
 
 	public String getPhoneNumber();
	public void setPhoneNumber(String phoneNumber);
 
 	public String getExtension();
	public void setExtension(String extension);
 
 	public Boolean getIsPrimaryPhoneInd();
	public void setIsPrimaryPhoneInd(Boolean isPrimaryPhoneInd);
 
 	public String getTelecomTypeCode();
	public void setTelecomTypeCode(String telecomTypeCode);
 
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
