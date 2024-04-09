package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.SeedingDeadlineDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class SeedingDeadlineDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String commodityTypeCode = "TESTCODE";
	private String commodityTypeCode2 = "TESTCODE2";
	private Integer cropCommodityId = 88888888;
	private List<String> seedingDeadlineGuids = null;
	private Integer newCropYear = 2011; //A crop year that is not used


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{

		if ( seedingDeadlineGuids != null && seedingDeadlineGuids.size() > 0 ) {
			SeedingDeadlineDao sdDao = persistenceSpringConfig.seedingDeadlineDao();
			for (String sdGuid : seedingDeadlineGuids) {
				SeedingDeadlineDto sdDto = sdDao.fetch(sdGuid);
				if (sdDto != null) {
					sdDao.delete(sdGuid);
				}
			}
		}
		
		deleteCommodityTypeCode(commodityTypeCode);
		deleteCommodityTypeCode(commodityTypeCode2);
		
		CropCommodityDao daoCommodity = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto dtoCommodity = daoCommodity.fetch(cropCommodityId);
		if (dtoCommodity != null) {
			daoCommodity.delete(cropCommodityId);
		}
		
		deleteUnderwritingYear(newCropYear);
		deleteUnderwritingYear(newCropYear -1);

	}
	
	private void deleteUnderwritingYear(Integer cropYear) throws DaoException {
		UnderwritingYearDao dao = persistenceSpringConfig.underwritingYearDao();
		UnderwritingYearDto dto = null;
		
		dto = dao.selectByCropYear(cropYear);

		if (dto != null) {
			dao.deleteByCropYear(cropYear);
		}
	}

	protected void deleteCommodityTypeCode(String commodityType) throws DaoException, NotFoundDaoException {
		CommodityTypeCodeDao ctcDao = persistenceSpringConfig.commodityTypeCodeDao();
		CommodityTypeCodeDto ctcDto = ctcDao.fetch(commodityType);
		if (ctcDto != null) {
			ctcDao.delete(commodityType);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteSeedingDeadline() throws Exception {

		seedingDeadlineGuids = new ArrayList<String>();
		
		Calendar cal = Calendar.getInstance();
		String userId = "JUNIT_TEST";
		
		//Create Commodity
		createCropCommodity(cropCommodityId, 1);

		//Create Commodity Type
		createCommodityTypeCode(commodityTypeCode, cropCommodityId);

		// Create SeedingDeadline
		Integer cropYear = 2023;
		SeedingDeadlineDao dao = persistenceSpringConfig.seedingDeadlineDao();

		cal.clear();
		cal.set(cropYear, Calendar.JANUARY, 15);
		Date fullCoverageDeadline = cal.getTime();
		Date fullCoverageDeadlineDefault = addDays(cal.getTime(), 1);

		cal.clear();
		cal.set(cropYear, Calendar.FEBRUARY, 16);
		Date finalCoverageDeadline = cal.getTime();
		Date finalCoverageDeadlineDefault = addDays(cal.getTime(), 1);
		
		SeedingDeadlineDto newDto = new SeedingDeadlineDto();
		newDto.setCommodityTypeCode(commodityTypeCode);
		newDto.setCropYear(cropYear);
		newDto.setFullCoverageDeadlineDate(fullCoverageDeadline);
		newDto.setFinalCoverageDeadlineDate(finalCoverageDeadline);
		newDto.setFullCoverageDeadlineDateDefault(fullCoverageDeadlineDefault);
		newDto.setFinalCoverageDeadlineDateDefault(finalCoverageDeadlineDefault);

		dao.insert(newDto, userId);
		seedingDeadlineGuids.add(newDto.getSeedingDeadlineGuid());
		
		//FETCH
		SeedingDeadlineDto fetchedDto = dao.fetch(newDto.getSeedingDeadlineGuid());

		Assert.assertEquals("CommodityTypeCode", newDto.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("FinalCoverageDeadlineDate", newDto.getFinalCoverageDeadlineDate(), fetchedDto.getFinalCoverageDeadlineDate());
		Assert.assertEquals("FullCoverageDeadlineDate", newDto.getFullCoverageDeadlineDate(), fetchedDto.getFullCoverageDeadlineDate());
		Assert.assertEquals("FullCoverageDeadlineDateDefault", newDto.getFullCoverageDeadlineDateDefault(), fetchedDto.getFullCoverageDeadlineDateDefault());
		Assert.assertEquals("FinalCoverageDeadlineDateDefault", newDto.getFinalCoverageDeadlineDateDefault(), fetchedDto.getFinalCoverageDeadlineDateDefault());
		Assert.assertEquals("SeedingDeadlineGuid", newDto.getSeedingDeadlineGuid(), fetchedDto.getSeedingDeadlineGuid());

		//UPDATE
		fetchedDto.setFinalCoverageDeadlineDate(addDays(fetchedDto.getFinalCoverageDeadlineDate(), 1));
		fetchedDto.setFullCoverageDeadlineDate(addDays(fetchedDto.getFullCoverageDeadlineDate(), 1));
		fetchedDto.setFinalCoverageDeadlineDateDefault(addDays(fetchedDto.getFinalCoverageDeadlineDateDefault(), 1));
		fetchedDto.setFullCoverageDeadlineDateDefault(addDays(fetchedDto.getFullCoverageDeadlineDateDefault(), 1));
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		SeedingDeadlineDto updatedDto = dao.fetch(fetchedDto.getSeedingDeadlineGuid());

		Assert.assertEquals("CommodityTypeCode", fetchedDto.getCommodityTypeCode(), updatedDto.getCommodityTypeCode());
		Assert.assertEquals("CropYear", fetchedDto.getCropYear(), updatedDto.getCropYear());
		Assert.assertEquals("FinalCoverageDeadlineDate", fetchedDto.getFinalCoverageDeadlineDate(), updatedDto.getFinalCoverageDeadlineDate());
		Assert.assertEquals("FullCoverageDeadlineDate", fetchedDto.getFullCoverageDeadlineDate(), updatedDto.getFullCoverageDeadlineDate());
		Assert.assertEquals("FullCoverageDeadlineDateDefault", fetchedDto.getFullCoverageDeadlineDateDefault(), updatedDto.getFullCoverageDeadlineDateDefault());
		Assert.assertEquals("FinalCoverageDeadlineDateDefault", fetchedDto.getFinalCoverageDeadlineDateDefault(), updatedDto.getFinalCoverageDeadlineDateDefault());
		Assert.assertEquals("SeedingDeadlineGuid", fetchedDto.getSeedingDeadlineGuid(), updatedDto.getSeedingDeadlineGuid());

		//SELECT
		SeedingDeadlineDto selectedDto = dao.selectForCommodityTypeAndYear(updatedDto.getCommodityTypeCode(), updatedDto.getCropYear());

		Assert.assertEquals("CommodityTypeCode", updatedDto.getCommodityTypeCode(), selectedDto.getCommodityTypeCode());
		Assert.assertEquals("CropYear", updatedDto.getCropYear(), selectedDto.getCropYear());
		Assert.assertEquals("FinalCoverageDeadlineDate", updatedDto.getFinalCoverageDeadlineDate(), selectedDto.getFinalCoverageDeadlineDate());
		Assert.assertEquals("FullCoverageDeadlineDate", updatedDto.getFullCoverageDeadlineDate(), selectedDto.getFullCoverageDeadlineDate());
		Assert.assertEquals("FullCoverageDeadlineDateDefault", updatedDto.getFullCoverageDeadlineDateDefault(), selectedDto.getFullCoverageDeadlineDateDefault());
		Assert.assertEquals("FinalCoverageDeadlineDateDefault", updatedDto.getFinalCoverageDeadlineDateDefault(), selectedDto.getFinalCoverageDeadlineDateDefault());
		Assert.assertEquals("SeedingDeadlineGuid", updatedDto.getSeedingDeadlineGuid(), selectedDto.getSeedingDeadlineGuid());
		
		
		//DELETE
		dao.delete(selectedDto.getSeedingDeadlineGuid());

		//FETCH
		SeedingDeadlineDto deletedDto = dao.fetch(selectedDto.getSeedingDeadlineGuid());
		Assert.assertNull(deletedDto);
		
		delete();

	}
	
	@Test 
	public void testSelectByYear() throws Exception {

		seedingDeadlineGuids = new ArrayList<String>();

		String userId = "JUNIT_TEST";

		//Create Commodity
		createCropCommodity(cropCommodityId, 1);

		//Create Commodity Type
		createCommodityTypeCode(commodityTypeCode, cropCommodityId);
		createCommodityTypeCode(commodityTypeCode2, cropCommodityId);

		//Create underwriting year
		createUnderwritingYear(newCropYear);
		createUnderwritingYear(newCropYear -1);

		// Create SeedingDeadline
		SeedingDeadlineDao dao = persistenceSpringConfig.seedingDeadlineDao();

		createSeedingDeadline(userId, newCropYear, commodityTypeCode);
		createSeedingDeadline(userId, newCropYear, commodityTypeCode2);
		createSeedingDeadline(userId, newCropYear -1, commodityTypeCode2);
		
		//Select for crop year
		List<SeedingDeadlineDto> dtos = dao.selectByYear(newCropYear);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		for (SeedingDeadlineDto dto : dtos) {
			Assert.assertEquals(newCropYear, dto.getCropYear());
		}
		
		delete();

	}

	public SeedingDeadlineDto createSeedingDeadline(String userId, Integer cropYear, String commodityType) throws DaoException {
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(cropYear, Calendar.JANUARY, 15);
		Date coverageDeadline = cal.getTime();

		SeedingDeadlineDao dao = persistenceSpringConfig.seedingDeadlineDao();

		Date fullCoverageDeadline = coverageDeadline;
		Date fullCoverageDeadlineDefault = addDays(coverageDeadline, 1);

		Date finalCoverageDeadline = addDays(coverageDeadline, 5);
		Date finalCoverageDeadlineDefault = addDays(coverageDeadline, 6);
		
		SeedingDeadlineDto newDto = new SeedingDeadlineDto();
		newDto.setCommodityTypeCode(commodityType);
		newDto.setCropYear(cropYear);
		newDto.setFullCoverageDeadlineDate(fullCoverageDeadline);
		newDto.setFinalCoverageDeadlineDate(finalCoverageDeadline);
		newDto.setFullCoverageDeadlineDateDefault(fullCoverageDeadlineDefault);
		newDto.setFinalCoverageDeadlineDateDefault(finalCoverageDeadlineDefault);

		dao.insert(newDto, userId);
		seedingDeadlineGuids.add(newDto.getSeedingDeadlineGuid());
		
		return newDto;
	}
	
	private void createUnderwritingYear(Integer cropYear) throws DaoException {
		
		UnderwritingYearDao dao = persistenceSpringConfig.underwritingYearDao();
		UnderwritingYearDto newDto = new UnderwritingYearDto();
		newDto.setCropYear(cropYear);
		dao.insert(newDto, "JUNIT_TEST");
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
	
	private void createCommodityTypeCode(String commodityTypeCode, Integer cropCommodityId) throws DaoException {
		CommodityTypeCodeDao ctcDao = persistenceSpringConfig.commodityTypeCodeDao();
		CommodityTypeCodeDto ctcDto = new CommodityTypeCodeDto();
		
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
		ctcDto.setCommodityTypeCode(commodityTypeCode);
		ctcDto.setCropCommodityId(cropCommodityId);
		ctcDto.setDescription(description);
		ctcDto.setEffectiveDate(effectiveDate);
		ctcDto.setExpiryDate(expiryDate);

		ctcDto.setDataSyncTransDate(dataSyncTransDate);

		ctcDao.insert(ctcDto, userId);		
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
