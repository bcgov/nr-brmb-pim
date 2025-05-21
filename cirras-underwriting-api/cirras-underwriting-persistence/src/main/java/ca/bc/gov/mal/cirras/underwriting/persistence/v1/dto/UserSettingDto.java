package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class UserSettingDto extends BaseDto<UserSettingDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(UserSettingDto.class);

	private String userSettingGuid;
	private String loginUserGuid;
	private String loginUserId;
	private String loginUserType;
	private String givenName;
	private String familyName;
	private Integer policySearchCropYear;
	private Integer policySearchInsurancePlanId;
	private Integer policySearchOfficeId;
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	//Extended columns
	private String policySearchInsurancePlanName;
	private String policySearchOfficeName;
	
	public UserSettingDto() {
	}
	
	
	public UserSettingDto(UserSettingDto dto) {
		
		this.userSettingGuid = dto.userSettingGuid;
		this.loginUserGuid = dto.loginUserGuid;
		this.loginUserId = dto.loginUserId;
		this.loginUserType = dto.loginUserType;
		this.givenName = dto.givenName;
		this.familyName = dto.familyName;
		this.policySearchCropYear = dto.policySearchCropYear;
		this.policySearchInsurancePlanId = dto.policySearchInsurancePlanId;
		this.policySearchOfficeId = dto.policySearchOfficeId;

		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.policySearchInsurancePlanName = dto.policySearchInsurancePlanName;
		this.policySearchOfficeName = dto.policySearchOfficeName;
	}
	

	@Override
	public boolean equalsBK(UserSettingDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	
	@Override
	public boolean equalsAll(UserSettingDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			result = result&&dtoUtils.equals("userSettingGuid", userSettingGuid, other.userSettingGuid);
			result = result&&dtoUtils.equals("loginUserGuid", loginUserGuid, other.loginUserGuid);
			result = result&&dtoUtils.equals("loginUserId", loginUserId, other.loginUserId);
			result = result&&dtoUtils.equals("loginUserType", loginUserType, other.loginUserType);
			result = result&&dtoUtils.equals("givenName", givenName, other.givenName);
			result = result&&dtoUtils.equals("familyName", familyName, other.familyName);
			result = result&&dtoUtils.equals("policySearchCropYear", policySearchCropYear, other.policySearchCropYear);
			result = result&&dtoUtils.equals("policySearchInsurancePlanId", policySearchInsurancePlanId, other.policySearchInsurancePlanId);
			result = result&&dtoUtils.equals("policySearchOfficeId", policySearchOfficeId, other.policySearchOfficeId);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public UserSettingDto copy() {
		return new UserSettingDto(this);
	}

	public String getUserSettingGuid() {
		return userSettingGuid;
	}
	public void setUserSettingGuid(String userSettingGuid) {
		this.userSettingGuid = userSettingGuid;
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
	
	public Integer getPolicySearchOfficeId() {
		return policySearchOfficeId;
	}
	public void setPolicySearchOfficeId(Integer policySearchOfficeId) {
		this.policySearchOfficeId = policySearchOfficeId;
	}
	
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getPolicySearchInsurancePlanName() {
		return policySearchInsurancePlanName;
	}
	public void setPolicySearchInsurancePlanName(String policySearchInsurancePlanName) {
		this.policySearchInsurancePlanName = policySearchInsurancePlanName;
	}
	
	public String getPolicySearchOfficeName() {
		return policySearchOfficeName;
	}
	public void setPolicySearchOfficeName(String policySearchOfficeName) {
		this.policySearchOfficeName = policySearchOfficeName;
	}
	
}
