package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class VerifiedYieldAmendmentDto extends BaseDto<VerifiedYieldAmendmentDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(VerifiedYieldAmendmentDto.class);
	
	private String verifiedYieldAmendmentGuid;
	private String verifiedYieldAmendmentCode;
	private String verifiedYieldContractGuid;
	private Integer cropCommodityId;
	private Integer cropVarietyId;
	private Boolean isPedigreeInd;
	private Integer fieldId;
	private Double yieldPerAcre;
	private Double acres;
	private String rationale;

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	// Extended columns
	private String cropCommodityName;
	private String cropVarietyName;
	private String fieldLabel;
	
	public VerifiedYieldAmendmentDto() {
	}
	
	
	public VerifiedYieldAmendmentDto(VerifiedYieldAmendmentDto dto) {
		
		this.verifiedYieldAmendmentGuid = dto.verifiedYieldAmendmentGuid;
		this.verifiedYieldAmendmentCode = dto.verifiedYieldAmendmentCode;
		this.verifiedYieldContractGuid = dto.verifiedYieldContractGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.cropVarietyId = dto.cropVarietyId;
		this.isPedigreeInd = dto.isPedigreeInd;
		this.fieldId = dto.fieldId;
		this.yieldPerAcre = dto.yieldPerAcre;
		this.acres = dto.acres;
		this.rationale = dto.rationale;
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.cropCommodityName = dto.cropCommodityName;
		this.cropVarietyName = dto.cropVarietyName;
		this.fieldLabel = dto.fieldLabel;
	}
	

	@Override
	public boolean equalsBK(VerifiedYieldAmendmentDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(VerifiedYieldAmendmentDto other) {
		boolean result = false;
		
		if(other!=null) {
			Integer decimalPrecision = 4;
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			result = result&&dtoUtils.equals("verifiedYieldAmendmentGuid", verifiedYieldAmendmentGuid, other.verifiedYieldAmendmentGuid);
			result = result&&dtoUtils.equals("verifiedYieldAmendmentCode", verifiedYieldAmendmentCode, other.verifiedYieldAmendmentCode);
			result = result&&dtoUtils.equals("verifiedYieldContractGuid", verifiedYieldContractGuid, other.verifiedYieldContractGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("cropVarietyId", cropVarietyId, other.cropVarietyId);
			result = result&&dtoUtils.equals("isPedigreeInd", isPedigreeInd, other.isPedigreeInd);
			result = result&&dtoUtils.equals("fieldId", fieldId, other.fieldId);
			result = result&&dtoUtils.equals("yieldPerAcre", yieldPerAcre, other.yieldPerAcre, decimalPrecision);
			result = result&&dtoUtils.equals("acres", acres, other.acres, decimalPrecision);
			result = result&&dtoUtils.equals("rationale", rationale, other.rationale);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public VerifiedYieldAmendmentDto copy() {
		return new VerifiedYieldAmendmentDto(this);
	}

	public String getVerifiedYieldAmendmentGuid() {
		return verifiedYieldAmendmentGuid;
	}
	public void setVerifiedYieldAmendmentGuid(String verifiedYieldAmendmentGuid) {
		this.verifiedYieldAmendmentGuid = verifiedYieldAmendmentGuid;
	}

	public String getVerifiedYieldAmendmentCode() {
		return verifiedYieldAmendmentCode;
	}
	public void setVerifiedYieldAmendmentCode(String verifiedYieldAmendmentCode) {
		this.verifiedYieldAmendmentCode = verifiedYieldAmendmentCode;
	}

	public String getVerifiedYieldContractGuid() {
		return verifiedYieldContractGuid;
	}
	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid) {
		this.verifiedYieldContractGuid = verifiedYieldContractGuid;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}
	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Integer getCropVarietyId() {
		return cropVarietyId;
	}

	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	}
	
	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}
	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
	}

	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public Double getYieldPerAcre() {
		return yieldPerAcre;
	}
	public void setYieldPerAcre(Double yieldPerAcre) {
		this.yieldPerAcre = yieldPerAcre;
	}


	public Double getAcres() {
		return acres;
	}
	public void setAcres(Double acres) {
		this.acres = acres;
	}

	public String getRationale() {
		return rationale;
	}
	public void setRationale(String rationale) {
		this.rationale = rationale;
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

	public String getCropCommodityName() {
		return cropCommodityName;
	}
	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}

	public String getCropVarietyName() {
		return cropVarietyName;
	}

	public void setCropVarietyName(String cropVarietyName) {
		this.cropVarietyName = cropVarietyName;
	}
	
	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
}
