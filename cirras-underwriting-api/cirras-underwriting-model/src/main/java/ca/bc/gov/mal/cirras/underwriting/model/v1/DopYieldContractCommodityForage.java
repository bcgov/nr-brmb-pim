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
	private Double harvestedAcresOverride;
	private Double quantityHarvestedTons;
	private Double quantityHarvestedTonsOverride;
	private Double yieldPerAcre;

	private String commodityTypeDescription;

	
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

	public Double getHarvestedAcresOverride() {
		return harvestedAcresOverride;
	}

	public void setHarvestedAcresOverride(Double harvestedAcresOverride) {
		this.harvestedAcresOverride = harvestedAcresOverride;
	}

	public Double getQuantityHarvestedTons() {
		return quantityHarvestedTons;
	}

	public void setQuantityHarvestedTons(Double quantityHarvestedTons) {
		this.quantityHarvestedTons = quantityHarvestedTons;
	}

	public Double getQuantityHarvestedTonsOverride() {
		return quantityHarvestedTonsOverride;
	}

	public void setQuantityHarvestedTonsOverride(Double quantityHarvestedTonsOverride) {
		this.quantityHarvestedTonsOverride = quantityHarvestedTonsOverride;
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
