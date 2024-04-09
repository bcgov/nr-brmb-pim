package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class InventoryContractCommodityDto extends BaseDto<InventoryContractCommodityDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventoryContractCommodityDto.class);

	private String inventoryContractCommodityGuid;
	private String inventoryContractGuid;
	private Integer cropCommodityId;
	private String cropCommodityName;
	private Double totalUnseededAcres;
	private Double totalUnseededAcresOverride;
	private Double totalSeededAcres;
	private Double totalSpotLossAcres;
	private Boolean isPedigreeInd;

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public InventoryContractCommodityDto() {
	}
	
	
	public InventoryContractCommodityDto(InventoryContractCommodityDto dto) {

		this.inventoryContractCommodityGuid = dto.inventoryContractCommodityGuid;
		this.inventoryContractGuid = dto.inventoryContractGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.cropCommodityName = dto.cropCommodityName;
		this.totalUnseededAcres = dto.totalUnseededAcres;
		this.totalUnseededAcresOverride = dto.totalUnseededAcresOverride;
		this.totalSeededAcres = dto.totalSeededAcres;
		this.totalSpotLossAcres = dto.totalSpotLossAcres;
		this.isPedigreeInd = dto.isPedigreeInd;

		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	}
	

	@Override
	public boolean equalsBK(InventoryContractCommodityDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(InventoryContractCommodityDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("inventoryContractCommodityGuid", inventoryContractCommodityGuid, other.inventoryContractCommodityGuid);
			result = result&&dtoUtils.equals("inventoryContractGuid", inventoryContractGuid, other.inventoryContractGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("totalUnseededAcres", totalUnseededAcres, other.totalUnseededAcres, 4);
			result = result&&dtoUtils.equals("totalUnseededAcresOverride", totalUnseededAcresOverride, other.totalUnseededAcresOverride, 4);
			result = result&&dtoUtils.equals("totalSeededAcres", totalSeededAcres, other.totalSeededAcres, 4);
			result = result&&dtoUtils.equals("totalSpotLossAcres", totalSpotLossAcres, other.totalSpotLossAcres, 4);
			result = result&&dtoUtils.equals("isPedigreeInd", isPedigreeInd, other.isPedigreeInd);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public InventoryContractCommodityDto copy() {
		return new InventoryContractCommodityDto(this);
	}
	
	public String getInventoryContractCommodityGuid() {
		return inventoryContractCommodityGuid;
	}
	public void setInventoryContractCommodityGuid(String inventoryContractCommodityGuid) {
		this.inventoryContractCommodityGuid = inventoryContractCommodityGuid;
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

	public String getCropCommodityName() {
		return cropCommodityName;
	}
	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}

	public Double getTotalUnseededAcres() {
		return totalUnseededAcres;
	}
	public void setTotalUnseededAcres(Double totalUnseededAcres) {
		this.totalUnseededAcres = totalUnseededAcres;
	}

	public Double getTotalUnseededAcresOverride() {
		return totalUnseededAcresOverride;
	}
	public void setTotalUnseededAcresOverride(Double totalUnseededAcresOverride) {
		this.totalUnseededAcresOverride = totalUnseededAcresOverride;
	}

	public Double getTotalSeededAcres() {
		return totalSeededAcres;
	}
	public void setTotalSeededAcres(Double totalSeededAcres) {
		this.totalSeededAcres = totalSeededAcres;
	}

	public Double getTotalSpotLossAcres() {
		return totalSpotLossAcres;
	}
	public void setTotalSpotLossAcres(Double totalSpotLossAcres) {
		this.totalSpotLossAcres = totalSpotLossAcres;
	}	

	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}
	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
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

}
