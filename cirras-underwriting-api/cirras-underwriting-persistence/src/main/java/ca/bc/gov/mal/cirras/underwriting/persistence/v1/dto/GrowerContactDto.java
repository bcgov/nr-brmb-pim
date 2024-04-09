package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class GrowerContactDto extends BaseDto<GrowerContactDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(GrowerContactDto.class);

	private Integer growerContactId;
	private Integer growerId;
	private Integer contactId;
	private Boolean isPrimaryContactInd;
	private Boolean isActivelyInvolvedInd;
	private Date dataSyncTransDate;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	public GrowerContactDto() {
	}
	
	
	public GrowerContactDto(GrowerContactDto dto) {

		this.growerContactId = dto.growerContactId;
		this.growerId = dto.growerId;
		this.contactId = dto.contactId;
		this.isPrimaryContactInd = dto.isPrimaryContactInd;
		this.isActivelyInvolvedInd = dto.isActivelyInvolvedInd;
		this.dataSyncTransDate = dto.dataSyncTransDate;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(GrowerContactDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(GrowerContactDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("growerContactId", growerContactId, other.growerContactId);
			result = result&&dtoUtils.equals("growerId", growerId, other.growerId);
			result = result&&dtoUtils.equals("contactId", contactId, other.contactId);
			result = result&&dtoUtils.equals("isPrimaryContactInd", isPrimaryContactInd, other.isPrimaryContactInd);
			result = result&&dtoUtils.equals("isActivelyInvolvedInd", isActivelyInvolvedInd, other.isActivelyInvolvedInd);
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
	public GrowerContactDto copy() {
		return new GrowerContactDto(this);
	}

 	public Integer getGrowerContactId() {
		return growerContactId;
	}

	public void setGrowerContactId(Integer growerContactId) {
		this.growerContactId = growerContactId;
	}
 
 	public Integer getGrowerId() {
		return growerId;
	}

	public void setGrowerId(Integer growerId) {
		this.growerId = growerId;
	}
 
 	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
 
 	public Boolean getIsPrimaryContactInd() {
		return isPrimaryContactInd;
	}

	public void setIsPrimaryContactInd(Boolean isPrimaryContactInd) {
		this.isPrimaryContactInd = isPrimaryContactInd;
	}
 
 	public Boolean getIsActivelyInvolvedInd() {
		return isActivelyInvolvedInd;
	}

	public void setIsActivelyInvolvedInd(Boolean isActivelyInvolvedInd) {
		this.isActivelyInvolvedInd = isActivelyInvolvedInd;
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
