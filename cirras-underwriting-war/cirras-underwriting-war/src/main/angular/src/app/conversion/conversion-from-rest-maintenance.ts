import { CropVarietyInsurabilityListRsrc, GradeModifierListRsrc, GradeModifierRsrc, GradeModifierTypeListRsrc, SeedingDeadlineListRsrc, UnderwritingYearListRsrc, YieldMeasUnitConversionListRsrc } from "@cirras/cirras-underwriting-api";
import { CropVarietyInsurabilityList, GradeModifier, GradeModifierList, GradeModifierTypeList, SeedingDeadlineList, UnderwritingYear, UnderwritingYearList, YieldMeasUnitConversionList } from "./models-maintenance";

export function convertToUnderwritingYearList(uwYearListRes: UnderwritingYearListRsrc): UnderwritingYearList {
    if (!uwYearListRes) {
        return {  collection: [] };
    }
    return {
        collection: uwYearListRes.collection && uwYearListRes.collection.length > 0 ? uwYearListRes.collection.map(item => convertToUnderwritingYear(item)) : [],
    };
  }

export function convertToUnderwritingYear(uwYear: any, etag?: string): UnderwritingYear {

    let ret: UnderwritingYear = {
  
      links: uwYear.links ? uwYear.links : null,      
      underwritingYearGuid: uwYear.underwritingYearGuid,
      cropYear: uwYear.cropYear,
      etag: etag,
      type: uwYear.type         
    };  
    return ret;
  }

  export function convertToSeedingDeadlinesList(sdListRes: SeedingDeadlineListRsrc, etag: string): SeedingDeadlineList {
    if (!sdListRes) {
        return {  
            links: [],
            collection: [] ,
            etag: etag,
            type: "http://underwriting.cirras.mal.gov.bc.ca/v1/SeedingDeadlineList"
        };
    }
    return {
        links: sdListRes.links,
        collection: sdListRes.collection && sdListRes.collection.length > 0 ? sdListRes.collection : [],
        etag: etag,
        type: sdListRes['@type']
    };
  }

  export function convertToGradeModifierTypesList(sdListRes: GradeModifierTypeListRsrc, etag: string): GradeModifierTypeList {
    if (!sdListRes) {
        return {  
            links: [],
            collection: [] ,
            etag: etag,
            type: "http://underwriting.cirras.mal.gov.bc.ca/v1/GradeModifierTypeList"
        };
    }
    return {
        links: sdListRes.links,
        collection: sdListRes.collection && sdListRes.collection.length > 0 ? sdListRes.collection : [],
        etag: etag,
        type: sdListRes['@type']
    };
  }

  export function convertToGradeModifierList(gradeModifierListRes: GradeModifierListRsrc, etag: string, cropYear: number): GradeModifierList {
    if (!gradeModifierListRes) {
        return {  
          links: [],
          collection: [],
          etag: etag,
          type: "http://underwriting.cirras.mal.gov.bc.ca/v1/GradeModifierList",
          cropYear: cropYear
        };
    }
    return {
        links: gradeModifierListRes.links,
        collection: gradeModifierListRes.collection && gradeModifierListRes.collection.length > 0 ? gradeModifierListRes.collection.map(item => convertToGradeModifier(item)) : [],
        etag: etag,
        type: gradeModifierListRes['@type'],
        cropYear: cropYear
      };
  }


  export function convertToGradeModifier(gradeModifierRes: GradeModifierRsrc): GradeModifier {

    let ret: GradeModifier = {
  
      links: gradeModifierRes.links ? gradeModifierRes.links : null,
      gradeModifierGuid: gradeModifierRes.gradeModifierGuid,
      cropCommodityId: gradeModifierRes.cropCommodityId,
      cropYear: gradeModifierRes.cropYear,
      gradeModifierTypeCode: gradeModifierRes.gradeModifierTypeCode,
      gradeModifierValue: gradeModifierRes.gradeModifierValue,
      description: gradeModifierRes.description,
      insurancePlanId: gradeModifierRes.insurancePlanId,
      deleteAllowedInd: gradeModifierRes.deleteAllowedInd,
      deletedByUserInd: gradeModifierRes.deletedByUserInd,
      etag: gradeModifierRes.etag,
      type: gradeModifierRes['@type']
    };  

    return ret;
  }

  export function convertToCropVarietyInsurabilityList(cviListRes: CropVarietyInsurabilityListRsrc, etag: string): CropVarietyInsurabilityList {
    if (!cviListRes) {
        return {  
            links: [],
            collection: [] ,
            etag: etag,
            type: "http://underwriting.cirras.mal.gov.bc.ca/v1/CropVarietyInsurabilityList"
        };
    }
    return {
        links: cviListRes.links,
        collection: cviListRes.collection && cviListRes.collection.length > 0 ? cviListRes.collection : [],
        etag: etag,
        type: cviListRes['@type']
    };
  }

  export function convertToYieldConversionList(ycListRes: YieldMeasUnitConversionListRsrc, etag: string): YieldMeasUnitConversionList {
    if (!ycListRes) {
        return {  
            links: [],
            collection: [] ,
            etag: etag,
            type: "http://underwriting.cirras.mal.gov.bc.ca/v1/YieldMeasUnitConversionList"
        };
    }
    return {
        links: ycListRes.links,
        collection: ycListRes.collection && ycListRes.collection.length > 0 ? ycListRes.collection : [],
        etag: etag,
        type: ycListRes['@type']
    };
  }
