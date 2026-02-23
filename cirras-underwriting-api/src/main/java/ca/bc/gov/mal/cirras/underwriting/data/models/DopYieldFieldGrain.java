package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.Date;


//
// This is not going to be a resource.
//
public class DopYieldFieldGrain implements Serializable {
	private static final long serialVersionUID = 1L;

	// DECLARED_YIELD_FIELD.
	private String declaredYieldFieldGuid;
	private String inventoryFieldGuid;
	private Double estimatedYieldPerAcre;
	private Double estimatedYieldPerAcreDefaultUnit;
	private Boolean unharvestedAcresInd;

	// INVENTORY_SEEDED_GRAIN.
	private String inventorySeededGrainGuid;
	private Integer cropCommodityId;
	private String cropCommodityName;
	private Integer cropVarietyId;
	private String cropVarietyName;
	private Boolean isPedigreeInd;
	private Date seededDate;
	private Double seededAcres;

	// INVENTORY_FIELD
	private Integer insurancePlanId;
	private Integer fieldId;
	private Integer cropYear;
	
	
	public String getDeclaredYieldFieldGuid() {
		return declaredYieldFieldGuid;
	}
	public void setDeclaredYieldFieldGuid(String declaredYieldFieldGuid) {
		this.declaredYieldFieldGuid = declaredYieldFieldGuid;
	}

	public String getInventoryFieldGuid() {
		return inventoryFieldGuid;
	}
	public void setInventoryFieldGuid(String inventoryFieldGuid) {
		this.inventoryFieldGuid = inventoryFieldGuid;
	}

	public Double getEstimatedYieldPerAcre() {
		return estimatedYieldPerAcre;
	}
	public void setEstimatedYieldPerAcre(Double estimatedYieldPerAcre) {
		this.estimatedYieldPerAcre = estimatedYieldPerAcre;
	}

	public Double getEstimatedYieldPerAcreDefaultUnit() {
		return estimatedYieldPerAcreDefaultUnit;
	}
	public void setEstimatedYieldPerAcreDefaultUnit(Double estimatedYieldPerAcreDefaultUnit) {
		this.estimatedYieldPerAcreDefaultUnit = estimatedYieldPerAcreDefaultUnit;
	}

	public Boolean getUnharvestedAcresInd() {
		return unharvestedAcresInd;
	}
	public void setUnharvestedAcresInd(Boolean unharvestedAcresInd) {
		this.unharvestedAcresInd = unharvestedAcresInd;
	}

	public String getInventorySeededGrainGuid() {
		return inventorySeededGrainGuid;
	}
	public void setInventorySeededGrainGuid(String inventorySeededGrainGuid) {
		this.inventorySeededGrainGuid = inventorySeededGrainGuid;
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

	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}
	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
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

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}
	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}
	
}
