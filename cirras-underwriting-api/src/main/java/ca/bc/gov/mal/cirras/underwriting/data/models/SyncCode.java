package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.Date;


public interface SyncCode extends Serializable {

 	public String getCodeTableType();
	public void setCodeTableType(String codeTableType);

	public String getUniqueKeyString();
	public void setUniqueKeyString(String uniqueKeyString);
 
 	public Integer getUniqueKeyInteger();
 	public void setUniqueKeyInteger(Integer uniqueKeyInteger);
 
 	public String getDescription();
	public void setDescription(String description);
 
 	public Boolean getIsActive();
	public void setIsActive(Boolean isActive);

	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);

	public String getTransactionType();
	public void setTransactionType(String transactionType);

}
