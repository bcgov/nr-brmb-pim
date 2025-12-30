package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.math.BigDecimal;
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

import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums;
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
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldRollupForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryCoverageTotalForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class DopYieldContractEndpointForageTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(DopYieldContractEndpointForageTest.class);


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
	
	private Integer growerId1 = 90000001;
	private Integer policyId1 = 90000002;
	private Integer gcyId1 = 90000003;
	private Integer contractId1 = 90000004;
	private String policyNumber1 = "998877-21";
	private String contractNumber1 = "998877";
	private Integer cropYear1 = 2021;

	private Integer legalLandId1 = 90000005;
	private Integer fieldId1 = 90000006;
	private Integer annualFieldDetailId1 = 90000007;
	private Integer contractedFieldDetailId1 = 90000008;
	private Integer fieldId2 = 90000026;
	private Integer annualFieldDetailId2 = 90000027;
	private Integer contractedFieldDetailId2 = 90000028;
	private Integer fieldId3 = 90000036;
	private Integer annualFieldDetailId3 = 90000037;
	private Integer contractedFieldDetailId3 = 90000038;
		
	private Integer insurancePlanId = 5; //Forage
	
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
		
		if ( searchResults.getCollection() != null && searchResults.getCollection().size() == 1 ) {
			UwContractRsrc referrer = searchResults.getCollection().get(0);

			if ( referrer.getDeclaredYieldContractGuid() != null ) {
				DopYieldContractRsrc dyc = service.getDopYieldContract(referrer);
				if ( dyc != null ) {
					service.deleteDopYieldContract(dyc);
				}
			}
			
			if ( referrer.getInventoryContractGuid() != null ) { 
				InventoryContractRsrc invContract = service.getInventoryContract(referrer);
				service.deleteInventoryContract(invContract);
			}
		}

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		service.deleteField(topLevelEndpoints, fieldId1.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId2.toString());
		service.deleteField(topLevelEndpoints, fieldId2.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId3.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId3.toString());
		service.deleteField(topLevelEndpoints, fieldId3.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId1.toString());
		
		service.deleteGrowerContractYear(topLevelEndpoints, gcyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId1.toString());
		service.deleteGrower(topLevelEndpoints, growerId1.toString());
	}

	private static final String ctcAlfalfa = "Alfalfa";
	private static final String ctcSilageCorn = "Silage Corn";
	private static final String ctcGrass = "Grass"; 
	Integer cropIdForage = 65;
	Integer cropIdSilageCorn = 71;
	Integer varietyIdAlfalafaGrass = 118;
	Integer varietyIdGrass = 223;
	Integer varietyIdSilageCorn = 1010863;
	String varietyNameAlfalafaGrass = "ALFALFA/GRASS";
	String varietyNameGrass = "GRASS";
	String varietyNameSilageCorn = "SILAGE CORN - UNSPECIFIED";

	String annual = InventoryServiceEnums.PlantDurationType.ANNUAL.toString();
	String perennial = InventoryServiceEnums.PlantDurationType.PERENNIAL.toString();
	
	Date seedingDate = null;
	
	
	@Test
	public void testDopYieldForageRollover() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testDopYieldForageRollover");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		createPolicy();
		createGrowerContractYear();

		createLegalLand();
		createField(fieldId1);
		createAnnualFieldDetail(annualFieldDetailId1, fieldId1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, 1);
		
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
		Assert.assertNull(referrer.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages());

		AnnualFieldRsrc field = invContract.getFields().get(0);

		// Remove default planting.
		field.getPlantings().remove(0);
		

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2024, Calendar.JUNE, 6);
		seedingDate = cal.getTime();
		
		// Planting 1
		InventoryField planting = createPlanting(field, 1, cropYear1, false);
		createInventorySeededForage(planting, cropIdForage, varietyIdAlfalafaGrass, ctcAlfalfa, true, 100.0, "E1", seedingDate); //Alfalfa Grass

		// Planting 2
		planting = createPlanting(field, 2, cropYear1, false);
		createInventorySeededForage(planting, cropIdForage, varietyIdGrass, ctcAlfalfa, false, 150.0, null, null); //Grass - Not insured
				
		// Planting 3
		planting = createPlanting(field, 3, cropYear1, false);
		createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, true, 130.0, null, null); //Silage Corn

		// Planting 4
		planting = createPlanting(field, 4, cropYear1, false);
		createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, true, 70.0, null, null); //Silage Corn		

		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings());
		Assert.assertEquals(4, fetchedInvContract.getFields().get(0).getPlantings().size());

		//Check DOP link
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
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc uwContractRsrc = searchResults.getCollection().get(0);
		
		Assert.assertNotNull(uwContractRsrc);
		Assert.assertNotNull(uwContractRsrc.getLinks());
		boolean dopLinkPresent = false;
		//Check if the rollover dop yield link is in the list
		for (RelLink link : uwContractRsrc.getLinks()) {
			if(link.getHref().contains("rolloverDopYieldContract")) {
				dopLinkPresent = true;
				break;
			}
		}
		Assert.assertTrue(dopLinkPresent);
		
		//Rollover DOP
		Assert.assertNull(uwContractRsrc.getDeclaredYieldContractGuid());
		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(uwContractRsrc);
		
		Assert.assertNotNull(newDyc);
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertNotNull(newDyc.getDopYieldContractCommodityForageList());
		Assert.assertEquals(2, newDyc.getDopYieldContractCommodityForageList().size());
		Assert.assertNotNull(newDyc.getFields());
		Assert.assertEquals(1, newDyc.getFields().size());
		Assert.assertNotNull(newDyc.getFields().get(0).getDopYieldFieldForageList());
		Assert.assertEquals(4, newDyc.getFields().get(0).getDopYieldFieldForageList().size());
		
		//Check commodity totals
		Boolean alfaalfaChecked = false;
		Boolean silageCornChecked = false;
		for (DopYieldContractCommodityForage dyccf : newDyc.getDopYieldContractCommodityForageList()) {
			Assert.assertNotNull(dyccf.getCropCommodityId());
			Assert.assertEquals(dyccf.getCropCommodityId(), dyccf.getCropCommodityId());
			Assert.assertNotNull(dyccf.getCommodityTypeDescription());
			Assert.assertEquals(dyccf.getCommodityTypeDescription(), dyccf.getCommodityTypeCode());
			Assert.assertTrue("Unexpected Commodity Type" + dyccf.getCommodityTypeCode(), dyccf.getCommodityTypeCode().equals(ctcAlfalfa) || dyccf.getCommodityTypeCode().equals(ctcSilageCorn));
			if(dyccf.getCommodityTypeCode().equals(ctcAlfalfa)) {
				Assert.assertEquals((double)100, dyccf.getTotalFieldAcres().doubleValue(), 0.1);
				alfaalfaChecked = true;
			} else if(dyccf.getCommodityTypeCode().equals(ctcSilageCorn)) {
				Assert.assertEquals((double)200, dyccf.getTotalFieldAcres().doubleValue(), 0.1);
				silageCornChecked = true;
			} 
		}
		Assert.assertTrue(alfaalfaChecked);
		Assert.assertTrue(silageCornChecked);

		//Check commodity totals
		alfaalfaChecked = false;
		silageCornChecked = false;
		for (DopYieldFieldRollupForage dyrf : newDyc.getDopYieldFieldRollupForageList()) {
			Assert.assertNotNull(dyrf.getCommodityTypeDescription());
			Assert.assertEquals(dyrf.getCommodityTypeDescription(), dyrf.getCommodityTypeCode());
			Assert.assertTrue("Unexpected Commodity Type" + dyrf.getCommodityTypeCode(), dyrf.getCommodityTypeCode().equals(ctcAlfalfa) || dyrf.getCommodityTypeCode().equals(ctcSilageCorn));
			if(dyrf.getCommodityTypeCode().equals(ctcAlfalfa)) {
				Assert.assertEquals((double)100, dyrf.getTotalFieldAcres().doubleValue(), 0.1);
				alfaalfaChecked = true;
			} else if(dyrf.getCommodityTypeCode().equals(ctcSilageCorn)) {
				Assert.assertEquals((double)200, dyrf.getTotalFieldAcres().doubleValue(), 0.1);
				silageCornChecked = true;
			} 
		}
		Assert.assertTrue(alfaalfaChecked);
		Assert.assertTrue(silageCornChecked);
		
		Boolean checkedPlanting1 = false;
		Boolean checkedPlanting2 = false;
		Boolean checkedPlanting3 = false;
		Boolean checkedPlanting4 = false;
		
		//Check Field level rollover
		for (DopYieldFieldForage dyff : newDyc.getFields().get(0).getDopYieldFieldForageList()) {
			Assert.assertEquals(fieldId1, dyff.getFieldId());
			
			if(dyff.getCropVarietyName().equals(varietyNameAlfalafaGrass)) {
				//Planting 1
				checkedPlanting1 = true;
				Assert.assertTrue(dyff.getIsQuantityInsurableInd());
				Assert.assertEquals(100.0, dyff.getFieldAcres().doubleValue(), 0.1);
				Assert.assertEquals(ctcAlfalfa, dyff.getCommodityTypeCode());
				Assert.assertEquals(ctcAlfalfa, dyff.getCommodityTypeDescription());
				
				Assert.assertEquals("E1", dyff.getPlantInsurabilityTypeCode());
				Assert.assertTrue(dyff.getSeedingDate().equals(seedingDate));
				Assert.assertTrue(dyff.getSeedingYear().equals(dyff.getCropYear() - 3));
				
			} else if(dyff.getCropVarietyName().equals(varietyNameGrass)) {
				//Planting 2
				checkedPlanting2 = true;
				Assert.assertFalse(dyff.getIsQuantityInsurableInd());
				Assert.assertEquals(150.0, dyff.getFieldAcres().doubleValue(), 0.1);
				Assert.assertEquals(ctcAlfalfa, dyff.getCommodityTypeCode());
				Assert.assertEquals(ctcAlfalfa, dyff.getCommodityTypeDescription());
				
				Assert.assertNull(dyff.getSeedingDate());
				Assert.assertNull(dyff.getPlantInsurabilityTypeCode());
				Assert.assertTrue(dyff.getSeedingYear().equals(dyff.getCropYear() - 3));
				
			} else if(dyff.getCropVarietyName().equals(varietyNameSilageCorn)) {
				Assert.assertTrue(dyff.getIsQuantityInsurableInd());
				Assert.assertEquals(ctcSilageCorn, dyff.getCommodityTypeCode());
				Assert.assertEquals(ctcSilageCorn, dyff.getCommodityTypeDescription());
				if(dyff.getFieldAcres().equals(130.0)) {
					//Planting 3
					checkedPlanting3 = true;
				} else if(dyff.getFieldAcres().equals(70.0)) {
					//Planting 4
					checkedPlanting4 = true;
				} else {
					Assert.fail("Unexpected field acres: " + dyff.getFieldAcres());
				}
				
				Assert.assertNull(dyff.getSeedingDate());
				Assert.assertNull(dyff.getPlantInsurabilityTypeCode());
				Assert.assertTrue(dyff.getSeedingYear().equals(dyff.getCropYear() - 3));
				
			}
		}
		
		Assert.assertTrue(checkedPlanting1);
		Assert.assertTrue(checkedPlanting2);
		Assert.assertTrue(checkedPlanting3);
		Assert.assertTrue(checkedPlanting4);
		
		//Create DOP Contract
		
		delete();
		
		logger.debug(">testDopYieldForageRollover");

	}
	

	@Test
	public void testInsertUpdateDeleteDopYieldForageContract() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInsertUpdateDeleteDopYieldForageContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2022, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();
		
		createExpectedContractCommodityList((double)100000, (double)1250, (double)5, (double)200000, 4705.8824, 9.4118);
		
		createExpectedYieldRollupList((double)11440.0000, (double)57.2, 420);
		
		createContractAndInventory();
		
		//Get uw contract
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
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		
		//Rollover DOP ********* 
		Assert.assertNull(referrer.getDeclaredYieldContractGuid());
		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(referrer);
		
		Assert.assertNotNull(newDyc);
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertEquals(contractId1, newDyc.getContractId());
		Assert.assertEquals(cropYear1, newDyc.getCropYear());
		
		//Field 1 checks
		AnnualFieldRsrc fetchedField1 = getField(fieldId1, newDyc.getFields());
		Assert.assertTrue(fetchedField1.getUwComments().size() == 1);
		Assert.assertEquals(3, fetchedField1.getDopYieldFieldForageList().size());

		//Commodity totals list
		Assert.assertNotNull(newDyc.getDopYieldContractCommodityForageList());
		Assert.assertEquals(3, newDyc.getDopYieldContractCommodityForageList().size());
		checkDopYieldContractCommodities(newDyc.getDopYieldContractCommodityForageList(), true);


		//Rollup list
		Assert.assertNotNull(newDyc.getDopYieldFieldRollupForageList());
		Assert.assertEquals(3, newDyc.getDopYieldFieldRollupForageList().size());

		newDyc.setDeclarationOfProductionDate(dopDate);
		newDyc.setDefaultYieldMeasUnitTypeCode("TON");
		newDyc.setEnteredYieldMeasUnitTypeCode("LB");
		newDyc.setGrainFromOtherSourceInd(false);
		newDyc.setBalerWagonInfo("Test Baler");
		newDyc.setTotalLivestock(100);
		newDyc.setInsurancePlanId(insurancePlanId);		
		
		// update the existing field level comment and add another field comment before create DOP
		List<UnderwritingComment> uwComments = fetchedField1.getUwComments();
		uwComments.get(0).setUnderwritingComment("Updated comment 1");

		// add a DOP contract level 
		UnderwritingComment contractUnderwritingComment = new UnderwritingComment();
		List<UnderwritingComment> contractUwComments = newDyc.getUwComments();
	
		contractUnderwritingComment = new UnderwritingComment();
		contractUnderwritingComment.setAnnualFieldDetailId(null);
		contractUnderwritingComment.setUnderwritingComment("Comment for dopContractYieldGuid " + newDyc.getDeclaredYieldContractGuid());
		contractUnderwritingComment.setUnderwritingCommentGuid(null);
		contractUnderwritingComment.setUnderwritingCommentTypeCode("DOP");
		contractUnderwritingComment.setUnderwritingCommentTypeDesc("Declaration of Production");		
		contractUnderwritingComment.setDeclaredYieldContractGuid(newDyc.getDeclaredYieldContractGuid());
		contractUnderwritingComment.setGrowerContractYearId(gcyId1);
	
		contractUwComments.add(contractUnderwritingComment);
		newDyc.setUwComments(contractUwComments);
		
		//Add forage dop cuts
		createCuts(newDyc);
		
		//Add contract commodities
		newDyc.setDopYieldContractCommodityForageList(expectedDopYieldContractCommodityForageList);
		
		//CREATE DOP *********************************************************************************************
		DopYieldContractRsrc fetchedDyc = service.createDopYieldContract(topLevelEndpoints, newDyc);
		
		//Check forage contract level fields
		Assert.assertEquals("BalerWagonInfo", newDyc.getBalerWagonInfo(), fetchedDyc.getBalerWagonInfo());
		Assert.assertEquals("TotalLivestock", newDyc.getTotalLivestock().doubleValue(), fetchedDyc.getTotalLivestock().doubleValue(), 0.1);

		//Check Commodity Totals
		checkDopYieldContractCommodities(fetchedDyc.getDopYieldContractCommodityForageList(), false);
		
		//Check Yield Rollup
		checkDopYieldRollup(fetchedDyc.getDopYieldFieldRollupForageList());
		
		//Check dop forage and cuts
		checkDopYieldFieldForage(newDyc.getFields(), 
				fetchedDyc.getFields(),
				newDyc.getEnteredYieldMeasUnitTypeCode());

		// check that there is one DOP contract level comment
		Assert.assertEquals("CountContractUwComments", newDyc.getUwComments().size(), fetchedDyc.getUwComments().size());
		Assert.assertEquals("UnderwritingCommentTypeCode", newDyc.getUwComments().get(0).getUnderwritingCommentTypeCode(), fetchedDyc.getUwComments().get(0).getUnderwritingCommentTypeCode());
		Assert.assertEquals("UnderwritingComment", newDyc.getUwComments().get(0).getUnderwritingComment() , fetchedDyc.getUwComments().get(0).getUnderwritingComment());
		Assert.assertEquals("DeclaredYieldContractGuid", fetchedDyc.getDeclaredYieldContractGuid(), fetchedDyc.getUwComments().get(0).getDeclaredYieldContractGuid());
		Assert.assertEquals("GrowerContractYearId", gcyId1, fetchedDyc.getUwComments().get(0).getGrowerContractYearId());
		Assert.assertNull("NullAnnualFieldDetailId", fetchedDyc.getUwComments().get(0).getAnnualFieldDetailId());
		
		
		//Update contract level data
		fetchedDyc.setBalerWagonInfo("Test Baler 2");
		fetchedDyc.setTotalLivestock(55);
		
		//Update silage corn cut 1
		fetchedField1 = getField(fieldId1, fetchedDyc.getFields());
		DopYieldFieldForage dyff = getDopYieldFieldForage(ctcSilageCorn, fetchedField1.getDopYieldFieldForageList());
		DopYieldFieldForageCut cut = getCut(1, dyff.getDopYieldFieldForageCuts());
		cut.setTotalBalesLoads(50);
		cut.setWeight(100000.0);
		cut.setMoisturePercent(75.0);
		
		//Add updated contract commodities
		createExpectedContractCommodityList((double)200000, (double)2500, (double)10, (double)100000, 2352.9412, 4.7059);
		fetchedDyc.setDopYieldContractCommodityForageList(expectedDopYieldContractCommodityForageList);
		
		createExpectedYieldRollupList((double)10065, (double)50.325, 370);

		//UPDATE DOP ***********************************************
		DopYieldContractRsrc updatedDyc = service.updateDopYieldContract(fetchedDyc);

		//Check Commodity Totals
		checkDopYieldContractCommodities(updatedDyc.getDopYieldContractCommodityForageList(), false);

		//Check Yield Rollup
		checkDopYieldRollup(updatedDyc.getDopYieldFieldRollupForageList());

		//Check dop forage and cuts
		checkDopYieldFieldForage(fetchedDyc.getFields(), 
				updatedDyc.getFields(),
				fetchedDyc.getEnteredYieldMeasUnitTypeCode());
			
		//Check forage contract level fields
		Assert.assertEquals("BalerWagonInfo", fetchedDyc.getBalerWagonInfo(), updatedDyc.getBalerWagonInfo());
		Assert.assertEquals("TotalLivestock", fetchedDyc.getTotalLivestock().doubleValue(), updatedDyc.getTotalLivestock().doubleValue(), 0.1);

		//Delete field to test if existing dop is deleted as well
		fetchedField1 = getField(fieldId1, updatedDyc.getFields());
		Assert.assertNotNull(fetchedField1.getDopYieldFieldForageList());
		Assert.assertTrue(fetchedField1.getDopYieldFieldForageList().size() > 0);
		Assert.assertNotNull(fetchedField1.getDopYieldFieldForageList().get(0).getDopYieldFieldForageCuts());
		Assert.assertTrue(fetchedField1.getDopYieldFieldForageList().get(0).getDopYieldFieldForageCuts().size() > 0);

		//There is dop on the field and if it didn't get deleted it would fail
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		service.deleteField(topLevelEndpoints, fieldId1.toString());
		
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
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());
		
		referrer = searchResults.getCollection().get(0);
		
		updatedDyc = service.getDopYieldContract(referrer);
		//Check if field has been deleted
		Assert.assertEquals(2, updatedDyc.getFields().size());

		//Delete DOP
		service.deleteDopYieldContract(updatedDyc);
		
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
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getDeclaredYieldContractGuid());

		delete();
		
		logger.debug(">testInsertUpdateDeleteDopYieldForageContract");

	}

	private DopYieldContractCommodityForage createDopYieldContractCommodityForage(
			String commodityTypeCode, 
			Double totalFieldAcres, 
			Double harvestedAcres, 
			Integer totalBales, 
			Double weight, 
			Double weightDefaultUnit, 
			Double moisturePercent,
			Double quantityHarvestedTons, 
			Double yieldPerAcre,
			Integer cropCommodityId, 
			String plantDurationTypeCode
			) {
		
		DopYieldContractCommodityForage model = new DopYieldContractCommodityForage();
		model.setCommodityTypeCode(commodityTypeCode);
		model.setCommodityTypeDescription(commodityTypeCode);
		model.setTotalFieldAcres(totalFieldAcres);
		model.setHarvestedAcres(harvestedAcres);
		model.setTotalBalesLoads(totalBales);
		model.setWeight(weight);
		model.setWeightDefaultUnit(weightDefaultUnit);
		model.setMoisturePercent(moisturePercent);
		model.setQuantityHarvestedTons(quantityHarvestedTons);
		model.setYieldPerAcre(yieldPerAcre);
		model.setCropCommodityId(cropCommodityId);
		model.setPlantDurationTypeCode(plantDurationTypeCode);

		
		return model;
	}

	private DopYieldFieldRollupForage createDopYieldFieldRollupForage(
			String commodityTypeCode, 
			Double totalFieldAcres, 
			Double harvestedAcres, 
			Double quantityHarvestedTons, 
			Double yieldPerAcre, 
			Integer totalBalesLoads
			) {

		DopYieldFieldRollupForage model = new DopYieldFieldRollupForage();
		model.setCommodityTypeCode(commodityTypeCode);
		model.setTotalFieldAcres(totalFieldAcres);
		model.setTotalBalesLoads(totalBalesLoads);
		model.setHarvestedAcres(harvestedAcres);
		model.setQuantityHarvestedTons(quantityHarvestedTons);
		model.setYieldPerAcre(yieldPerAcre);
		
		return model;
	}	
	
	private List<DopYieldContractCommodityForage> expectedDopYieldContractCommodityForageList;
	
	private void createExpectedContractCommodityList(
			Double weightSilageCorn, 
			Double quantityHarvestedTonsSilageCorn,
			Double yieldPerAcreSilageCorn,
			Double weightAlfalfa,
			Double quantityHarvestedTonsAlfalfa,
			Double yieldPerAcreAlfalfa) {

		//entered in LBS
		Double defaultWeightSilageCorn = weightSilageCorn / 2000;
		Double defaultWeightAlfalfa = weightAlfalfa / 2000;
		Double harvestedAcresSilageCorn = (double)250;
		Double harvestedAcresAlfalfa = (double)500;
		
		expectedDopYieldContractCommodityForageList = new ArrayList<DopYieldContractCommodityForage>();
		
		DopYieldContractCommodityForage dyccfSilageCorn = createDopYieldContractCommodityForage(
				ctcSilageCorn, 						// commodityTypeCode
				(double)300, 						// totalFieldAcres
				harvestedAcresSilageCorn,			// harvestedAcres
				100, 								// totalBales
				weightSilageCorn,					// weight  					100000	
				defaultWeightSilageCorn,			// weightDefaultUnit		50
				(double)75, 						// moisturePercent
				quantityHarvestedTonsSilageCorn,	// quantityHarvestedTons	1250
				yieldPerAcreSilageCorn,				// yieldPerAcre				6.25
				cropIdSilageCorn,					// cropCommodityId
				annual);							// plantDurationTypeCode

		DopYieldContractCommodityForage dyccfAlfalfa = createDopYieldContractCommodityForage(
				ctcAlfalfa, 
				(double)400, 					// totalFieldAcres
				harvestedAcresAlfalfa,			// harvestedAcres
				200, 							// totalBales
				weightAlfalfa,					// weight					200000
				defaultWeightAlfalfa,			// weightDefaultUnit		100
				(double)80, 					// moisturePercent
				quantityHarvestedTonsAlfalfa,	// quantityHarvestedTons	4705.8824
				yieldPerAcreAlfalfa,			// yieldPerAcre				11.7647
				cropIdForage,					// cropCommodityId
				perennial);		 				// plantDurationTypeCode


		DopYieldContractCommodityForage dyccfGrass = createDopYieldContractCommodityForage(
				ctcGrass, 
				(double)100, 				// totalFieldAcres
				null,		 				// harvestedAcres
				null, 						// totalBales
				null, 						// weight
				null,						// weightDefaultUnit
				null, 						// moisturePercent
				null,						// quantityHarvestedTons
				null,						// yieldPerAcre
				cropIdForage,				// cropCommodityId
				perennial);		 			// plantDurationTypeCode

		expectedDopYieldContractCommodityForageList.add(dyccfSilageCorn);
		expectedDopYieldContractCommodityForageList.add(dyccfAlfalfa);
		expectedDopYieldContractCommodityForageList.add(dyccfGrass);
		
	}
	
	private List<DopYieldFieldRollupForage> expectedDopYieldFieldRollupForageList;
	
	private void createExpectedYieldRollupList(Double quantityHarvestedTonsSilageCorn, Double yieldPerAcreSilageCorn, Integer totalBalesSilageCorn) {
		
		expectedDopYieldFieldRollupForageList = new ArrayList<DopYieldFieldRollupForage>();
		
		DopYieldFieldRollupForage dyrfSilageCorn = createDopYieldFieldRollupForage(
				ctcSilageCorn, 				// commodityTypeCode
				(double)300, 				// totalFieldAcres
				(double)200, 				// harvestedAcres
				quantityHarvestedTonsSilageCorn, // quantityHarvestedTons
				yieldPerAcreSilageCorn,		// yieldPerAcre
				totalBalesSilageCorn);		// totalBales
		
		DopYieldFieldRollupForage dyrfAlfalfa = createDopYieldFieldRollupForage(
				ctcAlfalfa, 
				(double)400, 				// totalFieldAcres
				(double)400, 				// harvestedAcres
				(double)11244.1176,			// quantityHarvestedTons
				(double)28.1103,			// yieldPerAcre
				555);						// totalBales
		
		DopYieldFieldRollupForage dyrfGrass = createDopYieldFieldRollupForage(
				ctcGrass, 
				(double)100, 				// totalFieldAcres
				(double)100, 				// harvestedAcres
				(double)4705.8824,			// quantityHarvestedTons
				(double)47.0588,				// yieldPerAcre
				200);						// totalBales
		

		expectedDopYieldFieldRollupForageList.add(dyrfSilageCorn);
		expectedDopYieldFieldRollupForageList.add(dyrfAlfalfa);
		expectedDopYieldFieldRollupForageList.add(dyrfGrass);
		
	}
	
	private void checkDopYieldContractCommodities(List<DopYieldContractCommodityForage> dopYieldContractCommodityForageList, Boolean checkRolledover) {
		
			for(DopYieldContractCommodityForage actual : dopYieldContractCommodityForageList) {
				
				DopYieldContractCommodityForage expected = getDopYieldContractCommodityForage(actual.getCommodityTypeCode(), expectedDopYieldContractCommodityForageList);
				
				if(checkRolledover) {
					checkRolledoverDopYieldContractCommodityForage(expected, actual);
				} else {
					checkDopYieldContractCommodityForage(expected, actual);
				}
			}
		}
	
	private void checkDopYieldRollup(List<DopYieldFieldRollupForage> dopYieldFieldRollupForageList) {
		
			for(DopYieldFieldRollupForage actual : dopYieldFieldRollupForageList) {
				
				DopYieldFieldRollupForage expected = getDopYieldFieldRollupForage(actual.getCommodityTypeCode(), expectedDopYieldFieldRollupForageList);
				
				checkDopYieldFieldRollupForage(expected, actual);
			}
		}	

	private void checkDopYieldContractCommodityForage(DopYieldContractCommodityForage expected, DopYieldContractCommodityForage actual) {
		
		Double actualQuantityHarvestedTons = actual.getQuantityHarvestedTons();
		if(actual.getQuantityHarvestedTons() != null) {
			actualQuantityHarvestedTons = BigDecimal.valueOf(actual.getQuantityHarvestedTons())
	                .setScale(4, BigDecimal.ROUND_HALF_UP)
	                .doubleValue();
		}
		
		Double actualYieldPerAcre = actual.getYieldPerAcre();
		if(actual.getQuantityHarvestedTons() != null) {
			actualYieldPerAcre = BigDecimal.valueOf(actual.getYieldPerAcre())
	                .setScale(4, BigDecimal.ROUND_HALF_UP)
	                .doubleValue();
		}
		
		Assert.assertEquals("CommodityTypeCode", expected.getCommodityTypeCode(), actual.getCommodityTypeCode());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("PlantDurationTypeCode", expected.getPlantDurationTypeCode(), actual.getPlantDurationTypeCode());
		Assert.assertEquals("TotalFieldAcres", expected.getTotalFieldAcres(), actual.getTotalFieldAcres());
		Assert.assertEquals("HarvestedAcres", expected.getHarvestedAcres(), actual.getHarvestedAcres());
		Assert.assertEquals("TotalBalesLoads", expected.getTotalBalesLoads(), actual.getTotalBalesLoads());
		Assert.assertEquals("Weight", expected.getWeight(), actual.getWeight());
		Assert.assertEquals("WeightDefaultUnit", expected.getWeightDefaultUnit(), actual.getWeightDefaultUnit());
		Assert.assertEquals("MoisturePercent", expected.getMoisturePercent(), actual.getMoisturePercent());
		Assert.assertEquals("QuantityHarvestedTons", expected.getQuantityHarvestedTons(), actualQuantityHarvestedTons);
		Assert.assertEquals("YieldPerAcre", expected.getYieldPerAcre(), actualYieldPerAcre);

	}
	
	private void checkRolledoverDopYieldContractCommodityForage(DopYieldContractCommodityForage expected, DopYieldContractCommodityForage actual) {
		
		Assert.assertEquals("CommodityTypeCode", expected.getCommodityTypeCode(), actual.getCommodityTypeCode());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("PlantDurationTypeCode", expected.getPlantDurationTypeCode(), actual.getPlantDurationTypeCode());
		Assert.assertEquals("TotalFieldAcres", expected.getTotalFieldAcres(), actual.getTotalFieldAcres());
		Assert.assertNull("HarvestedAcres", actual.getHarvestedAcres());
		Assert.assertNull("TotalBalesLoads", actual.getTotalBalesLoads());
		Assert.assertNull("Weight", actual.getWeight());
		Assert.assertNull("WeightDefaultUnit", actual.getWeightDefaultUnit());
		Assert.assertNull("MoisturePercent", actual.getMoisturePercent());
		Assert.assertNull("QuantityHarvestedTons", actual.getQuantityHarvestedTons());
		Assert.assertNull("YieldPerAcre", actual.getYieldPerAcre());

	}
	
	private void checkDopYieldFieldRollupForage(DopYieldFieldRollupForage expected, DopYieldFieldRollupForage actual) {
		
		Double actualQuantityHarvestedTons = actual.getQuantityHarvestedTons();
		if(actual.getQuantityHarvestedTons() != null) {
			actualQuantityHarvestedTons = BigDecimal.valueOf(actual.getQuantityHarvestedTons())
	                .setScale(4, BigDecimal.ROUND_HALF_UP)
	                .doubleValue();
		}
		
		Double actualYieldPerAcre = actual.getYieldPerAcre();
		if(actual.getQuantityHarvestedTons() != null) {
			actualYieldPerAcre = BigDecimal.valueOf(actual.getYieldPerAcre())
	                .setScale(4, BigDecimal.ROUND_HALF_UP)
	                .doubleValue();
		}
		
		Assert.assertEquals("CommodityTypeCode", expected.getCommodityTypeCode(), actual.getCommodityTypeCode());
		Assert.assertEquals("TotalFieldAcres", expected.getTotalFieldAcres(), actual.getTotalFieldAcres());
		Assert.assertEquals("HarvestedAcres", expected.getHarvestedAcres(), actual.getHarvestedAcres());
		Assert.assertEquals("TotalBalesLoads", expected.getTotalBalesLoads(), actual.getTotalBalesLoads());
		Assert.assertEquals("QuantityHarvestedTons", expected.getQuantityHarvestedTons(), actualQuantityHarvestedTons);
		Assert.assertEquals("YieldPerAcre", expected.getYieldPerAcre(), actualYieldPerAcre);

	}
	
	private void createCuts(DopYieldContractRsrc dopContract) {

		//Field 1 *********************
		AnnualFieldRsrc field1 = getField(fieldId1, dopContract.getFields());

		List<DopYieldFieldForage> dopFields = field1.getDopYieldFieldForageList();
		List<DopYieldFieldForageCut> cuts = new ArrayList<DopYieldFieldForageCut>();
		
		//Silage Corn QTY
		DopYieldFieldForage dyff = getDopYieldFieldForage(ctcSilageCorn, dopFields);
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, 100, (double)200000, (double)80, false)); //100 ton
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 2, 120, (double)160000, (double)85, false));  //80 ton
		dyff.setDopYieldFieldForageCuts(cuts);

		//Alfalfa NOT QTY
		dyff = getDopYieldFieldForage(ctcAlfalfa, dopFields);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, 100, (double)200000, (double)80, false)); //100 ton
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 2, 115, (double)150000, (double)78, false)); //75 ton
		dyff.setDopYieldFieldForageCuts(cuts);

		//No Commodity -> Expect null after saving and reloading as no cut data should be saved for plantings without crops 
		dyff = getDopYieldFieldForage(null, dopFields);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, 100, (double)200000, (double)80, false)); //100 ton
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 2, 115, (double)150000, (double)78, false)); //75 ton
		dyff.setDopYieldFieldForageCuts(cuts);

		//Field 2 *********************
		AnnualFieldRsrc field2 = getField(fieldId2, dopContract.getFields());

		dopFields = field2.getDopYieldFieldForageList();

		//Silage Corn QTY
		dyff = getDopYieldFieldForage(ctcSilageCorn, dopFields);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, 200, (double)400000, (double)80, false)); //200 ton
		dyff.setDopYieldFieldForageCuts(cuts);
		
		//Alfalfa QTY
		dyff = getDopYieldFieldForage(ctcAlfalfa, dopFields);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, 110, (double)160000, (double)85, false)); //80 ton
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 2, 130, (double)180000, (double)80, false)); //90 ton
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 3, 150, (double)200000, (double)75, true)); //deleted by user  //100 ton
		dyff.setDopYieldFieldForageCuts(cuts);

		//Field 3 *********************
		AnnualFieldRsrc field3 = getField(fieldId3, dopContract.getFields());

		dopFields = field3.getDopYieldFieldForageList();

		//Silage Corn QTY => no value in cuts (only one cut)
		dyff = getDopYieldFieldForage(ctcSilageCorn, dopFields);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, null, null, null, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		
		//Alfalfa QTY
		dyff = getDopYieldFieldForage(ctcAlfalfa, dopFields);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, 100, (double)200000, (double)80, false));  //100 ton
		//Alfalfa NULL VALUES
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 2, null, null, null, false));  //NULL Values
		dyff.setDopYieldFieldForageCuts(cuts);
		
		//Grass QTY
		dyff = getDopYieldFieldForage(ctcGrass, dopFields);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, 200, (double)200000, (double)80, false));  //100 ton
		dyff.setDopYieldFieldForageCuts(cuts);
	}

	private void checkDopYieldFieldForage(List<AnnualFieldRsrc> expectedFields, List<AnnualFieldRsrc> actualFields, String enteredYieldMeasUnitTypeCode) {
		
		Assert.assertEquals(expectedFields.size(), actualFields.size());
		
		for(AnnualFieldRsrc expectedField : expectedFields) {
			AnnualFieldRsrc actualField = getField(expectedField.getFieldId(), actualFields);
			Assert.assertNotNull(expectedField.getDopYieldFieldForageList());
			for(DopYieldFieldForage expectedDop : expectedField.getDopYieldFieldForageList()) {
				Assert.assertNotNull(actualField.getDopYieldFieldForageList());
				DopYieldFieldForage actualDop = getDopYieldFieldForage(expectedDop.getCommodityTypeCode(), actualField.getDopYieldFieldForageList());
				
				checkDopYieldFieldForage(expectedDop, actualDop);
				
				for(DopYieldFieldForageCut expectedCut : expectedDop.getDopYieldFieldForageCuts()) {
					if(expectedCut.getDeletedByUserInd() == false) {
						DopYieldFieldForageCut actualCut = getCut(expectedCut.getCutNumber(), actualDop.getDopYieldFieldForageCuts());
						if(actualDop.getCropCommodityId() == null) {
							//If there is no commodity, no cut data should be saved
							Assert.assertEquals("InventoryFieldGuid", expectedCut.getInventoryFieldGuid(), actualCut.getInventoryFieldGuid());
							Assert.assertEquals("CutNumber", expectedCut.getCutNumber(), actualCut.getCutNumber());
							Assert.assertNull("TotalBalesLoads", actualCut.getTotalBalesLoads());
							Assert.assertNull("Weight", actualCut.getWeight());
							Assert.assertNull("WeightDefaultUnit", actualCut.getWeightDefaultUnit());
							Assert.assertNull("MoisturePercent", actualCut.getMoisturePercent());
						} else {
							checkCut(expectedCut, actualCut, enteredYieldMeasUnitTypeCode);
						}
					}
				}
				
			}
		}
	}

	private void checkDopYieldFieldForage(DopYieldFieldForage expectedDop, DopYieldFieldForage actualDop) {
		Assert.assertEquals("CommodityTypeCode", expectedDop.getCommodityTypeCode(), actualDop.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDescription", expectedDop.getCommodityTypeDescription(), actualDop.getCommodityTypeDescription());
		Assert.assertEquals("IsQuantityInsurableInd", expectedDop.getIsQuantityInsurableInd(), actualDop.getIsQuantityInsurableInd());
		Assert.assertEquals("FieldAcres", expectedDop.getFieldAcres(), actualDop.getFieldAcres());
		Assert.assertEquals("CropVarietyName", expectedDop.getCropVarietyName(), actualDop.getCropVarietyName());
		Assert.assertEquals("CropVarietyId", expectedDop.getCropVarietyId(), actualDop.getCropVarietyId());
		Assert.assertEquals("CropCommodityId", expectedDop.getCropCommodityId(), actualDop.getCropCommodityId());
		Assert.assertEquals("PlantDurationTypeCode", expectedDop.getPlantDurationTypeCode(), actualDop.getPlantDurationTypeCode());
		Assert.assertEquals("IsHiddenOnPrintoutInd", expectedDop.getIsHiddenOnPrintoutInd(), actualDop.getIsHiddenOnPrintoutInd());
		
	}

	private void checkCut(DopYieldFieldForageCut expectedCut, DopYieldFieldForageCut actualCut, String enteredYieldMeasUnitTypeCode) {
		if(expectedCut.getDeclaredYieldFieldForageGuid() != null) {
			Assert.assertEquals("DeclaredYieldFieldForageGuid", expectedCut.getDeclaredYieldFieldForageGuid(), actualCut.getDeclaredYieldFieldForageGuid());
		}
		Assert.assertEquals("InventoryFieldGuid", expectedCut.getInventoryFieldGuid(), actualCut.getInventoryFieldGuid());
		Assert.assertEquals("CutNumber", expectedCut.getCutNumber(), actualCut.getCutNumber());
		Assert.assertEquals("TotalBalesLoads", expectedCut.getTotalBalesLoads(), actualCut.getTotalBalesLoads());
		Assert.assertEquals("Weight", expectedCut.getWeight(), actualCut.getWeight());
		if(actualCut.getWeight() != null) {
			if(enteredYieldMeasUnitTypeCode.equals("LB")) {
				//If entered in LB, the weight default unit 2000 times smaller
				Double expectedWeightDefaultUnit = actualCut.getWeight() / 2000;
				Assert.assertEquals("WeightDefaultUnit", expectedWeightDefaultUnit, actualCut.getWeightDefaultUnit());
			} else {
				//If entered in default units (TON) the weight and the weight default unit are equal
				Assert.assertEquals("WeightDefaultUnit", actualCut.getWeight(), actualCut.getWeightDefaultUnit());
			}
		} else {
			Assert.assertNull("WeightDefaultUnit", actualCut.getWeightDefaultUnit());
		}
		
		Assert.assertEquals("MoisturePercent", expectedCut.getMoisturePercent(), actualCut.getMoisturePercent());
	}
	
	protected void createContractAndInventory() throws ValidationException, CirrasUnderwritingServiceException {
		createGrower();
		createPolicy();
		createGrowerContractYear();

		createLegalLand();
		createField(fieldId1);
		createAnnualFieldDetail(annualFieldDetailId1, fieldId1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, 1);
		createField(fieldId2);
		createAnnualFieldDetail(annualFieldDetailId2, fieldId2);
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId2, 2);
		createField(fieldId3);
		createAnnualFieldDetail(annualFieldDetailId3, fieldId3);
		createContractedFieldDetail(contractedFieldDetailId3, annualFieldDetailId3, 3);
		
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
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertEquals(3, invContract.getFields().size());

		//Field 1 ****		
		AnnualFieldRsrc field1 = getField(fieldId1, invContract.getFields());

		// Remove default planting.
		field1.getPlantings().remove(0);
		
		
		
		// Planting 1
		InventoryField planting = createPlanting(field1, 1, cropYear1, false);
		createInventorySeededForage(planting, cropIdForage, varietyIdAlfalafaGrass, ctcAlfalfa, false, 200.0, "E1", seedingDate); //Alfalfa Grass not insured

		// Planting 2
		planting = createPlanting(field1, 2, cropYear1, false);
		createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, true, 100.0, null, null); //Silage Corn - insured

		// Planting 3
		planting = createPlanting(field1, 3, cropYear1, false);
		createInventorySeededForage(planting, null, null, null, true, 100.0, null, null); //Silage Corn - insured
		
		//Field 2 ****		
		AnnualFieldRsrc field2 = getField(fieldId2, invContract.getFields());
		
		// Remove default planting.
		field2.getPlantings().remove(0);

		// Planting 1
		planting = createPlanting(field2, 1, cropYear1, true);
		createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, true, 100.0, null, null); //Silage Corn - insured
		
		// Planting 2
		planting = createPlanting(field2, 2, cropYear1, false);
		createInventorySeededForage(planting, cropIdForage, varietyIdAlfalafaGrass, ctcAlfalfa, true, 200.0, null, null); //Alfalfa Grass - insured
		
		//Field 3 ****		
		AnnualFieldRsrc field3 = getField(fieldId3, invContract.getFields());
		
		// Remove default planting.
		field3.getPlantings().remove(0);

		// Planting 1
		planting = createPlanting(field3, 1, cropYear1, true);
		createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, true, 100.0, null, null); //Silage Corn - insured
		
		// Planting 2
		planting = createPlanting(field3, 2, cropYear1, true);
		createInventorySeededForage(planting, cropIdForage, varietyIdAlfalafaGrass, ctcAlfalfa, true, 200.0, null, null); //Alfalfa Grass - insured
		
		// Planting 3
		planting = createPlanting(field3, 3, cropYear1, false);
		createInventorySeededForage(planting, cropIdForage, varietyIdGrass, ctcGrass, true, 100.0, null, null); //Grass - insured
		
		// add comments
		UnderwritingComment underwritingComment = new UnderwritingComment();
		List<UnderwritingComment> uwComments = new ArrayList<UnderwritingComment>();

		underwritingComment = new UnderwritingComment();
		underwritingComment.setAnnualFieldDetailId(null);
		underwritingComment.setUnderwritingComment("Comment1 for field " + field1.getFieldId());
		underwritingComment.setUnderwritingCommentGuid(null);
		underwritingComment.setUnderwritingCommentTypeCode("INV");
		underwritingComment.setUnderwritingCommentTypeDesc("Inventory");		

		uwComments.add(underwritingComment);
		
		field1.setUwComments(uwComments);

		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertEquals(3, fetchedInvContract.getFields().size());
		
		AnnualFieldRsrc fetchedField1 = getField(fieldId1, fetchedInvContract.getFields());
		AnnualFieldRsrc fetchedField2 = getField(fieldId2, fetchedInvContract.getFields());
		AnnualFieldRsrc fetchedField3 = getField(fieldId3, fetchedInvContract.getFields());
		
		Assert.assertNotNull(fetchedField1.getPlantings());
		Assert.assertEquals(3, fetchedField1.getPlantings().size());
		Assert.assertNotNull(fetchedField2.getPlantings());
		Assert.assertEquals(2, fetchedField2.getPlantings().size());
		Assert.assertNotNull(fetchedField3.getPlantings());
		Assert.assertEquals(3, fetchedField3.getPlantings().size());
	}
	
	private AnnualFieldRsrc getField(Integer fieldId, List<AnnualFieldRsrc> contractFields) {
		
		List<AnnualFieldRsrc> fields = contractFields.stream().filter(x -> x.getFieldId().equals(fieldId)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, fields.size());
		
		return fields.get(0);
	}
	
	private DopYieldFieldForageCut getCut(Integer cutNumber, List<DopYieldFieldForageCut> cuts) {
		
		List<DopYieldFieldForageCut> foundCuts = cuts.stream().filter(x -> x.getCutNumber().equals(cutNumber)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, foundCuts.size());
		
		return foundCuts.get(0);
	}
	
	private DopYieldFieldForageCut createDopFieldForageCut(
			String inventoryFieldGuid,
			Integer cutNumber,
			Integer totalBalesLoads, 
			Double weight,
			Double moisturePercent, 
			Boolean deletedByUserInd 
		) {
		
		DopYieldFieldForageCut model = new DopYieldFieldForageCut();

		model.setInventoryFieldGuid(inventoryFieldGuid);
		model.setCutNumber(cutNumber);
		model.setTotalBalesLoads(totalBalesLoads);
		model.setWeight(weight);
		model.setWeightDefaultUnit(null);
		model.setMoisturePercent(moisturePercent);
		model.setDeletedByUserInd(deletedByUserInd);

		return model;
	}
	
	private DopYieldFieldForage getDopYieldFieldForage(String commodityTypeCode, List<DopYieldFieldForage> dyffList) {
		
		List<DopYieldFieldForage> dyffs = new ArrayList<DopYieldFieldForage>();
		
		if(commodityTypeCode == null) {
			dyffs = dyffList.stream().filter(x -> x.getCommodityTypeCode() == null) 
					.collect(Collectors.toList());
		} else {
			dyffs = dyffList.stream().filter(x -> x.getCommodityTypeCode() != null
					&& x.getCommodityTypeCode().equalsIgnoreCase(commodityTypeCode)) 
					.collect(Collectors.toList());
		}
		
		Assert.assertEquals(1, dyffs.size());
		
		return dyffs.get(0);
	}
	
	private DopYieldContractCommodityForage getDopYieldContractCommodityForage(String commodityTypeCode, List<DopYieldContractCommodityForage> dyccfList) {
		
		List<DopYieldContractCommodityForage> dyccfs = dyccfList.stream().filter(x -> x.getCommodityTypeCode().equalsIgnoreCase(commodityTypeCode)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, dyccfs.size());
		
		return dyccfs.get(0);
	}

	private DopYieldFieldRollupForage getDopYieldFieldRollupForage(String commodityTypeCode, List<DopYieldFieldRollupForage> dyccfList) {
		
		List<DopYieldFieldRollupForage> dyrfs = dyccfList.stream().filter(x -> x.getCommodityTypeCode().equalsIgnoreCase(commodityTypeCode)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, dyrfs.size());
		
		return dyrfs.get(0);
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

	private InventoryField createPlanting(AnnualFieldRsrc field, Integer plantingNumber, Integer cropYear, Boolean isHiddenOnPrintoutInd) {
		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(5);
		planting.setInventoryFieldGuid(null);
		planting.setLastYearCropCommodityId(null);
		planting.setLastYearCropCommodityName(null);
		planting.setLastYearCropVarietyId(null);
		planting.setLastYearCropVarietyName(null);
		planting.setIsHiddenOnPrintoutInd(isHiddenOnPrintoutInd);
		planting.setPlantingNumber(plantingNumber);
		planting.setUnderseededAcres(null);
		planting.setUnderseededCropVarietyId(null);
		planting.setUnderseededCropVarietyName(null);
		
		field.getPlantings().add(planting);

		return planting;
	}

	private InventorySeededForage createInventorySeededForage(
			InventoryField planting, 
            Integer cropCommodityId,
            Integer cropVarietyId,
            String commodityTypeCode,
			Boolean isQuantityInsurableInd,
			Double fieldAcres,
			String plantInsurabilityTypeCode,
			Date seedingDate) {
		
		InventorySeededForage isf = new InventorySeededForage();


		isf.setCommodityTypeCode(commodityTypeCode);
		isf.setCropCommodityId(cropCommodityId);
		isf.setCropVarietyId(cropVarietyId);
		isf.setCropVarietyName(null);
		isf.setFieldAcres(fieldAcres);
		isf.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		isf.setIsAwpEligibleInd(false);
		isf.setIsIrrigatedInd(false);
		isf.setIsQuantityInsurableInd(isQuantityInsurableInd);
		isf.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		isf.setSeedingYear(planting.getCropYear() - 3);
		isf.setSeedingDate(seedingDate);
		
		planting.getInventorySeededForages().add(isf);

		return isf;
	}	
		
	private void createGrower() throws ValidationException, CirrasUnderwritingServiceException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		GrowerRsrc resource = new GrowerRsrc();
		
		resource.setGrowerId(growerId1);
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
	
	private void createPolicy() throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(policyId1);
		resource.setGrowerId(growerId1);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setPolicyStatusCode("ACTIVE");
		resource.setOfficeId(1);
		resource.setPolicyNumber(policyNumber1);
		resource.setContractNumber(contractNumber1);
		resource.setContractId(contractId1);
		resource.setCropYear(cropYear1);
		
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.PolicyCreated);

		service.synchronizePolicy(resource);
	}
	
	private void createGrowerContractYear() throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(gcyId1);
		resource.setContractId(contractId1);
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
	
	private void createAnnualFieldDetail(Integer annualFieldDetailId, Integer fieldId) throws CirrasUnderwritingServiceException, ValidationException {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setLegalLandId(legalLandId1);
		resource.setFieldId(fieldId);
		resource.setCropYear(cropYear1);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		service.synchronizeAnnualFieldDetail(resource);
	}

	private void createContractedFieldDetail(Integer contractedFieldDetailId, Integer annualFieldDetailId, Integer displayOrder) throws CirrasUnderwritingServiceException, ValidationException {
		
		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId);
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setGrowerContractYearId(gcyId1);
		resource.setDisplayOrder(displayOrder);
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
