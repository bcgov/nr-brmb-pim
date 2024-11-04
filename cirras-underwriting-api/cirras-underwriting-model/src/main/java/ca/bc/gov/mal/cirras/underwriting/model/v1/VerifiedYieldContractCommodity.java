package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class VerifiedYieldContractCommodity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String verifiedYieldContractCommodityGuid;
	private String verifiedYieldContractGuid;
	private Integer cropCommodityId;
	private Boolean isPedigreeInd;
	private Double harvestedAcres;
	private Double harvestedAcresOverride;
	private Double storedYieldDefaultUnit;
	private Double soldYieldDefaultUnit;
	private Double productionGuarantee;
	private Double harvestedYield;
	private Double harvestedYieldOverride;
	private Double yieldPerAcre;

	private String cropCommodityName;
	private Double totalInsuredAcres;
	
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

	public Double getProductionGuarantee() {
		return productionGuarantee;
	}
	public void setProductionGuarantee(Double productionGuarantee) {
		this.productionGuarantee = productionGuarantee;
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

	public Double getYieldPerAcre() {
		return yieldPerAcre;
	}
	public void setYieldPerAcre(Double yieldPerAcre) {
		this.yieldPerAcre = yieldPerAcre;
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
