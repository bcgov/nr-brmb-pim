package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types;

import ca.bc.gov.nrs.common.wfone.rest.resource.types.BaseResourceTypes;

public class ResourceTypes extends BaseResourceTypes {
	
	public static final String NAMESPACE = "http://underwriting.cirras.mal.gov.bc.ca/v1/";

	public static final String ENDPOINTS_NAME = "endpoints";
	public static final String ENDPOINTS = NAMESPACE + ENDPOINTS_NAME;

	public static final String UWCONTRACT_LIST_NAME = "uwContractList";
	public static final String UWCONTRACT_LIST = NAMESPACE + UWCONTRACT_LIST_NAME;

	public static final String UWCONTRACT_NAME = "uwContract";
	public static final String UWCONTRACT = NAMESPACE + UWCONTRACT_NAME;

	public static final String INVENTORY_CONTRACT_LIST_NAME = "inventoryContractList";
	public static final String INVENTORY_CONTRACT_LIST = NAMESPACE + INVENTORY_CONTRACT_LIST_NAME;
	
	public static final String INVENTORY_CONTRACT_NAME = "inventoryContract";
	public static final String INVENTORY_CONTRACT = NAMESPACE + INVENTORY_CONTRACT_NAME;
	public static final String ROLLOVER_INVENTORY_CONTRACT = NAMESPACE + "rolloverInventoryContract";
	public static final String CREATE_INVENTORY_CONTRACT = NAMESPACE + "createInventoryContract";
	public static final String UPDATE_INVENTORY_CONTRACT = NAMESPACE + "updateInventoryContract";
	public static final String DELETE_INVENTORY_CONTRACT = NAMESPACE + "deleteInventoryContract";


	public static final String CROP_COMMODITY_LIST_NAME = "cropCommodityList";
	public static final String CROP_COMMODITY_LIST = NAMESPACE + CROP_COMMODITY_LIST_NAME;

	public static final String CROP_COMMODITY_NAME = "cropCommodity";
	public static final String CROP_COMMODITY = NAMESPACE + CROP_COMMODITY_NAME;

	public static final String ANNUAL_FIELD_LIST_NAME = "annualFieldList";
	public static final String ANNUAL_FIELD_LIST = NAMESPACE + ANNUAL_FIELD_LIST_NAME;	
	
	public static final String ANNUAL_FIELD_NAME = "annualField";
	public static final String ANNUAL_FIELD = NAMESPACE + ANNUAL_FIELD_NAME;
	public static final String ROLLOVER_ANNUAL_FIELD_INVENTORY = NAMESPACE + "rolloverAnnualFieldInventory";

	public static final String LEGAL_LAND_LIST_NAME = "legalLandList";
	public static final String LEGAL_LAND_LIST = NAMESPACE + LEGAL_LAND_LIST_NAME;	

	public static final String ADD_FIELD_VALIDATION_NAME = "addFieldValidation";
	public static final String ADD_FIELD_VALIDATION = NAMESPACE + ADD_FIELD_VALIDATION_NAME;

	public static final String REMOVE_FIELD_VALIDATION_NAME = "removeFieldValidation";
	public static final String REMOVE_FIELD_VALIDATION = NAMESPACE + REMOVE_FIELD_VALIDATION_NAME;
	
	
	public static final String RENAME_LEGAL_VALIDATION_NAME = "renameLegalValidation";
	public static final String RENAME_LEGAL_VALIDATION = NAMESPACE + RENAME_LEGAL_VALIDATION_NAME;
		
	public static final String REPLACE_LEGAL_VALIDATION_NAME = "replaceLegalValidation";
	public static final String REPLACE_LEGAL_VALIDATION = NAMESPACE + REPLACE_LEGAL_VALIDATION_NAME;
		
	//Generic Code Tables
	public static final String SYNC_CODE_NAME = "syncCode";
	public static final String SYNC_CODE = NAMESPACE + SYNC_CODE_NAME;
	public static final String SYNCHRONIZE_CODE = NAMESPACE + "SynchronizeCode";
	public static final String DELETE_SYNC_CODE = NAMESPACE + "deleteSyncCode";
	
	//Grower
	public static final String GROWER_NAME = "grower";
	public static final String GROWER = NAMESPACE + GROWER_NAME;
	public static final String SYNCHRONIZE_GROWER = NAMESPACE + "synchronizeGrower";
	public static final String DELETE_SYNC_GROWER = NAMESPACE + "deleteGrower";

	//Policy
	public static final String POLICY_NAME = "policy";
	public static final String POLICY = NAMESPACE + POLICY_NAME;
	public static final String SYNCHRONIZE_POLICY = NAMESPACE + "synchronizePolicy";
	public static final String DELETE_SYNC_POLICY = NAMESPACE + "deletePolicy";

	//Legal Land
	public static final String LEGAL_LAND_NAME = "LegalLand";
	public static final String LEGAL_LAND = NAMESPACE + LEGAL_LAND_NAME;
	public static final String CREATE_LEGAL_LAND = NAMESPACE + "createLegalLand";
	public static final String UPDATE_LEGAL_LAND = NAMESPACE + "updateLegalLand";
	public static final String DELETE_LEGAL_LAND = NAMESPACE + "deleteLegalLand";
	//Just used for testing at the moment
	public static final String SYNCHRONIZE_LEGAL_LAND = NAMESPACE + "synchronizeLegalLand";
	public static final String DELETE_SYNC_LEGAL_LAND = NAMESPACE + "deleteSyncLegalLand";
	
	//Field
	public static final String FIELD_NAME = "Field";
	public static final String FIELD = NAMESPACE + FIELD_NAME;
	public static final String SYNCHRONIZE_FIELD = NAMESPACE + "synchronizeField";
	public static final String DELETE_FIELD = NAMESPACE + "deleteField";
	
	//Legal Land - Field xref
	public static final String LEGAL_LAND_FIELD_XREF_NAME = "LegalLandFieldXref";
	public static final String LEGAL_LAND_FIELD_XREF = NAMESPACE + LEGAL_LAND_FIELD_XREF_NAME;
	public static final String SYNCHRONIZE_LEGAL_LAND_FIELD_XREF = NAMESPACE + "synchronizeLegalLandFieldXref";
	public static final String DELETE_LEGAL_LAND_FIELD_XREF = NAMESPACE + "deleteLegalLandFieldXref";

	//Annual Field Detail
	public static final String ANNUAL_FIELD_DETAIL_NAME = "AnnualFieldDetail";
	public static final String ANNUAL_FIELD_DETAIL = NAMESPACE + ANNUAL_FIELD_DETAIL_NAME;
	public static final String SYNCHRONIZE_ANNUAL_FIELD_DETAIL = NAMESPACE + "synchronizeAnnualFieldDetail";
	public static final String DELETE_ANNUAL_FIELD_DETAIL = NAMESPACE + "deleteAnnualFieldDetail";
	
	//Grower Contract Year
	public static final String GROWER_CONTRACT_YEAR_NAME = "GrowerContractYear";
	public static final String GROWER_CONTRACT_YEAR = NAMESPACE + GROWER_CONTRACT_YEAR_NAME;
	public static final String SYNCHRONIZE_GROWER_CONTRACT_YEAR = NAMESPACE + "synchronizeGrowerContractYear";
	public static final String DELETE_GROWER_CONTRACT_YEAR = NAMESPACE + "deleteGrowerContractYear";
	
	// Contracted Field Detail
	public static final String CONTRACTED_FIELD_DETAIL_NAME = "ContractedFieldDetail";
	public static final String CONTRACTED_FIELD_DETAIL = NAMESPACE + CONTRACTED_FIELD_DETAIL_NAME;
	public static final String SYNCHRONIZE_CONTRACTED_FIELD_DETAIL = NAMESPACE + "synchronizeContractedFieldDetail";
	public static final String DELETE_CONTRACTED_FIELD_DETAIL = NAMESPACE + "deleteContractedFieldDetail";
	
	//Commodity Variety
	public static final String SYNC_COMMODITY_VARIETY_NAME = "syncCommodityVariety";
	public static final String SYNC_COMMODITY_VARIETY = NAMESPACE + SYNC_COMMODITY_VARIETY_NAME;
	public static final String SYNCHRONIZE_SYNC_COMMODITY_VARIETY = NAMESPACE + "SynchronizeCommodityVariety";
	public static final String DELETE_SYNC_COMMODITY_VARIETY = NAMESPACE + "deleteSyncCommodityVariety";
	
	//Contact
	public static final String CONTACT_NAME = "Contact";
	public static final String CONTACT = NAMESPACE + CONTACT_NAME;
	public static final String SYNCHRONIZE_CONTACT = NAMESPACE + "SynchronizeContact";
	public static final String DELETE_CONTACT = NAMESPACE + "deleteContact";
	
	//Grower Contact
	public static final String GROWER_CONTACT_NAME = "GrowerContact";
	public static final String GROWER_CONTACT = NAMESPACE + GROWER_CONTACT_NAME;
	public static final String SYNCHRONIZE_GROWER_CONTACT = NAMESPACE + "SynchronizeGrowerContact";
	public static final String DELETE_GROWER_CONTACT = NAMESPACE + "deleteGrowerContact";
	
	//Contact Email
	public static final String CONTACT_EMAIL_NAME = "ContactEmail";
	public static final String CONTACT_EMAIL = NAMESPACE + CONTACT_EMAIL_NAME;
	public static final String SYNCHRONIZE_CONTACT_EMAIL = NAMESPACE + "SynchronizeContactEmail";
	public static final String DELETE_CONTACT_EMAIL = NAMESPACE + "deleteContactEmail";
	
	//Contact Phone
	public static final String CONTACT_PHONE_NAME = "ContactPhone";
	public static final String CONTACT_PHONE = NAMESPACE + CONTACT_PHONE_NAME;
	public static final String SYNCHRONIZE_CONTACT_PHONE = NAMESPACE + "SynchronizeContactPhone";
	public static final String DELETE_CONTACT_PHONE = NAMESPACE + "deleteContactPhone";
	
	//Commodity Type Code - For Data Sync
	public static final String COMMODITY_TYPE_CODE_NAME = "CommodityTypeCode";
	public static final String COMMODITY_TYPE_CODE = NAMESPACE + COMMODITY_TYPE_CODE_NAME;
	public static final String SYNCHRONIZE_COMMODITY_TYPE_CODE = NAMESPACE + "SynchronizeCommodityTypeCode";
	public static final String DELETE_COMMODITY_TYPE_CODE = NAMESPACE + "deleteCommodityTypeCode";

	//For API
	public static final String COMMODITY_TYPE_CODE_LIST_NAME = "CommodityTypeCodeList";
	public static final String COMMODITY_TYPE_CODE_LIST = NAMESPACE + COMMODITY_TYPE_CODE_LIST_NAME;

	//Commodity Type Variety Xref
	public static final String COMMODITY_TYPE_VARIETY_XREF_NAME = "CommodityTypeVarietyXref";
	public static final String COMMODITY_TYPE_VARIETY_XREF = NAMESPACE + COMMODITY_TYPE_VARIETY_XREF_NAME;
	public static final String SYNCHRONIZE_COMMODITY_TYPE_VARIETY_XREF = NAMESPACE + "SynchronizeCommodityTypeVarietyXref";
	public static final String DELETE_COMMODITY_TYPE_VARIETY_XREF = NAMESPACE + "deleteCommodityTypeVarietyXref";

	//DOP Yield Contract
	public static final String DOP_YIELD_CONTRACT_NAME = "dopYieldContract";
	public static final String DOP_YIELD_CONTRACT = NAMESPACE + DOP_YIELD_CONTRACT_NAME;
	public static final String ROLLOVER_DOP_YIELD_CONTRACT = NAMESPACE + "rolloverDopYieldContract";
	public static final String CREATE_DOP_YIELD_CONTRACT = NAMESPACE + "createDopYieldContract";
	public static final String UPDATE_DOP_YIELD_CONTRACT = NAMESPACE + "updateDopYieldContract";
	public static final String DELETE_DOP_YIELD_CONTRACT = NAMESPACE + "deleteDopYieldContract";
	
	//Yield Measurement Unit Type
	public static final String YIELD_MEAS_UNIT_TYPE_CODE_NAME = "YieldMeasUnitTypeCode";
	public static final String YIELD_MEAS_UNIT_TYPE_CODE = NAMESPACE + YIELD_MEAS_UNIT_TYPE_CODE_NAME;
	public static final String YIELD_MEAS_UNIT_TYPE_CODE_LIST_NAME = "YieldMeasUnitTypeCodeList";
	public static final String YIELD_MEAS_UNIT_TYPE_CODE_LIST = NAMESPACE + YIELD_MEAS_UNIT_TYPE_CODE_LIST_NAME;

	//Grade Modifier
	public static final String GRADE_MODIFIER_NAME = "GradeModifier";
	public static final String GRADE_MODIFIER = NAMESPACE + GRADE_MODIFIER_NAME;
	public static final String GRADE_MODIFIER_LIST_NAME = "GradeModifierList";
	public static final String GRADE_MODIFIER_LIST = NAMESPACE + GRADE_MODIFIER_LIST_NAME;
	public static final String SAVE_GRADE_MODIFIER_LIST = NAMESPACE + "saveGradeModifierList";

	//Grade Modifier Type
	public static final String GRADE_MODIFIER_TYPE_LIST_NAME = "GradeModifierTypeList";
	public static final String GRADE_MODIFIER_TYPE_LIST = NAMESPACE + GRADE_MODIFIER_TYPE_LIST_NAME;
	public static final String SAVE_GRADE_MODIFIER_TYPE_LIST = NAMESPACE + "saveGradeModifierTypeList";

	//Risk Area
	public static final String RISK_AREA_NAME = "RiskArea";
	public static final String RISK_AREA = NAMESPACE + RISK_AREA_NAME;
	public static final String RISK_AREA_LIST_NAME = "RiskAreaList";
	public static final String RISK_AREA_LIST = NAMESPACE + RISK_AREA_LIST_NAME;

	//Seeding Deadline
	public static final String SEEDING_DEADLINE_NAME = "SeedingDeadline";
	public static final String SEEDING_DEADLINE = NAMESPACE + SEEDING_DEADLINE_NAME;
	public static final String SEEDING_DEADLINE_LIST_NAME = "SeedingDeadlineList";
	public static final String SEEDING_DEADLINE_LIST = NAMESPACE + SEEDING_DEADLINE_LIST_NAME;
	public static final String SAVE_SEEDING_DEADLINE_LIST = NAMESPACE + "saveSeedingDeadlineList";

	//Underwriting Year
	public static final String UNDERWRITING_YEAR_NAME = "UnderwritingYear";
	public static final String UNDERWRITING_YEAR = NAMESPACE + UNDERWRITING_YEAR_NAME;
	public static final String CREATE_UNDERWRITING_YEAR = NAMESPACE + "createUnderwritingYear";
	public static final String DELETE_UNDERWRITING_YEAR = NAMESPACE + "deleteUnderwritingYear";
	//Underwriting Year List
	public static final String UNDERWRITING_YEAR_LIST_NAME = "UnderwritingYearList";
	public static final String UNDERWRITING_YEAR_LIST = NAMESPACE + UNDERWRITING_YEAR_LIST_NAME;

	//Crop Variety Insurability
	public static final String CROP_VARIETY_INSURABILITY_LIST_NAME = "CropVarietyInsurabilityList";
	public static final String CROP_VARIETY_INSURABILITY_LIST = NAMESPACE + CROP_VARIETY_INSURABILITY_LIST_NAME;
	public static final String SAVE_CROP_VARIETY_INSURABILITY_LIST = NAMESPACE + "saveCropVarietyInsurabilityList";

	//Yield Measurement Unit Conversion List
	public static final String YIELD_MEAS_UNIT_CONVERSION_LIST_NAME = "YieldMeasUnitConversionList";
	public static final String YIELD_MEAS_UNIT_CONVERSION_LIST = NAMESPACE + YIELD_MEAS_UNIT_CONVERSION_LIST_NAME;
	public static final String SAVE_YIELD_MEAS_UNIT_CONVERSION_LIST = NAMESPACE + "saveYieldMeasUnitConversionList";

	//Verified Yield Contract
	public static final String VERIFIED_YIELD_CONTRACT_NAME = "verifiedYieldContract";
	public static final String VERIFIED_YIELD_CONTRACT = NAMESPACE + VERIFIED_YIELD_CONTRACT_NAME;
	public static final String ROLLOVER_VERIFIED_YIELD_CONTRACT = NAMESPACE + "rolloverVerifiedYieldContract";
	public static final String CREATE_VERIFIED_YIELD_CONTRACT = NAMESPACE + "createVerifiedYieldContract";
	public static final String UPDATE_VERIFIED_YIELD_CONTRACT = NAMESPACE + "updateVerifiedYieldContract";
	public static final String DELETE_VERIFIED_YIELD_CONTRACT = NAMESPACE + "deleteVerifiedYieldContract";
	
}

