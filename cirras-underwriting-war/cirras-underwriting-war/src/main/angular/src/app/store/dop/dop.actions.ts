import {Action} from "@ngrx/store";
import { DopYieldContract, YieldMeasUnitTypeCodeList } from "src/app/conversion/models-yield";
import {ErrorState} from "../application/application.state";

export const LOAD_DOP = "LOAD_DOP";
export const LOAD_DOP_SUCCESS = "LOAD_DOP_SUCCESS";
export const LOAD_DOP_ERROR = "LOAD_DOP_ERROR";
export const CLEAR_DOP = "CLEAR_DOP";

export const ADD_FORAGE_DOP_YIELD_FIELD_CUT = "ADD_FORAGE_DOP_YIELD_FIELD_CUT";
export const DEL_FORAGE_DOP_YIELD_FIELD_CUT = "DEL_FORAGE_DOP_YIELD_FIELD_CUT";

export const ROLLOVER_DOP = "ROLLOVER_DOP";
export const ADD_NEW_DOP = "ADD_NEW_DOP"
export const UPDATE_DOP = "UPDATE_DOP"
export const DELETE_DOP = "DELETE_DOP"
export const DELETE_DOP_SUCCESS = "DELETE_DOP_SUCCESS";
export const DELETE_DOP_ERROR = "DELETE_DOP_ERROR";

export const LOAD_YIELD_MEAS_UNITS = "LOAD_YIELD_MEAS_UNITS";
export const LOAD_YIELD_MEAS_UNITS_SUCCESS = "LOAD_YIELD_MEAS_UNITS_SUCCESS";
export const LOAD_YIELD_MEAS_UNITS_ERROR = "LOAD_YIELD_MEAS_UNITS_ERROR";
export const CLEAR_YIELD_MEAS_UNITS = "CLEAR_YIELD_MEAS_UNITS";

export const GET_DOCUMENT_BYTES = "GET_DOCUMENT_BYTES";
export const GET_DOCUMENT_BYTES_SUCCESS = "GET_DOCUMENT_BYTES_SUCCESS";
export const GET_DOCUMENT_BYTES_ERROR = "GET_DOCUMENT_BYTES_ERROR";


// Yield Measurement Units actions
export interface LoadYieldMeasUnitListAction extends Action {
  componentId: string;
  payload: {
    insurancePlanId: string,
  };
}

export interface LoadYieldMeasUnitListSuccessAction extends Action {
  componentId: string;
  payload: {
    value: YieldMeasUnitTypeCodeList;
  };
}

export interface LoadYieldMeasUnitListErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

export function LoadYieldMeasUnitList(componentId: string, 
                              insurancePlanId: string
                             ): LoadYieldMeasUnitListAction {

  return {
    type: LOAD_YIELD_MEAS_UNITS,
    componentId: componentId,
    payload: {
      insurancePlanId
    }
  };
}

export function loadYieldMeasUnitListSuccess(componentId: string, value: YieldMeasUnitTypeCodeList): LoadYieldMeasUnitListSuccessAction {
  return {
    type: LOAD_YIELD_MEAS_UNITS_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}

export function loadYieldMeasUnitListError(componentId: string, error: ErrorState): LoadYieldMeasUnitListErrorAction {
  return {
    type: LOAD_YIELD_MEAS_UNITS_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}


// Load DOP
export interface LoadDopYieldContractAction extends Action {
  componentId: string;
  payload: {
    declaredYieldContractGuid: string; 
  };
}

export interface LoadDopYieldContractSuccessAction extends Action {
  componentId: string;
  payload: {
    value: DopYieldContract;
  };
}

export interface LoadDopYieldContractErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

// Clear DOP
export interface ClearDopYieldContractAction extends Action {
  payload: {
    value: DopYieldContract;
  };

}

export interface AddForageDopYieldFieldCutAction extends Action {
  payload: {
    dopYieldContract: DopYieldContract;
  };
}

export interface DelForageDopYieldFieldCutAction extends Action {
  payload: {
    dopYieldContract: DopYieldContract;
  };
}

export interface DeleteDopYieldContractErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

export function ClearDopYieldContract(): ClearDopYieldContractAction {
  return {
    type: CLEAR_DOP,
    payload: {
      value: <DopYieldContract>{}
    }
  };
}

export function AddForageDopYieldFieldCut(dopYieldContract: DopYieldContract): AddForageDopYieldFieldCutAction {
  return {
    type: ADD_FORAGE_DOP_YIELD_FIELD_CUT,
    payload: {
      dopYieldContract: dopYieldContract
    }
  }
}

export function DelForageDopYieldFieldCut(dopYieldContract: DopYieldContract): DelForageDopYieldFieldCutAction {
  return {
    type: DEL_FORAGE_DOP_YIELD_FIELD_CUT,
    payload: {
      dopYieldContract: dopYieldContract
    }
  }
}



//DopYieldContract 
export function LoadDopYieldContract(componentId: string, declaredYieldContractGuid: string): LoadDopYieldContractAction {
  
  return {
    type: LOAD_DOP,
    componentId: componentId,
    payload: {
      declaredYieldContractGuid
    }
  };
}

export function LoadDopYieldContractSuccess(componentId: string, value: DopYieldContract): LoadDopYieldContractSuccessAction {
  return {
    type: LOAD_DOP_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}

export function LoadDopYieldContractError(componentId: string, error: ErrorState): LoadDopYieldContractErrorAction {
  return {
    type: LOAD_DOP_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}

export function DeleteDopYieldContractError(componentId: string, error: ErrorState): DeleteDopYieldContractErrorAction {
  return {
    type: DELETE_DOP_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}

export interface RolloverDopYieldContractAction extends Action {
  componentId: string;
  payload: {
    policyId: string; 
  };
}

export function RolloverDopYieldContract(componentId: string, policyId: string): RolloverDopYieldContractAction {
  
  return {
    type: ROLLOVER_DOP,
    componentId: componentId,
    payload: {
      policyId
    }
  };
}

export interface DopYieldContractAction extends Action {
  componentId: string;
  payload: {
    dopYieldContract: DopYieldContract; 
  };
}

export interface DeleteDopYieldContractAction extends Action {
  componentId: string;
  payload: {
    dopYieldContract: DopYieldContract; 
    policyId: string;
  };
}

export function AddNewDopYieldContract(componentId: string, dopYieldContract: DopYieldContract): DopYieldContractAction {
  
  return {
    type: ADD_NEW_DOP,
    componentId: componentId,
    payload: {
      dopYieldContract
    }
  };
}

export function UpdateDopYieldContract(componentId: string, dopYieldContract: DopYieldContract): DopYieldContractAction {
  
  return {
    type: UPDATE_DOP,
    componentId: componentId,
    payload: {
      dopYieldContract
    }
  };
}

export function DeleteDopYieldContract(componentId: string, policyId: string, dopYieldContract: DopYieldContract): DeleteDopYieldContractAction {
  
  return {
    type: DELETE_DOP,
    componentId: componentId,
    payload: {
      dopYieldContract,
      policyId
    }
  };
}

export interface GetDopReportAction extends Action {
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

export interface GetReportSuccessAction extends Action {
  payload: {
    value: any;
    index: number;
  }
}

export interface GetReportErrorAction extends Action {
  payload: {
    error: Error;
  }
}

export function GetDopReport(reportName: string,
                            policyId: string, 
                            cropYear: string, 
                            insurancePlanId: string, 
                            officeId: string,
                            policyStatusCode: string,
                            policyNumber: string,
                            growerInfo: string,
                            sortColumn: string): GetDopReportAction {
  return {
    type: GET_DOCUMENT_BYTES,
    payload: {
      reportName: reportName,
      policyId: policyId,
      cropYear: cropYear,
      insurancePlanId: insurancePlanId,
      officeId: officeId,
      policyStatusCode: policyStatusCode, 
      policyNumber: policyNumber,
      growerInfo: growerInfo,
      sortColumn: sortColumn
    }
  };
}

export function GetReportSuccess(value: any, index?): GetReportSuccessAction {
  return {
    type: GET_DOCUMENT_BYTES_SUCCESS,
    payload: {
      value,
      index
    }
  };
}

export function GetReportError(error: Error): GetReportErrorAction {
  return {
    type: GET_DOCUMENT_BYTES_ERROR,
    payload: {
      error
    }
  };
}

