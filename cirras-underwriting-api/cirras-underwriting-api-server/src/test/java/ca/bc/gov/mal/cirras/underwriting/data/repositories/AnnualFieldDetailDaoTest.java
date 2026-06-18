package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class AnnualFieldDetailDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer annualFieldDetailId = 99202999;
	private Integer annualFieldDetailId2 = 882039992;
	private Integer legalLandId = 99202399;
	private Integer fieldId = 99202599;

	private String userId = "JUNIT_TEST";


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException {

		deleteAnnualFieldDetail(annualFieldDetailId);
		deleteAnnualFieldDetail(annualFieldDetailId2);
		
		// delete field
		deleteField();
		
		// delete legal land
		deleteLegalLand();

	}
	
	private void deleteAnnualFieldDetail(Integer afdId) throws NotFoundDaoException, DaoException{
		
		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto dto = dao.fetch(afdId);
		
		if (dto != null) {
			
			dao.delete(afdId);
			
		}


	}
	
	private void deleteField() throws DaoException, NotFoundDaoException {
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = fieldDao.fetch(fieldId);
		
		if (fieldDto != null) {
			fieldDao.delete(fieldId);
		}
	}

	private void deleteLegalLand() throws DaoException, NotFoundDaoException {
		LegalLandDao legalLandDao = persistenceSpringConfig.legalLandDao();
		LegalLandDto legalLandDto = legalLandDao.fetch(legalLandId);
		
		if (legalLandDto != null) {
			legalLandDao.delete(legalLandId);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteAnnualFieldDetail() throws Exception {

		LegalLandDao legalLandDao = persistenceSpringConfig.legalLandDao();
		LegalLandDto newlegalLandDto = new LegalLandDto();
		
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		
		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto newDto = new AnnualFieldDetailDto();
		
		
		Integer cropYear = 2022;

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
		
		//INSERT Annual Field Detail record
		newDto.setAnnualFieldDetailId(null);
		newDto.setLegalLandId(legalLandId);
		newDto.setFieldId(fieldId);
		newDto.setCropYear(cropYear);

		dao.insert(newDto, userId);
		
		//FETCH
		AnnualFieldDetailDto fetchedDto = dao.fetch(newDto.getAnnualFieldDetailId());

		Assert.assertEquals("AnnualFieldDetailId", newDto.getAnnualFieldDetailId(), fetchedDto.getAnnualFieldDetailId());
		Assert.assertEquals("LegalLandId", newDto.getLegalLandId(), fetchedDto.getLegalLandId());
		Assert.assertEquals("FieldId", newDto.getFieldId(), fetchedDto.getFieldId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		
		//UPDATE - only updating the crop year
		cropYear = 2021;
		
		fetchedDto.setCropYear(cropYear);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		AnnualFieldDetailDto updatedDto = dao.fetch(fetchedDto.getAnnualFieldDetailId());

		Assert.assertEquals("CropYear", fetchedDto.getCropYear(), updatedDto.getCropYear());

		//DELETE
		dao.delete(updatedDto.getAnnualFieldDetailId());

		//FETCH
		AnnualFieldDetailDto deletedDto = dao.fetch(updatedDto.getAnnualFieldDetailId());
		Assert.assertNull(deletedDto);
		
		//clean up 
		delete();

	}

	
	@Test 
	public void testgetAnnualFieldByFieldAndCropYear() throws Exception {

		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto newDto = new AnnualFieldDetailDto();
		
		
		Integer cropYear = 2022;

		//INSERT Legal Land
		createLegalLand();

		// INSERT FIELD
		createField();
		
		//INSERT Annual Field Detail record
		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setLegalLandId(legalLandId);
		newDto.setFieldId(fieldId);
		newDto.setCropYear(cropYear);

		dao.insertDataSync(newDto, userId);
		
		//FETCH
		AnnualFieldDetailDto fetchedDto = dao.getByFieldAndCropYear(fieldId, cropYear);

		Assert.assertEquals("AnnualFieldDetailId", newDto.getAnnualFieldDetailId(), fetchedDto.getAnnualFieldDetailId());
		Assert.assertEquals("LegalLandId", newDto.getLegalLandId(), fetchedDto.getLegalLandId());
		Assert.assertEquals("FieldId", newDto.getFieldId(), fetchedDto.getFieldId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		
		//clean up 
		delete();

	}
	
	@Test 
	public void testDeleteForField() throws Exception {

		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto newDto = new AnnualFieldDetailDto();
		
		// INSERT FIELD
		createField();
		
		//INSERT Annual Field Detail record
		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setLegalLandId(null);
		newDto.setFieldId(fieldId);
		newDto.setCropYear(2022);

		dao.insertDataSync(newDto, userId);
		
		//FETCH
		AnnualFieldDetailDto fetchedDto = dao.fetch(annualFieldDetailId);
		Assert.assertNotNull(fetchedDto);

		dao.deleteForField(fieldId);

		//FETCH
		fetchedDto = dao.fetch(annualFieldDetailId);
		Assert.assertNull(fetchedDto);

		//clean up 
		delete();

	}
	
	
	@Test 
	public void testgetTotalForLegalLandField() throws Exception {

		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		
		//INSERT Legal Land
		createLegalLand();

		// INSERT FIELD
		createField();
		
		//Check total annual detail records for legal land
		int totalRecords = dao.getTotalForLegalLandField(legalLandId, fieldId);
		Assert.assertEquals("No records expected", 0, totalRecords);
		
		//INSERT Annual Field Detail record
		createAnnualFieldDetail(annualFieldDetailId, 2022);

		//Check total annual detail records for legal land
		totalRecords = dao.getTotalForLegalLandField(legalLandId, fieldId);
		Assert.assertEquals("1 record expected", 1, totalRecords);

		createAnnualFieldDetail(annualFieldDetailId2, 2023);
		
		//Check total annual detail records for legal land
		totalRecords = dao.getTotalForLegalLandField(legalLandId, fieldId);
		Assert.assertEquals("2 records expected", 2, totalRecords);
		
		//clean up 
		delete();

	}		

	private void createLegalLand()
			throws DaoException {

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
		
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel("Test Field Label");
		newFieldDto.setActiveFromCropYear(2011);
		newFieldDto.setActiveToCropYear(null);

		fieldDao.insertDataSync(newFieldDto, userId);
		
	}	
	
	private void createAnnualFieldDetail(
			Integer afdId,
			Integer cropYear
			) throws DaoException {
		String userId = "JUNIT_TEST";

		// Annual Field Detail
		AnnualFieldDetailDao afdDao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto afdDto = new AnnualFieldDetailDto();

		afdDto.setAnnualFieldDetailId(afdId);
		afdDto.setLegalLandId(legalLandId);
		afdDto.setFieldId(fieldId);
		afdDto.setCropYear(cropYear);

		afdDao.insertDataSync(afdDto, userId);
	}	
}
