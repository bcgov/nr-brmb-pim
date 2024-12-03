package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface VerifiedYieldContract<A extends AnnualField> extends Serializable {

 	public String getVerifiedYieldContractGuid();
	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid);

 	public String getDeclaredYieldContractGuid();
	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid);		
	
 	public Integer getContractId();
	public void setContractId(Integer contractId);
 
 	public Integer getCropYear();
	public void setCropYear(Integer cropYear);
	
 	public Date getVerifiedYieldUpdateTimestamp();
	public void setVerifiedYieldUpdateTimestamp(Date verifiedYieldUpdateTimestamp);
 
 	public String getVerifiedYieldUpdateUser();
	public void setVerifiedYieldUpdateUser(String verifiedYieldUpdateUser);
  
 	public String getDefaultYieldMeasUnitTypeCode();
	public void setDefaultYieldMeasUnitTypeCode(String defaultYieldMeasUnitTypeCode);
 
	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);
				
	public Integer getGrowerContractYearId();
	public void setGrowerContractYearId(Integer growerContractYearId);

	public List<A> getFields();
	public void setFields(List<A> fields);
	
	public List<VerifiedYieldContractCommodity> getVerifiedYieldContractCommodities();
	public void setVerifiedYieldContractCommodities(List<VerifiedYieldContractCommodity> verifiedYieldContractCommodities);

	public List<VerifiedYieldAmendment> getVerifiedYieldAmendments();
	public void setVerifiedYieldAmendments(List<VerifiedYieldAmendment> verifiedYieldAmendments);
}
