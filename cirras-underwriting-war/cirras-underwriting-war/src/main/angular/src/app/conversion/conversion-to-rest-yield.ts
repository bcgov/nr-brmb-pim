import { DopYieldContractRsrc } from "@cirras/cirras-underwriting-api";
import { DopYieldContract } from "./models-yield";


export function convertToDopYieldContractRsrc(dopYieldContract: DopYieldContract): DopYieldContractRsrc {
    let ret = {
      '@type': "DopYieldContractRsrc",
        links: (dopYieldContract.links) ? dopYieldContract.links : null,
        declaredYieldContractGuid: dopYieldContract.declaredYieldContractGuid ? dopYieldContract.declaredYieldContractGuid : null,
        
        growerContractYearId: dopYieldContract.growerContractYearId,
        contractId: dopYieldContract.contractId,
        cropYear: dopYieldContract.cropYear,
        declarationOfProductionDate: dopYieldContract.declarationOfProductionDate,
        dopUpdateTimestamp: dopYieldContract.dopUpdateTimestamp,
        dopUpdateUser: dopYieldContract.dopUpdateUser,
        enteredYieldMeasUnitTypeCode: dopYieldContract.enteredYieldMeasUnitTypeCode,
        defaultYieldMeasUnitTypeCode: dopYieldContract.defaultYieldMeasUnitTypeCode,
        grainFromOtherSourceInd: dopYieldContract.grainFromOtherSourceInd,
        balerWagonInfo: dopYieldContract.balerWagonInfo,
        totalLivestock: dopYieldContract.totalLivestock,
        insurancePlanId: dopYieldContract.insurancePlanId,
        fields: dopYieldContract.fields,
        dopYieldFieldRollupList: dopYieldContract.dopYieldFieldRollupList,
        dopYieldContractCommodities: dopYieldContract.dopYieldContractCommodities,
        //dopYieldFieldRollupForageList: dopYieldContract.dopYieldFieldRollupForageList,
        dopYieldContractCommodityForageList: dopYieldContract.dopYieldContractCommodityForageList,
        uwComments: dopYieldContract.uwComments,
        etag: dopYieldContract.etag,
        type:  "DopYieldContractRsrc"
    };
    return ret;
  }

  