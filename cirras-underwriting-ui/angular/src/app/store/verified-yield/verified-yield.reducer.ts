import {Action} from "@ngrx/store";
import { getDefaultVerifiedYieldState, VerifiedYieldState } from "./verified-yield.state";
import { CLEAR_VERIFIED_YIELD, DELETE_VERIFIED_YIELD_SUCCESS, LOAD_VERIFIED_YIELD_SUCCESS, LoadVerifiedYieldContractSuccessAction } from "./verified-yield.actions";

export function verifiedYieldReducer(state: VerifiedYieldState = getDefaultVerifiedYieldState(), action: Action): VerifiedYieldState {

  switch (action.type) {

    case CLEAR_VERIFIED_YIELD: {
      return {...state, verifiedYieldContract: null }
    }

    case LOAD_VERIFIED_YIELD_SUCCESS: {
      const typedAction = <LoadVerifiedYieldContractSuccessAction>action;
      return {...state, verifiedYieldContract: typedAction.payload.value};
    }

    case DELETE_VERIFIED_YIELD_SUCCESS: {
      return {...state, verifiedYieldContract: null};
    }
    
    default: {
      return state;
    }
  }
}
