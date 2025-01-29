package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class VerifiedYieldContractCommodityDto extends BaseDto<VerifiedYieldContractCommodityDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(VerifiedYieldContractCommodityDto.class);

	private String verifiedYieldContractCommodityGuid;
	private String verifiedYieldContractGuid;
	private Integer cropCommodityId;
	private String commodityTypeCode;
	private Boolean isPedigreeInd;
	private Double harvestedAcres;
	private Double harvestedAcresOverride;
	private Double storedYieldDefaultUnit;
	private Double soldYieldDefaultUnit;
	private Double productionGuarantee;
	private Double harvestedYield;
	private Double harvestedYieldOverride;
	private Double yieldPerAcre;

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	// Extended columns
	private String cropCommodityName;
	private Double totalInsuredAcres;
	private String commodityTypeDescription;
	private Boolean isRolledupInd;
	
	public VerifiedYieldContractCommodityDto() {
	}
	
	
	public VerifiedYieldContractCommodityDto(VerifiedYieldContractCommodityDto dto) {

		this.verifiedYieldContractCommodityGuid = dto.verifiedYieldContractCommodityGuid;
		this.verifiedYieldContractGuid = dto.verifiedYieldContractGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.commodityTypeCode = dto.commodityTypeCode;
		this.isPedigreeInd = dto.isPedigreeInd;
		this.harvestedAcres = dto.harvestedAcres;
		this.harvestedAcresOverride = dto.harvestedAcresOverride;
		this.storedYieldDefaultUnit = dto.storedYieldDefaultUnit;
		this.soldYieldDefaultUnit = dto.soldYieldDefaultUnit;
		this.productionGuarantee = dto.productionGuarantee;
		this.harvestedYield = dto.harvestedYield;
		this.harvestedYieldOverride = dto.harvestedYieldOverride;
		this.yieldPerAcre = dto.yieldPerAcre;
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.cropCommodityName = dto.cropCommodityName;
		this.totalInsuredAcres = dto.totalInsuredAcres;
		this.commodityTypeDescription = dto.commodityTypeDescription;
		this.isRolledupInd = dto.isRolledupInd;

	}
	

	@Override
	public boolean equalsBK(VerifiedYieldContractCommodityDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(VerifiedYieldContractCommodityDto other) {
		boolean result = false;
		
		if(other!=null) {
			Integer decimalPrecision = 4;
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			result = result&&dtoUtils.equals("verifiedYieldContractCommodityGuid", verifiedYieldContractCommodityGuid, other.verifiedYieldContractCommodityGuid);
			result = result&&dtoUtils.equals("verifiedYieldContractGuid", verifiedYieldContractGuid, other.verifiedYieldContractGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("commodityTypeCode", commodityTypeCode, other.commodityTypeCode);
			result = result&&dtoUtils.equals("isPedigreeInd", isPedigreeInd, other.isPedigreeInd);
			result = result&&dtoUtils.equals("harvestedAcres", harvestedAcres, other.harvestedAcres, decimalPrecision);			
			result = result&&dtoUtils.equals("harvestedAcresOverride", harvestedAcresOverride, other.harvestedAcresOverride, decimalPrecision);
			result = result&&dtoUtils.equals("storedYieldDefaultUnit", storedYieldDefaultUnit, other.storedYieldDefaultUnit, decimalPrecision);
			result = result&&dtoUtils.equals("soldYieldDefaultUnit", soldYieldDefaultUnit, other.soldYieldDefaultUnit, decimalPrecision);
			result = result&&dtoUtils.equals("productionGuarantee", productionGuarantee, other.productionGuarantee, decimalPrecision);
			result = result&&dtoUtils.equals("harvestedYield", harvestedYield, other.harvestedYield, decimalPrecision);
			result = result&&dtoUtils.equals("harvestedYieldOverride", harvestedYieldOverride, other.harvestedYieldOverride, decimalPrecision);
			result = result&&dtoUtils.equals("yieldPerAcre", yieldPerAcre, other.yieldPerAcre, decimalPrecision);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public VerifiedYieldContractCommodityDto copy() {
		return new VerifiedYieldContractCommodityDto(this);
	}


	public String getVerifiedYieldContractCommodityGuid() {
		return verifiedYieldContractCommodityGuid;
	}
	public void setVerifiedYieldContractCommodityGuid(String verifiedYieldContractCommodityGuid) {
		this.verifiedYieldContractCommodityGuid = verifiedYieldContractCommodityGuid;
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

	public String getCommodityTypeCode() {
		return commodityTypeCode;
	}

	public void setCommodityTypeCode(String commodityTypeCode) {
		this.commodityTypeCode = commodityTypeCode;
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

	public Double getHarvestedAcresOverride() {
		return harvestedAcresOverride;
	}
	public void setHarvestedAcresOverride(Double harvestedAcresOverride) {
		this.harvestedAcresOverride = harvestedAcresOverride;
	}

	public Double getStoredYieldDefaultUnit() {
		return storedYieldDefaultUnit;
	}
	public void setStoredYieldDefaultUnit(Double storedYieldDefaultUnit) {
		this.storedYieldDefaultUnit = storedYieldDefaultUnit;
	}

	public Double getSoldYieldDefaultUnit() {
		return soldYieldDefaultUnit;
	}
	public void setSoldYieldDefaultUnit(Double soldYieldDefaultUnit) {
		this.soldYieldDefaultUnit = soldYieldDefaultUnit;
	}

	public Double getProductionGuarantee() {
		return productionGuarantee;
	}
	public void setProductionGuarantee(Double productionGuarantee) {
		this.productionGuarantee = productionGuarantee;
	}

	public Double getHarvestedYield() {
		return harvestedYield;
	}
	public void setHarvestedYield(Double harvestedYield) {
		this.harvestedYield = harvestedYield;
	}

	public Double getHarvestedYieldOverride() {
		return harvestedYieldOverride;
	}
	public void setHarvestedYieldOverride(Double harvestedYieldOverride) {
		this.harvestedYieldOverride = harvestedYieldOverride;
	}

	public Double getYieldPerAcre() {
		return yieldPerAcre;
	}
	public void setYieldPerAcre(Double yieldPerAcre) {
		this.yieldPerAcre = yieldPerAcre;
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

	public String getCommodityTypeDescription() {
		return commodityTypeDescription;
	}

	public void setCommodityTypeDescription(String commodityTypeDescription) {
		this.commodityTypeDescription = commodityTypeDescription;
	}
	
	public Boolean getIsRolledupInd() {
		return isRolledupInd;
	}

	public void setIsRolledupInd(Boolean isRolledupInd) {
		this.isRolledupInd = isRolledupInd;
	}
	
}
