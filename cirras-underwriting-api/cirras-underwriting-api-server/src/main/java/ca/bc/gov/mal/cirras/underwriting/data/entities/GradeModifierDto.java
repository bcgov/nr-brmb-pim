package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class GradeModifierDto extends BaseDto<GradeModifierDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(GradeModifierDto.class);

	private String gradeModifierGuid;
	private Integer cropCommodityId;
	private Integer cropYear;
	private String gradeModifierTypeCode;
	private Double gradeModifierValue;
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	//Joined columns
	private String description;
	private Integer insurancePlanId;
	private Integer totalUsed;
	
	public GradeModifierDto() {
	}
	
	
	public GradeModifierDto(GradeModifierDto dto) {

		this.gradeModifierGuid = dto.gradeModifierGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.cropYear = dto.cropYear;
		this.gradeModifierTypeCode = dto.gradeModifierTypeCode;
		this.gradeModifierValue = dto.gradeModifierValue;
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.description = dto.description;
		this.insurancePlanId = dto.insurancePlanId;
		this.totalUsed = dto.totalUsed;
	}
	

	@Override
	public boolean equalsBK(GradeModifierDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(GradeModifierDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("gradeModifierGuid", gradeModifierGuid, other.gradeModifierGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);
			result = result&&dtoUtils.equals("gradeModifierTypeCode", gradeModifierTypeCode, other.gradeModifierTypeCode);
			result = result&&dtoUtils.equals("gradeModifierValue", gradeModifierValue, other.gradeModifierValue, 4);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public GradeModifierDto copy() {
		return new GradeModifierDto(this);
	}

	public String getGradeModifierGuid() {
		return gradeModifierGuid;
	}

	public void setGradeModifierGuid(String gradeModifierGuid) {
		this.gradeModifierGuid = gradeModifierGuid;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Integer getCropYear() {
		return cropYear;
	}

	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public String getGradeModifierTypeCode() {
		return gradeModifierTypeCode;
	}

	public void setGradeModifierTypeCode(String gradeModifierTypeCode) {
		this.gradeModifierTypeCode = gradeModifierTypeCode;
	}

	public Double getGradeModifierValue() {
		return gradeModifierValue;
	}

	public void setGradeModifierValue(Double gradeModifierValue) {
		this.gradeModifierValue = gradeModifierValue;
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

 	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
	
	public Integer getTotalUsed() {
		return totalUsed;
	}
	
	public void setTotalUsed(Integer totalUsed) {
		this.totalUsed = totalUsed;
	}


		
}
