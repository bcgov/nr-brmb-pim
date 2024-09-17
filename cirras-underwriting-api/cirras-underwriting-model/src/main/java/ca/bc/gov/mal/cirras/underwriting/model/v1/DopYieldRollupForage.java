package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class DopYieldRollupForage implements Serializable {
	private static final long serialVersionUID = 1L;

	private String declaredYieldRollupForageGuid;
	private String declaredYieldContractGuid;
	private String commodityTypeCode;
	private Double totalFieldAcres;
	private Integer totalBalesLoads;
	private Double harvestedAcres;
	private Double quantityHarvestedTons;
	private Double yieldPerAcre;

	private String commodityTypeDescription;

	
	public String getDeclaredYieldRollupForageGuid() {
		return declaredYieldRollupForageGuid;
	}

	public void setDeclaredYieldRollupForageGuid(String declaredYieldRollupForageGuid) {
		this.declaredYieldRollupForageGuid = declaredYieldRollupForageGuid;
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

	public Integer getTotalBalesLoads() {
		return totalBalesLoads;
	}
	public void setTotalBalesLoads(Integer totalBalesLoads) {
		this.totalBalesLoads = totalBalesLoads;
	}
	
	public Double getHarvestedAcres() {
		return harvestedAcres;
	}

	public void setHarvestedAcres(Double harvestedAcres) {
		this.harvestedAcres = harvestedAcres;
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
		
}
