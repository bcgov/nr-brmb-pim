package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.util.Date;
import java.io.Serializable;

//
// This is not going to be a resource.
//
public class InventorySeededForage implements Serializable {
	private static final long serialVersionUID = 1L;

	private String inventorySeededForageGuid;
	private String inventoryFieldGuid;
	private Integer cropCommodityId;
	private Integer cropVarietyId;
	private String cropVarietyName;
	private String commodityTypeCode;
	private Double fieldAcres;
	private Integer seedingYear;
	private Date seedingDate;
	private Boolean isIrrigatedInd;
	private Boolean isQuantityInsurableInd;
	private String plantInsurabilityTypeCode;
	private Boolean isAwpEligibleInd;
	private Boolean deletedByUserInd;
	
	private String linkPlantingType;
	private String grainInventoryFieldGuid;
	private LinkedPlanting linkedPlanting;

	public String getInventorySeededForageGuid() {
		return inventorySeededForageGuid;
	}

	public void setInventorySeededForageGuid(String inventorySeededForageGuid) {
		this.inventorySeededForageGuid = inventorySeededForageGuid;
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

	public String getCropVarietyName() {
		return cropVarietyName;
	}
	public void setCropVarietyName(String cropVarietyName) {
		this.cropVarietyName = cropVarietyName;
	}

	public String getCommodityTypeCode() {
		return commodityTypeCode;
	}

	public void setCommodityTypeCode(String commodityTypeCode) {
		this.commodityTypeCode = commodityTypeCode;
	}

	public Double getFieldAcres() {
		return fieldAcres;
	}

	public void setFieldAcres(Double fieldAcres) {
		this.fieldAcres = fieldAcres;
	}

	public Integer getSeedingYear() {
		return seedingYear;
	}

	public void setSeedingYear(Integer seedingYear) {
		this.seedingYear = seedingYear;
	}
	
	public Date getSeedingDate() {
		return seedingDate;
	}

	public void setSeedingDate(Date seedingDate) {
		this.seedingDate = seedingDate;
	}

	public Boolean getIsIrrigatedInd() {
		return isIrrigatedInd;
	}

	public void setIsIrrigatedInd(Boolean isIrrigatedInd) {
		this.isIrrigatedInd = isIrrigatedInd;
	}

	public Boolean getIsQuantityInsurableInd() {
		return isQuantityInsurableInd;
	}

	public void setIsQuantityInsurableInd(Boolean isQuantityInsurableInd) {
		this.isQuantityInsurableInd = isQuantityInsurableInd;
	}

	public String getPlantInsurabilityTypeCode() {
		return plantInsurabilityTypeCode;
	}

	public void setPlantInsurabilityTypeCode(String plantInsurabilityTypeCode) {
		this.plantInsurabilityTypeCode = plantInsurabilityTypeCode;
	}

	public Boolean getIsAwpEligibleInd() {
		return isAwpEligibleInd;
	}

	public void setIsAwpEligibleInd(Boolean isAwpEligibleInd) {
		this.isAwpEligibleInd = isAwpEligibleInd;
	}
	
	public Boolean getDeletedByUserInd() {
		return deletedByUserInd;
	}

	public void setDeletedByUserInd(Boolean deletedByUserInd) {
		this.deletedByUserInd = deletedByUserInd;
	}

	public String getLinkPlantingType() {
		return linkPlantingType;
	}

	public void setLinkPlantingType(String linkPlantingType) {
		this.linkPlantingType = linkPlantingType;
	}

	public String getGrainInventoryFieldGuid() {
		return grainInventoryFieldGuid;
	}

	public void setGrainInventoryFieldGuid(String grainInventoryFieldGuid) {
		this.grainInventoryFieldGuid = grainInventoryFieldGuid;
	}

	public LinkedPlanting getLinkedPlanting() {
		return linkedPlanting;
	}

	public void setLinkedPlanting(LinkedPlanting linkedPlanting) {
		this.linkedPlanting = linkedPlanting;
	}
		
}
