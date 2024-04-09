package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.Calendar;
import java.util.Date;

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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;


public class ContactEmailEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(ContactEmailEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_CONTACT_EMAIL
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer contactEmailId = 999999999;
	private Integer contactId = 999999998;
	private Integer contactId2 = 999999997;
	
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

		service.deleteContactEmail(topLevelEndpoints, contactEmailId.toString());
		service.deleteContact(topLevelEndpoints, contactId.toString());
		service.deleteContact(topLevelEndpoints, contactId2.toString());
		
	}
	
	@Test
	public void testCreateUpdateDeleteContactEmail() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateContactEmail");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		createContact(createTransactionDate, contactId);
		createContact(createTransactionDate, contactId2);

		String emailAddress = "test1@test.com";
		Boolean isPrimaryEmailInd = true;
		Boolean isActive = false;

		//CREATE ContactEmail
		ContactEmailRsrc resource = new ContactEmailRsrc();

		resource.setContactEmailId(contactEmailId);
		resource.setContactId(contactId);
		resource.setEmailAddress(emailAddress);
		resource.setIsPrimaryEmailInd(isPrimaryEmailInd);
		resource.setIsActive(isActive);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.EmailsCreated);

		service.synchronizeContactEmail(resource);
		
		ContactEmailRsrc fetchedResource = service.getContactEmail(topLevelEndpoints, contactEmailId.toString()); 

		Assert.assertEquals("contactEmailId 1", resource.getContactEmailId(), fetchedResource.getContactEmailId());
		Assert.assertEquals("ContactId", resource.getContactId(), fetchedResource.getContactId());
		Assert.assertEquals("EmailAddress", resource.getEmailAddress(), fetchedResource.getEmailAddress());
		Assert.assertEquals("IsPrimaryEmailInd", resource.getIsPrimaryEmailInd(), fetchedResource.getIsPrimaryEmailInd());
		//Inactive records mean that the expiry date is set to the current date
		Assert.assertTrue("EffectiveDate ExpiryDate 1", fetchedResource.getEffectiveDate().compareTo(fetchedResource.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//UPDATE CODE
		emailAddress = "test22@test.com";
		isPrimaryEmailInd = false;
		isActive = true;

		fetchedResource.setContactId(contactId2);
		fetchedResource.setEmailAddress(emailAddress);
		fetchedResource.setIsPrimaryEmailInd(isPrimaryEmailInd);
		fetchedResource.setIsActive(isActive);

		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.EmailsUpdated);

		service.synchronizeContactEmail(fetchedResource);
		
		ContactEmailRsrc updatedResource = service.getContactEmail(topLevelEndpoints, contactEmailId.toString()); 

		Assert.assertEquals("ContactId 2", fetchedResource.getContactId(), updatedResource.getContactId());
		Assert.assertEquals("EmailAddress 2", fetchedResource.getEmailAddress(), updatedResource.getEmailAddress());
		Assert.assertEquals("IsPrimaryEmailInd 2", fetchedResource.getIsPrimaryEmailInd(), updatedResource.getIsPrimaryEmailInd());
		//Active records mean that the expiry date is set to a date in the future
		Assert.assertTrue("EffectiveDate ExpiryDate 2", updatedResource.getEffectiveDate().compareTo(updatedResource.getExpiryDate()) < 0);
		Assert.assertTrue("DataSyncTransDate 2", fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);
		
		//Delete e-mail using sync
		//In the claims listener, only the primary key and the transaction type are set
		ContactEmailRsrc toDeleteResource = new ContactEmailRsrc();
		toDeleteResource.setContactEmailId(contactEmailId);
		toDeleteResource.setTransactionType(PoliciesSyncEventTypes.EmailsDeleted);
		service.synchronizeContactEmail(toDeleteResource);
		
		try {
			//getContactEmail fails if there is no record
			ContactEmailRsrc fetchedDeletedResource = service.getContactEmail(topLevelEndpoints, contactEmailId.toString());
			Assert.fail("Getting Contact Email should have failed");
		
		} catch (CirrasUnderwritingServiceException e) {
			// Expected
		}
		
		//CLEAN UP: DELETE CODE
		delete();
		
		logger.debug(">testCreateUpdateContactEmail");
	}

	
	@Test
	public void testUpdateContactEmailWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateContactEmailWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		createContact(createTransactionDate, contactId);
		createContact(createTransactionDate, contactId2);

		String emailAddress = "test1@test.com";
		Boolean isPrimaryEmailInd = false;
		Boolean isActive = true;

		//CREATE ContactEmail
		ContactEmailRsrc resource = new ContactEmailRsrc();

		resource.setContactEmailId(contactEmailId);
		resource.setContactId(contactId);
		resource.setEmailAddress(emailAddress);
		resource.setIsPrimaryEmailInd(isPrimaryEmailInd);
		resource.setIsActive(isActive);

		resource.setDataSyncTransDate(createTransactionDate);
		
		
		//TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXPECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.EmailsDeleted);
		service.synchronizeContactEmail(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesSyncEventTypes.EmailsUpdated);

		service.synchronizeContactEmail(resource);

		ContactEmailRsrc fetchedResource = service.getContactEmail(topLevelEndpoints, contactEmailId.toString()); 

		Assert.assertEquals("ContactEmailId 1", resource.getContactEmailId(), fetchedResource.getContactEmailId());
		Assert.assertEquals("ContactId", resource.getContactId(), fetchedResource.getContactId());
		Assert.assertEquals("EmailAddress", resource.getEmailAddress(), fetchedResource.getEmailAddress());
		Assert.assertEquals("IsPrimaryEmailInd", resource.getIsPrimaryEmailInd(), fetchedResource.getIsPrimaryEmailInd());
		//Active records mean that the expiry date is set to a date in the future
		Assert.assertTrue("EffectiveDate ExpiryDate 1", fetchedResource.getEffectiveDate().compareTo(fetchedResource.getExpiryDate()) < 0);
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		fetchedResource.setIsActive(isActive);
		fetchedResource.setDataSyncTransDate(addSeconds(createTransactionDate, -1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.EmailsUpdated);
		service.synchronizeContactEmail(fetchedResource);
		
		ContactEmailRsrc notUpdatedResource = service.getContactEmail(topLevelEndpoints, contactEmailId.toString()); 

		Assert.assertTrue("DataSyncTransDate 2", resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);

		
		//UPDATE ContactEmail
		emailAddress = "test22@test.com";
		isPrimaryEmailInd = true;
		isActive = false;

		notUpdatedResource.setContactId(contactId2);
		notUpdatedResource.setEmailAddress(emailAddress);
		notUpdatedResource.setIsPrimaryEmailInd(isPrimaryEmailInd);
		notUpdatedResource.setIsActive(isActive);
		
		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		notUpdatedResource.setTransactionType(PoliciesSyncEventTypes.EmailsCreated);
		notUpdatedResource.setDataSyncTransDate(addSeconds(createTransactionDate, 10));
		service.synchronizeContactEmail(notUpdatedResource);
		
		ContactEmailRsrc updatedResource = service.getContactEmail(topLevelEndpoints, contactEmailId.toString()); 

		Assert.assertEquals("ContactId 2", notUpdatedResource.getContactId(), updatedResource.getContactId());
		Assert.assertEquals("EmailAddress 2", notUpdatedResource.getEmailAddress(), updatedResource.getEmailAddress());
		Assert.assertEquals("IsPrimaryEmailInd 2", notUpdatedResource.getIsPrimaryEmailInd(), updatedResource.getIsPrimaryEmailInd());
		//Inactive records mean that the expiry date is set to the current date
		Assert.assertTrue("EffectiveDate ExpiryDate 2", updatedResource.getEffectiveDate().compareTo(updatedResource.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate 2", notUpdatedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		//CLEAN UP: DELETE CODE
		delete();		
		
		logger.debug(">testUpdateContactEmailWithoutRecordNoUpdate");
	}
	
	@Test
	public void testAddEmailWithoutContact() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testAddEmailWithoutContact");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		String emailAddress = "test1@test.com";
		Boolean isPrimaryEmailInd = true;
		Boolean isActive = true;

		//CREATE ContactEmail
		ContactEmailRsrc resource = new ContactEmailRsrc();

		resource.setContactEmailId(contactEmailId);
		resource.setContactId(null);
		resource.setEmailAddress(emailAddress);
		resource.setIsPrimaryEmailInd(isPrimaryEmailInd);
		resource.setIsActive(isActive);
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.EmailsCreated);

		//Expects:
		//No insert
		//Log entry that only contact e-mails are synchronized.
		//  Search for "E-Mail Address not inserted or updated because it's not associated with a contact" in the log
		//	--> You might have to update the buffer size of the console
		service.synchronizeContactEmail(resource);

		//ContactEmailRsrc fetchedResource = service.getContactEmail(topLevelEndpoints, contactEmailId.toString()); 
		
		//Assert.assertNull("E-Mail added", fetchedResource);

		logger.debug(">testAddEmailWithoutContact");
	}

	private void createContact(Date createTransactionDate, Integer id) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		ContactRsrc resource = new ContactRsrc();
		
		String firstName = "Test Firstname";
		String lastName = "Test Lastname";

		//INSERT
		resource.setContactId(id);
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
