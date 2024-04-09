package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;

public interface CommodityTypeVarietyXref extends Serializable {

	public String getCommodityTypeCode();
	public void setCommodityTypeCode(String commodityTypeCode);

	public Integer getCropVarietyId();
	public void setCropVarietyId(Integer cropVarietyId);
	
	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);

	public String getTransactionType();
	public void setTransactionType(String transactionType);
}
