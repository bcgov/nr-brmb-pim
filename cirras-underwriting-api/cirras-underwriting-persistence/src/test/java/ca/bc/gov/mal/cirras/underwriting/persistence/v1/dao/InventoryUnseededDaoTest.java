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
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryUnseededDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryUnseededDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class InventoryUnseededDaoTest {
	
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
			InventoryUnseededDao invUnseededDao = persistenceSpringConfig.inventoryUnseededDao();
			invUnseededDao.deleteForField(fieldId2);
			InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
			invFieldDao.deleteForField(fieldId2);
			dao.delete(fieldId2);
		}
	}

	
	@Test 
	public void testInventoryUnseeded() throws Exception {

		Integer fieldId = 21949;
		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		String inventoryFieldGuid;
		String inventoryUnseededGuid;
		String userId = "UNITTEST";
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventoryUnseededDao invUnseededDao = persistenceSpringConfig.inventoryUnseededDao();

		// INSERT parent InventoryField
		InventoryFieldDto invFieldDto = new InventoryFieldDto();

		invFieldDto.setCropYear(cropYear);
		invFieldDto.setFieldId(fieldId);
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
		InventoryUnseededDto newDto = new InventoryUnseededDto();

		newDto.setAcresToBeSeeded(11.22);
		newDto.setCropCommodityId(16);
		newDto.setCropCommodityName("BARLEY");
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setIsUnseededInsurableInd(true);
		
		invUnseededDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventoryUnseededGuid());
		inventoryUnseededGuid = newDto.getInventoryUnseededGuid();
		
		
		//SELECT
		List<InventoryUnseededDto> dtos = invUnseededDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		InventoryUnseededDto fetchedDto = invUnseededDao.fetch(inventoryUnseededGuid);
		
		Assert.assertEquals("InventoryUnseededGuid", newDto.getInventoryUnseededGuid(), fetchedDto.getInventoryUnseededGuid());
		Assert.assertEquals("AcresToBeSeeded", newDto.getAcresToBeSeeded(), fetchedDto.getAcresToBeSeeded());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("InventoryFieldGuid", newDto.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("IsUnseededInsurableInd", newDto.getIsUnseededInsurableInd(), fetchedDto.getIsUnseededInsurableInd());
		
		//UPDATE
		fetchedDto.setAcresToBeSeeded(33.44);
		fetchedDto.setCropCommodityId(18);
		fetchedDto.setCropCommodityName("CANOLA");
		fetchedDto.setInventoryFieldGuid(inventoryFieldGuid);
		fetchedDto.setIsUnseededInsurableInd(false);

		invUnseededDao.update(fetchedDto, userId);

		//FETCH
		InventoryUnseededDto updatedDto = invUnseededDao.fetch(inventoryUnseededGuid);

		Assert.assertEquals("InventoryUnseededGuid", fetchedDto.getInventoryUnseededGuid(), updatedDto.getInventoryUnseededGuid());
		Assert.assertEquals("AcresToBeSeeded", fetchedDto.getAcresToBeSeeded(), updatedDto.getAcresToBeSeeded());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", fetchedDto.getCropCommodityName(), updatedDto.getCropCommodityName());
		Assert.assertEquals("InventoryFieldGuid", fetchedDto.getInventoryFieldGuid(), updatedDto.getInventoryFieldGuid());
		Assert.assertEquals("IsUnseededInsurableInd", fetchedDto.getIsUnseededInsurableInd(), updatedDto.getIsUnseededInsurableInd());
		
		//DELETE
		invUnseededDao.delete(inventoryUnseededGuid);
		
		//FETCH
		InventoryUnseededDto deletedDto = invUnseededDao.fetch(inventoryUnseededGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = invUnseededDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		//DELETE parent InventoryField
		invFieldDao.delete(inventoryFieldGuid);
		
	}
	
	@Test 
	public void testDeleteForField() throws Exception {

		FieldDao dao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = new FieldDto();
		
		
		String fieldLabel = "Test Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;

		String userId = "JUNIT_TEST";

		//INSERT
		fieldDto.setFieldId(fieldId2);
		fieldDto.setFieldLabel(fieldLabel);
		fieldDto.setActiveFromCropYear(activeFromCropYear);
		fieldDto.setActiveToCropYear(activeToCropYear);

		dao.insertDataSync(fieldDto, userId);
		
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
		invFieldDto.setPlantingNumber(1);

		
		invFieldDao.insert(invFieldDto, userId);

		String inventoryFieldGuid = invFieldDto.getInventoryFieldGuid();

		//INSERT
		InventoryUnseededDao invUnseededDao = persistenceSpringConfig.inventoryUnseededDao();
		InventoryUnseededDto newDto = new InventoryUnseededDto();

		newDto.setAcresToBeSeeded(11.22);
		newDto.setCropCommodityId(16);
		newDto.setCropCommodityName("BARLEY");
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setIsUnseededInsurableInd(true);
		
		invUnseededDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventoryUnseededGuid());

		
		//SELECT
		List<InventoryUnseededDto> dtos = invUnseededDao.select(inventoryFieldGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals("total unseeded records", 1, dtos.size());

		//Delete all unseeded records for field
		invUnseededDao.deleteForField(fieldId2);
		dtos = invUnseededDao.select(inventoryFieldGuid);
		Assert.assertTrue("unseeded records not deleted", dtos == null || dtos.size() == 0);
		
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
	public void testSelectForContractField() throws Exception {
		
		FieldDao dao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = new FieldDto();
		
		
		String fieldLabel = "Test Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;
		
		String userId = "JUNIT_TEST";

		//INSERT
		fieldDto.setFieldId(fieldId2);
		fieldDto.setFieldLabel(fieldLabel);
		fieldDto.setActiveFromCropYear(activeFromCropYear);
		fieldDto.setActiveToCropYear(activeToCropYear);

		dao.insertDataSync(fieldDto, userId);
		
		//FETCH
		FieldDto fetchedDto = dao.fetch(fieldId2);
		
		Assert.assertNotNull(fetchedDto);

		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventoryUnseededDao invUnseededDao = persistenceSpringConfig.inventoryUnseededDao();

		
		// INSERT inventory field and unseeded
		createInventoryFieldAndUnseeded(cropYear, insurancePlanId, 1, 25.0, 16, "BARLEY");
		createInventoryFieldAndUnseeded(cropYear, insurancePlanId, 2, 10.0, 16, "BARLEY");
		createInventoryFieldAndUnseeded(cropYear, insurancePlanId, 3, 17.0, 21, "FIELD PEA");
		createInventoryFieldAndUnseeded(cropYear, insurancePlanId, 4, 9.1, null, null);
		createInventoryFieldAndUnseeded(cropYear, insurancePlanId, 5, null, 24, "OAT"); //Set null but expect 0

		//SELECT
		List<InventoryUnseededDto> iuDtoList = invUnseededDao.selectTotalsForFieldYearPlan(fieldId2, cropYear, insurancePlanId);

		for (InventoryUnseededDto iuDto : iuDtoList) {
			if(iuDto.getCropCommodityId() == null) {
				Assert.assertEquals("total acres wrong (other)", iuDto.getTotalAcresToBeSeeded(), (Double)9.1);
			} else {
				if(iuDto.getCropCommodityId().equals(16)) { //Barley
					Assert.assertEquals("total acres wrong (" + iuDto.getCropCommodityId() + ")", iuDto.getTotalAcresToBeSeeded(), (Double)35.0);
				} else if (iuDto.getCropCommodityId().equals(21)) { //Field Pea
					Assert.assertEquals("total acres wrong (" + iuDto.getCropCommodityId() + ")", iuDto.getTotalAcresToBeSeeded(), (Double)17.0);
				} else if (iuDto.getCropCommodityId().equals(24)) { //Oat
					Assert.assertEquals("total acres wrong (" + iuDto.getCropCommodityId() + ")", iuDto.getTotalAcresToBeSeeded(), (Double)0.0);
				}
			}
			
		}
		
		//CLEAN UP
		//Delete all unseeded records for field
		invUnseededDao.deleteForField(fieldId2);
		
		//Delete all inventory field records for field
		invFieldDao.deleteForField(fieldId2);
		
		//DELETE
		dao.delete(fieldId2);

		//FETCH
		FieldDto deletedDto = dao.fetch(fieldId2);
		Assert.assertNull(deletedDto);
		
	}

	private void createInventoryFieldAndUnseeded(
			Integer cropYear, 
			Integer insurancePlanId, 
			Integer plantingNumber,
			Double acresToBeSeeded,
			Integer cropCommodityId,
			String cropName) throws DaoException {
		
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
		
		//Insert unseeded
		InventoryUnseededDao invUnseededDao = persistenceSpringConfig.inventoryUnseededDao();
		InventoryUnseededDto newDto = new InventoryUnseededDto();

		newDto.setAcresToBeSeeded(acresToBeSeeded);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropCommodityName(cropName);
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setIsUnseededInsurableInd(true);
		
		invUnseededDao.insert(newDto, userId);
				
	}
	 
}
