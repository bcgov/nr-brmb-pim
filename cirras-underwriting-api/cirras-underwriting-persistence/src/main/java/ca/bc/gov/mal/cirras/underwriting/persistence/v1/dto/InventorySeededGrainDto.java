package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.utils.DateUtils;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class InventorySeededGrainDto extends BaseDto<InventorySeededGrainDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventorySeededGrainDto.class);

	private String inventorySeededGrainGuid;
	private String inventoryFieldGuid;
	private Integer cropCommodityId;
	private String cropCommodityName;
	private Integer cropVarietyId;
	private String cropVarietyName;
	private String commodityTypeCode;
	private String commodityTypeDesc;
	private Boolean isQuantityInsurableInd;
	private Boolean isReplacedInd;
	private Boolean isPedigreeInd;
	private Boolean isSpotLossInsurableInd;
	private Date seededDate;
	private Double seededAcres;

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	private Double totalSeededAcres;
	private Double totalSpotLossAcres;
	
	public InventorySeededGrainDto() {
	}
	
	
	public InventorySeededGrainDto(InventorySeededGrainDto dto) {

		this.inventorySeededGrainGuid = dto.inventorySeededGrainGuid;
		this.inventoryFieldGuid = dto.inventoryFieldGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.cropCommodityName = dto.cropCommodityName;
		this.cropVarietyId = dto.cropVarietyId;
		this.cropVarietyName = dto.cropVarietyName;
		this.commodityTypeCode = dto.commodityTypeCode;
		this.commodityTypeDesc = dto.commodityTypeDesc;
		this.isQuantityInsurableInd = dto.isQuantityInsurableInd;
		this.isReplacedInd = dto.isReplacedInd;
		this.isPedigreeInd = dto.isPedigreeInd;
		this.isSpotLossInsurableInd = dto.isSpotLossInsurableInd;
		this.seededDate = dto.seededDate;
		this.seededAcres = dto.seededAcres;
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		
		this.totalSeededAcres = dto.totalSeededAcres;
		this.totalSpotLossAcres = dto.totalSpotLossAcres;

	}
	

	@Override
	public boolean equalsBK(InventorySeededGrainDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(InventorySeededGrainDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("inventorySeededGrainGuid", inventorySeededGrainGuid, other.inventorySeededGrainGuid);
			result = result&&dtoUtils.equals("inventoryFieldGuid", inventoryFieldGuid, other.inventoryFieldGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("cropVarietyId", cropVarietyId, other.cropVarietyId);
			result = result&&dtoUtils.equals("commodityTypeCode", commodityTypeCode, other.commodityTypeCode);
			result = result&&dtoUtils.equals("isQuantityInsurableInd", isQuantityInsurableInd, other.isQuantityInsurableInd);
			result = result&&dtoUtils.equals("isReplacedInd", isReplacedInd, other.isReplacedInd);
			result = result&&dtoUtils.equals("isPedigreeInd", isPedigreeInd, other.isPedigreeInd);
			result = result&&dtoUtils.equals("isSpotLossInsurableInd", isSpotLossInsurableInd, other.isSpotLossInsurableInd);
			result = result&&dtoUtils.equals("seededAcres", seededAcres, other.seededAcres, 4);
			//Nullable date
			result = result&&DateUtils.equalsDate(logger, "seededDate", seededDate, other.seededDate);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public InventorySeededGrainDto copy() {
		return new InventorySeededGrainDto(this);
	}
	
	public String getInventorySeededGrainGuid() {
		return inventorySeededGrainGuid;
	}
	public void setInventorySeededGrainGuid(String inventorySeededGrainGuid) {
		this.inventorySeededGrainGuid = inventorySeededGrainGuid;
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

	public String getCommodityTypeCode() {
		return commodityTypeCode;
	}
	public void setCommodityTypeCode(String commodityTypeCode) {
		this.commodityTypeCode = commodityTypeCode;
	}

	public String getCommodityTypeDesc() {
		return commodityTypeDesc;
	}
	public void setCommodityTypeDesc(String commodityTypeDesc) {
		this.commodityTypeDesc = commodityTypeDesc;
	}

	public Boolean getIsQuantityInsurableInd() {
		return isQuantityInsurableInd;
	}
	public void setIsQuantityInsurableInd(Boolean isQuantityInsurableInd) {
		this.isQuantityInsurableInd = isQuantityInsurableInd;
	}

	public Boolean getIsReplacedInd() {
		return isReplacedInd;
	}
	public void setIsReplacedInd(Boolean isReplacedInd) {
		this.isReplacedInd = isReplacedInd;
	}

	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}
	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
	}

	public Boolean getIsSpotLossInsurableInd() {
		return isSpotLossInsurableInd;
	}
	public void setIsSpotLossInsurableInd(Boolean isSpotLossInsurableInd) {
		this.isSpotLossInsurableInd = isSpotLossInsurableInd;
	}

	public Date getSeededDate() {
		return seededDate;
	}
	public void setSeededDate(Date seededDate) {
		this.seededDate = seededDate;
	}

	public Double getSeededAcres() {
		return seededAcres;
	}
	public void setSeededAcres(Double seededAcres) {
		this.seededAcres = seededAcres;
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

}
