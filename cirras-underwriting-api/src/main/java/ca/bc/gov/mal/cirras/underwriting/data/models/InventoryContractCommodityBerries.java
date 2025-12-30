package ca.bc.gov.mal.cirras.underwriting.data.models;

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
	private Double totalQuantityInsuredAcres;
	private Double totalQuantityUninsuredAcres;
	private Double totalPlantInsuredAcres;
	private Double totalPlantUninsuredAcres;

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

	public Double getTotalQuantityInsuredAcres() {
		return totalQuantityInsuredAcres;
	}

	public void setTotalQuantityInsuredAcres(Double totalQuantityInsuredAcres) {
		this.totalQuantityInsuredAcres = totalQuantityInsuredAcres;
	}

	public Double getTotalQuantityUninsuredAcres() {
		return totalQuantityUninsuredAcres;
	}

	public void setTotalQuantityUninsuredAcres(Double totalQuantityUninsuredAcres) {
		this.totalQuantityUninsuredAcres = totalQuantityUninsuredAcres;
	}

	public Double getTotalPlantInsuredAcres() {
		return totalPlantInsuredAcres;
	}

	public void setTotalPlantInsuredAcres(Double totalPlantInsuredAcres) {
		this.totalPlantInsuredAcres = totalPlantInsuredAcres;
	}

	public Double getTotalPlantUninsuredAcres() {
		return totalPlantUninsuredAcres;
	}

	public void setTotalPlantUninsuredAcres(Double totalPlantUninsuredAcres) {
		this.totalPlantUninsuredAcres = totalPlantUninsuredAcres;
	}

	public String getCropCommodityName() {
		return cropCommodityName;
	}

	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}
}
