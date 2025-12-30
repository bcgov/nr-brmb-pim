package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.Date;

public interface Policy extends Serializable {

	public Integer getPolicyId();
	public void setPolicyId(Integer policyId);
	
	public Integer getGrowerId();
	public void setGrowerId(Integer growerId);

	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);

	public String getInsurancePlanName();
	public void setInsurancePlanName(String insurancePlanName);

	public Integer getOfficeId();
	public void setOfficeId(Integer officeId);

	public String getPolicyStatusCode();
	public void setPolicyStatusCode(String policyStatusCode);
	
	public String getPolicyNumber();
	public void setPolicyNumber(String policyNumber);

	public String getContractNumber();
	public void setContractNumber(String contractNumber);

	public Integer getContractId();
	public void setContractId(Integer contractId);

	public Integer getCropYear();
	public void setCropYear(Integer cropYear);

	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);

	public String getTransactionType();
	public void setTransactionType(String transactionType);
}
