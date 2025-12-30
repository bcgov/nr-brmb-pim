package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class DopYieldFieldRollup implements Serializable {
	private static final long serialVersionUID = 1L;

	private String declaredYieldFieldRollupGuid;
	private String declaredYieldContractGuid;
	private Integer cropCommodityId;
	private Boolean isPedigreeInd;
	private Double estimatedYieldPerAcreTonnes;
	private Double estimatedYieldPerAcreBushels;
	
	private Double totalAcres; //Only used for rollup calculation
	private Double totalYield; //Only used for rollup calculation
	private String cropCommodityName;

 	public String getDeclaredYieldFieldRollupGuid() {
		return declaredYieldFieldRollupGuid;
	}

	public void setDeclaredYieldFieldRollupGuid(String declaredYieldFieldRollupGuid) {
		this.declaredYieldFieldRollupGuid = declaredYieldFieldRollupGuid;
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
 
 	public Double getEstimatedYieldPerAcreTonnes() {
		return estimatedYieldPerAcreTonnes;
	}

	public void setEstimatedYieldPerAcreTonnes(Double estimatedYieldPerAcreTonnes) {
		this.estimatedYieldPerAcreTonnes = estimatedYieldPerAcreTonnes;
	}
 
 	public Double getEstimatedYieldPerAcreBushels() {
		return estimatedYieldPerAcreBushels;
	}

	public void setEstimatedYieldPerAcreBushels(Double estimatedYieldPerAcreBushels) {
		this.estimatedYieldPerAcreBushels = estimatedYieldPerAcreBushels;
	}

	public String getCropCommodityName() {
		return cropCommodityName;
	}
	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}
	 
	public Double getTotalAcres() {
		return totalAcres;
	}

	public void setTotalAcres(Double totalAcres) {
		this.totalAcres = totalAcres;
	}	
	 
	public Double getTotalYield() {
		return totalYield;
	}

	public void setTotalYield(Double totalYield) {
		this.totalYield = totalYield;
	}	

}
