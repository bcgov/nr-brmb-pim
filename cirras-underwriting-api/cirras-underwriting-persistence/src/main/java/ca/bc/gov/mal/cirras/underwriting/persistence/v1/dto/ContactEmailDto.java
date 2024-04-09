package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ContactEmailDto extends BaseDto<ContactEmailDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ContactEmailDto.class);

	private Integer contactEmailId;
	private Integer contactId;
	private String emailAddress;
	private Boolean isPrimaryEmailInd;
	private Date effectiveDate;
	private Date expiryDate;
	private Date dataSyncTransDate;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	public ContactEmailDto() {
	}
	
	
	public ContactEmailDto(ContactEmailDto dto) {

		this.contactEmailId = dto.contactEmailId;
		this.contactId = dto.contactId;
		this.emailAddress = dto.emailAddress;
		this.isPrimaryEmailInd = dto.isPrimaryEmailInd;
		this.effectiveDate = dto.effectiveDate;
		this.expiryDate = dto.expiryDate;
		this.dataSyncTransDate = dto.dataSyncTransDate;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(ContactEmailDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ContactEmailDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("contactEmailId", contactEmailId, other.contactEmailId);
			result = result&&dtoUtils.equals("contactId", contactId, other.contactId);
			result = result&&dtoUtils.equals("emailAddress", emailAddress, other.emailAddress);
			result = result&&dtoUtils.equals("isPrimaryEmailInd", isPrimaryEmailInd, other.isPrimaryEmailInd);
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
	public ContactEmailDto copy() {
		return new ContactEmailDto(this);
	}

 	public Integer getContactEmailId() {
		return contactEmailId;
	}

	public void setContactEmailId(Integer contactEmailId) {
		this.contactEmailId = contactEmailId;
	}
 
 	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
 
 	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
 
 	public Boolean getIsPrimaryEmailInd() {
		return isPrimaryEmailInd;
	}

	public void setIsPrimaryEmailInd(Boolean isPrimaryEmailInd) {
		this.isPrimaryEmailInd = isPrimaryEmailInd;
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
