package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class AnnualFieldDetailDto extends BaseDto<AnnualFieldDetailDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(LegalLandDto.class);

	private Integer annualFieldDetailId;
	private Integer legalLandId;
	private Integer fieldId;
	private Integer cropYear;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	//Used to determine which annual field detail record is used to insert existing fields
	private Integer previousContractCropYear;
	private Integer previousContractLegalLandId;
	private Integer subsequentContractCropYear;
	private Integer subsequentContractLegalLandId;
	private Integer previousCropYear;
	private Integer previousLegalLandId;
	private Integer subsequentCropYear;
	private Integer subsequentLegalLandId;


	public AnnualFieldDetailDto() {
	}
	
	
	public AnnualFieldDetailDto(AnnualFieldDetailDto dto) {

		this.annualFieldDetailId = dto.annualFieldDetailId;
		this.legalLandId = dto.legalLandId;
		this.fieldId = dto.fieldId;
		this.cropYear = dto.cropYear;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(AnnualFieldDetailDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(AnnualFieldDetailDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("annualFieldDetailId", annualFieldDetailId, other.annualFieldDetailId);
			result = result&&dtoUtils.equals("legalLandId", legalLandId, other.legalLandId);
			result = result&&dtoUtils.equals("fieldId", fieldId, other.fieldId);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public AnnualFieldDetailDto copy() {
		return new AnnualFieldDetailDto(this);
	}
	
	public Integer getAnnualFieldDetailId() {
		return annualFieldDetailId;
	}

	public void setAnnualFieldDetailId(Integer annualFieldDetailId) {
		this.annualFieldDetailId = annualFieldDetailId;
	}

	public Integer getLegalLandId() {
		return legalLandId;
	}

	public void setLegalLandId(Integer legalLandId) {
		this.legalLandId = legalLandId;
	}

	public Integer getFieldId() {
		return fieldId;
	}

	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
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

	public Integer getPreviousContractCropYear() {
		return previousContractCropYear;
	}

	public void setPreviousContractCropYear(Integer previousContractCropYear) {
		this.previousContractCropYear = previousContractCropYear;
	}

	public Integer getPreviousContractLegalLandId() {
		return previousContractLegalLandId;
	}

	public void setPreviousContractLegalLandId(Integer previousContractLegalLandId) {
		this.previousContractLegalLandId = previousContractLegalLandId;
	}

	public Integer getSubsequentContractCropYear() {
		return subsequentContractCropYear;
	}

	public void setSubsequentContractCropYear(Integer subsequentContractCropYear) {
		this.subsequentContractCropYear = subsequentContractCropYear;
	}

	public Integer getSubsequentContractLegalLandId() {
		return subsequentContractLegalLandId;
	}

	public void setSubsequentContractLegalLandId(Integer subsequentContractLegalLandId) {
		this.subsequentContractLegalLandId = subsequentContractLegalLandId;
	}

	public Integer getPreviousCropYear() {
		return previousCropYear;
	}

	public void setPreviousCropYear(Integer previousCropYear) {
		this.previousCropYear = previousCropYear;
	}

	public Integer getPreviousLegalLandId() {
		return previousLegalLandId;
	}

	public void setPreviousLegalLandId(Integer previousLegalLandId) {
		this.previousLegalLandId = previousLegalLandId;
	}

	public Integer getSubsequentCropYear() {
		return subsequentCropYear;
	}

	public void setSubsequentCropYear(Integer subsequentCropYear) {
		this.subsequentCropYear = subsequentCropYear;
	}

	public Integer getSubsequentLegalLandId() {
		return subsequentLegalLandId;
	}

	public void setSubsequentLegalLandId(Integer subsequentLegalLandId) {
		this.subsequentLegalLandId = subsequentLegalLandId;
	}

	
}
