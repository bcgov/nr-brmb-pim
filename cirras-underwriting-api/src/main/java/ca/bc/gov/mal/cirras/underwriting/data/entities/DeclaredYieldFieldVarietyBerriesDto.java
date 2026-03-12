package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class DeclaredYieldFieldVarietyBerriesDto extends BaseDto<DeclaredYieldFieldVarietyBerriesDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldFieldVarietyBerriesDto.class);

	private String declaredYieldFieldVarietyBerriesGuid;
	private String declaredYieldFieldCommodityBerriesGuid;

	private Integer cropVarietyId;
	private Double plantedAcres;
	private Double matureEquivalentAcres;
	private Double soldShippedYield;
	private Double salesYield;
	private Double abandonmentYield;
	private Double totalProduction;
	private Double totalProductionOverride;

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	private String cropVarietyName;

	public DeclaredYieldFieldVarietyBerriesDto() {
	}
	
	
	public DeclaredYieldFieldVarietyBerriesDto(DeclaredYieldFieldVarietyBerriesDto dto) {

		this.declaredYieldFieldVarietyBerriesGuid = dto.declaredYieldFieldVarietyBerriesGuid;
		this.declaredYieldFieldCommodityBerriesGuid = dto.declaredYieldFieldCommodityBerriesGuid;
		this.cropVarietyId = dto.cropVarietyId;
		this.plantedAcres = dto.plantedAcres;
		this.matureEquivalentAcres = dto.matureEquivalentAcres;
		this.soldShippedYield = dto.soldShippedYield;
		this.salesYield = dto.salesYield;
		this.abandonmentYield = dto.abandonmentYield;
		this.totalProduction = dto.totalProduction;
		this.totalProductionOverride = dto.totalProductionOverride;

		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		
		this.cropVarietyName = dto.cropVarietyName;

	}
	

	@Override
	public boolean equalsBK(DeclaredYieldFieldVarietyBerriesDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(DeclaredYieldFieldVarietyBerriesDto other) {
		boolean result = false;
		
		if(other!=null) {
			Integer decimalPrecision = 4;
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("declaredYieldFieldVarietyBerriesGuid", declaredYieldFieldVarietyBerriesGuid, other.declaredYieldFieldVarietyBerriesGuid);
			result = result&&dtoUtils.equals("declaredYieldFieldCommodityBerriesGuid", declaredYieldFieldCommodityBerriesGuid, other.declaredYieldFieldCommodityBerriesGuid);
			result = result&&dtoUtils.equals("cropVarietyId", cropVarietyId, other.cropVarietyId);
			result = result&&dtoUtils.equals("plantedAcres", plantedAcres, other.plantedAcres, decimalPrecision);
			result = result&&dtoUtils.equals("matureEquivalentAcres", matureEquivalentAcres, other.matureEquivalentAcres, decimalPrecision);
			result = result&&dtoUtils.equals("soldShippedYield", soldShippedYield, other.soldShippedYield, decimalPrecision);
			result = result&&dtoUtils.equals("salesYield", salesYield, other.salesYield, decimalPrecision);
			result = result&&dtoUtils.equals("abandonmentYield", abandonmentYield, other.abandonmentYield, decimalPrecision);
			result = result&&dtoUtils.equals("totalProduction", totalProduction, other.totalProduction, decimalPrecision);
			result = result&&dtoUtils.equals("totalProductionOverride", totalProductionOverride, other.totalProductionOverride, decimalPrecision);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public DeclaredYieldFieldVarietyBerriesDto copy() {
		return new DeclaredYieldFieldVarietyBerriesDto(this);
	}

	public String getDeclaredYieldFieldVarietyBerriesGuid() {
		return declaredYieldFieldVarietyBerriesGuid;
	}

	public void setDeclaredYieldFieldVarietyBerriesGuid(String declaredYieldFieldVarietyBerriesGuid) {
		this.declaredYieldFieldVarietyBerriesGuid = declaredYieldFieldVarietyBerriesGuid;
	}

	public String getDeclaredYieldFieldCommodityBerriesGuid() {
		return declaredYieldFieldCommodityBerriesGuid;
	}

	public void setDeclaredYieldFieldCommodityBerriesGuid(String declaredYieldFieldCommodityBerriesGuid) {
		this.declaredYieldFieldCommodityBerriesGuid = declaredYieldFieldCommodityBerriesGuid;
	}

	public Integer getCropVarietyId() {
		return cropVarietyId;
	}

	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	}

	public Double getPlantedAcres() {
		return plantedAcres;
	}

	public void setPlantedAcres(Double plantedAcres) {
		this.plantedAcres = plantedAcres;
	}

	public Double getMatureEquivalentAcres() {
		return matureEquivalentAcres;
	}
	
	public void setMatureEquivalentAcres(Double matureEquivalentAcres) {
		this.matureEquivalentAcres = matureEquivalentAcres;
	}
	
	public Double getSoldShippedYield() {
		return soldShippedYield;
	}

	public void setSoldShippedYield(Double soldShippedYield) {
		this.soldShippedYield = soldShippedYield;
	}

	public Double getSalesYield() {
		return salesYield;
	}

	public void setSalesYield(Double salesYield) {
		this.salesYield = salesYield;
	}

	public Double getAbandonmentYield() {
		return abandonmentYield;
	}

	public void setAbandonmentYield(Double abandonmentYield) {
		this.abandonmentYield = abandonmentYield;
	}

	public Double getTotalProduction() {
		return totalProduction;
	}

	public void setTotalProduction(Double totalProduction) {
		this.totalProduction = totalProduction;
	}

	public Double getTotalProductionOverride() {
		return totalProductionOverride;
	}

	public void setTotalProductionOverride(Double totalProductionOverride) {
		this.totalProductionOverride = totalProductionOverride;
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

	public String getCropVarietyName() {
		return cropVarietyName;
	}

	public void setCropVarietyName(String cropVarietyName) {
		this.cropVarietyName = cropVarietyName;
	}
	
}
