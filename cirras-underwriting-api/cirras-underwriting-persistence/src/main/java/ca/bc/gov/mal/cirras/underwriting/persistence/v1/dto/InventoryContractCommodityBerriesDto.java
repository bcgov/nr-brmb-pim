package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class InventoryContractCommodityBerriesDto extends BaseDto<InventoryContractCommodityBerriesDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventoryContractCommodityBerriesDto.class);

	private String inventoryContractCommodityBerriesGuid;
	private String inventoryContractGuid;
	private Integer cropCommodityId;
	private Integer totalInsuredPlants;
	private Integer totalUninsuredPlants;
	private Double totalQuantityInsuredAcres;
	private Double totalQuantityUninsuredAcres;
	private Double totalPlantInsuredAcres;
	private Double totalPlantUninsuredAcres;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	private String cropCommodityName;

	
	public InventoryContractCommodityBerriesDto() {
	}
	
	
	public InventoryContractCommodityBerriesDto(InventoryContractCommodityBerriesDto dto) {

		this.inventoryContractCommodityBerriesGuid = dto.inventoryContractCommodityBerriesGuid;
		this.inventoryContractGuid = dto.inventoryContractGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.totalInsuredPlants = dto.totalInsuredPlants;
		this.totalUninsuredPlants = dto.totalUninsuredPlants;
		this.totalQuantityInsuredAcres = dto.totalQuantityInsuredAcres;
		this.totalQuantityUninsuredAcres = dto.totalQuantityUninsuredAcres;
		this.totalPlantInsuredAcres = dto.totalPlantInsuredAcres;
		this.totalPlantUninsuredAcres = dto.totalPlantUninsuredAcres;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.cropCommodityName = dto.cropCommodityName;
	}
	

	@Override
	public boolean equalsBK(InventoryContractCommodityBerriesDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(InventoryContractCommodityBerriesDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("inventoryContractCommodityBerriesGuid", inventoryContractCommodityBerriesGuid, other.inventoryContractCommodityBerriesGuid);
			result = result&&dtoUtils.equals("inventoryContractGuid", inventoryContractGuid, other.inventoryContractGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("totalInsuredPlants", totalInsuredPlants, other.totalInsuredPlants);
			result = result&&dtoUtils.equals("totalUninsuredPlants", totalUninsuredPlants, other.totalUninsuredPlants);
			result = result&&dtoUtils.equals("totalQuantityInsuredAcres", totalQuantityInsuredAcres, other.totalQuantityInsuredAcres, 4);
			result = result&&dtoUtils.equals("totalQuantityUninsuredAcres", totalQuantityUninsuredAcres, other.totalQuantityUninsuredAcres, 4);
			result = result&&dtoUtils.equals("totalPlantInsuredAcres", totalPlantInsuredAcres, other.totalPlantInsuredAcres, 4);
			result = result&&dtoUtils.equals("totalPlantUninsuredAcres", totalPlantUninsuredAcres, other.totalPlantUninsuredAcres, 4);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public InventoryContractCommodityBerriesDto copy() {
		return new InventoryContractCommodityBerriesDto(this);
	}
	
	public String getInventoryContractCommodityBerriesGuid() {
		return inventoryContractCommodityBerriesGuid;
	}

	public void setInventoryContractCommodityBerriesGuid(String inventoryContractCommodityBerriesGuid) {
		this.inventoryContractCommodityBerriesGuid = inventoryContractCommodityBerriesGuid;
	}

	public String getInventoryContractGuid() {
		return inventoryContractGuid;
	}

	public void setInventoryContractGuid(String inventoryContractGuid) {
		this.inventoryContractGuid = inventoryContractGuid;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Integer getTotalInsuredPlants() {
		return totalInsuredPlants;
	}

	public void setTotalInsuredPlants(Integer totalInsuredPlants) {
		this.totalInsuredPlants = totalInsuredPlants;
	}

	public Integer getTotalUninsuredPlants() {
		return totalUninsuredPlants;
	}

	public void setTotalUninsuredPlants(Integer totalUninsuredPlants) {
		this.totalUninsuredPlants = totalUninsuredPlants;
	}

	public Double getTotalQuantityInsuredAcres() {
		return totalQuantityInsuredAcres;
	}

	public void setTotalQuantityInsuredAcres(Double totalQuantityInsuredAcres) {
		this.totalQuantityInsuredAcres = totalQuantityInsuredAcres;
	}

	public Double getTotalQuantityUninsuredAcres() {
		return totalQuantityUninsuredAcres;
	}

	public void setTotalQuantityUninsuredAcres(Double totalQuantityUninsuredAcres) {
		this.totalQuantityUninsuredAcres = totalQuantityUninsuredAcres;
	}

	public Double getTotalPlantInsuredAcres() {
		return totalPlantInsuredAcres;
	}

	public void setTotalPlantInsuredAcres(Double totalPlantInsuredAcres) {
		this.totalPlantInsuredAcres = totalPlantInsuredAcres;
	}

	public Double getTotalPlantUninsuredAcres() {
		return totalPlantUninsuredAcres;
	}

	public void setTotalPlantUninsuredAcres(Double totalPlantUninsuredAcres) {
		this.totalPlantUninsuredAcres = totalPlantUninsuredAcres;
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

}
