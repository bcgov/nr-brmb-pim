package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.Date;


import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GrowerContractYear;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.GROWER_CONTRACT_YEAR_NAME)
@XmlSeeAlso({ GrowerContractYearSyncRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class GrowerContractYearSyncRsrc extends BaseResource implements GrowerContractYear {

	private static final long serialVersionUID = 1L;

	private Integer growerContractYearId;
	private Integer contractId;
	private Integer growerId;
	private Integer insurancePlanId;
	private Integer cropYear;
	private Date dataSyncTransDate;
	private String transactionType;

 	public Integer getGrowerContractYearId() {
		return growerContractYearId;
	}

	public void setGrowerContractYearId(Integer growerContractYearId) {
		this.growerContractYearId = growerContractYearId;
	}
 
 	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
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
