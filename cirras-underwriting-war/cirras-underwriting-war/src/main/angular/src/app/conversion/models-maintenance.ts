import { CropVarietyPlantInsurability, RelLink } from "./models";


export interface UnderwritingYearList {
    links?: Array<RelLink>;
    collection?: Array<UnderwritingYear>;
    //etag?: string;
    //type: string;
}

export interface UnderwritingYear {
    links?: Array<RelLink>;
    underwritingYearGuid?: string;
    cropYear?: number;
    etag?: string;
    type: string;
}


export interface SeedingDeadlineList {
    links?: Array<RelLink>;
    collection?: Array<SeedingDeadline>;
    etag?: string;
    type: string;
}

export interface SeedingDeadline {
    seedingDeadlineGuid?: string;
    commodityTypeCode?: string;
    cropYear?: number;
    fullCoverageDeadlineDate?: string;
    finalCoverageDeadlineDate?: string;
    fullCoverageDeadlineDateDefault?: string;
    finalCoverageDeadlineDateDefault?: string;
    deletedByUserInd?: boolean;
}

export interface GradeModifierTypeList {
    links?: Array<RelLink>;
    collection?: Array<GradeModifierType>;
    etag?: string;
    type: string;
}

export interface GradeModifierType {
    gradeModifierTypeCode?: string;
    description?: string;
    effectiveYear?: number;
    expiryYear?: number;
    deleteAllowedInd?: boolean;
    deletedByUserInd?: boolean;
    maxYearUsed?: number;
}


export interface GradeModifierList {
    links?: Array<RelLink>;
    collection?: Array<GradeModifier>;
    etag?: string;
    type: string;

    //Extended fields
    cropYear: number;
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
    deleteAllowedInd?: boolean;
    deletedByUserInd?: boolean;
    etag?: string;
    type: string;
}

export interface CropVarietyInsurabilityList {
    links?: Array<RelLink>;
    collection?: Array<CropVarietyInsurability>;
    etag?: string;
    type: string;
}


export interface CropVarietyInsurability {
    cropVarietyId?: number;
    varietyName?: string;
    plantDurationTypeCode?: string;
    cropVarietyInsurabilityGuid?: string;
    isQuantityInsurableInd?: boolean;
    isUnseededInsurableInd?: boolean;
    isPlantInsurableInd?: boolean;
    isAwpEligibleInd?: boolean;
    isUnderseedingEligibleInd?: boolean;
    deletedByUserInd?: boolean;
    isQuantityInsurableEditableInd?: boolean;
    isUnseededInsurableEditableInd?: boolean;
    isPlantInsurableEditableInd?: boolean;
    isAwpEligibleEditableInd?: boolean;
    isUnderseedingEligibleEditableInd?: boolean;
    cropVarietyPlantInsurabilities?: Array<CropVarietyPlantInsurability>;
}

export interface YieldMeasUnitConversionList {
    links?: Array<RelLink>;
    collection?: Array<YieldMeasUnitConversion>;
    etag?: string;
    type: string;
}

export interface YieldMeasUnitConversion {
    yieldMeasUnitConversionGuid?: string;
    cropCommodityId?: number;
    srcYieldMeasUnitTypeCode?: string;
    targetYieldMeasUnitTypeCode?: string;
    versionNumber?: number;
    effectiveCropYear?: number;
    expiryCropYear?: number;
    conversionFactor?: number;
    insurancePlanId?: number;
    commodityName?: string;
}