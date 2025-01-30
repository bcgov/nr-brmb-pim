import { DopYieldContractRsrc, VerifiedYieldContractRsrc } from "@cirras/cirras-underwriting-api";
import { DopYieldContract, VerifiedYieldContract } from "./models-yield";


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
        dopYieldFieldRollupForageList: dopYieldContract.dopYieldFieldRollupForageList,
        dopYieldContractCommodityForageList: dopYieldContract.dopYieldContractCommodityForageList,
        uwComments: dopYieldContract.uwComments,
        etag: dopYieldContract.etag,
        type:  "DopYieldContractRsrc"
    };
    return ret;
  }

  export function convertToVerifiedYieldContractRsrc(verifiedYieldContract: VerifiedYieldContract): VerifiedYieldContractRsrc {
    let ret = {
      '@type': "VerifiedYieldContractRsrc",
        links: (verifiedYieldContract.links) ? verifiedYieldContract.links : null,
        verifiedYieldContractGuid: verifiedYieldContract.verifiedYieldContractGuid ? verifiedYieldContract.verifiedYieldContractGuid : null,
        declaredYieldContractGuid: verifiedYieldContract.declaredYieldContractGuid ? verifiedYieldContract.declaredYieldContractGuid : null,
        contractId: verifiedYieldContract.contractId,
        cropYear: verifiedYieldContract.cropYear,
        verifiedYieldUpdateTimestamp: verifiedYieldContract.verifiedYieldUpdateTimestamp,
        verifiedYieldUpdateUser: verifiedYieldContract.verifiedYieldUpdateUser,
        defaultYieldMeasUnitTypeCode: verifiedYieldContract.defaultYieldMeasUnitTypeCode,
        insurancePlanId: verifiedYieldContract.insurancePlanId,
        growerContractYearId: verifiedYieldContract.growerContractYearId,
        updateProductValuesInd: verifiedYieldContract.updateProductValuesInd,
        fields: verifiedYieldContract.fields,
        verifiedYieldContractCommodities: verifiedYieldContract.verifiedYieldContractCommodities,
        verifiedYieldAmendments: verifiedYieldContract.verifiedYieldAmendments,
        verifiedYieldSummaries: verifiedYieldContract.verifiedYieldSummaries,
        verifiedYieldGrainBasket: verifiedYieldContract.verifiedYieldGrainBasket,
        productWarningMessages: verifiedYieldContract.productWarningMessages,
        etag: verifiedYieldContract.etag,
        type:  "VerifiedYieldContractRsrc"     
    };
    return ret;
  }

  