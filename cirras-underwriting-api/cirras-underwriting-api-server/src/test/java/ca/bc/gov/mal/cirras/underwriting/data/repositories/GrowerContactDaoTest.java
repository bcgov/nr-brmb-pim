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
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContactDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class GrowerContactDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer growerContactId = 999999999;
	private Integer contactId = 999999998;
	private Integer growerId = 999999997;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteContact();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteContact();
	}
	
	private void deleteContact() throws NotFoundDaoException, DaoException{
		
		GrowerContactDao daoGc = persistenceSpringConfig.growerContactDao();
		GrowerContactDto dtoGc = daoGc.fetch(growerContactId);
		if (dtoGc != null) {
			daoGc.delete(growerContactId);
		}

		ContactDao daoC = persistenceSpringConfig.contactDao();
		ContactDto dtoC = daoC.fetch(contactId);
		if (dtoC != null) {
			daoC.delete(contactId);
		}

		GrowerDao daoG = persistenceSpringConfig.growerDao();
		GrowerDto dtoG = daoG.fetch(growerId);
		if (dtoG != null) {
			daoG.delete(growerId);
		}

	}
	
	@Test 
	public void testInsertUpdateDeleteGrowerContact() throws Exception {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		Date dataSyncTransDate = addSeconds(dateTime, -120);


		createGrower(dataSyncTransDate);
		createContact(dataSyncTransDate);
		
		
		GrowerContactDao dao = persistenceSpringConfig.growerContactDao();
		GrowerContactDto newDto = new GrowerContactDto();
		
		Boolean isPrimaryContactInd = true;
		Boolean isActivelyInvolvedInd = false;

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setGrowerContactId(growerContactId);
		newDto.setGrowerId(growerId);
		newDto.setContactId(contactId);
		newDto.setIsPrimaryContactInd(isPrimaryContactInd);
		newDto.setIsActivelyInvolvedInd(isActivelyInvolvedInd);

		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		GrowerContactDto fetchedDto = dao.fetch(growerContactId);

		Assert.assertEquals("GrowerContactId", newDto.getGrowerContactId(), fetchedDto.getGrowerContactId());
		Assert.assertEquals("GrowerId", newDto.getGrowerId(), fetchedDto.getGrowerId());
		Assert.assertEquals("ContactId", newDto.getContactId(), fetchedDto.getContactId());
		Assert.assertEquals("IsPrimaryContactInd", newDto.getIsPrimaryContactInd(), fetchedDto.getIsPrimaryContactInd());
		Assert.assertEquals("IsActivelyInvolvedInd", newDto.getIsActivelyInvolvedInd(), fetchedDto.getIsActivelyInvolvedInd());
		Assert.assertTrue("DataSyncTransDate 1", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		isPrimaryContactInd = false;
		isActivelyInvolvedInd = true;
		
		dataSyncTransDate = addSeconds(dateTime, -60);
		fetchedDto.setIsPrimaryContactInd(isPrimaryContactInd);
		fetchedDto.setIsActivelyInvolvedInd(isActivelyInvolvedInd);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		GrowerContactDto updatedDto = dao.fetch(growerContactId);

		Assert.assertEquals("IsPrimaryContactInd 2", fetchedDto.getIsPrimaryContactInd(), updatedDto.getIsPrimaryContactInd());
		Assert.assertEquals("IsActivelyInvolvedInd 2", fetchedDto.getIsActivelyInvolvedInd(), updatedDto.getIsActivelyInvolvedInd());
		Assert.assertTrue("DataSyncTransDate 2", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		GrowerContactDto notUpdatedDto = dao.fetch(growerContactId);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(notUpdatedDto.getGrowerContactId());

		//FETCH
		GrowerContactDto deletedDto = dao.fetch(notUpdatedDto.getGrowerContactId());
		Assert.assertNull(deletedDto);

	}
	
	private void createGrower(Date dataSyncTransDate) throws DaoException {
		GrowerDao dao = persistenceSpringConfig.growerDao();
		GrowerDto newDto = new GrowerDto();
		
		Integer growerNumber = 123456;
		String growerName = "grower test name";
		String growerAddressLine1 = "address line 1";
		String growerAddressLine2 = "address line 2";
		String growerPostalCode = "V8P 4N8";
		String growerCity = "Victoria";
		Integer cityId = 1;
		String growerProvince = "BC";
		
		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setGrowerId(growerId);
		newDto.setGrowerNumber(growerNumber);
		newDto.setGrowerName(growerName);
		newDto.setGrowerAddressLine1(growerAddressLine1);
		newDto.setGrowerAddressLine2(growerAddressLine2);
		newDto.setGrowerPostalCode(growerPostalCode);
		newDto.setGrowerCity(growerCity);
		newDto.setCityId(cityId);
		newDto.setGrowerProvince(growerProvince);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
	}
	
	private void createContact(Date dataSyncTransDate) throws DaoException {
		ContactDao dao = persistenceSpringConfig.contactDao();
		ContactDto newDto = new ContactDto();
		
		String firstName = "Test Firstname";
		String lastName = "Test Lastname";
		

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setContactId(contactId);
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

}
