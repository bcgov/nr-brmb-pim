package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.SYNC_COMMODITY_VARIETY_NAME)
@XmlSeeAlso({ SyncCommodityVarietyRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class SyncCommodityVarietyRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	private Integer cropId;
	private String cropName;
	private Integer parentCropId;
	private Boolean isInventoryCrop;
	private Integer insurancePlanId;
	private String shortLabel;
	private String plantDurationTypeCode;
	private Boolean isYieldCrop;
	private Boolean isUnderwritingCrop;
	private Boolean isProductInsurableInd;
	private Boolean isCropInsuranceEligibleInd;
	private Boolean isPlantInsuranceEligibleInd;
	private Boolean isOtherInsuranceEligibleInd;
	private String yieldMeasUnitTypeCode;
	private Integer yieldDecimalPrecision;
	private Date dataSyncTransDate;
	private String transactionType;

	public Integer getCropId() {
		return cropId;
	}

	public void setCropId(Integer cropId) {
		this.cropId = cropId;
	}


	public String getCropName() {
		return cropName;
	}
	
	public void setCropName(String cropName) {
		this.cropName = cropName;
	}
	

	public Integer getParentCropId() {
		return parentCropId;
	}
	
	public void setParentCropId(Integer parentCropId) {
		this.parentCropId = parentCropId;
	}

	public Boolean getIsInventoryCrop() {
		return isInventoryCrop;
	}

	public void setIsInventoryCrop(Boolean isInventoryCrop) {
		this.isInventoryCrop = isInventoryCrop;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public String getShortLabel() {
		return shortLabel;
	}

	public void setShortLabel(String shortLabel) {
		this.shortLabel = shortLabel;
	}

	public String getPlantDurationTypeCode() {
		return plantDurationTypeCode;
	}

	public void setPlantDurationTypeCode(String plantDurationTypeCode) {
		this.plantDurationTypeCode = plantDurationTypeCode;
	}

	public Boolean getIsYieldCrop() {
		return isYieldCrop;
	}

	public void setIsYieldCrop(Boolean isYieldCrop) {
		this.isYieldCrop = isYieldCrop;
	}

	public Boolean getIsUnderwritingCrop() {
		return isUnderwritingCrop;
	}

	public void setIsUnderwritingCrop(Boolean isUnderwritingCrop) {
		this.isUnderwritingCrop = isUnderwritingCrop;
	}

	public Boolean getIsProductInsurableInd() {
		return isProductInsurableInd;
	}

	public void setIsProductInsurableInd(Boolean isProductInsurableInd) {
		this.isProductInsurableInd = isProductInsurableInd;
	}

	public Boolean getIsCropInsuranceEligibleInd() {
		return isCropInsuranceEligibleInd;
	}

	public void setIsCropInsuranceEligibleInd(Boolean isCropInsuranceEligibleInd) {
		this.isCropInsuranceEligibleInd = isCropInsuranceEligibleInd;
	}

	public Boolean getIsPlantInsuranceEligibleInd() {
		return isPlantInsuranceEligibleInd;
	}

	public void setIsPlantInsuranceEligibleInd(Boolean isPlantInsuranceEligibleInd) {
		this.isPlantInsuranceEligibleInd = isPlantInsuranceEligibleInd;
	}

	public Boolean getIsOtherInsuranceEligibleInd() {
		return isOtherInsuranceEligibleInd;
	}

	public void setIsOtherInsuranceEligibleInd(Boolean isOtherInsuranceEligibleInd) {
		this.isOtherInsuranceEligibleInd = isOtherInsuranceEligibleInd;
	}
	
	public String getYieldMeasUnitTypeCode() {
		return yieldMeasUnitTypeCode;
	}

	public void setYieldMeasUnitTypeCode(String yieldMeasUnitTypeCode) {
		this.yieldMeasUnitTypeCode = yieldMeasUnitTypeCode;
	}

	public Integer getYieldDecimalPrecision() {
		return yieldDecimalPrecision;
	}

	public void setYieldDecimalPrecision(Integer yieldDecimalPrecision) {
		this.yieldDecimalPrecision = yieldDecimalPrecision;
	}
	
	public Date getDataSyncTransDate() {
		return dataSyncTransDate;
	}
	
	public void setDataSyncTransDate(Date dataSyncTransDate) {
		this.dataSyncTransDate = dataSyncTransDate;
	}

	
	public String getTransactionType() {
		return transactionType;
	}

	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
