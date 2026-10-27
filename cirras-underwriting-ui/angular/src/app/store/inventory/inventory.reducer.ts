import {Action} from "@ngrx/store";
import { ClearInventoryContractAction, CLEAR_INVENTORY_CONTRACT, LoadInventoryContractSuccessAction, LOAD_INVENTORY_CONTRACT_SUCCESS } from "./inventory.actions";
import { getDefaultInventoryState, InventoryState } from "./inventory.state";

export function inventoryReducer(state: InventoryState = getDefaultInventoryState(), action: Action): InventoryState {

  switch (action.type) {

    case CLEAR_INVENTORY_CONTRACT: {
      const typedAction = <ClearInventoryContractAction>action;
      return {...state, inventoryContract: null }
    }

    case LOAD_INVENTORY_CONTRACT_SUCCESS: {
      const typedAction = <LoadInventoryContractSuccessAction>action;
      return {...state, inventoryContract: typedAction.payload.value};
    }

    default: {
      return state;
    }
  }
}