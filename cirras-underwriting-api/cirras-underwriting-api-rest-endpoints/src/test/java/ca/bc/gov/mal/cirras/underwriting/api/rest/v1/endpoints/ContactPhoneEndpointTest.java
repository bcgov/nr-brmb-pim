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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactPhoneRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;


public class ContactPhoneEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(ContactPhoneEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_CONTACT_PHONE
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer contactPhoneId = 999999999;
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

		service.deleteContactPhone(topLevelEndpoints, contactPhoneId.toString());
		service.deleteContact(topLevelEndpoints, contactId.toString());
		service.deleteContact(topLevelEndpoints, contactId2.toString());
		
	}
	
	@Test
	public void testCreateUpdateDeleteContactPhone() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateContactPhone");
		
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

		String phoneNumber = "2501113333";
		String extension = "123";
		String telecomTypeCode = "CELL";
		Boolean isPrimaryPhoneInd = true;
		Boolean isActive = false;

		//CREATE ContactPhone
		ContactPhoneRsrc resource = new ContactPhoneRsrc();

		resource.setContactPhoneId(contactPhoneId);
		resource.setContactId(contactId);
		resource.setPhoneNumber(phoneNumber);
		resource.setExtension(extension);
		resource.setIsPrimaryPhoneInd(isPrimaryPhoneInd);
		resource.setIsActive(isActive);
		resource.setTelecomTypeCode(telecomTypeCode);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.TelecomCreated);

		service.synchronizeContactPhone(resource);
		
		ContactPhoneRsrc fetchedResource = service.getContactPhone(topLevelEndpoints, contactPhoneId.toString()); 

		Assert.assertEquals("contactPhoneId 1", resource.getContactPhoneId(), fetchedResource.getContactPhoneId());
		Assert.assertEquals("ContactId", resource.getContactId(), fetchedResource.getContactId());
		Assert.assertEquals("PhoneNumber", resource.getPhoneNumber(), fetchedResource.getPhoneNumber());
		Assert.assertEquals("Extension", resource.getExtension(), fetchedResource.getExtension());
		Assert.assertEquals("IsPrimaryPhoneInd", resource.getIsPrimaryPhoneInd(), fetchedResource.getIsPrimaryPhoneInd());
		//Inactive records mean that the expiry date is set to the current date
		Assert.assertTrue("EffectiveDate ExpiryDate 1", fetchedResource.getEffectiveDate().compareTo(fetchedResource.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//UPDATE CODE
		phoneNumber = "6049998888";
		extension = "987";
		isPrimaryPhoneInd = false;
		isActive = true;

		fetchedResource.setContactId(contactId2);
		fetchedResource.setPhoneNumber(phoneNumber);
		fetchedResource.setExtension(extension);
		fetchedResource.setIsPrimaryPhoneInd(isPrimaryPhoneInd);
		fetchedResource.setIsActive(isActive);
		fetchedResource.setTelecomTypeCode(telecomTypeCode);

		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.TelecomUpdated);

		service.synchronizeContactPhone(fetchedResource);
		
		ContactPhoneRsrc updatedResource = service.getContactPhone(topLevelEndpoints, contactPhoneId.toString()); 

		Assert.assertEquals("ContactId 2", fetchedResource.getContactId(), updatedResource.getContactId());
		Assert.assertEquals("PhoneNumber 2", fetchedResource.getPhoneNumber(), updatedResource.getPhoneNumber());
		Assert.assertEquals("Extension 2", fetchedResource.getExtension(), updatedResource.getExtension());
		Assert.assertEquals("IsPrimaryPhoneInd 2", fetchedResource.getIsPrimaryPhoneInd(), updatedResource.getIsPrimaryPhoneInd());
		//Active records mean that the expiry date is set to a date in the future
		Assert.assertTrue("EffectiveDate ExpiryDate 2", updatedResource.getEffectiveDate().compareTo(updatedResource.getExpiryDate()) < 0);
		Assert.assertTrue("DataSyncTransDate 2", fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);
		
		//CLEAN UP: DELETE CODE
		delete();
		
		logger.debug(">testCreateUpdateContactPhone");
	}

	
	@Test
	public void testUpdateContactPhoneWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateContactPhoneWithoutRecordNoUpdate");
		
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

		String phoneNumber = "2501113333";
		String extension = "123";
		String telecomTypeCode = "PHONE";
		Boolean isPrimaryPhoneInd = false;
		Boolean isActive = true;

		//CREATE ContactPhone
		ContactPhoneRsrc resource = new ContactPhoneRsrc();

		resource.setContactPhoneId(contactPhoneId);
		resource.setContactId(contactId);
		resource.setPhoneNumber(phoneNumber);
		resource.setExtension(extension);
		resource.setIsPrimaryPhoneInd(isPrimaryPhoneInd);
		resource.setIsActive(isActive);
		resource.setTelecomTypeCode(telecomTypeCode);

		resource.setDataSyncTransDate(createTransactionDate);
		
		
		//TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXPECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.TelecomDeleted);
		service.synchronizeContactPhone(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesSyncEventTypes.TelecomUpdated);

		service.synchronizeContactPhone(resource);

		ContactPhoneRsrc fetchedResource = service.getContactPhone(topLevelEndpoints, contactPhoneId.toString()); 

		Assert.assertEquals("ContactPhoneId 1", resource.getContactPhoneId(), fetchedResource.getContactPhoneId());
		Assert.assertEquals("ContactId", resource.getContactId(), fetchedResource.getContactId());
		Assert.assertEquals("PhoneNumber", resource.getPhoneNumber(), fetchedResource.getPhoneNumber());
		Assert.assertEquals("Extension", resource.getExtension(), fetchedResource.getExtension());
		Assert.assertEquals("IsPrimaryPhoneInd", resource.getIsPrimaryPhoneInd(), fetchedResource.getIsPrimaryPhoneInd());
		//Active records mean that the expiry date is set to a date in the future
		Assert.assertTrue("EffectiveDate ExpiryDate 1", fetchedResource.getEffectiveDate().compareTo(fetchedResource.getExpiryDate()) < 0);
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		fetchedResource.setIsActive(isActive);
		fetchedResource.setDataSyncTransDate(addSeconds(createTransactionDate, -1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.TelecomUpdated);
		service.synchronizeContactPhone(fetchedResource);
		
		ContactPhoneRsrc notUpdatedResource = service.getContactPhone(topLevelEndpoints, contactPhoneId.toString()); 

		Assert.assertTrue("DataSyncTransDate 2", resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);

		
		//UPDATE ContactPhone
		phoneNumber = "6049998888";
		extension = "987";
		isPrimaryPhoneInd = true;
		isActive = false;

		notUpdatedResource.setContactId(contactId2);
		notUpdatedResource.setPhoneNumber(phoneNumber);
		notUpdatedResource.setExtension(extension);
		notUpdatedResource.setTelecomTypeCode(telecomTypeCode);
		notUpdatedResource.setIsPrimaryPhoneInd(isPrimaryPhoneInd);
		notUpdatedResource.setIsActive(isActive);
		
		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		notUpdatedResource.setTransactionType(PoliciesSyncEventTypes.TelecomCreated);
		notUpdatedResource.setDataSyncTransDate(addSeconds(createTransactionDate, 10));
		service.synchronizeContactPhone(notUpdatedResource);
		
		ContactPhoneRsrc updatedResource = service.getContactPhone(topLevelEndpoints, contactPhoneId.toString()); 

		Assert.assertEquals("ContactId 2", notUpdatedResource.getContactId(), updatedResource.getContactId());
		Assert.assertEquals("PhoneNumber 2", notUpdatedResource.getPhoneNumber(), updatedResource.getPhoneNumber());
		Assert.assertEquals("Extension 2", notUpdatedResource.getExtension(), updatedResource.getExtension());
		Assert.assertEquals("IsPrimaryPhoneInd 2", notUpdatedResource.getIsPrimaryPhoneInd(), updatedResource.getIsPrimaryPhoneInd());
		//Inactive records mean that the expiry date is set to the current date
		Assert.assertTrue("EffectiveDate ExpiryDate 2", updatedResource.getEffectiveDate().compareTo(updatedResource.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate 2", notUpdatedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		//CLEAN UP: DELETE CODE
		delete();		
		
		logger.debug(">testUpdateContactPhoneWithoutRecordNoUpdate");
	}

	@Test
	public void testSyncFax() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testSyncFax");
		
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

		String phoneNumber = "2501113333";
		String extension = "123";
		String telecomTypeCode = "FAX";
		Boolean isPrimaryPhoneInd = true;
		Boolean isActive = true;

		//CREATE ContactPhone
		ContactPhoneRsrc resource = new ContactPhoneRsrc();

		resource.setContactPhoneId(contactPhoneId);
		resource.setContactId(contactId);
		resource.setPhoneNumber(phoneNumber);
		resource.setExtension(extension);
		resource.setIsPrimaryPhoneInd(isPrimaryPhoneInd);
		resource.setIsActive(isActive);
		resource.setTelecomTypeCode(telecomTypeCode);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.TelecomCreated);

		service.synchronizeContactPhone(resource);
		
		ContactPhoneRsrc fetchedResource = service.getContactPhone(topLevelEndpoints, contactPhoneId.toString()); 
		
		Assert.assertNull("Fax Number Added", fetchedResource);

		//CLEAN UP: DELETE CODE
		delete();
		
		logger.debug(">testSyncFax");
	}
	
	@Test
	public void testUpdatePhoneToFax() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdatePhoneToFax");
		
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

		String phoneNumber = "2501113333";
		String extension = "123";
		String telecomTypeCode = "PHONE";
		Boolean isPrimaryPhoneInd = true;
		Boolean isActive = true;

		//CREATE ContactPhone
		ContactPhoneRsrc resource = new ContactPhoneRsrc();

		resource.setContactPhoneId(contactPhoneId);
		resource.setContactId(contactId);
		resource.setPhoneNumber(phoneNumber);
		resource.setExtension(extension);
		resource.setIsPrimaryPhoneInd(isPrimaryPhoneInd);
		resource.setIsActive(isActive);
		resource.setTelecomTypeCode(telecomTypeCode);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.TelecomCreated);

		service.synchronizeContactPhone(resource);
		
		ContactPhoneRsrc fetchedResource = service.getContactPhone(topLevelEndpoints, contactPhoneId.toString()); 

		Assert.assertNotNull("Phone added", fetchedResource);
		
		
		//EXPECTED To delete the phone record
		telecomTypeCode = "FAX";
		fetchedResource.setTelecomTypeCode(telecomTypeCode);
		fetchedResource.setIsActive(isActive);
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.TelecomUpdated);
		fetchedResource.setDataSyncTransDate(addSeconds(createTransactionDate, 10));
		service.synchronizeContactPhone(fetchedResource);
		
		ContactPhoneRsrc deletedResource = service.getContactPhone(topLevelEndpoints, contactPhoneId.toString()); 


		Assert.assertNull("Fax Number Added", deletedResource);

		//CLEAN UP: DELETE CODE
		delete();
		
		logger.debug(">testUpdatePhoneToFax");
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
