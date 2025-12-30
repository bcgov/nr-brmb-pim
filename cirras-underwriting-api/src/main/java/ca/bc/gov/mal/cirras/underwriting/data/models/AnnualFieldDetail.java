package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

public interface AnnualFieldDetail extends Serializable {

	public Integer getAnnualFieldDetailId();
	public void setAnnualFieldDetailId(Integer annualFieldDetailId);
	
	public Integer getLegalLandId();
	public void setLegalLandId(Integer legalLandId);
	
 	public Integer getFieldId();
	public void setFieldId(Integer fieldId);
 
 	public Integer getCropYear();
	public void setCropYear(Integer cropYear);	

	public String getTransactionType();
	public void setTransactionType(String transactionType);
}
