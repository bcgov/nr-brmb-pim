package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class VerifiedYieldContractDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	private Integer growerContractYearId = 99999999;
	private Integer contractId = 888888888;
	private Integer cropYear = 2020;
	private String declaredYieldContractGuid;
	private String verifiedYieldContractGuid;

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{

		VerifiedYieldContractDao vycDao = persistenceSpringConfig.verifiedYieldContractDao();
		VerifiedYieldContractDto vycDto = vycDao.fetch(verifiedYieldContractGuid);
		if (vycDto != null) {
			vycDao.delete(verifiedYieldContractGuid);
		}
		
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
	
	
	@Test
	public void testVerifiedYieldContract() throws Exception {

		String userId = "UNITTEST";

		createGrowerContractYear();
		createDeclaredYieldContract(userId);
		
		VerifiedYieldContractDao dao = persistenceSpringConfig.verifiedYieldContractDao();
		
		VerifiedYieldContractDto newDto = new VerifiedYieldContractDto();

		newDto.setContractId(contractId);
		newDto.setCropYear(cropYear);
		newDto.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto.setDefaultYieldMeasUnitTypeCode("TONNE");
		newDto.setGrowerContractYearId(growerContractYearId);
		newDto.setInsurancePlanId(4);		
		
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getVerifiedYieldContractGuid());

		verifiedYieldContractGuid = newDto.getVerifiedYieldContractGuid();
		
		//FETCH BY GCY
		VerifiedYieldContractDto fetchedDto = dao.getByGrowerContract(growerContractYearId);
		
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("DeclaredYieldContractGuid", newDto.getDeclaredYieldContractGuid(), fetchedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("DefaultYieldMeasUnitTypeCode", newDto.getDefaultYieldMeasUnitTypeCode(), fetchedDto.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals("GrowerContractYearId", newDto.getGrowerContractYearId(), fetchedDto.getGrowerContractYearId());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("VerifiedYieldContractGuid", newDto.getVerifiedYieldContractGuid(), fetchedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("VerifiedYieldUpdateUser", userId, fetchedDto.getVerifiedYieldUpdateUser());
		Assert.assertNotNull("VerifiedYieldUpdateTimestamp", fetchedDto.getVerifiedYieldUpdateTimestamp());

		//FETCH BY CN and YEAR
		fetchedDto = dao.getByContractAndYear(contractId, cropYear);

		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("DeclaredYieldContractGuid", newDto.getDeclaredYieldContractGuid(), fetchedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("DefaultYieldMeasUnitTypeCode", newDto.getDefaultYieldMeasUnitTypeCode(), fetchedDto.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals("GrowerContractYearId", newDto.getGrowerContractYearId(), fetchedDto.getGrowerContractYearId());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("VerifiedYieldContractGuid", newDto.getVerifiedYieldContractGuid(), fetchedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("VerifiedYieldUpdateUser", userId, fetchedDto.getVerifiedYieldUpdateUser());
		Assert.assertNotNull("VerifiedYieldUpdateTimestamp", fetchedDto.getVerifiedYieldUpdateTimestamp());
		
		//FETCH
		fetchedDto = dao.fetch(verifiedYieldContractGuid);

		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("DeclaredYieldContractGuid", newDto.getDeclaredYieldContractGuid(), fetchedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("DefaultYieldMeasUnitTypeCode", newDto.getDefaultYieldMeasUnitTypeCode(), fetchedDto.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals("GrowerContractYearId", newDto.getGrowerContractYearId(), fetchedDto.getGrowerContractYearId());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("VerifiedYieldContractGuid", newDto.getVerifiedYieldContractGuid(), fetchedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("VerifiedYieldUpdateUser", userId, fetchedDto.getVerifiedYieldUpdateUser());
		Assert.assertNotNull("VerifiedYieldUpdateTimestamp", fetchedDto.getVerifiedYieldUpdateTimestamp());
		
		
		//UPDATE
		fetchedDto.setDefaultYieldMeasUnitTypeCode("BUSHEL");

		dao.update(fetchedDto, userId);

		//FETCH
		VerifiedYieldContractDto updatedDto = dao.fetch(verifiedYieldContractGuid);

		Assert.assertEquals("ContractId", fetchedDto.getContractId(), updatedDto.getContractId());
		Assert.assertEquals("CropYear", fetchedDto.getCropYear(), updatedDto.getCropYear());
		Assert.assertEquals("DeclaredYieldContractGuid", fetchedDto.getDeclaredYieldContractGuid(), updatedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("DefaultYieldMeasUnitTypeCode", fetchedDto.getDefaultYieldMeasUnitTypeCode(), updatedDto.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals("GrowerContractYearId", fetchedDto.getGrowerContractYearId(), updatedDto.getGrowerContractYearId());
		Assert.assertEquals("InsurancePlanId", fetchedDto.getInsurancePlanId(), updatedDto.getInsurancePlanId());
		Assert.assertEquals("VerifiedYieldContractGuid", fetchedDto.getVerifiedYieldContractGuid(), updatedDto.getVerifiedYieldContractGuid());
		Assert.assertEquals("VerifiedYieldUpdateUser", userId, updatedDto.getVerifiedYieldUpdateUser());
		Assert.assertNotEquals("VerifiedYieldUpdateTimestamp", fetchedDto.getVerifiedYieldUpdateTimestamp(), updatedDto.getVerifiedYieldUpdateTimestamp());
								
		//DELETE
		dao.delete(verifiedYieldContractGuid);
		
		//FETCH
		VerifiedYieldContractDto deletedDto = dao.fetch(verifiedYieldContractGuid);
		Assert.assertNull(deletedDto);

		//FETCH BY GCY
		deletedDto = dao.getByGrowerContract(growerContractYearId);
		Assert.assertNull(deletedDto);

		//FETCH BY CN and YEAR
		deletedDto = dao.getByContractAndYear(contractId, cropYear);
		Assert.assertNull(deletedDto);
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
	
}
