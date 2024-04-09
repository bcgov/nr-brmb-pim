
import { UnderwritingComment } from "@cirras/cirras-underwriting-api";
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
    growerContractYearId?: number;
    contractId?: number;
    cropYear?: number;
    declarationOfProductionDate?: string;
    dopUpdateTimestamp?: string;
    dopUpdateUser?: string;
    enteredYieldMeasUnitTypeCode?: string;
    defaultYieldMeasUnitTypeCode?: string;
    grainFromOtherSourceInd?: boolean;
    insurancePlanId?: number;
    fields?: Array<AnnualField>;
    dopYieldFieldRollupList?: Array<DopYieldFieldRollup>;
    dopYieldContractCommodities?: Array<DopYieldContractCommodity>;
    uwComments?: Array<UnderwritingComment>;
    etag?: string;
    type: string;
  }


  export interface DopYieldField {
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

export interface DopYieldFieldRollup {
    declaredYieldFieldRollupGuid?: string;
    declaredYieldContractGuid?: string;
    cropCommodityId?: number;
    isPedigreeInd?: boolean;
    estimatedYieldPerAcreTonnes?: number;
    estimatedYieldPerAcreBushels?: number;
    cropCommodityName?: string;
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
