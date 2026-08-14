package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class DopYieldContractCommodity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String declaredYieldContractCommodityGuid;
	private String declaredYieldContractGuid;
	private Integer cropCommodityId;
	private Boolean isPedigreeInd;
	private Double harvestedAcres;
	private Double storedYield;
	private Double storedYieldDefaultUnit;
	private Double soldYield;
	private Double soldYieldDefaultUnit;
	private String gradeModifierTypeCode;

	private String cropCommodityName;
	private Double totalInsuredAcres;

 	public String getDeclaredYieldContractCommodityGuid() {
		return declaredYieldContractCommodityGuid;
	}

	public void setDeclaredYieldContractCommodityGuid(String declaredYieldContractCommodityGuid) {
		this.declaredYieldContractCommodityGuid = declaredYieldContractCommodityGuid;
	}
 
 	public String getDeclaredYieldContractGuid() {
		return declaredYieldContractGuid;
	}

	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid) {
		this.declaredYieldContractGuid = declaredYieldContractGuid;
	}
 
 	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}
 
 	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}

	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
	}
 
 	public Double getHarvestedAcres() {
		return harvestedAcres;
	}

	public void setHarvestedAcres(Double harvestedAcres) {
		this.harvestedAcres = harvestedAcres;
	}
 
 	public Double getStoredYield() {
		return storedYield;
	}

	public void setStoredYield(Double storedYield) {
		this.storedYield = storedYield;
	}
 
 	public Double getStoredYieldDefaultUnit() {
		return storedYieldDefaultUnit;
	}

	public void setStoredYieldDefaultUnit(Double storedYieldDefaultUnit) {
		this.storedYieldDefaultUnit = storedYieldDefaultUnit;
	}
 
 	public Double getSoldYield() {
		return soldYield;
	}

	public void setSoldYield(Double soldYield) {
		this.soldYield = soldYield;
	}
 
 	public Double getSoldYieldDefaultUnit() {
		return soldYieldDefaultUnit;
	}

	public void setSoldYieldDefaultUnit(Double soldYieldDefaultUnit) {
		this.soldYieldDefaultUnit = soldYieldDefaultUnit;
	}
 
 	public String getGradeModifierTypeCode() {
		return gradeModifierTypeCode;
	}

	public void setGradeModifierTypeCode(String gradeModifierTypeCode) {
		this.gradeModifierTypeCode = gradeModifierTypeCode;
	}
 
 	public String getCropCommodityName() {
		return cropCommodityName;
	}

	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}
 
 	public Double getTotalInsuredAcres() {
		return totalInsuredAcres;
	}

	public void setTotalInsuredAcres(Double totalInsuredAcres) {
		this.totalInsuredAcres = totalInsuredAcres;
	}
		
}
