package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class CropVarietyInsPlantInsXrefDto extends BaseDto<CropVarietyInsPlantInsXrefDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CropVarietyInsPlantInsXrefDto.class);

	private String cropVarietyInsurabilityGuid;
	private String plantInsurabilityTypeCode;
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	private Integer cropVarietyId;
	private String description;
	private Boolean isUsedInd;
		
	public CropVarietyInsPlantInsXrefDto() {
	}
	
	
	public CropVarietyInsPlantInsXrefDto(CropVarietyInsPlantInsXrefDto dto) {

		this.cropVarietyInsurabilityGuid = dto.cropVarietyInsurabilityGuid;
		this.plantInsurabilityTypeCode = dto.plantInsurabilityTypeCode;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		
		this.cropVarietyId = dto.cropVarietyId;
		this.description = dto.description;
		this.isUsedInd = dto.isUsedInd;

	}
	

	@Override
	public boolean equalsBK(CropVarietyInsPlantInsXrefDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(CropVarietyInsPlantInsXrefDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("cropVarietyInsurabilityGuid", cropVarietyInsurabilityGuid, other.cropVarietyInsurabilityGuid);
			result = result&&dtoUtils.equals("plantInsurabilityTypeCode", plantInsurabilityTypeCode, other.plantInsurabilityTypeCode);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public CropVarietyInsPlantInsXrefDto copy() {
		return new CropVarietyInsPlantInsXrefDto(this);
	}
	
	public String getCropVarietyInsurabilityGuid() {
		return cropVarietyInsurabilityGuid;
	}

	public void setCropVarietyInsurabilityGuid(String cropVarietyInsurabilityGuid) {
		this.cropVarietyInsurabilityGuid = cropVarietyInsurabilityGuid;
	}
	
	public String getPlantInsurabilityTypeCode() {
		return plantInsurabilityTypeCode;
	}

	public void setPlantInsurabilityTypeCode(String plantInsurabilityTypeCode) {
		this.plantInsurabilityTypeCode = plantInsurabilityTypeCode;
	}
	
	public Integer getCropVarietyId() {
		return cropVarietyId;
	}

	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Boolean getIsUsedInd() {
		return isUsedInd;
	}
	
	public void setIsUsedInd(Boolean isUsedInd) {
		this.isUsedInd = isUsedInd;
	}

}
