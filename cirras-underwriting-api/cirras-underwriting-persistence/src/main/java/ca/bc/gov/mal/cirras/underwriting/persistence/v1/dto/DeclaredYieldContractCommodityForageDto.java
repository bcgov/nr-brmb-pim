package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class DeclaredYieldContractCommodityForageDto extends BaseDto<DeclaredYieldContractCommodityForageDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldContractCommodityForageDto.class);

	private String declaredYieldContractCmdtyForageGuid;
	private String declaredYieldContractGuid;
	private String commodityTypeCode;
	private Double harvestedAcres;
	private Double harvestedAcresOverride;
	private Double quantityHarvestedTons;
	private Double quantityHarvestedTonsOverride;
	private Double yieldPerAcre;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	private String commodityTypeDescription;
	private Double totalFieldAcres;

	
	public DeclaredYieldContractCommodityForageDto() {
	}
	
	public DeclaredYieldContractCommodityForageDto(DeclaredYieldContractCommodityForageDto dto) {

		this.declaredYieldContractCmdtyForageGuid = dto.declaredYieldContractCmdtyForageGuid;
		this.declaredYieldContractGuid = dto.declaredYieldContractGuid;
		this.commodityTypeCode = dto.commodityTypeCode;
		this.totalFieldAcres = dto.totalFieldAcres;
		this.harvestedAcres = dto.harvestedAcres;
		this.harvestedAcresOverride = dto.harvestedAcresOverride;
		this.quantityHarvestedTons = dto.quantityHarvestedTons;
		this.quantityHarvestedTonsOverride = dto.quantityHarvestedTonsOverride;
		this.yieldPerAcre = dto.yieldPerAcre;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.commodityTypeDescription = dto.commodityTypeDescription;
	}
	

	@Override
	public boolean equalsBK(DeclaredYieldContractCommodityForageDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(DeclaredYieldContractCommodityForageDto other) {
		boolean result = false;
		
		if(other!=null) {
			Integer decimalPrecision = 4;
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			result = result&&dtoUtils.equals("declaredYieldContractCmdtyForageGuid", declaredYieldContractCmdtyForageGuid, other.declaredYieldContractCmdtyForageGuid);
			result = result&&dtoUtils.equals("declaredYieldContractGuid", declaredYieldContractGuid, other.declaredYieldContractGuid);
			result = result&&dtoUtils.equals("commodityTypeCode", commodityTypeCode, other.commodityTypeCode);
			result = result&&dtoUtils.equals("totalFieldAcres", totalFieldAcres, other.totalFieldAcres, decimalPrecision);
			result = result&&dtoUtils.equals("harvestedAcres", harvestedAcres, other.harvestedAcres, decimalPrecision);
			result = result&&dtoUtils.equals("harvestedAcresOverride", harvestedAcresOverride, other.harvestedAcresOverride, decimalPrecision);
			result = result&&dtoUtils.equals("quantityHarvestedTons", quantityHarvestedTons, other.quantityHarvestedTons, decimalPrecision);
			result = result&&dtoUtils.equals("quantityHarvestedTonsOverride", quantityHarvestedTonsOverride, other.quantityHarvestedTonsOverride, decimalPrecision);
			result = result&&dtoUtils.equals("yieldPerAcre", yieldPerAcre, other.yieldPerAcre, decimalPrecision);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public DeclaredYieldContractCommodityForageDto copy() {
		return new DeclaredYieldContractCommodityForageDto(this);
	}
	
	public String getDeclaredYieldContractCmdtyForageGuid() {
		return declaredYieldContractCmdtyForageGuid;
	}

	public void setDeclaredYieldContractCmdtyForageGuid(String declaredYieldContractCmdtyForageGuid) {
		this.declaredYieldContractCmdtyForageGuid = declaredYieldContractCmdtyForageGuid;
	}

	public String getDeclaredYieldContractGuid() {
		return declaredYieldContractGuid;
	}

	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid) {
		this.declaredYieldContractGuid = declaredYieldContractGuid;
	}

	public String getCommodityTypeCode() {
		return commodityTypeCode;
	}

	public void setCommodityTypeCode(String commodityTypeCode) {
		this.commodityTypeCode = commodityTypeCode;
	}

	public Double getTotalFieldAcres() {
		return totalFieldAcres;
	}
	
	public void setTotalFieldAcres(Double totalFieldAcres) {
		this.totalFieldAcres = totalFieldAcres;
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

	public Double getQuantityHarvestedTons() {
		return quantityHarvestedTons;
	}

	public void setQuantityHarvestedTons(Double quantityHarvestedTons) {
		this.quantityHarvestedTons = quantityHarvestedTons;
	}

	public Double getQuantityHarvestedTonsOverride() {
		return quantityHarvestedTonsOverride;
	}

	public void setQuantityHarvestedTonsOverride(Double quantityHarvestedTonsOverride) {
		this.quantityHarvestedTonsOverride = quantityHarvestedTonsOverride;
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

	public String getCommodityTypeDescription() {
		return commodityTypeDescription;
	}

	public void setCommodityTypeDescription(String commodityTypeDescription) {
		this.commodityTypeDescription = commodityTypeDescription;
	}

}
