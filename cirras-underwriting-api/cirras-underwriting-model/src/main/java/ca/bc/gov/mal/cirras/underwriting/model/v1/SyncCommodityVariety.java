package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;


public interface SyncCommodityVariety extends Serializable {

	public Integer getCropId();
	public void setCropId(Integer cropId);

	public String getCropName();
	public void setCropName(String cropName);	
	
	public Integer getParentCropId();
	public void setParentCropId(Integer parentCropId);

	public Boolean getIsInventoryCrop();
	public void setIsInventoryCrop(Boolean isInventoryCrop);
	
	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);

	public String getShortLabel();
	public void setShortLabel(String shortLabel);

	public String getPlantDurationTypeCode();
	public void setPlantDurationTypeCode(String plantDurationTypeCode);

	public Boolean getIsYieldCrop();
	public void setIsYieldCrop(Boolean isYieldCrop);

	public Boolean getIsUnderwritingCrop();
	public void setIsUnderwritingCrop(Boolean isUnderwritingCrop);

	public Boolean getIsProductInsurableInd();
	public void setIsProductInsurableInd(Boolean isProductInsurableInd);

	public Boolean getIsCropInsuranceEligibleInd();
	public void setIsCropInsuranceEligibleInd(Boolean isCropInsuranceEligibleInd);

	public Boolean getIsPlantInsuranceEligibleInd();
	public void setIsPlantInsuranceEligibleInd(Boolean isPlantInsuranceEligibleInd);

	public Boolean getIsOtherInsuranceEligibleInd();
	public void setIsOtherInsuranceEligibleInd(Boolean isOtherInsuranceEligibleInd);

	public String getYieldMeasUnitTypeCode();
	public void setYieldMeasUnitTypeCode(String yieldMeasUnitTypeCode);

	public Integer getYieldDecimalPrecision();
	public void setYieldDecimalPrecision(Integer yieldDecimalPrecision);
	
	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);
	
	public String getTransactionType();
	public void setTransactionType(String transactionType);

}
