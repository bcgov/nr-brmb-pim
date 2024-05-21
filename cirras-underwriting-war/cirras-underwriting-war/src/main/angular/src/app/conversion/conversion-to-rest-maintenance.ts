import { CropVarietyInsurabilityListRsrc, GradeModifierListRsrc, GradeModifierRsrc, GradeModifierTypeListRsrc, SeedingDeadlineListRsrc, UnderwritingYearRsrc, YieldMeasUnitConversionListRsrc } from "@cirras/cirras-underwriting-api";
import { CropVarietyInsurability, CropVarietyInsurabilityList, GradeModifier, GradeModifierList, GradeModifierTypeList, SeedingDeadlineList, UnderwritingYear, YieldMeasUnitConversionList } from "./models-maintenance";
import { etagFixer } from "../utils/etagFixer";

export function convertToUnderwritingYearRsrc(uwYear: UnderwritingYear): UnderwritingYearRsrc {   
  let ret = {
    '@type': "UnderwritingYearRsrc",
    links: uwYear.links ? uwYear.links : null,
    underwritingYearGuid: uwYear.underwritingYearGuid ? uwYear.underwritingYearGuid : null,
    cropYear: uwYear.cropYear,
    etag: etagFixer(uwYear.etag),
    type: "UnderwritingYearRsrc"
  }

  return ret
}

export function convertToSeedingDeadlineListRsrc(sdList: SeedingDeadlineList): SeedingDeadlineListRsrc{
    
    let ret = {
        '@type': sdList.type , // just "SeedingDeadlineList" does not work. It needs to be set to "http://underwriting.cirras.mal.gov.bc.ca/v1/SeedingDeadlineList",
        links: null,
        collection: sdList.collection,
        etag: etagFixer(sdList.etag),
        type: sdList.type  
    }
    return ret
  }

  export function convertToGradeModifierTypeListRsrc(gmtList: GradeModifierTypeList): GradeModifierTypeListRsrc{
    
    let ret = {
        '@type': gmtList.type, 
        links: null,
        collection: gmtList.collection,
        etag: etagFixer(gmtList.etag),
        type: gmtList.type  
    }
    return ret
  }

  export function convertToGradeModifierListRsrc(gmList: GradeModifierList): GradeModifierListRsrc {
    
    let resType = gmList.type ? gmList.type : "http://underwriting.cirras.mal.gov.bc.ca/v1/GradeModifierList"

    let ret = {
        '@type': resType,
        links: null,
        collection: gmList.collection ? gmList.collection.map(item => convertToGradeModifierRsrc(item)) : [],
        etag: etagFixer(gmList.etag),
        type: resType
    }
    return ret
  }

  export function convertToGradeModifierRsrc(gm: GradeModifier): GradeModifierRsrc {

    let resType = gm.type ? gm.type : "GradeModifierRsrc"

    let ret = {
      '@type': resType,
      links: gm.links ? gm.links : null,
      gradeModifierGuid: gm.gradeModifierGuid,
      cropCommodityId: gm.cropCommodityId,
      cropYear: gm.cropYear,
      gradeModifierTypeCode: gm.gradeModifierTypeCode,
      gradeModifierValue: gm.gradeModifierValue,
      description: gm.description,
      insurancePlanId: gm.insurancePlanId,
      deleteAllowedInd: gm.deleteAllowedInd,
      deletedByUserInd: gm.deletedByUserInd,
      etag: etagFixer(gm.etag),
      type: resType
    };

    return ret;
  }
  
  export function convertToCropVarietyInsurabilityListRsrc(cviList: CropVarietyInsurabilityList): CropVarietyInsurabilityListRsrc {
    
    let resType = cviList.type ? cviList.type : "http://underwriting.cirras.mal.gov.bc.ca/v1/CropVarietyInsurabilityList"

    let ret = {
        '@type': resType,
        links: null,
        collection: cviList.collection,
        etag: etagFixer(cviList.etag),
        type: resType
    }
    return ret
  }


  export function convertToYieldConversionListRsrc(ycList: YieldMeasUnitConversionList): YieldMeasUnitConversionListRsrc{
    
    let ret = {
        '@type': ycList.type , 
        links: null,
        collection: ycList.collection,
        etag: etagFixer(ycList.etag),
        type: ycList.type  
    }
    return ret
  }
