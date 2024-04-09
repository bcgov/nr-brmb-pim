package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class DeclaredYieldFieldRollupDto extends BaseDto<DeclaredYieldFieldRollupDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldFieldRollupDto.class);

	private String declaredYieldFieldRollupGuid;
	private String declaredYieldContractGuid;
	private Integer cropCommodityId;
	private Boolean isPedigreeInd;
	private Double estimatedYieldPerAcreTonnes;
	private Double estimatedYieldPerAcreBushels;

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	private String cropCommodityName;
	private String enteredYieldMeasUnitTypeCode;
	
	public DeclaredYieldFieldRollupDto() {
	}
	
	
	public DeclaredYieldFieldRollupDto(DeclaredYieldFieldRollupDto dto) {

		this.declaredYieldFieldRollupGuid = dto.declaredYieldFieldRollupGuid;
		this.declaredYieldContractGuid = dto.declaredYieldContractGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.isPedigreeInd = dto.isPedigreeInd;
		this.estimatedYieldPerAcreTonnes = dto.estimatedYieldPerAcreTonnes;
		this.estimatedYieldPerAcreBushels = dto.estimatedYieldPerAcreBushels;
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		
		this.cropCommodityName = dto.cropCommodityName;
		this.enteredYieldMeasUnitTypeCode = dto.enteredYieldMeasUnitTypeCode;

	}
	

	@Override
	public boolean equalsBK(DeclaredYieldFieldRollupDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(DeclaredYieldFieldRollupDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("declaredYieldFieldRollupGuid", declaredYieldFieldRollupGuid, other.declaredYieldFieldRollupGuid);
			result = result&&dtoUtils.equals("declaredYieldContractGuid", declaredYieldContractGuid, other.declaredYieldContractGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("isPedigreeInd", isPedigreeInd, other.isPedigreeInd);
			result = result&&dtoUtils.equals("estimatedYieldPerAcreTonnes", estimatedYieldPerAcreTonnes, other.estimatedYieldPerAcreTonnes, 4);
			result = result&&dtoUtils.equals("estimatedYieldPerAcreBushels", estimatedYieldPerAcreBushels, other.estimatedYieldPerAcreBushels, 4);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public DeclaredYieldFieldRollupDto copy() {
		return new DeclaredYieldFieldRollupDto(this);
	}

 	public String getDeclaredYieldFieldRollupGuid() {
		return declaredYieldFieldRollupGuid;
	}

	public void setDeclaredYieldFieldRollupGuid(String declaredYieldFieldRollupGuid) {
		this.declaredYieldFieldRollupGuid = declaredYieldFieldRollupGuid;
	}
 
 	public String getDeclaredYieldContractGuid() {
		return declaredYieldContractGuid;
	}

	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid) {
		this.declaredYieldContractGuid = declaredYieldContractGuid;
	}
 
 	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}
 
 	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}

	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
	}
 
 	public Double getEstimatedYieldPerAcreTonnes() {
		return estimatedYieldPerAcreTonnes;
	}

	public void setEstimatedYieldPerAcreTonnes(Double estimatedYieldPerAcreTonnes) {
		this.estimatedYieldPerAcreTonnes = estimatedYieldPerAcreTonnes;
	}
 
 	public Double getEstimatedYieldPerAcreBushels() {
		return estimatedYieldPerAcreBushels;
	}

	public void setEstimatedYieldPerAcreBushels(Double estimatedYieldPerAcreBushels) {
		this.estimatedYieldPerAcreBushels = estimatedYieldPerAcreBushels;
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
	 
 	public String getEnteredYieldMeasUnitTypeCode() {
		return enteredYieldMeasUnitTypeCode;
	}
	public void setEnteredYieldMeasUnitTypeCode(String enteredYieldMeasUnitTypeCode) {
		this.enteredYieldMeasUnitTypeCode = enteredYieldMeasUnitTypeCode;
	}
 
}
