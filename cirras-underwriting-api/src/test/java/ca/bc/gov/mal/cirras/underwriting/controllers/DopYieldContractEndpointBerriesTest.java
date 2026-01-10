package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
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
		
	private String inventoryFieldGuid1 = null;
	private String inventoryFieldGuid2 = null;
	
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
		createInventoryBerries(field.getPlantings().get(1), 12, "RASPBERRY", 1010694, "MALAHAT", 200.0, null, null, true, false, null, 2021, null, null, null, false);
		
		//Create inventory contract
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings());
		Assert.assertEquals(2, fetchedInvContract.getFields().get(0).getPlantings().size());
		
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
		
		delete();
		
		logger.debug(">testDopYieldRolloverBerries");
	}
	
	
	private Date getDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month, day);
		return cal.getTime();
	}
	
	
	// TODO: Remove?
	private Integer calculateTotalPlants(InventoryBerries inventoryBerries) {
		if(inventoryBerries.getPlantedAcres() != null && inventoryBerries.getRowSpacing() != null && inventoryBerries.getPlantSpacing() != null) {
			double spacing = inventoryBerries.getRowSpacing() * inventoryBerries.getPlantSpacing();
			if(spacing > 0) {
				double totalPlants = (inventoryBerries.getPlantedAcres() * 43560) / spacing; 
				return Math.toIntExact(Math.round(totalPlants));
			}
		}
		
		return 0;
	}
	

	// TODO: Remove?
	private InventoryContractCommodityBerries getInventoryContractCommodityBerries(Integer cropCommodityId, List<InventoryContractCommodityBerries> iccbList) {
		
		InventoryContractCommodityBerries iccb = null;
		
		List<InventoryContractCommodityBerries> iccbFiltered = iccbList.stream()
				.filter(x -> x.getCropCommodityId().equals(cropCommodityId))
				.collect(Collectors.toList());
		
		if (iccbFiltered != null && iccbFiltered.size() > 0) {
			iccb = iccbFiltered.get(0);
		}
		return iccb;
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
	
	private InventoryField getPlantingByNumber(Integer plantingNumber, List<InventoryField> inventoryFields) {
		
		List<InventoryField> filteredList = inventoryFields.stream().filter(x -> x.getPlantingNumber().equals(plantingNumber)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, filteredList.size());
		
		return filteredList.get(0);
	}

	// TODO: Remove?
	private void checkInventoryContractCommodityBerries(InventoryContractCommodityBerries expected, InventoryContractCommodityBerries actual, String inventoryContractGuid) {
		Assert.assertNotNull("InventoryContractCommodityBerriesGuid", actual.getInventoryContractCommodityBerriesGuid());
		Assert.assertEquals("InventoryContractGuid", inventoryContractGuid, actual.getInventoryContractGuid());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("TotalInsuredPlants", expected.getTotalInsuredPlants(), actual.getTotalInsuredPlants());
		Assert.assertEquals("TotalUninsuredPlants", expected.getTotalUninsuredPlants(), actual.getTotalUninsuredPlants());
		Assert.assertEquals("TotalQuantityInsuredAcres", expected.getTotalQuantityInsuredAcres(), actual.getTotalQuantityInsuredAcres());
		Assert.assertEquals("TotalQuantityUninsuredAcres", expected.getTotalQuantityUninsuredAcres(), actual.getTotalQuantityUninsuredAcres());
		Assert.assertEquals("TotalPlantInsuredAcres", expected.getTotalPlantInsuredAcres(), actual.getTotalPlantInsuredAcres());
		Assert.assertEquals("TotalPlantUninsuredAcres", expected.getTotalPlantUninsuredAcres(), actual.getTotalPlantUninsuredAcres());
	}

	// TODO: Remove?
	private void checkInventoryBerries(InventoryBerries expected, InventoryBerries actual, Boolean isRolledOver, Integer cropYear) {
		
		if(isRolledOver) {
			Assert.assertNull("InventoryBerriesGuid", actual.getInventoryBerriesGuid());
			Assert.assertNull("InventoryFieldGuid", actual.getInventoryFieldGuid());
			//Plant PlantInsurabilityTypeCode for strawberry
			if(expected.getCropCommodityId().equals(13)) {
				if (expected.getPlantInsurabilityTypeCode() != null && expected.getPlantInsurabilityTypeCode().equalsIgnoreCase("ST1")) {
					//Strawberries that were previously insured with ST1 (Strawberry Year 1) will now be ST2 (Strawberry Year 2)
					Assert.assertEquals("ST2", actual.getPlantInsurabilityTypeCode());
					Assert.assertTrue(actual.getIsPlantInsurableInd());
				} else {
					//All other cases it's null and plant insured = false
					Assert.assertNull(actual.getPlantInsurabilityTypeCode());
					Assert.assertFalse(actual.getIsPlantInsurableInd());
				}
			} else {
				Assert.assertNull("PlantInsurabilityTypeCode Null", actual.getPlantInsurabilityTypeCode());
				Assert.assertEquals("IsPlantInsurableInd", expected.getIsPlantInsurableInd(), actual.getIsPlantInsurableInd());
			}
			//Is Harvested is always set to false on rollover
			Assert.assertFalse("IsHarvestedInd", actual.getIsHarvestedInd());

		} else {
			Assert.assertNotNull("InventoryBerriesGuid", actual.getInventoryBerriesGuid());
			Assert.assertNotNull("InventoryFieldGuid", actual.getInventoryFieldGuid());
			Assert.assertEquals("IsPlantInsurableInd", expected.getIsPlantInsurableInd(), actual.getIsPlantInsurableInd());
			Assert.assertEquals("IsHarvestedInd", expected.getIsHarvestedInd(), actual.getIsHarvestedInd());


			if(expected.getPlantInsurabilityTypeCode() == null) {
				Assert.assertNull("PlantInsurabilityTypeCode Null", actual.getPlantInsurabilityTypeCode());
			} else {
				Assert.assertEquals("PlantInsurabilityTypeCode", expected.getPlantInsurabilityTypeCode(), actual.getPlantInsurabilityTypeCode());
			}
		}
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropVarietyId", expected.getCropVarietyId(), actual.getCropVarietyId());
		Assert.assertEquals("PlantedYear", expected.getPlantedYear(), actual.getPlantedYear());
		Assert.assertEquals("PlantedAcres", expected.getPlantedAcres(), actual.getPlantedAcres());
		Assert.assertEquals("RowSpacing", expected.getRowSpacing(), actual.getRowSpacing());
		Assert.assertEquals("PlantSpacing", expected.getPlantSpacing(), actual.getPlantSpacing());
		Assert.assertEquals("TotalPlants", expected.getTotalPlants(), actual.getTotalPlants());
		Assert.assertEquals("IsQuantityInsurableInd", expected.getIsQuantityInsurableInd(), actual.getIsQuantityInsurableInd());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("CropVarietyName", expected.getCropVarietyName(), actual.getCropVarietyName());
		Assert.assertEquals("BogId", expected.getBogId(), actual.getBogId());
		Assert.assertEquals("BogMowedDate", expected.getBogMowedDate(), actual.getBogMowedDate());
		Assert.assertEquals("BogRenovatedDate", expected.getBogRenovatedDate(), actual.getBogRenovatedDate());
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
		ib.setTotalPlants(calculateTotalPlants(ib));
		ib.setIsQuantityInsurableInd(isQuantityInsurableInd);
		ib.setIsPlantInsurableInd(isPlantInsurableInd);
		ib.setBogId(bogId);
		ib.setBogMowedDate(bogMowedDate);
		ib.setBogRenovatedDate(bogRenovatedDate);
		ib.setIsHarvestedInd(isHarvestedInd);
		
		planting.setInventoryBerries(ib);

		return ib;
	}	

	// TODO: Remove?
	private List<InventoryContractCommodityBerries> createExpectedInventoryContractCommodityBerries(AnnualFieldRsrc field) {

		List<InventoryContractCommodityBerries> expectedTotals = new ArrayList<InventoryContractCommodityBerries>();
		
		for ( InventoryField planting : field.getPlantings() ) {
			if(planting.getInventoryBerries() != null) {
				InventoryBerries ib = planting.getInventoryBerries();
				if(!Boolean.TRUE.equals(ib.getDeletedByUserInd()) && ib.getCropCommodityId() != null){
					List<InventoryContractCommodityBerries> iccbFiltered = null;

					if (expectedTotals != null && expectedTotals.size() > 0) {
						iccbFiltered = expectedTotals.stream()
								.filter(x -> x.getCropCommodityId().equals(ib.getCropCommodityId()))
								.collect(Collectors.toList());
					}
					
					Double quantityInsuredAcres = 0.0;
					Double quantityUninsuredAcres = 0.0;
					Double plantInsuredAcres = 0.0;
					Double plantUninsuredAcres = 0.0;
					Integer insuredPlants = 0;
					Integer uninsuredPlants = 0;
					
					if(Boolean.TRUE.equals(ib.getIsQuantityInsurableInd())) {
						//Quantity insurable
						quantityInsuredAcres = notNull(ib.getPlantedAcres(), (double) 0);
					} else {
						//Not Quantity insurable
						quantityUninsuredAcres = notNull(ib.getPlantedAcres(), (double) 0);
					}
					
					if(Boolean.TRUE.equals(ib.getIsPlantInsurableInd())) {
						//Plant insurable
						insuredPlants = notNull(ib.getTotalPlants(), 0);
						plantInsuredAcres = notNull(ib.getPlantedAcres(), (double) 0);
					} else {
						//Not Plant insurable
						uninsuredPlants = notNull(ib.getTotalPlants(), 0);
						plantUninsuredAcres = notNull(ib.getPlantedAcres(), (double) 0);
					}

					if (iccbFiltered == null || iccbFiltered.size() == 0) {
						// commodity is not in the list yet - Add it
						InventoryContractCommodityBerries iccb = new InventoryContractCommodityBerries();
						iccb.setCropCommodityId(ib.getCropCommodityId());
						iccb.setCropCommodityName(ib.getCropCommodityName());
						iccb.setTotalQuantityInsuredAcres(quantityInsuredAcres);
						iccb.setTotalQuantityUninsuredAcres(quantityUninsuredAcres);
						iccb.setTotalPlantInsuredAcres(plantInsuredAcres);
						iccb.setTotalPlantUninsuredAcres(plantUninsuredAcres);
						iccb.setTotalInsuredPlants(insuredPlants);
						iccb.setTotalUninsuredPlants(uninsuredPlants);
						expectedTotals.add(iccb);

					} else {
						// commodity already exists in the list. Add the new values
						InventoryContractCommodityBerries iccb = iccbFiltered.get(0);
						iccb.setTotalQuantityInsuredAcres(quantityInsuredAcres + iccb.getTotalQuantityInsuredAcres());
						iccb.setTotalQuantityUninsuredAcres(quantityUninsuredAcres + iccb.getTotalQuantityUninsuredAcres());
						iccb.setTotalPlantInsuredAcres(plantInsuredAcres + iccb.getTotalPlantInsuredAcres());
						iccb.setTotalPlantUninsuredAcres(plantUninsuredAcres + iccb.getTotalPlantUninsuredAcres());
						iccb.setTotalInsuredPlants(insuredPlants + iccb.getTotalInsuredPlants());
						iccb.setTotalUninsuredPlants(uninsuredPlants + iccb.getTotalUninsuredPlants());
					}
				}
			}
		}

		return expectedTotals;
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
