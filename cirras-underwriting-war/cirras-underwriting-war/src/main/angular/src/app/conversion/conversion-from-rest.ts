import { HttpErrorResponse } from "@angular/common/http";
import {ERROR_TYPE, ErrorState} from "../store/application/application.state";
import {UUID} from "angular2-uuid";
import { 
  UwContractsList,
  UwContract,
  InventoryContract,
  CropCommodityList,
  CropCommodity,
  LegalLandList,
  LegalLand,
  InventoryContractList,
  RiskArea,
  RiskAreaList
} from "./models";

import { CropCommodityListRsrc, CropCommodityRsrc, InventoryContractListRsrc, InventoryContractRsrc, LegalLandListRsrc, LegalLandRsrc, RiskAreaListRsrc, RiskAreaRsrc, UwContractListRsrc, UwContractRsrc } from "@cirras/cirras-underwriting-api";

const EMPTY_ARRAY = [];

export function convertToErrorState(error: Error, resourceName?: string): ErrorState {
    if (!error) {
        return null;
    }
    if (error instanceof HttpErrorResponse || error.name == "HttpErrorResponse") {
        let err = error as HttpErrorResponse;
        if (err.status == 404) {
            return {
                uuid: UUID.UUID(),
                type: ERROR_TYPE.NOT_FOUND,
                status: err.status,
                statusText: err.statusText,
                message: resourceName ? `${resourceName} not found` : err.message,
                name: err.name,
                responseEtag: err.headers.get("ETag"),
            };
        }
        if (err.status == 412) {
            return {
                uuid: UUID.UUID(),
                type: ERROR_TYPE.FAILED_PRECONDITION,
                status: err.status,
                statusText: err.statusText,
                message: resourceName ? `${resourceName} has changed since last retrieve` : err.message,
                name: err.name,
                responseEtag: err.headers.get("ETag"),
            };
        }

        if (err.status >= 500 || err.status == 0) {
            return {
                uuid: UUID.UUID(),
                type: ERROR_TYPE.FATAL,
                status: err.status,
                statusText: err.statusText,
                message: resourceName ? `Unexpected error performing operation on ${resourceName}` : err.message,
                name: err.name,
                responseEtag: undefined,
            };
        }
        return {            
            uuid: UUID.UUID(),
            type: err.status == 400 ? ERROR_TYPE.VALIDATION : ERROR_TYPE.FATAL,
            status: err.status,
            statusText: err.statusText,
            message: err.status == 400 ? "Validation Error" : err.message,
            name: err.name,
            validationErrors: err.error.messages,
            responseEtag: err.headers.get("ETag"),
        };
    } else {
        throw error;
    }

}

export function convertToUwContractsList(uwContractsListRes: UwContractListRsrc): UwContractsList {
    if (!uwContractsListRes) {
        return {collection: EMPTY_ARRAY};
    }
    return {
        collection: uwContractsListRes.collection && uwContractsListRes.collection.length > 0 ? uwContractsListRes.collection.map(item => convertToUwContract(item)) : EMPTY_ARRAY,
        pageNumber: uwContractsListRes.pageNumber ? uwContractsListRes.pageNumber : null,
        pageRowCount: uwContractsListRes.pageRowCount ? uwContractsListRes.pageRowCount : null,
        totalPageCount: uwContractsListRes.totalPageCount ? uwContractsListRes.totalPageCount : null,
        totalRowCount: uwContractsListRes.totalRowCount ? uwContractsListRes.totalRowCount : null
    };
  }

export function convertToUwContract(uwContract: any): UwContract {
    // linkedPolicies does not exist in uwContractRsrc type but the api is populating it
    // something is wrong with ng-lib and/or swagger
    // PIM-1141

    let ret: UwContract = {
  
      links: uwContract.links ? uwContract.links : null,      
      contractId: uwContract.contractId,
      contractNumber: uwContract.contractNumber,
      cropYear: uwContract.cropYear,
      insurancePlanId: uwContract.insurancePlanId,
      insurancePlanName: uwContract.insurancePlanName,

      policyId: uwContract.policyId,
      policyNumber: uwContract.policyNumber,
      policyStatusCode: uwContract.policyStatusCode,
      policyStatus: uwContract.policyStatus,
      
      growerContractYearId: uwContract.growerContractYearId,

      growerId: uwContract.growerId,
      growerNumber:uwContract.growerNumber,
      growerName: uwContract.growerName,
      growerAddressLine1: uwContract.growerAddressLine1,
      growerAddressLine2: uwContract.growerAddressLine2,
      growerPostalCode: uwContract.growerPostalCode,
      growerCity: uwContract.growerCity,
      growerProvince: uwContract.growerProvince,
      growerPrimaryEmail: uwContract.growerPrimaryEmail,
      growerPrimaryPhone: uwContract.growerPrimaryPhone,
      
      inventoryContractGuid: uwContract.inventoryContractGuid,
      declaredYieldContractGuid: uwContract.declaredYieldContractGuid,
      totalDopEligibleInventory: uwContract.totalDopEligibleInventory,

      linkedPolicies: uwContract.linkedPolicies,

      isSelectedForPrint: false,
       
      etag: uwContract.etag,
      type: uwContract.type         
    };  
    return ret;
  }


  export function convertToInventoryContract(invContractRsrc: InventoryContractRsrc, etag?: string): InventoryContract {
    let invContract = <InventoryContractRsrc>invContractRsrc;
  
    let ret: InventoryContract = {
  
      links: invContract.links ? invContract.links : null,
      
      inventoryContractGuid: invContract.inventoryContractGuid,
      contractId: invContract.contractId,
      cropYear: invContract.cropYear,
      growerContractYearId: invContract.growerContractYearId,
      insurancePlanId: invContract.insurancePlanId,
      insurancePlanName: invContract.insurancePlanName,
      unseededIntentionsSubmittedInd: invContract.unseededIntentionsSubmittedInd,
      seededCropReportSubmittedInd: invContract.seededCropReportSubmittedInd,

      fertilizerInd: invContract.fertilizerInd,
      herbicideInd: invContract.herbicideInd,
      tilliageInd: invContract.tilliageInd,
      otherChangesInd: invContract.otherChangesInd,
      otherChangesComment: invContract.otherChangesComment,

      grainFromPrevYearInd: invContract.grainFromPrevYearInd,

      invUpdateTimestamp: invContract.invUpdateTimestamp,
      invUpdateUser: invContract.invUpdateUser,
      
      policyNumber: invContract.policyNumber,
      growerNumber: invContract.growerNumber,
      growerName: invContract.growerName,

      commodities: invContract.commodities,
      inventoryCoverageTotalForages: invContract.inventoryCoverageTotalForages,
      fields: invContract.fields,

      etag: etag, //invContract.etag,
      type: invContract.type         
    };  
    return ret;
  }
  

  
export function convertToCropCommodityList(CropCommodityListRes: CropCommodityListRsrc): CropCommodityList {
    if (!CropCommodityListRes) {
        return {collection: EMPTY_ARRAY};
    }
    return {
        collection: CropCommodityListRes.collection && CropCommodityListRes.collection.length > 0 ? CropCommodityListRes.collection.map(item => convertToCropCommodity(item)) : EMPTY_ARRAY,
        // pageNumber: CropCommodityListRes.pageNumber ? CropCommodityListRes.pageNumber : null,
        // pageRowCount: CropCommodityListRes.pageRowCount ? CropCommodityListRes.pageRowCount : null,
        // totalPageCount: CropCommodityListRes.totalPageCount ? CropCommodityListRes.totalPageCount : null,
        // totalRowCount: CropCommodityListRes.totalRowCount ? CropCommodityListRes.totalRowCount : null
    };
  }

export function convertToCropCommodity(cropCommodityRes: CropCommodityRsrc): CropCommodity {
    let cropCommodity = <CropCommodityRsrc>cropCommodityRes;
  
    let ret: CropCommodity = {
  
      links: cropCommodity.links ? cropCommodity.links : null,      
      cropCommodityId: cropCommodity.cropCommodityId ? cropCommodity.cropCommodityId : null, 
      insurancePlanId: cropCommodity.insurancePlanId ? cropCommodity.insurancePlanId : null, 
      commodityName: cropCommodity.commodityName ? cropCommodity.commodityName : null, 
      shortLabel: cropCommodity.shortLabel ? cropCommodity.shortLabel : null, 
      plantDurationTypeCode: cropCommodity.plantDurationTypeCode ? cropCommodity.plantDurationTypeCode : null, 
      isInventoryCropInd: cropCommodity.isInventoryCropInd ? cropCommodity.isInventoryCropInd : null, 
      isYieldCropInd: cropCommodity.isYieldCropInd ? cropCommodity.isYieldCropInd : null,
      isUnderwritingCropInd: cropCommodity.isUnderwritingCropInd ? cropCommodity.isUnderwritingCropInd : null,
      isProductInsurableInd: cropCommodity.isProductInsurableInd ? cropCommodity.isProductInsurableInd : null,
      isCropInsuranceEligibleInd: cropCommodity.isCropInsuranceEligibleInd ? cropCommodity.isCropInsuranceEligibleInd : null,
      isPlantInsuranceEligibleInd: cropCommodity.isPlantInsuranceEligibleInd ? cropCommodity.isPlantInsuranceEligibleInd : null,
      isOtherInsuranceEligibleInd: cropCommodity.isOtherInsuranceEligibleInd ? cropCommodity.isOtherInsuranceEligibleInd : null,
      yieldMeasUnitTypeCode: cropCommodity.yieldMeasUnitTypeCode ? cropCommodity.yieldMeasUnitTypeCode : null,
      yieldDecimalPrecision: cropCommodity.yieldDecimalPrecision ? cropCommodity.yieldDecimalPrecision : null,
      dataSyncTransDate: cropCommodity.dataSyncTransDate ? cropCommodity.dataSyncTransDate : null,
      inventoryTypeCode: cropCommodity.inventoryTypeCode ? cropCommodity.inventoryTypeCode : null,
      yieldTypeCode: cropCommodity.yieldTypeCode ? cropCommodity.yieldTypeCode : null,

      cropVariety: cropCommodity.cropVariety ? cropCommodity.cropVariety : [],

      etag: cropCommodity.etag,
      type: cropCommodity.type         
    };  
    return ret;
  }



  export function convertToLegalLandList(LegalLandListRes: LegalLandListRsrc): LegalLandList {
    if (!LegalLandListRes) {
        return {collection: EMPTY_ARRAY};
    }
    return {
        collection: LegalLandListRes.collection && LegalLandListRes.collection.length > 0 ? LegalLandListRes.collection.map(item => convertToLegalLand(item, "")) : EMPTY_ARRAY,
        pageNumber: LegalLandListRes.pageNumber ? LegalLandListRes.pageNumber : null,
        pageRowCount: LegalLandListRes.pageRowCount ? LegalLandListRes.pageRowCount : null,
        totalPageCount: LegalLandListRes.totalPageCount ? LegalLandListRes.totalPageCount : null,
        totalRowCount: LegalLandListRes.totalRowCount ? LegalLandListRes.totalRowCount : null
    };
  }

export function convertToLegalLand(legalLandListRes: LegalLandRsrc, etag?: string): LegalLand {
    let legalLand = <LegalLandRsrc>legalLandListRes;
  
    let ret: LegalLand = {
      links: legalLand.links ? legalLand.links : null,  
      legalLandId:                  legalLand.legalLandId ? legalLand.legalLandId : null,
      primaryPropertyIdentifier:    legalLand.primaryPropertyIdentifier ? legalLand.primaryPropertyIdentifier : null,
      primaryReferenceTypeCode:     legalLand.primaryReferenceTypeCode ? legalLand.primaryReferenceTypeCode : null,
      primaryLandIdentifierTypeCode: legalLand.primaryLandIdentifierTypeCode ? legalLand.primaryLandIdentifierTypeCode : null,
      legalDescription:             legalLand.legalDescription ? legalLand.legalDescription : null,
      legalShortDescription:        legalLand.legalShortDescription ? legalLand.legalShortDescription : null,
      otherDescription:             legalLand.otherDescription ? legalLand.otherDescription : null,
      activeFromCropYear:           legalLand.activeFromCropYear ? legalLand.activeFromCropYear : null,
      activeToCropYear:             legalLand.activeToCropYear ? legalLand.activeToCropYear : null,
      riskAreas:                    legalLand.riskAreas ? legalLand.riskAreas : [],
      fields:                       legalLand.fields ? legalLand.fields : [],
      etag: etag,
      type: legalLand.type         
    };  
    return ret;
  }


  export function convertToInventoryContractList(inventoryContractListRes: InventoryContractListRsrc): InventoryContractList {
    if (!inventoryContractListRes) {
        return {collection: EMPTY_ARRAY};
    }
    return {
        collection: inventoryContractListRes.collection && inventoryContractListRes.collection.length > 0 ? inventoryContractListRes.collection.map(item => convertToInventoryContract(item)) : EMPTY_ARRAY,
        pageNumber: inventoryContractListRes.pageNumber ? inventoryContractListRes.pageNumber : null,
        pageRowCount: inventoryContractListRes.pageRowCount ? inventoryContractListRes.pageRowCount : null,
        totalPageCount: inventoryContractListRes.totalPageCount ? inventoryContractListRes.totalPageCount : null,
        totalRowCount: inventoryContractListRes.totalRowCount ? inventoryContractListRes.totalRowCount : null
    };
  }


  // Risk Area List
  export function convertToRiskAreaList(riskAreaListRes: RiskAreaListRsrc): RiskAreaList {
    if (!riskAreaListRes) {
        return {collection: EMPTY_ARRAY};
    }
    return {
        collection: riskAreaListRes.collection && riskAreaListRes.collection.length > 0 ? riskAreaListRes.collection.map(item => convertToRiskArea(item, "")) : EMPTY_ARRAY,
    };
  }

  export function convertToRiskArea(riskAreaRes: RiskAreaRsrc, etag?: string): RiskArea {
    let riskArea = <RiskAreaRsrc>riskAreaRes;
  
    let ret: RiskArea = {
      links:                riskArea.links,  
      riskAreaId:           riskArea.riskAreaId,
      insurancePlanId:      riskArea.insurancePlanId,
      riskAreaName:         riskArea.riskAreaName,
      description:          riskArea.description,

      effectiveDate:        riskArea.effectiveDate,
      expiryDate:           riskArea.expiryDate,

      legalLandId:          riskArea.legalLandId,
      insurancePlanName:    riskArea.insurancePlanName,
      
      etag: etag,
      type: riskArea.type         
    };  
    return ret;
  }
