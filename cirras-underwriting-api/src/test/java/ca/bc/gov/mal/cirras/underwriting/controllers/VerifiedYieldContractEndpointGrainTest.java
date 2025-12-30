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
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ProductRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.VerifiedYieldContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiableCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldGrainBasket;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldSummary;
import ca.bc.gov.mal.cirras.underwriting.services.utils.CommodityCoverageCode;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.services.utils.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class VerifiedYieldContractEndpointGrainTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(VerifiedYieldContractEndpointGrainTest.class);


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
	private Integer growerContractYearId1 = 999999999;
	
	private Integer legalLandId = 90000015;
	private Integer fieldId = 90000016;
	private Integer annualFieldDetailId1 = 90000017;
	private Integer contractedFieldDetailId1 = 90000018;
	
	private Integer productId1 = 99999999;
	private Integer productId2 = 88889999;
	private Integer productId3 = 77777777;
	private Integer productId4 = 66666666;


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
		
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId1.toString());

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
	public void testInsertUpdateDeleteGrainVerifiedYield() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testInsertUpdateDeleteGrainVerifiedYield");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		createGrower(growerId, 999888, "grower name", createTransactionDate);
		
		createLegalLand("test legal 9999", null, legalLandId, "999-888-000", 1980, null);
		createField(fieldId, "LOT 3", 1980, null);

		createGrowerContractYear(growerContractYearId1, contractId, growerId, cropYear1, 4, createTransactionDate);
		createPolicy(policyId1, growerId, 4, policyNumber1, contractNumber, contractId, cropYear1, createTransactionDate);
		createAnnualFieldDetail(annualFieldDetailId1, legalLandId, fieldId, cropYear1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);

		createInventoryContract(policyNumber1, 4, false);
		createDopYieldContract(policyNumber1, 4, false);
		
		//Barley - NON Pedigree - Product
		createUpdateProduct(policyId1, productId1, 16, 20, barleyNonPedigreePY, barleyNonPediProductionGuarantee, barleyNonPedigreeIV100, barleyNonPedigreeCoverage, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
		//Barley - Pedigree
		createUpdateProduct(policyId1, productId3, 17, 50, barleyPedigreePY, barleyPedigreeProductionGuarantee, barleyPedigreeIV100, barleyPedigreeCoverage, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
		
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
		Assert.assertEquals("TONNE", newContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(growerContractYearId1, newContract.getGrowerContractYearId());
		Assert.assertEquals(Integer.valueOf(4), newContract.getInsurancePlanId());
		Assert.assertNull(newContract.getVerifiedYieldContractGuid());
		Assert.assertNull(newContract.getVerifiedYieldUpdateUser());
		Assert.assertNull(newContract.getVerifiedYieldUpdateTimestamp());

		// Check VerifiedYieldContractCommodities
		Assert.assertEquals(3, newContract.getVerifiedYieldContractCommodities().size());

		// Check Rollover
		//Barley - NON Pedigree
		checkVerifiedContractCommodityRollover(newContract.getVerifiedYieldContractCommodities(), 16, false, barleyNonPediProductionGuarantee, barleyNonPedigreeSeededAcres); 
		//Canola - Pedigree
		checkVerifiedContractCommodityRollover(newContract.getVerifiedYieldContractCommodities(), 18, true, null, null); 
		//Barley - Pedigree
		checkVerifiedContractCommodityRollover(newContract.getVerifiedYieldContractCommodities(), 16, true, barleyPedigreeProductionGuarantee, null); 
		
		//Check verifiable Commodities and field
		Assert.assertEquals(1, newContract.getFields().size());
		AnnualFieldRsrc field = newContract.getFields().get(0);
		Assert.assertEquals(fieldId, field.getFieldId());
		Assert.assertEquals(2, field.getVerifiableCommodities().size());
		Boolean pedigreed = false;
		Boolean nonPedigreed = false;
		for(VerifiableCommodity vc : field.getVerifiableCommodities()) {
			Assert.assertEquals(16, vc.getCropCommodityId().intValue());
			if(vc.getIsPedigreeInd()) {
				pedigreed = true;
			} else if(vc.getIsPedigreeInd() == false) {
				nonPedigreed = true;
			}
		}
		Assert.assertTrue(pedigreed);
		Assert.assertTrue(nonPedigreed);
		
		//Create verified contract ******************************************************************************
		//Add override values for barley non pedigree
		VerifiedYieldContractCommodity barleyCommodity = getVerifiedYieldContractCommodity(16, false, newContract.getVerifiedYieldContractCommodities());
		Assert.assertNotNull(barleyCommodity);
		barleyCommodity.setHarvestedAcresOverride(22.0);
		barleyCommodity.setHarvestedYieldOverride(barleyNonPedigreeHarvestedYieldOverride);
		
		//Create product for Canola - Pedigree
		createUpdateProduct(policyId1, productId2, 19, 10, canolaPedigreePY, canolaPedigreeProductionGuarantee, canolaPedigreeIV100, null, "Open", CommodityCoverageCode.QUANTITY_GRAIN);

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
		
		Assert.assertNull(createdContract.getVerifiedYieldGrainBasket()); //No grain basket expected

		//Even though there is a product for canola now, there is no warning because purchase is not in status FINAL
		Assert.assertEquals(0, createdContract.getProductWarningMessages().size());

		// Check Contract Commodities
		//Barley - NON Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), 16, false, barleyNonPediProductionGuarantee, barleyNonPedigreeSeededAcres); 
		//Canola - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), 18, true, null, null); 
		//Barley - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), 16, true, barleyPedigreeProductionGuarantee, null); 

		//Delete Barley non Pedigree Product - Expect warning
		service.deleteProduct(topLevelEndpoints, productId1.toString());
		//Update Barley Pedigree Product Guarantee - Expect warning
		Double barleyPedProdGuaranteeNew = barleyPedigreeProductionGuarantee + 20;
		Double barleyPedigreePYNew = barleyPedigreePY + 10;
		Double barleyPedigreeIV100New = barleyPedigreeIV100 + 10;
		
		createUpdateProduct(policyId1, productId3, 17, 50, barleyPedigreePYNew, barleyPedProdGuaranteeNew, barleyPedigreeIV100New, barleyPedigreeCoverage, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
		
		//Create product for Grain Basket - No warning expected
		createUpdateProduct(policyId1, productId4, 1010076, 20, null, null, null, grainBasketCoverageDollar, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.GRAIN_BASKET);

		//Create product for Canola - Pedigree
		createUpdateProduct(policyId1, productId2, 19, 10, canolaPedigreePY, canolaPedigreeProductionGuarantee, canolaPedigreeIV100, canolaPedigreeCoverage, "Open", CommodityCoverageCode.QUANTITY_GRAIN);

		
		//Get contract again to prevent precondition fails
		createdContract = getVerifiedYieldContract(policyNumber1);
		Assert.assertNotNull(createdContract);
		
		//Update verified contract ******************************************************************************
		//Remove override values for barley non pedigree
		barleyCommodity = getVerifiedYieldContractCommodity(16, false, createdContract.getVerifiedYieldContractCommodities());
		Assert.assertNotNull(barleyCommodity);
		barleyCommodity.setHarvestedAcresOverride(null);
		barleyCommodity.setHarvestedYieldOverride(null);
		
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
		
		Assert.assertNotNull(updatedContract.getVerifiedYieldGrainBasket()); //grain basket expected because there is a product

		
		//4 warnings expected (2 Production Guarantee, 2 PY, 2 100% IV)
		Assert.assertEquals(6, updatedContract.getProductWarningMessages().size());
		
		// Check Contract Commodities
		//Barley - NON Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 16, false, barleyNonPediProductionGuarantee, barleyNonPedigreeSeededAcres); 
		//Canola - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 18, true, null, null); 
		//Barley - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 16, true, barleyPedigreeProductionGuarantee, null); 
		
		//UPDATE product values
		updatedContract.setUpdateProductValuesInd(true);
		
		updatedContract = service.updateVerifiedYieldContract(updatedContract);
		Assert.assertNotNull(updatedContract);
		
		//0 warnings expected
		Assert.assertEquals(0, updatedContract.getProductWarningMessages().size());

		// Check Contract Commodities
		//Barley - NON Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 16, false, null, barleyNonPedigreeSeededAcres); 
		//Canola - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 18, true, null, null); 
		//Barley - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 16, true, barleyPedProdGuaranteeNew, null); 

		//Update Barley Pedigree Product Guarantee and PY to NULL - Expect warning
		createUpdateProduct(policyId1, productId3, 17, 50, 20.5, null, null, barleyPedigreeCoverage, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
		//Update Canola Pedigree to status final
		//createUpdateProduct(policyId1, productId2, 19, 10, canolaPedigreePY, canolaPedigreeProductionGuarantee, null, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
		createUpdateProduct(policyId1, productId2, 19, 10, canolaPedigreePY, canolaPedigreeProductionGuarantee, canolaPedigreeIV100, canolaPedigreeCoverage, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
		//Update product for Grain Basket
		Double grainBasketCoverageDollarNew = grainBasketCoverageDollar + 10;
		createUpdateProduct(policyId1, productId4, 1010076, 20, null, null, null, grainBasketCoverageDollarNew, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.GRAIN_BASKET);

		//Get contract
		VerifiedYieldContractRsrc vyContract = getVerifiedYieldContract(policyNumber1);
		Assert.assertNotNull(vyContract);

		//8 warnings expected (2 Production Guarantee, 2 PY, 2 100% IV, 2 Grain Basket)
		Assert.assertEquals(8, vyContract.getProductWarningMessages().size());

		//UPDATE product values
		vyContract.setUpdateProductValuesInd(true);

		vyContract = service.updateVerifiedYieldContract(vyContract);
		Assert.assertNotNull(vyContract);

		//0 warnings expected
		Assert.assertEquals(0, vyContract.getProductWarningMessages().size());
		
		// Check Contract Commodities
		//Barley - NON Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, vyContract.getVerifiedYieldContractCommodities(), 16, false, null, barleyNonPedigreeSeededAcres); 
		//Canola - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, vyContract.getVerifiedYieldContractCommodities(), 18, true, canolaPedigreeProductionGuarantee, null); 
		//Barley - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, vyContract.getVerifiedYieldContractCommodities(), 16, true, null, null); 

		
		// Grain Basket additional warning tests *******************************************************************
		createUpdateProduct(policyId1, productId2, 19, 10, canolaPedigreePY, canolaPedigreeProductionGuarantee, canolaPedigreeIV100, canolaPedigreeCoverage + 10, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);

		//Get contract
		vyContract = getVerifiedYieldContract(policyNumber1);
		Assert.assertNotNull(vyContract);

		//1 warnings expected (1 Grain Basket)
		Assert.assertEquals(1, vyContract.getProductWarningMessages().size());
		
		createUpdateProduct(policyId1, productId2, 19, 10, canolaPedigreePY, canolaPedigreeProductionGuarantee, canolaPedigreeIV100, canolaPedigreeCoverage, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);

		vyContract = getVerifiedYieldContract(policyNumber1);
		Assert.assertNotNull(vyContract);

		//0 warnings expected
		Assert.assertEquals(0, vyContract.getProductWarningMessages().size());
		
		//Delete grain basket product
		service.deleteProduct(topLevelEndpoints, productId4.toString());		
		
		vyContract = getVerifiedYieldContract(policyNumber1);
		
		//DON'T update product values
		vyContract.setUpdateProductValuesInd(false);
		
		vyContract = service.updateVerifiedYieldContract(vyContract);
		Assert.assertNotNull(vyContract);

		Assert.assertNotNull(vyContract.getVerifiedYieldGrainBasket()); //grain basket expected because user didn't want to update

		//1 warning expected - Grain Basket
		Assert.assertEquals(1, vyContract.getProductWarningMessages().size());

		//UPDATE product values
		vyContract.setUpdateProductValuesInd(true);
		
		vyContract = service.updateVerifiedYieldContract(vyContract);
		Assert.assertNotNull(vyContract);

		//0 warnings expected
		Assert.assertEquals(0, vyContract.getProductWarningMessages().size());
		
		Assert.assertNull(vyContract.getVerifiedYieldGrainBasket()); //grain basket is deleted because user want to update

		//Create product for Grain Basket - No warning expected
		createUpdateProduct(policyId1, productId4, 1010076, 20, null, null, null, grainBasketCoverageDollar, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.GRAIN_BASKET);

		vyContract = getVerifiedYieldContract(policyNumber1);

		Assert.assertNull(vyContract.getVerifiedYieldGrainBasket()); //grain basket not expected

		//1 warning expected - Grain Basket
		Assert.assertEquals(1, vyContract.getProductWarningMessages().size());
		
		// END Grain Basket additional warning tests ***********************************************************

		
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
		
		logger.debug(">testInsertUpdateDeleteGrainVerifiedYield");
	}

	@Test
	public void testInsertUpdateDeleteGrainVerifiedYieldAmendments() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testInsertUpdateDeleteGrainVerifiedYieldAmendments");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		createGrower(growerId, 999888, "grower name", createTransactionDate);
		
		createLegalLand("test legal 9999", null, legalLandId, "999-888-000", 1980, null);
		createField(fieldId, "LOT 3", 1980, null);

		createGrowerContractYear(growerContractYearId1, contractId, growerId, cropYear1, 4, createTransactionDate);
		createPolicy(policyId1, growerId, 4, policyNumber1, contractNumber, contractId, cropYear1, createTransactionDate);
		createAnnualFieldDetail(annualFieldDetailId1, legalLandId, fieldId, cropYear1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);

		createInventoryContract(policyNumber1, 4, false);
		createDopYieldContract(policyNumber1, 4, false);
				
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
		Assert.assertEquals("TONNE", newContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(growerContractYearId1, newContract.getGrowerContractYearId());
		Assert.assertEquals(Integer.valueOf(4), newContract.getInsurancePlanId());
		Assert.assertNull(newContract.getVerifiedYieldContractGuid());
		Assert.assertNull(newContract.getVerifiedYieldUpdateUser());
		Assert.assertNull(newContract.getVerifiedYieldUpdateTimestamp());

		// Check VerifiedYieldAmendments
		Assert.assertEquals(0, newContract.getVerifiedYieldAmendments().size());
		
		
		//Create verified contract ******************************************************************************

		VerifiedYieldAmendment vya = createVerifiedYieldAmendment("Appraisal", 18, "CANOLA", false, null, null, 12.34, 56.78, "rationale 1");
		newContract.getVerifiedYieldAmendments().add(vya);
		
		vya = createVerifiedYieldAmendment("Assessment", 16, "BARLEY", true, fieldId, "LOT 3", 11.22, 33.44, "rationale 2");
		newContract.getVerifiedYieldAmendments().add(vya);

		vya = createVerifiedYieldAmendment("Assessment", 18, "CANOLA", true, null, null, 22.11, 44.33, "rationale 3");
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
		
		vya = createdContract.getVerifiedYieldAmendments().get(0);
		vya.setAcres(55.66);
		vya.setCropCommodityId(16);
		vya.setCropCommodityName("BARLEY");
		vya.setFieldId(fieldId);
		vya.setFieldLabel("LOT 3");
		vya.setIsPedigreeInd(true);
		vya.setRationale("rationale A");
		vya.setVerifiedYieldAmendmentCode("Assessment");
		vya.setYieldPerAcre(77.88);

		expectedAmendments.add(vya);
		
		vya = createdContract.getVerifiedYieldAmendments().get(1);
		vya.setAcres(98.76);
		vya.setCropCommodityId(18);
		vya.setCropCommodityName("CANOLA");
		vya.setFieldId(null);
		vya.setFieldLabel(null);
		vya.setIsPedigreeInd(false);
		vya.setRationale("rationale B");
		vya.setVerifiedYieldAmendmentCode("Appraisal");
		vya.setYieldPerAcre(54.32);
		
		// The order in which amendments will be returned is reversed by these updates.
		expectedAmendments.add(0, vya);

		vya = createdContract.getVerifiedYieldAmendments().get(2);
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
		
		logger.debug(">testInsertUpdateDeleteGrainVerifiedYieldAmendments");
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
	public void testGrainVerifiedYieldSummary() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testGrainVerifiedYieldSummary");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 because they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		createGrower(growerId, 999888, "grower name", createTransactionDate);
		
		createLegalLand("test legal 9999", null, legalLandId, "999-888-000", 1980, null);
		createField(fieldId, "LOT 3", 1980, null);

		createGrowerContractYear(growerContractYearId1, contractId, growerId, cropYear1, 4, createTransactionDate);
		createPolicy(policyId1, growerId, 4, policyNumber1, contractNumber, contractId, cropYear1, createTransactionDate);
		createAnnualFieldDetail(annualFieldDetailId1, legalLandId, fieldId, cropYear1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);

		createInventoryContract(policyNumber1, 4, true);
		createDopYieldContract(policyNumber1, 4, true);
		
		//Barley - NON Pedigree - Product
		createUpdateProduct(policyId1, productId1, 16, 20, barleyNonPedigreePY, barleyNonPediProductionGuarantee, barleyNonPedigreeIV100, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
		//Barley - Pedigree
		createUpdateProduct(policyId1, productId3, 17, 50, barleyPedigreePY, barleyPedigreeProductionGuarantee, barleyPedigreeIV100, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
		
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
		Assert.assertEquals("TONNE", newContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(growerContractYearId1, newContract.getGrowerContractYearId());
		Assert.assertEquals(Integer.valueOf(4), newContract.getInsurancePlanId());

		// Check VerifiedYieldContractCommodities
		Assert.assertEquals(2, newContract.getVerifiedYieldContractCommodities().size());
		
		// Check VerifiedYieldAmendments
		Assert.assertEquals(0, newContract.getVerifiedYieldAmendments().size());

		
		//Create verified contract ******************************************************************************
		//Add override values for barley non pedigree
		VerifiedYieldContractCommodity barleyCommodity = getVerifiedYieldContractCommodity(16, false, newContract.getVerifiedYieldContractCommodities());
		Assert.assertNotNull(barleyCommodity);
		barleyCommodity.setHarvestedAcresOverride(22.0);
		barleyCommodity.setHarvestedYieldOverride(barleyNonPedigreeHarvestedYieldOverride);
		
		//Create product for Canola - Pedigree
		createUpdateProduct(policyId1, productId2, 19, 10, 50.5, null, canolaPedigreePY, canolaPedigreeProductionGuarantee, "Open", CommodityCoverageCode.QUANTITY_GRAIN);
				
		//Create Amendments
		VerifiedYieldAmendment vya = createVerifiedYieldAmendment(appraisal, 24, "OAT", false, null, null, oatAppraisalAcres1, oatAppraisalYield1, "rationale 1");
		newContract.getVerifiedYieldAmendments().add(vya);
		
		vya = createVerifiedYieldAmendment(appraisal, 16, "BARLEY", false, fieldId, "LOT 3", barleyNonPedAppraisalAcres1, barleyNonPedAppraisalYield1, "rationale 2");
		newContract.getVerifiedYieldAmendments().add(vya);

		vya = createVerifiedYieldAmendment(appraisal, 16, "BARLEY", false, null, null, barleyNonPedAppraisalAcres2, barleyNonPedAppraisalYield2, "rationale 2");
		newContract.getVerifiedYieldAmendments().add(vya);

		vya = createVerifiedYieldAmendment(assessment, 16, "BARLEY", false, null, null, barleyNonPedAssessmentAcres1, barleyNonPedAssessmentYield1, "rationale 2");
		newContract.getVerifiedYieldAmendments().add(vya);
		
		vya = createVerifiedYieldAmendment(assessment, 16, "BARLEY", true, null, null, barleyPedigreeAssessmentAcres1, barleyPedigreeAssessmentYield1, "rationale 2");
		newContract.getVerifiedYieldAmendments().add(vya);
		
		vya = createVerifiedYieldAmendment(assessment, 16, "BARLEY", true, null, null, barleyPedigreeAssessmentAcres2, barleyPedigreeAssessmentYield2, "rationale 2");
		newContract.getVerifiedYieldAmendments().add(vya);
		
		List<VerifiedYieldSummary> expectedVys = createExpectedVerifiedYieldSummaries(newContract);

		VerifiedYieldContractRsrc createdContract = service.createVerifiedYieldContract(topLevelEndpoints, newContract);
		Assert.assertNotNull(createdContract);

		Assert.assertEquals(newContract.getContractId(), createdContract.getContractId());
		Assert.assertEquals(newContract.getCropYear(), createdContract.getCropYear());
		Assert.assertEquals(newContract.getDeclaredYieldContractGuid(), createdContract.getDeclaredYieldContractGuid());
		Assert.assertEquals(newContract.getDefaultYieldMeasUnitTypeCode(), createdContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(newContract.getGrowerContractYearId(), createdContract.getGrowerContractYearId());
		Assert.assertEquals(newContract.getInsurancePlanId(), createdContract.getInsurancePlanId());
		
		//Expect 3 verified yield summaries
		Assert.assertEquals(3, createdContract.getVerifiedYieldSummaries().size());
		
		//Check Verified Yield Summary
		checkVerifiedYieldSummaries(expectedVys, createdContract.getVerifiedYieldSummaries(), createdContract.getVerifiedYieldContractGuid());

		//Add comments
		List<UnderwritingComment> comments = new ArrayList<UnderwritingComment>();
		//Oat
		VerifiedYieldSummary oatVys = getVerifiedYieldSummary(24, false, createdContract.getVerifiedYieldSummaries());
		Assert.assertNotNull(oatVys);
		comments.add(createUwComment(oatVys.getVerifiedYieldSummaryGuid()));
		oatVys.setUwComments(comments);
		
		//Barley non pedigree - 2 comments
		VerifiedYieldSummary barleyVys = getVerifiedYieldSummary(16, false, createdContract.getVerifiedYieldSummaries());
		Assert.assertNotNull(barleyVys);
		comments = new ArrayList<UnderwritingComment>();
		comments.add(createUwComment(barleyVys.getVerifiedYieldSummaryGuid()));
		comments.add(createUwComment(barleyVys.getVerifiedYieldSummaryGuid()));
		barleyVys.setUwComments(comments);
		
		//Add amendment of another commodity
		vya = createVerifiedYieldAmendment(appraisal, 26, "WHEAT", false, null, null, oatAppraisalAcres1, oatAppraisalYield1, "rationale 1");
		createdContract.getVerifiedYieldAmendments().add(vya);

		VerifiedYieldContractRsrc updatedContract = service.updateVerifiedYieldContract(createdContract);
		Assert.assertNotNull(updatedContract);
		
		//Expect 4 verified yield summaries
		Assert.assertEquals(4, updatedContract.getVerifiedYieldSummaries().size());

		//Check Comments
		oatVys = getVerifiedYieldSummary(24, false, updatedContract.getVerifiedYieldSummaries());
		Assert.assertNotNull(oatVys);
		Assert.assertEquals(1, oatVys.getUwComments().size());

		barleyVys = getVerifiedYieldSummary(16, false, updatedContract.getVerifiedYieldSummaries());
		Assert.assertNotNull(barleyVys);
		Assert.assertEquals(2, barleyVys.getUwComments().size());
		//Remove one barley comment
		barleyVys.getUwComments().get(0).setDeletedByUserInd(true);

		VerifiedYieldSummary wheatVys = getVerifiedYieldSummary(26, false, updatedContract.getVerifiedYieldSummaries());
		Assert.assertNotNull(wheatVys);
		
		//Remove Oat Amendment - Expect summary to be deleted
		VerifiedYieldAmendment oatAmendment = getVerifiedYieldAmendment(24, false, updatedContract.getVerifiedYieldAmendments());
		Assert.assertNotNull(oatAmendment);
		oatAmendment.setDeletedByUserInd(true);
		
		//Remove 1 Barley non pedigree amendment - Expect updated summary
		Boolean barleyAmendmentRemoved = false;
		List<VerifiedYieldAmendment> barleyAmendments = getCommodityAmendments(16, false, updatedContract.getVerifiedYieldAmendments());
		for (VerifiedYieldAmendment barleyAmendment : barleyAmendments) {

			if(barleyAmendment.getVerifiedYieldAmendmentCode().equalsIgnoreCase(appraisal) &&
					barleyAmendment.getAcres().equals(barleyNonPedAppraisalAcres2) &&
					barleyAmendment.getYieldPerAcre().equals(barleyNonPedAppraisalYield2)) {

				barleyAmendment.setDeletedByUserInd(true);
				barleyAmendmentRemoved = true;
			}
		}
		Assert.assertTrue(barleyAmendmentRemoved);
		
		//Update expected summary values
		VerifiedYieldSummary expBarleyVys = getVerifiedYieldSummary(16, false, expectedVys);
		Assert.assertNotNull(expBarleyVys);
		Double appraisedYield = (barleyNonPedAppraisalYield1 * barleyNonPedAppraisalAcres1);
		expBarleyVys.setAppraisedYield(appraisedYield);
		Double yieldToCount = expBarleyVys.getHarvestedYield() + expBarleyVys.getAppraisedYield();
		expBarleyVys.setYieldToCount(yieldToCount);
		Double yieldPercentPy = expBarleyVys.getYieldToCount()/ (barleyNonPedigreeSeededAcres * barleyNonPedigreePY);
		expBarleyVys.setYieldPercentPy(yieldPercentPy);
		Double productionAcres = expBarleyVys.getProductionAcres() - barleyNonPedAppraisalAcres2;
		expBarleyVys.setProductionAcres(productionAcres);
		
		updatedContract = service.updateVerifiedYieldContract(updatedContract);
		Assert.assertNotNull(updatedContract);
		
		//Expect 3 verified yield summaries - Oat should be deleted
		Assert.assertEquals(3, updatedContract.getVerifiedYieldSummaries().size());

		barleyVys = getVerifiedYieldSummary(16, false, updatedContract.getVerifiedYieldSummaries());
		Assert.assertNotNull(barleyVys);
		//One comment should be deleted
		Assert.assertEquals(1, barleyVys.getUwComments().size());

		checkVerifiedYieldSummary(expBarleyVys, barleyVys, updatedContract.getVerifiedYieldContractGuid());				


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
		
		logger.debug(">testGrainVerifiedYieldSummary");
	}	

	@Test
	public void testGrainBasket() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testGrainBasket");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		createGrower(growerId, 999888, "grower name", createTransactionDate);
		
		createLegalLand("test legal 9999", null, legalLandId, "999-888-000", 1980, null);
		createField(fieldId, "LOT 3", 1980, null);

		createGrowerContractYear(growerContractYearId1, contractId, growerId, cropYear1, 4, createTransactionDate);
		createPolicy(policyId1, growerId, 4, policyNumber1, contractNumber, contractId, cropYear1, createTransactionDate);
		createAnnualFieldDetail(annualFieldDetailId1, legalLandId, fieldId, cropYear1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);

		createInventoryContract(policyNumber1, 4, true);
		createDopYieldContract(policyNumber1, 4, true);
		
		//Barley - NON Pedigree - Product
		createUpdateProduct(policyId1, productId1, 16, 20, barleyNonPedigreePY, barleyNonPediProductionGuarantee, barleyNonPedigreeIV100, barleyNonPedigreeCoverage, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
		//Barley - Pedigree
		createUpdateProduct(policyId1, productId3, 17, 50, barleyPedigreePY, barleyPedigreeProductionGuarantee, barleyPedigreeIV100, barleyPedigreeCoverage, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);
		
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
		Assert.assertEquals("TONNE", newContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(growerContractYearId1, newContract.getGrowerContractYearId());
		Assert.assertEquals(Integer.valueOf(4), newContract.getInsurancePlanId());

		// Expect 2 VerifiedYieldContractCommodities
		Assert.assertEquals(2, newContract.getVerifiedYieldContractCommodities().size());
		
		VerifiedYieldContractRsrc createdContract = service.createVerifiedYieldContract(topLevelEndpoints, newContract);
		Assert.assertNotNull(createdContract);

		Assert.assertEquals(newContract.getContractId(), createdContract.getContractId());
		Assert.assertEquals(newContract.getCropYear(), createdContract.getCropYear());
		Assert.assertEquals(newContract.getDeclaredYieldContractGuid(), createdContract.getDeclaredYieldContractGuid());
		Assert.assertEquals(newContract.getDefaultYieldMeasUnitTypeCode(), createdContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(newContract.getGrowerContractYearId(), createdContract.getGrowerContractYearId());
		Assert.assertEquals(newContract.getInsurancePlanId(), createdContract.getInsurancePlanId());
		
		//No grain basket expected because there was no product
		Assert.assertNull(createdContract.getVerifiedYieldGrainBasket());
		
		//Expect 2 verified yield summaries
		Assert.assertEquals(2, createdContract.getVerifiedYieldSummaries().size());

		//Create product for Grain Basket
		createUpdateProduct(policyId1, productId4, 1010076, 20, null, null, null, grainBasketCoverageDollar, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.GRAIN_BASKET);

		//Get contract again to prevent precondition fails
		createdContract = getVerifiedYieldContract(policyNumber1);
		Assert.assertNotNull(createdContract);
		
		VerifiedYieldContractRsrc updatedContract = service.updateVerifiedYieldContract(createdContract);
		Assert.assertNotNull(updatedContract);

		//grain basket expected because there is a product
		Assert.assertNotNull(updatedContract.getVerifiedYieldGrainBasket());

		//Calculate expected harvested value
		Double harvestedValue = calculateHarvestedValue(updatedContract);
		
		VerifiedYieldGrainBasket basket = updatedContract.getVerifiedYieldGrainBasket();

		Double expectedTotalQuantityCoverageValue = barleyNonPedigreeCoverage + barleyPedigreeCoverage;
		Double expectedTotalCoverageValue = grainBasketCoverageDollar + barleyNonPedigreeCoverage + barleyPedigreeCoverage;
		
		Assert.assertNotNull(basket.getVerifiedYieldGrainBasketGuid());
		Assert.assertNotNull(basket.getVerifiedYieldContractGuid());
		Assert.assertEquals(grainBasketCoverageDollar, basket.getBasketValue());
		Assert.assertEquals(expectedTotalQuantityCoverageValue, basket.getTotalQuantityCoverageValue(), 0.00001);
		Assert.assertEquals(expectedTotalCoverageValue, basket.getTotalCoverageValue(), 0.00001);
		Assert.assertEquals(harvestedValue, basket.getHarvestedValue());
		Assert.assertNull(basket.getComment());

		//Update 100% IV which will change the harvested value
		Double barleyPedigreeIV100New = barleyPedigreeIV100 + 10;
		Double barleyPedigreeCoverageNew = barleyPedigreeCoverage + 20;
		createUpdateProduct(policyId1, productId3, 17, 50, barleyPedigreePY, barleyPedigreeProductionGuarantee, barleyPedigreeIV100New, barleyPedigreeCoverageNew, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.QUANTITY_GRAIN);

		//Update grain basket coverage dollar
		Double grainBasketCoverageDollarNew = grainBasketCoverageDollar + 10;
		createUpdateProduct(policyId1, productId4, 1010076, 20, null, null, null, grainBasketCoverageDollarNew, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL, CommodityCoverageCode.GRAIN_BASKET);
		
		//Get contract again to prevent precondition fails
		updatedContract = getVerifiedYieldContract(policyNumber1);
		Assert.assertNotNull(updatedContract);
		
		//Update values from product
		updatedContract.setUpdateProductValuesInd(true);

		updatedContract.getVerifiedYieldGrainBasket().setComment("New Comment");
		
		VerifiedYieldContractRsrc vyContract = service.updateVerifiedYieldContract(updatedContract);
		Assert.assertNotNull(vyContract);

		harvestedValue = calculateHarvestedValue(vyContract);
		
		VerifiedYieldGrainBasket expectedBasket = updatedContract.getVerifiedYieldGrainBasket();
		VerifiedYieldGrainBasket actualBasket = vyContract.getVerifiedYieldGrainBasket();
		
		expectedTotalQuantityCoverageValue = barleyNonPedigreeCoverage + barleyPedigreeCoverageNew;
		expectedTotalCoverageValue = grainBasketCoverageDollarNew + barleyNonPedigreeCoverage + barleyPedigreeCoverageNew;
		Assert.assertEquals(expectedBasket.getVerifiedYieldGrainBasketGuid(), actualBasket.getVerifiedYieldGrainBasketGuid());
		Assert.assertEquals(expectedBasket.getVerifiedYieldContractGuid(), actualBasket.getVerifiedYieldContractGuid());
		Assert.assertEquals(grainBasketCoverageDollarNew, actualBasket.getBasketValue());
		Assert.assertEquals(expectedTotalQuantityCoverageValue, actualBasket.getTotalQuantityCoverageValue(), 0.00001);
		Assert.assertEquals(expectedTotalCoverageValue, actualBasket.getTotalCoverageValue(), 0.00001);
		Assert.assertEquals(harvestedValue, actualBasket.getHarvestedValue());
		Assert.assertEquals(expectedBasket.getComment(), actualBasket.getComment());

		//Delete grain basket product
		service.deleteProduct(topLevelEndpoints, productId4.toString());
		
		//Get contract again to prevent precondition fails
		vyContract = getVerifiedYieldContract(policyNumber1);
		Assert.assertNotNull(vyContract);
		
		//Don't update product values in verified yield
		vyContract.setUpdateProductValuesInd(false);
		vyContract = service.updateVerifiedYieldContract(vyContract);

		Assert.assertNotNull(vyContract);
		//Grain basket still on the contract because user chose to not update verified yield data
		Assert.assertNotNull(vyContract.getVerifiedYieldGrainBasket());
		
		//UPDATE product values in verified yield
		vyContract.setUpdateProductValuesInd(true);
		vyContract = service.updateVerifiedYieldContract(vyContract);

		Assert.assertNotNull(vyContract);
		//Grain basket removed on the contract because user chose to update verified yield data
		Assert.assertNull(vyContract.getVerifiedYieldGrainBasket());

		logger.debug(">testGrainBasket");
	}

	private Double calculateHarvestedValue(VerifiedYieldContractRsrc updatedContract) {
		//Harvested Value = SUM(yield to count * 100% IV)
		Double harvestedValue = 0.0;
		for(VerifiedYieldSummary vys : updatedContract.getVerifiedYieldSummaries()) {
			harvestedValue = harvestedValue + (vys.getYieldToCount() * vys.getInsurableValueHundredPercent());
		}
		return harvestedValue;
	}	
	
	private List<VerifiedYieldSummary> createExpectedVerifiedYieldSummaries(VerifiedYieldContractRsrc vyContract) {
		
		List<VerifiedYieldSummary> expVys = new ArrayList<VerifiedYieldSummary>();
		
		VerifiedYieldSummary vys = new VerifiedYieldSummary();
		
		VerifiedYieldContractCommodity barleyCommodity = getVerifiedYieldContractCommodity(16, false, vyContract.getVerifiedYieldContractCommodities());
		
		//Barley Non Pedigree
		vys.setCropCommodityId(16);
		vys.setCropCommodityName("BARLEY");
		vys.setIsPedigreeInd(false);
		vys.setHarvestedYield(barleyNonPedigreeHarvestedYieldOverride); //Use Override
		Double appraisedYield = (barleyNonPedAppraisalYield1 * barleyNonPedAppraisalAcres1) + (barleyNonPedAppraisalYield2 * barleyNonPedAppraisalAcres2);
		vys.setAppraisedYield(appraisedYield);
		Double assessedYield = (barleyNonPedAssessmentYield1 * barleyNonPedAssessmentAcres1);
		vys.setAssessedYield(assessedYield);
		Double yieldToCount = vys.getHarvestedYield() + vys.getAppraisedYield();
		vys.setYieldToCount(yieldToCount);
		Double yieldPercentPy = vys.getYieldToCount()/ (barleyNonPedigreeSeededAcres * barleyNonPedigreePY);
		vys.setYieldPercentPy(yieldPercentPy);
		vys.setProductionGuarantee(barleyNonPediProductionGuarantee);
		vys.setProbableYield(barleyNonPedigreePY);
		vys.setInsurableValueHundredPercent(barleyNonPedigreeIV100);
		vys.setTotalInsuredAcres(barleyNonPedigreeSeededAcres);
		Double effectiveHarvestedAcres = notNull(notNull(barleyCommodity.getHarvestedAcresOverride(), barleyCommodity.getHarvestedAcres()), 0.0);
		Double productionAcres = effectiveHarvestedAcres + barleyNonPedAppraisalAcres1 + barleyNonPedAppraisalAcres2;
		vys.setProductionAcres(productionAcres);

		expVys.add(vys);

		//Barley Pedigree
		barleyCommodity = getVerifiedYieldContractCommodity(16, true, vyContract.getVerifiedYieldContractCommodities());

		vys = new VerifiedYieldSummary();
		
		vys.setCropCommodityId(16);
		vys.setCropCommodityName("BARLEY");
		vys.setIsPedigreeInd(true);
		vys.setHarvestedYield(barleyPedigreeSoldYield + barleyPedigreeStoredYield);
		appraisedYield = null;
		vys.setAppraisedYield(appraisedYield);
		assessedYield = (barleyPedigreeAssessmentYield1 * barleyPedigreeAssessmentAcres1) + (barleyPedigreeAssessmentYield2 * barleyPedigreeAssessmentAcres2);
		vys.setAssessedYield(assessedYield);
		yieldToCount = vys.getHarvestedYield();
		vys.setYieldToCount(yieldToCount);
		yieldPercentPy = vys.getYieldToCount()/ (barleyPedigreeSeededAcres * barleyPedigreePY);
		vys.setYieldPercentPy(yieldPercentPy);
		vys.setProductionGuarantee(barleyPedigreeProductionGuarantee);
		vys.setProbableYield(barleyPedigreePY );
		vys.setInsurableValueHundredPercent(barleyPedigreeIV100 );
		vys.setTotalInsuredAcres(barleyPedigreeSeededAcres);
		effectiveHarvestedAcres = notNull(notNull(barleyCommodity.getHarvestedAcresOverride(), barleyCommodity.getHarvestedAcres()), 0.0);
		vys.setProductionAcres(effectiveHarvestedAcres);

		
		expVys.add(vys);

		//Oat Non Pedigree
		vys = new VerifiedYieldSummary();
		
		vys.setCropCommodityId(24);
		vys.setCropCommodityName("OAT");
		vys.setIsPedigreeInd(false);
		vys.setHarvestedYield(null);
		appraisedYield = (oatAppraisalYield1 * oatAppraisalAcres1);
		vys.setAppraisedYield(appraisedYield);
		assessedYield = null;
		vys.setAssessedYield(assessedYield);
		yieldToCount = vys.getAppraisedYield();
		vys.setYieldToCount(yieldToCount);
		yieldPercentPy = null;
		vys.setYieldPercentPy(yieldPercentPy);
		vys.setProductionGuarantee(null);
		vys.setProbableYield(null);
		vys.setTotalInsuredAcres(null);
		vys.setProductionAcres(oatAppraisalAcres1);
		
		expVys.add(vys);
		
		return expVys;
	}

	private void checkVerifiedContractCommodityTotals(
			List<VerifiedYieldContractCommodity> expectedCommodities,
			List<VerifiedYieldContractCommodity> actualCommodities,
			Integer cropCommodityId,
			Boolean isPedigreeInd,
			Double productionGuarantee,
			Double insuredAcres) {
		
		VerifiedYieldContractCommodity expectedCommodity = getVerifiedYieldContractCommodity(cropCommodityId, isPedigreeInd, expectedCommodities);
		VerifiedYieldContractCommodity actualCommodity = getVerifiedYieldContractCommodity(cropCommodityId, isPedigreeInd, actualCommodities);

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

	private void checkVerifiedContractCommodityRollover(
			List<VerifiedYieldContractCommodity> verifiedCommodities,
			Integer cropCommodityId,
			Boolean isPedigreeInd,
			Double productionGuarantee,
			Double insuredAcres) {
		
		DopYieldContractCommodity dopCommodity = getDopYieldContractCommodity(cropCommodityId, isPedigreeInd, dopYieldContractCommodityList);
		VerifiedYieldContractCommodity verifiedCommodity = getVerifiedYieldContractCommodity(cropCommodityId, isPedigreeInd, verifiedCommodities);

		//Harvested Yield
		if(dopCommodity.getSoldYieldDefaultUnit() == null && dopCommodity.getStoredYieldDefaultUnit() == null) {
			Assert.assertNull(verifiedCommodity.getHarvestedYield());
		} else {
			Double harvestedYield = notNull(dopCommodity.getSoldYieldDefaultUnit(), 0.0) + notNull(dopCommodity.getStoredYieldDefaultUnit(), 0.0);
			Assert.assertEquals(harvestedYield, verifiedCommodity.getHarvestedYield());
		}

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
		Assert.assertEquals(dopCommodity.getCropCommodityName(), verifiedCommodity.getCropCommodityName());
		Assert.assertEquals(dopCommodity.getHarvestedAcres(), verifiedCommodity.getHarvestedAcres());
		Assert.assertEquals(null, verifiedCommodity.getHarvestedAcresOverride());
		Assert.assertEquals(null, verifiedCommodity.getHarvestedYieldOverride());
		Assert.assertEquals(dopCommodity.getIsPedigreeInd(), verifiedCommodity.getIsPedigreeInd());
		Assert.assertEquals(productionGuarantee, verifiedCommodity.getProductionGuarantee());
		Assert.assertEquals(dopCommodity.getSoldYieldDefaultUnit(), verifiedCommodity.getSoldYieldDefaultUnit());
		Assert.assertEquals(dopCommodity.getStoredYieldDefaultUnit(), verifiedCommodity.getStoredYieldDefaultUnit());
		Assert.assertEquals(insuredAcres, verifiedCommodity.getTotalInsuredAcres());
		Assert.assertEquals(null, verifiedCommodity.getVerifiedYieldContractCommodityGuid());
		Assert.assertEquals(null, verifiedCommodity.getVerifiedYieldContractGuid());
		Assert.assertEquals(yieldPerAcre, verifiedCommodity.getYieldPerAcre(), 0.00005);
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
	
	private DopYieldContractCommodity getDopYieldContractCommodity(Integer cropCommodityId, Boolean isPedigree, List<DopYieldContractCommodity> dyccList) {
		
		DopYieldContractCommodity dycc = null;
		
		List<DopYieldContractCommodity> dyccFiltered = dyccList.stream()
				.filter(x -> x.getCropCommodityId() == cropCommodityId && x.getIsPedigreeInd() == isPedigree )
				.collect(Collectors.toList());
		
		if (dyccFiltered != null) {
			dycc = dyccFiltered.get(0);
		}
		return dycc;
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
	
	private VerifiedYieldContractCommodity getVerifiedYieldContractCommodity(Integer cropCommodityId, Boolean isPedigree, List<VerifiedYieldContractCommodity> vyccList) {
		
		VerifiedYieldContractCommodity vycc = null;
		
		List<VerifiedYieldContractCommodity> vyccFiltered = vyccList.stream()
				.filter(x -> x.getCropCommodityId().equals(cropCommodityId) && x.getIsPedigreeInd().equals(isPedigree) )
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
		Assert.assertEquals("ProductionAcres", expected.getProductionAcres(), actual.getProductionAcres());
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

	private void createGrowerContractYear(Integer gcyId, Integer contractId, Integer growerId, Integer cropYear, Integer insurancePlanId, Date createTransactionDate) throws DaoException, CirrasUnderwritingServiceException, ValidationException { 

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
	
	private Double barleyNonPedigreePY = 50.5;
	private Double barleyNonPediProductionGuarantee = 222.5;
	private Double barleyNonPedigreeIV100 = 76.31;
	private Double barleyNonPedigreeCoverage = 111.22;
	private Double barleyPedigreePY = 20.1;
	private Double barleyPedigreeProductionGuarantee = 15.1;
	private Double barleyPedigreeIV100 = 56.21;
	private Double barleyPedigreeCoverage = 333.44;
	private Double canolaPedigreePY = 80.0;
	private Double canolaPedigreeProductionGuarantee = 77.0;
	private Double canolaPedigreeIV100 = 68.0;
	private Double canolaPedigreeCoverage = 555.66;
	private Double grainBasketCoverageDollar = 5000.0;
	
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
	
	private void createInventoryContract(
			String policyNumber, 
			Integer insurancePlanId,
			Boolean addSecondCommodity) throws ValidationException, CirrasUnderwritingServiceException {

		boolean addedSeededGrain = false;
		boolean addedSeededForage = false;
		
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
					
					
					List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
					seededGrains.add(createInventorySeededGrain(16, "BARLEY", false, barleyNonPedigreeSeededAcres));

					planting.setInventorySeededGrains(seededGrains);
					
					addedSeededGrain = true;
				}
			}
			
			if(addedSeededGrain) {
				//Add additional plantings for verifiable commodity tests
				 
				InventoryField newPlanting = createPlanting(field, 2, cropYear1, false, insurancePlanId);
				List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
				seededGrains.add(createInventorySeededGrain(16, "BARLEY", true, barleyPedigreeSeededAcres));

				newPlanting.setInventorySeededGrains(seededGrains);
				
			}
		}
		
		
		if (insurancePlanId.equals(InsurancePlans.GRAIN.getInsurancePlanId())) {
			InventoryContractCommodity icc = createInventoryContractCommodity(16, "BARLEY", false, barleyNonPedigreeSeededAcres, barleyNonPedigreeSeededAcres, 0.0);
			resource.getCommodities().add(icc);

			if(addSecondCommodity) {
				icc = createInventoryContractCommodity(16, "BARLEY", true, barleyPedigreeSeededAcres, barleyPedigreeSeededAcres, 0.0);
				resource.getCommodities().add(icc);
			}
		}

		service.createInventoryContract(topLevelEndpoints, resource);

		if ( insurancePlanId.equals(InsurancePlans.GRAIN.getInsurancePlanId()) ) {
			Assert.assertTrue(addedSeededGrain);
		} else if ( insurancePlanId.equals(InsurancePlans.FORAGE.getInsurancePlanId()) ) {
			Assert.assertTrue(addedSeededForage);
		}
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
	
	private List<DopYieldContractCommodity> dopYieldContractCommodityList;
	private Double barleyNonPedigreeSoldYield = 66.77;
	private Double barleyNonPedigreeStoredYield = 88.99;
	private Double barleyPedigreeSoldYield = 67.8;
	private Double barleyPedigreeStoredYield = 100.5;
	private Double barleyNonPedigreeHarvestedYieldOverride = 120.0;
	
	private void createDopYieldContract(
			String policyNumber, 
			Integer insurancePlanId,
			Boolean isVerifiedYieldSummaryTest) throws ValidationException, CirrasUnderwritingServiceException {

		boolean addedYieldGrain = false;
		boolean addedYieldForage = false;
		
		
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
		
		if ( insurancePlanId.equals(InsurancePlans.GRAIN.getInsurancePlanId() ) ) { 		
			resource.setDefaultYieldMeasUnitTypeCode("TONNE");
			resource.setEnteredYieldMeasUnitTypeCode("TONNE");
			resource.setGrainFromOtherSourceInd(true);

			resource.getDopYieldContractCommodities().clear();
			dopYieldContractCommodityList = new ArrayList<DopYieldContractCommodity>();

			DopYieldContractCommodity dycc = createDopYieldContractCommodity(16, "BARLEY", 11.22, false, barleyNonPedigreeSoldYield, barleyNonPedigreeStoredYield);
			resource.getDopYieldContractCommodities().add(dycc);
			
			if(!isVerifiedYieldSummaryTest) {
				dycc = createDopYieldContractCommodity(18, "CANOLA", 33.44, true, 55.66, 77.88);
				resource.getDopYieldContractCommodities().add(dycc);
			}

			dycc = createDopYieldContractCommodity(16, "BARLEY", 11.22, true, barleyPedigreeSoldYield, barleyPedigreeStoredYield);
			resource.getDopYieldContractCommodities().add(dycc);
			
			dopYieldContractCommodityList = resource.getDopYieldContractCommodities();
			
		} else if ( insurancePlanId.equals(InsurancePlans.FORAGE.getInsurancePlanId() ) ) {
			resource.setDefaultYieldMeasUnitTypeCode("TON");
			resource.setEnteredYieldMeasUnitTypeCode("TON");
			resource.setGrainFromOtherSourceInd(false);
		}

		// Create field-level data by copying from seeded grain data.
		for ( AnnualFieldRsrc field : resource.getFields() ) {
			if ( field.getDopYieldFieldGrainList() != null && insurancePlanId.equals(InsurancePlans.GRAIN.getInsurancePlanId())) {
				for ( DopYieldFieldGrain yield : field.getDopYieldFieldGrainList() ) {
					yield.setEstimatedYieldPerAcre(yield.getSeededAcres());
					yield.setUnharvestedAcresInd(false);
					
					if ( yield.getEstimatedYieldPerAcre() > 0 ) {
						addedYieldGrain = true;
					}
				}
			} else if ( field.getDopYieldFieldForageList() != null && insurancePlanId.equals(InsurancePlans.FORAGE.getInsurancePlanId())) {
				for ( DopYieldFieldForage yield : field.getDopYieldFieldForageList() ) {

					List<DopYieldFieldForageCut> cutList = new ArrayList<DopYieldFieldForageCut>();
					DopYieldFieldForageCut cut = new DopYieldFieldForageCut();
					cut.setCutNumber(1);
					cut.setDeclaredYieldFieldForageGuid(null);
					cut.setInventoryFieldGuid(yield.getInventoryFieldGuid());
					cut.setMoisturePercent(0.15);
					cut.setTotalBalesLoads(10);
					cut.setWeight(20.0);
					cut.setDeletedByUserInd(false);
					
					cutList.add(cut);
					yield.setDopYieldFieldForageCuts(cutList);
					
					addedYieldForage = true;

				}
			}
		}

		service.createDopYieldContract(topLevelEndpoints, resource);

		if ( insurancePlanId.equals(InsurancePlans.GRAIN.getInsurancePlanId()) ) {
			Assert.assertTrue(addedYieldGrain);
		} else if ( insurancePlanId.equals(InsurancePlans.FORAGE.getInsurancePlanId()) ) {
			Assert.assertTrue(addedYieldForage);
		}
	}

	DopYieldContractCommodity createDopYieldContractCommodity(Integer cropCommodityId, String cropCommodityName, Double harvestedAcres, Boolean isPedigreeInd, Double soldYield, Double storedYield) {
		
		DopYieldContractCommodity dycc = new DopYieldContractCommodity();

		dycc.setCropCommodityId(cropCommodityId);
		dycc.setCropCommodityName(cropCommodityName);
		dycc.setDeclaredYieldContractCommodityGuid(null);
		dycc.setDeclaredYieldContractGuid(null);
		dycc.setGradeModifierTypeCode(null);
		dycc.setHarvestedAcres(harvestedAcres);
		dycc.setIsPedigreeInd(isPedigreeInd);
		dycc.setSoldYield(soldYield);
		dycc.setSoldYieldDefaultUnit(soldYield);
		dycc.setStoredYield(storedYield);
		dycc.setStoredYieldDefaultUnit(storedYield);
		
		return dycc;
	}

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
