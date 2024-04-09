package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class DeclaredYieldFieldDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	private Integer gcyId1 = 90000001;
	private Integer contractId1 = 90000002;
	private Integer cropYear1 = 2020;

	private Integer fieldId1 = 99999999;
	private Integer annualFieldDetailId1 = 90000003;
	private Integer contractedFieldDetailId1 = 90000004;
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}

	private void delete() throws NotFoundDaoException, DaoException {
		deleteDeclaredYieldField();
		deleteSeededGrain();
		deleteInventoryField();
		deleteDeclaredYieldContract();
		deleteContractedFieldDetail();
		deleteAnnualFieldDetail();
		deleteField();		
		deleteGrowerContractYear();
	}

	private void deleteDeclaredYieldField() throws NotFoundDaoException, DaoException {

		DeclaredYieldFieldDao declaredYieldFieldDao = persistenceSpringConfig.declaredYieldFieldDao();
		declaredYieldFieldDao.deleteForField(fieldId1);
		
	}
	
	private void deleteSeededGrain() throws NotFoundDaoException, DaoException {
		InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
		invSeededGrainDao.deleteForField(fieldId1);

	}

	private void deleteInventoryField() throws NotFoundDaoException, DaoException {

		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		invFieldDao.deleteForField(fieldId1);
		
	}
	
	private void deleteDeclaredYieldContract() throws NotFoundDaoException, DaoException {
		DeclaredYieldContractDao dao = persistenceSpringConfig.declaredYieldContractDao();
		DeclaredYieldContractDto dto = dao.getByContractAndYear(contractId1, cropYear1);
		
		if ( dto != null ) {
			dao.delete(dto.getDeclaredYieldContractGuid());			
		}		
	}

	
	private void deleteAnnualFieldDetail() throws NotFoundDaoException, DaoException {

		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto dto = dao.fetch(annualFieldDetailId1);

		if ( dto != null ) {
			dao.delete(annualFieldDetailId1);			
		}
		
	}
	
	private void deleteField() throws NotFoundDaoException, DaoException{
		
		FieldDao dao = persistenceSpringConfig.fieldDao();
		FieldDto dto = dao.fetch(fieldId1);
		if (dto != null) {
			dao.delete(fieldId1);
		}	
	}

	private void deleteGrowerContractYear() throws NotFoundDaoException, DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto dto = dao.fetch(gcyId1);
		if (dto != null) {
			dao.delete(gcyId1);
		}		
	}
	
	private void deleteContractedFieldDetail() throws NotFoundDaoException, DaoException {

		ContractedFieldDetailDao dao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto dto = dao.fetch(contractedFieldDetailId1);
		
		if (dto != null) {
			
			dao.delete(contractedFieldDetailId1);
		}
	}
	
	
	@Test 
	public void testDeclaredYieldField() throws Exception {

		Integer insurancePlanId = 4;
		String inventoryFieldGuid;
		String declaredYieldFieldGuid;
		Integer plantingNumber = 50;
		Integer cropCommodityId = 20;
		
		String userId = "UNITTEST";
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		DeclaredYieldFieldDao declaredYieldFieldDao = persistenceSpringConfig.declaredYieldFieldDao();
		
		//INSERT Field and contract data
		createGrowerContractYear(userId);
		createField(userId);
		
		createAnnualFieldDetail(userId);
		createContractedFieldDetail(userId);

		createDeclaredYieldContract(userId);

		// INSERT parent InventoryField
		InventoryFieldDto invFieldDto = new InventoryFieldDto();

		invFieldDto.setCropYear(cropYear1);
		invFieldDto.setFieldId(fieldId1);
		invFieldDto.setInsurancePlanId(insurancePlanId);
		invFieldDto.setLastYearCropCommodityId(cropCommodityId);
		invFieldDto.setLastYearCropCommodityName("FALL RYE");
		invFieldDto.setIsHiddenOnPrintoutInd(false);
		invFieldDto.setPlantingNumber(plantingNumber);

		
		invFieldDao.insert(invFieldDto, userId);
		inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		createInventorySeeded(cropCommodityId, inventoryFieldGuid);

		//Check dop yield field record = expected 0
		int totalDopWithYield = declaredYieldFieldDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals("totalDopWithYield 0", 0, totalDopWithYield);

		//INSERT
		DeclaredYieldFieldDto newDto = new DeclaredYieldFieldDto();

		newDto.setEstimatedYieldPerAcre(111.222);
		newDto.setEstimatedYieldPerAcreDefaultUnit(333.444);
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setUnharvestedAcresInd(true);
		
		declaredYieldFieldDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getDeclaredYieldFieldGuid());
		declaredYieldFieldGuid = newDto.getDeclaredYieldFieldGuid();
		
		//Check dop yield field record = expected 1
		totalDopWithYield = declaredYieldFieldDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals("totalDopWithYield 1", 1, totalDopWithYield);
		
		//GET BY INVENTORY FIELD
		DeclaredYieldFieldDto fetchedDto = declaredYieldFieldDao.getByInventoryField(inventoryFieldGuid);
		Assert.assertNotNull(fetchedDto);

		Assert.assertEquals("DeclaredYieldFieldGuid", newDto.getDeclaredYieldFieldGuid(), fetchedDto.getDeclaredYieldFieldGuid());
		Assert.assertEquals("EstimatedYieldPerAcre", newDto.getEstimatedYieldPerAcre(), fetchedDto.getEstimatedYieldPerAcre());
		Assert.assertEquals("EstimatedYieldPerAcreDefaultUnit", newDto.getEstimatedYieldPerAcreDefaultUnit(), fetchedDto.getEstimatedYieldPerAcreDefaultUnit());
		Assert.assertEquals("InventoryFieldGuid", newDto.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("UnharvestedAcresInd", newDto.getUnharvestedAcresInd(), fetchedDto.getUnharvestedAcresInd());
		
		
		//FETCH
		fetchedDto = declaredYieldFieldDao.fetch(declaredYieldFieldGuid);
		
		Assert.assertEquals("DeclaredYieldFieldGuid", newDto.getDeclaredYieldFieldGuid(), fetchedDto.getDeclaredYieldFieldGuid());
		Assert.assertEquals("EstimatedYieldPerAcre", newDto.getEstimatedYieldPerAcre(), fetchedDto.getEstimatedYieldPerAcre());
		Assert.assertEquals("EstimatedYieldPerAcreDefaultUnit", newDto.getEstimatedYieldPerAcreDefaultUnit(), fetchedDto.getEstimatedYieldPerAcreDefaultUnit());
		Assert.assertEquals("InventoryFieldGuid", newDto.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("UnharvestedAcresInd", newDto.getUnharvestedAcresInd(), fetchedDto.getUnharvestedAcresInd());

		//UPDATE
		fetchedDto.setEstimatedYieldPerAcre(222.111);
		fetchedDto.setEstimatedYieldPerAcreDefaultUnit(444.333);
		fetchedDto.setUnharvestedAcresInd(false);

		
		declaredYieldFieldDao.update(fetchedDto, userId);

		//FETCH
		DeclaredYieldFieldDto updatedDto = declaredYieldFieldDao.fetch(declaredYieldFieldGuid);

		Assert.assertEquals("DeclaredYieldFieldGuid", fetchedDto.getDeclaredYieldFieldGuid(), updatedDto.getDeclaredYieldFieldGuid());
		Assert.assertEquals("EstimatedYieldPerAcre", fetchedDto.getEstimatedYieldPerAcre(), updatedDto.getEstimatedYieldPerAcre());
		Assert.assertEquals("EstimatedYieldPerAcreDefaultUnit", fetchedDto.getEstimatedYieldPerAcreDefaultUnit(), updatedDto.getEstimatedYieldPerAcreDefaultUnit());
		Assert.assertEquals("InventoryFieldGuid", fetchedDto.getInventoryFieldGuid(), updatedDto.getInventoryFieldGuid());
		Assert.assertEquals("UnharvestedAcresInd", fetchedDto.getUnharvestedAcresInd(), updatedDto.getUnharvestedAcresInd());
		
		List<DeclaredYieldFieldDto> dtos = declaredYieldFieldDao.selectToRecalculate(cropCommodityId, "BUSHEL", cropYear1, cropYear1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//DELETE
		declaredYieldFieldDao.delete(declaredYieldFieldGuid);
		
		//FETCH
		DeclaredYieldFieldDto deletedDto = declaredYieldFieldDao.fetch(declaredYieldFieldGuid);
		Assert.assertNull(deletedDto);

		//GET BY INVENTORY FIELD
		deletedDto = declaredYieldFieldDao.getByInventoryField(inventoryFieldGuid);
		Assert.assertNull(deletedDto);

		DeclaredYieldFieldDto newDto2 = new DeclaredYieldFieldDto();

		newDto2.setEstimatedYieldPerAcre((double)0);
		newDto2.setEstimatedYieldPerAcreDefaultUnit(333.444);
		newDto2.setInventoryFieldGuid(inventoryFieldGuid);
		newDto2.setUnharvestedAcresInd(true);
		
		declaredYieldFieldDao.insert(newDto2, userId);
		Assert.assertNotNull(newDto2.getDeclaredYieldFieldGuid());
		
		//Check dop yield field record = expected 0
		totalDopWithYield = declaredYieldFieldDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals("totalDopWithYield 2", 0, totalDopWithYield);
		
		//GET BY INVENTORY FIELD
		DeclaredYieldFieldDto fetchedDto2 = declaredYieldFieldDao.getByInventoryField(inventoryFieldGuid);
		Assert.assertNotNull(fetchedDto2);

		//Delete all records for field
		declaredYieldFieldDao.deleteForField(fieldId1);
		fetchedDto2 = declaredYieldFieldDao.getByInventoryField(inventoryFieldGuid);
		Assert.assertNull(fetchedDto2);
				
		//DELETE
		delete();
		
	}

	@Test 
	public void testDeleteForDeclaredYieldContract() throws Exception {

		String userId = "UNITTEST";
		
		DeclaredYieldFieldDao declaredYieldFieldDao = persistenceSpringConfig.declaredYieldFieldDao();

		createGrowerContractYear(userId);
		createField(userId);
		
		createAnnualFieldDetail(userId);
		createContractedFieldDetail(userId);

		String declaredYieldContractGuid = createDeclaredYieldContract(userId);
		
		String invFieldGuid1 = createInventoryField(1, userId);
		String invFieldGuid2 = createInventoryField(2, userId);
		
		//INSERT Planting 1
		DeclaredYieldFieldDto newDto = new DeclaredYieldFieldDto();

		newDto.setEstimatedYieldPerAcre(111.222);
		newDto.setEstimatedYieldPerAcreDefaultUnit(333.444);
		newDto.setInventoryFieldGuid(invFieldGuid1);
		newDto.setUnharvestedAcresInd(true);
		
		declaredYieldFieldDao.insert(newDto, userId);

		//INSERT Planting 2
		newDto = new DeclaredYieldFieldDto();

		newDto.setEstimatedYieldPerAcre(12.34);
		newDto.setEstimatedYieldPerAcreDefaultUnit(56.78);
		newDto.setInventoryFieldGuid(invFieldGuid2);
		newDto.setUnharvestedAcresInd(false);
		
		declaredYieldFieldDao.insert(newDto, userId);
		
		//GET BY INVENTORY FIELD
		DeclaredYieldFieldDto fetchedDto = declaredYieldFieldDao.getByInventoryField(invFieldGuid1);
		Assert.assertNotNull(fetchedDto);

		fetchedDto = declaredYieldFieldDao.getByInventoryField(invFieldGuid2);
		Assert.assertNotNull(fetchedDto);

		//DELETE
		declaredYieldFieldDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
		
		//GET BY INVENTORY FIELD
		fetchedDto = declaredYieldFieldDao.getByInventoryField(invFieldGuid1);
		Assert.assertNull(fetchedDto);

		fetchedDto = declaredYieldFieldDao.getByInventoryField(invFieldGuid2);
		Assert.assertNull(fetchedDto);
		
	}
	
	
	private void createGrowerContractYear(String userId) throws DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto newDto = new GrowerContractYearDto();
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dataSyncTransDate = cal.getTime();
		
		//INSERT
		newDto.setGrowerContractYearId(gcyId1);
		newDto.setContractId(contractId1);
		newDto.setGrowerId(null);
		newDto.setInsurancePlanId(4);
		newDto.setCropYear(cropYear1);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
	}
	
	private void createField(String userId) throws DaoException {
		// INSERT FIELD
		
		String fieldLabel = "Test Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;

		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId1);
		newFieldDto.setFieldLabel(fieldLabel);
		newFieldDto.setActiveFromCropYear(activeFromCropYear);
		newFieldDto.setActiveToCropYear(activeToCropYear);

		fieldDao.insertDataSync(newFieldDto, userId);
	}

	private void createAnnualFieldDetail(String userId) throws DaoException { 
		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto newDto = new AnnualFieldDetailDto();

		//INSERT Annual Field Detail record
		newDto.setAnnualFieldDetailId(annualFieldDetailId1);
		newDto.setLegalLandId(null);
		newDto.setFieldId(fieldId1);
		newDto.setCropYear(cropYear1);

		dao.insertDataSync(newDto, userId);
		
	}
	
	private void createContractedFieldDetail(String userId) throws DaoException {
		
		ContractedFieldDetailDao contractedFieldDetailDao = persistenceSpringConfig.contractedFieldDetailDao();

		// INSERT
		ContractedFieldDetailDto newDto = new ContractedFieldDetailDto();

		newDto.setAnnualFieldDetailId(annualFieldDetailId1);
		newDto.setContractedFieldDetailId(contractedFieldDetailId1);
		newDto.setDisplayOrder(1);
		newDto.setGrowerContractYearId(gcyId1);

		contractedFieldDetailDao.insertDataSync(newDto, userId);
		
	}

	private String createInventoryField(Integer plantingNumber, String userId) throws DaoException { 
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();

		// INSERT InventoryField
		InventoryFieldDto invFieldDto = new InventoryFieldDto();

		invFieldDto.setCropYear(cropYear1);
		invFieldDto.setFieldId(fieldId1);
		invFieldDto.setInsurancePlanId(4);
		invFieldDto.setLastYearCropCommodityId(null);
		invFieldDto.setLastYearCropCommodityName(null);
		invFieldDto.setIsHiddenOnPrintoutInd(false);
		invFieldDto.setPlantingNumber(plantingNumber);
		invFieldDto.setUnderseededAcres(null);
		invFieldDto.setUnderseededCropVarietyId(null);
		
		invFieldDao.insert(invFieldDto, userId);
		
		return invFieldDto.getInventoryFieldGuid();
	}
	
	private void createInventorySeeded(
			Integer cropCommodityId,
			String inventoryFieldGuid
			) throws DaoException {
		
		String userId = "admin";
		
		//Insert Seeded
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
		InventorySeededGrainDto newDto = new InventorySeededGrainDto();

		newDto.setCommodityTypeCode("CPSW");
		newDto.setCommodityTypeDesc("Canadian Prairie Spring Wheat");
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropCommodityName("FALL RYE");
		newDto.setCropVarietyId(1010602);
		newDto.setCropVarietyName("AAC ENTICE");
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setIsPedigreeInd(false);
		newDto.setIsSpotLossInsurableInd(false);
		newDto.setIsQuantityInsurableInd(false);
		newDto.setIsReplacedInd(false);
		newDto.setSeededAcres(10.5);
		newDto.setSeededDate(dateTime);
		
		invSeededGrainDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventorySeededGrainGuid());
		
	}
	
	private String createDeclaredYieldContract(String userId) throws DaoException {

		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();

		DeclaredYieldContractDao dao = persistenceSpringConfig.declaredYieldContractDao();
		
		DeclaredYieldContractDto newDto = new DeclaredYieldContractDto();

		newDto.setContractId(contractId1);
		newDto.setCropYear(cropYear1);
		newDto.setDeclarationOfProductionDate(dopDate);
		newDto.setDefaultYieldMeasUnitTypeCode("TONNE");
		newDto.setEnteredYieldMeasUnitTypeCode("BUSHEL");
		newDto.setGrainFromOtherSourceInd(true);		
		newDto.setBalerWagonInfo(null);
		newDto.setTotalLivestock(null);

		//INSERT
		dao.insert(newDto, userId);		
		
		return newDto.getDeclaredYieldContractGuid();	
	}
	
}
