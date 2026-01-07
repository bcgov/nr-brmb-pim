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
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;


public class ContactEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(ContactEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_CONTACT
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer contactId = 999999999;
	
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

		
		service.deleteContact(topLevelEndpoints, contactId.toString());
		
	}
	
	@Test
	public void testCreateUpdateDeleteContact() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateContact");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		String firstName = "Test Firstname";
		String lastName = "Test Lastname";

		//CREATE Contact
		ContactRsrc resource = new ContactRsrc();
		
		resource.setContactId(contactId);
		resource.setFirstName(firstName);
		resource.setLastName(lastName);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.ContactsCreated);

		service.synchronizeContact(resource);
		
		ContactRsrc fetchedResource = service.getContact(topLevelEndpoints, contactId.toString()); 

		Assert.assertEquals("ContactId 1", resource.getContactId(), fetchedResource.getContactId());
		Assert.assertEquals("FirstName", resource.getFirstName(), fetchedResource.getFirstName());
		Assert.assertEquals("LastName", resource.getLastName(), fetchedResource.getLastName());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//UPDATE CODE
		firstName = "Test Firstname 2";
		lastName = "Test Lastname 2";

		fetchedResource.setFirstName(firstName);
		fetchedResource.setLastName(lastName);

		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.ContactsUpdated);

		service.synchronizeContact(fetchedResource);
		
		ContactRsrc updatedResource = service.getContact(topLevelEndpoints, contactId.toString()); 

		Assert.assertEquals("ContactId 2", fetchedResource.getContactId(), updatedResource.getContactId());
		Assert.assertEquals("FirstName 2", fetchedResource.getFirstName(), updatedResource.getFirstName());
		Assert.assertEquals("LastName 2", fetchedResource.getLastName(), updatedResource.getLastName());
		Assert.assertTrue("DataSyncTransDate 2", fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);
		
		//CLEAN UP: DELETE CODE
		delete();
		
		logger.debug(">testCreateUpdateContact");
	}

	
	@Test
	public void testUpdateContactWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateContactWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		String firstName = "Test Firstname";
		String lastName = "Test Lastname";

		//CREATE Contact
		ContactRsrc resource = new ContactRsrc();
		
		resource.setContactId(contactId);
		resource.setFirstName(firstName);
		resource.setLastName(lastName);
		resource.setDataSyncTransDate(createTransactionDate);
		
		
		//TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXPECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.ContactsDeleted);
		service.synchronizeContact(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesSyncEventTypes.ContactsUpdated);

		service.synchronizeContact(resource);

		ContactRsrc fetchedResource = service.getContact(topLevelEndpoints, contactId.toString()); 

		Assert.assertEquals("ContactId 1", resource.getContactId(), fetchedResource.getContactId());
		Assert.assertEquals("FirstName", resource.getFirstName(), fetchedResource.getFirstName());
		Assert.assertEquals("LastName", resource.getLastName(), fetchedResource.getLastName());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		fetchedResource.setDataSyncTransDate(addSeconds(createTransactionDate, -1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.ContactsUpdated);
		service.synchronizeContact(fetchedResource);
		
		ContactRsrc notUpdatedResource = service.getContact(topLevelEndpoints, contactId.toString()); 

		Assert.assertTrue("DataSyncTransDate 2", resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);

		
		//UPDATE Contact
		firstName = "Test Firstname 2";
		lastName = "Test Lastname 2";

		notUpdatedResource.setFirstName(firstName);
		notUpdatedResource.setLastName(lastName);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		notUpdatedResource.setTransactionType(PoliciesSyncEventTypes.ContactsCreated);
		notUpdatedResource.setDataSyncTransDate(addSeconds(createTransactionDate, 10));
		service.synchronizeContact(notUpdatedResource);
		
		ContactRsrc updatedResource = service.getContact(topLevelEndpoints, contactId.toString()); 

		Assert.assertEquals("ContactId 2", notUpdatedResource.getContactId(), updatedResource.getContactId());
		Assert.assertEquals("FirstName 2", notUpdatedResource.getFirstName(), updatedResource.getFirstName());
		Assert.assertEquals("LastName 2", notUpdatedResource.getLastName(), updatedResource.getLastName());
		Assert.assertTrue("DataSyncTransDate 2", notUpdatedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		//CLEAN UP: DELETE CODE
		delete();		
		
		logger.debug(">testUpdateContactWithoutRecordNoUpdate");
	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
