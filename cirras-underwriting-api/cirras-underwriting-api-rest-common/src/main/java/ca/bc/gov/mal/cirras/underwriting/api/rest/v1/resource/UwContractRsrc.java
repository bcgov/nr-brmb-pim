package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UwContract;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.UWCONTRACT_NAME)
@XmlSeeAlso({ UwContractRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class UwContractRsrc extends BaseResource implements UwContract<UwContractRsrc> {

	private static final long serialVersionUID = 1L;

	// contract
	private Integer contractId;
	private String contractNumber;
	private Integer cropYear;
	private Integer insurancePlanId;
	private String insurancePlanName;
	private Integer policyId;
	private String policyNumber;
	private String policyStatusCode;
	private String policyStatus;
	private Integer growerContractYearId;
	
	// grower
	private Integer growerId;
	private Integer growerNumber;
	private String growerName;
	private String growerAddressLine1;
	private String growerAddressLine2;
	private String growerPostalCode;
	private String growerCity;
	private String growerProvince;
	private String growerPrimaryEmail;
	private String growerPrimaryPhone;

	// other
	private String inventoryContractGuid;
	private String declaredYieldContractGuid;
	private String verifiedYieldContractGuid;
	private Integer totalDopEligibleInventory;

	private List<UwContractRsrc> linkedPolicies = new ArrayList<UwContractRsrc>();
	
	public Integer getContractId() {
		return contractId;
	}
	
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}	
	
	public Integer getCropYear() {
		return cropYear;
	}

	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
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
	
	public Integer getPolicyId() {
		return policyId;
	}
	
	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}
	
	
	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyStatusCode() {
		return policyStatusCode;
	}

	public void setPolicyStatusCode(String policyStatusCode) {
		this.policyStatusCode = policyStatusCode;
	}
	
	
	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}
	
	public Integer getGrowerContractYearId() {
		return growerContractYearId;
	}

	public void setGrowerContractYearId(Integer growerContractYearId) {
		this.growerContractYearId = growerContractYearId;
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
	
	public String getGrowerAddressLine1() {
		return growerAddressLine1;
	}

	public void setGrowerAddressLine1(String growerAddressLine1) {
		this.growerAddressLine1 = growerAddressLine1;
	}

	public String getGrowerAddressLine2() {
		return growerAddressLine2;
	}

	public void setGrowerAddressLine2(String growerAddressLine2) {
		this.growerAddressLine2 = growerAddressLine2;
	}	

	public String getGrowerPostalCode() {
		return growerPostalCode;
	}

	public void setGrowerPostalCode(String growerPostalCode) {
		this.growerPostalCode = growerPostalCode;
	}

	public String getGrowerCity() {
		return growerCity;
	}
	
	public void setGrowerCity(String growerCity) {
		this.growerCity = growerCity;
	}

	public String getGrowerProvince() {
		return growerProvince;
	}
	
	public void setGrowerProvince(String growerProvince) {
		this.growerProvince = growerProvince;
	}

	public String getGrowerPrimaryEmail() {
		return growerPrimaryEmail;
	}
	
	public void setGrowerPrimaryEmail(String growerPrimaryEmail) {
		this.growerPrimaryEmail = growerPrimaryEmail;
	}

	public String getGrowerPrimaryPhone() {
		return growerPrimaryPhone;
	}
	
	public void setGrowerPrimaryPhone(String growerPrimaryPhone) {
		this.growerPrimaryPhone = growerPrimaryPhone;
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
	
	public String getVerifiedYieldContractGuid() {
		return verifiedYieldContractGuid;
	}

	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid) {
		this.verifiedYieldContractGuid = verifiedYieldContractGuid;
	}

	public Integer getTotalDopEligibleInventory() {
		return totalDopEligibleInventory;
	}
	
	public void setTotalDopEligibleInventory(Integer totalDopEligibleInventory) {
		this.totalDopEligibleInventory = totalDopEligibleInventory;
	}

	public List<UwContractRsrc> getLinkedPolicies() {
		return linkedPolicies;
	}

	public void setLinkedPolicies(List<UwContractRsrc> linkedPolicies) {
		this.linkedPolicies = linkedPolicies;
	}
	
}
