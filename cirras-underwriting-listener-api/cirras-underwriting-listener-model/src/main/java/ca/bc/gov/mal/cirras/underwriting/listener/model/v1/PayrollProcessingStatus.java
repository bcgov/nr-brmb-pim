package ca.bc.gov.mal.cirras.underwriting.listener.model.v1;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface PayrollProcessingStatus extends Serializable {

	public String getPayrollProcessingStatusGuid();
	public void setPayrollProcessingStatusGuid(String payrollProcessingStatusGuid);

	public String getDiaryVersionGuid();
	public void setDiaryVersionGuid(String diaryVersionGuid);

	public String getPayrollProcessingStatusCode();
	public void setPayrollProcessingStatusCode(String payrollProcessingStatusCode);

	public String getUserUserid();
	public void setUserUserid(String userUserid);

	public String getUserType();
	public void setUserType(String userType);

	public String getUserGuid();
	public void setUserGuid(String userGuid);

	public LocalDateTime getValidStartTimestamp();
	public void setValidStartTimestamp(LocalDateTime validStartTimestamp);

	public LocalDateTime getValidEndTimestamp();
	public void setValidEndTimestamp(LocalDateTime validEndTimestamp);
	
}
