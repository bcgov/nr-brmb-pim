package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyInsurabilityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryUnseededDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class CropVarietyInsurabilityDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer cropVarietyId = 99999;
	private Integer cropCommodityId = 65;
	private String varietyName = "Test Variety";
	private Integer fieldId = 99569999;
	
	private Integer insurancePlanId = 5;  //Forage
	
	private String inventoryFieldGuid;
	private String inventoryUnseededGuid;
	private String inventorySeededForageGuid;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{
		
		deleteFieldAndInventory();

		// delete crop_variety_insurability
		CropVarietyInsurabilityDao daoIns = persistenceSpringConfig.cropVarietyInsurabilityDao();
		CropVarietyInsurabilityDto dtoIns = daoIns.fetch(cropVarietyId);
		if (dtoIns != null) {
			daoIns.delete(cropVarietyId);
		}
		
		CropVarietyDao dao = persistenceSpringConfig.cropVarietyDao();
		CropVarietyDto dto = dao.fetch(cropVarietyId);
		if (dto != null) {
			dao.delete(cropVarietyId);
		}
		
		
	}
	
	private void deleteFieldAndInventory() throws NotFoundDaoException, DaoException{
		
		FieldDao dao = persistenceSpringConfig.fieldDao();
		FieldDto dto = dao.fetch(fieldId);
		if (dto != null) {
			InventoryUnseededDao invUnseededDao = persistenceSpringConfig.inventoryUnseededDao();
			invUnseededDao.deleteForField(fieldId);
			InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
			invSeededForageDao.deleteForField(fieldId);
			InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
			invFieldDao.deleteForField(fieldId);
			dao.delete(fieldId);
		}
	}
	 
	@Test 
	public void testInsertUpdateDeleteCropVarietyInsurability() throws Exception {

		addVariety (cropVarietyId, cropCommodityId, varietyName );
		
		CropVarietyInsurabilityDao dao = persistenceSpringConfig.cropVarietyInsurabilityDao();

		//Test selectForInsurancePlan with the new variety without any insurability record.
		List<CropVarietyInsurabilityDto> dtos = dao.selectForInsurancePlan(insurancePlanId);
		Assert.assertNotNull(dtos);
		
		CropVarietyInsurabilityDto noInsurabilityDto = getCropVarietyInsurabilityDto(dtos, cropVarietyId);
		Assert.assertNotNull(noInsurabilityDto);
		Assert.assertNotNull("PlantDurationTypeCode", noInsurabilityDto.getPlantDurationTypeCode());
		Assert.assertEquals("VarietyName", varietyName, noInsurabilityDto.getVarietyName());
		Assert.assertEquals("CropVarietyId", cropVarietyId, noInsurabilityDto.getCropVarietyId());
		Assert.assertNull("CropVarietyInsurabilityGuid", noInsurabilityDto.getCropVarietyInsurabilityGuid());
		
		CropVarietyInsurabilityDto newDto = new CropVarietyInsurabilityDto();
		
		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setCropVarietyId(cropVarietyId);
		newDto.setIsQuantityInsurableInd(true);
		newDto.setIsPlantInsurableInd(false);
		newDto.setIsAwpEligibleInd(true);
		newDto.setIsUnseededInsurableInd(false);
		newDto.setIsUnderseedingEligibleInd(true);
		newDto.setIsGrainUnseededDefaultInd(true);
		
		dao.insert(newDto, userId);
		
		Assert.assertNotNull(newDto.getCropVarietyInsurabilityGuid());

		//FETCH
		CropVarietyInsurabilityDto fetchedDto = dao.fetch(cropVarietyId);

		Assert.assertEquals("CropVarietyId", newDto.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("IsQuantityInsurableInd", newDto.getIsQuantityInsurableInd(), fetchedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("IsPlantInsurableInd", newDto.getIsPlantInsurableInd(), fetchedDto.getIsPlantInsurableInd());
		Assert.assertEquals("IsAwpEligibleInd", newDto.getIsAwpEligibleInd(), fetchedDto.getIsAwpEligibleInd());
		Assert.assertEquals("IsUnseededInsurableInd", newDto.getIsUnseededInsurableInd(), fetchedDto.getIsUnseededInsurableInd());
		Assert.assertEquals("IsUnderseedingEligibleInd", newDto.getIsUnderseedingEligibleInd(), fetchedDto.getIsUnderseedingEligibleInd());
		Assert.assertEquals("IsGrainUnseededDefaultInd", newDto.getIsGrainUnseededDefaultInd(), fetchedDto.getIsGrainUnseededDefaultInd());
		
		//UPDATE
		fetchedDto.setIsQuantityInsurableInd(false);
		fetchedDto.setIsPlantInsurableInd(true);
		fetchedDto.setIsAwpEligibleInd(false);
		fetchedDto.setIsUnseededInsurableInd(true);
		fetchedDto.setIsUnderseedingEligibleInd(false);
		fetchedDto.setIsGrainUnseededDefaultInd(false);

		
		dao.update(fetchedDto, userId);
		
		//FETCH
		CropVarietyInsurabilityDto updatedDto = dao.fetch(cropVarietyId);
		
		Assert.assertEquals("cropVarietyId", fetchedDto.getCropVarietyId(), updatedDto.getCropVarietyId());
		Assert.assertEquals("IsQuantityInsurableInd", fetchedDto.getIsQuantityInsurableInd(), updatedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("IsPlantInsurableInd", fetchedDto.getIsPlantInsurableInd(), updatedDto.getIsPlantInsurableInd());
		Assert.assertEquals("IsAwpEligibleInd", fetchedDto.getIsAwpEligibleInd(), updatedDto.getIsAwpEligibleInd());
		Assert.assertEquals("IsUnseededInsurableInd", fetchedDto.getIsUnseededInsurableInd(), updatedDto.getIsUnseededInsurableInd());
		Assert.assertEquals("IsUnderseedingEligibleInd", fetchedDto.getIsUnderseedingEligibleInd(), updatedDto.getIsUnderseedingEligibleInd());
		Assert.assertEquals("IsGrainUnseededDefaultInd", fetchedDto.getIsGrainUnseededDefaultInd(), updatedDto.getIsGrainUnseededDefaultInd());

		//Test selectForInsurancePlan with the new variety with insurability record.
		dtos = dao.selectForInsurancePlan(insurancePlanId);
		Assert.assertNotNull(dtos);
		
		CropVarietyInsurabilityDto insurabilityDto = getCropVarietyInsurabilityDto(dtos, cropVarietyId);
		Assert.assertNotNull(insurabilityDto);
		Assert.assertNotNull("PlantDurationTypeCode", insurabilityDto.getPlantDurationTypeCode());
		Assert.assertEquals("VarietyName", varietyName, insurabilityDto.getVarietyName());
		Assert.assertEquals("CropVarietyId", cropVarietyId, insurabilityDto.getCropVarietyId());
		Assert.assertNotNull("CropVarietyInsurabilityGuid", insurabilityDto.getCropVarietyInsurabilityGuid());
		
		
		//DELETE
		dao.delete(updatedDto.getCropVarietyId());
		
		//FETCH
		CropVarietyInsurabilityDto deletedDto = dao.fetch(cropVarietyId);
		Assert.assertNull(deletedDto);
		
		// DELETE the variety
		CropVarietyDao daoVrty = persistenceSpringConfig.cropVarietyDao();
		CropVarietyDto dtoVrty = daoVrty.fetch(cropVarietyId);
		if (dtoVrty != null) {
			daoVrty.delete(cropVarietyId);
		}
		
	}
	
	@Test 
	public void testFetchAllCropVarietyInsurability() throws Exception {	
		
		CropVarietyInsurabilityDao dao = persistenceSpringConfig.cropVarietyInsurabilityDao();

		List<CropVarietyInsurabilityDto> dtos = dao.fetchAll();
		
		Assert.assertNotNull(dtos);

	}
	
	
	@Test 
	public void testSelectForInsurancePlan() throws Exception {	
		
		CropVarietyInsurabilityDao dao = persistenceSpringConfig.cropVarietyInsurabilityDao();

		//Test selectForInsurancePlan with the new variety without any insurability record.
		List<CropVarietyInsurabilityDto> dtos = dao.selectForInsurancePlan(insurancePlanId);
		Assert.assertNotNull(dtos);

		for (CropVarietyInsurabilityDto dto : dtos) {
			Assert.assertNotNull("CropVarietyId", dto.getCropVarietyId());
			Assert.assertNotNull("PlantDurationTypeCode", dto.getPlantDurationTypeCode());
			Assert.assertNotNull("VarietyName", dto.getVarietyName());

			//If there is a insurability record for the variety, these fields need to be not null
			if( dto.getCropVarietyInsurabilityGuid() != null) {
				Assert.assertNotNull("IsQuantityInsurableInd", dto.getIsQuantityInsurableInd());
				Assert.assertNotNull("IsUnseededInsurableInd", dto.getIsUnseededInsurableInd());
				Assert.assertNotNull("IsPlantInsurableInd", dto.getIsPlantInsurableInd());
				Assert.assertNotNull("IsAwpEligibleInd", dto.getIsAwpEligibleInd());
				Assert.assertNotNull("IsUnderseedingEligibleInd", dto.getIsUnderseedingEligibleInd());
				Assert.assertNotNull("IsGrainUnseededDefaultInd", dto.getIsGrainUnseededDefaultInd());
				Assert.assertNotNull("CreateUser", dto.getCreateUser());
				Assert.assertNotNull("CreateDate", dto.getCreateDate());
				Assert.assertNotNull("UpdateUser", dto.getUpdateUser());
				Assert.assertNotNull("UpdateDate", dto.getUpdateDate());
			}
		}
	}
	
	@Test 
	public void testSelectValidation() throws Exception {	
		
		String userId = "admin";
		
		CropVarietyInsurabilityDao dao = persistenceSpringConfig.cropVarietyInsurabilityDao();

		//Test selectForInsurancePlan with the new variety without any insurability record.
		List<CropVarietyInsurabilityDto> dtos = dao.selectValidation(insurancePlanId);
		Assert.assertNotNull(dtos);

		for (CropVarietyInsurabilityDto dto : dtos) {
			Assert.assertNotNull("CropVarietyId", dto.getCropVarietyId());
			
			Assert.assertTrue("IsQuantityInsurableEditableInd", dto.getIsQuantityInsurableEditableInd() == true || dto.getIsQuantityInsurableEditableInd() == false);
			Assert.assertTrue("IsUnseededInsurableEditableInd", dto.getIsUnseededInsurableEditableInd() == true || dto.getIsUnseededInsurableEditableInd() == false);
			Assert.assertTrue("IsPlantInsurableEditableInd", dto.getIsPlantInsurableEditableInd() == true || dto.getIsPlantInsurableEditableInd() == false);
			Assert.assertTrue("IsAwpEligibleEditableInd", dto.getIsAwpEligibleEditableInd() == true || dto.getIsAwpEligibleEditableInd() == false);
			Assert.assertTrue("IsUnderseedingEligibleEditableInd", dto.getIsUnderseedingEligibleEditableInd() == true || dto.getIsUnderseedingEligibleEditableInd() == false);

		}
		
		//Create variety 
		addVariety(cropVarietyId, cropCommodityId, varietyName );

		//Create variety insurability
		createCropVarietyInsurability();
		
		//Create field
		createField();
		
		//Create inventory field and unseeded
		createInventoryFieldAndUnseeded();
		
		//Create seeded forage
		createSeededForage();
		
		dtos = dao.selectValidation(insurancePlanId);
		Assert.assertNotNull(dtos);

		//Expect true for all flags for the new variety
		assertAllEditableFlags(true, true, true, true, true);
		
		//Set IsQuantityInsurableInd true
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		InventorySeededForageDto fetchedDto = invSeededForageDao.fetch(inventorySeededForageGuid);
		Assert.assertNotNull(fetchedDto);
		fetchedDto.setIsQuantityInsurableInd(true);
		invSeededForageDao.update(fetchedDto, userId);
		
		//Expect false for IsQuantityInsurableEditableInd
		assertAllEditableFlags(false, true, true, true, true);
		
		//Set IsAwpEligibleInd true
		fetchedDto = invSeededForageDao.fetch(inventorySeededForageGuid);
		Assert.assertNotNull(fetchedDto);
		fetchedDto.setIsAwpEligibleInd(true);
		invSeededForageDao.update(fetchedDto, userId);		

		//Expect false for IsAwpEligibleInd as well
		assertAllEditableFlags(false, false, true, true, true);

		//Set a PlantInsurabilityTypeCode
		fetchedDto = invSeededForageDao.fetch(inventorySeededForageGuid);
		Assert.assertNotNull(fetchedDto);
		fetchedDto.setPlantInsurabilityTypeCode("W1");
		invSeededForageDao.update(fetchedDto, userId);		

		//Expect false for IsPlantInsurableEditableInd as well
		assertAllEditableFlags(false, false, false, true, true);
		
		//Set IsUnseededInsurableEditableInd true
		InventoryUnseededDao invUnseededDao = persistenceSpringConfig.inventoryUnseededDao();
		InventoryUnseededDto unseededDto = invUnseededDao.fetch(inventoryUnseededGuid);
		unseededDto.setIsUnseededInsurableInd(true);
		invUnseededDao.update(unseededDto, userId);

		//Expect false for IsUnseededInsurableEditableInd as well
		assertAllEditableFlags(false, false, false, false, true);

		//Set IsUnderseedingEligibleEditableInd true
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventoryFieldDto invDto = invFieldDao.fetch(inventoryFieldGuid);
		invDto.setUnderseededCropVarietyId(cropVarietyId);
		invFieldDao.update(invDto, userId);

		//Expect false for IsUnderseedingEligibleEditableInd as well
		assertAllEditableFlags(false, false, false, false, false);

		//DELETE
		delete();
	}
	

	
	private void assertAllEditableFlags(
			Boolean isQuantityInsurableEditableInd,
			Boolean isAwpEligibleEditableInd,
			Boolean isPlantInsurableEditableInd,
			Boolean isUnseededInsurableEditableInd,
			Boolean isUnderseedingEligibleEditableInd) throws DaoException {
		
		CropVarietyInsurabilityDao dao = persistenceSpringConfig.cropVarietyInsurabilityDao();
		List<CropVarietyInsurabilityDto> dtos = dao.selectValidation(insurancePlanId);
		Assert.assertNotNull(dtos);
		CropVarietyInsurabilityDto dto = getCropVarietyInsurabilityDto(dtos, cropVarietyId);
		Assert.assertNotNull(dto);
		
		Assert.assertEquals("IsQuantityInsurableEditableInd", isQuantityInsurableEditableInd, dto.getIsQuantityInsurableEditableInd());
		Assert.assertEquals("IsUnseededInsurableEditableInd", isUnseededInsurableEditableInd, dto.getIsUnseededInsurableEditableInd());
		Assert.assertEquals("IsPlantInsurableEditableInd", isPlantInsurableEditableInd, dto.getIsPlantInsurableEditableInd());
		Assert.assertEquals("IsAwpEligibleEditableInd", isAwpEligibleEditableInd, dto.getIsAwpEligibleEditableInd());
		Assert.assertEquals("IsUnderseedingEligibleEditableInd", isUnderseedingEligibleEditableInd, dto.getIsUnderseedingEligibleEditableInd());

	}
	
	private CropVarietyInsurabilityDto getCropVarietyInsurabilityDto(List<CropVarietyInsurabilityDto> dtos, Integer varietyId) {
		
		List<CropVarietyInsurabilityDto> filteredDtos = dtos.stream().filter(x -> x.getCropVarietyId().equals(varietyId)).collect(Collectors.toList());
		
		if(filteredDtos != null && filteredDtos.size() == 1) {
			return filteredDtos.get(0);
		}
		return null;
	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	private static Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
	
	private void createCropVarietyInsurability() throws DaoException {
		
		CropVarietyInsurabilityDao dao = persistenceSpringConfig.cropVarietyInsurabilityDao();
		CropVarietyInsurabilityDto newDto = new CropVarietyInsurabilityDto();
		
		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setCropVarietyId(cropVarietyId);
		newDto.setIsQuantityInsurableInd(true);
		newDto.setIsPlantInsurableInd(true);
		newDto.setIsAwpEligibleInd(true);
		newDto.setIsUnseededInsurableInd(true);
		newDto.setIsUnderseedingEligibleInd(true);
		newDto.setIsGrainUnseededDefaultInd(true);
		
		dao.insert(newDto, userId);

	}
	
	private void addVariety (Integer cropVarietyId, Integer cropCommodityId, String varietyName ) throws DaoException {
		
		CropVarietyDao daoVrty = persistenceSpringConfig.cropVarietyDao();
		CropVarietyDto newDtoVrty = new CropVarietyDto();
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());;
		
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);
		Date dataSyncTransDate = addSeconds(dateTime, -120);

		String userId = "JUNIT_TEST";

		//INSERT
		newDtoVrty.setCropVarietyId(cropVarietyId);
		newDtoVrty.setCropCommodityId(cropCommodityId);
		newDtoVrty.setVarietyName(varietyName);
		
		newDtoVrty.setEffectiveDate(effectiveDate);
		newDtoVrty.setExpiryDate(expiryDate);
		newDtoVrty.setDataSyncTransDate(dataSyncTransDate);

		daoVrty.insert(newDtoVrty, userId);
	}

	
	private void createField() throws DaoException {
		// INSERT FIELD
		
		String fieldLabel = "Test Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2030;
		String userId = "admin";
		
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel(fieldLabel);
		newFieldDto.setActiveFromCropYear(activeFromCropYear);
		newFieldDto.setActiveToCropYear(activeToCropYear);
		
		fieldDao.insertDataSync(newFieldDto, userId);
	}
	
	private void createInventoryFieldAndUnseeded() throws DaoException {
		
		String userId = "admin";
		
		// INSERT inventory field
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventoryFieldDto invFieldDto = new InventoryFieldDto();

		invFieldDto.setCropYear(2021);
		invFieldDto.setFieldId(fieldId);
		invFieldDto.setInsurancePlanId(insurancePlanId);
		invFieldDto.setLastYearCropCommodityId(20);
		invFieldDto.setLastYearCropCommodityName("FALL RYE");
		invFieldDto.setLastYearCropVarietyId(null);
		invFieldDto.setLastYearCropVarietyName(null);
		invFieldDto.setIsHiddenOnPrintoutInd(false);
		invFieldDto.setPlantingNumber(1);
		invFieldDto.setUnderseededCropVarietyId(null);
		
		invFieldDao.insert(invFieldDto, userId);
		inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();
		
		//Insert unseeded
		InventoryUnseededDao invUnseededDao = persistenceSpringConfig.inventoryUnseededDao();
		InventoryUnseededDto newDto = new InventoryUnseededDto();

		newDto.setAcresToBeSeeded(15.5);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropCommodityName("test");
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setIsUnseededInsurableInd(false);
		
		invUnseededDao.insert(newDto, userId);
		
		inventoryUnseededGuid = newDto.getInventoryUnseededGuid();
				
	}
	
	private void createSeededForage() throws DaoException {
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, 15, Calendar.JANUARY);
		Date seedingDate = cal.getTime();
		String userId = "admin";
		
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();

		InventorySeededForageDto newDto = new InventorySeededForageDto();

		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setCommodityTypeCode("CPSW");
		newDto.setCropCommodityId(65);
		newDto.setCropVarietyId(cropVarietyId);
		newDto.setCropVarietyName("test");
		newDto.setFieldAcres(10.4);
		newDto.setSeedingYear(2020);		
		newDto.setSeedingDate(seedingDate);	
		newDto.setIsIrrigatedInd(true);

		newDto.setIsQuantityInsurableInd(false);
		newDto.setPlantInsurabilityTypeCode(null);
		newDto.setIsAwpEligibleInd(false);
		
		invSeededForageDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventorySeededForageGuid());
		inventorySeededForageGuid = newDto.getInventorySeededForageGuid();
		
	}
}
