package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactEmailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UserSettingRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class UserSettingEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(UserSettingEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_USER_SETTING,
		Scopes.GET_USER_SETTING,
		Scopes.CREATE_USER_SETTING,
		Scopes.UPDATE_USER_SETTING,
		Scopes.DELETE_USER_SETTING
	};

	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	private CirrasUnderwritingService serviceUser2;
	private EndpointsRsrc topLevelEndpointsUser2;

	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException, ValidationException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		serviceUser2 = getServiceSecondUser(SCOPES);
		topLevelEndpointsUser2 = serviceUser2.getTopLevelEndpoints();

		delete();
	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		delete();
	}

	
	private void delete() throws CirrasUnderwritingServiceException {

		//delete user setting
		UserSettingRsrc user = service.searchUserSetting(topLevelEndpoints);
		if (user != null && user.getUserSettingGuid() != null) {
			service.deleteUserSetting(user);
		}

		//delete user setting
		user = serviceUser2.searchUserSetting(topLevelEndpointsUser2);
		if (user != null && user.getUserSettingGuid() != null) {
			serviceUser2.deleteUserSetting(user);
		}
	}
	
	@Test
	public void testUserSetting() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testUserSetting");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String loginUserGuid = "9374JD83HD94JSLE893H3N58DJE74999";
		String loginUserId = "SCL\\TEST_SERVICE_CLIENT";
		String loginUserType = "SCL";

		
		//Search user
		UserSettingRsrc user = service.searchUserSetting(topLevelEndpoints);
		
		Assert.assertNotNull(user);
		
		try {
			//Expect error: Users are only allowed to create their own user settings
			UserSettingRsrc user2 = serviceUser2.createUserSetting(topLevelEndpointsUser2, user);
			Assert.fail("Should have failed: Users are only allowed to create their own user settings");
		
		} catch (CirrasUnderwritingServiceException e) {
			// Expected
		}
		
		user.setPolicySearchCropYear(2020);
		user.setPolicySearchInsurancePlanId(4);
		user.setPolicySearchInsurancePlanName("GRAIN");
		user.setPolicySearchOfficeId(1);
		user.setPolicySearchOfficeName("Abbotsford");
		user.setGivenName(null);
		user.setFamilyName(null);
		
		UserSettingRsrc createdUser = service.createUserSetting(topLevelEndpoints, user);
		
		Assert.assertNotNull(createdUser);
		
		Assert.assertNotNull("UserSettingGuid", createdUser.getUserSettingGuid());
		Assert.assertEquals("LoginUserGuid", loginUserGuid, createdUser.getLoginUserGuid());
		Assert.assertEquals("LoginUserId", loginUserId, createdUser.getLoginUserId());
		Assert.assertEquals("LoginUserType", loginUserType, createdUser.getLoginUserType());
		Assert.assertEquals("GivenName", user.getGivenName(), createdUser.getGivenName());
		Assert.assertEquals("FamilyName", user.getFamilyName(), createdUser.getFamilyName());
		Assert.assertEquals("PolicySearchCropYear", user.getPolicySearchCropYear(), createdUser.getPolicySearchCropYear());
		Assert.assertEquals("PolicySearchInsurancePlanId", user.getPolicySearchInsurancePlanId(), createdUser.getPolicySearchInsurancePlanId());
		Assert.assertEquals("PolicySearchOfficeId", user.getPolicySearchOfficeId(), createdUser.getPolicySearchOfficeId());

		try {
			//Expect error: Users are only allowed to update their own user settings
			UserSettingRsrc user2 = serviceUser2.updateUserSetting(createdUser);
			Assert.fail("Should have failed: Users are only allowed to update their own user settings");
		} catch (CirrasUnderwritingServiceException e) {
			// Expected
		}
		
		createdUser.setPolicySearchCropYear(2021);
		createdUser.setPolicySearchInsurancePlanId(5);
		createdUser.setPolicySearchInsurancePlanName("FORAGE");
		createdUser.setPolicySearchOfficeId(6);
		createdUser.setPolicySearchOfficeName("Oliver");
		
		UserSettingRsrc updatedUser = service.updateUserSetting(createdUser);
		
		Assert.assertNotNull(updatedUser);
		
		Assert.assertEquals("UserSettingGuid", createdUser.getUserSettingGuid(), updatedUser.getUserSettingGuid());
		Assert.assertEquals("LoginUserGuid", createdUser.getLoginUserGuid(), updatedUser.getLoginUserGuid());
		Assert.assertEquals("LoginUserId", createdUser.getLoginUserId(), updatedUser.getLoginUserId());
		Assert.assertEquals("LoginUserType", createdUser.getLoginUserType(), updatedUser.getLoginUserType());
		Assert.assertEquals("GivenName", createdUser.getGivenName(), updatedUser.getGivenName());
		Assert.assertEquals("FamilyName", createdUser.getFamilyName(), updatedUser.getFamilyName());
		Assert.assertEquals("PolicySearchCropYear", createdUser.getPolicySearchCropYear(), updatedUser.getPolicySearchCropYear());
		Assert.assertEquals("PolicySearchInsurancePlanId", createdUser.getPolicySearchInsurancePlanId(), updatedUser.getPolicySearchInsurancePlanId());
		Assert.assertEquals("PolicySearchOfficeId", createdUser.getPolicySearchOfficeId(), updatedUser.getPolicySearchOfficeId());

		try {
			//Expect error: Users are only allowed to delete their own user settings
			serviceUser2.deleteUserSetting(updatedUser);
			Assert.fail("Should have failed: Users are only allowed to delete their own user settings");
		} catch (CirrasUnderwritingServiceException e) {
			// Expected
		}		
		
		
		service.deleteUserSetting(updatedUser);
		
		//search still returns a user but it's the default user without primary key
		user = service.searchUserSetting(topLevelEndpoints);
		Assert.assertNotNull(user);
		Assert.assertNull("UserSettingGuid", user.getUserSettingGuid());

	}


	
}
