package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;


//
// This is not going to be a resource.
//
public class InventorySeededGrain implements Serializable {
	private static final long serialVersionUID = 1L;

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
	private Boolean deletedByUserInd;
	
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
	
	public Boolean getDeletedByUserInd() {
		return deletedByUserInd;
	}
	public void setDeletedByUserInd(Boolean deletedByUserInd) {
		this.deletedByUserInd = deletedByUserInd;
	}
}
