package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface VerifiedYieldContract<A extends AnnualField> extends Serializable {

 	public String getVerifiedYieldContractGuid();
	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid);
 
 	public Integer getContractId();
	public void setContractId(Integer contractId);
 
 	public Integer getCropYear();
	public void setCropYear(Integer cropYear);
  
 	public Date getVerifiedUpdateTimestamp();
	public void setVerifiedUpdateTimestamp(Date verifiedUpdateTimestamp);
 
 	public String getVerifiedUpdateUser();
	public void setVerifiedUpdateUser(String verifiedUpdateUser);
  
 	public String getDefaultYieldMeasUnitTypeCode();
	public void setDefaultYieldMeasUnitTypeCode(String defaultYieldMeasUnitTypeCode);
 
	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);
			
	public List<VerifiedYieldContractCommodity> getVerifiedYieldContractCommodities();
	public void setVerifiedYieldContractCommodities(List<VerifiedYieldContractCommodity> verifiedYieldContractCommodities);
	
	public Integer getGrowerContractYearId();
	public void setGrowerContractYearId(Integer growerContractYearId);	
}
