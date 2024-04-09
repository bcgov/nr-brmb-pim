import {Action} from "@ngrx/store";
import {LegalLand, LegalLandList, RiskAreaList} from "../../conversion/models";
import {ErrorState, PagingInfoRequest} from "../application/application.state";

export const SEARCH_LAND = "SEARCH_LAND";
export const SEARCH_LAND_SUCCESS = "SEARCH_LAND_SUCCESS";
export const SEARCH_LAND_ERROR = "SEARCH_LAND_ERROR";

export const CLEAR_LAND_SEARCH = "CLEAR_LAND_SEARCH";

export const GET_LEGAL_LAND = "GET_LEGAL_LAND";
export const GET_LEGAL_LAND_SUCCESS = "GET_LEGAL_LAND_SUCCESS";
export const GET_LEGAL_LAND_ERROR = "GET_LEGAL_LAND_ERROR";

export const ADD_NEW_LEGAL_LAND = "ADD_NEW_LEGAL_LAND";
export const UPDATE_LEGAL_LAND = "UPDATE_LEGAL_LAND";

export const DELETE_LEGAL_LAND = "DELETE_LEGAL_LAND";
export const DELETE_LEGAL_LAND_ERROR = "DELETE_LEGAL_LAND_ERROR";

export const CLEAR_LEGAL_LAND = "CLEAR_LEGAL_LAND";

export const GET_RISK_AREA_LIST = "GET_RISK_AREA_LIST";
export const GET_RISK_AREA_LIST_SUCCESS = "GET_RISK_AREA_LIST_SUCCESS";
export const GET_RISK_AREA_LIST_ERROR = "GET_RISK_AREA_LIST_ERROR";

export interface ClearLandSearchAction extends Action {
  payload: {
    value: LegalLandList;
  };
}

export function clearLandSearch(): ClearLandSearchAction {
  return {
    type: CLEAR_LAND_SEARCH,
    payload: {
      value: {collection: [] }
    }
  };
}

export interface SearchLandAction extends Action {
  componentId: string;
  payload: {
    pageInfoRequest: PagingInfoRequest,
    filters: {
      [param: string]: any[];
    }
  };
}

export interface SearchLandSuccessAction extends Action {
  componentId: string;
  payload: {
    value: LegalLandList;
  };
}

export interface SearchLandErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

export function searchLand(componentId: string, 
                              pageInfoRequest: PagingInfoRequest,
                              searchLandIdentifier: string,
                              searchLegalLocation: string,
                              searchGrower: string,
                              selectedDataset: string,
                              isWildCardSearch: String,
                              searchByLegalLocOrLegalDesc: String
                             ): SearchLandAction {
  let filters = {};
  filters["searchLandIdentifier"] = searchLandIdentifier ? [searchLandIdentifier] : undefined;
  filters["searchLegalLocation"] = searchLegalLocation ? [searchLegalLocation] : undefined;
  filters["searchGrower"] = searchGrower ? [searchGrower] : undefined;
  filters["selectedDataset"] = selectedDataset ? [selectedDataset] : undefined;
  filters["isWildCardSearch"] = isWildCardSearch ? [isWildCardSearch] : undefined;
  filters["searchByLegalLocOrLegalDesc"] = searchByLegalLocOrLegalDesc ? [searchByLegalLocOrLegalDesc] : undefined;

  return {
    type: SEARCH_LAND,
    componentId: componentId,
    payload: {
      pageInfoRequest,
      filters: filters
    }
  };
}

export function searchLandSuccess(componentId: string, value: LegalLandList): SearchLandSuccessAction {
  return {
    type: SEARCH_LAND_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}

export function searchLandError(componentId: string, error: ErrorState): SearchLandErrorAction {
  return {
    type: SEARCH_LAND_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}

// get legal land info for a legal land id
export interface GetLegalLandAction extends Action {
  componentId: string;
  payload: {
    legalLandId: string;
  };
}

export function getLegalLand(componentId: string, legalLandId: string): GetLegalLandAction {
  return {
    type: GET_LEGAL_LAND,
    componentId: componentId,
    payload: {
      legalLandId: legalLandId
    }
  };
}

export interface GetLegalLandSuccessAction extends Action {
  componentId: string;
  payload: {
    value: LegalLand;
  };
}

export interface GetLegalLandErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

export function getLegalLandSuccess(componentId: string, legalLand: LegalLand): GetLegalLandSuccessAction {
  return {
    type: GET_LEGAL_LAND_SUCCESS,
    componentId: componentId,
    payload: {
      value: legalLand
    }
  };
}

export function getLegalLandError(componentId: string, error: ErrorState): GetLegalLandErrorAction {
  return {
    type: GET_LEGAL_LAND_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}


export interface ManageLegalLandAction extends Action {
  componentId: string;
  payload: {
    legalLand: LegalLand; 
  };
}

export function addNewLegalLand(componentId: string, legalLand: LegalLand): ManageLegalLandAction {
  return {
    type: ADD_NEW_LEGAL_LAND,
    componentId: componentId,
    payload: {
      legalLand
    }
  };
}

export function updateLegalLand(componentId: string, legalLand: LegalLand): ManageLegalLandAction {
  return {
    type: UPDATE_LEGAL_LAND,
    componentId: componentId,
    payload: {
      legalLand
    }
  };
}

export function deleteLegalLand(componentId: string, legalLand: LegalLand): ManageLegalLandAction {
  return {
    type: DELETE_LEGAL_LAND,
    componentId: componentId,
    payload: {
      legalLand
    }
  };
}

export interface DeleteLegalLandErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

export function deleteLegalLandError(componentId: string, error: ErrorState): DeleteLegalLandErrorAction {
  return {
    type: DELETE_LEGAL_LAND_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}


// Clear Legal Land component
export interface ClearLegalLandAction extends Action {
  payload: {
    value: LegalLand;
  };

}

export function ClearLegalLand(): ClearLegalLandAction {
  return {
    type: CLEAR_LEGAL_LAND,
    payload: {
      value: <LegalLand>{}
    }
  };
}

// get Risk Areas
export interface GetRiskAreaListAction extends Action {
  componentId: string;
  payload: {  };
}

export function getRiskAreaList(componentId: string): GetRiskAreaListAction {
  return {
    type: GET_RISK_AREA_LIST,
    componentId: componentId,
    payload: {}
  };
}

export interface GetRiskAreaListSuccessAction extends Action {
  componentId: string;
  payload: {
    value: RiskAreaList;
  };
}

export interface GetRiskAreaListErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

export function getRiskAreaListSuccess(componentId: string, riskAreaList: RiskAreaList): GetRiskAreaListSuccessAction {
  return {
    type: GET_RISK_AREA_LIST_SUCCESS,
    componentId: componentId,
    payload: {
      value: riskAreaList
    }
  };
}

export function getRiskAreaListError(componentId: string, error: ErrorState): GetRiskAreaListErrorAction {
  return {
    type: GET_RISK_AREA_LIST_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}
