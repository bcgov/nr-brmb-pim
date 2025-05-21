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
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class VerifiedYieldContractCommodityDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer growerContractYearId = 99999999;
	private Integer contractId = 888888888;
	private Integer cropYear = 2020;
	private String verifiedYieldContractGuid;
	private String declaredYieldContractGuid;
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

		// Delete VerifiedYieldContractCommodity
		VerifiedYieldContractCommodityDao vyccDao = persistenceSpringConfig.verifiedYieldContractCommodityDao();
		List<VerifiedYieldContractCommodityDto> vyccDtos = vyccDao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		if ( vyccDtos != null && !vyccDtos.isEmpty() ) {
			vyccDao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
		}
		
		// Delete VerifiedYieldContract
		VerifiedYieldContractDao vycDao = persistenceSpringConfig.verifiedYieldContractDao();
		VerifiedYieldContractDto vycDto = vycDao.fetch(verifiedYieldContractGuid);
		if (vycDto != null) {
			vycDao.delete(verifiedYieldContractGuid);
		}

		// Delete DeclaredYieldContract
		DeclaredYieldContractDao dycDao = persistenceSpringConfig.declaredYieldContractDao();
		DeclaredYieldContractDto dycDto = dycDao.fetch(declaredYieldContractGuid);
		if (dycDto != null) {
			dycDao.delete(declaredYieldContractGuid);
		}

		// Delete InventoryContractCommodities
		InventoryContractCommodityDao iccDao = persistenceSpringConfig.inventoryContractCommodityDao();
		iccDao.deleteForInventoryContract(inventoryContractGuid);
		
		// Delete InventoryContract
		InventoryContractDao icDao = persistenceSpringConfig.inventoryContractDao();
		InventoryContractDto icDto = icDao.fetch(inventoryContractGuid);
		if (icDto != null) {
			icDao.delete(inventoryContractGuid);
		}
		
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto dto = dao.fetch(growerContractYearId);
		if (dto != null) {
			dao.delete(growerContractYearId);
		}
	}


	@Test 
	public void testVerifiedYieldContractCommodity() throws Exception {

		String verifiedYieldContractCommodityGuid;
		String userId = "UNITTEST";

		createGrowerContractYear();
		createInventoryContract(userId);
		createInventoryContractCommodity(16, "BARLEY", false, 23.45, userId);
		createDeclaredYieldContract(userId);
		createVerifiedYieldContract(userId);
		
		VerifiedYieldContractCommodityDao dao = persistenceSpringConfig.verifiedYieldContractCommodityDao();

		// INSERT
		VerifiedYieldContractCommodityDto newDto = new VerifiedYieldContractCommodityDto();

		
		newDto.setCropCommodityId(16);
		newDto.setCropCommodityName("BARLEY");
		newDto.setCommodityTypeCode("Six Row");
		newDto.setCommodityTypeDescription("Six Row Standard");
		newDto.setHarvestedAcres(11.22);
		newDto.setHarvestedAcresOverride(33.44);
		newDto.setHarvestedYield(66.77);
		newDto.setHarvestedYieldOverride(88.99);
		newDto.setIsPedigreeInd(false);
		newDto.setProductionGuarantee(13.57);
		newDto.setSoldYieldDefaultUnit(99.88);
		newDto.setStoredYieldDefaultUnit(77.66);
		newDto.setTotalInsuredAcres(23.45);
		newDto.setVerifiedYieldContractCommodityGuid(null);
		newDto.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
		newDto.setYieldPerAcre(55.44);

		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getVerifiedYieldContractCommodityGuid());
		verifiedYieldContractCommodityGuid = newDto.getVerifiedYieldContractCommodityGuid();
		
		//SELECT
		List<VerifiedYieldContractCommodityDto> dtos = dao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		VerifiedYieldContractCommodityDto fetchedDto = dtos.get(0);

		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("CommodityTypeCode", newDto.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDescription", newDto.getCommodityTypeDescription(), fetchedDto.getCommodityTypeDescription());
		Assert.assertEquals("HarvestedAcres", newDto.getHarvestedAcres(), fetchedDto.getHarvestedAcres());
		Assert.assertEquals("HarvestedAcresOverride", newDto.getHarvestedAcresOverride(), fetchedDto.getHarvestedAcresOverride());
		Assert.assertEquals("HarvestedYield", newDto.getHarvestedYield(), fetchedDto.getHarvestedYield());
		Assert.assertEquals("HarvestedYieldOverride", newDto.getHarvestedYieldOverride(), fetchedDto.getHarvestedYieldOverride());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("ProductionGuarantee", newDto.getProductionGuarantee(), fetchedDto.getProductionGuarantee());
		Assert.assertEquals("SoldYieldDefaultUnit", newDto.getSoldYieldDefaultUnit(), fetchedDto.getSoldYieldDefaultUnit());
		Assert.assertEquals("StoredYieldDefaultUnit", newDto.getStoredYieldDefaultUnit(), fetchedDto.getStoredYieldDefaultUnit());
		Assert.assertEquals("TotalInsuredAcres", newDto.getTotalInsuredAcres(), fetchedDto.getTotalInsuredAcres());
		Assert.assertEquals("VerifiedYieldContractCommodityGuid", newDto.getVerifiedYieldContractCommodityGuid(), fetchedDto.getVerifiedYieldContractCommodityGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", newDto.getVerifiedYieldContractGuid(), fetchedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("YieldPerAcre", newDto.getYieldPerAcre(), fetchedDto.getYieldPerAcre());
		
		//FETCH
		fetchedDto = dao.fetch(verifiedYieldContractCommodityGuid);
		
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("CommodityTypeCode", newDto.getCommodityTypeCode(), fetchedDto.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDescription", newDto.getCommodityTypeDescription(), fetchedDto.getCommodityTypeDescription());
		Assert.assertEquals("HarvestedAcres", newDto.getHarvestedAcres(), fetchedDto.getHarvestedAcres());
		Assert.assertEquals("HarvestedAcresOverride", newDto.getHarvestedAcresOverride(), fetchedDto.getHarvestedAcresOverride());
		Assert.assertEquals("HarvestedYield", newDto.getHarvestedYield(), fetchedDto.getHarvestedYield());
		Assert.assertEquals("HarvestedYieldOverride", newDto.getHarvestedYieldOverride(), fetchedDto.getHarvestedYieldOverride());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("ProductionGuarantee", newDto.getProductionGuarantee(), fetchedDto.getProductionGuarantee());
		Assert.assertEquals("SoldYieldDefaultUnit", newDto.getSoldYieldDefaultUnit(), fetchedDto.getSoldYieldDefaultUnit());
		Assert.assertEquals("StoredYieldDefaultUnit", newDto.getStoredYieldDefaultUnit(), fetchedDto.getStoredYieldDefaultUnit());
		Assert.assertEquals("TotalInsuredAcres", newDto.getTotalInsuredAcres(), fetchedDto.getTotalInsuredAcres());
		Assert.assertEquals("VerifiedYieldContractCommodityGuid", newDto.getVerifiedYieldContractCommodityGuid(), fetchedDto.getVerifiedYieldContractCommodityGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", newDto.getVerifiedYieldContractGuid(), fetchedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("YieldPerAcre", newDto.getYieldPerAcre(), fetchedDto.getYieldPerAcre());

		//UPDATE

		fetchedDto.setHarvestedAcres(13.57);
		fetchedDto.setHarvestedAcresOverride(24.68);
		fetchedDto.setHarvestedYield(35.79);
		fetchedDto.setHarvestedYieldOverride(46.80);
		fetchedDto.setProductionGuarantee(57.91);
		fetchedDto.setSoldYieldDefaultUnit(68.02);
		fetchedDto.setStoredYieldDefaultUnit(80.24);
		fetchedDto.setYieldPerAcre(2.46);
		
		dao.update(fetchedDto, userId);

		//FETCH
		VerifiedYieldContractCommodityDto updatedDto = dao.fetch(verifiedYieldContractCommodityGuid);

		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", fetchedDto.getCropCommodityName(), updatedDto.getCropCommodityName());
		Assert.assertEquals("CommodityTypeCode", fetchedDto.getCommodityTypeCode(), updatedDto.getCommodityTypeCode());
		Assert.assertEquals("CommodityTypeDescription", fetchedDto.getCommodityTypeDescription(), updatedDto.getCommodityTypeDescription());
		Assert.assertEquals("HarvestedAcres", fetchedDto.getHarvestedAcres(), updatedDto.getHarvestedAcres());
		Assert.assertEquals("HarvestedAcresOverride", fetchedDto.getHarvestedAcresOverride(), updatedDto.getHarvestedAcresOverride());
		Assert.assertEquals("HarvestedYield", fetchedDto.getHarvestedYield(), updatedDto.getHarvestedYield());
		Assert.assertEquals("HarvestedYieldOverride", fetchedDto.getHarvestedYieldOverride(), updatedDto.getHarvestedYieldOverride());
		Assert.assertEquals("IsPedigreeInd", fetchedDto.getIsPedigreeInd(), updatedDto.getIsPedigreeInd());
		Assert.assertEquals("ProductionGuarantee", fetchedDto.getProductionGuarantee(), updatedDto.getProductionGuarantee());
		Assert.assertEquals("SoldYieldDefaultUnit", fetchedDto.getSoldYieldDefaultUnit(), updatedDto.getSoldYieldDefaultUnit());
		Assert.assertEquals("StoredYieldDefaultUnit", fetchedDto.getStoredYieldDefaultUnit(), updatedDto.getStoredYieldDefaultUnit());
		Assert.assertEquals("TotalInsuredAcres", fetchedDto.getTotalInsuredAcres(), updatedDto.getTotalInsuredAcres());
		Assert.assertEquals("VerifiedYieldContractCommodityGuid", fetchedDto.getVerifiedYieldContractCommodityGuid(), updatedDto.getVerifiedYieldContractCommodityGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", fetchedDto.getVerifiedYieldContractGuid(), updatedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("YieldPerAcre", fetchedDto.getYieldPerAcre(), updatedDto.getYieldPerAcre());

		//INSERT second commodity
		VerifiedYieldContractCommodityDto newDto2 = new VerifiedYieldContractCommodityDto();
		
		newDto2.setCropCommodityId(18);
		newDto2.setCropCommodityName("CANOLA");
		newDto2.setHarvestedAcres(null);
		newDto2.setHarvestedAcresOverride(null);
		newDto2.setHarvestedYield(null);
		newDto2.setHarvestedYieldOverride(null);
		newDto2.setIsPedigreeInd(true);
		newDto2.setProductionGuarantee(null);
		newDto2.setSoldYieldDefaultUnit(null);
		newDto2.setStoredYieldDefaultUnit(null);
		newDto2.setTotalInsuredAcres(null);
		newDto2.setVerifiedYieldContractCommodityGuid(null);
		newDto2.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
		newDto2.setYieldPerAcre(null);

		dao.insert(newDto2, userId);
		
		//SELECT
		dtos = dao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
				
		//DELETE
		dao.delete(verifiedYieldContractCommodityGuid);
		VerifiedYieldContractCommodityDto deletedDto = dao.fetch(verifiedYieldContractCommodityGuid);
		Assert.assertNull(deletedDto);

		dao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
		deletedDto = dao.fetch(newDto2.getVerifiedYieldContractCommodityGuid());
		Assert.assertNull(deletedDto);

		dtos = dao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());		
	}

	private void createVerifiedYieldContract(String userId) throws DaoException {

		VerifiedYieldContractDao dao = persistenceSpringConfig.verifiedYieldContractDao();
		
		VerifiedYieldContractDto newDto = new VerifiedYieldContractDto();

		newDto.setContractId(contractId);
		newDto.setCropYear(cropYear);
		newDto.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto.setDefaultYieldMeasUnitTypeCode("TONNE");
		newDto.setInsurancePlanId(4);		
		
		//INSERT
		dao.insert(newDto, userId);
		verifiedYieldContractGuid = newDto.getVerifiedYieldContractGuid();
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
	
	private void createInventoryContractCommodity(Integer cropCommodityId, String cropCommodityName, Boolean isPedigreeInd, Double totalSeededAcres, String userId) throws DaoException {
		
		InventoryContractCommodityDao invContractCommodityDao = persistenceSpringConfig.inventoryContractCommodityDao();

		// INSERT
		InventoryContractCommodityDto newDto = new InventoryContractCommodityDto();
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropCommodityName(cropCommodityName);
		newDto.setInventoryContractGuid(inventoryContractGuid);
		newDto.setTotalSeededAcres(totalSeededAcres);
		newDto.setTotalSpotLossAcres(87.65);
		newDto.setTotalUnseededAcres(12.34);
		newDto.setTotalUnseededAcresOverride(56.78);
		newDto.setIsPedigreeInd(isPedigreeInd);
		
		invContractCommodityDao.insert(newDto, userId);

	}
	
	private void createGrowerContractYear() throws DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto newDto = new GrowerContractYearDto();
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();
		
		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setGrowerContractYearId(growerContractYearId);
		newDto.setContractId(contractId);
		newDto.setGrowerId(null);
		newDto.setInsurancePlanId(4);
		newDto.setCropYear(cropYear);
		newDto.setDataSyncTransDate(dateTime);
		
		dao.insert(newDto, userId);
	}
	 
}
