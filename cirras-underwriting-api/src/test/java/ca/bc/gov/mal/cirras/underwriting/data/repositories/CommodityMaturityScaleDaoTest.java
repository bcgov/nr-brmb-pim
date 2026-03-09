package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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

import ca.bc.gov.mal.cirras.underwriting.data.entities.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityMaturityScaleDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class CommodityMaturityScaleDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer cropCommodityId1 = 88888788;
	private Integer cropCommodityId2 = 88888799;
	private List<String> commodityMaturityScaleGuids = null;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{

		if ( commodityMaturityScaleGuids != null && commodityMaturityScaleGuids.size() > 0 ) {
			CommodityMaturityScaleDao sdDao = persistenceSpringConfig.commodityMaturityScaleDao();
			for (String sdGuid : commodityMaturityScaleGuids) {
				CommodityMaturityScaleDto sdDto = sdDao.fetch(sdGuid);
				if (sdDto != null) {
					sdDao.delete(sdGuid);
				}
			}
		}
		
		deleteCommodity(cropCommodityId1);
		deleteCommodity(cropCommodityId2);
		
	}

	protected void deleteCommodity(Integer cropCommodityId) throws DaoException, NotFoundDaoException {
		CropCommodityDao daoCommodity = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto dtoCommodity = daoCommodity.fetch(cropCommodityId);
		if (dtoCommodity != null) {
			daoCommodity.delete(cropCommodityId);
		}

	}
	
	@Test 
	public void testInsertUpdateDeleteCommodityMaturityScale() throws Exception {

		commodityMaturityScaleGuids = new ArrayList<String>();
		
		String userId = "JUNIT_TEST";
		
		//Create Commodity
		createCropCommodity(cropCommodityId1, 3, "Test Commodity 1", "TC1");
		createCropCommodity(cropCommodityId2, 3, "Test Commodity 2", "TC2");

		// Create CommodityMaturityScale
		CommodityMaturityScaleDao dao = persistenceSpringConfig.commodityMaturityScaleDao();
		
		CommodityMaturityScaleDto newDto = new CommodityMaturityScaleDto();
		newDto.setCropCommodityId(cropCommodityId1);
		newDto.setPlantAge(0);
		newDto.setScale(0.01);
		newDto.setVersionNumber(1);
		newDto.setEffectiveCropYear(2020);
		newDto.setExpiryCropYear(2026);

		dao.insert(newDto, userId);
		commodityMaturityScaleGuids.add(newDto.getCommodityMaturityScaleGuid());
		
		//FETCH
		CommodityMaturityScaleDto fetchedDto = dao.fetch(newDto.getCommodityMaturityScaleGuid());

		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("PlantAge", newDto.getPlantAge(), fetchedDto.getPlantAge());
		Assert.assertEquals("Scale", newDto.getScale(), fetchedDto.getScale());
		Assert.assertEquals("VersionNumber", newDto.getVersionNumber(), fetchedDto.getVersionNumber());
		Assert.assertEquals("EffectiveCropYear", newDto.getEffectiveCropYear(), fetchedDto.getEffectiveCropYear());
		Assert.assertEquals("ExpiryCropYear", newDto.getExpiryCropYear(), fetchedDto.getExpiryCropYear());
		Assert.assertNotNull("CommodityMaturityScaleGuid", fetchedDto.getCommodityMaturityScaleGuid());

		//UPDATE
		fetchedDto.setCropCommodityId(cropCommodityId2);
		fetchedDto.setPlantAge(1);
		fetchedDto.setScale(0.02);
		fetchedDto.setVersionNumber(2);
		fetchedDto.setEffectiveCropYear(2021);
		fetchedDto.setExpiryCropYear(2027);		
		dao.update(fetchedDto, userId);
		
		//FETCH
		CommodityMaturityScaleDto updatedDto = dao.fetch(fetchedDto.getCommodityMaturityScaleGuid());

		Assert.assertEquals("CommodityMaturityScaleGuid", fetchedDto.getCommodityMaturityScaleGuid(), updatedDto.getCommodityMaturityScaleGuid());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("PlantAge", fetchedDto.getPlantAge(), updatedDto.getPlantAge());
		Assert.assertEquals("Scale", fetchedDto.getScale(), updatedDto.getScale());
		Assert.assertEquals("VersionNumber", fetchedDto.getVersionNumber(), updatedDto.getVersionNumber());
		Assert.assertEquals("EffectiveCropYear", fetchedDto.getEffectiveCropYear(), updatedDto.getEffectiveCropYear());
		Assert.assertEquals("ExpiryCropYear", fetchedDto.getExpiryCropYear(), updatedDto.getExpiryCropYear());

		
		//DELETE
		dao.delete(updatedDto.getCommodityMaturityScaleGuid());

		//FETCH
		CommodityMaturityScaleDto deletedDto = dao.fetch(updatedDto.getCommodityMaturityScaleGuid());
		Assert.assertNull(deletedDto);
		
		delete();

	}
	
	@Test 
	public void testSelectByYear() throws Exception {

		commodityMaturityScaleGuids = new ArrayList<String>();

		String userId = "JUNIT_TEST";

		//Create Commodity
		createCropCommodity(cropCommodityId1, 3, "Test Commodity 1", "TC1");

		

		// Create CommodityMaturityScale
		CommodityMaturityScaleDao dao = persistenceSpringConfig.commodityMaturityScaleDao();

		createCommodityMaturityScale(userId, cropCommodityId1, 1, 0.5, 1, 2000, 2003);
		createCommodityMaturityScale(userId, cropCommodityId1, 2, 0.6, 1, 2000, 2003);
		createCommodityMaturityScale(userId, cropCommodityId1, 3, 0.7, 1, 2004, 2006);
		createCommodityMaturityScale(userId, cropCommodityId1, 4, 0.8, 1, 2007, 2007);

		Integer cropYear = 2000;

		//Select for crop year
		List<CommodityMaturityScaleDto> dtos = dao.selectByYear(cropYear);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		checkYears(dtos, cropYear);
		
		cropYear = 2003;

		//Select for crop year
		dtos = dao.selectByYear(cropYear);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		checkYears(dtos, cropYear);
		
		cropYear = 2005;

		//Select for crop year
		dtos = dao.selectByYear(cropYear);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		checkYears(dtos, cropYear);
		
		//Check if code was only valid for one year
		cropYear = 2007;

		//Select for crop year
		dtos = dao.selectByYear(cropYear);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		checkYears(dtos, cropYear);

		delete();

	}

	private void checkYears(List<CommodityMaturityScaleDto> dtos, Integer cropYear) {
		for (CommodityMaturityScaleDto dto : dtos) {
			Assert.assertTrue(cropYear >= dto.getEffectiveCropYear());
			Assert.assertTrue(cropYear <= dto.getExpiryCropYear());
		}
	}

	public CommodityMaturityScaleDto createCommodityMaturityScale(
			String userId, 
			Integer cropCommodityId, 
			Integer plantAge, 
			Double scale, 
			Integer versionNumber, 
			Integer effectiveCropYear, 
			Integer expiryCropYear) throws DaoException {

		CommodityMaturityScaleDao dao = persistenceSpringConfig.commodityMaturityScaleDao();

		
		CommodityMaturityScaleDto newDto = new CommodityMaturityScaleDto();
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setPlantAge(plantAge);
		newDto.setScale(scale);
		newDto.setVersionNumber(versionNumber);
		newDto.setEffectiveCropYear(effectiveCropYear);
		newDto.setExpiryCropYear(expiryCropYear);
		
		dao.insert(newDto, userId);
		commodityMaturityScaleGuids.add(newDto.getCommodityMaturityScaleGuid());
		
		return newDto;
	}
	
	private void createCropCommodity(Integer cropCommodityId, 
			Integer insurancePlanId, 
			String commodityName, 
			String shortLabel) throws DaoException {
		CropCommodityDao dao = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto newDto = new CropCommodityDto();
		
		//String commodityName = "Test Commodity";
		//String shortLabel = "TC";
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
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 because they are not set in the database
		Date dateTime = cal.getTime();

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		
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
