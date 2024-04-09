package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GradeModifierDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GradeModifierTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class GradeModifierTypeCodeDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String gradeModifierGuid1 = null;
	private String gradeModifierTypeCode1 = "TST1";
	private String gradeModifierTypeCode2 = "TST2";
	private String gradeModifierTypeCode3 = "TST3";
	// Set to years without existing data.
	private Integer cropYear1 = 2009;
	private Integer cropYear2 = 2010;
	private Integer cropYear3 = 2011;

	private Integer growerContractYearId = 99999999;
	private Integer contractId = 888888888;
	private String declaredYieldContractGuid;
	private String declaredYieldContractCommodityGuid;

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{
		
		
		//DELETE dop contract commodity
		if(declaredYieldContractGuid != null) {
			DeclaredYieldContractCommodityDao dopContractCommodityDao = persistenceSpringConfig.declaredYieldContractCommodityDao();
			DeclaredYieldContractCommodityDto declaredYieldContractCommodityDto = dopContractCommodityDao.fetch(declaredYieldContractCommodityGuid);
			if(declaredYieldContractCommodityDto != null) {
				dopContractCommodityDao.delete(declaredYieldContractCommodityGuid);
			}
		}
		
		//DELETE Declared Yield Contract
		if(declaredYieldContractGuid != null) {
			DeclaredYieldContractDao dopContractDao = persistenceSpringConfig.declaredYieldContractDao();
			DeclaredYieldContractDto declaredYieldContractDto = dopContractDao.fetch(declaredYieldContractGuid);
			if(declaredYieldContractDto != null) {
				dopContractDao.delete(declaredYieldContractGuid);
			}
		}

		//Delete grower contract year
		GrowerContractYearDao gcyDao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto gcyDto = gcyDao.fetch(growerContractYearId);
		if (gcyDto != null) {
			gcyDao.delete(growerContractYearId);
		}
		
		//Delete Grade Modifier
		GradeModifierDao dao = persistenceSpringConfig.gradeModifierDao();
		GradeModifierDto dto = null;
		if (gradeModifierGuid1 != null) {
			dto = dao.fetch(gradeModifierGuid1);

			if (dto != null) {
				dao.delete(gradeModifierGuid1);
			}
		}
		
		deleteGradeModifierTypeCode(gradeModifierTypeCode1);
		deleteGradeModifierTypeCode(gradeModifierTypeCode2);
		deleteGradeModifierTypeCode(gradeModifierTypeCode3);

		deleteUnderwritingYear(cropYear1);
		deleteUnderwritingYear(cropYear2);

	}
	
	private void deleteGradeModifierTypeCode(String gradeModifierTypeCode) throws DaoException {
		GradeModifierTypeCodeDao dao = persistenceSpringConfig.gradeModifierTypeCodeDao();
		GradeModifierTypeCodeDto dto = null;

		dto = dao.fetch(gradeModifierTypeCode);

		if (dto != null) {
			dao.delete(gradeModifierTypeCode);
		}
	}
	
	private void deleteUnderwritingYear(Integer cropYear) throws DaoException {
		UnderwritingYearDao dao = persistenceSpringConfig.underwritingYearDao();
		UnderwritingYearDto dto = null;
		
		dto = dao.selectByCropYear(cropYear);

		if (dto != null) {
			dao.deleteByCropYear(cropYear);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteGradeModifierTypeCode() throws Exception {

		String userId = "JUNIT_TEST";
		
		//Create underwriting year
		createUnderwritingYear(cropYear1);

		GradeModifierTypeCodeDao dao = persistenceSpringConfig.gradeModifierTypeCodeDao();
		GradeModifierTypeCodeDto newDto = new GradeModifierTypeCodeDto();
				
		List<GradeModifierTypeCodeDto> dtos = dao.fetchAll();
		Assert.assertNotNull(dtos);
		int totalGm = dtos.size();
		
		//INSERT
		newDto.setGradeModifierTypeCode(gradeModifierTypeCode1);
		newDto.setDescription("Modifier 1");
		newDto.setEffectiveYear(cropYear1);
		newDto.setExpiryYear(null);

		dao.insert(newDto, userId);

		//FETCH
		GradeModifierTypeCodeDto fetchedDto = dao.fetch(newDto.getGradeModifierTypeCode());

		Assert.assertEquals("GradeModifierTypeCode", newDto.getGradeModifierTypeCode(), fetchedDto.getGradeModifierTypeCode());
		Assert.assertEquals("Description", newDto.getDescription(), fetchedDto.getDescription());
		Assert.assertEquals("EffectiveYear", newDto.getEffectiveYear(), fetchedDto.getEffectiveYear());
		Assert.assertNull("ExpiryYear", fetchedDto.getExpiryYear());

		//UPDATE
		
		fetchedDto.setDescription("Modifier 1a");
		fetchedDto.setExpiryYear(2015);

		dao.update(fetchedDto, userId);
		
		//FETCH
		GradeModifierTypeCodeDto updatedDto = dao.fetch(fetchedDto.getGradeModifierTypeCode());
		
		Assert.assertEquals("Description", fetchedDto.getDescription(), updatedDto.getDescription());
		Assert.assertEquals("ExpiryYear", fetchedDto.getExpiryYear(), updatedDto.getExpiryYear());

		// Fetch All
		dtos = dao.fetchAll();		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(totalGm + 1, dtos.size());
		
		//DELETE
		dao.delete(gradeModifierTypeCode1);

		//FETCH
		GradeModifierTypeCodeDto deletedDto = dao.fetch(gradeModifierTypeCode1);
		Assert.assertNull(deletedDto);
		
		delete();
	}
	
	@Test 
	public void testGradeModifierTypeCodeSelect() throws Exception {

		//Create underwriting year
		createUnderwritingYear(cropYear1);
		createUnderwritingYear(cropYear2);
		
		GradeModifierTypeCodeDao dao = persistenceSpringConfig.gradeModifierTypeCodeDao();
		
		//Make sure there are no codes for the crop year
		List<GradeModifierTypeCodeDto> dtos = dao.select(cropYear1);
		Assert.assertEquals(0,  dtos.size());
		
		//Create grade modifier type code
		createGradeModifierTypeCode(gradeModifierTypeCode1, cropYear1, cropYear2);

		//Expect 1
		dtos = dao.select(cropYear1);
		Assert.assertEquals(1,  dtos.size());

		createGradeModifierTypeCode(gradeModifierTypeCode2, cropYear2, null);

		//Expect 2
		dtos = dao.select(cropYear2);
		Assert.assertEquals(2,  dtos.size());

		//Check if all records are between cropYear1
		checkGradeModifierTypeCodes(dtos, cropYear2);


		//Expect 1 because of effictive year
		dtos = dao.select(cropYear1);
		Assert.assertEquals(1,  dtos.size());

		//Check if all records are between cropYear1
		checkGradeModifierTypeCodes(dtos, cropYear1);


		//Expect 1 because of expiry year
		dtos = dao.select(cropYear3);
		Assert.assertEquals(1,  dtos.size());

		//Check if all records are between cropYear1
		checkGradeModifierTypeCodes(dtos, cropYear3);
		
		
		//Add grade modifier to test the totalUsed property
		createGradeModifier(gradeModifierTypeCode1, cropYear1);
		
		//Get grade modifier codes: Expect 0 total used and cropYear1 for maxYearUsed
		dtos = dao.select(cropYear1);
		Assert.assertNotNull(dtos);

		GradeModifierTypeCodeDto dto = getGradeModifierTypeCodeDto(dtos, gradeModifierTypeCode1);
		Assert.assertNotNull(dto);
		Assert.assertEquals("TotalUsed", 0, dto.getTotalUsed().intValue());
		Assert.assertEquals("MaxYearUsed", cropYear1, dto.getMaxYearUsed());

		
		createGrowerContractYear(cropYear1);
		createDeclaredYieldContract(cropYear1);
		createDeclaredYieldContractCommodity(gradeModifierTypeCode1);

		//Get grade modifier codes: Expect 1 total used and cropYear1 for maxYearUsed
		dtos = dao.select(cropYear1);
		Assert.assertNotNull(dtos);

		dto = getGradeModifierTypeCodeDto(dtos, gradeModifierTypeCode1);
		Assert.assertNotNull(dto);
		Assert.assertEquals("TotalUsed", 1, dto.getTotalUsed().intValue());	
		Assert.assertEquals("MaxYearUsed", cropYear1, dto.getMaxYearUsed());
		
		//DELETE
		delete();

	
	}
	
	private GradeModifierTypeCodeDto getGradeModifierTypeCodeDto(List<GradeModifierTypeCodeDto> dtos, String gradeModifierTypeCode) {
		
		List<GradeModifierTypeCodeDto> filteredDtos = dtos.stream().filter(x -> x.getGradeModifierTypeCode().equals(gradeModifierTypeCode)).collect(Collectors.toList());
		
		if(filteredDtos != null && filteredDtos.size() == 1) {
			return filteredDtos.get(0);
		}
		return null;
	}

	protected void checkGradeModifierTypeCodes(List<GradeModifierTypeCodeDto> dtos, Integer cropYear) {
		for (GradeModifierTypeCodeDto dto : dtos) {
			Assert.assertTrue("EffectiveYear", dto.getEffectiveYear() <= cropYear);
			if(dto.getExpiryYear() != null) {
				Assert.assertTrue("ExpiryYear", dto.getExpiryYear() >= cropYear);
			}
			Assert.assertEquals("TotalUsed", 0, dto.getTotalUsed().intValue());
			Assert.assertNull("MaxYearUsed", dto.getMaxYearUsed());
		}
	}	

	
	private void createGradeModifier(String gradeModifierTypeCode, Integer cropYear) throws DaoException {
		
		GradeModifierDao dao = persistenceSpringConfig.gradeModifierDao();
				
		// Grade Modifier 1
		GradeModifierDto newDto = new GradeModifierDto();

		newDto.setCropCommodityId(16);
		newDto.setCropYear(cropYear);
		newDto.setGradeModifierGuid(null);
		newDto.setGradeModifierTypeCode(gradeModifierTypeCode);
		newDto.setGradeModifierValue(12.3456);

		newDto.setDescription("Sample");
		newDto.setInsurancePlanId(4);
		
		dao.insert(newDto, "JUNIT_TEST");

		gradeModifierGuid1 = newDto.getGradeModifierGuid();

	}	
	
	private void createGradeModifierTypeCode(String gradeModifierTypeCode, Integer effectiveYear, Integer expiryYear) throws DaoException {
		
		GradeModifierTypeCodeDao dao = persistenceSpringConfig.gradeModifierTypeCodeDao();
		GradeModifierTypeCodeDto newDto = new GradeModifierTypeCodeDto();
				
		newDto.setGradeModifierTypeCode(gradeModifierTypeCode);
		newDto.setDescription("Modifier");
		newDto.setEffectiveYear(effectiveYear);
		newDto.setExpiryYear(expiryYear);

		dao.insert(newDto, "JUNIT_TEST");
	}	
	

	private void createDeclaredYieldContract(Integer cropYear) throws DaoException {

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
		dao.insert(newDto, "JUNIT_TEST");
		declaredYieldContractGuid = newDto.getDeclaredYieldContractGuid();
		
	}

	private void createDeclaredYieldContractCommodity(String gradeModifierTypeCode) throws DaoException {

		DeclaredYieldContractCommodityDao dao = persistenceSpringConfig.declaredYieldContractCommodityDao();
	
		// INSERT
		DeclaredYieldContractCommodityDto newDto = new DeclaredYieldContractCommodityDto();
		newDto.setCropCommodityId(16);
		newDto.setCropCommodityName("BARLEY");
		newDto.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto.setIsPedigreeInd(true);
		newDto.setHarvestedAcres((double)1000);
		newDto.setStoredYield((double)500);
		newDto.setStoredYieldDefaultUnit((double)400);
		newDto.setSoldYield((double)250);
		newDto.setSoldYieldDefaultUnit((double)200);
		newDto.setGradeModifierTypeCode(gradeModifierTypeCode);
		newDto.setTotalInsuredAcres(1.1);
	
		dao.insert(newDto, "JUNIT_TEST");
		Assert.assertNotNull(newDto.getDeclaredYieldContractCommodityGuid());
		declaredYieldContractCommodityGuid = newDto.getDeclaredYieldContractCommodityGuid();
	}
	
	private void createGrowerContractYear(Integer cropYear) throws DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto newDto = new GrowerContractYearDto();
		
		Integer growerId = 525593;
		Integer insurancePlanId = 5;

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
	
	private void createUnderwritingYear(Integer cropYear) throws DaoException {
		
		UnderwritingYearDao dao = persistenceSpringConfig.underwritingYearDao();
		UnderwritingYearDto newDto = new UnderwritingYearDto();
		newDto.setCropYear(cropYear);
		dao.insert(newDto, "JUNIT_TEST");
	}
}
