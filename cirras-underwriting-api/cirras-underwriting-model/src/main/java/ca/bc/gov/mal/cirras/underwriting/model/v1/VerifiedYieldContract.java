package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ca.bc.gov.nrs.wfone.common.model.Message;

public interface VerifiedYieldContract<A extends AnnualField, M extends Message> extends Serializable {

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
	
	public Boolean getUpdateProductValuesInd();
	public void setUpdateProductValuesInd(Boolean updateProductValuesInd);

	public List<A> getFields();
	public void setFields(List<A> fields);
	
	public List<VerifiedYieldContractCommodity> getVerifiedYieldContractCommodities();
	public void setVerifiedYieldContractCommodities(List<VerifiedYieldContractCommodity> verifiedYieldContractCommodities);

	public List<VerifiedYieldAmendment> getVerifiedYieldAmendments();
	public void setVerifiedYieldAmendments(List<VerifiedYieldAmendment> verifiedYieldAmendments);
	
	public List<VerifiedYieldSummary> getVerifiedYieldSummaries();
	public void setVerifiedYieldSummaries(List<VerifiedYieldSummary> verifiedYieldSummaries);
	
	public VerifiedYieldGrainBasket getVerifiedYieldGrainBasket();
	public void setVerifiedYieldGrainBasket(VerifiedYieldGrainBasket verifiedYieldGrainBasket);
	
	public List<M> getProductWarningMessages();
	public void setProductWarningMessages(List<M> productWarningMessages);

}
