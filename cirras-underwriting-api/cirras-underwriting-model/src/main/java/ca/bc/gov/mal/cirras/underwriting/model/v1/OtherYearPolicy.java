package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

public class OtherYearPolicy implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer policyId;
	private Integer insurancePlanId;
	private String policyNumber;
	private Integer cropYear;
	private String screenRecordGuid;
	private String screenType;
	
	
 	public Integer getPolicyId() {
		return policyId;
	}
	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}
	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

 	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
 
 	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public String getScreenRecordGuid() {
		return screenRecordGuid;
	}
	public void setScreenRecordGuid(String screenRecordGuid) {
		this.screenRecordGuid = screenRecordGuid;
	}

	public String getScreenType() {
		return screenType;
	}
	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}
	
}
