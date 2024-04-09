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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class CropCommodityDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer cropCommodityId = 99999;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteCropCommodity();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteCropCommodity();
	}
	
	private void deleteCropCommodity() throws NotFoundDaoException, DaoException{
		
		CropCommodityDao dao = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto dto = dao.fetch(cropCommodityId);
		if (dto != null) {
			dao.delete(cropCommodityId);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteCropCommodity() throws Exception {

		CropCommodityDao dao = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto newDto = new CropCommodityDto();
		
		String commodityName = "Test Commodity";
		String shortLabel = "TC";
		String plantDurationTypeCode = "PERENNIAL";
		Boolean isInventoryCropInd = true;
		Boolean isYieldCropInd = true;
		Boolean isUnderwritingCropInd = true;
		Boolean isProductInsurableInd = true;
		Boolean isCropInsuranceEligibleInd = true;
		Boolean isPlantInsuranceEligibleInd = false;
		Boolean isOtherInsuranceEligibleInd = false;
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
		newDto.setInsurancePlanId(1);
		newDto.setShortLabel(shortLabel);
		newDto.setPlantDurationTypeCode(plantDurationTypeCode);
		newDto.setIsInventoryCropInd(isInventoryCropInd);
		newDto.setIsYieldCropInd(isYieldCropInd);
		newDto.setIsUnderwritingCropInd(isUnderwritingCropInd);
		newDto.setIsProductInsurableInd(isProductInsurableInd);
		newDto.setIsCropInsuranceEligibleInd(isCropInsuranceEligibleInd);
		newDto.setIsPlantInsuranceEligibleInd(isPlantInsuranceEligibleInd);
		newDto.setIsOtherInsuranceEligibleInd(isOtherInsuranceEligibleInd);
		newDto.setYieldMeasUnitTypeCode(yieldMeasUnitTypeCode);
		newDto.setYieldDecimalPrecision(yieldDecimalPrecision);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		CropCommodityDto fetchedDto = dao.fetch(cropCommodityId);

		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CommodityName", newDto.getCommodityName(), fetchedDto.getCommodityName());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("ShortLabel", newDto.getShortLabel(), fetchedDto.getShortLabel());
		Assert.assertEquals("PlantDurationTypeCode", newDto.getPlantDurationTypeCode(), fetchedDto.getPlantDurationTypeCode());
		Assert.assertEquals("IsInventoryCropInd", newDto.getIsInventoryCropInd(), fetchedDto.getIsInventoryCropInd());
		Assert.assertEquals("IsYieldCropInd", newDto.getIsYieldCropInd(), fetchedDto.getIsYieldCropInd());
		Assert.assertEquals("IsUnderwritingCropInd", newDto.getIsUnderwritingCropInd(), fetchedDto.getIsUnderwritingCropInd());
		Assert.assertEquals("IsProductInsurableInd", newDto.getIsProductInsurableInd(), fetchedDto.getIsProductInsurableInd());
		Assert.assertEquals("IsCropInsuranceEligibleInd", newDto.getIsCropInsuranceEligibleInd(), fetchedDto.getIsCropInsuranceEligibleInd());
		Assert.assertEquals("IsPlantInsuranceEligibleInd", newDto.getIsPlantInsuranceEligibleInd(), fetchedDto.getIsPlantInsuranceEligibleInd());
		Assert.assertEquals("IsOtherInsuranceEligibleInd", newDto.getIsOtherInsuranceEligibleInd(), fetchedDto.getIsOtherInsuranceEligibleInd());
		Assert.assertEquals("YieldMeasUnitTypeCode", newDto.getYieldMeasUnitTypeCode(), fetchedDto.getYieldMeasUnitTypeCode());
		Assert.assertEquals("YieldDecimalPrecision", newDto.getYieldDecimalPrecision(), fetchedDto.getYieldDecimalPrecision());
		Assert.assertTrue("EffectiveDate", newDto.getEffectiveDate().compareTo(fetchedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", newDto.getExpiryDate().compareTo(fetchedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		commodityName = "Test Commodity 2";
		shortLabel = "TC2";
		plantDurationTypeCode = "ANNUAL";
		isInventoryCropInd = false;
		isYieldCropInd = false;
		isUnderwritingCropInd = false;
		isProductInsurableInd = false;
		isCropInsuranceEligibleInd = false;
		isPlantInsuranceEligibleInd = true;
		isOtherInsuranceEligibleInd = true;
		yieldMeasUnitTypeCode = "TONNE";
		yieldDecimalPrecision = 2;
		effectiveDate = addDays(date, -2);
		expiryDate = addDays(date, 2);
		dataSyncTransDate = addSeconds(dataSyncTransDate, 60);

		fetchedDto.setCommodityName(commodityName);
		fetchedDto.setInsurancePlanId(1);
		fetchedDto.setShortLabel(shortLabel);
		fetchedDto.setPlantDurationTypeCode(plantDurationTypeCode);
		fetchedDto.setIsInventoryCropInd(isInventoryCropInd);
		fetchedDto.setIsYieldCropInd(isYieldCropInd);
		fetchedDto.setIsUnderwritingCropInd(isUnderwritingCropInd);
		fetchedDto.setIsProductInsurableInd(isProductInsurableInd);
		fetchedDto.setIsCropInsuranceEligibleInd(isCropInsuranceEligibleInd);
		fetchedDto.setIsPlantInsuranceEligibleInd(isPlantInsuranceEligibleInd);
		fetchedDto.setIsOtherInsuranceEligibleInd(isOtherInsuranceEligibleInd);
		fetchedDto.setYieldMeasUnitTypeCode(yieldMeasUnitTypeCode);
		fetchedDto.setYieldDecimalPrecision(yieldDecimalPrecision);
		fetchedDto.setEffectiveDate(effectiveDate);
		fetchedDto.setExpiryDate(expiryDate);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		CropCommodityDto updatedDto = dao.fetch(cropCommodityId);
		
		Assert.assertEquals("CommodityName", fetchedDto.getCommodityName(), updatedDto.getCommodityName());
		Assert.assertEquals("InsurancePlanId", fetchedDto.getInsurancePlanId(), updatedDto.getInsurancePlanId());
		Assert.assertEquals("ShortLabel", fetchedDto.getShortLabel(), updatedDto.getShortLabel());
		Assert.assertEquals("PlantDurationTypeCode", fetchedDto.getPlantDurationTypeCode(), updatedDto.getPlantDurationTypeCode());
		Assert.assertEquals("IsInventoryCropInd", fetchedDto.getIsInventoryCropInd(), updatedDto.getIsInventoryCropInd());
		Assert.assertEquals("IsYieldCropInd", fetchedDto.getIsYieldCropInd(), updatedDto.getIsYieldCropInd());
		Assert.assertEquals("IsUnderwritingCropInd", fetchedDto.getIsUnderwritingCropInd(), updatedDto.getIsUnderwritingCropInd());
		Assert.assertEquals("IsProductInsurableInd", fetchedDto.getIsProductInsurableInd(), updatedDto.getIsProductInsurableInd());
		Assert.assertEquals("IsCropInsuranceEligibleInd", fetchedDto.getIsCropInsuranceEligibleInd(), updatedDto.getIsCropInsuranceEligibleInd());
		Assert.assertEquals("IsPlantInsuranceEligibleInd", fetchedDto.getIsPlantInsuranceEligibleInd(), updatedDto.getIsPlantInsuranceEligibleInd());
		Assert.assertEquals("IsOtherInsuranceEligibleInd", fetchedDto.getIsOtherInsuranceEligibleInd(), updatedDto.getIsOtherInsuranceEligibleInd());
		Assert.assertEquals("YieldMeasUnitTypeCode", fetchedDto.getYieldMeasUnitTypeCode(), updatedDto.getYieldMeasUnitTypeCode());
		Assert.assertEquals("YieldDecimalPrecision", fetchedDto.getYieldDecimalPrecision(), updatedDto.getYieldDecimalPrecision());
		Assert.assertTrue("EffectiveDate", fetchedDto.getEffectiveDate().compareTo(updatedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", fetchedDto.getExpiryDate().compareTo(updatedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		CropCommodityDto notUpdatedDto = dao.fetch(cropCommodityId);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(notUpdatedDto.getCropCommodityId());
		
		//FETCH
		CropCommodityDto deletedDto = dao.fetch(cropCommodityId);
		Assert.assertNull(deletedDto);

	}
	
	@Test 
	public void testFetchAllCropCommodities() throws Exception {	
		
		CropCommodityDao dao = persistenceSpringConfig.cropCommodityDao();

		List<CropCommodityDto> dtos = dao.fetchAll();
		
		Assert.assertNotNull(dtos);

	}

	@Test 
	public void testSelectCropCommodities() throws Exception {	
		
		CropCommodityDao dao = persistenceSpringConfig.cropCommodityDao();

		Integer insurancePlanId = 4;
		Integer cropYear = 2021;
		String commodityType = null;
		
		List<CropCommodityDto> dtos = dao.select(insurancePlanId, cropYear, commodityType);
		
		Assert.assertNotNull(dtos);
		
		//Return inventory commodities only
		commodityType = "INV";
		
		dtos = dao.select(insurancePlanId, cropYear, commodityType);
		
		Assert.assertNotNull(dtos);
		
		for (CropCommodityDto dto : dtos) {
			Assert.assertEquals("IsInventoryCropInd", true, dto.getIsInventoryCropInd());
		}
		
		//Return yield commodities only
		commodityType = "YLD";
		
		dtos = dao.select(insurancePlanId, cropYear, commodityType);
		
		Assert.assertNotNull(dtos);
		
		for (CropCommodityDto dto : dtos) {
			Assert.assertEquals("IsYieldCropInd", true, dto.getIsYieldCropInd());
		}
		
		//Return underwriting commodities only
		commodityType = "UW";
		
		dtos = dao.select(insurancePlanId, cropYear, commodityType);
		
		Assert.assertNotNull(dtos);
		
		for (CropCommodityDto dto : dtos) {
			Assert.assertEquals("IsUnderwritingCropInd", true, dto.getIsUnderwritingCropInd());
		}
	
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
