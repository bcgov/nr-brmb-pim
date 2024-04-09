package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;

public interface YieldMeasUnitTypeCode extends Serializable {

 	public String getYieldMeasUnitTypeCode();
	public void setYieldMeasUnitTypeCode(String yieldMeasUnitTypeCode);
 
 	public String getDescription();
	public void setDescription(String description);
 
 	public Integer getDecimalPrecision();
	public void setDecimalPrecision(Integer decimalPrecision);
 
 	public Date getEffectiveDate();
	public void setEffectiveDate(Date effectiveDate);
 
 	public Date getExpiryDate();
	public void setExpiryDate(Date expiryDate);
 
 	public Boolean getIsDefaultYieldUnitInd();
	public void setIsDefaultYieldUnitInd(Boolean isDefaultYieldUnitInd);
 
 	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);

}
