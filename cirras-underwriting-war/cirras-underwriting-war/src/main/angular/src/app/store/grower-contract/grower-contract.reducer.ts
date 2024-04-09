import {Action} from "@ngrx/store";
import {  LoadGrowerContractSuccessAction, LOAD_GROWER_CONTRACT_SUCCESS } from "./grower-contract.actions";
import { getDefaultGrowerContractState, GrowerContractState } from "./grower-contract.state";

export function growerContractInfoReducer(state: GrowerContractState = getDefaultGrowerContractState(), action: Action): GrowerContractState {

  switch (action.type) {
    
    case LOAD_GROWER_CONTRACT_SUCCESS: {
      const typedAction = <LoadGrowerContractSuccessAction>action;
      return {...state, growerContract: typedAction.payload.value};
    }

    default: {
      return state;
    }
  }
}