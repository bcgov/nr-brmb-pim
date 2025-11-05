package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.utils.DateUtils;
import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class InventoryBerriesDto extends BaseDto<InventoryBerriesDto> {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(InventoryBerriesDto.class);

	private String inventoryBerriesGuid;
	private String inventoryFieldGuid;
	private Integer cropCommodityId;
	private Integer cropVarietyId;
	private String plantInsurabilityTypeCode;
	private Integer plantedYear;
	private Double plantedAcres;
	private Integer rowSpacing;
	private Double plantSpacing;
	private Integer totalPlants;
	private Boolean isQuantityInsurableInd;
	private Boolean isPlantInsurableInd;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	private String cropCommodityName;
	private String cropVarietyName;
	
	public InventoryBerriesDto() {
	}
	
	
	public InventoryBerriesDto(InventoryBerriesDto dto) {

		this.inventoryBerriesGuid = dto.inventoryBerriesGuid;
		this.inventoryFieldGuid = dto.inventoryFieldGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.cropVarietyId = dto.cropVarietyId;
		this.plantedYear = dto.plantedYear;
		this.plantInsurabilityTypeCode = dto.plantInsurabilityTypeCode;
		this.plantedAcres = dto.plantedAcres;
		this.rowSpacing = dto.rowSpacing;
		this.plantSpacing = dto.plantSpacing;
		this.totalPlants = dto.totalPlants;
		this.isQuantityInsurableInd = dto.isQuantityInsurableInd;
		this.isPlantInsurableInd = dto.isPlantInsurableInd;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;


		this.cropCommodityName = dto.cropCommodityName;
		this.cropVarietyName = dto.cropVarietyName;

	}
	

	@Override
	public boolean equalsBK(InventoryBerriesDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(InventoryBerriesDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("inventoryBerriesGuid", inventoryBerriesGuid, other.inventoryBerriesGuid);
			result = result&&dtoUtils.equals("inventoryFieldGuid", inventoryFieldGuid, other.inventoryFieldGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("cropVarietyId", cropVarietyId, other.cropVarietyId);
			result = result&&dtoUtils.equals("plantedYear", plantedYear, other.plantedYear);
			result = result&&dtoUtils.equals("plantInsurabilityTypeCode", plantInsurabilityTypeCode, other.plantInsurabilityTypeCode);
			result = result&&dtoUtils.equals("plantedAcres", plantedAcres, other.plantedAcres, 4);
			result = result&&dtoUtils.equals("rowSpacing", rowSpacing, other.rowSpacing);
			result = result&&dtoUtils.equals("plantSpacing", plantSpacing, other.plantSpacing, 4);
			result = result&&dtoUtils.equals("totalPlants", totalPlants, other.totalPlants);
			result = result&&dtoUtils.equals("isQuantityInsurableInd", isQuantityInsurableInd, other.isQuantityInsurableInd);
			result = result&&dtoUtils.equals("isPlantInsurableInd", isPlantInsurableInd, other.isPlantInsurableInd);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public InventoryBerriesDto copy() {
		return new InventoryBerriesDto(this);
	}
	
	public String getInventoryBerriesGuid() {
		return inventoryBerriesGuid;
	}

	public void setInventoryBerriesGuid(String inventoryBerriesGuid) {
		this.inventoryBerriesGuid = inventoryBerriesGuid;
	}

	public String getInventoryFieldGuid() {
		return inventoryFieldGuid;
	}

	public void setInventoryFieldGuid(String inventoryFieldGuid) {
		this.inventoryFieldGuid = inventoryFieldGuid;
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

	public String getPlantInsurabilityTypeCode() {
		return plantInsurabilityTypeCode;
	}

	public void setPlantInsurabilityTypeCode(String plantInsurabilityTypeCode) {
		this.plantInsurabilityTypeCode = plantInsurabilityTypeCode;
	}

	public Integer getPlantedYear() {
		return plantedYear;
	}

	public void setPlantedYear(Integer plantedYear) {
		this.plantedYear = plantedYear;
	}

	public Double getPlantedAcres() {
		return plantedAcres;
	}

	public void setPlantedAcres(Double plantedAcres) {
		this.plantedAcres = plantedAcres;
	}

	public Integer getRowSpacing() {
		return rowSpacing;
	}

	public void setRowSpacing(Integer rowSpacing) {
		this.rowSpacing = rowSpacing;
	}

	public Double getPlantSpacing() {
		return plantSpacing;
	}

	public void setPlantSpacing(Double plantSpacing) {
		this.plantSpacing = plantSpacing;
	}

	public Integer getTotalPlants() {
		return totalPlants;
	}

	public void setTotalPlants(Integer
			totalPlants) {
		this.totalPlants = totalPlants;
	}

	public Boolean getIsQuantityInsurableInd() {
		return isQuantityInsurableInd;
	}

	public void setIsQuantityInsurableInd(Boolean isQuantityInsurableInd) {
		this.isQuantityInsurableInd = isQuantityInsurableInd;
	}

	public Boolean getIsPlantInsurableInd() {
		return isPlantInsurableInd;
	}

	public void setIsPlantInsurableInd(Boolean isPlantInsurableInd) {
		this.isPlantInsurableInd = isPlantInsurableInd;
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


}
