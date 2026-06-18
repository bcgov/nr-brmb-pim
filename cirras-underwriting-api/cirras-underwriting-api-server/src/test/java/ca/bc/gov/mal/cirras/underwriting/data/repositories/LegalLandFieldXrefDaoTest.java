package ca.bc.gov.mal.cirras.underwriting.data.repositories;

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

import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandFieldXrefDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class LegalLandFieldXrefDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer legalLandId = 987654321;
	private Integer fieldId = 987654321;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteLegalLandFieldXref();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteLegalLandFieldXref();
	}
	
	private void deleteLegalLandFieldXref() throws NotFoundDaoException, DaoException{
		
		// delete legal land - field xref table
		LegalLandFieldXrefDao dao = persistenceSpringConfig.legalLandFieldXrefDao();
		LegalLandFieldXrefDto dto = dao.fetch(legalLandId, fieldId);
		if (dto != null) {
			dao.delete(legalLandId, fieldId);
		}
		
		//delete legal land
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
		
	}
	
	@Test 
	public void testInsertDeleteLegalLandFieldXref() throws Exception {
		
		LegalLandDao legalLandDao = persistenceSpringConfig.legalLandDao();
		LegalLandDto newlegalLandDto = new LegalLandDto();
		
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		
		LegalLandFieldXrefDao dao = persistenceSpringConfig.legalLandFieldXrefDao();
		LegalLandFieldXrefDto newDto = new LegalLandFieldXrefDto();

		String userId = "JUNIT_TEST";

		//INSERT Legal Land
		newlegalLandDto.setLegalLandId(legalLandId);
		newlegalLandDto.setPrimaryReferenceTypeCode("OTHER");
		newlegalLandDto.setLegalDescription("Legal Description");
		newlegalLandDto.setLegalShortDescription("Short Legal");
		newlegalLandDto.setOtherDescription("Other Description");
		newlegalLandDto.setActiveFromCropYear(2011);
		newlegalLandDto.setActiveToCropYear(2022);

		legalLandDao.insertDataSync(newlegalLandDto, userId);

		// INSERT FIELD
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel("Field Label");
		newFieldDto.setActiveFromCropYear(2012);
		newFieldDto.setActiveToCropYear(2022);

		fieldDao.insertDataSync(newFieldDto, userId);
		
		// INSERT legal land - field xref 
		newDto.setLegalLandId(legalLandId);
		newDto.setFieldId(fieldId);

		dao.insert(newDto, userId);
		
		//FETCH
		LegalLandFieldXrefDto fetchedDto = dao.fetch(legalLandId, fieldId);

		Assert.assertEquals("LegalLandId", newDto.getLegalLandId(), fetchedDto.getLegalLandId());
		Assert.assertEquals("FieldId", newDto.getFieldId(), fetchedDto.getFieldId());

		//DELETE
		dao.delete(legalLandId, fieldId);

		//FETCH
		LegalLandFieldXrefDto deletedDto = dao.fetch(legalLandId, fieldId);
		Assert.assertNull(deletedDto);
		
		//clean up
		deleteLegalLandFieldXref();
		
	}
	
	@Test 
	public void testDeleteForField() throws Exception {

		String userId = "JUNIT_TEST";

		LegalLandFieldXrefDao dao = persistenceSpringConfig.legalLandFieldXrefDao();
		LegalLandFieldXrefDto newDto = new LegalLandFieldXrefDto();
		
		// INSERT FIELD
		createField();
		
		// INSERT LEGAL LAND
		createLegalLand();
		
		// INSERT LEGAL LAND FIELD XREF
		newDto.setLegalLandId(legalLandId);
		newDto.setFieldId(fieldId);

		dao.insert(newDto, userId);
		
		//FETCH
		LegalLandFieldXrefDto fetchedDto = dao.fetch(legalLandId, fieldId);
		Assert.assertNotNull(fetchedDto);

		dao.deleteForField(fieldId);

		//FETCH
		fetchedDto = dao.fetch(legalLandId, fieldId);
		Assert.assertNull(fetchedDto);

		//clean up 
		deleteLegalLandFieldXref();

	}

	private void createLegalLand()
			throws DaoException {
		String userId = "JUNIT_TEST";

		LegalLandDao dao = persistenceSpringConfig.legalLandDao();
		LegalLandDto newDto = new LegalLandDto();

		// INSERT
		newDto.setLegalLandId(legalLandId);
		newDto.setPrimaryPropertyIdentifier("111-222-333");
		newDto.setPrimaryReferenceTypeCode("OTHER");
		newDto.setLegalDescription("Legal");
		newDto.setLegalShortDescription("Short Legal");
		newDto.setOtherDescription("Other Desc 22222");
		newDto.setActiveFromCropYear(2011);
		newDto.setActiveToCropYear(null);
		newDto.setTotalAcres(12.3);
		newDto.setPrimaryLandIdentifierTypeCode("PID");

		dao.insertDataSync(newDto, userId);
	}

	private void createField() throws DaoException {
		// INSERT FIELD
		String userId = "JUNIT_TEST";

		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel("Test Field Label");
		newFieldDto.setActiveFromCropYear(2011);
		newFieldDto.setActiveToCropYear(null);

		fieldDao.insertDataSync(newFieldDto, userId);
		
	}	
}
