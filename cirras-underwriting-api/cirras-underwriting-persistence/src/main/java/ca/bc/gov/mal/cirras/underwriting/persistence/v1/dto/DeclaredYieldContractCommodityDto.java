package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class DeclaredYieldContractCommodityDto extends BaseDto<DeclaredYieldContractCommodityDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldContractCommodityDto.class);

	private String declaredYieldContractCommodityGuid;
	private String declaredYieldContractGuid;
	private Integer cropCommodityId;
	private Boolean isPedigreeInd;
	private Double harvestedAcres;
	private Double storedYield;
	private Double storedYieldDefaultUnit;
	private Double soldYield;
	private Double soldYieldDefaultUnit;
	private String gradeModifierTypeCode;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	private String cropCommodityName;
	private Double totalInsuredAcres;

	
	public DeclaredYieldContractCommodityDto() {
	}
	
	
	public DeclaredYieldContractCommodityDto(DeclaredYieldContractCommodityDto dto) {

		this.declaredYieldContractCommodityGuid = dto.declaredYieldContractCommodityGuid;
		this.declaredYieldContractGuid = dto.declaredYieldContractGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.isPedigreeInd = dto.isPedigreeInd;
		this.harvestedAcres = dto.harvestedAcres;
		this.storedYield = dto.storedYield;
		this.storedYieldDefaultUnit = dto.storedYieldDefaultUnit;
		this.soldYield = dto.soldYield;
		this.soldYieldDefaultUnit = dto.soldYieldDefaultUnit;
		this.gradeModifierTypeCode = dto.gradeModifierTypeCode;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		
		this.cropCommodityName = dto.cropCommodityName;
		this.totalInsuredAcres = dto.totalInsuredAcres;


	}
	

	@Override
	public boolean equalsBK(DeclaredYieldContractCommodityDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(DeclaredYieldContractCommodityDto other) {
		boolean result = false;
		
		if(other!=null) {
			Integer decimalPrecision = 4;
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("declaredYieldContractCommodityGuid", declaredYieldContractCommodityGuid, other.declaredYieldContractCommodityGuid);
			result = result&&dtoUtils.equals("declaredYieldContractGuid", declaredYieldContractGuid, other.declaredYieldContractGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("isPedigreeInd", isPedigreeInd, other.isPedigreeInd);
			result = result&&dtoUtils.equals("harvestedAcres", harvestedAcres, other.harvestedAcres, decimalPrecision);
			result = result&&dtoUtils.equals("storedYield", storedYield, other.storedYield, decimalPrecision);
			result = result&&dtoUtils.equals("storedYieldDefaultUnit", storedYieldDefaultUnit, other.storedYieldDefaultUnit, decimalPrecision);
			result = result&&dtoUtils.equals("soldYield", soldYield, other.soldYield, decimalPrecision);
			result = result&&dtoUtils.equals("soldYieldDefaultUnit", soldYieldDefaultUnit, other.soldYieldDefaultUnit, decimalPrecision);
			result = result&&dtoUtils.equals("gradeModifierTypeCode", gradeModifierTypeCode, other.gradeModifierTypeCode);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public DeclaredYieldContractCommodityDto copy() {
		return new DeclaredYieldContractCommodityDto(this);
	}
	
 	public String getDeclaredYieldContractCommodityGuid() {
		return declaredYieldContractCommodityGuid;
	}

	public void setDeclaredYieldContractCommodityGuid(String declaredYieldContractCommodityGuid) {
		this.declaredYieldContractCommodityGuid = declaredYieldContractCommodityGuid;
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
 
 	public Double getHarvestedAcres() {
		return harvestedAcres;
	}

	public void setHarvestedAcres(Double harvestedAcres) {
		this.harvestedAcres = harvestedAcres;
	}
 
 	public Double getStoredYield() {
		return storedYield;
	}

	public void setStoredYield(Double storedYield) {
		this.storedYield = storedYield;
	}
 
 	public Double getStoredYieldDefaultUnit() {
		return storedYieldDefaultUnit;
	}

	public void setStoredYieldDefaultUnit(Double storedYieldDefaultUnit) {
		this.storedYieldDefaultUnit = storedYieldDefaultUnit;
	}
 
 	public Double getSoldYield() {
		return soldYield;
	}

	public void setSoldYield(Double soldYield) {
		this.soldYield = soldYield;
	}
 
 	public Double getSoldYieldDefaultUnit() {
		return soldYieldDefaultUnit;
	}

	public void setSoldYieldDefaultUnit(Double soldYieldDefaultUnit) {
		this.soldYieldDefaultUnit = soldYieldDefaultUnit;
	}
 
 	public String getGradeModifierTypeCode() {
		return gradeModifierTypeCode;
	}

	public void setGradeModifierTypeCode(String gradeModifierTypeCode) {
		this.gradeModifierTypeCode = gradeModifierTypeCode;
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
 
 	public Double getTotalInsuredAcres() {
		return totalInsuredAcres;
	}

	public void setTotalInsuredAcres(Double totalInsuredAcres) {
		this.totalInsuredAcres = totalInsuredAcres;
	}

}
