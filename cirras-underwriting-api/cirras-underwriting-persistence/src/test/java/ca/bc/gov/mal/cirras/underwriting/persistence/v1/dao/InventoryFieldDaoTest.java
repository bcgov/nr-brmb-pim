package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class InventoryFieldDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer fieldId2 = 99999999;
	
	private String userId = "UNITTEST";

	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}

	private void delete() throws NotFoundDaoException, DaoException {
		deleteFieldInvYield(fieldId2);
	}
		
	private void deleteFieldInvYield(Integer fieldId) throws NotFoundDaoException, DaoException{

		FieldDao dao = persistenceSpringConfig.fieldDao();
		FieldDto dto = dao.fetch(fieldId);
		if (dto != null) {

			DeclaredYieldFieldDao dyfDao = persistenceSpringConfig.declaredYieldFieldDao();
			dyfDao.deleteForField(fieldId);
			
			InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
			invSeededGrainDao.deleteForField(fieldId);
			
			InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
			List<InventoryFieldDto> dtos = invFieldDao.selectForField(fieldId);
			//Remove link to seeded forage
			if(dtos != null && dtos.size() > 0) {
				for (InventoryFieldDto invFieldDto : dtos) {
					invFieldDto.setUnderseededInventorySeededForageGuid(null);
					invFieldDao.update(invFieldDto, userId);
				}
			}

			InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
			invSeededForageDao.deleteForField(fieldId);

			invFieldDao.deleteForField(fieldId);
			dao.delete(fieldId);
		}
	}

	@Test 
	public void testInventoryField() throws Exception {

		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		String inventoryFieldGuid;
		
		//INSERT Field
		createField(userId);

		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();

		// INSERT
		InventoryFieldDto newDto = new InventoryFieldDto();

		newDto.setCropYear(cropYear);
		newDto.setFieldId(fieldId2);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setLastYearCropCommodityId(65);
		newDto.setLastYearCropCommodityName("FORAGE");
		newDto.setLastYearCropVarietyId(220);
		newDto.setLastYearCropVarietyName("CLOVER/GRASS");
		newDto.setIsHiddenOnPrintoutInd(false);
		newDto.setUnderseededAcres(14.4);
		newDto.setUnderseededCropVarietyId(1010513);
		newDto.setUnderseededCropVarietyName("BRASETTO");
		newDto.setUnderseededInventorySeededForageGuid(null);
		newDto.setPlantingNumber(1);

		
		invFieldDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventoryFieldGuid());
		inventoryFieldGuid = newDto.getInventoryFieldGuid();
		
		//SELECT
		List<InventoryFieldDto> dtos = invFieldDao.select(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		dtos = invFieldDao.selectForField(fieldId2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		InventoryFieldDto fetchedDto = invFieldDao.fetch(inventoryFieldGuid);
		
		Assert.assertEquals("InventoryFieldGuid", newDto.getInventoryFieldGuid(), fetchedDto.getInventoryFieldGuid());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("FieldId", newDto.getFieldId(), fetchedDto.getFieldId());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("LastYearCropCommodityId", newDto.getLastYearCropCommodityId(), fetchedDto.getLastYearCropCommodityId());
		Assert.assertEquals("LastYearCropCommodityName", newDto.getLastYearCropCommodityName(), fetchedDto.getLastYearCropCommodityName());
		Assert.assertEquals("LastYearCropVarietyId", newDto.getLastYearCropVarietyId(), fetchedDto.getLastYearCropVarietyId());
		Assert.assertEquals("LastYearCropVarietyName", newDto.getLastYearCropVarietyName(), fetchedDto.getLastYearCropVarietyName());
		Assert.assertEquals("IsHiddenOnPrintoutInd", newDto.getIsHiddenOnPrintoutInd(), fetchedDto.getIsHiddenOnPrintoutInd());
		Assert.assertEquals("UnderseededAcres", newDto.getUnderseededAcres(), fetchedDto.getUnderseededAcres());
		Assert.assertEquals("UnderseededCropVarietyId", newDto.getUnderseededCropVarietyId(), fetchedDto.getUnderseededCropVarietyId());
		Assert.assertEquals("UnderseededCropVarietyName", newDto.getUnderseededCropVarietyName(), fetchedDto.getUnderseededCropVarietyName());
		Assert.assertEquals("UnderseededInventorySeededForageGuid", newDto.getUnderseededInventorySeededForageGuid(), fetchedDto.getUnderseededInventorySeededForageGuid());
		Assert.assertEquals("PlantingNumber", newDto.getPlantingNumber(), fetchedDto.getPlantingNumber());
		
		//UPDATE
		fetchedDto.setCropYear(cropYear);
		fetchedDto.setFieldId(fieldId2);
		fetchedDto.setInsurancePlanId(insurancePlanId);
		fetchedDto.setLastYearCropCommodityId(71);
		fetchedDto.setLastYearCropCommodityName("SILAGE CORN");
		fetchedDto.setLastYearCropVarietyId(1010863);
		fetchedDto.setLastYearCropVarietyName("SILAGE CORN - UNSPECIFIED");
		fetchedDto.setIsHiddenOnPrintoutInd(true);
		fetchedDto.setUnderseededAcres(15.5);
		fetchedDto.setUnderseededCropVarietyId(1010430);
		fetchedDto.setUnderseededCropVarietyName("CHAMPION");

		fetchedDto.setPlantingNumber(2);
		
		invFieldDao.update(fetchedDto, userId);

		//FETCH
		InventoryFieldDto updatedDto = invFieldDao.fetch(inventoryFieldGuid);

		Assert.assertEquals("InventoryFieldGuid", fetchedDto.getInventoryFieldGuid(), updatedDto.getInventoryFieldGuid());
		Assert.assertEquals("CropYear", fetchedDto.getCropYear(), updatedDto.getCropYear());
		Assert.assertEquals("FieldId", fetchedDto.getFieldId(), updatedDto.getFieldId());
		Assert.assertEquals("InsurancePlanId", fetchedDto.getInsurancePlanId(), updatedDto.getInsurancePlanId());
		Assert.assertEquals("LastYearCropCommodityId", fetchedDto.getLastYearCropCommodityId(), updatedDto.getLastYearCropCommodityId());
		Assert.assertEquals("LastYearCropCommodityName", fetchedDto.getLastYearCropCommodityName(), updatedDto.getLastYearCropCommodityName());
		Assert.assertEquals("LastYearCropVarietyId", fetchedDto.getLastYearCropVarietyId(), updatedDto.getLastYearCropVarietyId());
		Assert.assertEquals("LastYearCropVarietyName", fetchedDto.getLastYearCropVarietyName(), updatedDto.getLastYearCropVarietyName());
		Assert.assertEquals("IsHiddenOnPrintoutInd", fetchedDto.getIsHiddenOnPrintoutInd(), updatedDto.getIsHiddenOnPrintoutInd());
		Assert.assertEquals("UnderseededAcres", fetchedDto.getUnderseededAcres(), updatedDto.getUnderseededAcres());
		Assert.assertEquals("UnderseededCropVarietyId", fetchedDto.getUnderseededCropVarietyId(), updatedDto.getUnderseededCropVarietyId());
		Assert.assertEquals("UnderseededCropVarietyName", fetchedDto.getUnderseededCropVarietyName(), updatedDto.getUnderseededCropVarietyName());
		Assert.assertNull("UnderseededInventorySeededForageGuid", updatedDto.getUnderseededInventorySeededForageGuid());
		Assert.assertEquals("PlantingNumber", fetchedDto.getPlantingNumber(), updatedDto.getPlantingNumber());
		
		//Add a forage seeded record for the first planting
		String inventorySeededForageGuid = createSeededForage(inventoryFieldGuid, userId);
		
		//INSERT second field
		InventoryFieldDto newDto2 = new InventoryFieldDto();

		newDto2.setCropYear(cropYear);
		newDto2.setFieldId(fieldId2);
		newDto2.setInsurancePlanId(insurancePlanId);
		newDto2.setLastYearCropCommodityId(20);
		newDto2.setLastYearCropCommodityName("FALL RYE");
		newDto2.setLastYearCropVarietyId(null);
		newDto2.setLastYearCropVarietyName(null);
		newDto2.setIsHiddenOnPrintoutInd(false);
		newDto2.setUnderseededAcres(15.5);
		newDto2.setUnderseededCropVarietyId(1010430);
		newDto2.setUnderseededCropVarietyName("CHAMPION");
		newDto2.setUnderseededInventorySeededForageGuid(inventorySeededForageGuid);
		newDto2.setPlantingNumber(1);

		invFieldDao.insert(newDto2, userId);
		
		String inventoryFieldGuid2 = newDto2.getInventoryFieldGuid();
		
		//inventorySeededForageGuid needs a seeded forage record and is tested in a separate method 
		testUnderseededLink(newDto2, inventoryFieldGuid, inventoryFieldGuid2, inventorySeededForageGuid, cropYear, insurancePlanId, userId);
		
		//SELECT
		dtos = invFieldDao.select(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		dtos = invFieldDao.selectForField(fieldId2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		//Delete seeded forage
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		invSeededForageDao.deleteForField(fieldId2);

		invFieldDao.delete(inventoryFieldGuid2);
		invFieldDao.delete(inventoryFieldGuid);
		
		//FETCH
		InventoryFieldDto deletedDto = invFieldDao.fetch(inventoryFieldGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = invFieldDao.select(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		dtos = invFieldDao.selectForField(fieldId2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
	}
	
	private void testUnderseededLink(InventoryFieldDto invFieldDto, 
			String inventoryFieldGuid1, 
			String inventoryFieldGuid2, 
			String inventorySeededForageGuid, 
			Integer cropYear,
			Integer insurancePlanId,
			String userId) throws DaoException {
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();
		InventorySeededForageDao inventorySeededForageDao = persistenceSpringConfig.inventorySeededForageDao();

		//Test if the linked information appears on the inventoryField (for grain) and inventorySeededGrain (for Forage)
		//On GRAIN
		List<InventoryFieldDto> dtos = invFieldDao.select(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		
		dtos = dtos.stream()
		.filter(x -> x.getInventoryFieldGuid().contentEquals(inventoryFieldGuid2))
		.collect(Collectors.toList());

		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		InventoryFieldDto selectedInvField = dtos.get(0);
		
		Assert.assertEquals("UnderseededInventorySeededForageGuid 2", inventorySeededForageGuid, selectedInvField.getUnderseededInventorySeededForageGuid());
		Assert.assertEquals("LinkedCropVarietyId", forageVarietyId, selectedInvField.getLinkedCropVarietyId());
		Assert.assertEquals("LinkedVarietyName", forageVarietyName, selectedInvField.getLinkedVarietyName());
		Assert.assertEquals("LinkedFieldAcres", forageAcres, selectedInvField.getLinkedFieldAcres());
		
		//On Forage
		List<InventorySeededForageDto> forageDtos = inventorySeededForageDao.select(inventoryFieldGuid1);
		Assert.assertNotNull(forageDtos);
		Assert.assertEquals(1, forageDtos.size());
		
		InventorySeededForageDto forageDto = forageDtos.get(0);
		Assert.assertEquals("LinkedInventoryFieldGuid", invFieldDto.getInventoryFieldGuid(), forageDto.getLinkedInventoryFieldGuid());
		Assert.assertEquals("LinkedUnderseededCropVarietyId", invFieldDto.getUnderseededCropVarietyId(), forageDto.getLinkedUnderseededCropVarietyId());
		Assert.assertEquals("LinkedVarietyName", invFieldDto.getUnderseededCropVarietyName(), forageDto.getLinkedVarietyName());
		Assert.assertEquals("LinkedUnderseededAcres", invFieldDto.getUnderseededAcres(), forageDto.getLinkedUnderseededAcres());

		//Test select by seeded forage guid
		InventoryFieldDto fetchedDto = invFieldDao.selectLinkedGrainPlanting(inventorySeededForageGuid);
		
		Assert.assertNotNull(fetchedDto);
		Assert.assertEquals("UnderseededInventorySeededForageGuid 2", inventorySeededForageGuid, fetchedDto.getUnderseededInventorySeededForageGuid());
		Assert.assertNotNull(fetchedDto.getInventoryFieldGuid());

		//Get second planting
		fetchedDto = invFieldDao.fetch(inventoryFieldGuid2);

		//Test insert
		Assert.assertEquals("UnderseededInventorySeededForageGuid", inventorySeededForageGuid, fetchedDto.getUnderseededInventorySeededForageGuid());

		//set it to null
		fetchedDto.setUnderseededInventorySeededForageGuid(null);
		
		//update
		invFieldDao.update(fetchedDto, userId);
		
		//fetch again
		InventoryFieldDto updatedDto = invFieldDao.fetch(inventoryFieldGuid2);

		//Test update to NULL
		Assert.assertNull("UnderseededInventorySeededForageGuid 3", updatedDto.getUnderseededInventorySeededForageGuid());

		//remove link
		updatedDto.setUnderseededInventorySeededForageGuid(null);
		invFieldDao.update(updatedDto, userId);

	}

	
	@Test 
	public void testRemovePlantingLinkForFieldAndYear() throws Exception {

		Integer cropYear1 = 2020;
		Integer cropYear2 = 2021;
		Integer cropYear3 = 2022;

		//INSERT Field
		createField(userId);

		String forageInventoryFieldGuid1 = createInventoryField(cropYear1, fieldId2, 5, null, null, null, null, 1, userId);
		String inventorySeededForageGuid1 = createSeededForage(forageInventoryFieldGuid1, userId);
		String grainInventoryFieldGuid1 = createInventoryField(cropYear1, fieldId2, 4, 15.5, 1010430, "CHAMPION", inventorySeededForageGuid1, 1, userId);

		String forageInventoryFieldGuid2 = createInventoryField(cropYear1, fieldId2, 5, null, null, null, null, 2, userId);
		String inventorySeededForageGuid2 = createSeededForage(forageInventoryFieldGuid2, userId);
		String grainInventoryFieldGuid2 = createInventoryField(cropYear1, fieldId2, 4, 15.5, 1010430, "CHAMPION", inventorySeededForageGuid2, 2, userId);
		
		String forageInventoryFieldGuid3 = createInventoryField(cropYear2, fieldId2, 5, null, null, null, null, 2, userId);
		String inventorySeededForageGuid3 = createSeededForage(forageInventoryFieldGuid3, userId);
		String grainInventoryFieldGuid3 = createInventoryField(cropYear2, fieldId2, 4, 15.5, 1010430, "CHAMPION", inventorySeededForageGuid3, 1, userId);
		
		String forageInventoryFieldGuid4 = createInventoryField(cropYear3, fieldId2, 5, null, null, null, null, 2, userId);
		String inventorySeededForageGuid4 = createSeededForage(forageInventoryFieldGuid4, userId);
		String grainInventoryFieldGuid4 = createInventoryField(cropYear3, fieldId2, 4, 15.5, 1010430, "CHAMPION", inventorySeededForageGuid4, 1, userId);
				
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();

		//FETCH all
		InventoryFieldDto fetchedDto = invFieldDao.fetch(grainInventoryFieldGuid1);
		Assert.assertEquals(inventorySeededForageGuid1, fetchedDto.getUnderseededInventorySeededForageGuid());

		fetchedDto = invFieldDao.fetch(grainInventoryFieldGuid2);
		Assert.assertEquals(inventorySeededForageGuid2, fetchedDto.getUnderseededInventorySeededForageGuid());

		fetchedDto = invFieldDao.fetch(grainInventoryFieldGuid3);
		Assert.assertEquals(inventorySeededForageGuid3, fetchedDto.getUnderseededInventorySeededForageGuid());

		fetchedDto = invFieldDao.fetch(grainInventoryFieldGuid4);
		Assert.assertEquals(inventorySeededForageGuid4, fetchedDto.getUnderseededInventorySeededForageGuid());

		//********************************************
		// Test: Remove all planting links for field and YEAR
		invFieldDao.removeLinkToPlantingForFieldAndYear(fieldId2, cropYear2, userId);

		//FETCH all
		fetchedDto = invFieldDao.fetch(grainInventoryFieldGuid1);
		Assert.assertEquals(inventorySeededForageGuid1, fetchedDto.getUnderseededInventorySeededForageGuid());

		fetchedDto = invFieldDao.fetch(grainInventoryFieldGuid2);
		Assert.assertEquals(inventorySeededForageGuid2, fetchedDto.getUnderseededInventorySeededForageGuid());

		//Expect to be the only one with the link removed
		fetchedDto = invFieldDao.fetch(grainInventoryFieldGuid3);
		Assert.assertNull(fetchedDto.getUnderseededInventorySeededForageGuid());

		fetchedDto = invFieldDao.fetch(grainInventoryFieldGuid4);
		Assert.assertEquals(inventorySeededForageGuid4, fetchedDto.getUnderseededInventorySeededForageGuid());
		
		//********************************************
		// Test: Remove all planting links for field.
		invFieldDao.removeLinkToPlantingForField(fieldId2, userId);

		//FETCH
		fetchedDto = invFieldDao.fetch(grainInventoryFieldGuid1);
		Assert.assertNull(fetchedDto.getUnderseededInventorySeededForageGuid());

		fetchedDto = invFieldDao.fetch(grainInventoryFieldGuid2);
		Assert.assertNull(fetchedDto.getUnderseededInventorySeededForageGuid());

		fetchedDto = invFieldDao.fetch(grainInventoryFieldGuid4);
		Assert.assertNull(fetchedDto.getUnderseededInventorySeededForageGuid());

		//Delete seeded forage
		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		invSeededForageDao.deleteForField(fieldId2);

		invFieldDao.deleteForField(fieldId2);
		
		List<InventoryFieldDto> dtos = invFieldDao.selectForField(fieldId2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
	}
	
	
	@Test 
	public void testDeleteForField() throws Exception {

		FieldDao dao = persistenceSpringConfig.fieldDao();
		FieldDto newDto = new FieldDto();
		
		
		String fieldLabel = "Test Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setFieldId(fieldId2);
		newDto.setFieldLabel(fieldLabel);
		newDto.setActiveFromCropYear(activeFromCropYear);
		newDto.setActiveToCropYear(activeToCropYear);

		dao.insertDataSync(newDto, userId);
		
		//FETCH
		FieldDto fetchedDto = dao.fetch(fieldId2);
		
		Assert.assertNotNull(fetchedDto);

		//Add inventory field
		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();

		// INSERT
		InventoryFieldDto newinvDto = new InventoryFieldDto();

		newinvDto.setCropYear(cropYear);
		newinvDto.setFieldId(fieldId2);
		newinvDto.setInsurancePlanId(insurancePlanId);
		newinvDto.setLastYearCropCommodityId(20);
		newinvDto.setLastYearCropCommodityName("FALL RYE");
		newinvDto.setIsHiddenOnPrintoutInd(false);
		newinvDto.setPlantingNumber(1);

		
		invFieldDao.insert(newinvDto, userId);

		//Add second record
		newinvDto.setPlantingNumber(2);
		invFieldDao.insert(newinvDto, userId);

		
		//SELECT
		List<InventoryFieldDto> dtos = invFieldDao.select(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals("total inventory field records", 2, dtos.size());

		//Delete all inventory field records for field
		invFieldDao.deleteForField(fieldId2);
		dtos = invFieldDao.select(fieldId2, cropYear, insurancePlanId);
		Assert.assertTrue("inventory field records not deleted", dtos == null || dtos.size() == 0);
		
		//DELETE
		dao.delete(fieldId2);

		//FETCH
		FieldDto deletedDto = dao.fetch(fieldId2);
		Assert.assertNull(deletedDto);

	}

	@Test 
	public void testSelectForRolloverGrain() throws Exception {

		FieldDao dao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = new FieldDto();
		
		
		String fieldLabel = "Test Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;

		String userId = "JUNIT_TEST";

		//INSERT Field
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
		invFieldDto.setIsHiddenOnPrintoutInd(true);
		invFieldDto.setUnderseededAcres(14.4);
		invFieldDto.setUnderseededCropVarietyId(119);
		invFieldDto.setUnderseededCropVarietyName("ALFALFA");
		invFieldDto.setPlantingNumber(1);

		
		invFieldDao.insert(invFieldDto, userId);

		String inventoryFieldGuid1 = invFieldDto.getInventoryFieldGuid();
		
		//Add second record
		InventoryFieldDto invField2Dto = new InventoryFieldDto();

		invField2Dto.setCropYear(cropYear);
		invField2Dto.setFieldId(fieldId2);
		invField2Dto.setInsurancePlanId(insurancePlanId);
		invField2Dto.setLastYearCropCommodityId(18);
		invField2Dto.setLastYearCropCommodityName("CANOLA");
		invField2Dto.setIsHiddenOnPrintoutInd(true);
		invField2Dto.setPlantingNumber(2);
		invFieldDao.insert(invField2Dto, userId);

		String inventoryFieldGuid2 = invField2Dto.getInventoryFieldGuid();
		
		
		//INSERT
		//Add seeded grain to inv field 1
		createSeededGrain("Forage Oat", 24, 1010570, inventoryFieldGuid1, false, 0.0, userId); //Oat
		createSeededGrain("CPSW", 26, 1010602, inventoryFieldGuid1, true, 30.0, userId); 	//Wheat
		createSeededGrain("Forage Seed", 1010893, 1010999, inventoryFieldGuid1, true, 28.0, userId); 	//BROME (Forage Commodity)

		//Add seeded grain to inv field 2
		createSeededGrain(null, null, null, inventoryFieldGuid2, false, 20.0, userId);  //No Commodity
		createSeededGrain("Forage Oat", 24, 1010570, inventoryFieldGuid2, true, 15.0, userId);  //Oat
		createSeededGrain("Argentine Canola", 18, 1010471, inventoryFieldGuid2, true, 10.0, userId); //Canola
//		createSeededGrain("Argentine Canola", 18, 1010471, inventoryFieldGuid2, true, 10.0, userId); //Canola

		//SELECT For Rollover
		List<InventoryFieldDto> dtos = invFieldDao.selectForRollover(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		
		//Expected
		//Four records
		//Oat: 15 acres
		//Canola: 10 acres
		//No Commodity: 20 acres
		//Alfalfa: 14.4 acres because it's underseeded
		Assert.assertEquals("total records", 4, dtos.size());

		Integer plantingNumber = 0;
		for (InventoryFieldDto ifDto : dtos) {
			plantingNumber++;
			Assert.assertEquals(fieldId2, ifDto.getFieldId());
			Assert.assertEquals(plantingNumber, ifDto.getPlantingNumber());
			Assert.assertTrue(ifDto.getIsHiddenOnPrintoutInd());
			Integer lastYearCmdtyId = ifDto.getLastYearCropCommodityId();
			//Check if commodity is oat or canola
			Assert.assertTrue("commodity id not 24 or 18 or 65 or none", lastYearCmdtyId == null || lastYearCmdtyId.equals(24) || lastYearCmdtyId.equals(18) || lastYearCmdtyId.equals(65));
			//Check acres according to commodity
			if(lastYearCmdtyId == null) {
				//No Commodity
				Assert.assertNull(ifDto.getLastYearCropCommodityName());
				Assert.assertNull(ifDto.getLastYearCropVarietyName());
				Assert.assertNull(ifDto.getLastYearCropVarietyId());
				Assert.assertEquals("No Commodity not correct acres", (Double)20.0, ifDto.getAcresToBeSeeded());
				Assert.assertNull("IsGrainUnseededDefaultInd", ifDto.getIsGrainUnseededDefaultInd());
			} else if (lastYearCmdtyId.equals(24)) {
				//Oat
				Assert.assertEquals("OAT", ifDto.getLastYearCropCommodityName());
				Assert.assertNull(ifDto.getLastYearCropVarietyName());
				Assert.assertNull(ifDto.getLastYearCropVarietyId());
				Assert.assertEquals("Oat not correct acres", (Double)15.0, ifDto.getAcresToBeSeeded());
				Assert.assertNull("IsGrainUnseededDefaultInd", ifDto.getIsGrainUnseededDefaultInd());
			} else if (lastYearCmdtyId.equals(18)) {
				//Canola
				Assert.assertEquals("CANOLA", ifDto.getLastYearCropCommodityName());
				Assert.assertNull(ifDto.getLastYearCropVarietyName());
				Assert.assertNull(ifDto.getLastYearCropVarietyId());
				Assert.assertEquals("Canola not correct acres", (Double)10.0, ifDto.getAcresToBeSeeded());
				Assert.assertNull("IsGrainUnseededDefaultInd", ifDto.getIsGrainUnseededDefaultInd());
			} else if (lastYearCmdtyId.equals(65)) {
				//Forage - From underseeded Alfalfa
				Assert.assertEquals("ALFALFA", ifDto.getLastYearCropVarietyName());
				Assert.assertEquals(119, ifDto.getLastYearCropVarietyId().intValue());
				Assert.assertEquals("FORAGE", ifDto.getLastYearCropCommodityName());
				Assert.assertEquals("FORAGE not correct acres", (Double)14.4, ifDto.getAcresToBeSeeded());
				Assert.assertFalse("IsGrainUnseededDefaultInd", ifDto.getIsGrainUnseededDefaultInd());
			}
		}

		//Add third record with no seeded data.
		InventoryFieldDto invField3Dto = new InventoryFieldDto();

		invField3Dto.setCropYear(cropYear);
		invField3Dto.setFieldId(fieldId2);
		invField3Dto.setInsurancePlanId(insurancePlanId);
		invField3Dto.setLastYearCropCommodityId(null);
		invField3Dto.setLastYearCropCommodityName(null);
		invField3Dto.setIsHiddenOnPrintoutInd(false);  // Because this is false, it will be false for the rolled-over plantings.
		invField3Dto.setPlantingNumber(3);
		invFieldDao.insert(invField3Dto, userId);

		String inventoryFieldGuid3 = invField3Dto.getInventoryFieldGuid();

		//SELECT For Rollover
		dtos = invFieldDao.selectForRollover(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		
		//Expected
		//Four records
		//Oat: 15 acres
		//Canola: 10 acres
		//No Commodity: 20 acres
		//Alfalfa: 14.4 acres because it's underseeded		
		Assert.assertEquals("total records", 4, dtos.size());

		plantingNumber = 0;
		for (InventoryFieldDto ifDto : dtos) {
			plantingNumber++;
			Assert.assertEquals(fieldId2, ifDto.getFieldId());
			Assert.assertEquals(plantingNumber, ifDto.getPlantingNumber());
			Assert.assertFalse(ifDto.getIsHiddenOnPrintoutInd());   // Rolled-over as false because of the third planting.
			Integer lastYearCmdtyId = ifDto.getLastYearCropCommodityId();
			//Check if commodity is oat or canola
			Assert.assertTrue("commodity id not 24 or 18 or 65 or none", lastYearCmdtyId == null || lastYearCmdtyId.equals(24) || lastYearCmdtyId.equals(18) || lastYearCmdtyId.equals(65));
			//Check acres according to commodity
			if(lastYearCmdtyId == null) {
				//No Commodity
				Assert.assertNull(ifDto.getLastYearCropCommodityName());
				Assert.assertEquals("No Commodity not correct acres", (Double)20.0, ifDto.getAcresToBeSeeded());
			} else if (lastYearCmdtyId.equals(24)) {
				//Oat
				Assert.assertEquals("OAT", ifDto.getLastYearCropCommodityName());
				Assert.assertEquals("Oat not correct acres", (Double)15.0, ifDto.getAcresToBeSeeded());
			} else if (lastYearCmdtyId.equals(18)) {
				//Canola
				Assert.assertEquals("CANOLA", ifDto.getLastYearCropCommodityName());
				Assert.assertEquals("Canola not correct acres", (Double)10.0, ifDto.getAcresToBeSeeded());
			} else if (lastYearCmdtyId.equals(65)) {
				//Forage - From underseeded Alfalfa
				Assert.assertEquals("ALFALFA", ifDto.getLastYearCropVarietyName());
				Assert.assertEquals(119, ifDto.getLastYearCropVarietyId().intValue());
				Assert.assertEquals("FORAGE", ifDto.getLastYearCropCommodityName());
				Assert.assertEquals("FORAGE not correct acres", (Double)14.4, ifDto.getAcresToBeSeeded());
			}
		}
		
		//Delete all unseeded records for field
		InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
		invSeededGrainDao.deleteForField(fieldId2);
		List<InventorySeededGrainDto> seededGrainDtos = invSeededGrainDao.select(inventoryFieldGuid1);
		Assert.assertTrue("seeded records not deleted", seededGrainDtos == null || seededGrainDtos.size() == 0);

		//Remove underseeded information
		InventoryFieldDto ifUpdDto = invFieldDao.fetch(inventoryFieldGuid1);
		ifUpdDto.setUnderseededAcres(null);
		ifUpdDto.setUnderseededCropVarietyId(null);
		invFieldDao.update(ifUpdDto, userId);
		
		//Rollover when there is no seeded data.
		dtos = invFieldDao.selectForRollover(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		
		//Expected
		//1 record
		Assert.assertEquals("total records", 1, dtos.size());
		InventoryFieldDto ifDto = dtos.get(0);
		
		Assert.assertEquals(fieldId2, ifDto.getFieldId());
		Assert.assertEquals(1, ifDto.getPlantingNumber().intValue());
		Assert.assertFalse(ifDto.getIsHiddenOnPrintoutInd());   // Rolled-over as false because of the third planting.
		Assert.assertNull(ifDto.getLastYearCropCommodityId());
		Assert.assertNull(ifDto.getLastYearCropCommodityName());
		Assert.assertNull(ifDto.getAcresToBeSeeded());
		
		// Delete third planting.
		invFieldDao.delete(inventoryFieldGuid3);

		//Rollover when there is no seeded data.
		dtos = invFieldDao.selectForRollover(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		
		//Expected
		//1 record
		Assert.assertEquals("total records", 1, dtos.size());
		ifDto = dtos.get(0);
		
		Assert.assertEquals(fieldId2, ifDto.getFieldId());
		Assert.assertEquals(1, ifDto.getPlantingNumber().intValue());
		Assert.assertTrue(ifDto.getIsHiddenOnPrintoutInd());   // Rolled-over as true because it is true for all plantings.
		Assert.assertNull(ifDto.getLastYearCropCommodityId());
		Assert.assertNull(ifDto.getLastYearCropCommodityName());
		Assert.assertNull(ifDto.getAcresToBeSeeded());
		
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
	public void testSelectForRolloverForage() throws Exception {

		Integer cropYear = 2020;
		Integer insurancePlanId = 5;
		
		//INSERT Field
		createField(userId);

		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();

		// INSERT
		InventoryFieldDto newDto = new InventoryFieldDto();

		newDto.setCropYear(cropYear);
		newDto.setFieldId(fieldId2);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setLastYearCropCommodityId(null);
		newDto.setLastYearCropCommodityName(null);
		newDto.setLastYearCropVarietyId(null);
		newDto.setLastYearCropVarietyName(null);
		newDto.setIsHiddenOnPrintoutInd(false);
		newDto.setUnderseededAcres(null);
		newDto.setUnderseededCropVarietyId(null);
		newDto.setUnderseededCropVarietyName(null);
		newDto.setUnderseededInventorySeededForageGuid(null);
		newDto.setPlantingNumber(1);

		
		invFieldDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventoryFieldGuid());

		// INSERT
		InventoryFieldDto newDto2 = new InventoryFieldDto();

		newDto2.setCropYear(cropYear);
		newDto2.setFieldId(fieldId2);
		newDto2.setInsurancePlanId(insurancePlanId);
		newDto2.setLastYearCropCommodityId(null);
		newDto2.setLastYearCropCommodityName(null);
		newDto2.setLastYearCropVarietyId(null);
		newDto2.setLastYearCropVarietyName(null);
		newDto2.setIsHiddenOnPrintoutInd(true);
		newDto2.setUnderseededAcres(null);
		newDto2.setUnderseededCropVarietyId(null);
		newDto2.setUnderseededCropVarietyName(null);
		newDto2.setUnderseededInventorySeededForageGuid(null);
		newDto2.setPlantingNumber(2);
		
		invFieldDao.insert(newDto2, userId);
		Assert.assertNotNull(newDto2.getInventoryFieldGuid());
		
		
		//Select for rollover
		List<InventoryFieldDto> dtos = invFieldDao.selectForRollover(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		Assert.assertEquals("FieldId", newDto.getFieldId(), dtos.get(0).getFieldId());
		Assert.assertEquals("PlantingNumber", newDto.getPlantingNumber(), dtos.get(0).getPlantingNumber());
		Assert.assertNull("LastYearCropCommodityId", dtos.get(0).getLastYearCropCommodityId());
		Assert.assertNull("LastYearCropCommodityName", dtos.get(0).getLastYearCropCommodityName());
		Assert.assertNull("AcresToBeSeeded", dtos.get(0).getAcresToBeSeeded());
		Assert.assertNull("LastYearCropVarietyId", dtos.get(0).getLastYearCropVarietyId());
		Assert.assertNull("LastYearCropVarietyName", dtos.get(0).getLastYearCropVarietyName());
		Assert.assertNull("IsGrainUnseededDefaultInd", dtos.get(0).getIsGrainUnseededDefaultInd());
		Assert.assertEquals("IsHiddenOnPrintoutInd", newDto.getIsHiddenOnPrintoutInd(), dtos.get(0).getIsHiddenOnPrintoutInd());

		Assert.assertEquals("FieldId", newDto2.getFieldId(), dtos.get(1).getFieldId());
		Assert.assertEquals("PlantingNumber", newDto2.getPlantingNumber(), dtos.get(1).getPlantingNumber());
		Assert.assertNull("LastYearCropCommodityId", dtos.get(1).getLastYearCropCommodityId());
		Assert.assertNull("LastYearCropCommodityName", dtos.get(1).getLastYearCropCommodityName());
		Assert.assertNull("AcresToBeSeeded", dtos.get(1).getAcresToBeSeeded());
		Assert.assertNull("LastYearCropVarietyId", dtos.get(1).getLastYearCropVarietyId());
		Assert.assertNull("LastYearCropVarietyName", dtos.get(1).getLastYearCropVarietyName());
		Assert.assertNull("IsGrainUnseededDefaultInd", dtos.get(1).getIsGrainUnseededDefaultInd());
		Assert.assertEquals("IsHiddenOnPrintoutInd", newDto2.getIsHiddenOnPrintoutInd(), dtos.get(1).getIsHiddenOnPrintoutInd());
						
		invFieldDao.delete(newDto.getInventoryFieldGuid());
		invFieldDao.delete(newDto2.getInventoryFieldGuid());
		
		//SELECT
		dtos = invFieldDao.selectForRollover(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
	}
	
	@Test 
	public void testSelectForDeclaredYield() throws Exception {

		String userId = "JUNIT_TEST";

		//INSERT Field
		createField(userId);
		
		//Add inventory field
		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();

		// INSERT inventory field
		InventoryFieldDto invFieldDto = new InventoryFieldDto();

		invFieldDto.setCropYear(cropYear);
		invFieldDto.setFieldId(fieldId2);
		invFieldDto.setInsurancePlanId(insurancePlanId);
		invFieldDto.setLastYearCropCommodityId(65);
		invFieldDto.setLastYearCropCommodityName("FORAGE");
		invFieldDto.setLastYearCropVarietyId(220);
		invFieldDto.setLastYearCropVarietyName("CLOVER/GRASS");
		invFieldDto.setIsHiddenOnPrintoutInd(false);
		invFieldDto.setUnderseededAcres(14.4);
		invFieldDto.setUnderseededCropVarietyId(1010513);
		invFieldDto.setUnderseededCropVarietyName("BRASETTO");
		invFieldDto.setPlantingNumber(1);

		
		invFieldDao.insert(invFieldDto, userId);

		String inventoryFieldGuid1 = invFieldDto.getInventoryFieldGuid();
		
		//Add second record
		InventoryFieldDto invField2Dto = new InventoryFieldDto();

		invField2Dto.setCropYear(cropYear);
		invField2Dto.setFieldId(fieldId2);
		invField2Dto.setInsurancePlanId(insurancePlanId);
		invField2Dto.setLastYearCropCommodityId(18);
		invField2Dto.setLastYearCropCommodityName("CANOLA");
		invField2Dto.setLastYearCropVarietyId(null);
		invField2Dto.setLastYearCropVarietyName(null);
		invField2Dto.setIsHiddenOnPrintoutInd(false);
		invField2Dto.setPlantingNumber(2);
		invFieldDao.insert(invField2Dto, userId);

		String inventoryFieldGuid2 = invField2Dto.getInventoryFieldGuid();
		
		//SELECT
		List<InventoryFieldDto> dtos = invFieldDao.selectForDeclaredYield(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size()); // Empty, because they have neither seeded inventory nor declared yield fields.
		
		//INSERT
		//Add seeded grain to inv field 1
		createSeededGrain(null, 24, null, inventoryFieldGuid1, false, 30.0, userId);

		//Add seeded grain to inv field 2, but with 0 seeded acres.
		createSeededGrain(null, 24, null, inventoryFieldGuid2, false, 0.0, userId);

		//SELECT
		dtos = invFieldDao.selectForDeclaredYield(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size()); // Only first planting returned.
		
		InventoryFieldDto ifYieldDto = dtos.get(0);
		Assert.assertEquals("InventoryFieldGuid", invFieldDto.getInventoryFieldGuid(), ifYieldDto.getInventoryFieldGuid());
		Assert.assertEquals("CropYear", invFieldDto.getCropYear(), ifYieldDto.getCropYear());
		Assert.assertEquals("FieldId", invFieldDto.getFieldId(), ifYieldDto.getFieldId());
		Assert.assertEquals("InsurancePlanId", invFieldDto.getInsurancePlanId(), ifYieldDto.getInsurancePlanId());
		Assert.assertEquals("LastYearCropCommodityId", invFieldDto.getLastYearCropCommodityId(), ifYieldDto.getLastYearCropCommodityId());
		Assert.assertEquals("LastYearCropCommodityName", invFieldDto.getLastYearCropCommodityName(), ifYieldDto.getLastYearCropCommodityName());
		Assert.assertEquals("LastYearCropVarietyId", invFieldDto.getLastYearCropVarietyId(), ifYieldDto.getLastYearCropVarietyId());
		Assert.assertEquals("LastYearCropVarietyName", invFieldDto.getLastYearCropVarietyName(), ifYieldDto.getLastYearCropVarietyName());
		Assert.assertEquals("IsHiddenOnPrintoutInd", invFieldDto.getIsHiddenOnPrintoutInd(), ifYieldDto.getIsHiddenOnPrintoutInd());
		Assert.assertEquals("UnderseededAcres", invFieldDto.getUnderseededAcres(), ifYieldDto.getUnderseededAcres());
		Assert.assertEquals("UnderseededCropVarietyId", invFieldDto.getUnderseededCropVarietyId(), ifYieldDto.getUnderseededCropVarietyId());
		Assert.assertEquals("UnderseededCropVarietyName", invFieldDto.getUnderseededCropVarietyName(), ifYieldDto.getUnderseededCropVarietyName());
		Assert.assertEquals("PlantingNumber", invFieldDto.getPlantingNumber(), ifYieldDto.getPlantingNumber());
		
		DeclaredYieldFieldDao dyfDao = persistenceSpringConfig.declaredYieldFieldDao();
		DeclaredYieldFieldDto dyfDto = new DeclaredYieldFieldDto();
		
		// Add declared yield field to second planting.
		dyfDto.setEstimatedYieldPerAcre(12.34);
		dyfDto.setEstimatedYieldPerAcreDefaultUnit(56.78);
		dyfDto.setInventoryFieldGuid(inventoryFieldGuid2);
		dyfDto.setUnharvestedAcresInd(false);
		
		dyfDao.insert(dyfDto, userId);

		//SELECT
		dtos = invFieldDao.selectForDeclaredYield(fieldId2, cropYear, insurancePlanId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size()); // Both plantings returned.

		// Delete declared yield field.
		dyfDao.delete(dyfDto.getDeclaredYieldFieldGuid());
		
		//Delete all unseeded records for field
		InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
		invSeededGrainDao.deleteForField(fieldId2);
				
		//Delete all inventory field records for field
		invFieldDao.deleteForField(fieldId2);
		List<InventoryFieldDto> invFieldDtos = invFieldDao.select(fieldId2, cropYear, insurancePlanId);
		Assert.assertTrue("inventory field records not deleted", invFieldDtos == null || invFieldDtos.size() == 0);
		
		//DELETE
		FieldDao fldDao = persistenceSpringConfig.fieldDao();
		fldDao.delete(fieldId2);

		//FETCH
		FieldDto deletedDto = fldDao.fetch(fieldId2);
		Assert.assertNull(deletedDto);
	}
	
	private String createInventoryField(
			Integer cropYear,
			Integer fieldId,
			Integer insurancePlanId,
			Double underseededAcres,
			Integer underseededCropVarietyId,
			String underseededCropVarietyName,
			String underseededInventorySeededForageGuid,
			Integer plantingNumber,
			String userId
		) throws DaoException {
		
		InventoryFieldDao invFieldDao = persistenceSpringConfig.inventoryFieldDao();

		// INSERT
		InventoryFieldDto newDto = new InventoryFieldDto();

		newDto.setCropYear(cropYear);
		newDto.setFieldId(fieldId);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setLastYearCropCommodityId(null);
		newDto.setLastYearCropCommodityName(null);
		newDto.setLastYearCropVarietyId(null);
		newDto.setLastYearCropVarietyName(null);
		newDto.setIsHiddenOnPrintoutInd(false);
		newDto.setUnderseededAcres(underseededAcres);
		newDto.setUnderseededCropVarietyId(underseededCropVarietyId);
		newDto.setUnderseededCropVarietyName(underseededCropVarietyName);
		newDto.setUnderseededInventorySeededForageGuid(underseededInventorySeededForageGuid);
		newDto.setPlantingNumber(plantingNumber);
		
		invFieldDao.insert(newDto, userId);
		return newDto.getInventoryFieldGuid();

	}

	
	private void createSeededGrain(
			String commodityTypeCode, 
			Integer cropCommodityId, 
			Integer cropVarietyId, 
			String inventoryFieldGuid, 
			Boolean isReplacedInd, 
			Double seededAcres, 
			String userId
		) throws DaoException {
		
		InventorySeededGrainDao invSeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
		InventorySeededGrainDto newDto = new InventorySeededGrainDto();

		newDto.setCommodityTypeCode(commodityTypeCode);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropVarietyId(cropVarietyId);
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setIsPedigreeInd(true);
		newDto.setIsSpotLossInsurableInd(false);
		newDto.setIsQuantityInsurableInd(true);
		newDto.setIsReplacedInd(isReplacedInd);
		newDto.setSeededAcres(seededAcres);
		newDto.setSeededDate(null);
		
		invSeededGrainDao.insert(newDto, userId);

	}
	
	private Integer forageVarietyId = 1010602;
	private String forageVarietyName = "AAC ENTICE";
	private Double forageAcres = 10.4;
	
	private String createSeededForage(
			String inventoryFieldGuid,
			String userId 
			) throws DaoException {

		InventorySeededForageDao invSeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		InventorySeededForageDto newDto = new InventorySeededForageDto();

		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setCommodityTypeCode("CPSW");
		newDto.setCropCommodityId(26);
		newDto.setCropVarietyId(forageVarietyId);
		newDto.setCropVarietyName(forageVarietyName);
		newDto.setFieldAcres(forageAcres);
		newDto.setSeedingYear(2020);		
		newDto.setSeedingDate(null);		
		newDto.setIsIrrigatedInd(true);
		newDto.setIsQuantityInsurableInd(false);
		newDto.setPlantInsurabilityTypeCode("E1");
		newDto.setIsAwpEligibleInd(true);
		
		invSeededForageDao.insert(newDto, userId);
		
		return newDto.getInventorySeededForageGuid();

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
