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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.SeedingDeadlineDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class CommodityTypeCodeDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String commodityTypeCode = "TESTCODE";
	private Integer cropCommodityId1 = 88888888;
	private Integer cropCommodityId2 = 99999999;
	private String seedingDeadlineGuid = null;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{

		if ( seedingDeadlineGuid != null ) {
			SeedingDeadlineDao sdDao = persistenceSpringConfig.seedingDeadlineDao();
			SeedingDeadlineDto sdDto = sdDao.fetch(seedingDeadlineGuid);
			if (sdDto != null) {
				sdDao.delete(seedingDeadlineGuid);
			}
		}
		
		CommodityTypeCodeDao dao = persistenceSpringConfig.commodityTypeCodeDao();
		CommodityTypeCodeDto dto = dao.fetch(commodityTypeCode);
		if (dto != null) {
			dao.delete(commodityTypeCode);
		}
		
		CropCommodityDao daoCommodity = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto dtoCommodity = daoCommodity.fetch(cropCommodityId1);
		if (dtoCommodity != null) {
			daoCommodity.delete(cropCommodityId1);
		}

		dtoCommodity = daoCommodity.fetch(cropCommodityId2);
		if (dtoCommodity != null) {
			daoCommodity.delete(cropCommodityId2);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteCommodityTypeCode() throws Exception {

		CommodityTypeCodeDao dao = persistenceSpringConfig.commodityTypeCodeDao();
		CommodityTypeCodeDto newDto = new CommodityTypeCodeDto();
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();
		
		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

		Date dataSyncTransDate = addSeconds(dateTime, -120);
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);

		String description = "Test Type";
		
		String userId = "JUNIT_TEST";
		
		//Create Commodity
		createCropCommodity(cropCommodityId1, 1);
		createCropCommodity(cropCommodityId2, 1);

		//INSERT
		newDto.setCommodityTypeCode(commodityTypeCode);
		newDto.setCropCommodityId(cropCommodityId1);
		newDto.setDescription(description);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);

		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		CommodityTypeCodeDto fetchedDto = dao.fetch(commodityTypeCode);

		Assert.assertEquals("CommodityTypeCode", newDto.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("Description", newDto.getDescription(), fetchedDto.getDescription());
		Assert.assertTrue("EffectiveDate", newDto.getEffectiveDate().compareTo(fetchedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", newDto.getExpiryDate().compareTo(fetchedDto.getExpiryDate()) == 0);

		Assert.assertTrue("DataSyncTransDate 1", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		description = description + " 2";
		effectiveDate = addDays(date, -2);
		expiryDate = addDays(date, 2);
		dataSyncTransDate = addSeconds(dateTime, -60);
		
		fetchedDto.setCropCommodityId(cropCommodityId2);
		fetchedDto.setDescription(description);
		fetchedDto.setEffectiveDate(effectiveDate);
		fetchedDto.setExpiryDate(expiryDate);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		CommodityTypeCodeDto updatedDto = dao.fetch(commodityTypeCode);

		Assert.assertEquals("CropCommodityId 2", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("Description 2", fetchedDto.getDescription(), updatedDto.getDescription());
		Assert.assertTrue("EffectiveDate 2", fetchedDto.getEffectiveDate().compareTo(updatedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate 2", fetchedDto.getExpiryDate().compareTo(updatedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate 2", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		CommodityTypeCodeDto notUpdatedDto = dao.fetch(commodityTypeCode);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(notUpdatedDto.getCommodityTypeCode());

		//FETCH
		CommodityTypeCodeDto deletedDto = dao.fetch(notUpdatedDto.getCommodityTypeCode());
		Assert.assertNull(deletedDto);

	}

	@Test 
	public void testSelectByPlan() throws Exception {	
		
		CommodityTypeCodeDao dao = persistenceSpringConfig.commodityTypeCodeDao();

		//Return GRAIN varieties only
		Integer insurancePlanId = 4;

		// TEST 1: No crop year.
		List<CommodityTypeCodeDto> dtos = dao.selectByPlan(insurancePlanId, null);
		
		Assert.assertNotNull(dtos);
		
		for (CommodityTypeCodeDto dto : dtos) {
			Assert.assertEquals("insurancePlanId", insurancePlanId, dto.getInsurancePlanId());
			Assert.assertNotNull(dto.getCommodityTypeCode());
			Assert.assertNotNull(dto.getDescription());
			Assert.assertNotNull(dto.getCropVarietyId());
			Assert.assertNull(dto.getFullCoverageDeadlineDate());
			Assert.assertNull(dto.getFinalCoverageDeadlineDate());
		}
		
		// TEST 2: With crop year. Need to pick a crop year without existing deadlines.
		Integer cropYear = 2020;
		String commodityTypeWithDeadlines = dtos.get(0).getCommodityTypeCode();  // Pick an existing commodity type.
		String userId = "JUNIT_TEST";
		
		// Create SeedingDeadline
		SeedingDeadlineDao sdDao = persistenceSpringConfig.seedingDeadlineDao();

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(cropYear, Calendar.JANUARY, 15);
		Date fullCoverageDeadline = cal.getTime();
		Date fullCoverageDeadlineDefault = addDays(cal.getTime(), 1);

		cal.clear();
		cal.set(cropYear, Calendar.FEBRUARY, 16);
		Date finalCoverageDeadline = cal.getTime();
		Date finalCoverageDeadlineDefault = addDays(cal.getTime(), 1);
		
		SeedingDeadlineDto sdDto = new SeedingDeadlineDto();
		sdDto.setCommodityTypeCode(commodityTypeWithDeadlines);
		sdDto.setCropYear(cropYear);
		sdDto.setFullCoverageDeadlineDate(fullCoverageDeadline);
		sdDto.setFinalCoverageDeadlineDate(finalCoverageDeadline);
		sdDto.setFullCoverageDeadlineDateDefault(fullCoverageDeadlineDefault);
		sdDto.setFinalCoverageDeadlineDateDefault(finalCoverageDeadlineDefault);


		sdDao.insert(sdDto, userId);
		seedingDeadlineGuid = sdDto.getSeedingDeadlineGuid();
		
		// Search
		dtos = dao.selectByPlan(insurancePlanId, cropYear);
		
		Assert.assertNotNull(dtos);

		boolean found = false;
		for (CommodityTypeCodeDto dto : dtos) {			
			Assert.assertEquals("insurancePlanId", insurancePlanId, dto.getInsurancePlanId());
			Assert.assertNotNull(dto.getCommodityTypeCode());
			Assert.assertNotNull(dto.getDescription());
			Assert.assertNotNull(dto.getCropVarietyId());

			if (dto.getCommodityTypeCode().equals(commodityTypeWithDeadlines)) {
				found = true;
				Assert.assertEquals(sdDto.getFullCoverageDeadlineDate(), dto.getFullCoverageDeadlineDate());
				Assert.assertEquals(sdDto.getFinalCoverageDeadlineDate(), dto.getFinalCoverageDeadlineDate());
			}
		}

		if (!found) {
			Assert.fail("Commodity Type " + commodityTypeWithDeadlines + " missing.");
		}
		
		// Delete
		sdDao.delete(sdDto.getSeedingDeadlineGuid());		
	}
	

	@Test 
	public void testselectByCropCommodityPlan() throws Exception {	
		
		CommodityTypeCodeDao dao = persistenceSpringConfig.commodityTypeCodeDao();

		//Return GRAIN commodity types only
		Integer insurancePlanId = 4;

		List<CommodityTypeCodeDto> dtos = dao.selectByCropCommodityPlan(insurancePlanId);
		
		Assert.assertNotNull(dtos);
		
		for (CommodityTypeCodeDto dto : dtos) {
			Assert.assertEquals("insurancePlanId", insurancePlanId, dto.getInsurancePlanId());
			Assert.assertNotNull(dto.getCommodityTypeCode());
			Assert.assertNotNull(dto.getDescription());
		}
		
		//Return FORAGE commodity types only
		insurancePlanId = 5;

		dtos = dao.selectByCropCommodityPlan(insurancePlanId);
		
		Assert.assertNotNull(dtos);
		
		for (CommodityTypeCodeDto dto : dtos) {
			Assert.assertEquals("insurancePlanId", insurancePlanId, dto.getInsurancePlanId());
			Assert.assertNotNull(dto.getCommodityTypeCode());
			Assert.assertNotNull(dto.getDescription());
		}
	
	}
	
	private void createCropCommodity(Integer cropCommodityId, Integer insurancePlanId) throws DaoException {
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
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());;
		
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);
		Date dataSyncTransDate = addSeconds(dateTime, -120);

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
		newDto.setYieldMeasUnitTypeCode(yieldMeasUnitTypeCode);
		newDto.setYieldDecimalPrecision(yieldDecimalPrecision);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
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
}
