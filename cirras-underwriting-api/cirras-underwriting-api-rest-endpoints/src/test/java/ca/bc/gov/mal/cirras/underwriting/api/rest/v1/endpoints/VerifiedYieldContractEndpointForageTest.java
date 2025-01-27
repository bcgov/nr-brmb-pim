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
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiableCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldGrainBasket;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldSummary;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.CommodityCoverageCode;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InsurancePlans;
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
		
		createContractAndInventory();

		//Create DOP Contract Commodity Records
		createDopYieldContract(policyNumber1, insurancePlanId, false);

		//Barley - NON Pedigree - Product
		createUpdateProduct(policyId1, productId1, cropIdForage, 20, foragePY, forageProductionGuarantee, forageIV100, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_FORAGE);
		//Barley - Pedigree
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
		
		Assert.assertEquals(commodities.length, newContract.getVerifiedYieldContractCommodities().size());
		
		for(int i = 0; i < commodities.length; i++) {
			String commodity = commodities[i];
			VerifiedYieldContractCommodity vycc = newContract.getVerifiedYieldContractCommodities().get(i);
			
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
		
		
		
//		//Check verifiable Commodities and field
//		Assert.assertEquals(1, newContract.getFields().size());
//		AnnualFieldRsrc field = newContract.getFields().get(0);
//		Assert.assertEquals(fieldId, field.getFieldId());
//		Assert.assertEquals(2, field.getVerifiableCommodities().size());
//		Boolean pedigreed = false;
//		Boolean nonPedigreed = false;
//		for(VerifiableCommodity vc : field.getVerifiableCommodities()) {
//			Assert.assertEquals(16, vc.getCropCommodityId().intValue());
//			if(vc.getIsPedigreeInd()) {
//				pedigreed = true;
//			} else if(vc.getIsPedigreeInd() == false) {
//				nonPedigreed = true;
//			}
//		}
//		Assert.assertTrue(pedigreed);
//		Assert.assertTrue(nonPedigreed);
//		
//		//Create verified contract ******************************************************************************
//		//Add override values for barley non pedigree
//		VerifiedYieldContractCommodity barleyCommodity = getVerifiedYieldContractCommodity(16, false, newContract.getVerifiedYieldContractCommodities());
//		Assert.assertNotNull(barleyCommodity);
//		barleyCommodity.setHarvestedAcresOverride(22.0);
//		barleyCommodity.setHarvestedYieldOverride(barleyNonPedigreeHarvestedYieldOverride);
//		
//		//Create product for Canola - Pedigree
//		createUpdateProduct(policyId1, productId2, 19, 10, canolaPedigreePY, canolaPedigreeProductionGuarantee, canolaPedigreeIV100, null, "Open", CommodityCoverageCode.QUANTITY_GRAIN);
//
//		List<VerifiedYieldContractCommodity> expectedCommodities = newContract.getVerifiedYieldContractCommodities();
//		
//		VerifiedYieldContractRsrc createdContract = service.createVerifiedYieldContract(topLevelEndpoints, newContract);
//		Assert.assertNotNull(createdContract);
//
//		Assert.assertEquals(newContract.getContractId(), createdContract.getContractId());
//		Assert.assertEquals(newContract.getCropYear(), createdContract.getCropYear());
//		Assert.assertEquals(newContract.getDeclaredYieldContractGuid(), createdContract.getDeclaredYieldContractGuid());
//		Assert.assertEquals(newContract.getDefaultYieldMeasUnitTypeCode(), createdContract.getDefaultYieldMeasUnitTypeCode());
//		Assert.assertEquals(newContract.getGrowerContractYearId(), createdContract.getGrowerContractYearId());
//		Assert.assertEquals(newContract.getInsurancePlanId(), createdContract.getInsurancePlanId());
//		Assert.assertNotNull(createdContract.getVerifiedYieldContractGuid());
//		Assert.assertNotNull(createdContract.getVerifiedYieldUpdateUser());
//		Assert.assertNotNull(createdContract.getVerifiedYieldUpdateTimestamp());
//		
//		Assert.assertNull(createdContract.getVerifiedYieldGrainBasket()); //No grain basket expected
//
//		//Even though there is a product for canola now, there is no warning because purchase is not in status FINAL
//		Assert.assertEquals(0, createdContract.getProductWarningMessages().size());
//
//		// Check Contract Commodities
//		//Barley - NON Pedigree
//		checkVerifiedContractCommodityTotals(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), 16, false, barleyNonPediProductionGuarantee, barleyNonPedigreeSeededAcres); 
//		//Canola - Pedigree
//		checkVerifiedContractCommodityTotals(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), 18, true, null, null); 
//		//Barley - Pedigree
//		checkVerifiedContractCommodityTotals(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), 16, true, barleyPedigreeProductionGuarantee, null); 
//
//		//Delete Barley non Pedigree Product - Expect warning
//		service.deleteProduct(topLevelEndpoints, productId1.toString());
//		//Update Barley Pedigree Product Guarantee - Expect warning
//		Double barleyPedProdGuaranteeNew = barleyPedigreeProductionGuarantee + 20;
//		Double barleyPedigreePYNew = barleyPedigreePY + 10;
//		Double barleyPedigreeIV100New = barleyPedigreeIV100 + 10;
//		
//		createUpdateProduct(policyId1, productId3, 17, 50, barleyPedigreePYNew, barleyPedProdGuaranteeNew, barleyPedigreeIV100New, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
//		
//		//Create product for Grain Basket - No warning expected
//		createUpdateProduct(policyId1, productId4, 1010076, 20, null, null, null, grainBasketCoverageDollar, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.GRAIN_BASKET);
//
//		//Create product for Canola - Pedigree
//		createUpdateProduct(policyId1, productId2, 19, 10, canolaPedigreePY, canolaPedigreeProductionGuarantee, canolaPedigreeIV100, null, "Open", CommodityCoverageCode.QUANTITY_GRAIN);
//
//		
//		//Get contract again to prevent precondition fails
//		createdContract = getVerifiedYieldContract(policyNumber1);
//		Assert.assertNotNull(createdContract);
//		
//		//Update verified contract ******************************************************************************
//		//Remove override values for barley non pedigree
//		barleyCommodity = getVerifiedYieldContractCommodity(16, false, createdContract.getVerifiedYieldContractCommodities());
//		Assert.assertNotNull(barleyCommodity);
//		barleyCommodity.setHarvestedAcresOverride(null);
//		barleyCommodity.setHarvestedYieldOverride(null);
//		
//		//DON'T update product values
//		createdContract.setUpdateProductValuesInd(false);
//		
//		expectedCommodities = createdContract.getVerifiedYieldContractCommodities();
//
//		VerifiedYieldContractRsrc updatedContract = service.updateVerifiedYieldContract(createdContract);
//		Assert.assertNotNull(updatedContract);
//		
//		Assert.assertEquals(createdContract.getContractId(), updatedContract.getContractId());
//		Assert.assertEquals(createdContract.getCropYear(), updatedContract.getCropYear());
//		Assert.assertEquals(createdContract.getDeclaredYieldContractGuid(), updatedContract.getDeclaredYieldContractGuid());
//		Assert.assertEquals(createdContract.getDefaultYieldMeasUnitTypeCode(), updatedContract.getDefaultYieldMeasUnitTypeCode());
//		Assert.assertEquals(createdContract.getGrowerContractYearId(), updatedContract.getGrowerContractYearId());
//		Assert.assertEquals(createdContract.getInsurancePlanId(), updatedContract.getInsurancePlanId());
//		Assert.assertEquals(createdContract.getVerifiedYieldContractGuid(), updatedContract.getVerifiedYieldContractGuid());
//		Assert.assertEquals(createdContract.getVerifiedYieldUpdateUser(), updatedContract.getVerifiedYieldUpdateUser());
//		Assert.assertNotNull(updatedContract.getVerifiedYieldUpdateTimestamp());
//		
//		Assert.assertNotNull(updatedContract.getVerifiedYieldGrainBasket()); //grain basket expected because there is a product
//
//		
//		//4 warnings expected (2 Production Guarantee, 2 PY, 2 100% IV)
//		Assert.assertEquals(6, updatedContract.getProductWarningMessages().size());
//		
//		// Check Contract Commodities
//		//Barley - NON Pedigree
//		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 16, false, barleyNonPediProductionGuarantee, barleyNonPedigreeSeededAcres); 
//		//Canola - Pedigree
//		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 18, true, null, null); 
//		//Barley - Pedigree
//		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 16, true, barleyPedigreeProductionGuarantee, null); 
//		
//		//UPDATE product values
//		updatedContract.setUpdateProductValuesInd(true);
//		
//		updatedContract = service.updateVerifiedYieldContract(updatedContract);
//		Assert.assertNotNull(updatedContract);
//		
//		//0 warnings expected
//		Assert.assertEquals(0, updatedContract.getProductWarningMessages().size());
//
//		// Check Contract Commodities
//		//Barley - NON Pedigree
//		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 16, false, null, barleyNonPedigreeSeededAcres); 
//		//Canola - Pedigree
//		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 18, true, null, null); 
//		//Barley - Pedigree
//		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 16, true, barleyPedProdGuaranteeNew, null); 
//
//		//Update Barley Pedigree Product Guarantee and PY to NULL - Expect warning
//		createUpdateProduct(policyId1, productId3, 17, 50, 20.5, null, null, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
//		//Update Canola Pedigree to status final
//		//createUpdateProduct(policyId1, productId2, 19, 10, canolaPedigreePY, canolaPedigreeProductionGuarantee, null, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
//		createUpdateProduct(policyId1, productId2, 19, 10, canolaPedigreePY, canolaPedigreeProductionGuarantee, canolaPedigreeIV100, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
//		//Update product for Grain Basket
//		Double grainBasketCoverageDollarNew = grainBasketCoverageDollar + 10;
//		createUpdateProduct(policyId1, productId4, 1010076, 20, null, null, null, grainBasketCoverageDollarNew, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.GRAIN_BASKET);
//
//		//Get contract
//		VerifiedYieldContractRsrc vyContract = getVerifiedYieldContract(policyNumber1);
//		Assert.assertNotNull(vyContract);
//
//		//4 warnings expected (2 Production Guarantee, 2 PY, 2 100% IV, 1 Grain Basket)
//		Assert.assertEquals(7, vyContract.getProductWarningMessages().size());
//
//		//UPDATE product values
//		vyContract.setUpdateProductValuesInd(true);
//
//		vyContract = service.updateVerifiedYieldContract(vyContract);
//		Assert.assertNotNull(vyContract);
//
//		//0 warnings expected
//		Assert.assertEquals(0, vyContract.getProductWarningMessages().size());
//		
//		// Check Contract Commodities
//		//Barley - NON Pedigree
//		checkVerifiedContractCommodityTotals(expectedCommodities, vyContract.getVerifiedYieldContractCommodities(), 16, false, null, barleyNonPedigreeSeededAcres); 
//		//Canola - Pedigree
//		checkVerifiedContractCommodityTotals(expectedCommodities, vyContract.getVerifiedYieldContractCommodities(), 18, true, canolaPedigreeProductionGuarantee, null); 
//		//Barley - Pedigree
//		checkVerifiedContractCommodityTotals(expectedCommodities, vyContract.getVerifiedYieldContractCommodities(), 16, true, null, null); 
//
//		
//		// Grain Basket additional warning tests *******************************************************************
//		//Delete grain basket product
//		service.deleteProduct(topLevelEndpoints, productId4.toString());
//
//		vyContract = getVerifiedYieldContract(policyNumber1);
//		
//		//DON'T update product values
//		vyContract.setUpdateProductValuesInd(false);
//		
//		vyContract = service.updateVerifiedYieldContract(vyContract);
//		Assert.assertNotNull(vyContract);
//
//		Assert.assertNotNull(vyContract.getVerifiedYieldGrainBasket()); //grain basket expected because user didn't want to update
//
//		//1 warning expected - Grain Basket
//		Assert.assertEquals(1, vyContract.getProductWarningMessages().size());
//
//		//UPDATE product values
//		vyContract.setUpdateProductValuesInd(true);
//		
//		vyContract = service.updateVerifiedYieldContract(vyContract);
//		Assert.assertNotNull(vyContract);
//
//		//0 warnings expected
//		Assert.assertEquals(0, vyContract.getProductWarningMessages().size());
//		
//		Assert.assertNull(vyContract.getVerifiedYieldGrainBasket()); //grain basket is deleted because user want to update
//
//		//Create product for Grain Basket - No warning expected
//		createUpdateProduct(policyId1, productId4, 1010076, 20, null, null, null, grainBasketCoverageDollar, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.GRAIN_BASKET);
//
//		vyContract = getVerifiedYieldContract(policyNumber1);
//
//		Assert.assertNull(vyContract.getVerifiedYieldGrainBasket()); //grain basket not expected
//
//		//1 warning expected - Grain Basket
//		Assert.assertEquals(1, vyContract.getProductWarningMessages().size());
//		
//		// END Grain Basket additional warning tests ***********************************************************
//
//		
//		//Delete verified contract ******************************************************************************
//		service.deleteVerifiedYieldContract(vyContract);
//		
//		searchResults = service.getUwContractList(
//				topLevelEndpoints, 
//				null, 
//				null, 
//				null,
//				null,
//				policyNumber1,
//				null,
//				null, 
//				null, 
//				null, 
//				pageNumber, pageRowCount);
//
//		Assert.assertNotNull(searchResults);
//		Assert.assertEquals(1, searchResults.getCollection().size());
//
//		referrer = searchResults.getCollection().get(0);
//		Assert.assertNotNull(referrer.getInventoryContractGuid());
//		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
//		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
		
		logger.debug(">testInsertUpdateDeleteForageVerifiedYield");
	}

	@Test
	public void testInsertUpdateDeleteForageVerifiedYieldAmendments() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testInsertUpdateDeleteForageVerifiedYieldAmendments");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
//		//Date and Time without millisecond
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
//		Date transactionDate = cal.getTime();
//		Date createTransactionDate = addSeconds(transactionDate, -1);
//
//		createGrower(growerId, 999888, "grower name", createTransactionDate);
//		
//		createLegalLand("test legal 9999", null, legalLandId, "999-888-000", 1980, null);
//		createField(fieldId, "LOT 3", 1980, null);
//
//		createGrowerContractYear(growerContractYearId1, contractId, growerId, cropYear1, 4, createTransactionDate);
//		createPolicy(policyId1, growerId, 4, policyNumber1, contractNumber, contractId, cropYear1, createTransactionDate);
//		createAnnualFieldDetail(annualFieldDetailId1, legalLandId, fieldId, cropYear1);
//		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);
//
//		createInventoryContract(policyNumber1, 4, false);
//		createDopYieldContract(policyNumber1, 4, false);
//				
//		Integer pageNumber = 1;
//		Integer pageRowCount = 20;
//
//		// Rollover and create VerifiedYieldContract.
//		UwContractListRsrc searchResults = service.getUwContractList(
//				topLevelEndpoints, 
//				null, 
//				null, 
//				null,
//				null,
//				policyNumber1,
//				null,
//				null, 
//				null, 
//				null, 
//				pageNumber, pageRowCount);
//
//		Assert.assertNotNull(searchResults);
//		Assert.assertEquals(1, searchResults.getCollection().size());
//
//		UwContractRsrc referrer = searchResults.getCollection().get(0);
//		Assert.assertNotNull(referrer.getInventoryContractGuid());
//		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
//		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
//		
//		// Check VerifiedYieldContract
//		VerifiedYieldContractRsrc newContract = service.rolloverVerifiedYieldContract(referrer);
//		Assert.assertNotNull(newContract);
//
//		Assert.assertEquals(contractId, newContract.getContractId());
//		Assert.assertEquals(cropYear1, newContract.getCropYear());
//		Assert.assertEquals(referrer.getDeclaredYieldContractGuid(), newContract.getDeclaredYieldContractGuid());
//		Assert.assertEquals("TONNE", newContract.getDefaultYieldMeasUnitTypeCode());
//		Assert.assertEquals(growerContractYearId1, newContract.getGrowerContractYearId());
//		Assert.assertEquals(Integer.valueOf(4), newContract.getInsurancePlanId());
//		Assert.assertNull(newContract.getVerifiedYieldContractGuid());
//		Assert.assertNull(newContract.getVerifiedYieldUpdateUser());
//		Assert.assertNull(newContract.getVerifiedYieldUpdateTimestamp());
//
//		// Check VerifiedYieldAmendments
//		Assert.assertEquals(0, newContract.getVerifiedYieldAmendments().size());
//		
//		
//		//Create verified contract ******************************************************************************
//
//		VerifiedYieldAmendment vya = createVerifiedYieldAmendment("Appraisal", 18, "CANOLA", false, null, null, 12.34, 56.78, "rationale 1");
//		newContract.getVerifiedYieldAmendments().add(vya);
//		
//		vya = createVerifiedYieldAmendment("Assessment", 16, "BARLEY", true, fieldId, "LOT 3", 11.22, 33.44, "rationale 2");
//		newContract.getVerifiedYieldAmendments().add(vya);
//
//		vya = createVerifiedYieldAmendment("Assessment", 18, "CANOLA", true, null, null, 22.11, 44.33, "rationale 3");
//		newContract.getVerifiedYieldAmendments().add(vya);		
//		
//		VerifiedYieldContractRsrc createdContract = service.createVerifiedYieldContract(topLevelEndpoints, newContract);
//		Assert.assertNotNull(createdContract);
//
//		Assert.assertEquals(newContract.getContractId(), createdContract.getContractId());
//		Assert.assertEquals(newContract.getCropYear(), createdContract.getCropYear());
//		Assert.assertEquals(newContract.getDeclaredYieldContractGuid(), createdContract.getDeclaredYieldContractGuid());
//		Assert.assertEquals(newContract.getDefaultYieldMeasUnitTypeCode(), createdContract.getDefaultYieldMeasUnitTypeCode());
//		Assert.assertEquals(newContract.getGrowerContractYearId(), createdContract.getGrowerContractYearId());
//		Assert.assertEquals(newContract.getInsurancePlanId(), createdContract.getInsurancePlanId());
//		Assert.assertNotNull(createdContract.getVerifiedYieldContractGuid());
//		Assert.assertNotNull(createdContract.getVerifiedYieldUpdateUser());
//		Assert.assertNotNull(createdContract.getVerifiedYieldUpdateTimestamp());
//
//		checkVerifiedYieldAmendments(newContract.getVerifiedYieldAmendments(), createdContract.getVerifiedYieldAmendments());
//		
//		
//		//Update verified contract ******************************************************************************
//		List<VerifiedYieldAmendment> expectedAmendments = new ArrayList<VerifiedYieldAmendment>();
//		
//		vya = createdContract.getVerifiedYieldAmendments().get(0);
//		vya.setAcres(55.66);
//		vya.setCropCommodityId(16);
//		vya.setCropCommodityName("BARLEY");
//		vya.setFieldId(fieldId);
//		vya.setFieldLabel("LOT 3");
//		vya.setIsPedigreeInd(true);
//		vya.setRationale("rationale A");
//		vya.setVerifiedYieldAmendmentCode("Assessment");
//		vya.setYieldPerAcre(77.88);
//
//		expectedAmendments.add(vya);
//		
//		vya = createdContract.getVerifiedYieldAmendments().get(1);
//		vya.setAcres(98.76);
//		vya.setCropCommodityId(18);
//		vya.setCropCommodityName("CANOLA");
//		vya.setFieldId(null);
//		vya.setFieldLabel(null);
//		vya.setIsPedigreeInd(false);
//		vya.setRationale("rationale B");
//		vya.setVerifiedYieldAmendmentCode("Appraisal");
//		vya.setYieldPerAcre(54.32);
//		
//		// The order in which amendments will be returned is reversed by these updates.
//		expectedAmendments.add(0, vya);
//
//		vya = createdContract.getVerifiedYieldAmendments().get(2);
//		vya.setDeletedByUserInd(true);
//		
//		VerifiedYieldContractRsrc updatedContract = service.updateVerifiedYieldContract(createdContract);
//		Assert.assertNotNull(updatedContract);
//		
//		Assert.assertEquals(createdContract.getContractId(), updatedContract.getContractId());
//		Assert.assertEquals(createdContract.getCropYear(), updatedContract.getCropYear());
//		Assert.assertEquals(createdContract.getDeclaredYieldContractGuid(), updatedContract.getDeclaredYieldContractGuid());
//		Assert.assertEquals(createdContract.getDefaultYieldMeasUnitTypeCode(), updatedContract.getDefaultYieldMeasUnitTypeCode());
//		Assert.assertEquals(createdContract.getGrowerContractYearId(), updatedContract.getGrowerContractYearId());
//		Assert.assertEquals(createdContract.getInsurancePlanId(), updatedContract.getInsurancePlanId());
//		Assert.assertEquals(createdContract.getVerifiedYieldContractGuid(), updatedContract.getVerifiedYieldContractGuid());
//		Assert.assertEquals(createdContract.getVerifiedYieldUpdateUser(), updatedContract.getVerifiedYieldUpdateUser());
//		Assert.assertNotNull(updatedContract.getVerifiedYieldUpdateTimestamp());
//
//		checkVerifiedYieldAmendments(expectedAmendments, updatedContract.getVerifiedYieldAmendments());
//
//		//Delete verified contract ******************************************************************************
//		service.deleteVerifiedYieldContract(updatedContract);
//		
//		searchResults = service.getUwContractList(
//				topLevelEndpoints, 
//				null, 
//				null, 
//				null,
//				null,
//				policyNumber1,
//				null,
//				null, 
//				null, 
//				null, 
//				pageNumber, pageRowCount);
//
//		Assert.assertNotNull(searchResults);
//		Assert.assertEquals(1, searchResults.getCollection().size());
//
//		referrer = searchResults.getCollection().get(0);
//		Assert.assertNotNull(referrer.getInventoryContractGuid());
//		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
//		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
		
		logger.debug(">testInsertUpdateDeleteForageVerifiedYieldAmendments");
	}
	
	private String appraisal = InventoryServiceEnums.AmendmentTypeCode.Appraisal.toString();
	private String assessment = InventoryServiceEnums.AmendmentTypeCode.Assessment.toString();
	private Double oatAppraisalYield1 = 13.0;
	private Double oatAppraisalAcres1 = 5.0;
	private Double barleyNonPedAppraisalYield1 = 11.0;
	private Double barleyNonPedAppraisalAcres1 = 2.0;
	private Double barleyNonPedAppraisalYield2 = 12.0;
	private Double barleyNonPedAppraisalAcres2 = 5.0;
	private Double barleyNonPedAssessmentYield1 = 4.0;
	private Double barleyNonPedAssessmentAcres1 = 3.0;
	private Double barleyPedigreeAssessmentYield1 = 15.0;
	private Double barleyPedigreeAssessmentAcres1 = 3.0;
	private Double barleyPedigreeAssessmentYield2 = 3.0;
	private Double barleyPedigreeAssessmentAcres2 = 5.0;
	
	@Test
	public void testForageVerifiedYieldSummary() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testForageVerifiedYieldSummary");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
//		//Date and Time without millisecond
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
//		Date transactionDate = cal.getTime();
//		Date createTransactionDate = addSeconds(transactionDate, -1);
//
//		createGrower(growerId, 999888, "grower name", createTransactionDate);
//		
//		createLegalLand("test legal 9999", null, legalLandId, "999-888-000", 1980, null);
//		createField(fieldId, "LOT 3", 1980, null);
//
//		createGrowerContractYear(growerContractYearId1, contractId, growerId, cropYear1, 4, createTransactionDate);
//		createPolicy(policyId1, growerId, 4, policyNumber1, contractNumber, contractId, cropYear1, createTransactionDate);
//		createAnnualFieldDetail(annualFieldDetailId1, legalLandId, fieldId, cropYear1);
//		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);
//
//		createInventoryContract(policyNumber1, 4, true);
//		createDopYieldContract(policyNumber1, 4, true);
//		
//		//Barley - NON Pedigree - Product
//		createUpdateProduct(policyId1, productId1, 16, 20, barleyNonPedigreePY, barleyNonPediProductionGuarantee, barleyNonPedigreeIV100, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
//		//Barley - Pedigree
//		createUpdateProduct(policyId1, productId3, 17, 50, barleyPedigreePY, barleyPedigreeProductionGuarantee, barleyPedigreeIV100, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
//		
//		Integer pageNumber = 1;
//		Integer pageRowCount = 20;
//
//		// Rollover and create VerifiedYieldContract.
//		UwContractListRsrc searchResults = service.getUwContractList(
//				topLevelEndpoints, 
//				null, 
//				null, 
//				null,
//				null,
//				policyNumber1,
//				null,
//				null, 
//				null, 
//				null, 
//				pageNumber, pageRowCount);
//
//		Assert.assertNotNull(searchResults);
//		Assert.assertEquals(1, searchResults.getCollection().size());
//
//		UwContractRsrc referrer = searchResults.getCollection().get(0);
//		Assert.assertNotNull(referrer.getInventoryContractGuid());
//		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
//		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
//		
//		// Check VerifiedYieldContract
//		VerifiedYieldContractRsrc newContract = service.rolloverVerifiedYieldContract(referrer);
//		Assert.assertNotNull(newContract);
//
//		Assert.assertEquals(contractId, newContract.getContractId());
//		Assert.assertEquals(cropYear1, newContract.getCropYear());
//		Assert.assertEquals(referrer.getDeclaredYieldContractGuid(), newContract.getDeclaredYieldContractGuid());
//		Assert.assertEquals("TONNE", newContract.getDefaultYieldMeasUnitTypeCode());
//		Assert.assertEquals(growerContractYearId1, newContract.getGrowerContractYearId());
//		Assert.assertEquals(Integer.valueOf(4), newContract.getInsurancePlanId());
//
//		// Check VerifiedYieldContractCommodities
//		Assert.assertEquals(2, newContract.getVerifiedYieldContractCommodities().size());
//		
//		// Check VerifiedYieldAmendments
//		Assert.assertEquals(0, newContract.getVerifiedYieldAmendments().size());
//
//		
//		//Create verified contract ******************************************************************************
//		//Add override values for barley non pedigree
//		VerifiedYieldContractCommodity barleyCommodity = getVerifiedYieldContractCommodity(16, false, newContract.getVerifiedYieldContractCommodities());
//		Assert.assertNotNull(barleyCommodity);
//		barleyCommodity.setHarvestedAcresOverride(22.0);
//		barleyCommodity.setHarvestedYieldOverride(barleyNonPedigreeHarvestedYieldOverride);
//		
//		//Create product for Canola - Pedigree
//		createUpdateProduct(policyId1, productId2, 19, 10, 50.5, null, canolaPedigreePY, canolaPedigreeProductionGuarantee, "Open", CommodityCoverageCode.QUANTITY_GRAIN);
//				
//		//Create Amendments
//		VerifiedYieldAmendment vya = createVerifiedYieldAmendment(appraisal, 24, "OAT", false, null, null, oatAppraisalAcres1, oatAppraisalYield1, "rationale 1");
//		newContract.getVerifiedYieldAmendments().add(vya);
//		
//		vya = createVerifiedYieldAmendment(appraisal, 16, "BARLEY", false, fieldId, "LOT 3", barleyNonPedAppraisalAcres1, barleyNonPedAppraisalYield1, "rationale 2");
//		newContract.getVerifiedYieldAmendments().add(vya);
//
//		vya = createVerifiedYieldAmendment(appraisal, 16, "BARLEY", false, null, null, barleyNonPedAppraisalAcres2, barleyNonPedAppraisalYield2, "rationale 2");
//		newContract.getVerifiedYieldAmendments().add(vya);
//
//		vya = createVerifiedYieldAmendment(assessment, 16, "BARLEY", false, null, null, barleyNonPedAssessmentAcres1, barleyNonPedAssessmentYield1, "rationale 2");
//		newContract.getVerifiedYieldAmendments().add(vya);
//		
//		vya = createVerifiedYieldAmendment(assessment, 16, "BARLEY", true, null, null, barleyPedigreeAssessmentAcres1, barleyPedigreeAssessmentYield1, "rationale 2");
//		newContract.getVerifiedYieldAmendments().add(vya);
//		
//		vya = createVerifiedYieldAmendment(assessment, 16, "BARLEY", true, null, null, barleyPedigreeAssessmentAcres2, barleyPedigreeAssessmentYield2, "rationale 2");
//		newContract.getVerifiedYieldAmendments().add(vya);
//		
//		List<VerifiedYieldSummary> expectedVys = createExpectedVerifiedYieldSummaries();
//
//		VerifiedYieldContractRsrc createdContract = service.createVerifiedYieldContract(topLevelEndpoints, newContract);
//		Assert.assertNotNull(createdContract);
//
//		Assert.assertEquals(newContract.getContractId(), createdContract.getContractId());
//		Assert.assertEquals(newContract.getCropYear(), createdContract.getCropYear());
//		Assert.assertEquals(newContract.getDeclaredYieldContractGuid(), createdContract.getDeclaredYieldContractGuid());
//		Assert.assertEquals(newContract.getDefaultYieldMeasUnitTypeCode(), createdContract.getDefaultYieldMeasUnitTypeCode());
//		Assert.assertEquals(newContract.getGrowerContractYearId(), createdContract.getGrowerContractYearId());
//		Assert.assertEquals(newContract.getInsurancePlanId(), createdContract.getInsurancePlanId());
//		
//		//Expect 3 verified yield summaries
//		Assert.assertEquals(3, createdContract.getVerifiedYieldSummaries().size());
//		
//		//Check Verified Yield Summary
//		checkVerifiedYieldSummaries(expectedVys, createdContract.getVerifiedYieldSummaries(), createdContract.getVerifiedYieldContractGuid());
//
//		//Add comments
//		List<UnderwritingComment> comments = new ArrayList<UnderwritingComment>();
//		//Oat
//		VerifiedYieldSummary oatVys = getVerifiedYieldSummary(24, false, createdContract.getVerifiedYieldSummaries());
//		Assert.assertNotNull(oatVys);
//		comments.add(createUwComment(oatVys.getVerifiedYieldSummaryGuid()));
//		oatVys.setUwComments(comments);
//		
//		//Barley non pedigree - 2 comments
//		VerifiedYieldSummary barleyVys = getVerifiedYieldSummary(16, false, createdContract.getVerifiedYieldSummaries());
//		Assert.assertNotNull(barleyVys);
//		comments = new ArrayList<UnderwritingComment>();
//		comments.add(createUwComment(barleyVys.getVerifiedYieldSummaryGuid()));
//		comments.add(createUwComment(barleyVys.getVerifiedYieldSummaryGuid()));
//		barleyVys.setUwComments(comments);
//		
//		//Add amendment of another commodity
//		vya = createVerifiedYieldAmendment(appraisal, 26, "WHEAT", false, null, null, oatAppraisalAcres1, oatAppraisalYield1, "rationale 1");
//		createdContract.getVerifiedYieldAmendments().add(vya);
//
//		VerifiedYieldContractRsrc updatedContract = service.updateVerifiedYieldContract(createdContract);
//		Assert.assertNotNull(updatedContract);
//		
//		//Expect 4 verified yield summaries
//		Assert.assertEquals(4, updatedContract.getVerifiedYieldSummaries().size());
//
//		//Check Comments
//		oatVys = getVerifiedYieldSummary(24, false, updatedContract.getVerifiedYieldSummaries());
//		Assert.assertNotNull(oatVys);
//		Assert.assertEquals(1, oatVys.getUwComments().size());
//
//		barleyVys = getVerifiedYieldSummary(16, false, updatedContract.getVerifiedYieldSummaries());
//		Assert.assertNotNull(barleyVys);
//		Assert.assertEquals(2, barleyVys.getUwComments().size());
//		//Remove one barley comment
//		barleyVys.getUwComments().get(0).setDeletedByUserInd(true);
//
//		VerifiedYieldSummary wheatVys = getVerifiedYieldSummary(26, false, updatedContract.getVerifiedYieldSummaries());
//		Assert.assertNotNull(wheatVys);
//		
//		//Remove Oat Amendment - Expect summary to be deleted
//		VerifiedYieldAmendment oatAmendment = getVerifiedYieldAmendment(24, false, updatedContract.getVerifiedYieldAmendments());
//		Assert.assertNotNull(oatAmendment);
//		oatAmendment.setDeletedByUserInd(true);
//		
//		//Remove 1 Barley non pedigree amendment - Expect updated summary
//		Boolean barleyAmendmentRemoved = false;
//		List<VerifiedYieldAmendment> barleyAmendments = getCommodityAmendments(16, false, updatedContract.getVerifiedYieldAmendments());
//		for (VerifiedYieldAmendment barleyAmendment : barleyAmendments) {
//
//			if(barleyAmendment.getVerifiedYieldAmendmentCode().equalsIgnoreCase(appraisal) &&
//					barleyAmendment.getAcres().equals(barleyNonPedAppraisalAcres2) &&
//					barleyAmendment.getYieldPerAcre().equals(barleyNonPedAppraisalYield2)) {
//
//				barleyAmendment.setDeletedByUserInd(true);
//				barleyAmendmentRemoved = true;
//			}
//		}
//		Assert.assertTrue(barleyAmendmentRemoved);
//		
//		//Update expected summary values
//		VerifiedYieldSummary expBarleyVys = getVerifiedYieldSummary(16, false, expectedVys);
//		Assert.assertNotNull(expBarleyVys);
//		Double appraisedYield = (barleyNonPedAppraisalYield1 * barleyNonPedAppraisalAcres1);
//		expBarleyVys.setAppraisedYield(appraisedYield);
//		Double yieldToCount = expBarleyVys.getHarvestedYield() + expBarleyVys.getAppraisedYield();
//		expBarleyVys.setYieldToCount(yieldToCount);
//		Double yieldPercentPy = expBarleyVys.getYieldToCount()/ (barleyNonPedigreeSeededAcres * barleyNonPedigreePY);
//		expBarleyVys.setYieldPercentPy(yieldPercentPy);
//		
//		updatedContract = service.updateVerifiedYieldContract(updatedContract);
//		Assert.assertNotNull(updatedContract);
//		
//		//Expect 3 verified yield summaries - Oat should be deleted
//		Assert.assertEquals(3, updatedContract.getVerifiedYieldSummaries().size());
//
//		barleyVys = getVerifiedYieldSummary(16, false, updatedContract.getVerifiedYieldSummaries());
//		Assert.assertNotNull(barleyVys);
//		//One comment should be deleted
//		Assert.assertEquals(1, barleyVys.getUwComments().size());
//
//		checkVerifiedYieldSummary(expBarleyVys, barleyVys, updatedContract.getVerifiedYieldContractGuid());				
//
//
//		//Delete verified contract ******************************************************************************
//		service.deleteVerifiedYieldContract(updatedContract);
//		
//		searchResults = service.getUwContractList(
//				topLevelEndpoints, 
//				null, 
//				null, 
//				null,
//				null,
//				policyNumber1,
//				null,
//				null, 
//				null, 
//				null, 
//				pageNumber, pageRowCount);
//
//		Assert.assertNotNull(searchResults);
//		Assert.assertEquals(1, searchResults.getCollection().size());
//
//		referrer = searchResults.getCollection().get(0);
//		Assert.assertNotNull(referrer.getInventoryContractGuid());
//		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
//		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
		
		logger.debug(">testForageVerifiedYieldSummary");
	}	
	
	private List<VerifiedYieldSummary> createExpectedVerifiedYieldSummaries() {
		
		List<VerifiedYieldSummary> expVys = new ArrayList<VerifiedYieldSummary>();
//		
//		VerifiedYieldSummary vys = new VerifiedYieldSummary();
//		
//		//Barley Non Pedigree
//		vys.setCropCommodityId(16);
//		vys.setCropCommodityName("BARLEY");
//		vys.setIsPedigreeInd(false);
//		vys.setHarvestedYield(barleyNonPedigreeHarvestedYieldOverride); //Use Override
//		Double appraisedYield = (barleyNonPedAppraisalYield1 * barleyNonPedAppraisalAcres1) + (barleyNonPedAppraisalYield2 * barleyNonPedAppraisalAcres2);
//		vys.setAppraisedYield(appraisedYield);
//		Double assessedYield = (barleyNonPedAssessmentYield1 * barleyNonPedAssessmentAcres1);
//		vys.setAssessedYield(assessedYield);
//		Double yieldToCount = vys.getHarvestedYield() + vys.getAppraisedYield();
//		vys.setYieldToCount(yieldToCount);
//		Double yieldPercentPy = vys.getYieldToCount()/ (barleyNonPedigreeSeededAcres * barleyNonPedigreePY);
//		vys.setYieldPercentPy(yieldPercentPy);
//		vys.setProductionGuarantee(barleyNonPediProductionGuarantee);
//		vys.setProbableYield(barleyNonPedigreePY);
//		vys.setInsurableValueHundredPercent(barleyNonPedigreeIV100);
//		vys.setTotalInsuredAcres(barleyNonPedigreeSeededAcres);
//		
//		expVys.add(vys);
//
//		//Barley Pedigree
//		vys = new VerifiedYieldSummary();
//		
//		vys.setCropCommodityId(16);
//		vys.setCropCommodityName("BARLEY");
//		vys.setIsPedigreeInd(true);
//		vys.setHarvestedYield(barleyPedigreeSoldYield + barleyPedigreeStoredYield);
//		appraisedYield = null;
//		vys.setAppraisedYield(appraisedYield);
//		assessedYield = (barleyPedigreeAssessmentYield1 * barleyPedigreeAssessmentAcres1) + (barleyPedigreeAssessmentYield2 * barleyPedigreeAssessmentAcres2);
//		vys.setAssessedYield(assessedYield);
//		yieldToCount = vys.getHarvestedYield();
//		vys.setYieldToCount(yieldToCount);
//		yieldPercentPy = vys.getYieldToCount()/ (barleyPedigreeSeededAcres * barleyPedigreePY);
//		vys.setYieldPercentPy(yieldPercentPy);
//		vys.setProductionGuarantee(barleyPedigreeProductionGuarantee);
//		vys.setProbableYield(barleyPedigreePY );
//		vys.setInsurableValueHundredPercent(barleyPedigreeIV100 );
//		vys.setTotalInsuredAcres(barleyPedigreeSeededAcres);
//		
//		expVys.add(vys);
//
//		//Oat Non Pedigree
//		vys = new VerifiedYieldSummary();
//		
//		vys.setCropCommodityId(24);
//		vys.setCropCommodityName("OAT");
//		vys.setIsPedigreeInd(false);
//		vys.setHarvestedYield(null);
//		appraisedYield = (oatAppraisalYield1 * oatAppraisalAcres1);
//		vys.setAppraisedYield(appraisedYield);
//		assessedYield = null;
//		vys.setAssessedYield(assessedYield);
//		yieldToCount = vys.getAppraisedYield();
//		vys.setYieldToCount(yieldToCount);
//		yieldPercentPy = null;
//		vys.setYieldPercentPy(yieldPercentPy);
//		vys.setProductionGuarantee(null);
//		vys.setProbableYield(null);
//		vys.setTotalInsuredAcres(null);
//		
//		expVys.add(vys);
		
		return expVys;
	}

	private void checkVerifiedContractCommodityTotals(
			List<VerifiedYieldContractCommodity> expectedCommodities,
			List<VerifiedYieldContractCommodity> actualCommodities,
			Integer cropCommodityId,
			String commodityTypeCode,
			Double productionGuarantee,
			Double insuredAcres) {
		
		VerifiedYieldContractCommodity expectedCommodity = getVerifiedYieldContractCommodity(cropCommodityId, commodityTypeCode, expectedCommodities);
		VerifiedYieldContractCommodity actualCommodity = getVerifiedYieldContractCommodity(cropCommodityId, commodityTypeCode, actualCommodities);

		//TOOD: not necessary for forage
		//Harvested Yield
		if(expectedCommodity.getSoldYieldDefaultUnit() == null && expectedCommodity.getStoredYieldDefaultUnit() == null) {
			Assert.assertNull(actualCommodity.getHarvestedYield());
		} else {
			Double harvestedYield = notNull(expectedCommodity.getSoldYieldDefaultUnit(), 0.0) + notNull(expectedCommodity.getStoredYieldDefaultUnit(), 0.0);
			Assert.assertEquals(harvestedYield, actualCommodity.getHarvestedYield());
		}

		//Yield per acre: 0 if yield and acres are null
		Double yieldPerAcre = 0.0;
		Double effectiveAcres = notNull(expectedCommodity.getHarvestedAcresOverride(), expectedCommodity.getHarvestedAcres());
		Double effectiveYield = notNull(expectedCommodity.getHarvestedYieldOverride(), expectedCommodity.getHarvestedYield());

		if ( effectiveAcres != null && effectiveYield != null ) {
			if ( effectiveAcres > 0.0 ) {
				yieldPerAcre = effectiveYield / effectiveAcres;
			}
		}
		//TODO: add commodity type code and is rolled up
		Assert.assertEquals(expectedCommodity.getCropCommodityId(), actualCommodity.getCropCommodityId());
		Assert.assertEquals(expectedCommodity.getCropCommodityName(), actualCommodity.getCropCommodityName());
		Assert.assertEquals(expectedCommodity.getHarvestedAcres(), actualCommodity.getHarvestedAcres());
		Assert.assertEquals(expectedCommodity.getHarvestedAcresOverride(), actualCommodity.getHarvestedAcresOverride());
		Assert.assertEquals(expectedCommodity.getHarvestedYieldOverride(), actualCommodity.getHarvestedYieldOverride());
		Assert.assertEquals(expectedCommodity.getIsPedigreeInd(), actualCommodity.getIsPedigreeInd());
		Assert.assertEquals(productionGuarantee, actualCommodity.getProductionGuarantee());
		Assert.assertEquals(expectedCommodity.getSoldYieldDefaultUnit(), actualCommodity.getSoldYieldDefaultUnit());
		Assert.assertEquals(expectedCommodity.getStoredYieldDefaultUnit(), actualCommodity.getStoredYieldDefaultUnit());
		Assert.assertEquals(insuredAcres, actualCommodity.getTotalInsuredAcres());
		Assert.assertNotNull(actualCommodity.getVerifiedYieldContractCommodityGuid());
		Assert.assertNotNull(actualCommodity.getVerifiedYieldContractGuid());
		Assert.assertEquals(yieldPerAcre, actualCommodity.getYieldPerAcre(), 0.00005);
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
		Assert.assertNull(verifiedCommodity.getIsPedigreeInd());
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
		Assert.assertNull(verifiedCommodity.getIsPedigreeInd());
		Assert.assertNull(verifiedCommodity.getProductionGuarantee());
		Assert.assertNull(verifiedCommodity.getSoldYieldDefaultUnit());
		Assert.assertNull(verifiedCommodity.getStoredYieldDefaultUnit());
	}

	private void checkVerifiedYieldAmendments(List<VerifiedYieldAmendment> expected, List<VerifiedYieldAmendment> actual) {
		if ( expected == null && actual != null || expected != null && actual == null ) {
			Assert.fail();
		} else if ( expected != null && actual != null ) {
			Assert.assertEquals(expected.size(), actual.size());

			for ( int i = 0; i < expected.size(); i++ ) {
				checkVerifiedYieldAmendment(expected.get(i), actual.get(i));				
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
		Assert.assertEquals(expected.getIsPedigreeInd(), actual.getIsPedigreeInd());
		Assert.assertEquals(expected.getFieldId(), actual.getFieldId());
		Assert.assertEquals(expected.getYieldPerAcre(), actual.getYieldPerAcre());
		Assert.assertEquals(expected.getAcres(), actual.getAcres());
		Assert.assertEquals(expected.getRationale(), actual.getRationale());
		Assert.assertEquals(expected.getCropCommodityName(), actual.getCropCommodityName());
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
	
	private VerifiedYieldAmendment getVerifiedYieldAmendment(Integer cropCommodityId, Boolean isPedigree, List<VerifiedYieldAmendment> vyaList) {
		
		VerifiedYieldAmendment vya = null;
		
		List<VerifiedYieldAmendment> vyaFiltered = getCommodityAmendments(cropCommodityId, isPedigree, vyaList);
		
		if (vyaFiltered != null) {
			vya = vyaFiltered.get(0);
		}
		return vya;
	}

	private List<VerifiedYieldAmendment> getCommodityAmendments(Integer cropCommodityId, Boolean isPedigree,
			List<VerifiedYieldAmendment> vyaList) {
		List<VerifiedYieldAmendment> vyaFiltered = vyaList.stream()
				.filter(x -> x.getCropCommodityId().equals(cropCommodityId) && x.getIsPedigreeInd().equals(isPedigree) )
				.collect(Collectors.toList());
		return vyaFiltered;
	}
	
	private VerifiedYieldSummary getVerifiedYieldSummary(Integer cropCommodityId, Boolean isPedigree, List<VerifiedYieldSummary> vysList) {
		
		VerifiedYieldSummary vys = null;
		
		List<VerifiedYieldSummary> vysFiltered = vysList.stream()
				.filter(x -> x.getCropCommodityId().equals(cropCommodityId) && x.getIsPedigreeInd().equals(isPedigree) )
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
				
				VerifiedYieldSummary actualVys = getVerifiedYieldSummary(expectedVys.getCropCommodityId(), expectedVys.getIsPedigreeInd(), actual);
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
		Assert.assertEquals("HarvestedYield", expected.getHarvestedYield(), actual.getHarvestedYield());
		//Assert.assertEquals("HarvestedYieldPerAcre", expected.getHarvestedYieldPerAcre(), actual.getHarvestedYieldPerAcre());
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
	
	private Double barleyNonPedigreeSeededAcres = 23.45;
	private Double barleyPedigreeSeededAcres = 15.00;
	
	private static final String ctcAlfalfa = "Alfalfa";
	private static final String ctcSilageCorn = "Silage Corn";
	private static final String ctcPasture = "Pasture";
	private Integer cropIdForage = 65;
	private Integer cropIdSilageCorn = 71;
	private Integer varietyIdAlfalafaGrass = 118;
	private Integer varietyIdSilageCorn = 1010863;
	private Integer varietyIdCrownNative = 1010986;
	private String varietyNameAlfalafaGrass = "ALFALFA/GRASS";
	private String varietyNameSilageCorn = "SILAGE CORN - UNSPECIFIED";
	private Integer alfalfaBales = 50;
	private double alfalfaWeight = 2.0;
	private double alfalfaQuantityHarestedAcres = 320.0;
	private double alfalfaInsuredAcres1 = 100.0;
	private double alfalfaInsuredAcres2 = 150.0;
	private Integer pastureBales = 40;
	private double pastureWeight = 3.0;
	private double pastureQuantityHarestedAcres = 150.0;
	private double pastureInsuredAcres = 200.0;
	private Integer silageCornBales = 35;
	private double silageCornWeight = 2.5;
	private double silageCornQuantityHarestedAcres = 400.0;
	private double silageCornInsuredAcres1 = 250.0;
	private double silageCornInsuredAcres2 = 300.0;
	private double moisturePercent = 15.0;

	String annual = InventoryServiceEnums.PlantDurationType.ANNUAL.toString();
	String perennial = InventoryServiceEnums.PlantDurationType.PERENNIAL.toString();

	Date seedingDate = null;

	
	protected void createContractAndInventory() throws ValidationException, CirrasUnderwritingServiceException, DaoException {
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
		
		// Planting 1 - Forage - Alfalfa
		InventoryField planting = createPlanting(field1, 1, cropYear1);
		createInventorySeededForage(planting, cropIdForage, varietyIdAlfalafaGrass, ctcAlfalfa, alfalfaInsuredAcres1, seedingDate); //Alfalfa Grass

		// Planting 2 - Forage - Alfalfa
		planting = createPlanting(field1, 2, cropYear1);
		createInventorySeededForage(planting, cropIdForage, varietyIdAlfalafaGrass, ctcAlfalfa, alfalfaInsuredAcres2, seedingDate); //Alfalfa Grass

		// Planting 3 - Forage - Crown Native
		planting = createPlanting(field1, 3, cropYear1);
		createInventorySeededForage(planting, cropIdForage, varietyIdCrownNative, ctcPasture, pastureInsuredAcres, seedingDate); //Crown Native

		// Planting 4 - Silage Corn 1
		planting = createPlanting(field1, 4, cropYear1);
		createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, silageCornInsuredAcres1, null);

		// Planting 5 - Silage Corn 2
		planting = createPlanting(field1, 5, cropYear1);
		createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, silageCornInsuredAcres2, null);
		
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertEquals(1, fetchedInvContract.getFields().size());
		
		AnnualFieldRsrc fetchedField1 = getField(fieldId, fetchedInvContract.getFields());
		
		Assert.assertNotNull(fetchedField1.getPlantings());
		Assert.assertEquals(5, fetchedField1.getPlantings().size());
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
	
	private InventoryField createPlanting(AnnualFieldRsrc field, Integer plantingNumber, Integer cropYear, Boolean isHiddenOnPrintoutInd, Integer insurancePlanId) {
		
		InventoryUnseeded iu = new InventoryUnseeded();
		iu.setAcresToBeSeeded(null);
		iu.setCropCommodityId(null);
		iu.setIsUnseededInsurableInd(false);
		
		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(insurancePlanId);
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
		planting.setInventoryUnseeded(iu);
		
		field.getPlantings().add(planting);

		return planting;
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
	
	private List<DopYieldContractCommodityForage> dopYieldContractCommodityList;
	private Double barleyNonPedigreeSoldYield = 66.77;
	private Double barleyNonPedigreeStoredYield = 88.99;
	private Double barleyPedigreeSoldYield = 67.8;
	private Double barleyPedigreeStoredYield = 100.5;
	private Double barleyNonPedigreeHarvestedYieldOverride = 120.0;
	
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
		enterDopCommodityTotals(resource, ctcAlfalfa, alfalfaQuantityHarestedAcres, alfalfaBales, alfalfaWeight);
		enterDopCommodityTotals(resource, ctcPasture, pastureQuantityHarestedAcres, pastureBales, pastureWeight);
		enterDopCommodityTotals(resource, ctcSilageCorn, silageCornQuantityHarestedAcres, silageCornBales, silageCornWeight);
		
		DopYieldContractRsrc createdContract = service.createDopYieldContract(topLevelEndpoints, resource);
		
		dopYieldContractCommodityList = createdContract.getDopYieldContractCommodityForageList();
		
		addExpectedRollupDopCommodities(createdContract);

	}

	private void addExpectedRollupDopCommodities(DopYieldContractRsrc resource) {
		//Add expected rollup dop items
		//FORAGE
		Double forageInsuredAcres = alfalfaInsuredAcres1 + alfalfaInsuredAcres2 + pastureInsuredAcres;
		Double forageHarvestedAcres = alfalfaQuantityHarestedAcres + pastureQuantityHarestedAcres;
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
		
		//SILAGECORN
		Double silageCornInsuredAcres = silageCornInsuredAcres1 + silageCornInsuredAcres2;
		//Double silageCornHarvestedAcres = alfalfaQuantityHarestedAcres + pastureQuantityHarestedAcres;
		DopYieldContractCommodityForage dopSilageCorn = getDopYieldContractCommodityForage(cropIdSilageCorn, ctcSilageCorn, resource.getDopYieldContractCommodityForageList());
		Double silageCornQuantityHarvestedTons = dopSilageCorn.getQuantityHarvestedTons();

		DopYieldContractCommodityForage silageCornDccf = createDopYieldContractCommodityForage(
			null,								//commodityTypeCode, 
			silageCornInsuredAcres, 			//totalFieldAcres, 
			silageCornQuantityHarestedAcres,	//harvestedAcres, 
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
	
//	DopYieldContractCommodity createDopYieldContractCommodity(Integer cropCommodityId, String cropCommodityName, Double harvestedAcres, Boolean isPedigreeInd, Double soldYield, Double storedYield) {
//		
//		DopYieldContractCommodity dycc = new DopYieldContractCommodity();
//
//		dycc.setCropCommodityId(cropCommodityId);
//		dycc.setCropCommodityName(cropCommodityName);
//		dycc.setDeclaredYieldContractCommodityGuid(null);
//		dycc.setDeclaredYieldContractGuid(null);
//		dycc.setGradeModifierTypeCode(null);
//		dycc.setHarvestedAcres(harvestedAcres);
//		dycc.setIsPedigreeInd(isPedigreeInd);
//		dycc.setSoldYield(soldYield);
//		dycc.setSoldYieldDefaultUnit(soldYield);
//		dycc.setStoredYield(storedYield);
//		dycc.setStoredYieldDefaultUnit(storedYield);
//		
//		return dycc;
//	}

	VerifiedYieldAmendment createVerifiedYieldAmendment(
			String verifiedYieldAmendmentCode, 
			Integer cropCommodityId, 
			String cropCommodityName, 
			Boolean isPedigreeInd, 
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
		vya.setFieldId(fieldId);
		vya.setFieldLabel(fieldLabel);
		vya.setIsPedigreeInd(isPedigreeInd);
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
