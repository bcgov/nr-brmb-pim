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
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldSummaryDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class VerifiedYieldSummaryDaoTest {
	
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

		// Delete VerifiedYieldSummary
		VerifiedYieldSummaryDao vyaDao = persistenceSpringConfig.verifiedYieldSummaryDao();
		List<VerifiedYieldSummaryDto> vyaDtos = vyaDao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
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
	public void testVerifiedYieldSummary() throws Exception {

		String verifiedYieldSummaryGuid;
		String userId = "UNITTEST";

		createField("Test Field Label", userId);
		createGrowerContractYear();
		createInventoryContract(userId);
		createInventoryContractCommodity(16, "BARLEY", false, 23.45, userId);
		createDeclaredYieldContract(userId);
		createVerifiedYieldContract(userId);
		
		VerifiedYieldSummaryDao dao = persistenceSpringConfig.verifiedYieldSummaryDao();

		// INSERT
		VerifiedYieldSummaryDto newDto = new VerifiedYieldSummaryDto();

		newDto.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
		newDto.setCropCommodityId(16);
		newDto.setCropCommodityName("BARLEY");
		newDto.setIsPedigreeInd(false);
		newDto.setHarvestedYield(100.0);
		newDto.setHarvestedYieldPerAcre(10.0);
		newDto.setAppraisedYield(1.5);
		newDto.setAssessedYield(0.5);
		newDto.setYieldToCount(15.5);
		newDto.setYieldPercentPy(75.5);
		newDto.setProductionGuarantee(20.5);
		newDto.setProbableYield(17.5);
		newDto.setTotalInsuredAcres(23.45);

		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getVerifiedYieldSummaryGuid());
		verifiedYieldSummaryGuid = newDto.getVerifiedYieldSummaryGuid();
		
		//SELECT
		List<VerifiedYieldSummaryDto> dtos = dao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		VerifiedYieldSummaryDto fetchedDto = dtos.get(0);
		
		Assert.assertEquals("VerifiedYieldSummaryGuid", newDto.getVerifiedYieldSummaryGuid(), fetchedDto.getVerifiedYieldSummaryGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", newDto.getVerifiedYieldContractGuid(), fetchedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("HarvestedYield", newDto.getHarvestedYield(), fetchedDto.getHarvestedYield());
		Assert.assertEquals("HarvestedYieldPerAcre", newDto.getHarvestedYieldPerAcre(), fetchedDto.getHarvestedYieldPerAcre());
		Assert.assertEquals("AppraisedYield", newDto.getAppraisedYield(), fetchedDto.getAppraisedYield());
		Assert.assertEquals("AssessedYield", newDto.getAssessedYield(), fetchedDto.getAssessedYield());
		Assert.assertEquals("YieldToCount", newDto.getYieldToCount(), fetchedDto.getYieldToCount());
		Assert.assertEquals("YieldPercentPy", newDto.getYieldPercentPy(), fetchedDto.getYieldPercentPy());
		Assert.assertEquals("ProductionGuarantee", newDto.getProductionGuarantee(), fetchedDto.getProductionGuarantee());
		Assert.assertEquals("ProbableYield", newDto.getProbableYield(), fetchedDto.getProbableYield());
		Assert.assertEquals("TotalInsuredAcres", newDto.getTotalInsuredAcres(), fetchedDto.getTotalInsuredAcres());

		//FETCH
		fetchedDto = dao.fetch(verifiedYieldSummaryGuid);
		
		Assert.assertEquals("VerifiedYieldSummaryGuid", newDto.getVerifiedYieldSummaryGuid(), fetchedDto.getVerifiedYieldSummaryGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", newDto.getVerifiedYieldContractGuid(), fetchedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		Assert.assertEquals("HarvestedYield", newDto.getHarvestedYield(), fetchedDto.getHarvestedYield());
		Assert.assertEquals("HarvestedYieldPerAcre", newDto.getHarvestedYieldPerAcre(), fetchedDto.getHarvestedYieldPerAcre());
		Assert.assertEquals("AppraisedYield", newDto.getAppraisedYield(), fetchedDto.getAppraisedYield());
		Assert.assertEquals("AssessedYield", newDto.getAssessedYield(), fetchedDto.getAssessedYield());
		Assert.assertEquals("YieldToCount", newDto.getYieldToCount(), fetchedDto.getYieldToCount());
		Assert.assertEquals("YieldPercentPy", newDto.getYieldPercentPy(), fetchedDto.getYieldPercentPy());
		Assert.assertEquals("ProductionGuarantee", newDto.getProductionGuarantee(), fetchedDto.getProductionGuarantee());
		Assert.assertEquals("ProbableYield", newDto.getProbableYield(), fetchedDto.getProbableYield());
		Assert.assertEquals("TotalInsuredAcres", newDto.getTotalInsuredAcres(), fetchedDto.getTotalInsuredAcres());

		//UPDATE
		fetchedDto.setCropCommodityId(18);
		fetchedDto.setCropCommodityName("CANOLA");
		fetchedDto.setIsPedigreeInd(true);
		fetchedDto.setHarvestedYield(120.0);
		fetchedDto.setHarvestedYieldPerAcre(15.0);
		fetchedDto.setAppraisedYield(2.5);
		fetchedDto.setAssessedYield(1.5);
		fetchedDto.setYieldToCount(10.5);
		fetchedDto.setYieldPercentPy(85.5);
		fetchedDto.setProductionGuarantee(11.5);
		fetchedDto.setProbableYield(27.8);
		fetchedDto.setTotalInsuredAcres(null);
				
		dao.update(fetchedDto, userId);

		//FETCH
		VerifiedYieldSummaryDto updatedDto = dao.fetch(verifiedYieldSummaryGuid);

		Assert.assertEquals("VerifiedYieldSummaryGuid", fetchedDto.getVerifiedYieldSummaryGuid(), updatedDto.getVerifiedYieldSummaryGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", fetchedDto.getVerifiedYieldContractGuid(), updatedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", fetchedDto.getCropCommodityName(), updatedDto.getCropCommodityName());
		Assert.assertEquals("IsPedigreeInd", fetchedDto.getIsPedigreeInd(), updatedDto.getIsPedigreeInd());
		Assert.assertEquals("HarvestedYield", fetchedDto.getHarvestedYield(), updatedDto.getHarvestedYield());
		Assert.assertEquals("HarvestedYieldPerAcre", fetchedDto.getHarvestedYieldPerAcre(), updatedDto.getHarvestedYieldPerAcre());
		Assert.assertEquals("AppraisedYield", fetchedDto.getAppraisedYield(), updatedDto.getAppraisedYield());
		Assert.assertEquals("AssessedYield", fetchedDto.getAssessedYield(), updatedDto.getAssessedYield());
		Assert.assertEquals("YieldToCount", fetchedDto.getYieldToCount(), updatedDto.getYieldToCount());
		Assert.assertEquals("YieldPercentPy", fetchedDto.getYieldPercentPy(), updatedDto.getYieldPercentPy());
		Assert.assertEquals("ProductionGuarantee", fetchedDto.getProductionGuarantee(), updatedDto.getProductionGuarantee());
		Assert.assertEquals("ProbableYield", fetchedDto.getProbableYield(), updatedDto.getProbableYield());		
		Assert.assertEquals("TotalInsuredAcres", fetchedDto.getTotalInsuredAcres(), updatedDto.getTotalInsuredAcres());

		//INSERT second record
		VerifiedYieldSummaryDto newDto2 = new VerifiedYieldSummaryDto();

		newDto2.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
		newDto2.setCropCommodityId(16);
		newDto2.setCropCommodityName("BARLEY");
		newDto2.setIsPedigreeInd(false);
		newDto2.setHarvestedYield(100.0);
		newDto2.setHarvestedYieldPerAcre(10.0);
		newDto2.setAppraisedYield(1.5);
		newDto2.setAssessedYield(0.5);
		newDto2.setYieldToCount(15.5);
		newDto2.setYieldPercentPy(75.5);
		newDto2.setProductionGuarantee(20.5);
		newDto2.setProbableYield(17.5);
		newDto2.setTotalInsuredAcres(null);
		
		dao.insert(newDto2, userId);
		
		//SELECT
		dtos = dao.selectForVerifiedYieldContract(verifiedYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
				
		//DELETE
		dao.delete(verifiedYieldSummaryGuid);
		VerifiedYieldSummaryDto deletedDto = dao.fetch(verifiedYieldSummaryGuid);
		Assert.assertNull(deletedDto);

		dao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
		deletedDto = dao.fetch(newDto2.getVerifiedYieldSummaryGuid());
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
