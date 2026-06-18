package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class InventoryContractCommodity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String inventoryContractCommodityGuid;
	private String inventoryContractGuid;
	private Integer cropCommodityId;
	private String cropCommodityName;
	private Double totalUnseededAcres;
	private Double totalUnseededAcresOverride;
	private Double totalSeededAcres;
	private Double totalSpotLossAcres;
	private Boolean isPedigreeInd;

	public String getInventoryContractCommodityGuid() {
		return inventoryContractCommodityGuid;
	}
	public void setInventoryContractCommodityGuid(String inventoryContractCommodityGuid) {
		this.inventoryContractCommodityGuid = inventoryContractCommodityGuid;
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

	public String getCropCommodityName() {
		return cropCommodityName;
	}
	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}

	public Double getTotalUnseededAcres() {
		return totalUnseededAcres;
	}
	public void setTotalUnseededAcres(Double totalUnseededAcres) {
		this.totalUnseededAcres = totalUnseededAcres;
	}

	public Double getTotalUnseededAcresOverride() {
		return totalUnseededAcresOverride;
	}
	public void setTotalUnseededAcresOverride(Double totalUnseededAcresOverride) {
		this.totalUnseededAcresOverride = totalUnseededAcresOverride;
	}

	public Double getTotalSeededAcres() {
		return totalSeededAcres;
	}
	public void setTotalSeededAcres(Double totalSeededAcres) {
		this.totalSeededAcres = totalSeededAcres;
	}

	public Double getTotalSpotLossAcres() {
		return totalSpotLossAcres;
	}
	public void setTotalSpotLossAcres(Double totalSpotLossAcres) {
		this.totalSpotLossAcres = totalSpotLossAcres;
	}	

	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}
	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
	}
		
}
