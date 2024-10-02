package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class CropVarietyDto extends BaseDto<CropVarietyDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CropVarietyDto.class);

	private Integer cropVarietyId;
	private Integer cropCommodityId;
	private String varietyName;
	private Date effectiveDate;
	private Date expiryDate;
	private Date dataSyncTransDate;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	private Integer insurancePlanId;
	private Boolean isQuantityInsurableInd;
	private Boolean isUnseededInsurableInd;
	private Boolean isPlantInsurableInd;
	private Boolean isAwpEligibleInd;
	private Boolean isUnderseedingEligibleInd;
	private Boolean isGrainUnseededDefaultInd;
	
	public CropVarietyDto() {
	}
	
	
	public CropVarietyDto(CropVarietyDto dto) {

		this.cropVarietyId = dto.cropVarietyId;
		this.cropCommodityId = dto.cropCommodityId;
		this.varietyName = dto.varietyName;
		this.effectiveDate = dto.effectiveDate;
		this.expiryDate = dto.expiryDate;
		this.dataSyncTransDate = dto.dataSyncTransDate;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		
		this.insurancePlanId = dto.insurancePlanId;
		this.isQuantityInsurableInd = dto.isQuantityInsurableInd;
		this.isUnseededInsurableInd = dto.isUnseededInsurableInd;
		this.isPlantInsurableInd = dto.isPlantInsurableInd;
		this.isAwpEligibleInd = dto.isAwpEligibleInd;
		this.isUnderseedingEligibleInd = dto.isUnderseedingEligibleInd;
		this.isGrainUnseededDefaultInd = dto.isGrainUnseededDefaultInd;
	}
	

	@Override
	public boolean equalsBK(CropVarietyDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(CropVarietyDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("cropVarietyId", cropVarietyId, other.cropVarietyId);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("varietyName", varietyName, other.varietyName);
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
	public CropVarietyDto copy() {
		return new CropVarietyDto(this);
	}
	
	public Integer getCropVarietyId() {
		return cropVarietyId;
	}

	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	}
	
	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public String getVarietyName() {
		return varietyName;
	}

	public void setVarietyName(String varietyName) {
		this.varietyName = varietyName;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
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

}
