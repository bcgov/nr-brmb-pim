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

import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class DeclaredYieldFieldCommodityBerriesDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	private Integer contractId1 = 90000002;

	private Integer gcyId1 = 90000001;
	private Integer cropYear1 = 2019;

	private Integer gcyId2 = 90000201;
	private Integer cropYear2 = 2020;

	private Integer fieldId1 = 99999999;
	private Integer annualFieldDetailId1 = 90000003;
	private Integer contractedFieldDetailId1 = 90000004;

	private Integer annualFieldDetailId2 = 90000203;
	private Integer contractedFieldDetailId2 = 90000204;

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}

	private void delete() throws NotFoundDaoException, DaoException {
		deleteDeclaredYieldFieldCommodityBerries();
		deleteDeclaredYieldContract(contractId1, cropYear1);
		deleteDeclaredYieldContract(contractId1, cropYear2);
		deleteContractedFieldDetail(contractedFieldDetailId1);
		deleteContractedFieldDetail(contractedFieldDetailId2);
		deleteAnnualFieldDetail(annualFieldDetailId1);
		deleteAnnualFieldDetail(annualFieldDetailId2);
		deleteField();		
		deleteGrowerContractYear(gcyId1);
		deleteGrowerContractYear(gcyId2);
	}

	private void deleteDeclaredYieldFieldCommodityBerries() throws NotFoundDaoException, DaoException {

		DeclaredYieldFieldCommodityBerriesDao declaredYieldFieldCommodityBerriesDao = persistenceSpringConfig.declaredYieldFieldCommodityBerriesDao();
		declaredYieldFieldCommodityBerriesDao.deleteForField(fieldId1);
		
	}
	
	private void deleteDeclaredYieldContract(Integer contractId, Integer cropYear) throws NotFoundDaoException, DaoException {
		DeclaredYieldContractDao dao = persistenceSpringConfig.declaredYieldContractDao();
		DeclaredYieldContractDto dto = dao.getByContractAndYear(contractId, cropYear);
		
		if ( dto != null ) {
			dao.delete(dto.getDeclaredYieldContractGuid());			
		}		
	}

	
	private void deleteAnnualFieldDetail(Integer annualFieldDetailId) throws NotFoundDaoException, DaoException {

		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto dto = dao.fetch(annualFieldDetailId);

		if ( dto != null ) {
			dao.delete(annualFieldDetailId);			
		}
		
	}
	
	private void deleteField() throws NotFoundDaoException, DaoException{
		
		FieldDao dao = persistenceSpringConfig.fieldDao();
		FieldDto dto = dao.fetch(fieldId1);
		if (dto != null) {
			dao.delete(fieldId1);
		}	
	}

	private void deleteGrowerContractYear(Integer gcyId) throws NotFoundDaoException, DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto dto = dao.fetch(gcyId);
		if (dto != null) {
			dao.delete(gcyId);
		}		
	}
	
	private void deleteContractedFieldDetail(Integer contractedFieldDetailId) throws NotFoundDaoException, DaoException {

		ContractedFieldDetailDao dao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto dto = dao.fetch(contractedFieldDetailId);
		
		if (dto != null) {
			
			dao.delete(contractedFieldDetailId);
		}
	}
	
	
	@Test 
	public void testDeclaredYieldFieldCommodityBerries() throws Exception {

		String declaredYieldFieldCommodityBerriesGuid;
		
		String userId = "UNITTEST";
		DeclaredYieldFieldCommodityBerriesDao declaredYieldFieldCommodityBerriesDao = persistenceSpringConfig.declaredYieldFieldCommodityBerriesDao();
		
		//INSERT Field and contract data
		createGrowerContractYear(userId, gcyId1, cropYear1);
		createField(userId);
		
		createAnnualFieldDetail(userId, cropYear1, annualFieldDetailId1);
		createContractedFieldDetail(userId, gcyId1, annualFieldDetailId1, contractedFieldDetailId1);

		createDeclaredYieldContract(userId, cropYear1);

		//Check dop yield field record = expected 0
		int totalDopWithYield = declaredYieldFieldCommodityBerriesDao.getTotalDopRecordsWithYield(fieldId1, cropYear1);
		Assert.assertEquals("totalDopWithYield 0", 0, totalDopWithYield);

		//INSERT
		DeclaredYieldFieldCommodityBerriesDto newDto = new DeclaredYieldFieldCommodityBerriesDto();

		newDto.setCropCommodityId(10);
		newDto.setCropCommodityName("BLUEBERRY");
		newDto.setCropYear(cropYear1);
		newDto.setFieldId(fieldId1);
		newDto.setTotalProduction(11.22);
		newDto.setTotalProductionOverride(33.44);

		
		declaredYieldFieldCommodityBerriesDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getDeclaredYieldFieldCommodityBerriesGuid());
		declaredYieldFieldCommodityBerriesGuid = newDto.getDeclaredYieldFieldCommodityBerriesGuid();
		
		//Check dop yield field record = expected 1
		totalDopWithYield = declaredYieldFieldCommodityBerriesDao.getTotalDopRecordsWithYield(fieldId1, cropYear1);
		Assert.assertEquals("totalDopWithYield 1", 1, totalDopWithYield);
		
		//GET BY FIELD
		List<DeclaredYieldFieldCommodityBerriesDto> dtos = declaredYieldFieldCommodityBerriesDao.select(fieldId1, cropYear1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		DeclaredYieldFieldCommodityBerriesDto fetchedDto = dtos.get(0);
		

		Assert.assertEquals("DeclaredYieldFieldCommodityBerriesGuid", newDto.getDeclaredYieldFieldCommodityBerriesGuid(), fetchedDto.getDeclaredYieldFieldCommodityBerriesGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("FieldId", newDto.getFieldId(), fetchedDto.getFieldId());
		Assert.assertEquals("TotalProduction", newDto.getTotalProduction(), fetchedDto.getTotalProduction());
		Assert.assertEquals("TotalProductionOverride", newDto.getTotalProductionOverride(), fetchedDto.getTotalProductionOverride());
		
		
		//FETCH
		fetchedDto = declaredYieldFieldCommodityBerriesDao.fetch(declaredYieldFieldCommodityBerriesGuid);

		Assert.assertEquals("DeclaredYieldFieldCommodityBerriesGuid", newDto.getDeclaredYieldFieldCommodityBerriesGuid(), fetchedDto.getDeclaredYieldFieldCommodityBerriesGuid());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("FieldId", newDto.getFieldId(), fetchedDto.getFieldId());
		Assert.assertEquals("TotalProduction", newDto.getTotalProduction(), fetchedDto.getTotalProduction());
		Assert.assertEquals("TotalProductionOverride", newDto.getTotalProductionOverride(), fetchedDto.getTotalProductionOverride());

		//UPDATE
		fetchedDto.setTotalProduction(22.11);
		fetchedDto.setTotalProductionOverride(44.33);
		
		
		declaredYieldFieldCommodityBerriesDao.update(fetchedDto, userId);

		//FETCH
		DeclaredYieldFieldCommodityBerriesDto updatedDto = declaredYieldFieldCommodityBerriesDao.fetch(declaredYieldFieldCommodityBerriesGuid);

		Assert.assertEquals("DeclaredYieldFieldCommodityBerriesGuid", fetchedDto.getDeclaredYieldFieldCommodityBerriesGuid(), updatedDto.getDeclaredYieldFieldCommodityBerriesGuid());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", fetchedDto.getCropCommodityName(), updatedDto.getCropCommodityName());
		Assert.assertEquals("CropYear", fetchedDto.getCropYear(), updatedDto.getCropYear());
		Assert.assertEquals("FieldId", fetchedDto.getFieldId(), updatedDto.getFieldId());
		Assert.assertEquals("TotalProduction", fetchedDto.getTotalProduction(), updatedDto.getTotalProduction());
		Assert.assertEquals("TotalProductionOverride", fetchedDto.getTotalProductionOverride(), updatedDto.getTotalProductionOverride());
				
		//DELETE
		declaredYieldFieldCommodityBerriesDao.delete(declaredYieldFieldCommodityBerriesGuid);
		
		//FETCH
		DeclaredYieldFieldCommodityBerriesDto deletedDto = declaredYieldFieldCommodityBerriesDao.fetch(declaredYieldFieldCommodityBerriesGuid);
		Assert.assertNull(deletedDto);

		//GET BY FIELD
		dtos = declaredYieldFieldCommodityBerriesDao.select(fieldId1, cropYear1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		DeclaredYieldFieldCommodityBerriesDto newDto2 = new DeclaredYieldFieldCommodityBerriesDto();

		newDto2.setCropCommodityId(10);
		newDto2.setCropCommodityName("BLUEBERRY");
		newDto2.setCropYear(cropYear1);
		newDto2.setFieldId(fieldId1);
		newDto2.setTotalProduction(11.22);
		newDto2.setTotalProductionOverride(33.44);
		
		declaredYieldFieldCommodityBerriesDao.insert(newDto2, userId);
		Assert.assertNotNull(newDto2.getDeclaredYieldFieldCommodityBerriesGuid());
				
		//GET BY FIELD
		dtos = declaredYieldFieldCommodityBerriesDao.select(fieldId1, cropYear1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		//Delete all records for field
		declaredYieldFieldCommodityBerriesDao.deleteForField(fieldId1);
		dtos = declaredYieldFieldCommodityBerriesDao.select(fieldId1, cropYear1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
				
		//DELETE
		delete();
		
	}

	@Test 
	public void testDeleteForDeclaredYieldContract() throws Exception {

		String userId = "UNITTEST";
		
		DeclaredYieldFieldCommodityBerriesDao declaredYieldFieldCommodityBerriesDao = persistenceSpringConfig.declaredYieldFieldCommodityBerriesDao();

		createGrowerContractYear(userId, gcyId1, cropYear1);
		createField(userId);
		
		createAnnualFieldDetail(userId, cropYear1, annualFieldDetailId1);
		createContractedFieldDetail(userId, gcyId1, annualFieldDetailId1, contractedFieldDetailId1);

		String declaredYieldContractGuid = createDeclaredYieldContract(userId, cropYear1);
		
		//INSERT Commodity 1
		DeclaredYieldFieldCommodityBerriesDto newDto = new DeclaredYieldFieldCommodityBerriesDto();

		newDto.setCropCommodityId(10);
		newDto.setCropCommodityName("BLUEBERRY");
		newDto.setCropYear(cropYear1);
		newDto.setFieldId(fieldId1);
		newDto.setTotalProduction(11.22);
		newDto.setTotalProductionOverride(33.44);
		
		declaredYieldFieldCommodityBerriesDao.insert(newDto, userId);

		//INSERT Commodity 2
		newDto = new DeclaredYieldFieldCommodityBerriesDto();

		newDto.setCropCommodityId(12);
		newDto.setCropCommodityName("RASPBERRY");
		newDto.setCropYear(cropYear1);
		newDto.setFieldId(fieldId1);
		newDto.setTotalProduction(55.66);
		newDto.setTotalProductionOverride(77.88);
		
		declaredYieldFieldCommodityBerriesDao.insert(newDto, userId);
		
		//GET BY FIELD
		List<DeclaredYieldFieldCommodityBerriesDto> dtos = declaredYieldFieldCommodityBerriesDao.select(fieldId1, cropYear1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		//DELETE
		declaredYieldFieldCommodityBerriesDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
		
		//GET BY FIELD
		dtos = declaredYieldFieldCommodityBerriesDao.select(fieldId1, cropYear1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
	}
	
	@Test 
	public void testDeleteForFieldAndYear() throws Exception {

		String userId = "UNITTEST";
		
		DeclaredYieldFieldCommodityBerriesDao declaredYieldFieldCommodityBerriesDao = persistenceSpringConfig.declaredYieldFieldCommodityBerriesDao();

		createField(userId);
		
		//2019
		createGrowerContractYear(userId, gcyId1, cropYear1);
		createAnnualFieldDetail(userId, cropYear1, annualFieldDetailId1);
		createContractedFieldDetail(userId, gcyId1, annualFieldDetailId1, contractedFieldDetailId1);
		createDeclaredYieldContract(userId, cropYear1);

		//2020
		createGrowerContractYear(userId, gcyId2, cropYear2);
		createAnnualFieldDetail(userId, cropYear2, annualFieldDetailId2);
		createContractedFieldDetail(userId, gcyId2, annualFieldDetailId2, contractedFieldDetailId2);
		createDeclaredYieldContract(userId, cropYear2);
		
		//INSERT Commodity 2019
		DeclaredYieldFieldCommodityBerriesDto newDto = new DeclaredYieldFieldCommodityBerriesDto();

		newDto.setCropCommodityId(10);
		newDto.setCropCommodityName("BLUEBERRY");
		newDto.setCropYear(cropYear1);
		newDto.setFieldId(fieldId1);
		newDto.setTotalProduction(11.22);
		newDto.setTotalProductionOverride(33.44);
		
		declaredYieldFieldCommodityBerriesDao.insert(newDto, userId);

		//INSERT Commodity 2020
		newDto = new DeclaredYieldFieldCommodityBerriesDto();

		newDto.setCropCommodityId(10);
		newDto.setCropCommodityName("BLUEBERRY");
		newDto.setCropYear(cropYear2);
		newDto.setFieldId(fieldId1);
		newDto.setTotalProduction(11.22);
		newDto.setTotalProductionOverride(33.44);
		
		declaredYieldFieldCommodityBerriesDao.insert(newDto, userId);
		
		//GET BY FIELD
		List<DeclaredYieldFieldCommodityBerriesDto> dtos = declaredYieldFieldCommodityBerriesDao.select(fieldId1, cropYear1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		dtos = declaredYieldFieldCommodityBerriesDao.select(fieldId1, cropYear2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//DELETE 2020 data
		declaredYieldFieldCommodityBerriesDao.deleteForFieldAndYear(fieldId1, cropYear2);
		
		//GET BY FIELD
		dtos = declaredYieldFieldCommodityBerriesDao.select(fieldId1, cropYear1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		dtos = declaredYieldFieldCommodityBerriesDao.select(fieldId1, cropYear2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
		
		delete();
	}
	
	private void createGrowerContractYear(String userId, Integer gcyId, Integer cropYear) throws DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto newDto = new GrowerContractYearDto();
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dataSyncTransDate = cal.getTime();
		
		//INSERT
		newDto.setGrowerContractYearId(gcyId);
		newDto.setContractId(contractId1);
		newDto.setGrowerId(null);
		newDto.setInsurancePlanId(3);
		newDto.setCropYear(cropYear);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
	}
	
	private void createField(String userId) throws DaoException {
		// INSERT FIELD
		
		String fieldLabel = "Test Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;

		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId1);
		newFieldDto.setFieldLabel(fieldLabel);
		newFieldDto.setActiveFromCropYear(activeFromCropYear);
		newFieldDto.setActiveToCropYear(activeToCropYear);

		fieldDao.insertDataSync(newFieldDto, userId);
	}

	private void createAnnualFieldDetail(String userId, Integer cropYear, Integer annualFieldDetailId) throws DaoException { 
		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto newDto = new AnnualFieldDetailDto();

		//INSERT Annual Field Detail record
		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setLegalLandId(null);
		newDto.setFieldId(fieldId1);
		newDto.setCropYear(cropYear);

		dao.insertDataSync(newDto, userId);
		
	}
	
	private void createContractedFieldDetail(String userId, Integer gcyId, Integer annualFieldDetailId, Integer contractedFieldDetailId) throws DaoException {
		
		ContractedFieldDetailDao contractedFieldDetailDao = persistenceSpringConfig.contractedFieldDetailDao();

		// INSERT
		ContractedFieldDetailDto newDto = new ContractedFieldDetailDto();

		newDto.setAnnualFieldDetailId(annualFieldDetailId);
		newDto.setContractedFieldDetailId(contractedFieldDetailId);
		newDto.setDisplayOrder(1);
		newDto.setIsLeasedInd(false);
		newDto.setGrowerContractYearId(gcyId);

		contractedFieldDetailDao.insertDataSync(newDto, userId);
		
	}

	private String createDeclaredYieldContract(String userId, Integer cropYear) throws DaoException {

		DeclaredYieldContractDao dao = persistenceSpringConfig.declaredYieldContractDao();
		
		DeclaredYieldContractDto newDto = new DeclaredYieldContractDto();

		newDto.setContractId(contractId1);
		newDto.setCropYear(cropYear);
		newDto.setDeclarationOfProductionDate(null);
		newDto.setDefaultYieldMeasUnitTypeCode("LB");
		newDto.setEnteredYieldMeasUnitTypeCode("LB");
		newDto.setGrainFromOtherSourceInd(false);
		newDto.setBalerWagonInfo(null);
		newDto.setTotalLivestock(null);

		//INSERT
		dao.insert(newDto, userId);		
		
		return newDto.getDeclaredYieldContractGuid();	
	}
	
}
