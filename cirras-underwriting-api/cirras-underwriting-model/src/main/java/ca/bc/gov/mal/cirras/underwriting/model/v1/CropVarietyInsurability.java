package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CropVarietyInsurability implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer cropVarietyId;
	private String varietyName;
	private String plantDurationTypeCode;
	private String cropVarietyInsurabilityGuid;
	private Boolean isQuantityInsurableInd;
	private Boolean isUnseededInsurableInd;
	private Boolean isPlantInsurableInd;
	private Boolean isAwpEligibleInd;
	private Boolean isUnderseedingEligibleInd;
	private Boolean isGrainUnseededDefaultInd;

	private Boolean deletedByUserInd;
	
	private Boolean isQuantityInsurableEditableInd;
	private Boolean isUnseededInsurableEditableInd;
	private Boolean isPlantInsurableEditableInd;
	private Boolean isAwpEligibleEditableInd;
	private Boolean isUnderseedingEligibleEditableInd;


	private List<CropVarietyPlantInsurability> cropVarietyPlantInsurabilities = new ArrayList<CropVarietyPlantInsurability>();

	public String getCropVarietyInsurabilityGuid() {
		return cropVarietyInsurabilityGuid;
	}

	public void setCropVarietyInsurabilityGuid(String cropVarietyInsurabilityGuid) {
		this.cropVarietyInsurabilityGuid = cropVarietyInsurabilityGuid;
	}
	
	
	public Integer getCropVarietyId() {
		return cropVarietyId;
	}

	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	}
	
	public Boolean getIsQuantityInsurableInd() {
		return isQuantityInsurableInd;
	}

	public void setIsQuantityInsurableInd(Boolean isQuantityInsurableInd) {
		this.isQuantityInsurableInd = isQuantityInsurableInd;
	}

	public Boolean getIsUnseededInsurableInd() {
		return isUnseededInsurableInd;
	}

	public void setIsUnseededInsurableInd(Boolean isUnseededInsurableInd) {
		this.isUnseededInsurableInd = isUnseededInsurableInd;
	}

	public Boolean getIsPlantInsurableInd() {
		return isPlantInsurableInd;
	}

	public void setIsPlantInsurableInd(Boolean isPlantInsurableInd) {
		this.isPlantInsurableInd = isPlantInsurableInd;
	}
	
	public Boolean getIsAwpEligibleInd() {
		return isAwpEligibleInd;
	}

	public void setIsAwpEligibleInd(Boolean isAwpEligibleInd) {
		this.isAwpEligibleInd = isAwpEligibleInd;
	}
	
	public Boolean getIsUnderseedingEligibleInd() {
		return isUnderseedingEligibleInd;
	}

	public void setIsUnderseedingEligibleInd(Boolean isUnderseedingEligibleInd) {
		this.isUnderseedingEligibleInd = isUnderseedingEligibleInd;
	}

	public Boolean getIsGrainUnseededDefaultInd() {
		return isGrainUnseededDefaultInd;
	}

	public void setIsGrainUnseededDefaultInd(Boolean isGrainUnseededDefaultInd) {
		this.isGrainUnseededDefaultInd = isGrainUnseededDefaultInd;
	}
	
	public String getPlantDurationTypeCode() {
		return plantDurationTypeCode;
	}

	public void setPlantDurationTypeCode(String plantDurationTypeCode) {
		this.plantDurationTypeCode = plantDurationTypeCode;
	}

	public String getVarietyName() {
		return varietyName;
	}

	public void setVarietyName(String varietyName) {
		this.varietyName = varietyName;
	}

	public Boolean getDeletedByUserInd() {
		return deletedByUserInd;
	}

	public void setDeletedByUserInd(Boolean deletedByUserInd) {
		this.deletedByUserInd = deletedByUserInd;
	}

	public List<CropVarietyPlantInsurability> getCropVarietyPlantInsurabilities() {
		return cropVarietyPlantInsurabilities;
	}
	public void setCropVarietyPlantInsurabilities(List<CropVarietyPlantInsurability> cropVarietyPlantInsurabilities) {
		this.cropVarietyPlantInsurabilities = cropVarietyPlantInsurabilities;
	}
	public Boolean getIsQuantityInsurableEditableInd() {
		return isQuantityInsurableEditableInd;
	}

	public void setIsQuantityInsurableEditableInd(Boolean isQuantityInsurableEditableInd) {
		this.isQuantityInsurableEditableInd = isQuantityInsurableEditableInd;
	}

	public Boolean getIsUnseededInsurableEditableInd() {
		return isUnseededInsurableEditableInd;
	}

	public void setIsUnseededInsurableEditableInd(Boolean isUnseededInsurableEditableInd) {
		this.isUnseededInsurableEditableInd = isUnseededInsurableEditableInd;
	}

	public Boolean getIsPlantInsurableEditableInd() {
		return isPlantInsurableEditableInd;
	}

	public void setIsPlantInsurableEditableInd(Boolean isPlantInsurableEditableInd) {
		this.isPlantInsurableEditableInd = isPlantInsurableEditableInd;
	}

	public Boolean getIsAwpEligibleEditableInd() {
		return isAwpEligibleEditableInd;
	}

	public void setIsAwpEligibleEditableInd(Boolean isAwpEligibleEditableInd) {
		this.isAwpEligibleEditableInd = isAwpEligibleEditableInd;
	}

	public Boolean getIsUnderseedingEligibleEditableInd() {
		return isUnderseedingEligibleEditableInd;
	}

	public void setIsUnderseedingEligibleEditableInd(Boolean isUnderseedingEligibleEditableInd) {
		this.isUnderseedingEligibleEditableInd = isUnderseedingEligibleEditableInd;
	}

}
