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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldRollupForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class UwContractRolloverVerifiedYieldEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(UwContractRolloverVerifiedYieldEndpointTest.class);


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

	private void deleteDopYieldContract(String policyNumber) throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException {
		
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
				1, 20);
		
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
				1, 20);
		
		if ( searchResults.getCollection() != null && searchResults.getCollection().size() == 1 ) {
			UwContractRsrc referrer = searchResults.getCollection().get(0);
						
			if ( referrer.getInventoryContractGuid() != null ) { 
				InventoryContractRsrc invContract = service.getInventoryContract(referrer);
				service.deleteInventoryContract(invContract);
			}
		}
	}
	
	@Test
	public void testRolloverGrainVerifiedYieldContract() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testRolloverGrainVerifiedYieldContract");

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

		createGrainInventoryContract(policyNumber1);
		createGrainDopYieldContract(policyNumber1);

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
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
		
		// Check VerifiedYieldContract
		VerifiedYieldContractRsrc vyContract = service.rolloverVerifiedYieldContract(referrer);
		Assert.assertNotNull(vyContract);

		Assert.assertEquals(contractId, vyContract.getContractId());
		Assert.assertEquals(cropYear1, vyContract.getCropYear());
		Assert.assertEquals(referrer.getDeclaredYieldContractGuid(), vyContract.getDeclaredYieldContractGuid());
		Assert.assertEquals("TONNE", vyContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(growerContractYearId1, vyContract.getGrowerContractYearId());
		Assert.assertEquals(Integer.valueOf(4), vyContract.getInsurancePlanId());
		Assert.assertNull(vyContract.getVerifiedYieldContractGuid());
		Assert.assertNull(vyContract.getVerifiedYieldUpdateUser());
		Assert.assertNull(vyContract.getVerifiedYieldUpdateTimestamp());

		// Check VerifiedYieldContractCommodities
		Assert.assertEquals(3, vyContract.getVerifiedYieldContractCommodities().size());
		
		VerifiedYieldContractCommodity vycc = vyContract.getVerifiedYieldContractCommodities().get(0);

		Assert.assertEquals(Integer.valueOf(16), vycc.getCropCommodityId());
		Assert.assertEquals("BARLEY", vycc.getCropCommodityName());
		Assert.assertEquals(Double.valueOf(11.22), vycc.getHarvestedAcres());
		Assert.assertEquals(null, vycc.getHarvestedAcresOverride());
		Assert.assertEquals(Double.valueOf(155.76), vycc.getHarvestedYield());
		Assert.assertEquals(null, vycc.getHarvestedYieldOverride());
		Assert.assertEquals(false, vycc.getIsPedigreeInd());
		Assert.assertEquals(null, vycc.getProductionGuarantee());
		Assert.assertEquals(Double.valueOf(66.77), vycc.getSoldYieldDefaultUnit());
		Assert.assertEquals(Double.valueOf(88.99), vycc.getStoredYieldDefaultUnit());
		Assert.assertEquals(Double.valueOf(23.45), vycc.getTotalInsuredAcres());
		Assert.assertEquals(null, vycc.getVerifiedYieldContractCommodityGuid());
		Assert.assertEquals(null, vycc.getVerifiedYieldContractGuid());
		Assert.assertEquals(13.8824, vycc.getYieldPerAcre(), 0.00005);

		
		vycc = vyContract.getVerifiedYieldContractCommodities().get(1);

		Assert.assertEquals(Integer.valueOf(18), vycc.getCropCommodityId());
		Assert.assertEquals("CANOLA", vycc.getCropCommodityName());
		Assert.assertEquals(Double.valueOf(33.44), vycc.getHarvestedAcres());
		Assert.assertEquals(null, vycc.getHarvestedAcresOverride());
		Assert.assertEquals(Double.valueOf(133.54), vycc.getHarvestedYield());
		Assert.assertEquals(null, vycc.getHarvestedYieldOverride());
		Assert.assertEquals(true, vycc.getIsPedigreeInd());
		Assert.assertEquals(null, vycc.getProductionGuarantee());
		Assert.assertEquals(Double.valueOf(55.66), vycc.getSoldYieldDefaultUnit());
		Assert.assertEquals(Double.valueOf(77.88), vycc.getStoredYieldDefaultUnit());
		Assert.assertEquals(null, vycc.getTotalInsuredAcres());
		Assert.assertEquals(null, vycc.getVerifiedYieldContractCommodityGuid());
		Assert.assertEquals(null, vycc.getVerifiedYieldContractGuid());
		Assert.assertEquals(3.9934, vycc.getYieldPerAcre(), 0.00005);

		vycc = vyContract.getVerifiedYieldContractCommodities().get(2);

		Assert.assertEquals(Integer.valueOf(20), vycc.getCropCommodityId());
		Assert.assertEquals("FALL RYE", vycc.getCropCommodityName());
		Assert.assertEquals(null, vycc.getHarvestedAcres());
		Assert.assertEquals(null, vycc.getHarvestedAcresOverride());
		Assert.assertEquals(null, vycc.getHarvestedYield());
		Assert.assertEquals(null, vycc.getHarvestedYieldOverride());
		Assert.assertEquals(false, vycc.getIsPedigreeInd());
		Assert.assertEquals(null, vycc.getProductionGuarantee());
		Assert.assertEquals(null, vycc.getSoldYieldDefaultUnit());
		Assert.assertEquals(null, vycc.getStoredYieldDefaultUnit());
		Assert.assertEquals(null, vycc.getTotalInsuredAcres());
		Assert.assertEquals(null, vycc.getVerifiedYieldContractCommodityGuid());
		Assert.assertEquals(null, vycc.getVerifiedYieldContractGuid());
		Assert.assertEquals(null, vycc.getYieldPerAcre());
		
		logger.debug(">testRolloverGrainVerifiedYieldContract");
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
	
	private static final String ctcAlfalfa = "Alfalfa";
	private static final String ctcSilageCorn = "Silage Corn";
	private Integer cropIdForage = 65;
	private Integer cropIdSilageCorn = 71;
	private Integer varietyIdAlfalafaGrass = 118;
	private Integer varietyIdSilageCorn = 1010863;
	private Integer alfalfaBales = 50;
	private double alfalfaWeight = 2.0;
	private double alfalfaQuantityHarvestedAcres = 320.0;
	private double alfalfaInsuredAcres1 = 100.0;
	private Integer silageCornBales = 35;
	private double silageCornWeight = 2.5;
	private double silageCornInsuredAcres1 = 250.0;
	private double moisturePercent = 15.0;
	
	private List<DopYieldContractCommodityForage> dopYieldContractCommodityList;
	private List<DopYieldFieldRollupForage> dopYieldFieldRollupList;

	
	private void createForageInventoryContract(String policyNumber) throws ValidationException, CirrasUnderwritingServiceException {

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
		Date seedingDate = cal.getTime();
		
		// Planting 1 - Forage - Alfalfa
		InventoryField planting = createPlanting(field1, 1, cropYear1);
		createInventorySeededForage(planting, cropIdForage, varietyIdAlfalafaGrass, ctcAlfalfa, alfalfaInsuredAcres1, seedingDate); //Alfalfa Grass

		// Planting 2 - Silage Corn 1
		planting = createPlanting(field1, 2, cropYear1);
		createInventorySeededForage(planting, cropIdSilageCorn, varietyIdSilageCorn, ctcSilageCorn, silageCornInsuredAcres1, null);
		
		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertEquals(1, fetchedInvContract.getFields().size());
		
		AnnualFieldRsrc fetchedField1 = getField(fieldId, fetchedInvContract.getFields());
		
		Assert.assertNotNull(fetchedField1.getPlantings());
		Assert.assertEquals(2, fetchedField1.getPlantings().size());

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
	
	private void createGrainInventoryContract(String policyNumber) throws ValidationException, CirrasUnderwritingServiceException {

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
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());

		InventoryContractRsrc resource = service.rolloverInventoryContract(referrer);
		
		for ( AnnualFieldRsrc field : resource.getFields() ) {
			for ( InventoryField planting : field.getPlantings() ) {
				if ( planting.getInventorySeededGrains() != null ) {
					
					Calendar cal = Calendar.getInstance();
					cal.clear();
					cal.set(2020, Calendar.JANUARY, 15);
					Date seededDate = cal.getTime();
					
					InventorySeededGrain invSeededGrain = new InventorySeededGrain();

					invSeededGrain.setCommodityTypeCode("Two Row");
					invSeededGrain.setCommodityTypeDesc("Two Row Standard");
					invSeededGrain.setCropCommodityId(16);
					invSeededGrain.setCropCommodityName("BARLEY");
					invSeededGrain.setCropVarietyId(1010430);
					invSeededGrain.setCropVarietyName("CHAMPION");
					invSeededGrain.setInventoryFieldGuid(null);
					invSeededGrain.setInventorySeededGrainGuid(null);
					invSeededGrain.setIsPedigreeInd(false);
					invSeededGrain.setIsSpotLossInsurableInd(true);
					invSeededGrain.setIsQuantityInsurableInd(true);
					invSeededGrain.setIsReplacedInd(false);
					invSeededGrain.setSeededAcres(23.45);
					invSeededGrain.setSeededDate(seededDate);
					
					List<InventorySeededGrain> seededGrains = new ArrayList<InventorySeededGrain>();
					seededGrains.add(invSeededGrain);

					planting.setInventorySeededGrains(seededGrains);
				}
			}
		}

		InventoryContractCommodity icc = createInventoryContractCommodity(16, "BARLEY", false, 23.45, 23.45, 0.0);
		resource.getCommodities().add(icc);

		service.createInventoryContract(topLevelEndpoints, resource);

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
	
	private void createForageDopYieldContract(
			String policyNumber) throws ValidationException, CirrasUnderwritingServiceException {

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
		
		//Add cuts
		AnnualFieldRsrc field1 = getField(fieldId, resource.getFields());

		List<DopYieldFieldForage> dopFields = field1.getDopYieldFieldForageList();
		List<DopYieldFieldForageCut> cuts = new ArrayList<DopYieldFieldForageCut>();
		
		//Silage Corn QTY
		DopYieldFieldForage dyff = getDopYieldFieldForage(ctcSilageCorn, dopFields);
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, silageCornBales, (double)silageCornWeight, (double)0, false));
		dyff.setDopYieldFieldForageCuts(cuts);

		//Alfalfa QTY
		dyff = getDopYieldFieldForage(ctcAlfalfa, dopFields);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, 115, (double)150, (double)78, false)); //75 ton
		dyff.setDopYieldFieldForageCuts(cuts);
		
		DopYieldContractRsrc dopContract = service.createDopYieldContract(topLevelEndpoints, resource);
		
		dopYieldContractCommodityList = dopContract.getDopYieldContractCommodityForageList();
		dopYieldFieldRollupList = dopContract.getDopYieldFieldRollupForageList();

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
	
	private void createGrainDopYieldContract(String policyNumber) throws ValidationException, CirrasUnderwritingServiceException {

		boolean addedYieldGrain = false;
		
		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();
		
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
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNull(referrer.getDeclaredYieldContractGuid());
				
		DopYieldContractRsrc resource = service.rolloverDopYieldContract(referrer);
		
		resource.setDeclarationOfProductionDate(dopDate);
		
		resource.setDefaultYieldMeasUnitTypeCode("TONNE");
		resource.setEnteredYieldMeasUnitTypeCode("TONNE");
		resource.setGrainFromOtherSourceInd(true);

		resource.getDopYieldContractCommodities().clear();

		DopYieldContractCommodity dycc = createDopYieldContractCommodity(16, "BARLEY", 11.22, false, 66.77, 88.99);
		resource.getDopYieldContractCommodities().add(dycc);
		
		dycc = createDopYieldContractCommodity(18, "CANOLA", 33.44, true, 55.66, 77.88);
		resource.getDopYieldContractCommodities().add(dycc);

		dycc = createDopYieldContractCommodity(20, "FALL RYE", null, false, null, null);
		resource.getDopYieldContractCommodities().add(dycc);
			
		// Create field-level data by copying from seeded grain data.
		for ( AnnualFieldRsrc field : resource.getFields() ) {
			if ( field.getDopYieldFieldGrainList() != null) {
				for ( DopYieldFieldGrain yield : field.getDopYieldFieldGrainList() ) {
					yield.setEstimatedYieldPerAcre(yield.getSeededAcres());
					yield.setUnharvestedAcresInd(false);
					
					if ( yield.getEstimatedYieldPerAcre() > 0 ) {
						addedYieldGrain = true;
					}
				}
			}
		}

		service.createDopYieldContract(topLevelEndpoints, resource);

		Assert.assertTrue(addedYieldGrain);
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
		dycc.setStoredYield(storedYield);
		
		return dycc;
	}
	
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	
	@Test
	public void testRolloverForageVerifiedYieldContract() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, DaoException {
		logger.debug("<testRolloverForageVerifiedYieldContract");

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

		createGrowerContractYear(growerContractYearId1, contractId, growerId, cropYear1, InsurancePlans.FORAGE.getInsurancePlanId(), createTransactionDate);
		createPolicy(policyId1, growerId, InsurancePlans.FORAGE.getInsurancePlanId(), policyNumber1, contractNumber, contractId, cropYear1, createTransactionDate);
		createAnnualFieldDetail(annualFieldDetailId1, legalLandId, fieldId, cropYear1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);

		createForageInventoryContract(policyNumber1);
		createForageDopYieldContract(policyNumber1);
		
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
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
		Assert.assertNull(referrer.getVerifiedYieldContractGuid());
		
		// Check VerifiedYieldContract
		VerifiedYieldContractRsrc vyContract = service.rolloverVerifiedYieldContract(referrer);
		Assert.assertNotNull(vyContract);

		Assert.assertEquals(contractId, vyContract.getContractId());
		Assert.assertEquals(cropYear1, vyContract.getCropYear());
		Assert.assertEquals(referrer.getDeclaredYieldContractGuid(), vyContract.getDeclaredYieldContractGuid());
		Assert.assertEquals("TON", vyContract.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(growerContractYearId1, vyContract.getGrowerContractYearId());
		Assert.assertEquals(Integer.valueOf(5), vyContract.getInsurancePlanId());
		Assert.assertNull(vyContract.getVerifiedYieldContractGuid());
		Assert.assertNull(vyContract.getVerifiedYieldUpdateUser());
		Assert.assertNull(vyContract.getVerifiedYieldUpdateTimestamp());

		//Check VerifiedYieldContractCommodities
		//Alfalfa
		DopYieldContractCommodityForage dopCommodity = getDopYieldContractCommodityForage(cropIdForage, ctcAlfalfa, dopYieldContractCommodityList);
		VerifiedYieldContractCommodity verifiedCommodity = getVerifiedYieldContractCommodity(cropIdForage, ctcAlfalfa, vyContract.getVerifiedYieldContractCommodities());

		Assert.assertEquals(dopCommodity.getCropCommodityId(), verifiedCommodity.getCropCommodityId());
		Assert.assertEquals(dopCommodity.getCommodityTypeCode(), verifiedCommodity.getCommodityTypeCode());
		Assert.assertEquals(dopCommodity.getHarvestedAcres(), verifiedCommodity.getHarvestedAcres());
		Assert.assertEquals(dopCommodity.getTotalFieldAcres(), verifiedCommodity.getTotalInsuredAcres());
		Assert.assertEquals(dopCommodity.getQuantityHarvestedTons(), verifiedCommodity.getHarvestedYield(), 0.00005);
		Assert.assertEquals(getYieldPerAcre(verifiedCommodity), verifiedCommodity.getYieldPerAcre(), 0.00005);
		
		//Silage Corn
		DopYieldFieldRollupForage dopRollup = getDopYieldFieldRollupForage(ctcSilageCorn, dopYieldFieldRollupList);
		verifiedCommodity = getVerifiedYieldContractCommodity(cropIdSilageCorn, ctcSilageCorn, vyContract.getVerifiedYieldContractCommodities());

		Assert.assertEquals(cropIdSilageCorn, verifiedCommodity.getCropCommodityId());
		Assert.assertEquals(dopRollup.getCommodityTypeCode(), verifiedCommodity.getCommodityTypeCode());
		Assert.assertEquals(dopRollup.getHarvestedAcres(), verifiedCommodity.getHarvestedAcres());
		Assert.assertEquals(dopRollup.getTotalFieldAcres(), verifiedCommodity.getTotalInsuredAcres());
		Assert.assertEquals(dopRollup.getQuantityHarvestedTons(), verifiedCommodity.getHarvestedYield(), 0.00005);
		Assert.assertEquals(getYieldPerAcre(verifiedCommodity), verifiedCommodity.getYieldPerAcre(), 0.00005);
		
		logger.debug(">testRolloverForageVerifiedYieldContract");
	}
	
	private Double getYieldPerAcre(VerifiedYieldContractCommodity verifiedCommodity) {
		
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
		
		return yieldPerAcre;
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
	
	private DopYieldFieldRollupForage getDopYieldFieldRollupForage(
			String commodityTypeCode, 
			List<DopYieldFieldRollupForage> dyfrfList) {
		
		DopYieldFieldRollupForage dyfrf = null;
		
		List<DopYieldFieldRollupForage> dyfrfFiltered = dyfrfList.stream()
				.filter(x -> (x.getCommodityTypeCode() != null && x.getCommodityTypeCode().equals(commodityTypeCode))
								|| (commodityTypeCode == null && x.getCommodityTypeCode() == null) )
				
				.collect(Collectors.toList());
		
		if (dyfrfFiltered != null) {
			dyfrf = dyfrfFiltered.get(0);
		}
		return dyfrf;
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
	
	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}

}
