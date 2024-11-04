import {Action} from "@ngrx/store";
import {
  ApplicationState,
  ERROR_TYPE,
  ErrorState,
  getDefaultApplicationState,
  getDefaultFormState,
  getDefaultLoadStates
} from "./application.state";

import {SET_FORM_STATE_UNSAVED, SetFormStateUnsavedAction} from "./application.actions";
import { REPORT_PRINT_UW_CONTRACTS, REPORT_PRINT_UW_CONTRACTS_ERROR, REPORT_PRINT_UW_CONTRACTS_SUCCESS, SEARCH_UW_CONTRACTS, SEARCH_UW_CONTRACTS_ERROR, SEARCH_UW_CONTRACTS_SUCCESS } from "../uw-contracts-list/uw-contracts-list.actions";
import { UW_CONTRACTS_LIST_COMPONENT_ID } from "../uw-contracts-list/uw-contracts-list.state";
import { LOAD_INVENTORY_CONTRACT_ERROR, LOAD_INVENTORY_CONTRACT_SUCCESS } from "../inventory/inventory.actions";
import { LOAD_GROWER_CONTRACT_ERROR, LOAD_GROWER_CONTRACT_SUCCESS } from "../grower-contract/grower-contract.actions";
import { LOAD_DOP_ERROR, LOAD_DOP_SUCCESS, DELETE_DOP_SUCCESS, DELETE_DOP_ERROR } from "../dop/dop.actions";
import { LAND_LIST_COMPONENT_ID } from "../land-management/land-management.state";
import { SEARCH_LAND, SEARCH_LAND_SUCCESS, SEARCH_LAND_ERROR, CLEAR_LAND_SEARCH, GET_LEGAL_LAND_SUCCESS, GET_LEGAL_LAND_ERROR, DELETE_LEGAL_LAND_ERROR } from "../land-management/land-management.actions";
import { ADD_UW_YEAR, ADD_UW_YEAR_ERROR, LOAD_VARIETY_INSURABILITY, LOAD_VARIETY_INSURABILITY_ERROR, LOAD_VARIETY_INSURABILITY_SUCCESS, 
  LOAD_GRADE_MODIFIERS, LOAD_GRADE_MODIFIERS_ERROR, LOAD_GRADE_MODIFIERS_SUCCESS, LOAD_GRADE_MODIFIIER_TYPES, LOAD_GRADE_MODIFIIER_TYPES_ERROR, 
  LOAD_GRADE_MODIFIIER_TYPES_SUCCESS, LOAD_SEDING_DEADLINES, LOAD_SEDING_DEADLINES_ERROR, LOAD_SEDING_DEADLINES_SUCCESS, LOAD_UW_YEARS, 
  LOAD_UW_YEARS_ERROR, LOAD_UW_YEARS_SUCCESS, SAVE_GRADE_MODIFIERS, SAVE_GRADE_MODIFIERS_SUCCESS, SAVE_GRADE_MODIFIER_TYPES, SAVE_GRADE_MODIFIER_TYPES_SUCCESS, 
  SAVE_SEDING_DEADLINES, SAVE_SEDING_DEADLINES_SUCCESS, SAVE_VARIETY_INSURABILITY, SAVE_VARIETY_INSURABILITY_SUCCESS, LOAD_YIELD_CONVERSION_SUCCESS, SAVE_YIELD_CONVERSION, SAVE_YIELD_CONVERSION_SUCCESS } from "../maintenance/maintenance.actions";
import { MAINTENANCE_COMPONENT_ID } from "../maintenance/maintenance.state";
import { DELETE_VERIFIED_YIELD_ERROR, LOAD_VERIFIED_YIELD_ERROR, LOAD_VERIFIED_YIELD_SUCCESS } from "../verified-yield/verified-yield.actions";


export function applicationReducer(state: ApplicationState = getDefaultApplicationState(), action: Action): ApplicationState {
  switch (action.type) {
    case SEARCH_UW_CONTRACTS:  
    case REPORT_PRINT_UW_CONTRACTS:   
    case SEARCH_LAND:
    {
      return updateLoadState(state, action, true);
    }
    case SEARCH_UW_CONTRACTS_SUCCESS:
    case LOAD_INVENTORY_CONTRACT_SUCCESS:
    case LOAD_GROWER_CONTRACT_SUCCESS:
    case REPORT_PRINT_UW_CONTRACTS_SUCCESS:
    case LOAD_DOP_SUCCESS:
    case LOAD_VERIFIED_YIELD_SUCCESS:
    case SEARCH_LAND_SUCCESS:
    case GET_LEGAL_LAND_SUCCESS:
    case LOAD_UW_YEARS_SUCCESS:
    case LOAD_SEDING_DEADLINES_SUCCESS:
    case SAVE_SEDING_DEADLINES:
    case SAVE_SEDING_DEADLINES_SUCCESS:
    case LOAD_GRADE_MODIFIIER_TYPES_SUCCESS:
    case LOAD_GRADE_MODIFIERS_SUCCESS:
    case LOAD_VARIETY_INSURABILITY_SUCCESS:
    case SAVE_VARIETY_INSURABILITY:
    case SAVE_VARIETY_INSURABILITY_SUCCESS:
    case LOAD_YIELD_CONVERSION_SUCCESS:
    case SAVE_YIELD_CONVERSION:
    case SAVE_YIELD_CONVERSION_SUCCESS:
    {
      return updateLoadState(state, action, false);
    }
    case SEARCH_UW_CONTRACTS_ERROR:    
    case LOAD_INVENTORY_CONTRACT_ERROR:
    case LOAD_GROWER_CONTRACT_ERROR:
    case REPORT_PRINT_UW_CONTRACTS_ERROR:
    case LOAD_DOP_ERROR:
    case DELETE_DOP_ERROR:
    case LOAD_VERIFIED_YIELD_ERROR:
    case SEARCH_LAND_ERROR:
    case GET_LEGAL_LAND_ERROR:

    case LOAD_UW_YEARS_ERROR:
    case ADD_UW_YEAR_ERROR:
    case DELETE_LEGAL_LAND_ERROR:
    case LOAD_SEDING_DEADLINES_ERROR:
    case LOAD_GRADE_MODIFIERS_ERROR:
    case LOAD_GRADE_MODIFIIER_TYPES_ERROR:
    {
        return updateErrorState(state, action, action["payload"]["error"]);
    }
    case SET_FORM_STATE_UNSAVED: {
      let typedAction = <SetFormStateUnsavedAction>action;
      return {...state, formStates: {...state.formStates,
              [typedAction.payload.componentId]: {
                ...state.formStates[typedAction.payload.componentId],
                isUnsaved: typedAction.payload.isUnsaved}}
             };
    }
    default: {
      return state;
    }
  }
}

export function getStatePropertyNameForActionName(action: Action): string {
  let actionType = action.type;
  let typedAction = null;
  switch (actionType) {

    case SEARCH_UW_CONTRACTS:
    case SEARCH_UW_CONTRACTS_SUCCESS:
    case SEARCH_UW_CONTRACTS_ERROR:
    case REPORT_PRINT_UW_CONTRACTS:
    case REPORT_PRINT_UW_CONTRACTS_SUCCESS:
    case REPORT_PRINT_UW_CONTRACTS_ERROR:    
      return UW_CONTRACTS_LIST_COMPONENT_ID;

    case SEARCH_LAND:
    case SEARCH_LAND_SUCCESS:
    case SEARCH_LAND_ERROR:
      return LAND_LIST_COMPONENT_ID;

    case LOAD_UW_YEARS:
    case LOAD_UW_YEARS_SUCCESS:
    case LOAD_UW_YEARS_ERROR: 
    case ADD_UW_YEAR:
    case ADD_UW_YEAR_ERROR:
    case LOAD_SEDING_DEADLINES:
    case LOAD_SEDING_DEADLINES_SUCCESS:
    case LOAD_SEDING_DEADLINES_ERROR:
    case SAVE_SEDING_DEADLINES:
    case SAVE_SEDING_DEADLINES_SUCCESS:
    case LOAD_GRADE_MODIFIIER_TYPES:
    case LOAD_GRADE_MODIFIIER_TYPES_SUCCESS:
    case LOAD_GRADE_MODIFIIER_TYPES_ERROR:
    case SAVE_GRADE_MODIFIER_TYPES:
    case SAVE_GRADE_MODIFIER_TYPES_SUCCESS:
    case LOAD_GRADE_MODIFIERS:
    case LOAD_GRADE_MODIFIERS_SUCCESS:
    case LOAD_GRADE_MODIFIERS_ERROR:
    case SAVE_GRADE_MODIFIERS:
    case SAVE_GRADE_MODIFIERS_SUCCESS:
    case LOAD_VARIETY_INSURABILITY:
    case LOAD_VARIETY_INSURABILITY_SUCCESS:
    case LOAD_VARIETY_INSURABILITY_ERROR:
    case SAVE_VARIETY_INSURABILITY:
    case SAVE_VARIETY_INSURABILITY_SUCCESS:
    case LOAD_YIELD_CONVERSION_SUCCESS:
    case SAVE_YIELD_CONVERSION:
    case SAVE_YIELD_CONVERSION_SUCCESS:
      return MAINTENANCE_COMPONENT_ID;

    default:
      return null;
  }
}

export function updateLoadState(state: ApplicationState, action: Action, value: boolean): ApplicationState {
  let component = getStatePropertyNameForActionName(action);
  let st = state;
  if (value) { // if starting load, reset error state
      st = clearErrorState(state, action);
  } else { //if ending load, reset form state
      st = clearFormState(state, action);
  }
  // Only update state if there is a value change
  //console.log(component, action.type, value);
  if (component && (!state.loadStates || !state.loadStates[component] || state.loadStates[component].isLoading !== value)) {
    return {
      ...st,
      loadStates: {...st.loadStates, [component]: {isLoading: value}}
    };
  } else {
    return st;
  }
}

export function updateErrorState(state: ApplicationState, action: Action, value: ErrorState): ApplicationState {
  let component = getStatePropertyNameForActionName(action);
  if (component) {
    if (value.type == ERROR_TYPE.FATAL) {
      let ns = {
        ...state,
        //errorStates: {...state.errorStates, ["severe"]: [...state.errorStates["severe"], value]}, // severe errorstate does not seem to be used
        loadStates: getDefaultLoadStates() // set all load states to false on a fatal error
      };
      return ns;
    }

    if (state.errorStates && state.errorStates[component]) {
      if (state.errorStates[component].find && state.errorStates[component].find((errorState: ErrorState) => errorState.message == value.message)) {
        return state;
      }
    }

    return {
      ...state,
      errorStates: {...state.errorStates, [component]: [...state.errorStates[component], value]},
      loadStates: {...state.loadStates, [component]: {isLoading: false}}
    };
  } else {
      return state;
  }
}

export function clearErrorState(state: ApplicationState, action: Action): ApplicationState {
  //TODO filter out errors in 'errors' param by UUID
  let component = getStatePropertyNameForActionName(action);
  if (component) {
    return {
      ...state,
      errorStates: {...state.errorStates, [component]: []},
    };
  } else {
      return state;
  }
}

export function clearFormState(state: ApplicationState, action: Action): ApplicationState {
  let component = getStatePropertyNameForActionName(action);
  if (component) {
    return {
      ...state,
      formStates: {...state.formStates, [component]: getDefaultFormState()},
    };
  } else {
    return state;
  }
}
