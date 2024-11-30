package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ContractedFieldDetailDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	private Integer contractedFieldDetailId = 999199876;
	private Integer legalLandId = 99199999;
	private Integer fieldId = 99999299;
	private Integer annualFieldDetailId = 992888999;

	private String inventoryFieldGuid = null;
	private String inventorySeededGrainGuid = null;
	private String declaredYieldFieldGuid = null;
	

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteContractedFieldDetail();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteContractedFieldDetail();
	}
	
	private void deleteContractedFieldDetail() throws NotFoundDaoException, DaoException{

		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = fieldDao.fetch(fieldId);
		if (fieldDto != null) {
			InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
			invSeededGrainDao.deleteForField(fieldId);
			InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
			invSeededForageDao.deleteForField(fieldId);
			InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
			invFieldDao.deleteForField(fieldId);
		}
		
		ContractedFieldDetailDao dao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto dto = dao.fetchSimple(contractedFieldDetailId);
		
		if (dto != null) {
			
			dao.delete(contractedFieldDetailId);
		}
		
		//Delete annual field detail data
		AnnualFieldDetailDao annualFieldDetailDao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto annualFieldDetailDto = annualFieldDetailDao.fetch(annualFieldDetailId);
		
		if (annualFieldDetailDto != null) {
			annualFieldDetailDao.delete(annualFieldDetailId);
		}	

		// delete field
		if (fieldDto != null) {
			fieldDao.delete(fieldId);
		}
		
		// delete legal land
		LegalLandDao legalLandDao = persistenceSpringConfig.legalLandDao();
		LegalLandDto legalLandDto = legalLandDao.fetch(legalLandId);
		
		if (legalLandDto != null) {
			legalLandDao.delete(legalLandId);
		}

	}
	
	@Test 
	public void testContractedFieldDetail() throws Exception {

		Integer contractId = 2667;
		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		// Integer contractedFieldDetailId = 999888; // Must be a value not already in use.// specified at the top of the class
		Integer growerContractYearId = 89023;
		Integer annualFieldDetailId = 165966;
		Integer fieldId = 14875;
		String fieldLabel = "LOT 8";
		Integer legalLandId = 75;
		String otherLegalDesc = "L 8 BLK 22 SEC 33 TP 26 PL 1249"; // ES: it used to be null but then I updated the OtherLegal to be equal to the ShortLegal, so the description is visible on the inventory screen
		Integer displayOrder = 4;
		Integer numExistingFieldsOnCn = 3;
		
		String userId = "UNITTEST";

		ContractedFieldDetailDao contractedFieldDetailDao = persistenceSpringConfig.contractedFieldDetailDao();

		// INSERT
		ContractedFieldDetailDto newDto = new ContractedFieldDetailDto();

		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setContractedFieldDetailId(contractedFieldDetailId);
		newDto.setCropYear(cropYear);
		newDto.setContractId(contractId);
		newDto.setDisplayOrder(displayOrder);
		newDto.setFieldId(fieldId);
		newDto.setFieldLabel(fieldLabel);
		newDto.setGrowerContractYearId(growerContractYearId);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setLegalLandId(legalLandId);
		newDto.setOtherLegalDescription(otherLegalDesc);

		contractedFieldDetailDao.insertDataSync(newDto, userId);
		
		//SELECT
		List<ContractedFieldDetailDto> dtos = contractedFieldDetailDao.select(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(numExistingFieldsOnCn.intValue() + 1, dtos.size());

		PagedDtos<ContractedFieldDetailDto> pagedDtos = contractedFieldDetailDao.select(contractId, cropYear, "displayOrder", "ASC", 1000, 1, 20);
		Assert.assertNotNull(pagedDtos);
		Assert.assertEquals(numExistingFieldsOnCn.intValue() + 1, pagedDtos.getResults().size());
		
		//FETCH
		ContractedFieldDetailDto fetchedDto = contractedFieldDetailDao.fetch(contractedFieldDetailId);
		
		Assert.assertEquals("AnnualFieldDetailId", newDto.getAnnualFieldDetailId(), fetchedDto.getAnnualFieldDetailId());
		Assert.assertEquals("ContractedFieldDetailId", newDto.getContractedFieldDetailId(), fetchedDto.getContractedFieldDetailId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("DisplayOrder", newDto.getDisplayOrder(), fetchedDto.getDisplayOrder());
		Assert.assertEquals("FieldId", newDto.getFieldId(), fetchedDto.getFieldId());
		Assert.assertEquals("FieldLabel", newDto.getFieldLabel(), fetchedDto.getFieldLabel());
		Assert.assertEquals("GrowerContractYearId", newDto.getGrowerContractYearId(), fetchedDto.getGrowerContractYearId());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("LegalLandId", newDto.getLegalLandId(), fetchedDto.getLegalLandId());
		Assert.assertEquals("OtherLegalDescription", newDto.getOtherLegalDescription(), fetchedDto.getOtherLegalDescription());
		
		//UPDATE
		fetchedDto.setCropYear(cropYear);
		fetchedDto.setContractId(contractId);
		fetchedDto.setDisplayOrder(displayOrder + 1);
		fetchedDto.setFieldId(fieldId);
		fetchedDto.setFieldLabel(fieldLabel);
		fetchedDto.setGrowerContractYearId(growerContractYearId);
		fetchedDto.setInsurancePlanId(insurancePlanId);
		fetchedDto.setLegalLandId(legalLandId);
		fetchedDto.setOtherLegalDescription(otherLegalDesc);
		
		contractedFieldDetailDao.update(fetchedDto, userId);

		//FETCH
		ContractedFieldDetailDto updatedDto = contractedFieldDetailDao.fetch(contractedFieldDetailId);

		Assert.assertEquals("CropYear", fetchedDto.getCropYear(), updatedDto.getCropYear());
		Assert.assertEquals("ContractId", fetchedDto.getContractId(), updatedDto.getContractId());
		Assert.assertEquals("DisplayOrder", fetchedDto.getDisplayOrder(), updatedDto.getDisplayOrder());
		Assert.assertEquals("FieldId", fetchedDto.getFieldId(), updatedDto.getFieldId());
		Assert.assertEquals("FieldLabel", fetchedDto.getFieldLabel(), updatedDto.getFieldLabel());
		Assert.assertEquals("GrowerContractYearId", fetchedDto.getGrowerContractYearId(), updatedDto.getGrowerContractYearId());
		Assert.assertEquals("InsurancePlanId", fetchedDto.getInsurancePlanId(), updatedDto.getInsurancePlanId());
		Assert.assertEquals("LegalLandId", fetchedDto.getLegalLandId(), updatedDto.getLegalLandId());
		Assert.assertEquals("OtherLegalDescription", fetchedDto.getOtherLegalDescription(), updatedDto.getOtherLegalDescription());
		
		//DELETE
		contractedFieldDetailDao.delete(updatedDto.getContractedFieldDetailId());
		
		//FETCH
		ContractedFieldDetailDto deletedDto = contractedFieldDetailDao.fetch(contractedFieldDetailId);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = contractedFieldDetailDao.select(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(numExistingFieldsOnCn.intValue(), dtos.size());

		pagedDtos = contractedFieldDetailDao.select(contractId, cropYear, "displayOrder", "ASC", 1000, 1, 20);
		Assert.assertNotNull(pagedDtos);
		Assert.assertEquals(numExistingFieldsOnCn.intValue(), pagedDtos.getResults().size());
	}
	
	@Test 
	public void testInsertUpdateDeleteContractedFieldDetail() throws Exception {

		LegalLandDao legalLandDao = persistenceSpringConfig.legalLandDao();
		LegalLandDto newlegalLandDto = new LegalLandDto();
		
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		
		AnnualFieldDetailDao annualFieldDetailDao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto newAnnualFieldDetailDto = new AnnualFieldDetailDto();
		
		ContractedFieldDetailDao dao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto newDto = new ContractedFieldDetailDto();

		InventoryFieldDao ifDao = persistenceSpringConfig.inventoryFieldDao();
		InventoryFieldDto ifDto = new InventoryFieldDto();
		
		InventorySeededGrainDao isgDao = persistenceSpringConfig.inventorySeededGrainDao();
		InventorySeededGrainDto isgDto = new InventorySeededGrainDto();
		
		DeclaredYieldFieldDao dyfDao = persistenceSpringConfig.declaredYieldFieldDao();
		DeclaredYieldFieldDto dyfDto = new DeclaredYieldFieldDto();
		
		Integer growerContractYearId = 95204;
		Integer contractId = 3146;
		Integer cropYear = 2022;
		Integer insurancePlanId = 4;
		String insurancePlanName = "GRAIN";
		Integer displayOrder = 7;

		String userId = "JUNIT_TEST";

		// SELECT - No results.
		List<ContractedFieldDetailDto> cfdDtos = dao.selectForYearAndField(2022, fieldId);
		Assert.assertNotNull(cfdDtos);
		Assert.assertEquals(0, cfdDtos.size());

		cfdDtos = dao.selectForField(fieldId);
		Assert.assertNotNull(cfdDtos);
		Assert.assertEquals(0, cfdDtos.size());
		
		//INSERT Legal Land
		newlegalLandDto.setLegalLandId(legalLandId);
		newlegalLandDto.setPrimaryReferenceTypeCode("OTHER");
		newlegalLandDto.setLegalDescription("Legal Description");
		newlegalLandDto.setLegalShortDescription("Short Legal");
		newlegalLandDto.setOtherDescription("Other Description");
		newlegalLandDto.setActiveFromCropYear(2011);
		newlegalLandDto.setActiveToCropYear(2022);

		legalLandDao.insertDataSync(newlegalLandDto, userId);

		// INSERT FIELD
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel("Field Label");
		newFieldDto.setActiveFromCropYear(2012);
		newFieldDto.setActiveToCropYear(2022);

		fieldDao.insertDataSync(newFieldDto, userId);
		
		//INSERT Annual Field Detail record
		newAnnualFieldDetailDto.setAnnualFieldDetailId(annualFieldDetailId);
		newAnnualFieldDetailDto.setLegalLandId(legalLandId);
		newAnnualFieldDetailDto.setFieldId(fieldId);
		newAnnualFieldDetailDto.setCropYear(2022);

		annualFieldDetailDao.insertDataSync(newAnnualFieldDetailDto, userId);
		
		//INSERT Contracted Field Detail record
		newDto.setContractedFieldDetailId(null);
		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setDisplayOrder(displayOrder);
		newDto.setGrowerContractYearId(growerContractYearId);
		
		dao.insert(newDto, userId);

		// SELECT - 1 result.
		cfdDtos = dao.selectForYearAndField(2022, fieldId);
		Assert.assertNotNull(cfdDtos);
		Assert.assertEquals(1, cfdDtos.size());
		Assert.assertEquals(newDto.getContractedFieldDetailId(), cfdDtos.get(0).getContractedFieldDetailId());
		
		cfdDtos = dao.selectForField(fieldId);
		Assert.assertNotNull(cfdDtos);
		Assert.assertEquals(1, cfdDtos.size());
		Assert.assertEquals(newDto.getContractedFieldDetailId(), cfdDtos.get(0).getContractedFieldDetailId());
		
		//Select For Rollover
		ContractedFieldDetailDto forRolloverDto = dao.selectForFieldRollover(fieldId, 2022, 4);

		Assert.assertEquals("ContractedFieldDetailId for rollover", newDto.getContractedFieldDetailId(), forRolloverDto.getContractedFieldDetailId());
		Assert.assertEquals("AnnualFieldDetailId for rollover", newAnnualFieldDetailDto.getAnnualFieldDetailId(), forRolloverDto.getAnnualFieldDetailId());
		Assert.assertEquals("FieldId for rollover", newAnnualFieldDetailDto.getFieldId(), forRolloverDto.getFieldId());
		Assert.assertEquals("CropYear for rollover", newAnnualFieldDetailDto.getCropYear(), forRolloverDto.getCropYear());
		Assert.assertEquals("GrowerContractYearId for rollover", newDto.getGrowerContractYearId(), forRolloverDto.getGrowerContractYearId());
		Assert.assertEquals("InsurancePlanName for rollover", insurancePlanName, forRolloverDto.getInsurancePlanName());
		
		
		//FETCH
		ContractedFieldDetailDto fetchedDto = dao.fetchSimple(newDto.getContractedFieldDetailId());

		Assert.assertEquals("ContractedFieldDetailId", newDto.getContractedFieldDetailId(), fetchedDto.getContractedFieldDetailId());
		Assert.assertEquals("AnnualFieldDetailId", newDto.getAnnualFieldDetailId(), fetchedDto.getAnnualFieldDetailId());
		Assert.assertEquals("DisplayOrder", newDto.getDisplayOrder(), fetchedDto.getDisplayOrder());
		Assert.assertEquals("GrowerContractYearId", newDto.getGrowerContractYearId(), fetchedDto.getGrowerContractYearId());
		
		//UPDATE - only updating the display order
		displayOrder = 2;
		fetchedDto.setDisplayOrder(displayOrder);
		
		dao.updateSync(fetchedDto, userId);
		
		//FETCH
		ContractedFieldDetailDto updatedDto = dao.fetchSimple(fetchedDto.getContractedFieldDetailId());

		Assert.assertEquals("AnnualFieldDetailId", fetchedDto.getAnnualFieldDetailId(), updatedDto.getAnnualFieldDetailId());
		Assert.assertEquals("DisplayOrder", fetchedDto.getDisplayOrder(), updatedDto.getDisplayOrder());
		Assert.assertEquals("GrowerContractYearId", fetchedDto.getGrowerContractYearId(), updatedDto.getGrowerContractYearId());

		// selectForDeclaredYield
		ContractedFieldDetailDto cmpDto = dao.fetch(updatedDto.getContractedFieldDetailId());
		List<ContractedFieldDetailDto> dtos = dao.selectForDeclaredYield(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size()); // Empty, because none of them have seeded inventory or declared yield.

		//select for verified yield
		dtos = dao.selectForVerifiedYield(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size()); // Empty, because none of them have seeded inventory or is forage inventory 

		// Create inventory_field
		ifDto.setCropYear(cropYear);
		ifDto.setFieldId(fieldId);
		ifDto.setInsurancePlanId(insurancePlanId);
		ifDto.setIsHiddenOnPrintoutInd(false);
		ifDto.setPlantingNumber(1);
		
		ifDao.insert(ifDto, userId);
		ifDto = ifDao.fetch(ifDto.getInventoryFieldGuid());
		Assert.assertNotNull(ifDto);
		inventoryFieldGuid = ifDto.getInventoryFieldGuid();
		
		// Create inventory_seeded_grain
		isgDto.setCropCommodityId(16);
		isgDto.setInventoryFieldGuid(ifDto.getInventoryFieldGuid());
		isgDto.setIsPedigreeInd(false);
		isgDto.setIsQuantityInsurableInd(false);
		isgDto.setIsReplacedInd(false);
		isgDto.setIsSpotLossInsurableInd(false);
		isgDto.setSeededAcres(0.0);
		
		isgDao.insert(isgDto, userId);
		isgDto = isgDao.fetch(isgDto.getInventorySeededGrainGuid());
		Assert.assertNotNull(isgDto);
		inventorySeededGrainGuid = isgDto.getInventorySeededGrainGuid();
		
		//INSERT FORAGE
		InventorySeededForageDto isfDto3 = new InventorySeededForageDto();

		isfDto3.setInventoryFieldGuid(inventoryFieldGuid);
		isfDto3.setCommodityTypeCode("CPSW");
		isfDto3.setCropCommodityId(26);
		isfDto3.setCropVarietyId(1010602);
		isfDto3.setCropVarietyName("AAC ENTICE");
		isfDto3.setFieldAcres(10.4);
		isfDto3.setSeedingYear(2020);		
		isfDto3.setSeedingDate(null);		
		isfDto3.setIsIrrigatedInd(true);
		isfDto3.setIsQuantityInsurableInd(false);
		isfDto3.setPlantInsurabilityTypeCode("E1");
		isfDto3.setIsAwpEligibleInd(true);
		
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		invSeededForageDao.insert(isfDto3, userId);
		Assert.assertNotNull(isfDto3.getInventorySeededForageGuid());
		
		
		dtos = dao.selectForDeclaredYield(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size()); // Empty, because none of them have seeded inventory with crop and acres > 0, or declared yield.

		//select for verified yield
		dtos = dao.selectForVerifiedYield(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size()); // Empty, because none of them have seeded inventory with crop and acres > 0 or is forage inventory
		
		
		isgDto.setSeededAcres(11.22);
		isgDao.update(isgDto, userId);
		isgDto = isgDao.fetch(isgDto.getInventorySeededGrainGuid());
		
		dtos = dao.selectForDeclaredYield(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size()); // Only 1 record returned
		ContractedFieldDetailDto cfdYieldDto = dtos.get(0);

		Assert.assertEquals(cmpDto.getAnnualFieldDetailId(), cfdYieldDto.getAnnualFieldDetailId());
		Assert.assertEquals(cmpDto.getContractedFieldDetailId(), cfdYieldDto.getContractedFieldDetailId());
		Assert.assertEquals(cmpDto.getContractId(), cfdYieldDto.getContractId());
		Assert.assertEquals(cmpDto.getCropYear(), cfdYieldDto.getCropYear());
		Assert.assertEquals(cmpDto.getDisplayOrder(), cfdYieldDto.getDisplayOrder());
		Assert.assertEquals(cmpDto.getFieldId(), cfdYieldDto.getFieldId());
		Assert.assertEquals(cmpDto.getFieldLabel(), cfdYieldDto.getFieldLabel());
		Assert.assertEquals(cmpDto.getGrowerContractYearId(), cfdYieldDto.getGrowerContractYearId());
		Assert.assertEquals(cmpDto.getInsurancePlanId(), cfdYieldDto.getInsurancePlanId());
		Assert.assertEquals(cmpDto.getLegalLandId(), cfdYieldDto.getLegalLandId());
		Assert.assertEquals(cmpDto.getOtherLegalDescription(), cfdYieldDto.getOtherLegalDescription());

		//select for verified yield
		dtos = dao.selectForVerifiedYield(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size()); // Only 1 record returned
		cfdYieldDto = dtos.get(0);
				
		Assert.assertEquals(cmpDto.getAnnualFieldDetailId(), cfdYieldDto.getAnnualFieldDetailId());
		Assert.assertEquals(cmpDto.getContractedFieldDetailId(), cfdYieldDto.getContractedFieldDetailId());
		Assert.assertEquals(cmpDto.getContractId(), cfdYieldDto.getContractId());
		Assert.assertEquals(cmpDto.getCropYear(), cfdYieldDto.getCropYear());
		Assert.assertEquals(cmpDto.getDisplayOrder(), cfdYieldDto.getDisplayOrder());
		Assert.assertEquals(cmpDto.getFieldId(), cfdYieldDto.getFieldId());
		Assert.assertEquals(cmpDto.getFieldLabel(), cfdYieldDto.getFieldLabel());
		Assert.assertEquals(cmpDto.getGrowerContractYearId(), cfdYieldDto.getGrowerContractYearId());
		Assert.assertEquals(cmpDto.getInsurancePlanId(), cfdYieldDto.getInsurancePlanId());
		Assert.assertEquals(cmpDto.getLegalLandId(), cfdYieldDto.getLegalLandId());
		Assert.assertEquals(cmpDto.getOtherLegalDescription(), cfdYieldDto.getOtherLegalDescription());
		
		// Remove seeded inventory
		isgDto.setSeededAcres(0.0);
		isgDao.update(isgDto, userId);
		isgDto = isgDao.fetch(isgDto.getInventorySeededGrainGuid());
		
		dtos = dao.selectForDeclaredYield(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size()); // Empty, because none of them have seeded inventory with crop and acres > 0, or declared yield.

		//select for verified yield
		dtos = dao.selectForVerifiedYield(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size()); // Empty, because none of them have seeded inventory or is forage inventory
		
		// Add declared yield field.
		dyfDto.setEstimatedYieldPerAcre(12.34);
		dyfDto.setEstimatedYieldPerAcreDefaultUnit(56.78);
		dyfDto.setInventoryFieldGuid(ifDto.getInventoryFieldGuid());
		dyfDto.setUnharvestedAcresInd(false);
		
		dyfDao.insert(dyfDto, userId);
		dyfDto = dyfDao.fetch(dyfDto.getDeclaredYieldFieldGuid());
		Assert.assertNotNull(dyfDto);
		declaredYieldFieldGuid = dyfDto.getDeclaredYieldFieldGuid();
		
		dtos = dao.selectForDeclaredYield(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size()); // Only 1 record returned
		cfdYieldDto = dtos.get(0);
		
		Assert.assertEquals(cmpDto.getAnnualFieldDetailId(), cfdYieldDto.getAnnualFieldDetailId());
		Assert.assertEquals(cmpDto.getContractedFieldDetailId(), cfdYieldDto.getContractedFieldDetailId());
		Assert.assertEquals(cmpDto.getContractId(), cfdYieldDto.getContractId());
		Assert.assertEquals(cmpDto.getCropYear(), cfdYieldDto.getCropYear());
		Assert.assertEquals(cmpDto.getDisplayOrder(), cfdYieldDto.getDisplayOrder());
		Assert.assertEquals(cmpDto.getFieldId(), cfdYieldDto.getFieldId());
		Assert.assertEquals(cmpDto.getFieldLabel(), cfdYieldDto.getFieldLabel());
		Assert.assertEquals(cmpDto.getGrowerContractYearId(), cfdYieldDto.getGrowerContractYearId());
		Assert.assertEquals(cmpDto.getInsurancePlanId(), cfdYieldDto.getInsurancePlanId());
		Assert.assertEquals(cmpDto.getLegalLandId(), cfdYieldDto.getLegalLandId());
		Assert.assertEquals(cmpDto.getOtherLegalDescription(), cfdYieldDto.getOtherLegalDescription());

		
		//DELETE
		dyfDao.delete(dyfDto.getDeclaredYieldFieldGuid());
		declaredYieldFieldGuid = null;
		
		isgDao.delete(isgDto.getInventorySeededGrainGuid());
		inventorySeededGrainGuid = null;
		
		invSeededForageDao.delete(isfDto3.getInventorySeededForageGuid());
		
		ifDao.delete(ifDto.getInventoryFieldGuid());
		inventoryFieldGuid = null;
		
		dao.delete(updatedDto.getContractedFieldDetailId());

		//FETCH
		ContractedFieldDetailDto deletedDto = dao.fetch(updatedDto.getContractedFieldDetailId());
		Assert.assertNull(deletedDto);
		
		//clean up 
		deleteContractedFieldDetail();

	}
	
	
	@Test 
	public void testDeleteForField() throws Exception {
		
		// INSERT FIELD
		createField();
		
		// INSERT ANNUAL FIELD
		createAnnualField();
		
		// INSERT CONTRACTED FIELD
		createContractedFieldDetail();
		
		ContractedFieldDetailDao dao = persistenceSpringConfig.contractedFieldDetailDao();
		
		//FETCH
		ContractedFieldDetailDto fetchedDto = dao.fetch(contractedFieldDetailId);
		Assert.assertNotNull(fetchedDto);

		dao.deleteForField(fieldId);

		//FETCH
		ContractedFieldDetailDto deletedDto = dao.fetch(contractedFieldDetailId);
		Assert.assertNull(deletedDto);

		//clean up 
		deleteContractedFieldDetail();

	}
	
	private void createField() throws DaoException {
		// INSERT FIELD
		
		String userId = "UNITTEST";

		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel("Test Field Label");
		newFieldDto.setActiveFromCropYear(2011);
		newFieldDto.setActiveToCropYear(null);

		fieldDao.insertDataSync(newFieldDto, userId);
		
	}	


	private void createAnnualField() throws DaoException {
		
		String userId = "UNITTEST";

		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto newDto = new AnnualFieldDetailDto();

		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setLegalLandId(null);
		newDto.setFieldId(fieldId);
		newDto.setCropYear(2022);

		dao.insertDataSync(newDto, userId);
		
	}
	
	
	private void createContractedFieldDetail(
		) throws DaoException {
		String userId = "JUNIT_TEST";
		
		// Must be a value not already in use.// specified at the top of the class
		Integer growerContractYearId = 89023;

		ContractedFieldDetailDao cfdDao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto cfdDto = new ContractedFieldDetailDto();

		// INSERT
		cfdDto.setAnnualFieldDetailId(annualFieldDetailId);
		cfdDto.setContractedFieldDetailId(contractedFieldDetailId);
		cfdDto.setDisplayOrder(1);
		cfdDto.setGrowerContractYearId(growerContractYearId);

		cfdDao.insertDataSync(cfdDto, userId);
	}		
	
}
