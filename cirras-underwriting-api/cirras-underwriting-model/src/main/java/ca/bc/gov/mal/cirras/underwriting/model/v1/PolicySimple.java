package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

public class PolicySimple implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer policyId;
	private Integer insurancePlanId;
	private String insurancePlanName;
	private String policyNumber;
	private Integer cropYear;
	private Integer growerContractYearId;
	private String inventoryContractGuid;
	private String declaredYieldContractGuid;
	private Integer growerId;
	private Integer growerNumber;
	private String growerName;
	
	
 	public Integer getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}
 	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

 	public String getInsurancePlanName() {
		return insurancePlanName;
	}

	public void setInsurancePlanName(String insurancePlanName) {
		this.insurancePlanName = insurancePlanName;
	}
 
 	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
 
 	public Integer getCropYear() {
		return cropYear;
	}

	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}
	
 	public Integer getGrowerContractYearId() {
		return growerContractYearId;
	}

	public void setGrowerContractYearId(Integer growerContractYearId) {
		this.growerContractYearId = growerContractYearId;
	}

	public String getInventoryContractGuid() {
		return inventoryContractGuid;
	}

	public void setInventoryContractGuid(String inventoryContractGuid) {
		this.inventoryContractGuid = inventoryContractGuid;
	}
	
	public String getDeclaredYieldContractGuid() {
		return declaredYieldContractGuid;
	}
	
	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid) {
		this.declaredYieldContractGuid = declaredYieldContractGuid;
	}

 	public Integer getGrowerId() {
		return growerId;
	}

	public void setGrowerId(Integer growerId) {
		this.growerId = growerId;
	}

	public Integer getGrowerNumber() {
		return growerNumber;
	}
	
	public void setGrowerNumber(Integer growerNumber) {
		this.growerNumber = growerNumber;
	}
	
	public String getGrowerName() {
		return growerName;
	}
	
	public void setGrowerName(String growerName) {
		this.growerName = growerName;
	}
	
}
