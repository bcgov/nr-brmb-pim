package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class InventoryContractCommodityBerries implements Serializable {
	private static final long serialVersionUID = 1L;

	private String inventoryContractCommodityBerriesGuid;
	private String inventoryContractGuid;
	private Integer cropCommodityId;
	private String cropCommodityName;
	private Integer totalInsuredPlants;
	private Integer totalUninsuredPlants;
	private Double totalInsuredAcres;
	private Double totalUninsuredAcres;

	public String getInventoryContractCommodityBerriesGuid() {
		return inventoryContractCommodityBerriesGuid;
	}

	public void setInventoryContractCommodityBerriesGuid(String inventoryContractCommodityBerriesGuid) {
		this.inventoryContractCommodityBerriesGuid = inventoryContractCommodityBerriesGuid;
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

	public Integer getTotalInsuredPlants() {
		return totalInsuredPlants;
	}

	public void setTotalInsuredPlants(Integer totalInsuredPlants) {
		this.totalInsuredPlants = totalInsuredPlants;
	}

	public Integer getTotalUninsuredPlants() {
		return totalUninsuredPlants;
	}

	public void setTotalUninsuredPlants(Integer totalUninsuredPlants) {
		this.totalUninsuredPlants = totalUninsuredPlants;
	}

	public Double getTotalInsuredAcres() {
		return totalInsuredAcres;
	}

	public void setTotalInsuredAcres(Double totalInsuredAcres) {
		this.totalInsuredAcres = totalInsuredAcres;
	}

	public Double getTotalUninsuredAcres() {
		return totalUninsuredAcres;
	}

	public void setTotalUninsuredAcres(Double totalUninsuredAcres) {
		this.totalUninsuredAcres = totalUninsuredAcres;
	}

	public String getCropCommodityName() {
		return cropCommodityName;
	}

	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}
}
