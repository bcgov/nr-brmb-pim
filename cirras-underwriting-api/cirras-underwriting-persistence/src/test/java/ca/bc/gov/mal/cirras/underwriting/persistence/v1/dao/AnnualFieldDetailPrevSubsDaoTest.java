package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class AnnualFieldDetailPrevSubsDaoTest {
	
	private Integer legalLandId = 99999999;
	private Integer fieldId = 99999999;
	private Integer annualFieldDetailId2018 = 999992018;
	private Integer annualFieldDetailId2020 = 999992020;
	private Integer annualFieldDetailId2022 = 999992022;
	private Integer annualFieldDetailId2024 = 999992024;
	private Integer annualFieldDetailId2026 = 999992026;
	private Integer contractedFieldDetailId = 563217898;
	private Integer growerContractYearId = 99999999;
	private Integer contractId = 888888888;
	private Integer policyId = 987456321;
	private Integer cropYear2018 = 2018;
	private Integer cropYear2020 = 2020;
	private Integer cropYear2022 = 2022;
	private Integer cropYear2024 = 2024;
	private Integer cropYear2026 = 2026;
	private Integer growerId = 525593; //Has to be a valid grower
	private Integer insurancePlanId = 4;
	private String contractNumber = "123456";
	private String userId = "Dao Test"; 

	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteAnnualFieldDetail();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteAnnualFieldDetail();
	}
	
	private void deleteAnnualFieldDetail() throws NotFoundDaoException, DaoException{

		//delete contracted field
		ContractedFieldDetailDao cfdDao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto cfdDto = cfdDao.fetch(contractedFieldDetailId);		
		if (cfdDto != null) {			
			cfdDao.delete(contractedFieldDetailId);
		}		

		//delete annual field
		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		dao.delete(annualFieldDetailId2018);
		dao.delete(annualFieldDetailId2020);
		dao.delete(annualFieldDetailId2022);
		dao.delete(annualFieldDetailId2024);
		dao.delete(annualFieldDetailId2026);

		
		// delete field
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = fieldDao.fetch(fieldId);
		
		if (fieldDto != null) {
			fieldDao.delete(fieldId);
		}
		
		// delete legal land
		LegalLandDao legalLandDao = persistenceSpringConfig.legalLandDao();
		LegalLandDto legalLandDto = legalLandDao.fetch(legalLandId);
		
		if (legalLandDto != null) {
			legalLandDao.delete(legalLandId);
		}

		
		// delete Grower Contract Year
		GrowerContractYearDao gcyDao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto gcyDto = gcyDao.fetch(growerContractYearId);
		if (gcyDto != null) {
			gcyDao.delete(growerContractYearId);
		}
		
		//delete policy
		PolicyDao policyDao = persistenceSpringConfig.policyDao();
		PolicyDto policyDto = policyDao.fetch(policyId);
		if (policyDto != null) {
			policyDao.delete(policyId);
		}

	}
	
	@Test
	public void testGetPreviousSubsequentRecords() throws DaoException {
		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		
		//Create Field and Legal Land
		createField();
		createLegalLand();
		
		//Test without annual
		AnnualFieldDetailDto dto = dao.getPreviousSubsequentRecords(fieldId, cropYear2022);
		
		Assert.assertNotNull(dto);
		Assert.assertNull("PreviousContractCropYear not null", dto.getPreviousContractCropYear());
		Assert.assertNull("PreviousContractLegalLandId not null", dto.getPreviousContractLegalLandId());
		Assert.assertNull("SubsequentContractCropYear not null", dto.getSubsequentContractCropYear());
		Assert.assertNull("SubsequentContractLegalLandId not null", dto.getSubsequentContractLegalLandId());
		Assert.assertNull("PreviousCropYear not null", dto.getPreviousCropYear());
		Assert.assertNull("PreviousLegalLandId not null", dto.getPreviousLegalLandId());
		Assert.assertNull("SubsequentCropYear not null", dto.getSubsequentCropYear());
		Assert.assertNull("SubsequentLegalLandId not null", dto.getSubsequentLegalLandId());
		
		//Create annual detail records without contracts
		createAnnualFieldDetail(annualFieldDetailId2018, cropYear2018);
		createAnnualFieldDetail(annualFieldDetailId2020, cropYear2020);
		createAnnualFieldDetail(annualFieldDetailId2022, cropYear2022);
		createAnnualFieldDetail(annualFieldDetailId2024, cropYear2024);
		createAnnualFieldDetail(annualFieldDetailId2026, cropYear2026);
		
		//Test with annuals
		dto = dao.getPreviousSubsequentRecords(fieldId, cropYear2022);
		
		Assert.assertNotNull(dto);
		Assert.assertNull("2 PreviousContractCropYear not null", dto.getPreviousContractCropYear());
		Assert.assertNull("2 PreviousContractLegalLandId not null", dto.getPreviousContractLegalLandId());
		Assert.assertNull("2 SubsequentContractCropYear not null", dto.getSubsequentContractCropYear());
		Assert.assertNull("2 SubsequentContractLegalLandId not null", dto.getSubsequentContractLegalLandId());
		Assert.assertEquals("2 PreviousCropYear", cropYear2020, dto.getPreviousCropYear());
		Assert.assertEquals("2 PreviousLegalLandId", legalLandId, dto.getPreviousLegalLandId());
		Assert.assertEquals("2 SubsequentCropYear", cropYear2024, dto.getSubsequentCropYear());
		Assert.assertEquals("2 SubsequentLegalLandId", legalLandId, dto.getSubsequentLegalLandId());

		//create grower contract years
		createPolicy(cropYear2022, "123654-22");
		createGrowerContractYear(cropYear2022);
		createContractedFieldDetail();
		
		dto = dao.getPreviousSubsequentRecords(fieldId, 2025);
		
		//Check for 2025
		Assert.assertNotNull(dto);
		Assert.assertEquals("3 PreviousContractCropYear", cropYear2022, dto.getPreviousContractCropYear());
		Assert.assertEquals("3 PreviousContractLegalLandId", legalLandId, dto.getPreviousContractLegalLandId());
		Assert.assertNull("3 SubsequentContractCropYear not null", dto.getSubsequentContractCropYear());
		Assert.assertNull("3 SubsequentContractLegalLandId not null", dto.getSubsequentContractLegalLandId());
		Assert.assertEquals("3 PreviousCropYear", cropYear2024, dto.getPreviousCropYear());
		Assert.assertEquals("3 PreviousLegalLandId", legalLandId, dto.getPreviousLegalLandId());
		Assert.assertEquals("3 SubsequentCropYear", cropYear2026, dto.getSubsequentCropYear());
		Assert.assertEquals("3 SubsequentLegalLandId", legalLandId, dto.getSubsequentLegalLandId());
		
		dto = dao.getPreviousSubsequentRecords(fieldId, 2019);
		
		//Check for 2019
		Assert.assertNotNull(dto);
		Assert.assertNull("4 PreviousContractCropYear not null", dto.getPreviousContractCropYear());
		Assert.assertNull("4 PreviousContractLegalLandId not null", dto.getPreviousContractLegalLandId());
		Assert.assertEquals("4 SubsequentContractCropYear", cropYear2022, dto.getSubsequentContractCropYear());
		Assert.assertEquals("4 SubsequentContractLegalLandId", legalLandId, dto.getSubsequentContractLegalLandId());
		Assert.assertEquals("4 PreviousCropYear", cropYear2018, dto.getPreviousCropYear());
		Assert.assertEquals("4 PreviousLegalLandId", legalLandId, dto.getPreviousLegalLandId());
		Assert.assertEquals("4 SubsequentCropYear", cropYear2020, dto.getSubsequentCropYear());
		Assert.assertEquals("4 SubsequentLegalLandId", legalLandId, dto.getSubsequentLegalLandId());
		
	}
	
	
	private void createField() throws DaoException {
		// INSERT FIELD
		
		String fieldLabel = "Test Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;

		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel(fieldLabel);
		newFieldDto.setActiveFromCropYear(activeFromCropYear);
		newFieldDto.setActiveToCropYear(activeToCropYear);

		fieldDao.insertDataSync(newFieldDto, userId);
	}	

	private void createLegalLand() throws DaoException {
		String userId = "JUNIT_TEST";

		// Legal Land
		LegalLandDao llDao = persistenceSpringConfig.legalLandDao();
		LegalLandDto llDto = new LegalLandDto();
		
		//INSERT
		llDto.setLegalLandId(legalLandId);
		llDto.setPrimaryPropertyIdentifier("111-345-879");
		llDto.setPrimaryReferenceTypeCode("OTHER");
		llDto.setLegalDescription(null);
		llDto.setLegalShortDescription(null);
		llDto.setOtherDescription("Test Annual Field");
		llDto.setActiveFromCropYear(1980);
		llDto.setActiveToCropYear(null);
		llDto.setTotalAcres(12.3);
		llDto.setPrimaryLandIdentifierTypeCode("PID");

		llDao.insertDataSync(llDto, userId);
	}
	
	private void createAnnualFieldDetail(
			Integer annualFieldDetailId,
			Integer cropYear
			) throws DaoException {
		String userId = "JUNIT_TEST";

		// Annual Field Detail
		AnnualFieldDetailDao afdDao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto afdDto = new AnnualFieldDetailDto();

		afdDto.setAnnualFieldDetailId(annualFieldDetailId);
		afdDto.setLegalLandId(legalLandId);
		afdDto.setFieldId(fieldId);
		afdDto.setCropYear(cropYear);

		afdDao.insertDataSync(afdDto, userId);
	}
	
	private void createContractedFieldDetail(
		) throws DaoException {
		String userId = "JUNIT_TEST";

		ContractedFieldDetailDao cfdDao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto cfdDto = new ContractedFieldDetailDto();

		// INSERT
		cfdDto.setAnnualFieldDetailId(annualFieldDetailId2022);
		cfdDto.setContractedFieldDetailId(contractedFieldDetailId);
		cfdDto.setDisplayOrder(1);
		cfdDto.setIsLeasedInd(false);
		cfdDto.setGrowerContractYearId(growerContractYearId);

		cfdDao.insertDataSync(cfdDto, userId);
	}	
	
	private void createPolicy(
			Integer cropYear,
			String policyNumber) throws DaoException {
		String userId = "JUNIT_TEST";

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transDate = cal.getTime();		

		// Policy
		PolicyDao policyDao = persistenceSpringConfig.policyDao();
		
		PolicyDto policyDto = new PolicyDto();
		
		policyDto.setPolicyId(policyId);
		policyDto.setGrowerId(growerId);
		policyDto.setInsurancePlanId(4);
		policyDto.setPolicyStatusCode("ACTIVE");
		policyDto.setOfficeId(1);
		policyDto.setPolicyNumber(policyNumber);
		policyDto.setContractNumber(contractNumber);
		policyDto.setContractId(contractId);
		policyDto.setCropYear(cropYear);
		policyDto.setDataSyncTransDate(transDate);

		//INSERT
		policyDao.insert(policyDto, userId);
	}	
	
	private void createGrowerContractYear(Integer cropYear) throws DaoException {
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
		newDto.setGrowerId(growerId);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setCropYear(cropYear);
		newDto.setDataSyncTransDate(dateTime);

		dao.insert(newDto, userId);
	}
}
