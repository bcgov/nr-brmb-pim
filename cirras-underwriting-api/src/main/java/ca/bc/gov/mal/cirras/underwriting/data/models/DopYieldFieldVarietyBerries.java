package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;


//
// This is not going to be a resource.
//
public class DopYieldFieldVarietyBerries implements Serializable {
	private static final long serialVersionUID = 1L;

	private String declaredYieldFieldVarietyBerriesGuid;
	private String declaredYieldFieldCommodityBerriesGuid;

	private Integer cropVarietyId;
	private String cropVarietyName;
	private Double plantedAcres;
	private Double soldShippedYield;
	private Double salesYield;
	private Double abandonmentYield;
	private Double totalProduction;
	private Double totalProductionOverride;
	private Boolean isHiddenOnPrintoutInd;

	public String getDeclaredYieldFieldVarietyBerriesGuid() {
		return declaredYieldFieldVarietyBerriesGuid;
	}
	public void setDeclaredYieldFieldVarietyBerriesGuid(String declaredYieldFieldVarietyBerriesGuid) {
		this.declaredYieldFieldVarietyBerriesGuid = declaredYieldFieldVarietyBerriesGuid;
	}

	public String getDeclaredYieldFieldCommodityBerriesGuid() {
		return declaredYieldFieldCommodityBerriesGuid;
	}
	public void setDeclaredYieldFieldCommodityBerriesGuid(String declaredYieldFieldCommodityBerriesGuid) {
		this.declaredYieldFieldCommodityBerriesGuid = declaredYieldFieldCommodityBerriesGuid;
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

	public Double getPlantedAcres() {
		return plantedAcres;
	}
	public void setPlantedAcres(Double plantedAcres) {
		this.plantedAcres = plantedAcres;
	}

	public Double getSoldShippedYield() {
		return soldShippedYield;
	}
	public void setSoldShippedYield(Double soldShippedYield) {
		this.soldShippedYield = soldShippedYield;
	}

	public Double getSalesYield() {
		return salesYield;
	}
	public void setSalesYield(Double salesYield) {
		this.salesYield = salesYield;
	}

	public Double getAbandonmentYield() {
		return abandonmentYield;
	}
	public void setAbandonmentYield(Double abandonmentYield) {
		this.abandonmentYield = abandonmentYield;
	}

	public Double getTotalProduction() {
		return totalProduction;
	}
	public void setTotalProduction(Double totalProduction) {
		this.totalProduction = totalProduction;
	}

	public Double getTotalProductionOverride() {
		return totalProductionOverride;
	}
	public void setTotalProductionOverride(Double totalProductionOverride) {
		this.totalProductionOverride = totalProductionOverride;
	}

	public Boolean getIsHiddenOnPrintoutInd() {
		return isHiddenOnPrintoutInd;
	}
	public void setIsHiddenOnPrintoutInd(Boolean isHiddenOnPrintoutInd) {
		this.isHiddenOnPrintoutInd = isHiddenOnPrintoutInd;
	}

}
