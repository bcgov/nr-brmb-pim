package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class DeclaredYieldContractDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	@Test
	public void testDeclaredYieldContract() throws Exception {

		Integer contractId = 2667;
		Integer cropYear = 2020;
		Integer growerContractYearId = 89023;

		String declaredYieldContractGuid;
		String userId = "UNITTEST";

		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();


		// Dop Update Timestamp
		cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); // Set milliseconds to 0 because they are not set in the database
		Date currTimestamp = cal.getTime();

		Date dopUpdateTimestamp = addSeconds(currTimestamp, -120);
		
		DeclaredYieldContractDao dao = persistenceSpringConfig.declaredYieldContractDao();
		
		DeclaredYieldContractDto newDto = new DeclaredYieldContractDto();

		newDto.setContractId(contractId);
		newDto.setCropYear(cropYear);
		newDto.setDeclarationOfProductionDate(dopDate);
		newDto.setDefaultYieldMeasUnitTypeCode("TONNE");
		// newDto.setDopUpdateTimestamp(dopUpdateTimestamp);
		// newDto.setDopUpdateUser("JSMITH");
		newDto.setEnteredYieldMeasUnitTypeCode("BUSHEL");
		newDto.setGrainFromOtherSourceInd(true);
		newDto.setBalerWagonInfo("Test Insert");
		newDto.setTotalLivestock(100);
		newDto.setInsurancePlanId(4);
		
		
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getDeclaredYieldContractGuid());

		declaredYieldContractGuid = newDto.getDeclaredYieldContractGuid();
		
		//FETCH BY GCY
		DeclaredYieldContractDto fetchedDto = dao.getByGrowerContract(growerContractYearId);
		
		Assert.assertEquals("DeclaredYieldContractGuid", newDto.getDeclaredYieldContractGuid(), fetchedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("DeclarationOfProductionDate", newDto.getDeclarationOfProductionDate(), fetchedDto.getDeclarationOfProductionDate());
		Assert.assertEquals("DefaultYieldMeasUnitTypeCode", newDto.getDefaultYieldMeasUnitTypeCode(), fetchedDto.getDefaultYieldMeasUnitTypeCode());
		Assert.assertNotNull("DopUpdateTimestamp", fetchedDto.getDopUpdateTimestamp());
		Assert.assertNotNull("DopUpdateUser", fetchedDto.getDopUpdateUser());
		Assert.assertEquals("EnteredYieldMeasUnitTypeCode", newDto.getEnteredYieldMeasUnitTypeCode(), fetchedDto.getEnteredYieldMeasUnitTypeCode());
		Assert.assertEquals("GrainFromOtherSourceInd", newDto.getGrainFromOtherSourceInd(), fetchedDto.getGrainFromOtherSourceInd());
		Assert.assertEquals("BalerWagonInfo", newDto.getBalerWagonInfo(), fetchedDto.getBalerWagonInfo());
		Assert.assertEquals("TotalLivestock", newDto.getTotalLivestock(), fetchedDto.getTotalLivestock());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("GrowerContractYearId", fetchedDto.getGrowerContractYearId(), growerContractYearId );

		//FETCH BY CN and YEAR
		fetchedDto = dao.getByContractAndYear(contractId, cropYear);

		Assert.assertEquals("DeclaredYieldContractGuid", newDto.getDeclaredYieldContractGuid(), fetchedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("DeclarationOfProductionDate", newDto.getDeclarationOfProductionDate(), fetchedDto.getDeclarationOfProductionDate());
		Assert.assertEquals("DefaultYieldMeasUnitTypeCode", newDto.getDefaultYieldMeasUnitTypeCode(), fetchedDto.getDefaultYieldMeasUnitTypeCode());
		Assert.assertNotNull("DopUpdateTimestamp", fetchedDto.getDopUpdateTimestamp());
		Assert.assertNotNull("DopUpdateUser", fetchedDto.getDopUpdateUser());
		Assert.assertEquals("EnteredYieldMeasUnitTypeCode", newDto.getEnteredYieldMeasUnitTypeCode(), fetchedDto.getEnteredYieldMeasUnitTypeCode());
		Assert.assertEquals("GrainFromOtherSourceInd", newDto.getGrainFromOtherSourceInd(), fetchedDto.getGrainFromOtherSourceInd());
		Assert.assertEquals("BalerWagonInfo", newDto.getBalerWagonInfo(), fetchedDto.getBalerWagonInfo());
		Assert.assertEquals("TotalLivestock", newDto.getTotalLivestock(), fetchedDto.getTotalLivestock());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("GrowerContractYearId", fetchedDto.getGrowerContractYearId(), growerContractYearId );
		
		//FETCH
		fetchedDto = dao.fetch(declaredYieldContractGuid);
		Assert.assertEquals("DeclaredYieldContractGuid", newDto.getDeclaredYieldContractGuid(), fetchedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("DeclarationOfProductionDate", newDto.getDeclarationOfProductionDate(), fetchedDto.getDeclarationOfProductionDate());
		Assert.assertEquals("DefaultYieldMeasUnitTypeCode", newDto.getDefaultYieldMeasUnitTypeCode(), fetchedDto.getDefaultYieldMeasUnitTypeCode());
		Assert.assertNotNull("DopUpdateTimestamp", fetchedDto.getDopUpdateTimestamp());
		Assert.assertNotNull("DopUpdateUser", fetchedDto.getDopUpdateUser());
		Assert.assertEquals("EnteredYieldMeasUnitTypeCode", newDto.getEnteredYieldMeasUnitTypeCode(), fetchedDto.getEnteredYieldMeasUnitTypeCode());
		Assert.assertEquals("GrainFromOtherSourceInd", newDto.getGrainFromOtherSourceInd(), fetchedDto.getGrainFromOtherSourceInd());
		Assert.assertEquals("BalerWagonInfo", newDto.getBalerWagonInfo(), fetchedDto.getBalerWagonInfo());
		Assert.assertEquals("TotalLivestock", newDto.getTotalLivestock(), fetchedDto.getTotalLivestock());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("GrowerContractYearId", fetchedDto.getGrowerContractYearId(), growerContractYearId );
		
		// Dop Date
//		cal = Calendar.getInstance();
//		cal.clear();
//		cal.set(2020, Calendar.JANUARY, 16);
//		dopDate = cal.getTime();
//
//
//		dopUpdateTimestamp = addSeconds(dopUpdateTimestamp, 60);
		
		
		//UPDATE
		fetchedDto.setDeclarationOfProductionDate(dopDate);
		fetchedDto.setDefaultYieldMeasUnitTypeCode("BUSHEL");
		// fetchedDto.setDopUpdateTimestamp(dopUpdateTimestamp);
		fetchedDto.setDopUpdateUser("JDOE");
		fetchedDto.setEnteredYieldMeasUnitTypeCode("TONNE");
		fetchedDto.setGrainFromOtherSourceInd(false);
		fetchedDto.setBalerWagonInfo("Test Update");
		fetchedDto.setTotalLivestock(200);

		dao.update(fetchedDto, userId);

		//FETCH
		DeclaredYieldContractDto updatedDto = dao.fetch(declaredYieldContractGuid);

		Assert.assertEquals("DeclaredYieldContractGuid", fetchedDto.getDeclaredYieldContractGuid(), updatedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("ContractId", fetchedDto.getContractId(), updatedDto.getContractId());
		Assert.assertEquals("CropYear", fetchedDto.getCropYear(), updatedDto.getCropYear());
		Assert.assertEquals("DeclarationOfProductionDate", fetchedDto.getDeclarationOfProductionDate(), updatedDto.getDeclarationOfProductionDate());
		Assert.assertEquals("DefaultYieldMeasUnitTypeCode", fetchedDto.getDefaultYieldMeasUnitTypeCode(), updatedDto.getDefaultYieldMeasUnitTypeCode());
		//Assert.assertTrue("DopUpdateTimestamp", fetchedDto.getDopUpdateTimestamp().before(updatedDto.getDopUpdateTimestamp()));
		Assert.assertNotEquals("DopUpdateUser", fetchedDto.getDopUpdateUser(), updatedDto.getDopUpdateUser()); 
		Assert.assertEquals("EnteredYieldMeasUnitTypeCode", fetchedDto.getEnteredYieldMeasUnitTypeCode(), updatedDto.getEnteredYieldMeasUnitTypeCode());
		Assert.assertEquals("GrainFromOtherSourceInd", fetchedDto.getGrainFromOtherSourceInd(), updatedDto.getGrainFromOtherSourceInd());
		Assert.assertEquals("BalerWagonInfo", fetchedDto.getBalerWagonInfo(), updatedDto.getBalerWagonInfo());
		Assert.assertEquals("TotalLivestock", fetchedDto.getTotalLivestock(), updatedDto.getTotalLivestock());
		Assert.assertEquals("InsurancePlanId", fetchedDto.getInsurancePlanId(), updatedDto.getInsurancePlanId());
				
		//DELETE
		dao.delete(declaredYieldContractGuid);
		
		//FETCH
		DeclaredYieldContractDto deletedDto = dao.fetch(declaredYieldContractGuid);
		Assert.assertNull(deletedDto);

		//FETCH BY GCY
		deletedDto = dao.getByGrowerContract(growerContractYearId);
		Assert.assertNull(deletedDto);

		//FETCH BY CN and YEAR
		deletedDto = dao.getByContractAndYear(contractId, cropYear);
		Assert.assertNull(deletedDto);
	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	
}
