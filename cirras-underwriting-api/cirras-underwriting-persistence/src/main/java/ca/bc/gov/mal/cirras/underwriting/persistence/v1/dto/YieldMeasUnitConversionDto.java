package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class YieldMeasUnitConversionDto extends BaseDto<YieldMeasUnitConversionDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(YieldMeasUnitConversionDto.class);


	private String yieldMeasUnitConversionGuid;
	private Integer cropCommodityId;
	private String srcYieldMeasUnitTypeCode;
	private String targetYieldMeasUnitTypeCode;
	private Integer versionNumber;
	private Integer effectiveCropYear;
	private Integer expiryCropYear;
	private Double conversionFactor;
		
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	//Extended columns
	private Integer insurancePlanId;
	private String commodityName;

	
	public YieldMeasUnitConversionDto() {
	}
	
	
	public YieldMeasUnitConversionDto(YieldMeasUnitConversionDto dto) {

		this.yieldMeasUnitConversionGuid = dto.yieldMeasUnitConversionGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.srcYieldMeasUnitTypeCode = dto.srcYieldMeasUnitTypeCode;
		this.targetYieldMeasUnitTypeCode = dto.targetYieldMeasUnitTypeCode;
		this.versionNumber = dto.versionNumber;
		this.effectiveCropYear = dto.effectiveCropYear;
		this.expiryCropYear = dto.expiryCropYear;
		this.conversionFactor = dto.conversionFactor;
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.insurancePlanId = dto.insurancePlanId;
		this.commodityName = dto.commodityName;

	}
	

	@Override
	public boolean equalsBK(YieldMeasUnitConversionDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(YieldMeasUnitConversionDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());

			result = result&&dtoUtils.equals("yieldMeasUnitConversionGuid", yieldMeasUnitConversionGuid, other.yieldMeasUnitConversionGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("srcYieldMeasUnitTypeCode", srcYieldMeasUnitTypeCode, other.srcYieldMeasUnitTypeCode);
			result = result&&dtoUtils.equals("targetYieldMeasUnitTypeCode", targetYieldMeasUnitTypeCode, other.targetYieldMeasUnitTypeCode);
			result = result&&dtoUtils.equals("versionNumber", versionNumber, other.versionNumber);
			result = result&&dtoUtils.equals("effectiveCropYear", effectiveCropYear, other.effectiveCropYear);
			result = result&&dtoUtils.equals("expiryCropYear", expiryCropYear, other.expiryCropYear);
			result = result&&dtoUtils.equals("conversionFactor", conversionFactor, other.conversionFactor, 4);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public YieldMeasUnitConversionDto copy() {
		return new YieldMeasUnitConversionDto(this);
	}

	public String getYieldMeasUnitConversionGuid() {
		return yieldMeasUnitConversionGuid;
	}
	public void setYieldMeasUnitConversionGuid(String yieldMeasUnitConversionGuid) {
		this.yieldMeasUnitConversionGuid = yieldMeasUnitConversionGuid;
	}


	public Integer getCropCommodityId() {
		return cropCommodityId;
	}
	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}


	public String getSrcYieldMeasUnitTypeCode() {
		return srcYieldMeasUnitTypeCode;
	}
	public void setSrcYieldMeasUnitTypeCode(String srcYieldMeasUnitTypeCode) {
		this.srcYieldMeasUnitTypeCode = srcYieldMeasUnitTypeCode;
	}


	public String getTargetYieldMeasUnitTypeCode() {
		return targetYieldMeasUnitTypeCode;
	}
	public void setTargetYieldMeasUnitTypeCode(String targetYieldMeasUnitTypeCode) {
		this.targetYieldMeasUnitTypeCode = targetYieldMeasUnitTypeCode;
	}


	public Integer getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}


	public Integer getEffectiveCropYear() {
		return effectiveCropYear;
	}
	public void setEffectiveCropYear(Integer effectiveCropYear) {
		this.effectiveCropYear = effectiveCropYear;
	}


	public Integer getExpiryCropYear() {
		return expiryCropYear;
	}
	public void setExpiryCropYear(Integer expiryCropYear) {
		this.expiryCropYear = expiryCropYear;
	}


	public Double getConversionFactor() {
		return conversionFactor;
	}
	public void setConversionFactor(Double conversionFactor) {
		this.conversionFactor = conversionFactor;
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


}
