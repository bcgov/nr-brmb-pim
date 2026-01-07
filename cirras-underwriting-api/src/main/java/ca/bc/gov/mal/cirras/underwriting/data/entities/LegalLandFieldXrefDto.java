package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class LegalLandFieldXrefDto extends BaseDto<LegalLandFieldXrefDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(LegalLandFieldXrefDto.class);

	private Integer legalLandId;
	private Integer fieldId;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	public LegalLandFieldXrefDto() {
	}
	
	
	public LegalLandFieldXrefDto(LegalLandFieldXrefDto dto) {

		this.legalLandId = dto.legalLandId;
		this.fieldId = dto.fieldId;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(LegalLandFieldXrefDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(LegalLandFieldXrefDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("legalLandId", legalLandId, other.legalLandId);
			result = result&&dtoUtils.equals("fieldId", fieldId, other.fieldId);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public LegalLandFieldXrefDto copy() {
		return new LegalLandFieldXrefDto(this);
	}

 	public Integer getLegalLandId() {
		return legalLandId;
	}

	public void setLegalLandId(Integer legalLandId) {
		this.legalLandId = legalLandId;
	}
 
 	public Integer getFieldId() {
		return fieldId;
	}

	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
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
