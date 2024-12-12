import { DopYieldContractRsrc, VerifiedYieldContractRsrc, YieldMeasUnitTypeCodeListRsrc, YieldMeasUnitTypeCodeRsrc } from "@cirras/cirras-underwriting-api";
import { DopYieldContract, VerifiedYieldContract, YieldMeasUnitTypeCode, YieldMeasUnitTypeCodeList } from "./models-yield";

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
      balerWagonInfo: dopContract.balerWagonInfo,
      totalLivestock: dopContract.totalLivestock,
      insurancePlanId: dopContract.insurancePlanId,
      
      fields: dopContract.fields,
      dopYieldFieldRollupList: dopContract.dopYieldFieldRollupList,
      dopYieldContractCommodities: dopContract.dopYieldContractCommodities,
      dopYieldFieldRollupForageList: dopContract.dopYieldFieldRollupForageList,
      dopYieldContractCommodityForageList: dopContract.dopYieldContractCommodityForageList,
      uwComments: dopContract.uwComments,

      etag: etag, 
      type: dopContract.type         
    };  
    return ret;
  }

  
  export function convertToVerifiedYieldContract(verifiedYieldContractRsrc: VerifiedYieldContractRsrc, etag?: string): VerifiedYieldContract {
    let verifiedYieldContract = <VerifiedYieldContractRsrc>verifiedYieldContractRsrc;
  
    let ret: VerifiedYieldContract = {
  
      links: verifiedYieldContract.links ? verifiedYieldContract.links : null,
      
      verifiedYieldContractGuid: verifiedYieldContract.verifiedYieldContractGuid,
      declaredYieldContractGuid: verifiedYieldContract.declaredYieldContractGuid,
      contractId: verifiedYieldContract.contractId,
      cropYear: verifiedYieldContract.cropYear,
      verifiedYieldUpdateTimestamp:  verifiedYieldContract.verifiedYieldUpdateTimestamp,
      verifiedYieldUpdateUser: verifiedYieldContract.verifiedYieldUpdateUser,
      defaultYieldMeasUnitTypeCode: verifiedYieldContract.defaultYieldMeasUnitTypeCode,
      insurancePlanId: verifiedYieldContract.insurancePlanId,
      growerContractYearId: verifiedYieldContract.growerContractYearId,

      fields: verifiedYieldContract.fields,
      verifiedYieldContractCommodities: verifiedYieldContract.verifiedYieldContractCommodities,
      verifiedYieldAmendments: verifiedYieldContract.verifiedYieldAmendments,
      
      etag: etag, 
      type: verifiedYieldContract.type   
    };  
    return ret;
  }