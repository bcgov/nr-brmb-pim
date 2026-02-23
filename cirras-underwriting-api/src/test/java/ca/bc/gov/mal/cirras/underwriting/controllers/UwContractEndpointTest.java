package ca.bc.gov.mal.cirras.underwriting.controllers;

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

import ca.bc.gov.mal.cirras.underwriting.services.utils.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.ScreenType;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.clients.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.OtherYearPolicy;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class UwContractEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(UwContractEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.GET_UWCONTRACT,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.DELETE_INVENTORY_CONTRACT,
		Scopes.GET_INVENTORY_CONTRACT,
		Scopes.UPDATE_INVENTORY_CONTRACT,
		Scopes.CREATE_DOP_YIELD_CONTRACT,
		Scopes.DELETE_DOP_YIELD_CONTRACT,
		Scopes.GET_DOP_YIELD_CONTRACT,		
		Scopes.UPDATE_DOP_YIELD_CONTRACT,
		Scopes.GET_VERIFIED_YIELD_CONTRACT,
		Scopes.CREATE_VERIFIED_YIELD_CONTRACT,
		Scopes.UPDATE_VERIFIED_YIELD_CONTRACT,
		Scopes.DELETE_VERIFIED_YIELD_CONTRACT,
		Scopes.CREATE_SYNC_UNDERWRITING,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND,
		Scopes.GET_POLICY,
		Scopes.GET_GROWER
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	private Integer growerId1 = 90000001;
	private Integer contractId1 = 90000002;
	private Integer policyId1 = 90000003;
	private String policyNumber1 = "998877-20";
	private String contractNumber1 = "998877";
	private Integer growerContractYearId1 = 90000004;

	private Integer growerId2 = 90000005;
	private Integer contractId2 = 90000006;
	private Integer policyId2 = 90000007;
	private String policyNumber2 = "998899-20";
	private String contractNumber2 = "998899";
	private Integer growerContractYearId2 = 90000008;
	private Integer cropYear2 = 2020;

	// Note that policy 3 is deliberately meant to have the same contract as policy 2.
	private Integer contractId3 = 90000006;
	private Integer policyId3 = 90000013;
	private String policyNumber3 = "998899-19";
	private String contractNumber3 = "998899";
	private Integer growerContractYearId3 = 90000014;
	private Integer cropYear3 = 2019;
	
	
	// Field
	private Integer fieldId1 = 90000009;
	private Integer annualFieldDetailId1 = 90000010;
	private Integer contractedFieldDetailId1 = 90000011;
	private Integer contractedFieldDetailId2 = 90000012;

	
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

		deleteVerifiedYieldContract(policyNumber3);		
		deleteDopYieldContract(policyNumber3);
		deleteInventoryContract(policyNumber3);
		
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());

		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId1.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId2.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId3.toString());

		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		
		service.deleteField(topLevelEndpoints, fieldId1.toString());

		service.deletePolicy(topLevelEndpoints, policyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId2.toString());
		service.deletePolicy(topLevelEndpoints, policyId3.toString());

		service.deleteGrower(topLevelEndpoints, growerId1.toString());		
		service.deleteGrower(topLevelEndpoints, growerId2.toString());		
	}

	private void deleteVerifiedYieldContract(String policyNumber) throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException {

		Integer pageNumber = 1;
		Integer pageRowCount = 20;
		
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
						
			if ( referrer.getVerifiedYieldContractGuid() != null ) { 
				VerifiedYieldContractRsrc verifiedContract = service.getVerifiedYieldContract(referrer);
				service.deleteVerifiedYieldContract(verifiedContract);
			}
		}		
	}

	private void deleteDopYieldContract(String policyNumber) throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException {

		Integer pageNumber = 1;
		Integer pageRowCount = 20;
		
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
						
			if ( referrer.getDeclaredYieldContractGuid() != null ) { 
				DopYieldContractRsrc dopContract = service.getDopYieldContract(referrer);
				service.deleteDopYieldContract(dopContract);
			}
		}		
	}
	
	public void deleteInventoryContract(String policyNumber)
			throws CirrasUnderwritingServiceException {
		
		Integer pageNumber = 1;
		Integer pageRowCount = 20;
		
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
				pageNumber, 
				pageRowCount);
		
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
	
	private String createInventoryContract(String policyNumber) throws ValidationException, CirrasUnderwritingServiceException {

		boolean addedSeededGrain = false;
		
		Integer pageNumber = 1;
		Integer pageRowCount = 20;

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
				List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
				seededGrains.add(createInventorySeededGrain(16, "BARLEY", false, 23.45));

				planting.setInventorySeededGrains(seededGrains);
				
				addedSeededGrain = true;
			}			
		}
		
		InventoryContractCommodity icc = createInventoryContractCommodity(16, "BARLEY", false, 23.45, 23.45, 0.0);
		resource.getCommodities().add(icc);

		Assert.assertTrue(addedSeededGrain);

		return service.createInventoryContract(topLevelEndpoints, resource).getInventoryContractGuid();
	}

	private InventorySeededGrain createInventorySeededGrain(Integer cropCommodityId, String cropCommodityName, Boolean isPedigreeInd, Double seededAcres) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date seededDate = cal.getTime();
		
		InventorySeededGrain invSeededGrain = new InventorySeededGrain();

		invSeededGrain.setCommodityTypeCode("Two Row");
		invSeededGrain.setCommodityTypeDesc("Two Row Standard");
		invSeededGrain.setCropCommodityId(cropCommodityId);
		invSeededGrain.setCropCommodityName(cropCommodityName);
		invSeededGrain.setCropVarietyId(1010430);
		invSeededGrain.setCropVarietyName("CHAMPION");
		invSeededGrain.setInventoryFieldGuid(null);
		invSeededGrain.setInventorySeededGrainGuid(null);
		invSeededGrain.setIsPedigreeInd(isPedigreeInd);
		invSeededGrain.setIsSpotLossInsurableInd(true);
		invSeededGrain.setIsQuantityInsurableInd(true);
		invSeededGrain.setIsReplacedInd(false);
		invSeededGrain.setSeededAcres(seededAcres);
		invSeededGrain.setSeededDate(seededDate);
		return invSeededGrain;
	}

	private InventoryContractCommodity createInventoryContractCommodity(Integer cropCommodityId, String cropCommodityName, Boolean isPedigreeInd, Double totalSeededAcres, Double totalSpotLossAcres, Double totalUnseededAcres) {

		InventoryContractCommodity icc = new InventoryContractCommodity();
		icc.setCropCommodityId(cropCommodityId);
		icc.setCropCommodityName(cropCommodityName);
		icc.setInventoryContractCommodityGuid(null);
		icc.setInventoryContractGuid(null);
		icc.setIsPedigreeInd(isPedigreeInd);
		icc.setTotalSeededAcres(totalSeededAcres);
		icc.setTotalSpotLossAcres(totalSpotLossAcres);
		icc.setTotalUnseededAcres(totalUnseededAcres);
		icc.setTotalUnseededAcresOverride(null);
		
		return icc;
	}
	
	
	private String createDopYieldContract(String policyNumber) throws ValidationException, CirrasUnderwritingServiceException {

		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();
		
		Integer pageNumber = 1;
		Integer pageRowCount = 20;

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
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNull(referrer.getDeclaredYieldContractGuid());
				
		DopYieldContractRsrc resource = service.rolloverDopYieldContract(referrer);
		
		resource.setDeclarationOfProductionDate(dopDate);
		resource.setDefaultYieldMeasUnitTypeCode("TONNE");
		resource.setEnteredYieldMeasUnitTypeCode("TONNE");
		resource.setGrainFromOtherSourceInd(true);

		return service.createDopYieldContract(topLevelEndpoints, resource).getDeclaredYieldContractGuid();
	}

	private String createVerifiedYieldContract(String policyNumber) throws ValidationException, CirrasUnderwritingServiceException {
		
		Integer pageNumber = 1;
		Integer pageRowCount = 20;

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
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
				
		VerifiedYieldContractRsrc resource = service.rolloverVerifiedYieldContract(referrer);
		
		resource.setDefaultYieldMeasUnitTypeCode("TONNE");

		return service.createVerifiedYieldContract(topLevelEndpoints, resource).getVerifiedYieldContractGuid();
	}
	
	
	@Test
	public void testGetUwContract() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetUwContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		Integer policyId = 1063983;
		Integer growerId = 6022;
		Integer insurancePlanId = 4;
		String policyStatusCode = "ACTIVE";
		String policyNumber = "140160-21";
		String contractNumber = "140160";
		Integer contractId = 2532;
		Integer cropYear = 2021;
		
		String insurancePlanName = "GRAIN";
		String policyStatus = "ACTIVE";
		Integer growerNumber = 140160;
		String growerName = "BYZLV, NZXMF S";
		String growerPrimaryEmail = "noreply@vividsolutions.com";
		String growerPrimaryPhone = "1235556789";
		String inventoryContractGuid = "IC111157";
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
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
		UwContractRsrc fetchedResource = service.getUwContract(referrer, "false", "false", null);
		
		Assert.assertNotNull(fetchedResource);
		Assert.assertEquals(contractNumber, fetchedResource.getContractNumber());
		Assert.assertEquals(inventoryContractGuid, fetchedResource.getInventoryContractGuid());
		Assert.assertEquals(policyNumber, fetchedResource.getPolicyNumber());
		Assert.assertEquals(policyStatusCode, fetchedResource.getPolicyStatusCode());
		Assert.assertEquals(contractId, fetchedResource.getContractId());
		Assert.assertEquals(cropYear, fetchedResource.getCropYear());
		Assert.assertEquals(growerId, fetchedResource.getGrowerId());
		Assert.assertEquals(insurancePlanId, fetchedResource.getInsurancePlanId());
		Assert.assertEquals(policyId, fetchedResource.getPolicyId());
		Assert.assertEquals(growerName, fetchedResource.getGrowerName());
		Assert.assertEquals(insurancePlanName, fetchedResource.getInsurancePlanName());
		Assert.assertEquals(policyStatus, fetchedResource.getPolicyStatus());
		Assert.assertEquals(growerNumber, fetchedResource.getGrowerNumber());
		Assert.assertEquals(growerPrimaryEmail, fetchedResource.getGrowerPrimaryEmail());
		Assert.assertEquals(growerPrimaryPhone, fetchedResource.getGrowerPrimaryPhone());
		
		
		// TODO: These fields do not currently get populated for fetches.
		Assert.assertNull(fetchedResource.getGrowerAddressLine1());
		Assert.assertNull(fetchedResource.getGrowerAddressLine2());
		Assert.assertNull(fetchedResource.getGrowerCity());
		Assert.assertNull(fetchedResource.getGrowerPostalCode());
		Assert.assertNull(fetchedResource.getGrowerProvince());
		
		logger.debug(">testGetUwContract");
	}

	@Test
	public void testGetUwContractLoadLinkedPolicies() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testGetUwContractLoadLinkedPolicies");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		createGrower(growerId1, 999888, "grower name", createTransactionDate);
		createPolicy(policyId1, growerId1, 4, policyNumber1, contractNumber1, contractId1, 2020, createTransactionDate);
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

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
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);

		// Test: No linked policies exist.
		UwContractRsrc fetchedResource = service.getUwContract(referrer, "true", "false", null);
		Assert.assertNotNull(fetchedResource);
		Assert.assertEquals(policyNumber1, fetchedResource.getPolicyNumber());
		checkUwContractList(fetchedResource.getLinkedPolicies());
		
		// Test: Policies linked by same grower.
		createPolicy(policyId2, growerId1, 5, policyNumber2, contractNumber2, contractId2, 2020, createTransactionDate);

		// A. loadLinkedPolicies param is false.
		fetchedResource = service.getUwContract(referrer, "false", "false", null);
		Assert.assertNotNull(fetchedResource);
		Assert.assertEquals(policyNumber1, fetchedResource.getPolicyNumber());
		checkUwContractList(fetchedResource.getLinkedPolicies());
		
		// B. loadLinkedPolicies param is true.
		fetchedResource = service.getUwContract(referrer, "true", "false", null);
		Assert.assertNotNull(fetchedResource);
		Assert.assertEquals(policyNumber1, fetchedResource.getPolicyNumber());
		checkUwContractList(fetchedResource.getLinkedPolicies(), policyId2);
		
		service.deletePolicy(topLevelEndpoints, policyId2.toString());

		// Test: Policies linked by shared field.
		createGrower(growerId2, 999777, "grower name 2", createTransactionDate);
		createPolicy(policyId2, growerId2, 5, policyNumber2, contractNumber2, contractId2, 2020, createTransactionDate);

		createField(fieldId1, "LOT 1", 1980, null);
		createAnnualFieldDetail(annualFieldDetailId1, null, fieldId1, 2020);

		createGrowerContractYear(growerContractYearId1, contractId1, growerId1, 2020, 4, createTransactionDate);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);

		createGrowerContractYear(growerContractYearId2, contractId2, growerId2, 2020, 5, createTransactionDate);
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId1, growerContractYearId2, 1);

		fetchedResource = service.getUwContract(referrer, "true", "false", null);
		Assert.assertNotNull(fetchedResource);
		Assert.assertEquals(policyNumber1, fetchedResource.getPolicyNumber());
		checkUwContractList(fetchedResource.getLinkedPolicies(), policyId2);

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());

		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId1.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId2.toString());

		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());		
		service.deleteField(topLevelEndpoints, fieldId1.toString());

		service.deletePolicy(topLevelEndpoints, policyId2.toString());
		service.deleteGrower(topLevelEndpoints, growerId2.toString());
		
		logger.debug(">testGetUwContractLoadLinkedPolicies");
	}

	private void checkUwContractList(List<UwContractRsrc> uwContracts, Integer... expectedPolicyIds ) {

		Assert.assertNotNull(uwContracts);
		Assert.assertEquals(expectedPolicyIds.length, uwContracts.size());
				
		for ( int i = 0; i < uwContracts.size(); i++ ) {
			Assert.assertEquals(expectedPolicyIds[i], uwContracts.get(i).getPolicyId());
		}
	}

	@Test
	public void testGetUwContractLoadOtherYearPolicies() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testGetUwContractLoadOtherYearPolicies");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		createGrower(growerId2, 999777, "grower name 2", createTransactionDate);
		createPolicy(policyId2, growerId2, 4, policyNumber2, contractNumber2, contractId2, cropYear2, createTransactionDate);
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber2,
				null,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);

		// Test: No other year policies exist.
		UwContractRsrc fetchedResource = service.getUwContract(referrer, "false", "true", ScreenType.INVENTORY.name());
		Assert.assertNotNull(fetchedResource);
		Assert.assertEquals(policyNumber2, fetchedResource.getPolicyNumber());
		Assert.assertEquals(0, fetchedResource.getOtherYearPolicies().size());
		
		// Test: Other Year Policies by Inventory
		createPolicy(policyId3, growerId2, 4, policyNumber3, contractNumber3, contractId3, cropYear3, createTransactionDate);
		createGrowerContractYear(growerContractYearId3, contractId3, growerId2, cropYear3, 4, createTransactionDate);

		createField(fieldId1, "LOT 1", 1980, null);
		createAnnualFieldDetail(annualFieldDetailId1, null, fieldId1, cropYear3);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId3, 1);
		
		String invContractGuid3 = createInventoryContract(policyNumber3);

		fetchedResource = service.getUwContract(referrer, "false", "true", ScreenType.INVENTORY.name());
		Assert.assertNotNull(fetchedResource);
		Assert.assertEquals(policyNumber2, fetchedResource.getPolicyNumber());
		Assert.assertEquals(1, fetchedResource.getOtherYearPolicies().size());
		
		OtherYearPolicy oyp = fetchedResource.getOtherYearPolicies().get(0);
		Assert.assertEquals(cropYear3, oyp.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), oyp.getInsurancePlanId());
		Assert.assertEquals(policyId3, oyp.getPolicyId());
		Assert.assertEquals(policyNumber3, oyp.getPolicyNumber());
		Assert.assertEquals(invContractGuid3, oyp.getScreenRecordGuid());
		Assert.assertEquals(ScreenType.INVENTORY.name(), oyp.getScreenType());
		

		// Test: Other Year Policies by DOP
		String dopContractGuid3 = createDopYieldContract(policyNumber3);

		fetchedResource = service.getUwContract(referrer, "false", "true", ScreenType.DOP.name());
		Assert.assertNotNull(fetchedResource);
		Assert.assertEquals(policyNumber2, fetchedResource.getPolicyNumber());
		Assert.assertEquals(1, fetchedResource.getOtherYearPolicies().size());
		
		oyp = fetchedResource.getOtherYearPolicies().get(0);
		Assert.assertEquals(cropYear3, oyp.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), oyp.getInsurancePlanId());
		Assert.assertEquals(policyId3, oyp.getPolicyId());
		Assert.assertEquals(policyNumber3, oyp.getPolicyNumber());
		Assert.assertEquals(dopContractGuid3, oyp.getScreenRecordGuid());
		Assert.assertEquals(ScreenType.DOP.name(), oyp.getScreenType());

		// Test: Other Year Policies by VERIFIED
		String verContractGuid3 = createVerifiedYieldContract(policyNumber3);

		fetchedResource = service.getUwContract(referrer, "false", "true", ScreenType.VERIFIED.name());
		Assert.assertNotNull(fetchedResource);
		Assert.assertEquals(policyNumber2, fetchedResource.getPolicyNumber());
		Assert.assertEquals(1, fetchedResource.getOtherYearPolicies().size());
		
		oyp = fetchedResource.getOtherYearPolicies().get(0);
		Assert.assertEquals(cropYear3, oyp.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), oyp.getInsurancePlanId());
		Assert.assertEquals(policyId3, oyp.getPolicyId());
		Assert.assertEquals(policyNumber3, oyp.getPolicyNumber());
		Assert.assertEquals(verContractGuid3, oyp.getScreenRecordGuid());
		Assert.assertEquals(ScreenType.VERIFIED.name(), oyp.getScreenType());
		
		// Test: Other Year Policies exist, but not loaded.
		fetchedResource = service.getUwContract(referrer, "false", "false", null);
		Assert.assertNotNull(fetchedResource);
		Assert.assertEquals(policyNumber2, fetchedResource.getPolicyNumber());
		Assert.assertEquals(0, fetchedResource.getOtherYearPolicies().size());
		
		// Test: Load other year policies, but invalid screen type.
		try {
			fetchedResource = service.getUwContract(referrer, "false", "true", "NOSUCHTYPE");
			Assert.fail("Invalid screen type did not throw exception");
		} catch ( CirrasUnderwritingServiceException e) {
			// Ok.
		}

		logger.debug(">testGetUwContractLoadOtherYearPolicies");
	}

	
	@Test
	public void testUwContractDopEligibleInventory() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testUwContractDopEligibleInventory");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		String policyNumber = "140160-23";
		Integer eligibleInventory =3;
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
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

		Assert.assertEquals(policyNumber, referrer.getPolicyNumber());
		Assert.assertEquals("Eligible Inventory 1", eligibleInventory, referrer.getTotalDopEligibleInventory());

		UwContractRsrc fetchedResource = service.getUwContract(referrer, "false", "false", null);
		
		Assert.assertNotNull(fetchedResource);
		Assert.assertEquals(policyNumber, fetchedResource.getPolicyNumber());
		Assert.assertEquals("Eligible Inventory 2", eligibleInventory, fetchedResource.getTotalDopEligibleInventory());
		
		logger.debug(">testUwContractDopEligibleInventory");
	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
		
}
