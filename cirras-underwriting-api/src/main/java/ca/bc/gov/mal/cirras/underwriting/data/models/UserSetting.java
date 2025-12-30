package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

public interface UserSetting extends Serializable {

	public String getUserSettingGuid();
	public void setUserSettingGuid(String userSettingGuid);

	public String getLoginUserGuid();
	public void setLoginUserGuid(String loginUserGuid);

	public String getLoginUserId();
	public void setLoginUserId(String loginUserId);

	public String getLoginUserType();
	public void setLoginUserType(String loginUserType);
	
	public String getGivenName();
	public void setGivenName(String givenName);

	public String getFamilyName();
	public void setFamilyName(String familyName);
	
	public Integer getPolicySearchCropYear();
	public void setPolicySearchCropYear(Integer policySearchCropYear);
	
	public Integer getPolicySearchInsurancePlanId();
	public void setPolicySearchInsurancePlanId(Integer policySearchInsurancePlanId);

	public String getPolicySearchInsurancePlanName();
	public void setPolicySearchInsurancePlanName(String policySearchInsurancePlanName);
	
	public Integer getPolicySearchOfficeId();
	public void setPolicySearchOfficeId(Integer policySearchOfficeId);

	public String getPolicySearchOfficeName();
	public void setPolicySearchOfficeName(String policySearchOfficeName);

}
