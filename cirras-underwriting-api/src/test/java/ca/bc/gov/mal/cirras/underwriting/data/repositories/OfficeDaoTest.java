package ca.bc.gov.mal.cirras.underwriting.data.repositories;

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

import ca.bc.gov.mal.cirras.underwriting.data.entities.OfficeDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class OfficeDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer officeId = 999999999;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteOffice();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteOffice();
	}
	
	private void deleteOffice() throws NotFoundDaoException, DaoException{
		
		OfficeDao dao = persistenceSpringConfig.officeDao();
		OfficeDto dto = dao.fetch(officeId);
		if (dto != null) {
			dao.delete(officeId);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteOffice() throws Exception {

		OfficeDao dao = persistenceSpringConfig.officeDao();
		OfficeDto newDto = new OfficeDto();
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		Date dataSyncTransDate = addSeconds(dateTime, -120);

		String officeName = "Victoria";
		
		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setOfficeId(officeId);
		newDto.setOfficeName(officeName);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		OfficeDto fetchedDto = dao.fetch(officeId);

		Assert.assertEquals("OfficeId", newDto.getOfficeId(), fetchedDto.getOfficeId());
		Assert.assertEquals("OfficeName", newDto.getOfficeName(), fetchedDto.getOfficeName());
		Assert.assertTrue("DataSyncTransDate 1", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		officeName = "Vancouver";
		
		dataSyncTransDate = addSeconds(dateTime, -60);
		fetchedDto.setOfficeName(officeName);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		OfficeDto updatedDto = dao.fetch(officeId);

		Assert.assertEquals("OfficeName 2", fetchedDto.getOfficeName(), updatedDto.getOfficeName());
		Assert.assertTrue("DataSyncTransDate 2", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		OfficeDto notUpdatedDto = dao.fetch(officeId);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(notUpdatedDto.getOfficeId());

		//FETCH
		OfficeDto deletedDto = dao.fetch(notUpdatedDto.getOfficeId());
		Assert.assertNull(deletedDto);

	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

}
