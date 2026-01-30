package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandFieldXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandUpdateTypes;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class InventoryContractEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(InventoryContractEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.SEARCH_ANNUAL_FIELDS,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.DELETE_INVENTORY_CONTRACT,
		Scopes.GET_INVENTORY_CONTRACT,
		Scopes.UPDATE_INVENTORY_CONTRACT,
		Scopes.GET_LAND,
		Scopes.DELETE_COMMENTS,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
	};
	
	private static final String[] SCOPESUSER2 = {
			Scopes.GET_TOP_LEVEL, 
			Scopes.SEARCH_UWCONTRACTS,
			Scopes.SEARCH_ANNUAL_FIELDS,
			Scopes.CREATE_INVENTORY_CONTRACT,
			Scopes.DELETE_INVENTORY_CONTRACT,
			Scopes.GET_INVENTORY_CONTRACT,
			Scopes.UPDATE_INVENTORY_CONTRACT,
			Scopes.GET_LAND
		};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private CirrasUnderwritingService serviceUser2;
	private EndpointsRsrc topLevelEndpointsUser2;
	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException, ValidationException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		serviceUser2 = getServiceSecondUser(SCOPESUSER2);
		topLevelEndpointsUser2 = serviceUser2.getTopLevelEndpoints();

		deleteInventoryContract();
	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException, ValidationException {
		deleteInventoryContract();
	}
	
	//private void deleteInventoryContract(InventoryContractRsrc invContract) {
	private void deleteInventoryContract() throws CirrasUnderwritingServiceException, ValidationException {
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
				if ( invContract != null ) {
					//1. Delete comments
					Boolean deleteComments = false;

					for ( int i = 0; i < invContract.getFields().size(); i++) {
						AnnualFieldRsrc field = invContract.getFields().get(i);
					
						for ( int j = 0; j < field.getUwComments().size(); j++) {
							field.getUwComments().get(j).setDeletedByUserInd(true);
							deleteComments = true;
						}
					}	

					if(Boolean.TRUE.equals(deleteComments)) {
						invContract = service.updateInventoryContract(invContract.getInventoryContractGuid(), invContract);
					}
					
					//2. Delete inventory contract
					service.deleteInventoryContract(invContract);
				}
			}
			
		}

	}
	

	// TODO: Currently runs a fairly simple test expecting specific test data to already exist.
	//       Once the Update and Delete endpoints are implemented, this test should be updated 
	//       to use data created within this test, and to check all fields instead of just guids.
	@Test
	public void testGetInventoryContract() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetInventoryContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		String cropYear = null;
		String insurancePlanId = null;
		String officeId = null;
		String policyStatusCode = null;
		String policyNumber = "142448-21"; //"140160-21";
		String growerInfo = null;
		
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				officeId,
				policyStatusCode,
				policyNumber,
				growerInfo,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);

		InventoryContractRsrc invContract = service.getInventoryContract(uwContract);
		
		Assert.assertNotNull(invContract);
		Assert.assertEquals("26a188c7e0424f3abc671709ed5960bd", invContract.getInventoryContractGuid());
		Assert.assertEquals(2608, invContract.getContractId().intValue());
		Assert.assertEquals(2021, invContract.getCropYear().intValue());
		
		List<InventoryContractCommodity> commodities = invContract.getCommodities();
		Assert.assertEquals(2, commodities.size());
		Assert.assertEquals("e23325c5ce0d46cea3e05eab87af30e7", commodities.get(0).getInventoryContractCommodityGuid());
		Assert.assertEquals("8f535f384a9744b187bfbdcd3599424d", commodities.get(1).getInventoryContractCommodityGuid());
		
		List<AnnualFieldRsrc> fields = invContract.getFields();
		Assert.assertEquals(8, fields.size());
		Assert.assertEquals(134438, fields.get(0).getContractedFieldDetailId().intValue());
		Assert.assertEquals(134439, fields.get(1).getContractedFieldDetailId().intValue());
		Assert.assertEquals(134440, fields.get(2).getContractedFieldDetailId().intValue());
		Assert.assertEquals(134441, fields.get(3).getContractedFieldDetailId().intValue());
		Assert.assertEquals(134442, fields.get(4).getContractedFieldDetailId().intValue());
		Assert.assertEquals(134443, fields.get(5).getContractedFieldDetailId().intValue());
		Assert.assertEquals(134444, fields.get(6).getContractedFieldDetailId().intValue());
		Assert.assertEquals(134445, fields.get(7).getContractedFieldDetailId().intValue());
		
		List<InventoryField> plantings = fields.get(0).getPlantings();
		Assert.assertEquals(2, plantings.size());
		Assert.assertEquals("5b7fff68156d44fa9a96f059816d6ea7", plantings.get(0).getInventoryFieldGuid());
		Assert.assertEquals("4ade4dbc1ffe4ddf99134d4a7dcbf17f", plantings.get(0).getInventoryUnseeded().getInventoryUnseededGuid());
		Assert.assertEquals("7e793cda74a34fa591a61bc28fbfed7a", plantings.get(1).getInventoryFieldGuid());
		Assert.assertEquals("5800a8e29b6849d4ab669419022e919d", plantings.get(1).getInventoryUnseeded().getInventoryUnseededGuid());
		
		logger.debug(">testGetInventoryContract");
	}
	
	
	@Test
	public void testUpdateInventoryContractDebug() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testUpdateInventoryContractDebug");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		String cropYear = null;
		String insurancePlanId = null;
		String officeId = null;
		String policyStatusCode = null;
		String policyNumber = "141879-21";
		String growerInfo = null;
		
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				officeId,
				policyStatusCode,
				policyNumber,
				growerInfo,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);

		InventoryContractRsrc invContract = service.getInventoryContract(uwContract);
		
		Assert.assertNotNull(invContract);

		InventoryContractRsrc updatedInvContract = service.updateInventoryContract(invContract.getInventoryContractGuid(), invContract);

		Assert.assertNotNull(updatedInvContract);
		
		logger.debug(">testUpdateInventoryContractDebug");
	}
	
	private Integer contractId = 2541;
	private Integer cropYear = 2020;
	private Integer growerContractYearId = 89008;
	private String policyNumber = "140467-20";// "712325-22";  //"140467-20";

	
	private InventoryContractRsrc rolloverInventoryContract(CirrasUnderwritingService service, EndpointsRsrc topLevelEndpoints
			) throws CirrasUnderwritingServiceException {
		
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

		InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);

		return invContract;
	}
	
	private InventoryContractRsrc getInventoryContract(CirrasUnderwritingService service, EndpointsRsrc topLevelEndpoints
			) throws CirrasUnderwritingServiceException {
		
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

		InventoryContractRsrc invContract = service.getInventoryContract(referrer);
		Assert.assertNotNull(invContract);

		return invContract;
	}

	// TODO: Currently creates an InventoryContract and leaves it behind because delete hasn't been implemented yet.
	@Test
	public void testCreateInventoryContract() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
		logger.debug("<testCreateInventoryContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//------------------------------------------
		//
		// Create test data.
		//
		//------------------------------------------
		
		//Rollover contract 
		InventoryContractRsrc invContract = rolloverInventoryContract(service, topLevelEndpoints);
		
		//Needs to be null on rollover
		Assert.assertNull("Rollover GrainFromPrevYearInd NULL", invContract.getGrainFromPrevYearInd());

		invContract.getCommodities().add(createNewInvCommodities(16, "BARLEY", 12.34, 40.0, 23.45));

		createNewPlantingsUpdateFields(invContract);
		
		updateNewInventoryContract(invContract);

		//------------------------------------------
		//
		// Insert Inventory Contract
		//
		//------------------------------------------
		
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		//------------------------------------------
		//
		// Check Inventory Contract.
		//
		//------------------------------------------
		
		// InventoryContract
		checkInventoryContract(invContract, fetchedInvContract);

		// InventoryContractCommodity
		List<InventoryContractCommodity> fetchedCommodities = fetchedInvContract.getCommodities();
		Assert.assertEquals(invContract.getCommodities().size(), fetchedCommodities.size());

		for ( int i = 0; i < invContract.getCommodities().size(); i++) {
			checkInventoryContractCommodity(invContract.getCommodities().get(i), fetchedCommodities.get(i));
		}

		// AnnualField
		checkFieldsAndPlantings(invContract.getFields(), fetchedInvContract);
		
		deleteInventoryContract();
		
		logger.debug(">testCreateInventoryContract");
	}
	
	// TODO: Currently creates an InventoryContract, updates it and leaves it behind because delete hasn't been implemented yet.
	@Test
	public void testUpdateInventoryContract() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
		logger.debug("<testUpdateInventoryContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//------------------------------------------
		//
		// Create test data.
		//
		//------------------------------------------
		
		//Rollover contract
		InventoryContractRsrc invContract = rolloverInventoryContract(service, topLevelEndpoints);

		invContract.getCommodities().add(createNewInvCommodities(16, "BARLEY", 12.34, 40.0, 23.45));

		createNewPlantingsUpdateFields(invContract);
		
		updateNewInventoryContract(invContract);

		//------------------------------------------
		//
		// Insert Inventory Contract
		//
		//------------------------------------------
		
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		//------------------------------------------
		//
		// Check Inserted Inventory Contract.
		//
		//------------------------------------------
		
		// InventoryContract
		checkInventoryContract(invContract, fetchedInvContract);

		// InventoryContractCommodity
		List<InventoryContractCommodity> fetchedCommodities = fetchedInvContract.getCommodities();
		Assert.assertEquals(invContract.getCommodities().size(), fetchedCommodities.size());

		for ( int i = 0; i < invContract.getCommodities().size(); i++) {
			checkInventoryContractCommodity(invContract.getCommodities().get(i), fetchedCommodities.get(i));
		}

		// AnnualField
		checkFieldsAndPlantings(invContract.getFields(), fetchedInvContract);
		
		
		//------------------------------------------
		//
		// Update test data.
		//
		//------------------------------------------
		replaceInventoryCommodityData(fetchedInvContract);
		updatePlantingsAndFieldsData(fetchedInvContract);

		updateInventoryContractData(service, topLevelEndpoints, fetchedInvContract.getCommodities(), fetchedInvContract.getFields(), fetchedInvContract);
		
		//------------------------------------------
		//
		// Update Inventory Contract
		//
		//------------------------------------------
		
		InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

		//
		// Check Updated Inventory Contract.
		//
		//------------------------------------------
		
		// InventoryContract
		checkInventoryContract(fetchedInvContract, updatedInvContract);

		// InventoryContractCommodity
		List<InventoryContractCommodity> updatedCommodities = updatedInvContract.getCommodities();
		Assert.assertEquals(fetchedInvContract.getCommodities().size(), updatedCommodities.size());

		for ( int i = 0; i < fetchedInvContract.getCommodities().size(); i++) {
			checkInventoryContractCommodity(fetchedInvContract.getCommodities().get(i), updatedCommodities.get(i));
		}

		// AnnualField
		checkFieldsAndPlantings(fetchedInvContract.getFields(), updatedInvContract);
		
		deleteInventoryContract();

		logger.debug(">testUpdateInventoryContract");
	}
	
	
	// TODO: Currently creates an InventoryContract, updates it and leaves it behind because delete hasn't been implemented yet.
		@Test
		public void testUpdateExistingInventoryAndAddNewCropCommodity() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
			logger.debug("<testUpdateExistingInventoryAndAddNewCropCommodity");
			
			if(skipTests) {
				logger.warn("Skipping tests");
				return;
			}

			CirrasUnderwritingService service = getService(SCOPES);

			EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

			//------------------------------------------
			//
			// Create test data.
			//
			//------------------------------------------

			//Rollover contract
			InventoryContractRsrc invContract = rolloverInventoryContract(service, topLevelEndpoints);

			invContract.getCommodities().add(createNewInvCommodities(16, "BARLEY", 12.34, 40.0, 23.45));

			createNewPlantingsUpdateFields(invContract);
			
			updateNewInventoryContract(invContract);

			//------------------------------------------
			//
			// Insert Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

			//------------------------------------------
			//
			// Check Inserted Inventory Contract.
			//
			//------------------------------------------
			
			// InventoryContract
			checkInventoryContract(invContract, fetchedInvContract);

			// InventoryContractCommodity
			List<InventoryContractCommodity> fetchedCommodities = fetchedInvContract.getCommodities();
			Assert.assertEquals(invContract.getCommodities().size(), fetchedCommodities.size());

			for ( int i = 0; i < invContract.getCommodities().size(); i++) {
				checkInventoryContractCommodity(invContract.getCommodities().get(i), fetchedCommodities.get(i));
			}

			// AnnualField
			checkFieldsAndPlantings(invContract.getFields(), fetchedInvContract);
			
			
			//------------------------------------------
			//
			// Update test data.
			//
			//------------------------------------------
			updateInventoryCommodityData(fetchedInvContract);
			//Add a new crop commodity
			fetchedInvContract.getCommodities().add(createNewInvCommodities(18, "CANOLA", 12.34, 8.22, 8.22));
			fetchedInvContract.getFields().get(0).getPlantings().add(createAdditionalPlanting(fetchedInvContract.getFields().get(0).getFieldId(), 2));


			updateInventoryContractData(service, topLevelEndpoints, fetchedInvContract.getCommodities(), fetchedInvContract.getFields(), fetchedInvContract);
			
			//------------------------------------------
			//
			// Update Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

			//
			// Check Updated Inventory Contract.
			//
			//------------------------------------------
			
			// InventoryContract
			checkInventoryContract(fetchedInvContract, updatedInvContract);

			// InventoryContractCommodity
			List<InventoryContractCommodity> updatedCommodities = updatedInvContract.getCommodities();
			Assert.assertEquals(fetchedInvContract.getCommodities().size(), updatedCommodities.size());

			for ( int i = 0; i < fetchedInvContract.getCommodities().size(); i++) {
				checkInventoryContractCommodity(fetchedInvContract.getCommodities().get(i), updatedCommodities.get(i));
			}

			// AnnualField
			checkFieldsAndPlantings(fetchedInvContract.getFields(), updatedInvContract);
			
			deleteInventoryContract();
			
			logger.debug(">testUpdateExistingInventoryAndAddNewCropCommodity");
		}

		// TODO: Currently creates an InventoryContract, updates it and leaves it behind because delete hasn't been implemented yet.
		@Test
		public void testAddPlantingToExistingInventoryContract() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
			logger.debug("<testAddPlantingToExistingInventoryContract");
			
			if(skipTests) {
				logger.warn("Skipping tests");
				return;
			}

			CirrasUnderwritingService service = getService(SCOPES);

			EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

			//------------------------------------------
			//
			// Create test data.
			//
			//------------------------------------------

			//Rollover contract
			InventoryContractRsrc invContract = rolloverInventoryContract(service, topLevelEndpoints);

			invContract.getCommodities().add(createNewInvCommodities(16, "BARLEY", 12.34, 40.0, 23.45));

			createNewPlantingsUpdateFields(invContract);
			
			updateNewInventoryContract(invContract);

			//------------------------------------------
			//
			// Insert Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

			//------------------------------------------
			//
			// Check Inserted Inventory Contract.
			//
			//------------------------------------------
			
			// InventoryContract
			checkInventoryContract(invContract, fetchedInvContract);

			// InventoryContractCommodity
			List<InventoryContractCommodity> fetchedCommodities = fetchedInvContract.getCommodities();
			Assert.assertEquals("Commodity size 1: ", invContract.getCommodities().size(), fetchedCommodities.size());

			for ( int i = 0; i < invContract.getCommodities().size(); i++) {
				checkInventoryContractCommodity(invContract.getCommodities().get(i), fetchedCommodities.get(i));
			}

			// AnnualField
			checkFieldsAndPlantings(invContract.getFields(), fetchedInvContract);
			
			
			//------------------------------------------
			//
			// Update test data.
			//
			//------------------------------------------
			//Add second planting
			fetchedInvContract.getCommodities().add(createNewInvCommodities(18, "CANOLA", 12.34, 8.22, 8.22));
			fetchedInvContract.getFields().get(0).getPlantings().add(createAdditionalPlanting(fetchedInvContract.getFields().get(0).getFieldId(), 2));

			updateInventoryContractData(service, topLevelEndpoints, fetchedInvContract.getCommodities(), fetchedInvContract.getFields(), fetchedInvContract);
			
			//------------------------------------------
			//
			// Update Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

			//
			// Check Updated Inventory Contract.
			//
			//------------------------------------------
			
			// InventoryContract
			checkInventoryContract(fetchedInvContract, updatedInvContract);

			// InventoryContractCommodity
			List<InventoryContractCommodity> updatedCommodities = updatedInvContract.getCommodities();
			Assert.assertEquals("Commodity size 2: ", fetchedInvContract.getCommodities().size(), updatedCommodities.size());

			for ( int i = 0; i < fetchedInvContract.getCommodities().size(); i++) {
				checkInventoryContractCommodity(fetchedInvContract.getCommodities().get(i), updatedCommodities.get(i));
			}

			// AnnualField
			checkFieldsAndPlantings(fetchedInvContract.getFields(), updatedInvContract);
			
			deleteInventoryContract();
			
			logger.debug(">testAddPlantingToExistingInventoryContract");
		}

		@Test
		public void testRecalculateCropCommodity() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
			logger.debug("<testRecalculateCropCommodity");
			
			if(skipTests) {
				logger.warn("Skipping tests");
				return;
			}

			CirrasUnderwritingService service = getService(SCOPES);

			EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

			//------------------------------------------
			//
			// Create test data.
			//
			//------------------------------------------

			//Rollover contract
			InventoryContractRsrc invContract = rolloverInventoryContract(service, topLevelEndpoints);

			invContract.getCommodities().add(createNewInvCommodities(16, "BARLEY", 12.34, 40.0, 23.45));
			invContract.getCommodities().add(createNewInvCommodities(24, "OAT", 0.0, 0.0, 0.0));

			createNewPlantingsUpdateFields(invContract);
			
			//Add Forage plantings (adding it to first field despite the method name)
			//Don't expect any rollup to contract level
			invContract.getFields().get(0).getPlantings().add(createNewPlantingOnSecondField(1010893, "FORAGE SEED", 1010998, "TIMOTHY", 50.00, 2, true, true, invContract.getFields().get(0)));
			invContract.getFields().get(0).getPlantings().add(createNewPlantingOnSecondField(65, "FORAGE", 119, "ALFALFA", 74.00, 3, true, true, invContract.getFields().get(0)));

			updateNewInventoryContract(invContract);

			//------------------------------------------
			//
			// Insert Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

			//------------------------------------------
			//
			// Check Inserted Inventory Contract.
			//
			//------------------------------------------
			
			// InventoryContract
			checkInventoryContract(invContract, fetchedInvContract);

			// InventoryContractCommodity
			List<InventoryContractCommodity> fetchedCommodities = fetchedInvContract.getCommodities();
			//Forage commodities should not create a commodity totals record
			Assert.assertEquals(invContract.getCommodities().size(), fetchedCommodities.size());

			for ( int i = 0; i < invContract.getCommodities().size(); i++) {
				checkInventoryContractCommodity(invContract.getCommodities().get(i), fetchedCommodities.get(i));
			}

			// AnnualField
			checkFieldsAndPlantings(invContract.getFields(), fetchedInvContract);
			
			
			//------------------------------------------
			//
			// Update test data.
			//
			//------------------------------------------
			
			//Add second planting and crop commodity
			fetchedInvContract.getCommodities().add(createNewInvCommodities(18, "CANOLA", 12.34, 8.22, 8.22));
			fetchedInvContract.getFields().get(0).getPlantings().add(createAdditionalPlanting(fetchedInvContract.getFields().get(0).getFieldId(), 4));
			//Add a second Barley planting on a second field
			fetchedInvContract.getFields().get(1).getPlantings().add(createNewPlantingOnSecondField(16, "BARLEY", null, null, 12.34, 2, true, true, fetchedInvContract.getFields().get(1)));

			fetchedInvContract.getCommodities().add(createNewInvCommodities(null, null, 44.11, 0.0, 0.0)); //total of Lentil and cropId = null
			fetchedInvContract.getFields().get(1).getPlantings().add(createNewPlantingOnSecondField(null, null, null, null, 22.11, 3, false, false, fetchedInvContract.getFields().get(1)));

			fetchedInvContract.getCommodities().add(createNewInvCommodities(1010889, "LENTIL", 0.0, 15.0, 15.0)); //Unseeded should end up in OTHER (cropId = null)
			fetchedInvContract.getFields().get(1).getPlantings().add(createNewPlantingOnSecondField(1010889, "LENTIL", null, null, 22.0, 4, false, true, fetchedInvContract.getFields().get(1)));

			
			updateInventoryContractData(service, topLevelEndpoints, fetchedInvContract.getCommodities(), fetchedInvContract.getFields(), fetchedInvContract);
			
			//------------------------------------------
			//
			// Update Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

			//------------------------------------------
			//
			// Check Updated Inventory Contract.
			//
			//------------------------------------------
			
			// InventoryContract
			checkInventoryContract(fetchedInvContract, updatedInvContract);

			// InventoryContractCommodity
			Comparator<Integer> nullsLast = Comparator.nullsLast(Comparator.naturalOrder());
			//Sort list
			List<InventoryContractCommodity> updatedCommodities = updatedInvContract.getCommodities().stream()
																									//.sorted(Comparator.comparingInt(InventoryContractCommodity::getCropCommodityId))
																									.sorted(Comparator.comparing(InventoryContractCommodity::getCropCommodityId, nullsLast))
																									.collect(Collectors.toList());

			Assert.assertEquals(fetchedInvContract.getCommodities().size(), updatedCommodities.size());
			
			//Sort list
			List<InventoryContractCommodity> fetchedSortedCommodities = fetchedInvContract.getCommodities().stream()
					.sorted(Comparator.comparing(InventoryContractCommodity::getCropCommodityId, nullsLast))
					.collect(Collectors.toList());


			for ( int i = 0; i < fetchedSortedCommodities.size(); i++) {
				
				//Update for Barley
				if(fetchedSortedCommodities.get(i).getCropCommodityId() != null && fetchedSortedCommodities.get(i).getCropCommodityId().equals(16)) {
					//Double the unseeded acres as they have been updated when saved
					fetchedSortedCommodities.get(i).setTotalUnseededAcres(24.68);
					fetchedSortedCommodities.get(i).setTotalSeededAcres(55.0);
					fetchedSortedCommodities.get(i).setTotalSpotLossAcres(38.45);
				}
				
				checkInventoryContractCommodity(fetchedSortedCommodities.get(i), updatedCommodities.get(i));
			}

			// AnnualField
			checkFieldsAndPlantings(fetchedInvContract.getFields(), updatedInvContract);
			
			deleteInventoryContract();
			
			logger.debug(">testRecalculateCropCommodity");
		}
		

		// TODO: Currently creates an InventoryContract, updates it and leaves it behind because delete hasn't been implemented yet.
		@Test
		public void testDeleteUnseededPlanting() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
			logger.debug("<testDeleteUnseededPlanting");
			
			if(skipTests) {
				logger.warn("Skipping tests");
				return;
			}

			CirrasUnderwritingService service = getService(SCOPES);

			EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

			//------------------------------------------
			//
			// Create test data.
			//
			//------------------------------------------

			//Rollover contract
			InventoryContractRsrc invContract = rolloverInventoryContract(service, topLevelEndpoints);

			invContract.getCommodities().add(createNewInvCommodities(16, "BARLEY", 12.34, 0.0, 0.0));

			createPlantingsForDeleteUnseededTests(invContract);
			
			updateNewInventoryContract(invContract);
			


			//------------------------------------------
			//
			// Insert Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

			//------------------------------------------
			//
			// Check Inserted Inventory Contract.
			//
			//------------------------------------------
			
			// InventoryContract
			checkInventoryContract(invContract, fetchedInvContract);

			// AnnualField
			checkFieldsAndPlantings(invContract.getFields(), fetchedInvContract);
			
			
			//------------------------------------------
			//
			// Update test data.
			//
			//------------------------------------------
			Comparator<Integer> nullsLast = Comparator.nullsLast(Comparator.naturalOrder());
			List<InventoryField> sortedInventoryFields = fetchedInvContract.getFields().get(0).getPlantings().stream()
					.sorted(Comparator.comparing(InventoryField::getPlantingNumber, nullsLast))
					.collect(Collectors.toList());

			List<String> invFieldGuidsNotDeleted = new ArrayList<String>();
			
			Boolean[] deleteIndicator = {true, true, false, true, null};

			for (int i = 0; i < deleteIndicator.length; i++) {
				sortedInventoryFields.get(i).getInventoryUnseeded().setDeletedByUserInd(deleteIndicator[i]);
				
				if ( !Boolean.TRUE.equals(deleteIndicator[i]) || i == 1) {
					// Second delete won't actually happen because it has non-empty seeded data.
					invFieldGuidsNotDeleted.add(sortedInventoryFields.get(i).getInventoryFieldGuid());
				}
			}
			
			fetchedInvContract.getFields().get(0).setPlantings(sortedInventoryFields);
			updateInventoryContractData(service, topLevelEndpoints, fetchedInvContract.getCommodities(), fetchedInvContract.getFields(), fetchedInvContract);
			
			//------------------------------------------
			//
			// Update Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);
			
			
			//
			// Check Updated Inventory Contract.
			//
			//------------------------------------------

			Assert.assertEquals(invFieldGuidsNotDeleted.size(), updatedInvContract.getFields().get(0).getPlantings().size());
			
			Integer counter = 1;
			sortedInventoryFields = updatedInvContract.getFields().get(0).getPlantings().stream()
					.sorted(Comparator.comparing(InventoryField::getPlantingNumber, nullsLast))
					.collect(Collectors.toList());
			for (InventoryField inventoryField : sortedInventoryFields) {

				logger.debug(inventoryField.getInventoryFieldGuid() + " has planting number: " + inventoryField.getPlantingNumber());

				//Test if the planting number of the remaining is in sequence
				Assert.assertEquals("Wrong planting number for: " + inventoryField.getInventoryFieldGuid(), counter, inventoryField.getPlantingNumber());
				Assert.assertEquals(invFieldGuidsNotDeleted.get(counter - 1), inventoryField.getInventoryFieldGuid());
				counter += 1;
			}

			// Original Planting 2
			Assert.assertTrue(checkEmptyUnseeded(sortedInventoryFields.get(0).getInventoryUnseeded()));
			
			// Original Planting 3
			Assert.assertFalse(checkEmptyUnseeded(sortedInventoryFields.get(1).getInventoryUnseeded()));

			// Original Planting 5
			Assert.assertFalse(checkEmptyUnseeded(sortedInventoryFields.get(2).getInventoryUnseeded()));
			
			deleteInventoryContract();
			
			logger.debug(">testDeleteUnseededPlanting");
		}		

		// TODO: Currently creates an InventoryContract, updates it and leaves it behind because delete hasn't been implemented yet.
		@Test
		public void testDeleteSeededPlanting() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
			logger.debug("<testDeleteSeededPlanting");
			
			if(skipTests) {
				logger.warn("Skipping tests");
				return;
			}

			CirrasUnderwritingService service = getService(SCOPES);

			EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

			//------------------------------------------
			//
			// Create test data.
			//
			//------------------------------------------

			//Rollover contract
			InventoryContractRsrc invContract = rolloverInventoryContract(service, topLevelEndpoints);

			invContract.getCommodities().add(createNewInvCommodities(16, "BARLEY", 12.34, 0.0, 0.0));

			createPlantingsForDeleteSeededTests(invContract);
			
			updateNewInventoryContract(invContract);
			


			//------------------------------------------
			//
			// Insert Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

			//------------------------------------------
			//
			// Check Inserted Inventory Contract.
			//
			//------------------------------------------
			
			// InventoryContract
			checkInventoryContract(invContract, fetchedInvContract);

			// AnnualField
			checkFieldsAndPlantings(invContract.getFields(), fetchedInvContract);
			
			
			//------------------------------------------
			//
			// Update test data.
			//
			//------------------------------------------

			List<InventoryField> plantings = fetchedInvContract.getFields().get(0).getPlantings();
			List<String> invFieldGuidsNotDeleted = new ArrayList<String>();

			// Planting 1
			plantings.get(0).getInventorySeededGrains().get(0).setDeletedByUserInd(true);

			// Planting 2
			// Delete will not actually happen due to non-empty unseeded record.
			plantings.get(1).getInventorySeededGrains().get(0).setDeletedByUserInd(true);
			invFieldGuidsNotDeleted.add(plantings.get(1).getInventoryFieldGuid());
			
			// Planting 3
			// Delete will not actually happen due to non-empty seeded record.
			plantings.get(2).getInventorySeededGrains().get(0).setDeletedByUserInd(true);
			invFieldGuidsNotDeleted.add(plantings.get(2).getInventoryFieldGuid());
			
			// Planting 4
			plantings.get(3).getInventorySeededGrains().get(0).setDeletedByUserInd(false);
			invFieldGuidsNotDeleted.add(plantings.get(3).getInventoryFieldGuid());

			// Planting 5
			plantings.get(4).getInventorySeededGrains().get(0).setDeletedByUserInd(true);

			// Planting 6
			plantings.get(5).getInventorySeededGrains().get(0).setDeletedByUserInd(null);
			invFieldGuidsNotDeleted.add(plantings.get(5).getInventoryFieldGuid());

			// Planting 7
			// Planting will not be deleted because it has Unseeded data.
			// The first two Seeded Grains will be deleted, but the last one will 
			// not because it is the last one on that planting.
			plantings.get(6).getInventorySeededGrains().get(0).setDeletedByUserInd(true);
			plantings.get(6).getInventorySeededGrains().get(1).setDeletedByUserInd(true);
			plantings.get(6).getInventorySeededGrains().get(2).setDeletedByUserInd(true);
			invFieldGuidsNotDeleted.add(plantings.get(6).getInventoryFieldGuid());
			String seededGrainNotDeleted = plantings.get(6).getInventorySeededGrains().get(2).getInventorySeededGrainGuid();

			
			updateInventoryContractData(service, topLevelEndpoints, fetchedInvContract.getCommodities(), fetchedInvContract.getFields(), fetchedInvContract);
			
			//------------------------------------------
			//
			// Update Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);
			
			
			//
			// Check Updated Inventory Contract.
			//
			//------------------------------------------

			Assert.assertEquals(invFieldGuidsNotDeleted.size(), updatedInvContract.getFields().get(0).getPlantings().size());
			
			Integer counter = 1;
			plantings = updatedInvContract.getFields().get(0).getPlantings();
			for (InventoryField inventoryField : plantings) {

				logger.debug(inventoryField.getInventoryFieldGuid() + " has planting number: " + inventoryField.getPlantingNumber());

				//Test if the planting number of the remaining is in sequence
				Assert.assertEquals("Wrong planting number for: " + inventoryField.getInventoryFieldGuid(), counter, inventoryField.getPlantingNumber());
				Assert.assertEquals(invFieldGuidsNotDeleted.get(counter - 1), inventoryField.getInventoryFieldGuid());
				
				counter += 1;
			}

			// Original Planting 2
			Assert.assertEquals(1, plantings.get(0).getInventorySeededGrains().size());
			Assert.assertTrue(checkEmptyInventorySeededGrain(plantings.get(0).getInventorySeededGrains().get(0)));

			// Original Planting 3
			Assert.assertEquals(1, plantings.get(1).getInventorySeededGrains().size());
			Assert.assertFalse(checkEmptyInventorySeededGrain(plantings.get(1).getInventorySeededGrains().get(0)));

			// Original Planting 4
			Assert.assertEquals(1, plantings.get(2).getInventorySeededGrains().size());
			Assert.assertFalse(checkEmptyInventorySeededGrain(plantings.get(2).getInventorySeededGrains().get(0)));
			
			// Original Planting 6
			Assert.assertEquals(1, plantings.get(3).getInventorySeededGrains().size());
			Assert.assertFalse(checkEmptyInventorySeededGrain(plantings.get(3).getInventorySeededGrains().get(0)));
			
			// Original Planting 7
			// Check that first two seeded grains were deleted, but no the last one.
			Assert.assertEquals(1, plantings.get(4).getInventorySeededGrains().size());
			Assert.assertEquals(seededGrainNotDeleted, plantings.get(4).getInventorySeededGrains().get(0).getInventorySeededGrainGuid());
			Assert.assertTrue(checkEmptyInventorySeededGrain(plantings.get(4).getInventorySeededGrains().get(0)));
			
			deleteInventoryContract();
			
			logger.debug(">testDeleteSeededPlanting");
		}
		
		
		// TODO: Currently creates an InventoryContract, updates it and leaves it behind because delete hasn't been implemented yet.
		@Test
		public void testUpdateInventoryContractWithUwCommentDelete() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
			logger.debug("<testUpdateInventoryContractWithUwCommentDelete");
			
			if(skipTests) {
				logger.warn("Skipping tests");
				return;
			}

			CirrasUnderwritingService service = getService(SCOPES);

			EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

			//------------------------------------------
			//
			// Create test data.
			//
			//------------------------------------------

			//Rollover contract
			InventoryContractRsrc invContract = rolloverInventoryContract(service, topLevelEndpoints);

			invContract.getCommodities().add(createNewInvCommodities(16, "BARLEY", 12.34, 40.0, 23.45));

			createNewPlantingsUpdateFields(invContract);
			
			updateNewInventoryContract(invContract);

			
			//------------------------------------------
			//
			// Insert Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

			//------------------------------------------
			//
			// Check Inserted Inventory Contract.
			//
			//------------------------------------------
			
			// InventoryContract
			checkInventoryContract(invContract, fetchedInvContract);

			// InventoryContractCommodity
			List<InventoryContractCommodity> fetchedCommodities = fetchedInvContract.getCommodities();
			Assert.assertEquals(invContract.getCommodities().size(), fetchedCommodities.size());

			for ( int i = 0; i < invContract.getCommodities().size(); i++) {
				checkInventoryContractCommodity(invContract.getCommodities().get(i), fetchedCommodities.get(i));
			}

			// AnnualField
			checkFieldsAndPlantings(invContract.getFields(), fetchedInvContract);
			
			//------------------------------------------
			//
			// Get Inventory Contract with another user and try to delete a comment without permission
			//
			//------------------------------------------

			//Try to delete with second user without permissions to delete all comments
			//Expect an error message
			try {
				InventoryContractRsrc invContractUser2 = getInventoryContract(serviceUser2, topLevelEndpointsUser2);
				// Delete the second comment.
				invContractUser2.getFields().get(0).getUwComments().get(1).setDeletedByUserInd(true);
				serviceUser2.updateInventoryContract(invContractUser2.getInventoryContractGuid(), invContractUser2);
				Assert.fail("Attempt to delete another user's comment did not trigger ServiceException");
			} catch (CirrasUnderwritingServiceException e) {
				Assert.assertTrue(e.getMessage().contains("The current user is not authorized to delete this comment."));
			}
			
			//Add a comment with second user
			InventoryContractRsrc invContractUser2 = getInventoryContract(serviceUser2, topLevelEndpointsUser2);
			createComment(invContractUser2.getFields().get(0));
			serviceUser2.updateInventoryContract(invContractUser2.getInventoryContractGuid(), invContractUser2);

			//Get inventory contract with first user again
			fetchedInvContract = getInventoryContract(service, topLevelEndpoints);
			
			//Delete comment of the second user with the main user. 
			//No exception expected as this user has the permission to delete all comments
			AnnualFieldRsrc field = fetchedInvContract.getFields().get(0);
		
			String userToCompare = "SCL\\" + WebadeOauth2ClientId2;
			for ( int i = 0; i < field.getUwComments().size(); i++) {
				UnderwritingComment comment = field.getUwComments().get(i);
				if(comment.getCreateUser().equals(userToCompare)) {
					comment.setDeletedByUserInd(true);
				}
			}
			fetchedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);
			
			//------------------------------------------
			//
			// Update test data
			//
			//------------------------------------------
			replaceInventoryCommodityData(fetchedInvContract);
			updatePlantingsAndFieldsData(fetchedInvContract);

			// Delete the second comment.
			fetchedInvContract.getFields().get(0).getUwComments().get(1).setDeletedByUserInd(true);
			
			updateInventoryContractData(service, topLevelEndpoints, fetchedInvContract.getCommodities(), fetchedInvContract.getFields(), fetchedInvContract);
			
			//------------------------------------------
			//
			// Update Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

			//Manually remove the uw comment previously flagged as deleted for the comparison below.
			fetchedInvContract.getFields().get(0).getUwComments().remove(1);
			
			//
			// Check Updated Inventory Contract.
			//
			//------------------------------------------
			
			// InventoryContract
			checkInventoryContract(fetchedInvContract, updatedInvContract);

			// InventoryContractCommodity
			List<InventoryContractCommodity> updatedCommodities = updatedInvContract.getCommodities();
			Assert.assertEquals(fetchedInvContract.getCommodities().size(), updatedCommodities.size());

			for ( int i = 0; i < fetchedInvContract.getCommodities().size(); i++) {
				checkInventoryContractCommodity(fetchedInvContract.getCommodities().get(i), updatedCommodities.get(i));
			}

			// AnnualField
			checkFieldsAndPlantings(fetchedInvContract.getFields(), updatedInvContract);
			
			deleteInventoryContract();
			
			logger.debug(">testUpdateInventoryContractWithUwCommentDelete");
		}

		// This test is meant to be run after testUpdateInventoryContractWithUwCommentDelete(). It currently leaves behind its data. So after running that test, 
		// update the underwriting_comment.create_user to something other than the user id used by unit tests, then run this one.
		@Test
		public void testUpdateInventoryContractWithOtherUserUwComment() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
			logger.debug("<testUpdateInventoryContractWithOtherUserUwComment");
			
			if(skipTests) {
				logger.warn("Skipping tests");
				return;
			}

			CirrasUnderwritingService service = getService(SCOPES);

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
			Assert.assertNotNull(referrer.getInventoryContractGuid());

			InventoryContractRsrc invContract = service.getInventoryContract(referrer);
			Assert.assertNotNull(invContract);
			
			// Try to update a comment that has been created by a different user.
			invContract.getFields().get(0).getUwComments().get(0).setUnderwritingComment("try to edit another user comment");

			try {
				InventoryContractRsrc updatedInvContract = service.updateInventoryContract(invContract.getInventoryContractGuid(), invContract);
				Assert.fail("Attempt to edit another user's comment did not trigger ServiceException");
			} catch (CirrasUnderwritingServiceException e) {
				Assert.assertTrue(e.getMessage().contains("The current user is not authorized to edit this comment."));
			}
			
			logger.debug(">testUpdateInventoryContractWithOtherUserUwComment");
		}

		
		// TODO: Currently creates an InventoryContract, updates it and leaves it behind because delete hasn't been implemented yet.
		@Test
		public void testUpdateInventoryContractWithoutChanges() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
			logger.debug("<testUpdateInventoryContractWithoutChanges");
			
			if(skipTests) {
				logger.warn("Skipping tests");
				return;
			}

			CirrasUnderwritingService service = getService(SCOPES);

			EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

			//------------------------------------------
			//
			// Create test data.
			//
			//------------------------------------------

			//Rollover contract
			InventoryContractRsrc invContract = rolloverInventoryContract(service, topLevelEndpoints);

			invContract.getCommodities().add(createNewInvCommodities(16, "BARLEY", 12.34, 40.0, 23.45));

			createNewPlantingsUpdateFields(invContract);
			
			updateNewInventoryContract(invContract);
			
			//------------------------------------------
			//
			// Insert Inventory Contract
			//
			//------------------------------------------
			
			InventoryContractRsrc createdInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

			//------------------------------------------
			//
			// Update Inventory Contract without making any changes.
			//
			//------------------------------------------
			
			InventoryContractRsrc fetchedInvContract = service.updateInventoryContract(createdInvContract.getInventoryContractGuid(), createdInvContract);
			
			Assert.assertTrue("InvUpdateTimestamp", createdInvContract.getInvUpdateTimestamp().compareTo(fetchedInvContract.getInvUpdateTimestamp()) < 0 );
			Assert.assertNotNull("InvUpdateUser", fetchedInvContract.getInvUpdateUser());
			
			//------------------------------------------
			//
			// Check Inventory Contract.
			//
			//------------------------------------------
			
			// InventoryContract
			checkInventoryContract(invContract, fetchedInvContract);

			// InventoryContractCommodity
			List<InventoryContractCommodity> fetchedCommodities = fetchedInvContract.getCommodities();
			Assert.assertEquals(invContract.getCommodities().size(), fetchedCommodities.size());

			for ( int i = 0; i < invContract.getCommodities().size(); i++) {
				checkInventoryContractCommodity(invContract.getCommodities().get(i), fetchedCommodities.get(i));
			}

			// AnnualField
			checkFieldsAndPlantings(invContract.getFields(), fetchedInvContract);

			
			//------------------------------------------
			//
			// Update test data.
			//
			//------------------------------------------
			replaceInventoryCommodityData(fetchedInvContract);
			updatePlantingsAndFieldsData(fetchedInvContract);

			updateInventoryContractData(service, topLevelEndpoints, fetchedInvContract.getCommodities(), fetchedInvContract.getFields(), fetchedInvContract);
			
			//------------------------------------------
			//
			// Update Inventory Contract with changes
			//
			//------------------------------------------
			
			InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

			Assert.assertTrue("InvUpdateTimestamp 2", fetchedInvContract.getInvUpdateTimestamp().compareTo(updatedInvContract.getInvUpdateTimestamp()) < 0 );
			Assert.assertNotNull("InvUpdateUser 2", updatedInvContract.getInvUpdateUser());

			//
			// Check Updated Inventory Contract.
			//
			//------------------------------------------
			
			// InventoryContract
			checkInventoryContract(fetchedInvContract, updatedInvContract);

			// InventoryContractCommodity
			List<InventoryContractCommodity> updatedCommodities = updatedInvContract.getCommodities();
			Assert.assertEquals(fetchedInvContract.getCommodities().size(), updatedCommodities.size());

			for ( int i = 0; i < fetchedInvContract.getCommodities().size(); i++) {
				checkInventoryContractCommodity(fetchedInvContract.getCommodities().get(i), updatedCommodities.get(i));
			}

			// AnnualField
			checkFieldsAndPlantings(fetchedInvContract.getFields(), updatedInvContract);
			
			deleteInventoryContract();
			
			logger.debug(">testUpdateInventoryContractWithoutChanges");
		}		
		
	// TODO: Currently creates an InventoryContract, updates it and leaves it behind because delete hasn't been implemented yet.
	@Test
	public void testCreateNewLandAndPlantings() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
		logger.debug("<testCreateNewLandAndPlantings");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//------------------------------------------
		//
		// Create test data.
		//
		//------------------------------------------
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		String cropYear = null;
		String insurancePlanId = null;
		String officeId = null;
		String policyStatusCode = null;
		String policyNumber = "140160-23";
		String growerInfo = null;
		
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				officeId,
				policyStatusCode,
				policyNumber,
				growerInfo,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);

		InventoryContractRsrc invContract = service.getInventoryContract(uwContract);			
		
		AnnualFieldRsrc field = createNewFieldAndPlantingTest();
		
		invContract.getFields().add(field);

		//------------------------------------------
		//
		// Insert Inventory Contract
		//
		//------------------------------------------
		
		InventoryContractRsrc fetchedInvContract = service.updateInventoryContract(invContract.getInventoryContractGuid(), invContract);

		//------------------------------------------
		//
		// Check Inserted Inventory Contract.
		//
		//------------------------------------------
		
		//Check Data in the database directly
		
		logger.debug(">testCreateNewLandAndPlantings");
		
		/*
		 * Select Statements
			select * from contracted_field_detail cfd where cfd.annual_field_detail_id in (select annual_field_detail_id from annual_field_detail afd where afd.field_id in 
				(select field_id from field f where f.field_label = 'UW API Unit Test 2112 1'));
			select * from underwriting_comment uc where uc.annual_field_detail_id in (select annual_field_detail_id from annual_field_detail afd where afd.field_id in 
				(select field_id from field f where f.field_label = 'UW API Unit Test 2112 1'));
			select * from annual_field_detail afd where afd.field_id in (select field_id from field f where f.field_label = 'UW API Unit Test 2112 1');
			select * from legal_land_field_xref x where x.field_id in (select field_id from field f where f.field_label = 'UW API Unit Test 2112 1');
			select * from inventory_seeded_grain isg where isg.inventory_field_guid in (select inventory_field_guid from inventory_field inv where inv.field_id in 
				(select field_id from field f where f.field_label = 'UW API Unit Test 2112 1'));
			select * from inventory_unseeded iu where iu.inventory_field_guid in (select inventory_field_guid from inventory_field inv where inv.field_id in 
				(select field_id from field f where f.field_label = 'UW API Unit Test 2112 1'));
			select * from inventory_field inv where inv.field_id in (select field_id from field f where f.field_label = 'UW API Unit Test 2112 1');
			select * from field f where f.field_label = 'UW API Unit Test 2112 1';
			select * from legal_land ll where ll.other_description = 'UW API Unit Test Other 2112 1';
		 * Delete land and plantings afterwards
		 	delete from contracted_field_detail cfd where cfd.annual_field_detail_id in (select annual_field_detail_id from annual_field_detail afd where afd.field_id in 
				(select field_id from field f where f.field_label = 'UW API Unit Test 2112 1'));
			delete from underwriting_comment uc where uc.annual_field_detail_id in (select annual_field_detail_id from annual_field_detail afd where afd.field_id in 
				(select field_id from field f where f.field_label = 'UW API Unit Test 2112 1'));
			delete from annual_field_detail afd where afd.field_id in (select field_id from field f where f.field_label = 'UW API Unit Test 2112 1');
			delete from legal_land_field_xref x where x.field_id in (select field_id from field f where f.field_label = 'UW API Unit Test 2112 1');
			delete from inventory_seeded_grain isg where isg.inventory_field_guid in (select inventory_field_guid from inventory_field inv where inv.field_id in 
				(select field_id from field f where f.field_label = 'UW API Unit Test 2112 1'));
			delete from inventory_unseeded iu where iu.inventory_field_guid in (select inventory_field_guid from inventory_field inv where inv.field_id in 
				(select field_id from field f where f.field_label = 'UW API Unit Test 2112 1'));
			delete from inventory_field inv where inv.field_id in (select field_id from field f where f.field_label = 'UW API Unit Test 2112 1');
			delete from field f where f.field_label = 'UW API Unit Test 2112 1';
			delete from legal_land ll where ll.other_description = 'UW API Unit Test Other 2112 1';
		*/
	}
	
	@Test
	public void testTransferLandAndPlantings() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
		logger.debug("<testTransferLandAndPlantings");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//------------------------------------------
		//
		// Create test data.
		//
		//------------------------------------------
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		String policyNumber = "140533-22"; //gcy 96302
		//String policyNumber = "141879-22"; //gcy 96300

		
		
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
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);

		InventoryContractRsrc invContract = service.getInventoryContract(uwContract);
		
		//Field To Transfer
		String legalLandId = "8129";
		Integer cropYear = 2022;
		Integer fieldId = 22171;

		//Get Field
		AnnualFieldRsrc field = setFieldAndPlantingsForTransfer(topLevelEndpoints,
																service,
																legalLandId,
																cropYear,
																fieldId);
		
		invContract.getFields().add(field);

		//SECOND Field To Transfer 
		legalLandId = "14982";
		fieldId = 30145;

		//Get Field
		field = setFieldAndPlantingsForTransfer(topLevelEndpoints,
														service,
														legalLandId,
														cropYear,
														fieldId);

		invContract.getFields().add(field);

		//THIRD Field To Transfer 
		legalLandId = "8071";
		fieldId = 21644;

		//Get Field
		field = setFieldAndPlantingsForTransfer(topLevelEndpoints,
														service,
														legalLandId,
														cropYear,
														fieldId);

		invContract.getFields().add(field);
		
		
		

		//------------------------------------------
		//
		// Insert Inventory Contract
		//
		//------------------------------------------
		
		InventoryContractRsrc fetchedInvContract = service.updateInventoryContract(invContract.getInventoryContractGuid(), invContract);

		//------------------------------------------
		//
		// Check Inserted Inventory Contract.
		//
		//------------------------------------------
		
		//Check Data in the database or app directly
		
		logger.debug(">testTransferLandAndPlantings");
	}	

	@Test
	public void testAddExistingLandAndPlantings() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
		logger.debug("<testAddExistingLandAndPlantings");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//------------------------------------------
		//
		// Create test data.
		//
		//------------------------------------------
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		//String policyNumber = "140533-22"; //gcy 96302
		String policyNumber = "145060-23"; //gcy 96304

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
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);

		InventoryContractRsrc invContract = service.getInventoryContract(uwContract);
		
		//Field To Transfer
		String legalLandId = "8129";
		Integer cropYear = 2023;
		Integer fieldId = 22171;

		//Get Field
		AnnualFieldRsrc field = setFieldAndPlantingsForTransfer(topLevelEndpoints,
																service,
																legalLandId,
																cropYear,
																fieldId);
		
		invContract.getFields().add(field);

		//------------------------------------------
		//
		// Insert Inventory Contract
		//
		//------------------------------------------
		
		InventoryContractRsrc fetchedInvContract = service.updateInventoryContract(invContract.getInventoryContractGuid(), invContract);

		//------------------------------------------
		//
		// Check Inserted Inventory Contract.
		//
		//------------------------------------------
		
		//Check Data in the database or app directly
		
		logger.debug(">testAddExistingLandAndPlantings");
	}	
	
	@Test
	public void testTransferExistingLand() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
		logger.debug("<testTransferExistingLand");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//------------------------------------------
		//
		// Create test data.
		// Run scripts at the end of the method
		//
		//------------------------------------------
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		String policyNumber = "999777-22"; //to policy
		
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
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);
		Assert.assertNull(uwContract.getInventoryContractGuid());

		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContract);
		Assert.assertNotNull(invContract);
		
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		//Get field to transfer
		Integer cropYear = 2022;
		
		AnnualFieldListRsrc fieldList = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				"222222222", //fieldId
				null, 
				cropYear.toString());

		Assert.assertNotNull(fieldList);

		//AnnualField
		AnnualFieldRsrc field = fieldList.getCollection().get(0);

		Integer transferFromGrowerContractYearId = field.getPolicies().get(0).getGrowerContractYearId();
		
		//String inventoryContractGuid = field.getPolicies().get(0).getInventoryContractGuid();

		field.setTransferFromGrowerContractYearId(transferFromGrowerContractYearId);
		field.setLandUpdateType(LandUpdateTypes.ADD_EXISTING_LAND);
		field.setCropYear(cropYear);
		field.setDisplayOrder(3);
		
		
		fetchedInvContract.getFields().add(field);
		
		//------------------------------------------
		//
		// update Inventory Contract
		//
		//------------------------------------------
		fetchedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

		//------------------------------------------
		//
		// Check Inserted Inventory Contract.
		//
		//------------------------------------------
		
		//Check Data in the database or app directly
		
		logger.debug(">testTransferExistingLand");
		
		//		*****************
		//		INSERT STATEMENTS
		//		*****************
		//		--From Policy
		//		INSERT INTO cuws.policy(policy_id, grower_id, insurance_plan_id, policy_status_code, office_id, policy_number, contract_number, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
		//		VALUES (999888777, 4, 4, 'ACTIVE', 1, '999888-22', '999888', 999888777, 2022, now(), 'admin', now(), 'admin', now());
		//		INSERT INTO cuws.grower_contract_year(grower_contract_year_id, grower_id, insurance_plan_id, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
		//		VALUES (999992022, 4, 4, 999888777, 2022, now(), 'admin', now(), 'admin', now());
		//		--To Policy
		//		INSERT INTO cuws.policy(policy_id, grower_id, insurance_plan_id, policy_status_code, office_id, policy_number, contract_number, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
		//		VALUES (999777666, 4, 4, 'ACTIVE', 1, '999777-22', '999777', 999777666, 2022, now(), 'admin', now(), 'admin', now());
		//		INSERT INTO cuws.grower_contract_year(grower_contract_year_id, grower_id, insurance_plan_id, contract_id, crop_year, data_sync_trans_date, create_user, create_date, update_user, update_date)
		//		VALUES (997772022, 4, 4, 999777666, 2022, now(), 'admin', now(), 'admin', now());
		//		--Fields
		//		INSERT INTO cuws.field(field_id, field_label, active_from_crop_year, active_to_crop_year, create_user, create_date, update_user, update_date)
		//		VALUES (111111111, 'Test Field 1', 1980, null, 'admin', now(), 'admin', now());
		//		INSERT INTO cuws.field(field_id, field_label, active_from_crop_year, active_to_crop_year, create_user, create_date, update_user, update_date)
		//		VALUES (222222222, 'Test Field 2', 1980, null, 'admin', now(), 'admin', now());
		//		INSERT INTO cuws.field(field_id, field_label, active_from_crop_year, active_to_crop_year, create_user, create_date, update_user, update_date)
		//		VALUES (333333333, 'Test Field 3', 1980, null, 'admin', now(), 'admin', now());
		//		INSERT INTO cuws.field(field_id, field_label, active_from_crop_year, active_to_crop_year, create_user, create_date, update_user, update_date)
		//		VALUES (444444444, 'Test Field 4', 1980, null, 'admin', now(), 'admin', now());
		//		INSERT INTO cuws.field(field_id, field_label, active_from_crop_year, active_to_crop_year, create_user, create_date, update_user, update_date)
		//		VALUES (555555555, 'Test Field 5', 1980, null, 'admin', now(), 'admin', now());
		//		--Annual Fields
		//		INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, create_user, create_date, update_user, update_date)
		//		VALUES (111112022, null, 111111111, 2022, 'admin', now(), 'admin', now());
		//		INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, create_user, create_date, update_user, update_date)
		//		VALUES (222222022, null, 222222222, 2022,'admin', now(), 'admin', now());
		//		INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, create_user, create_date, update_user, update_date)
		//		VALUES (333332022, null, 333333333, 2022, 'admin', now(), 'admin', now());
		//		INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, create_user, create_date, update_user, update_date)
		//		VALUES (444442022, null, 444444444, 2022, 'admin', now(), 'admin', now());
		//		INSERT INTO cuws.annual_field_detail(annual_field_detail_id, legal_land_id, field_id, crop_year, create_user, create_date, update_user, update_date)
		//		VALUES (555552022, null, 555555555, 2022, 'admin', now(), 'admin', now());
		//		--Contracted Fields
		//		--From policy
		//		INSERT INTO cuws.contracted_field_detail(contracted_field_detail_id, annual_field_detail_id, grower_contract_year_id, display_order, create_user, create_date, update_user, update_date)
		//		VALUES (666662022, 111112022, 999992022, 1, 'admin', now(), 'admin', now());
		//		INSERT INTO cuws.contracted_field_detail(contracted_field_detail_id, annual_field_detail_id, grower_contract_year_id, display_order, create_user, create_date, update_user, update_date)
		//		VALUES (777772022, 222222022, 999992022, 2, 'admin', now(), 'admin', now());
		//		INSERT INTO cuws.contracted_field_detail(contracted_field_detail_id, annual_field_detail_id, grower_contract_year_id, display_order, create_user, create_date, update_user, update_date)
		//		VALUES (888882022, 333332022, 999992022, 3, 'admin', now(), 'admin', now());
		//		--To policy
		//		INSERT INTO cuws.contracted_field_detail(contracted_field_detail_id, annual_field_detail_id, grower_contract_year_id, display_order, create_user, create_date, update_user, update_date)
		//		VALUES (999992022, 444442022, 997772022, 1, 'admin', now(), 'admin', now());
		//		INSERT INTO cuws.contracted_field_detail(contracted_field_detail_id, annual_field_detail_id, grower_contract_year_id, display_order, create_user, create_date, update_user, update_date)
		//		VALUES (123452022, 555552022, 997772022, 2, 'admin', now(), 'admin', now());
		//		*****************
		//		SELECT STATEMENTS
		//		*****************
		//		SELECT * FROM inventory_unseeded WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id IN (111111111, 222222222, 333333333, 444444444, 555555555));
		//		SELECT * FROM inventory_field WHERE field_id IN (111111111, 222222222, 333333333, 444444444, 555555555);
		//		SELECT * FROM inventory_contract t WHERE t.contract_id = 999777666;
		//
		//		SELECT * FROM contracted_field_detail where annual_field_detail_id IN (select annual_field_detail_id FROM annual_field_detail WHERE field_id IN (111111111, 222222222, 333333333, 444444444, 555555555))
		//			ORDER BY grower_contract_year_id, display_order;
		//		SELECT * FROM annual_field_detail WHERE field_id IN (111111111, 222222222, 333333333, 444444444, 555555555);
		//		SELECT * FROM field WHERE field_id IN (111111111, 222222222, 333333333, 444444444, 555555555);
		//		SELECT * FROM grower_contract_year WHERE grower_contract_year_id IN (999992022, 997772022);
		//		SELECT * FROM policy WHERE policy_id IN (999888777, 999777666);
		//		888882022 = 2
		//		*****************
		//		DELETE STATEMENTS
		//		*****************
		//		DELETE FROM inventory_unseeded WHERE inventory_field_guid IN (SELECT inventory_field_guid FROM inventory_field WHERE field_id IN (111111111, 222222222, 333333333, 444444444, 555555555));
		//		DELETE FROM inventory_field WHERE field_id IN (111111111, 222222222, 333333333, 444444444, 555555555);
		//		DELETE FROM inventory_contract t WHERE t.contract_id = 999777666;
		//
		//		DELETE FROM contracted_field_detail where annual_field_detail_id IN (select annual_field_detail_id FROM annual_field_detail WHERE field_id IN (111111111, 222222222, 333333333, 444444444, 555555555));
		//		DELETE FROM annual_field_detail WHERE field_id IN (111111111, 222222222, 333333333, 444444444, 555555555);
		//		DELETE FROM field WHERE field_id IN (111111111, 222222222, 333333333, 444444444, 555555555);
		//		DELETE FROM grower_contract_year WHERE grower_contract_year_id IN (999992022, 997772022);
		//		DELETE FROM policy WHERE policy_id IN (999888777, 999777666);

	}	
	
	@Test
	public void testAddFieldToExistingLegal() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
		logger.debug("<testAddFieldToExistingLegal");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//------------------------------------------
		//
		// Create test data.
		//
		//------------------------------------------
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		String policyNumber = "140160-23"; //gcy 97636

		
		
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
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);

		InventoryContractRsrc invContract = service.getInventoryContract(uwContract);
		
		//Field To Transfer
		Integer legalLandId = 11286;
		Integer cropYear = 2022;

		//Get Field
		AnnualFieldRsrc field = addNewFieldToExistingLegal(legalLandId, cropYear);
		
		invContract.getFields().add(field);

		//------------------------------------------
		//
		// Insert Inventory Contract
		//
		//------------------------------------------
		
		InventoryContractRsrc fetchedInvContract = service.updateInventoryContract(invContract.getInventoryContractGuid(), invContract);

		//------------------------------------------
		//
		// Check Inserted Inventory Contract.
		//
		//------------------------------------------
		
		//Check Data in the database or app directly
		
		logger.debug(">testAddFieldToExistingLegal");
	}		
	@Test
	public void testRenameLegalLandAndFieldLabel() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
		logger.debug("<testRenameLegalLandAndFieldLabel");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//------------------------------------------
		//
		// Create test data.
		//
		//------------------------------------------
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		String policyNumber = "142232-22";

		
		
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
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);

		InventoryContractRsrc invContract = service.getInventoryContract(uwContract);
		
		//Legal Land To Rename
		Integer legalLandId = 11326; //Must be existing legal land on the policy

		//Get legal land
		List<AnnualField> fields = invContract.getFields().stream()
				.filter(x -> x.getLegalLandId().equals(legalLandId))
				.collect(Collectors.toList());
		
		AnnualField field = null;
		if(fields != null) {
			field = fields.get(0);
		}
		
		Assert.assertNotNull("Field Null", field);
		
		String originalFieldLabel = field.getFieldLabel();
		String originalOtherDescription = field.getOtherLegalDescription();
		
		String newFieldLabel = originalFieldLabel + " NEW";
		String newOtherDescription = originalOtherDescription + " NEW";
		String newPrimaryPropertyIdentifier = "XXX-XXX-XXX";
		
		field.setFieldLabel(newFieldLabel);
		field.setOtherLegalDescription(newOtherDescription);
		field.setPrimaryPropertyIdentifier(newPrimaryPropertyIdentifier);	
		field.setLandUpdateType(LandUpdateTypes.RENAME_LEGAL_LOCATION);
		
		//------------------------------------------
		//
		// Update Inventory Contract
		//
		//------------------------------------------
		
		InventoryContractRsrc fetchedInvContract = service.updateInventoryContract(invContract.getInventoryContractGuid(), invContract);

		//------------------------------------------
		//
		// Check Inserted Inventory Contract.
		//
		//------------------------------------------
		
		//Get legal land
		fields = fetchedInvContract.getFields().stream()
				.filter(x -> x.getLegalLandId().equals(legalLandId))
				.collect(Collectors.toList());
		
		field = null;
		if(fields != null) {
			field = fields.get(0);
		}
		
		
		Assert.assertEquals("Field Label New",  newFieldLabel, field.getFieldLabel());
		Assert.assertEquals("Other Description New",  newOtherDescription, field.getOtherLegalDescription());
		Assert.assertEquals("PID New",  newPrimaryPropertyIdentifier, field.getPrimaryPropertyIdentifier());
		//////////////////////////////
		//Check Data in CIRRAS as well
		//////////////////////////////
		
		//------------------------------------------
		//
		// Set other description to NULL
		//
		//------------------------------------------
		

		field.setOtherLegalDescription(null);
		field.setLandUpdateType(LandUpdateTypes.RENAME_LEGAL_LOCATION);
		
		//------------------------------------------
		//
		// Update Inventory Contract
		//
		//------------------------------------------
		
		InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

		//------------------------------------------
		//
		// Check Inserted Inventory Contract.
		//
		//------------------------------------------
		
		//Get legal land
		fields = updatedInvContract.getFields().stream()
				.filter(x -> x.getLegalLandId().equals(legalLandId))
				.collect(Collectors.toList());
		
		field = null;
		if(fields != null) {
			field = fields.get(0);
		}
		
		
		Assert.assertEquals("Other Description Null",  null, field.getOtherLegalDescription());				
		
		//------------------------------------------
		//
		// Reset the other description
		//
		//------------------------------------------
		
		field.setFieldLabel(originalFieldLabel);
		field.setOtherLegalDescription(originalOtherDescription);
		field.setLandUpdateType(LandUpdateTypes.RENAME_LEGAL_LOCATION);
		
		//------------------------------------------
		//
		// Update Inventory Contract
		//
		//------------------------------------------
		
		InventoryContractRsrc resetInvContract = service.updateInventoryContract(updatedInvContract.getInventoryContractGuid(), updatedInvContract);

		//------------------------------------------
		//
		// Check Inserted Inventory Contract.
		//
		//------------------------------------------
		
		//Get legal land
		fields = resetInvContract.getFields().stream()
				.filter(x -> x.getLegalLandId().equals(legalLandId))
				.collect(Collectors.toList());
		
		field = null;
		if(fields != null) {
			field = fields.get(0);
		}
		
		
		Assert.assertEquals("Field Label Original",  originalFieldLabel, field.getFieldLabel());		
		Assert.assertEquals("Other Description Original",  originalOtherDescription, field.getOtherLegalDescription());		
		
		
		logger.debug(">testRenameLegalLandAndFieldLabel");
	}	

	@Test
	public void testReplaceLegalLand() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
		logger.debug("<testReplaceLegalLand");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//------------------------------------------
		//
		// Create test data.
		//
		//------------------------------------------
		//Create legal land
		Integer legalLandId = 35795135;
		String otherDesc = "DL 2737 test replace";
		createLegalLand(legalLandId, otherDesc, service);

		//Get uw contract
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		String policyNumber = "142232-22";
		
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
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);

		InventoryContractRsrc invContract = service.getInventoryContract(uwContract);
		Assert.assertTrue(invContract.getFields().size() > 0);

		// Pick a field, any field.
		AnnualField field = invContract.getFields().get(0);
		Integer fieldId = field.getFieldId();
		Integer origLegalLandId = field.getLegalLandId();
		String origOtherDesc = field.getOtherLegalDescription();

		// TEST 1: Replace Existing.
		field.setLandUpdateType(LandUpdateTypes.REPLACE_LEGAL_LOCATION_EXISTING);
		field.setLegalLandId(legalLandId);
		field.setOtherLegalDescription(otherDesc);
				
		InventoryContractRsrc updatedInvContract = service.updateInventoryContract(invContract.getInventoryContractGuid(), invContract);		
		field = updatedInvContract.getFields().get(0);
		
		Assert.assertEquals(fieldId, field.getFieldId());
		Assert.assertEquals(legalLandId, field.getLegalLandId());
		Assert.assertEquals(otherDesc, field.getOtherLegalDescription());

		LegalLandFieldXrefRsrc lllx = service.getLegalLandFieldXref(topLevelEndpoints, legalLandId.toString(), fieldId.toString());
		Assert.assertEquals(legalLandId, lllx.getLegalLandId());
		Assert.assertEquals(fieldId, lllx.getFieldId());
		
		// Check CIRRAS manually.
		
		// TEST 2: Replace New.
		field.setLandUpdateType(LandUpdateTypes.REPLACE_LEGAL_LOCATION_NEW);
		field.setLegalLandId(null);
		field.setOtherLegalDescription("NW 11 22 33 test replace");
				
		updatedInvContract = service.updateInventoryContract(updatedInvContract.getInventoryContractGuid(), updatedInvContract);		
		field = updatedInvContract.getFields().get(0);		
		
		Assert.assertEquals(fieldId, field.getFieldId());
		Assert.assertNotNull(field.getLegalLandId());
		Assert.assertEquals("NW 11 22 33 test replace", field.getOtherLegalDescription());
		
		Integer createdLegalLandId = field.getLegalLandId();

		lllx = service.getLegalLandFieldXref(topLevelEndpoints, field.getLegalLandId().toString(), fieldId.toString());
		Assert.assertEquals(field.getLegalLandId(), lllx.getLegalLandId());
		Assert.assertEquals(fieldId, lllx.getFieldId());
		
		//Check if previous legal land field map record still exists
		lllx = service.getLegalLandFieldXref(topLevelEndpoints, legalLandId.toString(), fieldId.toString());
		Assert.assertNull(lllx);
		
		// Check CIRRAS manually.

		//------------------------------------------
		//
		// Reset the other description
		//
		//------------------------------------------
		
		field.setLandUpdateType(LandUpdateTypes.REPLACE_LEGAL_LOCATION_EXISTING);
		field.setLegalLandId(origLegalLandId);
		field.setOtherLegalDescription(origOtherDesc);
		
		updatedInvContract = service.updateInventoryContract(updatedInvContract.getInventoryContractGuid(), updatedInvContract);		
		field = updatedInvContract.getFields().get(0);		

		Assert.assertEquals(fieldId, field.getFieldId());
		Assert.assertEquals(origLegalLandId, field.getLegalLandId());
		Assert.assertEquals(origOtherDesc, field.getOtherLegalDescription());

		//Check if previous legal land field map record still exists
		lllx = service.getLegalLandFieldXref(topLevelEndpoints, createdLegalLandId.toString(), fieldId.toString());
		Assert.assertNull(lllx);

		//Cleanup
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());
		service.deleteLegalLandSync(topLevelEndpoints, createdLegalLandId.toString());
		
		logger.debug(">testReplaceLegalLand");
		
		//Query to check the legal land, xref and annual field records
		//		select ll.legal_land_id, ll.other_description, xref.field_id, afd.annual_field_detail_id
		//		from legal_land ll
		//		left join legal_land_field_xref xref on xref.legal_land_id = ll.legal_land_id
		//		left join annual_field_detail afd on afd.legal_land_id = ll.legal_land_id
		//		where ll.legal_land_id in (35795135, 1000011) --1000011 is the original legal land id and might change
		//		or ll.other_description = 'NW 11 22 33 test replace'
		
	}	
	
	private void createLegalLand(Integer legalLandId, String otherDescription, CirrasUnderwritingService service) throws CirrasUnderwritingServiceException, ValidationException {

		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(legalLandId);
		resource.setPrimaryPropertyIdentifier("111-222-934");
		resource.setPrimaryReferenceTypeCode("OTHER");
		resource.setLegalDescription("Legal Description");
		resource.setLegalShortDescription("Short Legal");
		resource.setOtherDescription(otherDescription);
		resource.setActiveFromCropYear(2010);
		resource.setActiveToCropYear(null);
		resource.setTransactionType(LandManagementEventTypes.LegalLandCreated);

		service.synchronizeLegalLand(resource);
	}

		
	@Test
	public void testUpdateDisplayOrder() throws CirrasUnderwritingServiceException, ValidationException, Oauth2ClientException {
		logger.debug("<testUpdateDisplayOrder");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasUnderwritingService service = getService(SCOPES);

		//Get inventory contract
		InventoryContractRsrc invContract = getInventoryContract(service);
		
		Assert.assertNotNull("invContract not null", invContract);
		
		List<AnnualFieldRsrc> fields = invContract.getFields();
		Assert.assertTrue("fields.size(): ", fields.size() > 0);
		
		//Get max display order
		OptionalInt tempMaxDisplayNumber = invContract.getFields().stream()
		.mapToInt(x -> x.getDisplayOrder())
		.max();
		
		Assert.assertTrue("tempMaxDisplayNumber.isPresent(): ", tempMaxDisplayNumber.isPresent());
		
		//Set highest and second highest display order number
		maxDisplayNumber = tempMaxDisplayNumber.getAsInt();
		prevDisplayNumber = maxDisplayNumber -1;
		
		//Get contracted field with highest display order number
		List<AnnualFieldRsrc> maxDisplayOrderFields = invContract.getFields().stream()
				.filter(x -> x.getDisplayOrder().equals(maxDisplayNumber)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals("maxDisplayOrderFields.size(): ", 1, maxDisplayOrderFields.size());

		//Get contracted field with second highest display order number
		List<AnnualFieldRsrc> prevDisplayOrderFields = invContract.getFields().stream()
				.filter(x -> x.getDisplayOrder().equals(prevDisplayNumber)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals("prevDisplayOrderFields.size(): ", 1 , prevDisplayOrderFields.size());

		//Set second highest display order number and store contracted field detail id
		maxDisplayOrderFields.get(0).setDisplayOrder(prevDisplayNumber);
		maxContractedFieldDetailId = maxDisplayOrderFields.get(0).getContractedFieldDetailId();
		logger.debug("maxContractedFieldDetailId before: " + maxContractedFieldDetailId);

		//Set highest display order number and store contracted field detail id
		prevDisplayOrderFields.get(0).setDisplayOrder(maxDisplayNumber);
		prevContractedFieldDetailId = prevDisplayOrderFields.get(0).getContractedFieldDetailId();
		logger.debug("prevContractedFieldDetailId before: " + prevContractedFieldDetailId);
	
		//Update display order
		service.updateInventoryContract(invContract.getInventoryContractGuid(), invContract);
		
		//Get the inventory contract again
		InventoryContractRsrc fetchedInvContract = getInventoryContract(service);
		
		Assert.assertNotNull("fetchedInvContract not null", fetchedInvContract);

		//Check if display order changed
		maxDisplayOrderFields = fetchedInvContract.getFields().stream()
				.filter(x -> x.getDisplayOrder().equals(maxDisplayNumber)) 
				.collect(Collectors.toList());
		
		//Check if the display order switched
		Assert.assertEquals("maxDisplayOrderFields.size() 2: ", 1, maxDisplayOrderFields.size());
		logger.debug("maxContractedFieldDetailId after: " + maxContractedFieldDetailId);
		Assert.assertEquals("maxDisplayOrderField ContractedFieldDetailId: ", prevContractedFieldDetailId, maxDisplayOrderFields.get(0).getContractedFieldDetailId());
		
		prevDisplayOrderFields = fetchedInvContract.getFields().stream()
				.filter(x -> x.getDisplayOrder().equals(prevDisplayNumber)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals("prevDisplayOrderFields.size() 2: ", 1, prevDisplayOrderFields.size());
		logger.debug("prevContractedFieldDetailId after: " + prevContractedFieldDetailId);
		Assert.assertEquals("maxDisplayOrderField ContractedFieldDetailId: ", maxContractedFieldDetailId, prevDisplayOrderFields.get(0).getContractedFieldDetailId());

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
				"142232-22",  //Needs to be contract with inventory
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
	
	private Integer maxDisplayNumber;
	private Integer prevDisplayNumber;
	private Integer maxContractedFieldDetailId;
	private Integer prevContractedFieldDetailId;
		
	private void updatePlantingsAndFieldsData(InventoryContractRsrc invContract) {

		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.FEBRUARY, 15);
		Date seededDate = cal.getTime();

		cal.clear();
		cal.set(2020, Calendar.MARCH, 15);
		Date seededDate2 = cal.getTime();
		
		
		//AnnualField
		List<AnnualFieldRsrc> fields = invContract.getFields();

		Assert.assertTrue(fields.size() > 0);

		AnnualFieldRsrc field = fields.get(0);

		InventoryField planting = field.getPlantings().get(0);

		planting.setLastYearCropCommodityId(71);
		planting.setLastYearCropCommodityName("SILAGE CORN");
		planting.setLastYearCropVarietyId(1010863);
		planting.setLastYearCropVarietyName("SILAGE CORN - UNSPECIFIED");
		planting.setIsHiddenOnPrintoutInd(true);
		planting.setPlantingNumber(2);
		planting.setUnderseededAcres(20.6);
		planting.setUnderseededCropVarietyId(119);
		planting.setUnderseededCropVarietyName("ALFALFA");

		//InventoryUnseeded
		InventoryUnseeded invUnseeded = planting.getInventoryUnseeded();
		invUnseeded.setAcresToBeSeeded(12.34);
		invUnseeded.setCropCommodityId(26);
		invUnseeded.setCropCommodityName("WHEAT");
		invUnseeded.setIsUnseededInsurableInd(false);

		//planting.setInventoryUnseeded(invUnseeded);

		//InventorySeededGrain
		InventorySeededGrain invSeededGrain = planting.getInventorySeededGrains().get(0);

		invSeededGrain.setCommodityTypeCode("Fall Rye");
		invSeededGrain.setCommodityTypeDesc("Fall Rye");
		invSeededGrain.setCropCommodityId(20);
		invSeededGrain.setCropCommodityName("FALL RYE");
		invSeededGrain.setCropVarietyId(1010513);
		invSeededGrain.setCropVarietyName("BRASETTO");
		invSeededGrain.setIsPedigreeInd(false);
		invSeededGrain.setIsSpotLossInsurableInd(false);
		invSeededGrain.setIsQuantityInsurableInd(false);
		invSeededGrain.setIsReplacedInd(true);
		invSeededGrain.setSeededAcres(22.11);
		invSeededGrain.setSeededDate(seededDate);

		invSeededGrain = planting.getInventorySeededGrains().get(1);

		invSeededGrain.setIsReplacedInd(true);
		invSeededGrain.setSeededDate(seededDate2);  // Set to later than the first seeded grain so that it stays in the same order.
		
		
		// Another planting with empty inventory unseeded and seeded grain records.
		InventoryField planting2 = new InventoryField();

		planting2.setCropYear(cropYear);
		planting2.setFieldId(field.getFieldId());
		planting2.setInsurancePlanId(4);
		planting2.setInventoryFieldGuid(null);
		planting2.setIsHiddenOnPrintoutInd(false);
		planting2.setLastYearCropCommodityId(null);
		planting2.setLastYearCropCommodityName(null);
		planting2.setLastYearCropVarietyId(null);
		planting2.setLastYearCropVarietyName(null);
		planting2.setPlantingNumber(3);

		InventoryUnseeded emptyIu = new InventoryUnseeded();
		emptyIu.setAcresToBeSeeded(null);
		emptyIu.setCropCommodityId(null);
		emptyIu.setCropCommodityName(null);
		emptyIu.setInventoryFieldGuid(null);
		emptyIu.setInventoryUnseededGuid(null);
		emptyIu.setIsUnseededInsurableInd(false);
		
		planting2.setInventoryUnseeded(emptyIu);
		
		InventorySeededGrain emptyIsg = new InventorySeededGrain();
		emptyIsg.setCommodityTypeCode(null);
		emptyIsg.setCommodityTypeDesc(null);
		emptyIsg.setCropCommodityId(null);
		emptyIsg.setCropCommodityName(null);
		emptyIsg.setCropVarietyId(null);
		emptyIsg.setCropVarietyName(null);
		emptyIsg.setInventoryFieldGuid(null);
		emptyIsg.setInventorySeededGrainGuid(null);
		emptyIsg.setIsPedigreeInd(false);
		emptyIsg.setIsQuantityInsurableInd(false);
		emptyIsg.setIsReplacedInd(false);
		emptyIsg.setIsSpotLossInsurableInd(false);
		emptyIsg.setSeededAcres(null);
		emptyIsg.setSeededDate(null);
		
		List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
		seededGrains.add(emptyIsg);
		planting2.setInventorySeededGrains(seededGrains);
		field.getPlantings().add(planting2);
		
//		List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
//		seededGrains.add(invSeededGrain);
//		planting.setInventorySeededGrains(seededGrains);

		//UnderwritingComment
		UnderwritingComment underwritingComment = field.getUwComments().get(0);

		underwritingComment.setUnderwritingComment("update to comment for planting number " + planting.getPlantingNumber() + ", Field " + planting.getFieldId());
		underwritingComment.setUnderwritingCommentTypeCode("INV");
		underwritingComment.setUnderwritingCommentTypeDesc("Inventory");
		
//		List<InventoryField> plantings = new ArrayList<InventoryField>();
//		plantings.add(planting);
//
//		Assert.assertTrue(field.getPlantings().isEmpty());
//		field.setPlantings(plantings);
	}

	private void replaceInventoryCommodityData(InventoryContractRsrc invContract) {
		
		InventoryContractCommodity invContractCommodity = invContract.getCommodities().get(0);
		invContractCommodity.setCropCommodityId(26);
		invContractCommodity.setCropCommodityName("WHEAT");
		invContractCommodity.setTotalSeededAcres(0.0);
		invContractCommodity.setTotalSpotLossAcres(0.0);
		invContractCommodity.setTotalUnseededAcres(12.34);
		invContractCommodity.setTotalUnseededAcresOverride(87.65);
		invContractCommodity.setIsPedigreeInd(false);
		
	}
	
	//Update existing crop commodity
	private void updateInventoryCommodityData(InventoryContractRsrc invContract) {
		
		InventoryContractCommodity invContractCommodity = invContract.getCommodities().get(0);
		invContractCommodity.setTotalSeededAcres(99.99);
		invContractCommodity.setTotalSpotLossAcres(56.78);
		invContractCommodity.setTotalUnseededAcres(12.34);
		invContractCommodity.setTotalUnseededAcresOverride(87.65);
		invContractCommodity.setIsPedigreeInd(false);
		
	}

	private void checkFieldsAndPlantings(List<AnnualFieldRsrc> fields, InventoryContractRsrc fetchedInvContract) {
		List<AnnualFieldRsrc> fetchedFields = fetchedInvContract.getFields();
		Assert.assertEquals(fields.size(), fetchedFields.size());

		for ( int i = 0; i < fields.size(); i++) {
			checkAnnualField(fields.get(i), fetchedFields.get(i));

			// InventoryField
			List<InventoryField> fetchedPlantings = fetchedFields.get(i).getPlantings();
			Assert.assertEquals(fields.get(i).getPlantings().size(), fetchedPlantings.size());
			
			//UnderwritingComment
			// Comments are sorted by comment before comparing, because they are normally sorted by create_date and underwriting_comment_guid, 
			// so there is no way to know in what order the comments will initially be saved.
			List<UnderwritingComment> uwCommentsJ = sortUwCommentsByComment(fields.get(i).getUwComments()); 
			List<UnderwritingComment> fetchedUwCommentsJ = sortUwCommentsByComment(fetchedFields.get(i).getUwComments());
			Assert.assertEquals(uwCommentsJ.size(), fetchedUwCommentsJ.size());

			for (int k = 0; k < uwCommentsJ.size(); k++) {
				checkUnderwritingComment(uwCommentsJ.get(k), fetchedUwCommentsJ.get(k));
			}
			
			for (int j = 0; j < fetchedPlantings.size(); j++) {
				InventoryField plantingJ = fields.get(i).getPlantings().get(j);
				InventoryField fetchedPlantingJ = fetchedPlantings.get(j);

				checkInventoryField(plantingJ, fetchedPlantingJ);
				
				//InventoryUnseeded
				checkInventoryUnseeded(plantingJ.getInventoryUnseeded(), fetchedPlantingJ.getInventoryUnseeded());

				//InventorySeededGrain
				List<InventorySeededGrain> seededGrainsJ = plantingJ.getInventorySeededGrains();
				List<InventorySeededGrain> fetchedSeededGrainsJ = fetchedPlantingJ.getInventorySeededGrains();
				Assert.assertEquals(seededGrainsJ.size(), fetchedSeededGrainsJ.size());

				for (int k = 0; k < seededGrainsJ.size(); k++) {
					checkInventorySeededGrain(seededGrainsJ.get(k), fetchedSeededGrainsJ.get(k));
				}			
				
			}
		}
	}
	
	private void updateNewInventoryContract(
			InventoryContractRsrc invContract) throws CirrasUnderwritingServiceException {

		invContract.setFertilizerInd(false);
		invContract.setGrainFromPrevYearInd(true);
		invContract.setHerbicideInd(true);
		invContract.setInventoryContractGuid(null);
		invContract.setOtherChangesComment("Other changes comment");
		invContract.setOtherChangesInd(true);
		invContract.setSeededCropReportSubmittedInd(false);
		invContract.setTilliageInd(false);
		invContract.setUnseededIntentionsSubmittedInd(false);
		
	}
	
	private void updateInventoryContractData(
			CirrasUnderwritingService service, 
			EndpointsRsrc topLevelEndpoints,
			List<InventoryContractCommodity> commodities,
			List<AnnualFieldRsrc> fields,
			InventoryContractRsrc invContract) throws CirrasUnderwritingServiceException {

		// InventoryContract
		invContract.setFertilizerInd(true);
		invContract.setGrainFromPrevYearInd(false);
		invContract.setHerbicideInd(false);
		invContract.setOtherChangesComment("Changed comment for update");
		invContract.setOtherChangesInd(false);
		invContract.setSeededCropReportSubmittedInd(true);
		invContract.setTilliageInd(true);
		invContract.setUnseededIntentionsSubmittedInd(true);
		
		invContract.setCommodities(commodities);
		
		invContract.setFields(fields);
		
	}	

	private void createNewPlantingsUpdateFields(InventoryContractRsrc invContract
			) throws CirrasUnderwritingServiceException {

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date seededDate = cal.getTime();
		
		//AnnualField
		List<AnnualFieldRsrc> fields = invContract.getFields();

		Assert.assertTrue(fields.size() > 0);

		AnnualFieldRsrc field = fields.get(0);
		
		//UnderwritingComment
		UnderwritingComment underwritingComment = new UnderwritingComment();

		underwritingComment.setAnnualFieldDetailId(field.getAnnualFieldDetailId());
		underwritingComment.setUnderwritingComment("Comment for field " + field.getFieldId());
		underwritingComment.setUnderwritingCommentGuid(null);
		underwritingComment.setUnderwritingCommentTypeCode("INV");
		underwritingComment.setUnderwritingCommentTypeDesc("Inventory");
		
		List<UnderwritingComment> uwComments = new ArrayList<UnderwritingComment>();
		uwComments.add(underwritingComment);

		underwritingComment = new UnderwritingComment();
		underwritingComment.setAnnualFieldDetailId(field.getAnnualFieldDetailId());
		underwritingComment.setUnderwritingComment("Another comment for field " + field.getFieldId());
		underwritingComment.setUnderwritingCommentGuid(null);
		underwritingComment.setUnderwritingCommentTypeCode("INV");
		underwritingComment.setUnderwritingCommentTypeDesc("Inventory");		

		uwComments.add(underwritingComment);
		
		field.setUwComments(uwComments);

		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(4);
		planting.setInventoryFieldGuid(null);
		planting.setLastYearCropCommodityId(65);
		planting.setLastYearCropCommodityName("FORAGE");
		planting.setLastYearCropVarietyId(220);
		planting.setLastYearCropVarietyName("CLOVER/GRASS");
		planting.setIsHiddenOnPrintoutInd(false);
		planting.setPlantingNumber(1);
		planting.setUnderseededAcres(14.4);
		planting.setUnderseededCropVarietyId(119);
		planting.setUnderseededCropVarietyName("ALFALFA");

		//InventoryUnseeded
		InventoryUnseeded invUnseeded = new InventoryUnseeded();
		invUnseeded.setAcresToBeSeeded(12.34);
		invUnseeded.setCropCommodityId(16);
		invUnseeded.setCropCommodityName("BARLEY");
		invUnseeded.setInventoryFieldGuid(null);
		invUnseeded.setInventoryUnseededGuid(null);
		invUnseeded.setIsUnseededInsurableInd(true);
		invUnseeded.setIsCropInsuranceEligibleInd(true);
		invUnseeded.setIsInventoryCropInd(true);
		
		planting.setInventoryUnseeded(invUnseeded);

		//InventorySeededGrain
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
		invSeededGrain.setIsReplacedInd(true);
		invSeededGrain.setSeededAcres(23.45);
		invSeededGrain.setSeededDate(seededDate);

		List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
		seededGrains.add(invSeededGrain);

		//2nd seeded grain
		invSeededGrain = new InventorySeededGrain();

		invSeededGrain.setCommodityTypeCode("Two Row");
		invSeededGrain.setCommodityTypeDesc("Two Row Standard");
		invSeededGrain.setCropCommodityId(16);
		invSeededGrain.setCropCommodityName("BARLEY");
		invSeededGrain.setCropVarietyId(1010430);
		invSeededGrain.setCropVarietyName("CHAMPION");
		invSeededGrain.setInventoryFieldGuid(null);
		invSeededGrain.setInventorySeededGrainGuid(null);
		invSeededGrain.setIsPedigreeInd(false);
		invSeededGrain.setIsSpotLossInsurableInd(false);
		invSeededGrain.setIsQuantityInsurableInd(true);
		invSeededGrain.setIsReplacedInd(false);
		invSeededGrain.setSeededAcres(16.55);
		invSeededGrain.setSeededDate(seededDate);
		
		seededGrains.add(invSeededGrain);
		planting.setInventorySeededGrains(seededGrains);

		List<InventoryField> plantings = new ArrayList<InventoryField>();
		plantings.add(planting);

		field.setPlantings(plantings);
		
		invContract.setFields(fields);
	}
	
	private void createComment(AnnualFieldRsrc field) {
		//UnderwritingComment
		UnderwritingComment underwritingComment = new UnderwritingComment();

		underwritingComment.setAnnualFieldDetailId(field.getAnnualFieldDetailId());
		underwritingComment.setUnderwritingComment("Comment 2nd user");
		underwritingComment.setUnderwritingCommentGuid(null);
		underwritingComment.setUnderwritingCommentTypeCode("INV");
		underwritingComment.setUnderwritingCommentTypeDesc("Inventory");
		
		List<UnderwritingComment> uwComments = field.getUwComments();
		uwComments.add(underwritingComment);
	
	}
	
	private InventoryField createNewPlantingOnSecondField(Integer cropId, String cropName, 
			Integer cropVarietyId, String cropVarietyName, 
			Double acresCropsToBeSeeded, Integer plantingNumber, 
			Boolean isCropInsuranceEligibleInd, Boolean isInventoryCropInd, AnnualFieldRsrc field) throws CirrasUnderwritingServiceException {

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date seededDate = cal.getTime();

		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(4);
		planting.setInventoryFieldGuid(null);
		planting.setLastYearCropCommodityId(20);
		planting.setLastYearCropCommodityName("FALL RYE");
		planting.setLastYearCropVarietyId(null);
		planting.setLastYearCropVarietyName(null);
		planting.setPlantingNumber(plantingNumber);
		planting.setIsHiddenOnPrintoutInd(false);
		planting.setUnderseededAcres(14.4);
		planting.setUnderseededCropVarietyId(119);
		planting.setUnderseededCropVarietyName("ALFALFA");

		//InventoryUnseeded
		InventoryUnseeded invUnseeded = new InventoryUnseeded();
		invUnseeded.setAcresToBeSeeded(acresCropsToBeSeeded);
		invUnseeded.setCropCommodityId(cropId);
		invUnseeded.setCropCommodityName(cropName);
		invUnseeded.setCropVarietyId(cropVarietyId);
		invUnseeded.setCropVarietyName(cropVarietyName);
		invUnseeded.setInventoryFieldGuid(null);
		invUnseeded.setInventoryUnseededGuid(null);
		invUnseeded.setIsUnseededInsurableInd(true);
		invUnseeded.setIsCropInsuranceEligibleInd(isCropInsuranceEligibleInd);
		invUnseeded.setIsInventoryCropInd(isInventoryCropInd);

		planting.setInventoryUnseeded(invUnseeded);

		if(cropId != null) {
			//InventorySeededGrain
			InventorySeededGrain invSeededGrain = new InventorySeededGrain();
	
			invSeededGrain.setCommodityTypeCode("CPSW");
			invSeededGrain.setCommodityTypeDesc("Canadian Prairie Spring Wheat");
			invSeededGrain.setCropCommodityId(cropId);
			invSeededGrain.setCropCommodityName(cropName);
			invSeededGrain.setCropVarietyId(1010602);
			invSeededGrain.setCropVarietyName("AAC ENTICE");
			invSeededGrain.setInventoryFieldGuid(null);
			invSeededGrain.setInventorySeededGrainGuid(null);
			invSeededGrain.setIsPedigreeInd(false);
			invSeededGrain.setIsSpotLossInsurableInd(true);
			invSeededGrain.setIsQuantityInsurableInd(true);
			invSeededGrain.setIsReplacedInd(false);
			invSeededGrain.setSeededAcres(15.0);
			invSeededGrain.setSeededDate(seededDate);
			
			List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
			seededGrains.add(invSeededGrain);
			planting.setInventorySeededGrains(seededGrains);
		}
		
		return planting;
	}
	
	private void createPlantingsForDeleteUnseededTests(InventoryContractRsrc invContract
			) throws CirrasUnderwritingServiceException {

		//AnnualField
		List<AnnualFieldRsrc> fields = invContract.getFields();

		Assert.assertTrue(fields.size() > 0);

		AnnualFieldRsrc field = fields.get(0);
		
		InventoryField planting1 = createPlanting(field, 1, cropYear, false);
		InventoryField planting2 = createPlanting(field, 2, cropYear, true);
		InventoryField planting3 = createPlanting(field, 3, cropYear, false);
		InventoryField planting4 = createPlanting(field, 4, cropYear, false);

		// Create empty seeded grain.
		InventorySeededGrain isg = new InventorySeededGrain();
		isg.setCommodityTypeCode(null);
		isg.setCommodityTypeDesc(null);
		isg.setCropCommodityId(null);
		isg.setCropCommodityName(null);
		isg.setCropVarietyId(null);
		isg.setCropVarietyName(null);
		isg.setInventoryFieldGuid(planting4.getInventoryFieldGuid());
		isg.setInventorySeededGrainGuid(null);
		isg.setIsPedigreeInd(false);
		isg.setIsQuantityInsurableInd(false);
		isg.setIsReplacedInd(false);
		isg.setIsSpotLossInsurableInd(false);
		isg.setSeededAcres(null);
		isg.setSeededDate(null);
		
		List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
		seededGrains.add(isg);
		planting4.setInventorySeededGrains(seededGrains);
		
		InventoryField planting5 = createPlanting(field, 5, cropYear, false);
		
		List<InventoryField> plantings = new ArrayList<InventoryField>();
		plantings.add(planting1);
		plantings.add(planting2);
		plantings.add(planting3);
		plantings.add(planting4);
		plantings.add(planting5);
		
		field.setPlantings(plantings);
		
		invContract.setFields(fields);
	}

	private void createPlantingsForDeleteSeededTests(InventoryContractRsrc invContract) throws CirrasUnderwritingServiceException {

		//AnnualField
		List<AnnualFieldRsrc> fields = invContract.getFields();

		Assert.assertTrue(fields.size() > 0);

		AnnualFieldRsrc field = fields.get(0);
		
		InventoryField planting1 = createBasePlanting(field, 1, cropYear);
		createEmptyUnseeded(planting1);
		createSeeded(planting1);
		
		InventoryField planting2 = createPlanting(field, 2, cropYear, true);

		InventoryField planting3 = createBasePlanting(field, 3, cropYear);
		createEmptyUnseeded(planting3);
		createSeeded(planting3);
		createSeeded(planting3);
		
		InventoryField planting4 = createPlanting(field, 4, cropYear, true);
		
		InventoryField planting5 = createBasePlanting(field, 5, cropYear);
		createEmptyUnseeded(planting5);
		createSeeded(planting5);
		createEmptySeeded(planting5);
		
		InventoryField planting6 = createPlanting(field, 6, cropYear, true);

		InventoryField planting7 = createPlanting(field, 7, cropYear, false);
		createSeeded(planting7);
		createSeeded(planting7);
		createSeeded(planting7);
		
		
		List<InventoryField> plantings = new ArrayList<InventoryField>();
		plantings.add(planting1);
		plantings.add(planting2);
		plantings.add(planting3);
		plantings.add(planting4);
		plantings.add(planting5);
		plantings.add(planting6);
		plantings.add(planting7);
		
		field.setPlantings(plantings);
		
		invContract.setFields(fields);
	}

	private void createEmptyUnseeded(InventoryField planting) {
		InventoryUnseeded invUnseeded = new InventoryUnseeded();
		invUnseeded.setAcresToBeSeeded(null);
		invUnseeded.setCropCommodityId(null);
		invUnseeded.setCropCommodityName(null);
		invUnseeded.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		invUnseeded.setInventoryUnseededGuid(null);
		invUnseeded.setIsUnseededInsurableInd(true);

		planting.setInventoryUnseeded(invUnseeded);

	}
	
	private boolean checkEmptyUnseeded(InventoryUnseeded inventoryUnseeded) {
		return inventoryUnseeded.getAcresToBeSeeded() == null && inventoryUnseeded.getCropCommodityId() == null;
	}
	
	private void createEmptySeeded(InventoryField planting) {
		InventorySeededGrain isg = new InventorySeededGrain();
		isg.setCommodityTypeCode(null);
		isg.setCommodityTypeDesc(null);
		isg.setCropCommodityId(null);
		isg.setCropCommodityName(null);
		isg.setCropVarietyId(null);
		isg.setCropVarietyName(null);
		isg.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		isg.setInventorySeededGrainGuid(null);
		isg.setIsPedigreeInd(false);
		isg.setIsQuantityInsurableInd(false);
		isg.setIsReplacedInd(false);
		isg.setIsSpotLossInsurableInd(false);
		isg.setSeededAcres(null);
		isg.setSeededDate(null);
		
		planting.getInventorySeededGrains().add(isg);		
	}

	private boolean checkEmptyInventorySeededGrain(InventorySeededGrain inventorySeededGrain) {
		return inventorySeededGrain.getCommodityTypeCode() == null && 
				inventorySeededGrain.getCropCommodityId() == null && 
				inventorySeededGrain.getCropVarietyId() == null && 
				inventorySeededGrain.getSeededAcres() == null && 
				inventorySeededGrain.getSeededDate() == null;
	}
	
	
	private void createSeeded(InventoryField planting) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(cropYear, Calendar.JANUARY, 15);
		Date seededDate = cal.getTime();

		
		InventorySeededGrain invSeededGrain = new InventorySeededGrain();
		
		invSeededGrain.setCommodityTypeCode("CPSW");
		invSeededGrain.setCommodityTypeDesc("Canadian Prairie Spring Wheat");
		invSeededGrain.setCropCommodityId(26);
		invSeededGrain.setCropCommodityName("WHEAT");
		invSeededGrain.setCropVarietyId(1010602);
		invSeededGrain.setCropVarietyName("AAC ENTICE");
		invSeededGrain.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		invSeededGrain.setInventorySeededGrainGuid(null);
		invSeededGrain.setIsPedigreeInd(false);
		invSeededGrain.setIsSpotLossInsurableInd(false);
		invSeededGrain.setIsQuantityInsurableInd(false);
		invSeededGrain.setIsReplacedInd(false);
		invSeededGrain.setSeededAcres(12.34);
		invSeededGrain.setSeededDate(seededDate);
		
		planting.getInventorySeededGrains().add(invSeededGrain);
	}

	private InventoryField createBasePlanting(AnnualFieldRsrc field, Integer plantingNumber, Integer cropYear) {

		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(4);
		planting.setInventoryFieldGuid(null);
		planting.setIsHiddenOnPrintoutInd(false);
		planting.setLastYearCropCommodityId(20);
		planting.setLastYearCropCommodityName("FALL RYE");
		planting.setLastYearCropVarietyId(null);
		planting.setLastYearCropVarietyName(null);
		planting.setUnderseededAcres(14.4);
		planting.setUnderseededCropVarietyId(119);
		planting.setUnderseededCropVarietyName("ALFALFA");
		planting.setPlantingNumber(plantingNumber);

		return planting;
	}	
	
	
	private InventoryField createPlanting(AnnualFieldRsrc field, Integer plantingNumber, Integer cropYear, boolean includeSeededGrain) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(cropYear, Calendar.JANUARY, 15);
		Date seededDate = cal.getTime();

		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(4);
		planting.setInventoryFieldGuid(null);
		planting.setLastYearCropCommodityId(20);
		planting.setLastYearCropCommodityName("FALL RYE");
		planting.setLastYearCropVarietyId(null);
		planting.setLastYearCropVarietyName(null);
		planting.setIsHiddenOnPrintoutInd(false);
		planting.setPlantingNumber(plantingNumber);
		planting.setUnderseededAcres(14.4);
		planting.setUnderseededCropVarietyId(119);
		planting.setUnderseededCropVarietyName("ALFALFA");

		//InventoryUnseeded
		InventoryUnseeded invUnseeded = new InventoryUnseeded();
		invUnseeded.setAcresToBeSeeded(12.34);
		invUnseeded.setCropCommodityId(16);
		invUnseeded.setCropCommodityName("BARLEY");
		invUnseeded.setInventoryFieldGuid(null);
		invUnseeded.setInventoryUnseededGuid(null);
		invUnseeded.setIsUnseededInsurableInd(true);

		planting.setInventoryUnseeded(invUnseeded);

		if (includeSeededGrain) {
			//InventorySeededGrain
			InventorySeededGrain invSeededGrain = new InventorySeededGrain();
	
			invSeededGrain.setCommodityTypeCode("CPSW");
			invSeededGrain.setCommodityTypeDesc("Canadian Prairie Spring Wheat");
			invSeededGrain.setCropCommodityId(26);
			invSeededGrain.setCropCommodityName("WHEAT");
			invSeededGrain.setCropVarietyId(1010602);
			invSeededGrain.setCropVarietyName("AAC ENTICE");
			invSeededGrain.setInventoryFieldGuid(null);
			invSeededGrain.setInventorySeededGrainGuid(null);
			invSeededGrain.setIsPedigreeInd(false);
			invSeededGrain.setIsSpotLossInsurableInd(false);
			invSeededGrain.setIsQuantityInsurableInd(false);
			invSeededGrain.setIsReplacedInd(false);
			invSeededGrain.setSeededAcres(12.34);
			invSeededGrain.setSeededDate(seededDate);
			
			List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
			seededGrains.add(invSeededGrain);
			planting.setInventorySeededGrains(seededGrains);
		}

		return planting;
	}	

	private InventoryField createAdditionalPlanting(Integer fieldId, Integer plantingNumber) throws CirrasUnderwritingServiceException {

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date seededDate = cal.getTime();
		
		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(fieldId);
		planting.setInsurancePlanId(4);
		planting.setInventoryFieldGuid(null);
		planting.setLastYearCropCommodityId(20);
		planting.setLastYearCropCommodityName("FALL RYE");
		planting.setLastYearCropVarietyId(null);
		planting.setLastYearCropVarietyName(null);
		planting.setIsHiddenOnPrintoutInd(false);
		planting.setPlantingNumber(plantingNumber);
		planting.setUnderseededAcres(15.5);
		planting.setUnderseededCropVarietyId(119);
		planting.setUnderseededCropVarietyName("ALFALFA");

		//InventoryUnseeded
		InventoryUnseeded invUnseeded = new InventoryUnseeded();
		invUnseeded.setAcresToBeSeeded(12.34);
		invUnseeded.setCropCommodityId(18);
		invUnseeded.setCropCommodityName("CANOLA");
		invUnseeded.setInventoryFieldGuid(null);
		invUnseeded.setInventoryUnseededGuid(null);
		invUnseeded.setIsUnseededInsurableInd(true);
		invUnseeded.setIsCropInsuranceEligibleInd(true);
		invUnseeded.setIsInventoryCropInd(true);

		planting.setInventoryUnseeded(invUnseeded);

		//InventorySeededGrain
		InventorySeededGrain invSeededGrain = new InventorySeededGrain();

		invSeededGrain.setCommodityTypeCode("Argentine Canola");
		invSeededGrain.setCommodityTypeDesc("Argentine Canola");
		invSeededGrain.setCropCommodityId(18);
		invSeededGrain.setCropCommodityName("CANOLA");
		invSeededGrain.setCropVarietyId(1010491);
		invSeededGrain.setCropVarietyName("L233P");
		invSeededGrain.setInventoryFieldGuid(null);
		invSeededGrain.setInventorySeededGrainGuid(null);
		invSeededGrain.setIsPedigreeInd(false);
		invSeededGrain.setIsSpotLossInsurableInd(true);
		invSeededGrain.setIsQuantityInsurableInd(true);
		invSeededGrain.setIsReplacedInd(false);
		invSeededGrain.setSeededAcres(8.22);
		invSeededGrain.setSeededDate(seededDate);
		
		List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
		seededGrains.add(invSeededGrain);
		planting.setInventorySeededGrains(seededGrains);
		
		return planting;
	}
	
	
	
	private InventoryContractCommodity createNewInvCommodities(Integer cropId, String cropName, Double totalUnseededAcres
			, Double totalSeededAcres, Double totalSpotLossAcres) {
		//InventoryContractCommodity
		InventoryContractCommodity invContractCommodity = new InventoryContractCommodity();
		invContractCommodity.setCropCommodityId(cropId);
		invContractCommodity.setCropCommodityName(cropName);
		invContractCommodity.setInventoryContractGuid(null);
		invContractCommodity.setInventoryContractCommodityGuid(null);
		invContractCommodity.setTotalSeededAcres(totalSeededAcres);
		invContractCommodity.setTotalSpotLossAcres(totalSpotLossAcres);
		invContractCommodity.setTotalUnseededAcres(totalUnseededAcres);
		invContractCommodity.setTotalUnseededAcresOverride(56.78);
		invContractCommodity.setIsPedigreeInd(false);
		
		return invContractCommodity;
	}
	
	
	private AnnualFieldRsrc createNewFieldAndPlantingTest() throws CirrasUnderwritingServiceException {

		
		//AnnualField

		AnnualFieldRsrc field = new AnnualFieldRsrc();
		field.setLandUpdateType(LandUpdateTypes.NEW_LAND);
		field.setCropYear(2022);
		field.setDisplayOrder(88);
		field.setFieldLabel("UW API Unit Test 2112 1");
		field.setOtherLegalDescription("UW API Unit Test Other 2112 1");
		
		//UnderwritingComment
		UnderwritingComment underwritingComment = new UnderwritingComment();

		underwritingComment = new UnderwritingComment();
		underwritingComment.setAnnualFieldDetailId(null);
		underwritingComment.setUnderwritingComment("Comment for field " + field.getFieldId());
		underwritingComment.setUnderwritingCommentGuid(null);
		underwritingComment.setUnderwritingCommentTypeCode("INV");
		underwritingComment.setUnderwritingCommentTypeDesc("Inventory");		

		List<UnderwritingComment> uwComments = new ArrayList<UnderwritingComment>();
		uwComments.add(underwritingComment);

		underwritingComment = new UnderwritingComment();
		underwritingComment.setAnnualFieldDetailId(null);
		underwritingComment.setUnderwritingComment("Another comment for field " + field.getFieldId());
		underwritingComment.setUnderwritingCommentGuid(null);
		underwritingComment.setUnderwritingCommentTypeCode("INV");
		underwritingComment.setUnderwritingCommentTypeDesc("Inventory");		

		uwComments.add(underwritingComment);
		
		field.setUwComments(uwComments);

		
		InventoryField planting1 = createPlanting(field, 1, cropYear, true);
		InventoryField planting2 = createPlanting(field, 2, cropYear, true);
		

		List<InventoryField> plantings = new ArrayList<InventoryField>();
		plantings.add(planting1);
		plantings.add(planting2);
		
		field.setPlantings(plantings);
		
		return field;
	}
	
	private AnnualFieldRsrc addNewFieldToExistingLegal(
			Integer legalLandId,
			Integer cropYear
			) throws CirrasUnderwritingServiceException {

		//AnnualField

		AnnualFieldRsrc field = new AnnualFieldRsrc();
		field.setLandUpdateType(LandUpdateTypes.ADD_NEW_FIELD);
		field.setCropYear(cropYear);
		field.setFieldLabel("UW API Unit Test");
		field.setOtherLegalDescription("DL 2359");
		field.setLegalLandId(legalLandId);
		field.setDisplayOrder(88);
		
		return field;
	}	
	
	private AnnualFieldRsrc setFieldAndPlantingsForTransfer(
			EndpointsRsrc topLevelEndpoints,
			CirrasUnderwritingService service,
			String legalLandId,
			Integer cropYear,
			Integer fieldId
			) throws CirrasUnderwritingServiceException {

		AnnualFieldListRsrc searchResults = service.getAnnualFieldList(
				topLevelEndpoints, 
				legalLandId, 
				null, //fieldId
				null, 
				cropYear.toString());

		Assert.assertNotNull(searchResults);



		List<AnnualFieldRsrc> fields = searchResults.getCollection().stream()
				.filter(x -> x.getFieldId().equals(fieldId))
				.collect(Collectors.toList());
		Assert.assertNotNull(fields);
		
		//AnnualField
		AnnualFieldRsrc field = fields.get(0);
		
		Integer transferFromGrowerContractYearId = field.getPolicies().get(0).getGrowerContractYearId();
		
		String inventoryContractGuid = field.getPolicies().get(0).getInventoryContractGuid();
		
		//If the source contract is from the same year and the field is on there, take this field and plantings
		if(inventoryContractGuid != null && field.getPolicies().get(0).getCropYear().equals(cropYear)) {
			//Get inventory if it exists
			//uwContract.setInventoryContractGuid(inventoryContractGuid);

			InventoryContractRsrc invContract = getUwContractForField(topLevelEndpoints, service, field.getPolicies().get(0).getPolicyNumber());
			
			fields = invContract.getFields().stream()
					.filter(x -> x.getFieldId().equals(fieldId))
					.collect(Collectors.toList());
			
			if(fields != null) {
				field = fields.get(0);
			}
			
		} 
		
		if(field.getPlantings() != null && field.getPlantings().size() == 0) {
			InventoryField planting1 = createPlanting(field, 1, cropYear, true);
			InventoryField planting2 = createPlanting(field, 2, cropYear, true);

			List<InventoryField> plantings = new ArrayList<InventoryField>();
			plantings.add(planting1);
			plantings.add(planting2);
			
			field.setPlantings(plantings);
		}
	
		field.setTransferFromGrowerContractYearId(transferFromGrowerContractYearId);
		field.setLandUpdateType(LandUpdateTypes.ADD_EXISTING_LAND);
		field.setCropYear(cropYear);
		field.setDisplayOrder(7);

		return field;
	}
	
	private InventoryContractRsrc getUwContractForField(
			EndpointsRsrc topLevelEndpoints,
			CirrasUnderwritingService service,
			String policyNumber
			) throws CirrasUnderwritingServiceException {
		
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
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);
		
		InventoryContractRsrc invContract = service.getInventoryContract(uwContract);
		
		return invContract;
	}	

	private void checkInventoryContract(InventoryContract expected, InventoryContract actual) {

		Assert.assertNotNull("InventoryContractGuid", actual.getInventoryContractGuid());
		Assert.assertEquals("ContractId", expected.getContractId(), actual.getContractId());
		Assert.assertEquals("CropYear", expected.getCropYear(), actual.getCropYear());
		Assert.assertEquals("FertilizerInd", expected.getFertilizerInd(), actual.getFertilizerInd());
		Assert.assertEquals("GrainFromPrevYearInd", expected.getGrainFromPrevYearInd(), actual.getGrainFromPrevYearInd());
		Assert.assertEquals("HerbicideInd", expected.getHerbicideInd(), actual.getHerbicideInd());
		Assert.assertEquals("OtherChangesComment", expected.getOtherChangesComment(), actual.getOtherChangesComment());
		Assert.assertEquals("OtherChangesInd", expected.getOtherChangesInd(), actual.getOtherChangesInd());
		Assert.assertEquals("SeededCropReportSubmittedInd", expected.getSeededCropReportSubmittedInd(), actual.getSeededCropReportSubmittedInd());
		Assert.assertEquals("TilliageInd", expected.getTilliageInd(), actual.getTilliageInd());
		Assert.assertEquals("UnseededIntentionsSubmittedInd", expected.getUnseededIntentionsSubmittedInd(), actual.getUnseededIntentionsSubmittedInd());
		
	}

	private void checkInventoryContractCommodity(InventoryContractCommodity expected, InventoryContractCommodity actual) {
		Assert.assertNotNull("InventoryContractCommodityGuid", actual.getInventoryContractCommodityGuid());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertNotNull("InventoryContractGuid", actual.getInventoryContractGuid());
		Assert.assertEquals("TotalSeededAcres", expected.getTotalSeededAcres(), actual.getTotalSeededAcres());
		Assert.assertEquals("TotalSpotLossAcres", expected.getTotalSpotLossAcres(), actual.getTotalSpotLossAcres());
		Assert.assertEquals("TotalUnseededAcres", expected.getTotalUnseededAcres(), actual.getTotalUnseededAcres());
		Assert.assertEquals("TotalUnseededAcresOverride", expected.getTotalUnseededAcresOverride(), actual.getTotalUnseededAcresOverride());
		Assert.assertEquals("IsPedigreeInd", expected.getIsPedigreeInd(), actual.getIsPedigreeInd());
	}

	private void checkAnnualField(AnnualFieldRsrc expected, AnnualFieldRsrc actual) {
		Assert.assertEquals("ContractedFieldDetailId", expected.getContractedFieldDetailId(), actual.getContractedFieldDetailId());
	}

	private void checkInventoryField(InventoryField expected, InventoryField actual) {
		Assert.assertNotNull("InventoryFieldGuid", actual.getInventoryFieldGuid());
		Assert.assertEquals("CropYear", expected.getCropYear(), actual.getCropYear());
		Assert.assertEquals("FieldId", expected.getFieldId(), actual.getFieldId());
		Assert.assertEquals("InsurancePlanId", expected.getInsurancePlanId(), actual.getInsurancePlanId());
		Assert.assertEquals("LastYearCropCommodityId", expected.getLastYearCropCommodityId(), actual.getLastYearCropCommodityId());
		Assert.assertEquals("LastYearCropCommodityName", expected.getLastYearCropCommodityName(), actual.getLastYearCropCommodityName());
		Assert.assertEquals("LastYearCropVarietyId", expected.getLastYearCropVarietyId(), actual.getLastYearCropVarietyId());
		Assert.assertEquals("LastYearCropVarietyName", expected.getLastYearCropVarietyName(), actual.getLastYearCropVarietyName());
		Assert.assertEquals("PlantingNumber", expected.getPlantingNumber(), actual.getPlantingNumber());
		Assert.assertEquals("IsHiddenOnPrintoutInd", expected.getIsHiddenOnPrintoutInd(), actual.getIsHiddenOnPrintoutInd());
		Assert.assertEquals("UnderseededCropVarietyId", expected.getUnderseededCropVarietyId(), actual.getUnderseededCropVarietyId());
		Assert.assertEquals("UnderseededCropVarietyName", expected.getUnderseededCropVarietyName(), actual.getUnderseededCropVarietyName());
		Assert.assertEquals("UnderseededAcres", expected.getUnderseededAcres(), actual.getUnderseededAcres());
	}
	
	private void checkInventoryUnseeded(InventoryUnseeded expected, InventoryUnseeded actual) {
		Assert.assertNotNull("InventoryUnseededGuid", actual.getInventoryUnseededGuid());
		Assert.assertEquals("AcresToBeSeeded", expected.getAcresToBeSeeded(), actual.getAcresToBeSeeded());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertNotNull("InventoryFieldGuid", actual.getInventoryFieldGuid());
		Assert.assertEquals("IsUnseededInsurableInd", expected.getIsUnseededInsurableInd(), actual.getIsUnseededInsurableInd());
	}
	
	private void checkInventorySeededGrain(InventorySeededGrain expected, InventorySeededGrain actual) {
		Assert.assertNotNull("InventorySeededGrainGuid", actual.getInventorySeededGrainGuid());
		Assert.assertEquals("CommodityTypeCode", expected.getCommodityTypeCode(), actual.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDesc", expected.getCommodityTypeDesc(), actual.getCommodityTypeDesc());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("CropVarietyId", expected.getCropVarietyId(), actual.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", expected.getCropVarietyName(), actual.getCropVarietyName());
		Assert.assertNotNull("InventoryFieldGuid", actual.getInventoryFieldGuid());
		Assert.assertEquals("IsPedigreeInd", expected.getIsPedigreeInd(), actual.getIsPedigreeInd());
		Assert.assertEquals("IsSpotLossInsurableInd", expected.getIsSpotLossInsurableInd(), actual.getIsSpotLossInsurableInd());
		Assert.assertEquals("IsQuantityInsurableInd", expected.getIsQuantityInsurableInd(), actual.getIsQuantityInsurableInd());
		Assert.assertEquals("IsReplacedInd", expected.getIsReplacedInd(), actual.getIsReplacedInd());
		Assert.assertEquals("SeededAcres", expected.getSeededAcres(), actual.getSeededAcres());
		Assert.assertEquals("SeededDate", expected.getSeededDate(), actual.getSeededDate());
	}

	private List<UnderwritingComment> sortUwCommentsByComment(List<UnderwritingComment> uwComments) {

		List<UnderwritingComment> sortedComments = uwComments.stream().sorted(new Comparator<UnderwritingComment>() {
			@Override
			public int compare(UnderwritingComment uc1, UnderwritingComment uc2) {
				return uc1.getUnderwritingComment().compareTo(uc2.getUnderwritingComment());
			}
		}).collect(Collectors.toList());
		
		return sortedComments;
	}
	
	private void checkUnderwritingComment(UnderwritingComment expected, UnderwritingComment actual) {
		Assert.assertNotNull("UnderwritingCommentGuid", actual.getUnderwritingCommentGuid());
		Assert.assertNotNull("AnnualFieldDetailId", actual.getAnnualFieldDetailId());
		Assert.assertEquals("UnderwritingComment", expected.getUnderwritingComment(), actual.getUnderwritingComment());
		Assert.assertEquals("UnderwritingCommentTypeCode", expected.getUnderwritingCommentTypeCode(), actual.getUnderwritingCommentTypeCode());
		Assert.assertEquals("UnderwritingCommentTypeDesc", expected.getUnderwritingCommentTypeDesc(), actual.getUnderwritingCommentTypeDesc());
		Assert.assertTrue("UserCanEditInd", actual.getUserCanEditInd());
	}

/*
The following SQL DELETE scripts can be used to remove all inventory_contract and related data from a policy.
It can be run from psql by setting the v_cn_id and v_crop_year variables.


delete
from underwriting_comment t
where t.annual_field_detail_id IN (SELECT afd.annual_field_detail_id
from annual_field_detail afd
join contracted_field_detail cfd ON cfd.annual_field_detail_id = afd.annual_field_detail_id
join grower_contract_year gcy ON gcy.grower_contract_year_id = cfd.grower_contract_year_id
WHERE gcy.contract_id = :v_cn_id
AND gcy.crop_year = :v_crop_year
);
 
delete
FROM inventory_contract_commodity c
WHERE c.inventory_contract_guid IN (SELECT inventory_contract_guid
FROM inventory_contract t
WHERE t.contract_id = :v_cn_id
AND t.crop_year = :v_crop_year);
 
delete
FROM inventory_contract t
WHERE t.contract_id = :v_cn_id
AND t.crop_year = :v_crop_year;
 
delete
from inventory_unseeded u
where u.inventory_field_guid IN 
(select inv.inventory_field_guid
from annual_field_detail afd
join field f on f.field_id = afd.field_id
join inventory_field inv on inv.field_id = f.field_id and inv.crop_year = afd.crop_year
join contracted_field_detail cfd ON cfd.annual_field_detail_id = afd.annual_field_detail_id
join grower_contract_year gcy ON gcy.grower_contract_year_id = cfd.grower_contract_year_id
WHERE gcy.contract_id = :v_cn_id
AND gcy.crop_year = :v_crop_year
);
 
delete
from inventory_seeded_grain g
where g.inventory_field_guid IN 
(select inv.inventory_field_guid
from annual_field_detail afd
join field f on f.field_id = afd.field_id
join inventory_field inv on inv.field_id = f.field_id and inv.crop_year = afd.crop_year
join contracted_field_detail cfd ON cfd.annual_field_detail_id = afd.annual_field_detail_id
join grower_contract_year gcy ON gcy.grower_contract_year_id = cfd.grower_contract_year_id
WHERE gcy.contract_id = :v_cn_id
AND gcy.crop_year = :v_crop_year
);
 
delete
from inventory_field inv
where inv.field_id IN (select f.field_id
from annual_field_detail afd
join field f on f.field_id = afd.field_id
join contracted_field_detail cfd ON cfd.annual_field_detail_id = afd.annual_field_detail_id
join grower_contract_year gcy ON gcy.grower_contract_year_id = cfd.grower_contract_year_id
WHERE gcy.contract_id = :v_cn_id
AND gcy.crop_year = :v_crop_year
)
and inv.crop_year = :v_crop_year;

delete
from field_underseeded_inventory fui
where fui.field_id IN (select f.field_id
from annual_field_detail afd
join field f on f.field_id = afd.field_id
join contracted_field_detail cfd ON cfd.annual_field_detail_id = afd.annual_field_detail_id
join grower_contract_year gcy ON gcy.grower_contract_year_id = cfd.grower_contract_year_id
WHERE gcy.contract_id = :v_cn_id
AND gcy.crop_year = :v_crop_year
)
and fui.crop_year = :v_crop_year;


 */
	
}
