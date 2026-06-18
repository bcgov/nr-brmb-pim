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

import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class GrowerContractYearDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer growerContractYearId = 99999999;
	private Integer growerContractYearId2 = 90000001;
	private Integer growerContractYearId3 = 90000002;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}

	private void delete() throws NotFoundDaoException, DaoException { 
		deleteGrowerContractYear(growerContractYearId);
		deleteGrowerContractYear(growerContractYearId2);
		deleteGrowerContractYear(growerContractYearId3);
	}
	
	private void deleteGrowerContractYear(Integer growerContractYearId) throws NotFoundDaoException, DaoException {
		
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto dto = dao.fetch(growerContractYearId);
		if (dto != null) {
			dao.delete(growerContractYearId);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteGrowerContractYear() throws Exception {

		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto newDto = new GrowerContractYearDto();
		
		Integer contractId = 99000001;
		Integer growerId = 525593;
		Integer insurancePlanId = 2;
		Integer cropYear = 2024;

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		Date dataSyncTransDate = addSeconds(dateTime, -120);

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setGrowerContractYearId(growerContractYearId);
		newDto.setContractId(contractId);
		newDto.setGrowerId(growerId);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setCropYear(cropYear);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		GrowerContractYearDto fetchedDto = dao.fetch(growerContractYearId);

		Assert.assertEquals("GrowerContractYearId 1", newDto.getGrowerContractYearId(), fetchedDto.getGrowerContractYearId());
		Assert.assertEquals("ContractId 1", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("GrowerId 1", newDto.getGrowerId(), fetchedDto.getGrowerId());
		Assert.assertEquals("InsurancePlanId 1", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("CropYear 1", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertTrue("DataSyncTransDate 1", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		contractId = 99000002;
		growerId = 8511;
		insurancePlanId = 3;
		cropYear = 2025;
		
		dataSyncTransDate = addSeconds(dateTime, -60);

		fetchedDto.setContractId(contractId);
		fetchedDto.setGrowerId(growerId);
		fetchedDto.setInsurancePlanId(insurancePlanId);
		fetchedDto.setCropYear(cropYear);

		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		GrowerContractYearDto updatedDto = dao.fetch(growerContractYearId);

		Assert.assertEquals("ContractId 2", fetchedDto.getContractId(), updatedDto.getContractId());
		Assert.assertEquals("GrowerId 2", fetchedDto.getGrowerId(), updatedDto.getGrowerId());
		Assert.assertEquals("InsurancePlanId 2", fetchedDto.getInsurancePlanId(), updatedDto.getInsurancePlanId());
		Assert.assertEquals("CropYear 2", fetchedDto.getCropYear(), updatedDto.getCropYear());
		Assert.assertTrue("DataSyncTransDate 2", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		GrowerContractYearDto notUpdatedDto = dao.fetch(growerContractYearId);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(notUpdatedDto.getGrowerContractYearId());

		//FETCH
		GrowerContractYearDto deletedDto = dao.fetch(notUpdatedDto.getGrowerContractYearId());
		Assert.assertNull(deletedDto);

	}
	
	@Test 
	public void testSelectLastYear() throws Exception {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();

		Integer contractId = 99000001;
		
		createGrowerContractYear(growerContractYearId, contractId, null, 2022);

		// Select - No results
		GrowerContractYearDto lastDto = dao.selectLastYear(contractId, 2022);
		Assert.assertNull(lastDto);

		// Select - 2020
		createGrowerContractYear(growerContractYearId2, contractId, null, 2020);
		
		lastDto = dao.selectLastYear(contractId, 2022);
		Assert.assertNotNull(lastDto);
		Assert.assertEquals(growerContractYearId2, lastDto.getGrowerContractYearId());

		// Select - 2020 (ignores 2019 record)
		createGrowerContractYear(growerContractYearId3, contractId, null, 2019);
		
		lastDto = dao.selectLastYear(contractId, 2022);
		Assert.assertNotNull(lastDto);
		Assert.assertEquals(growerContractYearId2, lastDto.getGrowerContractYearId());
	}
	
	
	private void createGrowerContractYear(
			Integer growerContractYearId, 
			Integer contractId, 
			Integer growerId,
			Integer cropYear) throws DaoException {
		String userId = "JUNIT_TEST";

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transDate = cal.getTime();		

		GrowerContractYearDao gcyDao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto gcyDto = new GrowerContractYearDto();		

		//INSERT
		gcyDto.setGrowerContractYearId(growerContractYearId);
		gcyDto.setContractId(contractId);
		gcyDto.setGrowerId(growerId);
		gcyDto.setInsurancePlanId(4);
		gcyDto.setCropYear(cropYear);
		gcyDto.setDataSyncTransDate(transDate);
		
		gcyDao.insert(gcyDto, userId);
	}
	
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

}
