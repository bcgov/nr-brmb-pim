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

import ca.bc.gov.mal.cirras.underwriting.data.repositories.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactEmailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactPhoneDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContactDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class PolicyDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer policyId = 99999999;

	private Integer policyId1 = 90000001;
	private Integer contractId1 = 90000002;
	private String policyNumber1 = "998877-20";
	private String contractNumber1 = "998877";
	private Integer gcyId1 = 90000003;
	private Integer cropYear1 = 2020;
	
	
	private Integer policyId2 = 90000004;
	private Integer contractId2 = 90000005;
	private String policyNumber2 = "998899-20";
	private String contractNumber2 = "998899";
	private Integer gcyId2 = 90000006;
	private Integer cropYear2 = 2020;

	private Integer policyId3 = 90000017;
	private Integer contractId3 = 90000018;
	private String policyNumber3 = "998866-20";
	private String contractNumber3 = "998866";
	private Integer gcyId3 = 90000019;
	private Integer cropYear3 = 2020;

	private Integer policyId4 = 90000020;
	private Integer contractId4 = 90000021;
	private String policyNumber4 = "998855-20";
	private String contractNumber4 = "998855";
	private Integer gcyId4 = 90000022;
	private Integer cropYear4 = 2020;
	
	private Integer growerId1 = 90000007;
	private Integer growerId2 = 90000008;

	private Integer growerContactId1 = 90000009;
	private Integer contactId1 = 90000010;
	private Integer contactPhoneId1 = 90000011;
	private Integer contactEmailId1 = 90000012;

	private Integer fieldId = 90000013;
	private Integer afdId = 90000014;
	private Integer cfdId1 = 90000015;
	private Integer cfdId2 = 90000016;
	
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
		resetTestDefaults();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
		resetTestDefaults();
	}
	

	private void delete() throws NotFoundDaoException, DaoException {

		deleteVerifiedYieldContract(gcyId1);
		deleteVerifiedYieldContract(gcyId2);
		deleteVerifiedYieldContract(gcyId3);
		deleteVerifiedYieldContract(gcyId4);
		deleteDeclaredYieldContract(gcyId1);
		deleteDeclaredYieldContract(gcyId2);
		deleteDeclaredYieldContract(gcyId3);
		deleteDeclaredYieldContract(gcyId4);
		deleteInventoryContract(gcyId1);
		deleteInventoryContract(gcyId2);
		deleteInventoryContract(gcyId3);
		deleteInventoryContract(gcyId4);
		deleteContractedFieldDetail(cfdId1);
		deleteContractedFieldDetail(cfdId2);
		deleteGrowerContractYear(gcyId1);
		deleteGrowerContractYear(gcyId2);
		deleteGrowerContractYear(gcyId3);
		deleteGrowerContractYear(gcyId4);
		deleteAnnualFieldDetail(afdId);
		deleteField(fieldId);
		deleteGrowerContact(growerContactId1);
		deleteContactPhone(contactPhoneId1);
		deleteContactEmail(contactEmailId1);
		deleteContact(contactId1);
		deletePolicy(policyId);
		deletePolicy(policyId1);
		deletePolicy(policyId2);
		deletePolicy(policyId3);
		deletePolicy(policyId4);
		deleteGrower(growerId1);
		deleteGrower(growerId2);
	}

	// Some tests modify these members so they have different contract numbers or crop years 
	// than the defaults. This resets them afterward.
	private void resetTestDefaults() {
		contractId2 = 90000005;
		policyNumber2 = "998899-20";
		contractNumber2 = "998899";
		cropYear2 = 2020;

		contractId3 = 90000018;
		policyNumber3 = "998866-20";
		contractNumber3 = "998866";
		cropYear3 = 2020;

		contractId4 = 90000021;
		policyNumber4 = "998855-20";
		contractNumber4 = "998855";
		cropYear4 = 2020;		
	}
	
	private void deletePolicy(Integer policyId) throws NotFoundDaoException, DaoException{
		
		PolicyDao dao = persistenceSpringConfig.policyDao();
		PolicyDto dto = dao.fetch(policyId);
		if (dto != null) {
			dao.delete(policyId);
		}
	}
	
	private void deleteField(Integer fieldId) throws NotFoundDaoException, DaoException{
				
		FieldDao fieldDao = persistenceSpringConfig.fieldDao();
		FieldDto fieldDto = fieldDao.fetch(fieldId);
		if (fieldDto != null) {
			fieldDao.delete(fieldId);
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

	private void deleteInventoryContract(Integer gcyId) throws NotFoundDaoException, DaoException {
		InventoryContractDao dao = persistenceSpringConfig.inventoryContractDao();
		InventoryContractDto dto = dao.getByGrowerContract(gcyId);
		if ( dto != null ) {
			dao.delete(dto.getInventoryContractGuid());			
		}		
	}

	private void deleteDeclaredYieldContract(Integer gcyId) throws NotFoundDaoException, DaoException {
		DeclaredYieldContractDao dao = persistenceSpringConfig.declaredYieldContractDao();
		DeclaredYieldContractDto dto = dao.getByGrowerContract(gcyId);
		if ( dto != null ) {
			dao.delete(dto.getDeclaredYieldContractGuid());			
		}		
	}
	
	private void deleteVerifiedYieldContract(Integer gcyId) throws NotFoundDaoException, DaoException {
		VerifiedYieldContractDao dao = persistenceSpringConfig.verifiedYieldContractDao();
		VerifiedYieldContractDto dto = dao.getByGrowerContract(gcyId);
		if ( dto != null ) {
			dao.delete(dto.getVerifiedYieldContractGuid());			
		}		
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

	private void createGrowerContact(Integer growerContactId, Integer growerId, Integer contactId) throws DaoException { 
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
	
	private void createContactPhone(Integer contactPhoneId, String phoneNumber, Integer contactId) throws DaoException { 

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
	
	private void createContactEmail(Integer contactEmailId, String emailAddress, Integer contactId) throws DaoException { 
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
			Integer cropYear,
			Integer insurancePlanId) throws DaoException {
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
		gcyDto.setInsurancePlanId(insurancePlanId);
		gcyDto.setCropYear(cropYear);
		gcyDto.setDataSyncTransDate(transDate);
		
		gcyDao.insert(gcyDto, userId);
	}
	
	private void createPolicy(
			Integer policyId,
			Integer growerId,
			Integer contractId,
			Integer cropYear,
			String policyNumber,
			String contractNumber,
			Integer insurancePlanId,
			String policyStatus,
			Integer officeId) throws DaoException {
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
		policyDto.setInsurancePlanId(insurancePlanId);
		policyDto.setPolicyStatusCode(policyStatus);
		policyDto.setOfficeId(officeId);
		policyDto.setPolicyNumber(policyNumber);
		policyDto.setContractNumber(contractNumber);
		policyDto.setContractId(contractId);
		policyDto.setCropYear(cropYear);
		policyDto.setDataSyncTransDate(transDate);

		//INSERT
		policyDao.insert(policyDto, userId);
	}
	

	private String createInventoryContract(Integer contractId, Integer cropYear, String userId) throws DaoException {

		//Date without time
		Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		InventoryContractDao invContractDao = persistenceSpringConfig.inventoryContractDao();

		// Create parent InventoryContract.
		InventoryContractDto invContractDto = new InventoryContractDto();

		invContractDto.setContractId(contractId);
		invContractDto.setCropYear(cropYear);
		invContractDto.setFertilizerInd(false);
		invContractDto.setGrainFromPrevYearInd(false);
		invContractDto.setHerbicideInd(false);
		invContractDto.setOtherChangesComment(null);
		invContractDto.setOtherChangesInd(false);
		invContractDto.setSeededCropReportSubmittedInd(false);
		invContractDto.setTilliageInd(false);
		invContractDto.setUnseededIntentionsSubmittedInd(false);
		invContractDto.setInvUpdateTimestamp(date);
		invContractDto.setInvUpdateUser(userId);

		invContractDao.insert(invContractDto, userId);
		
		return invContractDto.getInventoryContractGuid();
	}

	private String createDeclaredYieldContract(Integer contractId, Integer cropYear, String userId) throws DaoException {

		// Create parent Declared Yield Contract.
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2020, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();
		
		DeclaredYieldContractDao dao = persistenceSpringConfig.declaredYieldContractDao();
		
		DeclaredYieldContractDto newDto = new DeclaredYieldContractDto();

		newDto.setContractId(contractId);
		newDto.setCropYear(cropYear);
		newDto.setDeclarationOfProductionDate(dopDate);
		newDto.setDefaultYieldMeasUnitTypeCode("TONNE");
		newDto.setEnteredYieldMeasUnitTypeCode("BUSHEL");
		newDto.setGrainFromOtherSourceInd(false);
		newDto.setBalerWagonInfo(null);
		newDto.setTotalLivestock(null);
		
		//INSERT
		dao.insert(newDto, userId);
		return newDto.getDeclaredYieldContractGuid();
		
	}
	
	private String createVerifiedYieldContract(Integer contractId, Integer cropYear, String declaredYieldContractGuid, String userId) throws DaoException {

		VerifiedYieldContractDao dao = persistenceSpringConfig.verifiedYieldContractDao();
		
		VerifiedYieldContractDto newDto = new VerifiedYieldContractDto();

		newDto.setContractId(contractId);
		newDto.setCropYear(cropYear);
		newDto.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		newDto.setDefaultYieldMeasUnitTypeCode("TONNE");
		newDto.setInsurancePlanId(4);		
		
		//INSERT
		dao.insert(newDto, userId);
		return newDto.getVerifiedYieldContractGuid();
	}

	
	@Test 
	public void testInsertUpdateDeletePolicy() throws Exception {

		//Get any existing calculation and add a plant unit record
		Integer growerId = 8;
		Integer insurancePlanId = 5;
		String policyStatusCode = "ACTIVE";
		Integer officeId = 1;
		String policyNumber = "123123-21";
		String contractNumber = "123123";
		Integer contractId = 99998888;
		Integer cropYear = 2021;
		String userId = "UNITTEST";

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		Date dataSyncTransDate = addSeconds(dateTime, -120);
		

		PolicyDao dao = persistenceSpringConfig.policyDao();
		
		PolicyDto newDto = new PolicyDto();
		
		newDto.setPolicyId(policyId);
		newDto.setGrowerId(growerId);
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setPolicyStatusCode(policyStatusCode);
		newDto.setOfficeId(officeId);
		newDto.setPolicyNumber(policyNumber);
		newDto.setContractNumber(contractNumber);
		newDto.setContractId(contractId);
		newDto.setCropYear(cropYear);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		//INSERT
		dao.insert(newDto, userId);
		
		//FETCH
		PolicyDto fetchedDto = dao.fetch(policyId);
		
		Assert.assertEquals("PolicyId", newDto.getPolicyId(), fetchedDto.getPolicyId());
		Assert.assertEquals("GrowerId", newDto.getGrowerId(), fetchedDto.getGrowerId());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("PolicyStatusCode", newDto.getPolicyStatusCode(), fetchedDto.getPolicyStatusCode());
		Assert.assertEquals("OfficeId", newDto.getOfficeId(), fetchedDto.getOfficeId());
		Assert.assertEquals("PolicyNumber", newDto.getPolicyNumber(), fetchedDto.getPolicyNumber());
		Assert.assertEquals("ContractNumber", newDto.getContractNumber(), fetchedDto.getContractNumber());
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("DataSyncTransDate 1", newDto.getDataSyncTransDate(), fetchedDto.getDataSyncTransDate());
		Assert.assertNull("InventoryContractGuid", fetchedDto.getInventoryContractGuid());
		Assert.assertNull("DeclaredYieldContractGuid", fetchedDto.getDeclaredYieldContractGuid());
		Assert.assertNull("VerifiedYieldContractGuid", fetchedDto.getVerifiedYieldContractGuid());
		
		//UPDATE
		dataSyncTransDate = addSeconds(dateTime, -60);

		fetchedDto.setGrowerId(12);
		fetchedDto.setInsurancePlanId(4);
		fetchedDto.setPolicyStatusCode("UNDERWRITING");
		fetchedDto.setOfficeId(2);
		fetchedDto.setPolicyNumber("321321-22");
		fetchedDto.setContractNumber("321321");
		fetchedDto.setContractId(11112222);
		fetchedDto.setCropYear(2022);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);		
		
		dao.update(fetchedDto, userId);

		//FETCH
		PolicyDto updatedDto = dao.fetch(policyId);
		
		Assert.assertEquals("GrowerId", fetchedDto.getGrowerId(), updatedDto.getGrowerId());
		Assert.assertEquals("InsurancePlanId", fetchedDto.getInsurancePlanId(), updatedDto.getInsurancePlanId());
		Assert.assertEquals("PolicyStatusCode", fetchedDto.getPolicyStatusCode(), updatedDto.getPolicyStatusCode());
		Assert.assertEquals("OfficeId", fetchedDto.getOfficeId(), updatedDto.getOfficeId());
		Assert.assertEquals("PolicyNumber", fetchedDto.getPolicyNumber(), updatedDto.getPolicyNumber());
		Assert.assertEquals("ContractNumber", fetchedDto.getContractNumber(), updatedDto.getContractNumber());
		Assert.assertEquals("ContractId", fetchedDto.getContractId(), updatedDto.getContractId());
		Assert.assertEquals("CropYear", fetchedDto.getCropYear(), updatedDto.getCropYear());
		Assert.assertEquals("DataSyncTransDate 2", fetchedDto.getDataSyncTransDate(), updatedDto.getDataSyncTransDate());
		Assert.assertNull("InventoryContractGuid", updatedDto.getInventoryContractGuid());
		Assert.assertNull("DeclaredYieldContractGuid", updatedDto.getDeclaredYieldContractGuid());
		Assert.assertNull("VerifiedYieldContractGuid", updatedDto.getVerifiedYieldContractGuid());

		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		PolicyDto notUpdatedDto = dao.fetch(policyId);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate 3", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		//DELETE
		dao.delete(policyId);
		
		//FETCH
		PolicyDto deletedDto = dao.fetch(policyId);
		Assert.assertNull(deletedDto);
	}

	
	@Test 
	public void testFetchExtendedFields() throws Exception {

		Integer policyId = 1063983;
		Integer growerId = 6022;
		Integer insurancePlanId = 4;
		String policyStatusCode = "ACTIVE";
		Integer officeId = 3;
		String policyNumber = "140160-21";
		String contractNumber = "140160";
		Integer contractId = 2532;
		Integer cropYear = 2021;
		
		String insurancePlanName = "GRAIN";
		String policyStatus = "ACTIVE";
		Integer growerNumber = 140160;
		String growerName = "BYZLV, NZXMF S";
		String growerPrimaryEmail = "noreply@vividsolutions.com";
		String growerPrimaryPhone = "1235556789";
		String inventoryContractGuid = "IC111157";
		
		PolicyDao dao = persistenceSpringConfig.policyDao();
				
		//FETCH
		PolicyDto fetchedDto = dao.fetch(policyId);

		Assert.assertEquals("PolicyId", policyId, fetchedDto.getPolicyId());
		Assert.assertEquals("GrowerId", growerId, fetchedDto.getGrowerId());
		Assert.assertEquals("InsurancePlanId", insurancePlanId, fetchedDto.getInsurancePlanId());
		Assert.assertEquals("PolicyStatusCode", policyStatusCode, fetchedDto.getPolicyStatusCode());
		Assert.assertEquals("OfficeId", officeId, fetchedDto.getOfficeId());
		Assert.assertEquals("PolicyNumber", policyNumber, fetchedDto.getPolicyNumber());
		Assert.assertEquals("ContractNumber", contractNumber, fetchedDto.getContractNumber());
		Assert.assertEquals("ContractId", contractId, fetchedDto.getContractId());
		Assert.assertEquals("CropYear", cropYear, fetchedDto.getCropYear());

		Assert.assertEquals("InsurancePlanName", insurancePlanName, fetchedDto.getInsurancePlanName());
		Assert.assertEquals("PolicyStatus", policyStatus, fetchedDto.getPolicyStatus());
		Assert.assertEquals("GrowerNumber", growerNumber, fetchedDto.getGrowerNumber());
		Assert.assertEquals("GrowerName", growerName, fetchedDto.getGrowerName());
		Assert.assertEquals("GrowerPrimaryEmail", growerPrimaryEmail, fetchedDto.getGrowerPrimaryEmail());
		Assert.assertEquals("GrowerPrimaryPhone", growerPrimaryPhone, fetchedDto.getGrowerPrimaryPhone());
		Assert.assertEquals("InventoryContractGuid", inventoryContractGuid, fetchedDto.getInventoryContractGuid());
		
	}	
	
	private Integer maxRows = 1000;
	private Integer pageNumber = 1;
	private Integer pageRowCount = 200;
	
	@Test 
	public void testFetchEligibleInventoryGrain() throws Exception {

		//Get policy Ids: SELECT  p.policy_id from policy p where p.policy_number = ''
		Integer policyIdWithInv = 1069665; 	//grain policy with eligible inventory
		Integer policyIdWithoutInv = 1069666;	//grain policy without eligible inventory
		
		testEligibleInventory(policyIdWithInv, policyIdWithoutInv);
	}
	
	@Test 
	public void testFetchEligibleInventoryForage() throws Exception {

		//Get policy Ids: SELECT  p.policy_id from policy p where p.policy_number = ''
		Integer policyIdWithInv = 1068969; 	//policy with eligible inventory
		Integer policyIdWithoutInv = 1068994;	//policy without eligible inventory
		
		testEligibleInventory(policyIdWithInv, policyIdWithoutInv);
	}
	
	@Test 
	public void testEligibleInventoryBerries() throws Exception {

		//Get policy Ids: SELECT  p.policy_id from policy p where p.policy_number = ''
		Integer policyIdWithInv = 1072240; 	//policy with eligible inventory
		Integer policyIdWithoutInv = 1066901;	//policy without eligible inventory
		
		testEligibleInventory(policyIdWithInv, policyIdWithoutInv);
}

	private void testEligibleInventory(Integer policyIdWithInv, Integer policyIdWithoutInv)
			throws DaoException, TooManyRecordsException {
		PolicyDao dao = persistenceSpringConfig.policyDao();

		//FETCH With
		PolicyDto fetchedDto = dao.fetch(policyIdWithInv);

		Assert.assertEquals("PolicyId", policyIdWithInv, fetchedDto.getPolicyId());
		Assert.assertTrue("Eligible Inventory", fetchedDto.getTotalDopEligibleInventory().intValue() > 0);
		Assert.assertNotNull(fetchedDto.getPolicyNumber());
		
		// Test: search
		PagedDtos<PolicyDto> dtos = dao.select(null, null, null, null, fetchedDto.getPolicyNumber(), null, null, null, null, maxRows, pageNumber, pageRowCount);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.getResults().size());
		fetchedDto = dtos.getResults().get(0);
		Assert.assertTrue("Eligible Inventory", fetchedDto.getTotalDopEligibleInventory().intValue() > 0);
		
		//FETCH Without
		fetchedDto = dao.fetch(policyIdWithoutInv);

		Assert.assertEquals("PolicyId", policyIdWithoutInv, fetchedDto.getPolicyId());
		Assert.assertEquals("Eligible Inventory", 0, fetchedDto.getTotalDopEligibleInventory().intValue());

		// Test: search
		dtos = dao.select(null, null, null, null, fetchedDto.getPolicyNumber(), null, null, null, null, maxRows, pageNumber, pageRowCount);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.getResults().size());
		fetchedDto = dtos.getResults().get(0);
		Assert.assertEquals("Eligible Inventory", 0, fetchedDto.getTotalDopEligibleInventory().intValue());
	}	
	
	@Test 
	public void testFetchEligibleInventoryUnsupportedPlan() throws Exception {

		Integer unsupportedInsurancePlan = 1068244;	//Unsupported plan
		
		PolicyDao dao = persistenceSpringConfig.policyDao();

		//FETCH Plan not supported
		PolicyDto fetchedDto = dao.fetch(unsupportedInsurancePlan);

		Assert.assertEquals("PolicyId", unsupportedInsurancePlan, fetchedDto.getPolicyId());
		Assert.assertEquals("Eligible Inventory", 0, fetchedDto.getTotalDopEligibleInventory().intValue());
		
	}

	@Test 
	public void testSelectPolicies() throws Exception {	
		
		PolicyDao dao = persistenceSpringConfig.policyDao();

		Integer maxRows = new Integer(1000);
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(200);

		createGrower(growerId1, 999888, "grower name");
		createPolicy(policyId1, growerId1, contractId1, 2020, policyNumber1, contractNumber1, 4, "ACTIVE", 1);

		// Test: crop year, insurance plan, office, policy status
		PagedDtos<PolicyDto> dtos = dao.select(2020, 4, 1, "ACTIVE", null, null, null, null, null, maxRows, pageNumber, pageRowCount);
		Assert.assertNotNull(dtos);
		List<PolicyDto> pDtos = dtos.getResults();

		Assert.assertNotNull(pDtos);
		Assert.assertFalse(pDtos.isEmpty());

		// This will return many results.
		for ( PolicyDto pDto : pDtos ) {
			Assert.assertEquals(2020, pDto.getCropYear().intValue());
			Assert.assertEquals(4, pDto.getInsurancePlanId().intValue());
			Assert.assertEquals(1, pDto.getOfficeId().intValue());
			Assert.assertEquals("ACTIVE", pDto.getPolicyStatusCode());
		}
				
		// Test: policy number
		// A. Exact match.
		dtos = dao.select(null, null, null, null, policyNumber1, null, null, null, null, maxRows, pageNumber, pageRowCount);
		checkPolicyDtos(dtos, policyId1);

		// B. Partial match.
		dtos = dao.select(null, null, null, null, contractNumber1, null, null, null, null, maxRows, pageNumber, pageRowCount);
		checkPolicyDtos(dtos, policyId1);
		
		
		// Test: grower info
		createContact(contactId1);
		createGrowerContact(growerContactId1, growerId1, contactId1);
		createContactPhone(contactPhoneId1, "2501112222", contactId1);
		createContactEmail(contactEmailId1, "someEmail@vividsolutions.com", contactId1);


		// Grower Name
		dtos = dao.select(null, null, null, null, null, "grower na", null, null, null, maxRows, pageNumber, pageRowCount);
		checkPolicyDtos(dtos, policyId1);
		
		// Grower Number
		dtos = dao.select(null, null, null, null, null, "99988", null, null, null, maxRows, pageNumber, pageRowCount);
		checkPolicyDtos(dtos, policyId1);

		// Phone
		dtos = dao.select(null, null, null, null, null, "250111222", null, null, null, maxRows, pageNumber, pageRowCount);
		checkPolicyDtos(dtos, policyId1);

		// Email
		dtos = dao.select(null, null, null, null, null, "someEmail@vividsolutions", null, null, null, maxRows, pageNumber, pageRowCount);
		checkPolicyDtos(dtos, policyId1);
		

		deleteGrowerContact(growerContactId1);
		deleteContactPhone(contactPhoneId1);
		deleteContactEmail(contactEmailId1);
		deleteContact(contactId1);
		
		// Test: dataset type
		// A. Policy with same grower.
		createPolicy(policyId2, growerId1, contractId2, 2020, policyNumber2, contractNumber2, 5, "ACTIVE", 1);

		dtos = dao.select(null, null, null, null, policyNumber1, null, "LINKED_GF_POLICIES", null, null, maxRows, pageNumber, pageRowCount);
		checkPolicyDtos(dtos, policyId2);
		
		deletePolicy(policyId2);		
		
		// B. Policy with different grower but shared field.
		createGrower(growerId2, 999777, "grower name 2");
		createPolicy(policyId2, growerId2, contractId2, 2020, policyNumber2, contractNumber2, 5, "ACTIVE", 1);
		createField(fieldId, "LOT 1");
		createAnnualFieldDetail(afdId, fieldId, 2020, null);
		createGrowerContractYear(gcyId1, contractId1, growerId1, 2020, 4);
		createContractedFieldDetail(cfdId1, afdId, gcyId1);
		createGrowerContractYear(gcyId2, contractId2, growerId2, 2020, 5);
		createContractedFieldDetail(cfdId2, afdId, gcyId2);

		dtos = dao.select(null, null, null, null, policyNumber1, null, "LINKED_GF_POLICIES", null, null, maxRows, pageNumber, pageRowCount);
		checkPolicyDtos(dtos, policyId2);
				
		deleteContractedFieldDetail(cfdId1);
		deleteContractedFieldDetail(cfdId2);
		deleteGrowerContractYear(gcyId1);
		deleteGrowerContractYear(gcyId2);
		deleteAnnualFieldDetail(afdId);
		deleteField(fieldId);
		deletePolicy(policyId2);
		deleteGrower(growerId2);

		
		// C. Policy with same grower but different year.
		policyNumber2 = contractNumber2 + "-19";
		createPolicy(policyId2, growerId1, contractId2, 2019, policyNumber2, contractNumber2, 5, "ACTIVE", 1);

		dtos = dao.select(null, null, null, null, policyNumber1, null, "LINKED_GF_POLICIES", null, null, maxRows, pageNumber, pageRowCount);
		checkPolicyDtos(dtos);
		
		deletePolicy(policyId2);
		
		
		deletePolicy(policyId1);
		deleteGrower(growerId1);

	}

	private void checkPolicyDtos(PagedDtos<PolicyDto> pagedDtos, Integer... expectedPolicyIds ) {
		Assert.assertNotNull(pagedDtos);
		List<PolicyDto> dtos = pagedDtos.getResults();

		Assert.assertNotNull(dtos);
		Assert.assertEquals(expectedPolicyIds.length, dtos.size());
		
		for ( int i = 0; i < dtos.size(); i++ ) {
			Assert.assertEquals(expectedPolicyIds[i], dtos.get(i).getPolicyId());
		}
	}
	
	
	
	private int previousIntValue = 0;
	private int currentIntValue = 0;
	private String previousStringValue = "";
	private String currentStringValue = "";
	private boolean orderMatches = true;
	private boolean testSuccessful = true;
	private final String sortOrderAscending = "ASC";
	private final String sortOrderDescending = "DESC";
	private String sortDirection = "";

	@Test 
	public void testSelectPoliciesSortingByPolicyNumber() throws Exception {	
		
		PolicyDao dao = persistenceSpringConfig.policyDao();
		
		String sortColumn = null;
		sortColumn = "policyNumber";	//String
		
		sortDirection = sortOrderAscending;

		Integer maxRows = new Integer(1000);
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(50);
		Integer cropYear = 2021;
		Integer insurancePlanId = 4;
		
		
		PagedDtos<PolicyDto> dtos = dao.select(cropYear, insurancePlanId, null, null, null, null, null, sortColumn, sortDirection, maxRows, pageNumber, pageRowCount);

		Assert.assertNotNull("Policynumber ASC", dtos);
		
		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		testSuccessful = true;
		
		
		dtos.getResults().forEach((temp) -> {
			
			currentStringValue = temp.getPolicyNumber();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("Policynumber ASC", testSuccessful);
		
		
		sortDirection = sortOrderDescending;
		
		dtos = dao.select(cropYear, insurancePlanId, null, null, null, null, null, sortColumn, sortDirection, maxRows, pageNumber, pageRowCount);
		
		Assert.assertNotNull("Policynumber DESC", dtos);

		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		
		dtos.getResults().forEach((temp) -> {
			
			currentStringValue = temp.getPolicyNumber();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("Policynumber DESC", testSuccessful);
	}
	
	@Test 
	public void testSelectPoliciesSortingByPolicyStatus() throws Exception {	
		
		PolicyDao dao = persistenceSpringConfig.policyDao();
		
		String sortColumn = null;
		sortColumn = "policyStatus";	//String
		
		sortDirection = sortOrderAscending;

		Integer maxRows = new Integer(1000);
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(50);
		Integer cropYear = 2021;
		Integer insurancePlanId = 4;
		
		
		PagedDtos<PolicyDto> dtos = dao.select(cropYear, insurancePlanId, null, null, null, null, null, sortColumn, sortDirection, maxRows, pageNumber, pageRowCount);

		Assert.assertNotNull("policyStatus ASC", dtos);
		
		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		testSuccessful = true;
		
		
		dtos.getResults().forEach((temp) -> {
			
			currentStringValue = temp.getPolicyStatus();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("policyStatus ASC", testSuccessful);
		
		
		sortDirection = sortOrderDescending;
		
		dtos = dao.select(cropYear, insurancePlanId, null, null, null, null, null, sortColumn, sortDirection, maxRows, pageNumber, pageRowCount);
		
		Assert.assertNotNull("policyStatus DESC", dtos);

		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		
		dtos.getResults().forEach((temp) -> {
			
			currentStringValue = temp.getPolicyStatus();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("policyStatus DESC", testSuccessful);
	}
	
	@Test 
	public void testSelectPoliciesSortingByInsurancePlan() throws Exception {	
		
		PolicyDao dao = persistenceSpringConfig.policyDao();
		
		String sortColumn = null;
		sortColumn = "insurancePlanName";	//String
		
		sortDirection = sortOrderAscending;

		Integer maxRows = new Integer(1000);
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(250);
		Integer cropYear = 2021;
		Integer insurancePlanId = null;
		
		
		PagedDtos<PolicyDto> dtos = dao.select(cropYear, insurancePlanId, null, null, null, null, null, sortColumn, sortDirection, maxRows, pageNumber, pageRowCount);

		Assert.assertNotNull("insurancePlan ASC", dtos);
	
		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		testSuccessful = true;
		
		
		dtos.getResults().forEach((temp) -> {
			
			currentStringValue = temp.getInsurancePlanName();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("insurancePlan ASC", testSuccessful);
		
		
		sortDirection = sortOrderDescending;
		
		dtos = dao.select(cropYear, insurancePlanId, null, null, null, null, null, sortColumn, sortDirection, maxRows, pageNumber, pageRowCount);
		
		Assert.assertNotNull("insurancePlan DESC", dtos);

		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		
		dtos.getResults().forEach((temp) -> {
			
			currentStringValue = temp.getInsurancePlanName();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("insurancePlan DESC", testSuccessful);
	}	

	@Test 
	public void testSelectPoliciesSortingByGrowerName() throws Exception {	
		
		PolicyDao dao = persistenceSpringConfig.policyDao();
		
		String sortColumn = null;
		sortColumn = "growerName";	//String
		
		sortDirection = sortOrderAscending;

		Integer maxRows = new Integer(1000);
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(50);
		Integer cropYear = 2021;
		Integer insurancePlanId = 4;
		
		
		PagedDtos<PolicyDto> dtos = dao.select(cropYear, insurancePlanId, null, null, null, null, null, sortColumn, sortDirection, maxRows, pageNumber, pageRowCount);

		Assert.assertNotNull("growerName ASC", dtos);

		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		testSuccessful = true;
		
		
		dtos.getResults().forEach((temp) -> {
			
			currentStringValue = temp.getGrowerName();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("growerName ASC", testSuccessful);
		
		
		sortDirection = sortOrderDescending;
		
		dtos = dao.select(cropYear, insurancePlanId, null, null, null, null, null, sortColumn, sortDirection, maxRows, pageNumber, pageRowCount);
		
		Assert.assertNotNull("growerName DESC", dtos);

		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		
		dtos.getResults().forEach((temp) -> {
			
			currentStringValue = temp.getGrowerName();
			
			compareStrings(sortDirection);
        });	  
		
		Assert.assertTrue("growerName DESC", testSuccessful);
	}
	
	@Test 
	public void testSelectPoliciesSortingByGrowerNumber() throws Exception {	
		
		PolicyDao dao = persistenceSpringConfig.policyDao();
		
		String sortColumn = null;
		sortColumn = "growerNumber";	//String
		
		sortDirection = sortOrderAscending;

		Integer maxRows = new Integer(1000);
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(50);
		Integer cropYear = 2021;
		Integer insurancePlanId = 4;
		
		
		PagedDtos<PolicyDto> dtos = dao.select(cropYear, insurancePlanId, null, null, null, null, null, sortColumn, sortDirection, maxRows, pageNumber, pageRowCount);

		Assert.assertNotNull("growerNumber ASC", dtos);
	
		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		testSuccessful = true;
		
		
		dtos.getResults().forEach((temp) -> {
			
			currentIntValue = temp.getGrowerNumber();
			
			compareInt(sortDirection);
        });	  
		
		orderMatches = true;
		previousIntValue = 0;
		testSuccessful = true;
		
		
		Assert.assertTrue("growerNumber ASC", testSuccessful);
		
		
		sortDirection = sortOrderDescending;
		
		dtos = dao.select(cropYear, insurancePlanId, null, null, null, null, null, sortColumn, sortDirection, maxRows, pageNumber, pageRowCount);
		
		Assert.assertNotNull("growerNumber DESC", dtos);

		orderMatches = true;
		previousStringValue = "";
		currentStringValue = "";
		
		dtos.getResults().forEach((temp) -> {
			
			currentIntValue = temp.getGrowerNumber();
			
			compareInt(sortDirection);
        });	  
		
		Assert.assertTrue("growerNumber DESC", testSuccessful);
	}
	
	@Test 
	public void testRemoveNonNumericCharacters() throws Exception {

		String rawString = "+1 (250). 333-4567";
		String cleanString = rawString.replaceAll("[^\\d]", "");
		
		Assert.assertEquals("12503334567", cleanString);
	}
	
	@Test 
	public void testSelectByFieldAndYear() throws Exception {

		PolicyDao dao = persistenceSpringConfig.policyDao();

		Integer fieldId = 1376;
		Integer cropYear = 2022;
		
		List<PolicyDto> dtos = dao.selectByFieldAndYear(fieldId, cropYear);
		
		//Expects 2 policies associated with the land
		Assert.assertNotNull(dtos);
		Assert.assertEquals("dtos size", 2, dtos.size());

	}	
		

	@Test 
	public void testSelectByOtherYearInventory() throws Exception {	
		
		String userId = "JUNIT_TEST";

		PolicyDao dao = persistenceSpringConfig.policyDao();

		createGrower(growerId1, 999888, "grower name");
		createPolicy(policyId1, growerId1, contractId1, cropYear1, policyNumber1, contractNumber1, 4, "ACTIVE", 1);
		createGrowerContractYear(gcyId1, contractId1, growerId1, cropYear1, 4);
		String invContractGuid1 = createInventoryContract(contractId1, cropYear1, userId);

		// Test: No other years.
		List<PolicyDto> dtos = dao.selectByOtherYearInventory(contractId1, cropYear1, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		// Test: Other prev years, but no inventory.
		cropYear2 = 2019;
		contractId2 = contractId1;
		contractNumber2 = contractNumber1;
		policyNumber2 = contractNumber2 + "-19";

		cropYear3 = 2018;
		contractId3 = contractId1;
		contractNumber3 = contractNumber1;
		policyNumber3 = contractNumber3 + "-18";

		cropYear4 = 2017;
		contractId4 = contractId1;
		contractNumber4 = contractNumber1;
		policyNumber4 = contractNumber4 + "-17";		
		
		createPolicy(policyId2, growerId1, contractId2, cropYear2, policyNumber2, contractNumber2, 4, "ACTIVE", 1);
		createPolicy(policyId3, growerId1, contractId3, cropYear3, policyNumber3, contractNumber3, 4, "ACTIVE", 1);
		createPolicy(policyId4, growerId1, contractId4, cropYear4, policyNumber4, contractNumber4, 4, "ACTIVE", 1);

		createGrowerContractYear(gcyId2, contractId2, growerId1, cropYear2, 4);
		createGrowerContractYear(gcyId3, contractId3, growerId1, cropYear3, 4);
		createGrowerContractYear(gcyId4, contractId4, growerId1, cropYear4, 4);
		
		dtos = dao.selectByOtherYearInventory(contractId1, cropYear1, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		// Test: One year with inventory.
		String invContractGuid2 = createInventoryContract(contractId2, cropYear2, userId);

		dtos = dao.selectByOtherYearInventory(contractId1, cropYear1, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		PolicyDto dto = dtos.get(0);
		Assert.assertEquals(policyId2, dto.getPolicyId());
		Assert.assertEquals(policyNumber2, dto.getPolicyNumber());
		Assert.assertEquals(contractId2, dto.getContractId());
		Assert.assertEquals(cropYear2, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(invContractGuid2, dto.getInventoryContractGuid());
		

		// Test: All prev years with inventory.
		String invContractGuid3 = createInventoryContract(contractId3, cropYear3, userId);
		String invContractGuid4 = createInventoryContract(contractId4, cropYear4, userId);

		dtos = dao.selectByOtherYearInventory(contractId1, cropYear1, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		dto = dtos.get(0);
		Assert.assertEquals(policyId3, dto.getPolicyId());
		Assert.assertEquals(policyNumber3, dto.getPolicyNumber());
		Assert.assertEquals(contractId3, dto.getContractId());
		Assert.assertEquals(cropYear3, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(invContractGuid3, dto.getInventoryContractGuid());
		
		dto = dtos.get(1);
		Assert.assertEquals(policyId2, dto.getPolicyId());
		Assert.assertEquals(policyNumber2, dto.getPolicyNumber());
		Assert.assertEquals(contractId2, dto.getContractId());
		Assert.assertEquals(cropYear2, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(invContractGuid2, dto.getInventoryContractGuid());

		// Test: prev and future years with inventory.
		dtos = dao.selectByOtherYearInventory(contractId2, cropYear2, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(3, dtos.size());

		dto = dtos.get(0);
		Assert.assertEquals(policyId4, dto.getPolicyId());
		Assert.assertEquals(policyNumber4, dto.getPolicyNumber());
		Assert.assertEquals(contractId4, dto.getContractId());
		Assert.assertEquals(cropYear4, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(invContractGuid4, dto.getInventoryContractGuid());
		
		dto = dtos.get(1);
		Assert.assertEquals(policyId3, dto.getPolicyId());
		Assert.assertEquals(policyNumber3, dto.getPolicyNumber());
		Assert.assertEquals(contractId3, dto.getContractId());
		Assert.assertEquals(cropYear3, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(invContractGuid3, dto.getInventoryContractGuid());
		
		dto = dtos.get(2);
		Assert.assertEquals(policyId1, dto.getPolicyId());
		Assert.assertEquals(policyNumber1, dto.getPolicyNumber());
		Assert.assertEquals(contractId1, dto.getContractId());
		Assert.assertEquals(cropYear1, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(invContractGuid1, dto.getInventoryContractGuid());

		// Test: future years with inventory.
		dtos = dao.selectByOtherYearInventory(contractId4, cropYear4, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		dto = dtos.get(0);
		Assert.assertEquals(policyId3, dto.getPolicyId());
		Assert.assertEquals(policyNumber3, dto.getPolicyNumber());
		Assert.assertEquals(contractId3, dto.getContractId());
		Assert.assertEquals(cropYear3, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(invContractGuid3, dto.getInventoryContractGuid());
		
		dto = dtos.get(1);
		Assert.assertEquals(policyId2, dto.getPolicyId());
		Assert.assertEquals(policyNumber2, dto.getPolicyNumber());
		Assert.assertEquals(contractId2, dto.getContractId());
		Assert.assertEquals(cropYear2, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(invContractGuid2, dto.getInventoryContractGuid());
	}
		
	@Test 
	public void testSelectByOtherYearDop() throws Exception {	
		
		String userId = "JUNIT_TEST";

		PolicyDao dao = persistenceSpringConfig.policyDao();

		createGrower(growerId1, 999888, "grower name");
		createPolicy(policyId1, growerId1, contractId1, cropYear1, policyNumber1, contractNumber1, 4, "ACTIVE", 1);
		createGrowerContractYear(gcyId1, contractId1, growerId1, cropYear1, 4);
		String dyContractGuid1 = createDeclaredYieldContract(contractId1, cropYear1, userId);

		// Test: No other years.
		List<PolicyDto> dtos = dao.selectByOtherYearDop(contractId1, cropYear1, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		// Test: Other prev years, but no inventory.
		cropYear2 = 2019;
		contractId2 = contractId1;
		contractNumber2 = contractNumber1;
		policyNumber2 = contractNumber2 + "-19";

		cropYear3 = 2018;
		contractId3 = contractId1;
		contractNumber3 = contractNumber1;
		policyNumber3 = contractNumber3 + "-18";

		cropYear4 = 2017;
		contractId4 = contractId1;
		contractNumber4 = contractNumber1;
		policyNumber4 = contractNumber4 + "-17";		
		
		createPolicy(policyId2, growerId1, contractId2, cropYear2, policyNumber2, contractNumber2, 4, "ACTIVE", 1);
		createPolicy(policyId3, growerId1, contractId3, cropYear3, policyNumber3, contractNumber3, 4, "ACTIVE", 1);
		createPolicy(policyId4, growerId1, contractId4, cropYear4, policyNumber4, contractNumber4, 4, "ACTIVE", 1);

		createGrowerContractYear(gcyId2, contractId2, growerId1, cropYear2, 4);
		createGrowerContractYear(gcyId3, contractId3, growerId1, cropYear3, 4);
		createGrowerContractYear(gcyId4, contractId4, growerId1, cropYear4, 4);
		
		dtos = dao.selectByOtherYearDop(contractId1, cropYear1, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		// Test: One year with dop.
		String dyContractGuid2 = createDeclaredYieldContract(contractId2, cropYear2, userId);

		dtos = dao.selectByOtherYearDop(contractId1, cropYear1, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		PolicyDto dto = dtos.get(0);
		Assert.assertEquals(policyId2, dto.getPolicyId());
		Assert.assertEquals(policyNumber2, dto.getPolicyNumber());
		Assert.assertEquals(contractId2, dto.getContractId());
		Assert.assertEquals(cropYear2, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(dyContractGuid2, dto.getDeclaredYieldContractGuid());
		

		// Test: All prev years with inventory.
		String dyContractGuid3 = createDeclaredYieldContract(contractId3, cropYear3, userId);
		String dyContractGuid4 = createDeclaredYieldContract(contractId4, cropYear4, userId);

		dtos = dao.selectByOtherYearDop(contractId1, cropYear1, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		dto = dtos.get(0);
		Assert.assertEquals(policyId3, dto.getPolicyId());
		Assert.assertEquals(policyNumber3, dto.getPolicyNumber());
		Assert.assertEquals(contractId3, dto.getContractId());
		Assert.assertEquals(cropYear3, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(dyContractGuid3, dto.getDeclaredYieldContractGuid());
		
		dto = dtos.get(1);
		Assert.assertEquals(policyId2, dto.getPolicyId());
		Assert.assertEquals(policyNumber2, dto.getPolicyNumber());
		Assert.assertEquals(contractId2, dto.getContractId());
		Assert.assertEquals(cropYear2, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(dyContractGuid2, dto.getDeclaredYieldContractGuid());

		// Test: prev and future years with inventory.
		dtos = dao.selectByOtherYearDop(contractId2, cropYear2, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(3, dtos.size());

		dto = dtos.get(0);
		Assert.assertEquals(policyId4, dto.getPolicyId());
		Assert.assertEquals(policyNumber4, dto.getPolicyNumber());
		Assert.assertEquals(contractId4, dto.getContractId());
		Assert.assertEquals(cropYear4, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(dyContractGuid4, dto.getDeclaredYieldContractGuid());
		
		dto = dtos.get(1);
		Assert.assertEquals(policyId3, dto.getPolicyId());
		Assert.assertEquals(policyNumber3, dto.getPolicyNumber());
		Assert.assertEquals(contractId3, dto.getContractId());
		Assert.assertEquals(cropYear3, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(dyContractGuid3, dto.getDeclaredYieldContractGuid());
		
		dto = dtos.get(2);
		Assert.assertEquals(policyId1, dto.getPolicyId());
		Assert.assertEquals(policyNumber1, dto.getPolicyNumber());
		Assert.assertEquals(contractId1, dto.getContractId());
		Assert.assertEquals(cropYear1, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(dyContractGuid1, dto.getDeclaredYieldContractGuid());

		// Test: future years with dop.
		dtos = dao.selectByOtherYearDop(contractId4, cropYear4, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		dto = dtos.get(0);
		Assert.assertEquals(policyId3, dto.getPolicyId());
		Assert.assertEquals(policyNumber3, dto.getPolicyNumber());
		Assert.assertEquals(contractId3, dto.getContractId());
		Assert.assertEquals(cropYear3, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(dyContractGuid3, dto.getDeclaredYieldContractGuid());
		
		dto = dtos.get(1);
		Assert.assertEquals(policyId2, dto.getPolicyId());
		Assert.assertEquals(policyNumber2, dto.getPolicyNumber());
		Assert.assertEquals(contractId2, dto.getContractId());
		Assert.assertEquals(cropYear2, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(dyContractGuid2, dto.getDeclaredYieldContractGuid());
	}

	@Test 
	public void testSelectByOtherYearVerified() throws Exception {	
		
		String userId = "JUNIT_TEST";

		PolicyDao dao = persistenceSpringConfig.policyDao();

		createGrower(growerId1, 999888, "grower name");
		createPolicy(policyId1, growerId1, contractId1, cropYear1, policyNumber1, contractNumber1, 4, "ACTIVE", 1);
		createGrowerContractYear(gcyId1, contractId1, growerId1, cropYear1, 4);
		String dyContractGuid1 = createDeclaredYieldContract(contractId1, cropYear1, userId);
		String verContractGuid1 = createVerifiedYieldContract(contractId1, cropYear1, dyContractGuid1, userId);

		// Test: No other years.
		List<PolicyDto> dtos = dao.selectByOtherYearVerified(contractId1, cropYear1, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		// Test: Other prev years, but no verified yield.
		cropYear2 = 2019;
		contractId2 = contractId1;
		contractNumber2 = contractNumber1;
		policyNumber2 = contractNumber2 + "-19";

		cropYear3 = 2018;
		contractId3 = contractId1;
		contractNumber3 = contractNumber1;
		policyNumber3 = contractNumber3 + "-18";

		cropYear4 = 2017;
		contractId4 = contractId1;
		contractNumber4 = contractNumber1;
		policyNumber4 = contractNumber4 + "-17";		
		
		createPolicy(policyId2, growerId1, contractId2, cropYear2, policyNumber2, contractNumber2, 4, "ACTIVE", 1);
		createPolicy(policyId3, growerId1, contractId3, cropYear3, policyNumber3, contractNumber3, 4, "ACTIVE", 1);
		createPolicy(policyId4, growerId1, contractId4, cropYear4, policyNumber4, contractNumber4, 4, "ACTIVE", 1);

		createGrowerContractYear(gcyId2, contractId2, growerId1, cropYear2, 4);
		createGrowerContractYear(gcyId3, contractId3, growerId1, cropYear3, 4);
		createGrowerContractYear(gcyId4, contractId4, growerId1, cropYear4, 4);
		
		dtos = dao.selectByOtherYearVerified(contractId1, cropYear1, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());

		// Test: One year with verified yield.
		String dyContractGuid2 = createDeclaredYieldContract(contractId2, cropYear2, userId);
		String verContractGuid2 = createVerifiedYieldContract(contractId2, cropYear2, dyContractGuid2, userId);

		dtos = dao.selectByOtherYearVerified(contractId1, cropYear1, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());

		PolicyDto dto = dtos.get(0);
		Assert.assertEquals(policyId2, dto.getPolicyId());
		Assert.assertEquals(policyNumber2, dto.getPolicyNumber());
		Assert.assertEquals(contractId2, dto.getContractId());
		Assert.assertEquals(cropYear2, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(verContractGuid2, dto.getVerifiedYieldContractGuid());
		

		// Test: All prev years with verified yield.
		String dyContractGuid3 = createDeclaredYieldContract(contractId3, cropYear3, userId);
		String dyContractGuid4 = createDeclaredYieldContract(contractId4, cropYear4, userId);

		String verContractGuid3 = createVerifiedYieldContract(contractId3, cropYear3, dyContractGuid3, userId);
		String verContractGuid4 = createVerifiedYieldContract(contractId4, cropYear4, dyContractGuid4, userId);
		
		dtos = dao.selectByOtherYearVerified(contractId1, cropYear1, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		dto = dtos.get(0);
		Assert.assertEquals(policyId3, dto.getPolicyId());
		Assert.assertEquals(policyNumber3, dto.getPolicyNumber());
		Assert.assertEquals(contractId3, dto.getContractId());
		Assert.assertEquals(cropYear3, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(verContractGuid3, dto.getVerifiedYieldContractGuid());
		
		dto = dtos.get(1);
		Assert.assertEquals(policyId2, dto.getPolicyId());
		Assert.assertEquals(policyNumber2, dto.getPolicyNumber());
		Assert.assertEquals(contractId2, dto.getContractId());
		Assert.assertEquals(cropYear2, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(verContractGuid2, dto.getVerifiedYieldContractGuid());

		// Test: prev and future years with inventory.
		dtos = dao.selectByOtherYearVerified(contractId2, cropYear2, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(3, dtos.size());

		dto = dtos.get(0);
		Assert.assertEquals(policyId4, dto.getPolicyId());
		Assert.assertEquals(policyNumber4, dto.getPolicyNumber());
		Assert.assertEquals(contractId4, dto.getContractId());
		Assert.assertEquals(cropYear4, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(verContractGuid4, dto.getVerifiedYieldContractGuid());
		
		dto = dtos.get(1);
		Assert.assertEquals(policyId3, dto.getPolicyId());
		Assert.assertEquals(policyNumber3, dto.getPolicyNumber());
		Assert.assertEquals(contractId3, dto.getContractId());
		Assert.assertEquals(cropYear3, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(verContractGuid3, dto.getVerifiedYieldContractGuid());
		
		dto = dtos.get(2);
		Assert.assertEquals(policyId1, dto.getPolicyId());
		Assert.assertEquals(policyNumber1, dto.getPolicyNumber());
		Assert.assertEquals(contractId1, dto.getContractId());
		Assert.assertEquals(cropYear1, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(verContractGuid1, dto.getVerifiedYieldContractGuid());

		// Test: future years with verified yield.
		dtos = dao.selectByOtherYearVerified(contractId4, cropYear4, 2);
		Assert.assertNotNull(dtos);
		Assert.assertEquals(2, dtos.size());

		dto = dtos.get(0);
		Assert.assertEquals(policyId3, dto.getPolicyId());
		Assert.assertEquals(policyNumber3, dto.getPolicyNumber());
		Assert.assertEquals(contractId3, dto.getContractId());
		Assert.assertEquals(cropYear3, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(verContractGuid3, dto.getVerifiedYieldContractGuid());
		
		dto = dtos.get(1);
		Assert.assertEquals(policyId2, dto.getPolicyId());
		Assert.assertEquals(policyNumber2, dto.getPolicyNumber());
		Assert.assertEquals(contractId2, dto.getContractId());
		Assert.assertEquals(cropYear2, dto.getCropYear());
		Assert.assertEquals(Integer.valueOf(4), dto.getInsurancePlanId());
		Assert.assertEquals(verContractGuid2, dto.getVerifiedYieldContractGuid());
	}
	
	private void compareStrings(String sortDirection) {
		//First iteration of loop: previousValue = 0			
		if(previousStringValue != ""){
			
			int compare = previousStringValue.compareToIgnoreCase(currentStringValue);
			
			//compare < 0 if sA is smaller than sB
			//compare = 0 if sA is the same as sB
			//compare > 0 if sA is greater than sB 

			if(sortDirection.contentEquals(sortOrderAscending) ) {
				orderMatches = (compare <= 0); 
			} else {
				orderMatches = (compare >= 0); 				
			}
				
		}
		
		if(!orderMatches) {
		    System.out.println("Order is wrong: " + currentStringValue + " < " + previousStringValue);
		    testSuccessful = false;
		}
		
		previousStringValue = currentStringValue;
	}
	
	private void compareInt(String sortDirection) {
		//First iteration of loop: previousValue = 0			
		if(previousIntValue > 0){
			
			
			if(sortDirection.contentEquals(sortOrderAscending) ) {
				orderMatches = (previousIntValue <= currentIntValue); 
			} else {
				orderMatches = (previousIntValue >= currentIntValue); 			
			}

		}
		
		if(!orderMatches) {
            System.out.println("Order is wrong: " + currentIntValue + " < " + previousIntValue);
            testSuccessful = false;
		}
		
		previousIntValue = currentIntValue;

	}
	
	
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	private static Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
	
}
