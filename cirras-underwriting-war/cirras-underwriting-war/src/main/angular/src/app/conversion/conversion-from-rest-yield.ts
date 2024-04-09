import { DopYieldContractRsrc, YieldMeasUnitTypeCodeListRsrc, YieldMeasUnitTypeCodeRsrc } from "@cirras/cirras-underwriting-api";
import { DopYieldContract, YieldMeasUnitTypeCode, YieldMeasUnitTypeCodeList } from "./models-yield";

const EMPTY_ARRAY = [];

export function convertToYieldMeasUnitList(yieldMeasUnitListRes: YieldMeasUnitTypeCodeListRsrc): YieldMeasUnitTypeCodeList {
    if (!yieldMeasUnitListRes) {
        return {collection: EMPTY_ARRAY};
    }
    return {
        collection: yieldMeasUnitListRes.collection && yieldMeasUnitListRes.collection.length > 0 
            ? yieldMeasUnitListRes.collection.map(item => convertToYieldMeasUnit(item)) : EMPTY_ARRAY,
    };
  }

export function convertToYieldMeasUnit(yieldMeasUnitRes: YieldMeasUnitTypeCodeRsrc): YieldMeasUnitTypeCode {
    let yieldMeasUnit = <YieldMeasUnitTypeCodeRsrc>yieldMeasUnitRes;
  
    let ret: YieldMeasUnitTypeCode = {
  
        yieldMeasUnitTypeCode:  yieldMeasUnit.yieldMeasUnitTypeCode,
        description:            yieldMeasUnit.description,
        decimalPrecision:       yieldMeasUnit.decimalPrecision,
        effectiveDate:          yieldMeasUnit.effectiveDate,
        expiryDate:             yieldMeasUnit.expiryDate,
        isDefaultYieldUnitInd:  yieldMeasUnit.isDefaultYieldUnitInd,
        insurancePlanId:        yieldMeasUnit.insurancePlanId,        
    };  
    return ret;
  }



  export function convertToDopContract(dopContractRsrc: DopYieldContractRsrc, etag?: string): DopYieldContract {
    let dopContract = <DopYieldContractRsrc>dopContractRsrc;
  
    let ret: DopYieldContract = {
  
      links: dopContract.links ? dopContract.links : null,
      
      declaredYieldContractGuid: dopContract.declaredYieldContractGuid,
      growerContractYearId: dopContract.growerContractYearId,
      contractId: dopContract.contractId,
      cropYear: dopContract.cropYear,
      declarationOfProductionDate: dopContract.declarationOfProductionDate,
      
      dopUpdateTimestamp: dopContract.dopUpdateTimestamp,
      dopUpdateUser: dopContract.dopUpdateUser,

      enteredYieldMeasUnitTypeCode: dopContract.enteredYieldMeasUnitTypeCode,
      defaultYieldMeasUnitTypeCode: dopContract.defaultYieldMeasUnitTypeCode,

      grainFromOtherSourceInd: dopContract.grainFromOtherSourceInd,
      insurancePlanId: dopContract.insurancePlanId,
      
      fields: dopContract.fields,
      dopYieldFieldRollupList: dopContract.dopYieldFieldRollupList,
      dopYieldContractCommodities: dopContract.dopYieldContractCommodities,
      uwComments: dopContract.uwComments,

      etag: etag, 
      type: dopContract.type         
    };  
    return ret;
  }