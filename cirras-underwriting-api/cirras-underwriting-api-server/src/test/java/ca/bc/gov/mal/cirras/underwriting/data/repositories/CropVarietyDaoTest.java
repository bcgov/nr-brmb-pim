package ca.bc.gov.mal.cirras.underwriting.data.repositories;

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

import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class CropVarietyDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer cropVarietyId = 99999;
	
	private Integer insurancePlanId = 4;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteCropVariety();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteCropVariety();
	}
	
	private void deleteCropVariety() throws NotFoundDaoException, DaoException{
		
		CropVarietyDao dao = persistenceSpringConfig.cropVarietyDao();
		CropVarietyDto dto = dao.fetch(cropVarietyId);
		if (dto != null) {
			dao.delete(cropVarietyId);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteCropVariety() throws Exception {

		CropVarietyDao dao = persistenceSpringConfig.cropVarietyDao();
		CropVarietyDto newDto = new CropVarietyDto();
		
		Integer cropCommodityId = 16;
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
		
		//FETCH
		CropVarietyDto fetchedDto = dao.fetch(cropVarietyId);

		Assert.assertEquals("CropVarietyId", newDto.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("VarietyName", newDto.getVarietyName(), fetchedDto.getVarietyName());
		Assert.assertTrue("EffectiveDate", newDto.getEffectiveDate().compareTo(fetchedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", newDto.getExpiryDate().compareTo(fetchedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		cropCommodityId = 16;
		varietyName = "Test Variety 2";
		effectiveDate = addDays(date, -2);
		expiryDate = addDays(date, 2);
		dataSyncTransDate = addSeconds(dataSyncTransDate, 60);

		fetchedDto.setCropCommodityId(cropCommodityId);
		fetchedDto.setVarietyName(varietyName);
		fetchedDto.setEffectiveDate(effectiveDate);
		fetchedDto.setExpiryDate(expiryDate);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		CropVarietyDto updatedDto = dao.fetch(cropVarietyId);
		
		Assert.assertEquals("CommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("VarietyName", fetchedDto.getVarietyName(), updatedDto.getVarietyName());
		Assert.assertTrue("EffectiveDate", fetchedDto.getEffectiveDate().compareTo(updatedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", fetchedDto.getExpiryDate().compareTo(updatedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		//Expect NO update because the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		CropVarietyDto notUpdatedDto = dao.fetch(cropVarietyId);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(notUpdatedDto.getCropVarietyId());
		
		//FETCH
		CropVarietyDto deletedDto = dao.fetch(cropVarietyId);
		Assert.assertNull(deletedDto);

	}
	
	@Test 
	public void testFetchAllCropVarieties() throws Exception {	
		
		CropVarietyDao dao = persistenceSpringConfig.cropVarietyDao();

		List<CropVarietyDto> dtos = dao.fetchAll();
		
		Assert.assertNotNull(dtos);

	}

	@Test 
	public void testSelectCropVarieties() throws Exception {	
		
		CropVarietyDao dao = persistenceSpringConfig.cropVarietyDao();

		//Return GRAIN varieties only
		insurancePlanId = 4;
		
		List<CropVarietyDto> dtos = dao.select(insurancePlanId);
		
		Assert.assertNotNull(dtos);
		
		for (CropVarietyDto dto : dtos) {
			Assert.assertEquals("insurancePlanId", insurancePlanId, dto.getInsurancePlanId());
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
