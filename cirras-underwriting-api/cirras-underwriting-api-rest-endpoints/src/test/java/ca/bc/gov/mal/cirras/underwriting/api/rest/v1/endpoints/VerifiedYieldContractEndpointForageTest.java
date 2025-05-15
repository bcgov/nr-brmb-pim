package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ProductRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.VerifiedYieldContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiableVariety;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldSummary;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.CommodityCoverageCode;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class VerifiedYieldContractEndpointForageTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(VerifiedYieldContractEndpointForageTest.class);


	private static final String[] SCOPES = {
			Scopes.GET_TOP_LEVEL, 
			Scopes.SEARCH_UWCONTRACTS,
			Scopes.SEARCH_ANNUAL_FIELDS,
			Scopes.CREATE_INVENTORY_CONTRACT,
			Scopes.DELETE_INVENTORY_CONTRACT,
			Scopes.GET_INVENTORY_CONTRACT,
			Scopes.UPDATE_INVENTORY_CONTRACT,
			Scopes.CREATE_DOP_YIELD_CONTRACT,
			Scopes.DELETE_DOP_YIELD_CONTRACT,
			Scopes.GET_DOP_YIELD_CONTRACT,
			Scopes.UPDATE_DOP_YIELD_CONTRACT,
			Scopes.CREATE_VERIFIED_YIELD_CONTRACT,
			Scopes.CREATE_SYNC_UNDERWRITING,
			Scopes.UPDATE_SYNC_UNDERWRITING,
			Scopes.DELETE_SYNC_UNDERWRITING,
			Scopes.GET_GROWER,
			Scopes.GET_POLICY,
			Scopes.GET_LAND,
			Scopes.GET_LEGAL_LAND,
			Scopes.GET_VERIFIED_YIELD_CONTRACT,
			Scopes.CREATE_VERIFIED_YIELD_CONTRACT,
			Scopes.UPDATE_VERIFIED_YIELD_CONTRACT,
			Scopes.DELETE_VERIFIED_YIELD_CONTRACT
		};
	
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	private Integer contractId = 90000001;
	private Integer policyId1 = 90000002;
	private String policyNumber1 = "998877-22";
	private Integer cropYear1 = 2022;
	private String contractNumber = "998877";
	private Integer growerId = 90000003;
	private Integer growerContractYearId = 999999999;
	
	private Integer legalLandId = 90000015;
	private Integer fieldId = 90000016;
	private Integer annualFieldDetailId1 = 90000017;
	private Integer contractedFieldDetailId1 = 90000018;
	
	private Integer productId1 = 99999999;
	private Integer productId2 = 88889999;
	private Integer productId3 = 77777777;
	private Integer productId4 = 66666666;
	
	private Integer insurancePlanId = 5;


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

		service.deleteProduct(topLevelEndpoints, productId1.toString());
		service.deleteProduct(topLevelEndpoints, productId2.toString());
		service.deleteProduct(topLevelEndpoints, productId3.toString());
		service.deleteProduct(topLevelEndpoints, productId4.toString());
		
		deleteVerifiedYieldContract(policyNumber1);
		
		deleteDopYieldContract(policyNumber1);
		deleteInventoryContract(policyNumber1);
				
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId.toString());

		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());

		service.deleteField(topLevelEndpoints, fieldId.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId.toString());

		service.deletePolicy(topLevelEndpoints, policyId1.toString());
		service.deleteGrower(topLevelEndpoints, growerId.toString());	
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
	
	@Test
	public void testInsertUpdateDeleteForageVerifiedYield() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testInsertUpdateDeleteForageVerifiedYield");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		createContractAndInventory(false);

		//Create DOP Contract Commodity Records
		createDopYieldContract(policyNumber1, insurancePlanId, false);

		//Forage - Product
		createUpdateProduct(policyId1, productId1, cropIdForage, 20, foragePY, forageProductionGuarantee, forageIV100, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_FORAGE);
		//Silage Corn Product
		createUpdateProduct(policyId1, productId3, cropIdSilageCorn, 50, silageCornPY, silageCornProductionGuarantee, silageCornIV100, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_FORAGE);
		
		Integer pageNumber = 1;
		Integer pageRowCount = 20;

		// Rollover and create VerifiedYieldContract.
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
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
		
		// Check VerifiedYieldContract
		VerifiedYieldContractRsrc newContract = service.rolloverVerifiedYieldContract(referrer);
		Assert.assertNotNull(newContract);

		Assert.assertEquals(contractId, newContract.getContractId());
		Assert.assertEquals(cropYear1, newContract.getCropYear());
		Assert.assertEquals(referrer.getDeclaredYieldContractGuid(), newContract.getDeclaredYieldContractGuid());
		Assert.assertEquals("TON", newContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(growerContractYearId, newContract.getGrowerContractYearId());
		Assert.assertEquals(insurancePlanId, newContract.getInsurancePlanId());
		Assert.assertNull(newContract.getVerifiedYieldContractGuid());
		Assert.assertNull(newContract.getVerifiedYieldUpdateUser());
		Assert.assertNull(newContract.getVerifiedYieldUpdateTimestamp());

		// Check VerifiedYieldContractCommodities
		Assert.assertEquals(5, newContract.getVerifiedYieldContractCommodities().size());

		// Check Rollover
		//Alfalfa
		checkVerifiedContractCommodityRollover(newContract.getVerifiedYieldContractCommodities(), cropIdForage, ctcAlfalfa); 
		//Pasture
		checkVerifiedContractCommodityRollover(newContract.getVerifiedYieldContractCommodities(), cropIdForage, ctcPasture); 
		//Silage Corn
		checkVerifiedContractCommodityRollover(newContract.getVerifiedYieldContractCommodities(), cropIdSilageCorn, ctcSilageCorn);
		
		//Check rolled up rows
		//Forage
		checkVerifiedContractCommodityRolloverRolledUpRows(newContract.getVerifiedYieldContractCommodities(), cropIdForage, forageProductionGuarantee);
		//Silage Corn
		checkVerifiedContractCommodityRolloverRolledUpRows(newContract.getVerifiedYieldContractCommodities(), cropIdSilageCorn, silageCornProductionGuarantee);
		
		//Check order: rollup rows first before its commodity type rows
		String[] commodities = {
				cropIdForage.toString(), 
				cropIdForage.toString() + "_" + ctcAlfalfa, 
				cropIdForage.toString() + "_" + ctcPasture, 
				cropIdSilageCorn.toString(), 
				cropIdSilageCorn.toString() + "_" + ctcSilageCorn 
				};
		
		checkCommoditiesSortOrder(commodities, newContract.getVerifiedYieldContractCommodities());
		
		//Check verifiable Varieties and field
		Assert.assertEquals(1, newContract.getFields().size());
		AnnualFieldRsrc field = newContract.getFields().get(0);
		Assert.assertEquals(fieldId, field.getFieldId());
		Assert.assertEquals(3, field.getVerifiableVarieties().size());
		Map<Integer, String> expectedVarieties = new HashMap<Integer, String>();
		expectedVarieties.put(varietyIdAlfalafaGrass, varietyNameAlfalafaGrass);
		expectedVarieties.put(varietyIdCrownNative, varietyNameCrownNative);
		expectedVarieties.put(varietyIdSilageCorn, varietyNameSilageCorn);
		HashSet<Integer> actualVarieties = new HashSet<Integer>(); 

		for(VerifiableVariety vv : field.getVerifiableVarieties()) {
			//Check varietyId
			Assert.assertTrue(expectedVarieties.containsKey(vv.getCropVarietyId()));
			//Check variety name
			Assert.assertEquals(expectedVarieties.get(vv.getCropVarietyId()), vv.getCropVarietyName());

			actualVarieties.add(vv.getCropVarietyId()); //Add only adds a varietyId if it doesn't exist yet 
			
		}
		
		//Add variety id to make sure all expected varieties were returned
		Assert.assertEquals(expectedVarieties.size(), actualVarieties.size());

		
		//Create verified contract ******************************************************************************
		//Add override values for alfalfa
		VerifiedYieldContractCommodity alfalfaCommodity = getVerifiedYieldContractCommodity(cropIdForage, ctcAlfalfa, newContract.getVerifiedYieldContractCommodities());
		Assert.assertNotNull(alfalfaCommodity);
		alfalfaCommodity.setHarvestedAcresOverride(alfalfaHarvestedAcresOverride);
		
		//Update rolled up row
		VerifiedYieldContractCommodity forageCommodity = getVerifiedYieldContractCommodity(cropIdForage, null, newContract.getVerifiedYieldContractCommodities());
		Assert.assertNotNull(forageCommodity);
		Double rolledUpHarvestedAcresOverride = forageCommodity.getHarvestedAcres() - alfalfaCommodity.getHarvestedAcres() + alfalfaHarvestedAcresOverride;
		forageCommodity.setHarvestedAcresOverride(rolledUpHarvestedAcresOverride);

		//Add override yield for silage corn
		VerifiedYieldContractCommodity silageCornCommodity = getVerifiedYieldContractCommodity(cropIdSilageCorn, ctcSilageCorn, newContract.getVerifiedYieldContractCommodities());
		Assert.assertNotNull(silageCornCommodity);
		silageCornCommodity.setHarvestedYieldOverride(silageCornHarvestedYieldOverride);
		
		//Update rolled up row
		VerifiedYieldContractCommodity silageCornRollupCommodity = getVerifiedYieldContractCommodity(cropIdSilageCorn, null, newContract.getVerifiedYieldContractCommodities());
		Assert.assertNotNull(silageCornRollupCommodity);
		Double rolledUpHarvestedYieldOverride = silageCornRollupCommodity.getHarvestedYield() - silageCornCommodity.getHarvestedYield() + silageCornHarvestedYieldOverride;
		silageCornRollupCommodity.setHarvestedYieldOverride(rolledUpHarvestedYieldOverride);
		
		List<VerifiedYieldContractCommodity> expectedCommodities = newContract.getVerifiedYieldContractCommodities();
		
		VerifiedYieldContractRsrc createdContract = service.createVerifiedYieldContract(topLevelEndpoints, newContract);
		Assert.assertNotNull(createdContract);

		Assert.assertEquals(newContract.getContractId(), createdContract.getContractId());
		Assert.assertEquals(newContract.getCropYear(), createdContract.getCropYear());
		Assert.assertEquals(newContract.getDeclaredYieldContractGuid(), createdContract.getDeclaredYieldContractGuid());
		Assert.assertEquals(newContract.getDefaultYieldMeasUnitTypeCode(), createdContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(newContract.getGrowerContractYearId(), createdContract.getGrowerContractYearId());
		Assert.assertEquals(newContract.getInsurancePlanId(), createdContract.getInsurancePlanId());
		Assert.assertNotNull(createdContract.getVerifiedYieldContractGuid());
		Assert.assertNotNull(createdContract.getVerifiedYieldUpdateUser());
		Assert.assertNotNull(createdContract.getVerifiedYieldUpdateTimestamp());

		checkCommoditiesSortOrder(commodities, createdContract.getVerifiedYieldContractCommodities());

		// Check Contract Commodities
		//Alfalfa
		checkVerifiedContractCommodity(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), cropIdForage, ctcAlfalfa, null); 
		//Pasture
		checkVerifiedContractCommodity(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), cropIdForage, ctcPasture, null); 
		//Silage Corn
		checkVerifiedContractCommodity(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), cropIdSilageCorn, ctcSilageCorn, null);
		
		//Check rolled up rows
		//Forage
		checkVerifiedContractCommodity(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), cropIdForage, null, forageProductionGuarantee);
		//Silage Corn
		checkVerifiedContractCommodity(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), cropIdSilageCorn, null, silageCornProductionGuarantee);
		
		//Delete Forage Product - Expect warning
		service.deleteProduct(topLevelEndpoints, productId1.toString());
		//Update Silage Corn Product - Expect warning
		Double silageCornProductionGuaranteeNew = silageCornProductionGuarantee + 20;
		Double silageCornPYNew = silageCornPY + 10;
		Double silageCornIV100New = silageCornIV100 + 10;
		createUpdateProduct(policyId1, productId3, cropIdSilageCorn, 50, silageCornPYNew, silageCornProductionGuaranteeNew, silageCornIV100New, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_FORAGE);
		
		//Get contract again to prevent precondition fails
		createdContract = getVerifiedYieldContract(policyNumber1);
		Assert.assertNotNull(createdContract);
		
		//Update verified contract ******************************************************************************
		//Remove override values for alfalfa
		alfalfaCommodity = getVerifiedYieldContractCommodity(cropIdForage, ctcAlfalfa, createdContract.getVerifiedYieldContractCommodities());
		Assert.assertNotNull(alfalfaCommodity);
		alfalfaCommodity.setHarvestedAcresOverride(null);
		
		//Update rolled up row
		forageCommodity = getVerifiedYieldContractCommodity(cropIdForage, null, createdContract.getVerifiedYieldContractCommodities());
		Assert.assertNotNull(forageCommodity);
		forageCommodity.setHarvestedAcresOverride(null);
		
		//Add override yield for silage corn
		silageCornCommodity = getVerifiedYieldContractCommodity(cropIdSilageCorn, ctcSilageCorn, createdContract.getVerifiedYieldContractCommodities());
		Assert.assertNotNull(silageCornCommodity);
		silageCornCommodity.setHarvestedYieldOverride(null);
		
		//Update rolled up row
		silageCornRollupCommodity = getVerifiedYieldContractCommodity(cropIdSilageCorn, null, createdContract.getVerifiedYieldContractCommodities());
		Assert.assertNotNull(silageCornRollupCommodity);
		silageCornRollupCommodity.setHarvestedYieldOverride(null);

		//DON'T update product values
		createdContract.setUpdateProductValuesInd(false);
		
		expectedCommodities = createdContract.getVerifiedYieldContractCommodities();

		VerifiedYieldContractRsrc updatedContract = service.updateVerifiedYieldContract(createdContract);
		Assert.assertNotNull(updatedContract);
		
		Assert.assertEquals(createdContract.getContractId(), updatedContract.getContractId());
		Assert.assertEquals(createdContract.getCropYear(), updatedContract.getCropYear());
		Assert.assertEquals(createdContract.getDeclaredYieldContractGuid(), updatedContract.getDeclaredYieldContractGuid());
		Assert.assertEquals(createdContract.getDefaultYieldMeasUnitTypeCode(), updatedContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(createdContract.getGrowerContractYearId(), updatedContract.getGrowerContractYearId());
		Assert.assertEquals(createdContract.getInsurancePlanId(), updatedContract.getInsurancePlanId());
		Assert.assertEquals(createdContract.getVerifiedYieldContractGuid(), updatedContract.getVerifiedYieldContractGuid());
		Assert.assertEquals(createdContract.getVerifiedYieldUpdateUser(), updatedContract.getVerifiedYieldUpdateUser());
		Assert.assertNotNull(updatedContract.getVerifiedYieldUpdateTimestamp());
		
		checkCommoditiesSortOrder(commodities, updatedContract.getVerifiedYieldContractCommodities());

		//4 warnings expected (2 Production Guarantee, 2 PY, 2 100% IV)
		Assert.assertEquals(6, updatedContract.getProductWarningMessages().size());

		// Check Contract Commodities
		//Alfalfa
		checkVerifiedContractCommodity(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), cropIdForage, ctcAlfalfa, null); 
		//Pasture
		checkVerifiedContractCommodity(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), cropIdForage, ctcPasture, null); 
		//Silage Corn
		checkVerifiedContractCommodity(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), cropIdSilageCorn, ctcSilageCorn, null);
		
		//Check rolled up rows
		//Forage
		checkVerifiedContractCommodity(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), cropIdForage, null, forageProductionGuarantee);
		//Silage Corn
		checkVerifiedContractCommodity(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), cropIdSilageCorn, null, silageCornProductionGuarantee);
		
		//UPDATE product values
		updatedContract.setUpdateProductValuesInd(true);
		
		updatedContract = service.updateVerifiedYieldContract(updatedContract);
		Assert.assertNotNull(updatedContract);
		
		//0 warnings expected
		Assert.assertEquals(0, updatedContract.getProductWarningMessages().size());

		//Update Silage Corn Product Guarantee, PY and 100%IV to NULL - Expect warning
		createUpdateProduct(policyId1, productId3, cropIdSilageCorn, 50, null, null, null, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_FORAGE);

		//Get contract
		VerifiedYieldContractRsrc vyContract = getVerifiedYieldContract(policyNumber1);
		Assert.assertNotNull(vyContract);
		
		//3 warnings expected (1 Production Guarantee, 1 PY, 1 100% IV)
		Assert.assertEquals(3, vyContract.getProductWarningMessages().size());

		//UPDATE product values
		vyContract.setUpdateProductValuesInd(true);

		vyContract = service.updateVerifiedYieldContract(vyContract);
		Assert.assertNotNull(vyContract);

		//0 warnings expected
		Assert.assertEquals(0, vyContract.getProductWarningMessages().size());

		//Delete verified contract ******************************************************************************
		service.deleteVerifiedYieldContract(vyContract);
		
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

		referrer = searchResults.getCollection().get(0);
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
		
		logger.debug(">testInsertUpdateDeleteForageVerifiedYield");
	}

	private void checkCommoditiesSortOrder(String[] expectedCommodities, List<VerifiedYieldContractCommodity> actualCommodities) {
		Assert.assertEquals(expectedCommodities.length, actualCommodities.size());
		
		for(int i = 0; i < expectedCommodities.length; i++) {
			String commodity = expectedCommodities[i];
			VerifiedYieldContractCommodity vycc = actualCommodities.get(i);
			
			String commodityId = commodity;
			String commodityType = null;
			
			if(commodity.contains("_")) {
				String[] splitString = commodity.split("_");
				commodityId = splitString[0];
				commodityType = splitString[1];
			}
			
			Assert.assertEquals(commodityId, vycc.getCropCommodityId().toString());
			Assert.assertEquals(commodityType, vycc.getCommodityTypeCode());
		}
	}

	@Test
	public void testInsertUpdateDeleteForageVerifiedYieldAmendments() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testInsertUpdateDeleteForageVerifiedYieldAmendments");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		createContractAndInventory(false);

		//Create DOP Contract Commodity Records
		createDopYieldContract(policyNumber1, insurancePlanId, false);
		
		Integer pageNumber = 1;
		Integer pageRowCount = 20;

		// Rollover and create VerifiedYieldContract.
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
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
		
		// Check VerifiedYieldContract
		VerifiedYieldContractRsrc newContract = service.rolloverVerifiedYieldContract(referrer);
		Assert.assertNotNull(newContract);

		Assert.assertEquals(contractId, newContract.getContractId());
		Assert.assertEquals(cropYear1, newContract.getCropYear());
		Assert.assertEquals(referrer.getDeclaredYieldContractGuid(), newContract.getDeclaredYieldContractGuid());
		Assert.assertEquals("TON", newContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(growerContractYearId, newContract.getGrowerContractYearId());
		Assert.assertEquals(insurancePlanId, newContract.getInsurancePlanId());
		Assert.assertNull(newContract.getVerifiedYieldContractGuid());
		Assert.assertNull(newContract.getVerifiedYieldUpdateUser());
		Assert.assertNull(newContract.getVerifiedYieldUpdateTimestamp());


		// Check VerifiedYieldAmendments
		Assert.assertEquals(0, newContract.getVerifiedYieldAmendments().size());
		
		
		//Create verified contract ******************************************************************************

		VerifiedYieldAmendment vya = createVerifiedYieldAmendment("Appraisal", cropIdForage, cropNameForage, varietyIdAlfalafaGrass, varietyNameAlfalafaGrass, fieldId, "LOT 3", 12.34, 56.78, "rationale 1");
		newContract.getVerifiedYieldAmendments().add(vya);
		
		vya = createVerifiedYieldAmendment("Assessment", cropIdSilageCorn, cropNameSilageCorn, varietyIdSilageCorn, varietyNameSilageCorn, fieldId, "LOT 3", 11.22, 33.44, "rationale 2");
		newContract.getVerifiedYieldAmendments().add(vya);

		//Variety with no field
		vya = createVerifiedYieldAmendment("Assessment", cropIdForage, cropNameForage, varietyIdRedClover, varietyNameRedClover, null, null, 22.11, 44.33, "rationale 3");
		newContract.getVerifiedYieldAmendments().add(vya);		
		
		VerifiedYieldContractRsrc createdContract = service.createVerifiedYieldContract(topLevelEndpoints, newContract);
		Assert.assertNotNull(createdContract);

		Assert.assertEquals(newContract.getContractId(), createdContract.getContractId());
		Assert.assertEquals(newContract.getCropYear(), createdContract.getCropYear());
		Assert.assertEquals(newContract.getDeclaredYieldContractGuid(), createdContract.getDeclaredYieldContractGuid());
		Assert.assertEquals(newContract.getDefaultYieldMeasUnitTypeCode(), createdContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(newContract.getGrowerContractYearId(), createdContract.getGrowerContractYearId());
		Assert.assertEquals(newContract.getInsurancePlanId(), createdContract.getInsurancePlanId());
		Assert.assertNotNull(createdContract.getVerifiedYieldContractGuid());
		Assert.assertNotNull(createdContract.getVerifiedYieldUpdateUser());
		Assert.assertNotNull(createdContract.getVerifiedYieldUpdateTimestamp());

		checkVerifiedYieldAmendments(newContract.getVerifiedYieldAmendments(), createdContract.getVerifiedYieldAmendments());
		
		//Update verified contract ******************************************************************************
		List<VerifiedYieldAmendment> expectedAmendments = new ArrayList<VerifiedYieldAmendment>();
		
		vya = getVerifiedYieldAmendment(varietyIdAlfalafaGrass, createdContract.getVerifiedYieldAmendments());
		vya.setAcres(55.66);
		vya.setCropVarietyId(222);
		vya.setCropVarietyName("FV GRASS");
		vya.setRationale("rationale A");
		vya.setVerifiedYieldAmendmentCode("Assessment");
		vya.setYieldPerAcre(77.88);

		expectedAmendments.add(vya);
		
		vya = getVerifiedYieldAmendment(varietyIdSilageCorn, createdContract.getVerifiedYieldAmendments());
		vya.setAcres(98.76);
		vya.setFieldId(null);
		vya.setFieldLabel(null);
		vya.setRationale("rationale B");
		vya.setVerifiedYieldAmendmentCode("Appraisal");
		vya.setYieldPerAcre(54.32);
		
		expectedAmendments.add(vya);

		vya = getVerifiedYieldAmendment(varietyIdRedClover, createdContract.getVerifiedYieldAmendments());
		vya.setDeletedByUserInd(true);
		
		VerifiedYieldContractRsrc updatedContract = service.updateVerifiedYieldContract(createdContract);
		Assert.assertNotNull(updatedContract);
		
		Assert.assertEquals(createdContract.getContractId(), updatedContract.getContractId());
		Assert.assertEquals(createdContract.getCropYear(), updatedContract.getCropYear());
		Assert.assertEquals(createdContract.getDeclaredYieldContractGuid(), updatedContract.getDeclaredYieldContractGuid());
		Assert.assertEquals(createdContract.getDefaultYieldMeasUnitTypeCode(), updatedContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(createdContract.getGrowerContractYearId(), updatedContract.getGrowerContractYearId());
		Assert.assertEquals(createdContract.getInsurancePlanId(), updatedContract.getInsurancePlanId());
		Assert.assertEquals(createdContract.getVerifiedYieldContractGuid(), updatedContract.getVerifiedYieldContractGuid());
		Assert.assertEquals(createdContract.getVerifiedYieldUpdateUser(), updatedContract.getVerifiedYieldUpdateUser());
		Assert.assertNotNull(updatedContract.getVerifiedYieldUpdateTimestamp());

		checkVerifiedYieldAmendments(expectedAmendments, updatedContract.getVerifiedYieldAmendments());

		//Delete verified contract ******************************************************************************
		service.deleteVerifiedYieldContract(updatedContract);
		
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

		referrer = searchResults.getCollection().get(0);
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
		
		logger.debug(">testInsertUpdateDeleteForageVerifiedYieldAmendments");
	}
	
	private String appraisal = InventoryServiceEnums.AmendmentTypeCode.Appraisal.toString();
	private String assessment = InventoryServiceEnums.AmendmentTypeCode.Assessment.toString();
	private Double forageAppraisalYield1 = 13.0;
	private Double forageAppraisalAcres1 = 5.0;
	private Double forageAppraisalYield2 = 11.0;
	private Double forageAppraisalAcres2 = 2.0;
	private Double silageCornAppraisalYield1 = 12.0;
	private Double silageCornAppraisalAcres1 = 5.0;
	private Double forageAssessmentYield1 = 4.0;
	private Double forageAssessmentAcres1 = 3.0;
	private Double silageCornAssessmentYield1 = 15.0;
	private Double silageCornAssessmentAcres1 = 3.0;
	private Double silageCornAssessmentYield2 = 10.0;
	private Double silageCornAssessmentAcres2 = 2.0;
	
	@Test
	public void testForageVerifiedYieldSummary() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testForageVerifiedYieldSummary");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		createContractAndInventory(true);

		//Create DOP Contract Commodity Records
		createDopYieldContract(policyNumber1, insurancePlanId, true);

		//Forage - Product
		createUpdateProduct(policyId1, productId1, cropIdForage, 20, foragePY, forageProductionGuarantee, forageIV100, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_FORAGE);
		//Silage Corn Product
		createUpdateProduct(policyId1, productId3, cropIdSilageCorn, 50, silageCornPY, silageCornProductionGuarantee, silageCornIV100, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_FORAGE);

		Integer pageNumber = 1;
		Integer pageRowCount = 20;

		// Rollover and create VerifiedYieldContract.
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
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
		Assert.assertNull(referrer.getVerifiedYieldContractGuid());

		// Check VerifiedYieldContract
		VerifiedYieldContractRsrc newContract = service.rolloverVerifiedYieldContract(referrer);
		Assert.assertNotNull(newContract);

		Assert.assertEquals(contractId, newContract.getContractId());
		Assert.assertEquals(cropYear1, newContract.getCropYear());
		Assert.assertEquals(referrer.getDeclaredYieldContractGuid(), newContract.getDeclaredYieldContractGuid());
		Assert.assertEquals("TON", newContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(growerContractYearId, newContract.getGrowerContractYearId());
		Assert.assertEquals(insurancePlanId, newContract.getInsurancePlanId());
		Assert.assertNull(newContract.getVerifiedYieldContractGuid());
		Assert.assertNull(newContract.getVerifiedYieldUpdateUser());
		Assert.assertNull(newContract.getVerifiedYieldUpdateTimestamp());

		// Check VerifiedYieldContractCommodities
		Assert.assertEquals(3, newContract.getVerifiedYieldContractCommodities().size());
		
		// Check VerifiedYieldAmendments
		Assert.assertEquals(0, newContract.getVerifiedYieldAmendments().size());
		
		//Create verified contract ******************************************************************************

		//Forage - Appraisal
		VerifiedYieldAmendment vya = createVerifiedYieldAmendment(appraisal, cropIdForage, cropNameForage, varietyIdAlfalafaGrass, varietyNameAlfalafaGrass, fieldId, "LOT 3", forageAppraisalAcres1, forageAppraisalYield1, "rationale 1");
		newContract.getVerifiedYieldAmendments().add(vya);

		//Forage - Appraisal
		vya = createVerifiedYieldAmendment(appraisal, cropIdForage, cropNameForage, varietyIdRedClover, varietyNameRedClover, null, null, forageAppraisalAcres2, forageAppraisalYield2, "rationale 2");
		newContract.getVerifiedYieldAmendments().add(vya);		

		//Forage - Assessment
		vya = createVerifiedYieldAmendment(assessment, cropIdForage, cropNameForage, varietyIdAlfalafaGrass, varietyNameAlfalafaGrass, fieldId, "LOT 3", forageAssessmentAcres1, forageAssessmentYield1, "rationale 3");
		newContract.getVerifiedYieldAmendments().add(vya);

		//Silage Corn - Appraisal
		vya = createVerifiedYieldAmendment(appraisal, cropIdSilageCorn, cropNameSilageCorn, varietyIdSilageCorn, varietyNameSilageCorn, fieldId, "LOT 3", silageCornAppraisalAcres1, silageCornAppraisalYield1, "rationale 4");
		newContract.getVerifiedYieldAmendments().add(vya);

		//Silage Corn - Assessment
		vya = createVerifiedYieldAmendment(assessment, cropIdSilageCorn, cropNameSilageCorn, varietyIdSilageCorn, varietyNameSilageCorn, fieldId, "LOT 3", silageCornAssessmentAcres1, silageCornAssessmentYield1, "rationale 5");
		newContract.getVerifiedYieldAmendments().add(vya);

		//Silage Corn - Assessment
		vya = createVerifiedYieldAmendment(assessment, cropIdSilageCorn, cropNameSilageCorn, varietyIdSilageCorn, varietyNameSilageCorn, fieldId, "LOT 3", silageCornAssessmentAcres2, silageCornAssessmentYield2, "rationale 6");
		newContract.getVerifiedYieldAmendments().add(vya);

		VerifiedYieldContractRsrc createdContract = service.createVerifiedYieldContract(topLevelEndpoints, newContract);
		Assert.assertNotNull(createdContract);

		List<VerifiedYieldSummary> expectedVys = createExpectedVerifiedYieldSummaries(createdContract);

		
		Assert.assertEquals(newContract.getContractId(), createdContract.getContractId());
		Assert.assertEquals(newContract.getCropYear(), createdContract.getCropYear());
		Assert.assertEquals(newContract.getDeclaredYieldContractGuid(), createdContract.getDeclaredYieldContractGuid());
		Assert.assertEquals(newContract.getDefaultYieldMeasUnitTypeCode(), createdContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(newContract.getGrowerContractYearId(), createdContract.getGrowerContractYearId());
		Assert.assertEquals(newContract.getInsurancePlanId(), createdContract.getInsurancePlanId());
		
		//Expect 2 verified yield summaries
		Assert.assertEquals(2, createdContract.getVerifiedYieldSummaries().size());
		
		//Check Verified Yield Summary
		checkVerifiedYieldSummaries(expectedVys, createdContract.getVerifiedYieldSummaries(), createdContract.getVerifiedYieldContractGuid());

		//Add comments
		List<UnderwritingComment> comments = new ArrayList<UnderwritingComment>();
		//Forage - 2 comments
		VerifiedYieldSummary forageVys = getVerifiedYieldSummary(cropIdForage, createdContract.getVerifiedYieldSummaries());
		Assert.assertNotNull(forageVys);
		comments.add(createUwComment(forageVys.getVerifiedYieldSummaryGuid()));
		comments.add(createUwComment(forageVys.getVerifiedYieldSummaryGuid()));
		forageVys.setUwComments(comments);
		
		//Silage Corn - 1 comment
		VerifiedYieldSummary scVys = getVerifiedYieldSummary(cropIdSilageCorn, createdContract.getVerifiedYieldSummaries());
		Assert.assertNotNull(scVys);
		comments = new ArrayList<UnderwritingComment>();
		comments.add(createUwComment(scVys.getVerifiedYieldSummaryGuid()));
		scVys.setUwComments(comments);

		VerifiedYieldContractRsrc updatedContract = service.updateVerifiedYieldContract(createdContract);
		Assert.assertNotNull(updatedContract);
		
		//Expect 2 verified yield summaries
		Assert.assertEquals(2, updatedContract.getVerifiedYieldSummaries().size());

		//Check Comments
		forageVys = getVerifiedYieldSummary(cropIdForage, updatedContract.getVerifiedYieldSummaries());
		Assert.assertNotNull(forageVys);
		Assert.assertEquals(2, forageVys.getUwComments().size());
		//Remove one forage comment
		forageVys.getUwComments().get(0).setDeletedByUserInd(true);

		scVys = getVerifiedYieldSummary(cropIdSilageCorn, updatedContract.getVerifiedYieldSummaries());
		Assert.assertNotNull(scVys);
		Assert.assertEquals(1, scVys.getUwComments().size());
		
		//Remove Silage Corn Amendments - Expect summary to be deleted
		List<VerifiedYieldAmendment> silageCornAmendments = getVarietyAmendments(varietyIdSilageCorn, updatedContract.getVerifiedYieldAmendments());
		Assert.assertNotNull(silageCornAmendments);
		Assert.assertEquals(3, silageCornAmendments.size());
		for(VerifiedYieldAmendment scVya : silageCornAmendments) {
			scVya.setDeletedByUserInd(true);
		}
		
		//Remove Red Clover amendment - Expect updated summary
		VerifiedYieldAmendment redCloverAmendment = getVerifiedYieldAmendment(varietyIdRedClover, updatedContract.getVerifiedYieldAmendments());
		redCloverAmendment.setDeletedByUserInd(true);

		//Update expected summary values
		VerifiedYieldSummary expForageVys = getVerifiedYieldSummary(cropIdForage, expectedVys);
		Assert.assertNotNull(expForageVys);
		Double appraisedYield = (forageAppraisalYield1 * forageAppraisalAcres1);
		expForageVys.setAppraisedYield(appraisedYield);
		Double yieldToCount = expForageVys.getHarvestedYield() + expForageVys.getAppraisedYield();
		expForageVys.setYieldToCount(yieldToCount);
		Double yieldPercentPy = expForageVys.getYieldToCount()/ (expForageVys.getTotalInsuredAcres() * foragePY);
		expForageVys.setYieldPercentPy(yieldPercentPy);
		Double productionAcres = expForageVys.getProductionAcres() - forageAppraisalAcres2;
		expForageVys.setProductionAcres(productionAcres);
		
		updatedContract = service.updateVerifiedYieldContract(updatedContract);
		Assert.assertNotNull(updatedContract);
		
		//Expect 1 verified yield summaries - Oat should be deleted
		Assert.assertEquals(1, updatedContract.getVerifiedYieldSummaries().size());

		forageVys = getVerifiedYieldSummary(cropIdForage, updatedContract.getVerifiedYieldSummaries());
		Assert.assertNotNull(forageVys);
		//One comment should be deleted
		Assert.assertEquals(1, forageVys.getUwComments().size());

		checkVerifiedYieldSummary(expForageVys, forageVys, updatedContract.getVerifiedYieldContractGuid());				


		//Delete verified contract ******************************************************************************
		service.deleteVerifiedYieldContract(updatedContract);
		
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

		referrer = searchResults.getCollection().get(0);
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
		
		logger.debug(">testForageVerifiedYieldSummary");
	}	
	
	private List<VerifiedYieldSummary> createExpectedVerifiedYieldSummaries(VerifiedYieldContractRsrc vyContract) {
		
		List<VerifiedYieldSummary> expVys = new ArrayList<VerifiedYieldSummary>();
		
		VerifiedYieldSummary vys = new VerifiedYieldSummary();
		
		//Forage
		VerifiedYieldContractCommodity forageCommodity = getVerifiedYieldContractCommodity(cropIdForage, null, vyContract.getVerifiedYieldContractCommodities());
		Assert.assertNotNull(forageCommodity);
		
		vys.setCropCommodityId(cropIdForage);
		vys.setCropCommodityName(cropNameForage);
		vys.setIsPedigreeInd(false);
		vys.setHarvestedYield(forageCommodity.getHarvestedYield());
		vys.setHarvestedYieldPerAcre(forageCommodity.getYieldPerAcre());
		Double appraisedYield = (forageAppraisalYield1 * forageAppraisalAcres1) + (forageAppraisalYield2 * forageAppraisalAcres2);
		vys.setAppraisedYield(appraisedYield);
		Double assessedYield = (forageAssessmentYield1 * forageAssessmentAcres1);
		vys.setAssessedYield(assessedYield);
		Double yieldToCount = vys.getHarvestedYield() + vys.getAppraisedYield();
		vys.setYieldToCount(yieldToCount);
		Double yieldPercentPy = vys.getYieldToCount()/ (forageCommodity.getTotalInsuredAcres() * foragePY);
		vys.setYieldPercentPy(yieldPercentPy);
		vys.setProductionGuarantee(forageProductionGuarantee);
		vys.setProbableYield(foragePY);
		vys.setInsurableValueHundredPercent(forageIV100);
		vys.setTotalInsuredAcres(forageCommodity.getTotalInsuredAcres());
		Double effectiveHarvestedAcres = notNull(notNull(forageCommodity.getHarvestedAcresOverride(), forageCommodity.getHarvestedAcres()), 0.0);
		Double productionAcres = effectiveHarvestedAcres + forageAppraisalAcres1 + forageAppraisalAcres2;
		vys.setProductionAcres(productionAcres);
		
		expVys.add(vys);

		//Silage Corn - No commodity total record
		vys = new VerifiedYieldSummary();
		
		vys.setCropCommodityId(cropIdSilageCorn);
		vys.setCropCommodityName(cropNameSilageCorn);
		vys.setIsPedigreeInd(false);
		vys.setHarvestedYield(null);
		vys.setHarvestedYieldPerAcre(null);
		appraisedYield = silageCornAppraisalYield1 * silageCornAppraisalAcres1;
		vys.setAppraisedYield(appraisedYield);
		assessedYield = (silageCornAssessmentYield1 * silageCornAssessmentAcres1) + (silageCornAssessmentYield2 * silageCornAssessmentAcres2);
		vys.setAssessedYield(assessedYield);
		yieldToCount = vys.getAppraisedYield();
		vys.setYieldToCount(yieldToCount);
		yieldPercentPy = null;
		vys.setYieldPercentPy(yieldPercentPy);
		vys.setProductionGuarantee(silageCornProductionGuarantee);
		vys.setProbableYield(silageCornPY );
		vys.setInsurableValueHundredPercent(silageCornIV100 );
		vys.setTotalInsuredAcres(null);
		vys.setProductionAcres(silageCornAppraisalAcres1);
		
		expVys.add(vys);
		
		return expVys;
	}
	
	private void checkVerifiedContractCommodityRolloverRolledUpRows(
			List<VerifiedYieldContractCommodity> verifiedCommodities,
			Integer cropCommodityId,
			Double productionGuarantee) {
		
		DopYieldContractCommodityForage dopCommodity = getDopYieldContractCommodityForage(cropCommodityId, null, dopYieldContractCommodityList);
		VerifiedYieldContractCommodity verifiedCommodity = getVerifiedYieldContractCommodity(cropCommodityId, null, verifiedCommodities);

		//Yield per acre: 0 if yield and acres are null
		Double yieldPerAcre = null;
		Double effectiveAcres = notNull(verifiedCommodity.getHarvestedAcresOverride(), verifiedCommodity.getHarvestedAcres());
		Double effectiveYield = notNull(verifiedCommodity.getHarvestedYieldOverride(), verifiedCommodity.getHarvestedYield());

		if ( effectiveAcres != null && effectiveYield != null ) {
			if ( effectiveAcres == 0.0 ) {
				yieldPerAcre = 0.0;
			} else {
				yieldPerAcre = effectiveYield / effectiveAcres;
			}
		}
		
		Assert.assertEquals(dopCommodity.getCropCommodityId(), verifiedCommodity.getCropCommodityId());
		Assert.assertEquals(yieldPerAcre, verifiedCommodity.getYieldPerAcre(), 0.00005);
		Assert.assertEquals(productionGuarantee, verifiedCommodity.getProductionGuarantee());
		Assert.assertEquals(dopCommodity.getHarvestedAcres(), verifiedCommodity.getHarvestedAcres());
		Assert.assertEquals(dopCommodity.getTotalFieldAcres(), verifiedCommodity.getTotalInsuredAcres());
		Assert.assertTrue(verifiedCommodity.getIsRolledupInd()); //True for rolled up rows
		Assert.assertNull(verifiedCommodity.getCommodityTypeCode());
		Assert.assertNull(verifiedCommodity.getHarvestedAcresOverride());
		Assert.assertNull(verifiedCommodity.getHarvestedYieldOverride());
		Assert.assertNull(verifiedCommodity.getVerifiedYieldContractCommodityGuid());
		Assert.assertNull(verifiedCommodity.getVerifiedYieldContractGuid());
		Assert.assertFalse(verifiedCommodity.getIsPedigreeInd());
		Assert.assertNull(verifiedCommodity.getSoldYieldDefaultUnit());
		Assert.assertNull(verifiedCommodity.getStoredYieldDefaultUnit());
	}

	private void checkVerifiedContractCommodityRollover(
			List<VerifiedYieldContractCommodity> verifiedCommodities,
			Integer cropCommodityId,
			String commodityTypeCode) {
		
		DopYieldContractCommodityForage dopCommodity = getDopYieldContractCommodityForage(cropCommodityId, commodityTypeCode, dopYieldContractCommodityList);
		VerifiedYieldContractCommodity verifiedCommodity = getVerifiedYieldContractCommodity(cropCommodityId, commodityTypeCode, verifiedCommodities);

		//Yield per acre: 0 if yield and acres are null
		Double yieldPerAcre = null;
		Double effectiveAcres = notNull(verifiedCommodity.getHarvestedAcresOverride(), verifiedCommodity.getHarvestedAcres());
		Double effectiveYield = notNull(verifiedCommodity.getHarvestedYieldOverride(), verifiedCommodity.getHarvestedYield());

		if ( effectiveAcres != null && effectiveYield != null ) {
			if ( effectiveAcres == 0.0 ) {
				yieldPerAcre = 0.0;
			} else {
				yieldPerAcre = effectiveYield / effectiveAcres;
			}
		}
		
		Assert.assertEquals(dopCommodity.getCropCommodityId(), verifiedCommodity.getCropCommodityId());
		Assert.assertEquals(dopCommodity.getCommodityTypeCode(), verifiedCommodity.getCommodityTypeCode());
		Assert.assertEquals(dopCommodity.getHarvestedAcres(), verifiedCommodity.getHarvestedAcres());
		Assert.assertNull(verifiedCommodity.getHarvestedAcresOverride());
		Assert.assertNull(verifiedCommodity.getHarvestedYieldOverride());
		Assert.assertEquals(dopCommodity.getTotalFieldAcres(), verifiedCommodity.getTotalInsuredAcres());
		Assert.assertNull(verifiedCommodity.getVerifiedYieldContractCommodityGuid());
		Assert.assertNull(verifiedCommodity.getVerifiedYieldContractGuid());
		Assert.assertEquals(yieldPerAcre, verifiedCommodity.getYieldPerAcre(), 0.00005);
		Assert.assertFalse(verifiedCommodity.getIsRolledupInd());
		Assert.assertFalse(verifiedCommodity.getIsPedigreeInd());
		Assert.assertNull(verifiedCommodity.getProductionGuarantee());
		Assert.assertNull(verifiedCommodity.getSoldYieldDefaultUnit());
		Assert.assertNull(verifiedCommodity.getStoredYieldDefaultUnit());
	}
	
	private void checkVerifiedContractCommodity(
			List<VerifiedYieldContractCommodity> expectedCommodities,
			List<VerifiedYieldContractCommodity> actualCommodities,
			Integer cropCommodityId,
			String commodityTypeCode,
			Double productionGuarantee) {
		
		VerifiedYieldContractCommodity expectedCommodity = getVerifiedYieldContractCommodity(cropCommodityId, commodityTypeCode, expectedCommodities);
		VerifiedYieldContractCommodity actualCommodity = getVerifiedYieldContractCommodity(cropCommodityId, commodityTypeCode, actualCommodities);

		//Yield per acre: 0 if yield and acres are null
		Double yieldPerAcre = null;
		Double effectiveAcres = notNull(actualCommodity.getHarvestedAcresOverride(), actualCommodity.getHarvestedAcres());
		Double effectiveYield = notNull(actualCommodity.getHarvestedYieldOverride(), actualCommodity.getHarvestedYield());

		if ( effectiveAcres != null && effectiveYield != null ) {
			if ( effectiveAcres == 0.0 ) {
				yieldPerAcre = 0.0;
			} else {
				yieldPerAcre = effectiveYield / effectiveAcres;
			}
		}
		
		String displayName = expectedCommodity.getCommodityTypeDescription();
		Boolean isRolledUp = false;
		if(expectedCommodity.getCommodityTypeCode() == null) {
			displayName = expectedCommodity.getCropCommodityName();
			isRolledUp = true;
		}
		
		Assert.assertNotNull(actualCommodity.getVerifiedYieldContractCommodityGuid());
		Assert.assertNotNull(actualCommodity.getVerifiedYieldContractGuid());
		Assert.assertEquals(displayName, actualCommodity.getDisplayName());
		Assert.assertEquals(expectedCommodity.getCropCommodityId(), actualCommodity.getCropCommodityId());
		Assert.assertEquals(expectedCommodity.getCommodityTypeCode(), actualCommodity.getCommodityTypeCode());
		Assert.assertEquals(expectedCommodity.getHarvestedAcres(), actualCommodity.getHarvestedAcres());
		Assert.assertEquals(expectedCommodity.getHarvestedAcresOverride(), actualCommodity.getHarvestedAcresOverride());
		Assert.assertEquals(expectedCommodity.getHarvestedYield(), actualCommodity.getHarvestedYield());
		Assert.assertEquals(expectedCommodity.getHarvestedYieldOverride(), actualCommodity.getHarvestedYieldOverride());
		Assert.assertEquals(expectedCommodity.getTotalInsuredAcres(), actualCommodity.getTotalInsuredAcres());
		Assert.assertEquals(yieldPerAcre, actualCommodity.getYieldPerAcre(), 0.00005);
		Assert.assertEquals(isRolledUp, actualCommodity.getIsRolledupInd());
		Assert.assertEquals(productionGuarantee, actualCommodity.getProductionGuarantee());
		Assert.assertFalse(actualCommodity.getIsPedigreeInd());
		Assert.assertNull(actualCommodity.getSoldYieldDefaultUnit());
		Assert.assertNull(actualCommodity.getStoredYieldDefaultUnit());

	}

	private void checkVerifiedYieldAmendments(List<VerifiedYieldAmendment> expected, List<VerifiedYieldAmendment> actual) {
		if ( expected == null && actual != null || expected != null && actual == null ) {
			Assert.fail();
		} else if ( expected != null && actual != null ) {
			Assert.assertEquals(expected.size(), actual.size());
			
			for(VerifiedYieldAmendment expectedVya : expected) {
				
				VerifiedYieldAmendment actualVya = getVerifiedYieldAmendment(expectedVya.getCropVarietyId(), actual);

				checkVerifiedYieldAmendment(expectedVya, actualVya);				
			}
		}
		
	}
		
	private void checkVerifiedYieldAmendment(VerifiedYieldAmendment expected, VerifiedYieldAmendment actual) {
		
		if ( expected.getVerifiedYieldAmendmentGuid() != null ) {
			Assert.assertEquals(expected.getVerifiedYieldAmendmentGuid(), actual.getVerifiedYieldAmendmentGuid());			
		} else {
			Assert.assertNotNull(actual.getVerifiedYieldAmendmentGuid());
		}

		if ( expected.getVerifiedYieldContractGuid() != null ) {
			Assert.assertEquals(expected.getVerifiedYieldContractGuid(), actual.getVerifiedYieldContractGuid());			
		} else {
			Assert.assertNotNull(actual.getVerifiedYieldContractGuid());
		}
		
		Assert.assertEquals(expected.getVerifiedYieldAmendmentCode(), actual.getVerifiedYieldAmendmentCode());
		Assert.assertEquals(expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals(expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals(expected.getCropVarietyId(), actual.getCropVarietyId());
		Assert.assertEquals(expected.getCropVarietyName(), actual.getCropVarietyName());
		Assert.assertFalse(actual.getIsPedigreeInd());
		Assert.assertEquals(expected.getFieldId(), actual.getFieldId());
		Assert.assertEquals(expected.getYieldPerAcre(), actual.getYieldPerAcre());
		Assert.assertEquals(expected.getAcres(), actual.getAcres());
		Assert.assertEquals(expected.getRationale(), actual.getRationale());
		Assert.assertEquals(expected.getFieldLabel(), actual.getFieldLabel());
		Assert.assertEquals(expected.getDeletedByUserInd(), actual.getDeletedByUserInd());
	}
	
	private DopYieldContractCommodityForage getDopYieldContractCommodityForage(
			Integer cropCommodityId, 
			String commodityTypeCode, 
			List<DopYieldContractCommodityForage> dyccfList) {
		
		DopYieldContractCommodityForage dyccf = null;
		
		List<DopYieldContractCommodityForage> dyccfFiltered = dyccfList.stream()
				.filter(x -> x.getCropCommodityId() == cropCommodityId
						&& ((x.getCommodityTypeCode() != null && x.getCommodityTypeCode().equals(commodityTypeCode))
								|| (commodityTypeCode == null && x.getCommodityTypeCode() == null)) )
				
				.collect(Collectors.toList());
		
		if (dyccfFiltered != null) {
			dyccf = dyccfFiltered.get(0);
		}
		return dyccf;
	}
	
	private VerifiedYieldContractRsrc getVerifiedYieldContract(String policyNumber) throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException {

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
				return service.getVerifiedYieldContract(referrer);
			}
		}	
		
		return null;
	}
	
	private VerifiedYieldContractCommodity getVerifiedYieldContractCommodity(Integer cropCommodityId, String commodityTypeCode, List<VerifiedYieldContractCommodity> vyccList) {
		
		VerifiedYieldContractCommodity vycc = null;
		
		List<VerifiedYieldContractCommodity> vyccFiltered = vyccList.stream()
				.filter(x -> x.getCropCommodityId().equals(cropCommodityId) 
						&& ((x.getCommodityTypeCode() != null && x.getCommodityTypeCode().equals(commodityTypeCode))
								|| (commodityTypeCode == null && x.getCommodityTypeCode() == null)) )
				.collect(Collectors.toList());
		
		if (vyccFiltered != null) {
			vycc = vyccFiltered.get(0);
		}
		return vycc;
	}
	
	private VerifiedYieldAmendment getVerifiedYieldAmendment(Integer cropVarietyId, List<VerifiedYieldAmendment> vyaList) {
		
		VerifiedYieldAmendment vya = null;
		
		List<VerifiedYieldAmendment> vyaFiltered = getVarietyAmendments(cropVarietyId, vyaList);
		
		if (vyaFiltered != null) {
			vya = vyaFiltered.get(0);
		}
		return vya;
	}

	private List<VerifiedYieldAmendment> getVarietyAmendments(Integer cropVarietyId, List<VerifiedYieldAmendment> vyaList) {
		List<VerifiedYieldAmendment> vyaFiltered = vyaList.stream()
				.filter(x -> x.getCropVarietyId().equals(cropVarietyId) )
				.collect(Collectors.toList());
		return vyaFiltered;
	}
	
	private VerifiedYieldSummary getVerifiedYieldSummary(Integer cropCommodityId, List<VerifiedYieldSummary> vysList) {
		
		VerifiedYieldSummary vys = null;
		
		List<VerifiedYieldSummary> vysFiltered = vysList.stream()
				.filter(x -> x.getCropCommodityId().equals(cropCommodityId))
				.collect(Collectors.toList());
		
		if (vysFiltered != null) {
			vys = vysFiltered.get(0);
		}
		return vys;
	}
	
	private void checkVerifiedYieldSummaries(List<VerifiedYieldSummary> expected, List<VerifiedYieldSummary> actual, String verifiedYieldContractGuid) {
		if ( expected == null && actual != null || expected != null && actual == null ) {
			Assert.fail();
		} else if ( expected != null && actual != null ) {
			Assert.assertEquals(expected.size(), actual.size());

			for ( VerifiedYieldSummary expectedVys : expected ) {
				
				VerifiedYieldSummary actualVys = getVerifiedYieldSummary(expectedVys.getCropCommodityId(), actual);
				Assert.assertNotNull(actualVys);
				checkVerifiedYieldSummary(expectedVys, actualVys, verifiedYieldContractGuid);				
			}
		}
	}
	
	private void checkVerifiedYieldSummary(VerifiedYieldSummary expected, VerifiedYieldSummary actual, String verifiedYieldContractGuid) {
		Assert.assertNotNull("VerifiedYieldSummaryGuid", actual.getVerifiedYieldSummaryGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", verifiedYieldContractGuid, actual.getVerifiedYieldContractGuid());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("IsPedigreeInd", expected.getIsPedigreeInd(), actual.getIsPedigreeInd());
		Assert.assertEquals("ProductionAcres", expected.getProductionAcres(), actual.getProductionAcres());
		Assert.assertEquals("HarvestedYield", expected.getHarvestedYield(), actual.getHarvestedYield());
		Assert.assertEquals("HarvestedYieldPerAcre", expected.getHarvestedYieldPerAcre(), actual.getHarvestedYieldPerAcre());
		Assert.assertEquals("AppraisedYield", expected.getAppraisedYield(), actual.getAppraisedYield());
		Assert.assertEquals("AssessedYield", expected.getAssessedYield(), actual.getAssessedYield());
		Assert.assertEquals("YieldToCount", expected.getYieldToCount(), actual.getYieldToCount());
		if(expected.getYieldPercentPy() == null) {
			Assert.assertNull("YieldPercentPy Null", actual.getYieldPercentPy());
		} else {
			Assert.assertEquals("YieldPercentPy", expected.getYieldPercentPy(), actual.getYieldPercentPy(), 0.00005);
		}
		Assert.assertEquals("ProductionGuarantee", expected.getProductionGuarantee(), actual.getProductionGuarantee());
		Assert.assertEquals("ProbableYield", expected.getProbableYield(), actual.getProbableYield());
		Assert.assertEquals("InsurableValueHundredPercent", expected.getInsurableValueHundredPercent(), actual.getInsurableValueHundredPercent());
		Assert.assertEquals("TotalInsuredAcres", expected.getTotalInsuredAcres(), actual.getTotalInsuredAcres());
	}
	
	private void createLegalLand(
			String legalLocation, 
			String legalDescription, 
			Integer llId, 
			String primaryPropertyIdentifier, 
			Integer activeFromCropYear, 
			Integer activeToCropYear
	) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		
		String primaryReferenceTypeCode = "OTHER";
		String legalShortDescription = "Short Legal";
		String otherDescription = legalLocation;
		
		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(llId);
		resource.setPrimaryPropertyIdentifier(primaryPropertyIdentifier);
		resource.setPrimaryLandIdentifierTypeCode("PID");
		resource.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		resource.setLegalDescription(legalDescription);
		resource.setLegalShortDescription(legalShortDescription);
		resource.setOtherDescription(otherDescription);
		resource.setActiveFromCropYear(activeFromCropYear);
		resource.setActiveToCropYear(activeToCropYear);
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
	
	private void createPolicy() throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(policyId1);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setPolicyStatusCode("ACTIVE");
		resource.setOfficeId(1);
		resource.setPolicyNumber(policyNumber1);
		resource.setContractNumber(contractNumber);
		resource.setContractId(contractId);
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
		
		resource.setGrowerContractYearId(growerContractYearId);
		resource.setContractId(contractId);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setCropYear(cropYear1);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearCreated);

		service.synchronizeGrowerContractYear(resource);
		
	}
	
	private Double foragePY = 50.5;
	private Double forageProductionGuarantee = 222.5;
	private Double forageIV100 = 76.31;
	private Double silageCornPY = 20.1;
	private Double silageCornProductionGuarantee = 15.1;
	private Double silageCornIV100 = 56.21;
	
	private void createUpdateProduct(
			Integer policyId,
			Integer productId, 
			Integer cropCommodityId, 
			Integer deductibleLevel, 
			Double probableYield, 
			Double productionGuarantee, 
			Double insurableValueHundredPercent,
			Double coverageDollars, 
			String productStatusCode, 
			String commodityCoverageCode
			) throws CirrasUnderwritingServiceException, ValidationException {
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 because they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE Product
		ProductRsrc product = new ProductRsrc();
		
		product.setCommodityCoverageCode(commodityCoverageCode);
		product.setCropCommodityId(cropCommodityId);
		product.setDeductibleLevel(deductibleLevel);
		product.setInsuredByMeasType("ACRES");
		product.setPolicyId(policyId);
		product.setProbableYield(probableYield);
		product.setProductId(productId);
		product.setProductionGuarantee(productionGuarantee);
		product.setProductStatusCode(productStatusCode);
		product.setInsurableValueHundredPercent(insurableValueHundredPercent);
		product.setCoverageDollars(coverageDollars);

		
		product.setDataSyncTransDate(createTransactionDate);
		product.setTransactionType(PoliciesSyncEventTypes.ProductCreated);

		service.synchronizeProduct(product);

	}
	
	private static final String ctcAlfalfa = "Alfalfa";
	private static final String ctcSilageCorn = "Silage Corn";
	private static final String ctcPasture = "Pasture";
	private Integer cropIdForage = 65;
	private String cropNameForage = "FORAGE";
	private Integer cropIdSilageCorn = 71;
	private String cropNameSilageCorn = "SILAGE CORN";
	private Integer varietyIdAlfalafaGrass = 118;
	private Integer varietyIdSilageCorn = 1010863;
	private Integer varietyIdCrownNative = 1010986;
	private Integer varietyIdRedClover = 225;
	private String varietyNameAlfalafaGrass = "ALFALFA/GRASS";
	private String varietyNameCrownNative = "CROWN NATIVE";
	private String varietyNameSilageCorn = "SILAGE CORN - UNSPECIFIED";
	private String varietyNameRedClover = "RED CLOVER";
	private Integer alfalfaBales = 50;
	private double alfalfaWeight = 2.0;
	private double alfalfaQuantityHarvestedAcres = 320.0;
	private double alfalfaInsuredAcres1 = 100.0;
	private double alfalfaInsuredAcres2 = 150.0;
	private Integer pastureBales = 40;
	private double pastureWeight = 3.0;
	private double pastureQuantityHarvestedAcres = 150.0;
	private double pastureInsuredAcres = 200.0;
	private Integer silageCornBales = 35;
	private double silageCornWeight = 2.5;
	private double silageCornQuantityHarvestedAcres = 400.0;
	private double silageCornInsuredAcres1 = 250.0;
	private double silageCornInsuredAcres2 = 300.0;
	private double moisturePercent = 15.0;

	String annual = InventoryServiceEnums.PlantDurationType.ANNUAL.toString();
	String perennial = InventoryServiceEnums.PlantDurationType.PERENNIAL.toString();

	Date seedingDate = null;

	
	protected void createContractAndInventory(Boolean isVerifiedYieldSummaryTest) throws ValidationException, CirrasUnderwritingServiceException, DaoException {
		createGrower();
		createPolicy();
		createGrowerContractYear();

		createLegalLand("test legal 9999", null, legalLandId, "999-888-000", 1980, null);
		createField(fieldId, "LOT 3", 1980, null);
		createAnnualFieldDetail(annualFieldDetailId1, legalLandId, fieldId, cropYear1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId, 1);
		
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
		Assert.assertEquals(1, invContract.getFields().size());

		//Field 1 ****		
		AnnualFieldRsrc field1 = getField(fieldId, invContract.getFields());

		// Remove default planting.
		field1.getPlantings().remove(0);
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2024, Calendar.JUNE, 6);
		seedingDate = cal.getTime();
		
		Integer totalPlantings = 3;
		
		// Planting 1 - Forage - Alfalfa
		InventoryField planting = createPlanting(field1, 1, cropYear1);
		createInventorySeededForage(planting, cropIdForage, varietyIdAlfalafaGrass, ctcAlfalfa, alfalfaInsuredAcres1, seedingDate); //Alfalfa Grass

		// Planting 2 - Forage - Alfalfa
		planting = createPlanting(field1, 2, cropYear1);
		createInventorySeededForage(planting, cropIdForage, varietyIdAlfalafaGrass, ctcAlfalfa, alfalfaInsuredAcres2, seedingDate); //Alfalfa Grass

		// Planting 3 - Forage - Crown Native
		planting = createPlanting(field1, 3, cropYear1);
		createInventorySeededForage(planting, cropIdForage, varietyIdCrownNative, ctcPasture, pastureInsuredAcres, seedingDate); //Crown Native

		//NOT adding silage corn for the summary test 
		if(!isVerifiedYieldSummaryTest) {
			// Planting 4 - Silage Corn 1
			planting = createPlanting(field1, 4, cropYear1);
			createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, silageCornInsuredAcres1, null);
	
			// Planting 5 - Silage Corn 2
			planting = createPlanting(field1, 5, cropYear1);
			createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, silageCornInsuredAcres2, null);
		
			totalPlantings = 5;
		}
		
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertEquals(1, fetchedInvContract.getFields().size());
		
		AnnualFieldRsrc fetchedField1 = getField(fieldId, fetchedInvContract.getFields());
		
		Assert.assertNotNull(fetchedField1.getPlantings());
		Assert.assertEquals(totalPlantings.intValue(), fetchedField1.getPlantings().size());
	}
	
	private AnnualFieldRsrc getField(Integer fieldId, List<AnnualFieldRsrc> contractFields) {
		
		List<AnnualFieldRsrc> fields = contractFields.stream().filter(x -> x.getFieldId().equals(fieldId)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, fields.size());
		
		return fields.get(0);
	}
	
	private InventoryField createPlanting(AnnualFieldRsrc field, Integer plantingNumber, Integer cropYear) {
		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(5);
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
	
	private InventorySeededForage createInventorySeededForage(
			InventoryField planting, 
            Integer cropCommodityId,
            Integer cropVarietyId,
            String commodityTypeCode,
			Double fieldAcres,
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
		isf.setIsQuantityInsurableInd(true);
		isf.setPlantInsurabilityTypeCode(null);
		isf.setSeedingYear(planting.getCropYear() - 3);
		isf.setSeedingDate(seedingDate);
		
		planting.getInventorySeededForages().add(isf);

		return isf;
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

	
	private List<DopYieldContractCommodityForage> dopYieldContractCommodityList;
	private Double alfalfaHarvestedAcresOverride = 222.0;
	private Double silageCornHarvestedYieldOverride = 120.0;
	
	private void createDopYieldContract(
			String policyNumber, 
			Integer insurancePlanId,
			Boolean isVerifiedYieldSummaryTest) throws ValidationException, CirrasUnderwritingServiceException {

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
		resource.setDefaultYieldMeasUnitTypeCode("TON");
		resource.setEnteredYieldMeasUnitTypeCode("TON");
		resource.setGrainFromOtherSourceInd(false);
		
		//Set dop contract commodity totals
		enterDopCommodityTotals(resource, ctcAlfalfa, alfalfaQuantityHarvestedAcres, alfalfaBales, alfalfaWeight);
		enterDopCommodityTotals(resource, ctcPasture, pastureQuantityHarvestedAcres, pastureBales, pastureWeight);
		//NOT adding silage corn for the summary test 
		if(!isVerifiedYieldSummaryTest) {
			enterDopCommodityTotals(resource, ctcSilageCorn, silageCornQuantityHarvestedAcres, silageCornBales, silageCornWeight);
		}
		
		DopYieldContractRsrc createdContract = service.createDopYieldContract(topLevelEndpoints, resource);
		
		dopYieldContractCommodityList = createdContract.getDopYieldContractCommodityForageList();
		
		addExpectedRollupDopCommodities(createdContract, isVerifiedYieldSummaryTest);

	}

	private void addExpectedRollupDopCommodities(
			DopYieldContractRsrc resource,
			Boolean isVerifiedYieldSummaryTest) {
		//Add expected rollup dop items
		//FORAGE
		Double forageInsuredAcres = alfalfaInsuredAcres1 + alfalfaInsuredAcres2 + pastureInsuredAcres;
		Double forageHarvestedAcres = alfalfaQuantityHarvestedAcres + pastureQuantityHarvestedAcres;
		DopYieldContractCommodityForage dopAlfalfa = getDopYieldContractCommodityForage(cropIdForage, ctcAlfalfa, resource.getDopYieldContractCommodityForageList());
		DopYieldContractCommodityForage dopPasture = getDopYieldContractCommodityForage(cropIdForage, ctcPasture, resource.getDopYieldContractCommodityForageList());
		Double forageQuantityHarvestedTons = dopAlfalfa.getQuantityHarvestedTons() + dopPasture.getQuantityHarvestedTons();

		DopYieldContractCommodityForage forageDccf = createDopYieldContractCommodityForage(
			null,							//commodityTypeCode, 
			forageInsuredAcres, 			//totalFieldAcres, 
			forageHarvestedAcres,			//harvestedAcres, 
			null,							//totalBales, 
			null,							//weight, 
			null,							//weightDefaultUnit, 
			null,							//moisturePercent,
			forageQuantityHarvestedTons,	//quantityHarvestedTons, 
			null,							//yieldPerAcre,
			cropIdForage,					//cropCommodityId, 
			null							//plantDurationTypeCode
		);
		
		dopYieldContractCommodityList.add(forageDccf);
		
		if(!isVerifiedYieldSummaryTest) {
			
			//SILAGECORN
			Double silageCornInsuredAcres = silageCornInsuredAcres1 + silageCornInsuredAcres2;
			//Double silageCornHarvestedAcres = alfalfaQuantityHarestedAcres + pastureQuantityHarestedAcres;
			DopYieldContractCommodityForage dopSilageCorn = getDopYieldContractCommodityForage(cropIdSilageCorn, ctcSilageCorn, resource.getDopYieldContractCommodityForageList());
			Double silageCornQuantityHarvestedTons = dopSilageCorn.getQuantityHarvestedTons();
	
			DopYieldContractCommodityForage silageCornDccf = createDopYieldContractCommodityForage(
				null,								//commodityTypeCode, 
				silageCornInsuredAcres, 			//totalFieldAcres, 
				silageCornQuantityHarvestedAcres,	//harvestedAcres, 
				null,								//totalBales, 
				null,								//weight, 
				null,								//weightDefaultUnit, 
				null,								//moisturePercent,
				silageCornQuantityHarvestedTons,	//quantityHarvestedTons, 
				null,								//yieldPerAcre,
				cropIdSilageCorn,						//cropCommodityId, 
				null								//plantDurationTypeCode
			);
			
			dopYieldContractCommodityList.add(silageCornDccf);
		}
	}

	private void enterDopCommodityTotals(
			DopYieldContractRsrc resource,
			String coverageType,
			Double quantityHarvestedAcres, 
			Integer totalBales, 
			Double weight) {
		DopYieldContractCommodityForage dyccf = getDopYieldContractCommodityForage(coverageType, resource.getDopYieldContractCommodityForageList());
		Assert.assertNotNull(dyccf);
		dyccf.setTotalBalesLoads(totalBales);
		dyccf.setWeight(weight);
		dyccf.setWeightDefaultUnit(weight);
		dyccf.setMoisturePercent(moisturePercent);
		dyccf.setHarvestedAcres(quantityHarvestedAcres);
	}

	private DopYieldContractCommodityForage getDopYieldContractCommodityForage(String commodityTypeCode, List<DopYieldContractCommodityForage> dyccfList) {
		
		List<DopYieldContractCommodityForage> dyccfs = dyccfList.stream().filter(x -> x.getCommodityTypeCode().equalsIgnoreCase(commodityTypeCode)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, dyccfs.size());
		
		return dyccfs.get(0);
	}

	VerifiedYieldAmendment createVerifiedYieldAmendment(
			String verifiedYieldAmendmentCode, 
			Integer cropCommodityId,
			String cropCommodityName,
			Integer cropVarietyId,
			String cropVarietyName,
			Integer fieldId, 
			String fieldLabel, 
			Double acres, 
			Double yieldPerAcre,
			String rationale) 
	{

		VerifiedYieldAmendment vya = new VerifiedYieldAmendment();
		vya.setAcres(acres);
		vya.setCropCommodityId(cropCommodityId);
		vya.setCropCommodityName(cropCommodityName);
		vya.setCropVarietyId(cropVarietyId);
		vya.setCropVarietyName(cropVarietyName);
		vya.setFieldId(fieldId);
		vya.setFieldLabel(fieldLabel);
		vya.setIsPedigreeInd(false);
		vya.setRationale(rationale);
		vya.setVerifiedYieldAmendmentCode(verifiedYieldAmendmentCode);
		vya.setVerifiedYieldAmendmentGuid(null);
		vya.setVerifiedYieldContractGuid(null);
		vya.setYieldPerAcre(yieldPerAcre);
		
		return vya;
	}

	private UnderwritingComment createUwComment(String verifiedYieldSummaryGuid) {
		UnderwritingComment uwComment = new UnderwritingComment();
	
		uwComment = new UnderwritingComment();
		uwComment.setVerifiedYieldSummaryGuid(verifiedYieldSummaryGuid);
		uwComment.setUnderwritingComment("Verified Yield Comment");
		uwComment.setUnderwritingCommentGuid(null);
		uwComment.setUnderwritingCommentTypeCode("VY");
		uwComment.setUnderwritingCommentTypeDesc("Verified Yield");		

		return uwComment;
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}
}
