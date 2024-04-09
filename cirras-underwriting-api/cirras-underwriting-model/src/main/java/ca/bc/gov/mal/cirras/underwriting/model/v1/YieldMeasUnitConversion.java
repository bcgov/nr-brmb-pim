package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

public class YieldMeasUnitConversion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String yieldMeasUnitConversionGuid;
	private Integer cropCommodityId;
	private String srcYieldMeasUnitTypeCode;
	private String targetYieldMeasUnitTypeCode;
	private Integer versionNumber;
	private Integer effectiveCropYear;
	private Integer expiryCropYear;
	private Double conversionFactor;

	private Integer insurancePlanId;
	private String commodityName;
	private Boolean deletedByUserInd;


	public String getYieldMeasUnitConversionGuid() {
		return yieldMeasUnitConversionGuid;
	}

	public void setYieldMeasUnitConversionGuid(String yieldMeasUnitConversionGuid) {
		this.yieldMeasUnitConversionGuid = yieldMeasUnitConversionGuid;
	}
	
	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public String getSrcYieldMeasUnitTypeCode() {
		return srcYieldMeasUnitTypeCode;
	}

	public void setSrcYieldMeasUnitTypeCode(String srcYieldMeasUnitTypeCode) {
		this.srcYieldMeasUnitTypeCode = srcYieldMeasUnitTypeCode;
	}

	public String getTargetYieldMeasUnitTypeCode() {
		return targetYieldMeasUnitTypeCode;
	}

	public void setTargetYieldMeasUnitTypeCode(String targetYieldMeasUnitTypeCode) {
		this.targetYieldMeasUnitTypeCode = targetYieldMeasUnitTypeCode;
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Integer getEffectiveCropYear() {
		return effectiveCropYear;
	}

	public void setEffectiveCropYear(Integer effectiveCropYear) {
		this.effectiveCropYear = effectiveCropYear;
	}

	public Integer getExpiryCropYear() {
		return expiryCropYear;
	}

	public void setExpiryCropYear(Integer expiryCropYear) {
		this.expiryCropYear = expiryCropYear;
	}

	public Double getConversionFactor() {
		return conversionFactor;
	}
	public void setConversionFactor(Double conversionFactor) {
		this.conversionFactor = conversionFactor;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
	
 	public String getCommodityName() {
		return commodityName;
	}
	
 	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public Boolean getDeletedByUserInd() {
		return deletedByUserInd;
	}

	public void setDeletedByUserInd(Boolean deletedByUserInd) {
		this.deletedByUserInd = deletedByUserInd;
	}
}
