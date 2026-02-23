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

}
