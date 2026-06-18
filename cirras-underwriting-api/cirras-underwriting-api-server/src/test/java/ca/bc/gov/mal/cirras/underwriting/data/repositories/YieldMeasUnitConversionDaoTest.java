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

import ca.bc.gov.mal.cirras.underwriting.data.entities.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GradeModifierTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class YieldMeasUnitConversionDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String yieldMeasUnitConversionGuid1 = null;
	private String yieldMeasUnitConversionGuid2 = null;
	
	private Integer cropCommodityId = 88123888;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{
		
		YieldMeasUnitConversionDao dao = persistenceSpringConfig.yieldMeasUnitConversionDao();
		YieldMeasUnitConversionDto dto = null;

		if (yieldMeasUnitConversionGuid1 != null) {
			dto = dao.fetch(yieldMeasUnitConversionGuid1);

			if (dto != null) {
				dao.delete(yieldMeasUnitConversionGuid1);
			}
		}

		if (yieldMeasUnitConversionGuid2 != null) {
			dto = dao.fetch(yieldMeasUnitConversionGuid2);
	
			if (dto != null) {
				dao.delete(yieldMeasUnitConversionGuid2);
			}	
		}
		
		CropCommodityDao daoCommodity = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto dtoCommodity = daoCommodity.fetch(cropCommodityId);
		if (dtoCommodity != null) {
			daoCommodity.delete(cropCommodityId);
		}

	}
	
	@Test 
	public void testInsertUpdateDeleteYieldMeasUnitConversion() throws Exception {

		String userId = "JUNIT_TEST";

		YieldMeasUnitConversionDao dao = persistenceSpringConfig.yieldMeasUnitConversionDao();
		YieldMeasUnitConversionDto newDto = new YieldMeasUnitConversionDto();
				
		List<YieldMeasUnitConversionDto> dtos = dao.fetchAll();
		Assert.assertNotNull(dtos);
		int totalYmuc = dtos.size();
				
		newDto.setConversionFactor(12.345);
		newDto.setCropCommodityId(3);
		newDto.setEffectiveCropYear(1980);
		newDto.setExpiryCropYear(9999);
		newDto.setSrcYieldMeasUnitTypeCode("TONNE");
		newDto.setTargetYieldMeasUnitTypeCode("BUSHEL");
		newDto.setVersionNumber(1);
		newDto.setYieldMeasUnitConversionGuid(null);

		dao.insert(newDto, userId);

		yieldMeasUnitConversionGuid1 = newDto.getYieldMeasUnitConversionGuid();
		
		//FETCH
		YieldMeasUnitConversionDto fetchedDto = dao.fetch(newDto.getYieldMeasUnitConversionGuid());

		Assert.assertEquals("ConversionFactor", newDto.getConversionFactor(), fetchedDto.getConversionFactor());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("EffectiveCropYear", newDto.getEffectiveCropYear(), fetchedDto.getEffectiveCropYear());
		Assert.assertEquals("ExpiryCropYear", newDto.getExpiryCropYear(), fetchedDto.getExpiryCropYear());
		Assert.assertEquals("SrcYieldMeasUnitTypeCode", newDto.getSrcYieldMeasUnitTypeCode(), fetchedDto.getSrcYieldMeasUnitTypeCode());
		Assert.assertEquals("TargetYieldMeasUnitTypeCode", newDto.getTargetYieldMeasUnitTypeCode(), fetchedDto.getTargetYieldMeasUnitTypeCode());
		Assert.assertEquals("VersionNumber", newDto.getVersionNumber(), fetchedDto.getVersionNumber());
		Assert.assertEquals("YieldMeasUnitConversionGuid", newDto.getYieldMeasUnitConversionGuid(), fetchedDto.getYieldMeasUnitConversionGuid());

		// Extended columns not populated on fetch.
		Assert.assertNull(fetchedDto.getInsurancePlanId());

		//UPDATE
		fetchedDto.setConversionFactor(54.321);
		fetchedDto.setEffectiveCropYear(2022);
		fetchedDto.setExpiryCropYear(2030);
		fetchedDto.setVersionNumber(2);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		YieldMeasUnitConversionDto updatedDto = dao.fetch(fetchedDto.getYieldMeasUnitConversionGuid());

		
		Assert.assertEquals("ConversionFactor", fetchedDto.getConversionFactor(), updatedDto.getConversionFactor());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("EffectiveCropYear", fetchedDto.getEffectiveCropYear(), updatedDto.getEffectiveCropYear());
		Assert.assertEquals("ExpiryCropYear", fetchedDto.getExpiryCropYear(), updatedDto.getExpiryCropYear());
		Assert.assertEquals("SrcYieldMeasUnitTypeCode", fetchedDto.getSrcYieldMeasUnitTypeCode(), updatedDto.getSrcYieldMeasUnitTypeCode());
		Assert.assertEquals("TargetYieldMeasUnitTypeCode", fetchedDto.getTargetYieldMeasUnitTypeCode(), updatedDto.getTargetYieldMeasUnitTypeCode());
		Assert.assertEquals("VersionNumber", fetchedDto.getVersionNumber(), updatedDto.getVersionNumber());
		Assert.assertEquals("YieldMeasUnitConversionGuid", fetchedDto.getYieldMeasUnitConversionGuid(), updatedDto.getYieldMeasUnitConversionGuid());

		// Extended columns not populated on fetch.
		Assert.assertNull(updatedDto.getInsurancePlanId());
		
		// Fetch All
		dtos = dao.fetchAll();		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(totalYmuc + 1, dtos.size());
		
		//DELETE
		dao.delete(yieldMeasUnitConversionGuid1);
		yieldMeasUnitConversionGuid1 = null;

		//FETCH
		YieldMeasUnitConversionDto deletedDto = dao.fetch(updatedDto.getYieldMeasUnitConversionGuid());
		Assert.assertNull(deletedDto);

	}
	
	@Test 
	public void testYieldMeasUnitConversionSelectByYearAndPlan() throws Exception {
		
		String userId = "JUNIT_TEST";

		Integer cropYear = 2023;
		
		// Set to plan without existing data.
		Integer insurancePlanId = 2;
		
		YieldMeasUnitConversionDao dao = persistenceSpringConfig.yieldMeasUnitConversionDao();
		
		List<YieldMeasUnitConversionDto> dtos = dao.selectByYearAndPlan(cropYear, insurancePlanId);
		Assert.assertEquals(0,  dtos.size());

		// YieldMeasUnitConversion 1
		YieldMeasUnitConversionDto ymucDto1 = new YieldMeasUnitConversionDto();

		ymucDto1.setConversionFactor(12.345);
		ymucDto1.setCropCommodityId(3);
		ymucDto1.setEffectiveCropYear(1980);
		ymucDto1.setExpiryCropYear(9999);
		ymucDto1.setInsurancePlanId(insurancePlanId);
		ymucDto1.setSrcYieldMeasUnitTypeCode("TONNE");
		ymucDto1.setTargetYieldMeasUnitTypeCode("BUSHEL");
		ymucDto1.setVersionNumber(1);
		ymucDto1.setYieldMeasUnitConversionGuid(null);
		
		dao.insert(ymucDto1, userId);

		yieldMeasUnitConversionGuid1 = ymucDto1.getYieldMeasUnitConversionGuid();

		// YieldMeasUnitConversion 2
		YieldMeasUnitConversionDto ymucDto2 = new YieldMeasUnitConversionDto();

		//INSERT
		ymucDto2.setConversionFactor(56.789);
		ymucDto2.setCropCommodityId(9);
		ymucDto2.setEffectiveCropYear(2022);
		ymucDto2.setExpiryCropYear(2030);
		ymucDto2.setInsurancePlanId(insurancePlanId);
		ymucDto2.setSrcYieldMeasUnitTypeCode("BUSHEL");
		ymucDto2.setTargetYieldMeasUnitTypeCode("TONNE");
		ymucDto2.setVersionNumber(2);
		ymucDto2.setYieldMeasUnitConversionGuid(null);
		
		dao.insert(ymucDto2, userId);

		yieldMeasUnitConversionGuid2 = ymucDto2.getYieldMeasUnitConversionGuid();
		
		dtos = dao.selectByYearAndPlan(cropYear, insurancePlanId);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		// YieldMeasUnitConversion 1
		Assert.assertEquals("ConversionFactor", ymucDto1.getConversionFactor(), dtos.get(0).getConversionFactor());
		Assert.assertEquals("CropCommodityId", ymucDto1.getCropCommodityId(), dtos.get(0).getCropCommodityId());
		Assert.assertEquals("EffectiveCropYear", ymucDto1.getEffectiveCropYear(), dtos.get(0).getEffectiveCropYear());
		Assert.assertEquals("ExpiryCropYear", ymucDto1.getExpiryCropYear(), dtos.get(0).getExpiryCropYear());
		Assert.assertEquals("SrcYieldMeasUnitTypeCode", ymucDto1.getSrcYieldMeasUnitTypeCode(), dtos.get(0).getSrcYieldMeasUnitTypeCode());
		Assert.assertEquals("TargetYieldMeasUnitTypeCode", ymucDto1.getTargetYieldMeasUnitTypeCode(), dtos.get(0).getTargetYieldMeasUnitTypeCode());
		Assert.assertEquals("VersionNumber", ymucDto1.getVersionNumber(), dtos.get(0).getVersionNumber());
		Assert.assertEquals("YieldMeasUnitConversionGuid", ymucDto1.getYieldMeasUnitConversionGuid(), dtos.get(0).getYieldMeasUnitConversionGuid());

		// Extended columns are populated on select.
		Assert.assertEquals("InsurancePlanId", ymucDto1.getInsurancePlanId(), dtos.get(0).getInsurancePlanId());

		// YieldMeasUnitConversion 2
		Assert.assertEquals("ConversionFactor", ymucDto2.getConversionFactor(), dtos.get(1).getConversionFactor());
		Assert.assertEquals("CropCommodityId", ymucDto2.getCropCommodityId(), dtos.get(1).getCropCommodityId());
		Assert.assertEquals("EffectiveCropYear", ymucDto2.getEffectiveCropYear(), dtos.get(1).getEffectiveCropYear());
		Assert.assertEquals("ExpiryCropYear", ymucDto2.getExpiryCropYear(), dtos.get(1).getExpiryCropYear());
		Assert.assertEquals("SrcYieldMeasUnitTypeCode", ymucDto2.getSrcYieldMeasUnitTypeCode(), dtos.get(1).getSrcYieldMeasUnitTypeCode());
		Assert.assertEquals("TargetYieldMeasUnitTypeCode", ymucDto2.getTargetYieldMeasUnitTypeCode(), dtos.get(1).getTargetYieldMeasUnitTypeCode());
		Assert.assertEquals("VersionNumber", ymucDto2.getVersionNumber(), dtos.get(1).getVersionNumber());
		Assert.assertEquals("YieldMeasUnitConversionGuid", ymucDto2.getYieldMeasUnitConversionGuid(), dtos.get(1).getYieldMeasUnitConversionGuid());

		// Extended columns are populated on select.
		Assert.assertEquals("InsurancePlanId", ymucDto2.getInsurancePlanId(), dtos.get(1).getInsurancePlanId());
		
		// Change year filter.
		// No matches
		dtos = dao.selectByYearAndPlan(1970, insurancePlanId);
		Assert.assertEquals(0,  dtos.size());

		// Matches first record (min boundary - 1).
		dtos = dao.selectByYearAndPlan(2021, insurancePlanId);
		Assert.assertEquals(1,  dtos.size());
		Assert.assertEquals(yieldMeasUnitConversionGuid1, dtos.get(0).getYieldMeasUnitConversionGuid());

		// Matches both records (min boundary).
		dtos = dao.selectByYearAndPlan(2022, insurancePlanId);
		Assert.assertEquals(2,  dtos.size());
		Assert.assertEquals(yieldMeasUnitConversionGuid1, dtos.get(0).getYieldMeasUnitConversionGuid());
		Assert.assertEquals(yieldMeasUnitConversionGuid2, dtos.get(1).getYieldMeasUnitConversionGuid());

		// Matches both records (max boundary).
		dtos = dao.selectByYearAndPlan(2030, insurancePlanId);
		Assert.assertEquals(2,  dtos.size());
		Assert.assertEquals(yieldMeasUnitConversionGuid1, dtos.get(0).getYieldMeasUnitConversionGuid());
		Assert.assertEquals(yieldMeasUnitConversionGuid2, dtos.get(1).getYieldMeasUnitConversionGuid());

		// Matches first record (max boundary + 1).
		dtos = dao.selectByYearAndPlan(2031, insurancePlanId);
		Assert.assertEquals(1,  dtos.size());
		Assert.assertEquals(yieldMeasUnitConversionGuid1, dtos.get(0).getYieldMeasUnitConversionGuid());
		
		// Change plan filter.
		dtos = dao.selectByYearAndPlan(cropYear, 5);
		Assert.assertEquals(0,  dtos.size());
		
		//DELETE
		dao.delete(yieldMeasUnitConversionGuid1);
		yieldMeasUnitConversionGuid1 = null;
	
		//DELETE
		dao.delete(yieldMeasUnitConversionGuid2);
		yieldMeasUnitConversionGuid2 = null;
	}
	
	
	@Test 
	public void testYieldMeasUnitConversionSelectLatestVersionByPlan() throws Exception {
		
		String userId = "JUNIT_TEST";

		// Set to plan without existing data.
		Integer insurancePlanId = 4;
		String srcYieldMeasUnitTypeCode ="TONNE";
		String targetYieldMeasUnitTypeCode ="BUSHEL";

		
		YieldMeasUnitConversionDao dao = persistenceSpringConfig.yieldMeasUnitConversionDao();
		
		List<YieldMeasUnitConversionDto> dtos = dao.selectLatestVersionByPlan(insurancePlanId, srcYieldMeasUnitTypeCode, targetYieldMeasUnitTypeCode);
		Assert.assertNotNull(dtos);
		Assert.assertTrue(dtos.size() > 0);
		
		Integer totalExistingRecords = dtos.size();
		
		//Add commodity
		createCropCommodity(insurancePlanId);
		
		
		dtos = dao.selectLatestVersionByPlan(insurancePlanId, srcYieldMeasUnitTypeCode, targetYieldMeasUnitTypeCode);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(totalExistingRecords + 1, dtos.size());

		//Expect new commodity but without a conversion
		YieldMeasUnitConversionDto yldDto = getYieldMeasUnitConversionDto(dtos, cropCommodityId);
		Assert.assertNotNull(yldDto);
		Assert.assertNull(yldDto.getYieldMeasUnitConversionGuid());

		// Add conversion
		YieldMeasUnitConversionDto ymucDto1 = new YieldMeasUnitConversionDto();

		ymucDto1.setConversionFactor(12.345);
		ymucDto1.setCropCommodityId(cropCommodityId);
		ymucDto1.setEffectiveCropYear(1980);
		ymucDto1.setExpiryCropYear(9999);
		ymucDto1.setInsurancePlanId(insurancePlanId);
		ymucDto1.setSrcYieldMeasUnitTypeCode(srcYieldMeasUnitTypeCode);
		ymucDto1.setTargetYieldMeasUnitTypeCode(targetYieldMeasUnitTypeCode);
		ymucDto1.setVersionNumber(1);
		ymucDto1.setYieldMeasUnitConversionGuid(null);
		
		dao.insert(ymucDto1, userId);

		yieldMeasUnitConversionGuid1 = ymucDto1.getYieldMeasUnitConversionGuid();

		dtos = dao.selectLatestVersionByPlan(insurancePlanId, srcYieldMeasUnitTypeCode, targetYieldMeasUnitTypeCode);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(totalExistingRecords + 1, dtos.size());
		
		//Check if plan and units are correct
		for (YieldMeasUnitConversionDto dto : dtos) {
			Assert.assertEquals("InsurancePlanId", insurancePlanId, dto.getInsurancePlanId());
			Assert.assertNotNull("CommodityName", dto.getCommodityName());
			Assert.assertNotNull("CropCommodityId", dto.getCropCommodityId());
			if(dto.getYieldMeasUnitConversionGuid() != null) {
				Assert.assertEquals("SrcYieldMeasUnitTypeCode", srcYieldMeasUnitTypeCode, dto.getSrcYieldMeasUnitTypeCode());
				Assert.assertEquals("TargetYieldMeasUnitTypeCode", targetYieldMeasUnitTypeCode, dto.getTargetYieldMeasUnitTypeCode());
			}
		}
		
		//Expect new commodity but with a version 1 conversion
		yldDto = getYieldMeasUnitConversionDto(dtos, cropCommodityId);
		Assert.assertNotNull(yldDto);
		Assert.assertEquals("yieldMeasUnitConversionGuid1", yieldMeasUnitConversionGuid1, yldDto.getYieldMeasUnitConversionGuid());
		Assert.assertEquals("VersionNumber", ymucDto1.getVersionNumber(), yldDto.getVersionNumber());

		//ADD VERSION 2
		// YieldMeasUnitConversion 2
		YieldMeasUnitConversionDto ymucDto2 = new YieldMeasUnitConversionDto();

		//INSERT
		ymucDto2.setConversionFactor(56.789);
		ymucDto2.setCropCommodityId(cropCommodityId);
		ymucDto2.setEffectiveCropYear(2023);
		ymucDto2.setExpiryCropYear(2030);
		ymucDto2.setInsurancePlanId(insurancePlanId);
		ymucDto2.setSrcYieldMeasUnitTypeCode(srcYieldMeasUnitTypeCode);
		ymucDto2.setTargetYieldMeasUnitTypeCode(targetYieldMeasUnitTypeCode);
		ymucDto2.setVersionNumber(2);
		ymucDto2.setYieldMeasUnitConversionGuid(null);
		
		dao.insert(ymucDto2, userId);

		yieldMeasUnitConversionGuid2 = ymucDto2.getYieldMeasUnitConversionGuid();
		
		dtos = dao.selectLatestVersionByPlan(insurancePlanId, srcYieldMeasUnitTypeCode, targetYieldMeasUnitTypeCode);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(totalExistingRecords + 1, dtos.size());
		
		//Expect new commodity but with a version 2 conversion
		yldDto = getYieldMeasUnitConversionDto(dtos, cropCommodityId);
		Assert.assertNotNull(yldDto);
		Assert.assertEquals("yieldMeasUnitConversionGuid2", yieldMeasUnitConversionGuid2, yldDto.getYieldMeasUnitConversionGuid());
		Assert.assertEquals("VersionNumber", ymucDto2.getVersionNumber(), yldDto.getVersionNumber());

		//DELETE
		delete();
	}
	
	private YieldMeasUnitConversionDto getYieldMeasUnitConversionDto(List<YieldMeasUnitConversionDto> dtos, Integer cropCommodityId) {
		
		List<YieldMeasUnitConversionDto> filteredDtos = dtos.stream().filter(x -> x.getCropCommodityId().equals(cropCommodityId)).collect(Collectors.toList());
		
		if(filteredDtos != null && filteredDtos.size() == 1) {
			return filteredDtos.get(0);
		}
		return null;
	}
	
	private void createCropCommodity(Integer insurancePlanId) throws DaoException {
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
		Boolean isProductInsurableInd = true;
		Boolean isCropInsuranceEligibleInd = true;
		Boolean isPlantInsuranceEligibleInd = false;
		Boolean isOtherInsuranceEligibleInd = false;

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());;
		
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);
		Date dataSyncTransDate = dateTime;

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
		newDto.setIsProductInsurableInd(isProductInsurableInd);
		newDto.setIsCropInsuranceEligibleInd(isCropInsuranceEligibleInd);
		newDto.setIsPlantInsuranceEligibleInd(isPlantInsuranceEligibleInd);
		newDto.setIsOtherInsuranceEligibleInd(isOtherInsuranceEligibleInd);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
	}
	
	private static Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
}
