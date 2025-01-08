package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UserSetting;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.USER_SETTING_NAME)
@XmlSeeAlso({ UserSettingRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class UserSettingRsrc extends BaseResource implements UserSetting {

	private static final long serialVersionUID = 1L;

	// TODO: Rename to userSettingGuid?
	private String underwritingUserGuid;
	private String loginUserGuid;
	private String loginUserId;
	private String loginUserType;
	private String givenName;
	private String familyName;

	// Policy Search preferences
	private Integer policySearchCropYear;
	
	private Integer policySearchInsurancePlanId;
	private String policySearchInsurancePlanName;

	private Integer policySearchOfficeId;
	private String policySearchOfficeName;

	
	public String getUnderwritingUserGuid() {
		return underwritingUserGuid;
	}
	public void setUnderwritingUserGuid(String underwritingUserGuid) {
		this.underwritingUserGuid = underwritingUserGuid;
	}

	public String getLoginUserGuid() {
		return loginUserGuid;
	}
	public void setLoginUserGuid(String loginUserGuid) {
		this.loginUserGuid = loginUserGuid;
	}

	public String getLoginUserId() {
		return loginUserId;
	}
	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}

	public String getLoginUserType() {
		return loginUserType;
	}
	public void setLoginUserType(String loginUserType) {
		this.loginUserType = loginUserType;
	}
	
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	public Integer getPolicySearchCropYear() {
		return policySearchCropYear;
	}
	public void setPolicySearchCropYear(Integer policySearchCropYear) {
		this.policySearchCropYear = policySearchCropYear;
	}
	
	public Integer getPolicySearchInsurancePlanId() {
		return policySearchInsurancePlanId;
	}
	public void setPolicySearchInsurancePlanId(Integer policySearchInsurancePlanId) {
		this.policySearchInsurancePlanId = policySearchInsurancePlanId;
	}

	public String getPolicySearchInsurancePlanName() {
		return policySearchInsurancePlanName;
	}
	public void setPolicySearchInsurancePlanName(String policySearchInsurancePlanName) {
		this.policySearchInsurancePlanName = policySearchInsurancePlanName;
	}
	
	public Integer getPolicySearchOfficeId() {
		return policySearchOfficeId;
	}
	public void setPolicySearchOfficeId(Integer policySearchOfficeId) {
		this.policySearchOfficeId = policySearchOfficeId;
	}

	public String getPolicySearchOfficeName() {
		return policySearchOfficeName;
	}
	public void setPolicySearchOfficeName(String policySearchOfficeName) {
		this.policySearchOfficeName = policySearchOfficeName;
	}
	
}
