import {Action} from "@ngrx/store";
import { ClearUwContractsSearchAction, CLEAR_REPORT_PRINT, CLEAR_UW_CONTRACTS_SEARCH, ReportPrintSuccessAction, REPORT_PRINT_UW_CONTRACTS_SUCCESS, SearchUwContractsSuccessAction, SEARCH_UW_CONTRACTS_SUCCESS } from "./uw-contracts-list.actions";
import {getDefaultUwContractsListState, UwContractsListState} from "./uw-contracts-list.state";

export function uwContractsListReducer(state: UwContractsListState = getDefaultUwContractsListState(), action: Action): UwContractsListState {

  switch (action.type) {

    case CLEAR_UW_CONTRACTS_SEARCH: {
      const typedAction = <ClearUwContractsSearchAction>action;
      return {...state, uwContractsList: typedAction.payload.value};
    }

    case SEARCH_UW_CONTRACTS_SUCCESS: {
      const typedAction = <SearchUwContractsSuccessAction>action;
      return {...state, uwContractsList: typedAction.payload.value};
    }

    // report print
    case REPORT_PRINT_UW_CONTRACTS_SUCCESS: {
      const typedAction = <ReportPrintSuccessAction>action;
      return {...state, inventoryContractList: typedAction.payload.value};
    }

    case CLEAR_REPORT_PRINT: {
      const typedAction = <ClearUwContractsSearchAction>action;
      return {...state, inventoryContractList: null }; // typedAction.payload.value};
    }

    default: {
      return state;
    }
  }
}