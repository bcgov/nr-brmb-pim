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
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldVarietyBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class DeclaredYieldFieldVarietyBerriesDaoTest {
	
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
		deleteDeclaredYieldFieldVarietyBerries();
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

	private void deleteDeclaredYieldFieldVarietyBerries() throws NotFoundDaoException, DaoException {

		DeclaredYieldFieldVarietyBerriesDao declaredYieldFieldVarietyBerriesDao = persistenceSpringConfig.declaredYieldFieldVarietyBerriesDao();
		declaredYieldFieldVarietyBerriesDao.deleteForField(fieldId1);
		
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
	public void testDeclaredYieldFieldVarietyBerries() throws Exception {

		String declaredYieldFieldVarietyBerriesGuid;
		
		String userId = "UNITTEST";
		DeclaredYieldFieldVarietyBerriesDao declaredYieldFieldVarietyBerriesDao = persistenceSpringConfig.declaredYieldFieldVarietyBerriesDao();
		
		//INSERT Field and contract data
		createGrowerContractYear(userId, gcyId1, cropYear1);
		createField(userId);
		
		createAnnualFieldDetail(userId, cropYear1, annualFieldDetailId1);
		createContractedFieldDetail(userId, gcyId1, annualFieldDetailId1, contractedFieldDetailId1);

		createDeclaredYieldContract(userId, cropYear1);
		String declaredYieldFieldCommodityBerriesGuid = createDeclaredYieldFieldCommodityBerries(userId, 10, "BLUEBERRY", cropYear1);

		//INSERT
		DeclaredYieldFieldVarietyBerriesDto newDto = new DeclaredYieldFieldVarietyBerriesDto();

		newDto.setAbandonmentYield(11.22);
		newDto.setCropVarietyId(1010689);
		newDto.setCropVarietyName("BLUEJAY");
		newDto.setDeclaredYieldFieldCommodityBerriesGuid(declaredYieldFieldCommodityBerriesGuid);
		newDto.setPlantedAcres(33.44);
		newDto.setMatureEquivalentAcres(25.5);
		newDto.setSalesYield(55.66);
		newDto.setSoldShippedYield(77.88);
		newDto.setTotalProduction(99.00);
		newDto.setTotalProductionOverride(12.34);
		
		declaredYieldFieldVarietyBerriesDao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getDeclaredYieldFieldVarietyBerriesGuid());
		declaredYieldFieldVarietyBerriesGuid = newDto.getDeclaredYieldFieldVarietyBerriesGuid();
		
		//GET BY COMMODITY
		List<DeclaredYieldFieldVarietyBerriesDto> dtos = declaredYieldFieldVarietyBerriesDao.select(declaredYieldFieldCommodityBerriesGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		DeclaredYieldFieldVarietyBerriesDto fetchedDto = dtos.get(0);
		

		Assert.assertEquals("DeclaredYieldFieldVarietyBerriesGuid", newDto.getDeclaredYieldFieldVarietyBerriesGuid(), fetchedDto.getDeclaredYieldFieldVarietyBerriesGuid());
		Assert.assertEquals("AbandonmentYield", newDto.getAbandonmentYield(), fetchedDto.getAbandonmentYield());
		Assert.assertEquals("CropVarietyId", newDto.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", newDto.getCropVarietyName(), fetchedDto.getCropVarietyName());
		Assert.assertEquals("DeclaredYieldFieldCommodityBerriesGuid", newDto.getDeclaredYieldFieldCommodityBerriesGuid(), fetchedDto.getDeclaredYieldFieldCommodityBerriesGuid());
		Assert.assertEquals("PlantedAcres", newDto.getPlantedAcres(), fetchedDto.getPlantedAcres());
		Assert.assertEquals("MatureEquivalentAcres", newDto.getMatureEquivalentAcres(), fetchedDto.getMatureEquivalentAcres());
		Assert.assertEquals("SalesYield", newDto.getSalesYield(), fetchedDto.getSalesYield());
		Assert.assertEquals("SoldShippedYield", newDto.getSoldShippedYield(), fetchedDto.getSoldShippedYield());
		Assert.assertEquals("TotalProduction", newDto.getTotalProduction(), fetchedDto.getTotalProduction());
		Assert.assertEquals("TotalProductionOverride", newDto.getTotalProductionOverride(), fetchedDto.getTotalProductionOverride());
		
		
		//FETCH
		fetchedDto = declaredYieldFieldVarietyBerriesDao.fetch(declaredYieldFieldVarietyBerriesGuid);

		Assert.assertEquals("DeclaredYieldFieldVarietyBerriesGuid", newDto.getDeclaredYieldFieldVarietyBerriesGuid(), fetchedDto.getDeclaredYieldFieldVarietyBerriesGuid());
		Assert.assertEquals("AbandonmentYield", newDto.getAbandonmentYield(), fetchedDto.getAbandonmentYield());
		Assert.assertEquals("CropVarietyId", newDto.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", newDto.getCropVarietyName(), fetchedDto.getCropVarietyName());
		Assert.assertEquals("DeclaredYieldFieldCommodityBerriesGuid", newDto.getDeclaredYieldFieldCommodityBerriesGuid(), fetchedDto.getDeclaredYieldFieldCommodityBerriesGuid());
		Assert.assertEquals("PlantedAcres", newDto.getPlantedAcres(), fetchedDto.getPlantedAcres());
		Assert.assertEquals("MatureEquivalentAcres", newDto.getMatureEquivalentAcres(), fetchedDto.getMatureEquivalentAcres());
		Assert.assertEquals("SalesYield", newDto.getSalesYield(), fetchedDto.getSalesYield());
		Assert.assertEquals("SoldShippedYield", newDto.getSoldShippedYield(), fetchedDto.getSoldShippedYield());
		Assert.assertEquals("TotalProduction", newDto.getTotalProduction(), fetchedDto.getTotalProduction());
		Assert.assertEquals("TotalProductionOverride", newDto.getTotalProductionOverride(), fetchedDto.getTotalProductionOverride());

		//UPDATE
		fetchedDto.setAbandonmentYield(22.11);
		fetchedDto.setPlantedAcres(44.33);
		fetchedDto.setMatureEquivalentAcres(30.5);
		fetchedDto.setSalesYield(66.55);
		fetchedDto.setSoldShippedYield(88.77);
		fetchedDto.setTotalProduction(00.99);
		fetchedDto.setTotalProductionOverride(34.12);
		
		
		declaredYieldFieldVarietyBerriesDao.update(fetchedDto, userId);

		//FETCH
		DeclaredYieldFieldVarietyBerriesDto updatedDto = declaredYieldFieldVarietyBerriesDao.fetch(declaredYieldFieldVarietyBerriesGuid);

		Assert.assertEquals("DeclaredYieldFieldVarietyBerriesGuid", fetchedDto.getDeclaredYieldFieldVarietyBerriesGuid(), updatedDto.getDeclaredYieldFieldVarietyBerriesGuid());
		Assert.assertEquals("AbandonmentYield", fetchedDto.getAbandonmentYield(), updatedDto.getAbandonmentYield());
		Assert.assertEquals("CropVarietyId", fetchedDto.getCropVarietyId(), updatedDto.getCropVarietyId());
		Assert.assertEquals("CropVarietyName", fetchedDto.getCropVarietyName(), updatedDto.getCropVarietyName());
		Assert.assertEquals("DeclaredYieldFieldCommodityBerriesGuid", fetchedDto.getDeclaredYieldFieldCommodityBerriesGuid(), updatedDto.getDeclaredYieldFieldCommodityBerriesGuid());
		Assert.assertEquals("PlantedAcres", fetchedDto.getPlantedAcres(), updatedDto.getPlantedAcres());
		Assert.assertEquals("MatureEquivalentAcres", fetchedDto.getMatureEquivalentAcres(), updatedDto.getMatureEquivalentAcres());
		Assert.assertEquals("SalesYield", fetchedDto.getSalesYield(), updatedDto.getSalesYield());
		Assert.assertEquals("SoldShippedYield", fetchedDto.getSoldShippedYield(), updatedDto.getSoldShippedYield());
		Assert.assertEquals("TotalProduction", fetchedDto.getTotalProduction(), updatedDto.getTotalProduction());
		Assert.assertEquals("TotalProductionOverride", fetchedDto.getTotalProductionOverride(), updatedDto.getTotalProductionOverride());
				
		//DELETE
		declaredYieldFieldVarietyBerriesDao.delete(declaredYieldFieldVarietyBerriesGuid);
		
		//FETCH
		DeclaredYieldFieldVarietyBerriesDto deletedDto = declaredYieldFieldVarietyBerriesDao.fetch(declaredYieldFieldVarietyBerriesGuid);
		Assert.assertNull(deletedDto);

		//GET BY COMMODITY
		dtos = declaredYieldFieldVarietyBerriesDao.select(declaredYieldFieldCommodityBerriesGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		DeclaredYieldFieldVarietyBerriesDto newDto2 = new DeclaredYieldFieldVarietyBerriesDto();

		newDto2.setAbandonmentYield(11.22);
		newDto2.setCropVarietyId(1010689);
		newDto2.setCropVarietyName("BLUEJAY");
		newDto2.setDeclaredYieldFieldCommodityBerriesGuid(declaredYieldFieldCommodityBerriesGuid);
		newDto2.setPlantedAcres(33.44);
		newDto2.setMatureEquivalentAcres(25.5);
		newDto2.setSalesYield(55.66);
		newDto2.setSoldShippedYield(77.88);
		newDto2.setTotalProduction(99.00);
		newDto2.setTotalProductionOverride(12.34);
		
		declaredYieldFieldVarietyBerriesDao.insert(newDto2, userId);
		Assert.assertNotNull(newDto2.getDeclaredYieldFieldVarietyBerriesGuid());
				
		//GET BY COMMODITY
		dtos = declaredYieldFieldVarietyBerriesDao.select(declaredYieldFieldCommodityBerriesGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		//Delete all records for field
		declaredYieldFieldVarietyBerriesDao.deleteForField(fieldId1);
		dtos = declaredYieldFieldVarietyBerriesDao.select(declaredYieldFieldCommodityBerriesGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
				
		//DELETE
		delete();
		
	}

	@Test 
	public void testDeleteForDeclaredYieldContract() throws Exception {

		String userId = "UNITTEST";
		
		DeclaredYieldFieldVarietyBerriesDao declaredYieldFieldVarietyBerriesDao = persistenceSpringConfig.declaredYieldFieldVarietyBerriesDao();

		createGrowerContractYear(userId, gcyId1, cropYear1);
		createField(userId);
		
		createAnnualFieldDetail(userId, cropYear1, annualFieldDetailId1);
		createContractedFieldDetail(userId, gcyId1, annualFieldDetailId1, contractedFieldDetailId1);

		String declaredYieldContractGuid = createDeclaredYieldContract(userId, cropYear1);
		String declaredYieldFieldCommodityBerriesGuid = createDeclaredYieldFieldCommodityBerries(userId, 12, "RASPBERRY", cropYear1);

		//INSERT Variety 1
		DeclaredYieldFieldVarietyBerriesDto newDto = new DeclaredYieldFieldVarietyBerriesDto();

		newDto.setAbandonmentYield(11.22);
		newDto.setCropVarietyId(1010694);
		newDto.setCropVarietyName("MALAHAT");
		newDto.setDeclaredYieldFieldCommodityBerriesGuid(declaredYieldFieldCommodityBerriesGuid);
		newDto.setPlantedAcres(33.44);
		newDto.setMatureEquivalentAcres(25.5);
		newDto.setSalesYield(55.66);
		newDto.setSoldShippedYield(77.88);
		newDto.setTotalProduction(99.00);
		newDto.setTotalProductionOverride(12.34);
		
		declaredYieldFieldVarietyBerriesDao.insert(newDto, userId);

		//INSERT Variety 2
		newDto = new DeclaredYieldFieldVarietyBerriesDto();

		newDto.setAbandonmentYield(11.22);
		newDto.setCropVarietyId(1010695);
		newDto.setCropVarietyName("MEEKER");
		newDto.setDeclaredYieldFieldCommodityBerriesGuid(declaredYieldFieldCommodityBerriesGuid);
		newDto.setPlantedAcres(33.44);
		newDto.setMatureEquivalentAcres(20.5);
		newDto.setSalesYield(55.66);
		newDto.setSoldShippedYield(77.88);
		newDto.setTotalProduction(99.00);
		newDto.setTotalProductionOverride(12.34);
		
		declaredYieldFieldVarietyBerriesDao.insert(newDto, userId);
		
		//GET BY COMMODITY
		List<DeclaredYieldFieldVarietyBerriesDto> dtos = declaredYieldFieldVarietyBerriesDao.select(declaredYieldFieldCommodityBerriesGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		//DELETE
		declaredYieldFieldVarietyBerriesDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
		
		//GET BY COMMODITY
		dtos = declaredYieldFieldVarietyBerriesDao.select(declaredYieldFieldCommodityBerriesGuid);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
	}


	@Test 
	public void testDeleteForFieldAndYear() throws Exception {

		String userId = "UNITTEST";
		
		DeclaredYieldFieldVarietyBerriesDao declaredYieldFieldVarietyBerriesDao = persistenceSpringConfig.declaredYieldFieldVarietyBerriesDao();

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
		
		//INSERT Variety 2019
		String declaredYieldFieldCommodityBerriesGuid1 = createDeclaredYieldFieldCommodityBerries(userId, 12, "RASPBERRY", cropYear1);

		DeclaredYieldFieldVarietyBerriesDto newDto = new DeclaredYieldFieldVarietyBerriesDto();

		newDto.setAbandonmentYield(11.22);
		newDto.setCropVarietyId(1010694);
		newDto.setCropVarietyName("MALAHAT");
		newDto.setDeclaredYieldFieldCommodityBerriesGuid(declaredYieldFieldCommodityBerriesGuid1);
		newDto.setPlantedAcres(33.44);
		newDto.setMatureEquivalentAcres(25.5);
		newDto.setSalesYield(55.66);
		newDto.setSoldShippedYield(77.88);
		newDto.setTotalProduction(99.00);
		newDto.setTotalProductionOverride(12.34);
		
		declaredYieldFieldVarietyBerriesDao.insert(newDto, userId);
		
		//INSERT Variety 2020
		String declaredYieldFieldCommodityBerriesGuid2 = createDeclaredYieldFieldCommodityBerries(userId, 12, "RASPBERRY", cropYear2);

		newDto = new DeclaredYieldFieldVarietyBerriesDto();

		newDto.setAbandonmentYield(11.22);
		newDto.setCropVarietyId(1010694);
		newDto.setCropVarietyName("MALAHAT");
		newDto.setDeclaredYieldFieldCommodityBerriesGuid(declaredYieldFieldCommodityBerriesGuid2);
		newDto.setPlantedAcres(33.44);
		newDto.setMatureEquivalentAcres(20.5);
		newDto.setSalesYield(55.66);
		newDto.setSoldShippedYield(77.88);
		newDto.setTotalProduction(99.00);
		newDto.setTotalProductionOverride(12.34);
		
		declaredYieldFieldVarietyBerriesDao.insert(newDto, userId);

				
		//GET BY COMMODITY
		List<DeclaredYieldFieldVarietyBerriesDto> dtos = declaredYieldFieldVarietyBerriesDao.select(declaredYieldFieldCommodityBerriesGuid1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		dtos = declaredYieldFieldVarietyBerriesDao.select(declaredYieldFieldCommodityBerriesGuid2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//DELETE 2020 data
		declaredYieldFieldVarietyBerriesDao.deleteForFieldAndYear(fieldId1, cropYear2);
		
		//GET BY COMMODITY
		dtos = declaredYieldFieldVarietyBerriesDao.select(declaredYieldFieldCommodityBerriesGuid1);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		dtos = declaredYieldFieldVarietyBerriesDao.select(declaredYieldFieldCommodityBerriesGuid2);
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

	private String createDeclaredYieldFieldCommodityBerries(String userId, Integer cropCommodityId, String cropCommodityName, Integer cropYear) throws DaoException {

		DeclaredYieldFieldCommodityBerriesDao dao = persistenceSpringConfig.declaredYieldFieldCommodityBerriesDao();
		
		DeclaredYieldFieldCommodityBerriesDto newDto = new DeclaredYieldFieldCommodityBerriesDto();

		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropCommodityName(cropCommodityName);
		newDto.setCropYear(cropYear);
		newDto.setFieldId(fieldId1);
		newDto.setTotalProduction(null);
		newDto.setTotalProductionOverride(null);

		//INSERT
		dao.insert(newDto, userId);		
		
		return newDto.getDeclaredYieldFieldCommodityBerriesGuid();	
	}
	
}
