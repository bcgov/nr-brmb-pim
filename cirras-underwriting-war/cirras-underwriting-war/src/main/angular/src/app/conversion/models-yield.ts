
import { MessageRsrc, UnderwritingComment } from "@cirras/cirras-underwriting-api";
import { AnnualField, RelLink } from "./models";

export interface YieldMeasUnitTypeCodeList {
    // links?: Array<RelLink>;
    collection?: Array<YieldMeasUnitTypeCode>;
    // etag?: string;
    // type: string;
}

export interface YieldMeasUnitTypeCode {
    // links?: Array<RelLink>;
    yieldMeasUnitTypeCode?: string;
    description?: string;
    decimalPrecision?: number;
    effectiveDate?: string;
    expiryDate?: string;
    isDefaultYieldUnitInd?: boolean;
    insurancePlanId?: number;
    // etag?: string;
    // type: string;

}

export interface DopYieldContract {
    links?: Array<RelLink>;
    declaredYieldContractGuid?: string;
    contractId?: number;
    cropYear?: number;
    declarationOfProductionDate?: string;
    dopUpdateTimestamp?: string;
    dopUpdateUser?: string;
    enteredYieldMeasUnitTypeCode?: string;
    defaultYieldMeasUnitTypeCode?: string;
    grainFromOtherSourceInd?: boolean;
    balerWagonInfo?: string;
    totalLivestock?: number;
    insurancePlanId?: number;
    growerContractYearId?: number;
    fields?: Array<AnnualField>;
    uwComments?: Array<UnderwritingComment>;
    dopYieldFieldRollupList?: Array<DopYieldFieldRollup>;
    dopYieldContractCommodities?: Array<DopYieldContractCommodity>;
    dopYieldContractCommodityForageList?: Array<DopYieldContractCommodityForage>;
    dopYieldFieldRollupForageList?: Array<DopYieldFieldRollupForage>;
    etag?: string;
    type: string;
  }


export interface DopYieldFieldRollup {
    declaredYieldFieldRollupGuid?: string;
    declaredYieldContractGuid?: string;
    cropCommodityId?: number;
    isPedigreeInd?: boolean;
    estimatedYieldPerAcreTonnes?: number;
    estimatedYieldPerAcreBushels?: number;
    cropCommodityName?: string;
}

export interface DopYieldFieldRollupForage {
	declaredYieldFieldRollupForageGuid?: string;
	declaredYieldContractGuid?: string;
	commodityTypeCode?: string;
	totalFieldAcres?: number;
	totalBalesLoads?: number;
	harvestedAcres?: number;
	quantityHarvestedTons?: number;
	yieldPerAcre?: number;
	commodityTypeDescription?: string;
}

export interface DopYieldContractCommodity {
    declaredYieldContractCommodityGuid?: string;
    declaredYieldContractGuid?: string;
    cropCommodityId?: number;
    isPedigreeInd?: boolean;
    harvestedAcres?: number;
    storedYield?: number;
    storedYieldDefaultUnit?: number;
    soldYield?: number;
    soldYieldDefaultUnit?: number;
    gradeModifierTypeCode?: string;
    cropCommodityName?: string;
    totalInsuredAcres?: number;
}


export interface GradeModifierList {
    links?: Array<RelLink>;
    collection?: Array<GradeModifier>;
    etag?: string;
    type: string;
}

export interface GradeModifier {
    links?: Array<RelLink>;
    gradeModifierGuid?: string;
    cropCommodityId?: number;
    cropYear?: number;
    gradeModifierTypeCode?: string;
    gradeModifierValue?: number;
    description?: string;
    insurancePlanId?: number;
    etag?: string;
    type: string;
}

export interface DopYieldFieldGrain {
    declaredYieldFieldGuid?: string;
    inventoryFieldGuid?: string;
    estimatedYieldPerAcre?: number;
    estimatedYieldPerAcreDefaultUnit?: number;
    unharvestedAcresInd?: boolean;
    inventorySeededGrainGuid?: string;
    cropCommodityId?: number;
    cropCommodityName?: string;
    cropVarietyId?: number;
    cropVarietyName?: string;
    isPedigreeInd?: boolean;
    seededDate?: string;
    seededAcres?: number;
    insurancePlanId?: number;
    fieldId?: number;
    cropYear?: number;
}

export interface DopYieldFieldForage {
    inventoryFieldGuid?: string;
    commodityTypeCode?: string;
    commodityTypeDescription?: string;
    isQuantityInsurableInd?: boolean;
    fieldAcres?: number;
    cropVarietyName?: string;
    cropVarietyId?: number;
    cropCommodityId?: number;
    plantDurationTypeCode?: string;
    insurancePlanId?: number;
    fieldId?: number;
    cropYear?: number;
    isHiddenOnPrintoutInd?: boolean;
    plantInsurabilityTypeCode?: string;
    seedingYear?: number;
    seedingDate?: string;
    dopYieldFieldForageCuts?: Array<DopYieldFieldForageCut>;
}

export interface DopYieldFieldForageCut {
    declaredYieldFieldForageGuid?: string;
    inventoryFieldGuid?: string;
    cutNumber?: number;
    totalBalesLoads?: number;
    weight?: number;
    weightDefaultUnit?: number;
    moisturePercent?: number;
    deletedByUserInd?: boolean;
}

export interface DopYieldContractCommodityForage {
    declaredYieldContractCmdtyForageGuid?: string;
    declaredYieldContractGuid?: string;
    commodityTypeCode?: string;
    totalFieldAcres?: number;
    harvestedAcres?: number;
    totalBalesLoads?: number;
    weight?: number;
    weightDefaultUnit?: number;
    moisturePercent?: number;
    quantityHarvestedTons?: number;
    yieldPerAcre?: number;
    commodityTypeDescription?: string;
    cropCommodityId?: number;
    plantDurationTypeCode?: string;
}


export interface VerifiedYieldContract {
    links?: Array<RelLink>;
    verifiedYieldContractGuid?: string;
    declaredYieldContractGuid?: string;
    contractId?: number;
    cropYear?: number;
    verifiedYieldUpdateTimestamp?: string;
    verifiedYieldUpdateUser?: string;
    defaultYieldMeasUnitTypeCode?: string;
    insurancePlanId?: number;
    growerContractYearId?: number;
    updateProductValuesInd?: boolean;
    fields?: Array<AnnualField>;
    verifiedYieldContractCommodities?: Array<VerifiedYieldContractCommodity>;
    verifiedYieldAmendments?: Array<VerifiedYieldAmendment>;
    verifiedYieldSummaries?: Array<VerifiedYieldSummary>;
    verifiedYieldGrainBasket?: VerifiedYieldGrainBasket;
    productWarningMessages?: Array<MessageRsrc>;
    etag?: string;
    type: string;
  }

  export interface VerifiedYieldContractCommodity {
    verifiedYieldContractCommodityGuid?: string;
    verifiedYieldContractGuid?: string;
    cropCommodityId?: number;
    commodityTypeCode?: string;
    isPedigreeInd?: boolean;
    harvestedAcres?: number;
    harvestedAcresOverride?: number;
    storedYieldDefaultUnit?: number;
    soldYieldDefaultUnit?: number;
    productionGuarantee?: number;
    harvestedYield?: number;
    harvestedYieldOverride?: number;
    yieldPerAcre?: number;
    cropCommodityName?: string;
    totalInsuredAcres?: number;
    commodityTypeDescription?: string;
    displayName?: string;
    isRolledupInd?: boolean;
  }

  export interface VerifiedYieldAmendment {
    verifiedYieldAmendmentGuid?: string;
    verifiedYieldAmendmentCode?: string;
    verifiedYieldContractGuid?: string;
    cropCommodityId?: number;
    cropVarietyId?: number;
    isPedigreeInd?: boolean;
    fieldId?: number;
    yieldPerAcre?: number;
    acres?: number;
    rationale?: string;
    cropCommodityName?: string;
    cropVarietyName?: string;
    fieldLabel?: string;
    deletedByUserInd?: boolean;
}

  export interface VerifiableCommodity  {
    cropCommodityId?: number;
    cropCommodityName?: string;
    isPedigreeInd?: boolean;
  }

  export interface VerifiableVariety {
    cropVarietyId?: number;
    cropVarietyName?: string;
  }

  export interface VerifiedYieldSummary {
    verifiedYieldSummaryGuid?: string;
    verifiedYieldContractGuid?: string;
    cropCommodityId?: number;
    isPedigreeInd?: boolean;
    harvestedYield?: number;
    harvestedYieldPerAcre?: number;
    appraisedYield?: number;
    assessedYield?: number;
    yieldToCount?: number;
    yieldPercentPy?: number;
    productionGuarantee?: number;
    probableYield?: number;
    cropCommodityName?: string;
    totalInsuredAcres?: number;
    uwComments?: Array<UnderwritingComment>;
}

export interface VerifiedYieldGrainBasket {
    verifiedYieldGrainBasketGuid?: string;
    verifiedYieldContractGuid?: string;
    basketValue?: number;
    harvestedValue?: number;
    comment?: string;
}
