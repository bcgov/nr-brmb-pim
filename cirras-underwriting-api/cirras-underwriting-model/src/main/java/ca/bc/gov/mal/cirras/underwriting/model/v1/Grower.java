package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;

public interface Grower extends Serializable {

	public Integer getGrowerId();
	public void setGrowerId(Integer growerId);

	public Integer getGrowerNumber();
	public void setGrowerNumber(Integer growerNumber);

	public String getGrowerName();
	public void setGrowerName(String growerName);

	public String getGrowerAddressLine1();
	public void setGrowerAddressLine1(String growerAddressLine1);

	public String getGrowerAddressLine2();
	public void setGrowerAddressLine2(String growerAddressLine2);

	public String getGrowerPostalCode();
	public void setGrowerPostalCode(String growerPostalCode);

	public String getGrowerCity();
	public void setGrowerCity(String growerCity);

	public Integer getCityId();
	public void setCityId(Integer cityId);

	public String getGrowerProvince();
	public void setGrowerProvince(String growerProvince);

	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);

	public String getTransactionType();
	public void setTransactionType(String transactionType);
}
