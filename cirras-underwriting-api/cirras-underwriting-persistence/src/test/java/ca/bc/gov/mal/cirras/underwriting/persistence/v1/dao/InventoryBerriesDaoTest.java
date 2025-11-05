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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class InventoryBerriesDaoTest {
	
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
			InventoryBerriesDao invBerriesDao = persistenceSpringConfig.inventoryBerriesDao();
			invBerriesDao.deleteForField(fieldId2);
			InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
			invFieldDao.deleteForField(fieldId2);
			dao.delete(fieldId2);
		}
	}

	
	@Test 
	public void testInventoryBerries() throws Exception {

		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		String inventoryFieldGuid;
		String inventoryBerriesGuid;
		String userId = "UNITTEST";
		
		createField();
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventoryBerriesDao invBerriesDao = persistenceSpringConfig.inventoryBerriesDao();

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
		invFieldDto.setPlantingNumber(1);

		
		invFieldDao.insert(invFieldDto, userId);
		inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		//INSERT
		InventoryBerriesDto newDto = new InventoryBerriesDto();

		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setCropCommodityId(10);
		newDto.setCropCommodityName("BLUEBERRY");
		newDto.setCropVarietyId(1010689);
		newDto.setCropVarietyName("BLUEJAY");
		newDto.setPlantInsurabilityTypeCode("ST1");
		newDto.setPlantedYear(2020);
		newDto.setPlantedAcres((double)100);
		newDto.setRowSpacing(10);
		newDto.setPlantSpacing(5.3);
		newDto.setTotalPlants(5000);
		newDto.setIsQuantityInsurableInd(true);
		newDto.setIsPlantInsurableInd(false);
		newDto.setBogId("BogId");
		newDto.setBogMowedDate(getDate(2020, Calendar.JANUARY, 15));
		newDto.setBogRenovatedDate(getDate(2020, Calendar.JANUARY, 20));
		newDto.setIsHarvestedInd(false);

		
		invBerriesDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventoryBerriesGuid());
		inventoryBerriesGuid = newDto.getInventoryBerriesGuid();
		
		//SELECT
		List<InventoryBerriesDto> dtos = invBerriesDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		InventoryBerriesDto fetchedDto = invBerriesDao.fetch(inventoryBerriesGuid);
		
		Assert.assertEquals("InventoryBerriesGuid", newDto.getInventoryBerriesGuid(), fetchedDto.getInventoryBerriesGuid());
		Assert.assertEquals("InventoryFieldGuid", newDto.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("CropVarietyName", newDto.getCropVarietyName(), fetchedDto.getCropVarietyName());
		Assert.assertEquals("CropVarietyId", newDto.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("PlantInsurabilityTypeCode", newDto.getPlantInsurabilityTypeCode(), fetchedDto.getPlantInsurabilityTypeCode());
		Assert.assertEquals("PlantedYear", newDto.getPlantedYear(), fetchedDto.getPlantedYear());
		Assert.assertEquals("PlantedAcres", newDto.getPlantedAcres(), fetchedDto.getPlantedAcres());
		Assert.assertEquals("RowSpacing", newDto.getRowSpacing(), fetchedDto.getRowSpacing());
		Assert.assertEquals("PlantSpacing", newDto.getPlantSpacing(), fetchedDto.getPlantSpacing());
		Assert.assertEquals("TotalPlants", newDto.getTotalPlants(), fetchedDto.getTotalPlants());
		Assert.assertEquals("IsQuantityInsurableInd", newDto.getIsQuantityInsurableInd(), fetchedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("IsPlantInsurableInd", newDto.getIsPlantInsurableInd(), fetchedDto.getIsPlantInsurableInd());
		Assert.assertEquals("BogId", newDto.getBogId(), fetchedDto.getBogId());
		Assert.assertEquals("BogMowedDate", newDto.getBogMowedDate(), fetchedDto.getBogMowedDate());
		Assert.assertEquals("BogRenovatedDate", newDto.getBogRenovatedDate(), fetchedDto.getBogRenovatedDate());
		Assert.assertEquals("IsHarvestedInd", newDto.getIsHarvestedInd(), fetchedDto.getIsHarvestedInd());
		
		//UPDATE
		fetchedDto.setCropCommodityId(13);
		fetchedDto.setCropCommodityName("STRAWBERRY");
		fetchedDto.setCropVarietyId(1010702);
		fetchedDto.setCropVarietyName("HOOD");
		fetchedDto.setPlantInsurabilityTypeCode("ST2");
		fetchedDto.setPlantedYear(2021);
		fetchedDto.setPlantedAcres((double)101);
		fetchedDto.setRowSpacing(11);
		fetchedDto.setPlantSpacing(5.5);
		fetchedDto.setTotalPlants(5001);
		fetchedDto.setIsQuantityInsurableInd(false);
		fetchedDto.setIsPlantInsurableInd(true);
		fetchedDto.setBogId("BogId 2");
		fetchedDto.setBogMowedDate(getDate(2020, Calendar.FEBRUARY, 16));
		fetchedDto.setBogRenovatedDate(getDate(2020, Calendar.FEBRUARY, 21));
		fetchedDto.setIsHarvestedInd(true);

		invBerriesDao.update(fetchedDto, userId);

		//FETCH
		InventoryBerriesDto updatedDto = invBerriesDao.fetch(inventoryBerriesGuid);

		Assert.assertEquals("InventoryBerriesGuid", fetchedDto.getInventoryBerriesGuid(), updatedDto.getInventoryBerriesGuid());
		Assert.assertEquals("InventoryFieldGuid", fetchedDto.getInventoryFieldGuid(), updatedDto.getInventoryFieldGuid());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", fetchedDto.getCropCommodityName(), updatedDto.getCropCommodityName());
		Assert.assertEquals("CropVarietyName", fetchedDto.getCropVarietyName(), updatedDto.getCropVarietyName());
		Assert.assertEquals("CropVarietyId", fetchedDto.getCropVarietyId(), updatedDto.getCropVarietyId());
		Assert.assertEquals("PlantInsurabilityTypeCode", fetchedDto.getPlantInsurabilityTypeCode(), updatedDto.getPlantInsurabilityTypeCode());
		Assert.assertEquals("PlantedYear", fetchedDto.getPlantedYear(), updatedDto.getPlantedYear());
		Assert.assertEquals("PlantedAcres", fetchedDto.getPlantedAcres(), updatedDto.getPlantedAcres());
		Assert.assertEquals("RowSpacing", fetchedDto.getRowSpacing(), updatedDto.getRowSpacing());
		Assert.assertEquals("PlantSpacing", fetchedDto.getPlantSpacing(), updatedDto.getPlantSpacing());
		Assert.assertEquals("TotalPlants", fetchedDto.getTotalPlants(), updatedDto.getTotalPlants());
		Assert.assertEquals("IsQuantityInsurableInd", fetchedDto.getIsQuantityInsurableInd(), updatedDto.getIsQuantityInsurableInd());
		Assert.assertEquals("IsPlantInsurableInd", fetchedDto.getIsPlantInsurableInd(), updatedDto.getIsPlantInsurableInd());
		Assert.assertEquals("BogId", fetchedDto.getBogId(), updatedDto.getBogId());
		Assert.assertEquals("BogMowedDate", fetchedDto.getBogMowedDate(), updatedDto.getBogMowedDate());
		Assert.assertEquals("BogRenovatedDate", fetchedDto.getBogRenovatedDate(), updatedDto.getBogRenovatedDate());
		Assert.assertEquals("IsHarvestedInd", fetchedDto.getIsHarvestedInd(), updatedDto.getIsHarvestedInd());

		//DELETE
		invBerriesDao.delete(inventoryBerriesGuid);
		
		//FETCH
		InventoryBerriesDto deletedDto = invBerriesDao.fetch(inventoryBerriesGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = invBerriesDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		//DELETE parent InventoryField
		invFieldDao.delete(inventoryFieldGuid);
		
	}

	private Date getDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month, day);
		return cal.getTime();
	}
	
	@Test 
	public void testDeleteForField() throws Exception {

		String userId = "JUNIT_TEST";

		FieldDao dao = persistenceSpringConfig.fieldDao();

		createField();

		//Add inventory field
		Integer cropYear = 2020;
		Integer insurancePlanId = 3;
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();

		// INSERT inventory field
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

		
		invFieldDao.insert(invFieldDto, userId);

		String inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		//INSERT
		InventoryBerriesDao invBerriesDao = persistenceSpringConfig.inventoryBerriesDao();
		InventoryBerriesDto newDto = new InventoryBerriesDto();

		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setCropCommodityId(10);
		newDto.setCropCommodityName("BLUEBERRY");
		newDto.setIsQuantityInsurableInd(true);
		newDto.setIsPlantInsurableInd(false);
		newDto.setIsHarvestedInd(true);
		
		invBerriesDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventoryBerriesGuid());

		
		//SELECT
		List<InventoryBerriesDto> dtos = invBerriesDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals("total berries records", 1, dtos.size());

		//Delete all berries records for field
		invBerriesDao.deleteForField(fieldId2);
		dtos = invBerriesDao.select(inventoryFieldGuid);
		Assert.assertTrue("berries records not deleted", dtos == null || dtos.size() == 0);
		
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
	public void testDeleteForInventoryField() throws Exception {

		String userId = "JUNIT_TEST";

		FieldDao dao = persistenceSpringConfig.fieldDao();

		createField();

		//Add inventory field
		Integer cropYear = 2020;
		Integer insurancePlanId = 3;
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();

		// INSERT inventory field
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
		
		invFieldDao.insert(invFieldDto, userId);

		String inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		//INSERT
		InventoryBerriesDao invBerriesDao = persistenceSpringConfig.inventoryBerriesDao();
		InventoryBerriesDto newDto = new InventoryBerriesDto();

		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setCropCommodityId(10);
		newDto.setCropCommodityName("BLUEBERRY");
		newDto.setIsQuantityInsurableInd(true);
		newDto.setIsPlantInsurableInd(false);
		newDto.setIsHarvestedInd(true);
		
		invBerriesDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventoryBerriesGuid());

		
		//SELECT
		List<InventoryBerriesDto> dtos = invBerriesDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals("total berries records", 1, dtos.size());

		//Delete all berries records for inventory field
		invBerriesDao.deleteForInventoryField(inventoryFieldGuid);
		dtos = invBerriesDao.select(inventoryFieldGuid);
		Assert.assertTrue("berries records not deleted", dtos == null || dtos.size() == 0);
		
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

	private void createField() throws DaoException {

		String userId = "JUNIT_TEST";

		FieldDao dao = persistenceSpringConfig.fieldDao();

		FieldDto fieldDto = new FieldDto();
		
		String fieldLabel = "Test Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;


		//INSERT
		fieldDto.setFieldId(fieldId2);
		fieldDto.setFieldLabel(fieldLabel);
		fieldDto.setActiveFromCropYear(activeFromCropYear);
		fieldDto.setActiveToCropYear(activeToCropYear);

		dao.insertDataSync(fieldDto, userId);
		
		//FETCH
		FieldDto fetchedDto = dao.fetch(fieldId2);
		
		Assert.assertNotNull(fetchedDto);
	}
	 
}
