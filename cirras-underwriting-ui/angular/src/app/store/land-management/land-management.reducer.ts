import {Action} from "@ngrx/store";
import { ClearLandSearchAction, CLEAR_LAND_SEARCH, SearchLandSuccessAction, SEARCH_LAND_SUCCESS, GET_LEGAL_LAND, GetLegalLandAction, GET_LEGAL_LAND_SUCCESS, GetLegalLandSuccessAction, CLEAR_LEGAL_LAND, ClearLegalLandAction, GET_RISK_AREA_LIST_SUCCESS, GetRiskAreaListAction, GetRiskAreaListSuccessAction } from "./land-management.actions";
import {getDefaultLandSearchState, getDefaultLegalLandState, LandSearchState, ManageLegalLandState} from "./land-management.state";

export function landManagementReducer(state: LandSearchState = getDefaultLandSearchState(), action: Action): LandSearchState {

  switch (action.type) {

    case CLEAR_LAND_SEARCH: {
      const typedAction = <ClearLandSearchAction>action;
      return {...state, legalLandList: typedAction.payload.value};
    }

    case SEARCH_LAND_SUCCESS: {
      const typedAction = <SearchLandSuccessAction>action;
      return {...state, legalLandList: typedAction.payload.value};
    }

    default: {
      return state;
    }
  }
}

export function manageLegalLandReducer(state: ManageLegalLandState = getDefaultLegalLandState(), action: Action): ManageLegalLandState {

  switch (action.type) {

    case GET_LEGAL_LAND_SUCCESS: {
      const typedAction = <GetLegalLandSuccessAction>action;
      return {...state, legalLand: typedAction.payload.value }
    }

    case CLEAR_LEGAL_LAND: {
      const typedAction = <ClearLegalLandAction>action;
      return {...state, legalLand: null }
    }

    // risk area list 
    case GET_RISK_AREA_LIST_SUCCESS: {
      const typedAction = <GetRiskAreaListSuccessAction>action;
      return {...state, riskAreaList: typedAction.payload.value }
    }

    default: {
      return state;
    }
  }
}
