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

import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ContactDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer contactId = 999999999;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteContact();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteContact();
	}
	
	private void deleteContact() throws NotFoundDaoException, DaoException{
		
		ContactDao dao = persistenceSpringConfig.contactDao();
		ContactDto dto = dao.fetch(contactId);
		if (dto != null) {
			dao.delete(contactId);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteContact() throws Exception {

		ContactDao dao = persistenceSpringConfig.contactDao();
		ContactDto newDto = new ContactDto();
		
		String firstName = "Test Firstname";
		String lastName = "Test Lastname";
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		Date dataSyncTransDate = addSeconds(dateTime, -120);

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setContactId(contactId);
		newDto.setFirstName(firstName);
		newDto.setLastName(lastName);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		ContactDto fetchedDto = dao.fetch(contactId);

		Assert.assertEquals("ContactId", newDto.getContactId(), fetchedDto.getContactId());
		Assert.assertEquals("FirstName", newDto.getFirstName(), fetchedDto.getFirstName());
		Assert.assertEquals("LastName", newDto.getLastName(), fetchedDto.getLastName());
		Assert.assertTrue("DataSyncTransDate 1", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		firstName = "Test Firstname 2";
		lastName = "Test Lastname 2";
		
		dataSyncTransDate = addSeconds(dateTime, -60);
		fetchedDto.setFirstName(firstName);
		fetchedDto.setLastName(lastName);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ContactDto updatedDto = dao.fetch(contactId);

		Assert.assertEquals("FirstName 2", fetchedDto.getFirstName(), updatedDto.getFirstName());
		Assert.assertEquals("LastName 2", fetchedDto.getLastName(), updatedDto.getLastName());
		Assert.assertTrue("DataSyncTransDate 2", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		ContactDto notUpdatedDto = dao.fetch(contactId);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(notUpdatedDto.getContactId());

		//FETCH
		ContactDto deletedDto = dao.fetch(notUpdatedDto.getContactId());
		Assert.assertNull(deletedDto);

	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

}
