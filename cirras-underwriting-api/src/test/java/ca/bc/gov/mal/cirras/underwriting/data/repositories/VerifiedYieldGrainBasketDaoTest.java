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

import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldGrainBasketDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
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

	private Integer policyId = 90000015;
	private String policyNumber = "998877-20";
	private String contractNumber = "998877";
		
	private Integer growerId = 90000007;


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

		deletePolicy(policyId);
		deleteGrower(growerId);
		
		GrowerContractYearDao gcyDao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto gcyDto = gcyDao.fetch(growerContractYearId);
		if (gcyDto != null) {
			gcyDao.delete(growerContractYearId);
		}
		
	}

	
	private void deletePolicy(Integer policyId) throws NotFoundDaoException, DaoException{
		
		PolicyDao dao = persistenceSpringConfig.policyDao();
		PolicyDto dto = dao.fetch(policyId);
		if (dto != null) {
			dao.delete(policyId);
		}
	}
	
	private void deleteGrower(Integer growerId) throws DaoException {
		GrowerDao dao = persistenceSpringConfig.growerDao();
		GrowerDto dto = dao.fetch(growerId);
		if (dto != null) {
			dao.delete(growerId);
		}
	}

	@Test 
	public void testVerifiedYieldGrainBasket() throws Exception {

		String verifiedYieldGrainBasketGuid;
		String userId = "UNITTEST";
		Integer insurancePlanId = 4;
		String policyStatusCode = "ACTIVE";
		Integer officeId = 1;

		createGrower(growerId, 999888, "grower name");
		createPolicy(policyId, growerId, contractId, cropYear, policyNumber, contractNumber, insurancePlanId, policyStatusCode, officeId);
		createGrowerContractYear();
		createDeclaredYieldContract(userId);
		createVerifiedYieldContract(userId);
		
		VerifiedYieldGrainBasketDao dao = persistenceSpringConfig.verifiedYieldGrainBasketDao();

		// INSERT
		VerifiedYieldGrainBasketDto newDto = new VerifiedYieldGrainBasketDto();

		newDto.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
		newDto.setBasketValue(200.45);
		newDto.setTotalQuantityCoverageValue(567.89);
		newDto.setTotalCoverageValue(768.34);
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
		Assert.assertEquals("TotalQuantityCoverageValue", newDto.getTotalQuantityCoverageValue(), fetchedDto.getTotalQuantityCoverageValue());
		Assert.assertEquals("TotalCoverageValue", newDto.getTotalCoverageValue(), fetchedDto.getTotalCoverageValue());
		Assert.assertEquals("HarvestedValue", newDto.getHarvestedValue(), fetchedDto.getHarvestedValue());
		Assert.assertEquals("Comment", newDto.getComment(), fetchedDto.getComment());
		
		//FETCH
		fetchedDto = dao.fetch(verifiedYieldGrainBasketGuid);
		
		Assert.assertEquals("VerifiedYieldGrainBasketGuid", newDto.getVerifiedYieldGrainBasketGuid(), fetchedDto.getVerifiedYieldGrainBasketGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", newDto.getVerifiedYieldContractGuid(), fetchedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("BasketValue", newDto.getBasketValue(), fetchedDto.getBasketValue());
		Assert.assertEquals("TotalQuantityCoverageValue", newDto.getTotalQuantityCoverageValue(), fetchedDto.getTotalQuantityCoverageValue());
		Assert.assertEquals("TotalCoverageValue", newDto.getTotalCoverageValue(), fetchedDto.getTotalCoverageValue());
		Assert.assertEquals("HarvestedValue", newDto.getHarvestedValue(), fetchedDto.getHarvestedValue());
		Assert.assertEquals("Comment", newDto.getComment(), fetchedDto.getComment());

		//UPDATE
		fetchedDto.setBasketValue(300.98);
		fetchedDto.setTotalQuantityCoverageValue(987.65);
		fetchedDto.setTotalCoverageValue(1288.63);
		fetchedDto.setHarvestedValue(321.78);
		fetchedDto.setComment("Basket Value Comment 2");
				
		dao.update(fetchedDto, userId);

		//FETCH
		VerifiedYieldGrainBasketDto updatedDto = dao.fetch(verifiedYieldGrainBasketGuid);

		Assert.assertEquals("VerifiedYieldGrainBasketGuid", fetchedDto.getVerifiedYieldGrainBasketGuid(), updatedDto.getVerifiedYieldGrainBasketGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", fetchedDto.getVerifiedYieldContractGuid(), updatedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("BasketValue", fetchedDto.getBasketValue(), updatedDto.getBasketValue());
		Assert.assertEquals("TotalQuantityCoverageValue", fetchedDto.getTotalQuantityCoverageValue(), updatedDto.getTotalQuantityCoverageValue());
		Assert.assertEquals("TotalCoverageValue", fetchedDto.getTotalCoverageValue(), updatedDto.getTotalCoverageValue());
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
		newDto2.setTotalQuantityCoverageValue(11.22);
		newDto2.setTotalCoverageValue(134.67);
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

	private void createGrower(
			Integer growerId,
			Integer growerNumber,
			String growerName
		) throws DaoException {
			String userId = "JUNIT_TEST";

			//Date and Time without millisecond
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
			Date transDate = cal.getTime();

			GrowerDao growerDao = persistenceSpringConfig.growerDao();
			GrowerDto growerDto = new GrowerDto();
			
			//INSERT
			growerDto.setGrowerId(growerId);
			growerDto.setGrowerNumber(growerNumber);
			growerDto.setGrowerName(growerName);
			growerDto.setGrowerAddressLine1("address line 1");
			growerDto.setGrowerAddressLine2("address line 2");
			growerDto.setGrowerPostalCode("V8P 4N8");
			growerDto.setGrowerCity("Victoria");
			growerDto.setCityId(1);
			growerDto.setGrowerProvince("BC");
			growerDto.setDataSyncTransDate(transDate);

			growerDao.insert(growerDto, userId);
		}

		
		private void createPolicy(
				Integer policyId,
				Integer growerId,
				Integer contractId,
				Integer cropYear,
				String policyNumber,
				String contractNumber,
				Integer insurancePlanId,
				String policyStatus,
				Integer officeId) throws DaoException {
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
			policyDto.setInsurancePlanId(insurancePlanId);
			policyDto.setPolicyStatusCode(policyStatus);
			policyDto.setOfficeId(officeId);
			policyDto.setPolicyNumber(policyNumber);
			policyDto.setContractNumber(contractNumber);
			policyDto.setContractId(contractId);
			policyDto.setCropYear(cropYear);
			policyDto.setDataSyncTransDate(transDate);
			
			//INSERT
			policyDao.insert(policyDto, userId);
		}
		
}
