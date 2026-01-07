package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class YieldMeasUnitPlanXrefDto extends BaseDto<YieldMeasUnitPlanXrefDto> {


	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(YieldMeasUnitPlanXrefDto.class);

	private String yieldMeasUnitPlanXrefGuid;
	private String yieldMeasUnitTypeCode;
	private Integer insurancePlanId;
	private Boolean isDefaultYieldUnitInd;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;


	public YieldMeasUnitPlanXrefDto() {
	}
	
	
	public YieldMeasUnitPlanXrefDto(YieldMeasUnitPlanXrefDto dto) {

		this.yieldMeasUnitPlanXrefGuid = dto.yieldMeasUnitPlanXrefGuid;
		this.yieldMeasUnitTypeCode = dto.yieldMeasUnitTypeCode;
		this.insurancePlanId = dto.insurancePlanId;
		this.isDefaultYieldUnitInd = dto.isDefaultYieldUnitInd;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	}
	

	@Override
	public boolean equalsBK(YieldMeasUnitPlanXrefDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(YieldMeasUnitPlanXrefDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("yieldMeasUnitPlanXrefGuid", yieldMeasUnitPlanXrefGuid, other.yieldMeasUnitPlanXrefGuid);
			result = result&&dtoUtils.equals("yieldMeasUnitTypeCode", yieldMeasUnitTypeCode, other.yieldMeasUnitTypeCode);
			result = result&&dtoUtils.equals("insurancePlanId", insurancePlanId, other.insurancePlanId);
			result = result&&dtoUtils.equals("isDefaultYieldUnitInd", isDefaultYieldUnitInd, other.isDefaultYieldUnitInd);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public YieldMeasUnitPlanXrefDto copy() {
		return new YieldMeasUnitPlanXrefDto(this);
	}
	
 	public String getYieldMeasUnitPlanXrefGuid() {
		return yieldMeasUnitPlanXrefGuid;
	}

	public void setYieldMeasUnitPlanXrefGuid(String yieldMeasUnitPlanXrefGuid) {
		this.yieldMeasUnitPlanXrefGuid = yieldMeasUnitPlanXrefGuid;
	}
 
 	public String getYieldMeasUnitTypeCode() {
		return yieldMeasUnitTypeCode;
	}

	public void setYieldMeasUnitTypeCode(String yieldMeasUnitTypeCode) {
		this.yieldMeasUnitTypeCode = yieldMeasUnitTypeCode;
	}
 
 	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
 
 	public Boolean getIsDefaultYieldUnitInd() {
		return isDefaultYieldUnitInd;
	}

	public void setIsDefaultYieldUnitInd(Boolean isDefaultYieldUnitInd) {
		this.isDefaultYieldUnitInd = isDefaultYieldUnitInd;
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
