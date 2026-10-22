import {Action} from "@ngrx/store";
import {InventoryContractList, UwContractsList} from "../../conversion/models";
import {ErrorState, PagingInfoRequest} from "../application/application.state";

export const SEARCH_UW_CONTRACTS = "SEARCH_UW_CONTRACTS";
export const SEARCH_UW_CONTRACTS_SUCCESS = "SEARCH_UW_CONTRACTS_SUCCESS";
export const SEARCH_UW_CONTRACTS_ERROR = "SEARCH_UW_CONTRACTS_ERROR";

export const CLEAR_UW_CONTRACTS_SEARCH = "CLEAR_UW_CONTRACTS_SEARCH";

// PIM-1202: These actions were used to generate the Grain Inventory HTML report.
// That report has been migrated to Jasper, but the actions remain in case 
// another HTML report is needed in the future.
export const REPORT_PRINT_UW_CONTRACTS = "REPORT_PRINT_UW_CONTRACTS";
export const REPORT_PRINT_UW_CONTRACTS_SUCCESS = "REPORT_PRINT_UW_CONTRACTS_SUCCESS";
export const REPORT_PRINT_UW_CONTRACTS_ERROR = "REPORT_PRINT_UW_CONTRACTS_ERROR";

export const CLEAR_REPORT_PRINT = "CLEAR_REPORT_PRINT";

export interface ClearUwContractsSearchAction extends Action {
  payload: {
    value: UwContractsList;
  };
}

export function clearUwContractsSearch(): ClearUwContractsSearchAction {
  return {
    type: CLEAR_UW_CONTRACTS_SEARCH,
    payload: {
      value: {collection: [] }
    }
  };
}

export interface SearchUwContractsAction extends Action {
  componentId: string;
  payload: {
    pageInfoRequest: PagingInfoRequest,
    filters: {
      [param: string]: any[];
    }
  };
}

export interface SearchUwContractsSuccessAction extends Action {
  componentId: string;
  payload: {
    value: UwContractsList;
  };
}

export interface SearchUwContractsErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

export function searchUwContracts(componentId: string, 
                              pageInfoRequest: PagingInfoRequest,
                              searchGrower: string,
                              searchPolicyNumber: string,
                              selectedInsurancePlan: string,
                              selectedCropYear: string,
                              selectedPolicyStatus: string,
                              selectedOffice: string
                             ): SearchUwContractsAction {
  let filters = {};
  filters["searchGrower"] = searchGrower ? [searchGrower] : undefined;
  filters["searchPolicyNumber"] = searchPolicyNumber ? [searchPolicyNumber] : undefined;
  filters["selectedCropYear"] = selectedCropYear ? [selectedCropYear] : undefined;
  filters["selectedInsurancePlan"] = selectedInsurancePlan ? [selectedInsurancePlan] : undefined;
  filters["selectedPolicyStatus"] = selectedPolicyStatus ? [selectedPolicyStatus] : undefined;
  filters["selectedOffice"] = selectedOffice ? [selectedOffice] : undefined;

  return {
    type: SEARCH_UW_CONTRACTS,
    componentId: componentId,
    payload: {
      pageInfoRequest,
      filters: filters
    }
  };
}

export function searchUwContractsSuccess(componentId: string, value: UwContractsList): SearchUwContractsSuccessAction {
  return {
    type: SEARCH_UW_CONTRACTS_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}

export function searchUwContractsError(componentId: string, error: ErrorState): SearchUwContractsErrorAction {
  return {
    type: SEARCH_UW_CONTRACTS_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}


// BATCH PRINT

export function reportPrint(componentId: string, 
                                      pageInfoRequest: PagingInfoRequest,
                                      searchGrower: string,
                                      searchPolicyNumber: string,
                                      selectedInsurancePlan: string,
                                      selectedCropYear: string,
                                      selectedPolicyStatus: string,
                                      selectedOffice: string,
                                      selectedReportType: string,
                                      selectedReportSortBy: string,
                                      inventoryContractGuidList: string
                                    ): SearchUwContractsAction {
  let filters = {};
  filters["searchGrower"] = searchGrower ? [searchGrower] : undefined;
  filters["searchPolicyNumber"] = searchPolicyNumber ? [searchPolicyNumber] : undefined;
  filters["selectedCropYear"] = selectedCropYear ? [selectedCropYear] : undefined;
  filters["selectedInsurancePlan"] = selectedInsurancePlan ? [selectedInsurancePlan] : undefined;
  filters["selectedPolicyStatus"] = selectedPolicyStatus ? [selectedPolicyStatus] : undefined;
  filters["selectedOffice"] = selectedOffice ? [selectedOffice] : undefined;
  filters["selectedReportType"] = selectedReportType ? [selectedReportType] : undefined;
  filters["selectedReportSortBy"] = selectedReportSortBy ? [selectedReportSortBy] : undefined;
  filters["inventoryContractGuidList"] = inventoryContractGuidList ? [inventoryContractGuidList] : "";

  return {
      type: REPORT_PRINT_UW_CONTRACTS,
      componentId: componentId,
      payload: {
        pageInfoRequest,
        filters: filters
      }
  };
}

export interface ReportPrintSuccessAction extends Action {
  componentId: string;
  payload: {
    value: InventoryContractList;
  };
}

export function reportPrintSuccess(componentId: string, value: UwContractsList): SearchUwContractsSuccessAction {
  return {
    type: REPORT_PRINT_UW_CONTRACTS_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}

export function reportPrintError(componentId: string, error: ErrorState): SearchUwContractsErrorAction {
  return {
    type: REPORT_PRINT_UW_CONTRACTS_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}


export function clearReportPrint(): ClearUwContractsSearchAction {
  return {
    type: CLEAR_REPORT_PRINT,
    payload: {
      value: {collection: [] }
    }
  };
}