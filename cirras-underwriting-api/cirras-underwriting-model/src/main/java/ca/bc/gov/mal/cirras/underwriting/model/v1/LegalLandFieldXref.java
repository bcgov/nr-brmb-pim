package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

public interface LegalLandFieldXref extends Serializable {

 	public Integer getLegalLandId();
	public void setLegalLandId(Integer legalLandId);
 
	public Integer getFieldId();
	public void setFieldId(Integer fieldId);

	public String getTransactionType();
	public void setTransactionType(String transactionType);
}
