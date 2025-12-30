package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class InventoryUnseededDto extends BaseDto<InventoryUnseededDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventoryUnseededDto.class);

	private String inventoryUnseededGuid;
	private String inventoryFieldGuid;
	private Integer cropCommodityId;
	private String cropCommodityName;
	private Integer cropVarietyId;
	private Boolean isUnseededInsurableInd;
	private Double acresToBeSeeded;
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	private Double totalAcresToBeSeeded;
	//Commodity properties
	private Boolean isCropInsuranceEligibleInd;
	private Boolean isInventoryCropInd;
	private Integer cropInsurancePlanId;

	//Variety properties
	private String cropVarietyName;

	public InventoryUnseededDto() {
	}
	
	
	public InventoryUnseededDto(InventoryUnseededDto dto) {

		this.inventoryUnseededGuid = dto.inventoryUnseededGuid;
		this.inventoryFieldGuid = dto.inventoryFieldGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.cropCommodityName = dto.cropCommodityName;
		this.cropVarietyId = dto.cropVarietyId;
		this.isUnseededInsurableInd = dto.isUnseededInsurableInd;
		this.acresToBeSeeded = dto.acresToBeSeeded;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.totalAcresToBeSeeded = dto.totalAcresToBeSeeded;
		this.isInventoryCropInd = dto.isInventoryCropInd;
		this.isCropInsuranceEligibleInd = dto.isCropInsuranceEligibleInd;
		this.cropVarietyName = dto.cropVarietyName;
		this.cropInsurancePlanId = dto.cropInsurancePlanId;
	}
	

	@Override
	public boolean equalsBK(InventoryUnseededDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(InventoryUnseededDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("inventoryUnseededGuid", inventoryUnseededGuid, other.inventoryUnseededGuid);
			result = result&&dtoUtils.equals("inventoryFieldGuid", inventoryFieldGuid, other.inventoryFieldGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("cropVarietyId", cropVarietyId, other.cropVarietyId);
			result = result&&dtoUtils.equals("isUnseededInsurableInd", isUnseededInsurableInd, other.isUnseededInsurableInd);
			result = result&&dtoUtils.equals("acresToBeSeeded", acresToBeSeeded, other.acresToBeSeeded, 4);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public InventoryUnseededDto copy() {
		return new InventoryUnseededDto(this);
	}
	
	public String getInventoryUnseededGuid() {
		return inventoryUnseededGuid;
	}
	public void setInventoryUnseededGuid(String inventoryUnseededGuid) {
		this.inventoryUnseededGuid = inventoryUnseededGuid;
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

	public String getCropCommodityName() {
		return cropCommodityName;
	}
	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}

	public Integer getCropVarietyId() {
		return cropVarietyId;
	}
	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	}

	public String getCropVarietyName() {
		return cropVarietyName;
	}
	public void setCropVarietyName(String cropVarietyName) {
		this.cropVarietyName = cropVarietyName;
	}

	public Boolean getIsUnseededInsurableInd() {
		return isUnseededInsurableInd;
	}
	public void setIsUnseededInsurableInd(Boolean isUnseededInsurableInd) {
		this.isUnseededInsurableInd = isUnseededInsurableInd;
	}

	public Double getAcresToBeSeeded() {
		return acresToBeSeeded;
	}
	public void setAcresToBeSeeded(Double acresToBeSeeded) {
		this.acresToBeSeeded = acresToBeSeeded;
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

	public Double getTotalAcresToBeSeeded() {
		return totalAcresToBeSeeded;
	}
	public void setTotalAcresToBeSeeded(Double totalAcresToBeSeeded) {
		this.totalAcresToBeSeeded = totalAcresToBeSeeded;
	}

	public Boolean getIsInventoryCropInd() {
		return isInventoryCropInd;
	}

	public void setIsInventoryCropInd(Boolean isInventoryCropInd) {
		this.isInventoryCropInd = isInventoryCropInd;
	}

	public Boolean getIsCropInsuranceEligibleInd() {
		return isCropInsuranceEligibleInd;
	}

	public void setIsCropInsuranceEligibleInd(Boolean isCropInsuranceEligibleInd) {
		this.isCropInsuranceEligibleInd = isCropInsuranceEligibleInd;
	}

	public Integer getCropInsurancePlanId() {
		return cropInsurancePlanId;
	}
	public void setCropInsurancePlanId(Integer cropInsurancePlanId) {
		this.cropInsurancePlanId = cropInsurancePlanId;
	}
}
