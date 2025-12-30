package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.Date;

public interface RiskArea extends Serializable {

	public Integer getRiskAreaId();
	public void setRiskAreaId(Integer riskAreaId);

	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);

	public String getRiskAreaName();
	public void setRiskAreaName(String riskAreaName);

	public String getDescription();
	public void setDescription(String description);

	public Date getEffectiveDate();
	public void setEffectiveDate(Date effectiveDate);

	public Date getExpiryDate();
	public void setExpiryDate(Date expiryDate);
	
	public Integer getLegalLandId();
	public void setLegalLandId(Integer legalLandId);

	public String getInsurancePlanName();
	public void setInsurancePlanName(String insurancePlanName);

}
