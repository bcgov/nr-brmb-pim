import {Action} from "@ngrx/store";
import {ErrorState} from "../application/application.state";
import { CropVarietyInsurabilityList, GradeModifierList, GradeModifierTypeList, SeedingDeadlineList, UnderwritingYear, UnderwritingYearList, YieldMeasUnitConversionList } from "src/app/conversion/models-maintenance";

export const LOAD_UW_YEARS = "LOAD_UW_YEARS";
export const LOAD_UW_YEARS_SUCCESS = "LOAD_UW_YEARS_SUCCESS";
export const LOAD_UW_YEARS_ERROR = "LOAD_UW_YEARS_ERROR";

export const ADD_UW_YEAR = "ADD_UW_YEAR";
export const ADD_UW_YEAR_ERROR = "ADD_UW_YEAR_ERROR";

export const LOAD_SEDING_DEADLINES = "LOAD_SEDING_DEADLINES";
export const LOAD_SEDING_DEADLINES_SUCCESS = "LOAD_SEDING_DEADLINES_SUCCESS";
export const LOAD_SEDING_DEADLINES_ERROR = "LOAD_SEDING_DEADLINES_ERROR";

export const SAVE_SEDING_DEADLINES = "SAVE_SEDING_DEADLINES";
export const SAVE_SEDING_DEADLINES_SUCCESS = "SAVE_SEDING_DEADLINES_SUCCESS";

export const LOAD_GRADE_MODIFIIER_TYPES = "LOAD_GRADE_MODIFIIER_TYPES"
export const LOAD_GRADE_MODIFIIER_TYPES_SUCCESS = "LOAD_GRADE_MODIFIIER_TYPES_SUCCESS";
export const LOAD_GRADE_MODIFIIER_TYPES_ERROR = "LOAD_GRADE_MODIFIIER_TYPES_ERROR";

export const SAVE_GRADE_MODIFIER_TYPES = "SAVE_GRADE_MODIFIER_TYPES";
export const SAVE_GRADE_MODIFIER_TYPES_SUCCESS = "SAVE_GRADE_MODIFIER_TYPES_SUCCESS";

export const LOAD_GRADE_MODIFIERS = "LOAD_GRADE_MODIFIERS";
export const LOAD_GRADE_MODIFIERS_SUCCESS = "LOAD_GRADE_MODIFIERS_SUCCESS";
export const LOAD_GRADE_MODIFIERS_ERROR = "LOAD_GRADE_MODIFIERS_ERROR";

export const SAVE_GRADE_MODIFIERS = "SAVE_GRADE_MODIFIERS";
export const SAVE_GRADE_MODIFIERS_SUCCESS = "SAVE_GRADE_MODIFIERS_SUCCESS";

export const LOAD_VARIETY_INSURABILITY = "LOAD_VARIETY_INSURABILITY";
export const LOAD_VARIETY_INSURABILITY_SUCCESS = "LOAD_VARIETY_INSURABILITY_SUCCESS";
export const LOAD_VARIETY_INSURABILITY_ERROR = "LOAD_VARIETY_INSURABILITY_ERROR";

export const SAVE_VARIETY_INSURABILITY = "SAVE_VARIETY_INSURABILITY";
export const SAVE_VARIETY_INSURABILITY_SUCCESS = "SAVE_VARIETY_INSURABILITY_SUCCESS";


export const LOAD_YIELD_CONVERSION = "LOAD_YIELD_CONVERSION";
export const LOAD_YIELD_CONVERSION_SUCCESS = "LOAD_YIELD_CONVERSION_SUCCESS";
export const LOAD_YIELD_CONVERSION_ERROR = "LOAD_YIELD_CONVERSION_ERROR";

export const SAVE_YIELD_CONVERSION = "SAVE_YIELD_CONVERSION";
export const SAVE_YIELD_CONVERSION_SUCCESS = "SAVE_YIELD_CONVERSION_SUCCESS";

export const CLEAR_YIELD_CONVERSION = "CLEAR_YIELD_CONVERSION"

// Load uw years
export interface LoadUwYearsAction extends Action {
    componentId: string;
    payload: { };
  }
  
  export interface LoadUwYearsSuccessAction extends Action {
    componentId: string;
    payload: {
      value: UnderwritingYearList;
    };
  }
  
  export interface LoadErrorAction extends Action {
    componentId: string;
    payload: {
      error: ErrorState;
    };
  }
  
  export interface AddUwYearAction extends Action {
    componentId: string;
    payload: {
      uwYear: UnderwritingYear;
    };
  }  

export interface AddUwYearErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}
  export function loadUwYears(componentId: string): LoadUwYearsAction {
    return {
      type: LOAD_UW_YEARS,
      componentId: componentId,
      payload: {}
    };
  }
  
  export function loadUwYearsSuccess(componentId: string, value: UnderwritingYearList): LoadUwYearsSuccessAction {
    return {
      type: LOAD_UW_YEARS_SUCCESS,
      componentId: componentId,
      payload: {
        value
      }
    };
  }
  
  export function loadUwYearsError(componentId: string, error: ErrorState): LoadErrorAction {
    return {
      type: LOAD_UW_YEARS_ERROR,
      componentId: componentId,
      payload: {
        error
      }
    };
  }

  export function addUwYear(componentId: string, uwYear: UnderwritingYear): AddUwYearAction {
    return {
      type: ADD_UW_YEAR,
      componentId: componentId,
      payload: {
        uwYear
      }
    };
  }
  
  export function addUwYearError(componentId: string, error: ErrorState): AddUwYearErrorAction {
    return {
      type: ADD_UW_YEAR_ERROR,
      componentId: componentId,
      payload: {
        error
      }
    };
  }

  // GET SEEDING DEADLINES

  export interface LoadSeedingDeadlinesAction extends Action {
    componentId: string;
    payload: { 
        cropYear: string;
    };
  }
  
  export interface LoadSeedingDeadlinesSuccessAction extends Action {
    componentId: string;
    payload: {
      value: SeedingDeadlineList; 
    };
  }
  

  export function loadSeedingDeadlines(componentId: string, cropYear: string): LoadSeedingDeadlinesAction {
    return {
      type: LOAD_SEDING_DEADLINES,
      componentId: componentId,
      payload: {
        cropYear
      }
    };
  }
  
  export function loadSeedingDeadlinesSuccess(componentId: string, value: SeedingDeadlineList): LoadSeedingDeadlinesSuccessAction {
    return {
      type: LOAD_SEDING_DEADLINES_SUCCESS,
      componentId: componentId,
      payload: {
        value
      }
    };
  }
  
  export function loadSeedingDeadlinesError(componentId: string, error: ErrorState): LoadErrorAction {
    return {
      type: LOAD_SEDING_DEADLINES_ERROR,
      componentId: componentId,
      payload: {
        error
      }
    };
  }

// SAVE SEEDING DEADLINES
export interface SaveSeedingDeadlinesAction extends Action {
    componentId: string;
    cropYear: string;
    payload: {      
      value: SeedingDeadlineList; 
    };
  }

  export function saveSeedingDeadlines(componentId: string, cropYear: string, seedingDeadlineList: SeedingDeadlineList): SaveSeedingDeadlinesAction {
    return {
      type: SAVE_SEDING_DEADLINES,
      componentId: componentId,
      cropYear:  cropYear,   
      payload: {
        value: seedingDeadlineList
      }
    };
  }

  export function saveSeedingDeadlinesSuccess(componentId: string, value: SeedingDeadlineList): LoadSeedingDeadlinesSuccessAction {
    return {
      type: SAVE_SEDING_DEADLINES_SUCCESS,
      componentId: componentId,
      payload: {
        value
      }
    };
  }


  // GRADE MODIFIER TYPES
  export interface LoadGradeModifierTypesAction extends Action {
    componentId: string;
    payload: { 
        cropYear: string;
    };
  }
  
  export interface LoadGradeModifierTypesSuccessAction extends Action {
    componentId: string;
    payload: {
      value: GradeModifierTypeList 
    };
  }
  

  export function loadGradeModifierTypes(componentId: string, cropYear: string): LoadGradeModifierTypesAction {
    return {
      type: LOAD_GRADE_MODIFIIER_TYPES,
      componentId: componentId,
      payload: {
        cropYear: cropYear,
      }
    };
  }
  
  export function loadGradeModifierTypesSuccess(componentId: string, value: GradeModifierTypeList): LoadGradeModifierTypesSuccessAction {
    return {
      type: LOAD_GRADE_MODIFIIER_TYPES_SUCCESS,
      componentId: componentId,
      payload: {
        value
      }
    };
  }
  
  export function loadGradeModifierTypesError(componentId: string, error: ErrorState): LoadErrorAction {
    return {
      type: LOAD_GRADE_MODIFIIER_TYPES_ERROR,
      componentId: componentId,
      payload: {
        error
      }
    };
  }

  // save grade modifier types
export interface SaveGradeModifierTypesAction extends Action {
  componentId: string;
  cropYear:  string,  
  payload: {      
    value: GradeModifierTypeList; 
  };
}

export function saveGradeModifierTypes(componentId: string, cropYear: string, gradeModifierTypeList: GradeModifierTypeList): SaveGradeModifierTypesAction {
  return {
    type: SAVE_GRADE_MODIFIER_TYPES,
    componentId: componentId,
    cropYear:  cropYear,  
    payload: {
      value: gradeModifierTypeList
    }
  };
}

export function saveGradeModifierTypesSuccess(componentId: string, value: GradeModifierTypeList): LoadGradeModifierTypesSuccessAction {
  return {
    type: SAVE_GRADE_MODIFIER_TYPES_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}

  // GET GRADE MODIFIERS

  export interface LoadGradeModifiersAction extends Action {
    componentId: string;
    payload: { 
        cropYear: string;
        insurancePlanId: string;
        cropCommodityId: string;
    };
  }
  
  export interface LoadGradeModifiersSuccessAction extends Action {
    componentId: string;
    payload: {
      value: GradeModifierList; 
    };
  }
  

  export function loadGradeModifiers(componentId: string, cropYear: string, insurancePlanId: string, cropCommodityId: string): LoadGradeModifiersAction {
    return {
      type: LOAD_GRADE_MODIFIERS,
      componentId: componentId,
      payload: {
        cropYear,
        insurancePlanId,
        cropCommodityId
      }
    };
  }
  
  export function loadGradeModifiersSuccess(componentId: string, value: GradeModifierList): LoadGradeModifiersSuccessAction {
    return {
      type: LOAD_GRADE_MODIFIERS_SUCCESS,
      componentId: componentId,
      payload: {
        value
      }
    };
  }
  
  export function loadGradeModifiersError(componentId: string, error: ErrorState): LoadErrorAction {
    return {
      type: LOAD_GRADE_MODIFIERS_ERROR,
      componentId: componentId,
      payload: {
        error
      }
    };
  }

  // SAVE GRADE MODIFIERS
  export interface SaveGradeModifiersAction extends Action {
    componentId: string;
    cropYear: string;
    insurancePlanId: string;
    cropCommodityId: string;
    payload: {      
      value: GradeModifierList; 
    };
  }

  export function saveGradeModifiers(componentId: string, cropYear: string, insurancePlanId: string, cropCommodityId: string, gradeModifierList: GradeModifierList): SaveGradeModifiersAction {
    return {
      type: SAVE_GRADE_MODIFIERS,
      componentId: componentId,
      cropYear:  cropYear,
      insurancePlanId: insurancePlanId,   
      cropCommodityId: cropCommodityId,
      payload: {
        value: gradeModifierList
      }
    };
  }

  export function saveGradeModifiersSuccess(componentId: string, value: GradeModifierList): LoadGradeModifiersSuccessAction {
    return {
      type: SAVE_GRADE_MODIFIERS_SUCCESS,
      componentId: componentId,
      payload: {
        value
      }
    };
  }

  // GET VARIETY INSURABILITY

  export interface LoadVarietyInsurabilityAction extends Action {
    componentId: string;
    payload: { 
        insurancePlanId: string;
        loadForEdit: string;
    };
  }
  
  export interface LoadVarietyInsurabilitySuccessAction extends Action {
    componentId: string;
    payload: {
      value: CropVarietyInsurabilityList; 
    };
  }

  export function loadVarietyInsurability(componentId: string, insurancePlanId: string, loadForEdit: string): LoadVarietyInsurabilityAction {
    return {
      type: LOAD_VARIETY_INSURABILITY,
      componentId: componentId,
      payload: {
        insurancePlanId,
        loadForEdit
      }
    };
  }
  
  export function loadVarietyInsurabilitySuccess(componentId: string, value: CropVarietyInsurabilityList): LoadVarietyInsurabilitySuccessAction {
    return {
      type: LOAD_VARIETY_INSURABILITY_SUCCESS,
      componentId: componentId,
      payload: {
        value
      }
    };
  }
  
  export function loadVarietyInsurabilityError(componentId: string, error: ErrorState): LoadErrorAction {
    return {
      type: LOAD_VARIETY_INSURABILITY_ERROR,
      componentId: componentId,
      payload: {
        error
      }
    };
  }

  // SAVE VARIETY INSURABILITY
  export interface SaveVarietyInsurabilityAction extends Action {
    componentId: string;
    insurancePlanId: string;
    loadForEdit: string;
    etag: string;
    payload: {      
      value: CropVarietyInsurabilityList; 
    };
  }

  export function saveVarietyInsurability(componentId: string, 
                                        insurancePlanId: string, 
                                        loadForEdit: string, 
                                        etag: string,
                                        cropVarietyInsurabilityList: CropVarietyInsurabilityList): SaveVarietyInsurabilityAction {
    return {
      type: SAVE_VARIETY_INSURABILITY,
      componentId: componentId,
      insurancePlanId:  insurancePlanId,   
      loadForEdit: loadForEdit,
      etag: etag,
      payload: {
        value: cropVarietyInsurabilityList
      }
    };
  }

  export function saveVarietyInsurabilitySuccess(componentId: string, value: CropVarietyInsurabilityList): LoadVarietyInsurabilitySuccessAction {
    return {
      type: SAVE_VARIETY_INSURABILITY_SUCCESS,
      componentId: componentId,
      payload: {
        value
      }
    };
  }

  // Yield Convenrsion
  export interface LoadYieldConversionAction extends Action {
    componentId: string;
    payload: { 
        insurancePlanId: string;
        srcYieldMeasUnitTypeCode: string;
        targetYieldMeasUnitTypeCode: string;
    };
  }
  
  export interface LoadYieldConversionSuccessAction extends Action {
    componentId: string;
    payload: {
      value: YieldMeasUnitConversionList; 
    };
  }

  export function LoadYieldConversion(componentId: string, 
                                      insurancePlanId: string, 
                                      srcYieldMeasUnitTypeCode: string, 
                                      targetYieldMeasUnitTypeCode: string): LoadYieldConversionAction {
    return {
      type: LOAD_YIELD_CONVERSION,
      componentId: componentId,
      payload: {
        insurancePlanId,
        srcYieldMeasUnitTypeCode,
        targetYieldMeasUnitTypeCode
      }
    };
  }
  
  export function LoadYieldConversionSuccess(componentId: string, value: YieldMeasUnitConversionList): LoadYieldConversionSuccessAction {
    return {
      type: LOAD_YIELD_CONVERSION_SUCCESS,
      componentId: componentId,
      payload: {
        value
      }
    };
  }
  
  export function LoadYieldConversionError(componentId: string, error: ErrorState): LoadErrorAction {
    return {
      type: LOAD_YIELD_CONVERSION_ERROR,
      componentId: componentId,
      payload: {
        error
      }
    };
  }


  // CLEAR YIELD CONVERSION
  export interface CleaYieldConversionAction extends Action {
    payload: {
      value: YieldMeasUnitConversionList;
    };
  
  }

  export function ClearYieldConversion(): CleaYieldConversionAction {
    return {
      type: CLEAR_YIELD_CONVERSION,
      payload: {
        value: <YieldMeasUnitConversionList>{}
      }
    };
  }

  // SAVE YIELD CONVERSION
export interface SaveYieldConversionAction extends Action {
  componentId: string;
  insurancePlanId: string;
  srcYieldMeasUnitTypeCode: string;
  targetYieldMeasUnitTypeCode: string;
  payload: {      
    value: YieldMeasUnitConversionList; 
  };
}

export function SaveYieldConversion(componentId: string, 
                                    insurancePlanId: string, 
                                    srcYieldMeasUnitTypeCode: string,
                                    targetYieldMeasUnitTypeCode: string,
                                    yieldConversionList: YieldMeasUnitConversionList): SaveYieldConversionAction {
  return {
    type: SAVE_YIELD_CONVERSION,
    componentId: componentId,
    insurancePlanId:  insurancePlanId,   
    srcYieldMeasUnitTypeCode: srcYieldMeasUnitTypeCode,
    targetYieldMeasUnitTypeCode: targetYieldMeasUnitTypeCode,
    payload: {
      value: yieldConversionList
    }
  };
}

export function SaveYieldConversionSuccess(componentId: string, value: YieldMeasUnitConversionList): LoadYieldConversionSuccessAction {
  return {
    type: SAVE_YIELD_CONVERSION_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}
