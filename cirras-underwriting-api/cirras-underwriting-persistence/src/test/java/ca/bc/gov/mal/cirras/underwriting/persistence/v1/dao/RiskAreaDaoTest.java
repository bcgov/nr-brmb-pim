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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandRiskAreaXrefDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.RiskAreaDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class RiskAreaDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer riskAreaId = null;
	private Integer insurancePlanId = 4;
	private String insurancePlanName = "GRAIN";
	private Integer insurancePlanId2 = 5;

	private Integer legalLandId = 99999995;
	private Integer legalLandId2 = 888888884;
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{

		//Delete LegalLandRiskAreaXref and risk area
		deleteLegalLandRiskAreaXref(legalLandId);
		deleteLegalLandRiskAreaXref(legalLandId2);

		//Delete legal land
		deleteLegalLand();
	
	}
	
	private void deleteLegalLand() throws NotFoundDaoException, DaoException {

		LegalLandDao dao = persistenceSpringConfig.legalLandDao();
		LegalLandDto dto = dao.fetch(legalLandId);
		if (dto != null) {
			dao.delete(legalLandId);
		}
		dto = dao.fetch(legalLandId2);
		if (dto != null) {
			dao.delete(legalLandId2);
		}
	}

	public void deleteLegalLandRiskAreaXref(Integer legalLandId)
			throws DaoException, NotFoundDaoException {

		LegalLandRiskAreaXrefDao xrefDao = persistenceSpringConfig.legalLandRiskAreaXrefDao();

		RiskAreaDao dao = persistenceSpringConfig.riskAreaDao();
		List<RiskAreaDto> dtos = dao.selectByLegalLand(legalLandId);

		if(dtos != null && dtos.size() > 0) {
			for (RiskAreaDto dto : dtos) {
				xrefDao.delete(dto.getLegalLandId(), dto.getRiskAreaId());
				dao.delete(dto.getRiskAreaId());
			}
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteRiskArea() throws Exception {

		String userId = "JUNIT_TEST";
		
		RiskAreaDao dao = persistenceSpringConfig.riskAreaDao();
		RiskAreaDto newDto = new RiskAreaDto();
		
		String riskAreaName = "Risk Area Name";
		String description = "Test Risk Area";
		
		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());;
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);
		
		//INSERT
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setRiskAreaName(riskAreaName);
		newDto.setDescription(description);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);

		dao.insert(newDto, userId);
		
		riskAreaId = newDto.getRiskAreaId();

		//FETCH
		RiskAreaDto fetchedDto = dao.fetch(riskAreaId);
		
		Assert.assertEquals("RiskAreaId", newDto.getRiskAreaId(), fetchedDto.getRiskAreaId());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("RiskAreaName", newDto.getRiskAreaName(), fetchedDto.getRiskAreaName());
		Assert.assertEquals("Description", newDto.getDescription(), fetchedDto.getDescription());
		Assert.assertEquals("EffectiveDate", newDto.getEffectiveDate(), fetchedDto.getEffectiveDate());
		Assert.assertEquals("ExpiryDate", newDto.getExpiryDate(), fetchedDto.getExpiryDate());

		//UPDATE
		riskAreaName = "Risk Area Name 2";
		description = "Test Risk Area 2";
		effectiveDate = addDays(date, -2);
		expiryDate = addDays(date, 2);
		
		fetchedDto.setInsurancePlanId(insurancePlanId2);
		fetchedDto.setRiskAreaName(riskAreaName);
		fetchedDto.setDescription(description);
		fetchedDto.setEffectiveDate(effectiveDate);
		fetchedDto.setExpiryDate(expiryDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		RiskAreaDto updatedDto = dao.fetch(riskAreaId);
		Assert.assertEquals("InsurancePlanId", fetchedDto.getInsurancePlanId(), updatedDto.getInsurancePlanId());
		Assert.assertEquals("Description", fetchedDto.getDescription(), updatedDto.getDescription());
		Assert.assertEquals("RiskAreaName", fetchedDto.getRiskAreaName(), updatedDto.getRiskAreaName());
		Assert.assertEquals("EffectiveDate", fetchedDto.getEffectiveDate(), updatedDto.getEffectiveDate());
		Assert.assertEquals("ExpiryDate", fetchedDto.getExpiryDate(), updatedDto.getExpiryDate());
		
		//DELETE
		dao.delete(riskAreaId);

		//FETCH
		RiskAreaDto deletedDto = dao.fetch(riskAreaId);
		Assert.assertNull(deletedDto);

	}
	
	@Test 
	public void testRiskAreaSelect() throws Exception {

		Integer planId = null;
		String planName = null;
		Integer totalPlanRecords = 0;

		RiskAreaDao dao = persistenceSpringConfig.riskAreaDao();
		//Get all records
		List<RiskAreaDto> dtos = dao.select(null);

		Assert.assertNotNull(dtos);
		
		for(RiskAreaDto dto : dtos) {
			if(planId == null) {
				//set a plan id (doesn't matter which one)
				planId = dto.getInsurancePlanId();
				planName = dto.getInsurancePlanName();
			}
			if(planId.equals(dto.getInsurancePlanId())) {
				//store total records of planId
				totalPlanRecords++;
			}
		}
		
		dtos = dao.select(planId);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(totalPlanRecords.intValue(), dtos.size());
		
		for (RiskAreaDto dto : dtos) {
			Assert.assertEquals(planId, dto.getInsurancePlanId());
			Assert.assertEquals(planName, dto.getInsurancePlanName());
		}
		
	}	

	
	@Test 
	public void testRiskAreaSelectByLegalLand() throws Exception {

		// Add legal land
		String legalLocation = "Test Legal 1234";
		String legalLocation2 = legalLocation + " test";
		String legalDescription = "Legal Description";
		String legalDescription2 = "Legal Description 2";
		Integer cropYear = 2022;

		createLegalLand(legalLocation, legalDescription, legalLandId, cropYear, "111-222-333");
		createLegalLand(legalLocation2, legalDescription2, legalLandId2, cropYear, "111-222-333");
		
		//Add Risk Areas
		String riskAreaName = "Risk Area 1";
		String riskAreaName2 = "Risk Area 2";
		String riskAreaName3 = "Risk Area 3";
		String riskAreaDescription = "Risk Area Desc 1";
		String riskAreaDescription2 = "Risk Area Desc 2";
		String riskAreaDescription3 = "Risk Area Desc 3";

		Integer riskAreaId = createRiskArea(riskAreaName, riskAreaDescription);
		Integer riskAreaId2 = createRiskArea(riskAreaName2, riskAreaDescription2);
		Integer riskAreaId3 = createRiskArea(riskAreaName3, riskAreaDescription3);

		createLegalLandRiskAreaXref(legalLandId, riskAreaId);
		createLegalLandRiskAreaXref(legalLandId, riskAreaId2);
		createLegalLandRiskAreaXref(legalLandId2, riskAreaId3);
		
		
		RiskAreaDao dao = persistenceSpringConfig.riskAreaDao();
		List<RiskAreaDto> dtos = dao.selectByLegalLand(legalLandId);
		
		Assert.assertEquals(2, dtos.size()); //Expected 2
		
		for (RiskAreaDto dto : dtos) {
			if(dto.getRiskAreaId().equals(riskAreaId)) {
				assertRiskArea(riskAreaName, riskAreaDescription, legalLandId, dto);
			}else if (dto.getRiskAreaId().equals(riskAreaId2)) {
				assertRiskArea(riskAreaName2, riskAreaDescription2, legalLandId, dto);
			} else {
				Assert.fail("Unexpected risk area for legal land: " + dto.getRiskAreaName() );
			}
		}
		
		dtos = dao.selectByLegalLand(legalLandId2);
		
		Assert.assertEquals(1, dtos.size()); //Expected 1
		
		RiskAreaDto dto = dtos.get(0);
		
		if(dto.getRiskAreaId().equals(riskAreaId3)) {
			assertRiskArea(riskAreaName3, riskAreaDescription3, legalLandId2, dto);
		} else {
			Assert.fail("Unexpected risk area for legal land: " + dto.getRiskAreaName() );
		}
		
		delete();
	}

	public void assertRiskArea(String riskAreaName, String riskAreaDescription, Integer legalLandId, RiskAreaDto dto) {
		Assert.assertEquals("InsurancePlanId", insurancePlanId, dto.getInsurancePlanId());
		Assert.assertEquals("RiskAreaName", riskAreaName, dto.getRiskAreaName());
		Assert.assertEquals("Description", riskAreaDescription, dto.getDescription());
		Assert.assertEquals("LegalLandId", legalLandId, dto.getLegalLandId());
		Assert.assertEquals("InsurancePlanName", insurancePlanName, dto.getInsurancePlanName());
	}
	
	private Integer createRiskArea(String riskAreaName, String description) throws DaoException {
		
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());;
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);
		
		String userId = "JUNIT_TEST";
		
		RiskAreaDao dao = persistenceSpringConfig.riskAreaDao();
		RiskAreaDto newDto = new RiskAreaDto();
		
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setRiskAreaName(riskAreaName);
		newDto.setDescription(description);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);

		dao.insert(newDto, userId);
		
		return newDto.getRiskAreaId();

	}
	
	private void createLegalLandRiskAreaXref(Integer legalLandId, Integer riskAreaId) throws DaoException {
		
		String userId = "JUNIT_TEST";
		
		LegalLandRiskAreaXrefDao dao = persistenceSpringConfig.legalLandRiskAreaXrefDao();
		LegalLandRiskAreaXrefDto newDto = new LegalLandRiskAreaXrefDto();
		
		newDto.setLegalLandId(legalLandId);
		newDto.setRiskAreaId(riskAreaId);
		newDto.setActiveFromCropYear(2021);
		newDto.setActiveToCropYear(2030);

		dao.insert(newDto, userId);
		
	}
	
	private void createLegalLand(
			String legalLocation, 
			String legalDescription, 
			Integer llId, 
			Integer activeToCropYear, 
			String primaryPropertyIdentifier
	) throws DaoException {

		LegalLandDao dao = persistenceSpringConfig.legalLandDao();
		LegalLandDto newDto = new LegalLandDto();

		String primaryReferenceTypeCode = "OTHER";
		String legalShortDescription = "Short Legal";
		String otherDescription = legalLocation;
		Integer activeFromCropYear = 2011;
		Double totalAcres = 12.3;
		String primaryLandIdentifierTypeCode = "PID";


		String userId = "JUNIT_TEST";

		// INSERT
		newDto.setLegalLandId(llId);
		newDto.setPrimaryPropertyIdentifier(primaryPropertyIdentifier);
		newDto.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		newDto.setLegalDescription(legalDescription);
		newDto.setLegalShortDescription(legalShortDescription);
		newDto.setOtherDescription(otherDescription);
		newDto.setActiveFromCropYear(activeFromCropYear);
		newDto.setActiveToCropYear(activeToCropYear);
		newDto.setTotalAcres(totalAcres);
		newDto.setPrimaryLandIdentifierTypeCode(primaryLandIdentifierTypeCode);

		dao.insertDataSync(newDto, userId);
	}	

	private static Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
}
