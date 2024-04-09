package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class InventoryCoverageTotalForageDto extends BaseDto<InventoryCoverageTotalForageDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventoryCoverageTotalForageDto.class);

	private String inventoryCoverageTotalForageGuid;
	private String inventoryContractGuid;
	private Integer cropCommodityId;
	private String plantInsurabilityTypeCode;	
	private Double totalFieldAcres;
	private Boolean isUnseededInsurableInd;

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	//Extended columns
	private String cropCommodityName;
	private String plantInsurabilityTypeDesc;
	
	public InventoryCoverageTotalForageDto() {
	}
	
	
	public InventoryCoverageTotalForageDto(InventoryCoverageTotalForageDto dto) {

		this.inventoryCoverageTotalForageGuid = dto.inventoryCoverageTotalForageGuid;		
		this.inventoryContractGuid = dto.inventoryContractGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.plantInsurabilityTypeCode = dto.plantInsurabilityTypeCode;
		this.totalFieldAcres = dto.totalFieldAcres;
		this.isUnseededInsurableInd = dto.isUnseededInsurableInd;

		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.cropCommodityName = dto.cropCommodityName;
		this.plantInsurabilityTypeDesc = dto.plantInsurabilityTypeDesc;	
	}
	

	@Override
	public boolean equalsBK(InventoryCoverageTotalForageDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(InventoryCoverageTotalForageDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("inventoryCoverageTotalForageGuid", inventoryCoverageTotalForageGuid, other.inventoryCoverageTotalForageGuid);
			result = result&&dtoUtils.equals("inventoryContractGuid", inventoryContractGuid, other.inventoryContractGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("plantInsurabilityTypeCode", plantInsurabilityTypeCode, other.plantInsurabilityTypeCode);
			result = result&&dtoUtils.equals("totalFieldAcres", totalFieldAcres, other.totalFieldAcres, 4);
			result = result&&dtoUtils.equals("isUnseededInsurableInd", isUnseededInsurableInd, other.isUnseededInsurableInd);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public InventoryCoverageTotalForageDto copy() {
		return new InventoryCoverageTotalForageDto(this);
	}	
 
 	public String getInventoryCoverageTotalForageGuid() {
		return inventoryCoverageTotalForageGuid;
	}
	public void setInventoryCoverageTotalForageGuid(String inventoryCoverageTotalForageGuid) {
		this.inventoryCoverageTotalForageGuid = inventoryCoverageTotalForageGuid;
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

	public String getPlantInsurabilityTypeCode() {
		return plantInsurabilityTypeCode;
	}
	public void setPlantInsurabilityTypeCode(String plantInsurabilityTypeCode) {
		this.plantInsurabilityTypeCode = plantInsurabilityTypeCode;
	}

	public Double getTotalFieldAcres() {
		return totalFieldAcres;
	}
	public void setTotalFieldAcres(Double totalFieldAcres) {
		this.totalFieldAcres = totalFieldAcres;
	}
	
	public Boolean getIsUnseededInsurableInd() {
		return isUnseededInsurableInd;
	}
	public void setIsUnseededInsurableInd(Boolean isUnseededInsurableInd) {
		this.isUnseededInsurableInd = isUnseededInsurableInd;
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

	public String getPlantInsurabilityTypeDesc() {
		return plantInsurabilityTypeDesc;
	}
	public void setPlantInsurabilityTypeDesc(String plantInsurabilityTypeDesc) {
		this.plantInsurabilityTypeDesc = plantInsurabilityTypeDesc;
	}
	
}
