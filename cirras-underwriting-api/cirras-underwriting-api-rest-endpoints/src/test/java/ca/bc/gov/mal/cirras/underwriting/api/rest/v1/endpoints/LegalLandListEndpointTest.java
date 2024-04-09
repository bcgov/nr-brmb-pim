package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactEmailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactPhoneRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandFieldXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;


public class LegalLandListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(LegalLandListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND,
		Scopes.GET_CONTACT_EMAIL,
		Scopes.GET_CONTACT_PHONE,
		Scopes.GET_GROWER_CONTACT
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	private Integer legalLandId = 999999999;
	private Integer legalLandId2 = 888888888;

	private Integer fieldId = 90000005;

	private Integer growerId = 90000001;
	private Integer growerContactId = 90000002;
	private Integer contactId = 90000003;	
	private Integer contactPhoneId = 90000004;
	private Integer contactEmailId = 90000006;
	
	private Integer annualFieldDetailId = 90000007;
	private Integer contractedFieldDetailId = 90000008;
	private Integer gcyId = 90000009;
	private Integer contractId = 90000010;
	
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

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, gcyId.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId.toString());

		LegalLandFieldXrefRsrc llfx = service.getLegalLandFieldXref(topLevelEndpoints, legalLandId.toString(), fieldId.toString());
		if ( llfx != null ) { 
			service.deleteLegalLandFieldXref(topLevelEndpoints, legalLandId.toString(), fieldId.toString());
		}

		service.deleteField(topLevelEndpoints, fieldId.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId2.toString());
		service.deleteGrowerContact(topLevelEndpoints, growerContactId.toString());
		service.deleteContactPhone(topLevelEndpoints, contactPhoneId.toString());
		service.deleteContactEmail(topLevelEndpoints, contactEmailId.toString());
		service.deleteContact(topLevelEndpoints, contactId.toString());
		service.deleteGrower(topLevelEndpoints, growerId.toString());
		
	}
	
	
	private void createLegalLand(String legalLocation, String legalDescription, Integer llId, String primaryPropertyIdentifier) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		
		String primaryReferenceTypeCode = "OTHER";
		String legalShortDescription = "Short Legal";
		String otherDescription = legalLocation;
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;
		
		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(llId);
		resource.setPrimaryPropertyIdentifier(primaryPropertyIdentifier);
		resource.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		resource.setLegalDescription(legalDescription);
		resource.setLegalShortDescription(legalShortDescription);
		resource.setOtherDescription(otherDescription);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		resource.setPrimaryLandIdentifierTypeCode(null);
		resource.setTotalAcres(null);
		resource.setTransactionType(LandManagementEventTypes.LegalLandCreated);
		service.synchronizeLegalLand(resource);

	}

	
	private void createField(Integer fieldId, String fieldLabel, Integer activeFromCropYear, Integer activeToCropYear) throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel(fieldLabel);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
		
		service.synchronizeField(resource);
	}
	
	private void createLegalLandFieldXref(Integer fieldId, Integer legalLandId) throws CirrasUnderwritingServiceException, ValidationException {
	
		//CREATE Legal Land - Field Xref
		LegalLandFieldXrefRsrc legalLandFieldXrefResource = new LegalLandFieldXrefRsrc();
		
		legalLandFieldXrefResource.setLegalLandId(legalLandId);
		legalLandFieldXrefResource.setFieldId(fieldId);
		legalLandFieldXrefResource.setTransactionType(LandManagementEventTypes.LegalLandFieldXrefCreated);

		service.synchronizeLegalLandFieldXref(legalLandFieldXrefResource);
	
	}
	
	private void createAnnualFieldDetail( Integer annualFieldDetailId, Integer legalLandId, Integer fieldId, Integer cropYear) throws CirrasUnderwritingServiceException, ValidationException {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setLegalLandId(legalLandId);
		resource.setFieldId(fieldId);
		resource.setCropYear(cropYear);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);

		service.synchronizeAnnualFieldDetail(resource);
	}

	private void createContractedFieldDetail( 
			Integer contractedFieldDetailId, 
			Integer annualFieldDetailId, 
			Integer growerContractYearId, 
			Integer displayOrder) throws CirrasUnderwritingServiceException, ValidationException {

		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId);
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setGrowerContractYearId(growerContractYearId);
		resource.setDisplayOrder(displayOrder);
		resource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailCreated);

		service.synchronizeContractedFieldDetail(resource);
	}

	private void createGrower(Integer growerId, Integer growerNumber, String growerName, Date createTransactionDate) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		GrowerRsrc resource = new GrowerRsrc();
		
		String growerAddressLine1 = "address line 1";
		String growerAddressLine2 = "address line 2";
		String growerPostalCode = "V8P 4N8";
		String growerCity = "Victoria";
		Integer cityId = 1;
		String growerProvince = "BC";

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
	
	private void createContact(Integer contactId, Date createTransactionDate) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		ContactRsrc resource = new ContactRsrc();
		
		String firstName = "Test Firstname";
		String lastName = "Test Lastname";

		//INSERT
		resource.setContactId(contactId);
		resource.setFirstName(firstName);
		resource.setLastName(lastName);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.ContactsCreated);
		
		service.synchronizeContact(resource);
	}

	private void createGrowerContact(Integer growerContactId, Integer growerId, Integer contactId, Date createTransactionDate) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		Boolean isPrimaryContactInd = true;
		Boolean isActivelyInvolvedInd = true;

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
	}

	private void createContactPhone(Integer contactPhoneId, Integer contactId, String phoneNumber, Date createTransactionDate) throws DaoException, CirrasUnderwritingServiceException, ValidationException { 

		String extension = "123";
		String telecomTypeCode = "CELL";
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
	}

	private void createContactEmail(Integer contactEmailId, Integer contactId, String emailAddress, Date createTransactionDate) throws DaoException, CirrasUnderwritingServiceException, ValidationException { 

		Boolean isPrimaryEmailInd = true;
		Boolean isActive = true;

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
	}
	
	private void createGrowerContractYear(Integer gcyId, Integer contractId, Integer growerId, Integer cropYear, Date createTransactionDate) throws DaoException, CirrasUnderwritingServiceException, ValidationException { 

		Integer insurancePlanId = 4;

		//CREATE GrowerContractYear
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(gcyId);
		resource.setContractId(contractId);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setCropYear(cropYear);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearCreated);
		
		service.synchronizeGrowerContractYear(resource);
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	
	@Test
	public void testSearchLegalLand() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testSearchLegalLand");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 because they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		
		String legalLocation = "test legal 1234";
		String legalLocation2 = legalLocation + " test";
		String legalDescription = "Legal Description 5678";
		String legalDescription2 = "Legal Description 5678 2";
		String pidCommonPrefix = "999-888";
		String pid = pidCommonPrefix + "-777";
		String pid2 = pidCommonPrefix + "-999";

		
		createLegalLand(legalLocation, legalDescription, legalLandId, pid);
		createLegalLand(legalLocation2, legalDescription2, legalLandId2, pid2);

		createGrower(growerId, 999888, "grower name", createTransactionDate);
		createContact(contactId, createTransactionDate);
		createGrowerContact(growerContactId, growerId, contactId, createTransactionDate);
		createContactPhone(contactPhoneId, contactId, "2501112222", createTransactionDate);
		createContactEmail(contactEmailId, contactId, "someEmail@vividsolutions.com", createTransactionDate);
		
		createField(fieldId, "LOT 1", 2011, 2022);
		createLegalLandFieldXref(fieldId, legalLandId);
		createAnnualFieldDetail(annualFieldDetailId, legalLandId, fieldId, 2022);
		createGrowerContractYear(gcyId, contractId, growerId, 2022, createTransactionDate);
		createContractedFieldDetail(contractedFieldDetailId, annualFieldDetailId, gcyId, 1);
		
		// ----------------------------------------
		// TEST 1: Legal Location
		// ----------------------------------------
		
		//Use exact search for other description = return 1 record
		LegalLandListRsrc searchResults = service.getLegalLandList(topLevelEndpoints, legalLocation.toLowerCase(), null, null, null, "false", "true", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);
		
		//Use wildcard search for other description = return 2 records
		searchResults = service.getLegalLandList(topLevelEndpoints, legalLocation.toLowerCase(), null, null, null, "true", "true", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId, legalLandId2);
		
		//Use exact search for legal description = return 1 record
		searchResults = service.getLegalLandList(topLevelEndpoints, legalDescription.toLowerCase(), null, null, null, "false", "true", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);
				
		//Use wildcard search for legal description = return 2 records
		searchResults = service.getLegalLandList(topLevelEndpoints, legalDescription.toLowerCase(), null, null, null, "true", "true", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId, legalLandId2);

		//Use exact search for legal description = return 0 records
		searchResults = service.getLegalLandList(topLevelEndpoints, legalDescription.toLowerCase(), null, null, null, "false", "false", null, null, null, null);
		checkLegalLandList(searchResults);
				
		//Use wildcard search for legal description = return 0 records
		searchResults = service.getLegalLandList(topLevelEndpoints, legalDescription.toLowerCase(), null, null, null, "true", "false", null, null, null, null);
		checkLegalLandList(searchResults);
		
		//Full Wildcard search for legal location = return 1 record
		String wildcardLegal = "%" + legalLocation2.substring(2);
		searchResults = service.getLegalLandList(topLevelEndpoints, wildcardLegal, null, null, null, "true", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId2);
		
		// ----------------------------------------
		// TEST 2: Primary Property Identifier
		// ----------------------------------------
		
		searchResults = service.getLegalLandList(topLevelEndpoints, null, pid, null, null, "false", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);

		searchResults = service.getLegalLandList(topLevelEndpoints, null, pidCommonPrefix, null, null, "false", "false", null, null, null, null);
		checkLegalLandList(searchResults);
		
		searchResults = service.getLegalLandList(topLevelEndpoints, null, pidCommonPrefix, null, null, "true", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId, legalLandId2);

		//Full Wildcard search
		String wildCardPid = "%" + pid.substring(2);
		searchResults = service.getLegalLandList(topLevelEndpoints, null, wildCardPid, null, null, "true", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);

		// ----------------------------------------
		// TEST 3: Grower Info
		// ----------------------------------------

		// Exact Match:
		// ------------

		// Grower Name
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "grower name", null, "false", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);

		// Grower Number
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "999888", null, "false", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);
		
		// Phone
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "2501112222", null, "false", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);

		// Email
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "someEmail@vividsolutions.com", null, "false", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);
		

		// Wrong Grower Name
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "grower na", null, "false", "false", null, null, null, null);
		checkLegalLandList(searchResults);

		// Wrong Grower Number
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "99988", null, "false", "false", null, null, null, null);
		checkLegalLandList(searchResults);
		
		// Wrong Phone
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "250111222", null, "false", "false", null, null, null, null);
		checkLegalLandList(searchResults);

		// Wrong Email
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "someEmail@vividsolutions", null, "false", "false", null, null, null, null);
		checkLegalLandList(searchResults);


		// Wildcard
		// --------

		// Grower Name
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "grower na", null, "true", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);

		// Grower Number
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "99988", null, "true", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);
		
		// Phone
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "250111222", null, "true", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);

		// Email
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "someEmail@vividsolutions", null, "true", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);
		
		// FULL Wildcard
		// --------

		// Grower Name
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "%rower na", null, "true", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);

		// Grower Number
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "%9988", null, "true", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);
		
		// Phone
//		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "2501112222", null, "true", "false", null, null, null, null);
//		checkLegalLandList(searchResults, legalLandId);

		// Email
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, "%omeEmail@vividsolutions", null, "true", "false", null, null, null, null);
		checkLegalLandList(searchResults, legalLandId);
		
		// ----------------------------------------
		// TEST 4: Dataset Type: CLEANUP: Legal Land with only auto-generated PID.
		// ----------------------------------------
		
		searchResults = service.getLegalLandList(topLevelEndpoints, null, null, null, "CLEANUP", "false", "false", null, null, null, null);
		
		// There will be several existing records that match, so just verify that each has the expected PID format.
		Assert.assertNotNull(searchResults);
		List<LegalLandRsrc> legalLands = searchResults.getCollection();
		Assert.assertNotNull(legalLands);
		Assert.assertTrue(legalLands.size() > 0);

		for ( LegalLandRsrc ll : legalLands ) {
			Assert.assertTrue(ll.getPrimaryPropertyIdentifier().matches("^GF\\d+$"));
			Assert.assertEquals("OTHER", ll.getPrimaryLandIdentifierTypeCode());
		}
		
		//CLEAN UP: DELETE CODE
		delete();		
		
		logger.debug(">testSearchLegalLand");
	}

	private void checkLegalLandList(LegalLandListRsrc results, Integer... expectedLegalLandIds ) {

		Assert.assertNotNull(results);
		List<LegalLandRsrc> legalLands = results.getCollection();
		Assert.assertNotNull(legalLands);
		Assert.assertEquals(expectedLegalLandIds.length, legalLands.size());
				
		for ( int i = 0; i < legalLands.size(); i++ ) {
			Assert.assertEquals(expectedLegalLandIds[i], legalLands.get(i).getLegalLandId());
		}
	}

	
}
