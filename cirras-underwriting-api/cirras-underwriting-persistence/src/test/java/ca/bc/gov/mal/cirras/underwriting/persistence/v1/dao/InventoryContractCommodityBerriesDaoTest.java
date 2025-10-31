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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class InventoryContractCommodityBerriesDaoTest {
	
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
			InventoryContractDao invContractDao = persistenceSpringConfig.inventoryContractDao();
			InventoryContractCommodityBerriesDao invContractCommodityBerriesDao = persistenceSpringConfig.inventoryContractCommodityBerriesDao();
			List<InventoryContractDto> invContractDtos = invContractDao.select(dto.getContractId(), dto.getCropYear());
			if(invContractDtos != null) {
				for (InventoryContractDto inventoryContractDto : invContractDtos) {
					invContractCommodityBerriesDao.deleteForInventoryContract(inventoryContractDto.getInventoryContractGuid());
					invContractDao.delete(inventoryContractDto.getInventoryContractGuid());
				}
			}
			dao.delete(growerContractYearId);
		}
	}


	@Test 
	public void testInventoryContractCommodityBerries() throws Exception {

		String inventoryContractCommodityBerriesGuid;
		String userId = "UNITTEST";

		createGrowerContractYear();
		createInventoryContract(userId);

		InventoryContractCommodityBerriesDao invContractCommodityBerriesDao = persistenceSpringConfig.inventoryContractCommodityBerriesDao();

		// INSERT
		InventoryContractCommodityBerriesDto newDto = new InventoryContractCommodityBerriesDto();
		newDto.setInventoryContractGuid(inventoryContractGuid);
		newDto.setCropCommodityId(10);
		newDto.setCropCommodityName("BLUEBERRY");
		newDto.setTotalInsuredPlants(5000);
		newDto.setTotalUninsuredPlants(200);
		newDto.setTotalQuantityInsuredAcres((double)1000);
		newDto.setTotalQuantityUninsuredAcres((double)100);
		newDto.setTotalPlantInsuredAcres((double)2000);
		newDto.setTotalPlantUninsuredAcres((double)200);

		invContractCommodityBerriesDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventoryContractCommodityBerriesGuid());
		inventoryContractCommodityBerriesGuid = newDto.getInventoryContractCommodityBerriesGuid();
		
		//SELECT
		List<InventoryContractCommodityBerriesDto> dtos = invContractCommodityBerriesDao.select(inventoryContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		InventoryContractCommodityBerriesDto fetchedDto = invContractCommodityBerriesDao.fetch(inventoryContractCommodityBerriesGuid);
		
		Assert.assertEquals("InventoryContractCommodityBerriesGuid", newDto.getInventoryContractCommodityBerriesGuid(), fetchedDto.getInventoryContractCommodityBerriesGuid());
		Assert.assertEquals("InventoryContractGuid", newDto.getInventoryContractGuid(), fetchedDto.getInventoryContractGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("TotalInsuredPlants", newDto.getTotalInsuredPlants(), fetchedDto.getTotalInsuredPlants());
		Assert.assertEquals("TotalUninsuredPlants", newDto.getTotalUninsuredPlants(), fetchedDto.getTotalUninsuredPlants());
		Assert.assertEquals("TotalQuantityInsuredAcres", newDto.getTotalQuantityInsuredAcres(), fetchedDto.getTotalQuantityInsuredAcres());
		Assert.assertEquals("TotalQuantityUninsuredAcres", newDto.getTotalQuantityUninsuredAcres(), fetchedDto.getTotalQuantityUninsuredAcres());
		Assert.assertEquals("TotalPlantInsuredAcres", newDto.getTotalPlantInsuredAcres(), fetchedDto.getTotalPlantInsuredAcres());
		Assert.assertEquals("TotalPlantUninsuredAcres", newDto.getTotalPlantUninsuredAcres(), fetchedDto.getTotalPlantUninsuredAcres());

		//UPDATE
		fetchedDto.setTotalInsuredPlants(5001);
		fetchedDto.setTotalUninsuredPlants(201);
		fetchedDto.setTotalQuantityInsuredAcres((double)1001);
		fetchedDto.setTotalQuantityUninsuredAcres((double)101);
		fetchedDto.setTotalPlantInsuredAcres((double)2001);
		fetchedDto.setTotalPlantUninsuredAcres((double)201);
		
		invContractCommodityBerriesDao.update(fetchedDto, userId);

		//FETCH
		InventoryContractCommodityBerriesDto updatedDto = invContractCommodityBerriesDao.fetch(inventoryContractCommodityBerriesGuid);

		Assert.assertEquals("InventoryContractCommodityBerriesGuid", fetchedDto.getInventoryContractCommodityBerriesGuid(), updatedDto.getInventoryContractCommodityBerriesGuid());
		Assert.assertEquals("InventoryContractGuid", fetchedDto.getInventoryContractGuid(), updatedDto.getInventoryContractGuid());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("TotalInsuredPlants", fetchedDto.getTotalInsuredPlants(), updatedDto.getTotalInsuredPlants());
		Assert.assertEquals("TotalUninsuredPlants", fetchedDto.getTotalUninsuredPlants(), updatedDto.getTotalUninsuredPlants());
		Assert.assertEquals("TotalQuantityInsuredAcres", fetchedDto.getTotalQuantityInsuredAcres(), updatedDto.getTotalQuantityInsuredAcres());
		Assert.assertEquals("TotalQuantityUninsuredAcres", fetchedDto.getTotalQuantityUninsuredAcres(), updatedDto.getTotalQuantityUninsuredAcres());
		Assert.assertEquals("TotalPlantInsuredAcres", fetchedDto.getTotalPlantInsuredAcres(), updatedDto.getTotalPlantInsuredAcres());
		Assert.assertEquals("TotalPlantUninsuredAcres", fetchedDto.getTotalPlantUninsuredAcres(), updatedDto.getTotalPlantUninsuredAcres());

		//INSERT second commodity
		InventoryContractCommodityBerriesDto newDto2 = new InventoryContractCommodityBerriesDto();
		newDto2.setInventoryContractGuid(inventoryContractGuid);
		newDto2.setCropCommodityId(13);
		newDto2.setCropCommodityName("STRAWBERRY");
		newDto.setTotalQuantityInsuredAcres((double)1010);
		newDto.setTotalQuantityUninsuredAcres((double)110);
		newDto.setTotalPlantInsuredAcres((double)2010);
		newDto.setTotalPlantUninsuredAcres((double)210);

		invContractCommodityBerriesDao.insert(newDto2, userId);

		//SELECT
		dtos = invContractCommodityBerriesDao.select(inventoryContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		//DELETE
		invContractCommodityBerriesDao.delete(newDto2.getInventoryContractCommodityBerriesGuid());
		invContractCommodityBerriesDao.delete(inventoryContractCommodityBerriesGuid);
		
		//FETCH
		InventoryContractCommodityBerriesDto deletedDto = invContractCommodityBerriesDao.fetch(inventoryContractCommodityBerriesGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = invContractCommodityBerriesDao.select(inventoryContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
		
		//DELETE InventoryContract
		InventoryContractDao invContractDao = persistenceSpringConfig.inventoryContractDao();
		invContractDao.delete(inventoryContractGuid);

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
