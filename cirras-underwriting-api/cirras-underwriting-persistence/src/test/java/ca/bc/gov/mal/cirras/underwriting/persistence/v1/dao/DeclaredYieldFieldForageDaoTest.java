package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.time.LocalDate;
import java.time.ZoneId;
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
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class DeclaredYieldFieldForageDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	private Integer contractId1 = 90001002;

	private Integer gcyId1 = 90001001;
	private Integer cropYear1 = 2019;
	private Integer annualFieldDetailId1 = 90010003;
	private Integer contractedFieldDetailId1 = 90001004;

	private Integer fieldId1 = 90002005;
	
	
	private Integer gcyId2 = 90002201;
	private Integer cropYear2 = 2020;
	private Integer annualFieldDetailId2 = 90002203;
	private Integer contractedFieldDetailId2 = 90002204;
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}

	private void delete() throws NotFoundDaoException, DaoException {
		deleteDeclaredYieldContract(contractId1, cropYear1);
		deleteDeclaredYieldContract(contractId1, cropYear2);
		deleteSeededForage();
		deleteInventoryField();
		deleteContractedFieldDetail(contractedFieldDetailId1);
		deleteContractedFieldDetail(contractedFieldDetailId2);
		deleteAnnualFieldDetail(annualFieldDetailId1);
		deleteAnnualFieldDetail(annualFieldDetailId2);
		deleteField();		
		deleteGrowerContractYear(gcyId1);
		deleteGrowerContractYear(gcyId2);
		deleteCropCommodity();
	
	}
	
	private void deleteCropCommodity() throws DaoException {
		CropCommodityDao daoCommodity = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto dtoCommodity = daoCommodity.fetch(cropCommodityId);
		if (dtoCommodity != null) {
			daoCommodity.delete(cropCommodityId);
		}
		
	}

	private void deleteSeededForage() throws NotFoundDaoException, DaoException {
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		invSeededForageDao.deleteForField(fieldId1);
	}

	private void deleteInventoryField() throws NotFoundDaoException, DaoException {

		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		invFieldDao.deleteForField(fieldId1);
		
	}
	
	private void deleteDeclaredYieldContract(Integer contractId, Integer cropYear) throws NotFoundDaoException, DaoException {
		DeclaredYieldContractDao dao = persistenceSpringConfig.declaredYieldContractDao();
		DeclaredYieldContractDto dto = dao.getByContractAndYear(contractId, cropYear);
		
		if ( dto != null ) {
			DeclaredYieldFieldForageDao fieldDao = persistenceSpringConfig.declaredYieldFieldForageDao();
			fieldDao.deleteForDeclaredYieldContract(dto.getDeclaredYieldContractGuid());
			
			dao.delete(dto.getDeclaredYieldContractGuid());			
		}		
	}
	
	private void deleteAnnualFieldDetail(Integer annualFieldDetailId) throws NotFoundDaoException, DaoException {

		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto dto = dao.fetch(annualFieldDetailId);

		if ( dto != null ) {
			dao.delete(annualFieldDetailId);			
		}
		
	}
	
	private void deleteField() throws NotFoundDaoException, DaoException{
		
		FieldDao dao = persistenceSpringConfig.fieldDao();
		FieldDto dto = dao.fetch(fieldId1);
		if (dto != null) {
			dao.delete(fieldId1);
		}	
	}

	private void deleteGrowerContractYear(Integer gcyId) throws NotFoundDaoException, DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto dto = dao.fetch(gcyId);
		if (dto != null) {
			dao.delete(gcyId);
		}		
	}
	
	private void deleteContractedFieldDetail(Integer contractedFieldDetailId) throws NotFoundDaoException, DaoException {

		ContractedFieldDetailDao dao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto dto = dao.fetch(contractedFieldDetailId);
		
		if (dto != null) {
			
			dao.delete(contractedFieldDetailId);
		}
	}
	
	private Integer insurancePlanId = 5;
	private Integer cropCommodityId = 99551122;
	
	@Test 
	public void testDeclaredYieldFieldForage() throws Exception {

		String inventoryFieldGuid;
		String declaredYieldFieldForageGuid;
		Integer plantingNumber = 1;
		
		String userId = "UNITTEST";
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		DeclaredYieldFieldForageDao declaredYieldFieldForageDao = persistenceSpringConfig.declaredYieldFieldForageDao();
		
		//INSERT Field and contract data
		createGrowerContractYear(userId, gcyId1, cropYear1);
		createField(userId);
		
		createAnnualFieldDetail(userId, cropYear1, annualFieldDetailId1);
		createContractedFieldDetail(userId, gcyId1, annualFieldDetailId1, contractedFieldDetailId1);
		
		createCropCommodity();

		createDeclaredYieldContract(userId, cropYear1);

		// INSERT parent InventoryField
		InventoryFieldDto invFieldDto = new InventoryFieldDto();

		invFieldDto.setCropYear(cropYear1);
		invFieldDto.setFieldId(fieldId1);
		invFieldDto.setInsurancePlanId(insurancePlanId);
		invFieldDto.setLastYearCropCommodityId(cropCommodityId);
		invFieldDto.setLastYearCropCommodityName("FORAGE");
		invFieldDto.setLastYearCropVarietyId(null);
		invFieldDto.setLastYearCropVarietyName(null);
		invFieldDto.setIsHiddenOnPrintoutInd(false);
		invFieldDto.setPlantingNumber(plantingNumber);

		
		invFieldDao.insert(invFieldDto, userId);
		inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		createInventorySeeded(inventoryFieldGuid, userId);

		//Check dop yield field record = expected 0
		int totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals("totalDopWithYield 0", 0, totalDopWithYield);		
		
		//INSERT
		DeclaredYieldFieldForageDto newDto = new DeclaredYieldFieldForageDto();

		newDto.setCutNumber(1);
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setMoisturePercent(12.3456);
		newDto.setTotalBalesLoads(10);
		newDto.setWeight(11.2222);
		newDto.setWeightDefaultUnit(33.4444);
		
		
		declaredYieldFieldForageDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getDeclaredYieldFieldForageGuid());
		declaredYieldFieldForageGuid = newDto.getDeclaredYieldFieldForageGuid();

		//Check dop yield field record = expected 1
		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals("totalDopWithYield 1", 1, totalDopWithYield);
		
		//GET BY INVENTORY FIELD
		List<DeclaredYieldFieldForageDto> fetchedDtoList = declaredYieldFieldForageDao.getByInventoryField(inventoryFieldGuid);
		Assert.assertNotNull(fetchedDtoList);
		Assert.assertEquals(1, fetchedDtoList.size());

		DeclaredYieldFieldForageDto fetchedDto = fetchedDtoList.get(0);
		
		Assert.assertEquals("DeclaredYieldFieldForageGuid", newDto.getDeclaredYieldFieldForageGuid(), fetchedDto.getDeclaredYieldFieldForageGuid());
		Assert.assertEquals("CutNumber", newDto.getCutNumber(), fetchedDto.getCutNumber());
		Assert.assertEquals("InventoryFieldGuid", newDto.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("MoisturePercent", newDto.getMoisturePercent(), fetchedDto.getMoisturePercent());
		Assert.assertEquals("TotalBalesLoads", newDto.getTotalBalesLoads(), fetchedDto.getTotalBalesLoads());
		Assert.assertEquals("Weight", newDto.getWeight(), fetchedDto.getWeight());
		Assert.assertEquals("WeightDefaultUnit", newDto.getWeightDefaultUnit(), fetchedDto.getWeightDefaultUnit());
		
		//FETCH
		fetchedDto = declaredYieldFieldForageDao.fetch(declaredYieldFieldForageGuid);
		
		Assert.assertEquals("DeclaredYieldFieldForageGuid", newDto.getDeclaredYieldFieldForageGuid(), fetchedDto.getDeclaredYieldFieldForageGuid());
		Assert.assertEquals("CutNumber", newDto.getCutNumber(), fetchedDto.getCutNumber());
		Assert.assertEquals("InventoryFieldGuid", newDto.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("MoisturePercent", newDto.getMoisturePercent(), fetchedDto.getMoisturePercent());
		Assert.assertEquals("TotalBalesLoads", newDto.getTotalBalesLoads(), fetchedDto.getTotalBalesLoads());
		Assert.assertEquals("Weight", newDto.getWeight(), fetchedDto.getWeight());
		Assert.assertEquals("WeightDefaultUnit", newDto.getWeightDefaultUnit(), fetchedDto.getWeightDefaultUnit());
		
		
		//UPDATE
		fetchedDto.setCutNumber(2);
		fetchedDto.setMoisturePercent(65.4321);
		fetchedDto.setTotalBalesLoads(11);
		fetchedDto.setWeight(22.1111);
		fetchedDto.setWeightDefaultUnit(44.3333);
		
		declaredYieldFieldForageDao.update(fetchedDto, userId);

		//FETCH
		DeclaredYieldFieldForageDto updatedDto = declaredYieldFieldForageDao.fetch(declaredYieldFieldForageGuid);

		Assert.assertEquals("DeclaredYieldFieldForageGuid", fetchedDto.getDeclaredYieldFieldForageGuid(), updatedDto.getDeclaredYieldFieldForageGuid());
		Assert.assertEquals("CutNumber", fetchedDto.getCutNumber(), updatedDto.getCutNumber());
		Assert.assertEquals("InventoryFieldGuid", fetchedDto.getInventoryFieldGuid(), updatedDto.getInventoryFieldGuid());
		Assert.assertEquals("MoisturePercent", fetchedDto.getMoisturePercent(), updatedDto.getMoisturePercent());
		Assert.assertEquals("TotalBalesLoads", fetchedDto.getTotalBalesLoads(), updatedDto.getTotalBalesLoads());
		Assert.assertEquals("Weight", fetchedDto.getWeight(), updatedDto.getWeight());
		Assert.assertEquals("WeightDefaultUnit", fetchedDto.getWeightDefaultUnit(), updatedDto.getWeightDefaultUnit());

		// INSERT second record
		DeclaredYieldFieldForageDto newDto2 = new DeclaredYieldFieldForageDto();

		newDto2.setCutNumber(3);
		newDto2.setInventoryFieldGuid(inventoryFieldGuid);
		newDto2.setMoisturePercent(99.8888);
		newDto2.setTotalBalesLoads(5);
		newDto2.setWeight(55.6666);
		newDto2.setWeightDefaultUnit(77.8888);
				
		declaredYieldFieldForageDao.insert(newDto2, userId);
		Assert.assertNotNull(newDto2.getDeclaredYieldFieldForageGuid());

		//Check dop yield field record = expected 2
		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals("totalDopWithYield 2", 2, totalDopWithYield);
		
		//GET BY INVENTORY FIELD
		fetchedDtoList = declaredYieldFieldForageDao.getByInventoryField(inventoryFieldGuid);
		Assert.assertNotNull(fetchedDtoList);
		Assert.assertEquals(2, fetchedDtoList.size());

		Assert.assertEquals("DeclaredYieldFieldForageGuid", newDto.getDeclaredYieldFieldForageGuid(), fetchedDtoList.get(0).getDeclaredYieldFieldForageGuid());
		Assert.assertEquals("DeclaredYieldFieldForageGuid", newDto2.getDeclaredYieldFieldForageGuid(), fetchedDtoList.get(1).getDeclaredYieldFieldForageGuid());

		List<DeclaredYieldFieldForageDto> dtos = declaredYieldFieldForageDao.selectToRecalculate(cropCommodityId, "LB", cropYear1, cropYear1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		//DELETE
		declaredYieldFieldForageDao.delete(declaredYieldFieldForageGuid);
		
		//FETCH
		DeclaredYieldFieldForageDto deletedDto = declaredYieldFieldForageDao.fetch(declaredYieldFieldForageGuid);
		Assert.assertNull(deletedDto);

		//GET BY INVENTORY FIELD
		List<DeclaredYieldFieldForageDto> deletedDtoList = declaredYieldFieldForageDao.getByInventoryField(inventoryFieldGuid);
		Assert.assertNotNull(deletedDtoList);
		Assert.assertEquals(1, deletedDtoList.size());
		
		//Delete all records for field
		declaredYieldFieldForageDao.deleteForField(fieldId1);
		deletedDtoList = declaredYieldFieldForageDao.getByInventoryField(inventoryFieldGuid);
		Assert.assertNotNull(deletedDtoList);
		Assert.assertEquals(0, deletedDtoList.size());

		//Check dop yield field record = expected 0
		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals("totalDopWithYield 3", 0, totalDopWithYield);
				
		//FETCH
		deletedDto = declaredYieldFieldForageDao.fetch(newDto2.getDeclaredYieldFieldForageGuid());
		Assert.assertNull(deletedDto);

		//GET BY INVENTORY FIELD
		deletedDtoList = declaredYieldFieldForageDao.getByInventoryField(inventoryFieldGuid);
		Assert.assertNotNull(deletedDtoList);
		Assert.assertEquals(0, deletedDtoList.size());		
		
		//DELETE
		delete();		
	}
	
	@Test 
	public void testTotalDopRecordsWithYield() throws Exception {

		String inventoryFieldGuid;
		String declaredYieldFieldForageGuid;
		Integer plantingNumber = 1;
		
		String userId = "UNITTEST";
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		DeclaredYieldFieldForageDao declaredYieldFieldForageDao = persistenceSpringConfig.declaredYieldFieldForageDao();
		
		createField(userId);
		
		//A: Field with no contract, no plantings, no dop.
		int totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals(0, totalDopWithYield);

		
		//INSERT Field and contract data
		createGrowerContractYear(userId, gcyId1, cropYear1);
		
		createAnnualFieldDetail(userId, cropYear1, annualFieldDetailId1);
		createContractedFieldDetail(userId, gcyId1, annualFieldDetailId1, contractedFieldDetailId1);
		
		//B: Field with contract, no plantings, no dop.
		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals(0, totalDopWithYield);

		// INSERT parent InventoryField
		InventoryFieldDto invFieldDto = new InventoryFieldDto();

		invFieldDto.setCropYear(cropYear1);
		invFieldDto.setFieldId(fieldId1);
		invFieldDto.setInsurancePlanId(insurancePlanId);
		invFieldDto.setLastYearCropCommodityId(null);
		invFieldDto.setLastYearCropCommodityName(null);
		invFieldDto.setIsHiddenOnPrintoutInd(false);
		invFieldDto.setPlantingNumber(plantingNumber);

		
		invFieldDao.insert(invFieldDto, userId);
		inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		createInventorySeeded(inventoryFieldGuid, "Clover/Grass", 65, 220, 12.34, true, userId);

		//C: Field with contract, plantings, no dop.
		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals(0, totalDopWithYield);		

		createDeclaredYieldContract(userId, cropYear1);

		//D: Field with contract, plantings, declared_yield_contract but no declared_yield_field_forage.
		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals(1, totalDopWithYield);

		deleteSeededForage();
		createInventorySeeded(inventoryFieldGuid, "Clover/Grass", 65, 220, 12.34, false, userId);

		//E: Field with contract, plantings, declared_yield_contract, but not quantity insurable.
		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals(0, totalDopWithYield);
		
		deleteSeededForage();
		createInventorySeeded(inventoryFieldGuid, null, 65, 220, 12.34, true, userId);

		//F: Field with contract, plantings, declared_yield_contract, but no commodity type.
		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals(0, totalDopWithYield);

		deleteSeededForage();
		createInventorySeeded(inventoryFieldGuid, "Clover/Grass", 65, 220, 0.0, true, userId);

		//G: Field with contract, plantings, declared_yield_contract, but not field acres.
		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals(0, totalDopWithYield);
		
		//INSERT
		DeclaredYieldFieldForageDto newDto = new DeclaredYieldFieldForageDto();

		newDto.setCutNumber(1);
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setMoisturePercent(12.3456);
		newDto.setTotalBalesLoads(10);
		newDto.setWeight(11.2222);
		newDto.setWeightDefaultUnit(33.4444);
		
		
		declaredYieldFieldForageDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getDeclaredYieldFieldForageGuid());
		declaredYieldFieldForageGuid = newDto.getDeclaredYieldFieldForageGuid();


		//H: Field with dop.
		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals(1, totalDopWithYield);
		
		//I: Empty dop.
		//FETCH
		DeclaredYieldFieldForageDto fetchedDto = declaredYieldFieldForageDao.fetch(declaredYieldFieldForageGuid);
		
		//UPDATE
		fetchedDto.setCutNumber(1);
		fetchedDto.setMoisturePercent(0.15);
		fetchedDto.setTotalBalesLoads(0);
		fetchedDto.setWeight(0.0);
		fetchedDto.setWeightDefaultUnit(0.0);
		
		declaredYieldFieldForageDao.update(fetchedDto, userId);

		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals(0, totalDopWithYield);

		
		//J: Dop with Bales.
		//FETCH
		fetchedDto = declaredYieldFieldForageDao.fetch(declaredYieldFieldForageGuid);
		
		//UPDATE
		fetchedDto.setCutNumber(1);
		fetchedDto.setMoisturePercent(0.15);
		fetchedDto.setTotalBalesLoads(10);
		fetchedDto.setWeight(0.0);
		fetchedDto.setWeightDefaultUnit(0.0);
		
		declaredYieldFieldForageDao.update(fetchedDto, userId);

		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals(1, totalDopWithYield);
		
		//K: Dop with weight.
		//FETCH
		fetchedDto = declaredYieldFieldForageDao.fetch(declaredYieldFieldForageGuid);
		
		//UPDATE
		fetchedDto.setCutNumber(1);
		fetchedDto.setMoisturePercent(0.15);
		fetchedDto.setTotalBalesLoads(0);
		fetchedDto.setWeight(11.2222);
		fetchedDto.setWeightDefaultUnit(33.4444);
		
		declaredYieldFieldForageDao.update(fetchedDto, userId);

		totalDopWithYield = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId1, cropYear1, insurancePlanId);
		Assert.assertEquals(1, totalDopWithYield);
		
		//DELETE
		declaredYieldFieldForageDao.delete(declaredYieldFieldForageGuid);
		
		delete();		
	}
	
	@Test 
	public void testDeleteForDeclaredYieldContract() throws Exception {

		String userId = "UNITTEST";
		
		DeclaredYieldFieldForageDao declaredYieldFieldForageDao = persistenceSpringConfig.declaredYieldFieldForageDao();

		createGrowerContractYear(userId, gcyId1, cropYear1);
		createField(userId);
		
		createAnnualFieldDetail(userId, cropYear1, annualFieldDetailId1);
		createContractedFieldDetail(userId, gcyId1, annualFieldDetailId1, contractedFieldDetailId1);

		createCropCommodity();
		
		String declaredYieldContractGuid = createDeclaredYieldContract(userId, cropYear1);
		
		String invFieldGuid1 = createInventoryField(1, userId, cropYear1);
		String invFieldGuid2 = createInventoryField(2, userId, cropYear1);
		
		//INSERT Planting 1
		DeclaredYieldFieldForageDto newDto = new DeclaredYieldFieldForageDto();

		newDto.setCutNumber(1);
		newDto.setInventoryFieldGuid(invFieldGuid1);
		newDto.setMoisturePercent(12.3456);
		newDto.setTotalBalesLoads(10);
		newDto.setWeight(11.2222);
		newDto.setWeightDefaultUnit(33.4444);
				
		declaredYieldFieldForageDao.insert(newDto, userId);

		//INSERT Planting 2
		newDto = new DeclaredYieldFieldForageDto();

		newDto.setCutNumber(1);
		newDto.setInventoryFieldGuid(invFieldGuid2);
		newDto.setMoisturePercent(12.3456);
		newDto.setTotalBalesLoads(10);
		newDto.setWeight(11.2222);
		newDto.setWeightDefaultUnit(33.4444);
		
		declaredYieldFieldForageDao.insert(newDto, userId);
		
		//GET BY INVENTORY FIELD
		List<DeclaredYieldFieldForageDto> fetchedDtos = declaredYieldFieldForageDao.getByInventoryField(invFieldGuid1);
		Assert.assertNotNull(fetchedDtos);

		fetchedDtos = declaredYieldFieldForageDao.getByInventoryField(invFieldGuid2);
		Assert.assertNotNull(fetchedDtos);
		
		//DELETE
		declaredYieldFieldForageDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
		
		//GET BY INVENTORY FIELD
		fetchedDtos = declaredYieldFieldForageDao.getByInventoryField(invFieldGuid1);
		Assert.assertNotNull(fetchedDtos);
		Assert.assertEquals(0, fetchedDtos.size());

		fetchedDtos = declaredYieldFieldForageDao.getByInventoryField(invFieldGuid2);
		Assert.assertNotNull(fetchedDtos);
		Assert.assertEquals(0, fetchedDtos.size());
		
		//DELETE
		delete();		
		
	}	
	
	@Test 
	public void testDeleteForFieldAndYear() throws Exception {

		String userId = "UNITTEST";
		
		DeclaredYieldFieldForageDao declaredYieldFieldForageDao = persistenceSpringConfig.declaredYieldFieldForageDao();

		createField(userId);
		
		createCropCommodity();
		
		//2019
		createGrowerContractYear(userId, gcyId1, cropYear1);
		createAnnualFieldDetail(userId, cropYear1, annualFieldDetailId1);
		createContractedFieldDetail(userId, gcyId1, annualFieldDetailId1, contractedFieldDetailId1);
		createDeclaredYieldContract(userId, cropYear1);
		String invFieldGuid1 = createInventoryField(1, userId, cropYear1);

		//2020
		createGrowerContractYear(userId, gcyId2, cropYear2);
		createAnnualFieldDetail(userId, cropYear2, annualFieldDetailId2);
		createContractedFieldDetail(userId, gcyId2, annualFieldDetailId2, contractedFieldDetailId2);
		createDeclaredYieldContract(userId, cropYear2);
		String invFieldGuid2 = createInventoryField(2, userId, cropYear2);
		
		//INSERT Planting 2019
		DeclaredYieldFieldForageDto newDto = new DeclaredYieldFieldForageDto();

		newDto.setCutNumber(1);
		newDto.setInventoryFieldGuid(invFieldGuid1);
		newDto.setMoisturePercent(12.3456);
		newDto.setTotalBalesLoads(10);
		newDto.setWeight(11.2222);
		newDto.setWeightDefaultUnit(33.4444);
				
		declaredYieldFieldForageDao.insert(newDto, userId);

		//INSERT Planting 2020
		newDto = new DeclaredYieldFieldForageDto();

		newDto.setCutNumber(1);
		newDto.setInventoryFieldGuid(invFieldGuid2);
		newDto.setMoisturePercent(12.3456);
		newDto.setTotalBalesLoads(10);
		newDto.setWeight(11.2222);
		newDto.setWeightDefaultUnit(33.4444);
		
		declaredYieldFieldForageDao.insert(newDto, userId);
		
		//GET BY INVENTORY FIELD
		List<DeclaredYieldFieldForageDto> fetchedDtos = declaredYieldFieldForageDao.getByInventoryField(invFieldGuid1);
		Assert.assertNotNull(fetchedDtos);
		Assert.assertEquals(1, fetchedDtos.size());

		fetchedDtos = declaredYieldFieldForageDao.getByInventoryField(invFieldGuid2);
		Assert.assertNotNull(fetchedDtos);
		Assert.assertEquals(1, fetchedDtos.size());
		
		//DELETE 2020 data
		declaredYieldFieldForageDao.deleteForFieldAndYear(fieldId1, cropYear2);
		
		//GET BY INVENTORY FIELD
		fetchedDtos = declaredYieldFieldForageDao.getByInventoryField(invFieldGuid1);
		Assert.assertNotNull(fetchedDtos);
		Assert.assertEquals(1, fetchedDtos.size());

		fetchedDtos = declaredYieldFieldForageDao.getByInventoryField(invFieldGuid2);
		Assert.assertNotNull(fetchedDtos);
		Assert.assertEquals(0, fetchedDtos.size());
		
		delete();
		
	}	
	
	private void createGrowerContractYear(String userId, Integer gcyId, Integer cropYear) throws DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto newDto = new GrowerContractYearDto();
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dataSyncTransDate = cal.getTime();
		
		//INSERT
		newDto.setGrowerContractYearId(gcyId);
		newDto.setContractId(contractId1);
		newDto.setGrowerId(null);
		newDto.setInsurancePlanId(5);
		newDto.setCropYear(cropYear);
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

	private void createAnnualFieldDetail(String userId, Integer cropYear, Integer annualFieldDetailId) throws DaoException { 
		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto newDto = new AnnualFieldDetailDto();

		//INSERT Annual Field Detail record
		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setLegalLandId(null);
		newDto.setFieldId(fieldId1);
		newDto.setCropYear(cropYear);

		dao.insertDataSync(newDto, userId);
		
	}
	
	private void createContractedFieldDetail(String userId, Integer gcyId, Integer annualFieldDetailId, Integer contractedFieldDetailId) throws DaoException {
		
		ContractedFieldDetailDao contractedFieldDetailDao = persistenceSpringConfig.contractedFieldDetailDao();

		// INSERT
		ContractedFieldDetailDto newDto = new ContractedFieldDetailDto();

		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setContractedFieldDetailId(contractedFieldDetailId);
		newDto.setDisplayOrder(1);
		newDto.setIsLeasedInd(false);
		newDto.setGrowerContractYearId(gcyId);

		contractedFieldDetailDao.insertDataSync(newDto, userId);
		
	}
	
	private String createInventoryField(Integer plantingNumber, String userId, Integer cropYear) throws DaoException { 
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();

		// INSERT InventoryField
		InventoryFieldDto invFieldDto = new InventoryFieldDto();

		invFieldDto.setCropYear(cropYear);
		invFieldDto.setFieldId(fieldId1);
		invFieldDto.setInsurancePlanId(insurancePlanId);
		invFieldDto.setLastYearCropCommodityId(cropCommodityId);
		invFieldDto.setLastYearCropCommodityName("FORAGE");
		invFieldDto.setLastYearCropVarietyId(null);
		invFieldDto.setLastYearCropVarietyName(null);
		invFieldDto.setIsHiddenOnPrintoutInd(false);
		invFieldDto.setPlantingNumber(plantingNumber);

		invFieldDao.insert(invFieldDto, userId);
		return invFieldDto.getInventoryFieldGuid();
	}
	
	private void createInventorySeeded(
			String inventoryFieldGuid,
			String userId
			) throws DaoException {
		
		//Insert Seeded
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		InventorySeededForageDto newDto = new InventorySeededForageDto();

		newDto.setCommodityTypeCode(null);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropVarietyId(null);
		newDto.setFieldAcres(45.67);
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setIsAwpEligibleInd(true);
		newDto.setIsIrrigatedInd(false);
		newDto.setIsQuantityInsurableInd(false);
		newDto.setPlantInsurabilityTypeCode(null);
		newDto.setSeedingDate(null);
		newDto.setSeedingYear(2010);
		
				
		invSeededForageDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventorySeededForageGuid());		
	}

	private void createInventorySeeded(
			String inventoryFieldGuid,
			String commodityTypeCode,
			Integer cropCommodityId,
			Integer cropVarietyId,
			Double fieldAcres,
			Boolean isQuantityInsurable,
			String userId
			) throws DaoException {
		
		//Insert Seeded
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		InventorySeededForageDto newDto = new InventorySeededForageDto();

		newDto.setCommodityTypeCode(commodityTypeCode);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropVarietyId(cropVarietyId);
		newDto.setFieldAcres(fieldAcres);
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setIsAwpEligibleInd(true);
		newDto.setIsIrrigatedInd(false);
		newDto.setIsQuantityInsurableInd(isQuantityInsurable);
		newDto.setPlantInsurabilityTypeCode(null);
		newDto.setSeedingDate(null);
		newDto.setSeedingYear(2010);
		
				
		invSeededForageDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventorySeededForageGuid());		
	}
	
	
	private String createDeclaredYieldContract(String userId, Integer cropYear) throws DaoException {

		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();

		DeclaredYieldContractDao dao = persistenceSpringConfig.declaredYieldContractDao();
		
		DeclaredYieldContractDto newDto = new DeclaredYieldContractDto();

		newDto.setContractId(contractId1);
		newDto.setCropYear(cropYear);
		newDto.setDeclarationOfProductionDate(dopDate);
		newDto.setDefaultYieldMeasUnitTypeCode("TON");
		newDto.setEnteredYieldMeasUnitTypeCode("LB");
		newDto.setGrainFromOtherSourceInd(false);
		newDto.setBalerWagonInfo(null);
		newDto.setTotalLivestock(null);

		//INSERT
		dao.insert(newDto, userId);		
		
		return newDto.getDeclaredYieldContractGuid();	
	}
	
	private void createCropCommodity() throws DaoException {
		CropCommodityDao dao = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto newDto = new CropCommodityDto();
		
		String commodityName = "Test Commodity";
		String shortLabel = "TC";
		String plantDurationTypeCode = "PERENNIAL";
		Boolean isInventoryCropInd = true;
		Boolean isYieldCropInd = true;
		Boolean isUnderwritingCropInd = true;
		String yieldMeasUnitTypeCode = "TON";
		Integer yieldDecimalPrecision = 1;

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);

		String userId = "JUNIT_TEST";
		
		//INSERT
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCommodityName(commodityName);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setShortLabel(shortLabel);
		newDto.setPlantDurationTypeCode(plantDurationTypeCode);
		newDto.setIsInventoryCropInd(isInventoryCropInd);
		newDto.setIsYieldCropInd(isYieldCropInd);
		newDto.setIsUnderwritingCropInd(isUnderwritingCropInd);
		newDto.setIsProductInsurableInd(true);
		newDto.setIsCropInsuranceEligibleInd(true);
		newDto.setIsPlantInsuranceEligibleInd(true);
		newDto.setIsOtherInsuranceEligibleInd(true);
		newDto.setYieldMeasUnitTypeCode(yieldMeasUnitTypeCode);
		newDto.setYieldDecimalPrecision(yieldDecimalPrecision);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dateTime);

		dao.insert(newDto, userId);
	}	
	
	private static Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}	
}
