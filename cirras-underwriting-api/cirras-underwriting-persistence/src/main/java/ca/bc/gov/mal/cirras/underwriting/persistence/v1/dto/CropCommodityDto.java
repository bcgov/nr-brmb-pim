package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class CropCommodityDto extends BaseDto<CropCommodityDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CropCommodityDto.class);

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
	private Date effectiveDate;
	private Date expiryDate;
	private Date dataSyncTransDate;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	private String inventoryTypeCode;
	private String yieldTypeCode;

	
	public CropCommodityDto() {
	}
	
	
	public CropCommodityDto(CropCommodityDto dto) {

		this.cropCommodityId = dto.cropCommodityId;
		this.insurancePlanId = dto.insurancePlanId;
		this.commodityName = dto.commodityName;
		this.shortLabel = dto.shortLabel;
		this.plantDurationTypeCode = dto.plantDurationTypeCode;
		this.isInventoryCropInd = dto.isInventoryCropInd;
		this.isYieldCropInd = dto.isYieldCropInd;
		this.isUnderwritingCropInd = dto.isUnderwritingCropInd;
		this.isProductInsurableInd = dto.isProductInsurableInd;
		this.isCropInsuranceEligibleInd = dto.isCropInsuranceEligibleInd;
		this.isPlantInsuranceEligibleInd = dto.isPlantInsuranceEligibleInd;
		this.isOtherInsuranceEligibleInd = dto.isOtherInsuranceEligibleInd;
		this.yieldMeasUnitTypeCode = dto.yieldMeasUnitTypeCode;
		this.yieldDecimalPrecision = dto.yieldDecimalPrecision;
		this.effectiveDate = dto.effectiveDate;
		this.expiryDate = dto.expiryDate;
		this.dataSyncTransDate = dto.dataSyncTransDate;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.inventoryTypeCode = dto.inventoryTypeCode;
		this.yieldTypeCode = dto.yieldTypeCode;

	}
	

	@Override
	public boolean equalsBK(CropCommodityDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(CropCommodityDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("insurancePlanId", insurancePlanId, other.insurancePlanId);
			result = result&&dtoUtils.equals("commodityName", commodityName, other.commodityName);
			result = result&&dtoUtils.equals("shortLabel", shortLabel, other.shortLabel);
			result = result&&dtoUtils.equals("plantDurationTypeCode", plantDurationTypeCode, other.plantDurationTypeCode);
			result = result&&dtoUtils.equals("isInventoryCropInd", isInventoryCropInd, other.isInventoryCropInd);
			result = result&&dtoUtils.equals("isYieldCropInd", isYieldCropInd, other.isYieldCropInd);
			result = result&&dtoUtils.equals("isUnderwritingCropInd", isUnderwritingCropInd, other.isUnderwritingCropInd);
			result = result&&dtoUtils.equals("isProductInsurableInd", isProductInsurableInd, other.isProductInsurableInd);
			result = result&&dtoUtils.equals("isCropInsuranceEligibleInd", isCropInsuranceEligibleInd, other.isCropInsuranceEligibleInd);
			result = result&&dtoUtils.equals("isPlantInsuranceEligibleInd", isPlantInsuranceEligibleInd, other.isPlantInsuranceEligibleInd);
			result = result&&dtoUtils.equals("isOtherInsuranceEligibleInd", isOtherInsuranceEligibleInd, other.isOtherInsuranceEligibleInd);
			result = result&&dtoUtils.equals("yieldMeasUnitTypeCode", yieldMeasUnitTypeCode, other.yieldMeasUnitTypeCode);
			result = result&&dtoUtils.equals("yieldDecimalPrecision", yieldDecimalPrecision, other.yieldDecimalPrecision);
			result = result&&dtoUtils.equals("effectiveDate",
					LocalDateTime.ofInstant(effectiveDate.toInstant(), ZoneId.systemDefault()), 
					LocalDateTime.ofInstant(other.effectiveDate.toInstant(), ZoneId.systemDefault()));
			result = result&&dtoUtils.equals("expiryDate",
					LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()), 
					LocalDateTime.ofInstant(other.expiryDate.toInstant(), ZoneId.systemDefault()));
			result = result&&dtoUtils.equals("dataSyncTransDate",
					LocalDateTime.ofInstant(dataSyncTransDate.toInstant(), ZoneId.systemDefault()), 
					LocalDateTime.ofInstant(other.dataSyncTransDate.toInstant(), ZoneId.systemDefault()));
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public CropCommodityDto copy() {
		return new CropCommodityDto(this);
	}
	
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

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getDataSyncTransDate() {
		return dataSyncTransDate;
	}

	public void setDataSyncTransDate(Date dataSyncTransDate) {
		this.dataSyncTransDate = dataSyncTransDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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


}
