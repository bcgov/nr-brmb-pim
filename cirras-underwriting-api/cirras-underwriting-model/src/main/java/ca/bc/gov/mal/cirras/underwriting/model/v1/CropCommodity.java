package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public interface CropCommodity extends Serializable {

 	public Integer getCropCommodityId();
	public void setCropCommodityId(Integer cropCommodityId);
 
 	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);
 
 	public String getCommodityName();
	public void setCommodityName(String commodityName);
 
 	public String getShortLabel();
	public void setShortLabel(String shortLabel);
 
 	public String getPlantDurationTypeCode();
	public void setPlantDurationTypeCode(String plantDurationTypeCode);
 
 	public Boolean getIsInventoryCropInd();
	public void setIsInventoryCropInd(Boolean isInventoryCropInd);
 
 	public Boolean getIsYieldCropInd();
	public void setIsYieldCropInd(Boolean isYieldCropInd);
 
 	public Boolean getIsUnderwritingCropInd();
	public void setIsUnderwritingCropInd(Boolean isUnderwritingCropInd);

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
 
 	public String getInventoryTypeCode();
	public void setInventoryTypeCode(String inventoryTypeCode);
 
 	public String getYieldTypeCode();
	public void setYieldTypeCode(String yieldTypeCode);
	
	public List<CropVariety> getCropVariety();
	public void setCropVariety(List<CropVariety> varieties);

	
}
