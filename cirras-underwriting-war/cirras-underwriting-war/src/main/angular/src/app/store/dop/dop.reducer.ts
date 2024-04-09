import {Action} from "@ngrx/store";
import { ClearDopYieldContractAction, CLEAR_DOP, CLEAR_YIELD_MEAS_UNITS, LoadDopYieldContractSuccessAction, LoadYieldMeasUnitListSuccessAction, LOAD_DOP_SUCCESS, LOAD_YIELD_MEAS_UNITS_SUCCESS, DELETE_DOP_SUCCESS } from "./dop.actions";
import { DopState, getDefaultDopState } from "./dop.state";

export function dopReducer(state: DopState = getDefaultDopState(), action: Action): DopState {

  switch (action.type) {

    case CLEAR_DOP: {
      // const typedAction = <ClearDopYieldContractAction>action;
      return {...state, dopYieldContract: null }
    }

    case LOAD_DOP_SUCCESS: {
      const typedAction = <LoadDopYieldContractSuccessAction>action;
      return {...state, dopYieldContract: typedAction.payload.value};
    }

    case DELETE_DOP_SUCCESS: {
      return {...state, dopYieldContract: null};
    }
    // yield measurements units
    case LOAD_YIELD_MEAS_UNITS_SUCCESS: {
      const typedAction = <LoadYieldMeasUnitListSuccessAction>action;
      return {...state, yieldMeasUnitList: typedAction.payload.value};
    }

    case CLEAR_YIELD_MEAS_UNITS: {
        return {...state, yieldMeasUnitList: null};
    }

    default: {
      return state;
    }
  }
}