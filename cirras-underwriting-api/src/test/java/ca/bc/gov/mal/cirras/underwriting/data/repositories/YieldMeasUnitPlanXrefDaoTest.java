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

import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitPlanXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class YieldMeasUnitPlanXrefDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String yieldMeasUnitPlanXrefGuid = null;
	private String yieldMeasUnitTypeCode = "TEST";
	private String yieldMeasUnitTypeCode2 = "TEST2";


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{

		if ( yieldMeasUnitPlanXrefGuid != null ) {
			YieldMeasUnitPlanXrefDao sdDao = persistenceSpringConfig.yieldMeasUnitPlanXrefDao();
			YieldMeasUnitPlanXrefDto sdDto = sdDao.fetch(yieldMeasUnitPlanXrefGuid);
			if (sdDto != null) {
				sdDao.delete(yieldMeasUnitPlanXrefGuid);
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
	public void testInsertUpdateDeleteYieldMeasUnitPlanXref() throws Exception {

		String userId = "JUNIT_TEST";

		Integer insurancePlanId = 4;
		Boolean isDefaultYieldUnitInd = false;
		
		Integer insurancePlanId2 = 5;
		Boolean isDefaultYieldUnitInd2 = true;
		
		// Create Yield Measurement Unit Type Codes.
		createYieldMeasUnitTypeCode(yieldMeasUnitTypeCode);
		createYieldMeasUnitTypeCode(yieldMeasUnitTypeCode2);

		// Create YieldMeasUnitPlanXref
		YieldMeasUnitPlanXrefDao dao = persistenceSpringConfig.yieldMeasUnitPlanXrefDao();
		
		YieldMeasUnitPlanXrefDto newDto = new YieldMeasUnitPlanXrefDto();
		newDto.setYieldMeasUnitPlanXrefGuid(yieldMeasUnitPlanXrefGuid);
		newDto.setYieldMeasUnitTypeCode(yieldMeasUnitTypeCode);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setIsDefaultYieldUnitInd(isDefaultYieldUnitInd);

		dao.insert(newDto, userId);
		yieldMeasUnitPlanXrefGuid = newDto.getYieldMeasUnitPlanXrefGuid();
		
		//FETCH
		YieldMeasUnitPlanXrefDto fetchedDto = dao.fetch(newDto.getYieldMeasUnitPlanXrefGuid());

		Assert.assertEquals("YieldMeasUnitPlanXrefGuid", newDto.getYieldMeasUnitPlanXrefGuid(), fetchedDto.getYieldMeasUnitPlanXrefGuid());
		Assert.assertEquals("YieldMeasUnitTypeCode", newDto.getYieldMeasUnitTypeCode(), fetchedDto.getYieldMeasUnitTypeCode());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("IsDefaultYieldUnitInd", newDto.getIsDefaultYieldUnitInd(), fetchedDto.getIsDefaultYieldUnitInd());

		//UPDATE
		fetchedDto.setYieldMeasUnitTypeCode(yieldMeasUnitTypeCode2);
		fetchedDto.setInsurancePlanId(insurancePlanId2);
		fetchedDto.setIsDefaultYieldUnitInd(isDefaultYieldUnitInd2);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		YieldMeasUnitPlanXrefDto updatedDto = dao.fetch(fetchedDto.getYieldMeasUnitPlanXrefGuid());

		Assert.assertEquals("YieldMeasUnitTypeCode", fetchedDto.getYieldMeasUnitTypeCode(), updatedDto.getYieldMeasUnitTypeCode());
		Assert.assertEquals("InsurancePlanId", fetchedDto.getInsurancePlanId(), updatedDto.getInsurancePlanId());
		Assert.assertEquals("IsDefaultYieldUnitInd", fetchedDto.getIsDefaultYieldUnitInd(), updatedDto.getIsDefaultYieldUnitInd());
		
		//DELETE
		dao.delete(updatedDto.getYieldMeasUnitPlanXrefGuid());

		//FETCH
		YieldMeasUnitPlanXrefDto deletedDto = dao.fetch(updatedDto.getYieldMeasUnitPlanXrefGuid());
		Assert.assertNull(deletedDto);

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

	private static Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
}
