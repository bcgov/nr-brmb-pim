package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class LegalLandDto extends BaseDto<LegalLandDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(LegalLandDto.class);

	private Integer legalLandId;
	private String primaryPropertyIdentifier;
	private String primaryLandIdentifierTypeCode;
	private String primaryReferenceTypeCode;
	private String legalDescription;
	private String legalShortDescription;
	private String otherDescription;
	private Integer activeFromCropYear;
	private Integer activeToCropYear;
	private Double totalAcres;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	private List<RiskAreaDto> riskAreas = new ArrayList<RiskAreaDto>();

	private List<FieldDto> fields = new ArrayList<FieldDto>();


	public LegalLandDto() {
	}
	
	
	public LegalLandDto(LegalLandDto dto) {

		this.legalLandId = dto.legalLandId;
		this.primaryPropertyIdentifier = dto.primaryPropertyIdentifier;
		this.primaryLandIdentifierTypeCode = dto.primaryLandIdentifierTypeCode;
		this.primaryReferenceTypeCode = dto.primaryReferenceTypeCode;
		this.legalDescription = dto.legalDescription;
		this.legalShortDescription = dto.legalShortDescription;
		this.otherDescription = dto.otherDescription;
		this.activeFromCropYear = dto.activeFromCropYear;
		this.activeToCropYear = dto.activeToCropYear;
		this.totalAcres = dto.totalAcres;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		if ( dto.riskAreas != null ) {			
			this.riskAreas = new ArrayList<>();
			
			for ( RiskAreaDto ifDto : dto.riskAreas) {
				this.riskAreas.add(ifDto.copy());
			}
		}
		
		if ( dto.fields != null ) {			
			this.fields = new ArrayList<>();
			
			for ( FieldDto fDto : dto.fields) {
				this.fields.add(fDto.copy());
			}
		}	
	}
	

	@Override
	public boolean equalsBK(LegalLandDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(LegalLandDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("legalLandId", legalLandId, other.legalLandId);
			result = result&&dtoUtils.equals("primaryPropertyIdentifier", primaryPropertyIdentifier, other.primaryPropertyIdentifier);
			result = result&&dtoUtils.equals("primaryLandIdentifierTypeCode", primaryLandIdentifierTypeCode, other.primaryLandIdentifierTypeCode);
			result = result&&dtoUtils.equals("primaryReferenceTypeCode", primaryReferenceTypeCode, other.primaryReferenceTypeCode);
			result = result&&dtoUtils.equals("legalDescription", legalDescription, other.legalDescription);
			result = result&&dtoUtils.equals("legalShortDescription", legalShortDescription, other.legalShortDescription);
			result = result&&dtoUtils.equals("otherDescription", otherDescription, other.otherDescription);
			result = result&&dtoUtils.equals("activeFromCropYear", activeFromCropYear, other.activeFromCropYear);
			result = result&&dtoUtils.equals("activeToCropYear", activeToCropYear, other.activeToCropYear);
			result = result&&dtoUtils.equals("totalAcres", totalAcres, other.totalAcres, 4);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public LegalLandDto copy() {
		return new LegalLandDto(this);
	}

 	public Integer getLegalLandId() {
		return legalLandId;
	}

	public void setLegalLandId(Integer legalLandId) {
		this.legalLandId = legalLandId;
	}
	
	public String getPrimaryPropertyIdentifier() {
		return primaryPropertyIdentifier;
	}

	public void setPrimaryPropertyIdentifier(String primaryPropertyIdentifier) {
		this.primaryPropertyIdentifier = primaryPropertyIdentifier;
	}

	public String getPrimaryLandIdentifierTypeCode() {
		return primaryLandIdentifierTypeCode;
	}

	public void setPrimaryLandIdentifierTypeCode(String primaryLandIdentifierTypeCode) {
		this.primaryLandIdentifierTypeCode = primaryLandIdentifierTypeCode;
	}
	
 	public String getPrimaryReferenceTypeCode() {
		return primaryReferenceTypeCode;
	}

	public void setPrimaryReferenceTypeCode(String primaryReferenceTypeCode) {
		this.primaryReferenceTypeCode = primaryReferenceTypeCode;
	}
 
 	public String getLegalDescription() {
		return legalDescription;
	}

	public void setLegalDescription(String legalDescription) {
		this.legalDescription = legalDescription;
	}
 
 	public String getLegalShortDescription() {
		return legalShortDescription;
	}

	public void setLegalShortDescription(String legalShortDescription) {
		this.legalShortDescription = legalShortDescription;
	}
 
 	public String getOtherDescription() {
		return otherDescription;
	}

	public void setOtherDescription(String otherDescription) {
		this.otherDescription = otherDescription;
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

	public Double getTotalAcres() {
		return totalAcres;
	}

	public void setTotalAcres(Double totalAcres) {
		this.totalAcres = totalAcres;
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

	public List<RiskAreaDto> getRiskAreas() {
		return riskAreas;
	}

	public void setRiskAreas(List<RiskAreaDto> riskAreas) {
		this.riskAreas = riskAreas;
	}

	public List<FieldDto> getFields() {
		return fields;
	}

	public void setFields(List<FieldDto> fields) {
		this.fields = fields;
	}
}
