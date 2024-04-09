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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitPlanXrefDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class YieldMeasUnitTypeCodeDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String yieldMeasUnitPlanXrefGuid = null;
	private String yieldMeasUnitPlanXrefGuid2 = null;
	private String yieldMeasUnitTypeCode = "TEST";
	private String yieldMeasUnitTypeCode2 = "TEST2";
	private Integer insurancePlanId = 4;

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{

		YieldMeasUnitPlanXrefDao sdDao = persistenceSpringConfig.yieldMeasUnitPlanXrefDao();

		if ( yieldMeasUnitPlanXrefGuid != null ) {
			YieldMeasUnitPlanXrefDto sdDto = sdDao.fetch(yieldMeasUnitPlanXrefGuid);
			if (sdDto != null) {
				sdDao.delete(yieldMeasUnitPlanXrefGuid);
			}
		}
		if ( yieldMeasUnitPlanXrefGuid2 != null ) {
			YieldMeasUnitPlanXrefDto sdDto = sdDao.fetch(yieldMeasUnitPlanXrefGuid2);
			if (sdDto != null) {
				sdDao.delete(yieldMeasUnitPlanXrefGuid2);
			}
		}
		
		YieldMeasUnitTypeCodeDao daoCode = persistenceSpringConfig.yieldMeasUnitTypeCodeDao();
		YieldMeasUnitTypeCodeDto dtoCode = daoCode.fetch(yieldMeasUnitTypeCode);

		if (dtoCode != null) {
			daoCode.delete(yieldMeasUnitTypeCode);
		}

		dtoCode = daoCode.fetch(yieldMeasUnitTypeCode2);

		if (dtoCode != null) {
			daoCode.delete(yieldMeasUnitTypeCode2);
		}	

	}
	
	@Test 
	public void testInsertUpdateDeleteYieldMeasUnitTypeCode() throws Exception {

		String userId = "JUNIT_TEST";
		
		YieldMeasUnitTypeCodeDao dao = persistenceSpringConfig.yieldMeasUnitTypeCodeDao();
		YieldMeasUnitTypeCodeDto newDto = new YieldMeasUnitTypeCodeDto();
		
		String description = "Test Description";
		Integer decimalPrecision = 1;
		
		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());;
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);
		
		//INSERT
		newDto.setYieldMeasUnitTypeCode(yieldMeasUnitTypeCode);
		newDto.setDescription(description);
		newDto.setDecimalPrecision(decimalPrecision);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);

		dao.insert(newDto, userId);

		//FETCH
		YieldMeasUnitTypeCodeDto fetchedDto = dao.fetch(yieldMeasUnitTypeCode);

		Assert.assertEquals("YieldMeasUnitTypeCode", newDto.getYieldMeasUnitTypeCode(), fetchedDto.getYieldMeasUnitTypeCode());
		Assert.assertEquals("Description", newDto.getDescription(), fetchedDto.getDescription());
		Assert.assertEquals("DecimalPrecision", newDto.getDecimalPrecision(), fetchedDto.getDecimalPrecision());
		Assert.assertEquals("EffectiveDate", newDto.getEffectiveDate(), fetchedDto.getEffectiveDate());
		Assert.assertEquals("ExpiryDate", newDto.getExpiryDate(), fetchedDto.getExpiryDate());

		//UPDATE
		description = "Test Description 2";
		decimalPrecision = 2;
		effectiveDate = addDays(date, -2);
		expiryDate = addDays(date, 2);
		
		fetchedDto.setDescription(description);
		fetchedDto.setDecimalPrecision(decimalPrecision);
		fetchedDto.setEffectiveDate(effectiveDate);
		fetchedDto.setExpiryDate(expiryDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		YieldMeasUnitTypeCodeDto updatedDto = dao.fetch(yieldMeasUnitTypeCode);
		Assert.assertEquals("Description", fetchedDto.getDescription(), updatedDto.getDescription());
		Assert.assertEquals("DecimalPrecision", fetchedDto.getDecimalPrecision(), updatedDto.getDecimalPrecision());
		Assert.assertEquals("EffectiveDate", fetchedDto.getEffectiveDate(), updatedDto.getEffectiveDate());
		Assert.assertEquals("ExpiryDate", fetchedDto.getExpiryDate(), updatedDto.getExpiryDate());
		
		//DELETE
		dao.delete(yieldMeasUnitTypeCode);

		//FETCH
		YieldMeasUnitTypeCodeDto deletedDto = dao.fetch(yieldMeasUnitTypeCode);
		Assert.assertNull(deletedDto);

	}
	
	@Test 
	public void testYieldMeasUnitTypeCodeSelectByPlan() throws Exception {

		
		YieldMeasUnitTypeCodeDao dao = persistenceSpringConfig.yieldMeasUnitTypeCodeDao();

		List<YieldMeasUnitTypeCodeDto> dtos = dao.selectByPlan(insurancePlanId);

		Integer totalCodes = 0;

		//Store current record count
		if(dtos != null && dtos.size() > 0) {
			totalCodes = dtos.size();
		}
		
		// Create Yield Measurement Unit Type Codes.
		createYieldMeasUnitTypeCode(yieldMeasUnitTypeCode);
		createYieldMeasUnitTypeCode(yieldMeasUnitTypeCode2);
		
		// Create Yield Measurement Unit Plan Xref.
		yieldMeasUnitPlanXrefGuid = createYieldMeasUnitPlanXref(yieldMeasUnitTypeCode);
		yieldMeasUnitPlanXrefGuid2 = createYieldMeasUnitPlanXref(yieldMeasUnitTypeCode2);

		
		dtos = dao.selectByPlan(insurancePlanId);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(totalCodes + 2, dtos.size());
		
		for (YieldMeasUnitTypeCodeDto dto : dtos) {
			Assert.assertEquals(insurancePlanId, dto.getInsurancePlanId());
			Assert.assertNotNull("YieldMeasUnitTypeCode", dto.getYieldMeasUnitTypeCode());
			Assert.assertNotNull("Description", dto.getDescription());
			Assert.assertNotNull("DecimalPrecision", dto.getDecimalPrecision());
			Assert.assertNotNull("EffectiveDate", dto.getEffectiveDate());
			Assert.assertNotNull("ExpiryDate", dto.getExpiryDate());
			Assert.assertNotNull("IsDefaultYieldUnitInd", dto.getIsDefaultYieldUnitInd());
		}
		
	}	
	

	private void createYieldMeasUnitTypeCode(String yieldMeasUnitTypeCode) throws DaoException {

		YieldMeasUnitTypeCodeDao dao = persistenceSpringConfig.yieldMeasUnitTypeCodeDao();
		YieldMeasUnitTypeCodeDto newDto = new YieldMeasUnitTypeCodeDto();
		
		String description = yieldMeasUnitTypeCode;
		Integer decimalPrecision = 1;
		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());;
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);


		
		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setYieldMeasUnitTypeCode(yieldMeasUnitTypeCode);
		newDto.setDescription(description);
		newDto.setDecimalPrecision(decimalPrecision);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);

		dao.insert(newDto, userId);
	}

	
	private String createYieldMeasUnitPlanXref(String yieldMeasUnitTypeCode) throws DaoException {

		
		Boolean isDefaultYieldUnitInd = false;
		String userId = "JUNIT_TEST";

		YieldMeasUnitPlanXrefDao dao = persistenceSpringConfig.yieldMeasUnitPlanXrefDao();
		
		YieldMeasUnitPlanXrefDto newDto = new YieldMeasUnitPlanXrefDto();
		newDto.setYieldMeasUnitPlanXrefGuid(yieldMeasUnitPlanXrefGuid);
		newDto.setYieldMeasUnitTypeCode(yieldMeasUnitTypeCode);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setIsDefaultYieldUnitInd(isDefaultYieldUnitInd);

		dao.insert(newDto, userId);
		return newDto.getYieldMeasUnitPlanXrefGuid();
	}

	private static Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
}
