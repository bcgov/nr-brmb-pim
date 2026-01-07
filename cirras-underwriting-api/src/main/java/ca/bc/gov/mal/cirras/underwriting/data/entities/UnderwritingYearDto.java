package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class UnderwritingYearDto extends BaseDto<UnderwritingYearDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(UnderwritingYearDto.class);

	private String underwritingYearGuid;
	private Integer cropYear;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public UnderwritingYearDto() {
	}
	
	
	public UnderwritingYearDto(UnderwritingYearDto dto) {

		this.underwritingYearGuid = dto.underwritingYearGuid;
		this.cropYear = dto.cropYear;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;	
	}
	

	@Override
	public boolean equalsBK(UnderwritingYearDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(UnderwritingYearDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("underwritingYearGuid", underwritingYearGuid, other.underwritingYearGuid);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public UnderwritingYearDto copy() {
		return new UnderwritingYearDto(this);
	}

	public String getUnderwritingYearGuid() {
		return underwritingYearGuid;
	}
	
	public void setUnderwritingYearGuid(String underwritingYearGuid) {
		this.underwritingYearGuid = underwritingYearGuid;
	}

	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
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
