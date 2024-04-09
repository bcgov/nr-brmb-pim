package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

public interface Field extends Serializable {

 	public Integer getFieldId();
	public void setFieldId(Integer fieldId);
 
 	public String getFieldLabel();
	public void setFieldLabel(String fieldLabel);
 
 	public Integer getActiveFromCropYear();
	public void setActiveFromCropYear(Integer activeFromCropYear);
 
 	public Integer getActiveToCropYear();
	public void setActiveToCropYear(Integer activeToCropYear);

	public Integer getTotalLegalLand();
	public void setTotalLegalLand(Integer totalLegalLand);

	public String getTransactionType();
	public void setTransactionType(String transactionType);
}
