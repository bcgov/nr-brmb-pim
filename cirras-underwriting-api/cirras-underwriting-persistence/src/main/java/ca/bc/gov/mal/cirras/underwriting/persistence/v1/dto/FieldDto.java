package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class FieldDto extends BaseDto<FieldDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(LegalLandDto.class);

	private Integer fieldId;
	private String fieldLabel;
	private String location;
	private Integer activeFromCropYear;
	private Integer activeToCropYear;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	private Integer maxCropYear;
	private String otherLegalDescription;
	private Integer legalLandId;
	private Integer totalLegalLand;

	private List<PolicyDto> policies = new ArrayList<PolicyDto>();


	public FieldDto() {
	}
	
	
	public FieldDto(FieldDto dto) {

		this.fieldId = dto.fieldId;
		this.fieldLabel = dto.fieldLabel;
		this.location = dto.location;
		this.activeFromCropYear = dto.activeFromCropYear;
		this.activeToCropYear = dto.activeToCropYear;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.maxCropYear  = dto.maxCropYear;
		this.legalLandId = dto.legalLandId;
		this.otherLegalDescription = dto.otherLegalDescription;
		this.totalLegalLand = dto.totalLegalLand;
		
		if ( dto.policies != null ) {			
			this.policies = new ArrayList<>();
			
			for ( PolicyDto ifDto : dto.policies) {
				this.policies.add(ifDto.copy());
			}
		}	

	}
	

	@Override
	public boolean equalsBK(FieldDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(FieldDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("fieldId", fieldId, other.fieldId);
			result = result&&dtoUtils.equals("fieldLabel", fieldLabel, other.fieldLabel);
			result = result&&dtoUtils.equals("location", location, other.location);
			result = result&&dtoUtils.equals("activeFromCropYear", activeFromCropYear, other.activeFromCropYear);
			result = result&&dtoUtils.equals("activeToCropYear", activeToCropYear, other.activeToCropYear);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public FieldDto copy() {
		return new FieldDto(this);
	}
 	
	public Integer getFieldId() {
		return fieldId;
	}

	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
 	public Integer getActiveFromCropYear() {
		return activeFromCropYear;
	}

	public void setActiveFromCropYear(Integer activeFromCropYear) {
		this.activeFromCropYear = activeFromCropYear;
	}
 
 	public Integer getActiveToCropYear() {
		return activeToCropYear;
	}

	public void setActiveToCropYear(Integer activeToCropYear) {
		this.activeToCropYear = activeToCropYear;
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
	
 	public Integer getMaxCropYear() {
		return maxCropYear;
	}

	public void setMaxCropYear(Integer maxCropYear) {
		this.maxCropYear = maxCropYear;
	}

	public List<PolicyDto> getPolicies() {
		return policies;
	}

	public void setPolicies(List<PolicyDto> policies) {
		this.policies = policies;
	}

	public Integer getLegalLandId() {
		return legalLandId;
	}

	public void setLegalLandId(Integer legalLandId) {
		this.legalLandId = legalLandId;
	}

 	public String getOtherLegalDescription() {
		return otherLegalDescription;
	}

	public void setOtherLegalDescription(String otherLegalDescription) {
		this.otherLegalDescription = otherLegalDescription;
	}

	public Integer getTotalLegalLand() {
		return totalLegalLand;
	}
	
	public void setTotalLegalLand(Integer totalLegalLand) {
		this.totalLegalLand = totalLegalLand;
	}

}
