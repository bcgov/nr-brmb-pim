package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class InsurancePlanDto extends BaseDto<InsurancePlanDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InsurancePlanDto.class);

	private Integer insurancePlanId;
	private String insurancePlanName;
	private Date effectiveDate;
	private Date expiryDate;
	private Date dataSyncTransDate;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
		
	public InsurancePlanDto() {
	}
	
	
	public InsurancePlanDto(InsurancePlanDto dto) {

		this.insurancePlanId = dto.insurancePlanId;
		this.insurancePlanName = dto.insurancePlanName;
		this.effectiveDate = dto.effectiveDate;
		this.expiryDate = dto.expiryDate;
		this.dataSyncTransDate = dto.dataSyncTransDate;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(InsurancePlanDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(InsurancePlanDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("insurancePlanId", insurancePlanId, other.insurancePlanId);
			result = result&&dtoUtils.equals("insurancePlanName", this.insurancePlanName, other.insurancePlanName);
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
	public InsurancePlanDto copy() {
		return new InsurancePlanDto(this);
	}
	
	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
	
	public String getInsurancePlanName() {
		return insurancePlanName;
	}

	public void setInsurancePlanName(String insurancePlanName) {
		this.insurancePlanName = insurancePlanName;
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
