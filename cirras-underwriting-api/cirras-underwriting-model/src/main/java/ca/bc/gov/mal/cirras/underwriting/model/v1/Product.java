package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;

public interface Product extends Serializable {

	public Integer getProductId();
	public void setProductId(Integer productId);

	public Integer getPolicyId();
	public void setPolicyId(Integer policyId);

	public Integer getCropCommodityId();
	public void setCropCommodityId(Integer cropCommodityId);

	public String getCommodityCoverageCode();
	public void setCommodityCoverageCode(String commodityCoverageCode);

	public String getProductStatusCode();
	public void setProductStatusCode(String productStatusCode);

	public Integer getDeductibleLevel();
	public void setDeductibleLevel(Integer deductibleLevel);

	public Double getProductionGuarantee();
	public void setProductionGuarantee(Double productionGuarantee);

	public Double getProbableYield();
	public void setProbableYield(Double probableYield);

	public String getInsuredByMeasType();
	public void setInsuredByMeasType(String insuredByMeasType);
	
	public Double getInsurableValueHundredPercent();
	public void setInsurableValueHundredPercent(Double insurableValueHundredPercent);

	public Double getCoverageDollars();
	public void setCoverageDollars(Double coverageDollars);

	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);

	public String getTransactionType();
	public void setTransactionType(String transactionType);
	
}
