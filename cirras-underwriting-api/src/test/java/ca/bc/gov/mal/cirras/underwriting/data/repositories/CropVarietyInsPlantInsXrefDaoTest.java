package ca.bc.gov.mal.cirras.underwriting.data.repositories;

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

import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyInsPlantInsXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyInsurabilityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class CropVarietyInsPlantInsXrefDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer cropVarietyId = 98999;
	private Integer cropCommodityId = 65;
	private String varietyName = "Test Variety";
	private String userId = "JUNIT_TEST";
	private String plantInsurabilityTypeCode = "E1";
	private Integer insurancePlanId = 5;
	
	private Integer fieldId = 99588999;
	private String inventoryFieldGuid;
	private String inventorySeededForageGuid;

	private String cropVarietyInsurabilityGuid;
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteRecords();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteRecords();
	}
	
	private void deleteRecords() throws NotFoundDaoException, DaoException{

		deleteFieldAndInventory();
		
		// delete crop_variety_insurability
		CropVarietyInsurabilityDao daoIns = persistenceSpringConfig.cropVarietyInsurabilityDao();
		CropVarietyInsurabilityDto dtoIns = daoIns.fetch(cropVarietyId);
		if (dtoIns != null) {
			CropVarietyInsPlantInsXrefDao daoXref = persistenceSpringConfig.cropVarietyInsPlantInsXrefDao();
			daoXref.deleteForVariety(dtoIns.getCropVarietyInsurabilityGuid());
			
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
			InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
			invSeededForageDao.deleteForField(fieldId);
			InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
			invFieldDao.deleteForField(fieldId);
			dao.delete(fieldId);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteCropVariety() throws Exception {
		
		addVariety (cropVarietyId, cropCommodityId, varietyName, userId );
		
		String cropVarietyInsurabilityGuid = addCropVarietyInsurability(cropVarietyId, userId);
		
		CropVarietyInsPlantInsXrefDao dao = persistenceSpringConfig.cropVarietyInsPlantInsXrefDao();
		CropVarietyInsPlantInsXrefDto newDto = new CropVarietyInsPlantInsXrefDto();
		
		//INSERT
		newDto.setCropVarietyInsurabilityGuid(cropVarietyInsurabilityGuid);
		newDto.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		dao.insert(newDto, userId);
		
		//FETCH
		CropVarietyInsPlantInsXrefDto fetchedDto = dao.fetch(cropVarietyInsurabilityGuid, plantInsurabilityTypeCode);

		Assert.assertEquals("cropVarietyInsurabilityGuid", newDto.getCropVarietyInsurabilityGuid(), fetchedDto.getCropVarietyInsurabilityGuid());
		Assert.assertEquals("PlantInsurabilityTypeCode", newDto.getPlantInsurabilityTypeCode(), fetchedDto.getPlantInsurabilityTypeCode());
		
		//DELETE
		dao.delete(cropVarietyInsurabilityGuid, plantInsurabilityTypeCode);
		
		//FETCH
		CropVarietyInsPlantInsXrefDto deletedDto = dao.fetch(cropVarietyInsurabilityGuid, plantInsurabilityTypeCode);
		Assert.assertNull(deletedDto);
		
		// DELETE CropVarietyInsurability and CropVariety
		deleteRecords();
	}
	
	@Test 
	public void testSelectPlantInsForCropVarietyIds() throws Exception {	
		
		Integer existingCropVarietyId = 118;
		
		CropVarietyInsPlantInsXrefDao dao = persistenceSpringConfig.cropVarietyInsPlantInsXrefDao();

		List<CropVarietyInsPlantInsXrefDto> dtos = dao.selectPlantInsForCropVarieties(existingCropVarietyId);
		
		Assert.assertNotNull(dtos);
		
		for (CropVarietyInsPlantInsXrefDto dto : dtos ) {
			Assert.assertEquals("cropVarietyId", dto.getCropVarietyId(), existingCropVarietyId);
		}

	}
	
	@Test 
	public void testSelectPlantInsForAllCropVarietyId() throws Exception {	
		
		CropVarietyInsPlantInsXrefDao dao = persistenceSpringConfig.cropVarietyInsPlantInsXrefDao();

		List<CropVarietyInsPlantInsXrefDto> dtos = dao.selectPlantInsForCropVarieties(null);
		
		Assert.assertNotNull(dtos);

	}
	
	
	@Test 
	public void testSelectPlantInsForCropVarietyId() throws Exception {	
		
		CropVarietyInsPlantInsXrefDao dao = persistenceSpringConfig.cropVarietyInsPlantInsXrefDao();

		List<CropVarietyInsPlantInsXrefDto> dtos = dao.fetchAll();
		
		Assert.assertNotNull(dtos);

	}
	
	@Test 
	public void testSelectForInsurancePlan() throws Exception {	
		
		CropVarietyInsPlantInsXrefDao dao = persistenceSpringConfig.cropVarietyInsPlantInsXrefDao();

		List<CropVarietyInsPlantInsXrefDto> dtos = dao.selectForInsurancePlan(insurancePlanId);
		Assert.assertNotNull(dtos);
		
		for (CropVarietyInsPlantInsXrefDto dto : dtos ) {
			Assert.assertTrue("IsUsedInd", dto.getIsUsedInd() == true || dto.getIsUsedInd() == false);
			Assert.assertNotNull(dto.getCropVarietyInsurabilityGuid());
			Assert.assertNotNull(dto.getCropVarietyId());
			Assert.assertNotNull(dto.getPlantInsurabilityTypeCode());
			Assert.assertNotNull(dto.getDescription());
		}
		
		//Create variety 
		addVariety(cropVarietyId, cropCommodityId, varietyName, userId);

		//Create variety insurability
		addCropVarietyInsurability(cropVarietyId, userId);
		
		//Create plant insurability xref
		addCropVarietyInsPlantInsXref();
		
		//Create field
		createField();
		
		//Create inventory field
		createInventoryField();
		
		//Create seeded forage
		createSeededForage();

		//1. Test with Not Used
		dtos = dao.selectForInsurancePlan(insurancePlanId);
		Assert.assertNotNull(dtos);
		CropVarietyInsPlantInsXrefDto dto = getCropVarietyInsPlantInsXrefDto(dtos);
		Assert.assertNotNull(dto);
		
		//Expect false
		Assert.assertEquals("IsUsedInd", false, dto.getIsUsedInd());

		//Update seededForage.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		//Set a PlantInsurabilityTypeCode
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		InventorySeededForageDto fetchedDto = invSeededForageDao.fetch(inventorySeededForageGuid);
		Assert.assertNotNull(fetchedDto);
		fetchedDto.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		invSeededForageDao.update(fetchedDto, userId);		

		//2. Test with Used
		dtos = dao.selectForInsurancePlan(insurancePlanId);
		Assert.assertNotNull(dtos);
		dto = getCropVarietyInsPlantInsXrefDto(dtos);
		Assert.assertNotNull(dto);
		
		//Expect true
		Assert.assertEquals("IsUsedInd", true, dto.getIsUsedInd());
		
		deleteRecords();
	}
	
	private CropVarietyInsPlantInsXrefDto getCropVarietyInsPlantInsXrefDto(List<CropVarietyInsPlantInsXrefDto> dtos) {

		List<CropVarietyInsPlantInsXrefDto> filteredDtos = dtos.stream()
				.filter(x -> x.getCropVarietyInsurabilityGuid().equals(cropVarietyInsurabilityGuid) &&
						x.getPlantInsurabilityTypeCode().equals(plantInsurabilityTypeCode)).collect(Collectors.toList());
		
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
	
	private void addVariety (Integer cropVarietyId, Integer cropCommodityId, String varietyName, String userId ) throws DaoException {
		
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

		//INSERT
		newDtoVrty.setCropVarietyId(cropVarietyId);
		newDtoVrty.setCropCommodityId(cropCommodityId);
		newDtoVrty.setVarietyName(varietyName);
		
		newDtoVrty.setEffectiveDate(effectiveDate);
		newDtoVrty.setExpiryDate(expiryDate);
		newDtoVrty.setDataSyncTransDate(dataSyncTransDate);

		daoVrty.insert(newDtoVrty, userId);
	}

	private void addCropVarietyInsPlantInsXref() throws DaoException {
		CropVarietyInsPlantInsXrefDao dao = persistenceSpringConfig.cropVarietyInsPlantInsXrefDao();
		CropVarietyInsPlantInsXrefDto newDto = new CropVarietyInsPlantInsXrefDto();
		
		//INSERT
		newDto.setCropVarietyInsurabilityGuid(cropVarietyInsurabilityGuid);
		newDto.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		dao.insert(newDto, userId);

	}

	private String addCropVarietyInsurability (Integer cropVarietyId, String userId ) throws DaoException {
		
		CropVarietyInsurabilityDao dao = persistenceSpringConfig.cropVarietyInsurabilityDao();
		CropVarietyInsurabilityDto newDto = new CropVarietyInsurabilityDto();

		//INSERT
		newDto.setCropVarietyId(cropVarietyId);
		newDto.setIsQuantityInsurableInd(true);
		newDto.setIsPlantInsurableInd(false);
		newDto.setIsAwpEligibleInd(true);
		newDto.setIsUnseededInsurableInd(false);
		newDto.setIsUnderseedingEligibleInd(true);
		
		dao.insert(newDto, userId);
		
		cropVarietyInsurabilityGuid = newDto.getCropVarietyInsurabilityGuid();
		
		return newDto.getCropVarietyInsurabilityGuid();
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

	private void createInventoryField() throws DaoException {
		
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
