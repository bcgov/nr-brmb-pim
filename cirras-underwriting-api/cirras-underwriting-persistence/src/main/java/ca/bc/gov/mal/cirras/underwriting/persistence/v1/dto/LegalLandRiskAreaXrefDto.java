package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class LegalLandRiskAreaXrefDto extends BaseDto<LegalLandRiskAreaXrefDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(LegalLandRiskAreaXrefDto.class);

	private Integer legalLandId;
	private Integer riskAreaId;
	private Integer activeFromCropYear;
	private Integer activeToCropYear;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	public LegalLandRiskAreaXrefDto() {
	}
	
	
	public LegalLandRiskAreaXrefDto(LegalLandRiskAreaXrefDto dto) {

		this.legalLandId = dto.legalLandId;
		this.riskAreaId = dto.riskAreaId;
		this.activeFromCropYear = dto.activeFromCropYear;
		this.activeToCropYear = dto.activeToCropYear;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(LegalLandRiskAreaXrefDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(LegalLandRiskAreaXrefDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("legalLandId", legalLandId, other.legalLandId);
			result = result&&dtoUtils.equals("riskAreaId", riskAreaId, other.riskAreaId);
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
	public LegalLandRiskAreaXrefDto copy() {
		return new LegalLandRiskAreaXrefDto(this);
	}

	public Integer getLegalLandId() {
		return legalLandId;
	}

	public void setLegalLandId(Integer legalLandId) {
		this.legalLandId = legalLandId;
	}

	public Integer getRiskAreaId() {
		return riskAreaId;
	}

	public void setRiskAreaId(Integer riskAreaId) {
		this.riskAreaId = riskAreaId;
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

}
