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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingCommentDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class UnderwritingCommentDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteUwCommentDetail();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteUwCommentDetail();
	}
	
	private void deleteUwCommentDetail() throws NotFoundDaoException, DaoException{
		
		
		UnderwritingCommentDao underwritingCommentDao = persistenceSpringConfig.underwritingCommentDao();
		underwritingCommentDao.deleteForAnnualField(annualFieldDetailId);
		
				
		// delete annual field
		AnnualFieldDetailDao afdDao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto afdDto = afdDao.fetch(annualFieldDetailId);
		
		if (afdDto != null) {
			afdDao.delete(annualFieldDetailId);
		}
		
		// delete legal land
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
	
	private Integer fieldId = 99999999;;
	private Integer legalLandId = 99999999;
	private Integer annualFieldDetailId = 99999999;

	
	@Test 
	public void testUnderwritingComment() throws Exception {

		Integer cropYear = 2020;
		String underwritingCommentGuid;
		String dopUwCommentGuid;
		
		String userId = "UNITTEST";
		
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		UnderwritingCommentDao underwritingCommentDao = persistenceSpringConfig.underwritingCommentDao();
		LegalLandDao legalLandDao = persistenceSpringConfig.legalLandDao();
		AnnualFieldDetailDao afdDao = persistenceSpringConfig.annualFieldDetailDao();

		//INSERT Legal Land
		LegalLandDto newlegalLandDto = new LegalLandDto();
		newlegalLandDto.setLegalLandId(legalLandId);
		newlegalLandDto.setPrimaryReferenceTypeCode("OTHER");
		newlegalLandDto.setLegalDescription("Legal Description");
		newlegalLandDto.setLegalShortDescription("Short Legal");
		newlegalLandDto.setOtherDescription("Other Description");
		newlegalLandDto.setActiveFromCropYear(2011);
		newlegalLandDto.setActiveToCropYear(2022);

		legalLandDao.insertDataSync(newlegalLandDto, userId);

		// INSERT FIELD
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel("Field Label");
		newFieldDto.setActiveFromCropYear(2012);
		newFieldDto.setActiveToCropYear(2022);

		fieldDao.insertDataSync(newFieldDto, userId);

		//INSERT Annual Field Detail record
		AnnualFieldDetailDto newAfdDto = new AnnualFieldDetailDto();
		newAfdDto.setAnnualFieldDetailId(annualFieldDetailId);
		newAfdDto.setLegalLandId(legalLandId);
		newAfdDto.setFieldId(fieldId);
		newAfdDto.setCropYear(cropYear);

		afdDao.insertDataSync(newAfdDto, userId);


		//INSERT
		UnderwritingCommentDto newDto = new UnderwritingCommentDto();

		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setUnderwritingComment("test comment 1");
		newDto.setUnderwritingCommentTypeCode("INV");
		newDto.setUnderwritingCommentTypeDesc("Inventory"); // Not saved directly, but indirectly via underwritingCommentTypeCode.
		
		underwritingCommentDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getUnderwritingCommentGuid());
		underwritingCommentGuid = newDto.getUnderwritingCommentGuid();
		
		//SELECT
		List<UnderwritingCommentDto> dtos = underwritingCommentDao.select(annualFieldDetailId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		dtos = underwritingCommentDao.selectForField(fieldId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		
		//FETCH
		UnderwritingCommentDto fetchedDto = underwritingCommentDao.fetch(underwritingCommentGuid);
		
		Assert.assertEquals("underwritingCommentGuid", newDto.getUnderwritingCommentGuid(), fetchedDto.getUnderwritingCommentGuid());
		Assert.assertEquals("annualFieldDetailId", newDto.getAnnualFieldDetailId(), fetchedDto.getAnnualFieldDetailId());
		Assert.assertEquals("underwritingComment", newDto.getUnderwritingComment(), fetchedDto.getUnderwritingComment());
		Assert.assertEquals("underwritingCommentTypeCode", newDto.getUnderwritingCommentTypeCode(), fetchedDto.getUnderwritingCommentTypeCode());
		Assert.assertEquals("underwritingCommentTypeDesc", newDto.getUnderwritingCommentTypeDesc(), fetchedDto.getUnderwritingCommentTypeDesc());
		
		
		//UPDATE
		fetchedDto.setUnderwritingComment("test comment 2");
		fetchedDto.setUnderwritingCommentTypeCode("INV");
		fetchedDto.setUnderwritingCommentTypeDesc("Inventory");
		
		underwritingCommentDao.update(fetchedDto, userId);

		//FETCH
		UnderwritingCommentDto updatedDto = underwritingCommentDao.fetch(underwritingCommentGuid);

		Assert.assertEquals("underwritingCommentGuid", fetchedDto.getUnderwritingCommentGuid(), updatedDto.getUnderwritingCommentGuid());
		Assert.assertEquals("underwritingComment", fetchedDto.getUnderwritingComment(), updatedDto.getUnderwritingComment());
		Assert.assertEquals("underwritingCommentTypeCode", fetchedDto.getUnderwritingCommentTypeCode(), updatedDto.getUnderwritingCommentTypeCode());
		Assert.assertEquals("underwritingCommentTypeDesc", fetchedDto.getUnderwritingCommentTypeDesc(), updatedDto.getUnderwritingCommentTypeDesc());
		
		//INSERT second comment
		UnderwritingCommentDto newDto2 = new UnderwritingCommentDto();

		newDto2.setAnnualFieldDetailId(annualFieldDetailId);
		newDto2.setUnderwritingComment("test comment 3");
		newDto2.setUnderwritingCommentTypeCode("INV");
		newDto2.setUnderwritingCommentTypeDesc("Inventory"); // Not saved directly, but indirectly via underwritingCommentTypeCode.
		
		underwritingCommentDao.insert(newDto2, userId);

		//SELECT
		dtos = underwritingCommentDao.select(annualFieldDetailId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		dtos = underwritingCommentDao.selectForField(fieldId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		//INSERT DOP comment
		// These need to be set to an existing declared_yield_contract with no comments.
		String declaredYieldContractGuid = "afbbe9d605384b4992b439d1a35adbba";
		Integer growerContractYearId = 97637;
		
		//INSERT
		UnderwritingCommentDto newDto3 = new UnderwritingCommentDto();
		newDto3.setUnderwritingComment("test DOP comment 1");
		newDto3.setUnderwritingCommentTypeCode("DOP");
		newDto3.setUnderwritingCommentTypeDesc("Declaration of Production"); // Not saved directly, but indirectly via underwritingCommentTypeCode.
		newDto3.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto3.setGrowerContractYearId(growerContractYearId);
		
		underwritingCommentDao.insert(newDto3, userId);
		Assert.assertNotNull(newDto3.getUnderwritingCommentGuid());
		dopUwCommentGuid = newDto3.getUnderwritingCommentGuid();
		
		//SELECT for DOP
		dtos = underwritingCommentDao.selectForDopContract(declaredYieldContractGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//FETCH
		UnderwritingCommentDto fetchedDto2 = underwritingCommentDao.fetch(dopUwCommentGuid);
		
		Assert.assertEquals("declaredYieldContractGuid", fetchedDto2.getDeclaredYieldContractGuid(), newDto3.getDeclaredYieldContractGuid());
		Assert.assertEquals("growerContractYearId", fetchedDto2.getGrowerContractYearId(), newDto3.getGrowerContractYearId());

		//DELETE dop comment
		underwritingCommentDao.delete(dopUwCommentGuid);
		
		//FETCH
		UnderwritingCommentDto deletedDto = underwritingCommentDao.fetch(dopUwCommentGuid);
		Assert.assertNull(deletedDto);
		
		
		//DELETE first comment
		underwritingCommentDao.delete(underwritingCommentGuid);
		
		//FETCH
		deletedDto = underwritingCommentDao.fetch(underwritingCommentGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = underwritingCommentDao.select(annualFieldDetailId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());		

		//DELETE second comment (by InventoryField).
		underwritingCommentDao.deleteForAnnualField(annualFieldDetailId);
		
		//SELECT
		dtos = underwritingCommentDao.select(annualFieldDetailId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());		

		dtos = underwritingCommentDao.selectForField(fieldId);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());		
		
		//DELETE annual field
		afdDao.delete(annualFieldDetailId);
		
		//DELETE field
		fieldDao.delete(fieldId);
		
		//DELETE legal land
		legalLandDao.delete(legalLandId);
		
	}

	
	@Test 
	public void testDeleteForField() throws Exception {
		
		// INSERT FIELD
		createField();
		
		// INSERT ANNUAL FIELD
		createAnnualField();
		
		// INSERT CONTRACTED FIELD
		String underwritingCommentGuid = createUwComment();
		
		UnderwritingCommentDao dao = persistenceSpringConfig.underwritingCommentDao();
		
		//FETCH
		UnderwritingCommentDto fetchedDto = dao.fetch(underwritingCommentGuid);
		Assert.assertNotNull(fetchedDto);

		dao.deleteForField(fieldId);

		//FETCH
		UnderwritingCommentDto deletedDto = dao.fetch(underwritingCommentGuid);
		Assert.assertNull(deletedDto);

		//clean up 
		deleteUwCommentDetail();

	}
	
	private String createUwComment() throws DaoException {
		String userId = "UNITTEST";

		UnderwritingCommentDao dao = persistenceSpringConfig.underwritingCommentDao();
		UnderwritingCommentDto newDto = new UnderwritingCommentDto();

		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setUnderwritingComment("test comment 1");
		newDto.setUnderwritingCommentTypeCode("INV");
		newDto.setUnderwritingCommentTypeDesc("Inventory"); // Not saved directly, but indirectly via underwritingCommentTypeCode.
		
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getUnderwritingCommentGuid());
		return newDto.getUnderwritingCommentGuid();

		
	}

	private void createField() throws DaoException {
		// INSERT FIELD
		
		String userId = "UNITTEST";

		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel("Test Field Label");
		newFieldDto.setActiveFromCropYear(2011);
		newFieldDto.setActiveToCropYear(null);

		fieldDao.insertDataSync(newFieldDto, userId);
		
	}	


	private void createAnnualField() throws DaoException {
		
		String userId = "UNITTEST";

		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto newDto = new AnnualFieldDetailDto();

		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setLegalLandId(null);
		newDto.setFieldId(fieldId);
		newDto.setCropYear(2022);

		dao.insertDataSync(newDto, userId);

	}
}
