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

import ca.bc.gov.mal.cirras.underwriting.data.entities.ClaimCalculationBerriesSyncDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationBerriesSyncDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String claimCalculationBerriesGuid = "testClaimCalculationBerriesGuid";

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteClaimCalculationBerriesSync();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteClaimCalculationBerriesSync();
	}
	
	private void deleteClaimCalculationBerriesSync() throws NotFoundDaoException, DaoException{
		
		ClaimCalculationBerriesSyncDao dao = persistenceSpringConfig.claimCalculationBerriesSyncDao();
		ClaimCalculationBerriesSyncDto dto = dao.fetch(claimCalculationBerriesGuid);
		if (dto != null) {
			dao.delete(claimCalculationBerriesGuid);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteClaimCalculationBerriesSync() throws Exception {

		ClaimCalculationBerriesSyncDao dao = persistenceSpringConfig.claimCalculationBerriesSyncDao();
		ClaimCalculationBerriesSyncDto newDto = new ClaimCalculationBerriesSyncDto();
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		Date dataSyncTransDate = addSeconds(dateTime, -120);

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setCropCommodityId(10);
		newDto.setContractId(12345666);
		newDto.setCropYear(2025);
		newDto.setClaimCalculationGuid("testClaimCalculationGuid");
		newDto.setClaimCalculationBerriesGuid(claimCalculationBerriesGuid);
		newDto.setTotalYieldForCalculation(100.0);
		newDto.setCalculationStatusCode("DRAFT");
		newDto.setCalculationVersion(1);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		ClaimCalculationBerriesSyncDto fetchedDto = dao.fetch(claimCalculationBerriesGuid);
		

		Assert.assertEquals("ClaimCalculationBerriesSyncGuid", newDto.getClaimCalculationBerriesSyncGuid(), fetchedDto.getClaimCalculationBerriesSyncGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("ClaimCalculationGuid", newDto.getClaimCalculationGuid(), fetchedDto.getClaimCalculationGuid());
		Assert.assertEquals("ClaimCalculationBerriesGuid", newDto.getClaimCalculationBerriesGuid(), fetchedDto.getClaimCalculationBerriesGuid());
		Assert.assertEquals("TotalYieldForCalculation", newDto.getTotalYieldForCalculation(), fetchedDto.getTotalYieldForCalculation());
		Assert.assertEquals("CalculationStatusCode", newDto.getCalculationStatusCode(), fetchedDto.getCalculationStatusCode());
		Assert.assertEquals("CalculationVersion", newDto.getCalculationVersion(), fetchedDto.getCalculationVersion());
		Assert.assertTrue("DataSyncTransDate 1", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		dataSyncTransDate = addSeconds(dateTime, -60);
		fetchedDto.setTotalYieldForCalculation(200.0);
		fetchedDto.setCalculationStatusCode("APPROVED");
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ClaimCalculationBerriesSyncDto updatedDto = dao.fetch(claimCalculationBerriesGuid);

		Assert.assertEquals("TotalYieldForCalculation", fetchedDto.getTotalYieldForCalculation(), updatedDto.getTotalYieldForCalculation());
		Assert.assertEquals("CalculationStatusCode", fetchedDto.getCalculationStatusCode(), updatedDto.getCalculationStatusCode());
		Assert.assertTrue("DataSyncTransDate 2", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		ClaimCalculationBerriesSyncDto notUpdatedDto = dao.fetch(claimCalculationBerriesGuid);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(notUpdatedDto.getClaimCalculationBerriesGuid());

		//FETCH
		ClaimCalculationBerriesSyncDto deletedDto = dao.fetch(notUpdatedDto.getClaimCalculationBerriesGuid());
		Assert.assertNull(deletedDto);

	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

}
