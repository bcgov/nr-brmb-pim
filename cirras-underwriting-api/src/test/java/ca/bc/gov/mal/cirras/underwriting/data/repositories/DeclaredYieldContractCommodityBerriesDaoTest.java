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

import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class DeclaredYieldContractCommodityBerriesDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer growerContractYearId = 99999999;
	private Integer contractId = 888888888;
	private Integer cropYear = 2020;
	private String declaredYieldContractGuid = null;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{

		
		DeclaredYieldContractDao dopContractDao = persistenceSpringConfig.declaredYieldContractDao();
		DeclaredYieldContractDto dtoDeclaredYieldContract = dopContractDao.getByContractAndYear(contractId, cropYear);
		if (dtoDeclaredYieldContract != null) {

			//DELETE Declared Yield Contract Commodity Berries
			DeclaredYieldContractCommodityBerriesDao dyccbDao = persistenceSpringConfig.declaredYieldContractCommodityBerriesDao();
			dyccbDao.deleteForDeclaredYieldContract(dtoDeclaredYieldContract.getDeclaredYieldContractGuid());

			//DELETE Declared Yield Contract
			dopContractDao.delete(dtoDeclaredYieldContract.getDeclaredYieldContractGuid());
		}
		
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto dto = dao.fetch(growerContractYearId);
		if (dto != null) {
			dao.delete(growerContractYearId);
		}
	}


	@Test 
	public void testDeclaredYieldContractCommodityBerries() throws Exception {

		String declaredYieldContractCommodityBerriesGuid;
		String userId = "UNITTEST";

		createGrowerContractYear();
		createDeclaredYieldContract(userId);
		
		DeclaredYieldContractCommodityBerriesDao dao = persistenceSpringConfig.declaredYieldContractCommodityBerriesDao();

		// INSERT
		DeclaredYieldContractCommodityBerriesDto newDto = new DeclaredYieldContractCommodityBerriesDto();
		newDto.setCropCommodityId(10);
		newDto.setCropCommodityName("BLUEBERRY");
		newDto.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto.setTotalProduction(500.0);
		newDto.setTotalProductionOverride(250.0);
		newDto.setTotalPlantedAcres(125.0);
		newDto.setTotalMatureEquivalentAcres(100.1);


		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getDeclaredYieldContractCommodityBerriesGuid());
		declaredYieldContractCommodityBerriesGuid = newDto.getDeclaredYieldContractCommodityBerriesGuid();
		
		//SELECT
		List<DeclaredYieldContractCommodityBerriesDto> dtos = dao.selectForDeclaredYieldContract(declaredYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		DeclaredYieldContractCommodityBerriesDto fetchedDto = dao.fetch(declaredYieldContractCommodityBerriesGuid);
		
		Assert.assertEquals("DeclaredYieldContractCommodityBerriesGuid", newDto.getDeclaredYieldContractCommodityBerriesGuid(), fetchedDto.getDeclaredYieldContractCommodityBerriesGuid());
		Assert.assertEquals("DeclaredYieldContractGuid", newDto.getDeclaredYieldContractGuid(), fetchedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("TotalProduction", newDto.getTotalProduction(), fetchedDto.getTotalProduction());
		Assert.assertEquals("TotalProductionOverride", newDto.getTotalProductionOverride(), fetchedDto.getTotalProductionOverride());
		Assert.assertEquals("TotalPlantedAcres", newDto.getTotalPlantedAcres(), fetchedDto.getTotalPlantedAcres());
		Assert.assertEquals("TotalMatureEquivalentAcres", newDto.getTotalMatureEquivalentAcres(), fetchedDto.getTotalMatureEquivalentAcres());

		//UPDATE
		fetchedDto.setTotalProduction(700.0);
		fetchedDto.setTotalProductionOverride(300.0);
		fetchedDto.setTotalPlantedAcres(250.0);
		fetchedDto.setTotalMatureEquivalentAcres(210.1);
		
		dao.update(fetchedDto, userId);

		//FETCH
		DeclaredYieldContractCommodityBerriesDto updatedDto = dao.fetch(declaredYieldContractCommodityBerriesGuid);

		Assert.assertEquals("TotalProduction", fetchedDto.getTotalProduction(), updatedDto.getTotalProduction());
		Assert.assertEquals("TotalProductionOverride", fetchedDto.getTotalProductionOverride(), updatedDto.getTotalProductionOverride());
		Assert.assertEquals("TotalPlantedAcres", fetchedDto.getTotalPlantedAcres(), updatedDto.getTotalPlantedAcres());
		Assert.assertEquals("TotalMatureEquivalentAcres", fetchedDto.getTotalMatureEquivalentAcres(), updatedDto.getTotalMatureEquivalentAcres());


		//INSERT second commodity
		DeclaredYieldContractCommodityBerriesDto newDto2 = new DeclaredYieldContractCommodityBerriesDto();
		newDto2.setCropCommodityId(12);
		newDto2.setCropCommodityName("RASPBERRY");
		newDto2.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto2.setTotalProduction(123.4567);
		newDto2.setTotalProductionOverride(987.6543);
		newDto2.setTotalPlantedAcres(125.1234);
		newDto2.setTotalMatureEquivalentAcres(100.9876);

		dao.insert(newDto2, userId);

		//SELECT
		dtos = dao.selectForDeclaredYieldContract(declaredYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		
		//DELETE
		dao.delete(newDto2.getDeclaredYieldContractCommodityBerriesGuid());

		//FETCH
		DeclaredYieldContractCommodityBerriesDto deletedDto = dao.fetch(newDto2.getDeclaredYieldContractCommodityBerriesGuid());
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = dao.selectForDeclaredYieldContract(declaredYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		//DELETE for DeclaredYieldContract
		dao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
		
		//FETCH
		deletedDto = dao.fetch(declaredYieldContractCommodityBerriesGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = dao.selectForDeclaredYieldContract(declaredYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		//DELETE Declared Yield Contract
		DeclaredYieldContractDao dopContractDao = persistenceSpringConfig.declaredYieldContractDao();
		dopContractDao.delete(declaredYieldContractGuid);
	}

	private void createDeclaredYieldContract(String userId) throws DaoException {

		// Create parent Declared Yield Contract.		
		DeclaredYieldContractDao dao = persistenceSpringConfig.declaredYieldContractDao();
		
		DeclaredYieldContractDto newDto = new DeclaredYieldContractDto();

		newDto.setContractId(contractId);
		newDto.setCropYear(cropYear);
		newDto.setDeclarationOfProductionDate(null);
		newDto.setDefaultYieldMeasUnitTypeCode("LB");
//		newDto.setDopUpdateTimestamp(dopDate);
//		newDto.setDopUpdateUser("JSMITH");
		newDto.setEnteredYieldMeasUnitTypeCode("LB");
		newDto.setGrainFromOtherSourceInd(false);
		newDto.setBalerWagonInfo(null);
		newDto.setTotalLivestock(null);
		
		//INSERT
		dao.insert(newDto, userId);
		declaredYieldContractGuid = newDto.getDeclaredYieldContractGuid();
	}

	
	private void createGrowerContractYear() throws DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto newDto = new GrowerContractYearDto();
		
		Integer growerId = 525593;
		Integer insurancePlanId = 3;

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		
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
	 
}
