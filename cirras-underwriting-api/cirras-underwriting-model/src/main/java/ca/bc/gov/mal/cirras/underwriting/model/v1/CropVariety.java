package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CropVariety implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer cropVarietyId;
	private Integer cropCommodityId;
	private String varietyName;
	private Date dataSyncTransDate;
	private Integer insurancePlanId;
	private Boolean isQuantityInsurableInd;
	private Boolean isUnseededInsurableInd;
	private Boolean isPlantInsurableInd;
	private Boolean isAwpEligibleInd;
	private Boolean isUnderseedingEligibleInd;
	private Boolean isGrainUnseededDefaultInd;

	private List<CropVarietyCommodityType> cropVarietyCommodityTypeList;
	private List<CropVarietyPlantInsurability> cropVarietyPlantInsurabilityList;

	public Integer getCropVarietyId() {
		return this.cropVarietyId;
	};

	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	};

	public Integer getCropCommodityId() {
		return this.cropCommodityId;
	};

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	};

	public String getVarietyName() {
		return this.varietyName;
	};

	public void setVarietyName(String varietyName) {
		this.varietyName = varietyName;
	};

	public Date getDataSyncTransDate() {
		return this.dataSyncTransDate;
	};

	public void setDataSyncTransDate(Date dataSyncTransDate) {
		this.dataSyncTransDate = dataSyncTransDate;
	};

	public Integer getInsurancePlanId() {
		return this.insurancePlanId;
	};

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	};

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

	public List<CropVarietyCommodityType> getCropVarietyCommodityTypes() {
		return this.cropVarietyCommodityTypeList;
	};

	public void setCropVarietyCommodityTypes(List<CropVarietyCommodityType> cropVarietyCommodityTypes) {
		this.cropVarietyCommodityTypeList = cropVarietyCommodityTypes;
	};

	public List<CropVarietyPlantInsurability> getCropVarietyPlantInsurabilities() {
		return this.cropVarietyPlantInsurabilityList;
	};

	public void setCropVarietyPlantInsurabilities(List<CropVarietyPlantInsurability> cropVarietyPlantInsurabilityList) {
		this.cropVarietyPlantInsurabilityList = cropVarietyPlantInsurabilityList;
	};

}
