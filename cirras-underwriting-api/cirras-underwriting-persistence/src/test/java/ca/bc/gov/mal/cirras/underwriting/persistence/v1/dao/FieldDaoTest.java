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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandFieldXrefDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class FieldDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer fieldId = 99999999;

	private Integer growerId = 90000001;
	private Integer policyId = 90000002;
	private Integer contractId = 90000003;
	private Integer gcyId = 90000004;

	private Integer legalLandId = 90000006;
	private Integer legalLandId2 = 90000018;

	private Integer fieldId1 = 90000005;
	private Integer afdId1 = 90000007;
	private Integer cfdId1 = 90000008;

	private Integer fieldId2 = 90000009;
	private Integer afdId2 = 90000010;
	private Integer cfdId2 = 90000011;
	

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{
		deleteContractedFieldDetail(cfdId1);
		deleteContractedFieldDetail(cfdId2);

		deleteGrowerContractYear(gcyId);
		deletePolicy(policyId);
		deleteGrower(growerId);

		deleteAnnualFieldDetail(afdId1);
		deleteAnnualFieldDetail(afdId2);
		
		deleteLegalLandFieldXref(fieldId1, legalLandId);
		deleteLegalLandFieldXref(fieldId1, legalLandId2);
		deleteLegalLandFieldXref(fieldId2, legalLandId);
		
		deleteLegalLand(legalLandId);
		deleteLegalLand(legalLandId2);
		
		deleteField(fieldId);
		deleteField(fieldId1);
		deleteField(fieldId2);
	}
	
	@Test 
	public void testInsertUpdateDeleteField() throws Exception {

		FieldDao dao = persistenceSpringConfig.fieldDao();
		FieldDto newDto = new FieldDto();
		
		
		String fieldLabel = "Test Field Label";
		String location = "location a";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setFieldId(null);
		newDto.setFieldLabel(fieldLabel);
		newDto.setLocation(location);
		newDto.setActiveFromCropYear(activeFromCropYear);
		newDto.setActiveToCropYear(activeToCropYear);

		dao.insert(newDto, userId);
		
		//FETCH
		FieldDto fetchedDto = dao.fetch(newDto.getFieldId());

		Assert.assertEquals("FieldId", newDto.getFieldId(), fetchedDto.getFieldId());
		Assert.assertEquals("FieldLabel", newDto.getFieldLabel(), fetchedDto.getFieldLabel());
		Assert.assertEquals("Location", newDto.getLocation(), fetchedDto.getLocation());
		Assert.assertEquals("ActiveFromCropYear", newDto.getActiveFromCropYear(), fetchedDto.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear", newDto.getActiveToCropYear(), fetchedDto.getActiveToCropYear());
		
		//UPDATE
		fieldLabel = "Test Field Label 2";
		location = "location b";
		activeFromCropYear = 2000;
		activeToCropYear = 2012;
		
		fetchedDto.setFieldLabel(fieldLabel);
		fetchedDto.setActiveFromCropYear(activeFromCropYear);
		fetchedDto.setActiveToCropYear(activeToCropYear);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		FieldDto updatedDto = dao.fetch(fetchedDto.getFieldId());

		Assert.assertEquals("FieldLabel", fetchedDto.getFieldLabel(), updatedDto.getFieldLabel());
		Assert.assertEquals("Location", newDto.getLocation(), fetchedDto.getLocation());
		Assert.assertEquals("ActiveFromCropYear", fetchedDto.getActiveFromCropYear(), updatedDto.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear", fetchedDto.getActiveToCropYear(), updatedDto.getActiveToCropYear());
		
		//DELETE
		dao.delete(updatedDto.getFieldId());

		//FETCH
		FieldDto deletedDto = dao.fetch(updatedDto.getFieldId());
		Assert.assertNull(deletedDto);

	}

	@Test 
	public void testselectForLegalLandOrField() throws Exception {
		// Create test data
		Integer cropYear = 2022;

		//Legal Land
		LegalLandDto legalLandDto1 = createLegalLand(legalLandId, cropYear, "legal desc 1", "short legal desc 1", "other legal desc 1", "111-222-333");
		LegalLandDto legalLandDto2 = createLegalLand(legalLandId2, cropYear, "legal desc 2", "short legal desc 2", "other legal desc 2", "999-888-777");

		// Fields
		FieldDto field1 = createField(fieldId1, "LOT 1", cropYear, "TEST Location 16159");
		FieldDto field2 = createField(fieldId2, "LOT 2", cropYear, "TEST Location 29943");

		// Add Legal Land Field XREF
		createLegalLandFieldXref(fieldId1, legalLandId);
		createLegalLandFieldXref(fieldId1, legalLandId2);
		createLegalLandFieldXref(fieldId2, legalLandId);
		
		createAnnualFieldDetail(afdId1, fieldId1, cropYear, legalLandId);
		createAnnualFieldDetail(afdId2, fieldId2, cropYear, legalLandId);
		
		FieldDao dao = persistenceSpringConfig.fieldDao();
		
		//Get by legal land id
		List<FieldDto> dtos = dao.selectForLegalLandOrField(legalLandId, null, null, cropYear);

		Assert.assertNotNull(dtos);
		Assert.assertEquals("dtos size 1", 2, dtos.size());
		
		checkAddFieldFields(field1, legalLandDto1, getFieldById(fieldId1, dtos));
		checkAddFieldFields(field2, legalLandDto1, getFieldById(fieldId2, dtos));
		
		//Get by field id
		dtos = dao.selectForLegalLandOrField(null, fieldId1, null, cropYear);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals("dtos size 2 field", 1, dtos.size());

		checkAddFieldFields(field1, legalLandDto1, getFieldById(fieldId1, dtos));
		
		//Get by field location (Partial Search): TEST Location - Expect 2 fields
		dtos = dao.selectForLegalLandOrField(null, null, "test location", cropYear);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals("dtos size 3 field location", 2, dtos.size());

		checkAddFieldFields(field1, legalLandDto1, getFieldById(fieldId1, dtos));
		checkAddFieldFields(field2, legalLandDto1, getFieldById(fieldId2, dtos));
		
		//Get by field location: Expect 1 field
		dtos = dao.selectForLegalLandOrField(null, null, "test location 29943", cropYear);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals("dtos size 4 field location", 1, dtos.size());

		checkAddFieldFields(field2, legalLandDto1, getFieldById(fieldId2, dtos));
		
		delete();
	}
	
	private FieldDto getFieldById(Integer fieldId, List<FieldDto> fields) {
		
		List<FieldDto> filteredList = fields.stream().filter(x -> x.getFieldId().equals(fieldId)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, filteredList.size());
		
		return filteredList.get(0);
	}
	
	private LegalLandDto getLegaLandById(Integer legalLandId, List<LegalLandDto> legalLandList) {
		
		List<LegalLandDto> filteredList = legalLandList.stream().filter(x -> x.getLegalLandId().equals(legalLandId)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, filteredList.size());
		
		return filteredList.get(0);
	}

	private void checkAddFieldFields(FieldDto expectedField, LegalLandDto expectedLegalLand, FieldDto actualField) {
		Assert.assertEquals("FieldId", expectedField.getFieldId(), actualField.getFieldId());
		Assert.assertEquals("FieldLabel", expectedField.getFieldLabel(), actualField.getFieldLabel());
		Assert.assertEquals("Location", expectedField.getLocation(), actualField.getLocation());
		Assert.assertEquals("PrimaryPropertyIdentifier", expectedLegalLand.getPrimaryPropertyIdentifier(), actualField.getPrimaryPropertyIdentifier());
		Assert.assertEquals("LegalLandId", expectedLegalLand.getLegalLandId(), actualField.getLegalLandId());
		Assert.assertEquals("OtherLegalDescription", expectedLegalLand.getOtherDescription(), actualField.getOtherLegalDescription());
	}
	
	@Test 
	public void testselectForLegalLandOrField_OLD() throws Exception {
		
		//Pre-Conditions:
		//Legal Land needs 2 fields in the first cropYear and 1 field in the second cropYear

		FieldDao dao = persistenceSpringConfig.fieldDao();
		
		Integer legalLandId = 2723;
		Integer fieldId = 30121;
		Integer cropYear = 2022;
		String otherLegalDescription = "PCLA(SEE 60255I) OF PCLB(SEE 16200I) L 5 DL 812 PL 730A EXC PT INCL IN SRW PL 13512";
		
		//Get by legal land id
		List<FieldDto> dtos = dao.selectForLegalLandOrField(legalLandId, null, null, cropYear);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals("dtos size 1", 2, dtos.size());
		
		for (FieldDto dto : dtos) {
			Assert.assertTrue("Max Crop Year 1: " + dto.getMaxCropYear(), dto.getMaxCropYear() <= cropYear);
			Assert.assertEquals("LegalLandId 1", legalLandId, dto.getLegalLandId());
			Assert.assertEquals("OtherLegalDescription 1", otherLegalDescription, dto.getOtherLegalDescription());
		}

		//Get by field id
		dtos = dao.selectForLegalLandOrField(null, fieldId, null, cropYear);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals("dtos size 1 field", 1, dtos.size());
		
		for (FieldDto dto : dtos) {
			Assert.assertTrue("Max Crop Year 1 field: " + dto.getMaxCropYear(), dto.getMaxCropYear() <= cropYear);
			Assert.assertEquals("LegalLandId 1 field", legalLandId, dto.getLegalLandId());
			Assert.assertEquals("OtherLegalDescription 1 field", otherLegalDescription, dto.getOtherLegalDescription());
		}

		//Same legal land but different year where one of the fields has a different legal land id
		cropYear = 2023;
		
		//Get by legal land id
		dtos = dao.selectForLegalLandOrField(legalLandId, null, null, cropYear);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals("dtos size 2", 1, dtos.size());
		
		for (FieldDto dto : dtos) {
			Assert.assertTrue("Max Crop Year 2: " + dto.getMaxCropYear(), dto.getMaxCropYear() <= cropYear);
			Assert.assertEquals("LegalLandId 2", legalLandId, dto.getLegalLandId());
			Assert.assertEquals("OtherLegalDescription 2", otherLegalDescription, dto.getOtherLegalDescription());
		}
		
		//Get by field id
		dtos = dao.selectForLegalLandOrField(null, fieldId, null, cropYear);
		Assert.assertNotNull(dtos);
		Assert.assertEquals("dtos size 2 field", 1, dtos.size());
		
		for (FieldDto dto : dtos) {
			Assert.assertTrue("Max Crop Year 2 field: " + dto.getMaxCropYear(), dto.getMaxCropYear() <= cropYear);
			Assert.assertEquals("LegalLandId 2 field", legalLandId, dto.getLegalLandId());
			Assert.assertEquals("OtherLegalDescription 2 field", otherLegalDescription, dto.getOtherLegalDescription());
		}

		
	}	

	@Test 
	public void testSelectByLastPolicyForLegalLand() throws Exception {

		// Create test data
		Integer cropYear = 2022;

		createGrower(growerId, 999888, "grower name");		

		createPolicy(policyId, growerId, contractId, cropYear, "123456-22", "123456");
		createGrowerContractYear(gcyId, contractId, growerId, cropYear);
		
		createLegalLand(legalLandId, cropYear, "legal desc", "short legal desc", "other legal desc", "111-222-333");

		// Field 1
		createField(fieldId1, "LOT 1", cropYear, null);
		createAnnualFieldDetail(afdId1, fieldId1, cropYear, legalLandId);
		createContractedFieldDetail(cfdId1, afdId1, gcyId);

		// Field 2
		createField(fieldId2, "LOT 2", cropYear, null);
		createAnnualFieldDetail(afdId2, fieldId2, cropYear, legalLandId);
		createContractedFieldDetail(cfdId2, afdId2, gcyId);
		
		FieldDao dao = persistenceSpringConfig.fieldDao();
		
		// Test 1: LegalLandId and CropYear filters.
		// 1A. Same year.
		List<FieldDto> dtos = dao.selectByLastPolicyForLegalLand(legalLandId, cropYear, null, null, null);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		Assert.assertEquals(fieldId1, dtos.get(0).getFieldId());
		Assert.assertEquals("LOT 1", dtos.get(0).getFieldLabel());
		Assert.assertEquals(cropYear, dtos.get(0).getMaxCropYear());
		
		Assert.assertEquals(fieldId2, dtos.get(1).getFieldId());
		Assert.assertEquals("LOT 2", dtos.get(1).getFieldLabel());
		Assert.assertEquals(cropYear, dtos.get(1).getMaxCropYear());

		// 1B. Newer year.
		dtos = dao.selectByLastPolicyForLegalLand(legalLandId, cropYear + 1, null, null, null);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		Assert.assertEquals(fieldId1, dtos.get(0).getFieldId());
		Assert.assertEquals("LOT 1", dtos.get(0).getFieldLabel());
		Assert.assertEquals(cropYear, dtos.get(0).getMaxCropYear());
		
		Assert.assertEquals(fieldId2, dtos.get(1).getFieldId());
		Assert.assertEquals("LOT 2", dtos.get(1).getFieldLabel());
		Assert.assertEquals(cropYear, dtos.get(1).getMaxCropYear());
		
		// 1C. Older year.
		dtos = dao.selectByLastPolicyForLegalLand(legalLandId, cropYear - 1, null, null, null);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		// Test 2: includeContractId filter.
		dtos = dao.selectByLastPolicyForLegalLand(legalLandId, cropYear, contractId, null, null);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		Assert.assertEquals(fieldId1, dtos.get(0).getFieldId());
		Assert.assertEquals("LOT 1", dtos.get(0).getFieldLabel());
		Assert.assertEquals(cropYear, dtos.get(0).getMaxCropYear());
		
		Assert.assertEquals(fieldId2, dtos.get(1).getFieldId());
		Assert.assertEquals("LOT 2", dtos.get(1).getFieldLabel());
		Assert.assertEquals(cropYear, dtos.get(1).getMaxCropYear());
		
		// Test 3: excludeContractId filter.
		dtos = dao.selectByLastPolicyForLegalLand(legalLandId, cropYear, null, contractId, null);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
		
		// Test 4: excludeFieldId filter.
		dtos = dao.selectByLastPolicyForLegalLand(legalLandId, cropYear, null, null, fieldId1);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
				
		Assert.assertEquals(fieldId2, dtos.get(0).getFieldId());
		Assert.assertEquals("LOT 2", dtos.get(0).getFieldLabel());
		Assert.assertEquals(cropYear, dtos.get(0).getMaxCropYear());
		
	}	

	@Test 
	public void testSelectOtherFieldsForLegalLand() throws Exception {

		// Create test data
		Integer cropYear = 2022;

		createLegalLand(legalLandId, cropYear, "legal desc", "short legal desc", "other legal desc", "111-222-333");

		// Field 1
		createField(fieldId1, "LOT 1", cropYear, null);

		// Field 2
		createField(fieldId2, "LOT 2", cropYear, null);
		
		// Add Legal Land Field XREF
		createLegalLandFieldXref(fieldId1, legalLandId);
		createLegalLandFieldXref(fieldId2, legalLandId);

		
		FieldDao dao = persistenceSpringConfig.fieldDao();
		
		// Test: Get fieldId1 and exclude fieldId2
		List<FieldDto> dtos = dao.selectOtherFieldsForLegalLand(legalLandId, fieldId2, cropYear);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		Assert.assertEquals(fieldId1, dtos.get(0).getFieldId());
		Assert.assertEquals("LOT 1", dtos.get(0).getFieldLabel());
		
	}	
	
	@Test 
	public void testSelectForLegalLand() throws Exception {

		// Create test data
		Integer cropYear = 2022;

		createLegalLand(legalLandId, cropYear, "legal desc", "short legal desc", "other legal desc", "111-222-333");
		createLegalLand(legalLandId2, cropYear, "legal desc", "short legal desc", "other legal desc", "111-222-333");

		// Field 1
		createField(fieldId1, "LOT 1", cropYear, null);
		createField(fieldId2, "LOT 2", cropYear, null);

		// Add Legal Land Field XREF
		createLegalLandFieldXref(fieldId1, legalLandId);
		createLegalLandFieldXref(fieldId1, legalLandId2);
		createLegalLandFieldXref(fieldId2, legalLandId);

		
		FieldDao dao = persistenceSpringConfig.fieldDao();
		
		// Test: Get for legal land 1
		List<FieldDto> dtos = dao.selectForLegalLand(legalLandId);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());
		
		Assert.assertEquals(fieldId1, dtos.get(0).getFieldId());
		Assert.assertEquals("LOT 1", dtos.get(0).getFieldLabel());
		Assert.assertEquals(2, dtos.get(0).getTotalLegalLand().intValue());
		
		Assert.assertEquals(fieldId2, dtos.get(1).getFieldId());
		Assert.assertEquals("LOT 2", dtos.get(1).getFieldLabel());
		Assert.assertEquals(1, dtos.get(1).getTotalLegalLand().intValue());
		
		// Test: Get for legal land 2
		dtos = dao.selectForLegalLand(legalLandId2);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		Assert.assertEquals(fieldId1, dtos.get(0).getFieldId());
		Assert.assertEquals("LOT 1", dtos.get(0).getFieldLabel());
		Assert.assertEquals(2, dtos.get(0).getTotalLegalLand().intValue());
		
	}	
	
	private FieldDto createField(
			Integer fieldId, 
			String fieldLabel, 
			Integer cropYear,
			String location) throws DaoException {
		String userId = "JUNIT_TEST";

		// Field
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = new FieldDto();
		
		fieldDto.setFieldId(fieldId);
		fieldDto.setFieldLabel(fieldLabel);
		fieldDto.setActiveFromCropYear(1980);
		fieldDto.setActiveToCropYear(cropYear);
		fieldDto.setLocation(location);
		
		fieldDao.insertDataSync(fieldDto, userId);
		
		return fieldDto;
	}

	private void deleteField(Integer fieldId) throws DaoException{
		FieldDao dao = persistenceSpringConfig.fieldDao();
		FieldDto dto = dao.fetch(fieldId);
		if (dto != null) {
			dao.delete(fieldId);
		}
	}
	
	
	private LegalLandDto createLegalLand(
			Integer legalLandId, 
			Integer cropYear, 
			String legalDesc, 
			String shortLegalDesc, 
			String otherLegalDesc, 
			String primaryPid) throws DaoException {
		String userId = "JUNIT_TEST";

		// Legal Land
		LegalLandDao llDao = persistenceSpringConfig.legalLandDao();
		LegalLandDto llDto = new LegalLandDto();
		
		//INSERT
		llDto.setLegalLandId(legalLandId);
		llDto.setPrimaryPropertyIdentifier(primaryPid);
		llDto.setPrimaryReferenceTypeCode("OTHER");
		llDto.setLegalDescription(legalDesc);
		llDto.setLegalShortDescription(shortLegalDesc);
		llDto.setOtherDescription(otherLegalDesc);
		llDto.setActiveFromCropYear(1980);
		llDto.setActiveToCropYear(cropYear);
		llDto.setTotalAcres(12.3);
		llDto.setPrimaryLandIdentifierTypeCode("PID");


		llDao.insertDataSync(llDto, userId);
		
		return llDto;
	}

	private void deleteLegalLand(Integer legalLandId) throws DaoException {
		LegalLandDao dao = persistenceSpringConfig.legalLandDao();
		LegalLandDto dto = dao.fetch(legalLandId);
		if (dto != null) {
			dao.delete(legalLandId);
		}
	}
	
	private void createAnnualFieldDetail(
			Integer annualFieldDetailId,
			Integer fieldId, 
			Integer cropYear, 
			Integer legalLandId) throws DaoException {
		String userId = "JUNIT_TEST";

		// Annual Field Detail
		AnnualFieldDetailDao afdDao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto afdDto = new AnnualFieldDetailDto();

		afdDto.setAnnualFieldDetailId(annualFieldDetailId);
		afdDto.setLegalLandId(legalLandId);
		afdDto.setFieldId(fieldId);
		afdDto.setCropYear(cropYear);

		afdDao.insertDataSync(afdDto, userId);
	}

	private void deleteAnnualFieldDetail(Integer annualFieldDetailId) throws DaoException {
		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto dto = dao.fetch(annualFieldDetailId);
		if (dto != null) {
			dao.delete(annualFieldDetailId);			
		}		
	}
	
	private void createGrower(
			Integer growerId,
			Integer growerNumber,
			String growerName) throws DaoException {
		String userId = "JUNIT_TEST";

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transDate = cal.getTime();

		GrowerDao growerDao = persistenceSpringConfig.growerDao();
		GrowerDto growerDto = new GrowerDto();
		
		//INSERT
		growerDto.setGrowerId(growerId);
		growerDto.setGrowerNumber(growerNumber);
		growerDto.setGrowerName(growerName);
		growerDto.setGrowerAddressLine1("address line 1");
		growerDto.setGrowerAddressLine2("address line 2");
		growerDto.setGrowerPostalCode("V8P 4N8");
		growerDto.setGrowerCity("Victoria");
		growerDto.setCityId(1);
		growerDto.setGrowerProvince("BC");
		growerDto.setDataSyncTransDate(transDate);

		growerDao.insert(growerDto, userId);
	}

	private void deleteGrower(Integer growerId) throws DaoException {
		GrowerDao dao = persistenceSpringConfig.growerDao();
		GrowerDto dto = dao.fetch(growerId);
		if (dto != null) {
			dao.delete(growerId);
		}
	}
	
	private void createPolicy(
			Integer policyId,
			Integer growerId,
			Integer contractId,
			Integer cropYear,
			String policyNumber,
			String contractNumber) throws DaoException {
		String userId = "JUNIT_TEST";

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transDate = cal.getTime();		

		// Policy
		PolicyDao policyDao = persistenceSpringConfig.policyDao();
		
		PolicyDto policyDto = new PolicyDto();
		
		policyDto.setPolicyId(policyId);
		policyDto.setGrowerId(growerId);
		policyDto.setInsurancePlanId(4);
		policyDto.setPolicyStatusCode("ACTIVE");
		policyDto.setOfficeId(1);
		policyDto.setPolicyNumber(policyNumber);
		policyDto.setContractNumber(contractNumber);
		policyDto.setContractId(contractId);
		policyDto.setCropYear(cropYear);
		policyDto.setDataSyncTransDate(transDate);

		//INSERT
		policyDao.insert(policyDto, userId);
	}

	private void deletePolicy(Integer policyId) throws DaoException {
		PolicyDao dao = persistenceSpringConfig.policyDao();
		PolicyDto dto = dao.fetch(policyId);
		if (dto != null) {
			dao.delete(policyId);
		}
	}
	
	private void createGrowerContractYear(
			Integer growerContractYearId, 
			Integer contractId, 
			Integer growerId,
			Integer cropYear) throws DaoException {
		String userId = "JUNIT_TEST";

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transDate = cal.getTime();		

		GrowerContractYearDao gcyDao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto gcyDto = new GrowerContractYearDto();		

		//INSERT
		gcyDto.setGrowerContractYearId(growerContractYearId);
		gcyDto.setContractId(contractId);
		gcyDto.setGrowerId(growerId);
		gcyDto.setInsurancePlanId(4);
		gcyDto.setCropYear(cropYear);
		gcyDto.setDataSyncTransDate(transDate);
		
		gcyDao.insert(gcyDto, userId);
	}
	
	private void deleteGrowerContractYear(Integer growerContractYearId) throws DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto dto = dao.fetch(growerContractYearId);
		if (dto != null) {
			dao.delete(growerContractYearId);
		}
	}
	
	private void createContractedFieldDetail(
			Integer contractedFieldDetailId, 
			Integer annualFieldDetailId, 
			Integer growerContractYearId) throws DaoException {
		String userId = "JUNIT_TEST";

		ContractedFieldDetailDao cfdDao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto cfdDto = new ContractedFieldDetailDto();

		// INSERT
		cfdDto.setAnnualFieldDetailId(annualFieldDetailId);
		cfdDto.setContractedFieldDetailId(contractedFieldDetailId);
		cfdDto.setDisplayOrder(1);
		cfdDto.setIsLeasedInd(false);
		cfdDto.setGrowerContractYearId(growerContractYearId);

		cfdDao.insertDataSync(cfdDto, userId);
	}
	
	private void deleteContractedFieldDetail(Integer contractedFieldDetailId) throws DaoException {
		ContractedFieldDetailDao dao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto dto = dao.fetch(contractedFieldDetailId);		
		if (dto != null) {			
			dao.delete(contractedFieldDetailId);
		}		
	}
	

	private void createLegalLandFieldXref(Integer fieldId, Integer legalLandId) throws DaoException {
		String userId = "JUNIT_TEST";

		LegalLandFieldXrefDao dao = persistenceSpringConfig.legalLandFieldXrefDao();
		LegalLandFieldXrefDto newDto = new LegalLandFieldXrefDto();

		newDto.setLegalLandId(legalLandId);
		newDto.setFieldId(fieldId);

		dao.insert(newDto, userId);

	}

	private void deleteLegalLandFieldXref(Integer fieldId, Integer legalLandId) throws NotFoundDaoException, DaoException{
		
		// delete legal land - field xref table
		LegalLandFieldXrefDao dao = persistenceSpringConfig.legalLandFieldXrefDao();
		LegalLandFieldXrefDto dto = dao.fetch(legalLandId, fieldId);
		if (dto != null) {
			dao.delete(legalLandId, fieldId);
		}
		
	}

}
