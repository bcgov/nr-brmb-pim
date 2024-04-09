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

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldListRsrc;
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
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryCoverageTotalForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LinkedPlanting;
import ca.bc.gov.mal.cirras.underwriting.model.v1.PolicySimple;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandUpdateTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class InventoryContractEndpointLandTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(InventoryContractEndpointLandTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.SEARCH_ANNUAL_FIELDS,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.DELETE_INVENTORY_CONTRACT,
		Scopes.GET_INVENTORY_CONTRACT,
		Scopes.UPDATE_INVENTORY_CONTRACT,
		Scopes.PRINT_INVENTORY_CONTRACT,
		Scopes.CREATE_SYNC_UNDERWRITING,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_GROWER,
		Scopes.GET_POLICY,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND
	};


	private static final String commentText = "Comment for field1";

	private Integer cropYear1 = 2021;
	
	private Integer growerId1 = 90001501;
	private Integer growerNumber1 = 999888;
	
	private Integer policyId1 = 90001502;
	private Integer gcyId1 = 90001503;
	private Integer contractId1 = 90001504;
	private String policyNumber1 = "998874-21";
	private String contractNumber1 = "998874";

	private Integer policyId2 = 90002502;
	private Integer gcyId2 = 90002503;
	private Integer contractId2 = 90002504;
	private String policyNumber2 = "998815-21";
	private String contractNumber2 = "998815";
	
	private Integer legalLandId1 = 90001505;
	private Integer fieldId1 = 90001506;
	private Integer annualFieldDetailId1 = 90001507;
	private Integer contractedFieldDetailId1 = 90001508;

	private Integer fieldId2 = 90001516;
	private Integer annualFieldDetailId2 = 90001517;
	private Integer contractedFieldDetailId2 = 90001518;

	private Integer contractedFieldDetailId3 = 90001522;

	private Integer insurancePlanIdForage = 5;
	private Integer insurancePlanIdGrain = 4;
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

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

	
	private void delete() throws CirrasUnderwritingServiceException {
		
		deleteInventoryContract(policyNumber1);
		deleteInventoryContract(policyNumber2);
		
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId3.toString());

		deleteContractedFieldDetailAddedViaInvCn(policyNumber2);
		
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId2.toString());

		service.deleteField(topLevelEndpoints, fieldId1.toString());
		service.deleteField(topLevelEndpoints, fieldId2.toString());

		service.deleteLegalLandSync(topLevelEndpoints, legalLandId1.toString());
		
		service.deleteGrowerContractYear(topLevelEndpoints, gcyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId1.toString());

		service.deleteGrowerContractYear(topLevelEndpoints, gcyId2.toString());
		service.deletePolicy(topLevelEndpoints, policyId2.toString());

		service.deleteGrower(topLevelEndpoints, growerId1.toString());
	}

	public void deleteInventoryContract(String policyNumber)
			throws CirrasUnderwritingServiceException {
		
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

	public void deleteContractedFieldDetailAddedViaInvCn(String policyNumber) throws CirrasUnderwritingServiceException {
		
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

		List<AnnualFieldRsrc> fetchedFields = null;
		if ( searchResults != null && searchResults.getCollection().size() > 0 ) {
			UwContractRsrc referrer = searchResults.getCollection().get(0);

			if ( referrer.getInventoryContractGuid() == null ) { 
				InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);

				// Rolling over an InventoryContract is currently the only way to get the list of fields associated with a policy.
				fetchedFields = invContract.getFields();
			}
		}

		if ( fetchedFields != null ) { 
			for ( AnnualFieldRsrc fetchedField : fetchedFields ) { 
				service.deleteContractedFieldDetail(topLevelEndpoints, fetchedField.getContractedFieldDetailId().toString());
			}
		}		
	}
	
	
	@Test
	public void testInventoryContractRemoveDeleteFieldForage() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInventoryContractRemoveDeleteFieldForage");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		createPolicy(insurancePlanIdForage, policyId1, policyNumber1, contractNumber1, contractId1);
		createGrowerContractYear(insurancePlanIdForage, gcyId1, contractId1);

		createLegalLand();
		//Add field 1
		createField(fieldId1);
		createAnnualFieldDetail(fieldId1, annualFieldDetailId1);
		createContractedFieldDetail(annualFieldDetailId1, contractedFieldDetailId1, gcyId1, 1);
		//Add field 2
		createField(fieldId2);
		createAnnualFieldDetail(fieldId2, annualFieldDetailId2);
		createContractedFieldDetail(annualFieldDetailId2, contractedFieldDetailId2, gcyId1, 2);
		
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
				policyNumber1,
				null,
				null, 
				null, 
				null, 
				pageNumber, 
				pageRowCount);
		
		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(0, invContract.getInventoryCoverageTotalForages().size());

		Assert.assertEquals(2, invContract.getFields().size());
		
		AnnualFieldRsrc field1 = getField(fieldId1, invContract.getFields());

		// Remove default planting.
		field1.getPlantings().remove(0);
		
		// Planting 1
		InventoryField planting = createPlanting(field1, 1, cropYear1, insurancePlanIdForage);
		createInventorySeededForage(planting, 65, "E2", true, 12.0, null); //FORAGE Establishment 2
		addComment(field1);

		//Field 2
		AnnualFieldRsrc field2 = getField(fieldId2, invContract.getFields());

		// Remove default planting.
		field2.getPlantings().remove(0);
		
		// Planting 1
		planting = createPlanting(field2, 3, cropYear1, insurancePlanIdForage);
		createInventorySeededForage(planting, 65, "E2", true, 10.0, null); //FORAGE Establishment 2

		// Planting 2
		planting = createPlanting(field2, 2, cropYear1, insurancePlanIdForage);
		createInventorySeededForage(planting, 65, "W3", true, 11.0, null); //FORAGE Winter Survival 3

		// Forage Totals
		List<InventoryCoverageTotalForage> expectedTotals = new ArrayList<InventoryCoverageTotalForage>();
		InventoryCoverageTotalForage ictf = createInventoryCovarageTotalForage(invContract.getInventoryContractGuid(), 65, "FORAGE", null, null, 33.0, false);
		expectedTotals.add(ictf);

		ictf = createInventoryCovarageTotalForage(invContract.getInventoryContractGuid(), null, null, "E2", "Establishment 2", 22.0, false);
		expectedTotals.add(ictf);
		
		ictf = createInventoryCovarageTotalForage(invContract.getInventoryContractGuid(), null, null, "W3", "Winter Survival 3", 11.0, false);
		expectedTotals.add(ictf);

		//Create contract
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertEquals(expectedTotals.size(), fetchedInvContract.getInventoryCoverageTotalForages().size());

		//Check totals
		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryCoverageTotalForage(expectedTotals.get(i), fetchedInvContract.getInventoryCoverageTotalForages().get(i));
		}

		//Check comment and remove field1
		field1 = getField(fieldId1, fetchedInvContract.getFields());
		Assert.assertEquals(1, field1.getUwComments().size());
		Assert.assertEquals(commentText, field1.getUwComments().get(0).getUnderwritingComment());
		field1.setLandUpdateType(LandUpdateTypes.REMOVE_FIELD_FROM_POLICY);

		field2 = getField(fieldId2, fetchedInvContract.getFields());
		field2.setDisplayOrder(1);

		// Update expected totals.
		// FORAGE
		expectedTotals.get(0).setTotalFieldAcres(21.0);

		// Establishment 2
		expectedTotals.get(1).setTotalFieldAcres(10.0);

		InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);
		
		Assert.assertEquals(1, updatedInvContract.getFields().size());
		//Check displayorder
		Assert.assertEquals(1, updatedInvContract.getFields().get(0).getDisplayOrder().intValue());

		//Check totals
		Assert.assertEquals(expectedTotals.size(), updatedInvContract.getInventoryCoverageTotalForages().size());

		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryCoverageTotalForage(expectedTotals.get(i), updatedInvContract.getInventoryCoverageTotalForages().get(i));
		}
		
		//Add field1 again
		AnnualFieldListRsrc fieldList = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				fieldId1.toString(),
				cropYear1.toString());

		Assert.assertNotNull(fieldList);
		AnnualFieldRsrc field = fieldList.getCollection().get(0);
		
		//Rollover plantings
		AnnualFieldRsrc rolledoverField = service.rolloverAnnualFieldInventory(field, cropYear1.toString(), insurancePlanIdForage.toString()); 

		
		rolledoverField.setTransferFromGrowerContractYearId(null);
		rolledoverField.setLandUpdateType(LandUpdateTypes.ADD_EXISTING_LAND);
		rolledoverField.setCropYear(cropYear1);
		rolledoverField.setDisplayOrder(2);
		
		updatedInvContract.getFields().add(rolledoverField);

		updatedInvContract = service.updateInventoryContract(updatedInvContract.getInventoryContractGuid(), updatedInvContract);

		// Update expected totals.
		// FORAGE
		expectedTotals.get(0).setTotalFieldAcres(33.0);
		// Establishment 2
		expectedTotals.get(1).setTotalFieldAcres(22.0);

		//Expect 2 fields
		Assert.assertEquals(2, updatedInvContract.getFields().size());

		//Check totals
		Assert.assertEquals(expectedTotals.size(), updatedInvContract.getInventoryCoverageTotalForages().size());

		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryCoverageTotalForage(expectedTotals.get(i), updatedInvContract.getInventoryCoverageTotalForages().get(i));
		}
		
		//Delete field1
		field1 = getField(fieldId1, updatedInvContract.getFields());
		Assert.assertEquals(1, field1.getUwComments().size());
		Assert.assertEquals(commentText, field1.getUwComments().get(0).getUnderwritingComment());
		field1.setLandUpdateType(LandUpdateTypes.DELETE_FIELD);

		updatedInvContract = service.updateInventoryContract(updatedInvContract.getInventoryContractGuid(), updatedInvContract);

		// Update expected totals.
		// FORAGE
		expectedTotals.get(0).setTotalFieldAcres(21.0);
		// Establishment 2
		expectedTotals.get(1).setTotalFieldAcres(10.0);

		//Expect 1 field
		Assert.assertEquals(1, updatedInvContract.getFields().size());

		//Check totals
		Assert.assertEquals(expectedTotals.size(), updatedInvContract.getInventoryCoverageTotalForages().size());

		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryCoverageTotalForage(expectedTotals.get(i), updatedInvContract.getInventoryCoverageTotalForages().get(i));
		}
				
		//Search field1: should not find it
		AnnualFieldListRsrc fieldList2 = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				fieldId1.toString(),
				cropYear1.toString());

		Assert.assertNotNull(fieldList2);
		Assert.assertEquals(0, fieldList2.getCollection().size());
		
		delete();
		
		logger.debug(">testInventoryContractRemoveDeleteFieldForage");
	}
	
	@Test
	public void testInventoryContractRemoveDeleteFieldGrain() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInventoryContractRemoveDeleteFieldGrain");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		createPolicy(insurancePlanIdGrain, policyId1, policyNumber1, contractNumber1, contractId1);
		createGrowerContractYear(insurancePlanIdGrain, gcyId1, contractId1);

		createLegalLand();
		//Add field 1
		createField(fieldId1);
		createAnnualFieldDetail(fieldId1, annualFieldDetailId1);
		createContractedFieldDetail(annualFieldDetailId1, contractedFieldDetailId1, gcyId1, 1);
		//Add field 2
		createField(fieldId2);
		createAnnualFieldDetail(fieldId2, annualFieldDetailId2);
		createContractedFieldDetail(annualFieldDetailId2, contractedFieldDetailId2, gcyId1, 2);
		
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
				policyNumber1,
				null,
				null, 
				null, 
				null, 
				pageNumber, 
				pageRowCount);
		
		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() == 1);

		UwContractRsrc uwContract = searchResults.getCollection().get(0);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(0, invContract.getCommodities().size());

		Assert.assertEquals(2, invContract.getFields().size());
		
		AnnualFieldRsrc field1 = getField(fieldId1, invContract.getFields());

		// Remove default planting.
		field1.getPlantings().remove(0);
		
		// Planting 1
		InventoryField planting = createPlanting(field1, 1, cropYear1, insurancePlanIdGrain);
		createInventoryUnseeded(planting, 16, true, 100.0); //Barley

		//Field 2
		AnnualFieldRsrc field2 = getField(fieldId2, invContract.getFields());

		// Remove default planting.
		field2.getPlantings().remove(0);
		
		// Planting 1
		planting = createPlanting(field2, 1, cropYear1, insurancePlanIdGrain);
		createInventoryUnseeded(planting, 16, true, 50.0); //Barley

		// Planting 2
		planting = createPlanting(field2, 2, cropYear1, insurancePlanIdGrain);
		createInventoryUnseeded(planting, 26, true, 25.0); //Wheat


		// Grain Totals
		List<InventoryContractCommodity> expectedTotals = new ArrayList<InventoryContractCommodity>();
		InventoryContractCommodity icc = createInvCommodities(16, "BARLEY", 150.0);
		expectedTotals.add(icc);
		invContract.getCommodities().add(icc);
		
		icc = createInvCommodities(26, "WHEAT", 25.0);
		expectedTotals.add(icc);
		invContract.getCommodities().add(icc);
		
		//Create contract
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		//Check totals
		Assert.assertEquals(invContract.getCommodities().size(), fetchedInvContract.getCommodities().size());
		for (int i = 0; i < invContract.getCommodities().size(); i++) {
			checkInventoryContractCommodity(invContract.getCommodities().get(i), fetchedInvContract.getCommodities().get(i));
		}

		//Remove field1
		field1 = getField(fieldId1, fetchedInvContract.getFields());
		field1.setLandUpdateType(LandUpdateTypes.REMOVE_FIELD_FROM_POLICY);

		field2 = getField(fieldId2, fetchedInvContract.getFields());
		field2.setDisplayOrder(1);

		// Update expected totals.
		// Barley
		expectedTotals.get(0).setTotalUnseededAcres(50.0);

		InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);
		
		Assert.assertEquals(1, updatedInvContract.getFields().size());
		//Check displayorder
		Assert.assertEquals(1, updatedInvContract.getFields().get(0).getDisplayOrder().intValue());

		//Check totals
		Assert.assertEquals(expectedTotals.size(), updatedInvContract.getCommodities().size());
		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryContractCommodity(expectedTotals.get(i), updatedInvContract.getCommodities().get(i));
		}

		//Add field1 again
		AnnualFieldListRsrc fieldList = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				fieldId1.toString(),
				cropYear1.toString());

		Assert.assertNotNull(fieldList);
		AnnualFieldRsrc field = fieldList.getCollection().get(0);
		
		//Rollover plantings
		AnnualFieldRsrc rolledoverField = service.rolloverAnnualFieldInventory(field, cropYear1.toString(), insurancePlanIdGrain.toString()); 

		
		rolledoverField.setTransferFromGrowerContractYearId(null);
		rolledoverField.setLandUpdateType(LandUpdateTypes.ADD_EXISTING_LAND);
		rolledoverField.setCropYear(cropYear1);
		rolledoverField.setDisplayOrder(2);
		
		updatedInvContract.getFields().add(rolledoverField);

		updatedInvContract = service.updateInventoryContract(updatedInvContract.getInventoryContractGuid(), updatedInvContract);

		// Update expected totals.
		// Barley
		expectedTotals.get(0).setTotalUnseededAcres(150.0);

		//Expect 2 fields
		Assert.assertEquals(2, updatedInvContract.getFields().size());

		//Check totals
		Assert.assertEquals(expectedTotals.size(), updatedInvContract.getCommodities().size());
		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryContractCommodity(expectedTotals.get(i), updatedInvContract.getCommodities().get(i));
		}

		//Delete field1
		field1 = getField(fieldId1, updatedInvContract.getFields());
		field1.setLandUpdateType(LandUpdateTypes.DELETE_FIELD);

		updatedInvContract = service.updateInventoryContract(updatedInvContract.getInventoryContractGuid(), updatedInvContract);

		// Update expected totals.
		// Barley
		expectedTotals.get(0).setTotalUnseededAcres(50.0);

		//Expect 1 field
		Assert.assertEquals(1, updatedInvContract.getFields().size());

		//Check totals
		Assert.assertEquals(expectedTotals.size(), updatedInvContract.getCommodities().size());
		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryContractCommodity(expectedTotals.get(i), updatedInvContract.getCommodities().get(i));
		}
		
		//Search field1: should not find it
		AnnualFieldListRsrc fieldList2 = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				fieldId1.toString(),
				cropYear1.toString());

		Assert.assertNotNull(fieldList2);
		Assert.assertEquals(0, fieldList2.getCollection().size());
		
		delete();
		
		logger.debug(">testInventoryContractRemoveDeleteFieldGrain");
	}	

	
	@Test
	public void testAddFieldToMultiplePolicies() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testAddFieldToMultiplePolicies");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		//Grain policy
		createPolicy(insurancePlanIdGrain, policyId1, policyNumber1, contractNumber1, contractId1);
		createGrowerContractYear(insurancePlanIdGrain, gcyId1, contractId1);
		//Forage policy
		createPolicy(insurancePlanIdForage, policyId2, policyNumber2, contractNumber2, contractId2);
		createGrowerContractYear(insurancePlanIdForage, gcyId2, contractId2);

		createLegalLand();
		//Add field 1
		createField(fieldId1);
		createAnnualFieldDetail(fieldId1, annualFieldDetailId1);
		createContractedFieldDetail(annualFieldDetailId1, contractedFieldDetailId1, gcyId1, 1);

		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		UwContractRsrc uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(0, invContract.getCommodities().size());

		Assert.assertEquals(1, invContract.getFields().size());
		
		AnnualFieldRsrc field1 = getField(fieldId1, invContract.getFields());

		// Remove default planting.
		field1.getPlantings().remove(0);
		
		// Planting 1
		InventoryField planting = createPlanting(field1, 1, cropYear1, insurancePlanIdGrain);
		createInventoryUnseeded(planting, 16, true, 100.0); //Barley

		// Grain Totals
		List<InventoryContractCommodity> expectedTotals = new ArrayList<InventoryContractCommodity>();
		InventoryContractCommodity icc = createInvCommodities(16, "BARLEY", 100.0);
		expectedTotals.add(icc);
		invContract.getCommodities().add(icc);
		
		//Create contract
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		//Check totals
		Assert.assertEquals(invContract.getCommodities().size(), fetchedInvContract.getCommodities().size());
		for (int i = 0; i < invContract.getCommodities().size(); i++) {
			checkInventoryContractCommodity(invContract.getCommodities().get(i), fetchedInvContract.getCommodities().get(i));
		}

		//*************************************************
		//Get Forage policy
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);

		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(0, invContract.getCommodities().size());
		Assert.assertEquals(0, invContract.getFields().size());

		//Add field to forage policy
		AnnualFieldListRsrc fieldList = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				fieldId1.toString(),
				cropYear1.toString());

		Assert.assertNotNull(fieldList);

		//AnnualField
		AnnualFieldRsrc field = fieldList.getCollection().get(0);

		field.setTransferFromGrowerContractYearId(null);
		field.setLandUpdateType(LandUpdateTypes.ADD_EXISTING_LAND);
		field.setCropYear(cropYear1);
		field.setDisplayOrder(1);
		
		invContract.getFields().add(field);
		
		// Planting 1
		planting = createPlanting(field, 1, cropYear1, insurancePlanIdForage);
		createInventorySeededForage(planting, 65, "E2", true, 10.0, null); //FORAGE Establishment 2

		// Planting 2
		planting = createPlanting(field, 2, cropYear1, insurancePlanIdForage);
		createInventorySeededForage(planting, 65, "W3", true, 11.0, null); //FORAGE Winter Survival 3

		// Forage Totals
		List<InventoryCoverageTotalForage> expectedForageTotals = new ArrayList<InventoryCoverageTotalForage>();
		InventoryCoverageTotalForage ictf = createInventoryCovarageTotalForage(invContract.getInventoryContractGuid(), 65, "FORAGE", null, null, 21.0, false);
		expectedForageTotals.add(ictf);

		ictf = createInventoryCovarageTotalForage(invContract.getInventoryContractGuid(), null, null, "E2", "Establishment 2", 10.0, false);
		expectedForageTotals.add(ictf);
		
		ictf = createInventoryCovarageTotalForage(invContract.getInventoryContractGuid(), null, null, "W3", "Winter Survival 3", 11.0, false);
		expectedForageTotals.add(ictf);

		//Create contract
		fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertEquals(1, fetchedInvContract.getFields().size());

		Assert.assertEquals(expectedForageTotals.size(), fetchedInvContract.getInventoryCoverageTotalForages().size());

		//Check totals
		for (int i = 0; i < expectedForageTotals.size(); i++) {
			checkInventoryCoverageTotalForage(expectedForageTotals.get(i), fetchedInvContract.getInventoryCoverageTotalForages().get(i));
		}
		
		//Check insurance plan of crops
		for (int i = 0; i < fetchedInvContract.getFields().size(); i++) {
			for (int j = 0; j < fetchedInvContract.getFields().get(i).getPlantings().size(); j++) {
				Assert.assertEquals(insurancePlanIdForage, fetchedInvContract.getFields().get(i).getPlantings().get(j).getInsurancePlanId());
			}
		}
		
		//Remove field1
		field1 = getField(fieldId1, fetchedInvContract.getFields());
		field1.setLandUpdateType(LandUpdateTypes.REMOVE_FIELD_FROM_POLICY);

		InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);
		
		Assert.assertEquals(0, updatedInvContract.getFields().size());
		
		//*************************************************
		//Get Grain policy again
		//*************************************************
		uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc grainContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(1, grainContract.getFields().size());
		
		field1 = getField(fieldId1, grainContract.getFields());

		Assert.assertEquals(expectedTotals.size(), grainContract.getCommodities().size());
		
		//Check totals
		for (int i = 0; i < expectedTotals.size(); i++) {
			checkInventoryContractCommodity(expectedTotals.get(i), grainContract.getCommodities().get(i));
		}	
		
		//Check insurance plan of crops
		for (int i = 0; i < grainContract.getFields().size(); i++) {
			for (int j = 0; j < grainContract.getFields().get(i).getPlantings().size(); j++) {
				Assert.assertEquals(insurancePlanIdGrain, grainContract.getFields().get(i).getPlantings().get(j).getInsurancePlanId());
			}
		}

		delete();
		
		logger.debug(">testAddFieldToMultiplePolicies");
	}
	
	@Test
	public void testLinkUnlinkPlantings() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testLinkUnlinkPlantings");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		//Grain policy
		createPolicy(insurancePlanIdGrain, policyId1, policyNumber1, contractNumber1, contractId1);
		createGrowerContractYear(insurancePlanIdGrain, gcyId1, contractId1);
		//Forage policy
		createPolicy(insurancePlanIdForage, policyId2, policyNumber2, contractNumber2, contractId2);
		createGrowerContractYear(insurancePlanIdForage, gcyId2, contractId2);

		createLegalLand();
		//Add field 1
		createField(fieldId1);
		createAnnualFieldDetail(fieldId1, annualFieldDetailId1);
		createContractedFieldDetail(annualFieldDetailId1, contractedFieldDetailId1, gcyId1, 1);

		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//*************************************************
		//Get Grain Policy
		//*************************************************
		UwContractRsrc uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(1, invContract.getFields().size());
		
		AnnualFieldRsrc field1 = getField(fieldId1, invContract.getFields());

		// Remove default planting.
		field1.getPlantings().remove(0);
		
		// Planting 1
		InventoryField planting = createPlanting(field1, 1, cropYear1, insurancePlanIdGrain);
		createInventoryUnseeded(planting, 16, true, 100.0); //Barley

		// Grain Totals
		List<InventoryContractCommodity> expectedTotals = new ArrayList<InventoryContractCommodity>();
		InventoryContractCommodity icc = createInvCommodities(16, "BARLEY", 100.0);
		expectedTotals.add(icc);
		invContract.getCommodities().add(icc);
		
		//Create contract
		InventoryContractRsrc grainInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		String grainInventoryFieldGuid = grainInvContract.getFields().get(0).getPlantings().get(0).getInventoryFieldGuid();

		//*************************************************
		//Get Forage policy
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(0, invContract.getFields().size());

		//Add field to forage policy
		AnnualFieldListRsrc fieldList = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				fieldId1.toString(),
				cropYear1.toString());

		Assert.assertNotNull(fieldList);

		//AnnualField
		AnnualFieldRsrc field = fieldList.getCollection().get(0);

		field.setTransferFromGrowerContractYearId(null);
		field.setLandUpdateType(LandUpdateTypes.ADD_EXISTING_LAND);
		field.setCropYear(cropYear1);
		field.setDisplayOrder(1);
		
		invContract.getFields().add(field);
		
		// Planting 1
		planting = createPlanting(field, 1, cropYear1, insurancePlanIdForage);
		createInventorySeededForage(planting, 65, "E2", true, 10.0, null); //FORAGE Establishment 2

		// Planting 2 -> Linked planting
		planting = createPlanting(field, 2, cropYear1, insurancePlanIdForage);
		createInventorySeededForage(planting, 65, "W3", true, 11.0, grainInventoryFieldGuid); //FORAGE Winter Survival 3

		//Create contract
		InventoryContractRsrc forageInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertEquals(1, forageInvContract.getFields().size());
		Assert.assertEquals(2, forageInvContract.getFields().get(0).getPlantings().size());

		//Set the GUID of the seeded forage record that is linked
		String inventorySeededForageGuid = null;
		InventorySeededForage foragePlanting = null;
		for (InventoryField invField : forageInvContract.getFields().get(0).getPlantings()) {
			if (invField.getPlantingNumber() == 2) {
				inventorySeededForageGuid = invField.getInventorySeededForages().get(0).getInventorySeededForageGuid();
				foragePlanting = invField.getInventorySeededForages().get(0);
			}
		}
		Assert.assertNotNull(foragePlanting);
		
		
		//*************************************************
		//Grain policy: Check if plantings are linked
		//*************************************************
		uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		grainInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(1, grainInvContract.getFields().size());
		Assert.assertEquals(1, grainInvContract.getFields().get(0).getPlantings().size());
		//Check if it's linked
		InventoryField grainPlanting = grainInvContract.getFields().get(0).getPlantings().get(0);
		LinkedPlanting linkedPlanting = grainPlanting.getLinkedPlanting();
		Assert.assertNotNull(linkedPlanting);
		//Check values of the linked planting
		Assert.assertEquals(inventorySeededForageGuid, linkedPlanting.getUnderseededInventorySeededForageGuid());
		Assert.assertEquals(foragePlanting.getCropVarietyId(), linkedPlanting.getCropVarietyId());
		Assert.assertEquals(foragePlanting.getCropVarietyName(), linkedPlanting.getVarietyName());
		Assert.assertEquals(foragePlanting.getFieldAcres(), linkedPlanting.getAcres());
		
		//Linked policies
		Assert.assertNotNull(grainInvContract.getFields().get(0).getPolicies());
		PolicySimple policy = grainInvContract.getFields().get(0).getPolicies().get(0);
		Assert.assertEquals(policyNumber2, policy.getPolicyNumber());
		Assert.assertNotNull(policy.getInventoryContractGuid());
		Assert.assertNotNull(policy.getGrowerName());
		Assert.assertNotNull(policy.getGrowerNumber());

		//*************************************************
		//FORAGE policy: Check linked field/policy data and remove link
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		forageInvContract = service.getInventoryContract(uwContract);

		//Get linked planting
		Assert.assertEquals(1, forageInvContract.getFields().size());
		Assert.assertEquals(2, forageInvContract.getFields().get(0).getPlantings().size());
		field1 = getField(fieldId1, forageInvContract.getFields());
		InventorySeededForage invSeededForage = getSeededForage(inventorySeededForageGuid, field1);
		Assert.assertNotNull(invSeededForage);
		linkedPlanting = invSeededForage.getLinkedPlanting();
		Assert.assertNotNull(linkedPlanting);
		//Check values of linked planting
		Assert.assertEquals(grainPlanting.getInventoryFieldGuid(), linkedPlanting.getInventoryFieldGuid());
		Assert.assertEquals(grainPlanting.getUnderseededCropVarietyId(), linkedPlanting.getCropVarietyId());
		Assert.assertEquals(grainPlanting.getUnderseededCropVarietyName(), linkedPlanting.getVarietyName());
		Assert.assertEquals(grainPlanting.getUnderseededAcres(), linkedPlanting.getAcres());
		
		//Linked policies
		Assert.assertNotNull(field1.getPolicies());
		Assert.assertEquals(policyNumber1, field1.getPolicies().get(0).getPolicyNumber());

		//Remove link 
		invSeededForage.setLinkPlantingType(InventoryServiceEnums.LinkPlantingType.REMOVE_LINK.toString());

		forageInvContract = service.updateInventoryContract(forageInvContract.getInventoryContractGuid(), forageInvContract);

		//*************************************************
		//Grain policy: Check if link has been removed
		//*************************************************
		uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		grainInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(1, grainInvContract.getFields().size());
		Assert.assertEquals(1, grainInvContract.getFields().get(0).getPlantings().size());
		//Check if link is removed
		Assert.assertNull(grainInvContract.getFields().get(0).getPlantings().get(0).getUnderseededInventorySeededForageGuid());

		//*************************************************
		//FORAGE policy: Link plantings again
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		forageInvContract = service.getInventoryContract(uwContract);

		//Add link again 
		field1 = getField(fieldId1, forageInvContract.getFields());
		invSeededForage = getSeededForage(inventorySeededForageGuid, field1);
		Assert.assertNotNull(invSeededForage);
		
		invSeededForage.setLinkPlantingType(InventoryServiceEnums.LinkPlantingType.ADD_LINK.toString());
		invSeededForage.setGrainInventoryFieldGuid(grainInventoryFieldGuid);

		forageInvContract = service.updateInventoryContract(forageInvContract.getInventoryContractGuid(), forageInvContract);

		//*************************************************
		//Grain policy: Check if plantings are linked
		//*************************************************
		uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		grainInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(1, grainInvContract.getFields().size());
		Assert.assertEquals(1, grainInvContract.getFields().get(0).getPlantings().size());
		//Check if it's linked
		Assert.assertEquals(inventorySeededForageGuid, grainInvContract.getFields().get(0).getPlantings().get(0).getUnderseededInventorySeededForageGuid());

		//*************************************************
		//FORAGE policy: Remove planting which removes the link
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		forageInvContract = service.getInventoryContract(uwContract);

		//Remove link 
		field1 = getField(fieldId1, forageInvContract.getFields());
		invSeededForage = getSeededForage(inventorySeededForageGuid, field1);
		Assert.assertNotNull(invSeededForage);
		
		invSeededForage.setDeletedByUserInd(true);

		forageInvContract = service.updateInventoryContract(forageInvContract.getInventoryContractGuid(), forageInvContract);

		//*************************************************
		//Grain policy: Check if link has been removed
		//*************************************************
		uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		grainInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(1, grainInvContract.getFields().size());
		Assert.assertEquals(1, grainInvContract.getFields().get(0).getPlantings().size());
		//Check if link is removed
		Assert.assertNull(grainInvContract.getFields().get(0).getPlantings().get(0).getUnderseededInventorySeededForageGuid());

		//*************************************************
		//FORAGE policy: Re-add planting and link.
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		forageInvContract = service.getInventoryContract(uwContract);

		// Planting 2 -> Linked planting
		field1 = getField(fieldId1, forageInvContract.getFields());
		planting = createPlanting(field1, 2, cropYear1, insurancePlanIdForage);
		createInventorySeededForage(planting, 65, "W3", true, 11.0, grainInventoryFieldGuid); //FORAGE Winter Survival 3
		
		forageInvContract = service.updateInventoryContract(forageInvContract.getInventoryContractGuid(), forageInvContract);

		//Set the GUID of the seeded forage record that is linked
		inventorySeededForageGuid = null;
		invSeededForage = null;
		for (InventoryField invField : forageInvContract.getFields().get(0).getPlantings()) {
			if (invField.getPlantingNumber() == 2) {
				inventorySeededForageGuid = invField.getInventorySeededForages().get(0).getInventorySeededForageGuid();
				invSeededForage = invField.getInventorySeededForages().get(0);
			}
		}
		Assert.assertNotNull(inventorySeededForageGuid);
		Assert.assertNotNull(invSeededForage);
		
		//*************************************************
		//FORAGE policy: Remove field, because there is no other way to remove the contracted field detail record
		//               Planting link will be removed as well.
		//*************************************************
		//Remove field1
		field1 = getField(fieldId1, forageInvContract.getFields());
		field1.setLandUpdateType(LandUpdateTypes.REMOVE_FIELD_FROM_POLICY);

		forageInvContract = service.updateInventoryContract(forageInvContract.getInventoryContractGuid(), forageInvContract);
		
		Assert.assertEquals(0, forageInvContract.getFields().size());
		
		//*************************************************
		//Grain policy: Check if planting link is removed
		//*************************************************
		uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		grainInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(1, grainInvContract.getFields().size());
		Assert.assertEquals(1, grainInvContract.getFields().get(0).getPlantings().size());
		//Check if link is removed
		Assert.assertNull(grainInvContract.getFields().get(0).getPlantings().get(0).getUnderseededInventorySeededForageGuid());

		//*************************************************
		//FORAGE policy: Re-add field, planting and link.
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		forageInvContract = service.getInventoryContract(uwContract);
		
		//Add field to forage policy
		fieldList = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				fieldId1.toString(),
				cropYear1.toString());

		Assert.assertNotNull(fieldList);

		//AnnualField
		field = fieldList.getCollection().get(0);
		
		field = service.rolloverAnnualFieldInventory(field, cropYear1.toString(), insurancePlanIdForage.toString());

		field.setTransferFromGrowerContractYearId(null);
		field.setLandUpdateType(LandUpdateTypes.ADD_EXISTING_LAND);
		field.setCropYear(cropYear1);
		field.setDisplayOrder(1);
		
		//Add link again 
		invSeededForage = getSeededForage(inventorySeededForageGuid, field);
		Assert.assertNotNull(invSeededForage);
		
		invSeededForage.setLinkPlantingType(InventoryServiceEnums.LinkPlantingType.ADD_LINK.toString());
		invSeededForage.setGrainInventoryFieldGuid(grainInventoryFieldGuid);

		forageInvContract.getFields().add(field);

		forageInvContract = service.updateInventoryContract(forageInvContract.getInventoryContractGuid(), forageInvContract);

		//*************************************************
		//Grain policy: Check if plantings are linked
		//*************************************************
		uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		grainInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(1, grainInvContract.getFields().size());
		Assert.assertEquals(1, grainInvContract.getFields().get(0).getPlantings().size());
		//Check if it's linked
		Assert.assertEquals(inventorySeededForageGuid, grainInvContract.getFields().get(0).getPlantings().get(0).getUnderseededInventorySeededForageGuid());

		//*************************************************
		//GRAIN policy: Remove field, planting link will be removed as well.
		//*************************************************
		//Remove field1
		field1 = getField(fieldId1, grainInvContract.getFields());
		field1.setLandUpdateType(LandUpdateTypes.REMOVE_FIELD_FROM_POLICY);

		grainInvContract = service.updateInventoryContract(grainInvContract.getInventoryContractGuid(), grainInvContract);
		
		Assert.assertEquals(0, grainInvContract.getFields().size());
		
		//*************************************************
		//FORAGE policy: Check if planting link is removed
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		forageInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(1, forageInvContract.getFields().size());
		Assert.assertEquals(2, forageInvContract.getFields().get(0).getPlantings().size());
		//Check if link is removed
		field1 = getField(fieldId1, forageInvContract.getFields());
		invSeededForage = getSeededForage(inventorySeededForageGuid, field1);
		Assert.assertNull(invSeededForage.getLinkedPlanting());
		
		//Delete field1
		field1.setLandUpdateType(LandUpdateTypes.DELETE_FIELD);

		forageInvContract = service.updateInventoryContract(forageInvContract.getInventoryContractGuid(), forageInvContract);
		
		Assert.assertEquals(0, forageInvContract.getFields().size());
		
		//*************************************************
		//FORAGE policy: Delete inventory Contract
		//*************************************************
		deleteInventoryContract(policyNumber2);
		
		
		delete();
		
		logger.debug(">testLinkUnlinkPlantings");
	}
	
	
	@Test
	public void testRolloverLinkedPolicies() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testRolloverLinkedPolicies");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Create field
		createLegalLand();
		createField(fieldId1);
		createAnnualFieldDetail(fieldId1, annualFieldDetailId1);
		//Add field 2
		createField(fieldId2);
		createAnnualFieldDetail(fieldId2, annualFieldDetailId2);


		createGrower();
		
		//Grain policy 2021
		createPolicy(insurancePlanIdGrain, policyId1, policyNumber1, contractNumber1, contractId1);
		createGrowerContractYear(insurancePlanIdGrain, gcyId1, contractId1);
		//Add fields to grain policy
		createContractedFieldDetail(annualFieldDetailId1, contractedFieldDetailId1, gcyId1, 1);
		createContractedFieldDetail(annualFieldDetailId2, contractedFieldDetailId3, gcyId1, 2);

		//Forage policy 2021
		createPolicy(insurancePlanIdForage, policyId2, policyNumber2, contractNumber2, contractId2);
		createGrowerContractYear(insurancePlanIdForage, gcyId2, contractId2);
		//Add field to forage policy
		createContractedFieldDetail(annualFieldDetailId1, contractedFieldDetailId2, gcyId2, 1);


		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//*************************************************
		//Get Grain Policy
		//*************************************************
		UwContractRsrc uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(2, invContract.getFields().size());
		
		AnnualFieldRsrc field1 = getField(fieldId1, invContract.getFields());
		//Check for linked policies
		Assert.assertNotNull(field1.getPolicies());
		Assert.assertEquals(policyNumber2, field1.getPolicies().get(0).getPolicyNumber());
		
		//Create contract
		service.createInventoryContract(topLevelEndpoints, invContract);

		//*************************************************
		//Grain policy: Get inventory policy and check if associated policies are listed
		//*************************************************
		uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc grainInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(2, grainInvContract.getFields().size());
		
		field1 = getField(fieldId1, grainInvContract.getFields());
		//Check for linked policies
		Assert.assertNotNull(field1.getPolicies());
		Assert.assertEquals(policyNumber2, field1.getPolicies().get(0).getPolicyNumber());
		
		//*************************************************
		//Check associated policies on rollover annual field
		//*************************************************

		//Get field 2
		AnnualFieldListRsrc fieldList = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				fieldId2.toString(),
				cropYear1.toString());

		Assert.assertNotNull(fieldList);
		AnnualFieldRsrc field2 = fieldList.getCollection().get(0);
		
		//Rollover plantings -> for forage
		field2 = service.rolloverAnnualFieldInventory(field2, cropYear1.toString(), insurancePlanIdForage.toString()); 

		Assert.assertNotNull(field2.getPolicies());
		Assert.assertEquals(policyNumber1, field2.getPolicies().get(0).getPolicyNumber());

		delete();
		
		logger.debug(">testRolloverLinkedPolicies");
	}	

	@Test
	public void testDeleteInventoryContract() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testDeleteInventoryContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		//Grain policy
		createPolicy(insurancePlanIdGrain, policyId1, policyNumber1, contractNumber1, contractId1);
		createGrowerContractYear(insurancePlanIdGrain, gcyId1, contractId1);
		//Forage policy
		createPolicy(insurancePlanIdForage, policyId2, policyNumber2, contractNumber2, contractId2);
		createGrowerContractYear(insurancePlanIdForage, gcyId2, contractId2);

		createLegalLand();
		
		//Add field 1
		createField(fieldId1);
		createAnnualFieldDetail(fieldId1, annualFieldDetailId1);
		createContractedFieldDetail(annualFieldDetailId1, contractedFieldDetailId1, gcyId1, 1);

		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//*************************************************
		//Get Grain Policy
		//*************************************************
		UwContractRsrc uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(1, invContract.getFields().size());
		
		AnnualFieldRsrc field1 = getField(fieldId1, invContract.getFields());

		// Remove default planting.
		field1.getPlantings().remove(0);
		
		// Planting 1
		InventoryField planting = createPlanting(field1, 1, cropYear1, insurancePlanIdGrain);
		createInventoryUnseeded(planting, 16, true, 100.0); //Barley

		// Grain Totals
		List<InventoryContractCommodity> expectedTotals = new ArrayList<InventoryContractCommodity>();
		InventoryContractCommodity icc = createInvCommodities(16, "BARLEY", 100.0);
		expectedTotals.add(icc);
		invContract.getCommodities().add(icc);
		
		//Create contract
		InventoryContractRsrc grainInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		String grainInventoryFieldGuid = grainInvContract.getFields().get(0).getPlantings().get(0).getInventoryFieldGuid();

		//*************************************************
		//Get Forage policy
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(0, invContract.getFields().size());

		//Add field to forage policy
		AnnualFieldListRsrc fieldList = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				fieldId1.toString(),
				cropYear1.toString());

		Assert.assertNotNull(fieldList);

		//AnnualField
		AnnualFieldRsrc field = fieldList.getCollection().get(0);

		field.setTransferFromGrowerContractYearId(null);
		field.setLandUpdateType(LandUpdateTypes.ADD_EXISTING_LAND);
		field.setCropYear(cropYear1);
		field.setDisplayOrder(1);
		
		invContract.getFields().add(field);
		
		// Planting 1
		planting = createPlanting(field, 1, cropYear1, insurancePlanIdForage);
		createInventorySeededForage(planting, 65, "E2", true, 10.0, null); //FORAGE Establishment 2

		// Planting 2
		planting = createPlanting(field, 2, cropYear1, insurancePlanIdForage);
		createInventorySeededForage(planting, 65, "W3", true, 11.0, grainInventoryFieldGuid); //FORAGE Winter Survival 3

		//Create contract
		InventoryContractRsrc forageInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertEquals(1, forageInvContract.getFields().size());
		Assert.assertEquals(2, forageInvContract.getFields().get(0).getPlantings().size());
		
		Integer cfdId = forageInvContract.getFields().get(0).getContractedFieldDetailId();

		//Set the GUID of the seeded forage record that is linked
		String inventorySeededForageGuid = null;
		for (InventoryField invField : forageInvContract.getFields().get(0).getPlantings()) {
			if (invField.getPlantingNumber() == 2) {
				inventorySeededForageGuid = invField.getInventorySeededForages().get(0).getInventorySeededForageGuid();
			}
		}
		
		//*************************************************
		//Grain policy: Check if plantings are linked
		//*************************************************
		uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		grainInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(1, grainInvContract.getFields().size());
		Assert.assertEquals(1, grainInvContract.getFields().get(0).getPlantings().size());
		//Check if it's linked
		Assert.assertEquals(inventorySeededForageGuid, grainInvContract.getFields().get(0).getPlantings().get(0).getUnderseededInventorySeededForageGuid());

		//*************************************************
		//FORAGE policy: Delete inventory Contract
		//*************************************************
		deleteInventoryContract(policyNumber2);
		
		//Check if inventory is deleted
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		//Clean up
		//Delete contracted field detail record of added field
		service.deleteContractedFieldDetail(topLevelEndpoints, cfdId.toString());

		delete();
		
		logger.debug(">testDeleteInventoryContract");
	}
	

	@Test
	public void testDeleteInventoryContractWithLinkedPlantings() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testDeleteInventoryContractWithLinkedPlantings");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		//Grain policy
		createPolicy(insurancePlanIdGrain, policyId1, policyNumber1, contractNumber1, contractId1);
		createGrowerContractYear(insurancePlanIdGrain, gcyId1, contractId1);
		//Forage policy
		createPolicy(insurancePlanIdForage, policyId2, policyNumber2, contractNumber2, contractId2);
		createGrowerContractYear(insurancePlanIdForage, gcyId2, contractId2);

		createLegalLand();
		//Add field 1
		createField(fieldId1);
		createAnnualFieldDetail(fieldId1, annualFieldDetailId1);
		createContractedFieldDetail(annualFieldDetailId1, contractedFieldDetailId1, gcyId1, 1);

		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//*************************************************
		//Get Grain Policy
		//*************************************************
		UwContractRsrc uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(1, invContract.getFields().size());
		
		AnnualFieldRsrc field1 = getField(fieldId1, invContract.getFields());

		// Remove default planting.
		field1.getPlantings().remove(0);
		
		// Planting 1
		InventoryField planting = createPlanting(field1, 1, cropYear1, insurancePlanIdGrain);
		createInventoryUnseeded(planting, 16, true, 100.0); //Barley

		// Grain Totals
		List<InventoryContractCommodity> expectedTotals = new ArrayList<InventoryContractCommodity>();
		InventoryContractCommodity icc = createInvCommodities(16, "BARLEY", 100.0);
		expectedTotals.add(icc);
		invContract.getCommodities().add(icc);
		
		//Create contract
		InventoryContractRsrc grainInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		String grainInventoryFieldGuid = grainInvContract.getFields().get(0).getPlantings().get(0).getInventoryFieldGuid();

		//*************************************************
		//Get Forage policy
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(0, invContract.getFields().size());

		//Add field to forage policy
		AnnualFieldListRsrc fieldList = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				fieldId1.toString(),
				cropYear1.toString());

		Assert.assertNotNull(fieldList);

		//AnnualField
		AnnualFieldRsrc field = fieldList.getCollection().get(0);

		field.setTransferFromGrowerContractYearId(null);
		field.setLandUpdateType(LandUpdateTypes.ADD_EXISTING_LAND);
		field.setCropYear(cropYear1);
		field.setDisplayOrder(1);
		
		invContract.getFields().add(field);
		
		// Planting 1
		planting = createPlanting(field, 1, cropYear1, insurancePlanIdForage);
		createInventorySeededForage(planting, 65, "E2", true, 10.0, null); //FORAGE Establishment 2

		// Planting 2 -> Linked planting
		planting = createPlanting(field, 2, cropYear1, insurancePlanIdForage);
		createInventorySeededForage(planting, 65, "W3", true, 11.0, grainInventoryFieldGuid); //FORAGE Winter Survival 3

		//Create contract
		InventoryContractRsrc forageInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertEquals(1, forageInvContract.getFields().size());
		Assert.assertEquals(2, forageInvContract.getFields().get(0).getPlantings().size());

		//Set the GUID of the seeded forage record that is linked
		String inventorySeededForageGuid = null;
		InventorySeededForage foragePlanting = null;
		for (InventoryField invField : forageInvContract.getFields().get(0).getPlantings()) {
			if (invField.getPlantingNumber() == 2) {
				inventorySeededForageGuid = invField.getInventorySeededForages().get(0).getInventorySeededForageGuid();
				foragePlanting = invField.getInventorySeededForages().get(0);
			}
		}
		Assert.assertNotNull(foragePlanting);
		
		
		//*************************************************
		//Grain policy: Check if plantings are linked
		//*************************************************
		uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		grainInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(1, grainInvContract.getFields().size());
		Assert.assertEquals(1, grainInvContract.getFields().get(0).getPlantings().size());
		//Check if it's linked
		InventoryField grainPlanting = grainInvContract.getFields().get(0).getPlantings().get(0);
		Assert.assertEquals(inventorySeededForageGuid, grainPlanting.getUnderseededInventorySeededForageGuid());
		
		//*************************************************
		//FORAGE policy: Check linked field/policy data
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		forageInvContract = service.getInventoryContract(uwContract);

		//Get linked planting
		Assert.assertEquals(1, forageInvContract.getFields().size());
		Assert.assertEquals(2, forageInvContract.getFields().get(0).getPlantings().size());
		field1 = getField(fieldId1, forageInvContract.getFields());
		InventorySeededForage invSeededForage = getSeededForage(inventorySeededForageGuid, field1);
		Assert.assertNotNull(invSeededForage);
		LinkedPlanting linkedPlanting = invSeededForage.getLinkedPlanting();
		Assert.assertNotNull(linkedPlanting);
		//Check values of linked planting
		Assert.assertEquals(grainPlanting.getInventoryFieldGuid(), linkedPlanting.getInventoryFieldGuid());
		Assert.assertEquals(grainPlanting.getUnderseededCropVarietyId(), linkedPlanting.getCropVarietyId());
		Assert.assertEquals(grainPlanting.getUnderseededCropVarietyName(), linkedPlanting.getVarietyName());
		Assert.assertEquals(grainPlanting.getUnderseededAcres(), linkedPlanting.getAcres());
		
		//Delete Forage Inventory Contract
		service.deleteInventoryContract(forageInvContract);

		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		
		//*************************************************
		//Grain policy: Check if link has been removed
		//*************************************************
		uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		
		grainInvContract = service.getInventoryContract(uwContract);
		
		Assert.assertEquals(1, grainInvContract.getFields().size());
		Assert.assertEquals(1, grainInvContract.getFields().get(0).getPlantings().size());
		//Check if link is removed
		Assert.assertNull(grainInvContract.getFields().get(0).getPlantings().get(0).getUnderseededInventorySeededForageGuid());

		delete();
		
		logger.debug(">testDeleteInventoryContractWithLinkedPlantings");
	}
	
	
	public UwContractRsrc getUwContract(String policyNumber,
										CirrasUnderwritingService service, 
										EndpointsRsrc topLevelEndpoints) throws CirrasUnderwritingServiceException {
		
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
	
	private AnnualFieldRsrc getField(Integer fieldId, List<AnnualFieldRsrc> contractFields) {
		
		List<AnnualFieldRsrc> fields = contractFields.stream().filter(x -> x.getFieldId().equals(fieldId)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, fields.size());
		
		return fields.get(0);
	}
	
	private InventorySeededForage getSeededForage(String inventorySeededForageGuid, AnnualFieldRsrc field) {

		for (InventoryField invField : field.getPlantings()) {
			for (InventorySeededForage seeded : invField.getInventorySeededForages()) {
				if(seeded.getInventorySeededForageGuid().equals(inventorySeededForageGuid)) {
					return seeded;
				}
			}
		}
		
		return null;

	}
	
	public void checkInventoryCoverageTotalForage(InventoryCoverageTotalForage expected, InventoryCoverageTotalForage actual) {
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("PlantInsurabilityTypeCode", expected.getPlantInsurabilityTypeCode(), actual.getPlantInsurabilityTypeCode());
		Assert.assertEquals("PlantInsurabilityTypeDesc", expected.getPlantInsurabilityTypeDesc(), actual.getPlantInsurabilityTypeDesc());
		Assert.assertEquals("TotalFieldAcres", expected.getTotalFieldAcres(), actual.getTotalFieldAcres());		
		Assert.assertEquals("IsUnseededInsurableInd", expected.getIsUnseededInsurableInd(), actual.getIsUnseededInsurableInd());
	}
	
	public void checkInventorySeededForage(InventorySeededForage expectedForage, InventorySeededForage actualForage) {
		Assert.assertEquals("CropCommodityId", expectedForage.getCropCommodityId(), actualForage.getCropCommodityId());
		Assert.assertEquals("CropVarietyId", expectedForage.getCropVarietyId(), actualForage.getCropVarietyId());
		Assert.assertEquals("CommodityTypeCode", expectedForage.getCommodityTypeCode(), actualForage.getCommodityTypeCode());
		Assert.assertEquals("FieldAcres", expectedForage.getFieldAcres(), actualForage.getFieldAcres());
		Assert.assertEquals("SeedingYear", expectedForage.getSeedingYear(), actualForage.getSeedingYear());
		Assert.assertEquals("SeedingDate", expectedForage.getSeedingDate(), actualForage.getSeedingDate());
		Assert.assertEquals("IsIrrigatedInd", expectedForage.getIsIrrigatedInd(), actualForage.getIsIrrigatedInd());
		Assert.assertEquals("IsQuantityInsurableInd", expectedForage.getIsQuantityInsurableInd(), actualForage.getIsQuantityInsurableInd());
		Assert.assertEquals("PlantInsurabilityTypeCode", expectedForage.getPlantInsurabilityTypeCode(), actualForage.getPlantInsurabilityTypeCode());
		Assert.assertEquals("IsAwpEligibleInd", expectedForage.getIsAwpEligibleInd(), actualForage.getIsAwpEligibleInd());
	}

	private InventoryField createPlanting(AnnualFieldRsrc field, Integer plantingNumber, Integer cropYear, Integer insurancePlanId) {
		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(insurancePlanId);
		planting.setInventoryFieldGuid(null);
		planting.setLastYearCropCommodityId(null);
		planting.setLastYearCropCommodityName(null);
		planting.setIsHiddenOnPrintoutInd(false);
		planting.setPlantingNumber(plantingNumber);
		planting.setUnderseededAcres(14.4);
		planting.setUnderseededCropVarietyId(119);
		planting.setUnderseededCropVarietyName("ALFALFA");

		
		field.getPlantings().add(planting);

		return planting;
	}

	private InventorySeededForage createInventorySeededForage(
			InventoryField planting, 
            Integer cropCommodityId, 
			String plantInsurabilityTypeCode,
			Boolean isQuantityInsurableInd,
			Double fieldAcres,
			String grainInventoryFieldGuid) {
		
		InventorySeededForage isf = new InventorySeededForage();


		isf.setCommodityTypeCode(null);
		isf.setCropCommodityId(cropCommodityId);
		isf.setCropVarietyId(119);
		isf.setCropVarietyName("ALFALFA");
		isf.setFieldAcres(fieldAcres);
		isf.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		isf.setIsAwpEligibleInd(false);
		isf.setIsIrrigatedInd(false);
		isf.setIsQuantityInsurableInd(isQuantityInsurableInd);
		isf.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		isf.setSeedingYear(planting.getCropYear() - 3);
		if(grainInventoryFieldGuid != null) {
			isf.setGrainInventoryFieldGuid(grainInventoryFieldGuid);
			isf.setLinkPlantingType(InventoryServiceEnums.LinkPlantingType.ADD_LINK.toString());
		}
		
		planting.getInventorySeededForages().add(isf);

		return isf;
	}	

	private void createInventoryUnseeded(
			InventoryField planting, 
            Integer cropCommodityId, 
            Boolean isUnseededInsurableInd,
            Double acresToBeSeeded) {
		
		InventoryUnseeded inventoryUnseeded = new InventoryUnseeded();
		inventoryUnseeded.setCropCommodityId(cropCommodityId);
		inventoryUnseeded.setIsUnseededInsurableInd(isUnseededInsurableInd);
		inventoryUnseeded.setAcresToBeSeeded(acresToBeSeeded);
		inventoryUnseeded.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		planting.setInventoryUnseeded(inventoryUnseeded);

	}
	
	private InventoryContractCommodity createInvCommodities(Integer cropId, String cropName, Double totalUnseededAcres) {
		//InventoryContractCommodity
		InventoryContractCommodity invContractCommodity = new InventoryContractCommodity();
		invContractCommodity.setCropCommodityId(cropId);
		invContractCommodity.setCropCommodityName(cropName);
		invContractCommodity.setTotalUnseededAcres(totalUnseededAcres);
		invContractCommodity.setIsPedigreeInd(false);
		return invContractCommodity;
	}
	
	private void checkInventoryContractCommodity(InventoryContractCommodity expected, InventoryContractCommodity actual) {
		Assert.assertNotNull("InventoryContractCommodityGuid", actual.getInventoryContractCommodityGuid());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertNotNull("InventoryContractGuid", actual.getInventoryContractGuid());
		Assert.assertEquals("TotalUnseededAcres", expected.getTotalUnseededAcres(), actual.getTotalUnseededAcres());
		Assert.assertEquals("IsPedigreeInd", expected.getIsPedigreeInd(), actual.getIsPedigreeInd());
	}

	private InventoryCoverageTotalForage createInventoryCovarageTotalForage(
			String invContractGuid,
            Integer cropCommodityId, 
            String cropCommodityName,
			String plantInsurabilityTypeCode,
			String plantInsurabilityTypeDesc,
			Double totalFieldAcres,
			Boolean isUnseededInsurableInd) {
	
		InventoryCoverageTotalForage ictf = new InventoryCoverageTotalForage();

		ictf.setCropCommodityId(cropCommodityId);
		ictf.setCropCommodityName(cropCommodityName);
		ictf.setInventoryContractGuid(invContractGuid);
		ictf.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		ictf.setPlantInsurabilityTypeDesc(plantInsurabilityTypeDesc);
		ictf.setTotalFieldAcres(totalFieldAcres);
		ictf.setIsUnseededInsurableInd(isUnseededInsurableInd);

		return ictf;
	}
	
	private void createGrower() throws ValidationException, CirrasUnderwritingServiceException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		GrowerRsrc resource = new GrowerRsrc();
		
		resource.setGrowerId(growerId1);
		resource.setGrowerNumber(growerNumber1);
		resource.setGrowerName("grower test name");
		resource.setGrowerAddressLine1("address line 1");
		resource.setGrowerAddressLine2("address line 2");
		resource.setGrowerPostalCode("V8P 4N8");
		resource.setGrowerCity("Victoria");
		resource.setCityId(1);
		resource.setGrowerProvince("BC");
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerCreated);

		service.synchronizeGrower(resource);
		
	}
	
	private void createPolicy(Integer insurancePlanId, Integer policyId, String policyNumber, String contractNumber, Integer contractId) throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(policyId);
		resource.setGrowerId(growerId1);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setPolicyStatusCode("ACTIVE");
		resource.setOfficeId(1);
		resource.setPolicyNumber(policyNumber);
		resource.setContractNumber(contractNumber);
		resource.setContractId(contractId);
		resource.setCropYear(cropYear1);
		
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.PolicyCreated);

		service.synchronizePolicy(resource);
	}
	
	private void createGrowerContractYear(Integer insurancePlanId, Integer gcyId, Integer contractId) throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(gcyId);
		resource.setContractId(contractId);
		resource.setGrowerId(growerId1);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setCropYear(cropYear1);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearCreated);

		service.synchronizeGrowerContractYear(resource);
		
	}

	private void createLegalLand() throws CirrasUnderwritingServiceException, ValidationException {
				
		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(legalLandId1);
		resource.setPrimaryPropertyIdentifier("GF0099999");
		resource.setPrimaryReferenceTypeCode("OTHER");
		resource.setLegalDescription(null);
		resource.setLegalShortDescription(null);
		resource.setOtherDescription("TEST LEGAL LOC 123");
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2022);
		resource.setTransactionType(LandManagementEventTypes.LegalLandCreated);
		
		service.synchronizeLegalLand(resource);

	}
	
	private void createField(Integer fieldId) throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel("Field Label");
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2022);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
		
		service.synchronizeField(resource);
	}
	
	private void createAnnualFieldDetail(Integer fieldId, Integer annualFieldDetailId) throws CirrasUnderwritingServiceException, ValidationException {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setLegalLandId(legalLandId1);
		resource.setFieldId(fieldId);
		resource.setCropYear(cropYear1);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		service.synchronizeAnnualFieldDetail(resource);
	}

	private void createContractedFieldDetail(Integer annualFieldDetailId, Integer contractedFieldDetailId, Integer gcyId, Integer displayOrder) throws CirrasUnderwritingServiceException, ValidationException {
		
		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId);
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setGrowerContractYearId(gcyId);
		resource.setDisplayOrder(displayOrder);
		resource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailCreated);
	
		service.synchronizeContractedFieldDetail(resource);

	}
	
	private void addComment(AnnualFieldRsrc field) {
		UnderwritingComment underwritingComment = new UnderwritingComment();

		underwritingComment.setAnnualFieldDetailId(field.getAnnualFieldDetailId());
		underwritingComment.setUnderwritingComment(commentText);
		underwritingComment.setUnderwritingCommentGuid(null);
		underwritingComment.setUnderwritingCommentTypeCode("INV");
		underwritingComment.setUnderwritingCommentTypeDesc("Inventory");
		
		List<UnderwritingComment> uwComments = new ArrayList<UnderwritingComment>();
		uwComments.add(underwritingComment);
		
		field.setUwComments(uwComments);

	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
}
