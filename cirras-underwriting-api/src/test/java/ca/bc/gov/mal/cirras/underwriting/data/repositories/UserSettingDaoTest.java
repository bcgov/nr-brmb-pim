package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.data.entities.UserSettingDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class UserSettingDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String loginUserGuid = "login-user-guid-123";

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteUserSetting();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteUserSetting();
	}
	
	private void deleteUserSetting() throws NotFoundDaoException, DaoException{
		
		UserSettingDao dao = persistenceSpringConfig.userSettingDao();
		UserSettingDto dto = dao.getByLoginUserGuid(loginUserGuid);
		if (dto != null) {
			dao.delete(dto.getUserSettingGuid());
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteUserSetting() throws Exception {

		UserSettingDao dao = persistenceSpringConfig.userSettingDao();
		UserSettingDto newDto = new UserSettingDto();
		
		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setLoginUserGuid(loginUserGuid);
		newDto.setLoginUserId("user id");
		newDto.setLoginUserType("SCL");
		newDto.setGivenName("given name");
		newDto.setFamilyName("family name");
		newDto.setPolicySearchCropYear(2005);
		newDto.setPolicySearchInsurancePlanId(4);
		newDto.setPolicySearchOfficeId(1);

		dao.insert(newDto, userId);
		
		//FETCH
		UserSettingDto fetchedDto = dao.getByLoginUserGuid(loginUserGuid);
		
		String userSettingGuid = fetchedDto.getUserSettingGuid();
		
		Assert.assertNotNull(fetchedDto.getUserSettingGuid());
		Assert.assertEquals(newDto.getLoginUserGuid(), fetchedDto.getLoginUserGuid());
		Assert.assertEquals(newDto.getLoginUserId(), fetchedDto.getLoginUserId());
		Assert.assertEquals(newDto.getLoginUserType(), fetchedDto.getLoginUserType());
		Assert.assertEquals(newDto.getGivenName(), fetchedDto.getGivenName());
		Assert.assertEquals(newDto.getFamilyName(), fetchedDto.getFamilyName());
		Assert.assertEquals(newDto.getPolicySearchCropYear(), fetchedDto.getPolicySearchCropYear());
		Assert.assertEquals(newDto.getPolicySearchInsurancePlanId(), fetchedDto.getPolicySearchInsurancePlanId());
		Assert.assertEquals(newDto.getPolicySearchOfficeId(), fetchedDto.getPolicySearchOfficeId());

		//UPDATE
		fetchedDto.setLoginUserId("user id 2");
		fetchedDto.setLoginUserType("ABC");
		fetchedDto.setGivenName("given name 2");
		fetchedDto.setFamilyName("family name 2");
		fetchedDto.setPolicySearchCropYear(2006);
		fetchedDto.setPolicySearchInsurancePlanId(5);
		fetchedDto.setPolicySearchOfficeId(2);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		UserSettingDto updatedDto = dao.fetch(userSettingGuid);
		
		Assert.assertEquals(fetchedDto.getUserSettingGuid(), updatedDto.getUserSettingGuid());
		Assert.assertEquals(fetchedDto.getLoginUserGuid(), updatedDto.getLoginUserGuid());
		Assert.assertEquals(fetchedDto.getLoginUserId(), updatedDto.getLoginUserId());
		Assert.assertEquals(fetchedDto.getLoginUserType(), updatedDto.getLoginUserType());
		Assert.assertEquals(fetchedDto.getGivenName(), updatedDto.getGivenName());
		Assert.assertEquals(fetchedDto.getFamilyName(), updatedDto.getFamilyName());
		Assert.assertEquals(fetchedDto.getPolicySearchCropYear(), updatedDto.getPolicySearchCropYear());
		Assert.assertEquals(fetchedDto.getPolicySearchInsurancePlanId(), updatedDto.getPolicySearchInsurancePlanId());
		Assert.assertEquals(fetchedDto.getPolicySearchOfficeId(), updatedDto.getPolicySearchOfficeId());

		
		//UPDATE - Set all nullable values to null
		updatedDto.setGivenName(null);
		updatedDto.setFamilyName(null);
		updatedDto.setPolicySearchCropYear(null);
		updatedDto.setPolicySearchInsurancePlanId(null);
		updatedDto.setPolicySearchOfficeId(null);
		
		dao.update(updatedDto, userId);
		
		//FETCH
		UserSettingDto dto = dao.fetch(userSettingGuid);
		
		Assert.assertNull(dto.getGivenName());
		Assert.assertNull(dto.getFamilyName());
		Assert.assertNull(dto.getPolicySearchCropYear());
		Assert.assertNull(dto.getPolicySearchInsurancePlanId());
		Assert.assertNull(dto.getPolicySearchOfficeId());
		
		//DELETE
		dao.delete(userSettingGuid);

		//FETCH
		UserSettingDto deletedDto = dao.fetch(userSettingGuid);
		Assert.assertNull(deletedDto);

	}

}
