package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;

public interface Contact extends Serializable {

 	public Integer getContactId();
	public void setContactId(Integer contactId);
 
 	public String getFirstName();
	public void setFirstName(String firstName);
 
 	public String getLastName();
	public void setLastName(String lastName);
	
	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);

	public String getTransactionType();
	public void setTransactionType(String transactionType);
}
