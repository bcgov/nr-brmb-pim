package ca.bc.gov.mal.cirras.underwriting.data.resources;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CONTRACTED_FIELD_DETAIL_NAME)
@XmlSeeAlso({ ContractedFieldDetailRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ContractedFieldDetailRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	private Integer contractedFieldDetailId;
	private Integer annualFieldDetailId;
	private Integer growerContractYearId;
	private Integer displayOrder;
	private Boolean isLeasedInd;
	private String transactionType;
	
	public Integer getContractedFieldDetailId() {
		return contractedFieldDetailId;
	}

	public void setContractedFieldDetailId(Integer contractedFieldDetailId) {
		this.contractedFieldDetailId = contractedFieldDetailId;
	}
	
	public Integer getAnnualFieldDetailId() {
		return annualFieldDetailId;
	}

	public void setAnnualFieldDetailId(Integer annualFieldDetailId) {
		this.annualFieldDetailId = annualFieldDetailId;
	}
		
	public Integer getGrowerContractYearId() {
		return growerContractYearId;
	}

	public void setGrowerContractYearId(Integer growerContractYearId) {
		this.growerContractYearId = growerContractYearId;
	}
	
	public Integer getDisplayOrder() {
		return this.displayOrder;
	}
	
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Boolean getIsLeasedInd() {
		return isLeasedInd;
	}

	public void setIsLeasedInd(Boolean isLeasedInd) {
		this.isLeasedInd = isLeasedInd;
	}
	
	
	public String getTransactionType() {
		return transactionType;
	}

	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}



}
