package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ContractedFieldDetailDisplayOrderDaoTest {
	
	private Integer fieldId1 = 12999991;
	private Integer fieldId2 = 12999992;
	private Integer fieldId3 = 12999993;
	private Integer fieldId4 = 12999994;
	private Integer annualFieldDetailId1 = 889999991;
	private Integer annualFieldDetailId2 = 889999992;
	private Integer annualFieldDetailId3 = 889999993;
	private Integer annualFieldDetailId4 = 889999994;
	private Integer contractedFieldDetailId1 = 563217891;
	private Integer contractedFieldDetailId2 = 563217892;
	private Integer contractedFieldDetailId3 = 563217893;
	private Integer contractedFieldDetailId4 = 563217894;
	private Integer growerContractYearId = 12999999;
	private Integer contractId = 128888888;
	private Integer policyId = 987454522;
	private Integer cropYear = 2018;
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
		deleteContractedField(contractedFieldDetailId1);		
		deleteContractedField(contractedFieldDetailId2);		
		deleteContractedField(contractedFieldDetailId3);		
		deleteContractedField(contractedFieldDetailId4);		

		//delete annual field
		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		dao.delete(annualFieldDetailId1);
		dao.delete(annualFieldDetailId2);
		dao.delete(annualFieldDetailId3);
		dao.delete(annualFieldDetailId4);

		
		// delete field
		deleteField(fieldId1);
		deleteField(fieldId2);
		deleteField(fieldId3);
		deleteField(fieldId4);
		
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

	public void deleteField(Integer fieldId) throws DaoException, NotFoundDaoException {
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = fieldDao.fetch(fieldId);
		
		if (fieldDto != null) {
			fieldDao.delete(fieldId);
		}
	}

	public void deleteContractedField(Integer contractedFieldDetailId) throws DaoException, NotFoundDaoException {
		ContractedFieldDetailDao cfdDao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto cfdDto = cfdDao.fetch(contractedFieldDetailId);		
		if (cfdDto != null) {			
			cfdDao.delete(contractedFieldDetailId);
		}
	}
	
	@Test
	public void testDisplayOrderUpdates() throws DaoException {
		ContractedFieldDetailDao dao = persistenceSpringConfig.contractedFieldDetailDao();
		
		
		//create grower contract years
		createPolicy(cropYear, "123654-18");
		createGrowerContractYear(cropYear);
		
		//Create Fields
		int[] initialDisplayOrder = {1,3,4,5};
		createField(fieldId1, annualFieldDetailId1, contractedFieldDetailId1, initialDisplayOrder[0]);
		createField(fieldId2, annualFieldDetailId2, contractedFieldDetailId2, initialDisplayOrder[1]);
		createField(fieldId3, annualFieldDetailId3, contractedFieldDetailId3, initialDisplayOrder[2]);
		createField(fieldId4, annualFieldDetailId4, contractedFieldDetailId4, initialDisplayOrder[3]);
		
		List<ContractedFieldDetailDto> dtos = dao.selectForDisplayOrderUpdate(growerContractYearId);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(4, dtos.size());

		//Check initial display order
		int i = 0;
		for (ContractedFieldDetailDto cfdDto : dtos) {
			Assert.assertEquals("Initial Display Order Wrong", (int)initialDisplayOrder[i], (int)cfdDto.getDisplayOrder());
			i++;
		}
		
		//Update each dto if necessary
		i = 1;
		for (ContractedFieldDetailDto cfdDto : dtos) {
			if(!cfdDto.getDisplayOrder().equals(i)) {
				cfdDto.setDisplayOrder(i);
				dao.updateDisplayOrder(cfdDto, userId);
			}
			i++;
		}
		
		//get the records again
		dtos = dao.selectForDisplayOrderUpdate(growerContractYearId);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(4, dtos.size());

		//Check if it's in order without gaps
		i = 1;
		for (ContractedFieldDetailDto cfdDto : dtos) {
			Assert.assertEquals("Display Order has gaps", i, (int)cfdDto.getDisplayOrder());
			i++;
		}
	
	}
	
	
	private void createField(
			Integer fieldId, 
			Integer annualFieldDetailId, 
			Integer contractedFieldDetailId,
			Integer displayOrder) throws DaoException {
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
		
		createAnnualFieldDetail(annualFieldDetailId, fieldId);
		createContractedFieldDetail(contractedFieldDetailId, annualFieldDetailId, displayOrder);
	}	
	
	private void createAnnualFieldDetail(
			Integer annualFieldDetailId,
			Integer fieldId
			) throws DaoException {
		String userId = "JUNIT_TEST";

		// Annual Field Detail
		AnnualFieldDetailDao afdDao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto afdDto = new AnnualFieldDetailDto();

		afdDto.setAnnualFieldDetailId(annualFieldDetailId);
		afdDto.setLegalLandId(null);
		afdDto.setFieldId(fieldId);
		afdDto.setCropYear(cropYear);

		afdDao.insertDataSync(afdDto, userId);
	}
	
	private void createContractedFieldDetail(
			Integer contractedFieldDetailId, 
			Integer annualFieldDetailId,
			Integer displayOrder
		) throws DaoException {
		String userId = "JUNIT_TEST";

		ContractedFieldDetailDao cfdDao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto cfdDto = new ContractedFieldDetailDto();

		// INSERT
		cfdDto.setAnnualFieldDetailId(annualFieldDetailId);
		cfdDto.setContractedFieldDetailId(contractedFieldDetailId);
		cfdDto.setDisplayOrder(displayOrder);
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
