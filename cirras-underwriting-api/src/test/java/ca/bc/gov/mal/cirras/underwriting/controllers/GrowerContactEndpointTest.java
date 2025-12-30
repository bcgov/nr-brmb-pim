package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.clients.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;


public class GrowerContactEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(GrowerContactEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_GROWER_CONTACT
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer growerContactId = 999999999;
	private Integer contactId = 999999998;
	private Integer growerId = 999999997;
	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		delete();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		service.deleteGrowerContact(topLevelEndpoints, growerContactId.toString());
		service.deleteGrower(topLevelEndpoints, growerId.toString());
		service.deleteContact(topLevelEndpoints, contactId.toString());
		
	}
	
	@Test
	public void testCreateUpdateDeleteGrowerContact() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateGrowerContact");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		createGrower(createTransactionDate);
		createContact(createTransactionDate);

		Boolean isPrimaryContactInd = true;
		Boolean isActivelyInvolvedInd = false;

		//CREATE GrowerContact
		GrowerContactRsrc resource = new GrowerContactRsrc();
		
		resource.setGrowerContactId(growerContactId);
		resource.setGrowerId(growerId);
		resource.setContactId(contactId);
		resource.setIsPrimaryContactInd(isPrimaryContactInd);
		resource.setIsActivelyInvolvedInd(isActivelyInvolvedInd);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.InsuredGrowerContactsCreated);

		service.synchronizeGrowerContact(resource);
		
		//DEBUG ON MONDAY: This gives a NULLPOINTER ERROR
		GrowerContactRsrc fetchedResource = service.getGrowerContact(topLevelEndpoints, growerContactId.toString()); 

		Assert.assertEquals("GrowerContactId 1", resource.getGrowerContactId(), fetchedResource.getGrowerContactId());
		Assert.assertEquals("GrowerId", resource.getGrowerId(), fetchedResource.getGrowerId());
		Assert.assertEquals("ContactId", resource.getContactId(), fetchedResource.getContactId());
		Assert.assertEquals("IsPrimaryContactInd", resource.getIsPrimaryContactInd(), fetchedResource.getIsPrimaryContactInd());
		Assert.assertEquals("IsActivelyInvolvedInd", resource.getIsActivelyInvolvedInd(), fetchedResource.getIsActivelyInvolvedInd());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//UPDATE CODE
		isPrimaryContactInd = false;
		isActivelyInvolvedInd = true;

		fetchedResource.setIsPrimaryContactInd(isPrimaryContactInd);
		fetchedResource.setIsActivelyInvolvedInd(isActivelyInvolvedInd);

		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.InsuredGrowerContactsUpdated);

		service.synchronizeGrowerContact(fetchedResource);
		
		GrowerContactRsrc updatedResource = service.getGrowerContact(topLevelEndpoints, growerContactId.toString()); 

		Assert.assertEquals("IsPrimaryContactInd 2", fetchedResource.getIsPrimaryContactInd(), updatedResource.getIsPrimaryContactInd());
		Assert.assertEquals("IsActivelyInvolvedInd 2", fetchedResource.getIsActivelyInvolvedInd(), updatedResource.getIsActivelyInvolvedInd());
		Assert.assertTrue("DataSyncTransDate 2", fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);
		
		//CLEAN UP: DELETE CODE
		delete();
		
		logger.debug(">testCreateUpdateGrowerContact");
	}

	
	@Test
	public void testUpdateGrowerContactWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateGrowerContactWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		createGrower(createTransactionDate);
		createContact(createTransactionDate);

		Boolean isPrimaryContactInd = true;
		Boolean isActivelyInvolvedInd = false;

		//CREATE GrowerContact
		GrowerContactRsrc resource = new GrowerContactRsrc();
		
		resource.setGrowerContactId(growerContactId);
		resource.setGrowerId(growerId);
		resource.setContactId(contactId);
		resource.setIsPrimaryContactInd(isPrimaryContactInd);
		resource.setIsActivelyInvolvedInd(isActivelyInvolvedInd);
		resource.setDataSyncTransDate(createTransactionDate);
		
		
		//TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXPECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.InsuredGrowerContactsDeleted);
		service.synchronizeGrowerContact(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesSyncEventTypes.InsuredGrowerContactsUpdated);

		service.synchronizeGrowerContact(resource);

		GrowerContactRsrc fetchedResource = service.getGrowerContact(topLevelEndpoints, growerContactId.toString()); 

		Assert.assertEquals("GrowerContactId 1", resource.getGrowerContactId(), fetchedResource.getGrowerContactId());
		Assert.assertEquals("GrowerId", resource.getGrowerId(), fetchedResource.getGrowerId());
		Assert.assertEquals("ContactId", resource.getContactId(), fetchedResource.getContactId());
		Assert.assertEquals("IsPrimaryContactInd", resource.getIsPrimaryContactInd(), fetchedResource.getIsPrimaryContactInd());
		Assert.assertEquals("IsActivelyInvolvedInd", resource.getIsActivelyInvolvedInd(), fetchedResource.getIsActivelyInvolvedInd());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		fetchedResource.setDataSyncTransDate(addSeconds(createTransactionDate, -1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.InsuredGrowerContactsUpdated);
		service.synchronizeGrowerContact(fetchedResource);
		
		GrowerContactRsrc notUpdatedResource = service.getGrowerContact(topLevelEndpoints, growerContactId.toString()); 

		Assert.assertTrue("DataSyncTransDate 2", resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);

		
		//UPDATE GrowerContact
		isPrimaryContactInd = false;
		isActivelyInvolvedInd = true;

		notUpdatedResource.setIsPrimaryContactInd(isPrimaryContactInd);
		notUpdatedResource.setIsActivelyInvolvedInd(isActivelyInvolvedInd);
		
		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		notUpdatedResource.setTransactionType(PoliciesSyncEventTypes.InsuredGrowerContactsCreated);
		notUpdatedResource.setDataSyncTransDate(addSeconds(createTransactionDate, 10));
		service.synchronizeGrowerContact(notUpdatedResource);
		
		GrowerContactRsrc updatedResource = service.getGrowerContact(topLevelEndpoints, growerContactId.toString()); 

		Assert.assertEquals("IsPrimaryContactInd 2", notUpdatedResource.getIsPrimaryContactInd(), updatedResource.getIsPrimaryContactInd());
		Assert.assertEquals("IsActivelyInvolvedInd 2", notUpdatedResource.getIsActivelyInvolvedInd(), updatedResource.getIsActivelyInvolvedInd());
		Assert.assertTrue("DataSyncTransDate 2", notUpdatedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		//CLEAN UP: DELETE CODE
		delete();		
		
		logger.debug(">testUpdateGrowerContactWithoutRecordNoUpdate");
	}

	private void createGrower(Date createTransactionDate) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		GrowerRsrc resource = new GrowerRsrc();
		
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
		resource.setGrowerId(growerId);
		resource.setGrowerNumber(growerNumber);
		resource.setGrowerName(growerName);
		resource.setGrowerAddressLine1(growerAddressLine1);
		resource.setGrowerAddressLine2(growerAddressLine2);
		resource.setGrowerPostalCode(growerPostalCode);
		resource.setGrowerCity(growerCity);
		resource.setCityId(cityId);
		resource.setGrowerProvince(growerProvince);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerCreated);

		service.synchronizeGrower(resource);
	}
	
	private void createContact(Date createTransactionDate) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		ContactRsrc resource = new ContactRsrc();
		
		String firstName = "Test Firstname";
		String lastName = "Test Lastname";
		

		String userId = "JUNIT_TEST";

		//INSERT
		resource.setContactId(contactId);
		resource.setFirstName(firstName);
		resource.setLastName(lastName);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.ContactsCreated);

		service.synchronizeContact(resource);

	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
