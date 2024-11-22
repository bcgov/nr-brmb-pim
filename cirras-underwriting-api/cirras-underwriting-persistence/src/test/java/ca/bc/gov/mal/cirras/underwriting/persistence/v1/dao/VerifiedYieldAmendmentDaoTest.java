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
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldAmendmentDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class VerifiedYieldAmendmentDaoTest {
	
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

		// Delete VerifiedYieldAmendment
		VerifiedYieldAmendmentDao vyaDao = persistenceSpringConfig.verifiedYieldAmendmentDao();
		List<VerifiedYieldAmendmentDto> vyaDtos = vyaDao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		if ( vyaDtos != null && !vyaDtos.isEmpty() ) {
			vyaDao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
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
	public void testVerifiedYieldAmendment() throws Exception {

		String verifiedYieldAmendmentGuid;
		String userId = "UNITTEST";

		createField("Test Field Label", userId);
		createGrowerContractYear();
		createInventoryContract(userId);
		createDeclaredYieldContract(userId);
		createVerifiedYieldContract(userId);
		
		VerifiedYieldAmendmentDao dao = persistenceSpringConfig.verifiedYieldAmendmentDao();

		// INSERT
		VerifiedYieldAmendmentDto newDto = new VerifiedYieldAmendmentDto();

		newDto.setAcres(12.34);
		newDto.setCropCommodityId(16);
		newDto.setCropCommodityName("BARLEY");
		newDto.setFieldId(fieldId);
		newDto.setFieldLabel("Test Field Label");
		newDto.setIsPedigreeInd(false);
		newDto.setRationale("user comment");
		newDto.setVerifiedYieldAmendmentCode("Appraisal");
		newDto.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
		newDto.setYieldPerAcre(56.78);

		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getVerifiedYieldAmendmentGuid());
		verifiedYieldAmendmentGuid = newDto.getVerifiedYieldAmendmentGuid();
		
		//SELECT
		List<VerifiedYieldAmendmentDto> dtos = dao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		VerifiedYieldAmendmentDto fetchedDto = dtos.get(0);

		Assert.assertEquals("Acres", newDto.getAcres(), fetchedDto.getAcres());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("FieldId", newDto.getFieldId(), fetchedDto.getFieldId());
		Assert.assertEquals("FieldLabel", newDto.getFieldLabel(), fetchedDto.getFieldLabel());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("Rationale", newDto.getRationale(), fetchedDto.getRationale());
		Assert.assertEquals("VerifiedYieldAmendmentCode", newDto.getVerifiedYieldAmendmentCode(), fetchedDto.getVerifiedYieldAmendmentCode());
		Assert.assertEquals("VerifiedYieldAmendmentGuid", newDto.getVerifiedYieldAmendmentGuid(), fetchedDto.getVerifiedYieldAmendmentGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", newDto.getVerifiedYieldContractGuid(), fetchedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("YieldPerAcre", newDto.getYieldPerAcre(), fetchedDto.getYieldPerAcre());
		
		//FETCH
		fetchedDto = dao.fetch(verifiedYieldAmendmentGuid);
		
		Assert.assertEquals("Acres", newDto.getAcres(), fetchedDto.getAcres());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("FieldId", newDto.getFieldId(), fetchedDto.getFieldId());
		Assert.assertEquals("FieldLabel", newDto.getFieldLabel(), fetchedDto.getFieldLabel());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("Rationale", newDto.getRationale(), fetchedDto.getRationale());
		Assert.assertEquals("VerifiedYieldAmendmentCode", newDto.getVerifiedYieldAmendmentCode(), fetchedDto.getVerifiedYieldAmendmentCode());
		Assert.assertEquals("VerifiedYieldAmendmentGuid", newDto.getVerifiedYieldAmendmentGuid(), fetchedDto.getVerifiedYieldAmendmentGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", newDto.getVerifiedYieldContractGuid(), fetchedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("YieldPerAcre", newDto.getYieldPerAcre(), fetchedDto.getYieldPerAcre());

		//UPDATE
		fetchedDto.setAcres(11.22);
		fetchedDto.setCropCommodityId(18);
		fetchedDto.setCropCommodityName("CANOLA");
		fetchedDto.setFieldId(null);
		fetchedDto.setFieldLabel(null);
		fetchedDto.setIsPedigreeInd(true);
		fetchedDto.setRationale("user comment 2");
		fetchedDto.setVerifiedYieldAmendmentCode("Assessment");
		fetchedDto.setYieldPerAcre(33.44);
				
		dao.update(fetchedDto, userId);

		//FETCH
		VerifiedYieldAmendmentDto updatedDto = dao.fetch(verifiedYieldAmendmentGuid);

		Assert.assertEquals("Acres", fetchedDto.getAcres(), updatedDto.getAcres());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", fetchedDto.getCropCommodityName(), updatedDto.getCropCommodityName());
		Assert.assertEquals("FieldId", fetchedDto.getFieldId(), updatedDto.getFieldId());
		Assert.assertEquals("FieldLabel", fetchedDto.getFieldLabel(), updatedDto.getFieldLabel());
		Assert.assertEquals("IsPedigreeInd", fetchedDto.getIsPedigreeInd(), updatedDto.getIsPedigreeInd());
		Assert.assertEquals("Rationale", fetchedDto.getRationale(), updatedDto.getRationale());
		Assert.assertEquals("VerifiedYieldAmendmentCode", fetchedDto.getVerifiedYieldAmendmentCode(), updatedDto.getVerifiedYieldAmendmentCode());
		Assert.assertEquals("VerifiedYieldAmendmentGuid", fetchedDto.getVerifiedYieldAmendmentGuid(), updatedDto.getVerifiedYieldAmendmentGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", fetchedDto.getVerifiedYieldContractGuid(), updatedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("YieldPerAcre", fetchedDto.getYieldPerAcre(), updatedDto.getYieldPerAcre());

		//INSERT second amendment
		VerifiedYieldAmendmentDto newDto2 = new VerifiedYieldAmendmentDto();

		newDto2.setAcres(12.34);
		newDto2.setCropCommodityId(16);
		newDto2.setCropCommodityName("BARLEY");
		newDto2.setFieldId(fieldId);
		newDto2.setFieldLabel("Test Field Label");
		newDto2.setIsPedigreeInd(false);
		newDto2.setRationale("user comment");
		newDto2.setVerifiedYieldAmendmentCode("Appraisal");
		newDto2.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
		newDto2.setYieldPerAcre(56.78);

		dao.insert(newDto2, userId);
		
		//SELECT
		dtos = dao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
				
		//DELETE
		dao.delete(verifiedYieldAmendmentGuid);
		VerifiedYieldAmendmentDto deletedDto = dao.fetch(verifiedYieldAmendmentGuid);
		Assert.assertNull(deletedDto);

		dao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
		deletedDto = dao.fetch(newDto2.getVerifiedYieldAmendmentGuid());
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
