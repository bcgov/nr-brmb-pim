package ca.bc.gov.mal.cirras.underwriting.controllers;

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

import ca.bc.gov.mal.cirras.underwriting.services.utils.LandManagementEventTypes;
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
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldVarietyBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class DopYieldContractEndpointBerriesTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(DopYieldContractEndpointBerriesTest.class);


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
		Scopes.GET_LEGAL_LAND,
		Scopes.CREATE_DOP_YIELD_CONTRACT,
		Scopes.UPDATE_DOP_YIELD_CONTRACT,
		Scopes.GET_DOP_YIELD_CONTRACT,
		Scopes.DELETE_DOP_YIELD_CONTRACT
	};
	
	private Integer growerId = 90000011;
	private String contractNumber = "998891";
	private Integer contractId = 90000014;

	private Integer policyId1 = 90000012;
	private Integer gcyId1 = 90000013;
	private String policyNumber1 = "998891-21";
	private Integer cropYear1 = 2021;

	private Integer policyId2 = 92000012;
	private Integer gcyId2 = 92000013;
	private String policyNumber2 = "998891-22";
	private Integer cropYear2 = 2022;

	private Integer legalLandId = 90000015;
	private Integer fieldId = 90000016;
	
	private Integer annualFieldDetailId1 = 90000017;
	private Integer contractedFieldDetailId1 = 90000018;
	
	private Integer annualFieldDetailId2 = 92000017;
	private Integer contractedFieldDetailId2 = 92000018;

	private Integer annualFieldDetailId3 = null;
	private Integer contractedFieldDetailId3 = null;

	
	private String fieldLocation = "Field Location";
			
	private Integer insurancePlanId = 3; //Berries
	
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

		deleteDopYieldContract(policyNumber1);
		deleteDopYieldContract(policyNumber2);
		
		deleteInventoryContract(policyNumber1);
		deleteInventoryContract(policyNumber2);

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId2.toString());

		if(contractedFieldDetailId3 != null) {
			service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId3.toString());
			contractedFieldDetailId3 = null;
		}

		if(annualFieldDetailId3 != null) {
			service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId3.toString());
			annualFieldDetailId3 = null;
		}

		service.deleteField(topLevelEndpoints, fieldId.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());
		
		service.deleteGrowerContractYear(topLevelEndpoints, gcyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId1.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, gcyId2.toString());
		service.deletePolicy(topLevelEndpoints, policyId2.toString());
		service.deleteGrower(topLevelEndpoints, growerId.toString());
	}

	private void deleteInventoryContract(String policyNumber) throws CirrasUnderwritingServiceException {
		
		UwContractRsrc uwContract = getUwContract(policyNumber, service, topLevelEndpoints);
		
		if ( uwContract != null ) {
			
			if ( uwContract.getInventoryContractGuid() != null ) { 
				InventoryContractRsrc invContract = service.getInventoryContract(uwContract);
				service.deleteInventoryContract(invContract);
			}
		}
	}

	private void deleteDopYieldContract(String policyNumber) throws CirrasUnderwritingServiceException {
		
		UwContractRsrc uwContract = getUwContract(policyNumber, service, topLevelEndpoints);
		
		if ( uwContract != null ) {
			
			if ( uwContract.getDeclaredYieldContractGuid() != null ) { 
				DopYieldContractRsrc dopYieldContract = service.getDopYieldContract(uwContract);
				service.deleteDopYieldContract(dopYieldContract);
			}
		}
	}
	
	@Test
	public void testDopYieldRolloverBerries() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testDopYieldRolloverBerries");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		createPolicy(policyId1, policyNumber1, cropYear1);
		createGrowerContractYear(gcyId1, cropYear1);

		createLegalLand();
		createField();
		createAnnualFieldDetail(annualFieldDetailId1, cropYear1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, gcyId1, false);
		
		UwContractRsrc uwContractRsrc = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContractRsrc);
		Assert.assertNull(uwContractRsrc.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContractRsrc);
		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings().get(0).getInventoryBerries());

		// Remove default planting.
		AnnualFieldRsrc field = invContract.getFields().get(0);
		field.getPlantings().remove(0);

		createPlanting(field, 1, cropYear1);
		createInventoryBerries(field.getPlantings().get(0), 10, "BLUEBERRY", 1010689, "BLUEJAY", 100.0, 10, 5.3, true, true, null, 2020, null, null, null, false);

		createPlanting(field, 2, cropYear1);
		createInventoryBerries(field.getPlantings().get(1), 10, "BLUEBERRY", 1010689, "BLUEJAY", 300.0, 9, 11.0, false, false, null, 2018, null, null, null, false);
		
		createPlanting(field, 3, cropYear1);
		createInventoryBerries(field.getPlantings().get(2), 12, "RASPBERRY", 1010694, "MALAHAT", 200.0, null, null, true, false, null, 2021, null, null, null, false);

		createPlanting(field, 4, cropYear1);
		createInventoryBerries(field.getPlantings().get(3), 12, "RASPBERRY", 1010695, "MEEKER", 500.0, null, null, true, false, null, 2021, null, null, null, false);

		// Planting with 0 acres will be excluded from DOP rollover.
		createPlanting(field, 5, cropYear1);
		createInventoryBerries(field.getPlantings().get(4), 13, "STRAWBERRY", 1010702, "HOOD", 0.0, null, null, true, true, null, 2021, null, null, null, false);
		
		
		//Create inventory contract
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings());
		Assert.assertEquals(5, fetchedInvContract.getFields().get(0).getPlantings().size());
		
		uwContractRsrc = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContractRsrc);
		Assert.assertNotNull(uwContractRsrc.getInventoryContractGuid());
		Assert.assertNull(uwContractRsrc.getDeclaredYieldContractGuid());
		
		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(uwContractRsrc);

		Assert.assertNotNull(newDyc);
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertNotNull(newDyc.getFields());
		Assert.assertEquals(1, newDyc.getFields().size());
		Assert.assertNotNull(newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList());
		Assert.assertEquals(2, newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().size());

		// Expected values
		// Dop Yield Contract
		DopYieldContractRsrc expectedDyc = new DopYieldContractRsrc();
		expectedDyc.setContractId(contractId);
		expectedDyc.setCropYear(cropYear1);
		expectedDyc.setDefaultYieldMeasUnitTypeCode("LB");
		expectedDyc.setGrowerContractYearId(gcyId1);
		expectedDyc.setInsurancePlanId(insurancePlanId);

		// Fields
		AnnualFieldRsrc expectedField = new AnnualFieldRsrc();
		expectedField.setAnnualFieldDetailId(annualFieldDetailId1);
		expectedField.setContractedFieldDetailId(contractedFieldDetailId1);
		expectedField.setCropYear(cropYear1);
		expectedField.setDisplayOrder(1);
		expectedField.setFieldId(fieldId);
		expectedField.setFieldLabel("Field Label");
		expectedField.setFieldLocation(fieldLocation);
		expectedField.setIsLeasedInd(false);
		expectedField.setLegalLandId(legalLandId);
		expectedField.setOtherLegalDescription("TEST LEGAL LOC 123");
		expectedField.setPrimaryPropertyIdentifier("GF0099999");

		// BLUEBERRY
		DopYieldFieldCommodityBerries expectedDyfcb = new DopYieldFieldCommodityBerries();
		expectedDyfcb.setCropCommodityId(10);
		expectedDyfcb.setCropCommodityName("BLUEBERRY");		
		expectedDyfcb.setCropYear(cropYear1);
		expectedDyfcb.setFieldId(fieldId);		
		expectedDyfcb.setTotalProduction(null);		
		expectedDyfcb.setTotalProductionOverride(null);

		DopYieldFieldVarietyBerries expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010689);
		expectedDyfvb.setCropVarietyName("BLUEJAY");
		expectedDyfvb.setIsHiddenOnPrintoutInd(false);
		expectedDyfvb.setPlantedAcres(400.0);
		expectedDyfvb.setSalesYield(null);
		expectedDyfvb.setSoldShippedYield(null);
		expectedDyfvb.setTotalProduction(null);
		expectedDyfvb.setTotalProductionOverride(null);
		
		expectedDyfcb.getDopYieldFieldVarietyBerriesList().add(expectedDyfvb);
		
		expectedField.getDopYieldFieldCommodityBerriesList().add(expectedDyfcb);

		// RASPBERRY
		expectedDyfcb = new DopYieldFieldCommodityBerries();
		expectedDyfcb.setCropCommodityId(12);
		expectedDyfcb.setCropCommodityName("RASPBERRY");
		expectedDyfcb.setCropYear(cropYear1);
		expectedDyfcb.setFieldId(fieldId);		
		expectedDyfcb.setTotalProduction(null);		
		expectedDyfcb.setTotalProductionOverride(null);

		expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010694);
		expectedDyfvb.setCropVarietyName("MALAHAT");
		expectedDyfvb.setIsHiddenOnPrintoutInd(false);
		expectedDyfvb.setPlantedAcres(200.0);
		expectedDyfvb.setSalesYield(null);
		expectedDyfvb.setSoldShippedYield(null);
		expectedDyfvb.setTotalProduction(null);
		expectedDyfvb.setTotalProductionOverride(null);
		
		expectedDyfcb.getDopYieldFieldVarietyBerriesList().add(expectedDyfvb);

		expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010695);
		expectedDyfvb.setCropVarietyName("MEEKER");
		expectedDyfvb.setIsHiddenOnPrintoutInd(false);
		expectedDyfvb.setPlantedAcres(500.0);
		expectedDyfvb.setSalesYield(null);
		expectedDyfvb.setSoldShippedYield(null);
		expectedDyfvb.setTotalProduction(null);
		expectedDyfvb.setTotalProductionOverride(null);
		
		expectedDyfcb.getDopYieldFieldVarietyBerriesList().add(expectedDyfvb);
		
		expectedField.getDopYieldFieldCommodityBerriesList().add(expectedDyfcb);

		expectedDyc.getFields().add(expectedField);

		
		// Dop Yield Contract Commodity Berries
		DopYieldContractCommodityBerries expectedDyccb = new DopYieldContractCommodityBerries();
		expectedDyccb.setCropCommodityId(10);
		expectedDyccb.setCropCommodityName("BLUEBERRY");
		expectedDyccb.setTotalProduction(null);
		expectedDyccb.setTotalProductionOverride(null);
		
		expectedDyc.getDopYieldContractCommodityBerriesList().add(expectedDyccb);
		
		expectedDyccb = new DopYieldContractCommodityBerries();
		expectedDyccb.setCropCommodityId(12);
		expectedDyccb.setCropCommodityName("RASPBERRY");
		expectedDyccb.setTotalProduction(null);
		expectedDyccb.setTotalProductionOverride(null);
		
		expectedDyc.getDopYieldContractCommodityBerriesList().add(expectedDyccb);

		
		// Check rolled-over DOP.
		checkDopYieldContract(expectedDyc, newDyc);

		
		delete();
		
		logger.debug(">testDopYieldRolloverBerries");
	}
		
	@Test
	public void testInsertUpdateDeleteDopYieldBerriesContract() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInsertUpdateDeleteDopYieldBerriesContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		createPolicy(policyId1, policyNumber1, cropYear1);
		createGrowerContractYear(gcyId1, cropYear1);

		createLegalLand();
		createField();
		createAnnualFieldDetail(annualFieldDetailId1, cropYear1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, gcyId1, false);
		
		UwContractRsrc uwContractRsrc = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContractRsrc);
		Assert.assertNull(uwContractRsrc.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContractRsrc);
		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings().get(0).getInventoryBerries());

		// Remove default planting.
		AnnualFieldRsrc field = invContract.getFields().get(0);
		field.getPlantings().remove(0);

		createPlanting(field, 1, cropYear1);
		createInventoryBerries(field.getPlantings().get(0), 10, "BLUEBERRY", 1010689, "BLUEJAY", 100.0, 10, 5.3, true, true, null, 2020, null, null, null, false);

		createPlanting(field, 2, cropYear1);
		createInventoryBerries(field.getPlantings().get(1), 10, "BLUEBERRY", 1010689, "BLUEJAY", 300.0, 9, 11.0, false, false, null, 2018, null, null, null, false);
		
		createPlanting(field, 3, cropYear1);
		createInventoryBerries(field.getPlantings().get(2), 12, "RASPBERRY", 1010694, "MALAHAT", 200.0, null, null, true, false, null, 2021, null, null, null, false);

		createPlanting(field, 4, cropYear1);
		createInventoryBerries(field.getPlantings().get(3), 12, "RASPBERRY", 1010695, "MEEKER", 500.0, null, null, true, false, null, 2021, null, null, null, false);

		// Planting with 0 acres will be excluded from DOP rollover.
		createPlanting(field, 5, cropYear1);
		createInventoryBerries(field.getPlantings().get(4), 13, "STRAWBERRY", 1010702, "HOOD", 0.0, null, null, true, true, null, 2021, null, null, null, false);
		
		
		//Create inventory contract
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings());
		Assert.assertEquals(5, fetchedInvContract.getFields().get(0).getPlantings().size());
		
		uwContractRsrc = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContractRsrc);
		Assert.assertNotNull(uwContractRsrc.getInventoryContractGuid());
		Assert.assertNull(uwContractRsrc.getDeclaredYieldContractGuid());
		
		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(uwContractRsrc);

		Assert.assertNotNull(newDyc);
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertNotNull(newDyc.getFields());
		Assert.assertEquals(1, newDyc.getFields().size());
		Assert.assertNotNull(newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList());
		Assert.assertEquals(2, newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().size());

		// Expected values
		// Dop Yield Contract
		DopYieldContractRsrc expectedDyc = new DopYieldContractRsrc();
		expectedDyc.setContractId(contractId);
		expectedDyc.setCropYear(cropYear1);
		expectedDyc.setDefaultYieldMeasUnitTypeCode("LB");
		expectedDyc.setGrowerContractYearId(gcyId1);
		expectedDyc.setInsurancePlanId(insurancePlanId);

		// Fields
		AnnualFieldRsrc expectedField = new AnnualFieldRsrc();
		expectedField.setAnnualFieldDetailId(annualFieldDetailId1);
		expectedField.setContractedFieldDetailId(contractedFieldDetailId1);
		expectedField.setCropYear(cropYear1);
		expectedField.setDisplayOrder(1);
		expectedField.setFieldId(fieldId);
		expectedField.setFieldLabel("Field Label");
		expectedField.setFieldLocation(fieldLocation);
		expectedField.setIsLeasedInd(false);
		expectedField.setLegalLandId(legalLandId);
		expectedField.setOtherLegalDescription("TEST LEGAL LOC 123");
		expectedField.setPrimaryPropertyIdentifier("GF0099999");

		// BLUEBERRY
		DopYieldFieldCommodityBerries expectedDyfcb = new DopYieldFieldCommodityBerries();
		expectedDyfcb.setCropCommodityId(10);
		expectedDyfcb.setCropCommodityName("BLUEBERRY");		
		expectedDyfcb.setCropYear(cropYear1);
		expectedDyfcb.setFieldId(fieldId);		
		expectedDyfcb.setTotalProduction(null);		
		expectedDyfcb.setTotalProductionOverride(null);

		DopYieldFieldVarietyBerries expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010689);
		expectedDyfvb.setCropVarietyName("BLUEJAY");
		expectedDyfvb.setIsHiddenOnPrintoutInd(false);
		expectedDyfvb.setPlantedAcres(400.0);
		expectedDyfvb.setSalesYield(null);
		expectedDyfvb.setSoldShippedYield(null);
		expectedDyfvb.setTotalProduction(null);
		expectedDyfvb.setTotalProductionOverride(null);
		
		expectedDyfcb.getDopYieldFieldVarietyBerriesList().add(expectedDyfvb);
		
		expectedField.getDopYieldFieldCommodityBerriesList().add(expectedDyfcb);

		// RASPBERRY
		expectedDyfcb = new DopYieldFieldCommodityBerries();
		expectedDyfcb.setCropCommodityId(12);
		expectedDyfcb.setCropCommodityName("RASPBERRY");
		expectedDyfcb.setCropYear(cropYear1);
		expectedDyfcb.setFieldId(fieldId);		
		expectedDyfcb.setTotalProduction(null);		
		expectedDyfcb.setTotalProductionOverride(null);

		expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010694);
		expectedDyfvb.setCropVarietyName("MALAHAT");
		expectedDyfvb.setIsHiddenOnPrintoutInd(false);
		expectedDyfvb.setPlantedAcres(200.0);
		expectedDyfvb.setSalesYield(null);
		expectedDyfvb.setSoldShippedYield(null);
		expectedDyfvb.setTotalProduction(null);
		expectedDyfvb.setTotalProductionOverride(null);
		
		expectedDyfcb.getDopYieldFieldVarietyBerriesList().add(expectedDyfvb);

		expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010695);
		expectedDyfvb.setCropVarietyName("MEEKER");
		expectedDyfvb.setIsHiddenOnPrintoutInd(false);
		expectedDyfvb.setPlantedAcres(500.0);
		expectedDyfvb.setSalesYield(null);
		expectedDyfvb.setSoldShippedYield(null);
		expectedDyfvb.setTotalProduction(null);
		expectedDyfvb.setTotalProductionOverride(null);
		
		expectedDyfcb.getDopYieldFieldVarietyBerriesList().add(expectedDyfvb);
		
		expectedField.getDopYieldFieldCommodityBerriesList().add(expectedDyfcb);

		expectedDyc.getFields().add(expectedField);

		
		// Dop Yield Contract Commodity Berries
		DopYieldContractCommodityBerries expectedDyccb = new DopYieldContractCommodityBerries();
		expectedDyccb.setCropCommodityId(10);
		expectedDyccb.setCropCommodityName("BLUEBERRY");
		expectedDyccb.setTotalProduction(null);
		expectedDyccb.setTotalProductionOverride(null);
		
		expectedDyc.getDopYieldContractCommodityBerriesList().add(expectedDyccb);
		
		expectedDyccb = new DopYieldContractCommodityBerries();
		expectedDyccb.setCropCommodityId(12);
		expectedDyccb.setCropCommodityName("RASPBERRY");
		expectedDyccb.setTotalProduction(null);
		expectedDyccb.setTotalProductionOverride(null);
		
		expectedDyc.getDopYieldContractCommodityBerriesList().add(expectedDyccb);

		
		// Check rolled-over DOP.
		checkDopYieldContract(expectedDyc, newDyc);

		// Insert DOP
		newDyc.setEnteredYieldMeasUnitTypeCode("LB");
		newDyc.setGrainFromOtherSourceInd(false);

		expectedDyc.setEnteredYieldMeasUnitTypeCode("LB");
		expectedDyc.setGrainFromOtherSourceInd(false);
		
		// Dop Yield Contract Commodity Berries
		// BLUEBERRY
		newDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProduction(11.22);
		newDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProductionOverride(33.44);

		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProduction(11.22);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProductionOverride(33.44);
		
		// RASPBERRY
		newDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProduction(55.66);
		newDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProductionOverride(77.88);
		
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProduction(55.66);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProductionOverride(77.88);

		// Dop Yield Field Commodity Berries
		// BLUEBERRY
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProduction(11.99);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(22.88);

		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProduction(11.99);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(22.88);
		
		// RASPBERRY
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProduction(33.77);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProductionOverride(44.66);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProduction(33.77);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProductionOverride(44.66);
		

		// Dop Yield Field Variety Berries
		// BLUEBERRY - BLUEJAY
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(11.55);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(22.66);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(33.77);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(44.88);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(55.99);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(11.55);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(22.66);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(33.77);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(44.88);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(55.99);
		
		// RASPBERRY - MEEKER
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setAbandonmentYield(66.00);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSalesYield(77.11);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSoldShippedYield(88.22);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProduction(99.33);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProductionOverride(00.44);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setAbandonmentYield(66.00);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSalesYield(77.11);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSoldShippedYield(88.22);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProduction(99.33);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProductionOverride(00.44);
		
		DopYieldContractRsrc fetchedDyc = service.createDopYieldContract(topLevelEndpoints, newDyc);
		checkDopYieldContract(expectedDyc, fetchedDyc);
		
		// Fetch DOP
		uwContractRsrc = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContractRsrc);
		Assert.assertNotNull(uwContractRsrc.getInventoryContractGuid());
		Assert.assertNotNull(uwContractRsrc.getDeclaredYieldContractGuid());

		fetchedDyc = service.getDopYieldContract(uwContractRsrc);
		checkDopYieldContract(expectedDyc, fetchedDyc);
		
		// Update DOP
		// Dop Yield Contract Commodity Berries
		// BLUEBERRY
		fetchedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProduction(null);
		fetchedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProductionOverride(null);

		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProduction(null);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProductionOverride(null);

		// RASPBERRY
		fetchedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProduction(98.76);
		fetchedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProductionOverride(54.32);
		
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProduction(98.76);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProductionOverride(54.32);

		// Dop Yield Field Commodity Berries
		// BLUEBERRY
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProduction(99.11);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(88.22);

		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProduction(99.11);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(88.22);
		
		// RASPBERRY
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProduction(null);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProductionOverride(null);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProduction(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProductionOverride(null);
		
		// Dop Yield Field Variety Berries
		// BLUEBERRY - BLUEJAY
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(null);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		// RASPBERRY - MEEKER
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setAbandonmentYield(00.66);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSalesYield(11.77);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSoldShippedYield(22.88);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProduction(33.99);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProductionOverride(44.00);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setAbandonmentYield(00.66);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSalesYield(11.77);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSoldShippedYield(22.88);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProduction(33.99);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProductionOverride(44.00);
		
		DopYieldContractRsrc updatedDyc = service.updateDopYieldContract(fetchedDyc);
		checkDopYieldContract(expectedDyc, updatedDyc);

		//Delete DOP
		service.deleteDopYieldContract(updatedDyc);
		
		// Fetch DOP
		uwContractRsrc = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContractRsrc);
		Assert.assertNotNull(uwContractRsrc.getInventoryContractGuid());
		Assert.assertNull(uwContractRsrc.getDeclaredYieldContractGuid());
		
		delete();
		
		logger.debug(">testInsertUpdateDeleteDopYieldBerriesContract");
	}

	
	
	private UwContractRsrc getUwContract(String policyNumber,
			CirrasUnderwritingService service, 
			EndpointsRsrc topLevelEndpoints) throws CirrasUnderwritingServiceException {

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
		1, 
		20);

		Assert.assertNotNull(searchResults);

		if ( searchResults.getCollection() != null && searchResults.getCollection().size() == 1 ) {
			UwContractRsrc uwContract = searchResults.getCollection().get(0);
			return uwContract;
		}

		return null;
	}
	
	private InventoryField createPlanting(AnnualFieldRsrc field, Integer plantingNumber, Integer cropYear) {
		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(insurancePlanId);
		planting.setInventoryFieldGuid(null);
		planting.setLastYearCropCommodityId(null);
		planting.setLastYearCropCommodityName(null);
		planting.setLastYearCropVarietyId(null);
		planting.setLastYearCropVarietyName(null);
		planting.setIsHiddenOnPrintoutInd(false);
		planting.setPlantingNumber(plantingNumber);
		planting.setUnderseededAcres(null);
		planting.setUnderseededCropVarietyId(null);
		planting.setUnderseededCropVarietyName(null);
		
		field.getPlantings().add(planting);

		return planting;
	}

	private InventoryBerries createInventoryBerries(
			InventoryField planting, 
            Integer cropCommodityId,
			String cropCommodityName,
            Integer cropVarietyId,
			String cropVarietyName,
			Double plantedAcres,
			Integer rowSpacing,
			Double plantSpacing,
			Boolean isQuantityInsurableInd,
			Boolean isPlantInsurableInd, 
			String plantInsurabilityTypeCode,
			Integer plantedYear, 
			String bogId, 
			Date bogMowedDate, 
			Date bogRenovatedDate, 
			Boolean isHarvestedInd
			) {
		
		InventoryBerries ib = new InventoryBerries();

		ib.setCropCommodityId(cropCommodityId);
		ib.setCropCommodityName(cropCommodityName);
		ib.setCropVarietyId(cropVarietyId);
		ib.setCropVarietyName(cropVarietyName);
		ib.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		ib.setPlantedYear(plantedYear);
		ib.setPlantedAcres(plantedAcres);
		ib.setRowSpacing(rowSpacing);
		ib.setPlantSpacing(plantSpacing);
		ib.setTotalPlants(null);
		ib.setIsQuantityInsurableInd(isQuantityInsurableInd);
		ib.setIsPlantInsurableInd(isPlantInsurableInd);
		ib.setBogId(bogId);
		ib.setBogMowedDate(bogMowedDate);
		ib.setBogRenovatedDate(bogRenovatedDate);
		ib.setIsHarvestedInd(isHarvestedInd);
		
		planting.setInventoryBerries(ib);

		return ib;
	}	


	private void checkDopYieldContract(DopYieldContractRsrc expected, DopYieldContractRsrc actual) {
		Assert.assertEquals(expected.getBalerWagonInfo(), actual.getBalerWagonInfo());
		Assert.assertEquals(expected.getContractId(), actual.getContractId());
		Assert.assertEquals(expected.getCropYear(), actual.getCropYear());
		Assert.assertEquals(expected.getDefaultYieldMeasUnitTypeCode(), actual.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(expected.getEnteredYieldMeasUnitTypeCode(), actual.getEnteredYieldMeasUnitTypeCode());
		Assert.assertEquals(expected.getGrainFromOtherSourceInd(), actual.getGrainFromOtherSourceInd());
		Assert.assertEquals(expected.getGrowerContractYearId(), actual.getGrowerContractYearId());
		Assert.assertEquals(expected.getInsurancePlanId(), actual.getInsurancePlanId());
		Assert.assertEquals(expected.getTotalLivestock(), actual.getTotalLivestock());
		Assert.assertEquals(expected.getDeclarationOfProductionDate(), actual.getDeclarationOfProductionDate());

		// Dop Yield Contract Commodity Berries
		Assert.assertEquals(expected.getDopYieldContractCommodityBerriesList().size(), actual.getDopYieldContractCommodityBerriesList().size());
		
		for ( int i = 0; i < expected.getDopYieldContractCommodityBerriesList().size(); i++ ) {
			checkDopYieldContractCommodityBerries(expected.getDopYieldContractCommodityBerriesList().get(i), actual.getDopYieldContractCommodityBerriesList().get(i));
		}

		// Fields
		Assert.assertEquals(expected.getFields().size(), actual.getFields().size());
		
		for ( int i = 0; i < expected.getFields().size(); i++ ) {
			checkField(expected.getFields().get(i), actual.getFields().get(i));
		}
	}

	private void checkDopYieldContractCommodityBerries(DopYieldContractCommodityBerries expected, DopYieldContractCommodityBerries actual) {
		Assert.assertEquals(expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals(expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals(expected.getTotalProduction(), actual.getTotalProduction());
		Assert.assertEquals(expected.getTotalProductionOverride(), actual.getTotalProductionOverride());
	}
	
	
	private void checkField(AnnualFieldRsrc expected, AnnualFieldRsrc actual) {
		Assert.assertEquals(expected.getAnnualFieldDetailId(), actual.getAnnualFieldDetailId());
		Assert.assertEquals(expected.getContractedFieldDetailId(), actual.getContractedFieldDetailId());
		Assert.assertEquals(expected.getCropYear(), actual.getCropYear());
		Assert.assertEquals(expected.getDisplayOrder(), actual.getDisplayOrder());
		Assert.assertEquals(expected.getFieldId(), actual.getFieldId());
		Assert.assertEquals(expected.getFieldLabel(), actual.getFieldLabel());
		Assert.assertEquals(expected.getFieldLocation(), actual.getFieldLocation());
		Assert.assertEquals(expected.getIsLeasedInd(), actual.getIsLeasedInd());
		Assert.assertEquals(expected.getLegalLandId(), actual.getLegalLandId());
		Assert.assertEquals(expected.getOtherLegalDescription(), actual.getOtherLegalDescription());
		Assert.assertEquals(expected.getPrimaryPropertyIdentifier(), actual.getPrimaryPropertyIdentifier());

		Assert.assertEquals(expected.getDopYieldFieldCommodityBerriesList().size(), actual.getDopYieldFieldCommodityBerriesList().size());
		
		for ( int i = 0; i < expected.getDopYieldFieldCommodityBerriesList().size(); i++ ) {
			checkDopYieldFieldCommodityBerries(expected.getDopYieldFieldCommodityBerriesList().get(i), actual.getDopYieldFieldCommodityBerriesList().get(i));
		}	
	}
	
	private void checkDopYieldFieldCommodityBerries(DopYieldFieldCommodityBerries expected, DopYieldFieldCommodityBerries actual) {
		Assert.assertEquals(expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals(expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals(expected.getCropYear(), actual.getCropYear());
		Assert.assertEquals(expected.getFieldId(), actual.getFieldId());
		Assert.assertEquals(expected.getTotalProduction(), actual.getTotalProduction());
		Assert.assertEquals(expected.getTotalProductionOverride(), actual.getTotalProductionOverride());

		Assert.assertEquals(expected.getDopYieldFieldVarietyBerriesList().size(), actual.getDopYieldFieldVarietyBerriesList().size());
		
		for ( int i = 0; i < expected.getDopYieldFieldVarietyBerriesList().size(); i++ ) {
			checkDopYieldFieldVarietyBerries(expected.getDopYieldFieldVarietyBerriesList().get(i), actual.getDopYieldFieldVarietyBerriesList().get(i));
		}
	}
	
	private void checkDopYieldFieldVarietyBerries(DopYieldFieldVarietyBerries expected, DopYieldFieldVarietyBerries actual) {
		Assert.assertEquals(expected.getAbandonmentYield(), actual.getAbandonmentYield());
		Assert.assertEquals(expected.getCropVarietyId(), actual.getCropVarietyId());
		Assert.assertEquals(expected.getCropVarietyName(), actual.getCropVarietyName());
		Assert.assertEquals(expected.getIsHiddenOnPrintoutInd(), actual.getIsHiddenOnPrintoutInd());
		Assert.assertEquals(expected.getPlantedAcres(), actual.getPlantedAcres());
		Assert.assertEquals(expected.getSalesYield(), actual.getSalesYield());
		Assert.assertEquals(expected.getSoldShippedYield(), actual.getSoldShippedYield());
		Assert.assertEquals(expected.getTotalProduction(), actual.getTotalProduction());
		Assert.assertEquals(expected.getTotalProductionOverride(), actual.getTotalProductionOverride());
	}
	
	private Integer notNull(Integer value, Integer defaultValue) {
		return (value == null) ? defaultValue : value;
	}

	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}
	
	private void createGrower() throws ValidationException, CirrasUnderwritingServiceException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		GrowerRsrc resource = new GrowerRsrc();
		
		resource.setGrowerId(growerId);
		resource.setGrowerNumber(999888);
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
	
	private void createPolicy(Integer policyId, String policyNumber, Integer cropYear) throws ValidationException, CirrasUnderwritingServiceException {

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

		service.synchronizePolicy(resource);
	}
	
	private void createGrowerContractYear(Integer gcyId, Integer cropYear) throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 because they are not set in the database
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

		service.synchronizeGrowerContractYear(resource);
		
	}

	private void createLegalLand() throws CirrasUnderwritingServiceException, ValidationException {
				
		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(legalLandId);
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
	
	private void createField() throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel("Field Label");
		resource.setFieldLocation(fieldLocation );
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2022);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
		
		service.synchronizeField(resource);
	}
	
	private void createAnnualFieldDetail(Integer annualFieldDetailId, Integer cropYear) throws CirrasUnderwritingServiceException, ValidationException {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setLegalLandId(legalLandId);
		resource.setFieldId(fieldId);
		resource.setCropYear(cropYear);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		service.synchronizeAnnualFieldDetail(resource);
	}

	private void createContractedFieldDetail(Integer contractedFieldDetailId, Integer annualFieldDetailId, Integer gcyId, Boolean isLeased) throws CirrasUnderwritingServiceException, ValidationException {
		
		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId);
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setGrowerContractYearId(gcyId);
		resource.setDisplayOrder(1);
		resource.setIsLeasedInd(isLeased);
		resource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailCreated);
	
		service.synchronizeContractedFieldDetail(resource);

	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
}
