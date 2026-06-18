package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandRiskAreaXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.RiskAreaDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class LegalLandRiskAreaXrefDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer legalLandId = 987654321;
	private Integer riskAreaId = null;
	private Integer riskAreaId2 = null;

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteLegalLandRiskAreaXref();
	}
	
	private void deleteLegalLandRiskAreaXref() throws NotFoundDaoException, DaoException{
		
		// delete legal land - risk area xref table
		LegalLandRiskAreaXrefDao dao = persistenceSpringConfig.legalLandRiskAreaXrefDao();
		LegalLandRiskAreaXrefDto dto = dao.fetch(legalLandId, riskAreaId);
		if (dto != null) {
			dao.delete(legalLandId, riskAreaId);
		}

		dto = dao.fetch(legalLandId, riskAreaId2);
		if (dto != null) {
			dao.delete(legalLandId, riskAreaId2);
		}
		
		//delete legal land
		LegalLandDao legalLandDao = persistenceSpringConfig.legalLandDao();
		LegalLandDto legalLandDto = legalLandDao.fetch(legalLandId);
		if (legalLandDto != null) {
			legalLandDao.delete(legalLandId);
		}
		
		// delete risk area
		RiskAreaDao riskAreaDao = persistenceSpringConfig.riskAreaDao();
		RiskAreaDto riskAreaDto = riskAreaDao.fetch(riskAreaId);
		if (riskAreaDto != null) {
			riskAreaDao.delete(riskAreaId);
		}
		
		riskAreaDto = riskAreaDao.fetch(riskAreaId2);
		if (riskAreaDto != null) {
			riskAreaDao.delete(riskAreaId2);
		}
		
	}
	
	@Test 
	public void testInsertDeleteLegalLandFieldXref() throws Exception {
		
		LegalLandDao legalLandDao = persistenceSpringConfig.legalLandDao();
		LegalLandDto newlegalLandDto = new LegalLandDto();
		
		RiskAreaDao raDao = persistenceSpringConfig.riskAreaDao();
		RiskAreaDto raDto = new RiskAreaDto();
		
		
		LegalLandRiskAreaXrefDao dao = persistenceSpringConfig.legalLandRiskAreaXrefDao();
		LegalLandRiskAreaXrefDto newDto = new LegalLandRiskAreaXrefDto();

		String userId = "JUNIT_TEST";
		
		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());;
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);

		//INSERT Legal Land
		newlegalLandDto.setLegalLandId(legalLandId);
		newlegalLandDto.setPrimaryReferenceTypeCode("OTHER");
		newlegalLandDto.setLegalDescription("Legal Description");
		newlegalLandDto.setLegalShortDescription("Short Legal");
		newlegalLandDto.setOtherDescription("Other Description");
		newlegalLandDto.setActiveFromCropYear(2011);
		newlegalLandDto.setActiveToCropYear(2022);

		legalLandDao.insertDataSync(newlegalLandDto, userId);

		// INSERT RISK AREA
		raDto.setInsurancePlanId(4);
		raDto.setRiskAreaName("Risk Area Name");
		raDto.setDescription("Test Risk Area");
		raDto.setEffectiveDate(effectiveDate);
		raDto.setExpiryDate(expiryDate);

		raDao.insert(raDto, userId);
		
		riskAreaId = raDto.getRiskAreaId();
		
		// INSERT legal land - field xref 
		newDto.setLegalLandId(legalLandId);
		newDto.setRiskAreaId(riskAreaId);
		newDto.setActiveFromCropYear(2021);
		newDto.setActiveToCropYear(2030);

		dao.insert(newDto, userId);
		
		//FETCH
		LegalLandRiskAreaXrefDto fetchedDto = dao.fetch(legalLandId, riskAreaId);

		Assert.assertEquals("LegalLandId", newDto.getLegalLandId(), fetchedDto.getLegalLandId());
		Assert.assertEquals("RiskAreaId", newDto.getRiskAreaId(), fetchedDto.getRiskAreaId());
		Assert.assertEquals("ActiveFromCropYear", newDto.getActiveFromCropYear(), fetchedDto.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear", newDto.getActiveToCropYear(), fetchedDto.getActiveToCropYear());

		//UPDATE
		fetchedDto.setActiveFromCropYear(2023);
		fetchedDto.setActiveToCropYear(2025);
		
		dao.update(fetchedDto, userId);

		LegalLandRiskAreaXrefDto updatedDto = dao.fetch(legalLandId, riskAreaId);

		Assert.assertEquals("ActiveFromCropYear", fetchedDto.getActiveFromCropYear(), updatedDto.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear", fetchedDto.getActiveToCropYear(), updatedDto.getActiveToCropYear());

		//DELETE
		dao.delete(legalLandId, riskAreaId);

		//FETCH
		LegalLandRiskAreaXrefDto deletedDto = dao.fetch(legalLandId, riskAreaId);
		Assert.assertNull(deletedDto);
		
		//clean up
		deleteLegalLandRiskAreaXref();
		
	}

	@Test 
	public void testDeleteForLegalLand() throws Exception {
		
		LegalLandDao legalLandDao = persistenceSpringConfig.legalLandDao();
		LegalLandDto newlegalLandDto = new LegalLandDto();
		
		RiskAreaDao raDao = persistenceSpringConfig.riskAreaDao();
		RiskAreaDto raDto = new RiskAreaDto();
		
		
		LegalLandRiskAreaXrefDao dao = persistenceSpringConfig.legalLandRiskAreaXrefDao();
		LegalLandRiskAreaXrefDto newDto = new LegalLandRiskAreaXrefDto();

		String userId = "JUNIT_TEST";
		
		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());;
		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);

		//INSERT Legal Land
		newlegalLandDto.setLegalLandId(legalLandId);
		newlegalLandDto.setPrimaryReferenceTypeCode("OTHER");
		newlegalLandDto.setLegalDescription("Legal Description");
		newlegalLandDto.setLegalShortDescription("Short Legal");
		newlegalLandDto.setOtherDescription("Other Description");
		newlegalLandDto.setActiveFromCropYear(2011);
		newlegalLandDto.setActiveToCropYear(2022);

		legalLandDao.insertDataSync(newlegalLandDto, userId);

		// INSERT RISK AREA
		raDto.setInsurancePlanId(4);
		raDto.setRiskAreaName("Risk Area Name");
		raDto.setDescription("Test Risk Area");
		raDto.setEffectiveDate(effectiveDate);
		raDto.setExpiryDate(expiryDate);

		raDao.insert(raDto, userId);
		
		riskAreaId = raDto.getRiskAreaId();

		// INSERT SECOND RISK AREA
		raDto.setInsurancePlanId(4);
		raDto.setRiskAreaName("Risk Area Name 2");
		raDto.setDescription("Test Risk Area 2");
		raDto.setEffectiveDate(effectiveDate);
		raDto.setExpiryDate(expiryDate);

		raDao.insert(raDto, userId);
		
		riskAreaId2 = raDto.getRiskAreaId();

		// INSERT legal land - field xref 
		newDto.setLegalLandId(legalLandId);
		newDto.setRiskAreaId(riskAreaId);
		newDto.setActiveFromCropYear(2021);
		newDto.setActiveToCropYear(2030);

		dao.insert(newDto, userId);

		// INSERT second legal land - field xref 
		newDto.setLegalLandId(legalLandId);
		newDto.setRiskAreaId(riskAreaId2);
		newDto.setActiveFromCropYear(2021);
		newDto.setActiveToCropYear(2030);

		dao.insert(newDto, userId);

		//FETCH
		List<RiskAreaDto> riskAreas = raDao.selectByLegalLand(legalLandId);
		Assert.assertEquals(2, riskAreas.size());

		//DELETE
		dao.deleteForLegalLand(legalLandId);

		//FETCH
		riskAreas = raDao.selectByLegalLand(legalLandId);
		Assert.assertEquals(0, riskAreas.size());
		
		//clean up
		deleteLegalLandRiskAreaXref();
		
	}
	
	private static Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

}
