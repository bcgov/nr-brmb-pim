package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class InventoryBerries implements Serializable {
	private static final long serialVersionUID = 1L;

	private String inventoryBerriesGuid;
	private String inventoryFieldGuid;
	private Integer cropCommodityId;
	private Integer cropVarietyId;
	private Integer plantedYear;
	private Double plantedAcres;
	private Integer rowSpacing;
	private Double plantSpacing;
	private Integer totalPlants;
	private Boolean isQuantityInsurableInd;
	private Boolean isPlantInsurableInd;

	private String cropCommodityName;
	private String cropVarietyName;
	
	public String getInventoryBerriesGuid() {
		return inventoryBerriesGuid;
	}

	public void setInventoryBerriesGuid(String inventoryBerriesGuid) {
		this.inventoryBerriesGuid = inventoryBerriesGuid;
	}

	public String getInventoryFieldGuid() {
		return inventoryFieldGuid;
	}

	public void setInventoryFieldGuid(String inventoryFieldGuid) {
		this.inventoryFieldGuid = inventoryFieldGuid;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Integer getCropVarietyId() {
		return cropVarietyId;
	}

	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	}

	public Integer getPlantedYear() {
		return plantedYear;
	}

	public void setPlantedYear(Integer plantedYear) {
		this.plantedYear = plantedYear;
	}

	public Double getPlantedAcres() {
		return plantedAcres;
	}

	public void setPlantedAcres(Double plantedAcres) {
		this.plantedAcres = plantedAcres;
	}

	public Integer getRowSpacing() {
		return rowSpacing;
	}

	public void setRowSpacing(Integer rowSpacing) {
		this.rowSpacing = rowSpacing;
	}

	public Double getPlantSpacing() {
		return plantSpacing;
	}

	public void setPlantSpacing(Double plantSpacing) {
		this.plantSpacing = plantSpacing;
	}

	public Integer getTotalPlants() {
		return totalPlants;
	}

	public void setTotalPlants(Integer
			totalPlants) {
		this.totalPlants = totalPlants;
	}

	public Boolean getIsQuantityInsurableInd() {
		return isQuantityInsurableInd;
	}

	public void setIsQuantityInsurableInd(Boolean isQuantityInsurableInd) {
		this.isQuantityInsurableInd = isQuantityInsurableInd;
	}

	public Boolean getIsPlantInsurableInd() {
		return isPlantInsurableInd;
	}

	public void setIsPlantInsurableInd(Boolean isPlantInsurableInd) {
		this.isPlantInsurableInd = isPlantInsurableInd;
	}

	public String getCropCommodityName() {
		return cropCommodityName;
	}

	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}

	public String getCropVarietyName() {
		return cropVarietyName;
	}

	public void setCropVarietyName(String cropVarietyName) {
		this.cropVarietyName = cropVarietyName;
	}
		
}
