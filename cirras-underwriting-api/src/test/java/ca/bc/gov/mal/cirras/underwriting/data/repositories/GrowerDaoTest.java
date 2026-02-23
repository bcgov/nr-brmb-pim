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

import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class GrowerDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer growerId = 99999999;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteGrower();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteGrower();
	}
	
	private void deleteGrower() throws NotFoundDaoException, DaoException{
		
		GrowerDao dao = persistenceSpringConfig.growerDao();
		GrowerDto dto = dao.fetch(growerId);
		if (dto != null) {
			dao.delete(growerId);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteGrower() throws Exception {

		GrowerDao dao = persistenceSpringConfig.growerDao();
		GrowerDto newDto = new GrowerDto();
		
		Integer growerNumber = 123456;
		String growerName = "grower test name";
		String growerAddressLine1 = "address line 1";
		String growerAddressLine2 = "address line 2";
		String growerPostalCode = "V8P 4N8";
		String growerCity = "Victoria";
		Integer cityId = 1;
		String growerProvince = "BC";

		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());;

		
		Date dataSyncTransDate = addSeconds(dateTime, -120);

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setGrowerId(growerId);
		newDto.setGrowerNumber(growerNumber);
		newDto.setGrowerName(growerName);
		newDto.setGrowerAddressLine1(growerAddressLine1);
		newDto.setGrowerAddressLine2(growerAddressLine2);
		newDto.setGrowerPostalCode(growerPostalCode);
		newDto.setGrowerCity(growerCity);
		newDto.setCityId(cityId);
		newDto.setGrowerProvince(growerProvince);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		GrowerDto fetchedDto = dao.fetch(growerId);

		Assert.assertEquals("GrowerId", newDto.getGrowerId(), fetchedDto.getGrowerId());
		Assert.assertEquals("GrowerNumber", newDto.getGrowerNumber(), fetchedDto.getGrowerNumber());
		Assert.assertEquals("GrowerName", newDto.getGrowerName(), fetchedDto.getGrowerName());
		Assert.assertEquals("GrowerAddressLine1", newDto.getGrowerAddressLine1(), fetchedDto.getGrowerAddressLine1());
		Assert.assertEquals("GrowerAddressLine2", newDto.getGrowerAddressLine2(), fetchedDto.getGrowerAddressLine2());
		Assert.assertEquals("GrowerPostalCode", newDto.getGrowerPostalCode(), fetchedDto.getGrowerPostalCode());
		Assert.assertEquals("GrowerCity", newDto.getGrowerCity(), fetchedDto.getGrowerCity());
		Assert.assertEquals("CityId", newDto.getCityId(), fetchedDto.getCityId());
		Assert.assertEquals("GrowerProvince", newDto.getGrowerProvince(), fetchedDto.getGrowerProvince());
		Assert.assertTrue("DataSyncTransDate 1", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		growerNumber = 555666;
		growerName = "grower test name updated";
		growerAddressLine1 = "address line 1 updated";
		growerAddressLine2 = "address line 2 updated";
		growerPostalCode = "K1K 2K2";
		growerCity = "Kelowna";
		cityId = 2;
		growerProvince = "AB";
		
		dataSyncTransDate = addSeconds(dateTime, -60);
		fetchedDto.setGrowerNumber(growerNumber);
		fetchedDto.setGrowerName(growerName);
		fetchedDto.setGrowerAddressLine1(growerAddressLine1);
		fetchedDto.setGrowerAddressLine2(growerAddressLine2);
		fetchedDto.setGrowerPostalCode(growerPostalCode);
		fetchedDto.setGrowerCity(growerCity);
		fetchedDto.setCityId(cityId);
		fetchedDto.setGrowerProvince(growerProvince);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		GrowerDto updatedDto = dao.fetch(growerId);

		Assert.assertEquals("GrowerNumber", fetchedDto.getGrowerNumber(), updatedDto.getGrowerNumber());
		Assert.assertEquals("GrowerName", fetchedDto.getGrowerName(), updatedDto.getGrowerName());
		Assert.assertEquals("GrowerAddressLine1", fetchedDto.getGrowerAddressLine1(), updatedDto.getGrowerAddressLine1());
		Assert.assertEquals("GrowerAddressLine2", fetchedDto.getGrowerAddressLine2(), updatedDto.getGrowerAddressLine2());
		Assert.assertEquals("GrowerPostalCode", fetchedDto.getGrowerPostalCode(), updatedDto.getGrowerPostalCode());
		Assert.assertEquals("GrowerCity", fetchedDto.getGrowerCity(), updatedDto.getGrowerCity());
		Assert.assertEquals("CityId", fetchedDto.getCityId(), updatedDto.getCityId());
		Assert.assertEquals("GrowerProvince", fetchedDto.getGrowerProvince(), updatedDto.getGrowerProvince());
		Assert.assertTrue("DataSyncTransDate 2", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		GrowerDto notUpdatedDto = dao.fetch(growerId);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(notUpdatedDto.getGrowerId());

		//FETCH
		GrowerDto deletedDto = dao.fetch(notUpdatedDto.getGrowerId());
		Assert.assertNull(deletedDto);

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
