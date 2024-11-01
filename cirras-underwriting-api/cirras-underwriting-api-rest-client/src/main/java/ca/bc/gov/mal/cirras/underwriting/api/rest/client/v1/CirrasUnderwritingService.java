package ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1;

import java.time.LocalDate;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropCommodityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropVarietyInsurabilityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GradeModifierListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GradeModifierTypeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AddFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CommodityTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactEmailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactPhoneRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandFieldXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RemoveFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RenameLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ReplaceLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RiskAreaListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SeedingDeadlineListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.YieldMeasUnitConversionListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.YieldMeasUnitTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeVarietyXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableListRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.HealthCheckResponseRsrc;
import ca.bc.gov.nrs.wfone.common.rest.client.RestClientServiceException;

public interface CirrasUnderwritingService {
	
	EndpointsRsrc getTopLevelEndpoints() throws CirrasUnderwritingServiceException;

	String getSwaggerString() throws RestClientServiceException;

	HealthCheckResponseRsrc getHealthCheck(String callstack) throws RestClientServiceException;

	//////////////////////////////////////////////////////
	// Code tables
	//////////////////////////////////////////////////////
	CodeTableListRsrc getCodeTables(
			EndpointsRsrc parent,
			String codeTableName, 
			LocalDate effectiveAsOfDate) throws CirrasUnderwritingServiceException;

	CodeTableRsrc getCodeTable(CodeTableRsrc codeTable, LocalDate effectiveAsOfDate) throws CirrasUnderwritingServiceException;

	CodeTableRsrc updateCodeTable(CodeTableRsrc codeTable) throws CirrasUnderwritingServiceException, ValidationException;

	//////////////////////////////////////////////////////
	// UW Contracts
	//////////////////////////////////////////////////////
	UwContractListRsrc getUwContractList(
			EndpointsRsrc parent,
			String cropYear,
			String insurancePlanId, 
			String officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String datasetType,
			String sortColumn,
		    String sortDirection,
			Integer pageNumber, 
			Integer pageRowCount) throws CirrasUnderwritingServiceException;

	UwContractRsrc getUwContract(UwContractRsrc resource, String loadLinkedPolicies) throws CirrasUnderwritingServiceException;

	//////////////////////////////////////////////////////
	// Annual Fields
	//////////////////////////////////////////////////////
	AnnualFieldListRsrc getAnnualFieldList(
			EndpointsRsrc parent,
			String legalLandId,
			String fieldId,
			String cropYear
		)	
		throws CirrasUnderwritingServiceException;
	
	AnnualFieldRsrc rolloverAnnualFieldInventory(AnnualFieldRsrc resource, String rolloverToCropYear, String insurancePlanId) throws CirrasUnderwritingServiceException;

	
	//////////////////////////////////////////////////////
	// Inventory Contracts
	//////////////////////////////////////////////////////	
	InventoryContractRsrc getInventoryContract(UwContractRsrc resource) throws CirrasUnderwritingServiceException;

	InventoryContractRsrc rolloverInventoryContract(UwContractRsrc resource) throws CirrasUnderwritingServiceException;

	InventoryContractRsrc createInventoryContract(EndpointsRsrc parent, InventoryContractRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	
	InventoryContractRsrc updateInventoryContract(String inventoryContractGuid, InventoryContractRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	
	void deleteInventoryContract(InventoryContractRsrc resource) throws CirrasUnderwritingServiceException;

	AddFieldValidationRsrc validateAddField(UwContractRsrc resource, String fieldId, String transferFromPolicyId) throws CirrasUnderwritingServiceException;

	RemoveFieldValidationRsrc validateRemoveField(UwContractRsrc resource, String fieldId) throws CirrasUnderwritingServiceException;
	
	RenameLegalValidationRsrc validateRenameLegal(UwContractRsrc resource, String annualFieldDetailId, String newLegalLocation) throws CirrasUnderwritingServiceException;
	
	ReplaceLegalValidationRsrc validateReplaceLegal(UwContractRsrc resource, String annualFieldDetailId, String fieldLabel, String legalLandId) throws CirrasUnderwritingServiceException;
	
	InventoryContractListRsrc getInventoryContractList(
			EndpointsRsrc parent,
			String cropYear,
			String insurancePlanId, 
			String officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String inventoryContractGuids
		)	
		throws CirrasUnderwritingServiceException;

	byte[] generateInventoryReport(
			EndpointsRsrc parent, 
			String cropYear,
			String insurancePlanId, 
			String officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String policyIds
		) throws CirrasUnderwritingServiceException;
	
	
	//////////////////////////////////////////////////////
	// DOP Yield Contract
	//////////////////////////////////////////////////////	
	DopYieldContractRsrc rolloverDopYieldContract(UwContractRsrc resource) throws CirrasUnderwritingServiceException;

	DopYieldContractRsrc getDopYieldContract(UwContractRsrc resource) throws CirrasUnderwritingServiceException;

	byte[] generateDopReport(
			EndpointsRsrc parent, 
			String cropYear,
			String insurancePlanId, 
			String officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String policyIds
		) throws CirrasUnderwritingServiceException;
	
	DopYieldContractRsrc createDopYieldContract(EndpointsRsrc parent, DopYieldContractRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	
	DopYieldContractRsrc updateDopYieldContract(DopYieldContractRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;

	void deleteDopYieldContract(DopYieldContractRsrc resource) throws CirrasUnderwritingServiceException;

	//////////////////////////////////////////////////////
	// Verified Yield Contract
	//////////////////////////////////////////////////////
	VerifiedYieldContractRsrc rolloverVerifiedYieldContract(UwContractRsrc resource) throws CirrasUnderwritingServiceException;

	VerifiedYieldContractRsrc getVerifiedYieldContract(UwContractRsrc resource) throws CirrasUnderwritingServiceException;
	
	
	//////////////////////////////////////////////////////
	// Seeding Deadline
	//////////////////////////////////////////////////////	
	SeedingDeadlineListRsrc getSeedingDeadlines(EndpointsRsrc parent, String cropYear) throws CirrasUnderwritingServiceException;

	SeedingDeadlineListRsrc saveSeedingDeadlines(EndpointsRsrc parent, SeedingDeadlineListRsrc resource, String cropYear) throws CirrasUnderwritingServiceException, ValidationException;
	
	//////////////////////////////////////////////////////
	// Yield Measurement Unit Type Code
	//////////////////////////////////////////////////////	
	YieldMeasUnitTypeCodeListRsrc getYieldMeasUnitTypeCodeList(
			EndpointsRsrc parent,
			String insurancePlanId) throws CirrasUnderwritingServiceException;

	
	//////////////////////////////////////////////////////
	// Grade Modifier
	//////////////////////////////////////////////////////	
	GradeModifierListRsrc getGradeModifierList(EndpointsRsrc parent, String cropYear, String insurancePlanId, String cropCommodityId) throws CirrasUnderwritingServiceException;
	GradeModifierListRsrc saveGradeModifiers(GradeModifierListRsrc resource, String cropYear, String insurancePlanId, String cropCommodityId) throws CirrasUnderwritingServiceException, ValidationException;
	
	//////////////////////////////////////////////////////
	// Grade Modifier Type
	//////////////////////////////////////////////////////	
	GradeModifierTypeListRsrc getGradeModifierTypeList(EndpointsRsrc parent, String cropYear) throws CirrasUnderwritingServiceException;
	GradeModifierTypeListRsrc saveGradeModifierTypes(GradeModifierTypeListRsrc resource, String cropYear) throws CirrasUnderwritingServiceException, ValidationException;
	
	//////////////////////////////////////////////////////
	// Crop Variety Insurability
	//////////////////////////////////////////////////////	
	CropVarietyInsurabilityListRsrc getCropVarietyInsurabilities(EndpointsRsrc parent, String insurancePlanId, String loadForEdit) throws CirrasUnderwritingServiceException;
	CropVarietyInsurabilityListRsrc saveCropVarietyInsurabilities(CropVarietyInsurabilityListRsrc resource, String insurancePlanId, String loadForEdit) throws CirrasUnderwritingServiceException, ValidationException;
	
	//////////////////////////////////////////////////////
	// Yield Measurement Unit Conversion
	//////////////////////////////////////////////////////	
	YieldMeasUnitConversionListRsrc getYieldMeasUnitConversions(EndpointsRsrc parent, String insurancePlanId, String srcYieldMeasUnitTypeCode, String targetYieldMeasUnitTypeCode) throws CirrasUnderwritingServiceException;
	YieldMeasUnitConversionListRsrc saveYieldMeasUnitConversions(YieldMeasUnitConversionListRsrc resource, String insurancePlanId, String srcYieldMeasUnitTypeCode, String targetYieldMeasUnitTypeCode) throws CirrasUnderwritingServiceException, ValidationException;
	
	//////////////////////////////////////////////////////
	// Commodity Types
	//////////////////////////////////////////////////////	
	CommodityTypeCodeListRsrc getCommodityTypeCodeList(EndpointsRsrc parent, String insurancePlanId) throws CirrasUnderwritingServiceException;
	
	//////////////////////////////////////////////////////
	// Risk Area
	//////////////////////////////////////////////////////	
	RiskAreaListRsrc getRiskAreaList(EndpointsRsrc parent, String insurancePlanId) throws CirrasUnderwritingServiceException;
	
	//////////////////////////////////////////////////////
	// Crop Commodities
	//////////////////////////////////////////////////////	
	CropCommodityListRsrc getCropCommodityList(
			EndpointsRsrc parent,
			String insurancePlanId, 
			String cropYear, 
			String commodityType,
			String loadChildren) throws CirrasUnderwritingServiceException;


	//////////////////////////////////////////////////////
	// Legal Land List
	//////////////////////////////////////////////////////
	LegalLandListRsrc getLegalLandList(
			EndpointsRsrc parent,
			String legalLocation, 
			String primaryPropertyIdentifier, 
			String growerInfo,
			String datasetType, 
			String isWildCardSearch, 
			String searchByLegalLocOrLegalDesc, 
			String sortColumn,
			String sortDirection, 
			Integer pageNumber, 
			Integer pageRowCount
		) throws CirrasUnderwritingServiceException;
	
	//////////////////////////////////////////////////////
	// Legal Land
	//////////////////////////////////////////////////////
	LegalLandRsrc createLegalLand(EndpointsRsrc parent, LegalLandRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	LegalLandRsrc getLegalLand(LegalLandRsrc resource) throws CirrasUnderwritingServiceException;
	LegalLandRsrc updateLegalLand(LegalLandRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteLegalLand(LegalLandRsrc resource) throws CirrasUnderwritingServiceException;

	//////////////////////////////////////////////////////
	// Underwriting Year
	//////////////////////////////////////////////////////
	UnderwritingYearRsrc createUnderwritingYear(EndpointsRsrc parent, UnderwritingYearRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	UnderwritingYearRsrc getUnderwritingYear(UnderwritingYearRsrc resource) throws CirrasUnderwritingServiceException;
	void deleteUnderwritingYear(UnderwritingYearRsrc resource) throws CirrasUnderwritingServiceException;
	UnderwritingYearListRsrc getUnderwritingYearList(EndpointsRsrc parent) throws CirrasUnderwritingServiceException;
	
	//////////////////////////////////////////////////////
	//DATA SYNC METHODS
	//////////////////////////////////////////////////////

	//Generic Code sync
	void synchronizeCode(SyncCodeRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteSyncCode(EndpointsRsrc parent, String codeTableType, String uniqueKey) throws CirrasUnderwritingServiceException;

	//Grower
	GrowerRsrc getGrower(EndpointsRsrc parent, String growerId) throws CirrasUnderwritingServiceException;
	void synchronizeGrower(GrowerRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteGrower(EndpointsRsrc parent, String growerId) throws CirrasUnderwritingServiceException;

	//Policy
	PolicyRsrc getPolicy(EndpointsRsrc parent, String policyId) throws CirrasUnderwritingServiceException;
	void synchronizePolicy(PolicyRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deletePolicy(EndpointsRsrc parent, String policyId) throws CirrasUnderwritingServiceException;

	//Legal Land Sync
	void synchronizeLegalLand(LegalLandRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteLegalLandSync(EndpointsRsrc parent, String legalLandId) throws CirrasUnderwritingServiceException;
	
	//Field
	FieldRsrc getField(EndpointsRsrc parent, String fieldId) throws CirrasUnderwritingServiceException;
	void synchronizeField(FieldRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteField(EndpointsRsrc parent, String fieldId) throws CirrasUnderwritingServiceException;
	
	//Legal Land - Field Xref
	LegalLandFieldXrefRsrc getLegalLandFieldXref(EndpointsRsrc parent, String legalLandId, String fieldId) throws CirrasUnderwritingServiceException;
	void synchronizeLegalLandFieldXref(LegalLandFieldXrefRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteLegalLandFieldXref(EndpointsRsrc parent, String legalLandId, String fieldId) throws CirrasUnderwritingServiceException;
		
	//AnnualFieldDetail
	AnnualFieldDetailRsrc getAnnualFieldDetail(EndpointsRsrc parent, String fieldId) throws CirrasUnderwritingServiceException;
	void synchronizeAnnualFieldDetail(AnnualFieldDetailRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteAnnualFieldDetail(EndpointsRsrc parent, String annualFieldDetailId) throws CirrasUnderwritingServiceException;
		
	//Grower Contract Year
	GrowerContractYearSyncRsrc getGrowerContractYear(EndpointsRsrc parent, String growerContractYearId) throws CirrasUnderwritingServiceException;
	void synchronizeGrowerContractYear(GrowerContractYearSyncRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteGrowerContractYear(EndpointsRsrc parent, String growerContractYearId) throws CirrasUnderwritingServiceException;
	
	//ContractedFieldDetail
	ContractedFieldDetailRsrc getContractedFieldDetail(EndpointsRsrc parent, String contractedFieldDetailId) throws CirrasUnderwritingServiceException;
	void synchronizeContractedFieldDetail(ContractedFieldDetailRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteContractedFieldDetail(EndpointsRsrc parent, String contractedFieldDetailId) throws CirrasUnderwritingServiceException;
	
	//Commodity and Variety
	SyncCommodityVarietyRsrc getSyncCommodityVariety(EndpointsRsrc parent, String crptId) throws CirrasUnderwritingServiceException;
	void synchronizeCommodityVariety(SyncCommodityVarietyRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteSyncCommodityVariety(EndpointsRsrc parent, String crptId) throws CirrasUnderwritingServiceException;

	//Contact
	ContactRsrc getContact(EndpointsRsrc parent, String contactId) throws CirrasUnderwritingServiceException;
	void synchronizeContact(ContactRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteContact(EndpointsRsrc parent, String contactId) throws CirrasUnderwritingServiceException;

	//Grower Contact
	GrowerContactRsrc getGrowerContact(EndpointsRsrc parent, String growerContactId) throws CirrasUnderwritingServiceException;
	void synchronizeGrowerContact(GrowerContactRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteGrowerContact(EndpointsRsrc parent, String growerContactId) throws CirrasUnderwritingServiceException;

	//Contact Email
	ContactEmailRsrc getContactEmail(EndpointsRsrc parent, String contactEmailId) throws CirrasUnderwritingServiceException;
	void synchronizeContactEmail(ContactEmailRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteContactEmail(EndpointsRsrc parent, String contactEmailId) throws CirrasUnderwritingServiceException;

	//Contact Phone
	ContactPhoneRsrc getContactPhone(EndpointsRsrc parent, String contactPhoneId) throws CirrasUnderwritingServiceException;
	void synchronizeContactPhone(ContactPhoneRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteContactPhone(EndpointsRsrc parent, String contactPhoneId) throws CirrasUnderwritingServiceException;

	//Commodity Type Code
	SyncCommodityTypeCodeRsrc getCommodityTypeCode(EndpointsRsrc parent, String commodityTypeCode) throws CirrasUnderwritingServiceException;
	void synchronizeCommodityTypeCode(SyncCommodityTypeCodeRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteCommodityTypeCode(EndpointsRsrc parent, String commodityTypeCode) throws CirrasUnderwritingServiceException;

	//Commodity Type Variety Xref
	SyncCommodityTypeVarietyXrefRsrc getCommodityTypeVarietyXref(EndpointsRsrc parent, String commodityTypeCode, String cropVarietyId) throws CirrasUnderwritingServiceException;
	void synchronizeCommodityTypeVarietyXref(SyncCommodityTypeVarietyXrefRsrc resource) throws CirrasUnderwritingServiceException, ValidationException;
	void deleteCommodityTypeVarietyXref(EndpointsRsrc parent, String commodityTypeCode, String cropVarietyId) throws CirrasUnderwritingServiceException;
}
