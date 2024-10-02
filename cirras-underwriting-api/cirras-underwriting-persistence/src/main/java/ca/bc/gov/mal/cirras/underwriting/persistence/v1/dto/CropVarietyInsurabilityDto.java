package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class CropVarietyInsurabilityDto extends BaseDto<CropVarietyInsurabilityDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CropVarietyInsurabilityDto.class);

	private String cropVarietyInsurabilityGuid;
	private Integer cropVarietyId;
	private Boolean isQuantityInsurableInd;
	private Boolean isUnseededInsurableInd;
	private Boolean isPlantInsurableInd;
	private Boolean isAwpEligibleInd;
	private Boolean isUnderseedingEligibleInd;
	private Boolean isGrainUnseededDefaultInd;
	 
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	private String plantDurationTypeCode;
	private String varietyName;

	private Boolean isQuantityInsurableEditableInd;
	private Boolean isUnseededInsurableEditableInd;
	private Boolean isPlantInsurableEditableInd;
	private Boolean isAwpEligibleEditableInd;
	private Boolean isUnderseedingEligibleEditableInd;
	
	public CropVarietyInsurabilityDto() {
	}
	
	
	public CropVarietyInsurabilityDto(CropVarietyInsurabilityDto dto) {

		this.cropVarietyInsurabilityGuid = dto.cropVarietyInsurabilityGuid;
		this.cropVarietyId = dto.cropVarietyId;
		this.isQuantityInsurableInd = dto.isQuantityInsurableInd;
		this.isUnseededInsurableInd = dto.isUnseededInsurableInd;
		this.isPlantInsurableInd = dto.isPlantInsurableInd;
		this.isAwpEligibleInd = dto.isAwpEligibleInd;
		this.isUnderseedingEligibleInd = dto.isUnderseedingEligibleInd;
		this.isGrainUnseededDefaultInd = dto.isGrainUnseededDefaultInd;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		
		this.plantDurationTypeCode = dto.plantDurationTypeCode;
		this.varietyName = dto.varietyName;
		this.isQuantityInsurableEditableInd = dto.isQuantityInsurableEditableInd;
		this.isUnseededInsurableEditableInd = dto.isUnseededInsurableEditableInd;
		this.isPlantInsurableEditableInd = dto.isPlantInsurableEditableInd;
		this.isAwpEligibleEditableInd = dto.isAwpEligibleEditableInd;
		this.isUnderseedingEligibleEditableInd = dto.isUnderseedingEligibleEditableInd;
		
	}
	

	@Override
	public boolean equalsBK(CropVarietyInsurabilityDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(CropVarietyInsurabilityDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("cropVarietyInsurabilityGuid", cropVarietyInsurabilityGuid, other.cropVarietyInsurabilityGuid);
			result = result&&dtoUtils.equals("cropVarietyId", cropVarietyId, other.cropVarietyId);
			result = result&&dtoUtils.equals("isQuantityInsurableInd", isQuantityInsurableInd, other.isQuantityInsurableInd);
			result = result&&dtoUtils.equals("isUnseededInsurableInd", isUnseededInsurableInd, other.isUnseededInsurableInd);
			result = result&&dtoUtils.equals("isPlantInsurableInd", isPlantInsurableInd, other.isPlantInsurableInd);
			result = result&&dtoUtils.equals("isAwpEligibleInd", isAwpEligibleInd, other.isAwpEligibleInd);
			result = result&&dtoUtils.equals("isUnderseedingEligibleInd", isUnderseedingEligibleInd, other.isUnderseedingEligibleInd);
			result = result&&dtoUtils.equals("isGrainUnseededDefaultInd", isGrainUnseededDefaultInd, other.isGrainUnseededDefaultInd);

		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public CropVarietyInsurabilityDto copy() {
		return new CropVarietyInsurabilityDto(this);
	}
	
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
