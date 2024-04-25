package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.utils.DateUtils;
import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class InventorySeededForageDto extends BaseDto<InventorySeededForageDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventorySeededForageDto.class);

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

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	//Contains linked planting information
	private String linkedInventoryFieldGuid;
	private Integer linkedUnderseededCropVarietyId;
	private String linkedVarietyName;
	private Double linkedUnderseededAcres;
	
	private String commodityTypeDescription;
	private Double totalFieldAcres;
	private String plantDurationTypeCode;

	public InventorySeededForageDto() {
	}
	
	
	public InventorySeededForageDto(InventorySeededForageDto dto) {

		this.inventorySeededForageGuid = dto.inventorySeededForageGuid;
		this.inventoryFieldGuid = dto.inventoryFieldGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.cropVarietyId = dto.cropVarietyId;
		this.cropVarietyName = dto.cropVarietyName;
		this.commodityTypeCode = dto.commodityTypeCode;
		this.fieldAcres = dto.fieldAcres;
		this.seedingYear = dto.seedingYear;
		this.seedingDate = dto.seedingDate;
		this.isIrrigatedInd = dto.isIrrigatedInd;
		this.isQuantityInsurableInd = dto.isQuantityInsurableInd;
		this.plantInsurabilityTypeCode = dto.plantInsurabilityTypeCode;
		this.isAwpEligibleInd = dto.isAwpEligibleInd;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		
		this.linkedInventoryFieldGuid = dto.linkedInventoryFieldGuid;
		this.linkedUnderseededCropVarietyId = dto.linkedUnderseededCropVarietyId;
		this.linkedVarietyName = dto.linkedVarietyName;
		this.linkedUnderseededAcres = dto.linkedUnderseededAcres;

		this.commodityTypeDescription = dto.commodityTypeDescription;
		this.totalFieldAcres = dto.totalFieldAcres;
		this.plantDurationTypeCode = dto.plantDurationTypeCode;
	}
	

	@Override
	public boolean equalsBK(InventorySeededForageDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(InventorySeededForageDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("inventorySeededForageGuid", inventorySeededForageGuid, other.inventorySeededForageGuid);
			result = result&&dtoUtils.equals("inventoryFieldGuid", inventoryFieldGuid, other.inventoryFieldGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("cropVarietyId", cropVarietyId, other.cropVarietyId);
			result = result&&dtoUtils.equals("commodityTypeCode", commodityTypeCode, other.commodityTypeCode);
			result = result&&dtoUtils.equals("fieldAcres", fieldAcres, other.fieldAcres, 4);
			result = result&&dtoUtils.equals("seedingYear", seedingYear, other.seedingYear);
			result = result&&dtoUtils.equals("isIrrigatedInd", isIrrigatedInd, other.isIrrigatedInd);
			result = result&&dtoUtils.equals("isQuantityInsurableInd", isQuantityInsurableInd, other.isQuantityInsurableInd);
			result = result&&dtoUtils.equals("plantInsurabilityTypeCode", plantInsurabilityTypeCode, other.plantInsurabilityTypeCode);
			result = result&&dtoUtils.equals("isAwpEligibleInd", isAwpEligibleInd, other.isAwpEligibleInd);
			//Nullable date
			result = result&&DateUtils.equalsDate(logger, "seedingDate", seedingDate, other.seedingDate);
			
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public InventorySeededForageDto copy() {
		return new InventorySeededForageDto(this);
	}
	
	public String getCropVarietyName() {
		return cropVarietyName;
	}
	public void setCropVarietyName(String cropVarietyName) {
		this.cropVarietyName = cropVarietyName;
	}

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

	public String getLinkedInventoryFieldGuid() {
		return linkedInventoryFieldGuid;
	}

	public void setLinkedInventoryFieldGuid(String linkedInventoryFieldGuid) {
		this.linkedInventoryFieldGuid = linkedInventoryFieldGuid;
	}

	public Integer getLinkedUnderseededCropVarietyId() {
		return linkedUnderseededCropVarietyId;
	}

	public void setLinkedUnderseededCropVarietyId(Integer linkedUnderseededCropVarietyId) {
		this.linkedUnderseededCropVarietyId = linkedUnderseededCropVarietyId;
	}

	public String getLinkedVarietyName() {
		return linkedVarietyName;
	}

	public void setLinkedVarietyName(String linkedVarietyName) {
		this.linkedVarietyName = linkedVarietyName;
	}

	public Double getLinkedUnderseededAcres() {
		return linkedUnderseededAcres;
	}

	public void setLinkedUnderseededAcres(Double linkedUnderseededAcres) {
		this.linkedUnderseededAcres = linkedUnderseededAcres;
	}

	public String getCommodityTypeDescription() {
		return commodityTypeDescription;
	}

	public void setCommodityTypeDescription(String commodityTypeDescription) {
		this.commodityTypeDescription = commodityTypeDescription;
	}

	public Double getTotalFieldAcres() {
		return totalFieldAcres;
	}
	
	public void setTotalFieldAcres(Double totalFieldAcres) {
		this.totalFieldAcres = totalFieldAcres;
	}

	public String getPlantDurationTypeCode() {
		return plantDurationTypeCode;
	}

	public void setPlantDurationTypeCode(String plantDurationTypeCode) {
		this.plantDurationTypeCode = plantDurationTypeCode;
	}

}
