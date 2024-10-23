package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class VerifiedYieldContractCommodity implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO: Check against db names.
	private String verifiedYieldContractCommodityGuid;
	private String verifiedYieldContractGuid;
	private Integer cropCommodityId;
	private Boolean isPedigreeInd;
	private Double harvestedAcres;
	private Double harvestedAcresOverride;
	private Double storedYieldDefaultUnit;
	private Double soldYieldDefaultUnit;
	private Double harvestedYield;
	private Double harvestedYieldOverride;
	private Double harvestedYieldPerAcre;
	private String gradeModifierTypeCode;  // TODO: Do we need this?

	private String cropCommodityName;
	private Double totalInsuredAcres;
	private Double productionGuarantee;
	
	public String getVerifiedYieldContractCommodityGuid() {
		return verifiedYieldContractCommodityGuid;
	}
	public void setVerifiedYieldContractCommodityGuid(String verifiedYieldContractCommodityGuid) {
		this.verifiedYieldContractCommodityGuid = verifiedYieldContractCommodityGuid;
	}

	public String getVerifiedYieldContractGuid() {
		return verifiedYieldContractGuid;
	}
	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid) {
		this.verifiedYieldContractGuid = verifiedYieldContractGuid;
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

	public Double getHarvestedAcresOverride() {
		return harvestedAcresOverride;
	}
	public void setHarvestedAcresOverride(Double harvestedAcresOverride) {
		this.harvestedAcresOverride = harvestedAcresOverride;
	}
	
	public Double getStoredYieldDefaultUnit() {
		return storedYieldDefaultUnit;
	}
	public void setStoredYieldDefaultUnit(Double storedYieldDefaultUnit) {
		this.storedYieldDefaultUnit = storedYieldDefaultUnit;
	}

	public Double getSoldYieldDefaultUnit() {
		return soldYieldDefaultUnit;
	}
	public void setSoldYieldDefaultUnit(Double soldYieldDefaultUnit) {
		this.soldYieldDefaultUnit = soldYieldDefaultUnit;
	}

	public Double getHarvestedYield() {
		return harvestedYield;
	}
	public void setHarvestedYield(Double harvestedYield) {
		this.harvestedYield = harvestedYield;
	}
	
	public Double getHarvestedYieldOverride() {
		return harvestedYieldOverride;
	}
	public void setHarvestedYieldOverride(Double harvestedYieldOverride) {
		this.harvestedYieldOverride = harvestedYieldOverride;
	}

	public Double getHarvestedYieldPerAcre() {
		return harvestedYieldPerAcre;
	}
	public void setHarvestedYieldPerAcre(Double harvestedYieldPerAcre) {
		this.harvestedYieldPerAcre = harvestedYieldPerAcre;
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

	public Double getProductionGuarantee() {
		return productionGuarantee;
	}
	public void setProductionGuarantee(Double productionGuarantee) {
		this.productionGuarantee = productionGuarantee;
	}
		
}
