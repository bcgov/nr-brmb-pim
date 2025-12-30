package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.List;

public interface UwContract<U extends UwContract<?>> extends Serializable {

	public Integer getContractId();
	public void setContractId(Integer contractId);

	public String getContractNumber();
	public void setContractNumber(String contractNumber);

	public Integer getCropYear();
	public void setCropYear(Integer cropYear);
	
	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);

	public String getInsurancePlanName();
	public void setInsurancePlanName(String insurancePlanName);
	
	public Integer getPolicyId();
	public void setPolicyId(Integer policyId);
	
	public String getPolicyNumber();
	public void setPolicyNumber(String policyNumber);

	public String getPolicyStatusCode();
	public void setPolicyStatusCode(String policyStatusCode);

	public String getPolicyStatus();
	public void setPolicyStatus(String policyStatus);
	
	public Integer getGrowerContractYearId();
	public void setGrowerContractYearId(Integer growerContractYearId);
	
	public Integer getGrowerId();
	public void setGrowerId(Integer growerId);
	
	public Integer getGrowerNumber();
	public void setGrowerNumber(Integer growerNumber);

	public String getGrowerName();
	public void setGrowerName(String growerName) ;
	
	public String getGrowerAddressLine1();
	public void setGrowerAddressLine1(String growerAddressLine1) ;

	public String getGrowerAddressLine2();
	public void setGrowerAddressLine2(String growerAddressLine2);

	public String getGrowerPostalCode() ;
	public void setGrowerPostalCode(String growerPostalCode);

	public String getGrowerCity();
	public void setGrowerCity(String growerCity);

	public String getGrowerProvince();
	public void setGrowerProvince(String growerProvince);

	public String getGrowerPrimaryEmail();
	public void setGrowerPrimaryEmail(String growerPrimaryEmail);

	public String getGrowerPrimaryPhone();
	public void setGrowerPrimaryPhone(String growerPrimaryPhone);
	
	public String getInventoryContractGuid();
	public void setInventoryContractGuid(String inventoryContractGuid);

	public String getDeclaredYieldContractGuid();
	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid);

	public String getVerifiedYieldContractGuid();
	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid);
	
	public Integer getTotalDopEligibleInventory();
	public void setTotalDopEligibleInventory(Integer totalDopEligibleInventory);

	public List<U> getLinkedPolicies();
	public void setLinkedPolicies(List<U> linkedPolicies);

	public List<OtherYearPolicy> getOtherYearPolicies();
	public void setOtherYearPolicies(List<OtherYearPolicy> otherYearPolicies);
	
}
