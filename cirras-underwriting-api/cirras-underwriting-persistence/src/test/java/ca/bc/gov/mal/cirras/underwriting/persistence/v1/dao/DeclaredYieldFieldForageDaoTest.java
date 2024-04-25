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
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class DeclaredYieldFieldForageDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	private Integer gcyId1 = 90000001;
	private Integer contractId1 = 90000002;
	private Integer cropYear1 = 2020;

	private Integer fieldId1 = 90000005;
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
		deleteSeededForage();
		deleteInventoryField();
		deleteDeclaredYieldContract();
		deleteContractedFieldDetail();
		deleteAnnualFieldDetail();
		deleteField();		
		deleteGrowerContractYear();
	}
	
	private void deleteSeededForage() throws NotFoundDaoException, DaoException {
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		invSeededForageDao.deleteForField(fieldId1);
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
	
	private Integer insurancePlanId = 5;
	private Integer cropCommodityId = 65;
	
	@Test 
	public void testDeclaredYieldFieldForage() throws Exception {

		String inventoryFieldGuid;
		String declaredYieldFieldForageGuid;
		Integer plantingNumber = 1;
		
		String userId = "UNITTEST";
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		DeclaredYieldFieldForageDao declaredYieldFieldForageDao = persistenceSpringConfig.declaredYieldFieldForageDao();
		
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
		invFieldDto.setLastYearCropCommodityName("FORAGE");
		invFieldDto.setIsHiddenOnPrintoutInd(false);
		invFieldDto.setPlantingNumber(plantingNumber);

		
		invFieldDao.insert(invFieldDto, userId);
		inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		createInventorySeeded(inventoryFieldGuid, userId);

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
		
		//GET BY INVENTORY FIELD
		fetchedDtoList = declaredYieldFieldForageDao.getByInventoryField(inventoryFieldGuid);
		Assert.assertNotNull(fetchedDtoList);
		Assert.assertEquals(2, fetchedDtoList.size());

		Assert.assertEquals("DeclaredYieldFieldForageGuid", newDto.getDeclaredYieldFieldForageGuid(), fetchedDtoList.get(0).getDeclaredYieldFieldForageGuid());
		Assert.assertEquals("DeclaredYieldFieldForageGuid", newDto2.getDeclaredYieldFieldForageGuid(), fetchedDtoList.get(1).getDeclaredYieldFieldForageGuid());

		//DELETE
		declaredYieldFieldForageDao.delete(declaredYieldFieldForageGuid);
		
		//FETCH
		DeclaredYieldFieldForageDto deletedDto = declaredYieldFieldForageDao.fetch(declaredYieldFieldForageGuid);
		Assert.assertNull(deletedDto);

		//GET BY INVENTORY FIELD
		List<DeclaredYieldFieldForageDto> deletedDtoList = declaredYieldFieldForageDao.getByInventoryField(inventoryFieldGuid);
		Assert.assertNotNull(deletedDtoList);
		Assert.assertEquals(1, deletedDtoList.size());

		//DELETE second record
		declaredYieldFieldForageDao.delete(newDto2.getDeclaredYieldFieldForageGuid());
		
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
	public void testDeleteForDeclaredYieldContract() throws Exception {

		String userId = "UNITTEST";
		
		DeclaredYieldFieldForageDao declaredYieldFieldForageDao = persistenceSpringConfig.declaredYieldFieldForageDao();

		createGrowerContractYear(userId);
		createField(userId);
		
		createAnnualFieldDetail(userId);
		createContractedFieldDetail(userId);

		String declaredYieldContractGuid = createDeclaredYieldContract(userId);
		
		String invFieldGuid1 = createInventoryField(1, userId);
		String invFieldGuid2 = createInventoryField(2, userId);
		
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
		newDto.setInsurancePlanId(5);
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
		invFieldDto.setInsurancePlanId(insurancePlanId);
		invFieldDto.setLastYearCropCommodityId(cropCommodityId);
		invFieldDto.setLastYearCropCommodityName("FORAGE");
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
		newDto.setCropCommodityId(null);
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
		newDto.setDefaultYieldMeasUnitTypeCode("TON");
		newDto.setEnteredYieldMeasUnitTypeCode("LB");
		newDto.setGrainFromOtherSourceInd(false);
		newDto.setBalerWagonInfo(null);
		newDto.setTotalLivestock(null);

		//INSERT
		dao.insert(newDto, userId);		
		
		return newDto.getDeclaredYieldContractGuid();	
	}
	
}
