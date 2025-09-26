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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class InventorySeededForageDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	private Integer fieldId2 = 99999911;
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{
		
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = fieldDao.fetch(fieldId2);
		if (fieldDto != null) {
			InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
			invSeededForageDao.deleteForField(fieldId2);
			InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
			invFieldDao.deleteForField(fieldId2);
			fieldDao.delete(fieldId2);
		}
		
		fieldDto = fieldDao.fetch(fieldId);
		if (fieldDto != null) {
			InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
			invSeededForageDao.deleteForField(fieldId);

			InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
			invFieldDao.deleteForField(fieldId);
			
			ContractedFieldDetailDao cfdDao = persistenceSpringConfig.contractedFieldDetailDao();
			cfdDao.deleteForField(fieldId);
			
			AnnualFieldDetailDao afdDao = persistenceSpringConfig.annualFieldDetailDao();
			afdDao.deleteForField(fieldId);

			fieldDao.delete(fieldId);
		}
		
		GrowerContractYearDao gcyDao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto gcyDto = gcyDao.fetch(growerContractYearId);
		if (gcyDto != null) {
			gcyDao.delete(growerContractYearId);
		}
		
	}

	@Test 
	public void testInventorySeededForage() throws Exception {

		//Integer fieldId = 21949;
		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		String inventoryFieldGuid;
		String inventorySeededForageGuid;
		Integer plantingNumber = 50;
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, 15, Calendar.JANUARY);
		Date seedingDate = cal.getTime();
		
		
		String userId = "UNITTEST";
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		
		//INSERT Field
		createField(userId);

		// INSERT parent InventoryField
		InventoryFieldDto invFieldDto = new InventoryFieldDto();

		invFieldDto.setCropYear(cropYear);
		invFieldDto.setFieldId(fieldId2);
		invFieldDto.setInsurancePlanId(insurancePlanId);
		invFieldDto.setLastYearCropCommodityId(20);
		invFieldDto.setLastYearCropCommodityName("FALL RYE");
		invFieldDto.setLastYearCropVarietyId(null);
		invFieldDto.setLastYearCropVarietyName(null);
		invFieldDto.setIsHiddenOnPrintoutInd(false);
		invFieldDto.setPlantingNumber(plantingNumber);

		
		invFieldDao.insert(invFieldDto, userId);
		inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		//INSERT
		InventorySeededForageDto newDto = new InventorySeededForageDto();

		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setCommodityTypeCode("CPSW");
		newDto.setCropCommodityId(26);
		newDto.setCropVarietyId(1010602);
		newDto.setCropVarietyName("AAC ENTICE");
		newDto.setFieldAcres(10.4);
		newDto.setSeedingYear(2020);		
		newDto.setSeedingDate(seedingDate);		
		newDto.setIsIrrigatedInd(true);
		newDto.setIsQuantityInsurableInd(false);
		newDto.setPlantInsurabilityTypeCode("E1");
		newDto.setIsAwpEligibleInd(true);
		
		invSeededForageDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventorySeededForageGuid());
		inventorySeededForageGuid = newDto.getInventorySeededForageGuid();
		
		//SELECT
		List<InventorySeededForageDto> dtos = invSeededForageDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		InventorySeededForageDto fetchedDto = invSeededForageDao.fetch(inventorySeededForageGuid);

		Assert.assertEquals("InventorySeededForageGuid", newDto.getInventorySeededForageGuid(), fetchedDto.getInventorySeededForageGuid());
		Assert.assertEquals("InventoryFieldGuid", newDto.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropVarietyId", newDto.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", newDto.getCropVarietyName(), fetchedDto.getCropVarietyName());
		Assert.assertEquals("CommodityTypeCode", newDto.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("FieldAcres", newDto.getFieldAcres(), fetchedDto.getFieldAcres());
		Assert.assertEquals("SeedingYear", newDto.getSeedingYear(), fetchedDto.getSeedingYear());
		Assert.assertEquals("IsIrrigatedInd", newDto.getIsIrrigatedInd(), fetchedDto.getIsIrrigatedInd());
		Assert.assertEquals("IsQuantityInsurableInd", newDto.getIsQuantityInsurableInd(), fetchedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("PlantInsurabilityTypeCode", newDto.getPlantInsurabilityTypeCode(), fetchedDto.getPlantInsurabilityTypeCode());
		Assert.assertEquals("IsAwpEligibleInd", newDto.getIsAwpEligibleInd(), fetchedDto.getIsAwpEligibleInd());
		Assert.assertEquals("SeedingDate", newDto.getSeedingDate(), fetchedDto.getSeedingDate());
		
		//UPDATE
		cal.set(Calendar.DAY_OF_MONTH, 16);
		seedingDate = cal.getTime();
		
		fetchedDto.setCommodityTypeCode("Six Row");
		fetchedDto.setCropCommodityId(16);
		fetchedDto.setCropVarietyId(1010640);
		fetchedDto.setCropVarietyName("CDC TITANIUM");
		fetchedDto.setInventoryFieldGuid(inventoryFieldGuid);
		fetchedDto.setFieldAcres(22.5);
		fetchedDto.setSeedingYear(2021);
		fetchedDto.setIsIrrigatedInd(false);
		fetchedDto.setIsQuantityInsurableInd(true);
		fetchedDto.setPlantInsurabilityTypeCode("W1");
		fetchedDto.setIsAwpEligibleInd(false);
		fetchedDto.setSeedingDate(seedingDate);

		invSeededForageDao.update(fetchedDto, userId);

		//FETCH
		InventorySeededForageDto updatedDto = invSeededForageDao.fetch(inventorySeededForageGuid);

		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropVarietyId", fetchedDto.getCropVarietyId(), updatedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", fetchedDto.getCropVarietyName(), updatedDto.getCropVarietyName());
		Assert.assertEquals("CommodityTypeCode", fetchedDto.getCommodityTypeCode(), updatedDto.getCommodityTypeCode());
		Assert.assertEquals("FieldAcres", fetchedDto.getFieldAcres(), updatedDto.getFieldAcres());
		Assert.assertEquals("SeedingYear", fetchedDto.getSeedingYear(), updatedDto.getSeedingYear());
		Assert.assertEquals("IsIrrigatedInd", fetchedDto.getIsIrrigatedInd(), updatedDto.getIsIrrigatedInd());
		Assert.assertEquals("IsQuantityInsurableInd", fetchedDto.getIsQuantityInsurableInd(), updatedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("PlantInsurabilityTypeCode", fetchedDto.getPlantInsurabilityTypeCode(), updatedDto.getPlantInsurabilityTypeCode());
		Assert.assertEquals("IsAwpEligibleInd", fetchedDto.getIsAwpEligibleInd(), updatedDto.getIsAwpEligibleInd());
		Assert.assertEquals("SeedingDate", fetchedDto.getSeedingDate(), updatedDto.getSeedingDate());
		
		//FETCH SIMPLE (only returns the inventory seeded forage record)
		updatedDto = invSeededForageDao.fetchSimple(inventorySeededForageGuid);

		Assert.assertNull("CropVarietyName", updatedDto.getCropVarietyName());

		Assert.assertEquals("InventorySeededForageGuid", fetchedDto.getInventorySeededForageGuid(), updatedDto.getInventorySeededForageGuid());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropVarietyId", fetchedDto.getCropVarietyId(), updatedDto.getCropVarietyId());
		Assert.assertEquals("CommodityTypeCode", fetchedDto.getCommodityTypeCode(), updatedDto.getCommodityTypeCode());
		Assert.assertEquals("FieldAcres", fetchedDto.getFieldAcres(), updatedDto.getFieldAcres());
		Assert.assertEquals("SeedingYear", fetchedDto.getSeedingYear(), updatedDto.getSeedingYear());
		Assert.assertEquals("IsIrrigatedInd", fetchedDto.getIsIrrigatedInd(), updatedDto.getIsIrrigatedInd());
		Assert.assertEquals("IsQuantityInsurableInd", fetchedDto.getIsQuantityInsurableInd(), updatedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("PlantInsurabilityTypeCode", fetchedDto.getPlantInsurabilityTypeCode(), updatedDto.getPlantInsurabilityTypeCode());
		Assert.assertEquals("IsAwpEligibleInd", fetchedDto.getIsAwpEligibleInd(), updatedDto.getIsAwpEligibleInd());
		Assert.assertEquals("SeedingDate", fetchedDto.getSeedingDate(), updatedDto.getSeedingDate());
		
		//INSERT second seeded forage record
		InventorySeededForageDto newDto2 = new InventorySeededForageDto();

		newDto2.setInventoryFieldGuid(inventoryFieldGuid);
		newDto2.setCommodityTypeCode(null);
		newDto2.setCropCommodityId(null);
		newDto2.setCropVarietyId(null);
		newDto2.setCropVarietyName(null);
		newDto2.setFieldAcres(null);
		newDto2.setSeedingYear(null);
		newDto2.setIsIrrigatedInd(true);
		newDto2.setIsQuantityInsurableInd(true);
		newDto2.setPlantInsurabilityTypeCode("E1");
		newDto2.setIsAwpEligibleInd(true);	
		newDto2.setSeedingDate(null);	
		
		
		invSeededForageDao.insert(newDto2, userId);
		
		//FETCH
		InventorySeededForageDto fetchedDto2 = invSeededForageDao.fetch(newDto2.getInventorySeededForageGuid());
		
		Assert.assertEquals("CropCommodityId", newDto2.getCropCommodityId(), fetchedDto2.getCropCommodityId());
		Assert.assertEquals("CropVarietyId", newDto2.getCropVarietyId(), fetchedDto2.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", newDto2.getCropVarietyName(), fetchedDto2.getCropVarietyName());
		Assert.assertEquals("CommodityTypeCode", newDto2.getCommodityTypeCode(), fetchedDto2.getCommodityTypeCode());
		Assert.assertEquals("FieldAcres", newDto2.getFieldAcres(), fetchedDto2.getFieldAcres());
		Assert.assertEquals("SeedingYear", newDto2.getSeedingYear(), fetchedDto2.getSeedingYear());
		Assert.assertEquals("IsIrrigatedInd", newDto2.getIsIrrigatedInd(), fetchedDto2.getIsIrrigatedInd());
		Assert.assertEquals("IsQuantityInsurableInd", newDto2.getIsQuantityInsurableInd(), fetchedDto2.getIsQuantityInsurableInd());
		Assert.assertEquals("PlantInsurabilityTypeCode", newDto2.getPlantInsurabilityTypeCode(), fetchedDto2.getPlantInsurabilityTypeCode());
		Assert.assertEquals("IsAwpEligibleInd", newDto2.getIsAwpEligibleInd(), fetchedDto2.getIsAwpEligibleInd());
		Assert.assertEquals("SeedingDate", newDto2.getSeedingDate(), fetchedDto2.getSeedingDate());
		
		//SELECT
		dtos = invSeededForageDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		
		//DELETE
		invSeededForageDao.delete(newDto2.getInventorySeededForageGuid());
		invSeededForageDao.delete(inventorySeededForageGuid);
		
		//FETCH
		InventorySeededForageDto deletedDto = invSeededForageDao.fetchSimple(inventorySeededForageGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = invSeededForageDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		//DELETE parent InventoryField
		invFieldDao.delete(inventoryFieldGuid);
		
	}

	@Test 
	public void testSelectForRollover() throws Exception {

		Integer cropYear = 2020;
		Integer insurancePlanId = 5;
				
		String userId = "UNITTEST";
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		
		//INSERT Field
		createField(userId);

		// INSERT parent InventoryField
		InventoryFieldDto invFieldDto = new InventoryFieldDto();

		invFieldDto.setCropYear(cropYear);
		invFieldDto.setFieldId(fieldId2);
		invFieldDto.setInsurancePlanId(insurancePlanId);
		invFieldDto.setLastYearCropCommodityId(null);
		invFieldDto.setLastYearCropCommodityName(null);
		invFieldDto.setLastYearCropVarietyId(null);
		invFieldDto.setLastYearCropVarietyName(null);
		invFieldDto.setIsHiddenOnPrintoutInd(false);
		invFieldDto.setPlantingNumber(1);
		invFieldDto.setUnderseededAcres(null);
		invFieldDto.setUnderseededCropVarietyId(null);
		invFieldDto.setUnderseededCropVarietyName(null);
		invFieldDto.setUnderseededInventorySeededForageGuid(null);
		
		
		invFieldDao.insert(invFieldDto, userId);

		//INSERT
		InventorySeededForageDto newDto = new InventorySeededForageDto();
		
		newDto.setInventoryFieldGuid(invFieldDto.getInventoryFieldGuid());
		newDto.setCommodityTypeCode("Alfalfa");
		newDto.setCropCommodityId(65);
		newDto.setCropVarietyId(118);
		newDto.setCropVarietyName("ALFALFA/GRASS");
		newDto.setFieldAcres(10.4);
		newDto.setSeedingYear(2015);
		newDto.setSeedingDate(null);
		newDto.setIsIrrigatedInd(true);
		newDto.setIsQuantityInsurableInd(true);
		newDto.setPlantInsurabilityTypeCode("E1");
		newDto.setIsAwpEligibleInd(true);
		
		invSeededForageDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventorySeededForageGuid());
		
		//Select for rollover
		List<InventorySeededForageDto> dtos = invSeededForageDao.selectForRollover(fieldId2, cropYear, insurancePlanId, 1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		InventorySeededForageDto fetchedDto = dtos.get(0);
		
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropVarietyId", newDto.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", newDto.getCropVarietyName(), fetchedDto.getCropVarietyName());
		Assert.assertEquals("CommodityTypeCode", newDto.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("FieldAcres", newDto.getFieldAcres(), fetchedDto.getFieldAcres());
		Assert.assertEquals("SeedingYear", newDto.getSeedingYear(), fetchedDto.getSeedingYear());
		Assert.assertEquals("IsIrrigatedInd", newDto.getIsIrrigatedInd(), fetchedDto.getIsIrrigatedInd());
		Assert.assertEquals("IsQuantityInsurableInd", newDto.getIsQuantityInsurableInd(), fetchedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("PlantInsurabilityTypeCode", newDto.getPlantInsurabilityTypeCode(), fetchedDto.getPlantInsurabilityTypeCode());
		Assert.assertEquals("IsAwpEligibleInd", newDto.getIsAwpEligibleInd(), fetchedDto.getIsAwpEligibleInd());
		
		
		//DELETE
		invSeededForageDao.delete(newDto.getInventorySeededForageGuid());
		
		//SELECT
		dtos = invSeededForageDao.selectForRollover(fieldId2, cropYear, insurancePlanId, 1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		//DELETE parent InventoryField
		invFieldDao.delete(invFieldDto.getInventoryFieldGuid());		
	}
	
	@Test 
	public void testSelectForVerifiedYield() throws Exception {

		Integer cropYear = 2020;
		Integer insurancePlanId = 5;
		String inventoryFieldGuid;
		
		String userId = "UNITTEST";
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();

		//INSERT Field
		createField(userId);

		// INSERT parent InventoryField
		InventoryFieldDto invFieldDto = new InventoryFieldDto();

		invFieldDto.setCropYear(cropYear);
		invFieldDto.setFieldId(fieldId2);
		invFieldDto.setInsurancePlanId(insurancePlanId);
		invFieldDto.setLastYearCropCommodityId(null);
		invFieldDto.setLastYearCropCommodityName(null);
		invFieldDto.setLastYearCropVarietyId(null);
		invFieldDto.setLastYearCropVarietyName(null);
		invFieldDto.setIsHiddenOnPrintoutInd(false);
		invFieldDto.setPlantingNumber(1);
		invFieldDto.setUnderseededAcres(null);
		invFieldDto.setUnderseededCropVarietyId(null);
		invFieldDto.setUnderseededCropVarietyName(null);
		invFieldDto.setUnderseededInventorySeededForageGuid(null);

		
		invFieldDao.insert(invFieldDto, userId);
		inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		//INSERT NO VARIETY
		InventorySeededForageDto isfDto1 = new InventorySeededForageDto();
		
		isfDto1.setInventoryFieldGuid(invFieldDto.getInventoryFieldGuid());
		isfDto1.setCommodityTypeCode(null);
		isfDto1.setCropCommodityId(null);
		isfDto1.setCropVarietyId(null);
		isfDto1.setCropVarietyName(null);
		isfDto1.setFieldAcres(10.4);
		isfDto1.setSeedingYear(2015);
		isfDto1.setSeedingDate(null);
		isfDto1.setIsIrrigatedInd(true);
		isfDto1.setIsQuantityInsurableInd(false);
		isfDto1.setPlantInsurabilityTypeCode(null);
		isfDto1.setIsAwpEligibleInd(false);
		
		invSeededForageDao.insert(isfDto1, userId);
		Assert.assertNotNull(isfDto1.getInventorySeededForageGuid());
		
		//INSERT
		InventorySeededForageDto isfDto2 = new InventorySeededForageDto();
		
		isfDto2.setInventoryFieldGuid(invFieldDto.getInventoryFieldGuid());
		isfDto2.setCommodityTypeCode("Alfalfa");
		isfDto2.setCropCommodityId(65);
		isfDto2.setCropVarietyId(118);
		isfDto2.setCropVarietyName("ALFALFA/GRASS");
		isfDto2.setFieldAcres(10.4);
		isfDto2.setSeedingYear(2015);
		isfDto2.setSeedingDate(null);
		isfDto2.setIsIrrigatedInd(true);
		isfDto2.setIsQuantityInsurableInd(true);
		isfDto2.setPlantInsurabilityTypeCode("E1");
		isfDto2.setIsAwpEligibleInd(true);
		
		invSeededForageDao.insert(isfDto2, userId);
		Assert.assertNotNull(isfDto2.getInventorySeededForageGuid());

		//SELECT FOR VERIFIED YIELD
		// Only the second isf record is returned.
		List<InventorySeededForageDto> dtos = invSeededForageDao.selectForVerifiedYield(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		InventorySeededForageDto fetchedDto = dtos.get(0);
		
		Assert.assertEquals("CropCommodityId", isfDto2.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropVarietyId", isfDto2.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", isfDto2.getCropVarietyName(), fetchedDto.getCropVarietyName());
		Assert.assertEquals("CommodityTypeCode", isfDto2.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("FieldAcres", isfDto2.getFieldAcres(), fetchedDto.getFieldAcres());
		Assert.assertEquals("SeedingYear", isfDto2.getSeedingYear(), fetchedDto.getSeedingYear());
		Assert.assertEquals("IsIrrigatedInd", isfDto2.getIsIrrigatedInd(), fetchedDto.getIsIrrigatedInd());
		Assert.assertEquals("IsQuantityInsurableInd", isfDto2.getIsQuantityInsurableInd(), fetchedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("PlantInsurabilityTypeCode", isfDto2.getPlantInsurabilityTypeCode(), fetchedDto.getPlantInsurabilityTypeCode());
		Assert.assertEquals("IsAwpEligibleInd", isfDto2.getIsAwpEligibleInd(), fetchedDto.getIsAwpEligibleInd());
		
		//DELETE
		invSeededForageDao.delete(isfDto1.getInventorySeededForageGuid());
		invSeededForageDao.delete(isfDto2.getInventorySeededForageGuid());
		
		//SELECT
		dtos = invSeededForageDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		//DELETE parent InventoryField
		invFieldDao.delete(inventoryFieldGuid);		
	}
	
	
	@Test 
	public void testDeleteForField() throws Exception {

		FieldDao dao = persistenceSpringConfig.fieldDao();

		String userId = "JUNIT_TEST";

		//INSERT Field
		createField(userId);
	
		
		//FETCH
		FieldDto fetchedDto = dao.fetch(fieldId2);
		
		Assert.assertNotNull(fetchedDto);

		//Add inventory field
		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();

		// INSERT inventory field
		InventoryFieldDto invFieldDto = new InventoryFieldDto();

		invFieldDto.setCropYear(cropYear);
		invFieldDto.setFieldId(fieldId2);
		invFieldDto.setInsurancePlanId(insurancePlanId);
		invFieldDto.setLastYearCropCommodityId(20);
		invFieldDto.setLastYearCropCommodityName("FALL RYE");
		invFieldDto.setLastYearCropVarietyId(null);
		invFieldDto.setLastYearCropVarietyName(null);
		invFieldDto.setIsHiddenOnPrintoutInd(false);
		invFieldDto.setPlantingNumber(20);

		
		invFieldDao.insert(invFieldDto, userId);

		String inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		//INSERT
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		InventorySeededForageDto newDto = new InventorySeededForageDto();

		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setCommodityTypeCode("CPSW");
		newDto.setCropCommodityId(26);
		newDto.setCropVarietyId(1010602);
		newDto.setCropVarietyName("AAC ENTICE");
		newDto.setFieldAcres(10.4);
		newDto.setSeedingYear(2020);
		newDto.setIsIrrigatedInd(true);
		newDto.setIsQuantityInsurableInd(false);
		newDto.setPlantInsurabilityTypeCode("E1");
		newDto.setIsAwpEligibleInd(true);

		
		invSeededForageDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventorySeededForageGuid());

		
		//SELECT
		List<InventorySeededForageDto> dtos = invSeededForageDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals("total unseeded records", 1, dtos.size());

		//Delete all unseeded records for field
		invSeededForageDao.deleteForField(fieldId2);
		dtos = invSeededForageDao.select(inventoryFieldGuid);
		Assert.assertTrue("seeded records not deleted", dtos == null || dtos.size() == 0);
		
		//Delete all inventory field records for field
		invFieldDao.deleteForField(fieldId2);
		List<InventoryFieldDto> invFieldDtos = invFieldDao.select(fieldId2, cropYear, insurancePlanId);
		Assert.assertTrue("inventory field records not deleted", invFieldDtos == null || invFieldDtos.size() == 0);
		
		//DELETE
		dao.delete(fieldId2);

		//FETCH
		FieldDto deletedDto = dao.fetch(fieldId2);
		Assert.assertNull(deletedDto);

	}
	
	@Test
	public void testSelectForDopContractCommodityTotals() throws Exception {

		String userId = "JUNIT_TEST";
		String ctcAlfalfa = "Alfalfa";
		String ctcSilageCorn = "Silage Corn";
		Integer cropIdAlfalfa = 65;
		Integer cropIdSilageCorn = 71;
		Integer varietyIdAlfalafaGrass = 118;
		Integer varietyIdSilageCorn = 1010863;

		//Insert grower contract year
		createGrowerContractYear(userId);
		//INSERT Field, annual field and contracted field
		createFieldData(userId);

		//Add inventory field
		String invGuidAlfalfa1 = createInventoryField(userId, 1, cropIdAlfalfa); //Alfalfa		
		String invGuidAlfalfa2 = createInventoryField(userId, 2, cropIdAlfalfa); //Alfalfa		
		String invGuidSilageCorn1 = createInventoryField(userId, 3, cropIdSilageCorn); //Silage Corn		
		String invGuidSilageCorn2 = createInventoryField(userId, 4, 71); //Silage Corn		
		
		//Create seeded inventory
		createSeededInventory(userId, invGuidAlfalfa1, ctcAlfalfa, cropIdAlfalfa, varietyIdAlfalafaGrass, 100.0, true);
		createSeededInventory(userId, invGuidAlfalfa2, ctcAlfalfa, cropIdAlfalfa, varietyIdAlfalafaGrass, 150.0, false); //Not quantity insured
		createSeededInventory(userId, invGuidSilageCorn1, ctcSilageCorn, cropIdSilageCorn, varietyIdSilageCorn, 130.0, true);
		createSeededInventory(userId, invGuidSilageCorn2, ctcSilageCorn, cropIdSilageCorn, varietyIdSilageCorn, 70.0, true);

		//SELECT
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		List<InventorySeededForageDto> dtos = invSeededForageDao.selectForDopContractCommodityTotals(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals("total records", 2, dtos.size());
		
		for (InventorySeededForageDto dto : dtos) {
			Assert.assertNotNull(dto.getCommodityTypeDescription());
			if(dto.getCommodityTypeCode().equals(ctcAlfalfa)) {
				Assert.assertEquals((double)100, dto.getTotalFieldAcres().doubleValue(), 0.1);
			} else if(dto.getCommodityTypeCode().equals(ctcSilageCorn)) {
				Assert.assertEquals((double)200, dto.getTotalFieldAcres().doubleValue(), 0.1);
			} else {
				Assert.fail("Unexpected commodity type: " + dto.getCommodityTypeCode());
			}
		}
		
		//SELECT Field level data
		ContractedFieldDetailDao cfdDao = persistenceSpringConfig.contractedFieldDetailDao();
		List<ContractedFieldDetailDto> cfdDtos = cfdDao.selectForDeclaredYield(contractId, cropYear);
		Assert.assertNotNull(cfdDtos);
		Assert.assertEquals("total records", 1, cfdDtos.size()); //Expect 1 field record
		
		
		InventoryFieldDao invDao = persistenceSpringConfig.inventoryFieldDao();
		InventorySeededForageDao isfDao = persistenceSpringConfig.inventorySeededForageDao();

		for (ContractedFieldDetailDto dto : cfdDtos) {

			//Inventory Field Records
			List<InventoryFieldDto> invDtos = invDao.selectForDeclaredYield(dto.getFieldId(), cropYear, dto.getInsurancePlanId());
			Assert.assertNotNull(invDtos);
			Assert.assertEquals("total records", 4, invDtos.size()); //Expect 4 inventory field records
			
			for (InventoryFieldDto invDto : invDtos) {
				//Seeded Records
				List<InventorySeededForageDto> isfDtos = isfDao.selectForDeclaredYield(invDto.getInventoryFieldGuid());
				Assert.assertNotNull(isfDtos);
				Assert.assertEquals("total records", 1, isfDtos.size()); //Expect 1 record for each inventory field record
				InventorySeededForageDto isfDto = isfDtos.get(0);
				if(isfDto.getCropVarietyId().equals(varietyIdAlfalafaGrass)) {
					Assert.assertEquals(isfDto.getPlantDurationTypeCode(), "PERENNIAL");
				} else if(isfDto.getCropVarietyId().equals(varietyIdSilageCorn)) {
					Assert.assertEquals(isfDto.getPlantDurationTypeCode(), "ANNUAL");
				} else {
					Assert.fail("Unexpected variety Id: " + isfDto.getCropVarietyId());
				}

			}
		}
		
		//Clean up
		delete();
	}
	
	private Integer cropYear = 2015;
	private Integer fieldId = 99919999;
	private Integer contractedFieldDetailId = 999199876;
	private Integer annualFieldDetailId = 999188999;
	private Integer growerContractYearId = 9595959;
	private Integer contractId = 888118888;
	private Integer insurancePlanId = 5;

	private void createSeededInventory(
			String userId,
			String inventoryFieldGuid,
			String commodityTypeCode,
			Integer cropCommodityId,
			Integer cropVarietyId,
			Double fieldAcres,
			Boolean isQuantityInsurable) throws DaoException {
		// INSERT
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		InventorySeededForageDto newDto = new InventorySeededForageDto();

		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setCommodityTypeCode(commodityTypeCode);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropVarietyId(cropVarietyId);
		newDto.setFieldAcres(fieldAcres);
		newDto.setSeedingYear(cropYear);
		newDto.setIsIrrigatedInd(true);
		newDto.setIsQuantityInsurableInd(isQuantityInsurable);
		newDto.setPlantInsurabilityTypeCode("E1");
		newDto.setIsAwpEligibleInd(true);
		
		invSeededForageDao.insert(newDto, userId);
	}
	
	private String createInventoryField(String userId, Integer plantingNumber, Integer cropCommodityId) throws DaoException {
		// INSERT
		InventoryFieldDto newDto = new InventoryFieldDto();

		newDto.setCropYear(cropYear);
		newDto.setFieldId(fieldId);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setLastYearCropCommodityId(cropCommodityId);
		newDto.setLastYearCropCommodityName(null);
		newDto.setLastYearCropVarietyId(null);
		newDto.setLastYearCropVarietyName(null);
		newDto.setIsHiddenOnPrintoutInd(false);
		newDto.setUnderseededAcres(null);
		newDto.setUnderseededCropVarietyId(null);
		newDto.setUnderseededCropVarietyName(null);
		newDto.setPlantingNumber(plantingNumber);

		InventoryFieldDao inventoryFieldDao = persistenceSpringConfig.inventoryFieldDao(); 
		inventoryFieldDao.insert(newDto, userId);
		return newDto.getInventoryFieldGuid();
	}
	
	private void createGrowerContractYear(String userId) throws DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto newDto = new GrowerContractYearDto();
		
		//Has to be a valid grower
		Integer growerId = 525593;
		Integer insurancePlanId = 5;

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		//INSERT
		newDto.setGrowerContractYearId(growerContractYearId);
		newDto.setContractId(contractId);
		newDto.setGrowerId(growerId);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setCropYear(cropYear);
		newDto.setDataSyncTransDate(dateTime);

		dao.insert(newDto, userId);
	}
	
	private void createFieldData(String userId) throws DaoException {
		// INSERT FIELD

		String fieldLabel = "Test Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;
		
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel(fieldLabel);
		newFieldDto.setActiveFromCropYear(activeFromCropYear);
		newFieldDto.setActiveToCropYear(activeToCropYear);

		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		fieldDao.insertDataSync(newFieldDto, userId);
		
		//INSERT Annual Field Detail record
		AnnualFieldDetailDto newAnnualFieldDetailDto = new AnnualFieldDetailDto();
		
		newAnnualFieldDetailDto.setAnnualFieldDetailId(annualFieldDetailId);
		newAnnualFieldDetailDto.setLegalLandId(null);
		newAnnualFieldDetailDto.setFieldId(fieldId);
		newAnnualFieldDetailDto.setCropYear(cropYear);

		AnnualFieldDetailDao annualFieldDetailDao = persistenceSpringConfig.annualFieldDetailDao();
		annualFieldDetailDao.insertDataSync(newAnnualFieldDetailDto, userId);
		
		//INSERT Contracted Field Detail record		
		ContractedFieldDetailDto newContractedFieldDetailDto = new ContractedFieldDetailDto();

		newContractedFieldDetailDto.setContractedFieldDetailId(contractedFieldDetailId);
		newContractedFieldDetailDto.setAnnualFieldDetailId(annualFieldDetailId);
		newContractedFieldDetailDto.setDisplayOrder(1);
		newContractedFieldDetailDto.setIsLeasedInd(false);
		newContractedFieldDetailDto.setGrowerContractYearId(growerContractYearId);

		ContractedFieldDetailDao contractedFieldDetailDao = persistenceSpringConfig.contractedFieldDetailDao();
		contractedFieldDetailDao.insertDataSync(newContractedFieldDetailDto, userId);

	}
	
	private void createField(String userId) throws DaoException {
		// INSERT FIELD
		
		String fieldLabel = "Test Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;

		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId2);
		newFieldDto.setFieldLabel(fieldLabel);
		newFieldDto.setActiveFromCropYear(activeFromCropYear);
		newFieldDto.setActiveToCropYear(activeToCropYear);
		
		fieldDao.insertDataSync(newFieldDto, userId);
	}
	 

}
