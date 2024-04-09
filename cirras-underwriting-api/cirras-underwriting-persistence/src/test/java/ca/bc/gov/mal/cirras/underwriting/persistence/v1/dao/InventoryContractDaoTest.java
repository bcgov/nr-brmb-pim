package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryCoverageTotalForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryUnseededDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class InventoryContractDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String inventoryContractGuid;

	@Test 
	public void testInventoryContract() throws Exception {

		Integer contractId = 2876;
		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		String insurancePlanName = "GRAIN";
		
		String userId = "UNITTEST";
		String userId2 = "UNITTEST2";
		
		InventoryContractDao dao = persistenceSpringConfig.inventoryContractDao();
		
		InventoryContractDto newDto = new InventoryContractDto();

		newDto.setContractId(contractId);
		newDto.setCropYear(cropYear);
		newDto.setFertilizerInd(false);
		newDto.setGrainFromPrevYearInd(true);
		newDto.setHerbicideInd(true);
		newDto.setOtherChangesComment("Other changes comment");
		newDto.setOtherChangesInd(true);
		newDto.setSeededCropReportSubmittedInd(false);
		newDto.setTilliageInd(false);
		newDto.setUnseededIntentionsSubmittedInd(false);
		newDto.setInvUpdateUser(userId);


		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventoryContractGuid());

		inventoryContractGuid = newDto.getInventoryContractGuid();
		
		//FETCH
		InventoryContractDto fetchedDto = dao.fetch(inventoryContractGuid);
		
		Assert.assertEquals("InventoryContractGuid", newDto.getInventoryContractGuid(), fetchedDto.getInventoryContractGuid());
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("FertilizerInd", newDto.getFertilizerInd(), fetchedDto.getFertilizerInd());
		Assert.assertEquals("GrainFromPrevYearInd", newDto.getGrainFromPrevYearInd(), fetchedDto.getGrainFromPrevYearInd());
		Assert.assertEquals("HerbicideInd", newDto.getHerbicideInd(), fetchedDto.getHerbicideInd());
		Assert.assertEquals("OtherChangesComment", newDto.getOtherChangesComment(), fetchedDto.getOtherChangesComment());
		Assert.assertEquals("OtherChangesInd", newDto.getOtherChangesInd(), fetchedDto.getOtherChangesInd());
		Assert.assertEquals("SeededCropReportSubmittedInd", newDto.getSeededCropReportSubmittedInd(), fetchedDto.getSeededCropReportSubmittedInd());
		Assert.assertEquals("TilliageInd", newDto.getTilliageInd(), fetchedDto.getTilliageInd());
		Assert.assertEquals("UnseededIntentionsSubmittedInd", newDto.getUnseededIntentionsSubmittedInd(), fetchedDto.getUnseededIntentionsSubmittedInd());
		Assert.assertEquals("InsurancePlanId", insurancePlanId, fetchedDto.getInsurancePlanId());
		Assert.assertEquals("InsurancePlanName", insurancePlanName, fetchedDto.getInsurancePlanName());
		Assert.assertEquals("InvUpdateTimestamp", fetchedDto.getUpdateDate(), fetchedDto.getInvUpdateTimestamp()); //Compare with updated date
		Assert.assertEquals("InvUpdateUser", newDto.getInvUpdateUser(), fetchedDto.getInvUpdateUser());

		
		//SELECT
		List<InventoryContractDto> dtos = dao.select(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//UPDATE
		fetchedDto.setFertilizerInd(true);
		fetchedDto.setGrainFromPrevYearInd(false);
		fetchedDto.setHerbicideInd(false);
		fetchedDto.setOtherChangesComment("Other changes comment 2");
		fetchedDto.setOtherChangesInd(false);
		fetchedDto.setSeededCropReportSubmittedInd(true);
		fetchedDto.setTilliageInd(true);
		fetchedDto.setUnseededIntentionsSubmittedInd(true);
		fetchedDto.setInvUpdateUser(userId2);

		dao.update(fetchedDto, userId2);

		//FETCH
		InventoryContractDto updatedDto = dao.fetch(inventoryContractGuid);

		Assert.assertEquals("InventoryContractGuid", fetchedDto.getInventoryContractGuid(), updatedDto.getInventoryContractGuid());
		Assert.assertEquals("ContractId", fetchedDto.getContractId(), updatedDto.getContractId());
		Assert.assertEquals("CropYear", fetchedDto.getCropYear(), updatedDto.getCropYear());
		Assert.assertEquals("FertilizerInd", fetchedDto.getFertilizerInd(), updatedDto.getFertilizerInd());
		Assert.assertEquals("GrainFromPrevYearInd", fetchedDto.getGrainFromPrevYearInd(), updatedDto.getGrainFromPrevYearInd());
		Assert.assertEquals("HerbicideInd", fetchedDto.getHerbicideInd(), updatedDto.getHerbicideInd());
		Assert.assertEquals("OtherChangesComment", fetchedDto.getOtherChangesComment(), updatedDto.getOtherChangesComment());
		Assert.assertEquals("OtherChangesInd", fetchedDto.getOtherChangesInd(), updatedDto.getOtherChangesInd());
		Assert.assertEquals("SeededCropReportSubmittedInd", fetchedDto.getSeededCropReportSubmittedInd(), updatedDto.getSeededCropReportSubmittedInd());
		Assert.assertEquals("TilliageInd", fetchedDto.getTilliageInd(), updatedDto.getTilliageInd());
		Assert.assertEquals("UnseededIntentionsSubmittedInd", fetchedDto.getUnseededIntentionsSubmittedInd(), updatedDto.getUnseededIntentionsSubmittedInd());
		Assert.assertEquals("InsurancePlanId", insurancePlanId, updatedDto.getInsurancePlanId());
		Assert.assertEquals("InsurancePlanName", insurancePlanName, updatedDto.getInsurancePlanName());
		Assert.assertEquals("InvUpdateTimestamp", updatedDto.getUpdateDate(), updatedDto.getInvUpdateTimestamp()); //Compare with updated date
		Assert.assertEquals("InvUpdateUser", fetchedDto.getInvUpdateUser(), updatedDto.getInvUpdateUser());

		//DELETE
		dao.delete(inventoryContractGuid);
		
		//FETCH
		InventoryContractDto deletedDto = dao.fetch(inventoryContractGuid);
		Assert.assertNull(deletedDto);

		//SELECT
		dtos = dao.select(contractId, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
	
	}

	@Test 
	public void testGetByGrowerContract() throws Exception {

		
		Integer contractId = 2876; //Has to be a valid contract
		Integer growerContractYearId = 88962; //Has to be the correct gcyId
		Integer cropYear = 2020;
		Integer insurancePlanId = 4;
		String insurancePlanName = "GRAIN";
		String inventoryContractGuid;
		String userId = "UNITTEST";

		InventoryContractDao dao = persistenceSpringConfig.inventoryContractDao();
		
		InventoryContractDto newDto = new InventoryContractDto();

		newDto.setContractId(contractId);
		newDto.setCropYear(cropYear);
		newDto.setFertilizerInd(false);
		newDto.setGrainFromPrevYearInd(true);
		newDto.setHerbicideInd(true);
		newDto.setOtherChangesComment("Other changes comment");
		newDto.setOtherChangesInd(true);
		newDto.setSeededCropReportSubmittedInd(false);
		newDto.setTilliageInd(false);
		newDto.setUnseededIntentionsSubmittedInd(false);
		newDto.setInvUpdateUser(userId);

		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getInventoryContractGuid());

		inventoryContractGuid = newDto.getInventoryContractGuid();
		
		//FETCH
		InventoryContractDto fetchedDto = dao.getByGrowerContract(growerContractYearId);
		
		Assert.assertEquals("InventoryContractGuid", newDto.getInventoryContractGuid(), fetchedDto.getInventoryContractGuid());
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("FertilizerInd", newDto.getFertilizerInd(), fetchedDto.getFertilizerInd());
		Assert.assertEquals("GrainFromPrevYearInd", newDto.getGrainFromPrevYearInd(), fetchedDto.getGrainFromPrevYearInd());
		Assert.assertEquals("HerbicideInd", newDto.getHerbicideInd(), fetchedDto.getHerbicideInd());
		Assert.assertEquals("OtherChangesComment", newDto.getOtherChangesComment(), fetchedDto.getOtherChangesComment());
		Assert.assertEquals("OtherChangesInd", newDto.getOtherChangesInd(), fetchedDto.getOtherChangesInd());
		Assert.assertEquals("SeededCropReportSubmittedInd", newDto.getSeededCropReportSubmittedInd(), fetchedDto.getSeededCropReportSubmittedInd());
		Assert.assertEquals("TilliageInd", newDto.getTilliageInd(), fetchedDto.getTilliageInd());
		Assert.assertEquals("UnseededIntentionsSubmittedInd", newDto.getUnseededIntentionsSubmittedInd(), fetchedDto.getUnseededIntentionsSubmittedInd());
		Assert.assertEquals("InsurancePlanId", insurancePlanId, fetchedDto.getInsurancePlanId());
		Assert.assertEquals("InsurancePlanName", insurancePlanName, fetchedDto.getInsurancePlanName());
		Assert.assertEquals("InvUpdateTimestamp", fetchedDto.getUpdateDate(), fetchedDto.getInvUpdateTimestamp()); //Compare with updated date
		Assert.assertEquals("InvUpdateUser", newDto.getInvUpdateUser(), fetchedDto.getInvUpdateUser());

		//DELETE
		dao.delete(inventoryContractGuid);
		
		//FETCH
		InventoryContractDto deletedDto = dao.fetch(inventoryContractGuid);
		Assert.assertNull(deletedDto);
	
	}	
	
	@Test 
	public void testSelectForPrintout() throws Exception {
		
		PolicyDao policyDao = persistenceSpringConfig.policyDao();

		Integer maxRows = new Integer(1000);
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(50);
		Integer cropYear = 2020;
		String policyNumber = "144733-20";
		Integer insurancePlanId = 4;
		String insurancePlanName = "GRAIN";
		
		PagedDtos<PolicyDto> policyDtos = policyDao.select(cropYear, null, null, null, policyNumber, null, null, null, null, maxRows, pageNumber, pageRowCount);
		
		Assert.assertNotNull(policyDtos);		
		Assert.assertEquals(1, policyDtos.getTotalRowCount());
		
		PolicyDto policyDto = policyDtos.getResults().get(0);
		Assert.assertNotNull(policyDto);		

		Integer contractId = policyDto.getContractId();
		Integer growerContractYearId = policyDto.getGrowerContractYearId();
		String growerName = policyDto.getGrowerName();
		Integer growerNumber = policyDto.getGrowerNumber();
		
		String inventoryContractGuid;
		String userId = "UNITTEST";

		InventoryContractDao dao = persistenceSpringConfig.inventoryContractDao();
		
		InventoryContractDto newDto = new InventoryContractDto();

		//Check if the inventory contract already exists
		List<InventoryContractDto> dtos = dao.select(contractId, cropYear);

		if (dtos == null || dtos.size() == 0) {

			newDto.setContractId(contractId);
			newDto.setCropYear(cropYear);
			newDto.setFertilizerInd(false);
			newDto.setGrainFromPrevYearInd(true);
			newDto.setHerbicideInd(true);
			newDto.setOtherChangesComment("Other changes comment");
			newDto.setOtherChangesInd(true);
			newDto.setSeededCropReportSubmittedInd(false);
			newDto.setTilliageInd(false);
			newDto.setUnseededIntentionsSubmittedInd(false);
			newDto.setInvUpdateUser(userId);

			//INSERT
			dao.insert(newDto, userId);
			Assert.assertNotNull(newDto.getInventoryContractGuid());
		
		} else {
			newDto = dtos.get(0);
			newDto.setInvUpdateUser(newDto.getUpdateUser());
		}

		inventoryContractGuid = newDto.getInventoryContractGuid();
		
		//SelectForPrintout
		InventoryContractDto fetchedDto = dao.selectForPrintout(inventoryContractGuid);
		
		Assert.assertEquals("InventoryContractGuid", newDto.getInventoryContractGuid(), fetchedDto.getInventoryContractGuid());
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("FertilizerInd", newDto.getFertilizerInd(), fetchedDto.getFertilizerInd());
		Assert.assertEquals("GrainFromPrevYearInd", newDto.getGrainFromPrevYearInd(), fetchedDto.getGrainFromPrevYearInd());
		Assert.assertEquals("HerbicideInd", newDto.getHerbicideInd(), fetchedDto.getHerbicideInd());
		Assert.assertEquals("OtherChangesComment", newDto.getOtherChangesComment(), fetchedDto.getOtherChangesComment());
		Assert.assertEquals("OtherChangesInd", newDto.getOtherChangesInd(), fetchedDto.getOtherChangesInd());
		Assert.assertEquals("SeededCropReportSubmittedInd", newDto.getSeededCropReportSubmittedInd(), fetchedDto.getSeededCropReportSubmittedInd());
		Assert.assertEquals("TilliageInd", newDto.getTilliageInd(), fetchedDto.getTilliageInd());
		Assert.assertEquals("UnseededIntentionsSubmittedInd", newDto.getUnseededIntentionsSubmittedInd(), fetchedDto.getUnseededIntentionsSubmittedInd());
		Assert.assertEquals("GrowerContractYearId", growerContractYearId, fetchedDto.getGrowerContractYearId());
		Assert.assertEquals("PolicyNumber", policyNumber, fetchedDto.getPolicyNumber());
		Assert.assertEquals("GrowerName", growerName, fetchedDto.getGrowerName());
		Assert.assertEquals("GrowerNumber", growerNumber, fetchedDto.getGrowerNumber());
		Assert.assertEquals("InsurancePlanId", insurancePlanId, fetchedDto.getInsurancePlanId());
		Assert.assertEquals("InsurancePlanName", insurancePlanName, fetchedDto.getInsurancePlanName());
		Assert.assertNotNull("InvUpdateTimestamp", fetchedDto.getInvUpdateTimestamp());
		Assert.assertEquals("InvUpdateUser", newDto.getInvUpdateUser(), fetchedDto.getInvUpdateUser());

	}	
	
	private InventoryContractDao inventoryContractDao;
	private InventoryContractCommodityDao inventoryContractCommodityDao;
	private InventoryCoverageTotalForageDao inventoryCoverageTotalForageDao;
	private InventoryFieldDao inventoryFieldDao;
	private AnnualFieldDetailDao annualFieldDetailDao;
	private ContractedFieldDetailDao contractedFieldDetailDao;
	private InventorySeededGrainDao inventorySeededGrainDao;
	private InventoryUnseededDao inventoryUnseededDao;
	private InventorySeededForageDao inventorySeededForageDao;
	private FieldDao fieldDao;
	private String inventoryContractCommodityGuid;
	private String inventoryCoverageTotalForageGuid;
	private String inventoryFieldGuid1;
	private String inventoryFieldGuid2;
	private String inventorySeededForageGuid;
	private Integer fieldId = 99999999;
	private Integer contractedFieldDetailId = 999999876;
	private Integer annualFieldDetailId = 999888999;
	private Integer cropYear = 2020;
	private Integer growerContractYearId = 88962;
	private Integer insurancePlanId1 = 4;
	//private Integer insurancePlanId2 = 5;
	
	@Test 
	public void testDeleteInventoryContract() throws Exception {

		String userId = "UNITTEST";
		
		inventoryContractDao = persistenceSpringConfig.inventoryContractDao();
		inventoryContractCommodityDao = persistenceSpringConfig.inventoryContractCommodityDao();
		inventoryCoverageTotalForageDao = persistenceSpringConfig.inventoryCoverageTotalForageDao();
		fieldDao = persistenceSpringConfig.fieldDao();
		inventoryFieldDao = persistenceSpringConfig.inventoryFieldDao();
		annualFieldDetailDao = persistenceSpringConfig.annualFieldDetailDao();
		contractedFieldDetailDao = persistenceSpringConfig.contractedFieldDetailDao();
		inventorySeededGrainDao = persistenceSpringConfig.inventorySeededGrainDao();
		inventoryUnseededDao = persistenceSpringConfig.inventoryUnseededDao();
		inventorySeededForageDao = persistenceSpringConfig.inventorySeededForageDao();
		
		//Create inventory contract
		createInventoryContract(userId);

		//Create contract commodity
		createInventoryContractCommodity(16, "BARLEY", 10.5, true, userId);
		
		//Create contract total forage
		createInventoryCoverageTotalForage(71, "SILAGE CORN", 7.77, userId);
		
		//Create Field
		createField(userId);
		
		//Create Forage Inventory Field
		inventoryFieldGuid2 = createInventoryField(userId, insurancePlanId1, 1);

		//Create Seeded Forage
		createSeededForage(userId);
		
		//Create Forage Inventory Field
		inventoryFieldGuid1 = createInventoryField(userId, insurancePlanId1, 2);
		
		//Create unseeded Grain
		createUnseeded(25.0, 16, "BARLEY", userId);
		
		//Create Seeded Grain
		createSeededGrain("Forage Oat", 24, 1010570, inventoryFieldGuid1, false, 30.0, userId);
		createSeededGrain("CPSW", 26, 1010602, inventoryFieldGuid1, true, 0.0, userId);
		
		//FETCH
		InventoryContractDto fetchedDto = inventoryContractDao.fetch(inventoryContractGuid);
		Assert.assertNotNull(fetchedDto);

		//Remove the link to seeded forage record before deleting it
		inventoryFieldDao.removeLinkToPlantingForInventoryContract(inventoryContractGuid, userId);
		
		//DELETE
		inventorySeededForageDao.deleteForInventoryContract(inventoryContractGuid);
		inventoryUnseededDao.deleteForInventoryContract(inventoryContractGuid);
		inventorySeededGrainDao.deleteForInventoryContract(inventoryContractGuid);
		inventoryFieldDao.deleteForInventoryContract(inventoryContractGuid);
		contractedFieldDetailDao.delete(contractedFieldDetailId);
		annualFieldDetailDao.delete(annualFieldDetailId);
		fieldDao.delete(fieldId);
		inventoryCoverageTotalForageDao.deleteForInventoryContract(inventoryContractGuid);
		inventoryContractCommodityDao.deleteForInventoryContract(inventoryContractGuid);
		inventoryContractDao.delete(inventoryContractGuid);
		
		//FETCH
		InventoryContractDto deletedDto = inventoryContractDao.fetch(inventoryContractGuid);
		Assert.assertNull(deletedDto);

		InventoryContractCommodityDto deletedContractCommodityDto = inventoryContractCommodityDao.fetch(inventoryContractCommodityGuid);
		Assert.assertNull(deletedContractCommodityDto);

		InventoryCoverageTotalForageDto deletedCoverageTotalForageDto = inventoryCoverageTotalForageDao.fetch(inventoryCoverageTotalForageGuid);
		Assert.assertNull(deletedCoverageTotalForageDto);

		List<InventorySeededGrainDto> deletedInventorySeededGrainDto = inventorySeededGrainDao.select(inventoryFieldGuid1);
		Assert.assertTrue(deletedInventorySeededGrainDto == null || deletedInventorySeededGrainDto.size() == 0);
		
		List<InventoryUnseededDto> deletedInventoryUnseededDto = inventoryUnseededDao.select(inventoryFieldGuid1);
		Assert.assertTrue(deletedInventoryUnseededDto == null || deletedInventoryUnseededDto.size() == 0);

		List<InventorySeededForageDto> deletedInventorySeededForageDto = inventorySeededForageDao.select(inventoryFieldGuid2);
		Assert.assertTrue(deletedInventorySeededForageDto == null || deletedInventorySeededForageDto.size() == 0);

		InventoryFieldDto deletedInventoryField = inventoryFieldDao.fetch(inventoryFieldGuid1);
		Assert.assertNull(deletedInventoryField);

		deletedInventoryField = inventoryFieldDao.fetch(inventoryFieldGuid2);
		Assert.assertNull(deletedInventoryField);
		
		AnnualFieldDetailDto deletedAnnualFieldDetailDto = annualFieldDetailDao.fetch(annualFieldDetailId);
		Assert.assertNull(deletedAnnualFieldDetailDto);
		
		ContractedFieldDetailDto deletedContractedFieldDetailDto = contractedFieldDetailDao.fetch(contractedFieldDetailId);
		Assert.assertNull(deletedContractedFieldDetailDto);

		FieldDto deletedField = fieldDao.fetch(fieldId);
		Assert.assertNull(deletedField);
		
		/*
		 IN CASE SOMETHING GOES WRONG, THE DATA CAN BE REMOVED WITH THESE STATEMENTS 
		 
		 	update inventory_field set
			underseeded_inventory_seeded_forage_guid = null
			where field_id = 99999999;
			
			delete from inventory_seeded_forage t
			where t.inventory_field_guid in (select inventory_field_guid from inventory_field where field_id = 99999999);
			
			delete from inventory_seeded_grain t
			where t.inventory_field_guid in (select inventory_field_guid from inventory_field where field_id = 99999999);
			
			delete from inventory_unseeded t
			where t.inventory_field_guid in (select inventory_field_guid from inventory_field where field_id = 99999999);
			
			delete from inventory_field t where t.field_id = 99999999;
			
			delete from contracted_field_detail t
			where t.annual_field_detail_id in (select annual_field_detail_id from annual_field_detail t where t.field_id = 99999999);
			
			delete from annual_field_detail t where t.field_id = 99999999;
			
			delete from field t where t.field_id = 99999999;
			
			delete from inventory_contract_commodity t 
			where t.inventory_contract_guid in (select inventory_contract_guid from inventory_contract where contract_id = 2876 and crop_year = 2020);
			
			delete from inventory_coverage_total_forage t 
			where t.inventory_contract_guid in (select inventory_contract_guid from inventory_contract where contract_id = 2876 and crop_year = 2020);
			
			delete from inventory_contract t where t.contract_id = 2876 and t.crop_year = 2020;

		*/

	}	
	
	private void createInventoryContract(String userId) throws DaoException {
		InventoryContractDao invContractDao = persistenceSpringConfig.inventoryContractDao();

		Integer contractId = 2876;

		// Create parent InventoryContract.
		InventoryContractDto invContractDto = new InventoryContractDto();

		invContractDto.setContractId(contractId);
		invContractDto.setCropYear(cropYear);
		invContractDto.setFertilizerInd(false);
		invContractDto.setGrainFromPrevYearInd(true);
		invContractDto.setHerbicideInd(true);
		invContractDto.setOtherChangesComment("Other changes comment");
		invContractDto.setOtherChangesInd(true);
		invContractDto.setSeededCropReportSubmittedInd(false);
		invContractDto.setTilliageInd(false);
		invContractDto.setUnseededIntentionsSubmittedInd(false);

		invContractDao.insert(invContractDto, userId);
		inventoryContractGuid = invContractDto.getInventoryContractGuid();
	}
	
	private void createInventoryContractCommodity(
			Integer cropCommodityId,
			String cropCommodityName,
			Double totalSeededAcres,
			Boolean isPedigreedInd,
			String userId) throws DaoException {
	
		// INSERT
		InventoryContractCommodityDto newDto = new InventoryContractCommodityDto();
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropCommodityName(cropCommodityName);
		newDto.setInventoryContractGuid(inventoryContractGuid);
		newDto.setTotalSeededAcres(totalSeededAcres);
		newDto.setTotalSpotLossAcres(null);
		newDto.setTotalUnseededAcres(null);
		newDto.setTotalUnseededAcresOverride(null);
		newDto.setIsPedigreeInd(isPedigreedInd);
	
		inventoryContractCommodityDao.insert(newDto, userId);
		inventoryContractCommodityGuid = newDto.getInventoryContractCommodityGuid();
	}	
	
	private void createInventoryCoverageTotalForage(
			Integer cropCommodityId,
			String cropCommodityName,
			Double totalFieldAcres,
			String userId) throws DaoException {
	
		// INSERT
		InventoryCoverageTotalForageDto newDto = new InventoryCoverageTotalForageDto();
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropCommodityName(cropCommodityName);
		newDto.setInventoryContractGuid(inventoryContractGuid);
		newDto.setPlantInsurabilityTypeCode(null);
		newDto.setPlantInsurabilityTypeDesc(null);
		newDto.setTotalFieldAcres(totalFieldAcres);
		newDto.setIsUnseededInsurableInd(false);

		inventoryCoverageTotalForageDao.insert(newDto, userId);
		inventoryCoverageTotalForageGuid = newDto.getInventoryCoverageTotalForageGuid();
	}	
	
	private void createField(String userId) throws DaoException {
		// INSERT FIELD

		String fieldLabel = "Test Field Label";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;
		
		FieldDto newFieldDto = new FieldDto();
		newFieldDto.setFieldId(fieldId);
		newFieldDto.setFieldLabel(fieldLabel);
		newFieldDto.setActiveFromCropYear(activeFromCropYear);
		newFieldDto.setActiveToCropYear(activeToCropYear);

		fieldDao.insertDataSync(newFieldDto, userId);
		
		//INSERT Annual Field Detail record
		AnnualFieldDetailDto newAnnualFieldDetailDto = new AnnualFieldDetailDto();
		
		newAnnualFieldDetailDto.setAnnualFieldDetailId(annualFieldDetailId);
		newAnnualFieldDetailDto.setLegalLandId(null);
		newAnnualFieldDetailDto.setFieldId(fieldId);
		newAnnualFieldDetailDto.setCropYear(cropYear);

		annualFieldDetailDao.insertDataSync(newAnnualFieldDetailDto, userId);
		
		//INSERT Contracted Field Detail record		
		ContractedFieldDetailDto newContractedFieldDetailDto = new ContractedFieldDetailDto();

		newContractedFieldDetailDto.setContractedFieldDetailId(contractedFieldDetailId);
		newContractedFieldDetailDto.setAnnualFieldDetailId(annualFieldDetailId);
		newContractedFieldDetailDto.setDisplayOrder(1);
		newContractedFieldDetailDto.setGrowerContractYearId(growerContractYearId);

		contractedFieldDetailDao.insertDataSync(newContractedFieldDetailDto, userId);

	}
	
	private String createInventoryField(String userId, Integer insurancePlanId, Integer plantingNumber) throws DaoException {
		// INSERT
		InventoryFieldDto newDto = new InventoryFieldDto();

		newDto.setCropYear(cropYear);
		newDto.setFieldId(fieldId);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setLastYearCropCommodityId(20);
		newDto.setLastYearCropCommodityName("FALL RYE");
		newDto.setIsHiddenOnPrintoutInd(false);
		newDto.setUnderseededAcres(14.4);
		newDto.setUnderseededCropVarietyId(1010513);
		newDto.setUnderseededCropVarietyName("BRASETTO");
		newDto.setPlantingNumber(plantingNumber);
		//Link plantings
		newDto.setUnderseededInventorySeededForageGuid(inventorySeededForageGuid);

		inventoryFieldDao.insert(newDto, userId);
		return newDto.getInventoryFieldGuid();
	}	
	
	private void createSeededGrain(
			String commodityTypeCode, 
			Integer cropCommodityId, 
			Integer cropVarietyId, 
			String inventoryFieldGuid, 
			Boolean isReplacedInd, 
			Double seededAcres, 
			String userId
		) throws DaoException {
		
		InventorySeededGrainDto newDto = new InventorySeededGrainDto();

		newDto.setCommodityTypeCode(commodityTypeCode);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropVarietyId(cropVarietyId);
		newDto.setInventoryFieldGuid(inventoryFieldGuid);
		newDto.setIsPedigreeInd(true);
		newDto.setIsSpotLossInsurableInd(false);
		newDto.setIsQuantityInsurableInd(true);
		newDto.setIsReplacedInd(isReplacedInd);
		newDto.setSeededAcres(seededAcres);
		newDto.setSeededDate(null);
		
		inventorySeededGrainDao.insert(newDto, userId);

	}
	
	private void createUnseeded(
			Double acresToBeSeeded,
			Integer cropCommodityId,
			String cropName,
			String userId) throws DaoException {
		
		//Insert unseeded
		InventoryUnseededDto newDto = new InventoryUnseededDto();

		newDto.setAcresToBeSeeded(acresToBeSeeded);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropCommodityName(cropName);
		newDto.setInventoryFieldGuid(inventoryFieldGuid1);
		newDto.setIsUnseededInsurableInd(true);
		
		inventoryUnseededDao.insert(newDto, userId);
	}
	
	private void createSeededForage(String userId) throws DaoException {
		
		InventorySeededForageDto newDto = new InventorySeededForageDto();

		newDto.setInventoryFieldGuid(inventoryFieldGuid2);
		newDto.setCommodityTypeCode("CPSW");
		newDto.setCropCommodityId(26);
		newDto.setCropVarietyId(1010602);
		newDto.setCropVarietyName("AAC ENTICE");
		newDto.setFieldAcres(10.4);
		newDto.setSeedingYear(2020);
		newDto.setIsIrrigatedInd(true);
		newDto.setIsQuantityInsurableInd(false);
		newDto.setPlantInsurabilityTypeCode("E1");
		newDto.setIsAwpEligibleInd(true);
		
		inventorySeededForageDao.insert(newDto, userId);
		
		InventorySeededForageDto newDto2 = new InventorySeededForageDto();
		newDto2.setCommodityTypeCode("Six Row");
		newDto2.setCropCommodityId(16);
		newDto2.setCropVarietyId(1010640);
		newDto2.setCropVarietyName("CDC TITANIUM");
		newDto2.setInventoryFieldGuid(inventoryFieldGuid2);
		newDto2.setFieldAcres(22.5);
		newDto2.setSeedingYear(2021);
		newDto2.setIsIrrigatedInd(false);
		newDto2.setIsQuantityInsurableInd(true);
		newDto2.setPlantInsurabilityTypeCode("W1");
		newDto2.setIsAwpEligibleInd(false);
		inventorySeededForageDao.insert(newDto2, userId);
		
		inventorySeededForageGuid = newDto2.getInventorySeededForageGuid();
		
	}
}
