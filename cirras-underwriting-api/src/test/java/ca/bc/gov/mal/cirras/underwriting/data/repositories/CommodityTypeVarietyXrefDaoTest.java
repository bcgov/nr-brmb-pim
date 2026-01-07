package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityTypeVarietyXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class CommodityTypeVarietyXrefDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String commodityTypeCode = "TESTCODE";
	private Integer cropCommodityId = 88888888;
	private Integer cropVarietyId = 99999999;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{
		
		CommodityTypeVarietyXrefDao dao = persistenceSpringConfig.commodityTypeVarietyXrefDao();
		CommodityTypeVarietyXrefDto dto = dao.fetch(commodityTypeCode, cropVarietyId);
		if (dto != null) {
			dao.delete(commodityTypeCode, cropVarietyId);
		}
		
		CommodityTypeCodeDao daoCommodityTypeCode = persistenceSpringConfig.commodityTypeCodeDao();
		CommodityTypeCodeDto dtoCommodityTypeCode = daoCommodityTypeCode.fetch(commodityTypeCode);
		if (dtoCommodityTypeCode != null) {
			daoCommodityTypeCode.delete(commodityTypeCode);
		}
		
		CropVarietyDao daoVariety = persistenceSpringConfig.cropVarietyDao();
		CropVarietyDto dtoVariety = daoVariety.fetch(cropVarietyId);
		if (dtoVariety != null) {
			daoVariety.delete(cropVarietyId);
		}

		CropCommodityDao daoCommodity = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto dtoCommodity = daoCommodity.fetch(cropCommodityId);
		if (dtoCommodity != null) {
			daoCommodity.delete(cropCommodityId);
		}
		
	}
	
	@Test 
	public void testInsertDeleteCommodityTypeCode() throws Exception {

		CommodityTypeVarietyXrefDao dao = persistenceSpringConfig.commodityTypeVarietyXrefDao();
		CommodityTypeVarietyXrefDto newDto = new CommodityTypeVarietyXrefDto();
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();
		
		Date dataSyncTransDate = addSeconds(dateTime, -120);
		
		String userId = "JUNIT_TEST";
		
		//Create Commodity, variety and commodity type code
		createCropCommodity();
		createCropVariety();
		createCommodityTypeCode();

		//INSERT
		newDto.setCommodityTypeCode(commodityTypeCode);
		newDto.setCropVarietyId(cropVarietyId);

		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		CommodityTypeVarietyXrefDto fetchedDto = dao.fetch(commodityTypeCode, cropVarietyId);

		Assert.assertEquals("CommodityTypeCode", newDto.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("CropVarietyId", newDto.getCropVarietyId(), fetchedDto.getCropVarietyId());

		Assert.assertTrue("DataSyncTransDate 1", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		//DELETE
		dao.delete(fetchedDto.getCommodityTypeCode(), fetchedDto.getCropVarietyId());

		//FETCH
		CommodityTypeVarietyXrefDto deletedDto = dao.fetch(fetchedDto.getCommodityTypeCode(), fetchedDto.getCropVarietyId());
		Assert.assertNull(deletedDto);

	}

	private void createCommodityTypeCode() throws DaoException {
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
		
		//INSERT
		newDto.setCommodityTypeCode(commodityTypeCode);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setDescription(description);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);

		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
	}

	private void createCropVariety() throws DaoException {
		CropVarietyDao dao = persistenceSpringConfig.cropVarietyDao();
		CropVarietyDto newDto = new CropVarietyDto();
		
		String varietyName = "Test Variety";

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
		newDto.setCropVarietyId(cropVarietyId);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setVarietyName(varietyName);
		
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);

	}

	
	private void createCropCommodity() throws DaoException {
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
		newDto.setInsurancePlanId(1);
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
