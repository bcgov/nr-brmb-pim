import {searchReducer, SearchState} from "@wf1/wfcc-core-lib";
import {Action} from "@ngrx/store";
import {SearchActions} from "@wf1/wfcc-core-lib/lib/search/store/actions";

import deepEqual from "deep-equal";
import { SearchUwContractsAction, SEARCH_UW_CONTRACTS } from "../uw-contracts-list/uw-contracts-list.actions";
import { SEARCH_LAND, SearchLandAction } from "../land-management/land-management.actions";

export function pageSearchReducer(state, action: Action): SearchState {
    // if (action.type == SEARCH_CLAIMS) {
    //     const typedAction = <SearchClaimsAction>action;
    //     return pagingSearchHelper(state, typedAction);
    // } else if (action.type == SEARCH_CALCULATIONS) {
    if (action.type == SEARCH_UW_CONTRACTS) {
        const typedAction = <SearchUwContractsAction>action;
        return pagingSearchHelper(state, typedAction);        
    } else if (action.type == SEARCH_LAND) {
        const typedAction = <SearchLandAction>action;
        return pagingSearchHelper(state, typedAction);        
    } else {
    let searchAction = action as SearchActions;
        return searchReducer(state, searchAction);
    }
}

export function pagingSearchHelper(state, typedAction, updateFilters: boolean = true): SearchState {
    // if (state && state.componentId) {
    //     console.log("state.componentId: " + state.componentId, typedAction.type);
    // }
    if (state
        && state.componentId == typedAction.componentId
        && typedAction.payload.pageInfoRequest
        && (state.pageSize !== typedAction.payload.pageInfoRequest.pageRowCount
            || state.pageIndex !== typedAction.payload.pageInfoRequest.pageNumber
            || state.sortParam !== typedAction.payload.pageInfoRequest.sortColumn
            || state.sortDirection !== typedAction.payload.pageInfoRequest.sortDirection
            || state.query !== typedAction.payload.pageInfoRequest.query
            || state.filters !== typedAction.payload.filters)) {
        if (updateFilters) {
            return {
                ...state,
                // pageSize: parseInt(typedAction.payload.pageInfoRequest.pageRowCount),
                // pageIndex: parseInt(typedAction.payload.pageInfoRequest.pageNumber),
                pageSize: typedAction.payload.pageInfoRequest.pageRowCount?parseInt(typedAction.payload.pageInfoRequest.pageRowCount):0,
                pageIndex: typedAction.payload.pageInfoRequest.pageNumber?parseInt(typedAction.payload.pageInfoRequest.pageNumber):1,                
                sortParam: typedAction.payload.pageInfoRequest.sortColumn,
                sortDirection: typedAction.payload.pageInfoRequest.sortDirection,
                query: typedAction.payload.pageInfoRequest.query,
                filters: typedAction.payload.filters ? typedAction.payload.filters : {}
            };
        } else {
            return {
                ...state,
                pageSize: typedAction.payload.pageInfoRequest.pageRowCount?parseInt(typedAction.payload.pageInfoRequest.pageRowCount):0,
                pageIndex: typedAction.payload.pageInfoRequest.pageNumber?parseInt(typedAction.payload.pageInfoRequest.pageNumber):1,
                sortParam: typedAction.payload.pageInfoRequest.sortColumn,
                sortDirection: typedAction.payload.pageInfoRequest.sortDirection,
                query: typedAction.payload.pageInfoRequest.query
            };
        }
    } else {
        return state;
    }
}

export function pagingFilterHelper(state, typedAction): SearchState {
    // if(state && state.componentId) { console.log("state.componentId: " + state.componentId); };

    if (state && state.componentId == typedAction.componentId
        && !deepEqual(state.filters, typedAction.payload.filters)) {
        // console.log("update page filter state");

        return {
            ...state,
            filters: {...typedAction.payload.filters}
        };
    } else {
        return state;
    }
}

