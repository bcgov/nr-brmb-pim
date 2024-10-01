package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class DopYieldContractCommodityForage implements Serializable {
	private static final long serialVersionUID = 1L;

	private String declaredYieldContractCmdtyForageGuid;
	private String declaredYieldContractGuid;
	private String commodityTypeCode;
	private Double totalFieldAcres;
	private Double harvestedAcres;
	private Integer totalBalesLoads;
	private Double weight;
	private Double weightDefaultUnit;
	private Double moisturePercent;
	private Double quantityHarvestedTons;
	private Double yieldPerAcre;

	private String commodityTypeDescription;
	private Integer cropCommodityId;
	private String plantDurationTypeCode;
	
	public String getDeclaredYieldContractCmdtyForageGuid() {
		return declaredYieldContractCmdtyForageGuid;
	}

	public void setDeclaredYieldContractCmdtyForageGuid(String declaredYieldContractCmdtyForageGuid) {
		this.declaredYieldContractCmdtyForageGuid = declaredYieldContractCmdtyForageGuid;
	}

	public String getDeclaredYieldContractGuid() {
		return declaredYieldContractGuid;
	}

	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid) {
		this.declaredYieldContractGuid = declaredYieldContractGuid;
	}

	public String getCommodityTypeCode() {
		return commodityTypeCode;
	}

	public void setCommodityTypeCode(String commodityTypeCode) {
		this.commodityTypeCode = commodityTypeCode;
	}

	public Double getTotalFieldAcres() {
		return totalFieldAcres;
	}
	
	public void setTotalFieldAcres(Double totalFieldAcres) {
		this.totalFieldAcres = totalFieldAcres;
	}

	public Double getHarvestedAcres() {
		return harvestedAcres;
	}

	public void setHarvestedAcres(Double harvestedAcres) {
		this.harvestedAcres = harvestedAcres;
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

	public Double getQuantityHarvestedTons() {
		return quantityHarvestedTons;
	}

	public void setQuantityHarvestedTons(Double quantityHarvestedTons) {
		this.quantityHarvestedTons = quantityHarvestedTons;
	}

	public Double getYieldPerAcre() {
		return yieldPerAcre;
	}

	public void setYieldPerAcre(Double yieldPerAcre) {
		this.yieldPerAcre = yieldPerAcre;
	}

	public String getCommodityTypeDescription() {
		return commodityTypeDescription;
	}

	public void setCommodityTypeDescription(String commodityTypeDescription) {
		this.commodityTypeDescription = commodityTypeDescription;
	}
		
 	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public String getPlantDurationTypeCode() {
		return plantDurationTypeCode;
	}

	public void setPlantDurationTypeCode(String plantDurationTypeCode) {
		this.plantDurationTypeCode = plantDurationTypeCode;
	}
}
