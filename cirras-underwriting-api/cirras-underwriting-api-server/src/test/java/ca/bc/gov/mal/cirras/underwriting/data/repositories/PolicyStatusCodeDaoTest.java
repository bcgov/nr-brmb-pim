package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.data.entities.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyStatusCodeDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class PolicyStatusCodeDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String policyStatusCode = "TESTSTATUS";


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deletePolicyStatusCode();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deletePolicyStatusCode();
	}
	
	private void deletePolicyStatusCode() throws NotFoundDaoException, DaoException{
		
		PolicyStatusCodeDao dao = persistenceSpringConfig.policyStatusCodeDao();
		PolicyStatusCodeDto dto = dao.fetch(policyStatusCode);
		if (dto != null) {
			dao.delete(policyStatusCode);
		}
	}
	
	@Test 
	public void testInsertUpdateDeletePolicyStatusCode() throws Exception {

		PolicyStatusCodeDao dao = persistenceSpringConfig.policyStatusCodeDao();
		PolicyStatusCodeDto newDto = new PolicyStatusCodeDto();
		
		String description = "Test Status";
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

		
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);
		Date dataSyncTransDate = addSeconds(dateTime, -120);

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setPolicyStatusCode(policyStatusCode);
		newDto.setDescription(description);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		PolicyStatusCodeDto fetchedDto = dao.fetch(policyStatusCode);

		Assert.assertEquals("PolicyStatusCode", newDto.getPolicyStatusCode(), fetchedDto.getPolicyStatusCode());
		Assert.assertEquals("Description", newDto.getDescription(), fetchedDto.getDescription());
		Assert.assertTrue("EffectiveDate", newDto.getEffectiveDate().compareTo(fetchedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", newDto.getExpiryDate().compareTo(fetchedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate 1", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		description = "Test Status 2";
		effectiveDate = addDays(date, -2);
		expiryDate = addDays(date, 2);
		dataSyncTransDate = addSeconds(dateTime, -60);

		fetchedDto.setDescription(description);
		fetchedDto.setEffectiveDate(effectiveDate);
		fetchedDto.setExpiryDate(expiryDate);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		PolicyStatusCodeDto updatedDto = dao.fetch(policyStatusCode);
		
		Assert.assertEquals("Description", fetchedDto.getDescription(), updatedDto.getDescription());
		Assert.assertTrue("EffectiveDate", fetchedDto.getEffectiveDate().compareTo(updatedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", fetchedDto.getExpiryDate().compareTo(updatedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate 2", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		PolicyStatusCodeDto notUpdatedDto = dao.fetch(policyStatusCode);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(notUpdatedDto.getPolicyStatusCode());

		//FETCH
		PolicyStatusCodeDto deletedDto = dao.fetch(notUpdatedDto.getPolicyStatusCode());
		Assert.assertNull(deletedDto);

	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	private static Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

}
