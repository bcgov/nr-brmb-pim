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

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandUpdateTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryCoverageTotalForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class DopYieldContractEndpointLandTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(DopYieldContractEndpointLandTest.class);


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
	
	private Integer cropYear = 2021;

	private Integer growerId1 = 90000001;
	private Integer policyId1 = 90000002;
	private Integer gcyId1 = 90000003;
	private Integer contractId1 = 90000004;
	private String policyNumber1 = "998877-21";
	private String contractNumber1 = "998877";
	
	private Integer growerId2 = 90002001;
	private Integer policyId2 = 90002002;
	private Integer gcyId2 = 90002003;
	private Integer contractId2 = 90002004;
	private String policyNumber2 = "118877-21";
	private String contractNumber2 = "118877";
	
	private Integer legalLandId1 = 90000005;
	private Integer fieldId1 = 90000006;
	private Integer annualFieldDetailId1 = 90000007;
	private Integer contractedFieldDetailId1 = 90000008;
		
	private Integer insurancePlanIdForage = 5; //Forage
	private Integer insurancePlanIdGrain = 4; //Grain
	
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

		deleteContract(policyId1);
		deleteContract(policyId2);

		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		service.deleteField(topLevelEndpoints, fieldId1.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId1.toString());
		
		service.deleteGrowerContractYear(topLevelEndpoints, gcyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId1.toString());
		service.deleteGrower(topLevelEndpoints, growerId1.toString());

		service.deleteGrowerContractYear(topLevelEndpoints, gcyId2.toString());
		service.deletePolicy(topLevelEndpoints, policyId2.toString());
		service.deleteGrower(topLevelEndpoints, growerId2.toString());
	}

	protected void deleteContract(Integer policyNumber) throws CirrasUnderwritingServiceException {
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber.toString(),
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
	}

	private static final String ctcAlfalfa = "Alfalfa";
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
	
	@Test
	public void testLandTransferWithDopForage() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testLandTransferWithDopForage");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Create source policy
		UwContractListRsrc searchResults = createGrowerAndPolicy(
				growerId1,
				policyId1,
				contractId1,
				gcyId1, 
				policyNumber1, 
				contractNumber1, 
				insurancePlanIdForage);
		
		createLand();

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer;
		InventoryContractRsrc invContract;
		InventoryContractRsrc fetchedInvContract;

		//Create Inventory
		createForageInventory(searchResults);

		//Create Dop
		DopYieldContractRsrc fetchedDyc = createForageDop();
		
		Assert.assertNotNull(fetchedDyc);
		AnnualFieldRsrc fetchedField = getField(fieldId1, fetchedDyc.getFields());
		
		//Check if cut is there
		Assert.assertEquals(1, fetchedField.getDopYieldFieldForageList().get(0).getDopYieldFieldForageCuts().size());
		
		//Create target policy *****************************************************************************
		searchResults = createGrowerAndPolicy(
				growerId2,
				policyId2,
				contractId2,
				gcyId2, 
				policyNumber2, 
				contractNumber2, 
				insurancePlanIdForage);
		
		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());
		
		invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertEquals(0, invContract.getFields().size());

		//Transfer Field
		AnnualFieldListRsrc fieldList = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				fieldId1.toString(),
				cropYear.toString());

		Assert.assertNotNull(fieldList);

		AnnualFieldRsrc annualField = fieldList.getCollection().get(0);

		//Get field inventory data
		AnnualFieldRsrc rolledoverField = service.rolloverAnnualFieldInventory(annualField, cropYear.toString(), insurancePlanIdForage.toString()); 

		rolledoverField.setTransferFromGrowerContractYearId(gcyId1);
		rolledoverField.setLandUpdateType(LandUpdateTypes.ADD_EXISTING_LAND);
		rolledoverField.setCropYear(cropYear);
		rolledoverField.setDisplayOrder(1);
		
		invContract.getFields().add(rolledoverField);

		//Create inventory contract
		fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		//Check if field is there
		Assert.assertEquals(1, fetchedInvContract.getFields().size());

		//Get inventory contract
		UwContractRsrc uwContractRsrc = getUwContract(policyNumber2);
		
		//Rollover DOP
		Assert.assertNull(uwContractRsrc.getDeclaredYieldContractGuid());
		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(uwContractRsrc);
		
		Assert.assertNotNull(newDyc);
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertEquals(contractId2, newDyc.getContractId());
		Assert.assertEquals(cropYear, newDyc.getCropYear());
		
		//Field 1 checks
		AnnualFieldRsrc field1 = getField(fieldId1, newDyc.getFields());
		Assert.assertEquals(1, field1.getDopYieldFieldForageList().size());
		//Check if cut is gone ********************************************************
		Assert.assertEquals(0, field1.getDopYieldFieldForageList().get(0).getDopYieldFieldForageCuts().size());

		delete();
		
		logger.debug(">testLandTransferWithDopForage");

	}
	
	@Test
	public void testLandTransferWithDopGrain() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testLandTransferWithDopGrain");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Create source policy
		UwContractListRsrc searchResults = createGrowerAndPolicy(
				growerId1,
				policyId1,
				contractId1,
				gcyId1, 
				policyNumber1, 
				contractNumber1, 
				insurancePlanIdGrain);

		createLand();

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer;
		InventoryContractRsrc invContract;
		InventoryContractRsrc fetchedInvContract;

		//Create Inventory
		createGrainInventory(searchResults);

		//Create Dop
		DopYieldContractRsrc fetchedDyc = createGrainDop();
		
		Assert.assertNotNull(fetchedDyc);
		AnnualFieldRsrc fetchedField = getField(fieldId1, fetchedDyc.getFields());
		
		//Check if cut is there
		Assert.assertEquals(1, fetchedField.getDopYieldFieldGrainList().size());
		
		//Create target policy *****************************************************************************
		searchResults = createGrowerAndPolicy(
				growerId2,
				policyId2,
				contractId2,
				gcyId2, 
				policyNumber2, 
				contractNumber2, 
				insurancePlanIdGrain);
		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());
		
		invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertEquals(0, invContract.getFields().size());

		//Transfer Field
		AnnualFieldListRsrc fieldList = service.getAnnualFieldList(
				topLevelEndpoints, 
				null, 
				fieldId1.toString(),
				cropYear.toString());

		Assert.assertNotNull(fieldList);

		AnnualFieldRsrc annualField = fieldList.getCollection().get(0);

		//Get field inventory data
		AnnualFieldRsrc rolledoverField = service.rolloverAnnualFieldInventory(annualField, cropYear.toString(), insurancePlanIdGrain.toString()); 

		rolledoverField.setTransferFromGrowerContractYearId(gcyId1);
		rolledoverField.setLandUpdateType(LandUpdateTypes.ADD_EXISTING_LAND);
		rolledoverField.setCropYear(cropYear);
		rolledoverField.setDisplayOrder(1);
		
		invContract.getFields().add(rolledoverField);

		//Create inventory contract
		fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		//Check if field is there
		Assert.assertEquals(1, fetchedInvContract.getFields().size());

		//Get inventory contract
		UwContractRsrc uwContractRsrc = getUwContract(policyNumber2);
		
		//Rollover DOP
		Assert.assertNull(uwContractRsrc.getDeclaredYieldContractGuid());
		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(uwContractRsrc);
		
		Assert.assertNotNull(newDyc);
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertEquals(contractId2, newDyc.getContractId());
		Assert.assertEquals(cropYear, newDyc.getCropYear());
		
		//Field 1 checks
		AnnualFieldRsrc field1 = getField(fieldId1, newDyc.getFields());
		//Check if dop is gone ********************************************************
		Assert.assertEquals(1, field1.getDopYieldFieldGrainList().size());
		Assert.assertNull(field1.getDopYieldFieldGrainList().get(0).getDeclaredYieldFieldGuid());

		delete();
		
		logger.debug(">testLandTransferWithDopGrain");

	}	

	protected DopYieldContractRsrc createForageDop() throws CirrasUnderwritingServiceException, ValidationException {
		//Get UW contract
		UwContractRsrc uwContractRsrc = getUwContract(policyNumber1);
		
		//Rollover DOP
		Assert.assertNull(uwContractRsrc.getDeclaredYieldContractGuid());
		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(uwContractRsrc);
		
		Assert.assertNotNull(newDyc);
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertEquals(contractId1, newDyc.getContractId());
		Assert.assertEquals(cropYear, newDyc.getCropYear());
		
		//Field 1 checks
		AnnualFieldRsrc field1 = getField(fieldId1, newDyc.getFields());
		Assert.assertEquals(1, field1.getDopYieldFieldForageList().size());

		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2022, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();
		
		newDyc.setDeclarationOfProductionDate(dopDate);
		newDyc.setDefaultYieldMeasUnitTypeCode("TON");
		newDyc.setEnteredYieldMeasUnitTypeCode("TON");
		newDyc.setGrainFromOtherSourceInd(false);
		newDyc.setBalerWagonInfo("Test Baler");
		newDyc.setTotalLivestock(100);
		newDyc.setInsurancePlanId(insurancePlanIdForage);		
		
		//Add empty forage dop cut
		List<DopYieldFieldForage> dopFields = field1.getDopYieldFieldForageList();
		List<DopYieldFieldForageCut> cuts = new ArrayList<DopYieldFieldForageCut>();
		
		DopYieldFieldForage dyff = dopFields.get(0);
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, null, null, null, false)); //Empty Cut
		dyff.setDopYieldFieldForageCuts(cuts);

		//CREATE DOP *********************************************************************************************
		DopYieldContractRsrc fetchedDyc = service.createDopYieldContract(topLevelEndpoints, newDyc);
		return fetchedDyc;
	}
	
	protected DopYieldContractRsrc createGrainDop() throws CirrasUnderwritingServiceException, ValidationException {
		//Get UW contract
		UwContractRsrc uwContractRsrc = getUwContract(policyNumber1);
		
		//Rollover DOP
		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(uwContractRsrc);

		Assert.assertEquals(contractId1, newDyc.getContractId());
		Assert.assertEquals(cropYear, newDyc.getCropYear());
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertTrue(newDyc.getFields().size() == 1);
		
		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2022, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();
		
		newDyc.setDeclarationOfProductionDate(dopDate);
		newDyc.setDefaultYieldMeasUnitTypeCode("TONNE");
		newDyc.setEnteredYieldMeasUnitTypeCode("TONNE");
		newDyc.setGrainFromOtherSourceInd(true);
		newDyc.setBalerWagonInfo("Test Insert");
		newDyc.setTotalLivestock(100);
		newDyc.setInsurancePlanId(insurancePlanIdGrain);
		
		DopYieldFieldGrain dyf1 = newDyc.getFields().get(0).getDopYieldFieldGrainList().get(0);
		
		dyf1.setEstimatedYieldPerAcre(null);
		dyf1.setEstimatedYieldPerAcreDefaultUnit(null); // Calculated by backend
		dyf1.setUnharvestedAcresInd(false);

		//CREATE DOP *********************************************************************************************
		DopYieldContractRsrc fetchedDyc = service.createDopYieldContract(topLevelEndpoints, newDyc);
		return fetchedDyc;
	}


	protected void createForageInventory(UwContractListRsrc searchResults)
			throws CirrasUnderwritingServiceException, ValidationException {
		
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
		
		// Planting 1
		InventoryField planting = createPlanting(field, 1, cropYear, insurancePlanIdForage);
		createInventorySeededForage(planting, cropIdForage, varietyIdAlfalafaGrass, ctcAlfalfa, true, 100.0); //Alfalfa Grass

		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);
		
		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertNotNull(fetchedInvContract.getFields().get(0).getPlantings());
		Assert.assertEquals(1, fetchedInvContract.getFields().get(0).getPlantings().size());
	}
	
	private void createGrainInventory(UwContractListRsrc searchResults) throws ValidationException, CirrasUnderwritingServiceException {

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());

		InventoryContractRsrc resource = service.rolloverInventoryContract(referrer);
		
		resource.setFertilizerInd(false);
		resource.setGrainFromPrevYearInd(true);
		resource.setHerbicideInd(false);
		resource.setInventoryContractGuid(null);
		resource.setOtherChangesComment("other changes comment");
		resource.setOtherChangesInd(true);
		resource.setSeededCropReportSubmittedInd(false);
		resource.setTilliageInd(false);
		resource.setUnseededIntentionsSubmittedInd(false);

		List<InventorySeededGrain> isgList = new ArrayList<InventorySeededGrain>();
		
		InventorySeededGrain isg = new InventorySeededGrain();
		isg.setCommodityTypeCode(null);
		isg.setCropCommodityId(16);
		isg.setCropVarietyId(null);
		isg.setIsPedigreeInd(false);
		isg.setIsQuantityInsurableInd(true);
		isg.setIsReplacedInd(false);
		isg.setIsSpotLossInsurableInd(false);
		isg.setSeededAcres((double)25);
		isg.setSeededDate(null);
		
		isgList.add(isg);
		
		resource.getFields().get(0).getPlantings().get(0).setInventorySeededGrains(isgList);
		
		InventoryUnseeded iu = new InventoryUnseeded();
		iu.setAcresToBeSeeded(null);
		iu.setCropCommodityId(null);
		iu.setIsUnseededInsurableInd(false);

		//Create inventory contract commodities
		List<InventoryContractCommodity> iccList = new ArrayList<InventoryContractCommodity>();
		InventoryContractCommodity invContractCommodity = new InventoryContractCommodity();
		invContractCommodity.setCropCommodityId(16);
		invContractCommodity.setCropCommodityName("BARLEY");
		invContractCommodity.setInventoryContractGuid(null);
		invContractCommodity.setInventoryContractCommodityGuid(null);
		invContractCommodity.setTotalSeededAcres((double)25);
		invContractCommodity.setTotalSpotLossAcres(0.0);
		invContractCommodity.setTotalUnseededAcres(10.0);
		invContractCommodity.setTotalUnseededAcresOverride(56.78);
		invContractCommodity.setIsPedigreeInd(false);
		
		iccList.add(invContractCommodity);
		resource.setCommodities(iccList);
		
		resource = service.createInventoryContract(topLevelEndpoints, resource);
	}	

	protected UwContractRsrc getUwContract(String policyNumber) throws CirrasUnderwritingServiceException {
		UwContractListRsrc searchResults;
		searchResults = service.getUwContractList(
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

		UwContractRsrc uwContractRsrc = searchResults.getCollection().get(0);
		
		Assert.assertNotNull(uwContractRsrc);
		Assert.assertNotNull(uwContractRsrc.getLinks());
		return uwContractRsrc;
	}

	private void createLand() throws CirrasUnderwritingServiceException, ValidationException {

		createLegalLand();
		createField(fieldId1);
		createAnnualFieldDetail(annualFieldDetailId1, fieldId1);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, 1, gcyId1);

	}

	protected UwContractListRsrc createGrowerAndPolicy(Integer growerId, 
			Integer policyId, 
			Integer contractId, 
			Integer gcyId, 
			String policyNumber,
			String contractNumber, 
			Integer insurancePlanId)
			throws ValidationException, CirrasUnderwritingServiceException {
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

		createGrower(growerId);
		createPolicy(policyId, growerId, insurancePlanId, policyNumber, contractNumber, contractId);
		createGrowerContractYear(growerId, insurancePlanId, contractId, gcyId);

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
		return searchResults;
	}

	private AnnualFieldRsrc getField(Integer fieldId, List<AnnualFieldRsrc> contractFields) {
		
		List<AnnualFieldRsrc> fields = contractFields.stream().filter(x -> x.getFieldId().equals(fieldId)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, fields.size());
		
		return fields.get(0);
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

	private InventoryField createPlanting(
			AnnualFieldRsrc field, 
			Integer plantingNumber, 
			Integer cropYear, 
			Integer insurancePlan) {
		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(insurancePlan);
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
			Boolean isQuantityInsurableInd,
			Double fieldAcres) {
		
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
		isf.setPlantInsurabilityTypeCode(null);
		isf.setSeedingYear(planting.getCropYear() - 3);
		
		planting.getInventorySeededForages().add(isf);

		return isf;
	}	
		
	private void createGrower(Integer growerId) throws ValidationException, CirrasUnderwritingServiceException {
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
	
	private void createPolicy(
			Integer policyId, 
			Integer growerId, 
			Integer insurancePlanId, 
			String policyNumber, 
			String contractNumber, 
			Integer contractId) throws ValidationException, CirrasUnderwritingServiceException {

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
	
	private void createGrowerContractYear(
			Integer growerId, 
			Integer insurancePlanId,
			Integer contractId,
			Integer gcyId) throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
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
		resource.setCropYear(cropYear);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		service.synchronizeAnnualFieldDetail(resource);
	}

	private void createContractedFieldDetail(Integer contractedFieldDetailId, Integer annualFieldDetailId, Integer displayOrder, Integer gcyId) throws CirrasUnderwritingServiceException, ValidationException {
		
		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId);
		resource.setAnnualFieldDetailId(annualFieldDetailId);
		resource.setGrowerContractYearId(gcyId);
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
