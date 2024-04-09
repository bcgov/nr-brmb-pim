package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.ContractedFieldDetail;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CONTRACTED_FIELD_DETAIL_NAME)
@XmlSeeAlso({ ContractedFieldDetailRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ContractedFieldDetailRsrc extends BaseResource implements ContractedFieldDetail {

	private static final long serialVersionUID = 1L;

	private Integer contractedFieldDetailId;
	private Integer annualFieldDetailId;
	private Integer growerContractYearId;
	private Integer displayOrder;
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

	@Override
	public String getTransactionType() {
		return transactionType;
	}

	@Override
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}



}
