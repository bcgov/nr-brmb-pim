package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

public class CropVarietyPlantInsurability implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer cropVarietyId;
	private String plantInsurabilityTypeCode;
	private String description;
	private String cropVarietyInsurabilityGuid;
	
	private Boolean deletedByUserInd;
	private Boolean isUsedInd;

	public Integer getCropVarietyId() {
		return this.cropVarietyId;
	};
	
	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	};
	
 	public String getPlantInsurabilityTypeCode() {
		return plantInsurabilityTypeCode;
	}

	public void setPlantInsurabilityTypeCode(String plantInsurabilityTypeCode) {
		this.plantInsurabilityTypeCode = plantInsurabilityTypeCode;
	}
	 
 	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCropVarietyInsurabilityGuid() {
		return cropVarietyInsurabilityGuid;
	}

	public void setCropVarietyInsurabilityGuid(String cropVarietyInsurabilityGuid) {
		this.cropVarietyInsurabilityGuid = cropVarietyInsurabilityGuid;
	}

	public Boolean getDeletedByUserInd() {
		return deletedByUserInd;
	}

	public void setDeletedByUserInd(Boolean deletedByUserInd) {
		this.deletedByUserInd = deletedByUserInd;
	}
	
	public Boolean getIsUsedInd() {
		return isUsedInd;
	}
	
	public void setIsUsedInd(Boolean isUsedInd) {
		this.isUsedInd = isUsedInd;
	}
}
