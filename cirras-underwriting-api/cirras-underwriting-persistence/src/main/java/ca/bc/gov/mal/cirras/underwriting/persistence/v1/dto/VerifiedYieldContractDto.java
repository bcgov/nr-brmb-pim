package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.utils.DateUtils;
import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class VerifiedYieldContractDto extends BaseDto<VerifiedYieldContractDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(VerifiedYieldContractDto.class);

	private String verifiedYieldContractGuid;
	private Integer contractId;
	private Integer cropYear;
	private String declaredYieldContractGuid;
	private String defaultYieldMeasUnitTypeCode;
	private Date verifiedYieldUpdateTimestamp;
	private String verifiedYieldUpdateUser;
	
	private List<ContractedFieldDetailDto> fields = new ArrayList<ContractedFieldDetailDto>();
	private List<VerifiedYieldContractCommodityDto> verifiedYieldContractCommodities = new ArrayList<VerifiedYieldContractCommodityDto>();
	private List<VerifiedYieldAmendmentDto> verifiedYieldAmendments = new ArrayList<VerifiedYieldAmendmentDto>();
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	//Extended columns
	private Integer insurancePlanId;
	private Integer growerContractYearId;
	
	public VerifiedYieldContractDto() {
	}
	
	
	public VerifiedYieldContractDto(VerifiedYieldContractDto dto) {

		this.verifiedYieldContractGuid = dto.verifiedYieldContractGuid;
		this.contractId = dto.contractId;
		this.cropYear = dto.cropYear;
		this.declaredYieldContractGuid = dto.declaredYieldContractGuid;
		this.defaultYieldMeasUnitTypeCode = dto.defaultYieldMeasUnitTypeCode;
		this.verifiedYieldUpdateTimestamp = dto.verifiedYieldUpdateTimestamp;
		this.verifiedYieldUpdateUser = dto.verifiedYieldUpdateUser;

		if ( dto.fields != null ) {			
			this.fields = new ArrayList<>();
			
			for ( ContractedFieldDetailDto cfdDto : dto.fields ) {
				this.fields.add(cfdDto.copy());
			}
		}
		
		if ( dto.verifiedYieldContractCommodities != null ) {			
			this.verifiedYieldContractCommodities = new ArrayList<>();
			
			for ( VerifiedYieldContractCommodityDto vyccDto : dto.verifiedYieldContractCommodities ) {
				this.verifiedYieldContractCommodities.add(vyccDto.copy());
			}
		}	

		if ( dto.verifiedYieldAmendments != null ) {			
			this.verifiedYieldAmendments = new ArrayList<>();
			
			for ( VerifiedYieldAmendmentDto vyaDto : dto.verifiedYieldAmendments ) {
				this.verifiedYieldAmendments.add(vyaDto.copy());
			}
		}
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.insurancePlanId = dto.insurancePlanId;
		this.growerContractYearId = dto.growerContractYearId;
	}
	

	@Override
	public boolean equalsBK(VerifiedYieldContractDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(VerifiedYieldContractDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			result = result&&dtoUtils.equals("verifiedYieldContractGuid", verifiedYieldContractGuid, other.verifiedYieldContractGuid);
			result = result&&dtoUtils.equals("contractId", contractId, other.contractId);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);
			result = result&&dtoUtils.equals("declaredYieldContractGuid", declaredYieldContractGuid, other.declaredYieldContractGuid);
			result = result&&dtoUtils.equals("defaultYieldMeasUnitTypeCode", defaultYieldMeasUnitTypeCode, other.defaultYieldMeasUnitTypeCode);
			result = result&&DateUtils.equalsDate(logger, "verifiedYieldUpdateTimestamp", verifiedYieldUpdateTimestamp, other.verifiedYieldUpdateTimestamp);
			result = result&&dtoUtils.equals("verifiedYieldUpdateUser", verifiedYieldUpdateUser, other.verifiedYieldUpdateUser);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public VerifiedYieldContractDto copy() {
		return new VerifiedYieldContractDto(this);
	}


	public String getVerifiedYieldContractGuid() {
		return verifiedYieldContractGuid;
	}
	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid) {
		this.verifiedYieldContractGuid = verifiedYieldContractGuid;
	}

	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public String getDeclaredYieldContractGuid() {
		return declaredYieldContractGuid;
	}
	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid) {
		this.declaredYieldContractGuid = declaredYieldContractGuid;
	}

	public String getDefaultYieldMeasUnitTypeCode() {
		return defaultYieldMeasUnitTypeCode;
	}
	public void setDefaultYieldMeasUnitTypeCode(String defaultYieldMeasUnitTypeCode) {
		this.defaultYieldMeasUnitTypeCode = defaultYieldMeasUnitTypeCode;
	}

	public Date getVerifiedYieldUpdateTimestamp() {
		return verifiedYieldUpdateTimestamp;
	}
	public void setVerifiedYieldUpdateTimestamp(Date verifiedYieldUpdateTimestamp) {
		this.verifiedYieldUpdateTimestamp = verifiedYieldUpdateTimestamp;
	}

	public String getVerifiedYieldUpdateUser() {
		return verifiedYieldUpdateUser;
	}
	public void setVerifiedYieldUpdateUser(String verifiedYieldUpdateUser) {
		this.verifiedYieldUpdateUser = verifiedYieldUpdateUser;
	}

	public List<ContractedFieldDetailDto> getFields() {
		return fields;
	}
	public void setFields(List<ContractedFieldDetailDto> fields) {
		this.fields = fields;
	}
	
	public List<VerifiedYieldContractCommodityDto> getVerifiedYieldContractCommodities() {
		return verifiedYieldContractCommodities;
	}
	public void setVerifiedYieldContractCommodities(List<VerifiedYieldContractCommodityDto> verifiedYieldContractCommodities) {
		this.verifiedYieldContractCommodities = verifiedYieldContractCommodities;
	}

	public List<VerifiedYieldAmendmentDto> getVerifiedYieldAmendments() {
		return verifiedYieldAmendments;
	}
	public void setVerifiedYieldAmendments(List<VerifiedYieldAmendmentDto> verifiedYieldAmendments) {
		this.verifiedYieldAmendments = verifiedYieldAmendments;
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

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}
	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public Integer getGrowerContractYearId() {
		return growerContractYearId;
	}
	public void setGrowerContractYearId(Integer growerContractYearId) {
		this.growerContractYearId = growerContractYearId;
	}	
}
