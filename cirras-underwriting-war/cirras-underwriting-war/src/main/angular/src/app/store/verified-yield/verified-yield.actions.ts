import {Action} from "@ngrx/store";
import { VerifiedYieldContract } from "src/app/conversion/models-yield";
import {ErrorState} from "../application/application.state";

export const LOAD_VERIFIED_YIELD = "LOAD_VERIFIED_YIELD";
export const LOAD_VERIFIED_YIELD_SUCCESS = "LOAD_VERIFIED_YIELD_SUCCESS";
export const LOAD_VERIFIED_YIELD_ERROR = "LOAD_VERIFIED_YIELD_ERROR";
export const CLEAR_VERIFIED_YIELD = "CLEAR_VERIFIED_YIELD";

export const ROLLOVER_VERIFIED_YIELD = "ROLLOVER_VERIFIED_YIELD";
export const ADD_NEW_VERIFIED_YIELD = "ADD_NEW_VERIFIED_YIELD"
export const UPDATE_VERIFIED_YIELD = "UPDATE_VERIFIED_YIELD"
export const DELETE_VERIFIED_YIELD = "DELETE_VERIFIED_YIELD"
export const DELETE_VERIFIED_YIELD_SUCCESS = "DELETE_VERIFIED_YIELD_SUCCESS";
export const DELETE_VERIFIED_YIELD_ERROR = "DELETE_VERIFIED_YIELD_ERROR";

export const LOAD_YIELD_MEAS_UNITS = "LOAD_YIELD_MEAS_UNITS";
export const LOAD_YIELD_MEAS_UNITS_SUCCESS = "LOAD_YIELD_MEAS_UNITS_SUCCESS";
export const LOAD_YIELD_MEAS_UNITS_ERROR = "LOAD_YIELD_MEAS_UNITS_ERROR";
export const CLEAR_YIELD_MEAS_UNITS = "CLEAR_YIELD_MEAS_UNITS";

export const GET_DOCUMENT_BYTES = "GET_DOCUMENT_BYTES";
export const GET_DOCUMENT_BYTES_SUCCESS = "GET_DOCUMENT_BYTES_SUCCESS";
export const GET_DOCUMENT_BYTES_ERROR = "GET_DOCUMENT_BYTES_ERROR";


// Load VERIFIED_YIELD
export interface LoadVerifiedYieldContractAction extends Action {
  componentId: string;
  payload: {
    verifiedYieldContractGuid: string; 
  };
}

export interface LoadVerifiedYieldContractSuccessAction extends Action {
  componentId: string;
  payload: {
    value: VerifiedYieldContract;
  };
}

export interface LoadVerifiedYieldContractErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

// Clear VERIFIED_YIELD
export interface ClearVerifiedYieldContractAction extends Action {
  payload: {
    value: VerifiedYieldContract;
  };

}

export interface DeleteVerifiedYieldContractErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

export function ClearVerifiedYieldContract(): ClearVerifiedYieldContractAction {
  return {
    type: CLEAR_VERIFIED_YIELD,
    payload: {
      value: <VerifiedYieldContract>{}
    }
  };
}





//VerifiedYieldContract 
export function LoadVerifiedYieldContract(componentId: string, verifiedYieldContractGuid: string): LoadVerifiedYieldContractAction {
  
  return {
    type: LOAD_VERIFIED_YIELD,
    componentId: componentId,
    payload: {
      verifiedYieldContractGuid
    }
  };
}

export function LoadVerifiedYieldContractSuccess(componentId: string, value: VerifiedYieldContract): LoadVerifiedYieldContractSuccessAction {
  return {
    type: LOAD_VERIFIED_YIELD_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}

export function LoadVerifiedYieldContractError(componentId: string, error: ErrorState): LoadVerifiedYieldContractErrorAction {
  return {
    type: LOAD_VERIFIED_YIELD_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}

export function DeleteVerifiedYieldContractError(componentId: string, error: ErrorState): DeleteVerifiedYieldContractErrorAction {
  return {
    type: DELETE_VERIFIED_YIELD_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}

export interface RolloverVerifiedYieldContractAction extends Action {
  componentId: string;
  payload: {
    policyId: string; 
  };
}

export function RolloverVerifiedYieldContract(componentId: string, policyId: string): RolloverVerifiedYieldContractAction {
  
  return {
    type: ROLLOVER_VERIFIED_YIELD,
    componentId: componentId,
    payload: {
      policyId
    }
  };
}

export interface VerifiedYieldContractAction extends Action {
  componentId: string;
  payload: {
    verifiedYieldContract: VerifiedYieldContract; 
  };
}

export interface DeleteVerifiedYieldContractAction extends Action {
  componentId: string;
  payload: {
    verifiedYieldContract: VerifiedYieldContract; 
    policyId: string;
  };
}

export function AddNewVerifiedYieldContract(componentId: string, verifiedYieldContract: VerifiedYieldContract): VerifiedYieldContractAction {
  
  return {
    type: ADD_NEW_VERIFIED_YIELD,
    componentId: componentId,
    payload: {
        verifiedYieldContract
    }
  };
}

export function UpdateVerifiedYieldContract(componentId: string, verifiedYieldContract: VerifiedYieldContract): VerifiedYieldContractAction {
  
  return {
    type: UPDATE_VERIFIED_YIELD,
    componentId: componentId,
    payload: {
      verifiedYieldContract
    }
  };
}

export function DeleteVerifiedYieldContract(componentId: string, policyId: string, verifiedYieldContract: VerifiedYieldContract): DeleteVerifiedYieldContractAction {
  
  return {
    type: DELETE_VERIFIED_YIELD,
    componentId: componentId,
    payload: {
      verifiedYieldContract,
      policyId
    }
  };
}

export interface GetVerifiedYieldReportAction extends Action {
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
  }
}
