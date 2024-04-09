import {Action} from "@ngrx/store";
import { MaintenanceState, getDefaultMaintenanceState } from "./maintenance.state";
import { LOAD_VARIETY_INSURABILITY_SUCCESS, LOAD_GRADE_MODIFIERS_SUCCESS, LOAD_GRADE_MODIFIIER_TYPES_SUCCESS, LOAD_SEDING_DEADLINES_SUCCESS, 
    LOAD_UW_YEARS_SUCCESS, LoadVarietyInsurabilitySuccessAction, LoadGradeModifierTypesSuccessAction, LoadGradeModifiersSuccessAction,
    LoadSeedingDeadlinesSuccessAction, LoadUwYearsSuccessAction, SAVE_GRADE_MODIFIERS_SUCCESS, SAVE_GRADE_MODIFIER_TYPES_SUCCESS, 
    SAVE_SEDING_DEADLINES_SUCCESS, 
    SAVE_VARIETY_INSURABILITY_SUCCESS,
    SAVE_YIELD_CONVERSION_SUCCESS,
    LOAD_YIELD_CONVERSION_SUCCESS,
    LoadYieldConversionSuccessAction,
    CleaYieldConversionAction,
    CLEAR_YIELD_CONVERSION} from "./maintenance.actions";

export function maintenanceReducer(state: MaintenanceState = getDefaultMaintenanceState(), action: Action): MaintenanceState {

  switch (action.type) {

    case LOAD_UW_YEARS_SUCCESS: {
      const typedAction = <LoadUwYearsSuccessAction>action;
      return {...state, underwritingYears: typedAction.payload.value};
    }

    case LOAD_SEDING_DEADLINES_SUCCESS:
    case SAVE_SEDING_DEADLINES_SUCCESS: {
        const typedAction = <LoadSeedingDeadlinesSuccessAction>action;
        return {...state, seedingDeadlineList: typedAction.payload.value};
    }

    case LOAD_GRADE_MODIFIIER_TYPES_SUCCESS:
    case SAVE_GRADE_MODIFIER_TYPES_SUCCESS: {
      const typedAction = <LoadGradeModifierTypesSuccessAction>action;
      return {...state, gradeModifierTypesList: typedAction.payload.value};
    }

    case LOAD_GRADE_MODIFIERS_SUCCESS:
    case SAVE_GRADE_MODIFIERS_SUCCESS: {
        const typedAction = <LoadGradeModifiersSuccessAction>action;
        return {...state, gradeModifierList: typedAction.payload.value};
    }

    case LOAD_VARIETY_INSURABILITY_SUCCESS:
    case SAVE_VARIETY_INSURABILITY_SUCCESS: 
    {
        const typedAction = <LoadVarietyInsurabilitySuccessAction>action;
        return {...state, cropVarietyInsurabilityList: typedAction.payload.value};
    }

    case LOAD_YIELD_CONVERSION_SUCCESS:
    case SAVE_YIELD_CONVERSION_SUCCESS:
    {
        const typedAction = <LoadYieldConversionSuccessAction>action;
        return {...state, yieldMeasUnitConversionList: typedAction.payload.value};
    }

    case CLEAR_YIELD_CONVERSION: 
    { 
      const typedAction = <CleaYieldConversionAction>action;
      return {...state, yieldMeasUnitConversionList: null};
    }

    default: {
      return state;
    }
  }
}