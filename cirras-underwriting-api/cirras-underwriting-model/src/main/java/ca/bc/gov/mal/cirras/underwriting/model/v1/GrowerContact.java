package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;

public interface GrowerContact extends Serializable {

	public Integer getGrowerContactId();
	public void setGrowerContactId(Integer growerContactId);

	public Integer getGrowerId();
	public void setGrowerId(Integer growerId);

	public Integer getContactId();
	public void setContactId(Integer contactId);

	public Boolean getIsPrimaryContactInd();
	public void setIsPrimaryContactInd(Boolean isPrimaryContactInd);

	public Boolean getIsActivelyInvolvedInd();
	public void setIsActivelyInvolvedInd(Boolean isActivelyInvolvedInd);
	
	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);

	public String getTransactionType();
	public void setTransactionType(String transactionType);
}
