package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class YieldMeasUnitTypeCodeDto extends BaseDto<YieldMeasUnitTypeCodeDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(YieldMeasUnitTypeCodeDto.class);

	private String yieldMeasUnitTypeCode;
	private String description;
	private Integer decimalPrecision;
	private Date effectiveDate;
	private Date expiryDate;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	private Boolean isDefaultYieldUnitInd;
	private Integer insurancePlanId;

	public YieldMeasUnitTypeCodeDto() {
	}
	
	
	public YieldMeasUnitTypeCodeDto(YieldMeasUnitTypeCodeDto dto) {

		this.yieldMeasUnitTypeCode = dto.yieldMeasUnitTypeCode;
		this.description = dto.description;
		this.decimalPrecision = dto.decimalPrecision;
		this.effectiveDate = dto.effectiveDate;
		this.expiryDate = dto.expiryDate;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.isDefaultYieldUnitInd = dto.isDefaultYieldUnitInd;
		this.insurancePlanId = dto.insurancePlanId;
	}
	

	@Override
	public boolean equalsBK(YieldMeasUnitTypeCodeDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(YieldMeasUnitTypeCodeDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("yieldMeasUnitTypeCode", yieldMeasUnitTypeCode, other.yieldMeasUnitTypeCode);
			result = result&&dtoUtils.equals("description", description, other.description);
			result = result&&dtoUtils.equals("decimalPrecision", decimalPrecision, other.decimalPrecision);
			result = result&&dtoUtils.equals("effectiveDate",
					LocalDateTime.ofInstant(effectiveDate.toInstant(), ZoneId.systemDefault()), 
					LocalDateTime.ofInstant(other.effectiveDate.toInstant(), ZoneId.systemDefault()));
			result = result&&dtoUtils.equals("expiryDate",
					LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()), 
					LocalDateTime.ofInstant(other.expiryDate.toInstant(), ZoneId.systemDefault()));
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public YieldMeasUnitTypeCodeDto copy() {
		return new YieldMeasUnitTypeCodeDto(this);
	}
	
 	public String getYieldMeasUnitTypeCode() {
		return yieldMeasUnitTypeCode;
	}

	public void setYieldMeasUnitTypeCode(String yieldMeasUnitTypeCode) {
		this.yieldMeasUnitTypeCode = yieldMeasUnitTypeCode;
	}
 
 	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
 
 	public Integer getDecimalPrecision() {
		return decimalPrecision;
	}

	public void setDecimalPrecision(Integer decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
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

 	public Boolean getIsDefaultYieldUnitInd() {
		return isDefaultYieldUnitInd;
	}

	public void setIsDefaultYieldUnitInd(Boolean isDefaultYieldUnitInd) {
		this.isDefaultYieldUnitInd = isDefaultYieldUnitInd;
	}
 
 	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
 

}
