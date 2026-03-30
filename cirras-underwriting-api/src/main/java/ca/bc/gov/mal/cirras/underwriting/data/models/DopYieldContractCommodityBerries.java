package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class DopYieldContractCommodityBerries implements Serializable {
	private static final long serialVersionUID = 1L;

	private String declaredYieldContractCommodityBerriesGuid;
	private String declaredYieldContractGuid;
	private Integer cropCommodityId;
	private String cropCommodityName;
	private Double totalProduction;
	private Double totalProductionOverride;
	private Double totalPlantedAcres;
	private Double totalMatureEquivalentAcres;
	private Double totalSoldShippedYield;
	private Double totalSalesYield;
	private Double totalAbandonmentYield;

	public String getDeclaredYieldContractCommodityBerriesGuid() {
		return declaredYieldContractCommodityBerriesGuid;
	}
	public void setDeclaredYieldContractCommodityBerriesGuid(String declaredYieldContractCommodityBerriesGuid) {
		this.declaredYieldContractCommodityBerriesGuid = declaredYieldContractCommodityBerriesGuid;
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

	public String getCropCommodityName() {
		return cropCommodityName;
	}
	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
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

	public Double getTotalPlantedAcres() {
		return totalPlantedAcres;
	}

	public void setTotalPlantedAcres(Double totalPlantedAcres) {
		this.totalPlantedAcres = totalPlantedAcres;
	}

	public Double getTotalMatureEquivalentAcres() {
		return totalMatureEquivalentAcres;
	}

	public void setTotalMatureEquivalentAcres(Double totalMatureEquivalentAcres) {
		this.totalMatureEquivalentAcres = totalMatureEquivalentAcres;
	}

	public Double getTotalSoldShippedYield() {
		return totalSoldShippedYield;
	}

	public void setTotalSoldShippedYield(Double totalSoldShippedYield) {
		this.totalSoldShippedYield = totalSoldShippedYield;
	}

	public Double getTotalSalesYield() {
		return totalSalesYield;
	}

	public void setTotalSalesYield(Double totalSalesYield) {
		this.totalSalesYield = totalSalesYield;
	}

	public Double getTotalAbandonmentYield() {
		return totalAbandonmentYield;
	}

	public void setTotalAbandonmentYield(Double totalAbandonmentYield) {
		this.totalAbandonmentYield = totalAbandonmentYield;
	}
	
	

}
