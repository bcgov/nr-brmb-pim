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
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiableCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InsurancePlans;
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

		createInventoryContract(policyNumber1, 4);
		createDopYieldContract(policyNumber1, 4);
		
		//Barley - NON Pedigree - Product
		createUpdateProduct(policyId1, productId1, 16, 20, 15.5, barleyNonPediProductionGuarantee, null, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL);
		//Barley - Pedigree
		createUpdateProduct(policyId1, productId3, 17, 50, 20.5, barleyPedigreeProductionGuarantee, null, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL);
		
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
		checkVerifiedContractCommodityRollover(newContract.getVerifiedYieldContractCommodities(), 16, false, barleyNonPediProductionGuarantee, 23.45); 
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
		barleyCommodity.setHarvestedYieldOverride(120.0);
		
		//Create product for Canola - Pedigree
		createUpdateProduct(policyId1, productId2, 19, 10, 50.5, null, null, canolaPedigreeProductionGuarantee, "Open");
		
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

		//Even though there is a product for canola now, there is no warning because purchase is not in status FINAL
		Assert.assertEquals(0, createdContract.getProductWarningMessages().size());

		// Check Contract Commodities
		//Barley - NON Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), 16, false, barleyNonPediProductionGuarantee, 23.45); 
		//Canola - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), 18, true, null, null); 
		//Barley - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, createdContract.getVerifiedYieldContractCommodities(), 16, true, barleyPedigreeProductionGuarantee, null); 

		//Delete Barley non Pedigree Product - Expect warning
		service.deleteProduct(topLevelEndpoints, productId1.toString());
		//Update Barley Pedigree Product Guarantee - Expect warning
		Double barleyPedProdGuaranteeNew = barleyPedigreeProductionGuarantee + 20;
		createUpdateProduct(policyId1, productId3, 17, 50, 20.5, barleyPedProdGuaranteeNew, null, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL);

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
		
		//2 warnings expected
		Assert.assertEquals(2, updatedContract.getProductWarningMessages().size());
		
		// Check Contract Commodities
		//Barley - NON Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 16, false, barleyNonPediProductionGuarantee, 23.45); 
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
		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 16, false, null, 23.45); 
		//Canola - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 18, true, null, null); 
		//Barley - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, updatedContract.getVerifiedYieldContractCommodities(), 16, true, barleyPedProdGuaranteeNew, null); 

		//Update Barley Pedigree Product Guarantee to NULL - Expect warning
		createUpdateProduct(policyId1, productId3, 17, 50, 20.5, null, null, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL);
		//Update Canola Pedigree to status final
		createUpdateProduct(policyId1, productId2, 19, 10, 50.5, canolaPedigreeProductionGuarantee, null, null, VerifiedYieldContractRsrcFactory.PRODUCT_STATUS_FINAL);
		
		//Get contract
		VerifiedYieldContractRsrc vyContract = getVerifiedYieldContract(policyNumber1);
		Assert.assertNotNull(vyContract);

		//2 warnings expected
		Assert.assertEquals(2, vyContract.getProductWarningMessages().size());

		//UPDATE product values
		vyContract.setUpdateProductValuesInd(true);

		vyContract = service.updateVerifiedYieldContract(vyContract);
		Assert.assertNotNull(vyContract);

		//0 warnings expected
		Assert.assertEquals(0, updatedContract.getProductWarningMessages().size());
		
		// Check Contract Commodities
		//Barley - NON Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, vyContract.getVerifiedYieldContractCommodities(), 16, false, null, 23.45); 
		//Canola - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, vyContract.getVerifiedYieldContractCommodities(), 18, true, canolaPedigreeProductionGuarantee, null); 
		//Barley - Pedigree
		checkVerifiedContractCommodityTotals(expectedCommodities, vyContract.getVerifiedYieldContractCommodities(), 16, true, null, null); 

		
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
				.filter(x -> x.getCropCommodityId() == cropCommodityId && x.getIsPedigreeInd() == isPedigree )
				.collect(Collectors.toList());
		
		if (vyccFiltered != null) {
			vycc = vyccFiltered.get(0);
		}
		return vycc;
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
	
	private Double barleyNonPediProductionGuarantee = 222.5;
	private Double barleyPedigreeProductionGuarantee = 15.1;
	private Double canolaPedigreeProductionGuarantee = 77.0;
	
	private void createUpdateProduct(
			Integer policyId,
			Integer productId, 
			Integer cropCommodityId, 
			Integer deductibleLevel, 
			Double probableYield, 
			Double productionGuarantee, 
			Double insurableValueHundredPercent,
			Double coverageDollars, 
			String productStatusCode
			) throws CirrasUnderwritingServiceException, ValidationException {
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 because they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE Product
		ProductRsrc product = new ProductRsrc();
		
		product.setCommodityCoverageCode("CQG");
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
	
	private void createInventoryContract(String policyNumber, Integer insurancePlanId) throws ValidationException, CirrasUnderwritingServiceException {

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
					seededGrains.add(createInventorySeededGrain(16, "BARLEY", false, 23.45));

					planting.setInventorySeededGrains(seededGrains);
					
					addedSeededGrain = true;
				}
			}
			
			if(addedSeededGrain) {
				//Add additional plantings for verifiable commodity tests
				 
				InventoryField newPlanting = createPlanting(field, 2, cropYear1, false, insurancePlanId);
				List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
				seededGrains.add(createInventorySeededGrain(16, "BARLEY", true, 18.0));

				newPlanting.setInventorySeededGrains(seededGrains);
				
				//field.getPlantings().add(newPlanting);
			}
		}
		
		
		if (insurancePlanId.equals(InsurancePlans.GRAIN.getInsurancePlanId())) {
			InventoryContractCommodity icc = createInventoryContractCommodity(16, "BARLEY", false, 23.45, 23.45, 0.0);
			resource.getCommodities().add(icc);
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
	
	private void createDopYieldContract(String policyNumber, Integer insurancePlanId) throws ValidationException, CirrasUnderwritingServiceException {

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

			DopYieldContractCommodity dycc = createDopYieldContractCommodity(16, "BARLEY", 11.22, false, 66.77, 88.99);
			resource.getDopYieldContractCommodities().add(dycc);
			
			dycc = createDopYieldContractCommodity(18, "CANOLA", 33.44, true, 55.66, 77.88);
			resource.getDopYieldContractCommodities().add(dycc);

			//dycc = createDopYieldContractCommodity(20, "FALL RYE", null, false, null, null);
			dycc = createDopYieldContractCommodity(16, "BARLEY", 22.0, true, 67.8, 100.5);
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
