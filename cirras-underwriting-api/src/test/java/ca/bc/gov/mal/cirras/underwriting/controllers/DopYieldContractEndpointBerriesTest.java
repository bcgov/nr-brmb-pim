package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.DopYieldContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldVarietyBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryBerries;
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
	private Integer fieldId1 = 90000016;
	private Integer fieldId2 = 90000019;
	
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
	private DopYieldContractRsrcFactory dopYieldContractRsrcFactory;

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

		service.deleteField(topLevelEndpoints, fieldId1.toString());
		service.deleteField(topLevelEndpoints, fieldId2.toString());
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
		createField(fieldId1);
		createAnnualFieldDetail(annualFieldDetailId1, cropYear1, fieldId1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, gcyId1, false, 1);
		
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
		
		//Calculate expected MEA acres
		List<InventoryField> plantings = fetchedInvContract.getFields().get(0).getPlantings();
		Map<Integer, Double> expectedMeaFieldVarieties = new HashMap<>();		//Field Variety MEA
		Map<String, Double> expectedMeaFieldCommodities = new HashMap<>();		//Field Commodity MEA
		Map<String, Double> expectedAcresFieldCommodities = new HashMap<>();		//Field Commodity Planted Acres
		Map<Integer, Double> expectedMeaContractCommodities = new HashMap<>();	//Contract Commodity MEA
		Map<Integer, Double> expectedAcresContractCommodities = new HashMap<>();	//Contract Commodity Planted Acres
		for (InventoryField inventoryField : plantings) {
			double expectedMea = inventoryField.getInventoryBerries().getMatureEquivalentAcres();
			double expectedPlantedAcres = inventoryField.getInventoryBerries().getPlantedAcres();
			//If variety has been added already, add the new mea to the stored value
			if(expectedMeaFieldVarieties.size() > 0 && expectedMeaFieldVarieties.containsKey(inventoryField.getInventoryBerries().getCropVarietyId())) {
				expectedMeaFieldVarieties.put(inventoryField.getInventoryBerries().getCropVarietyId(), (expectedMeaFieldVarieties.get(inventoryField.getInventoryBerries().getCropVarietyId()) + expectedMea));
			} else {
				expectedMeaFieldVarieties.put(inventoryField.getInventoryBerries().getCropVarietyId(), expectedMea);
			}
			
			Integer cropCommodityId = inventoryField.getInventoryBerries().getCropCommodityId();
			
			//Field Commodity
			String key = inventoryField.getFieldId().toString() + "_" + cropCommodityId;
			if(expectedMeaFieldCommodities.size() > 0 && expectedMeaFieldCommodities.containsKey(key)) {
				expectedMeaFieldCommodities.put(key, expectedMeaFieldCommodities.get(key) + expectedMea);
			} else {
				expectedMeaFieldCommodities.put(key, expectedMea);
			}
			if(expectedAcresFieldCommodities.size() > 0 && expectedAcresFieldCommodities.containsKey(key)) {
				expectedAcresFieldCommodities.put(key, expectedAcresFieldCommodities.get(key) + expectedPlantedAcres);
			} else {
				expectedAcresFieldCommodities.put(key, expectedPlantedAcres);
			}

			//Contract Commodity
			if(expectedMeaContractCommodities.size() > 0 && expectedMeaContractCommodities.containsKey(cropCommodityId)) {
				expectedMeaContractCommodities.put(cropCommodityId, (expectedMeaContractCommodities.get(cropCommodityId) + expectedMea));
			} else {
				expectedMeaContractCommodities.put(cropCommodityId, expectedMea);
			}
			if(expectedAcresContractCommodities.size() > 0 && expectedAcresContractCommodities.containsKey(cropCommodityId)) {
				expectedAcresContractCommodities.put(cropCommodityId, (expectedAcresContractCommodities.get(cropCommodityId) + expectedPlantedAcres));
			} else {
				expectedAcresContractCommodities.put(cropCommodityId, expectedPlantedAcres);
			}
		}
	
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
		expectedField.setFieldId(fieldId1);
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
		expectedDyfcb.setFieldId(fieldId1);		
		expectedDyfcb.setTotalProduction(null);		
		expectedDyfcb.setTotalProductionOverride(null);
		expectedDyfcb.setTotalPlantedAcres(expectedAcresFieldCommodities.get(fieldId1 + "_" + 10));
		expectedDyfcb.setTotalMatureEquivalentAcres(expectedMeaFieldCommodities.get(fieldId1 + "_" + 10));


		DopYieldFieldVarietyBerries expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010689);
		expectedDyfvb.setCropVarietyName("BLUEJAY");
		expectedDyfvb.setPlantedAcres(400.0);
		expectedDyfvb.setMatureEquivalentAcres(expectedMeaFieldVarieties.get(1010689));
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
		expectedDyfcb.setFieldId(fieldId1);		
		expectedDyfcb.setTotalProduction(null);		
		expectedDyfcb.setTotalProductionOverride(null);
		expectedDyfcb.setTotalPlantedAcres(expectedAcresFieldCommodities.get(fieldId1 + "_" + 12));
		expectedDyfcb.setTotalMatureEquivalentAcres(expectedMeaFieldCommodities.get(fieldId1 + "_" + 12));


		expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010694);
		expectedDyfvb.setCropVarietyName("MALAHAT");
		expectedDyfvb.setPlantedAcres(200.0);
		expectedDyfvb.setMatureEquivalentAcres(expectedMeaFieldVarieties.get(1010694));
		expectedDyfvb.setSalesYield(null);
		expectedDyfvb.setSoldShippedYield(null);
		expectedDyfvb.setTotalProduction(null);
		expectedDyfvb.setTotalProductionOverride(null);
		
		expectedDyfcb.getDopYieldFieldVarietyBerriesList().add(expectedDyfvb);

		expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010695);
		expectedDyfvb.setCropVarietyName("MEEKER");
		expectedDyfvb.setPlantedAcres(500.0);
		expectedDyfvb.setMatureEquivalentAcres(expectedMeaFieldVarieties.get(1010695));
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
		expectedDyccb.setTotalPlantedAcres(expectedAcresContractCommodities.get(10));
		expectedDyccb.setTotalMatureEquivalentAcres(expectedMeaContractCommodities.get(10));
		expectedDyccb.setTotalSoldShippedYield(null);
		expectedDyccb.setTotalSalesYield(null);
		expectedDyccb.setTotalAbandonmentYield(null);

		
		expectedDyc.getDopYieldContractCommodityBerriesList().add(expectedDyccb);
		
		expectedDyccb = new DopYieldContractCommodityBerries();
		expectedDyccb.setCropCommodityId(12);
		expectedDyccb.setCropCommodityName("RASPBERRY");
		expectedDyccb.setTotalProduction(null);
		expectedDyccb.setTotalProductionOverride(null);
		expectedDyccb.setTotalPlantedAcres(expectedAcresContractCommodities.get(12));
		expectedDyccb.setTotalMatureEquivalentAcres(expectedMeaContractCommodities.get(12));
		expectedDyccb.setTotalSoldShippedYield(null);
		expectedDyccb.setTotalSalesYield(null);
		expectedDyccb.setTotalAbandonmentYield(null);
		
		expectedDyc.getDopYieldContractCommodityBerriesList().add(expectedDyccb);

		
		// Check rolled-over DOP.
		checkDopYieldContract(expectedDyc, newDyc);

		
		delete();
		
		logger.debug(">testDopYieldRolloverBerries");
	}
	
	//This tests the a sole factory method and not a complete endpoint call
	//Tests the rollup of planted acres and MEA in berries inventory from field commodity level to contract commodity level
	@Test
	public void testSetYieldContractCommodityBerriesTotalAcres() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testSetYieldContractCommodityBerriesTotalAcres");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		dopYieldContractRsrcFactory = new DopYieldContractRsrcFactory();
		
		// Remove default planting.
		AnnualFieldRsrc field1 = new AnnualFieldRsrc();
		AnnualFieldRsrc field2 = new AnnualFieldRsrc();
		List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();
		List<DopYieldFieldCommodityBerries> berriesList1 = new ArrayList<DopYieldFieldCommodityBerries>();
		List<DopYieldFieldCommodityBerries> berriesList2 = new ArrayList<DopYieldFieldCommodityBerries>();

		//Create dop yield field commodities
		//Field 1
		berriesList1.add(createSimpleDopYieldFieldCommodity(10, "BLUEBERRY", 100.0, 50.0));
		berriesList1.add(createSimpleDopYieldFieldCommodity(10, "BLUEBERRY", 200.0, 150.0));
		berriesList1.add(createSimpleDopYieldFieldCommodity(12, "RASPBERRY", 200.0, 150.0));
		
		//Field 2
		berriesList2.add(createSimpleDopYieldFieldCommodity(10, "BLUEBERRY", 300.0, 250.0));
		berriesList2.add(createSimpleDopYieldFieldCommodity(10, "BLUEBERRY", 200.0, 150.0));
		berriesList2.add(createSimpleDopYieldFieldCommodity(12, "RASPBERRY", 50.0, 30.0));
		berriesList2.add(createSimpleDopYieldFieldCommodity(12, "RASPBERRY", 100.0, 70.0));
		berriesList2.add(createSimpleDopYieldFieldCommodity(13, "STRAWBERRY", 100.0, 50.0));
		
		field1.setDopYieldFieldCommodityBerriesList(berriesList1);
		field2.setDopYieldFieldCommodityBerriesList(berriesList2);
		
		fields.add(field1);
		fields.add(field2);

		//Add contract commodities
		List<DeclaredYieldContractCommodityBerriesDto> dtos = new ArrayList<DeclaredYieldContractCommodityBerriesDto>();
		dtos.add(addCommodity(10, "BLUEBERRY"));
		dtos.add(addCommodity(11, "CRANBERRY"));
		dtos.add(addCommodity(12, "RASPBERRY"));
		dtos.add(addCommodity(13, "STRAWBERRY"));
		
		dopYieldContractRsrcFactory.setYieldContractCommodityBerriesTotalAcres(fields, dtos);
		
		//Expected values
		Double plantedAcresBlueberry = 800.0;
		Double meaBlueberry = 600.0;
		Double plantedAcresCranberry = 0.0;
		Double meaCranberry = 0.0;
		Double plantedAcresRaspberry = 350.0;
		Double meaRaspberry = 250.0;
		Double plantedAcresStrawberry = 100.0;
		Double meaStrawberry = 50.0;
		
		//Check values
		assertDeclaredYieldContractCommodityBerries(getDopYieldFieldCommodityBerries(10, dtos), plantedAcresBlueberry, meaBlueberry);
		assertDeclaredYieldContractCommodityBerries(getDopYieldFieldCommodityBerries(11, dtos), plantedAcresCranberry, meaCranberry);
		assertDeclaredYieldContractCommodityBerries(getDopYieldFieldCommodityBerries(12, dtos), plantedAcresRaspberry, meaRaspberry);
		assertDeclaredYieldContractCommodityBerries(getDopYieldFieldCommodityBerries(13, dtos), plantedAcresStrawberry, meaStrawberry);

		logger.debug(">testSetYieldContractCommodityBerriesTotalAcres");
	}

	private void assertDeclaredYieldContractCommodityBerries(DeclaredYieldContractCommodityBerriesDto expected, Double plantedAcres, Double meAcres) {
		Assert.assertNotNull(expected);
		Assert.assertEquals(expected.getTotalPlantedAcres(), plantedAcres, 0.0005);
		Assert.assertEquals(expected.getTotalMatureEquivalentAcres(), meAcres, 0.0005);
	}
	
	private DeclaredYieldContractCommodityBerriesDto getDopYieldFieldCommodityBerries(Integer cropCommodityId, List<DeclaredYieldContractCommodityBerriesDto> dtos) {
		
		DeclaredYieldContractCommodityBerriesDto dto = null;
		
		List<DeclaredYieldContractCommodityBerriesDto> filteredList = dtos.stream()
				.filter(x -> x.getCropCommodityId() == cropCommodityId)
				.collect(Collectors.toList());
		
		if (filteredList != null) {
			dto = filteredList.get(0);
		}
		return dto;
	}

	
	private DeclaredYieldContractCommodityBerriesDto addCommodity(Integer cropCommodityId, String cropCommodityName) {
		DeclaredYieldContractCommodityBerriesDto dto = new DeclaredYieldContractCommodityBerriesDto();
		dto.setCropCommodityId(cropCommodityId);
		dto.setCropCommodityName(cropCommodityName);
		return dto;
	}
	
	private DopYieldFieldCommodityBerries createSimpleDopYieldFieldCommodity(
            Integer cropCommodityId,
			String cropCommodityName,
			Double plantedAcres,
			Double meAcres
			) {
		
		DopYieldFieldCommodityBerries dop = new DopYieldFieldCommodityBerries();

		dop.setCropCommodityId(cropCommodityId);
		dop.setCropCommodityName(cropCommodityName);
		dop.setTotalPlantedAcres(plantedAcres);
		dop.setTotalMatureEquivalentAcres(meAcres);

		return dop;
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

		createField(fieldId1);
		createAnnualFieldDetail(annualFieldDetailId1, cropYear1, fieldId1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, gcyId1, false, 1);

		createField(fieldId2);
		createAnnualFieldDetail(annualFieldDetailId2, cropYear1, fieldId2);
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId2, gcyId1, false, 2);		
		
		UwContractRsrc uwContractRsrc = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContractRsrc);
		Assert.assertNull(uwContractRsrc.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContractRsrc);
		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings().get(0).getInventoryBerries());

		// Field 1
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

		// Field 2
		// Remove default planting.
		field = invContract.getFields().get(1);
		field.getPlantings().remove(0);

		createPlanting(field, 1, cropYear1);
		createInventoryBerries(field.getPlantings().get(0), 10, "BLUEBERRY", 1010689, "BLUEJAY", 600.0, 10, 5.3, true, true, null, 2020, null, null, null, false);
		
		//Create inventory contract
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings());
		Assert.assertEquals(5, fetchedInvContract.getFields().get(0).getPlantings().size());
		
		//Calculate expected MEA acres
		List<InventoryField> plantings = fetchedInvContract.getFields().get(0).getPlantings();
		plantings.addAll(fetchedInvContract.getFields().get(1).getPlantings());
		Map<String, Double> expectedMeaFieldVarieties = new HashMap<>();		//Field Variety MEA
		Map<String, Double> expectedMeaFieldCommodities = new HashMap<>();		//Field Commodity MEA
		Map<String, Double> expectedAcresFieldCommodities = new HashMap<>();		//Field Commodity Planted Acres
		Map<Integer, Double> expectedMeaContractCommodities = new HashMap<>();	//Contract Commodity MEA
		Map<Integer, Double> expectedAcresContractCommodities = new HashMap<>();	//Contract Commodity Planted Acres
		for (InventoryField inventoryField : plantings) {
			String key = inventoryField.getFieldId().toString() + "_" + inventoryField.getInventoryBerries().getCropVarietyId();
			double expectedMea = inventoryField.getInventoryBerries().getMatureEquivalentAcres();
			double expectedPlantedAcres = inventoryField.getInventoryBerries().getPlantedAcres();
			//If variety has been added already, add the new mea to the stored value
			if(expectedMeaFieldVarieties.size() > 0 && expectedMeaFieldVarieties.containsKey(key)) {
				expectedMeaFieldVarieties.put(key, (expectedMeaFieldVarieties.get(key) + expectedMea));
			} else {
				expectedMeaFieldVarieties.put(key, expectedMea);
			}
			
			Integer cropCommodityId = inventoryField.getInventoryBerries().getCropCommodityId();
			
			//Field Commodity
			key = inventoryField.getFieldId().toString() + "_" + cropCommodityId;
			if(expectedMeaFieldCommodities.size() > 0 && expectedMeaFieldCommodities.containsKey(key)) {
				expectedMeaFieldCommodities.put(key, expectedMeaFieldCommodities.get(key) + expectedMea);
			} else {
				expectedMeaFieldCommodities.put(key, expectedMea);
			}
			if(expectedAcresFieldCommodities.size() > 0 && expectedAcresFieldCommodities.containsKey(key)) {
				expectedAcresFieldCommodities.put(key, expectedAcresFieldCommodities.get(key) + expectedPlantedAcres);
			} else {
				expectedAcresFieldCommodities.put(key, expectedPlantedAcres);
			}

			//Contract Commodity
			if(expectedMeaContractCommodities.size() > 0 && expectedMeaContractCommodities.containsKey(cropCommodityId)) {
				expectedMeaContractCommodities.put(cropCommodityId, (expectedMeaContractCommodities.get(cropCommodityId) + expectedMea));
			} else {
				expectedMeaContractCommodities.put(cropCommodityId, expectedMea);
			}
			if(expectedAcresContractCommodities.size() > 0 && expectedAcresContractCommodities.containsKey(cropCommodityId)) {
				expectedAcresContractCommodities.put(cropCommodityId, (expectedAcresContractCommodities.get(cropCommodityId) + expectedPlantedAcres));
			} else {
				expectedAcresContractCommodities.put(cropCommodityId, expectedPlantedAcres);
			}
		}
		
		uwContractRsrc = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContractRsrc);
		Assert.assertNotNull(uwContractRsrc.getInventoryContractGuid());
		Assert.assertNull(uwContractRsrc.getDeclaredYieldContractGuid());
		
		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(uwContractRsrc);

		Assert.assertNotNull(newDyc);
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertNotNull(newDyc.getFields());
		Assert.assertEquals(2, newDyc.getFields().size());
		Assert.assertNotNull(newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList());
		Assert.assertEquals(2, newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().size());
		Assert.assertNotNull(newDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList());
		Assert.assertEquals(1, newDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().size());

		// Expected values
		// Dop Yield Contract
		DopYieldContractRsrc expectedDyc = new DopYieldContractRsrc();
		expectedDyc.setContractId(contractId);
		expectedDyc.setCropYear(cropYear1);
		expectedDyc.setDefaultYieldMeasUnitTypeCode("LB");
		expectedDyc.setGrowerContractYearId(gcyId1);
		expectedDyc.setInsurancePlanId(insurancePlanId);

		// Fields
		// Field 1
		AnnualFieldRsrc expectedField = new AnnualFieldRsrc();
		expectedField.setAnnualFieldDetailId(annualFieldDetailId1);
		expectedField.setContractedFieldDetailId(contractedFieldDetailId1);
		expectedField.setCropYear(cropYear1);
		expectedField.setDisplayOrder(1);
		expectedField.setFieldId(fieldId1);
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
		expectedDyfcb.setFieldId(fieldId1);		
		expectedDyfcb.setTotalProduction(null);		
		expectedDyfcb.setTotalProductionOverride(null);
		expectedDyfcb.setTotalPlantedAcres(expectedAcresFieldCommodities.get(fieldId1 + "_" + 10));
		expectedDyfcb.setTotalMatureEquivalentAcres(expectedMeaFieldCommodities.get(fieldId1 + "_" + 10));

		DopYieldFieldVarietyBerries expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010689);
		expectedDyfvb.setCropVarietyName("BLUEJAY");
		expectedDyfvb.setPlantedAcres(400.0);
		expectedDyfvb.setMatureEquivalentAcres(expectedMeaFieldVarieties.get(fieldId1 + "_" + 1010689));
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
		expectedDyfcb.setFieldId(fieldId1);		
		expectedDyfcb.setTotalProduction(null);		
		expectedDyfcb.setTotalProductionOverride(null);
		expectedDyfcb.setTotalPlantedAcres(expectedAcresFieldCommodities.get(fieldId1 + "_" + 12));
		expectedDyfcb.setTotalMatureEquivalentAcres(expectedMeaFieldCommodities.get(fieldId1 + "_" + 12));

		expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010694);
		expectedDyfvb.setCropVarietyName("MALAHAT");
		expectedDyfvb.setPlantedAcres(200.0);
		expectedDyfvb.setMatureEquivalentAcres(expectedMeaFieldVarieties.get(fieldId1 + "_" + 1010694));
		expectedDyfvb.setSalesYield(null);
		expectedDyfvb.setSoldShippedYield(null);
		expectedDyfvb.setTotalProduction(null);
		expectedDyfvb.setTotalProductionOverride(null);
		
		expectedDyfcb.getDopYieldFieldVarietyBerriesList().add(expectedDyfvb);

		expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010695);
		expectedDyfvb.setCropVarietyName("MEEKER");
		expectedDyfvb.setPlantedAcres(500.0);
		expectedDyfvb.setMatureEquivalentAcres(expectedMeaFieldVarieties.get(fieldId1 + "_" + 1010695));
		expectedDyfvb.setSalesYield(null);
		expectedDyfvb.setSoldShippedYield(null);
		expectedDyfvb.setTotalProduction(null);
		expectedDyfvb.setTotalProductionOverride(null);
		
		expectedDyfcb.getDopYieldFieldVarietyBerriesList().add(expectedDyfvb);
		
		expectedField.getDopYieldFieldCommodityBerriesList().add(expectedDyfcb);

		expectedDyc.getFields().add(expectedField);

		// Field 2
		expectedField = new AnnualFieldRsrc();
		expectedField.setAnnualFieldDetailId(annualFieldDetailId2);
		expectedField.setContractedFieldDetailId(contractedFieldDetailId2);
		expectedField.setCropYear(cropYear1);
		expectedField.setDisplayOrder(2);
		expectedField.setFieldId(fieldId2);
		expectedField.setFieldLabel("Field Label");
		expectedField.setFieldLocation(fieldLocation);
		expectedField.setIsLeasedInd(false);
		expectedField.setLegalLandId(legalLandId);
		expectedField.setOtherLegalDescription("TEST LEGAL LOC 123");
		expectedField.setPrimaryPropertyIdentifier("GF0099999");

		// BLUEBERRY
		expectedDyfcb = new DopYieldFieldCommodityBerries();
		expectedDyfcb.setCropCommodityId(10);
		expectedDyfcb.setCropCommodityName("BLUEBERRY");		
		expectedDyfcb.setCropYear(cropYear1);
		expectedDyfcb.setFieldId(fieldId2);		
		expectedDyfcb.setTotalProduction(null);		
		expectedDyfcb.setTotalProductionOverride(null);
		expectedDyfcb.setTotalPlantedAcres(expectedAcresFieldCommodities.get(fieldId2 + "_" + 10));
		expectedDyfcb.setTotalMatureEquivalentAcres(expectedMeaFieldCommodities.get(fieldId2 + "_" + 10));

		expectedDyfvb = new DopYieldFieldVarietyBerries();
		expectedDyfvb.setAbandonmentYield(null);
		expectedDyfvb.setCropVarietyId(1010689);
		expectedDyfvb.setCropVarietyName("BLUEJAY");
		expectedDyfvb.setPlantedAcres(600.0);
		expectedDyfvb.setMatureEquivalentAcres(expectedMeaFieldVarieties.get(fieldId2 + "_" + 1010689));
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
		expectedDyccb.setTotalPlantedAcres(expectedAcresContractCommodities.get(10));
		expectedDyccb.setTotalMatureEquivalentAcres(expectedMeaContractCommodities.get(10));
		expectedDyccb.setTotalSoldShippedYield(null);
		expectedDyccb.setTotalSalesYield(null);
		expectedDyccb.setTotalAbandonmentYield(null);

		
		expectedDyc.getDopYieldContractCommodityBerriesList().add(expectedDyccb);
		
		expectedDyccb = new DopYieldContractCommodityBerries();
		expectedDyccb.setCropCommodityId(12);
		expectedDyccb.setCropCommodityName("RASPBERRY");
		expectedDyccb.setTotalProduction(null);
		expectedDyccb.setTotalProductionOverride(null);
		expectedDyccb.setTotalPlantedAcres(expectedAcresContractCommodities.get(12));
		expectedDyccb.setTotalMatureEquivalentAcres(expectedMeaContractCommodities.get(12));
		expectedDyccb.setTotalSoldShippedYield(null);
		expectedDyccb.setTotalSalesYield(null);
		expectedDyccb.setTotalAbandonmentYield(null);
		
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
		newDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProductionOverride(33.44);

		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProduction(22.88);   // Calculated
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProductionOverride(33.44);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalSoldShippedYield(33.77);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalSalesYield(22.66);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalAbandonmentYield(11.55);

		// RASPBERRY
		newDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProductionOverride(77.88);

		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProduction(44.66);  // Calculated
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProductionOverride(77.88);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalSoldShippedYield(88.22);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalSalesYield(77.11);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalAbandonmentYield(66.00);

		// Field 1
		// Dop Yield Field Commodity Berries
		// BLUEBERRY
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(22.88);

		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProduction(55.99);   // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(22.88);
		
		// RASPBERRY
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProductionOverride(44.66);
	
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProduction(00.44); // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProductionOverride(44.66);
		

		// Dop Yield Field Variety Berries
		// BLUEBERRY - BLUEJAY
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(11.55);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(22.66);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(33.77);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(55.99);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(11.55);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(22.66);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(33.77);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(11.55 + 22.66 + 33.77);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(55.99);

		// RASPBERRY - MALAHAT
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(null);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		// RASPBERRY - MEEKER
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setAbandonmentYield(66.00);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSalesYield(77.11);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSoldShippedYield(88.22);
		newDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProductionOverride(00.44);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setAbandonmentYield(66.00);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSalesYield(77.11);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSoldShippedYield(88.22);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProduction(66.00 + 77.11 + 88.22);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProductionOverride(00.44);

		// Field 2
		// Dop Yield Field Commodity Berries
		// BLUEBERRY
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).setTotalProduction(null);   // Calculated
		
		// Dop Yield Field Variety Berries
		// BLUEBERRY - BLUEJAY
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(null);  // Calculated
		
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
		fetchedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProductionOverride(null);

		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProduction(88.22);  // Calculated
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProductionOverride(null);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalSoldShippedYield(null);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalSalesYield(null);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalAbandonmentYield(null);

		// RASPBERRY
		fetchedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProductionOverride(54.32);

		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProduction(44.00);  // Calculated
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProductionOverride(54.32);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalSoldShippedYield(22.88);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalSalesYield(11.77);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalAbandonmentYield(00.66);

		
		// Field 1
		// Dop Yield Field Commodity Berries
		// BLUEBERRY
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(88.22);

		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProduction(null);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(88.22);
		
		// RASPBERRY
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProductionOverride(null);

		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProduction(44.00);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProductionOverride(null);
		
		// Dop Yield Field Variety Berries
		// BLUEBERRY - BLUEJAY
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(null);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		// RASPBERRY - MEEKER
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setAbandonmentYield(00.66);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSalesYield(11.77);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSoldShippedYield(22.88);
		fetchedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProductionOverride(44.00);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setAbandonmentYield(00.66);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSalesYield(11.77);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSoldShippedYield(22.88);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProduction(00.66 + 11.77 + 22.88);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProductionOverride(44.00);
		
		DopYieldContractRsrc updatedDyc = service.updateDopYieldContract(fetchedDyc);
		checkDopYieldContract(expectedDyc, updatedDyc);

		// Update DOP - Test Calculations
		// Dop Yield Contract Commodity Berries
		// BLUEBERRY
		updatedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProductionOverride(null);

		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProduction(81.55);  // Calculated: 67.98 + 13.57
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProductionOverride(null);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalSoldShippedYield(63.77);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalSalesYield(42.66);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalAbandonmentYield(21.55);

		
		// RASPBERRY
		updatedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProductionOverride(null);

		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProduction(132.84);  // Calculated: 35.31 + 97.53
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProductionOverride(null);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalSoldShippedYield(22.88);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalSalesYield(11.77);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalAbandonmentYield(00.66);

		// Field 1
		// Dop Yield Field Commodity Berries
		// BLUEBERRY
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(null);

		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProduction(67.98);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(null);
		
		// RASPBERRY
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProductionOverride(null);

		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProduction(132.84);  // Calculated: 35.31 + 97.53
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProductionOverride(null);
		
		// Dop Yield Field Variety Berries
		// BLUEBERRY - BLUEJAY
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(11.55);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(22.66);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(33.77);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(11.55);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(22.66);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(33.77);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(67.98);  // Calculated: 11.55 + 22.66 + 33.77
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);

		// RASPBERRY - MALAHAT
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(97.53);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(null);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(97.53);
		
		// RASPBERRY - MEEKER
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setAbandonmentYield(00.66);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSalesYield(11.77);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSoldShippedYield(22.88);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProductionOverride(null);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setAbandonmentYield(00.66);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSalesYield(11.77);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSoldShippedYield(22.88);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProduction(35.31);  // Calculated: 00.66 + 11.77 + 22.88
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProductionOverride(null);

		
		
		// Field 2
		// Dop Yield Field Commodity Berries
		// BLUEBERRY
		updatedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(13.57);

		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).setTotalProduction(60.0);  // Calculated
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(13.57);
				
		// Dop Yield Field Variety Berries
		// BLUEBERRY - BLUEJAY
		updatedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(10.0);
		updatedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(20.0);
		updatedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(30.0);
		updatedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(10.0);
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(20.0);
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(30.0);
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(10.0 + 20.0 + 30.0);  // Calculated
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		updatedDyc = service.updateDopYieldContract(updatedDyc);
		checkDopYieldContract(expectedDyc, updatedDyc);

		// Update DOP - Test Calculations 2
		// Dop Yield Contract Commodity Berries
		// BLUEBERRY
		updatedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProductionOverride(null);

		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProduction(null);  // Calculated
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalProductionOverride(null);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalSoldShippedYield(null);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalSalesYield(null);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(0).setTotalAbandonmentYield(null);

		// RASPBERRY
		updatedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProductionOverride(null);

		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProduction(97.53);  // Calculated
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalProductionOverride(null);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalSoldShippedYield(null);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalSalesYield(null);
		expectedDyc.getDopYieldContractCommodityBerriesList().get(1).setTotalAbandonmentYield(null);

		// Field 1
		// Dop Yield Field Commodity Berries
		// BLUEBERRY
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(null);

		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProduction(null);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(null);
		
		// RASPBERRY
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProductionOverride(97.53);

		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProduction(null);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).setTotalProductionOverride(97.53);
		
		// Dop Yield Field Variety Berries
		// BLUEBERRY - BLUEJAY
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(null);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);

		// RASPBERRY - MALAHAT
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(null);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		// RASPBERRY - MEEKER
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setAbandonmentYield(null);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSalesYield(null);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSoldShippedYield(null);
		updatedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProductionOverride(null);
		
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setAbandonmentYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSalesYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setSoldShippedYield(null);
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProduction(null);  // Calculated
		expectedDyc.getFields().get(0).getDopYieldFieldCommodityBerriesList().get(1).getDopYieldFieldVarietyBerriesList().get(1).setTotalProductionOverride(null);

		
		
		// Field 2
		// Dop Yield Field Commodity Berries
		// BLUEBERRY
		updatedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(null);

		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).setTotalProduction(null);  // Calculated
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).setTotalProductionOverride(null);
				
		// Dop Yield Field Variety Berries
		// BLUEBERRY - BLUEJAY
		updatedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		updatedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		updatedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		updatedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setAbandonmentYield(null);
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSalesYield(null);
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setSoldShippedYield(null);
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProduction(null);  // Calculated
		expectedDyc.getFields().get(1).getDopYieldFieldCommodityBerriesList().get(0).getDopYieldFieldVarietyBerriesList().get(0).setTotalProductionOverride(null);
		
		updatedDyc = service.updateDopYieldContract(updatedDyc);
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
		Assert.assertEquals(expected.getTotalPlantedAcres(), actual.getTotalPlantedAcres());
		Assert.assertEquals(expected.getTotalMatureEquivalentAcres(), actual.getTotalMatureEquivalentAcres());
		Assert.assertEquals(expected.getTotalSoldShippedYield(), actual.getTotalSoldShippedYield());
		Assert.assertEquals(expected.getTotalSalesYield(), actual.getTotalSalesYield());
		Assert.assertEquals(expected.getTotalAbandonmentYield(), actual.getTotalAbandonmentYield());

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
		Assert.assertEquals(expected.getPlantedAcres(), actual.getPlantedAcres());
		Assert.assertEquals(expected.getMatureEquivalentAcres(), actual.getMatureEquivalentAcres());
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
	
	private void createField(Integer fieldId) throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId);
		resource.setFieldLabel("Field Label");
		resource.setFieldLocation(fieldLocation );
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2022);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
		
		service.synchronizeField(resource);
	}
	
	private void createAnnualFieldDetail(Integer annualFieldDetailId, Integer cropYear, Integer fieldId) throws CirrasUnderwritingServiceException, ValidationException {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setLegalLandId(legalLandId);
		resource.setFieldId(fieldId);
		resource.setCropYear(cropYear);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		service.synchronizeAnnualFieldDetail(resource);
	}

	private void createContractedFieldDetail(Integer contractedFieldDetailId, Integer annualFieldDetailId, Integer gcyId, Boolean isLeased, Integer displayOrder) throws CirrasUnderwritingServiceException, ValidationException {
		
		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId);
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setGrowerContractYearId(gcyId);
		resource.setDisplayOrder(displayOrder);
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
