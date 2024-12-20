import {Action} from "@ngrx/store";
import {UwContract, UwContractsList} from "../../conversion/models";
import {ErrorState} from "../application/application.state";

export const LOAD_GROWER_CONTRACT = "LOAD_GROWER_CONTRACT";
export const LOAD_GROWER_CONTRACT_SUCCESS = "LOAD_GROWER_CONTRACT_SUCCESS";
export const LOAD_GROWER_CONTRACT_ERROR = "LOAD_GROWER_CONTRACT_ERROR";



// grower / policy info header

export interface LoadGrowerContractAction extends Action {
    componentId: string;
    payload: {
      value: string; 
      screenType: string;
    };
  }
  
  export interface LoadGrowerContractSuccessAction extends Action {
    componentId: string;
    payload: {
      value: UwContract;
    };
  }
  
  export interface LoadGrowerContractErrorAction extends Action {
    componentId: string;
    payload: {
      error: ErrorState;
    };
  }
  
  // add policy Id and inventoryContractGuid
  export function LoadGrowerContract(componentId: string, value: string, screenType: string): LoadGrowerContractAction {
    
    return {
      type: LOAD_GROWER_CONTRACT,
      componentId: componentId,
      payload: {
          value,
          screenType
      }
    };
  }
  
  export function LoadGrowerContractSuccess(componentId: string, uwContract: UwContract): LoadGrowerContractSuccessAction {
    return {
      type: LOAD_GROWER_CONTRACT_SUCCESS,
      componentId: componentId,
      payload: {
        value: uwContract
      }
    };
  }
  
  export function LoadGrowerContractError(componentId: string, error: ErrorState): LoadGrowerContractErrorAction {
    return {
      type: LOAD_GROWER_CONTRACT_ERROR,
      componentId: componentId,
      payload: {
        error
      }
    };
  }