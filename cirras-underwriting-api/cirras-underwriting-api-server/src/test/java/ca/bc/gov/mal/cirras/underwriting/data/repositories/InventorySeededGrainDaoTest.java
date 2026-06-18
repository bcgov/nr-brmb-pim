package ca.bc.gov.mal.cirras.underwriting.data.repositories;

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

import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class InventorySeededGrainDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	private Integer fieldId2 = 99999999;
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteField();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteField();
	}
	
	private void deleteField() throws NotFoundDaoException, DaoException{
		
		FieldDao dao = persistenceSpringConfig.fieldDao();
		FieldDto dto = dao.fetch(fieldId2);
		if (dto != null) {
			InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
			invSeededGrainDao.deleteForField(fieldId2);
			InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
			invSeededForageDao.deleteForField(fieldId2);
			InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
			invFieldDao.deleteForField(fieldId2);
			dao.delete(fieldId2);
		}
	}

	@Test 
	public void testInventorySeededGrain() throws Exception {

		//Integer fieldId = 21949;
		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		String inventoryFieldGuid;
		String inventorySeededGrainGuid;
		Integer plantingNumber = 50;

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, 15, Calendar.JANUARY);
		Date seededDate = cal.getTime();
		
		String userId = "UNITTEST";
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
		
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
		InventorySeededGrainDto newDto = new InventorySeededGrainDto();

		newDto.setCommodityTypeCode("CPSW");
		newDto.setCommodityTypeDesc("Canadian Prairie Spring Wheat");
		newDto.setCropCommodityId(26);
		newDto.setCropCommodityName("WHEAT");
		newDto.setCropVarietyId(1010602);
		newDto.setCropVarietyName("AAC ENTICE");
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setIsPedigreeInd(true);
		newDto.setIsSpotLossInsurableInd(false);
		newDto.setIsQuantityInsurableInd(true);
		newDto.setIsReplacedInd(false);
		newDto.setSeededAcres(11.22);
		newDto.setSeededDate(seededDate);
		
		
		invSeededGrainDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventorySeededGrainGuid());
		inventorySeededGrainGuid = newDto.getInventorySeededGrainGuid();
		
		
		//SELECT
		List<InventorySeededGrainDto> dtos = invSeededGrainDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		InventorySeededGrainDto fetchedDto = invSeededGrainDao.fetch(inventorySeededGrainGuid);
		
		Assert.assertEquals("InventorySeededGrainGuid", newDto.getInventorySeededGrainGuid(), fetchedDto.getInventorySeededGrainGuid());
		Assert.assertEquals("CommodityTypeCode", newDto.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDesc", newDto.getCommodityTypeDesc(), fetchedDto.getCommodityTypeDesc());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("CropVarietyId", newDto.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", newDto.getCropVarietyName(), fetchedDto.getCropVarietyName());
		Assert.assertEquals("InventoryFieldGuid", newDto.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("IsSpotLossInsurableInd", newDto.getIsSpotLossInsurableInd(), fetchedDto.getIsSpotLossInsurableInd());
		Assert.assertEquals("IsQuantityInsurableInd", newDto.getIsQuantityInsurableInd(), fetchedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("IsReplacedInd", newDto.getIsReplacedInd(), fetchedDto.getIsReplacedInd());
		Assert.assertEquals("SeededAcres", newDto.getSeededAcres(), fetchedDto.getSeededAcres());
		Assert.assertEquals("SeededDate", newDto.getSeededDate(), fetchedDto.getSeededDate());

		//UPDATE
		cal.set(Calendar.DAY_OF_MONTH, 16);
		seededDate = cal.getTime();

		fetchedDto.setCommodityTypeCode("Six Row");
		fetchedDto.setCommodityTypeDesc("Six Row Standard");
		fetchedDto.setCropCommodityId(16);
		fetchedDto.setCropCommodityName("BARLEY");
		fetchedDto.setCropVarietyId(1010640);
		fetchedDto.setCropVarietyName("CDC TITANIUM");
		fetchedDto.setInventoryFieldGuid(inventoryFieldGuid);
		fetchedDto.setIsPedigreeInd(false);
		fetchedDto.setIsSpotLossInsurableInd(true);
		fetchedDto.setIsQuantityInsurableInd(false);
		fetchedDto.setIsReplacedInd(true);
		fetchedDto.setSeededAcres(33.44);
		fetchedDto.setSeededDate(seededDate);

		invSeededGrainDao.update(fetchedDto, userId);

		//FETCH
		InventorySeededGrainDto updatedDto = invSeededGrainDao.fetch(inventorySeededGrainGuid);

		Assert.assertEquals("InventorySeededGrainGuid", fetchedDto.getInventorySeededGrainGuid(), updatedDto.getInventorySeededGrainGuid());
		Assert.assertEquals("CommodityTypeCode", fetchedDto.getCommodityTypeCode(), updatedDto.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDesc", fetchedDto.getCommodityTypeDesc(), updatedDto.getCommodityTypeDesc());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", fetchedDto.getCropCommodityName(), updatedDto.getCropCommodityName());
		Assert.assertEquals("CropVarietyId", fetchedDto.getCropVarietyId(), updatedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", fetchedDto.getCropVarietyName(), updatedDto.getCropVarietyName());
		Assert.assertEquals("InventoryFieldGuid", fetchedDto.getInventoryFieldGuid(), updatedDto.getInventoryFieldGuid());
		Assert.assertEquals("IsPedigreeInd", fetchedDto.getIsPedigreeInd(), updatedDto.getIsPedigreeInd());
		Assert.assertEquals("IsSpotLossInsurableInd", fetchedDto.getIsSpotLossInsurableInd(), updatedDto.getIsSpotLossInsurableInd());
		Assert.assertEquals("IsQuantityInsurableInd", fetchedDto.getIsQuantityInsurableInd(), updatedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("IsReplacedInd", fetchedDto.getIsReplacedInd(), updatedDto.getIsReplacedInd());
		Assert.assertEquals("SeededAcres", fetchedDto.getSeededAcres(), updatedDto.getSeededAcres());
		Assert.assertEquals("SeededDate", fetchedDto.getSeededDate(), updatedDto.getSeededDate());

		//FETCH SIMPLE (only returns the inventory seeded grain record)
		updatedDto = invSeededGrainDao.fetchSimple(inventorySeededGrainGuid);

		Assert.assertNull("CommodityTypeDesc", updatedDto.getCommodityTypeDesc());
		Assert.assertNull("CropCommodityName", updatedDto.getCropCommodityName());
		Assert.assertNull("CropVarietyName", updatedDto.getCropVarietyName());

		Assert.assertEquals("InventorySeededGrainGuid", fetchedDto.getInventorySeededGrainGuid(), updatedDto.getInventorySeededGrainGuid());
		Assert.assertEquals("CommodityTypeCode", fetchedDto.getCommodityTypeCode(), updatedDto.getCommodityTypeCode());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropVarietyId", fetchedDto.getCropVarietyId(), updatedDto.getCropVarietyId());
		Assert.assertEquals("InventoryFieldGuid", fetchedDto.getInventoryFieldGuid(), updatedDto.getInventoryFieldGuid());
		Assert.assertEquals("IsPedigreeInd", fetchedDto.getIsPedigreeInd(), updatedDto.getIsPedigreeInd());
		Assert.assertEquals("IsSpotLossInsurableInd", fetchedDto.getIsSpotLossInsurableInd(), updatedDto.getIsSpotLossInsurableInd());
		Assert.assertEquals("IsQuantityInsurableInd", fetchedDto.getIsQuantityInsurableInd(), updatedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("IsReplacedInd", fetchedDto.getIsReplacedInd(), updatedDto.getIsReplacedInd());
		Assert.assertEquals("SeededAcres", fetchedDto.getSeededAcres(), updatedDto.getSeededAcres());
		Assert.assertEquals("SeededDate", fetchedDto.getSeededDate(), updatedDto.getSeededDate());

		
		//INSERT second seeded grain record
		InventorySeededGrainDto newDto2 = new InventorySeededGrainDto();

		newDto2.setCommodityTypeCode(null);
		newDto2.setCommodityTypeDesc(null);
		newDto2.setCropCommodityId(null);
		newDto2.setCropCommodityName(null);
		newDto2.setCropVarietyId(null);
		newDto2.setCropVarietyName(null);
		newDto2.setInventoryFieldGuid(inventoryFieldGuid);
		newDto2.setIsPedigreeInd(false);
		newDto2.setIsSpotLossInsurableInd(false);
		newDto2.setIsQuantityInsurableInd(false);
		newDto2.setIsReplacedInd(false);
		newDto2.setSeededAcres(null);
		newDto2.setSeededDate(null);		
		
		invSeededGrainDao.insert(newDto2, userId);
		
		//FETCH
		InventorySeededGrainDto fetchedDto2 = invSeededGrainDao.fetch(newDto2.getInventorySeededGrainGuid());
		
		Assert.assertEquals("InventorySeededGrainGuid", newDto2.getInventorySeededGrainGuid(), fetchedDto2.getInventorySeededGrainGuid());
		Assert.assertEquals("CommodityTypeCode", newDto2.getCommodityTypeCode(), fetchedDto2.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDesc", newDto2.getCommodityTypeDesc(), fetchedDto2.getCommodityTypeDesc());
		Assert.assertEquals("CropCommodityId", newDto2.getCropCommodityId(), fetchedDto2.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto2.getCropCommodityName(), fetchedDto2.getCropCommodityName());
		Assert.assertEquals("CropVarietyId", newDto2.getCropVarietyId(), fetchedDto2.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", newDto2.getCropVarietyName(), fetchedDto2.getCropVarietyName());
		Assert.assertEquals("InventoryFieldGuid", newDto2.getInventoryFieldGuid(), fetchedDto2.getInventoryFieldGuid());
		Assert.assertEquals("IsPedigreeInd", newDto2.getIsPedigreeInd(), fetchedDto2.getIsPedigreeInd());
		Assert.assertEquals("IsSpotLossInsurableInd", newDto2.getIsSpotLossInsurableInd(), fetchedDto2.getIsSpotLossInsurableInd());
		Assert.assertEquals("IsQuantityInsurableInd", newDto2.getIsQuantityInsurableInd(), fetchedDto2.getIsQuantityInsurableInd());
		Assert.assertEquals("IsReplacedInd", newDto2.getIsReplacedInd(), fetchedDto2.getIsReplacedInd());
		Assert.assertEquals("SeededAcres", newDto2.getSeededAcres(), fetchedDto2.getSeededAcres());
		Assert.assertEquals("SeededDate", newDto2.getSeededDate(), fetchedDto2.getSeededDate());
		
		//SELECT
		dtos = invSeededGrainDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		
		//DELETE
		invSeededGrainDao.delete(newDto2.getInventorySeededGrainGuid());
		invSeededGrainDao.delete(inventorySeededGrainGuid);
		
		//FETCH
		InventorySeededGrainDto deletedDto = invSeededGrainDao.fetchSimple(inventorySeededGrainGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = invSeededGrainDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		//DELETE parent InventoryField
		invFieldDao.delete(inventoryFieldGuid);
		
	}

	@Test 
	public void testDeleteForField() throws Exception {

		FieldDao dao = persistenceSpringConfig.fieldDao();
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

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
		InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
		InventorySeededGrainDto newDto = new InventorySeededGrainDto();

		newDto.setCommodityTypeCode("CPSW");
		newDto.setCommodityTypeDesc("Canadian Prairie Spring Wheat");
		newDto.setCropCommodityId(26);
		newDto.setCropCommodityName("WHEAT");
		newDto.setCropVarietyId(1010602);
		newDto.setCropVarietyName("AAC ENTICE");
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setIsPedigreeInd(true);
		newDto.setIsSpotLossInsurableInd(false);
		newDto.setIsQuantityInsurableInd(true);
		newDto.setIsReplacedInd(false);
		newDto.setSeededAcres(11.22);
		newDto.setSeededDate(dateTime);
		
		invSeededGrainDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventorySeededGrainGuid());

		
		//SELECT
		List<InventorySeededGrainDto> dtos = invSeededGrainDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals("total unseeded records", 1, dtos.size());

		//Delete all unseeded records for field
		invSeededGrainDao.deleteForField(fieldId2);
		dtos = invSeededGrainDao.select(inventoryFieldGuid);
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
	public void testSelectForDeclaredYield() throws Exception {

		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		String inventoryFieldGuid;
		Integer plantingNumber = 1;
		
		String userId = "UNITTEST";

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date seededDate = cal.getTime();
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
		
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
		invFieldDto.setUnderseededAcres(null);
		invFieldDto.setUnderseededCropVarietyId(null);

		
		invFieldDao.insert(invFieldDto, userId);
		inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		//INSERT
		InventorySeededGrainDto isgDto1 = new InventorySeededGrainDto();

		isgDto1.setCommodityTypeCode("CPSW");
		isgDto1.setCommodityTypeDesc("Canadian Prairie Spring Wheat");
		isgDto1.setCropCommodityId(26);
		isgDto1.setCropCommodityName("WHEAT");
		isgDto1.setCropVarietyId(1010602);
		isgDto1.setCropVarietyName("AAC ENTICE");
		isgDto1.setInventoryFieldGuid(inventoryFieldGuid);
		isgDto1.setIsPedigreeInd(false);
		isgDto1.setIsSpotLossInsurableInd(false);
		isgDto1.setIsQuantityInsurableInd(true);
		isgDto1.setIsReplacedInd(false);
		isgDto1.setSeededAcres(0.0);
		isgDto1.setSeededDate(seededDate);
		
		invSeededGrainDao.insert(isgDto1, userId);
		Assert.assertNotNull(isgDto1.getInventorySeededGrainGuid());
		
		//INSERT
		InventorySeededGrainDto isgDto2 = new InventorySeededGrainDto();

		isgDto2.setCommodityTypeCode(null);
		isgDto2.setCommodityTypeDesc(null);
		isgDto2.setCropCommodityId(26);
		isgDto2.setCropCommodityName("WHEAT");
		isgDto2.setCropVarietyId(null);
		isgDto2.setCropVarietyName(null);
		isgDto2.setInventoryFieldGuid(inventoryFieldGuid);
		isgDto2.setIsPedigreeInd(false);
		isgDto2.setIsSpotLossInsurableInd(false);
		isgDto2.setIsQuantityInsurableInd(false);
		isgDto2.setIsReplacedInd(false);
		isgDto2.setSeededAcres(11.22);
		isgDto2.setSeededDate(null);

		invSeededGrainDao.insert(isgDto2, userId);
		Assert.assertNotNull(isgDto2.getInventorySeededGrainGuid());
		
		//SELECT FOR DECLARED YIELD
		// Only the second isg record is returned.
		List<InventorySeededGrainDto> dtos = invSeededGrainDao.selectForDeclaredYield(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		InventorySeededGrainDto fetchedDto = dtos.get(0);

		Assert.assertEquals("InventorySeededGrainGuid", isgDto2.getInventorySeededGrainGuid(), fetchedDto.getInventorySeededGrainGuid());
		Assert.assertEquals("CommodityTypeCode", isgDto2.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDesc", isgDto2.getCommodityTypeDesc(), fetchedDto.getCommodityTypeDesc());
		Assert.assertEquals("CropCommodityId", isgDto2.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", isgDto2.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("CropVarietyId", isgDto2.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", isgDto2.getCropVarietyName(), fetchedDto.getCropVarietyName());
		Assert.assertEquals("InventoryFieldGuid", isgDto2.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("IsPedigreeInd", isgDto2.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("IsSpotLossInsurableInd", isgDto2.getIsSpotLossInsurableInd(), fetchedDto.getIsSpotLossInsurableInd());
		Assert.assertEquals("IsQuantityInsurableInd", isgDto2.getIsQuantityInsurableInd(), fetchedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("IsReplacedInd", isgDto2.getIsReplacedInd(), fetchedDto.getIsReplacedInd());
		Assert.assertEquals("SeededAcres", isgDto2.getSeededAcres(), fetchedDto.getSeededAcres());
		Assert.assertEquals("SeededDate", isgDto2.getSeededDate(), fetchedDto.getSeededDate());

		//UPDATES
		isgDto1 = invSeededGrainDao.fetch(isgDto1.getInventorySeededGrainGuid());
		isgDto1.setSeededAcres(33.44);		
		invSeededGrainDao.update(isgDto1, userId);

		isgDto2 = invSeededGrainDao.fetch(isgDto2.getInventorySeededGrainGuid());
		isgDto2.setCommodityTypeCode(null);
		isgDto2.setCommodityTypeDesc(null);
		isgDto2.setCropCommodityId(null);
		isgDto2.setCropCommodityName(null);
		isgDto2.setCropVarietyId(null);
		isgDto2.setCropVarietyName(null);		
		invSeededGrainDao.update(isgDto2, userId);
		
		
		//SELECT FOR DECLARED YIELD
		// Only the first isg record is returned.
		dtos = invSeededGrainDao.selectForDeclaredYield(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		fetchedDto = dtos.get(0);

		Assert.assertEquals("InventorySeededGrainGuid", isgDto1.getInventorySeededGrainGuid(), fetchedDto.getInventorySeededGrainGuid());
		Assert.assertEquals("CommodityTypeCode", isgDto1.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDesc", isgDto1.getCommodityTypeDesc(), fetchedDto.getCommodityTypeDesc());
		Assert.assertEquals("CropCommodityId", isgDto1.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", isgDto1.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("CropVarietyId", isgDto1.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", isgDto1.getCropVarietyName(), fetchedDto.getCropVarietyName());
		Assert.assertEquals("InventoryFieldGuid", isgDto1.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("IsPedigreeInd", isgDto1.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("IsSpotLossInsurableInd", isgDto1.getIsSpotLossInsurableInd(), fetchedDto.getIsSpotLossInsurableInd());
		Assert.assertEquals("IsQuantityInsurableInd", isgDto1.getIsQuantityInsurableInd(), fetchedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("IsReplacedInd", isgDto1.getIsReplacedInd(), fetchedDto.getIsReplacedInd());
		Assert.assertEquals("SeededAcres", isgDto1.getSeededAcres(), fetchedDto.getSeededAcres());
		Assert.assertEquals("SeededDate", isgDto1.getSeededDate(), fetchedDto.getSeededDate());
		
		
		//DELETE
		invSeededGrainDao.delete(isgDto1.getInventorySeededGrainGuid());
		invSeededGrainDao.delete(isgDto2.getInventorySeededGrainGuid());
		

		//SELECT
		dtos = invSeededGrainDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		//DELETE parent InventoryField
		invFieldDao.delete(inventoryFieldGuid);		
	}

	@Test 
	public void testSelectForVerifiedYield() throws Exception {

		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		String inventoryFieldGuid;
		Integer plantingNumber = 1;
		
		String userId = "UNITTEST";

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date seededDate = cal.getTime();
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
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
		invFieldDto.setUnderseededAcres(null);
		invFieldDto.setUnderseededCropVarietyId(null);

		
		invFieldDao.insert(invFieldDto, userId);
		inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		//INSERT ZERO ACRES
		InventorySeededGrainDto isgDto1 = new InventorySeededGrainDto();

		isgDto1.setCommodityTypeCode("CPSW");
		isgDto1.setCommodityTypeDesc("Canadian Prairie Spring Wheat");
		isgDto1.setCropCommodityId(26);
		isgDto1.setCropCommodityName("WHEAT");
		isgDto1.setCropVarietyId(1010602);
		isgDto1.setCropVarietyName("AAC ENTICE");
		isgDto1.setInventoryFieldGuid(inventoryFieldGuid);
		isgDto1.setIsPedigreeInd(false);
		isgDto1.setIsSpotLossInsurableInd(false);
		isgDto1.setIsQuantityInsurableInd(true);
		isgDto1.setIsReplacedInd(false);
		isgDto1.setSeededAcres(0.0);
		isgDto1.setSeededDate(seededDate);
		
		invSeededGrainDao.insert(isgDto1, userId);
		Assert.assertNotNull(isgDto1.getInventorySeededGrainGuid());
		
		//INSERT
		InventorySeededGrainDto isgDto2 = new InventorySeededGrainDto();

		isgDto2.setCommodityTypeCode(null);
		isgDto2.setCommodityTypeDesc(null);
		isgDto2.setCropCommodityId(26);
		isgDto2.setCropCommodityName("WHEAT");
		isgDto2.setCropVarietyId(null);
		isgDto2.setCropVarietyName(null);
		isgDto2.setInventoryFieldGuid(inventoryFieldGuid);
		isgDto2.setIsPedigreeInd(false);
		isgDto2.setIsSpotLossInsurableInd(false);
		isgDto2.setIsQuantityInsurableInd(false);
		isgDto2.setIsReplacedInd(false);
		isgDto2.setSeededAcres(11.22);
		isgDto2.setSeededDate(null);

		invSeededGrainDao.insert(isgDto2, userId);
		Assert.assertNotNull(isgDto2.getInventorySeededGrainGuid());

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
		
		invSeededForageDao.insert(isfDto3, userId);
		Assert.assertNotNull(isfDto3.getInventorySeededForageGuid());
		
		//SELECT FOR DECLARED YIELD
		// Only the second isg record is returned.
		List<InventorySeededGrainDto> dtos = invSeededGrainDao.selectForVerifiedYield(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		InventorySeededGrainDto fetchedDto = dtos.get(0);

		Assert.assertEquals("InventorySeededGrainGuid", isgDto2.getInventorySeededGrainGuid(), fetchedDto.getInventorySeededGrainGuid());
		Assert.assertEquals("CommodityTypeCode", isgDto2.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDesc", isgDto2.getCommodityTypeDesc(), fetchedDto.getCommodityTypeDesc());
		Assert.assertEquals("CropCommodityId", isgDto2.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", isgDto2.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("CropVarietyId", isgDto2.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", isgDto2.getCropVarietyName(), fetchedDto.getCropVarietyName());
		Assert.assertEquals("InventoryFieldGuid", isgDto2.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("IsPedigreeInd", isgDto2.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("IsSpotLossInsurableInd", isgDto2.getIsSpotLossInsurableInd(), fetchedDto.getIsSpotLossInsurableInd());
		Assert.assertEquals("IsQuantityInsurableInd", isgDto2.getIsQuantityInsurableInd(), fetchedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("IsReplacedInd", isgDto2.getIsReplacedInd(), fetchedDto.getIsReplacedInd());
		Assert.assertEquals("SeededAcres", isgDto2.getSeededAcres(), fetchedDto.getSeededAcres());
		Assert.assertEquals("SeededDate", isgDto2.getSeededDate(), fetchedDto.getSeededDate());

		//UPDATES
		isgDto1 = invSeededGrainDao.fetch(isgDto1.getInventorySeededGrainGuid());
		isgDto1.setSeededAcres(33.44);		
		invSeededGrainDao.update(isgDto1, userId);

		isgDto2 = invSeededGrainDao.fetch(isgDto2.getInventorySeededGrainGuid());
		isgDto2.setCommodityTypeCode(null);
		isgDto2.setCommodityTypeDesc(null);
		isgDto2.setCropCommodityId(null);
		isgDto2.setCropCommodityName(null);
		isgDto2.setCropVarietyId(null);
		isgDto2.setCropVarietyName(null);		
		invSeededGrainDao.update(isgDto2, userId);
		
		
		//SELECT FOR DECLARED YIELD
		// Only the first isg record is returned.
		dtos = invSeededGrainDao.selectForVerifiedYield(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		fetchedDto = dtos.get(0);

		Assert.assertEquals("InventorySeededGrainGuid", isgDto1.getInventorySeededGrainGuid(), fetchedDto.getInventorySeededGrainGuid());
		Assert.assertEquals("CommodityTypeCode", isgDto1.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDesc", isgDto1.getCommodityTypeDesc(), fetchedDto.getCommodityTypeDesc());
		Assert.assertEquals("CropCommodityId", isgDto1.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", isgDto1.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("CropVarietyId", isgDto1.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", isgDto1.getCropVarietyName(), fetchedDto.getCropVarietyName());
		Assert.assertEquals("InventoryFieldGuid", isgDto1.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("IsPedigreeInd", isgDto1.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("IsSpotLossInsurableInd", isgDto1.getIsSpotLossInsurableInd(), fetchedDto.getIsSpotLossInsurableInd());
		Assert.assertEquals("IsQuantityInsurableInd", isgDto1.getIsQuantityInsurableInd(), fetchedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("IsReplacedInd", isgDto1.getIsReplacedInd(), fetchedDto.getIsReplacedInd());
		Assert.assertEquals("SeededAcres", isgDto1.getSeededAcres(), fetchedDto.getSeededAcres());
		Assert.assertEquals("SeededDate", isgDto1.getSeededDate(), fetchedDto.getSeededDate());
		
		
		//DELETE
		invSeededGrainDao.delete(isgDto1.getInventorySeededGrainGuid());
		invSeededGrainDao.delete(isgDto2.getInventorySeededGrainGuid());
		invSeededForageDao.delete(isfDto3.getInventorySeededForageGuid());
		

		//SELECT
		dtos = invSeededGrainDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		//DELETE parent InventoryField
		invFieldDao.delete(inventoryFieldGuid);		
	}
	
	@Test 
	public void testSelectForContractField() throws Exception {

		FieldDao dao = persistenceSpringConfig.fieldDao();
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();

		String userId = "JUNIT_TEST";

		//INSERT Field
		createField(userId);

		//Add inventory field
		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		
		// INSERT inventory field and unseeded
		createInventoryFieldAndSeeded(cropYear, insurancePlanId, 1, 25.0, 16, "BARLEY", false, true, false);
		createInventoryFieldAndSeeded(cropYear, insurancePlanId, 2, 10.0, 16, "BARLEY", true, true, false);
		createInventoryFieldAndSeeded(cropYear, insurancePlanId, 3, 7.0, 16, "BARLEY", true, false, false);
		createInventoryFieldAndSeeded(cropYear, insurancePlanId, 4, 13.0, 21, "FIELD PEA", true, false, false);
		createInventoryFieldAndSeeded(cropYear, insurancePlanId, 5, 27.0, 21, "FIELD PEA", true, false, false);
		createInventoryFieldAndSeeded(cropYear, insurancePlanId, 6, 16.0, 18, "CANOLA", false, true, false);
		createInventoryFieldAndSeeded(cropYear, insurancePlanId, 7, 22.0, 18, "CANOLA", true, true, true);
		//Barley: total acres: 17; total spot loss: 35
		//Field Pea: total acres: 40; total spot loss: 
		//Canola: total acres: 0; total spot loss: 16
		//Canola PEDIGREE: total acres: 18; total spot loss: 18

		//SELECT
		List<InventorySeededGrainDto> dtoList = invSeededGrainDao.selectTotalsForFieldYearPlan(fieldId2, cropYear, insurancePlanId);
		
		for (InventorySeededGrainDto dto : dtoList) {

			if(dto.getCropCommodityId().equals(16) && dto.getIsPedigreeInd() == false) { //Barley
				Assert.assertEquals("total seeded acres wrong (" + dto.getCropCommodityId() + ")", (Double)17.0, dto.getTotalSeededAcres());
				Assert.assertEquals("total spot loss acres wrong (" + dto.getCropCommodityId() + ")", (Double)35.0, dto.getTotalSpotLossAcres());
			} else if (dto.getCropCommodityId().equals(21) && dto.getIsPedigreeInd() == false) { //Field Pea
				Assert.assertEquals("total seeded acres wrong (" + dto.getCropCommodityId() + ")", (Double)40.0, dto.getTotalSeededAcres());
				Assert.assertEquals("total spot loss acres wrong (" + dto.getCropCommodityId() + ")", (Double)0.0, dto.getTotalSpotLossAcres());
			} else if (dto.getCropCommodityId().equals(18) && dto.getIsPedigreeInd() == false) { //Canola
				Assert.assertEquals("total seeded acres wrong (" + dto.getCropCommodityId() + ")", (Double)0.0, dto.getTotalSeededAcres());
				Assert.assertEquals("total spot loss acres wrong (" + dto.getCropCommodityId() + ")", (Double)16.0, dto.getTotalSpotLossAcres());
			} else if (dto.getCropCommodityId().equals(18) && dto.getIsPedigreeInd() == true) { //Canola PEDIGREE
				Assert.assertEquals("total seeded acres wrong (Pedigree) (" + dto.getCropCommodityId() + ")", (Double)22.0, dto.getTotalSeededAcres());
				Assert.assertEquals("total spot loss acres wrong (Pedigree) (" + dto.getCropCommodityId() + ")", (Double)22.0, dto.getTotalSpotLossAcres());
			}
			
		}
		
		//CLEAN UP
		//Delete all seeded records for field
		invSeededGrainDao.deleteForField(fieldId2);
		
		//Delete all inventory field records for field
		invFieldDao.deleteForField(fieldId2);
		
		//DELETE
		dao.delete(fieldId2);

		//FETCH
		FieldDto deletedDto = dao.fetch(fieldId2);
		Assert.assertNull(deletedDto);		
		
	}	
	
	private void createInventoryFieldAndSeeded(
			Integer cropYear, 
			Integer insurancePlanId, 
			Integer plantingNumber,
			Double seededAcres,
			Integer cropCommodityId,
			String cropName,
			Boolean isQuantityInsurable,
			Boolean isSpotLossInsurable,
			Boolean isPedigree) throws DaoException {
		
		String userId = "admin";
		
		// INSERT inventory field
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
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
		String inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();
		
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
		newDto.setCropCommodityName(cropName);
		newDto.setCropVarietyId(1010602);
		newDto.setCropVarietyName("AAC ENTICE");
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setIsPedigreeInd(isPedigree);
		newDto.setIsSpotLossInsurableInd(isSpotLossInsurable);
		newDto.setIsQuantityInsurableInd(isQuantityInsurable);
		newDto.setIsReplacedInd(false);
		newDto.setSeededAcres(seededAcres);
		newDto.setSeededDate(dateTime);
		
		invSeededGrainDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventorySeededGrainGuid());
		
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
