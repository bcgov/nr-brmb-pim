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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandFieldXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;


public class GrowerContractYearSyncEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(GrowerContractYearSyncEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND,
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.UPDATE_INVENTORY_CONTRACT,
		Scopes.DELETE_INVENTORY_CONTRACT,
		Scopes.GET_INVENTORY_CONTRACT,
		Scopes.GET_DOP_YIELD_CONTRACT,
		Scopes.CREATE_DOP_YIELD_CONTRACT,
		Scopes.UPDATE_DOP_YIELD_CONTRACT,
		Scopes.DELETE_DOP_YIELD_CONTRACT,
		Scopes.GET_GROWER,
		Scopes.GET_POLICY,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer contractId = 90000001;
	private Integer policyId = 90000002;
	private String policyNumber = "998877-22";
	private String contractNumber = "998877";
	private Integer growerId = 90000003;
	private Integer growerContractYearId = 999999999;
	private Integer growerContractYearId1 = 90000004;

	private Integer growerContractYearId2 = 90000005;
	private Integer growerContractYearId3 = 90000006;
	
	private Integer growerId2 = 90000027;
	private Integer policyId2 = 90002502;
	private Integer contractId2 = 90002504;
	private String policyNumber2 = "998815-21";
	private String contractNumber2 = "998815";
	
	// Field 1
	private Integer legalLandId1 = 90000007;
	private Integer fieldId1 = 90000008;
	private Integer annualFieldDetailId1 = 90000009;
	private Integer contractedFieldDetailId1 = 90000010;
	
	// Field 2
	private Integer legalLandId2 = 90000011;
	private Integer fieldId2 = 90000012;
	private Integer annualFieldDetailId2 = 90000013;
	private Integer contractedFieldDetailId2 = 90000014;

	// Field 3
	private Integer legalLandId3 = 90000015;
	private Integer fieldId3 = 90000016;
	private Integer annualFieldDetailId3 = 90000017;
	private Integer contractedFieldDetailId3 = 90000018;
	private Integer annualFieldDetailId4 = 90000019;
	private Integer contractedFieldDetailId4 = 90000020;

	// Field 4
	private Integer legalLandId4 = 90000021;
	private Integer fieldId4 = 90000022;
	private Integer annualFieldDetailId5 = 90000023;
	private Integer contractedFieldDetailId5 = 90000024;
	private Integer annualFieldDetailId6 = 90000025;
	private Integer contractedFieldDetailId6 = 90000026;
	
	
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

		deleteInventoryAndYieldContract(policyNumber);
		deleteInventoryAndYieldContract(policyNumber2);
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

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

		List<AnnualFieldRsrc> fetchedFields = null;
		if ( searchResults != null && searchResults.getCollection().size() > 0 ) {
			UwContractRsrc referrer = searchResults.getCollection().get(0);

			if ( referrer.getInventoryContractGuid() == null ) { 
				InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);

				// Rolling over an InventoryContract is currently the only way to get the list of fields associated with a policy.
				fetchedFields = invContract.getFields();
			}
		}
		
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId2.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId3.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId4.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId5.toString());
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId6.toString());

		if ( fetchedFields != null ) { 
			for ( AnnualFieldRsrc fetchedField : fetchedFields ) { 
				service.deleteContractedFieldDetail(topLevelEndpoints, fetchedField.getContractedFieldDetailId().toString());
			}
		}
		
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId1.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId2.toString());
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId3.toString());

		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId2.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId3.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId4.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId5.toString());
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId6.toString());

		if ( fetchedFields != null ) { 
			for ( AnnualFieldRsrc fetchedField : fetchedFields ) { 
				service.deleteAnnualFieldDetail(topLevelEndpoints, fetchedField.getAnnualFieldDetailId().toString());
			}
		}
		
		LegalLandFieldXrefRsrc llfx = service.getLegalLandFieldXref(topLevelEndpoints, legalLandId1.toString(), fieldId1.toString());
		if ( llfx != null ) { 
			service.deleteLegalLandFieldXref(topLevelEndpoints, legalLandId1.toString(), fieldId1.toString());
		}

		llfx = service.getLegalLandFieldXref(topLevelEndpoints, legalLandId2.toString(), fieldId2.toString());
		if ( llfx != null ) { 
			service.deleteLegalLandFieldXref(topLevelEndpoints, legalLandId2.toString(), fieldId2.toString());
		}

		llfx = service.getLegalLandFieldXref(topLevelEndpoints, legalLandId3.toString(), fieldId3.toString());
		if ( llfx != null ) { 
			service.deleteLegalLandFieldXref(topLevelEndpoints, legalLandId3.toString(), fieldId3.toString());
		}

		llfx = service.getLegalLandFieldXref(topLevelEndpoints, legalLandId4.toString(), fieldId4.toString());
		if ( llfx != null ) { 
			service.deleteLegalLandFieldXref(topLevelEndpoints, legalLandId4.toString(), fieldId4.toString());
		}

		service.deleteField(topLevelEndpoints, fieldId1.toString());
		service.deleteField(topLevelEndpoints, fieldId2.toString());
		service.deleteField(topLevelEndpoints, fieldId3.toString());
		service.deleteField(topLevelEndpoints, fieldId4.toString());

		service.deleteLegalLandSync(topLevelEndpoints, legalLandId1.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId2.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId3.toString());
		service.deleteLegalLandSync(topLevelEndpoints, legalLandId4.toString());

		service.deletePolicy(topLevelEndpoints, policyId.toString());
		service.deletePolicy(topLevelEndpoints, policyId2.toString());
		service.deleteGrower(topLevelEndpoints, growerId.toString());
		service.deleteGrower(topLevelEndpoints, growerId2.toString());
	
	}
	
	public void deleteInventoryAndYieldContract(String policyNumber)
			throws CirrasUnderwritingServiceException {
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		
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
	
	private void createLegalLandFieldXref(Integer fieldId, Integer legalLandId) throws CirrasUnderwritingServiceException, ValidationException {
	
		//CREATE Legal Land - Field Xref
		LegalLandFieldXrefRsrc legalLandFieldXrefResource = new LegalLandFieldXrefRsrc();
		
		legalLandFieldXrefResource.setLegalLandId(legalLandId);
		legalLandFieldXrefResource.setFieldId(fieldId);
		legalLandFieldXrefResource.setTransactionType(LandManagementEventTypes.LegalLandFieldXrefCreated);

		service.synchronizeLegalLandFieldXref(legalLandFieldXrefResource);
	
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
	
	
	@Test
	public void testCreateUpdateDeleteGrowerContractYear() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateGrowerContractYear");
		
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
		createGrower(growerId2, 999777, "grower name 2", createTransactionDate);
		
		//CREATE GrowerContractYear
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(growerContractYearId);
		resource.setContractId(contractId);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(4);
		resource.setCropYear(2022);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearCreated);

		service.synchronizeGrowerContractYear(resource);
		
		GrowerContractYearSyncRsrc fetchedResource = service.getGrowerContractYear(topLevelEndpoints, growerContractYearId.toString()); 

		Assert.assertEquals("GrowerContractYearId 1", resource.getGrowerContractYearId(), fetchedResource.getGrowerContractYearId());
		Assert.assertEquals("ContractId 1", resource.getContractId(), fetchedResource.getContractId());
		Assert.assertEquals("GrowerId 1", resource.getGrowerId(), fetchedResource.getGrowerId());
		Assert.assertEquals("InsurancePlanId 1", resource.getInsurancePlanId(), fetchedResource.getInsurancePlanId());
		Assert.assertEquals("CropYear 1", resource.getCropYear(), fetchedResource.getCropYear());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//UPDATE CODE
		fetchedResource.setContractId(contractId2);
		fetchedResource.setGrowerId(growerId2);
		fetchedResource.setInsurancePlanId(5);
		fetchedResource.setCropYear(2021);
		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearUpdated);

		service.synchronizeGrowerContractYear(fetchedResource);
		
		GrowerContractYearSyncRsrc updatedResource = service.getGrowerContractYear(topLevelEndpoints, growerContractYearId.toString()); 

		Assert.assertEquals("ContractId 2", fetchedResource.getContractId(), updatedResource.getContractId());
		Assert.assertEquals("GrowerId 2", fetchedResource.getGrowerId(), updatedResource.getGrowerId());
		Assert.assertEquals("InsurancePlanId 2", fetchedResource.getInsurancePlanId(), updatedResource.getInsurancePlanId());
		Assert.assertEquals("CropYear 2", fetchedResource.getCropYear(), updatedResource.getCropYear());
		Assert.assertTrue("DataSyncTransDate 2", fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);
		
		//CLEAN UP: DELETE CODE
		updatedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		updatedResource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearDeleted);
		
		service.synchronizeGrowerContractYear(updatedResource);

		delete();
		
		logger.debug(">testCreateUpdateGrowerContractYear");
	}

	
	@Test
	public void testUpdateGrowerContractYearWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateGrowerContractYearWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		Integer contractId = 1870;
		Integer growerId = 525593;
		Integer insurancePlanId = 2;
		Integer cropYear = 2024;

		//CREATE GrowerContractYear
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(growerContractYearId);
		resource.setContractId(contractId);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setCropYear(cropYear);
		resource.setDataSyncTransDate(createTransactionDate);
		
		
		//TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXPECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearDeleted);
		service.synchronizeGrowerContractYear(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearUpdated);

		service.synchronizeGrowerContractYear(resource);

		GrowerContractYearSyncRsrc fetchedResource = service.getGrowerContractYear(topLevelEndpoints, growerContractYearId.toString()); 

		Assert.assertEquals("GrowerContractYearId 1", resource.getGrowerContractYearId(), fetchedResource.getGrowerContractYearId());
		Assert.assertEquals("ContractId 1", resource.getContractId(), fetchedResource.getContractId());
		Assert.assertEquals("GrowerId 1", resource.getGrowerId(), fetchedResource.getGrowerId());
		Assert.assertEquals("InsurancePlanId 1", resource.getInsurancePlanId(), fetchedResource.getInsurancePlanId());
		Assert.assertEquals("CropYear 1", resource.getCropYear(), fetchedResource.getCropYear());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		fetchedResource.setDataSyncTransDate(addSeconds(createTransactionDate, -1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearUpdated);
		service.synchronizeGrowerContractYear(fetchedResource);
		
		GrowerContractYearSyncRsrc notUpdatedResource = service.getGrowerContractYear(topLevelEndpoints, growerContractYearId.toString()); 

		Assert.assertTrue("DataSyncTransDate 2", resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);

		
		//UPDATE GrowerContractYear
		contractId = 1829;
		growerId = 8511;
		insurancePlanId = 3;
		cropYear = 2025;

		notUpdatedResource.setContractId(contractId);
		notUpdatedResource.setGrowerId(growerId);
		notUpdatedResource.setInsurancePlanId(insurancePlanId);
		notUpdatedResource.setCropYear(cropYear);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		notUpdatedResource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearCreated);
		notUpdatedResource.setDataSyncTransDate(addSeconds(createTransactionDate, 10));
		service.synchronizeGrowerContractYear(notUpdatedResource);
		
		GrowerContractYearSyncRsrc updatedResource = service.getGrowerContractYear(topLevelEndpoints, growerContractYearId.toString()); 

		Assert.assertEquals("GrowerContractYearId 2", notUpdatedResource.getGrowerContractYearId(), updatedResource.getGrowerContractYearId());
		Assert.assertEquals("ContractId 2", notUpdatedResource.getContractId(), updatedResource.getContractId());
		Assert.assertEquals("GrowerId 2", notUpdatedResource.getGrowerId(), updatedResource.getGrowerId());
		Assert.assertEquals("InsurancePlanId 2", notUpdatedResource.getInsurancePlanId(), updatedResource.getInsurancePlanId());
		Assert.assertEquals("CropYear 2", notUpdatedResource.getCropYear(), updatedResource.getCropYear());
		Assert.assertTrue("DataSyncTransDate 2", notUpdatedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		//CLEAN UP: DELETE CODE
		service.deleteGrowerContractYear(topLevelEndpoints, growerContractYearId.toString());
		
		logger.debug(">testUpdateGrowerContractYearWithoutRecordNoUpdate");
	}

	@Test
	public void testRolloverGrowerContractYear() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testRolloverGrowerContractYear");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		// Create historical gcy to rollover from.
		createGrower(growerId, 999888, "grower name", createTransactionDate);
		createGrowerContractYear(growerContractYearId1, contractId, null, 2020, 4, createTransactionDate);

		// Field 1: Field and Legal land active.
		createLegalLand("test legal 1234", null, legalLandId1, "999-888-777", 1980, null);
		createField(fieldId1, "LOT 1", 1980, null);
		createLegalLandFieldXref(fieldId1, legalLandId1);
		createAnnualFieldDetail(annualFieldDetailId1, legalLandId1, fieldId1, 2020);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);

		// Field 2: Field and Legal inactive.
		createLegalLand("test legal 5678", null, legalLandId2, "999-888-999", 2011, 2020);
		createField(fieldId2, "LOT 2", 2011, 2020);
		createLegalLandFieldXref(fieldId2, legalLandId2);
		createAnnualFieldDetail(annualFieldDetailId2, legalLandId2, fieldId2, 2020);
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId2, growerContractYearId1, 3); // Display order is 3 to verify it re-calculates.
		
		// Field 3: Already on a different contract, so not rolled-over.
		createLegalLand("test legal 9999", null, legalLandId3, "999-888-000", 1980, null);
		createField(fieldId3, "LOT 3", 1980, null);
		createLegalLandFieldXref(fieldId3, legalLandId3);

		//2020
		createAnnualFieldDetail(annualFieldDetailId3, legalLandId3, fieldId3, 2020);
		createContractedFieldDetail(contractedFieldDetailId3, annualFieldDetailId3, growerContractYearId1, 2);

		//2022
		createGrowerContractYear(growerContractYearId2, 99000001, null, 2022, 4, createTransactionDate);
		createAnnualFieldDetail(annualFieldDetailId4, legalLandId3, fieldId3, 2022);
		createContractedFieldDetail(contractedFieldDetailId4, annualFieldDetailId4, growerContractYearId2, 1);
		
		// Field 4: Already on a different contract, but on a different plan, so is rolled-over.
		createLegalLand("test legal 9988", null, legalLandId4, "999-888-111", 1980, null);
		createField(fieldId4, "LOT 4", 1980, null);
		createLegalLandFieldXref(fieldId4, legalLandId4);

		//2020
		createAnnualFieldDetail(annualFieldDetailId5, legalLandId4, fieldId4, 2020);
		createContractedFieldDetail(contractedFieldDetailId5, annualFieldDetailId5, growerContractYearId1, 4);

		//2022
		createGrowerContractYear(growerContractYearId3, 99000002, null, 2022, 5, createTransactionDate);
		createAnnualFieldDetail(annualFieldDetailId6, legalLandId4, fieldId4, 2022);
		createContractedFieldDetail(contractedFieldDetailId6, annualFieldDetailId6, growerContractYearId3, 1);

		
		
		//CREATE GrowerContractYear for 2022
		createGrowerContractYear(growerContractYearId, contractId, growerId, 2022, 4, createTransactionDate);
		createPolicy(policyId, growerId, 4, policyNumber, contractNumber, contractId, 2022, createTransactionDate);
		
		GrowerContractYearSyncRsrc fetchedResource = service.getGrowerContractYear(topLevelEndpoints, growerContractYearId.toString());

		// Check GCY
		Assert.assertEquals(growerContractYearId, fetchedResource.getGrowerContractYearId());
		Assert.assertEquals(contractId, fetchedResource.getContractId());
		Assert.assertEquals(growerId, fetchedResource.getGrowerId());
		Assert.assertEquals(4, fetchedResource.getInsurancePlanId().intValue());
		Assert.assertEquals(2022, fetchedResource.getCropYear().intValue());

		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

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

		InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);
		Assert.assertNotNull(invContract);

		// Rolling over an InventoryContract is currently the only way to get the list of fields associated with a policy.
		// Check fields
		List<AnnualFieldRsrc> fetchedFields = invContract.getFields();
		
		Assert.assertNotNull(fetchedFields);
		Assert.assertEquals(3, fetchedFields.size());
		
		AnnualFieldRsrc fetchedField1 = fetchedFields.get(0);
		Assert.assertEquals(2022, fetchedField1.getCropYear().intValue());
		Assert.assertEquals(1, fetchedField1.getDisplayOrder().intValue());
		Assert.assertEquals(fieldId1, fetchedField1.getFieldId());
		Assert.assertEquals(legalLandId1, fetchedField1.getLegalLandId());
		
		AnnualFieldRsrc fetchedField2 = fetchedFields.get(1);
		Assert.assertEquals(2022, fetchedField2.getCropYear().intValue());
		Assert.assertEquals(2, fetchedField2.getDisplayOrder().intValue());
		Assert.assertEquals(fieldId2, fetchedField2.getFieldId());
		Assert.assertEquals(legalLandId2, fetchedField2.getLegalLandId());

		AnnualFieldRsrc fetchedField3 = fetchedFields.get(2);
		Assert.assertEquals(2022, fetchedField3.getCropYear().intValue());
		Assert.assertEquals(3, fetchedField3.getDisplayOrder().intValue());
		Assert.assertEquals(fieldId4, fetchedField3.getFieldId());
		Assert.assertEquals(legalLandId4, fetchedField3.getLegalLandId());
		
		
		// Active From/To range should not have changed for Field 1, but should have for Field 2.
		FieldRsrc fldResource = service.getField(topLevelEndpoints, fieldId1.toString());
		Assert.assertEquals(1980, fldResource.getActiveFromCropYear().intValue());
		Assert.assertNull(fldResource.getActiveToCropYear());

		fldResource = service.getField(topLevelEndpoints, fieldId2.toString());
		Assert.assertEquals(2011, fldResource.getActiveFromCropYear().intValue());
		Assert.assertEquals(2022, fldResource.getActiveToCropYear().intValue());

		fldResource = service.getField(topLevelEndpoints, fieldId4.toString());
		Assert.assertEquals(1980, fldResource.getActiveFromCropYear().intValue());
		Assert.assertNull(fldResource.getActiveToCropYear());
		
		
		// Legal Land for Field 1
		LegalLandListRsrc lls = service.getLegalLandList(topLevelEndpoints, "test legal 1234", "999-888-777", null, null, "false", "false", null, null, null, null);
		Assert.assertNotNull(lls);
		Assert.assertEquals(1, lls.getCollection().size());
		
		LegalLandRsrc llResource = lls.getCollection().get(0);
		Assert.assertEquals(legalLandId1, llResource.getLegalLandId());
		Assert.assertEquals(1980, llResource.getActiveFromCropYear().intValue());
		Assert.assertNull(llResource.getActiveToCropYear());

		// Legal Land for Field 2
		lls = service.getLegalLandList(topLevelEndpoints, "test legal 5678", "999-888-999", null, null, "false", "false", null, null, null, null);
		Assert.assertNotNull(lls);
		Assert.assertEquals(1, lls.getCollection().size());
		
		llResource = lls.getCollection().get(0);
		Assert.assertEquals(legalLandId2, llResource.getLegalLandId());
		Assert.assertEquals(2011, llResource.getActiveFromCropYear().intValue());
		Assert.assertEquals(2022, llResource.getActiveToCropYear().intValue());

		// Legal Land for Field 4
		lls = service.getLegalLandList(topLevelEndpoints, "test legal 9988", "999-888-111", null, null, "false", "false", null, null, null, null);
		Assert.assertNotNull(lls);
		Assert.assertEquals(1, lls.getCollection().size());
		
		llResource = lls.getCollection().get(0);
		Assert.assertEquals(legalLandId4, llResource.getLegalLandId());
		Assert.assertEquals(1980, llResource.getActiveFromCropYear().intValue());
		Assert.assertNull(llResource.getActiveToCropYear());
		
		
		delete();
		
		logger.debug(">testRolloverGrowerContractYear");
	}
	

	@Test
	public void testDeleteGrowerContractYearWithInventoryAndYield() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testDeleteGrowerContractYearWithInventoryAndYield");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		// Create Field
		createLegalLand("test legal 1234", null, legalLandId1, "999-888-777", 1980, null);
		createField(fieldId1, "LOT 1", 1980, null);
		createLegalLandFieldXref(fieldId1, legalLandId1);
		createAnnualFieldDetail(annualFieldDetailId1, legalLandId1, fieldId1, 2020);

		//Create Grower
		createGrower(growerId, 999888, "grower name", createTransactionDate);
		
		// Create GRAIN field and gcy 
		createPolicy(policyId, growerId, 4, policyNumber, contractNumber, contractId, 2020, createTransactionDate);
		createGrowerContractYear(growerContractYearId1, contractId, growerId, 2020, 4, createTransactionDate);
		createContractedFieldDetail(contractedFieldDetailId1, annualFieldDetailId1, growerContractYearId1, 1);
		
		// Create FORAGE field and gcy 
		createPolicy(policyId2, growerId, 5, policyNumber2, contractNumber2, contractId2, 2020, createTransactionDate);
		createGrowerContractYear(growerContractYearId2, contractId2, growerId, 2020, 5, createTransactionDate);
		createContractedFieldDetail(contractedFieldDetailId2, annualFieldDetailId1, growerContractYearId2, 1);


		//*************************************************
		//Add Grain Inventory
		//*************************************************
		UwContractRsrc uwContract = getUwContract(policyNumber, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContractGrain = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(1, invContractGrain.getFields().size());
		
		AnnualFieldRsrc field1 = getField(fieldId1, invContractGrain.getFields());

		// Remove default planting.
		field1.getPlantings().remove(0);
		
		// Planting 1
		InventoryField planting = createPlanting(field1, 1, 2020, 4);
		createInventoryUnseeded(planting, 16, true, 100.0); //Barley
		createInventorySeededGrain(planting);

		// Grain Totals
		InventoryContractCommodity icc = createInvCommodities(16, "BARLEY", 100.0);
		invContractGrain.getCommodities().add(icc);
		
		//Create contract
		InventoryContractRsrc grainInvContract = service.createInventoryContract(topLevelEndpoints, invContractGrain);

		String grainInventoryFieldGuid = grainInvContract.getFields().get(0).getPlantings().get(0).getInventoryFieldGuid();
		
		//Check if the data is saved
		Assert.assertNotNull(grainInvContract.getCommodities());
		Assert.assertEquals(1, invContractGrain.getFields().size());
		Assert.assertNotNull(grainInvContract.getFields().get(0).getPlantings());
		Assert.assertNotNull(grainInvContract.getFields().get(0).getPlantings().get(0).getInventoryFieldGuid());
		Assert.assertNotNull(grainInvContract.getFields().get(0).getPlantings().get(0).getInventoryUnseeded());
		Assert.assertNotNull(grainInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededGrains());

		//*************************************************
		//Add Grain DOP
		//*************************************************
		uwContract = getUwContract(policyNumber, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getDeclaredYieldContractGuid());

		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(uwContract);

		newDyc.setDeclarationOfProductionDate(transactionDate);
		newDyc.setDefaultYieldMeasUnitTypeCode("TONNE");
		newDyc.setEnteredYieldMeasUnitTypeCode("BUSHEL");
		newDyc.setGrainFromOtherSourceInd(true);
		newDyc.setInsurancePlanId(4);
		
		AnnualFieldRsrc dopField = newDyc.getFields().get(0);
		
		// Add field level comment
		createFieldComment(dopField);

		// Add contract level comment
		createDopContractComment(newDyc);
		
		DopYieldFieldGrain dyf1 = dopField.getDopYieldFieldGrainList().get(0);
		
		dyf1.setEstimatedYieldPerAcre(11.22);
		dyf1.setEstimatedYieldPerAcreDefaultUnit(null); // Calculated by backend
		dyf1.setUnharvestedAcresInd(true);
		
		//DOP Contract Commodities
		createDopYieldCommodities(newDyc);
		
		// Save DOP
		DopYieldContractRsrc grainDopContract = service.createDopYieldContract(topLevelEndpoints, newDyc);

		//Check if the data is saved
		Assert.assertNotNull(grainDopContract.getDopYieldContractCommodities());
		Assert.assertNotNull(grainDopContract.getDopYieldFieldRollupList());
		Assert.assertNotNull(grainDopContract.getUwComments());
		Assert.assertEquals(1, grainDopContract.getFields().size());
		Assert.assertNotNull(grainDopContract.getFields().get(0).getDopYieldFieldGrainList());
		Assert.assertNotNull(grainDopContract.getFields().get(0).getUwComments());
		
		//*************************************************
		//Add Forage Inventory
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		Assert.assertNull(uwContract.getInventoryContractGuid());
		
		InventoryContractRsrc invContractForage = service.rolloverInventoryContract(uwContract);
		
		Assert.assertEquals(1, invContractForage.getFields().size());
		
		field1 = getField(fieldId1, invContractForage.getFields());

		// Remove default planting.
		field1.getPlantings().remove(0);

		// Forage Planting
		planting = createPlanting(field1, 1, 2020, 5);
		createInventorySeededForage(planting, 65, "W3", true, 11.0, grainInventoryFieldGuid); //FORAGE Winter Survival 3

		//Create contract
		InventoryContractRsrc forageInvContract = service.createInventoryContract(topLevelEndpoints, invContractForage);		
		
		//Check if the data is saved
		Assert.assertNotNull(forageInvContract.getInventoryCoverageTotalForages());
		Assert.assertEquals(1, forageInvContract.getFields().size());
		Assert.assertNotNull(forageInvContract.getFields().get(0).getPlantings());
		Assert.assertNotNull(forageInvContract.getFields().get(0).getPlantings().get(0).getInventoryFieldGuid());
		Assert.assertNotNull(forageInvContract.getFields().get(0).getPlantings().get(0).getInventorySeededForages());

		//*************************************************
		//Add Forage DOP
		//*************************************************
		uwContract = getUwContract(policyNumber2, service, topLevelEndpoints);
		DopYieldContractRsrc newDycf = service.rolloverDopYieldContract(uwContract);
		
		newDycf.setDeclarationOfProductionDate(transactionDate);
		newDycf.setDefaultYieldMeasUnitTypeCode("TON");
		newDycf.setEnteredYieldMeasUnitTypeCode("TON");
		newDycf.setGrainFromOtherSourceInd(false);
		newDycf.setBalerWagonInfo("Test Baler");
		newDycf.setTotalLivestock(100);
		newDycf.setInsurancePlanId(5);	
		
		field1 = getField(fieldId1, newDycf.getFields());
		 
		DopYieldFieldForage dyff = field1.getDopYieldFieldForageList().get(0);
		DopYieldFieldForageCut cut = new DopYieldFieldForageCut();
		cut.setInventoryFieldGuid(dyff.getInventoryFieldGuid());
		cut.setCutNumber(1);
		cut.setTotalBalesLoads(100);
		cut.setWeight(10.0);
		cut.setWeightDefaultUnit(null);
		cut.setMoisturePercent(15.0);
		cut.setDeletedByUserInd(false);
		
		List<DopYieldFieldForageCut> dopYieldFieldForageCuts = new ArrayList<DopYieldFieldForageCut>();
		
		dopYieldFieldForageCuts.add(cut);
		dyff.setDopYieldFieldForageCuts(dopYieldFieldForageCuts);

		//CREATE DOP *********************************************************************************************
		DopYieldContractRsrc forageDyc = service.createDopYieldContract(topLevelEndpoints, newDycf);

		//Check if the data is saved
		Assert.assertNotNull(forageDyc.getDopYieldContractCommodityForageList());
		Assert.assertEquals(1, forageDyc.getDopYieldContractCommodityForageList().size());
		Assert.assertEquals(1, forageDyc.getFields().size());
		Assert.assertNotNull(forageDyc.getFields().get(0).getDopYieldFieldForageList());
		Assert.assertEquals(1, forageDyc.getFields().get(0).getDopYieldFieldForageList().size());
		
		//*************************************************
		//Delete grower contract year of the forage inventory which has to delete all inventory data as well
		//*************************************************
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		resource.setGrowerContractYearId(growerContractYearId2);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearDeleted);
		service.synchronizeGrowerContractYear(resource);
		
		//Delete policy as well
		service.deletePolicy(topLevelEndpoints, policyId2.toString());
		
		checkGrowerContractDeleted(policyNumber2, service, topLevelEndpoints);
		
		//*************************************************
		//Check if Grain data is still there
		//*************************************************
		uwContract = getUwContract(policyNumber, service, topLevelEndpoints);
		InventoryContractRsrc invContract = service.getInventoryContract(uwContract);

		//Check if the data is saved
		Assert.assertNotNull(invContract.getCommodities());
		Assert.assertEquals(1, invContract.getFields().size());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings().get(0).getInventoryFieldGuid());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings().get(0).getInventoryUnseeded());
		Assert.assertNotNull(invContract.getFields().get(0).getPlantings().get(0).getInventorySeededGrains());

		grainDopContract = service.getDopYieldContract(uwContract);

		//Check if the data is saved
		Assert.assertNotNull(grainDopContract.getDopYieldContractCommodities());
		Assert.assertNotNull(grainDopContract.getDopYieldFieldRollupList());
		Assert.assertNotNull(grainDopContract.getUwComments());
		Assert.assertEquals(1, grainDopContract.getFields().size());
		Assert.assertNotNull(grainDopContract.getFields().get(0).getDopYieldFieldGrainList());
		Assert.assertNotNull(grainDopContract.getFields().get(0).getUwComments());
		
		//*************************************************
		//Delete grower contract year of the grain inventory and dop which has to delete all inventory and dop data as well
		//*************************************************
		resource = new GrowerContractYearSyncRsrc();
		resource.setGrowerContractYearId(growerContractYearId1);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearDeleted);
		service.synchronizeGrowerContractYear(resource);
		
		//Delete policy as well
		service.deletePolicy(topLevelEndpoints, policyId.toString());

		checkGrowerContractDeleted(policyNumber, service, topLevelEndpoints);
		
		//Cleanup
		delete();
		
		logger.debug(">testDeleteGrowerContractYearWithInventoryAndYield");
		
		/*
		Use these queries to check if the field level data is deleted (before cleanup delete())
		
		select *
		from annual_field_detail afd
		where afd.annual_field_detail_id = 90000009
		 
		select ifd.insurance_plan_id, ifd.inventory_field_guid, isf.inventory_seeded_forage_guid, iu.inventory_unseeded_guid, isg.inventory_seeded_grain_guid, dyf.declared_yield_field_guid
		from inventory_field ifd
		left join inventory_seeded_forage isf on isf.inventory_field_guid = ifd.inventory_field_guid
		left join inventory_unseeded iu on iu.inventory_field_guid = ifd.inventory_field_guid
		left join inventory_seeded_grain isg on isg.inventory_field_guid = ifd.inventory_field_guid
		left join declared_yield_field dyf on dyf.inventory_field_guid = ifd.inventory_field_guid
		where ifd.field_id = 90000008
		and ifd.crop_year = 2020
		 
		 
		 */
	}
	
	private void createDopYieldCommodities(DopYieldContractRsrc newDyc) {
		
		List<DopYieldContractCommodity> dopCommodities = new ArrayList<DopYieldContractCommodity>();
		
		//Barley
		DopYieldContractCommodity dycc = getDopYieldContractCommodity(16, newDyc.getDopYieldContractCommodities());
		if(dycc != null) {
			dycc.setHarvestedAcres((double)100);
			dycc.setStoredYield((double)50);
			dycc.setStoredYieldDefaultUnit(1.0886); //Conversion factor: 45.93
			dycc.setSoldYield((double)30);
			dycc.setSoldYieldDefaultUnit(0.6532); //Conversion factor: 45.93
			dycc.setGradeModifierTypeCode("BLYFOOD");
			
			dopCommodities.add(dycc);
		}
		
		newDyc.setDopYieldContractCommodities(dopCommodities);
		
	}	
	
	private DopYieldContractCommodity getDopYieldContractCommodity(Integer cropCommodityId, List<DopYieldContractCommodity> dyccList) {
		
		DopYieldContractCommodity dycc = null;
		
		List<DopYieldContractCommodity> dyccFiltered = dyccList.stream()
				.filter(x -> x.getCropCommodityId() == cropCommodityId)
				.collect(Collectors.toList());
		
		if (dyccFiltered != null) {
			dycc = dyccFiltered.get(0);
		}
		return dycc;
	}
	
	private void createDopContractComment(DopYieldContractRsrc newDyc) {
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
		contractUnderwritingComment.setGrowerContractYearId(growerContractYearId1);
	
		contractUwComments.add(contractUnderwritingComment);
		newDyc.setUwComments(contractUwComments);
	}
	
	
	private void createFieldComment(AnnualFieldRsrc field) {
		//UnderwritingComment
		UnderwritingComment underwritingComment = new UnderwritingComment();

		underwritingComment.setAnnualFieldDetailId(field.getAnnualFieldDetailId());
		underwritingComment.setUnderwritingComment("Comment 2nd user");
		underwritingComment.setUnderwritingCommentGuid(null);
		underwritingComment.setUnderwritingCommentTypeCode("INV");
		underwritingComment.setUnderwritingCommentTypeDesc("Inventory");
		
		List<UnderwritingComment> uwComments = field.getUwComments();
		uwComments.add(underwritingComment);
	
	}
	
	public UwContractRsrc getUwContract(String policyNumber,
			CirrasUnderwritingService service, 
			EndpointsRsrc topLevelEndpoints) throws CirrasUnderwritingServiceException {

		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		
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
		
		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() == 1);
		
		UwContractRsrc uwContract = searchResults.getCollection().get(0);
		return uwContract;
	}
	
	public void checkGrowerContractDeleted(String policyNumber,
			CirrasUnderwritingService service, 
			EndpointsRsrc topLevelEndpoints) throws CirrasUnderwritingServiceException {

		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		
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
		
		
		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().isEmpty());
	}	
		
	private AnnualFieldRsrc getField(Integer fieldId, List<AnnualFieldRsrc> contractFields) {
		
		List<AnnualFieldRsrc> fields = contractFields.stream().filter(x -> x.getFieldId().equals(fieldId)) 
		.collect(Collectors.toList());
		
		Assert.assertEquals(1, fields.size());
		
		return fields.get(0);
	}


	private InventoryField createPlanting(AnnualFieldRsrc field, Integer plantingNumber, Integer cropYear, Integer insurancePlanId) {
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
		planting.setUnderseededAcres(14.4);
		planting.setUnderseededCropVarietyId(119);
		planting.setUnderseededCropVarietyName("ALFALFA");

		
		field.getPlantings().add(planting);

		return planting;
	}

	private InventorySeededForage createInventorySeededForage(
			InventoryField planting, 
            Integer cropCommodityId, 
			String plantInsurabilityTypeCode,
			Boolean isQuantityInsurableInd,
			Double fieldAcres,
			String grainInventoryFieldGuid) {
		
		InventorySeededForage isf = new InventorySeededForage();


		isf.setCommodityTypeCode("Alfalfa");
		isf.setCropCommodityId(cropCommodityId);
		isf.setCropVarietyId(119);
		isf.setCropVarietyName("ALFALFA");
		isf.setFieldAcres(fieldAcres);
		isf.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		isf.setIsAwpEligibleInd(false);
		isf.setIsIrrigatedInd(false);
		isf.setIsQuantityInsurableInd(isQuantityInsurableInd);
		isf.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		isf.setSeedingYear(planting.getCropYear() - 3);
		if(grainInventoryFieldGuid != null) {
			isf.setGrainInventoryFieldGuid(grainInventoryFieldGuid);
			isf.setLinkPlantingType(InventoryServiceEnums.LinkPlantingType.ADD_LINK.toString());
		}
		
		planting.getInventorySeededForages().add(isf);

		return isf;
	}	

	private void createInventoryUnseeded(
			InventoryField planting, 
            Integer cropCommodityId, 
            Boolean isUnseededInsurableInd,
            Double acresToBeSeeded) {
		
		InventoryUnseeded inventoryUnseeded = new InventoryUnseeded();
		inventoryUnseeded.setCropCommodityId(cropCommodityId);
		inventoryUnseeded.setIsUnseededInsurableInd(isUnseededInsurableInd);
		inventoryUnseeded.setAcresToBeSeeded(acresToBeSeeded);
		inventoryUnseeded.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		planting.setInventoryUnseeded(inventoryUnseeded);

	}
	

	private void createInventorySeededGrain(InventoryField planting) {

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
		
		planting.setInventorySeededGrains(isgList);

	}
	

	
	private InventoryContractCommodity createInvCommodities(Integer cropId, String cropName, Double totalUnseededAcres) {
		//InventoryContractCommodity
		InventoryContractCommodity invContractCommodity = new InventoryContractCommodity();
		invContractCommodity.setCropCommodityId(cropId);
		invContractCommodity.setCropCommodityName(cropName);
		invContractCommodity.setTotalUnseededAcres(totalUnseededAcres);
		invContractCommodity.setIsPedigreeInd(false);
		return invContractCommodity;
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
