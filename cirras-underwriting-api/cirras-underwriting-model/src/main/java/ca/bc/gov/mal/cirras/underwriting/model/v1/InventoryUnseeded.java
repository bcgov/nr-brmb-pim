package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;

//
// This is not going to be a resource.
//
public class InventoryUnseeded implements Serializable {
	private static final long serialVersionUID = 1L;

	private String inventoryUnseededGuid;
	private String inventoryFieldGuid;
	private Integer cropCommodityId;
	private String cropCommodityName;
	private Boolean isUnseededInsurableInd;
	private Double acresToBeSeeded;
	private Boolean deletedByUserInd;

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
	
	public Boolean getDeletedByUserInd() {
		return deletedByUserInd;
	}
	public void setDeletedByUserInd(Boolean deletedByUserInd) {
		this.deletedByUserInd = deletedByUserInd;
	}

}
