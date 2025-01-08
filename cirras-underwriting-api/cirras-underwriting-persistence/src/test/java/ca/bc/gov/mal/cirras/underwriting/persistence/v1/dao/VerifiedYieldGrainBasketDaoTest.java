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
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingCommentDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldGrainBasketDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class VerifiedYieldGrainBasketDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer growerContractYearId = 90000001;
	private Integer contractId = 90000002;
	private Integer cropYear = 2020;
	private String verifiedYieldContractGuid;
	private String declaredYieldContractGuid;
	private String inventoryContractGuid;
	private Integer fieldId = 90000003;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{

		// Delete VerifiedYieldGrainBasket
		VerifiedYieldGrainBasketDao vygbDao = persistenceSpringConfig.verifiedYieldGrainBasketDao();
		List<VerifiedYieldGrainBasketDto> vygbDtos = vygbDao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		if ( vygbDtos != null && !vygbDtos.isEmpty() ) {
			vygbDao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
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
		
		GrowerContractYearDao gcyDao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto gcyDto = gcyDao.fetch(growerContractYearId);
		if (gcyDto != null) {
			gcyDao.delete(growerContractYearId);
		}
		
		FieldDao fldDao = persistenceSpringConfig.fieldDao();
		FieldDto fldDto = fldDao.fetch(fieldId);
		if (fldDto != null) {
			fldDao.delete(fieldId);
		}	
		
	}


	@Test 
	public void testVerifiedYieldGrainBasket() throws Exception {

		String verifiedYieldGrainBasketGuid;
		String userId = "UNITTEST";

		createField("Test Field Label", userId);
		createGrowerContractYear();
		createInventoryContract(userId);
		createInventoryContractCommodity(16, "BARLEY", false, 23.45, userId);
		createDeclaredYieldContract(userId);
		createVerifiedYieldContract(userId);
		
		VerifiedYieldGrainBasketDao dao = persistenceSpringConfig.verifiedYieldGrainBasketDao();

		// INSERT
		VerifiedYieldGrainBasketDto newDto = new VerifiedYieldGrainBasketDto();

		newDto.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
		newDto.setBasketValue(200.45);
		newDto.setHarvestedValue(140.23);
		newDto.setComment("Basket Value Comment");

		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getVerifiedYieldGrainBasketGuid());
		verifiedYieldGrainBasketGuid = newDto.getVerifiedYieldGrainBasketGuid();
		
		//SELECT
		List<VerifiedYieldGrainBasketDto> dtos = dao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		VerifiedYieldGrainBasketDto fetchedDto = dtos.get(0);
		
		Assert.assertEquals("VerifiedYieldGrainBasketGuid", newDto.getVerifiedYieldGrainBasketGuid(), fetchedDto.getVerifiedYieldGrainBasketGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", newDto.getVerifiedYieldContractGuid(), fetchedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("BasketValue", newDto.getBasketValue(), fetchedDto.getBasketValue());
		Assert.assertEquals("HarvestedValue", newDto.getHarvestedValue(), fetchedDto.getHarvestedValue());
		Assert.assertEquals("Comment", newDto.getComment(), fetchedDto.getComment());
		
		//FETCH
		fetchedDto = dao.fetch(verifiedYieldGrainBasketGuid);
		
		Assert.assertEquals("VerifiedYieldGrainBasketGuid", newDto.getVerifiedYieldGrainBasketGuid(), fetchedDto.getVerifiedYieldGrainBasketGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", newDto.getVerifiedYieldContractGuid(), fetchedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("BasketValue", newDto.getBasketValue(), fetchedDto.getBasketValue());
		Assert.assertEquals("HarvestedValue", newDto.getHarvestedValue(), fetchedDto.getHarvestedValue());
		Assert.assertEquals("Comment", newDto.getComment(), fetchedDto.getComment());

		//UPDATE
		fetchedDto.setBasketValue(300.98);
		fetchedDto.setHarvestedValue(321.78);
		fetchedDto.setComment("Basket Value Comment 2");
				
		dao.update(fetchedDto, userId);

		//FETCH
		VerifiedYieldGrainBasketDto updatedDto = dao.fetch(verifiedYieldGrainBasketGuid);

		Assert.assertEquals("VerifiedYieldGrainBasketGuid", fetchedDto.getVerifiedYieldGrainBasketGuid(), updatedDto.getVerifiedYieldGrainBasketGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", fetchedDto.getVerifiedYieldContractGuid(), updatedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("BasketValue", fetchedDto.getBasketValue(), updatedDto.getBasketValue());
		Assert.assertEquals("HarvestedValue", fetchedDto.getHarvestedValue(), updatedDto.getHarvestedValue());
		Assert.assertEquals("Comment", fetchedDto.getComment(), updatedDto.getComment());

		// There can only be one record per contract
		// DELETE for verifiedYieldGrainBasketGuid
		dao.delete(verifiedYieldGrainBasketGuid);
		VerifiedYieldGrainBasketDto deletedDto = dao.fetch(verifiedYieldGrainBasketGuid);
		Assert.assertNull(deletedDto);
		
		dtos = dao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
		
		
		//INSERT second record
		VerifiedYieldGrainBasketDto newDto2 = new VerifiedYieldGrainBasketDto();

		newDto2.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
		newDto2.setBasketValue(123.45);
		newDto2.setHarvestedValue(133.66);
		newDto2.setComment("Basket Value Comment 3");
		
		dao.insert(newDto2, userId);
		
		//SELECT
		dtos = dao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
				
		//DELETE for verifiedYieldContractGuid 
		
		dao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
		deletedDto = dao.fetch(newDto2.getVerifiedYieldGrainBasketGuid());
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

	private void createField(String fieldLabel, String userId) throws DaoException {
		// INSERT FIELD
		
		Integer activeFromCropYear = 1980;

		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel(fieldLabel);
		newFieldDto.setActiveFromCropYear(activeFromCropYear);
		newFieldDto.setActiveToCropYear(null);

		fieldDao.insertDataSync(newFieldDto, userId);
	}
	
}
