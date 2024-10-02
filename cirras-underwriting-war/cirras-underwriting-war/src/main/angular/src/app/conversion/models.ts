import {  InventoryContractCommodity, InventoryCoverageTotalForage, InventoryField, PolicySimple, UnderwritingComment } from "@cirras/cirras-underwriting-api";
import {  DopYieldFieldForage, DopYieldFieldGrain } from "./models-yield";


export interface PagedCollection {
    pageNumber?: number;
    pageRowCount?: number;
    totalRowCount?: number;
    totalPageCount?: number;
    collection?: Array<any>;
}

export interface RelLink {
  rel?: string;
  href?: string;
  method?: string;
  etag?: string;
  type: string;
}

export interface UwContractsList extends PagedCollection {
  collection: Array<UwContract>;
}

export interface UwContract {
  links?: Array<RelLink>;
  contractId?: number;
  contractNumber?: string;
  cropYear?: number;
  insurancePlanId?: number;
  insurancePlanName?: string;
  policyId?: number;
  policyNumber?: string;
  policyStatusCode?: string;
  policyStatus?: string;
  growerContractYearId?: number;
  growerId?: number;
  growerNumber?: number;
  growerName?: string;
  growerAddressLine1?: string;
  growerAddressLine2?: string;
  growerPostalCode?: string;
  growerCity?: string;
  growerProvince?: string;
  growerPrimaryEmail?: string;
  growerPrimaryPhone?: string;
  inventoryContractGuid?: string;
  declaredYieldContractGuid?: string;
  totalDopEligibleInventory?: number;

  isSelectedForPrint?: boolean;
  linkedPolicies? : Array<UwContract>;
  
  etag?: string;
  type?: string;
}


export interface AnnualField {

  links?: Array<RelLink>;
  contractedFieldDetailId?: number;
  annualFieldDetailId?: number;
  fieldId?: number;
  legalLandId?: number;
  fieldLabel?: string;
  otherLegalDescription?: string;
  displayOrder?: number;
  cropYear?: number;
  landUpdateType?: string;
  transferFromGrowerContractYearId?: number;
  plantings?: Array<InventoryField>;
  dopYieldFieldGrainList?: Array<DopYieldFieldGrain>;
  dopYieldFieldForageList?: Array<DopYieldFieldForage>;
  uwComments?: Array<UnderwritingComment>;
  policies?: Array<PolicySimple>;
  etag?: string;
  type: string;

}

export interface InventoryContractList extends PagedCollection {
  collection: Array<InventoryContract>;
}

export interface InventoryContract {
  links?: Array<RelLink>;
  inventoryContractGuid?: string;
  contractId?: number;
  cropYear?: number;
  growerContractYearId?: number;
  insurancePlanId?: number;
  insurancePlanName?: string;
  unseededIntentionsSubmittedInd?: boolean;
  seededCropReportSubmittedInd?: boolean;
  fertilizerInd?: boolean;
  herbicideInd?: boolean;
  tilliageInd?: boolean;
  otherChangesInd?: boolean;
  otherChangesComment?: string;
  grainFromPrevYearInd?: boolean | null;
  invUpdateTimestamp?: string;
  invUpdateUser?: string;
  policyNumber?: string;
  growerNumber?: number;
  growerName?: string;
  commodities?: Array<InventoryContractCommodity>;
  inventoryCoverageTotalForages?: Array<InventoryCoverageTotalForage>;
  fields?: Array<AnnualField>;
  etag?: string;
  type: string;
}

export interface CropCommodityList {
  // links?: Array<RelLink>;
  collection?: Array<CropCommodity>;
  // etag?: string;
  // type: string;
}

export interface CropCommodity {
  links?: Array<RelLink>;
    cropCommodityId?: number;
    insurancePlanId?: number;
    commodityName?: string;
    shortLabel?: string;
    plantDurationTypeCode?: string;
    isInventoryCropInd?: boolean;
    isYieldCropInd?: boolean;
    isUnderwritingCropInd?: boolean;
    isProductInsurableInd?: boolean;
    isCropInsuranceEligibleInd?: boolean;
    isPlantInsuranceEligibleInd?: boolean;
    isOtherInsuranceEligibleInd?: boolean;
    yieldMeasUnitTypeCode?: string;
    yieldDecimalPrecision?: number;
    dataSyncTransDate?: string;
    inventoryTypeCode?: string;
    yieldTypeCode?: string;
    cropVariety?: Array<CropVariety>;
    etag?: string;
    type: string;
}

export interface CropVariety {
  cropVarietyId?: number;
  cropCommodityId?: number;
  varietyName?: string;
  dataSyncTransDate?: string;
  insurancePlanId?: number;
  isQuantityInsurableInd?: boolean;
  isUnseededInsurableInd?: boolean;
  isPlantInsurableInd?: boolean;
  isAwpEligibleInd?: boolean;
  isUnderseedingEligibleInd?: boolean;
  isGrainUnseededDefaultInd?: boolean;
  cropVarietyCommodityTypes?: Array<CropVarietyCommodityType>;
  cropVarietyPlantInsurabilities?: Array<CropVarietyPlantInsurability>;
}

export interface CropVarietyCommodityType {
  commodityTypeCode?: string;
  description?: string;
  cropVarietyId?: number;
  insurancePlanId?: number;
  fullCoverageDeadlineDate?: string;
  finalCoverageDeadlineDate?: string;
}

export interface CropVarietyPlantInsurability {
  cropVarietyId?: number;
  plantInsurabilityTypeCode?: string;
  description?: string;
}

export interface LegalLandList extends PagedCollection  {
  collection?: Array<LegalLand>;
}

export interface LegalLand {
  links?: Array<RelLink>;
  legalLandId?: number;
  primaryPropertyIdentifier?: string;
  primaryLandIdentifierTypeCode?: string;
  primaryReferenceTypeCode?: string;
  legalDescription?: string;
  legalShortDescription?: string;
  otherDescription?: string;
  activeFromCropYear?: number;
  activeToCropYear?: number;
  totalAcres?: number;
  transactionType?: string;
  riskAreas?: Array<LegalLandRiskArea>;
  fields?: Array<Field>;
  etag?: string;
  type: string;
}

// Risk Areas
export interface RiskArea {
  links?: Array<RelLink>;
  riskAreaId?: number;
  insurancePlanId?: number;
  riskAreaName?: string;
  description?: string;
  effectiveDate?: string;
  expiryDate?: string;
  legalLandId?: number;
  insurancePlanName?: string;
  etag?: string;
  type: string;
}

export interface RiskAreaList {
  collection?: Array<RiskArea>;
}

export interface LegalLandRiskArea {
  riskAreaId?: number;
  insurancePlanId?: number;
  riskAreaName?: string;
  description?: string;
  legalLandId?: number;
  insurancePlanName?: string;
  deletedByUserInd?: boolean;
}

export interface Field {
  links?: Array<RelLink>;
  fieldId?: number;
  fieldLabel?: string;
  activeFromCropYear?: number;
  activeToCropYear?: number;
  totalLegalLand?: number;
  transactionType?: string;
  etag?: string;
  type: string;
}
