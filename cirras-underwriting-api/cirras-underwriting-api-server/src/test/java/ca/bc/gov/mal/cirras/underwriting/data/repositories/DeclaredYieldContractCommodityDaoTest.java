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

import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractDao;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class DeclaredYieldContractCommodityDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer growerContractYearId = 99999999;
	private Integer contractId = 888888888;
	private Integer cropYear = 2020;
	private String declaredYieldContractGuid;
	private String inventoryContractGuid;
	private Double totalInsuredAcres = (double)1234;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{
		
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto dto = dao.fetch(growerContractYearId);
		if (dto != null) {
			dao.delete(growerContractYearId);
		}
	}


	@Test 
	public void testDeclaredYieldContractCommodity() throws Exception {

		String declaredYieldContractCommodityGuid;
		String userId = "UNITTEST";

		createGrowerContractYear();
		createDeclaredYieldContract(userId);
		createInventoryContract(userId);
		createInventoryContractCommodity(userId);
		
		DeclaredYieldContractCommodityDao dao = persistenceSpringConfig.declaredYieldContractCommodityDao();

		// INSERT
		DeclaredYieldContractCommodityDto newDto = new DeclaredYieldContractCommodityDto();
		newDto.setCropCommodityId(16);
		newDto.setCropCommodityName("BARLEY");
		newDto.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto.setIsPedigreeInd(true);
		newDto.setHarvestedAcres((double)1000);
		newDto.setStoredYield((double)500);
		newDto.setStoredYieldDefaultUnit((double)400);
		newDto.setSoldYield((double)250);
		newDto.setSoldYieldDefaultUnit((double)200);
		newDto.setGradeModifierTypeCode("BLY1");
		newDto.setTotalInsuredAcres(totalInsuredAcres);


		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getDeclaredYieldContractCommodityGuid());
		declaredYieldContractCommodityGuid = newDto.getDeclaredYieldContractCommodityGuid();
		
		//SELECT
		List<DeclaredYieldContractCommodityDto> dtos = dao.selectForDeclaredYieldContract(declaredYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		DeclaredYieldContractCommodityDto fetchedDto = dao.fetch(declaredYieldContractCommodityGuid);
		
		Assert.assertEquals("DeclaredYieldContractCommodityGuid", newDto.getDeclaredYieldContractCommodityGuid(), fetchedDto.getDeclaredYieldContractCommodityGuid());
		Assert.assertEquals("DeclaredYieldContractGuid", newDto.getDeclaredYieldContractGuid(), fetchedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("HarvestedAcres", newDto.getHarvestedAcres(), fetchedDto.getHarvestedAcres());
		Assert.assertEquals("StoredYield", newDto.getStoredYield(), fetchedDto.getStoredYield());
		Assert.assertEquals("StoredYieldDefaultUnit", newDto.getStoredYieldDefaultUnit(), fetchedDto.getStoredYieldDefaultUnit());
		Assert.assertEquals("SoldYield", newDto.getSoldYield(), fetchedDto.getSoldYield());
		Assert.assertEquals("SoldYieldDefaultUnit", newDto.getSoldYieldDefaultUnit(), fetchedDto.getSoldYieldDefaultUnit());
		Assert.assertEquals("GradeModifierTypeCode", newDto.getGradeModifierTypeCode(), fetchedDto.getGradeModifierTypeCode());
		Assert.assertEquals("TotalInsuredAcres", newDto.getTotalInsuredAcres(), fetchedDto.getTotalInsuredAcres());

		//UPDATE
		fetchedDto.setHarvestedAcres((double)2000);
		fetchedDto.setStoredYield((double)700);
		fetchedDto.setStoredYieldDefaultUnit((double)500);
		fetchedDto.setSoldYield((double)300);
		fetchedDto.setSoldYieldDefaultUnit((double)230);
		fetchedDto.setGradeModifierTypeCode("OAT3");
		
		dao.update(fetchedDto, userId);

		//FETCH
		DeclaredYieldContractCommodityDto updatedDto = dao.fetch(declaredYieldContractCommodityGuid);

		Assert.assertEquals("HarvestedAcres", fetchedDto.getHarvestedAcres(), updatedDto.getHarvestedAcres());
		Assert.assertEquals("StoredYield", fetchedDto.getStoredYield(), updatedDto.getStoredYield());
		Assert.assertEquals("StoredYieldDefaultUnit", fetchedDto.getStoredYieldDefaultUnit(), updatedDto.getStoredYieldDefaultUnit());
		Assert.assertEquals("SoldYield", fetchedDto.getSoldYield(), updatedDto.getSoldYield());
		Assert.assertEquals("SoldYieldDefaultUnit", fetchedDto.getSoldYieldDefaultUnit(), updatedDto.getSoldYieldDefaultUnit());
		Assert.assertEquals("GradeModifierTypeCode", fetchedDto.getGradeModifierTypeCode(), updatedDto.getGradeModifierTypeCode());


		//INSERT second commodity
		DeclaredYieldContractCommodityDto newDto2 = new DeclaredYieldContractCommodityDto();
		newDto2.setCropCommodityId(16);
		newDto2.setCropCommodityName("BARLEY");
		newDto2.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto2.setIsPedigreeInd(false);
		newDto2.setHarvestedAcres((double)1000);
		newDto2.setStoredYield((double)500);
		newDto2.setStoredYieldDefaultUnit((double)400);
		newDto2.setSoldYield((double)250);
		newDto2.setSoldYieldDefaultUnit((double)200);
		newDto2.setGradeModifierTypeCode("BLY1");
		newDto2.setTotalInsuredAcres(totalInsuredAcres);

		dao.insert(newDto2, userId);

		//SELECT
		dtos = dao.selectForDeclaredYieldContract(declaredYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		dtos = dao.selectToRecalculate(16, "BUSHEL", cropYear, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		
		//DELETE
		dao.delete(newDto2.getDeclaredYieldContractCommodityGuid());
		dao.delete(declaredYieldContractCommodityGuid);
		
		//FETCH
		DeclaredYieldContractCommodityDto deletedDto = dao.fetch(declaredYieldContractCommodityGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = dao.selectForDeclaredYieldContract(declaredYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		//DELETE Declared Yield Contract
		DeclaredYieldContractDao dopContractDao = persistenceSpringConfig.declaredYieldContractDao();
		dopContractDao.delete(declaredYieldContractGuid);

		//DELETE InventoryContractCommodity
		InventoryContractCommodityDao invContractCmdtyDao = persistenceSpringConfig.inventoryContractCommodityDao();
		invContractCmdtyDao.deleteForInventoryContract(inventoryContractGuid);
		
		//DELETE InventoryContract
		InventoryContractDao invContractDao = persistenceSpringConfig.inventoryContractDao();
		invContractDao.delete(inventoryContractGuid);

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
//		newDto.setDopUpdateTimestamp(dopDate);
//		newDto.setDopUpdateUser("JSMITH");
		newDto.setEnteredYieldMeasUnitTypeCode("BUSHEL");
		newDto.setGrainFromOtherSourceInd(true);
		newDto.setBalerWagonInfo(null);
		newDto.setTotalLivestock(null);
		
		//INSERT
		dao.insert(newDto, userId);
		declaredYieldContractGuid = newDto.getDeclaredYieldContractGuid();
		
	}
	
	private void createInventoryContract(String userId) throws DaoException {

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		InventoryContractDao invContractDao = persistenceSpringConfig.inventoryContractDao();

		// Create parent InventoryContract.
		InventoryContractDto invContractDto = new InventoryContractDto();

		invContractDto.setContractId(contractId);
		invContractDto.setCropYear(cropYear);
		invContractDto.setFertilizerInd(false);
		invContractDto.setGrainFromPrevYearInd(true);
		invContractDto.setHerbicideInd(true);
		invContractDto.setOtherChangesComment("Other changes comment");
		invContractDto.setOtherChangesInd(true);
		invContractDto.setSeededCropReportSubmittedInd(false);
		invContractDto.setTilliageInd(false);
		invContractDto.setUnseededIntentionsSubmittedInd(false);
		invContractDto.setInvUpdateTimestamp(date);
		invContractDto.setInvUpdateUser(userId);

		invContractDao.insert(invContractDto, userId);
		inventoryContractGuid = invContractDto.getInventoryContractGuid();
	}
	
	private void createInventoryContractCommodity(String userId) throws DaoException {
		
		InventoryContractCommodityDao invContractCommodityDao = persistenceSpringConfig.inventoryContractCommodityDao();

		// INSERT
		InventoryContractCommodityDto newDto = new InventoryContractCommodityDto();
		newDto.setCropCommodityId(16);
		newDto.setCropCommodityName("BARLEY");
		newDto.setInventoryContractGuid(inventoryContractGuid);
		newDto.setTotalSeededAcres(totalInsuredAcres);
		newDto.setTotalSpotLossAcres(87.65);
		newDto.setTotalUnseededAcres(12.34);
		newDto.setTotalUnseededAcresOverride(56.78);
		newDto.setIsPedigreeInd(true);

		invContractCommodityDao.insert(newDto, userId);

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
