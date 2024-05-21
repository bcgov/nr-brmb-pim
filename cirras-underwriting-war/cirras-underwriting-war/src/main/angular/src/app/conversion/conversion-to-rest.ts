
import {  AnnualFieldRsrc, InventoryContractRsrc, LegalLandRsrc } from "@cirras/cirras-underwriting-api";
import {
  AnnualField,
  InventoryContract,
  LegalLand
} from "./models";
import { etagFixer } from "../utils/etagFixer";

export function convertToInventoryContractRsrc(inventoryContract: InventoryContract): InventoryContractRsrc {
  let ret = {
    '@type': "InventoryContractRsrc",
      links: (inventoryContract.links) ? inventoryContract.links : null,
      inventoryContractGuid: inventoryContract.inventoryContractGuid ? inventoryContract.inventoryContractGuid : null,
      contractId: inventoryContract.contractId ? inventoryContract.contractId : null,
      cropYear: inventoryContract.cropYear ? inventoryContract.cropYear : null,
      growerContractYearId: inventoryContract.growerContractYearId ? inventoryContract.growerContractYearId : null,

      insurancePlanId: inventoryContract.insurancePlanId ? inventoryContract.insurancePlanId : null,
      insurancePlanName: inventoryContract.insurancePlanName ? inventoryContract.insurancePlanName : null,

      unseededIntentionsSubmittedInd: inventoryContract.unseededIntentionsSubmittedInd ? inventoryContract.unseededIntentionsSubmittedInd : false,
      seededCropReportSubmittedInd: inventoryContract.seededCropReportSubmittedInd ? inventoryContract.seededCropReportSubmittedInd : false,
      fertilizerInd: inventoryContract.fertilizerInd ? inventoryContract.fertilizerInd : false,
      herbicideInd: inventoryContract.herbicideInd ? inventoryContract.herbicideInd : false,
      tilliageInd: inventoryContract.tilliageInd ? inventoryContract.tilliageInd : false,
      otherChangesInd: inventoryContract.otherChangesInd ? inventoryContract.otherChangesInd : false,
      otherChangesComment: inventoryContract.otherChangesComment ? inventoryContract.otherChangesComment : null,
      grainFromPrevYearInd: inventoryContract.grainFromPrevYearInd ? inventoryContract.grainFromPrevYearInd : false,
      invUpdateTimestamp: inventoryContract.invUpdateTimestamp,
      invUpdateUser: inventoryContract.invUpdateUser,
        
      policyNumber: inventoryContract.policyNumber ? inventoryContract.policyNumber : null,
      growerNumber: inventoryContract.growerNumber ? inventoryContract.growerNumber : null,
      growerName: inventoryContract.growerName ? inventoryContract.growerName : null,
      commodities: inventoryContract.commodities ? inventoryContract.commodities : null, 
      inventoryCoverageTotalForages: inventoryContract.inventoryCoverageTotalForages ? inventoryContract.inventoryCoverageTotalForages : null,
      fields: inventoryContract.fields ? inventoryContract.fields.map(item => convertToAnnualFieldRsrc(item)) : [],
      etag: inventoryContract.etag,
      type:  "InventoryContractRsrc"
  };
  return ret;
}


export function convertToAnnualFieldRsrc(fld: AnnualField): AnnualFieldRsrc{
  let ret = {
    '@type': "AnnualFieldRsrc",
    links: fld.links ? fld.links : null,
    contractedFieldDetailId: fld.contractedFieldDetailId ? fld.contractedFieldDetailId : null,
    annualFieldDetailId: fld.annualFieldDetailId ? fld.annualFieldDetailId : null,
    fieldId: fld.fieldId,
    legalLandId: fld.legalLandId ? fld.legalLandId : null,
    fieldLabel: fld.fieldLabel ? fld.fieldLabel : null,
    otherLegalDescription: fld.otherLegalDescription ? fld.otherLegalDescription : null,
    displayOrder: fld.displayOrder,
    cropYear: fld.cropYear,
    landUpdateType: fld.landUpdateType ? fld.landUpdateType : null,
    plantings: fld.plantings,
    uwComments: fld.uwComments,
    policies: fld.policies,
    transferFromGrowerContractYearId: fld.transferFromGrowerContractYearId ? fld.transferFromGrowerContractYearId : null,
    etag: fld.etag,
    type: "AnnualFieldRsrc"
  };
  return ret;
}

export function convertToLegalLandRsrc(legalLand: LegalLand): LegalLandRsrc {   
  let ret = {
    '@type': "LegalLandRsrc",
    links:                          legalLand.links ? legalLand.links : null,
    legalLandId:                    legalLand.legalLandId ? legalLand.legalLandId : null,
    primaryPropertyIdentifier:      legalLand.primaryPropertyIdentifier ? legalLand.primaryPropertyIdentifier : null,
    primaryLandIdentifierTypeCode:  legalLand.primaryLandIdentifierTypeCode ? legalLand.primaryLandIdentifierTypeCode : null,
    primaryReferenceTypeCode:       legalLand.primaryReferenceTypeCode ? legalLand.primaryReferenceTypeCode : null,
    legalDescription:               legalLand.legalDescription,
    legalShortDescription:          legalLand.legalShortDescription,
    otherDescription:               legalLand.otherDescription,
    activeFromCropYear:             legalLand.activeFromCropYear,
    activeToCropYear:               legalLand.activeToCropYear,
    totalAcres:                     legalLand.totalAcres,
    riskAreas:                      legalLand.riskAreas,
    fields:                         legalLand.fields,
    transactionType:                legalLand.transactionType,
    etag:                           legalLand.etag,
    type: "LegalLandRsrc"
  }

  return ret
}