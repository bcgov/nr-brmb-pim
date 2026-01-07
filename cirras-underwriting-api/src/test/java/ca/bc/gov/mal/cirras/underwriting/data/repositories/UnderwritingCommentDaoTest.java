package ca.bc.gov.mal.cirras.underwriting.data.repositories;

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

import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.UnderwritingCommentDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldSummaryDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class UnderwritingCommentDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteUwCommentDetail();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteUwCommentDetail();
	}
	
	private void deleteUwCommentDetail() throws NotFoundDaoException, DaoException{
		
		
		UnderwritingCommentDao underwritingCommentDao = persistenceSpringConfig.underwritingCommentDao();
		underwritingCommentDao.deleteForAnnualField(annualFieldDetailId);
		
				
		// delete annual field
		AnnualFieldDetailDao afdDao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto afdDto = afdDao.fetch(annualFieldDetailId);
		
		if (afdDto != null) {
			afdDao.delete(annualFieldDetailId);
		}
		
		// delete legal land
		LegalLandDao legalLandDao = persistenceSpringConfig.legalLandDao();
		LegalLandDto legalLandDto = legalLandDao.fetch(legalLandId);
		
		if (legalLandDto != null) {
			legalLandDao.delete(legalLandId);
		}
		
		// delete field
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = fieldDao.fetch(fieldId);
		
		if (fieldDto != null) {
			fieldDao.delete(fieldId);
		}
		
		//Delete Verified Yield Summary Comments
		underwritingCommentDao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
		
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
		
		GrowerContractYearDao gcyDao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto gcyDto = gcyDao.fetch(growerContractYearId);
		if (gcyDto != null) {
			gcyDao.delete(growerContractYearId);
		}
		
	}
	
	private Integer fieldId = 99999999;;
	private Integer legalLandId = 99999999;
	private Integer annualFieldDetailId = 99999999;
	private Integer growerContractYearId = 90000001;
	private Integer contractId = 90000002;
	private Integer cropYear = 2020;
	private String verifiedYieldContractGuid;
	private String declaredYieldContractGuid;
	
	@Test 
	public void testUnderwritingComment() throws Exception {

		Integer cropYear = 2020;
		String underwritingCommentGuid;
		String dopUwCommentGuid;
		
		String userId = "UNITTEST";
		
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		UnderwritingCommentDao underwritingCommentDao = persistenceSpringConfig.underwritingCommentDao();
		LegalLandDao legalLandDao = persistenceSpringConfig.legalLandDao();
		AnnualFieldDetailDao afdDao = persistenceSpringConfig.annualFieldDetailDao();

		//INSERT Legal Land
		LegalLandDto newlegalLandDto = new LegalLandDto();
		newlegalLandDto.setLegalLandId(legalLandId);
		newlegalLandDto.setPrimaryReferenceTypeCode("OTHER");
		newlegalLandDto.setLegalDescription("Legal Description");
		newlegalLandDto.setLegalShortDescription("Short Legal");
		newlegalLandDto.setOtherDescription("Other Description");
		newlegalLandDto.setActiveFromCropYear(2011);
		newlegalLandDto.setActiveToCropYear(2022);

		legalLandDao.insertDataSync(newlegalLandDto, userId);

		// INSERT FIELD
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel("Field Label");
		newFieldDto.setActiveFromCropYear(2012);
		newFieldDto.setActiveToCropYear(2022);

		fieldDao.insertDataSync(newFieldDto, userId);

		//INSERT Annual Field Detail record
		AnnualFieldDetailDto newAfdDto = new AnnualFieldDetailDto();
		newAfdDto.setAnnualFieldDetailId(annualFieldDetailId);
		newAfdDto.setLegalLandId(legalLandId);
		newAfdDto.setFieldId(fieldId);
		newAfdDto.setCropYear(cropYear);

		afdDao.insertDataSync(newAfdDto, userId);


		//INSERT
		UnderwritingCommentDto newDto = new UnderwritingCommentDto();

		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setUnderwritingComment("test comment 1");
		newDto.setUnderwritingCommentTypeCode("INV");
		newDto.setUnderwritingCommentTypeDesc("Inventory"); // Not saved directly, but indirectly via underwritingCommentTypeCode.
		
		underwritingCommentDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getUnderwritingCommentGuid());
		underwritingCommentGuid = newDto.getUnderwritingCommentGuid();
		
		//SELECT
		List<UnderwritingCommentDto> dtos = underwritingCommentDao.select(annualFieldDetailId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		dtos = underwritingCommentDao.selectForField(fieldId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		
		//FETCH
		UnderwritingCommentDto fetchedDto = underwritingCommentDao.fetch(underwritingCommentGuid);
		
		Assert.assertEquals("underwritingCommentGuid", newDto.getUnderwritingCommentGuid(), fetchedDto.getUnderwritingCommentGuid());
		Assert.assertEquals("annualFieldDetailId", newDto.getAnnualFieldDetailId(), fetchedDto.getAnnualFieldDetailId());
		Assert.assertEquals("underwritingComment", newDto.getUnderwritingComment(), fetchedDto.getUnderwritingComment());
		Assert.assertEquals("underwritingCommentTypeCode", newDto.getUnderwritingCommentTypeCode(), fetchedDto.getUnderwritingCommentTypeCode());
		Assert.assertEquals("underwritingCommentTypeDesc", newDto.getUnderwritingCommentTypeDesc(), fetchedDto.getUnderwritingCommentTypeDesc());
		
		
		//UPDATE
		fetchedDto.setUnderwritingComment("test comment 2");
		fetchedDto.setUnderwritingCommentTypeCode("INV");
		fetchedDto.setUnderwritingCommentTypeDesc("Inventory");
		
		underwritingCommentDao.update(fetchedDto, userId);

		//FETCH
		UnderwritingCommentDto updatedDto = underwritingCommentDao.fetch(underwritingCommentGuid);

		Assert.assertEquals("underwritingCommentGuid", fetchedDto.getUnderwritingCommentGuid(), updatedDto.getUnderwritingCommentGuid());
		Assert.assertEquals("underwritingComment", fetchedDto.getUnderwritingComment(), updatedDto.getUnderwritingComment());
		Assert.assertEquals("underwritingCommentTypeCode", fetchedDto.getUnderwritingCommentTypeCode(), updatedDto.getUnderwritingCommentTypeCode());
		Assert.assertEquals("underwritingCommentTypeDesc", fetchedDto.getUnderwritingCommentTypeDesc(), updatedDto.getUnderwritingCommentTypeDesc());
		
		//INSERT second comment
		UnderwritingCommentDto newDto2 = new UnderwritingCommentDto();

		newDto2.setAnnualFieldDetailId(annualFieldDetailId);
		newDto2.setUnderwritingComment("test comment 3");
		newDto2.setUnderwritingCommentTypeCode("INV");
		newDto2.setUnderwritingCommentTypeDesc("Inventory"); // Not saved directly, but indirectly via underwritingCommentTypeCode.
		
		underwritingCommentDao.insert(newDto2, userId);

		//SELECT
		dtos = underwritingCommentDao.select(annualFieldDetailId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		dtos = underwritingCommentDao.selectForField(fieldId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		//INSERT DOP comment
		// These need to be set to an existing declared_yield_contract with no comments.
		String declaredYieldContractGuid = "afbbe9d605384b4992b439d1a35adbba";
		Integer growerContractYearId = 97637;
		
		//INSERT
		UnderwritingCommentDto newDto3 = new UnderwritingCommentDto();
		newDto3.setUnderwritingComment("test DOP comment 1");
		newDto3.setUnderwritingCommentTypeCode("DOP");
		newDto3.setUnderwritingCommentTypeDesc("Declaration of Production"); // Not saved directly, but indirectly via underwritingCommentTypeCode.
		newDto3.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto3.setGrowerContractYearId(growerContractYearId);
		
		underwritingCommentDao.insert(newDto3, userId);
		Assert.assertNotNull(newDto3.getUnderwritingCommentGuid());
		dopUwCommentGuid = newDto3.getUnderwritingCommentGuid();
		
		//SELECT for DOP
		dtos = underwritingCommentDao.selectForDopContract(declaredYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		UnderwritingCommentDto fetchedDto2 = underwritingCommentDao.fetch(dopUwCommentGuid);
		
		Assert.assertEquals("declaredYieldContractGuid", fetchedDto2.getDeclaredYieldContractGuid(), newDto3.getDeclaredYieldContractGuid());
		Assert.assertEquals("growerContractYearId", fetchedDto2.getGrowerContractYearId(), newDto3.getGrowerContractYearId());

		//DELETE dop comment
		underwritingCommentDao.delete(dopUwCommentGuid);
		
		//FETCH
		UnderwritingCommentDto deletedDto = underwritingCommentDao.fetch(dopUwCommentGuid);
		Assert.assertNull(deletedDto);
		
		
		//DELETE first comment
		underwritingCommentDao.delete(underwritingCommentGuid);
		
		//FETCH
		deletedDto = underwritingCommentDao.fetch(underwritingCommentGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = underwritingCommentDao.select(annualFieldDetailId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());		

		//DELETE second comment (by InventoryField).
		underwritingCommentDao.deleteForAnnualField(annualFieldDetailId);
		
		//SELECT
		dtos = underwritingCommentDao.select(annualFieldDetailId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());		

		dtos = underwritingCommentDao.selectForField(fieldId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());		
		
		//DELETE annual field
		afdDao.delete(annualFieldDetailId);
		
		//DELETE field
		fieldDao.delete(fieldId);
		
		//DELETE legal land
		legalLandDao.delete(legalLandId);
		
	}

	
	@Test 
	public void testDeleteForField() throws Exception {
		
		// INSERT FIELD
		createField();
		
		// INSERT ANNUAL FIELD
		createAnnualField();
		
		// INSERT CONTRACTED FIELD
		String underwritingCommentGuid = createUwComment();
		
		UnderwritingCommentDao dao = persistenceSpringConfig.underwritingCommentDao();
		
		//FETCH
		UnderwritingCommentDto fetchedDto = dao.fetch(underwritingCommentGuid);
		Assert.assertNotNull(fetchedDto);

		dao.deleteForField(fieldId);

		//FETCH
		UnderwritingCommentDto deletedDto = dao.fetch(underwritingCommentGuid);
		Assert.assertNull(deletedDto);

		//clean up 
		deleteUwCommentDetail();

	}
	
	@Test 
	public void testVerifiedYieldSummaryComments() throws Exception {
		
		String userId = "UNITTEST";
		
		createGrowerContractYear();
		createDeclaredYieldContract(userId);
		createVerifiedYieldContract(userId);
		
		String verifiedYieldSummaryGuid1 = createVerifiedYieldSummary(userId, 16);
		String verifiedYieldSummaryGuid2 = createVerifiedYieldSummary(userId, 18);

		createVerifiedYieldSummaryComment(verifiedYieldSummaryGuid1, "test comment 1");
		createVerifiedYieldSummaryComment(verifiedYieldSummaryGuid1, "test comment 2");

		createVerifiedYieldSummaryComment(verifiedYieldSummaryGuid2, "test comment 3");

		UnderwritingCommentDao dao = persistenceSpringConfig.underwritingCommentDao();
		
		//Select for first verified yield summary
		List<UnderwritingCommentDto> fetchedDtos = dao.selectForVerifiedYieldSummary(verifiedYieldSummaryGuid1);
		Assert.assertNotNull(fetchedDtos);
		Assert.assertEquals(2, fetchedDtos.size());

		//Select for second verified yield summary
		fetchedDtos = dao.selectForVerifiedYieldSummary(verifiedYieldSummaryGuid2);
		Assert.assertNotNull(fetchedDtos);
		Assert.assertEquals(1, fetchedDtos.size());
		
		//DELETE for second verified yield summary
		dao.deleteForVerifiedYieldSummaryGuid(verifiedYieldSummaryGuid2);
		fetchedDtos = dao.selectForVerifiedYieldSummary(verifiedYieldSummaryGuid2);
		Assert.assertNotNull(fetchedDtos);
		Assert.assertEquals(0, fetchedDtos.size());

		//Select for first verified yield summary again
		fetchedDtos = dao.selectForVerifiedYieldSummary(verifiedYieldSummaryGuid1);
		Assert.assertNotNull(fetchedDtos);
		Assert.assertEquals(2, fetchedDtos.size());

		//DELETE for verified yield contract
		dao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
		fetchedDtos = dao.selectForVerifiedYieldSummary(verifiedYieldSummaryGuid1);
		Assert.assertNotNull(fetchedDtos);
		Assert.assertEquals(0, fetchedDtos.size());

		//clean up 
		deleteUwCommentDetail();

	}
	
	
	private String createVerifiedYieldSummaryComment(String verifiedYieldSummaryGuid, String comment) throws DaoException {
		String userId = "UNITTEST";

		UnderwritingCommentDao dao = persistenceSpringConfig.underwritingCommentDao();
		UnderwritingCommentDto newDto = new UnderwritingCommentDto();

		newDto.setVerifiedYieldSummaryGuid(verifiedYieldSummaryGuid);
		newDto.setUnderwritingComment(comment);
		newDto.setUnderwritingCommentTypeCode("VY");
		newDto.setUnderwritingCommentTypeDesc("Verified Yield");
		
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getUnderwritingCommentGuid());
		return newDto.getUnderwritingCommentGuid();

		
	}
	
	private String createUwComment() throws DaoException {
		String userId = "UNITTEST";

		UnderwritingCommentDao dao = persistenceSpringConfig.underwritingCommentDao();
		UnderwritingCommentDto newDto = new UnderwritingCommentDto();

		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setUnderwritingComment("test comment 1");
		newDto.setUnderwritingCommentTypeCode("INV");
		newDto.setUnderwritingCommentTypeDesc("Inventory"); // Not saved directly, but indirectly via underwritingCommentTypeCode.
		
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getUnderwritingCommentGuid());
		return newDto.getUnderwritingCommentGuid();

		
	}

	private void createField() throws DaoException {
		// INSERT FIELD
		
		String userId = "UNITTEST";

		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel("Test Field Label");
		newFieldDto.setActiveFromCropYear(2011);
		newFieldDto.setActiveToCropYear(null);

		fieldDao.insertDataSync(newFieldDto, userId);
		
	}	


	private void createAnnualField() throws DaoException {
		
		String userId = "UNITTEST";

		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto newDto = new AnnualFieldDetailDto();

		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setLegalLandId(null);
		newDto.setFieldId(fieldId);
		newDto.setCropYear(2022);

		dao.insertDataSync(newDto, userId);

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
	
	private String createVerifiedYieldSummary(String userId, Integer cropCommodityId) throws DaoException {

		VerifiedYieldSummaryDao dao = persistenceSpringConfig.verifiedYieldSummaryDao();

		VerifiedYieldSummaryDto newDto = new VerifiedYieldSummaryDto();

		newDto.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
		newDto.setCropCommodityId(cropCommodityId);
		//newDto.setCropCommodityName("BARLEY");
		newDto.setIsPedigreeInd(false);
		newDto.setHarvestedYield(100.0);
		newDto.setHarvestedYieldPerAcre(10.0);
		newDto.setAppraisedYield(1.5);
		newDto.setAssessedYield(0.5);
		newDto.setYieldToCount(15.5);
		newDto.setYieldPercentPy(75.5);
		newDto.setProductionGuarantee(20.5);
		newDto.setProbableYield(17.5);

		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getVerifiedYieldSummaryGuid());
		
		return newDto.getVerifiedYieldSummaryGuid();
	}

}
