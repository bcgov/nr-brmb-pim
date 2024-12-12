import {Action} from "@ngrx/store";
import { InventoryContract} from "../../conversion/models";
import {ErrorState} from "../application/application.state";

export const LOAD_INVENTORY_CONTRACT = "LOAD_INVENTORY_CONTRACT";
export const LOAD_INVENTORY_CONTRACT_SUCCESS = "LOAD_INVENTORY_CONTRACT_SUCCESS";
export const LOAD_INVENTORY_CONTRACT_ERROR = "LOAD_INVENTORY_CONTRACT_ERROR";
export const CLEAR_INVENTORY_CONTRACT = "CLEAR_INVENTORY_CONTRACT";

export const ROLLOVER_INVENTORY_CONTRACT = "ROLLOVER_INVENTORY_CONTRACT";
export const ADD_NEW_INVENTORY_CONTRACT = "ADD_NEW_INVENTORY_CONTRACT"
export const UPDATE_INVENTORY_CONTRACT = "UPDATE_INVENTORY_CONTRACT"

export const GET_INVENTORY_REPORT_BYTES = "GET_INVENTORY_REPORT_BYTES";

export const DELETE_INVENTORY_CONTRACT = "DELETE_INVENTORY_CONTRACT"
// export const DELETE_INVENTORY_CONTRACT_SUCCESS = "DELETE_INVENTORY_CONTRACT_SUCCESS"; // not needed for now
export const DELETE_INVENTORY_CONTRACT_ERROR = "DELETE_INVENTORY_CONTRACT_ERROR";

export interface LoadInventoryContractAction extends Action {
  componentId: string;
  payload: {
    inventoryContractGuid: string; 
  };
}

export interface LoadInventoryContractSuccessAction extends Action {
  componentId: string;
  payload: {
    value: InventoryContract;
  };
}

export interface LoadInventoryContractErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

// ClearInventoryContract
export interface ClearInventoryContractAction extends Action {
  payload: {
    value: InventoryContract;
  };

}

export function ClearInventoryContract(): ClearInventoryContractAction {
  return {
    type: CLEAR_INVENTORY_CONTRACT,
    payload: {
      value: <InventoryContract>{}
    }
  };
}



//InventoryContract
export function LoadInventoryContract(componentId: string, inventoryContractGuid: string): LoadInventoryContractAction {
  
  return {
    type: LOAD_INVENTORY_CONTRACT,
    componentId: componentId,
    payload: {
      inventoryContractGuid
    }
  };
}

export function LoadInventoryContractSuccess(componentId: string, value: InventoryContract): LoadInventoryContractSuccessAction {
  return {
    type: LOAD_INVENTORY_CONTRACT_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}

export function LoadInventoryContractError(componentId: string, error: ErrorState): LoadInventoryContractErrorAction {
  return {
    type: LOAD_INVENTORY_CONTRACT_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}


export interface RolloverInventoryContractAction extends Action {
  componentId: string;
  payload: {
    policyId: string; 
  };
}

export function RolloverInventoryContract(componentId: string, policyId: string): RolloverInventoryContractAction {
  
  return {
    type: ROLLOVER_INVENTORY_CONTRACT,
    componentId: componentId,
    payload: {
      policyId
    }
  };
}

export interface InventoryContractAction extends Action {
  componentId: string;
  policyId: string;
  payload: {
    inventoryContract: InventoryContract; 
  };
}

export function AddNewInventoryContract(componentId: string, policyId: string, inventoryContract: InventoryContract): InventoryContractAction {
  
  return {
    type: ADD_NEW_INVENTORY_CONTRACT,
    componentId: componentId,
    policyId: policyId,
    payload: {
      inventoryContract
    }
  };
}

export function UpdateInventoryContract(componentId: string, policyId: string, inventoryContract: InventoryContract): InventoryContractAction {
  
  return {
    type: UPDATE_INVENTORY_CONTRACT,
    componentId: componentId,
    policyId: policyId,
    payload: {
      inventoryContract
    }
  };
}


// INVENTORY PRINTOUT
export interface GetInventoryReportAction extends Action {
  payload: {
    reportName: string,
    policyId: string;
    cropYear: string;
    insurancePlanId: string;
    officeId: string;
    policyStatusCode: string;
    policyNumber: string;
    growerInfo: string;
    sortColumn: string;
    reportType: string;
  }
}

export function GetInventoryReport(reportName: string,
                            policyId: string, 
                            cropYear: string, 
                            insurancePlanId: string, 
                            officeId: string,
                            policyStatusCode: string,
                            policyNumber: string,
                            growerInfo: string,
                            sortColumn: string,
                            reportType: string): GetInventoryReportAction {
  return {
    type: GET_INVENTORY_REPORT_BYTES,
    payload: {
      reportName: reportName,
      policyId: policyId,
      cropYear: cropYear,
      insurancePlanId: insurancePlanId,
      officeId: officeId,
      policyStatusCode: policyStatusCode, 
      policyNumber: policyNumber,
      growerInfo: growerInfo,
      sortColumn: sortColumn,
      reportType: reportType
    }
  };
}


// Delete Inventory
export interface DeleteInventoryContractAction extends Action {
  componentId: string;
  payload: {
    etag: string;
    inventoryContractGuid: string; 
    policyId: string;
  };
}

export interface DeleteInventoryContractErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

export function DeleteInventoryContract(componentId: string, inventoryContractGuid: string, policyId: string, etag: string): DeleteInventoryContractAction {
  
  return {
    type: DELETE_INVENTORY_CONTRACT,
    componentId: componentId,
    payload: {
      etag,
      inventoryContractGuid,
      policyId
    }
  };
}

export function DeleteInventoryContractError(componentId: string, error: ErrorState): DeleteInventoryContractErrorAction {
  return {
    type: DELETE_INVENTORY_CONTRACT_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}