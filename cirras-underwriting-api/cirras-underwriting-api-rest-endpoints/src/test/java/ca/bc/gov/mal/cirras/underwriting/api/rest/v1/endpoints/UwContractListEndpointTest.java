package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class UwContractListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(UwContractListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.GET_UWCONTRACT,
		Scopes.CREATE_SYNC_UNDERWRITING,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND,
		Scopes.GET_POLICY,
		Scopes.GET_GROWER,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.UPDATE_INVENTORY_CONTRACT,
		Scopes.DELETE_INVENTORY_CONTRACT,
		Scopes.GET_INVENTORY_CONTRACT,
		Scopes.SEARCH_ANNUAL_FIELDS
	};

	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	private Integer growerId1 = 90000001;
	private Integer growerContactId1 = 90000002;
	private Integer contactId1 = 90000003;
	
	private Integer contractId1 = 90000004;
	private Integer policyId1 = 90000005;
	private String policyNumber1 = "998877-20";
	private String contractNumber1 = "998877";
	private Integer growerContractYearId1 = 90000006;

	private Integer contractId2 = 90000007;
	private Integer policyId2 = 90000008;
	private String policyNumber2 = "998899-20";
	private String contractNumber2 = "998899";
	private Integer growerContractYearId2 = 90000009;
	
	// Field
	private Integer fieldId1 = 90000010;
	private Integer annualFieldDetailId1 = 90000011;
	private Integer contractedFieldDetailId1 = 90000012;
	private Integer contractedFieldDetailId2 = 90000013;

	
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

	private void delete() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException {

		deleteInventoryContract(policyNumber1);		
		
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());

		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId1.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId2.toString());

		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		
		service.deleteField(topLevelEndpoints, fieldId1.toString());

		service.deletePolicy(topLevelEndpoints, policyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId2.toString());

		service.deleteGrowerContact(topLevelEndpoints, growerContactId1.toString());
		service.deleteContact(topLevelEndpoints, contactId1.toString());
		
		service.deleteGrower(topLevelEndpoints, growerId1.toString());		
	}

	private void deleteInventoryContract(String policyNumber) throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException {

		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber,
				null,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);
		
		if ( searchResults.getCollection() != null && searchResults.getCollection().size() == 1 ) {
			UwContractRsrc referrer = searchResults.getCollection().get(0);
						
			if ( referrer.getInventoryContractGuid() != null ) { 
				InventoryContractRsrc invContract = service.getInventoryContract(referrer);
				service.deleteInventoryContract(invContract);
			}
		}		
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

	private void createGrowerContractYear(
			Integer gcyId, 
			Integer contractId, 
			Integer growerId, 
			Integer cropYear, 
			Integer insurancePlanId, 
			Date createTransactionDate
	) throws DaoException, CirrasUnderwritingServiceException, ValidationException { 

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
	
	private void createPolicy(
			Integer policyId, 
			Integer growerId, 
			Integer insurancePlanId, 
			String policyNumber, 
			String contractNumber, 
			Integer contractId, 
			Integer cropYear, 
			Date createTransactionDate
	) throws ValidationException, CirrasUnderwritingServiceException {

		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(policyId);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setPolicyStatusCode("ACTIVE");
		resource.setOfficeId(1);
		resource.setPolicyNumber(policyNumber);
		resource.setContractNumber(contractNumber);
		resource.setContractId(contractId);
		resource.setCropYear(cropYear);
		
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.PolicyCreated);
		
		service.synchronizePolicy(resource);
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
	

	private void createInventoryContract(String policyNumber, Integer insurancePlanId) throws ValidationException, CirrasUnderwritingServiceException {

		boolean addedSeededGrain = false;
		boolean addedSeededForage = false;
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber,
				null,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());

		InventoryContractRsrc resource = service.rolloverInventoryContract(referrer);
		
		for ( AnnualFieldRsrc field : resource.getFields() ) {
			for ( InventoryField planting : field.getPlantings() ) {
				if ( planting.getInventorySeededForages() != null && insurancePlanId.equals(InsurancePlans.FORAGE.getInsurancePlanId()) ) {
					for ( InventorySeededForage isf : planting.getInventorySeededForages() ) {

						// Fix Seeded Forage, which sets null defaults for a few mandatory columns.
						isf.setIsIrrigatedInd(false);
						isf.setIsQuantityInsurableInd(true);
						isf.setCommodityTypeCode("CPSW");
						isf.setCropCommodityId(26);
						isf.setCropVarietyId(1010602);
						isf.setCropVarietyName("AAC ENTICE");
						isf.setFieldAcres(10.4);
						isf.setSeedingYear(2018);
						isf.setSeedingDate(null);
						isf.setPlantInsurabilityTypeCode("E1");
						isf.setIsAwpEligibleInd(true);
							
						addedSeededForage = true;
					}
				} else if ( planting.getInventorySeededGrains() != null && insurancePlanId.equals(InsurancePlans.GRAIN.getInsurancePlanId()) ) {
					
					Calendar cal = Calendar.getInstance();
					cal.clear();
					cal.set(2020, Calendar.JANUARY, 15);
					Date seededDate = cal.getTime();
					
					InventorySeededGrain invSeededGrain = new InventorySeededGrain();

					invSeededGrain.setCommodityTypeCode("Two Row");
					invSeededGrain.setCommodityTypeDesc("Two Row Standard");
					invSeededGrain.setCropCommodityId(16);
					invSeededGrain.setCropCommodityName("BARLEY");
					invSeededGrain.setCropVarietyId(1010430);
					invSeededGrain.setCropVarietyName("CHAMPION");
					invSeededGrain.setInventoryFieldGuid(null);
					invSeededGrain.setInventorySeededGrainGuid(null);
					invSeededGrain.setIsPedigreeInd(false);
					invSeededGrain.setIsSpotLossInsurableInd(true);
					invSeededGrain.setIsQuantityInsurableInd(true);
					invSeededGrain.setIsReplacedInd(false);
					invSeededGrain.setSeededAcres(23.45);
					invSeededGrain.setSeededDate(seededDate);
					
					List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
					seededGrains.add(invSeededGrain);

					planting.setInventorySeededGrains(seededGrains);
					
					addedSeededGrain = true;
				}
			}
		}
		
		service.createInventoryContract(topLevelEndpoints, resource);

		if ( insurancePlanId.equals(InsurancePlans.GRAIN.getInsurancePlanId()) ) {
			Assert.assertTrue(addedSeededGrain);
		} else if ( insurancePlanId.equals(InsurancePlans.FORAGE.getInsurancePlanId()) ) {
			Assert.assertTrue(addedSeededForage);
		}
	}
	
	
	@Test
	public void testSearchUwContracts() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testSearchUwContracts");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		String cropYear = "2020";
		String insurancePlanId = "5";
		String officeId = null;
		String policyStatusCode = null;
		String policyNumber = null;
		String growerInfo = null;
		String datasetType = null;

		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				officeId,
				policyStatusCode,
				policyNumber,
				growerInfo,
				datasetType, 
				null, 
				null, 
				pageNumber, 
				pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);
		
		logger.debug(">testSearchUwContracts");
	}
	
	
	@Test 
	public void testSelectPoliciesWithFilters() throws Exception {	
		logger.debug("<testSelectPoliciesWithFilters");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		createGrower(growerId1, 999888, "grower name", createTransactionDate);
		createContact(contactId1, createTransactionDate);
		createGrowerContact(growerContactId1, growerId1, contactId1, createTransactionDate);
		createPolicy(policyId1, growerId1, 4, policyNumber1, contractNumber1, contractId1, 2020, createTransactionDate);

		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(200);

		// Test: crop year, insurance plan, office, policy status
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				"2020", 
				"4", 
				"1",
				"ACTIVE",
				null,
				null,
				null, 
				null, 
				null, 
				pageNumber, 
				pageRowCount);
		
		Assert.assertNotNull(searchResults);
		List<UwContractRsrc> uwContracts = searchResults.getCollection();
		Assert.assertNotNull(uwContracts);
		Assert.assertFalse(uwContracts.isEmpty());

		// This will return many results.
		for ( UwContractRsrc uwContract : uwContracts ) {
			Assert.assertEquals(2020, uwContract.getCropYear().intValue());
			Assert.assertEquals(4, uwContract.getInsurancePlanId().intValue());
			Assert.assertEquals("ACTIVE", uwContract.getPolicyStatus());
		}
		
		// Test: Policy Number
		// A. Exact match.
		searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber1,
				null,
				null, 
				null, 
				null, 
				pageNumber, 
				pageRowCount);

		checkUwContractList(searchResults, policyId1);

		// B. Partial match.
		searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				contractNumber1,
				null,
				null, 
				null, 
				null, 
				pageNumber, 
				pageRowCount);

		checkUwContractList(searchResults, policyId1);
		
		
		// Test: Grower Name
		searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				null,
				"grower na",
				null, 
				null, 
				null, 
				pageNumber, 
				pageRowCount);

		checkUwContractList(searchResults, policyId1);

		// Test: Grower Number
		searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				null,
				"99988",
				null, 
				null, 
				null, 
				pageNumber, 
				pageRowCount);

		checkUwContractList(searchResults, policyId1);

		// Test: Grower Email, Grower Phone
		// Not covered here, but is tested in PolicyDaoTest.
		
		// Test: dataset type = LINKED_GF_POLICIES
		// A. Policies with same grower.
		createPolicy(policyId2, growerId1, 5, policyNumber2, contractNumber2, contractId2, 2020, createTransactionDate);
		
		searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber1,
				null,
				"LINKED_GF_POLICIES", 
				null, 
				null, 
				pageNumber, 
				pageRowCount);

		checkUwContractList(searchResults, policyId2);
		
		// B. Policies with shared fields. Not covered here, but is tested in PolicyDaoTest.
		
		logger.debug(">testSelectPoliciesWithFilters");
	}

	@Test 
	public void testTotalDopEligibleInventory() throws Exception {	
		logger.debug("<testTotalDopEligibleInventory");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		createGrower(growerId1, 999888, "grower name", createTransactionDate);
		createContact(contactId1, createTransactionDate);
		createGrowerContact(growerContactId1, growerId1, contactId1, createTransactionDate);
		createPolicy(policyId1, growerId1, 4, policyNumber1, contractNumber1, contractId1, 2020, createTransactionDate);

		createField(fieldId1, "LOT 1", 1980, null);
		createAnnualFieldDetail(annualFieldDetailId1, null, fieldId1, 2020);

		createGrowerContractYear(growerContractYearId1, contractId1, growerId1, 2020, 4, createTransactionDate);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(200);

		// Test: Total DOP Eligible Inventory
		// A. Select - 0
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber1,
				null,
				null, 
				null, 
				null, 
				pageNumber, 
				pageRowCount);

		checkUwContractList(searchResults, policyId1);
		Assert.assertEquals(0, searchResults.getCollection().get(0).getTotalDopEligibleInventory().intValue());

		// B: Fetch - 0
		UwContractRsrc uwContract = service.getUwContract(searchResults.getCollection().get(0), "false", "false", null);
		Assert.assertEquals(0, uwContract.getTotalDopEligibleInventory().intValue());

		// C: Select - 1
		createInventoryContract(policyNumber1, 4);

		searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber1,
				null,
				null, 
				null, 
				null, 
				pageNumber, 
				pageRowCount);

		checkUwContractList(searchResults, policyId1);
		Assert.assertEquals(1, searchResults.getCollection().get(0).getTotalDopEligibleInventory().intValue());

		// D: Fetch - 1
		uwContract = service.getUwContract(searchResults.getCollection().get(0), "false", "false", null);
		Assert.assertEquals(1, uwContract.getTotalDopEligibleInventory().intValue());
		
				
		logger.debug(">testTotalDopEligibleInventory");
	}
	
	
	private void checkUwContractList(UwContractListRsrc results, Integer... expectedPolicyIds ) {

		Assert.assertNotNull(results);
		List<UwContractRsrc> uwContracts = results.getCollection();
		Assert.assertNotNull(uwContracts);
		Assert.assertEquals(expectedPolicyIds.length, uwContracts.size());
				
		for ( int i = 0; i < uwContracts.size(); i++ ) {
			Assert.assertEquals(expectedPolicyIds[i], uwContracts.get(i).getPolicyId());
		}
	}
	
	
	private int previousIntValue = 0;
	private int currentIntValue = 0;
	private String previousStringValue = "";
	private String currentStringValue = "";
	private boolean orderMatches = true;
	private boolean testSuccessful = true;
	private final String sortOrderAscending = "ASC";
	private final String sortOrderDescending = "DESC";
	private String sortDirection = "";

	@Test 
	public void testSelectPoliciesSortingByPolicyNumber() throws Exception {	
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		
		String sortColumn = null;
		sortColumn = "policyNumber";	//String
		
		sortDirection = sortOrderAscending;

		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(50);
		String cropYear = "2021";
		String insurancePlanId = "4";
		
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				null,
				null,
				null,
				null,
				null, 
				sortColumn, 
				sortDirection, 
				pageNumber, pageRowCount);

		
		Assert.assertNotNull("Policynumber ASC", searchResults);
		
		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		testSuccessful = true;
		
		searchResults.getCollection().forEach((temp) -> {
			
			currentStringValue = temp.getPolicyNumber();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("Policynumber ASC", testSuccessful);
		
		
		sortDirection = sortOrderDescending;
		
		searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				null,
				null,
				null,
				null,
				null, 
				sortColumn, 
				sortDirection, 
				pageNumber, pageRowCount);
		
		Assert.assertNotNull("Policynumber DESC", searchResults);

		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		
		searchResults.getCollection().forEach((temp) -> {
			
			currentStringValue = temp.getPolicyNumber();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("Policynumber DESC", testSuccessful);
	}
	
	@Test 
	public void testSelectPoliciesSortingByPolicyStatus() throws Exception {	
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		
		String sortColumn = null;
		sortColumn = "policyStatus";	//String
		
		sortDirection = sortOrderAscending;

		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(50);
		String cropYear = "2021";
		String insurancePlanId = "4";
		
		
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				null,
				null,
				null,
				null,
				null, 
				sortColumn, 
				sortDirection, 
				pageNumber, pageRowCount);

		Assert.assertNotNull("policyStatus ASC", searchResults);
		
		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			
			currentStringValue = temp.getPolicyStatus();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("policyStatus ASC", testSuccessful);
		
		
		sortDirection = sortOrderDescending;
		
		searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				null,
				null,
				null,
				null,
				null, 
				sortColumn, 
				sortDirection, 
				pageNumber, pageRowCount);
		
		Assert.assertNotNull("policyStatus DESC", searchResults);

		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		
		searchResults.getCollection().forEach((temp) -> {
			
			currentStringValue = temp.getPolicyStatus();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("policyStatus DESC", testSuccessful);
	}
	
	@Test 
	public void testSelectPoliciesSortingByInsurancePlan() throws Exception {	
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		
		String sortColumn = null;
		sortColumn = "insurancePlanName";	//String
		
		sortDirection = sortOrderAscending;

		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(250);
		String cropYear = "2021";
		String insurancePlanId = null;
		
		
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				null,
				null,
				null,
				null,
				null, 
				sortColumn, 
				sortDirection, 
				pageNumber, pageRowCount);

		Assert.assertNotNull("insurancePlan ASC", searchResults);
	
		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		testSuccessful = true;
		
		searchResults.getCollection().forEach((temp) -> {
			
			currentStringValue = temp.getInsurancePlanName();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("insurancePlan ASC", testSuccessful);
		
		
		sortDirection = sortOrderDescending;
		
		searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				null,
				null,
				null,
				null,
				null, 
				sortColumn, 
				sortDirection, 
				pageNumber, pageRowCount);
		
		Assert.assertNotNull("insurancePlan DESC", searchResults);

		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		
		searchResults.getCollection().forEach((temp) -> {
			
			currentStringValue = temp.getInsurancePlanName();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("insurancePlan DESC", testSuccessful);
	}	

	@Test 
	public void testSelectPoliciesSortingByGrowerName() throws Exception {	
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		
		String sortColumn = null;
		sortColumn = "growerName";	//String
		
		sortDirection = sortOrderAscending;

		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(50);
		String cropYear = "2021";
		String insurancePlanId = "4";
		
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				null,
				null,
				null,
				null,
				null, 
				sortColumn, 
				sortDirection, 
				pageNumber, pageRowCount);

		Assert.assertNotNull("growerName ASC", searchResults);

		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		testSuccessful = true;
		
		searchResults.getCollection().forEach((temp) -> {
			
			currentStringValue = temp.getGrowerName();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("growerName ASC", testSuccessful);
		
		
		sortDirection = sortOrderDescending;
		
		searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				null,
				null,
				null,
				null,
				null, 
				sortColumn, 
				sortDirection, 
				pageNumber, pageRowCount);
		
		Assert.assertNotNull("growerName DESC", searchResults);

		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		
		searchResults.getCollection().forEach((temp) -> {
			
			currentStringValue = temp.getGrowerName();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("growerName DESC", testSuccessful);
	}
	
	@Test 
	public void testSelectPoliciesSortingByGrowerNumber() throws Exception {	
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		
		String sortColumn = null;
		sortColumn = "growerNumber";	//String
		
		sortDirection = sortOrderAscending;

		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(50);
		String cropYear = "2021";
		String insurancePlanId = "4";
		
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				null,
				null,
				null,
				null,
				null, 
				sortColumn, 
				sortDirection, 
				pageNumber, pageRowCount);

		Assert.assertNotNull("growerNumber ASC", searchResults);
	
		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		testSuccessful = true;
		
		searchResults.getCollection().forEach((temp) -> {
			
			currentIntValue = temp.getGrowerNumber();
			
			compareInt(sortDirection);
        });	  
		
		orderMatches = true;
		previousIntValue = 0;
		testSuccessful = true;
		
		
		Assert.assertTrue("growerNumber ASC", testSuccessful);
		
		
		sortDirection = sortOrderDescending;
		
		searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				null,
				null,
				null,
				null,
				null, 
				sortColumn, 
				sortDirection, 
				pageNumber, pageRowCount);
		
		Assert.assertNotNull("growerNumber DESC", searchResults);

		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		
		searchResults.getCollection().forEach((temp) -> {
			
			currentIntValue = temp.getGrowerNumber();
			
			compareInt(sortDirection);
        });	  
		
		Assert.assertTrue("growerNumber DESC", testSuccessful);
	}

	private void compareStrings(String sortDirection) {
		//First iteration of loop: previousValue = 0			
		if(previousStringValue != ""){
			
			int compare = previousStringValue.compareToIgnoreCase(currentStringValue);
			
			//compare < 0 if sA is smaller than sB
			//compare = 0 if sA is the same as sB
			//compare > 0 if sA is greater than sB 

			if(sortDirection.contentEquals(sortOrderAscending) ) {
				orderMatches = (compare <= 0); 
			} else {
				orderMatches = (compare >= 0); 				
			}
				
		}
		
		if(!orderMatches) {
		    System.out.println("Order is wrong: " + currentStringValue + " < " + previousStringValue);
		    testSuccessful = false;
		}
		
		previousStringValue = currentStringValue;
	}
	
	private void compareInt(String sortDirection) {
		//First iteration of loop: previousValue = 0			
		if(previousIntValue > 0){
			
			
			if(sortDirection.contentEquals(sortOrderAscending) ) {
				orderMatches = (previousIntValue <= currentIntValue); 
			} else {
				orderMatches = (previousIntValue >= currentIntValue); 			
			}

		}
		
		if(!orderMatches) {
            System.out.println("Order is wrong: " + currentIntValue + " < " + previousIntValue);
            testSuccessful = false;
		}
		
		previousIntValue = currentIntValue;

	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	
}
