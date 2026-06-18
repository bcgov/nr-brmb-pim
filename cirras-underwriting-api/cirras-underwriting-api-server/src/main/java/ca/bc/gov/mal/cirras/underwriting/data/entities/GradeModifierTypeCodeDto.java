package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class GradeModifierTypeCodeDto extends BaseDto<GradeModifierTypeCodeDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(GradeModifierTypeCodeDto.class);

	private String gradeModifierTypeCode;
	private String description;
	private Integer effectiveYear;
	private Integer expiryYear;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	//Joined columns
	private Integer totalUsed;
	private Integer maxYearUsed;

	public GradeModifierTypeCodeDto() {
	}
	
	
	public GradeModifierTypeCodeDto(GradeModifierTypeCodeDto dto) {

		this.gradeModifierTypeCode = dto.gradeModifierTypeCode;
		this.effectiveYear = dto.effectiveYear;
		this.expiryYear = dto.expiryYear;
		this.description = dto.description;
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.totalUsed = dto.totalUsed;
		this.maxYearUsed = dto.maxYearUsed;

	}
	

	@Override
	public boolean equalsBK(GradeModifierTypeCodeDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(GradeModifierTypeCodeDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("gradeModifierTypeCode", gradeModifierTypeCode, other.gradeModifierTypeCode);
			result = result&&dtoUtils.equals("description", description, other.description);
			result = result&&dtoUtils.equals("effectiveYear", effectiveYear, other.effectiveYear);
			result = result&&dtoUtils.equals("expiryYear", expiryYear, other.expiryYear);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public GradeModifierTypeCodeDto copy() {
		return new GradeModifierTypeCodeDto(this);
	}

	public String getGradeModifierTypeCode() {
		return gradeModifierTypeCode;
	}

	public void setGradeModifierTypeCode(String gradeModifierTypeCode) {
		this.gradeModifierTypeCode = gradeModifierTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getEffectiveYear() {
		return effectiveYear;
	}

	public void setEffectiveYear(Integer effectiveYear) {
		this.effectiveYear = effectiveYear;
	}

	public Integer getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(Integer expiryYear) {
		this.expiryYear = expiryYear;
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


	public Integer getTotalUsed() {
		return totalUsed;
	}

	public void setTotalUsed(Integer totalUsed) {
		this.totalUsed = totalUsed;
	}

	public Integer getMaxYearUsed() {
		return maxYearUsed;
	}

	public void setMaxYearUsed(Integer maxYearUsed) {
		this.maxYearUsed = maxYearUsed;
	}
	
}
