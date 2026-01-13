package ca.bc.gov.mal.cirras.underwriting.data.repositories;

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

import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactEmailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactPhoneDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContactDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandFieldXrefDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class, PersistenceSpringConfig.class })
public class LegalLandDaoTest {

	@Autowired
	private PersistenceSpringConfig persistenceSpringConfig;

	private Integer legalLandId = 99999999;
	private Integer legalLandId2 = 888888888;
	private Integer legalLandId3 = 777777777;
	
	private Integer fieldId = 90000005;

	private Integer growerId = 90000001;
	private Integer growerContactId = 90000002;
	private Integer contactId = 90000003;
	private Integer contactPhoneId = 90000004;
	private Integer contactEmailId = 90000006;

	private Integer afdId = 90000007;
	private Integer cfdId = 90000008;
	private Integer gcyId = 90000009;
	private Integer contractId = 90000010;

	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException {
		delete();
	}

	@After
	public void cleanUp() throws NotFoundDaoException, DaoException {
		delete();
	}

	private void delete() throws NotFoundDaoException, DaoException {

		deleteContractedFieldDetail(cfdId);
		deleteGrowerContractYear(gcyId);
		deleteAnnualFieldDetail(afdId);
		deleteLegalLandFieldXrefAndField();
		deleteLegalLand();
		deleteGrowerContact(growerContactId);
		deleteContactPhone(contactPhoneId);
		deleteContactEmail(contactEmailId);
		deleteContact(contactId);
		deleteGrower(growerId);
	}
	
	private void deleteLegalLandFieldXrefAndField() throws NotFoundDaoException, DaoException{
		
		// delete legal land - field xref table
		LegalLandFieldXrefDao dao = persistenceSpringConfig.legalLandFieldXrefDao();
		LegalLandFieldXrefDto dto = dao.fetch(legalLandId, fieldId);
		if (dto != null) {
			dao.delete(legalLandId, fieldId);
		}
		
		dto = dao.fetch(legalLandId2, fieldId);
		if (dto != null) {
			dao.delete(legalLandId2, fieldId);
		}		
		
		dto = dao.fetch(legalLandId3, fieldId);
		if (dto != null) {
			dao.delete(legalLandId3, fieldId);
		}
		
		// delete field
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = fieldDao.fetch(fieldId);
		if (fieldDto != null) {
			fieldDao.delete(fieldId);
		}
		
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
		dto = dao.fetch(legalLandId3);
		if (dto != null) {
			dao.delete(legalLandId3);
		}
	}

	private void deleteGrower(Integer growerId) throws DaoException {
		GrowerDao dao = persistenceSpringConfig.growerDao();
		GrowerDto dto = dao.fetch(growerId);
		if (dto != null) {
			dao.delete(growerId);
		}
	}

	private void deleteContact(Integer contactId) throws NotFoundDaoException, DaoException {		
		ContactDao dao = persistenceSpringConfig.contactDao();
		ContactDto dto = dao.fetch(contactId);
		if (dto != null) {
			dao.delete(contactId);
		}
	}
	
	private void deleteGrowerContact(Integer growerContactId) throws NotFoundDaoException, DaoException {
		GrowerContactDao dao = persistenceSpringConfig.growerContactDao();
		GrowerContactDto dto = dao.fetch(growerContactId);
		if (dto != null) {
			dao.delete(growerContactId);
		}
	}

	private void deleteContactPhone(Integer contactPhoneId) throws NotFoundDaoException, DaoException {
		ContactPhoneDao dao = persistenceSpringConfig.contactPhoneDao();
		ContactPhoneDto dto = dao.fetch(contactPhoneId);
		if (dto != null) {
			dao.delete(contactPhoneId);
		}
	}
	
	private void deleteContactEmail(Integer contactEmailId) throws NotFoundDaoException, DaoException { 
		ContactEmailDao dao = persistenceSpringConfig.contactEmailDao();
		ContactEmailDto dto = dao.fetch(contactEmailId);
		if (dto != null) {
			dao.delete(contactEmailId);
		}
	}
	
	private void deleteAnnualFieldDetail(Integer annualFieldDetailId) throws DaoException {
		AnnualFieldDetailDao dao = persistenceSpringConfig.annualFieldDetailDao();
		AnnualFieldDetailDto dto = dao.fetch(annualFieldDetailId);
		if (dto != null) {
			dao.delete(annualFieldDetailId);			
		}		
	}

	private void deleteContractedFieldDetail(Integer contractedFieldDetailId) throws DaoException {
		ContractedFieldDetailDao dao = persistenceSpringConfig.contractedFieldDetailDao();
		ContractedFieldDetailDto dto = dao.fetch(contractedFieldDetailId);		
		if (dto != null) {			
			dao.delete(contractedFieldDetailId);
		}		
	}

	private void deleteGrowerContractYear(Integer growerContractYearId) throws DaoException {
		GrowerContractYearDao dao = persistenceSpringConfig.growerContractYearDao();
		GrowerContractYearDto dto = dao.fetch(growerContractYearId);
		if (dto != null) {
			dao.delete(growerContractYearId);
		}
	}
	
	
	@Test
	public void testInsertUpdateDeleteLegalLand() throws Exception {

		LegalLandDao dao = persistenceSpringConfig.legalLandDao();
		LegalLandDto newDto = new LegalLandDto();

		String primaryPropertyIdentifier = "111-222-333";
		String primaryReferenceTypeCode = "OTHER";
		String legalDescription = "Legal Description";
		String legalShortDescription = "Short Legal";
		String otherDescription = "Other Description";
		Integer activeFromCropYear = 2011;
		Integer activeToCropYear = 2022;
		Double totalAcres = 12.3;
		String primaryLandIdentifierTypeCode = "PID";

		String userId = "JUNIT_TEST";

		// INSERT
		newDto.setLegalLandId(null);
		newDto.setPrimaryPropertyIdentifier(primaryPropertyIdentifier);
		newDto.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		newDto.setLegalDescription(legalDescription);
		newDto.setLegalShortDescription(legalShortDescription);
		newDto.setOtherDescription(otherDescription);
		newDto.setTotalAcres(totalAcres);
		newDto.setPrimaryLandIdentifierTypeCode(primaryLandIdentifierTypeCode);

		newDto.setActiveFromCropYear(activeFromCropYear);
		newDto.setActiveToCropYear(activeToCropYear);
		dao.insert(newDto, userId);

		// FETCH
		LegalLandDto fetchedDto = dao.fetch(newDto.getLegalLandId());

		Assert.assertEquals("LegalLandId", newDto.getLegalLandId(), fetchedDto.getLegalLandId());
		Assert.assertEquals("PrimaryPropertyIdentifier", newDto.getPrimaryPropertyIdentifier(), fetchedDto.getPrimaryPropertyIdentifier());
		Assert.assertEquals("PrimaryReferenceTypeCode", newDto.getPrimaryReferenceTypeCode(), fetchedDto.getPrimaryReferenceTypeCode());
		Assert.assertEquals("LegalDescription", newDto.getLegalDescription(), fetchedDto.getLegalDescription());
		Assert.assertEquals("LegalShortDescription", newDto.getLegalShortDescription(), fetchedDto.getLegalShortDescription());
		Assert.assertEquals("OtherDescription", newDto.getOtherDescription(), fetchedDto.getOtherDescription());
		Assert.assertEquals("TotalAcres", newDto.getTotalAcres(), fetchedDto.getTotalAcres());
		Assert.assertEquals("PrimaryLandIdentifierTypeCode", newDto.getPrimaryLandIdentifierTypeCode(), fetchedDto.getPrimaryLandIdentifierTypeCode());
		Assert.assertEquals("ActiveFromCropYear", newDto.getActiveFromCropYear(), fetchedDto.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear", newDto.getActiveToCropYear(), fetchedDto.getActiveToCropYear());

		// UPDATE
		primaryPropertyIdentifier = "111-111-111";
		primaryReferenceTypeCode = "LEGAL";
		legalDescription = "Legal Description 2";
		legalShortDescription = "Short Legal 2";
		otherDescription = "Other Description 2";
		activeFromCropYear = 2000;
		activeToCropYear = 2012;
		totalAcres = 55.4;
		primaryLandIdentifierTypeCode = "PIN";


		fetchedDto.setPrimaryPropertyIdentifier(primaryPropertyIdentifier);
		fetchedDto.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		fetchedDto.setLegalDescription(legalDescription);
		fetchedDto.setLegalShortDescription(legalShortDescription);
		fetchedDto.setOtherDescription(otherDescription);
		fetchedDto.setActiveFromCropYear(activeFromCropYear);
		fetchedDto.setActiveToCropYear(activeToCropYear);
		fetchedDto.setTotalAcres(totalAcres);
		fetchedDto.setPrimaryLandIdentifierTypeCode(primaryLandIdentifierTypeCode);

		dao.update(fetchedDto, userId);

		// FETCH
		LegalLandDto updatedDto = dao.fetch(fetchedDto.getLegalLandId());
		Assert.assertEquals("PrimaryPropertyIdentifier", fetchedDto.getPrimaryPropertyIdentifier(), updatedDto.getPrimaryPropertyIdentifier());
		Assert.assertEquals("PrimaryReferenceTypeCode", fetchedDto.getPrimaryReferenceTypeCode(), updatedDto.getPrimaryReferenceTypeCode());
		Assert.assertEquals("LegalDescription", fetchedDto.getLegalDescription(), updatedDto.getLegalDescription());
		Assert.assertEquals("LegalShortDescription", fetchedDto.getLegalShortDescription(), updatedDto.getLegalShortDescription());
		Assert.assertEquals("OtherDescription", fetchedDto.getOtherDescription(), updatedDto.getOtherDescription());
		Assert.assertEquals("TotalAcres", fetchedDto.getTotalAcres(), updatedDto.getTotalAcres());
		Assert.assertEquals("PrimaryLandIdentifierTypeCode", fetchedDto.getPrimaryLandIdentifierTypeCode(), updatedDto.getPrimaryLandIdentifierTypeCode());
		Assert.assertEquals("ActiveFromCropYear", fetchedDto.getActiveFromCropYear(), updatedDto.getActiveFromCropYear());
		Assert.assertEquals("ActiveToCropYear", fetchedDto.getActiveToCropYear(), updatedDto.getActiveToCropYear());

		// DELETE
		dao.delete(updatedDto.getLegalLandId());

		// FETCH
		LegalLandDto deletedDto = dao.fetch(updatedDto.getLegalLandId());
		Assert.assertNull(deletedDto);

	}

	@Test
	public void testSelect() throws Exception {

		LegalLandDao dao = persistenceSpringConfig.legalLandDao();
		String legalLocation = "Test Legal 1234";
		String legalLocation2 = legalLocation + " test";
		String legalDescription = "Legal Description Dao Test";
		String legalDescription2 = "Legal Description Dao Test 2";
		String pidCommonPrefix = "999-888";
		String pid = pidCommonPrefix + "-777";
		String pid2 = pidCommonPrefix + "-999";

		createLegalLand(legalLocation, legalDescription, legalLandId, 2022, pid);
		createLegalLand(legalLocation2, legalDescription2, legalLandId2, 2022, pid2);

		// ----------------------------------------
		// TEST 1: Legal Location/Legal Description
		// ----------------------------------------
		
		// Use exact search for other description = return 1 record
		// Setting search word to lower case to make sure that search is case
		// insensitive.
		PagedDtos<LegalLandDto> pagedDtos = dao.select(legalLocation.toLowerCase(), null, null, null, false, true, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (exact, other description)", pagedDtos, legalLandId);
		
		// Use wildcard search for other description = return 2 records
		pagedDtos = dao.select(legalLocation.toLowerCase(), null, null, null, true, true, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (wildcard,other description)", pagedDtos, legalLandId, legalLandId2);

		// Use exact search for legal description = return 1 record
		pagedDtos = dao.select(legalDescription.toLowerCase(), null, null, null, false, true, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (exact, legal description)", pagedDtos, legalLandId);

		// Use wildcard search for legal description = return 2 records
		pagedDtos = dao.select(legalDescription.toLowerCase(), null, null, null, true, true, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (wildcard, legal description)", pagedDtos, legalLandId, legalLandId2);

		// Use exact search but exclude legal description = return 0 records
		pagedDtos = dao.select(legalDescription.toLowerCase(), null, null, null, false, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (exact, exclude legal description)", pagedDtos);

		// Use wildcard search but exclude legal description = return 0 records
		pagedDtos = dao.select(legalDescription.toLowerCase(), null, null, null, true, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (wildcard, exclude legal description)", pagedDtos);

		// ----------------------------------------
		// TEST 2: Primary Property Identifier
		// ----------------------------------------
		
		pagedDtos = dao.select(null, pid, null, null, false, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (exact, primary property identifier)", pagedDtos, legalLandId);
		
		pagedDtos = dao.select(null, pidCommonPrefix, null, null, false, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (wildcard, primary property identifier)", pagedDtos);
		
		pagedDtos = dao.select(null, pidCommonPrefix, null, null, true, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (wildcard, primary property identifier)", pagedDtos, legalLandId, legalLandId2);

		// ----------------------------------------
		// TEST 3: Grower Info
		// ----------------------------------------

		createGrower(growerId, 999888, "grower name");
		createContact(contactId);
		createGrowerContact(growerContactId);
		createContactPhone(contactPhoneId, "2501112222");
		createContactEmail(contactEmailId, "someEmail@vividsolutions.com");
		createField(fieldId, "LOT 1");
		createLegalLandFieldXref(fieldId, legalLandId);
		createAnnualFieldDetail(afdId, fieldId, 2022, legalLandId);
		createGrowerContractYear(gcyId, contractId, growerId, 2022);
		createContractedFieldDetail(cfdId, afdId, gcyId);

		// Exact Match:
		// ------------

		// Grower Name
		pagedDtos = dao.select(null, null, "grower name", null, false, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (exact, grower name)", pagedDtos, legalLandId);
		
		// Grower Number
		pagedDtos = dao.select(null, null, "999888", null, false, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (exact, grower number)", pagedDtos, legalLandId);
		
		// Phone
		pagedDtos = dao.select(null, null, "2501112222", null, false, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (exact, grower phone)", pagedDtos, legalLandId);
		
		// Email
		pagedDtos = dao.select(null, null, "someEmail@vividsolutions.com", null, false, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (exact, grower email)", pagedDtos, legalLandId);

		
		// Wrong Grower Name
		pagedDtos = dao.select(null, null, "grower na", null, false, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (exact, grower name)", pagedDtos);
		
		// Wrong Grower Number
		pagedDtos = dao.select(null, null, "99988", null, false, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (exact, grower number)", pagedDtos);
		
		// Wrong Phone
		pagedDtos = dao.select(null, null, "250111222", null, false, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (exact, grower phone)", pagedDtos);
		
		// Wrong Email
		pagedDtos = dao.select(null, null, "someEmail@vividsolutions", null, false, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (exact, grower email)", pagedDtos);

		
		// Wildcard
		// --------
		
		// Grower Name
		pagedDtos = dao.select(null, null, "grower na", null, true, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (wildcard, grower name)", pagedDtos, legalLandId);
		
		// Grower Number
		pagedDtos = dao.select(null, null, "99988", null, true, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (wildcard, grower number)", pagedDtos, legalLandId);
		
		// Phone
		pagedDtos = dao.select(null, null, "250111222", null, true, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (wildcard, grower phone)", pagedDtos, legalLandId);
		
		// Email
		pagedDtos = dao.select(null, null, "someEmail@vividsolutions", null, true, false, null, null, 800, null, null);
		checkLegalLandDtos("Returned Records (wildcard, grower email)", pagedDtos, legalLandId);
		
		// ----------------------------------------
		// TEST 4: Dataset Type: CLEANUP: Legal Land with only auto-generated PID.
		// ----------------------------------------
		
		pagedDtos = dao.select(null, null, null, "CLEANUP", false, false, null, null, 800, null, null);

		// There will be several existing records that match, so just verify that each has the expected PID format.
		Assert.assertNotNull(pagedDtos);
		List<LegalLandDto> dtos = pagedDtos.getResults();

		Assert.assertNotNull(dtos);
		Assert.assertTrue(dtos.size() > 0);
		
		for ( LegalLandDto dto : dtos ) {
			Assert.assertTrue(dto.getPrimaryPropertyIdentifier().matches("^GF\\d+$"));
			Assert.assertEquals("OTHER", dto.getPrimaryLandIdentifierTypeCode());
		}
	}
	
	private void checkLegalLandDtos(String errorMsg, PagedDtos<LegalLandDto> pagedDtos, Integer... expectedLegalLandIds ) {
		Assert.assertNotNull(pagedDtos);
		List<LegalLandDto> dtos = pagedDtos.getResults();

		Assert.assertNotNull(dtos);
		Assert.assertEquals(expectedLegalLandIds.length, dtos.size());
		
		for ( int i = 0; i < dtos.size(); i++ ) {
			Assert.assertEquals(errorMsg, expectedLegalLandIds[i], dtos.get(i).getLegalLandId());
		}
	}

	@Test
	public void testSearchOtherLegalLandForField() throws Exception {

		// Add legal land
		LegalLandDao dao = persistenceSpringConfig.legalLandDao();
		String legalLocation = "Test Legal 1234";
		String legalLocation2 = legalLocation + " test";
		String legalLocation3 = legalLocation + " test 3";
		String legalDescription = "Legal Description";
		String legalDescription2 = "Legal Description 2";
		String legalDescription3 = "Legal Description 3";
		Integer cropYear = 2022;

		createLegalLand(legalLocation, legalDescription, legalLandId, cropYear, "111-222-333");
		createLegalLand(legalLocation2, legalDescription2, legalLandId2, cropYear, "111-222-333");
		createLegalLand(legalLocation3, legalDescription3, legalLandId3, cropYear - 1, "111-222-333"); // Create an inactive legal land for cropYear

		// Add Field
		createField(fieldId, "LOT 1");

		// Add Legal Land Field XREF
		createLegalLandFieldXref(fieldId, legalLandId);
		createLegalLandFieldXref(fieldId, legalLandId2);
		createLegalLandFieldXref(fieldId, legalLandId3);
		
		//Returns all active legal land but legalLandId
		List<LegalLandDto> dtos = dao.searchOtherLegalLandForField(fieldId, legalLandId, cropYear);

		Assert.assertNotNull(dtos);
		Assert.assertEquals("Returned Records 1", 1, dtos.size());

		//Returns all active legal land but legalLandId
		//active cropYear -2
		dtos = dao.searchOtherLegalLandForField(fieldId, legalLandId, cropYear-2);

		Assert.assertNotNull(dtos);
		Assert.assertEquals("Returned Records 2", 2, dtos.size());
		
	}

	@Test
	public void testGetNextPidSequence() throws Exception {

		LegalLandDao dao = persistenceSpringConfig.legalLandDao();

		//Returns the next number of the PID sequence (ll_pid_seq)
		Integer pid = dao.getNextPidSequence();

		Assert.assertNotNull(pid);
		Assert.assertTrue("PID not greater min value", pid >= 1000);

		//Get the next number which needs to be 1 greater than the previous
		Integer pid2 = dao.getNextPidSequence();

		Assert.assertNotNull(pid2);
		Integer difference = pid2 - pid;
		Assert.assertTrue("PID not 1 greater than previous", difference == 1);

	}
	
	
	private void createLegalLandFieldXref(Integer fieldId, Integer legalLandId) throws DaoException {
		String userId = "JUNIT_TEST";

		LegalLandFieldXrefDao dao = persistenceSpringConfig.legalLandFieldXrefDao();
		LegalLandFieldXrefDto newDto = new LegalLandFieldXrefDto();

		newDto.setLegalLandId(legalLandId);
		newDto.setFieldId(fieldId);

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

	private void createField(Integer fieldId, String fieldLabel) throws DaoException {
		String userId = "JUNIT_TEST";

		// Field
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = new FieldDto();

		fieldDto.setFieldId(fieldId);
		fieldDto.setFieldLabel(fieldLabel);
		fieldDto.setActiveFromCropYear(1980);
		fieldDto.setActiveToCropYear(null);

		fieldDao.insertDataSync(fieldDto, userId);
	}

	private void createGrower(
		Integer growerId,
		Integer growerNumber,
		String growerName
	) throws DaoException {
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

	private void createContact(Integer contactId) throws DaoException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transDate = cal.getTime();
		
		ContactDao dao = persistenceSpringConfig.contactDao();
		ContactDto newDto = new ContactDto();
		
		String firstName = "Test Firstname";
		String lastName = "Test Lastname";

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setContactId(contactId);
		newDto.setFirstName(firstName);
		newDto.setLastName(lastName);
		newDto.setDataSyncTransDate(transDate);

		dao.insert(newDto, userId);
	}

	private void createGrowerContact(Integer growerContactId) throws DaoException { 
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transDate = cal.getTime();

		GrowerContactDao dao = persistenceSpringConfig.growerContactDao();
		GrowerContactDto newDto = new GrowerContactDto();
		
		Boolean isPrimaryContactInd = true;
		Boolean isActivelyInvolvedInd = true;

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setGrowerContactId(growerContactId);
		newDto.setGrowerId(growerId);
		newDto.setContactId(contactId);
		newDto.setIsPrimaryContactInd(isPrimaryContactInd);
		newDto.setIsActivelyInvolvedInd(isActivelyInvolvedInd);
		newDto.setDataSyncTransDate(transDate);

		dao.insert(newDto, userId);
	}
	
	private void createContactPhone(Integer contactPhoneId, String phoneNumber) throws DaoException { 

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);
		
		ContactPhoneDao dao = persistenceSpringConfig.contactPhoneDao();
		ContactPhoneDto newDto = new ContactPhoneDto();
		
		String extension = "123";
		Boolean isPrimaryPhoneInd = true;

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setContactPhoneId(contactPhoneId);
		newDto.setContactId(contactId);
		newDto.setPhoneNumber(phoneNumber);
		newDto.setExtension(extension);
		newDto.setIsPrimaryPhoneInd(isPrimaryPhoneInd);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dateTime);
		
		dao.insert(newDto, userId);
	}
	
	private void createContactEmail(Integer contactEmailId, String emailAddress) throws DaoException { 
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

		Date effectiveDate = addDays(date, -1);
		Date expiryDate = addDays(date, 1);
		
		ContactEmailDao dao = persistenceSpringConfig.contactEmailDao();
		ContactEmailDto newDto = new ContactEmailDto();
		
		Boolean isPrimaryEmailInd = true;

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setContactEmailId(contactEmailId);
		newDto.setContactId(contactId);
		newDto.setEmailAddress(emailAddress);
		newDto.setIsPrimaryEmailInd(isPrimaryEmailInd);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dateTime);
		
		dao.insert(newDto, userId);
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
	
	
	private static Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
	
}
