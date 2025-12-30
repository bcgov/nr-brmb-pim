package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.Date;

public interface GrowerContractYear extends Serializable {

	public Integer getGrowerContractYearId();
	public void setGrowerContractYearId(Integer growerContractYearId);

	public Integer getContractId();
	public void setContractId(Integer contractId);

	public Integer getGrowerId();
	public void setGrowerId(Integer growerId);

	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);

	public Integer getCropYear();
	public void setCropYear(Integer cropYear);

	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);

	public String getTransactionType();
	public void setTransactionType(String transactionType);
}
