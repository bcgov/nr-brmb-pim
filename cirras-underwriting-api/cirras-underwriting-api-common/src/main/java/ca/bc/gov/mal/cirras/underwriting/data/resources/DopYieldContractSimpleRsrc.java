package ca.bc.gov.mal.cirras.underwriting.data.resources;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerries;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.DOP_YIELD_CONTRACT_SIMPLE_NAME)
@XmlSeeAlso({ DopYieldContractSimpleRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class DopYieldContractSimpleRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	private String declaredYieldContractGuid;
	private Integer contractId;
	private Integer cropYear;
	
	private DopYieldContractCommodityBerries dopYieldContractCommodityBerries;

 	public String getDeclaredYieldContractGuid() {
		return declaredYieldContractGuid;
	}

	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid) {
		this.declaredYieldContractGuid = declaredYieldContractGuid;
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
	
	public DopYieldContractCommodityBerries getDopYieldContractCommodityBerries() {
		return dopYieldContractCommodityBerries;
	}

	public void setDopYieldContractCommodityBerries(DopYieldContractCommodityBerries dopYieldContractCommodityBerries) {
		this.dopYieldContractCommodityBerries = dopYieldContractCommodityBerries;
	}
	
}
