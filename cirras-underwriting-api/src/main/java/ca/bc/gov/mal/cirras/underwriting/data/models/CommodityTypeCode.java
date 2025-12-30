package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.Date;

public interface CommodityTypeCode extends Serializable {

	public String getCommodityTypeCode();
	public void setCommodityTypeCode(String commodityTypeCode);

	public Integer getCropCommodityId();
	public void setCropCommodityId(Integer cropCommodityId);

	public String getDescription();
	public void setDescription(String description);
	 
 	public Date getEffectiveDate();
	public void setEffectiveDate(Date effectiveDate);
 
 	public Date getExpiryDate();
	public void setExpiryDate(Date expiryDate);

	public Boolean getIsActive();
	public void setIsActive(Boolean isActive);
	
	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);

	public String getTransactionType();
	public void setTransactionType(String transactionType);
	
 	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);

}
