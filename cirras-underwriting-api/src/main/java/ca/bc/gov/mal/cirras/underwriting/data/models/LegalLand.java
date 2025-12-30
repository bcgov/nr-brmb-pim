package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.List;

public interface LegalLand<A extends Field> extends Serializable {

 	public Integer getLegalLandId();
	public void setLegalLandId(Integer legalLandId);
	
	public String getPrimaryPropertyIdentifier();
	public void setPrimaryPropertyIdentifier(String primaryPropertyIdentifier);

	public String getPrimaryLandIdentifierTypeCode();
	public void setPrimaryLandIdentifierTypeCode(String primaryLandIdentifierTypeCode);	
	
 	public String getPrimaryReferenceTypeCode();
	public void setPrimaryReferenceTypeCode(String primaryReferenceTypeCode);
 
 	public String getLegalDescription();
	public void setLegalDescription(String legalDescription);
 
 	public String getLegalShortDescription();
	public void setLegalShortDescription(String legalShortDescription);
 
 	public String getOtherDescription();
	public void setOtherDescription(String otherDescription);
 
 	public Integer getActiveFromCropYear();
	public void setActiveFromCropYear(Integer activeFromCropYear);
 
 	public Integer getActiveToCropYear();
	public void setActiveToCropYear(Integer activeToCropYear);

	public Double getTotalAcres();
	public void setTotalAcres(Double totalAcres);
	
	public String getTransactionType();
	public void setTransactionType(String transactionType);
	
	public List<LegalLandRiskArea> getRiskAreas();
	public void setRiskAreas(List<LegalLandRiskArea> riskAreas);

	public List<A> getFields();
	public void setFields(List<A> fields);

}
