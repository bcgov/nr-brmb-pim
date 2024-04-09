package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContactDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContactEmailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ContactEmailDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer contactEmailId = 999999999;
	private Integer contactId = 999999998;
	private Integer contactId2 = 999999997;

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteContact();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteContact();
	}
	
	private void deleteContact() throws NotFoundDaoException, DaoException{
		
		ContactEmailDao daoGc = persistenceSpringConfig.contactEmailDao();
		ContactEmailDto dtoGc = daoGc.fetch(contactEmailId);
		if (dtoGc != null) {
			daoGc.delete(contactEmailId);
		}

		ContactDao daoC = persistenceSpringConfig.contactDao();
		ContactDto dtoC = daoC.fetch(contactId);
		if (dtoC != null) {
			daoC.delete(contactId);
		}

		dtoC = daoC.fetch(contactId2);
		if (dtoC != null) {
			daoC.delete(contactId2);
		}

	}
	
	@Test 
	public void testInsertUpdateDeleteContactEmail() throws Exception {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

		Date dataSyncTransDate = addSeconds(dateTime, -120);
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);
		
		createContact(dataSyncTransDate, contactId);
		createContact(dataSyncTransDate, contactId2);

		
		ContactEmailDao dao = persistenceSpringConfig.contactEmailDao();
		ContactEmailDto newDto = new ContactEmailDto();
		
		String emailAddress = "test1@test.com";
		Boolean isPrimaryEmailInd = true;


		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setContactEmailId(contactEmailId);
		newDto.setContactId(contactId);
		newDto.setEmailAddress(emailAddress);
		newDto.setIsPrimaryEmailInd(isPrimaryEmailInd);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		ContactEmailDto fetchedDto = dao.fetch(contactEmailId);

		Assert.assertEquals("ContactEmailId", newDto.getContactEmailId(), fetchedDto.getContactEmailId());
		Assert.assertEquals("ContactId", newDto.getContactId(), fetchedDto.getContactId());
		Assert.assertEquals("EmailAddress", newDto.getEmailAddress(), fetchedDto.getEmailAddress());
		Assert.assertEquals("IsPrimaryEmailInd", newDto.getIsPrimaryEmailInd(), fetchedDto.getIsPrimaryEmailInd());
		Assert.assertTrue("EffectiveDate", newDto.getEffectiveDate().compareTo(fetchedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", newDto.getExpiryDate().compareTo(fetchedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate 1", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		emailAddress = "test22@test.com";
		isPrimaryEmailInd = false;
		effectiveDate = addDays(date, -2);
		expiryDate = addDays(date, 2);
		
		dataSyncTransDate = addSeconds(dateTime, -60);
		
		fetchedDto.setContactId(contactId2);
		fetchedDto.setEmailAddress(emailAddress);
		fetchedDto.setIsPrimaryEmailInd(isPrimaryEmailInd);
		fetchedDto.setEffectiveDate(effectiveDate);
		fetchedDto.setExpiryDate(expiryDate);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ContactEmailDto updatedDto = dao.fetch(contactEmailId);

		Assert.assertEquals("ContactId 2", fetchedDto.getContactId(), updatedDto.getContactId());
		Assert.assertEquals("EmailAddress 2", fetchedDto.getEmailAddress(), updatedDto.getEmailAddress());
		Assert.assertEquals("IsPrimaryEmailInd 2", fetchedDto.getIsPrimaryEmailInd(), updatedDto.getIsPrimaryEmailInd());
		Assert.assertTrue("EffectiveDate 2", fetchedDto.getEffectiveDate().compareTo(updatedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate 2", fetchedDto.getExpiryDate().compareTo(updatedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate 2", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		ContactEmailDto notUpdatedDto = dao.fetch(contactEmailId);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(notUpdatedDto.getContactEmailId());

		//FETCH
		ContactEmailDto deletedDto = dao.fetch(notUpdatedDto.getContactEmailId());
		Assert.assertNull(deletedDto);

	}
	
	private void createContact(Date dataSyncTransDate, Integer id) throws DaoException {
		ContactDao dao = persistenceSpringConfig.contactDao();
		ContactDto newDto = new ContactDto();
		
		String firstName = "Test Firstname";
		String lastName = "Test Lastname";
		

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setContactId(id);
		newDto.setFirstName(firstName);
		newDto.setLastName(lastName);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
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
