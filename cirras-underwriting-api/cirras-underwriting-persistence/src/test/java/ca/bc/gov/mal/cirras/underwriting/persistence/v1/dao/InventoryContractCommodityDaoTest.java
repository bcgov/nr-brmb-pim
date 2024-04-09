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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class InventoryContractCommodityDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer growerContractYearId = 99999999;
	private Integer contractId = 888888888;
	private Integer cropYear = 2024;
	private String inventoryContractGuid;



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
	public void testInventoryContractCommodity() throws Exception {

		String inventoryContractCommodityGuid;
		String userId = "UNITTEST";

		createGrowerContractYear();
		createInventoryContract(userId);

		InventoryContractCommodityDao invContractCommodityDao = persistenceSpringConfig.inventoryContractCommodityDao();

		// INSERT
		InventoryContractCommodityDto newDto = new InventoryContractCommodityDto();
		newDto.setCropCommodityId(16);
		newDto.setCropCommodityName("BARLEY");
		newDto.setInventoryContractGuid(inventoryContractGuid);
		newDto.setTotalSeededAcres(43.21);
		newDto.setTotalSpotLossAcres(87.65);
		newDto.setTotalUnseededAcres(12.34);
		newDto.setTotalUnseededAcresOverride(56.78);
		newDto.setIsPedigreeInd(true);


		invContractCommodityDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventoryContractCommodityGuid());
		inventoryContractCommodityGuid = newDto.getInventoryContractCommodityGuid();
		
		//SELECT
		List<InventoryContractCommodityDto> dtos = invContractCommodityDao.select(inventoryContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		InventoryContractCommodityDto fetchedDto = invContractCommodityDao.fetch(inventoryContractCommodityGuid);
		
		Assert.assertEquals("InventoryContractCommodityGuid", newDto.getInventoryContractCommodityGuid(), fetchedDto.getInventoryContractCommodityGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("InventoryContractGuid", newDto.getInventoryContractGuid(), fetchedDto.getInventoryContractGuid());
		Assert.assertEquals("TotalSeededAcres", newDto.getTotalSeededAcres(), fetchedDto.getTotalSeededAcres());
		Assert.assertEquals("TotalSpotLossAcres", newDto.getTotalSpotLossAcres(), fetchedDto.getTotalSpotLossAcres());
		Assert.assertEquals("TotalUnseededAcres", newDto.getTotalUnseededAcres(), fetchedDto.getTotalUnseededAcres());
		Assert.assertEquals("TotalUnseededAcresOverride", newDto.getTotalUnseededAcresOverride(), fetchedDto.getTotalUnseededAcresOverride());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());

		//UPDATE
		fetchedDto.setCropCommodityId(18);
		fetchedDto.setCropCommodityName("CANOLA");
		fetchedDto.setInventoryContractGuid(inventoryContractGuid);
		fetchedDto.setTotalSeededAcres(11.11);
		fetchedDto.setTotalSpotLossAcres(22.22);
		fetchedDto.setTotalUnseededAcres(33.33);
		fetchedDto.setTotalUnseededAcresOverride(44.44);
		fetchedDto.setIsPedigreeInd(false);
		
		invContractCommodityDao.update(fetchedDto, userId);

		//FETCH
		InventoryContractCommodityDto updatedDto = invContractCommodityDao.fetch(inventoryContractCommodityGuid);

		Assert.assertEquals("InventoryContractCommodityGuid", fetchedDto.getInventoryContractCommodityGuid(), updatedDto.getInventoryContractCommodityGuid());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", fetchedDto.getCropCommodityName(), updatedDto.getCropCommodityName());
		Assert.assertEquals("InventoryContractGuid", fetchedDto.getInventoryContractGuid(), updatedDto.getInventoryContractGuid());
		Assert.assertEquals("TotalSeededAcres", fetchedDto.getTotalSeededAcres(), updatedDto.getTotalSeededAcres());
		Assert.assertEquals("TotalSpotLossAcres", fetchedDto.getTotalSpotLossAcres(), updatedDto.getTotalSpotLossAcres());
		Assert.assertEquals("TotalUnseededAcres", fetchedDto.getTotalUnseededAcres(), updatedDto.getTotalUnseededAcres());
		Assert.assertEquals("TotalUnseededAcresOverride", fetchedDto.getTotalUnseededAcresOverride(), updatedDto.getTotalUnseededAcresOverride());
		Assert.assertEquals("IsPedigreeInd", fetchedDto.getIsPedigreeInd(), updatedDto.getIsPedigreeInd());

		//INSERT second commodity
		InventoryContractCommodityDto newDto2 = new InventoryContractCommodityDto();
		newDto2.setCropCommodityId(null);
		newDto2.setCropCommodityName(null);
		newDto2.setInventoryContractGuid(inventoryContractGuid);
		newDto2.setTotalSeededAcres(77.77);
		newDto2.setTotalSpotLossAcres(88.88);
		newDto2.setTotalUnseededAcres(null);
		newDto2.setTotalUnseededAcresOverride(null);
		newDto2.setIsPedigreeInd(true);

		invContractCommodityDao.insert(newDto2, userId);

		//SELECT
		dtos = invContractCommodityDao.select(inventoryContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		//DELETE
		invContractCommodityDao.delete(newDto2.getInventoryContractCommodityGuid());
		invContractCommodityDao.delete(inventoryContractCommodityGuid);
		
		//FETCH
		InventoryContractCommodityDto deletedDto = invContractCommodityDao.fetch(inventoryContractCommodityGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = invContractCommodityDao.select(inventoryContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
		
		//DELETE InventoryContract
		InventoryContractDao invContractDao = persistenceSpringConfig.inventoryContractDao();
		invContractDao.delete(inventoryContractGuid);

	}
	
	@Test 
	public void testSelectForDopContract() throws Exception {

		String userId = "UNITTEST";

		// INSERT
		createGrowerContractYear();
		createInventoryContract(userId);

		Double totalSeededAcresBarley = (double)500;
		Double totalSeededAcresCanola = (double)150;
		Double totalSeededAcresOat = (double)321;
		createInventoryContractCommodity(16, "BARLEY", totalSeededAcresBarley, true, userId);
		createInventoryContractCommodity(18, "CANOLA", totalSeededAcresCanola, false, userId);
		createInventoryContractCommodity(24, "OAT", totalSeededAcresOat, false, userId);
		
		InventoryContractCommodityDao invContractCommodityDao = persistenceSpringConfig.inventoryContractCommodityDao();

		//SELECT
		List<InventoryContractCommodityDto> dtos = invContractCommodityDao.selectForDopContract(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(3, dtos.size());
		
		for (InventoryContractCommodityDto dto : dtos) {
			switch (dto.getCropCommodityId()) {
			case 16:
				Assert.assertEquals("Commodity Name", "BARLEY", dto.getCropCommodityName());
				Assert.assertEquals("Pedigree", true, dto.getIsPedigreeInd());
				Assert.assertEquals("Total Seeded Acres", totalSeededAcresBarley, dto.getTotalSeededAcres());
				break;
			case 18:
				Assert.assertEquals("Commodity Name", "CANOLA", dto.getCropCommodityName());
				Assert.assertEquals("Pedigree", false, dto.getIsPedigreeInd());
				Assert.assertEquals("Total Seeded Acres", totalSeededAcresCanola, dto.getTotalSeededAcres());
				break;
			case 24:
				Assert.assertEquals("Commodity Name", "OAT", dto.getCropCommodityName());
				Assert.assertEquals("Pedigree", false, dto.getIsPedigreeInd());
				Assert.assertEquals("Total Seeded Acres", totalSeededAcresOat, dto.getTotalSeededAcres());
				break;
			default:
				break;
			}
		}
		
		//DELETE inventory contract commodities
		invContractCommodityDao.deleteForInventoryContract(inventoryContractGuid);
		
		//SELECT
		dtos = invContractCommodityDao.selectForDopContract(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
		
		//DELETE InventoryContract
		InventoryContractDao invContractDao = persistenceSpringConfig.inventoryContractDao();
		invContractDao.delete(inventoryContractGuid);

	}

	private void createInventoryContractCommodity(
			Integer cropCommodityId,
			String cropCommodityName,
			Double totalSeededAcres,
			Boolean isPedigreedInd,
			String userId) throws DaoException {
		InventoryContractCommodityDao invContractCommodityDao = persistenceSpringConfig.inventoryContractCommodityDao();
	
		// INSERT
		InventoryContractCommodityDto newDto = new InventoryContractCommodityDto();
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropCommodityName(cropCommodityName);
		newDto.setInventoryContractGuid(inventoryContractGuid);
		newDto.setTotalSeededAcres(totalSeededAcres);
		newDto.setTotalSpotLossAcres(null);
		newDto.setTotalUnseededAcres(null);
		newDto.setTotalUnseededAcresOverride(null);
		newDto.setIsPedigreeInd(isPedigreedInd);
	
		invContractCommodityDao.insert(newDto, userId);
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
