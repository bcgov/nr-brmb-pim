package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
//
// This is not going to be a resource.
//
public class DopYieldFieldForageCut implements Serializable {
	private static final long serialVersionUID = 1L;

	// DECLARED_YIELD_FIELD_FORAGE
	private String declaredYieldFieldForageGuid;
	private String inventoryFieldGuid;
	private Integer cutNumber;
	private Integer totalBalesLoads;
	private Double weight;
	private Double weightDefaultUnit;
	private Double moisturePercent;
	private Boolean deletedByUserInd;
	
	public String getDeclaredYieldFieldForageGuid() {
		return declaredYieldFieldForageGuid;
	}

	public void setDeclaredYieldFieldForageGuid(String declaredYieldFieldForageGuid) {
		this.declaredYieldFieldForageGuid = declaredYieldFieldForageGuid;
	}

	public String getInventoryFieldGuid() {
		return inventoryFieldGuid;
	}

	public void setInventoryFieldGuid(String inventoryFieldGuid) {
		this.inventoryFieldGuid = inventoryFieldGuid;
	}

	public Integer getCutNumber() {
		return cutNumber;
	}

	public void setCutNumber(Integer cutNumber) {
		this.cutNumber = cutNumber;
	}

	public Integer getTotalBalesLoads() {
		return totalBalesLoads;
	}

	public void setTotalBalesLoads(Integer totalBalesLoads) {
		this.totalBalesLoads = totalBalesLoads;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getWeightDefaultUnit() {
		return weightDefaultUnit;
	}

	public void setWeightDefaultUnit(Double weightDefaultUnit) {
		this.weightDefaultUnit = weightDefaultUnit;
	}

	public Double getMoisturePercent() {
		return moisturePercent;
	}

	public void setMoisturePercent(Double moisturePercent) {
		this.moisturePercent = moisturePercent;
	}
	
	public Boolean getDeletedByUserInd() {
		return deletedByUserInd;
	}

	public void setDeletedByUserInd(Boolean deletedByUserInd) {
		this.deletedByUserInd = deletedByUserInd;
	}
}
