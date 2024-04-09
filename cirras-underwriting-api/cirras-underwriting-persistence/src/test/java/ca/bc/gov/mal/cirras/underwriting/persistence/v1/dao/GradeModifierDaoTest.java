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
public class GradeModifierDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String gradeModifierGuid1 = null;
	private String gradeModifierGuid2 = null;
	private String gradeModifierGuid3 = null;
	// Set to years without existing data.
	private Integer cropYear1 = 2022;
	private Integer cropYear2 = 2021;

	private String gradeModifierTypeCode1 = "TST1";
	private String gradeModifierTypeCode2 = "TST2";
	private String gradeModifierTypeCode3 = "TST3";
	private String gradeModifierTypeDesc = "Modifier";

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
		
		deleteDopData();
		
		GradeModifierDao dao = persistenceSpringConfig.gradeModifierDao();
		GradeModifierDto dto = null;

		if (gradeModifierGuid1 != null) {
			dto = dao.fetch(gradeModifierGuid1);

			if (dto != null) {
				dao.delete(gradeModifierGuid1);
			}
		}

		if (gradeModifierGuid2 != null) {
			dto = dao.fetch(gradeModifierGuid2);
	
			if (dto != null) {
				dao.delete(gradeModifierGuid2);
			}	
		}

		if (gradeModifierGuid3 != null) {
			dto = dao.fetch(gradeModifierGuid3);
	
			if (dto != null) {
				dao.delete(gradeModifierGuid3);
			}	
		}
		
		deleteGradeModifierTypeCode(gradeModifierTypeCode1);
		deleteGradeModifierTypeCode(gradeModifierTypeCode2);
		deleteGradeModifierTypeCode(gradeModifierTypeCode3);
		
		deleteUnderwritingYear(cropYear1);
		deleteUnderwritingYear(cropYear2);

	}
	
	private void deleteDopData() throws DaoException {
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
	public void testInsertUpdateDeleteGradeModifier() throws Exception {

		String userId = "JUNIT_TEST";
		
		//Create underwriting year
		createUnderwritingYear(cropYear1);

		GradeModifierDao dao = persistenceSpringConfig.gradeModifierDao();
		GradeModifierDto newDto = new GradeModifierDto();
				
		List<GradeModifierDto> dtos = dao.fetchAll();
		Assert.assertNotNull(dtos);
		int totalGm = dtos.size();
		
		//INSERT
		newDto.setCropCommodityId(16);
		newDto.setCropYear(cropYear1);
		newDto.setGradeModifierGuid(null);
		newDto.setGradeModifierTypeCode("BLYSAMPLE");
		newDto.setGradeModifierValue(12.3456);

		dao.insert(newDto, userId);

		gradeModifierGuid1 = newDto.getGradeModifierGuid();
		
		//FETCH
		GradeModifierDto fetchedDto = dao.fetch(newDto.getGradeModifierGuid());

		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("GradeModifierGuid", newDto.getGradeModifierGuid(), fetchedDto.getGradeModifierGuid());
		Assert.assertEquals("GradeModifierTypeCode", newDto.getGradeModifierTypeCode(), fetchedDto.getGradeModifierTypeCode());
		Assert.assertEquals("GradeModifierValue", newDto.getGradeModifierValue(), fetchedDto.getGradeModifierValue());

		// Extended columns not populated on fetch.
		Assert.assertNull(fetchedDto.getDescription());
		Assert.assertNull(fetchedDto.getInsurancePlanId());

		//UPDATE
		fetchedDto.setGradeModifierValue(65.4321);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		GradeModifierDto updatedDto = dao.fetch(fetchedDto.getGradeModifierGuid());

		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropYear", fetchedDto.getCropYear(), updatedDto.getCropYear());
		Assert.assertEquals("GradeModifierGuid", fetchedDto.getGradeModifierGuid(), updatedDto.getGradeModifierGuid());
		Assert.assertEquals("GradeModifierTypeCode", fetchedDto.getGradeModifierTypeCode(), updatedDto.getGradeModifierTypeCode());
		Assert.assertEquals("GradeModifierValue", fetchedDto.getGradeModifierValue(), updatedDto.getGradeModifierValue());

		// Extended columns not populated on fetch.
		Assert.assertNull(updatedDto.getDescription());
		Assert.assertNull(updatedDto.getInsurancePlanId());

		// Fetch All
		dtos = dao.fetchAll();		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(totalGm + 1, dtos.size());
		
		//DELETE
		dao.delete(gradeModifierGuid1);
		gradeModifierGuid1 = null;

		//FETCH
		GradeModifierDto deletedDto = dao.fetch(updatedDto.getGradeModifierGuid());
		Assert.assertNull(deletedDto);

	}
	
	@Test 
	public void testGradeModifierSelectByYearPlanCommodity() throws Exception {
		
		String userId = "JUNIT_TEST";
		Integer canolaCropId = 18;

		//Create underwriting year
		createUnderwritingYear(cropYear1);
		createUnderwritingYear(cropYear2);

		//Add grade modifier codes
		createGradeModifierTypeCode(gradeModifierTypeCode1, 2000, null);
		createGradeModifierTypeCode(gradeModifierTypeCode2, 2000, null);
		createGradeModifierTypeCode(gradeModifierTypeCode3, 2000, null);

		//Create dop data
		createGrowerContractYear(cropYear1);
		createDeclaredYieldContract(cropYear1);
		createDeclaredYieldContractCommodity(gradeModifierTypeCode1);
		
		GradeModifierDao dao = persistenceSpringConfig.gradeModifierDao();
		
		List<GradeModifierDto> dtos = dao.selectByYearPlanCommodity(cropYear1, 4, null);
		Assert.assertEquals(0,  dtos.size());

		// Grade Modifier 1
		GradeModifierDto gmDto1 = new GradeModifierDto();

		gmDto1.setCropCommodityId(16);
		gmDto1.setCropYear(cropYear1);
		gmDto1.setGradeModifierGuid(null);
		gmDto1.setGradeModifierTypeCode(gradeModifierTypeCode1);
		gmDto1.setGradeModifierValue(12.3456);

		gmDto1.setDescription(gradeModifierTypeDesc);
		gmDto1.setInsurancePlanId(4);
		
		dao.insert(gmDto1, userId);

		gradeModifierGuid1 = gmDto1.getGradeModifierGuid();

		// Grade Modifier 2
		GradeModifierDto gmDto2 = new GradeModifierDto();

		//INSERT
		gmDto2.setCropCommodityId(canolaCropId);
		gmDto2.setCropYear(cropYear1);
		gmDto2.setGradeModifierGuid(null);
		gmDto2.setGradeModifierTypeCode(gradeModifierTypeCode2);
		gmDto2.setGradeModifierValue(11.22);

		gmDto2.setDescription(gradeModifierTypeDesc);
		gmDto2.setInsurancePlanId(4);
		
		dao.insert(gmDto2, userId);

		gradeModifierGuid2 = gmDto2.getGradeModifierGuid();

		// Grade Modifier 3
		GradeModifierDto gmDto3 = new GradeModifierDto();

		//INSERT
		gmDto3.setCropCommodityId(canolaCropId);
		gmDto3.setCropYear(cropYear1);
		gmDto3.setGradeModifierGuid(null);
		gmDto3.setGradeModifierTypeCode(gradeModifierTypeCode3);
		gmDto3.setGradeModifierValue(33.44);

		gmDto3.setDescription(gradeModifierTypeDesc);
		gmDto3.setInsurancePlanId(4);
		
		dao.insert(gmDto3, userId);

		gradeModifierGuid3 = gmDto3.getGradeModifierGuid();
		
		dtos = dao.selectByYearPlanCommodity(cropYear1, 4, null);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(3, dtos.size());

		// Grade Modifier 1
		Assert.assertEquals("CropCommodityId", gmDto1.getCropCommodityId(), dtos.get(0).getCropCommodityId());
		Assert.assertEquals("CropYear", gmDto1.getCropYear(), dtos.get(0).getCropYear());
		Assert.assertEquals("GradeModifierGuid", gmDto1.getGradeModifierGuid(), dtos.get(0).getGradeModifierGuid());
		Assert.assertEquals("GradeModifierTypeCode", gmDto1.getGradeModifierTypeCode(), dtos.get(0).getGradeModifierTypeCode());
		Assert.assertEquals("GradeModifierValue", gmDto1.getGradeModifierValue(), dtos.get(0).getGradeModifierValue());
		Assert.assertEquals("TotalUsed", 1, dtos.get(0).getTotalUsed().intValue());

		// Extended columns are populated on select.
		Assert.assertEquals("Description", gmDto1.getDescription(), dtos.get(0).getDescription());
		Assert.assertEquals("InsurancePlanId", gmDto1.getInsurancePlanId(), dtos.get(0).getInsurancePlanId());
		
		// Grade Modifier 2
		Assert.assertEquals("CropCommodityId", gmDto2.getCropCommodityId(), dtos.get(1).getCropCommodityId());
		Assert.assertEquals("CropYear", gmDto2.getCropYear(), dtos.get(1).getCropYear());
		Assert.assertEquals("GradeModifierGuid", gmDto2.getGradeModifierGuid(), dtos.get(1).getGradeModifierGuid());
		Assert.assertEquals("GradeModifierTypeCode", gmDto2.getGradeModifierTypeCode(), dtos.get(1).getGradeModifierTypeCode());
		Assert.assertEquals("GradeModifierValue", gmDto2.getGradeModifierValue(), dtos.get(1).getGradeModifierValue());
		Assert.assertEquals("TotalUsed", 0, dtos.get(1).getTotalUsed().intValue());

		// Extended columns are populated on select.
		Assert.assertEquals("Description", gmDto2.getDescription(), dtos.get(1).getDescription());
		Assert.assertEquals("InsurancePlanId", gmDto2.getInsurancePlanId(), dtos.get(1).getInsurancePlanId());

		// Grade Modifier 3
		Assert.assertEquals("CropCommodityId", gmDto3.getCropCommodityId(), dtos.get(2).getCropCommodityId());
		Assert.assertEquals("CropYear", gmDto3.getCropYear(), dtos.get(2).getCropYear());
		Assert.assertEquals("GradeModifierGuid", gmDto3.getGradeModifierGuid(), dtos.get(2).getGradeModifierGuid());
		Assert.assertEquals("GradeModifierTypeCode", gmDto3.getGradeModifierTypeCode(), dtos.get(2).getGradeModifierTypeCode());
		Assert.assertEquals("GradeModifierValue", gmDto3.getGradeModifierValue(), dtos.get(2).getGradeModifierValue());
		Assert.assertEquals("TotalUsed", 0, dtos.get(2).getTotalUsed().intValue());

		// Extended columns are populated on select.
		Assert.assertEquals("Description", gmDto3.getDescription(), dtos.get(2).getDescription());
		Assert.assertEquals("InsurancePlanId", gmDto3.getInsurancePlanId(), dtos.get(2).getInsurancePlanId());
		
		//Add commodity filter
		dtos = dao.selectByYearPlanCommodity(cropYear1, 4, canolaCropId);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		for (GradeModifierDto dto : dtos) {
			Assert.assertEquals(canolaCropId, dto.getCropCommodityId());
		}
		
		// Change year filter.
		dtos = dao.selectByYearPlanCommodity(cropYear2, 4, null);
		Assert.assertEquals(0,  dtos.size());
	
		// Change plan filter.
		dtos = dao.selectByYearPlanCommodity(cropYear1, 5, null);
		Assert.assertEquals(0,  dtos.size());
		
		//DELETE
		dao.delete(gradeModifierGuid1);
		gradeModifierGuid1 = null;
	
		//DELETE
		dao.delete(gradeModifierGuid2);
		gradeModifierGuid2 = null;

		//DELETE
		dao.delete(gradeModifierGuid3);
		gradeModifierGuid3 = null;
	
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

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		
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
