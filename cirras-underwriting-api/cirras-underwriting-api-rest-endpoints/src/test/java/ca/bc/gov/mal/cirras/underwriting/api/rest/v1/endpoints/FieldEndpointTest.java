package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;


public class FieldEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(FieldEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_LAND,
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.GET_INVENTORY_CONTRACT,
		Scopes.UPDATE_INVENTORY_CONTRACT,
		Scopes.SEARCH_ANNUAL_FIELDS,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.DELETE_INVENTORY_CONTRACT,
		Scopes.CREATE_SYNC_UNDERWRITING,
		Scopes.GET_GROWER,
		Scopes.GET_POLICY,
		Scopes.GET_LEGAL_LAND		
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer contractedFieldDetailId = 999999999;
	private Integer annualFieldDetailId = 999999996;
	private Integer legalLandId = 999999998;
	private Integer fieldId = 999999997;

	private Integer growerId1 = 90001501;
	private Integer growerNumber1 = 999888;
	private Integer policyId1 = 90001502;
	private Integer gcyId1 = 90001503;
	private Integer contractId1 = 90001504;
	private String policyNumber1 = "998874-21";
	private String contractNumber1 = "998874";
	private Integer cropYear1 = 2021;

	private Integer policyId2 = 90002502;
	private Integer gcyId2 = 90002503;
	private Integer contractId2 = 90002504;
	private String policyNumber2 = "998815-21";
	private String contractNumber2 = "998815";
	private Integer contractedFieldDetailId2 = 90001518;

	
	
	
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
		deleteInventoryContract(policyNumber2);
		
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());

		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId.toString());

		service.deleteField(topLevelEndpoints, fieldId.toString());
		
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());

		service.deleteGrowerContractYear(topLevelEndpoints, gcyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId1.toString());

		service.deleteGrowerContractYear(topLevelEndpoints, gcyId2.toString());
		service.deletePolicy(topLevelEndpoints, policyId2.toString());

		service.deleteGrower(topLevelEndpoints, growerId1.toString());
	}

	private void deleteInventoryContract(String policyNumber) throws CirrasUnderwritingServiceException {
		
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
	
	
	
	
	@Test
	public void testCreateUpdateDeleteField() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateField");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String fieldLabel = "Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;

		//CREATE Field
		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel(fieldLabel);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);

		service.synchronizeField(resource);
		
		FieldRsrc fetchedResource = service.getField(topLevelEndpoints, fieldId.toString()); 

		Assert.assertEquals("FieldId 1", resource.getFieldId(), fetchedResource.getFieldId());
		Assert.assertEquals("Field Label 1", resource.getFieldLabel(), fetchedResource.getFieldLabel());
		Assert.assertEquals("ActiveFromCropYear 1", resource.getActiveFromCropYear(), fetchedResource.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear 1", resource.getActiveToCropYear(), fetchedResource.getActiveToCropYear());
		
		//UPDATE CODE
		fieldLabel = "Field Label 2";
		activeFromCropYear = 2000;
		activeToCropYear = 2011;

		fetchedResource.setFieldLabel(fieldLabel);
		fetchedResource.setActiveFromCropYear(activeFromCropYear);
		fetchedResource.setActiveToCropYear(activeToCropYear);
		fetchedResource.setTransactionType(LandManagementEventTypes.FieldUpdated);

		service.synchronizeField(fetchedResource);
		
		FieldRsrc updatedResource = service.getField(topLevelEndpoints, fieldId.toString()); 

		Assert.assertEquals("FieldId 2", fetchedResource.getFieldId(), updatedResource.getFieldId());
		Assert.assertEquals("Field label 2", fetchedResource.getFieldLabel(), updatedResource.getFieldLabel());
		Assert.assertEquals("ActiveFromCropYear 2", fetchedResource.getActiveFromCropYear(), updatedResource.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear 2", fetchedResource.getActiveToCropYear(), updatedResource.getActiveToCropYear());
		
		//CLEAN UP: DELETE CODE
		delete();
		
		logger.debug(">testCreateUpdateField");
	}

	
	@Test
	public void testUpdateFieldWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateFieldWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		String fieldLabel = "Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;

		//CREATE Field
		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel(fieldLabel);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		
		
		//TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXPECTED)
		resource.setTransactionType(LandManagementEventTypes.FieldDeleted);
		service.synchronizeField(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(LandManagementEventTypes.FieldUpdated);

		service.synchronizeField(resource);

		FieldRsrc fetchedResource = service.getField(topLevelEndpoints, fieldId.toString()); 

		Assert.assertEquals("FieldId 1", resource.getFieldId(), fetchedResource.getFieldId());
		Assert.assertEquals("Field Label 1", resource.getFieldLabel(), fetchedResource.getFieldLabel());
		Assert.assertEquals("ActiveFromCropYear 1", resource.getActiveFromCropYear(), fetchedResource.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear 1", resource.getActiveToCropYear(), fetchedResource.getActiveToCropYear());

		//UPDATE Field
		fieldLabel = "Field Label 2";
		activeFromCropYear = 2000;
		activeToCropYear = 2011;

		fetchedResource.setFieldLabel(fieldLabel);
		fetchedResource.setActiveFromCropYear(activeFromCropYear);
		fetchedResource.setActiveToCropYear(activeToCropYear);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		fetchedResource.setTransactionType(LandManagementEventTypes.FieldCreated);
		service.synchronizeField(fetchedResource);
		
		FieldRsrc updatedResource = service.getField(topLevelEndpoints, fieldId.toString()); 

		Assert.assertEquals("FieldId 2", fetchedResource.getFieldId(), updatedResource.getFieldId());
		Assert.assertEquals("Field Label 2", fetchedResource.getFieldLabel(), updatedResource.getFieldLabel());
		Assert.assertEquals("ActiveFromCropYear 2", fetchedResource.getActiveFromCropYear(), updatedResource.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear 2", fetchedResource.getActiveToCropYear(), updatedResource.getActiveToCropYear());

		//CLEAN UP: DELETE CODE
		delete();		
		
		logger.debug(">testUpdateFieldWithoutRecordNoUpdate");
	}
	
	@Test
	public void testDeleteFieldWithInventoryRecords() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testDeleteFieldWithInventoryRecords");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 because they are not set in the database
		Date transactionDate = cal.getTime();

		//CREATE Legal Land
		LegalLandRsrc legalLandResource = createLegalLand( legalLandId, "OTHER", "Legal Description", "Short Legal",  "Other Description", 2011, 2022);
		service.synchronizeLegalLand(legalLandResource);
								
		//CREATE Field
		FieldRsrc fieldResource = createField( fieldId, "Field Label", 2011, 2022);
		service.synchronizeField(fieldResource);
		
		//Needs to be a gcy that has a inventory contract
		Integer localGrowerContractYearId = 95128;
		
		//CREATE Annual Field Detail
		AnnualFieldDetailRsrc annualFieldDetailResource = createAnnualFieldDetail(annualFieldDetailId, legalLandId, fieldId, 2022);
		service.synchronizeAnnualFieldDetail(annualFieldDetailResource);

		//CREATE Contracted Field Detail
		ContractedFieldDetailRsrc contractedFieldDetailResource = createContractedFieldDetail( contractedFieldDetailId, annualFieldDetailId, localGrowerContractYearId, 1);
		service.synchronizeContractedFieldDetail(contractedFieldDetailResource);
		
		//Add a seeded grain record
		addSeededGrain(transactionDate);
		
		//Check records before deleting
		checkRecords(false);

		//CLEAN UP: DELETE CODE
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId.toString());
		service.deleteField(topLevelEndpoints, fieldId.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());
		
		//Check records after deleting
		checkRecords(true);
		
		logger.debug(">testDeleteFieldWithInventoryRecords");
	}

	@Test
	public void testDeleteFieldWithLinkedPlantings() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testDeleteFieldWithLinkedPlantings");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		GrowerRsrc growerResource = createGrower(growerId1, growerNumber1);
		service.synchronizeGrower(growerResource);		
		
		//Grain policy
		PolicyRsrc policyResource = createPolicy(4, policyId1, policyNumber1, contractNumber1, contractId1, growerId1, cropYear1);
		service.synchronizePolicy(policyResource);

		GrowerContractYearSyncRsrc gcyResource = createGrowerContractYear(4, gcyId1, contractId1, growerId1, cropYear1);
		service.synchronizeGrowerContractYear(gcyResource);

		//Forage policy
		policyResource = createPolicy(5, policyId2, policyNumber2, contractNumber2, contractId2, growerId1, cropYear1);
		service.synchronizePolicy(policyResource);

		gcyResource = createGrowerContractYear(5, gcyId2, contractId2, growerId1, cropYear1);
		service.synchronizeGrowerContractYear(gcyResource);

		LegalLandRsrc legalLandResource = createLegalLand(legalLandId, "OTHER", "Legal Description", "Short Legal", "Other Description", 1980, null);
		service.synchronizeLegalLand(legalLandResource);

		FieldRsrc fieldResource = createField(fieldId, "Field Label", 1980, null);
		service.synchronizeField(fieldResource);

		AnnualFieldDetailRsrc annualFieldDetailResource = createAnnualFieldDetail(annualFieldDetailId, legalLandId, fieldId, cropYear1);
		service.synchronizeAnnualFieldDetail(annualFieldDetailResource);

		ContractedFieldDetailRsrc contractedFieldDetailResource = createContractedFieldDetail(contractedFieldDetailId, annualFieldDetailId, gcyId1, 1);
		service.synchronizeContractedFieldDetail(contractedFieldDetailResource);

		contractedFieldDetailResource = createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId, gcyId2, 1);
		service.synchronizeContractedFieldDetail(contractedFieldDetailResource);

		// Create GRAIN Inventory Contract.
		UwContractRsrc grainUwContract = getUwContract(policyNumber1);
		Assert.assertNull(grainUwContract.getInventoryContractGuid());
		InventoryContractRsrc grainInvContract = service.rolloverInventoryContract(grainUwContract);
		grainInvContract = service.createInventoryContract(topLevelEndpoints, grainInvContract);
		
		String grainInventoryFieldGuid = grainInvContract.getFields().get(0).getPlantings().get(0).getInventoryFieldGuid();
				
		// Create FORAGE Inventory Contract.
		UwContractRsrc forageUwContract = getUwContract(policyNumber2);
		Assert.assertNull(forageUwContract.getInventoryContractGuid());
		InventoryContractRsrc forageInvContract = service.rolloverInventoryContract(forageUwContract);

		InventorySeededForage isf = forageInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages().get(0);
		isf.setGrainInventoryFieldGuid(grainInventoryFieldGuid);
		isf.setIsAwpEligibleInd(false);
		isf.setIsIrrigatedInd(false);
		isf.setIsQuantityInsurableInd(true);
		isf.setLinkPlantingType(InventoryServiceEnums.LinkPlantingType.ADD_LINK.toString());
		
		forageInvContract = service.createInventoryContract(topLevelEndpoints, forageInvContract);
		String underseededInventorySeededForageGuid = forageInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages().get(0).getInventorySeededForageGuid();
		
		// Confirm plantings are linked.
		grainUwContract = getUwContract(policyNumber1);
		Assert.assertNotNull(grainUwContract.getInventoryContractGuid());
		grainInvContract = service.getInventoryContract(grainUwContract);
		Assert.assertEquals(underseededInventorySeededForageGuid, grainInvContract.getFields().get(0).getPlantings().get(0).getUnderseededInventorySeededForageGuid());

		// Test: Delete Field. If it crashes, then linked plantings were not properly unlinked first.
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId.toString());
		service.deleteField(topLevelEndpoints, fieldId.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());

		// Check inventory contracts.
		grainInvContract = service.getInventoryContract(grainUwContract);
		Assert.assertEquals(0,  grainInvContract.getFields().size());

		forageUwContract = getUwContract(policyNumber2);
		Assert.assertNotNull(forageUwContract.getInventoryContractGuid());
		forageInvContract = service.getInventoryContract(forageUwContract);
		Assert.assertEquals(0,  forageInvContract.getFields().size());
		
		logger.debug(">testDeleteFieldWithLinkedPlantings");
	}
	
	
	private void checkRecords(Boolean isNull) throws CirrasUnderwritingServiceException {
		//Get inventory contract
		InventoryContractRsrc invContract = getInventoryContract(service);
		
		//Get field
		List<AnnualFieldRsrc> fields = invContract.getFields().stream()
				.filter(x -> x.getFieldId().equals(fieldId)) 
				.collect(Collectors.toList());
		
		if(isNull) {
			Assert.assertTrue("Field Null", (fields == null || fields.size() == 0));
		} else {

			Assert.assertTrue("Field Not Null", (fields != null && fields.size() > 0));
			
			InventoryField planting = fields.get(0).getPlantings().get(0);
			//check if all records are there
			//Inventory Field
			Assert.assertNotNull("Inventory Field Not Null", planting);
			Assert.assertEquals("Field Id: ", fieldId, planting.getFieldId());
			
			//Unseeded
			Assert.assertNotNull("Inventory Unseeded Not Null", planting.getInventoryUnseeded());
			
			//Seeded
			Assert.assertNotNull("Inventory Seeded Not Null", planting.getInventorySeededGrains());

		}
	}

	private void addSeededGrain(Date transactionDate) throws CirrasUnderwritingServiceException, ValidationException {

		//Get inventory contract
		InventoryContractRsrc invContract = getInventoryContract(service);
		
		//Get field
		List<AnnualFieldRsrc> fields = invContract.getFields().stream()
				.filter(x -> x.getFieldId().equals(fieldId)) 
				.collect(Collectors.toList());
		
		InventoryField planting = fields.get(0).getPlantings().get(0);
		//InventorySeededGrain
		InventorySeededGrain invSeededGrain = new InventorySeededGrain();

		invSeededGrain.setCommodityTypeCode("CPSW");
		invSeededGrain.setCommodityTypeDesc("Canadian Prairie Spring Wheat");
		invSeededGrain.setCropCommodityId(26);
		invSeededGrain.setCropCommodityName("WHEAT");
		invSeededGrain.setCropVarietyId(1010602);
		invSeededGrain.setCropVarietyName("AAC ENTICE");
		invSeededGrain.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		invSeededGrain.setInventorySeededGrainGuid(null);
		invSeededGrain.setIsPedigreeInd(true);
		invSeededGrain.setIsSpotLossInsurableInd(true);
		invSeededGrain.setIsQuantityInsurableInd(true);
		invSeededGrain.setIsReplacedInd(false);
		invSeededGrain.setSeededAcres(12.34);
		invSeededGrain.setSeededDate(transactionDate);
		
		List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
		seededGrains.add(invSeededGrain);
		planting.setInventorySeededGrains(seededGrains);

		planting.setInventorySeededGrains(seededGrains);
		
		service.updateInventoryContract(invContract.getInventoryContractGuid(), invContract);

	}	
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	
	private InventoryContractRsrc getInventoryContract(CirrasUnderwritingService service)
			throws CirrasUnderwritingServiceException {
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				"2022", 
				null, 
				null,
				null,
				"140913-22",  //Needs to be contract with inventory
				null,
				null, 
				null, 
				null, 
				1, 20);

		Assert.assertNotNull("searchResults: ", searchResults);
		Assert.assertTrue("searchResults.getCollection().size(): ", searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);

		InventoryContractRsrc invContract = service.getInventoryContract(uwContract);
		
		return invContract;
	}

	private UwContractRsrc getUwContract(String policyNumber) throws CirrasUnderwritingServiceException {

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
				pageNumber, 
				pageRowCount);
		
		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() == 1);
		
		UwContractRsrc uwContract = searchResults.getCollection().get(0);
		return uwContract;
	}
	
	
	private static LegalLandRsrc createLegalLand(Integer legalLandId, 
			String primaryReferenceTypeCode, String legalDescription, String legalShortDescription, String otherDescription,
			Integer activeFromCropYear, Integer activeToCropYear) {

		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(legalLandId);
		resource.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		resource.setLegalDescription(legalDescription);
		resource.setLegalShortDescription(legalShortDescription);
		resource.setOtherDescription(otherDescription);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		resource.setPrimaryLandIdentifierTypeCode(null);
		resource.setPrimaryPropertyIdentifier(null);
		resource.setTotalAcres(null);
		resource.setTransactionType(LandManagementEventTypes.LegalLandCreated);
		
		return resource;
	}
	
	private static FieldRsrc createField( Integer fieldId, String fieldLabel, Integer activeFromCropYear, Integer activeToCropYear) {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel(fieldLabel);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
				
		return resource;
	}
	
	
	private static AnnualFieldDetailRsrc createAnnualFieldDetail( Integer annualFieldDetailId, Integer legalLandId, Integer fieldId, Integer cropYear) {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setLegalLandId(legalLandId);
		resource.setFieldId(fieldId);
		resource.setCropYear(cropYear);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		return resource;
	}
	
	private static ContractedFieldDetailRsrc createContractedFieldDetail( 
			Integer contractedFieldDetailId, 
			Integer annualFieldDetailId, 
			Integer growerContractYearId, 
			Integer displayOrder) {

		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId);
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setGrowerContractYearId(growerContractYearId);
		resource.setDisplayOrder(displayOrder);
		resource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailCreated);
		
		return resource;
	}

	private static GrowerRsrc createGrower(Integer growerId, Integer growerNumber) throws ValidationException, CirrasUnderwritingServiceException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		GrowerRsrc resource = new GrowerRsrc();
		
		resource.setGrowerId(growerId);
		resource.setGrowerNumber(growerNumber);
		resource.setGrowerName("grower test name");
		resource.setGrowerAddressLine1("address line 1");
		resource.setGrowerAddressLine2("address line 2");
		resource.setGrowerPostalCode("V8P 4N8");
		resource.setGrowerCity("Victoria");
		resource.setCityId(1);
		resource.setGrowerProvince("BC");
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerCreated);
		
		return resource;		
	}

	private static PolicyRsrc createPolicy(Integer insurancePlanId, Integer policyId, String policyNumber, String contractNumber, Integer contractId, Integer growerId, Integer cropYear) throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

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

		return resource;
	}
	
	private static GrowerContractYearSyncRsrc createGrowerContractYear(Integer insurancePlanId, Integer gcyId, Integer contractId, Integer growerId, Integer cropYear) throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(gcyId);
		resource.setContractId(contractId);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setCropYear(cropYear);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearCreated);

		return resource;
	}
	
}
