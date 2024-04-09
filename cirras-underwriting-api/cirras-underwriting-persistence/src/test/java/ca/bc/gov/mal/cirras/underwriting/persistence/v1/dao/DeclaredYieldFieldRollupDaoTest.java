package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class DeclaredYieldFieldRollupDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	private String declaredYieldContractGuid;
	private Integer cropYear = 2020;
	
	@Test
	public void testDeclaredYieldFieldRollup() throws Exception {

		String userId = "UNITTEST";
		
		//Create a dop contract
		createDeclaredYieldContract(userId);

		DeclaredYieldFieldRollupDao dao = persistenceSpringConfig.declaredYieldFieldRollupDao();
		
		DeclaredYieldFieldRollupDto newDto = new DeclaredYieldFieldRollupDto();

		newDto.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto.setCropCommodityId(24);
		newDto.setCropCommodityName("OAT"); //For tests
		newDto.setIsPedigreeInd(true);
		newDto.setEstimatedYieldPerAcreTonnes(1000.5);
		newDto.setEstimatedYieldPerAcreBushels(700.3);

		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getDeclaredYieldFieldRollupGuid());

		String declaredYieldFieldRollupGuid = newDto.getDeclaredYieldFieldRollupGuid();
		
		//FETCH
		DeclaredYieldFieldRollupDto fetchedDto = dao.fetch(declaredYieldFieldRollupGuid);
		Assert.assertEquals("DeclaredYieldFieldRollupGuid", newDto.getDeclaredYieldFieldRollupGuid(), fetchedDto.getDeclaredYieldFieldRollupGuid());
		Assert.assertEquals("DeclaredYieldContractGuid", newDto.getDeclaredYieldContractGuid(), fetchedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("EstimatedYieldPerAcreTonnes", newDto.getEstimatedYieldPerAcreTonnes(), fetchedDto.getEstimatedYieldPerAcreTonnes());
		Assert.assertEquals("EstimatedYieldPerAcreBushels", newDto.getEstimatedYieldPerAcreBushels(), fetchedDto.getEstimatedYieldPerAcreBushels());
		
		
		//UPDATE
		fetchedDto.setCropCommodityId(26);
		fetchedDto.setCropCommodityName("WHEAT");
		fetchedDto.setIsPedigreeInd(false);
		fetchedDto.setEstimatedYieldPerAcreTonnes(505.5);
		fetchedDto.setEstimatedYieldPerAcreBushels(361.3);

		dao.update(fetchedDto, userId);

		//FETCH
		DeclaredYieldFieldRollupDto updatedDto = dao.fetch(declaredYieldFieldRollupGuid);

		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", fetchedDto.getCropCommodityName(), updatedDto.getCropCommodityName());
		Assert.assertEquals("IsPedigreeInd", fetchedDto.getIsPedigreeInd(), updatedDto.getIsPedigreeInd());
		Assert.assertEquals("EstimatedYieldPerAcreTonnes", fetchedDto.getEstimatedYieldPerAcreTonnes(), updatedDto.getEstimatedYieldPerAcreTonnes());
		Assert.assertEquals("EstimatedYieldPerAcreBushels", fetchedDto.getEstimatedYieldPerAcreBushels(), updatedDto.getEstimatedYieldPerAcreBushels());

		//DELETE
		dao.delete(declaredYieldFieldRollupGuid);
		
		//FETCH
		DeclaredYieldFieldRollupDto deletedDto = dao.fetch(declaredYieldFieldRollupGuid);
		Assert.assertNull(deletedDto);

		//DELETE declared yield contract
		DeclaredYieldContractDao dopContractDao = persistenceSpringConfig.declaredYieldContractDao();
		dopContractDao.delete(declaredYieldContractGuid);

	}
	
	@Test
	public void testDeclaredYieldFieldRollupByContract() throws Exception {

		String userId = "UNITTEST";
		
		//Create a dop contract
		createDeclaredYieldContract(userId);

		DeclaredYieldFieldRollupDao dao = persistenceSpringConfig.declaredYieldFieldRollupDao();
		
		DeclaredYieldFieldRollupDto newDto = new DeclaredYieldFieldRollupDto();

		newDto.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto.setCropCommodityId(24);
		newDto.setCropCommodityName("OAT"); //For tests
		newDto.setIsPedigreeInd(true);
		newDto.setEstimatedYieldPerAcreTonnes(1000.5);
		newDto.setEstimatedYieldPerAcreBushels(700.3);

		//INSERT First
		dao.insert(newDto, userId);
		
		newDto = new DeclaredYieldFieldRollupDto();

		newDto.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto.setCropCommodityId(26);
		newDto.setCropCommodityName("WHEAT"); //For tests
		newDto.setIsPedigreeInd(false);
		newDto.setEstimatedYieldPerAcreTonnes(2000.5);
		newDto.setEstimatedYieldPerAcreBushels(1400.3);

		//INSERT Second
		dao.insert(newDto, userId);
		
		//FETCH By Yield Contract
		List<DeclaredYieldFieldRollupDto> dtos = dao.selectForDeclaredYieldContract(declaredYieldContractGuid);

		//Check if the declared yield contract is the same
		for (DeclaredYieldFieldRollupDto dto : dtos) {
			Assert.assertEquals("DeclaredYieldContractGuid", declaredYieldContractGuid, dto.getDeclaredYieldContractGuid());
		}
		
        dtos = dao.selectToRecalculate(24, cropYear, cropYear);
        Assert.assertNotNull(dtos);
        Assert.assertEquals(1, dtos.size());
        Assert.assertEquals(24, dtos.get(0).getCropCommodityId().intValue());

		//DELETE
		dao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
		
		//FETCH
		dtos = dao.selectForDeclaredYieldContract(declaredYieldContractGuid);
		Assert.assertEquals(0, dtos.size());

		//DELETE declared yield contract
		DeclaredYieldContractDao dopContractDao = persistenceSpringConfig.declaredYieldContractDao();
		dopContractDao.delete(declaredYieldContractGuid);

	}
	
	private void createDeclaredYieldContract(String userId) throws DaoException {
		
		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();

		Integer contractId = 2667;

		DeclaredYieldContractDao dao = persistenceSpringConfig.declaredYieldContractDao();
		
		DeclaredYieldContractDto newDto = new DeclaredYieldContractDto();
		

		newDto.setContractId(contractId);
		newDto.setCropYear(cropYear);
		newDto.setDeclarationOfProductionDate(dopDate);
		newDto.setDefaultYieldMeasUnitTypeCode("TONNE");
		newDto.setEnteredYieldMeasUnitTypeCode("BUSHEL");
		newDto.setGrainFromOtherSourceInd(true);
		newDto.setBalerWagonInfo(null);
		newDto.setTotalLivestock(null);
		
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getDeclaredYieldContractGuid());

		declaredYieldContractGuid = newDto.getDeclaredYieldContractGuid();

	}
	
}
