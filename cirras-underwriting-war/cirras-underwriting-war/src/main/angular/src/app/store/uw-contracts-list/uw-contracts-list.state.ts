import {SearchState} from "@wf1/wfcc-core-lib";
import {InventoryContractList, UwContractsList} from "../../conversion/models";
import {getDefaultPagingInfoRequest} from "../application/application.state";

export const UW_CONTRACTS_SEARCH_COMPONENT_ID = "searchUwContracts";
export const UW_CONTRACTS_LIST_COMPONENT_ID = "uwContractsList";

export const EMPTY_UWCONTRACTSLIST: UwContractsList = {
  pageNumber: null,
  pageRowCount: null,
  totalPageCount: null,
  totalRowCount: null,
  collection: []
};

export const initialUwContractsListSearchState: SearchState = {
  query: null,
  sortParam: "policyNumber",
  sortDirection: "ASC",
  sortModalVisible: false,
  filters: {},
  hiddenFilters: {},
  componentId: UW_CONTRACTS_SEARCH_COMPONENT_ID
};

export const initUwContractsListPaging = getDefaultPagingInfoRequest(1, 20, "policyNumber", "ASC", undefined);

export interface UwContractsListState {
  uwContractsList?: UwContractsList;  
  inventoryContractList?: InventoryContractList;
}

export function getDefaultUwContractsListState(): UwContractsListState {
  return {
    uwContractsList: EMPTY_UWCONTRACTSLIST,   
    inventoryContractList: null // EMPTY_UWCONTRACTSLIST,
  };
}
