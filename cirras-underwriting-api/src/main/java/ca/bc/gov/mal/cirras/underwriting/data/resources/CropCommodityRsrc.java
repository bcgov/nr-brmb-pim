package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.Date;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.CropCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.CropVariety;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CROP_COMMODITY_NAME)
@XmlSeeAlso({ CropCommodityRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class CropCommodityRsrc extends BaseResource implements CropCommodity {

	private static final long serialVersionUID = 1L;

	private Integer cropCommodityId;
	private Integer insurancePlanId;
	private String commodityName;
	private String shortLabel;
	private String plantDurationTypeCode;
	private Boolean isInventoryCropInd;
	private Boolean isYieldCropInd;
	private Boolean isUnderwritingCropInd;
	private Boolean isProductInsurableInd;
	private Boolean isCropInsuranceEligibleInd;
	private Boolean isPlantInsuranceEligibleInd;
	private Boolean isOtherInsuranceEligibleInd;
	private String yieldMeasUnitTypeCode;
	private Integer yieldDecimalPrecision;
	private Date dataSyncTransDate;

	private String inventoryTypeCode;
	private String yieldTypeCode;
	
	private List<CropVariety> cropVarietyList;
	
	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
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

	public Boolean getIsInventoryCropInd() {
		return isInventoryCropInd;
	}

	public void setIsInventoryCropInd(Boolean isInventoryCropInd) {
		this.isInventoryCropInd = isInventoryCropInd;
	}

	public Boolean getIsYieldCropInd() {
		return isYieldCropInd;
	}

	public void setIsYieldCropInd(Boolean isYieldCropInd) {
		this.isYieldCropInd = isYieldCropInd;
	}

	public Boolean getIsUnderwritingCropInd() {
		return isUnderwritingCropInd;
	}

	public void setIsUnderwritingCropInd(Boolean isUnderwritingCropInd) {
		this.isUnderwritingCropInd = isUnderwritingCropInd;
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

	public String getInventoryTypeCode() {
		return inventoryTypeCode;
	}

	public void setInventoryTypeCode(String inventoryTypeCode) {
		this.inventoryTypeCode = inventoryTypeCode;
	}

	public String getYieldTypeCode() {
		return yieldTypeCode;
	}

	public void setYieldTypeCode(String yieldTypeCode) {
		this.yieldTypeCode = yieldTypeCode;
	}
	
	public List<CropVariety> getCropVariety(){
		return this.cropVarietyList;
	};
	
	public void setCropVariety(List<CropVariety> varieties) {
		this.cropVarietyList = varieties;
	};
}
