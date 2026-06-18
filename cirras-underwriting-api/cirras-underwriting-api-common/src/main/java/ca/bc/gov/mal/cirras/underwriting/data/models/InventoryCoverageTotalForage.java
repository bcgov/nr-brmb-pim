package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class InventoryCoverageTotalForage implements Serializable {
	private static final long serialVersionUID = 1L;

	private String inventoryCoverageTotalForageGuid;
	private String inventoryContractGuid;
	private Integer cropCommodityId;
	private String plantInsurabilityTypeCode;	
	private Double totalFieldAcres;
	private Boolean isUnseededInsurableInd;

	private String cropCommodityName;
	private String plantInsurabilityTypeDesc;

 	public String getInventoryCoverageTotalForageGuid() {
		return inventoryCoverageTotalForageGuid;
	}
	public void setInventoryCoverageTotalForageGuid(String inventoryCoverageTotalForageGuid) {
		this.inventoryCoverageTotalForageGuid = inventoryCoverageTotalForageGuid;
	}

	public String getInventoryContractGuid() {
		return inventoryContractGuid;
	}
	public void setInventoryContractGuid(String inventoryContractGuid) {
		this.inventoryContractGuid = inventoryContractGuid;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}
	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public String getPlantInsurabilityTypeCode() {
		return plantInsurabilityTypeCode;
	}
	public void setPlantInsurabilityTypeCode(String plantInsurabilityTypeCode) {
		this.plantInsurabilityTypeCode = plantInsurabilityTypeCode;
	}

	public Double getTotalFieldAcres() {
		return totalFieldAcres;
	}
	public void setTotalFieldAcres(Double totalFieldAcres) {
		this.totalFieldAcres = totalFieldAcres;
	} 

	public Boolean getIsUnseededInsurableInd() {
		return isUnseededInsurableInd;
	}
	public void setIsUnseededInsurableInd(Boolean isUnseededInsurableInd) {
		this.isUnseededInsurableInd = isUnseededInsurableInd;
	}
	
	public String getCropCommodityName() {
		return cropCommodityName;
	}
	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}

	public String getPlantInsurabilityTypeDesc() {
		return plantInsurabilityTypeDesc;
	}
	public void setPlantInsurabilityTypeDesc(String plantInsurabilityTypeDesc) {
		this.plantInsurabilityTypeDesc = plantInsurabilityTypeDesc;
	}
}
