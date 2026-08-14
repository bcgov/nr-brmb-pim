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

import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryCoverageTotalForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractDao;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryCoverageTotalForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class InventoryCoverageTotalForageDaoTest {
	
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
	public void testInventoryCoverageTotalForage() throws Exception {

		String inventoryCoverageTotalForageGuid;
		String userId = "UNITTEST";

		createGrowerContractYear();
		createInventoryContract(userId);

		InventoryCoverageTotalForageDao dao = persistenceSpringConfig.inventoryCoverageTotalForageDao();

		// INSERT
		InventoryCoverageTotalForageDto newDto = new InventoryCoverageTotalForageDto();
		newDto.setCropCommodityId(71);
		newDto.setCropCommodityName("SILAGE CORN");
		newDto.setInventoryContractGuid(inventoryContractGuid);
		newDto.setPlantInsurabilityTypeCode(null);
		newDto.setPlantInsurabilityTypeDesc(null);
		newDto.setTotalFieldAcres(12.3456);
		newDto.setIsUnseededInsurableInd(true);

		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventoryCoverageTotalForageGuid());
		inventoryCoverageTotalForageGuid = newDto.getInventoryCoverageTotalForageGuid();
		
		//SELECT
		List<InventoryCoverageTotalForageDto> dtos = dao.select(inventoryContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		InventoryCoverageTotalForageDto fetchedDto = dao.fetch(inventoryCoverageTotalForageGuid);
		
		Assert.assertEquals("InventoryCoverageTotalForageGuid", newDto.getInventoryCoverageTotalForageGuid(), fetchedDto.getInventoryCoverageTotalForageGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("InventoryContractGuid", newDto.getInventoryContractGuid(), fetchedDto.getInventoryContractGuid());
		Assert.assertEquals("PlantInsurabilityTypeCode", newDto.getPlantInsurabilityTypeCode(), fetchedDto.getPlantInsurabilityTypeCode());
		Assert.assertEquals("PlantInsurabilityTypeDesc", newDto.getPlantInsurabilityTypeDesc(), fetchedDto.getPlantInsurabilityTypeDesc());
		Assert.assertEquals("TotalFieldAcres", newDto.getTotalFieldAcres(), fetchedDto.getTotalFieldAcres());
		Assert.assertEquals("IsUnseededInsurableInd", newDto.getIsUnseededInsurableInd(), fetchedDto.getIsUnseededInsurableInd());
		
		//UPDATE
		fetchedDto.setTotalFieldAcres(98.7654);
				
		dao.update(fetchedDto, userId);

		//FETCH
		InventoryCoverageTotalForageDto updatedDto = dao.fetch(inventoryCoverageTotalForageGuid);

		Assert.assertEquals("InventoryCoverageTotalForageGuid", fetchedDto.getInventoryCoverageTotalForageGuid(), updatedDto.getInventoryCoverageTotalForageGuid());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", fetchedDto.getCropCommodityName(), updatedDto.getCropCommodityName());
		Assert.assertEquals("InventoryContractGuid", fetchedDto.getInventoryContractGuid(), updatedDto.getInventoryContractGuid());
		Assert.assertEquals("PlantInsurabilityTypeCode", fetchedDto.getPlantInsurabilityTypeCode(), updatedDto.getPlantInsurabilityTypeCode());
		Assert.assertEquals("PlantInsurabilityTypeDesc", fetchedDto.getPlantInsurabilityTypeDesc(), updatedDto.getPlantInsurabilityTypeDesc());
		Assert.assertEquals("TotalFieldAcres", fetchedDto.getTotalFieldAcres(), updatedDto.getTotalFieldAcres());
		
		//INSERT second commodity
		InventoryCoverageTotalForageDto newDto2 = new InventoryCoverageTotalForageDto();
		newDto2.setCropCommodityId(null);
		newDto2.setCropCommodityName(null);
		newDto2.setInventoryContractGuid(inventoryContractGuid);
		newDto2.setPlantInsurabilityTypeCode("W2");
		newDto2.setPlantInsurabilityTypeDesc("Winter Survival 2");
		newDto2.setTotalFieldAcres(11.2233);
		newDto2.setIsUnseededInsurableInd(false);

		dao.insert(newDto2, userId);

		//FETCH
		InventoryCoverageTotalForageDto fetchedDto2 = dao.fetch(newDto2.getInventoryCoverageTotalForageGuid());
		
		Assert.assertEquals("InventoryCoverageTotalForageGuid", newDto2.getInventoryCoverageTotalForageGuid(), fetchedDto2.getInventoryCoverageTotalForageGuid());
		Assert.assertEquals("CropCommodityId", newDto2.getCropCommodityId(), fetchedDto2.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto2.getCropCommodityName(), fetchedDto2.getCropCommodityName());
		Assert.assertEquals("InventoryContractGuid", newDto2.getInventoryContractGuid(), fetchedDto2.getInventoryContractGuid());
		Assert.assertEquals("PlantInsurabilityTypeCode", newDto2.getPlantInsurabilityTypeCode(), fetchedDto2.getPlantInsurabilityTypeCode());
		Assert.assertEquals("PlantInsurabilityTypeDesc", newDto2.getPlantInsurabilityTypeDesc(), fetchedDto2.getPlantInsurabilityTypeDesc());
		Assert.assertEquals("TotalFieldAcres", newDto2.getTotalFieldAcres(), fetchedDto2.getTotalFieldAcres());
		Assert.assertEquals("IsUnseededInsurableInd", newDto2.getIsUnseededInsurableInd(), fetchedDto2.getIsUnseededInsurableInd());
		
		//SELECT
		dtos = dao.select(inventoryContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		//DELETE
		dao.delete(inventoryCoverageTotalForageGuid);
		
		//FETCH
		InventoryCoverageTotalForageDto deletedDto = dao.fetch(inventoryCoverageTotalForageGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = dao.select(inventoryContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		//DELETE by InventoryContract
		dao.deleteForInventoryContract(inventoryContractGuid);
		
		//SELECT
		dtos = dao.select(inventoryContractGuid);
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
		invContractDto.setGrainFromPrevYearInd(false);
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
		
		Integer growerId = null;
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
