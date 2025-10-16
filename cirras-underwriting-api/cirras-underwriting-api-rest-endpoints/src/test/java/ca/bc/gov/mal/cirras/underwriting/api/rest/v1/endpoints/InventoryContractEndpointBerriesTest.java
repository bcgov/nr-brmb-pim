package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

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

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
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
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryBerries;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class InventoryContractEndpointBerriesTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(InventoryContractEndpointBerriesTest.class);


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
		Scopes.CREATE_DOP_YIELD_CONTRACT
	};
	
	private Integer growerId1 = 90000011;
	private Integer policyId1 = 90000012;
	private Integer gcyId1 = 90000013;
	private Integer contractId1 = 90000014;
	private String policyNumber1 = "998891-21";
	private String contractNumber1 = "998891";
	private Integer cropYear1 = 2021;

	private Integer legalLandId1 = 90000015;
	private Integer fieldId1 = 90000016;
	private Integer annualFieldDetailId1 = 90000017;
	private Integer contractedFieldDetailId1 = 90000018;
	
	private String fieldLocation = "Field Location";
		
	private String inventoryFieldGuid1 = null;
	
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

		Integer pageNumber = 1;
		Integer pageRowCount = 20;
		
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
			
			if ( referrer.getInventoryContractGuid() != null ) { 
				InventoryContractRsrc invContract = service.getInventoryContract(referrer);
				service.deleteInventoryContract(invContract);
			}
		}

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		service.deleteField(topLevelEndpoints, fieldId1.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId1.toString());
		
		service.deleteGrowerContractYear(topLevelEndpoints, gcyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId1.toString());
		service.deleteGrower(topLevelEndpoints, growerId1.toString());
	}

	
	@Test
	public void testInsertUpdateDeleteInventoryBerries() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInsertUpdateDeleteInventoryBerries");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		createPolicy();
		createGrowerContractYear();

		createLegalLand();
		createField();
		createAnnualFieldDetail();
		createContractedFieldDetail(false);
		
		UwContractRsrc referrer = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNull(referrer.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings().get(0).getInventoryBerries());
		
		InventoryBerries newBerries = invContract.getFields().get(0).getPlantings().get(0).getInventoryBerries();
		Assert.assertNull("InventoryBerriesGuid", newBerries.getInventoryBerriesGuid());
		Assert.assertNull("InventoryFieldGuid", newBerries.getInventoryFieldGuid());
		Assert.assertNull("CropCommodityId", newBerries.getCropCommodityId());
		Assert.assertNull("CropVarietyId", newBerries.getCropVarietyId());
		Assert.assertNull("PlantedYear", newBerries.getPlantedYear());
		Assert.assertNull("PlantedAcres", newBerries.getPlantedAcres());
		Assert.assertNull("RowSpacing", newBerries.getRowSpacing());
		Assert.assertNull("PlantSpacing", newBerries.getPlantSpacing());
		Assert.assertNull("TotalPlants", newBerries.getTotalPlants());
		Assert.assertNull("IsQuantityInsurableInd", newBerries.getIsQuantityInsurableInd());
		Assert.assertNull("IsPlantInsurableInd", newBerries.getIsPlantInsurableInd());
		//Check field data
		AnnualFieldRsrc field = invContract.getFields().get(0);
		Assert.assertEquals("FieldLocation", fieldLocation, field.getFieldLocation());
		Assert.assertEquals("IsLeased", false, field.getIsLeasedInd());

		newBerries.setCropCommodityId(10);
		newBerries.setCropCommodityName("BLUEBERRY");
		newBerries.setCropVarietyId(1010689);
		newBerries.setCropVarietyName("BLUEJAY");
		newBerries.setPlantedYear(2020);
		newBerries.setPlantedAcres((double)100);
		newBerries.setRowSpacing(10);
		newBerries.setPlantSpacing(5.3);
		newBerries.setTotalPlants(calculateTotalPlants(newBerries));
		newBerries.setIsQuantityInsurableInd(true);
		newBerries.setIsPlantInsurableInd(false);

		//Create inventory contract
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		field = fetchedInvContract.getFields().get(0);
		inventoryFieldGuid1 = field.getPlantings().get(0).getInventoryFieldGuid();
		newBerries.setInventoryFieldGuid(inventoryFieldGuid1);
		
		InventoryBerries fetchedBerries = fetchedInvContract.getFields().get(0).getPlantings().get(0).getInventoryBerries();

		checkInventoryBerries(newBerries, fetchedBerries);
		
		//Check field data
		field = fetchedInvContract.getFields().get(0);
		Assert.assertEquals("FieldLocation", fieldLocation, field.getFieldLocation());
		Assert.assertEquals("IsLeased", false, field.getIsLeasedInd());

		//Update
		fetchedBerries.setCropCommodityId(12);
		fetchedBerries.setCropCommodityName("RASPBERRY");
		fetchedBerries.setCropVarietyId(1010694);
		fetchedBerries.setCropVarietyName("MALAHAT");
		fetchedBerries.setPlantedYear(2021);
		fetchedBerries.setPlantedAcres((double)200);
		fetchedBerries.setRowSpacing(5);
		fetchedBerries.setPlantSpacing(4.9);
		fetchedBerries.setTotalPlants(calculateTotalPlants(fetchedBerries));
		fetchedBerries.setIsQuantityInsurableInd(false);
		fetchedBerries.setIsPlantInsurableInd(true);

		InventoryContractRsrc updatedInvContract = service.updateInventoryContract(fetchedInvContract.getInventoryContractGuid(), fetchedInvContract);

		InventoryBerries updatedBerries = updatedInvContract.getFields().get(0).getPlantings().get(0).getInventoryBerries();

		checkInventoryBerries(fetchedBerries, updatedBerries);

		//Update Field location
		fieldLocation = fieldLocation + " Update";
		FieldRsrc fetchedField = service.getField(topLevelEndpoints, fieldId1.toString());
		fetchedField.setFieldLocation(fieldLocation);
		fetchedField.setTransactionType(LandManagementEventTypes.FieldUpdated);
		service.synchronizeField(fetchedField);
		
		//Update is Leased
		ContractedFieldDetailRsrc fetchedCfd = service.getContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString()); 
		fetchedCfd.setIsLeasedInd(true);
		fetchedCfd.setTransactionType(LandManagementEventTypes.ContractedFieldDetailUpdated);
		service.synchronizeContractedFieldDetail(fetchedCfd);
		
		//Check field data
		UwContractRsrc uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNotNull(uwContract.getInventoryContractGuid());
		invContract = service.getInventoryContract(uwContract);

		field = invContract.getFields().get(0);
		Assert.assertEquals("FieldLocation", fieldLocation, field.getFieldLocation());
		Assert.assertEquals("IsLeased", true, field.getIsLeasedInd());
		
		delete();
		
		logger.debug(">testInsertUpdateDeleteInventoryBerries");
	}
	
	@Test
	public void testDeleteInventoryBerriesPlanting() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testDeleteInventoryBerriesPlanting");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		createPolicy();
		createGrowerContractYear();

		createLegalLand();
		createField();
		createAnnualFieldDetail();
		createContractedFieldDetail(false);
		
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		UwContractRsrc uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContract);
		
		AnnualFieldRsrc field = invContract.getFields().get(0);

		// Remove default planting.
		field.getPlantings().remove(0);
		
		// Planting 1
		InventoryField planting = createPlanting(field, 1, cropYear1);
		createInventoryBerries(planting, 10, "BLUEBERRY", 1010689, "BLUEJAY", (double)10, 10, 5.3, true, true); //Blueberry insured for both

		// Planting 2
		planting = createPlanting(field, 2, cropYear1);
		createInventoryBerries(planting, 10, "BLUEBERRY", 1010691, "ELLIOTT", (double)20, 5, 4.9, true, false); //Blueberry Quantity insured
				
		// Planting 3
		planting = createPlanting(field, 3, cropYear1);
		createInventoryBerries(planting, 10, "BLUEBERRY", 1010690, "LEGACY", (double)12, 12, 5.0, false, true); //Blueberry Plant insured

		// Planting 4
		planting = createPlanting(field, 4, cropYear1);
		createInventoryBerries(planting, 12, "RASPBERRY", 1010694, "MALAHAT", (double)13, 10, 7.0, true, true); //Raspberry insured for both
		
		// Planting 5
		planting = createPlanting(field, 5, cropYear1);
		createInventoryBerries(planting, 12, "RASPBERRY", 1010694, "MALAHAT", (double)15, 9, 6.0, true, true); //Raspberry insured for both

		invContract = service.createInventoryContract(topLevelEndpoints, invContract);

		//Mark some plantings to delete
		field = invContract.getFields().get(0);

		List<InventoryField> plantings = invContract.getFields().get(0).getPlantings();
		Assert.assertEquals(5, plantings.size());
		
		//Remove planting number 2 and 4
		planting = getPlantingByNumber(2, plantings);
		planting.getInventoryBerries().setDeletedByUserInd(true);
		planting = getPlantingByNumber(5, plantings);
		planting.getInventoryBerries().setDeletedByUserInd(true);
		
		//Update inventory contract
		invContract = service.updateInventoryContract(invContract.getInventoryContractGuid(), invContract);
		
		//Check if plantings were deleted
		plantings = invContract.getFields().get(0).getPlantings();
		Assert.assertEquals(3, plantings.size());
		
		//Check if planting numbers of the remaining plantings are in order without gaps.
		Integer counter = 1;
		Comparator<Integer> nullsLast = Comparator.nullsLast(Comparator.naturalOrder());
		List<InventoryField> sortedInventoryFields = plantings.stream()
				.sorted(Comparator.comparing(InventoryField::getPlantingNumber, nullsLast))
				.collect(Collectors.toList());
		for (InventoryField inventoryField : sortedInventoryFields) {

			logger.debug(inventoryField.getInventoryFieldGuid() + " has planting number: " + inventoryField.getPlantingNumber());

			//Test if the planting number of the remaining is in sequence
			Assert.assertEquals("Wrong planting number for: " + inventoryField.getInventoryFieldGuid(), counter, inventoryField.getPlantingNumber());
			counter += 1;
		}		

		delete();

		logger.debug(">testDeleteInventoryBerriesPlanting");
	}
	
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
	
	@Test
	public void testInventoryContractCommodityBerries() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInventoryContractCommodityBerries");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		createGrower();
		createPolicy();
		createGrowerContractYear();

		createLegalLand();
		createField();
		createAnnualFieldDetail();
		createContractedFieldDetail(false);
		
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		UwContractRsrc uwContract = getUwContract(policyNumber1, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContract = service.rolloverInventoryContract(uwContract);
		
		Assert.assertNotNull(invContract.getInventoryContractCommodityBerries());
		Assert.assertEquals(0, invContract.getInventoryContractCommodityBerries().size());

		AnnualFieldRsrc field = invContract.getFields().get(0);

		// Remove default planting.
		field.getPlantings().remove(0);
		
		// Planting 1
		InventoryField planting = createPlanting(field, 1, cropYear1);
		createInventoryBerries(planting, 10, "BLUEBERRY", 1010689, "BLUEJAY", (double)10, 10, 5.3, true, true); //Blueberry insured for both

		// Planting 2
		planting = createPlanting(field, 2, cropYear1);
		createInventoryBerries(planting, 10, "BLUEBERRY", 1010691, "ELLIOTT", (double)20, 5, 4.9, true, false); //Blueberry Quantity insured
				
		// Planting 3
		planting = createPlanting(field, 3, cropYear1);
		createInventoryBerries(planting, 10, "BLUEBERRY", 1010690, "LEGACY", (double)12, 12, 5.0, false, true); //Blueberry Plant insured

		// Planting 4
		planting = createPlanting(field, 4, cropYear1);
		createInventoryBerries(planting, 12, "RASPBERRY", 1010694, "MALAHAT", (double)13, 10, 7.0, true, true); //Raspberry insured for both
		
		// Planting 5
		planting = createPlanting(field, 5, cropYear1);
		createInventoryBerries(planting, 12, "RASPBERRY", 1010694, "MALAHAT", (double)15, 9, 6.0, true, true); //Raspberry insured for both

		//Berries Totals
		List<InventoryContractCommodityBerries> expectedTotals = createExpectedInventoryContractCommodityBerries(field);

		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertEquals(expectedTotals.size(), fetchedInvContract.getInventoryContractCommodityBerries().size());

		for (InventoryContractCommodityBerries expectedIccb : expectedTotals) {
			InventoryContractCommodityBerries actualIccb = getInventoryContractCommodityBerries(expectedIccb.getCropCommodityId(), fetchedInvContract.getInventoryContractCommodityBerries());
			Assert.assertNotNull(actualIccb);
			checkInventoryContractCommodityBerries(expectedIccb, actualIccb, fetchedInvContract.getInventoryContractGuid());
		}

		delete();

		logger.debug(">testInventoryContractCommodityBerries");
	}
	
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

//	@Test
//	public void testGenerateInventoryReport() throws CirrasUnderwritingServiceException, Oauth2ClientException {
//		logger.debug("<testGenerateInventoryReport");
//		
//		if(skipTests) {
//			logger.warn("Skipping tests");
//			return;
//		}
//
//		// Test 1: Generate Forage report.
//		byte[] reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "5", null, null, null, null, null, null, null);
//		
//		Assert.assertNotNull(reportContent);
//		
//		logger.debug(">testGenerateInventoryReport - Returned " + reportContent.length + " bytes");	
//
//		// Test 2: Generate Grain Unseeded report
//		reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "4", null, null, null, null, null, null, InventoryReportType.unseeded.name());
//		
//		Assert.assertNotNull(reportContent);
//		
//		logger.debug(">testGenerateInventoryReport - Returned " + reportContent.length + " bytes");	
//
//		// Test 3: Generate Grain Seeded report
//		reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "4", null, null, null, null, null, null, InventoryReportType.seeded.name());
//		
//		Assert.assertNotNull(reportContent);
//		
//		logger.debug(">testGenerateInventoryReport - Returned " + reportContent.length + " bytes");
//		
//		// Test 4: Omit Insurance Plan: Report generation should fail.
//		try {
//			reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", null, null, null, null, null, null, null, null);
//			Assert.fail("Report generated for missing insurance plan id ");
//		} catch ( CirrasUnderwritingServiceException e ) {
//			// Ok.
//		}
//
//		// Test 5: Invalid Insurance Plan: Report generation should fail.
//		try {
//			reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "2", null, null, null, null, null, null, null);
//			Assert.fail("Report generated for invalid insurance plan id ");
//		} catch ( CirrasUnderwritingServiceException e ) {
//			// Ok.
//		}
//
//		// Test 6: Omit Report Type for GRAIN: Report generation should fail.
//		try {
//			reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "4", null, null, null, null, null, null, null);
//			Assert.fail("Report generated for omitted report type");
//		} catch ( CirrasUnderwritingServiceException e ) {
//			// Ok.
//		}
//	
//		// Test 7: Invalid Report Type for GRAIN: Report generation should fail.
//		try {
//			reportContent = service.generateInventoryReport(topLevelEndpoints, "2023", "4", null, null, null, null, null, null, "nosuchtype");
//			Assert.fail("Report generated for invalid report type");
//		} catch ( CirrasUnderwritingServiceException e ) {
//			// Ok.
//		}
//	}	
	
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
		Assert.assertTrue(searchResults.getCollection().size() == 1);
		
		UwContractRsrc uwContract = searchResults.getCollection().get(0);
		return uwContract;
	}
	
	private InventoryField getPlantingByNumber(Integer plantingNumber, List<InventoryField> inventoryFields) {
		
		List<InventoryField> filteredList = inventoryFields.stream().filter(x -> x.getPlantingNumber().equals(plantingNumber)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, filteredList.size());
		
		return filteredList.get(0);
	}
	
	private void checkInventoryContractCommodityBerries(InventoryContractCommodityBerries expected, InventoryContractCommodityBerries actual, String inventoryContractGuid) {
		Assert.assertNotNull("InventoryContractCommodityBerriesGuid", actual.getInventoryContractCommodityBerriesGuid());
		Assert.assertEquals("InventoryContractGuid", inventoryContractGuid, actual.getInventoryContractGuid());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("TotalInsuredPlants", expected.getTotalInsuredPlants(), actual.getTotalInsuredPlants());
		Assert.assertEquals("TotalUninsuredPlants", expected.getTotalUninsuredPlants(), actual.getTotalUninsuredPlants());
		Assert.assertEquals("TotalInsuredAcres", expected.getTotalInsuredAcres(), actual.getTotalInsuredAcres());
		Assert.assertEquals("TotalUninsuredAcres", expected.getTotalUninsuredAcres(), actual.getTotalUninsuredAcres());
	}
	
	private void checkInventoryBerries(InventoryBerries expected, InventoryBerries actual) {
		
		Assert.assertNotNull("InventoryBerriesGuid", actual.getInventoryBerriesGuid());
		Assert.assertEquals("InventoryFieldGuid", expected.getInventoryFieldGuid(), actual.getInventoryFieldGuid());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("CropVarietyId", expected.getCropVarietyId(), actual.getCropVarietyId());
		Assert.assertEquals("PlantedYear", expected.getPlantedYear(), actual.getPlantedYear());
		Assert.assertEquals("PlantedAcres", expected.getPlantedAcres(), actual.getPlantedAcres());
		Assert.assertEquals("RowSpacing", expected.getRowSpacing(), actual.getRowSpacing());
		Assert.assertEquals("PlantSpacing", expected.getPlantSpacing(), actual.getPlantSpacing());
		Assert.assertEquals("TotalPlants", expected.getTotalPlants(), actual.getTotalPlants());
		Assert.assertEquals("IsQuantityInsurableInd", expected.getIsQuantityInsurableInd(), actual.getIsQuantityInsurableInd());
		Assert.assertEquals("IsPlantInsurableInd", expected.getIsPlantInsurableInd(), actual.getIsPlantInsurableInd());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("CropVarietyName", expected.getCropVarietyName(), actual.getCropVarietyName());

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
			Boolean isPlantInsurableInd
			) {
		
		InventoryBerries ib = new InventoryBerries();

		ib.setCropCommodityId(cropCommodityId);
		ib.setCropCommodityName(cropCommodityName);
		ib.setCropVarietyId(cropVarietyId);
		ib.setCropVarietyName(cropVarietyName);
		ib.setPlantedYear(2020);
		ib.setPlantedAcres(plantedAcres);
		ib.setRowSpacing(rowSpacing);
		ib.setPlantSpacing(plantSpacing);
		ib.setTotalPlants(calculateTotalPlants(ib));
		ib.setIsQuantityInsurableInd(isQuantityInsurableInd);
		ib.setIsPlantInsurableInd(isPlantInsurableInd);

		planting.setInventoryBerries(ib);

		return ib;
	}	

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
					
					Double insuredAcres = 0.0;
					Double uninsuredAcres = 0.0;
					Integer insuredPlants = 0;
					Integer uninsuredPlants = 0;
					
					if(Boolean.TRUE.equals(ib.getIsQuantityInsurableInd())) {
						//Quantity insurable
						insuredAcres = notNull(ib.getPlantedAcres(), (double) 0);
					} else {
						//Not Quantity insurable
						uninsuredAcres = notNull(ib.getPlantedAcres(), (double) 0);
					}
					
					if(Boolean.TRUE.equals(ib.getIsPlantInsurableInd())) {
						//Plant insurable
						insuredPlants = notNull(ib.getTotalPlants(), 0);
					} else {
						//Not Plant insurable
						uninsuredPlants = notNull(ib.getTotalPlants(), 0);
					}

					if (iccbFiltered == null || iccbFiltered.size() == 0) {
						// commodity is not in the list yet - Add it
						InventoryContractCommodityBerries iccb = new InventoryContractCommodityBerries();
						iccb.setCropCommodityId(ib.getCropCommodityId());
						iccb.setCropCommodityName(ib.getCropCommodityName());
						iccb.setTotalInsuredAcres(insuredAcres);
						iccb.setTotalUninsuredAcres(uninsuredAcres);
						iccb.setTotalInsuredPlants(insuredPlants);
						iccb.setTotalUninsuredPlants(uninsuredPlants);
						expectedTotals.add(iccb);

					} else {
						// commodity already exists in the list. Add the new values
						InventoryContractCommodityBerries iccb = iccbFiltered.get(0);
						iccb.setTotalInsuredAcres(insuredAcres + iccb.getTotalInsuredAcres());
						iccb.setTotalUninsuredAcres(uninsuredAcres + iccb.getTotalUninsuredAcres());
						iccb.setTotalInsuredPlants(insuredPlants + iccb.getTotalInsuredPlants());
						iccb.setTotalUninsuredPlants(uninsuredPlants + iccb.getTotalUninsuredPlants());
					}
				}
			}
		}

		return expectedTotals;
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
	
	private void createField() throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId1);
		resource.setFieldLabel("Field Label");
		resource.setFieldLocation(fieldLocation );
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2022);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
		
		service.synchronizeField(resource);
	}
	
	private void createAnnualFieldDetail() throws CirrasUnderwritingServiceException, ValidationException {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId1);
		resource.setLegalLandId(legalLandId1);
		resource.setFieldId(fieldId1);
		resource.setCropYear(cropYear1);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		service.synchronizeAnnualFieldDetail(resource);
	}

	private void createContractedFieldDetail(Boolean isLeased) throws CirrasUnderwritingServiceException, ValidationException {
		
		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId1);
		resource.setAnnualFieldDetailId(annualFieldDetailId1);
		resource.setGrowerContractYearId(gcyId1);
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
