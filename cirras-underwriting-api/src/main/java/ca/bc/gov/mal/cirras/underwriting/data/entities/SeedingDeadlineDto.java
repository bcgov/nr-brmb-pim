package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class SeedingDeadlineDto extends BaseDto<SeedingDeadlineDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(SeedingDeadlineDto.class);

	private String seedingDeadlineGuid;
	private String commodityTypeCode;
	private Integer cropYear;
	private Date fullCoverageDeadlineDate;
	private Date finalCoverageDeadlineDate;
	private Date fullCoverageDeadlineDateDefault;
	private Date finalCoverageDeadlineDateDefault;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public SeedingDeadlineDto() {
	}
	
	
	public SeedingDeadlineDto(SeedingDeadlineDto dto) {

		this.seedingDeadlineGuid = dto.seedingDeadlineGuid;
		this.commodityTypeCode = dto.commodityTypeCode;
		this.cropYear = dto.cropYear;
		this.fullCoverageDeadlineDate = dto.fullCoverageDeadlineDate;
		this.finalCoverageDeadlineDate = dto.finalCoverageDeadlineDate;
		this.fullCoverageDeadlineDateDefault = dto.fullCoverageDeadlineDateDefault;
		this.finalCoverageDeadlineDateDefault = dto.finalCoverageDeadlineDateDefault;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;	
	}
	

	@Override
	public boolean equalsBK(SeedingDeadlineDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(SeedingDeadlineDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("seedingDeadlineGuid", seedingDeadlineGuid, other.seedingDeadlineGuid);
			result = result&&dtoUtils.equals("commodityTypeCode", commodityTypeCode, other.commodityTypeCode);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);
			result = result&&dtoUtils.equals("fullCoverageDeadlineDate",
					LocalDateTime.ofInstant(fullCoverageDeadlineDate.toInstant(), ZoneId.systemDefault()), 
					LocalDateTime.ofInstant(other.fullCoverageDeadlineDate.toInstant(), ZoneId.systemDefault()));
			result = result&&dtoUtils.equals("finalCoverageDeadlineDate",
					LocalDateTime.ofInstant(finalCoverageDeadlineDate.toInstant(), ZoneId.systemDefault()), 
					LocalDateTime.ofInstant(other.finalCoverageDeadlineDate.toInstant(), ZoneId.systemDefault()));
			result = result&&dtoUtils.equals("fullCoverageDeadlineDateDefault",
					LocalDateTime.ofInstant(fullCoverageDeadlineDateDefault.toInstant(), ZoneId.systemDefault()), 
					LocalDateTime.ofInstant(other.fullCoverageDeadlineDateDefault.toInstant(), ZoneId.systemDefault()));
			result = result&&dtoUtils.equals("finalCoverageDeadlineDateDefault",
					LocalDateTime.ofInstant(finalCoverageDeadlineDateDefault.toInstant(), ZoneId.systemDefault()), 
					LocalDateTime.ofInstant(other.finalCoverageDeadlineDateDefault.toInstant(), ZoneId.systemDefault()));
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public SeedingDeadlineDto copy() {
		return new SeedingDeadlineDto(this);
	}

	public String getSeedingDeadlineGuid() {
		return seedingDeadlineGuid;
	}
	public void setSeedingDeadlineGuid(String seedingDeadlineGuid) {
		this.seedingDeadlineGuid = seedingDeadlineGuid;
	}

	public String getCommodityTypeCode() {
		return commodityTypeCode;
	}
	public void setCommodityTypeCode(String commodityTypeCode) {
		this.commodityTypeCode = commodityTypeCode;
	}

	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public Date getFullCoverageDeadlineDate() {
		return fullCoverageDeadlineDate;
	}
	public void setFullCoverageDeadlineDate(Date fullCoverageDeadlineDate) {
		this.fullCoverageDeadlineDate = fullCoverageDeadlineDate;
	}

	public Date getFinalCoverageDeadlineDate() {
		return finalCoverageDeadlineDate;
	}
	public void setFinalCoverageDeadlineDate(Date finalCoverageDeadlineDate) {
		this.finalCoverageDeadlineDate = finalCoverageDeadlineDate;
	}

	public Date getFullCoverageDeadlineDateDefault() {
		return fullCoverageDeadlineDateDefault;
	}

	public void setFullCoverageDeadlineDateDefault(Date fullCoverageDeadlineDateDefault) {
		this.fullCoverageDeadlineDateDefault = fullCoverageDeadlineDateDefault;
	}

	public Date getFinalCoverageDeadlineDateDefault() {
		return finalCoverageDeadlineDateDefault;
	}

	public void setFinalCoverageDeadlineDateDefault(Date finalCoverageDeadlineDateDefault) {
		this.finalCoverageDeadlineDateDefault = finalCoverageDeadlineDateDefault;
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
