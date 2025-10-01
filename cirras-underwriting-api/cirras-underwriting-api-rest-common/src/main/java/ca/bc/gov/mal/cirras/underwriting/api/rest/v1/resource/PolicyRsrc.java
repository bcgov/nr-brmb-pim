package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Policy;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.POLICY_NAME)
@XmlSeeAlso({ PolicyRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class PolicyRsrc extends BaseResource implements Policy {

	private static final long serialVersionUID = 1L;

	private Integer policyId;
	private Integer growerId;
	private Integer insurancePlanId;
	private String insurancePlanName;
	private Integer officeId;
	private String policyStatusCode;
	private String policyNumber;
	private String contractNumber;
	private Integer contractId;
	private Integer cropYear;
	private Date dataSyncTransDate;
	private String transactionType;

 	public Integer getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}
 
 	public Integer getGrowerId() {
		return growerId;
	}

	public void setGrowerId(Integer growerId) {
		this.growerId = growerId;
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
 
 	public Integer getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
	}
 
 	public String getPolicyStatusCode() {
		return policyStatusCode;
	}

	public void setPolicyStatusCode(String policyStatusCode) {
		this.policyStatusCode = policyStatusCode;
	}
 
 	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
 
 	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
 
 	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
 
 	public Integer getCropYear() {
		return cropYear;
	}

	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}
	
	public Date getDataSyncTransDate() {
		return dataSyncTransDate;
	}
	
	public void setDataSyncTransDate(Date dataSyncTransDate) {
		this.dataSyncTransDate = dataSyncTransDate;
	}

	@Override
	public String getTransactionType() {
		return transactionType;
	}

	@Override
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
