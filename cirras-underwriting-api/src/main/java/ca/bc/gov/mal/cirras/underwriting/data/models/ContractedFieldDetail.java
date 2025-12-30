package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

public interface ContractedFieldDetail extends Serializable {

	public Integer getContractedFieldDetailId();
	public void setContractedFieldDetailId(Integer contractedFieldDetailId);
	
	public Integer getAnnualFieldDetailId();
	public void setAnnualFieldDetailId(Integer annualFieldDetailId);
	
	public Integer getGrowerContractYearId();
	public void setGrowerContractYearId(Integer growerContractYearId);
	
 	public Integer getDisplayOrder();
	public void setDisplayOrder(Integer displayOrder);	
	
	public Boolean getIsLeasedInd();
	public void setIsLeasedInd(Boolean isLeasedInd);

	public String getTransactionType();
	public void setTransactionType(String transactionType);
}
