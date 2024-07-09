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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class DeclaredYieldContractCommodityForageDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer growerContractYearId = 99999999;
	private Integer contractId = 888888888;
	private Integer cropYear = 2020;
	private String declaredYieldContractGuid;
	private String commodityTypeCode1 = "TESTCODE DYF";
	private String commodityTypeCode2 = "TESTCODE DYF2";
	private String declaredYieldContractCmdtyForageGuid;
	private Integer cropCommodityId = 88995566;
	private Integer insurancePlanId = 5;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{
		
		//DELETE Declared Yield Contract Commodity Forage
		DeclaredYieldContractCommodityForageDao dyccfDao = persistenceSpringConfig.declaredYieldContractCommodityForageDao();
		dyccfDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
		
		//DELETE Declared Yield Contract
		if(declaredYieldContractGuid != null) {
			DeclaredYieldContractDao dopContractDao = persistenceSpringConfig.declaredYieldContractDao();
			DeclaredYieldContractDto dtoDeclaredYieldContract = dopContractDao.fetch(declaredYieldContractGuid);
			if (dtoDeclaredYieldContract != null) {
				dopContractDao.delete(declaredYieldContractGuid);
			}
		}

		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto dto = dao.fetch(growerContractYearId);
		if (dto != null) {
			dao.delete(growerContractYearId);
		}
		
		deleteCommodityTypeCode(commodityTypeCode1);
		deleteCommodityTypeCode(commodityTypeCode2);

		CropCommodityDao daoCommodity = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto dtoCommodity = daoCommodity.fetch(cropCommodityId);
		if (dtoCommodity != null) {
			daoCommodity.delete(cropCommodityId);
		}

	}

	protected void deleteCommodityTypeCode(String commodityTypeCode) throws DaoException, NotFoundDaoException {
		CommodityTypeCodeDao daoCommodityTypeCode = persistenceSpringConfig.commodityTypeCodeDao();
		CommodityTypeCodeDto dtoCommodityTypeCode = daoCommodityTypeCode.fetch(commodityTypeCode);
		if (dtoCommodityTypeCode != null) {
			daoCommodityTypeCode.delete(commodityTypeCode);
		}
	}


	@Test 
	public void testDeclaredYieldContractCommodityForage() throws Exception {

		String userId = "UNITTEST";

		createGrowerContractYear();
		createDeclaredYieldContract(userId);
		createCropCommodity();
		createCommodityTypeCode(commodityTypeCode1);
		createCommodityTypeCode(commodityTypeCode2);
		
		DeclaredYieldContractCommodityForageDao dao = persistenceSpringConfig.declaredYieldContractCommodityForageDao();

		// INSERT
		DeclaredYieldContractCommodityForageDto newDto = new DeclaredYieldContractCommodityForageDto();
		newDto.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto.setCommodityTypeCode(commodityTypeCode1);
		newDto.setTotalFieldAcres(40.0);
		newDto.setHarvestedAcres(15.0);
		newDto.setHarvestedAcresOverride(10.0);
		newDto.setQuantityHarvestedTons(18.0);
		newDto.setQuantityHarvestedTonsOverride(14.0);
		newDto.setYieldPerAcre(1.0);

		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getDeclaredYieldContractCmdtyForageGuid());
		declaredYieldContractCmdtyForageGuid = newDto.getDeclaredYieldContractCmdtyForageGuid();
		
		//SELECT for declared yield contract
		List<DeclaredYieldContractCommodityForageDto> dtos = dao.selectForDeclaredYieldContract(declaredYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		DeclaredYieldContractCommodityForageDto dto = dtos.get(0);
		Assert.assertEquals("DeclaredYieldContractCommodityGuid", newDto.getDeclaredYieldContractCmdtyForageGuid(), dto.getDeclaredYieldContractCmdtyForageGuid());
		Assert.assertEquals("DeclaredYieldContractGuid", newDto.getDeclaredYieldContractGuid(), dto.getDeclaredYieldContractGuid());
		Assert.assertEquals("CommodityTypeCode", newDto.getCommodityTypeCode(), dto.getCommodityTypeCode());
		Assert.assertEquals("TotalFieldAcres", newDto.getTotalFieldAcres(), dto.getTotalFieldAcres());
		Assert.assertEquals("HarvestedAcres", newDto.getHarvestedAcres(), dto.getHarvestedAcres());
		Assert.assertEquals("HarvestedAcresOverride", newDto.getHarvestedAcresOverride(), dto.getHarvestedAcresOverride());
		Assert.assertEquals("QuantityHarvestedTons", newDto.getQuantityHarvestedTons(), dto.getQuantityHarvestedTons());
		Assert.assertEquals("QuantityHarvestedTonsOverride", newDto.getQuantityHarvestedTonsOverride(), dto.getQuantityHarvestedTonsOverride());
		Assert.assertEquals("YieldPerAcre", newDto.getYieldPerAcre(), dto.getYieldPerAcre());
		
		//FETCH
		DeclaredYieldContractCommodityForageDto fetchedDto = dao.fetch(declaredYieldContractCmdtyForageGuid);
		
		Assert.assertEquals("DeclaredYieldContractCommodityGuid", newDto.getDeclaredYieldContractCmdtyForageGuid(), fetchedDto.getDeclaredYieldContractCmdtyForageGuid());
		Assert.assertEquals("DeclaredYieldContractGuid", newDto.getDeclaredYieldContractGuid(), fetchedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("CommodityTypeCode", newDto.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("TotalFieldAcres", newDto.getTotalFieldAcres(), fetchedDto.getTotalFieldAcres());
		Assert.assertEquals("HarvestedAcres", newDto.getHarvestedAcres(), fetchedDto.getHarvestedAcres());
		Assert.assertEquals("HarvestedAcresOverride", newDto.getHarvestedAcresOverride(), fetchedDto.getHarvestedAcresOverride());
		Assert.assertEquals("QuantityHarvestedTons", newDto.getQuantityHarvestedTons(), fetchedDto.getQuantityHarvestedTons());
		Assert.assertEquals("QuantityHarvestedTonsOverride", newDto.getQuantityHarvestedTonsOverride(), fetchedDto.getQuantityHarvestedTonsOverride());
		Assert.assertEquals("YieldPerAcre", newDto.getYieldPerAcre(), fetchedDto.getYieldPerAcre());

		//UPDATE
		fetchedDto.setTotalFieldAcres(40.5);
		fetchedDto.setHarvestedAcres(15.5);
		fetchedDto.setHarvestedAcresOverride(10.5);
		fetchedDto.setQuantityHarvestedTons(18.5);
		fetchedDto.setQuantityHarvestedTonsOverride(14.5);
		fetchedDto.setYieldPerAcre(1.5);

		dao.update(fetchedDto, userId);

		//FETCH
		DeclaredYieldContractCommodityForageDto updatedDto = dao.fetch(declaredYieldContractCmdtyForageGuid);

		Assert.assertEquals("TotalFieldAcres", fetchedDto.getTotalFieldAcres(), updatedDto.getTotalFieldAcres());
		Assert.assertEquals("HarvestedAcres", fetchedDto.getHarvestedAcres(), updatedDto.getHarvestedAcres());
		Assert.assertEquals("HarvestedAcresOverride", fetchedDto.getHarvestedAcresOverride(), updatedDto.getHarvestedAcresOverride());
		Assert.assertEquals("QuantityHarvestedTons", fetchedDto.getQuantityHarvestedTons(), updatedDto.getQuantityHarvestedTons());
		Assert.assertEquals("QuantityHarvestedTonsOverride", fetchedDto.getQuantityHarvestedTonsOverride(), updatedDto.getQuantityHarvestedTonsOverride());
		Assert.assertEquals("YieldPerAcre", fetchedDto.getYieldPerAcre(), updatedDto.getYieldPerAcre());

		//INSERT second commodity
		DeclaredYieldContractCommodityForageDto newDto2 = new DeclaredYieldContractCommodityForageDto();
		newDto2.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto2.setCommodityTypeCode(commodityTypeCode2);
		newDto2.setTotalFieldAcres(40.0);
		newDto2.setHarvestedAcres(15.0);
		newDto2.setHarvestedAcresOverride(10.0);
		newDto2.setQuantityHarvestedTons(18.0);
		newDto2.setQuantityHarvestedTonsOverride(14.0);
		newDto2.setYieldPerAcre(1.0);

		dao.insert(newDto2, userId);

		//SELECT
		dtos = dao.selectForDeclaredYieldContract(declaredYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		//Check if all records have the correct declared yield contract association
		for (DeclaredYieldContractCommodityForageDto dyccfDto : dtos) {
			Assert.assertEquals(dyccfDto.getDeclaredYieldContractGuid(), declaredYieldContractGuid);
		}
		
		dtos = dao.selectToRecalculate(cropCommodityId, "LB", cropYear, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		//DELETE
		dao.delete(newDto2.getDeclaredYieldContractCmdtyForageGuid());
		dao.delete(declaredYieldContractCmdtyForageGuid);
		
		//FETCH
		DeclaredYieldContractCommodityForageDto deletedDto = dao.fetch(declaredYieldContractCmdtyForageGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = dao.selectForDeclaredYieldContract(declaredYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		delete();

	}

	private void createDeclaredYieldContract(String userId) throws DaoException {

		// Create parent Declared Yield Contract.
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();
		
		DeclaredYieldContractDao dao = persistenceSpringConfig.declaredYieldContractDao();
		
		DeclaredYieldContractDto newDto = new DeclaredYieldContractDto();

		newDto.setContractId(contractId);
		newDto.setCropYear(cropYear);
		newDto.setDeclarationOfProductionDate(dopDate);
		newDto.setDefaultYieldMeasUnitTypeCode("TONNE");
		newDto.setEnteredYieldMeasUnitTypeCode("LB");
		newDto.setGrainFromOtherSourceInd(true);
		newDto.setBalerWagonInfo(null);
		newDto.setTotalLivestock(null);
		
		//INSERT
		dao.insert(newDto, userId);
		declaredYieldContractGuid = newDto.getDeclaredYieldContractGuid();
		
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
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);

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
		newDto.setIsProductInsurableInd(true);
		newDto.setIsCropInsuranceEligibleInd(true);
		newDto.setIsPlantInsuranceEligibleInd(true);
		newDto.setIsOtherInsuranceEligibleInd(true);
		newDto.setYieldMeasUnitTypeCode(yieldMeasUnitTypeCode);
		newDto.setYieldDecimalPrecision(yieldDecimalPrecision);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dateTime);

		dao.insert(newDto, userId);
	}
	
	private void createCommodityTypeCode(String commodityTypeCode) throws DaoException {
		CommodityTypeCodeDao dao = persistenceSpringConfig.commodityTypeCodeDao();
		CommodityTypeCodeDto newDto = new CommodityTypeCodeDto();
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();
		
		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

		Date dataSyncTransDate = dateTime;
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
	
	private void createGrowerContractYear() throws DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto newDto = new GrowerContractYearDto();
		
		Integer growerId = 525593;
		Integer insurancePlanId = 5;

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();
		
		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setGrowerContractYearId(growerContractYearId);
		newDto.setContractId(contractId);
		newDto.setGrowerId(growerId);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setCropYear(cropYear);
		newDto.setDataSyncTransDate(dateTime);

		dao.insert(newDto, userId);
	}

	private static Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
}
